// $Id: ExtFlashM.nc,v 1.1 2008/08/25 16:48:47 razvanm Exp $

/*
 * "Copyright (c) 2000-2005 The Regents of the University  of California.
 * All rights reserved.
 *
 * Permission to use, copy, modify, and distribute this software and its
 * documentation for any purpose, without fee, and without written agreement is
 * hereby granted, provided that the above copyright notice, the following
 * two paragraphs and the author appear in all copies of this software.
 *
 * IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR
 * DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES ARISING OUT
 * OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF THE UNIVERSITY OF
 * CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY
 * AND FITNESS FOR A PARTICULAR PURPOSE.  THE SOFTWARE PROVIDED HEREUNDER IS
 * ON AN "AS IS" BASIS, AND THE UNIVERSITY OF CALIFORNIA HAS NO OBLIGATION TO
 * PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR MODIFICATIONS."
 */

/**
 * @author Jonathan Hui <jwhui@cs.berkeley.edu>
 * @author Razvan Musaloiu-E. <razvanm@cs.jhu.edu>
 */

module ExtFlashM {
  provides {
    interface StdControl;
    interface Init;
    interface ExtFlash;
  }
  uses {
    interface HPLUSARTControl as USARTControl;
  }
}

implementation {
  uint32_t addr;

  inline void TOSH_wait() {
    nop(); nop();
  }

  // send a bit via bit-bManging to the flash
  void TOSH_FLASH_AT45_DP_bit(bool set) {
    if (set)
      TOSH_SET_SIMO0_PIN();
    else
      TOSH_CLR_SIMO0_PIN();
    TOSH_SET_UCLK0_PIN();
    TOSH_CLR_UCLK0_PIN();
  }

  command error_t Init.init() {
    // the flash might be in deep sleep: wake it up before proceeding
    P3SEL = 0; // select internal func

    TOSH_MAKE_SIMO0_OUTPUT();
    TOSH_MAKE_UCLK0_OUTPUT();
    TOSH_MAKE_FLASH_CS_OUTPUT();
    TOSH_SET_FLASH_CS_PIN();

    TOSH_wait();

    // initiate sequence;
    TOSH_CLR_FLASH_CS_PIN();
    TOSH_CLR_UCLK0_PIN();

    // opcode: 0xab: 0b10101011
    TOSH_FLASH_AT45_DP_bit(TRUE);   // 0
    TOSH_FLASH_AT45_DP_bit(FALSE);  // 1
    TOSH_FLASH_AT45_DP_bit(TRUE);   // 2
    TOSH_FLASH_AT45_DP_bit(FALSE);  // 3
    TOSH_FLASH_AT45_DP_bit(TRUE);   // 4
    TOSH_FLASH_AT45_DP_bit(FALSE);  // 5
    TOSH_FLASH_AT45_DP_bit(TRUE);   // 6
    TOSH_FLASH_AT45_DP_bit(TRUE);   // 7

    TOSH_SET_FLASH_CS_PIN();
    TOSH_SET_UCLK0_PIN();
    TOSH_SET_SIMO0_PIN();

    P3SEL = 0x0e; // go back to I2C and SPI mod func
    TOSH_wait();
    call USARTControl.setModeSPI();
    return SUCCESS;
  }

  command error_t StdControl.start() {
    return SUCCESS;
  }

  command error_t StdControl.stop() {
    call USARTControl.disableSPI();
    return SUCCESS;
  }

  command void ExtFlash.startRead(uint32_t newAddr) {

    uint8_t cmd[4];
    uint8_t i;
    uint32_t page = newAddr / 512;
    uint32_t offset = newAddr % 512;

    addr = newAddr;

    cmd[0] = 0x03;
    cmd[1] = page >> 6;
    cmd[2] = (page << 2) | (offset >> 8);
    cmd[3] = offset;

    TOSH_CLR_FLASH_CS_PIN();

    for ( i = 0; i < sizeof(cmd); i++ ) {
      call USARTControl.tx(cmd[i]);
      while(call USARTControl.isTxEmpty() != SUCCESS);
    }
  }

  command uint8_t ExtFlash.readByte() {
    if (!(addr & 0x1ff)) {
      call ExtFlash.stopRead();
      call ExtFlash.startRead(addr);
    }
    addr++;
    call USARTControl.rx();
    call USARTControl.tx(0);
    while(call USARTControl.isRxIntrPending() != SUCCESS);
    return call USARTControl.rx();
  }

  command void ExtFlash.stopRead() {
    TOSH_SET_FLASH_CS_PIN();
  }

}
