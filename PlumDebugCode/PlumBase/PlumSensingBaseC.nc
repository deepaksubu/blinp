/**
 * @author Kevin Klues <klueska@cs.stanford.edu>
 * @date July 24, 2007
 */

#include "Plum.h"
#include "message.h"
module PlumSensingBaseC {
  uses {
    interface Boot;
    interface Queue<message_t> as MsgQueue;
    interface Leds;

    interface SplitControl as SerialAMControl;
    interface AMPacket as SerialAMPacket;
    interface Packet as SerialPacket;
	
    interface SplitControl as RadioAMControl;
    interface AMPacket as RadioAMPacket;
    interface Packet as RadioPacket;
	interface CC2420Config;

    interface Receive as SerialCmdMsgReceive;
    interface AMSend as RadioCmdMsgSend;
    interface Receive as RadioSampleReceive;
    interface AMSend as SerialSampleSend;
    interface Receive as RadioStatusReceive;
    interface AMSend as SerialStatusSend;
    interface AMSend as SerialCmdMsgSend;

	interface Counter<TMilli, uint32_t> as TimeCounter;	
  }
}
implementation {
  bool serialSending;
  uint16_t dest_addr;
  message_t sample_msg;
  message_t pending_msg;
  bool msgPending = FALSE;
  bool m_scan = FALSE;
  uint32_t timePrev;

  event void Boot.booted() {
    serialSending = FALSE;
//    cmd_msg_payload = (plum_cmd_msg_t*)call SerialPacket.getPayload(&cmd_msg, sizeof(plum_cmd_msg_t));
    call RadioAMControl.start();
  }
  
  event void RadioAMControl.startDone(error_t error) {
	call CC2420Config.setAutoAck(FALSE, FALSE);
	call CC2420Config.sync();
    call SerialAMControl.start();
  }

  event void SerialAMControl.startDone(error_t error) {
  }

  event void RadioAMControl.stopDone(error_t error) {
  }

  event void SerialAMControl.stopDone(error_t error) {
  }

  event message_t* SerialCmdMsgReceive.receive(message_t* msg, void* payload, uint8_t len) {
	call Leds.led0On();

	if (((plum_cmd_msg_t*) payload)->cmdID == PLUM_SCAN) {
		// scan
		if (m_scan == FALSE) {
			m_scan = TRUE;
			call CC2420Config.setAutoAck(TRUE, TRUE);
			call CC2420Config.sync();
		}
		else {
			m_scan = FALSE;
			if (msgPending == FALSE) {
				call CC2420Config.setAutoAck(FALSE, FALSE);
				call CC2420Config.sync();
			}
		}
	}
	else {
		if(((plum_cmd_msg_t*)call RadioPacket.getPayload(&pending_msg, sizeof(plum_cmd_msg_t)))->cmdID == PLUM_TIME) {
			atomic {
				timePrev = call TimeCounter.get();
			}
		}				
		
		call CC2420Config.setAutoAck(TRUE, TRUE);
		call CC2420Config.sync();

		memcpy(call RadioPacket.getPayload(&pending_msg, sizeof(plum_cmd_msg_t)), payload, len);

//		memcpy(&pending_msg, msg, len);
		msgPending = TRUE;
		
//		call RadioCmdMsgSend.send(((plum_cmd_msg_t*) payload)->addr, msg, sizeof(plum_cmd_msg_t));
		call Leds.led2On();
	}
	return msg;
  }

  event void RadioCmdMsgSend.sendDone(message_t* msg, error_t error) {
    if(error == SUCCESS)
      call Leds.led0Off();
  }

  event void SerialCmdMsgSend.sendDone(message_t* msg, error_t error) {
  }
  
  event message_t* RadioSampleReceive.receive(message_t* msg, void* payload, uint8_t len) {
    call Leds.led1On();
    if(call MsgQueue.empty() == FALSE || serialSending == TRUE)
      call MsgQueue.enqueue(*msg);
    else {
      dest_addr = call SerialAMPacket.destination(msg); 
      serialSending = TRUE;
      call SerialSampleSend.send(dest_addr, msg, sizeof(nx_plum_sample_t));
    }
    return msg;
  }

  event void SerialSampleSend.sendDone(message_t* msg, error_t error) {
	if(error == SUCCESS)
	  call Leds.led1Off();
	if(call MsgQueue.empty() == FALSE) {
      sample_msg = call MsgQueue.dequeue();
      dest_addr = call SerialAMPacket.destination(msg);
      call SerialSampleSend.send(dest_addr, &sample_msg, sizeof(nx_plum_sample_t));
    }
    else serialSending = FALSE;
  }

  event message_t* RadioStatusReceive.receive(message_t* msg, void* payload, uint8_t len) {
    call Leds.led1On();
    if(call MsgQueue.empty() == FALSE || serialSending == TRUE)
      call MsgQueue.enqueue(*msg);
    else {
      dest_addr = call SerialAMPacket.destination(msg); 
      serialSending = TRUE;
      call SerialStatusSend.send(dest_addr, msg, sizeof(nx_plum_status_t));
    }

	if (msgPending == TRUE) {
		call Leds.led2Off();

		if(((plum_cmd_msg_t*)call RadioPacket.getPayload(&pending_msg, sizeof(plum_cmd_msg_t)))->cmdID == PLUM_TIME) {
			atomic {
				uint32_t timeNow = call TimeCounter.get();
				((plum_cmd_msg_t*)call RadioPacket.getPayload(&pending_msg, sizeof(plum_cmd_msg_t)))->unixTime = ((timeNow - timePrev) / 1024) + ((plum_cmd_msg_t*)call RadioPacket.getPayload(&pending_msg, sizeof(plum_cmd_msg_t)))->unixTime;
			}
		}
		
		call RadioCmdMsgSend.send(((plum_cmd_msg_t*)call RadioPacket.getPayload(&pending_msg, sizeof(plum_cmd_msg_t)))->addr, &pending_msg, sizeof(plum_cmd_msg_t));
		call SerialCmdMsgSend.send(((plum_cmd_msg_t*)call RadioPacket.getPayload(&pending_msg, sizeof(plum_cmd_msg_t)))->addr, &pending_msg, sizeof(plum_cmd_msg_t));
		
		msgPending = FALSE;

		if (m_scan == FALSE) {
			call CC2420Config.setAutoAck(FALSE, FALSE);
			call CC2420Config.sync();
		}
	}
	
    return msg;
  }

  event void SerialStatusSend.sendDone(message_t* msg, error_t error) {
	if(error == SUCCESS)
	  call Leds.led1Off();
	if(call MsgQueue.empty() == FALSE) {
      sample_msg = call MsgQueue.dequeue();
      dest_addr = call SerialAMPacket.destination(msg);
      call SerialStatusSend.send(dest_addr, &sample_msg, sizeof(nx_plum_status_t));
    }
    else serialSending = FALSE;
  }

  event void CC2420Config.syncDone( error_t error ) {}

  async event void TimeCounter.overflow() {}

}

