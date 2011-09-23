/*
 * Copyright (c) 2009 Communication Group and Eislab at
 * Lulea University of Technology
 *
 * Contact: Laurynas Riliskis, LTU
 * Mail: laurynas.riliskis@ltu.se
 * All rights reserved.
 *
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the
 *   distribution.
 * - Neither the name of Communication Group at Lulea University of Technology
 *   nor the names of its contributors may be used to endorse or promote
 *    products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL STANFORD
 * UNIVERSITY OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/*
 * Copyright (c) 2006 Arch Rock Corporation
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the
 *   distribution.
 * - Neither the name of the Arch Rock Corporation nor the names of
 *   its contributors may be used to endorse or promote products derived
 *   from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE
 * ARCH ROCK OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE
 *
 */

/**
 * Generic HAL uart for M16c/62p.
 *
 * @author Henrik Makitaavola <henrik.makitaavola@gmail.com>
 * @author Alec Woo <awoo@archrock.com>
 * @author Jonathan Hui <jhui@archrock.com>
 * @author Philip Levis <pal@cs.stanford.edu>
 */

#include <Timer.h>

generic module M16c62pUartP()
{  
  provides interface Init;
  provides interface StdControl;
  provides interface UartByte;
  provides interface UartStream;
  
  uses interface StdControl as HplUartTxControl;
  uses interface StdControl as HplUartRxControl;
  uses interface HplM16c62pUart as HplUart;
  uses interface Counter<TMicro, uint16_t>;
  
}
implementation
{
  
  norace uint16_t m_tx_len, m_rx_len;
  norace uint8_t *m_tx_buf, *m_rx_buf;
  norace uint16_t m_tx_pos, m_rx_pos;
  norace uint16_t m_byte_time;
  norace uint8_t m_rx_intr;
  norace uint8_t m_tx_intr;
  
  command error_t Init.init()
  {
    // TODO(henrik) fix this for the different uarts.
    /*if (PLATFORM_BAUDRATE == 19200UL)
      m_byte_time = 200; // 1 TMicor ~= 2.12 us, one byte = 417us ~= 200
    else if (PLATFORM_BAUDRATE == 57600UL)
      m_byte_time = 68;  // 1 TMicor ~= 2.12 us, one byte = 138us ~= 65*/
    m_byte_time = 68;
    return SUCCESS;
  }
  
  command error_t StdControl.start()
  {
    /* make sure interrupts are off and set flags */
    call HplUart.disableTxInterrupt();
    call HplUart.disableRxInterrupt();
    m_rx_intr = 0;
    m_tx_intr = 0;

    /* enable tx/rx */
    call HplUartTxControl.start();
    call HplUartRxControl.start();

    // Bug fix: pal 11/26/07: RX interrupts should be enabled on start
    call HplUart.enableRxInterrupt();
    return SUCCESS;
  }

  command error_t StdControl.stop()
  {
    call HplUartTxControl.stop();
    call HplUartRxControl.stop();
    return SUCCESS;
  }

  async command error_t UartStream.enableReceiveInterrupt()
  {
    atomic
    {
      m_rx_intr = 3;
      call HplUart.enableRxInterrupt();
    }
    return SUCCESS;
  }

  async command error_t UartStream.disableReceiveInterrupt()
  {
    atomic
    {
      call HplUart.disableRxInterrupt();
      m_rx_intr = 0;
    }
    return SUCCESS;
  }

  async command error_t UartStream.receive( uint8_t* buf, uint16_t len )
  {
    
    if ( len == 0 )
      return FAIL;
    atomic
    {
      if ( m_rx_buf )
	return EBUSY;
      m_rx_buf = buf;
      m_rx_len = len;
      m_rx_pos = 0;
      m_rx_intr |= 1;
      call HplUart.enableRxInterrupt();
    }
    
    return SUCCESS;
    
  }

  async event void HplUart.rxDone( uint8_t data ) 
  {

    if ( m_rx_buf ) 
    {
      m_rx_buf[ m_rx_pos++ ] = data;
      if ( m_rx_pos >= m_rx_len ) 
      {
	    uint8_t* buf = m_rx_buf;
	    atomic
        {
	      m_rx_buf = NULL;
	      if(m_rx_intr != 3)
          {
	        call HplUart.disableRxInterrupt();
	        m_rx_intr = 0;
	      }
	    }  
	    signal UartStream.receiveDone( buf, m_rx_len, SUCCESS );
      }
    }
    else 
    {
      signal UartStream.receivedByte( data );
    }    
  }

  async command error_t UartStream.send( uint8_t *buf, uint16_t len)
  {
    
    if ( len == 0 )
      return FAIL;
    else if ( m_tx_buf )
      return EBUSY;
    
    m_tx_len = len;
    m_tx_buf = buf;
    m_tx_pos = 0;
    m_tx_intr = 1;
    call HplUart.enableTxInterrupt();
    call HplUart.tx( buf[ m_tx_pos++ ] );
    
    return SUCCESS;
    
  }

  async event void HplUart.txDone() 
  {
    
    if ( m_tx_pos < m_tx_len ) 
    {
      call HplUart.tx( m_tx_buf[ m_tx_pos++ ] );
    }
    else 
    {
      uint8_t* buf = m_tx_buf;
      m_tx_buf = NULL;
      m_tx_intr = 0;
      call HplUart.disableTxInterrupt();
      signal UartStream.sendDone( buf, m_tx_len, SUCCESS );
    }
    
  }

  async command error_t UartByte.send( uint8_t byte )
  {
    if(m_tx_intr)
      return FAIL;

    call HplUart.tx( byte );
    while ( !call HplUart.isTxEmpty() );
    return SUCCESS;
  }
  
  async command error_t UartByte.receive( uint8_t * byte, uint8_t timeout)
  {

    uint16_t timeout_micro = m_byte_time * timeout + 1;
    uint16_t start;
    
    if(m_rx_intr)
      return FAIL;

    start = call Counter.get();
    while ( call HplUart.isRxEmpty() ) 
    {
      if ( ( (uint16_t)call Counter.get() - start ) >= timeout_micro )
	    return FAIL;
    }
    *byte = call HplUart.rx();
    
    return SUCCESS;
    
  }
  
  async event void Counter.overflow() {}

  default async event void UartStream.sendDone( uint8_t* buf, uint16_t len, error_t error ){}
  default async event void UartStream.receivedByte( uint8_t byte ){}
  default  async event void UartStream.receiveDone( uint8_t* buf, uint16_t len, error_t error ){}

}
