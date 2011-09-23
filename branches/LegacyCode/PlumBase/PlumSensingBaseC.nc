/**
 * @author Jay Tanea <taneja@cs.berkeley.edu>
 * @date August 30, 2011
 */

#include "Plum.h"
#include "message.h"
#include "printf.h"

#define MSG_BUFFER_SIZE 50
#define COMMAND_TIMEOUT 20
#define COMMAND_TIMER 5
#define ONE_MINUTE 61440 // 60 * 1024

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
	interface Timer<TMilli> as CommandTimer;
  }
}
implementation {
  bool serialSending;
  uint16_t dest_addr;
  message_t sample_msg;
  plum_cmd_msg_t msgBuffer[MSG_BUFFER_SIZE];
  uint32_t msgTimes[MSG_BUFFER_SIZE];
  message_t pending_msg;
  bool msgPending = FALSE;
  
  uint8_t bufferTail = 0;
  uint8_t currentBuffer = 0;
  
  bool m_scan = FALSE;

  task void sendPendingMsg();
  task void removeStaleCmd();

  event void Boot.booted() {
    serialSending = FALSE;
//    cmd_msg_payload = (plum_cmd_msg_t*)call SerialPacket.getPayload(&cmd_msg, sizeof(plum_cmd_msg_t));
    call RadioAMControl.start();
	call CommandTimer.startPeriodic(COMMAND_TIMER * 1024L);
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

  event void CommandTimer.fired() {
	  uint8_t current_index = bufferTail;
	  uint32_t current_time = call TimeCounter.get();
	  
	  if (current_time - msgTimes[current_index] > COMMAND_TIMEOUT * 1000L && current_index != currentBuffer) {
		  post removeStaleCmd();
	  }
  }

  task void removeStaleCmd() {
	  uint8_t current_index = bufferTail;
	  uint32_t current_time = call TimeCounter.get();
	  plum_cmd_msg_t emptyMsg;

	  if (current_time - msgTimes[current_index] > COMMAND_TIMEOUT * 1000L && current_index != currentBuffer) {	  
		  atomic {
			  if (current_time - msgTimes[current_index] > COMMAND_TIMEOUT * 1000L && current_index != currentBuffer) {
//				  printf("Removing stale message in queue for addr: %d, position = %d (%d-%d), cmd = %d, times = %ld,%ld\n", msgBuffer[current_index].addr, current_index, bufferTail, currentBuffer, msgBuffer[current_index].cmdID, msgTimes[current_index], current_time);
//				  printfflush();

				  msgTimes[current_index] = 0;
				  memcpy(&msgBuffer[current_index], &emptyMsg, sizeof(plum_cmd_msg_t));
				  bufferTail = (bufferTail + 1) % MSG_BUFFER_SIZE;
			  }
		  }
		  post removeStaleCmd();
	  }
  }
	  
	  
//	  while (current_index != currentBuffer) {
//		  if (current_time - msgTimes[current_index] > 12 * 1000L) {
//			  printf("%d, %d, %d, %ld, %ld, %d, %d - Stale message in queue for addr, position, cmd, times, tail, current\n", msgBuffer[current_index].addr, current_index, msgBuffer[current_index].cmdID, current_time, msgTimes[current_index], bufferTail, currentBuffer);
//			  printfflush();
//		  }			  
		  
//		  if (current_time - msgTimes[current_index] > COMMAND_TIMEOUT * 1000L) {			  
////			  printf("Removing stale message in queue for addr: %d, position = %d (%d-%d), cmd = %d\n", msgBuffer[current_index].addr, current_index, bufferTail, currentBuffer, msgBuffer[current_index].cmdID);
////			  printfflush();

  //		  atomic {			  
  //			  while (current_index != currentBuffer) {
  //				  memcpy(&msgBuffer[current_index], &msgBuffer[(current_index + 1) % MSG_BUFFER_SIZE], sizeof(plum_cmd_msg_t));
//					  if ((current_index + 1) % MSG_BUFFER_SIZE != currentBuffer) {
//						  msgTimes[current_index] = msgTimes[(current_index + 1) % MSG_BUFFER_SIZE];
//					  }
//					  else {
//						  msgTimes[current_index] = 0;					  
//					  }
  //				  msgTimes[current_index] = current_time;
					  
  //				  printf("Switch %d %d\n", current_index, currentBuffer);
//				  printf("Switch %d,%d %d,%d, %d,%d %ld,%ld\n", current_index, current_index + 1, msgBuffer[current_index].addr, msgBuffer[current_index + 1].addr, msgBuffer[current_index].cmdID, msgBuffer[current_index + 1].cmdID, msgTimes[current_index], msgTimes[current_index + 1]);
  //				  printfflush();
  //				  if ((current_index + 1) % MSG_BUFFER_SIZE >= currentBuffer && current_index < currentBuffer) {
  //					  current_index = currentBuffer;
	//					  break;
  //				  }
  //				  current_index = (current_index + 1) % MSG_BUFFER_SIZE;
  //			  }
  //			  currentBuffer = (currentBuffer - 1) % MSG_BUFFER_SIZE;
  //		  }
  //	  }
  //	  else {
  //		  current_index = (current_index + 1) % MSG_BUFFER_SIZE;
  //	  }
  //  }
  //}
  
  event message_t* SerialCmdMsgReceive.receive(message_t* msg, void* payload, uint8_t len) {
	uint32_t timePrev;
	uint8_t current_index;
	bool msgWritten = FALSE;

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
			if (bufferTail == currentBuffer) {
				call CC2420Config.setAutoAck(FALSE, FALSE);
				call CC2420Config.sync();
			}
		}
	}
	else {
		atomic {		
			current_index = bufferTail;
			while (current_index != currentBuffer) {			
				if(msgBuffer[current_index].addr == ((plum_cmd_msg_t *) payload)->addr &&
				   msgBuffer[current_index].cmdID == ((plum_cmd_msg_t *) payload)->cmdID) {
					if (((plum_cmd_msg_t *) payload)->cmdID == PLUM_READ &&
						((plum_cmd_msg_t *) payload)->blockStart == msgBuffer[current_index].blockStart &&
						((plum_cmd_msg_t *) payload)->blockEnd == msgBuffer[current_index].blockEnd) {

//						printf("Received command message: addr = %d, cmd = %d, tail = %d, current = %d, already exists = %d (%d %d %d %d)\n", ((plum_cmd_msg_t *) payload)->addr, ((plum_cmd_msg_t *) payload)->cmdID, bufferTail, currentBuffer, current_index, ((plum_cmd_msg_t *) payload)->blockStart, msgBuffer[current_index].blockStart, ((plum_cmd_msg_t *) payload)->blockEnd, msgBuffer[current_index].blockEnd);
//						printfflush();
						
						msgWritten = TRUE;
						break;
					}
					else if (((plum_cmd_msg_t *) payload)->cmdID == PLUM_READ &&
							 !(((plum_cmd_msg_t *) payload)->blockStart == msgBuffer[current_index].blockStart &&
							   ((plum_cmd_msg_t *) payload)->blockEnd == msgBuffer[current_index].blockEnd)) {
//						printf("Received command message: addr = %d, cmd = %d, tail = %d, current = %d, not replaced = %d\n", ((plum_cmd_msg_t *) payload)->addr, ((plum_cmd_msg_t *) payload)->cmdID, bufferTail, currentBuffer, current_index);
//						printfflush();
//						
//						msgWritten = TRUE;
//						break;
					}
					else if (((plum_cmd_msg_t *) payload)->cmdID == PLUM_TIME) {
						memcpy(&msgBuffer[current_index], payload, len);
						msgTimes[current_index] = call TimeCounter.get();

						atomic {
							timePrev = call TimeCounter.get();
							msgBuffer[current_index].sampleRate = (uint16_t) (timePrev >> 16);
							msgBuffer[current_index].statusRate = (uint16_t) (timePrev & 0xFFFF);
						}

//						printf("Received command message (overwrite): addr = %d, cmd = %d, tail = %d, current = %d, replaced = %d\n", ((plum_cmd_msg_t *) payload)->addr, ((plum_cmd_msg_t *) payload)->cmdID, bufferTail, currentBuffer, current_index);
//						printfflush();

						msgWritten = TRUE;
						break;
					}
					else {
						memcpy(&msgBuffer[current_index], payload, len);
						msgTimes[current_index] = call TimeCounter.get();

//						printf("Received command message (overwrite): addr = %d, cmd = %d, tail = %d, current = %d, replaced = %d\n", ((plum_cmd_msg_t *) payload)->addr, ((plum_cmd_msg_t *) payload)->cmdID, bufferTail, currentBuffer, current_index);
//						printfflush();
						
						msgWritten = TRUE;
						break;
					}
				}
				current_index = (current_index + 1) % MSG_BUFFER_SIZE;
			}

			if (msgWritten == FALSE) {
				if ((currentBuffer + 1) % MSG_BUFFER_SIZE != bufferTail) {		
					memcpy(&msgBuffer[currentBuffer], payload, len);
					msgTimes[current_index] = call TimeCounter.get();

					if (msgBuffer[currentBuffer].cmdID == PLUM_TIME) {
						atomic {
							timePrev = call TimeCounter.get();
							msgBuffer[currentBuffer].sampleRate = (uint16_t) (timePrev >> 16);
							msgBuffer[currentBuffer].statusRate = (uint16_t) (timePrev & 0xFFFF);
						}
					}					

//					printf("Received new command message: addr = %d, cmd = %d, tail = %d, current = %d\n", ((plum_cmd_msg_t *) payload)->addr, ((plum_cmd_msg_t *) payload)->cmdID, bufferTail, currentBuffer);
//					printfflush();

					currentBuffer = (currentBuffer + 1) % MSG_BUFFER_SIZE;
					msgWritten = TRUE;
				}
				else {
					// Drop message - no room in buffer
				}
			}

			if (msgWritten == TRUE) {
				call CC2420Config.setAutoAck(TRUE, TRUE);
				call CC2420Config.sync();
			}
				
		}
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
	uint8_t current_index;
	call Leds.led1On();

    if(call MsgQueue.empty() == FALSE || serialSending == TRUE)
      call MsgQueue.enqueue(*msg);
    else {
      dest_addr = call SerialAMPacket.destination(msg); 
      serialSending = TRUE;
      call SerialStatusSend.send(dest_addr, msg, sizeof(nx_plum_status_t));
    }

//	printf("Received status message: addr = %d,%d, tail = %d, current = %d, msgPending = %d, tail : %d, %d\n", ((nx_plum_status_t *) payload)->sender, msgBuffer[bufferTail].addr, bufferTail, currentBuffer, msgPending, msgBuffer[bufferTail].addr, msgBuffer[bufferTail].cmdID);
//	printfflush();
	
	// check if message for this node exists
	// if so, mark it to be sent
	atomic {
		if (bufferTail != currentBuffer && msgPending == FALSE) {			
			current_index = bufferTail;
			while (current_index != currentBuffer) {			
//				if(msgBuffer[current_index].addr == call RadioAMPacket.source(msg)) {
				if(msgBuffer[current_index].addr == ((nx_plum_status_t *) payload)->sender) {
					memcpy(call RadioPacket.getPayload(&pending_msg, sizeof(plum_cmd_msg_t)), &msgBuffer[current_index], sizeof(plum_cmd_msg_t));

					call Leds.led2Toggle();

					msgPending = TRUE;

//					printf("Found message in queue for addr: %d, position = %d, cmd = %d\n", call RadioAMPacket.source(msg), current_index, msgBuffer[current_index].cmdID);
//					printfflush();
					
					while (current_index != currentBuffer) {
						memcpy(&msgBuffer[current_index], &msgBuffer[(current_index + 1) % MSG_BUFFER_SIZE], sizeof(plum_cmd_msg_t));
						msgTimes[current_index] = msgTimes[(current_index + 1) % MSG_BUFFER_SIZE];
						current_index = (current_index + 1) % MSG_BUFFER_SIZE;
					}
					currentBuffer = (currentBuffer - 1) % MSG_BUFFER_SIZE;
					
					break;
				}
				else {
					current_index = (current_index + 1) % MSG_BUFFER_SIZE;
				}
			}
		}
	}

	if (msgPending == TRUE) {
		post sendPendingMsg();
	}
	
    return msg;
  }

  task void sendPendingMsg() {
	  uint32_t timeNow, timePrev;

//	printf("sendPendingMsg(): addr = %d, tail = %d, current = %d, cmd = %d\n", ((plum_cmd_msg_t*)call RadioPacket.getPayload(&pending_msg, sizeof(plum_cmd_msg_t)))->addr, bufferTail, currentBuffer, ((plum_cmd_msg_t*)call RadioPacket.getPayload(&pending_msg, sizeof(plum_cmd_msg_t)))->cmdID);
//	printfflush();		  
	  
	  if (msgPending == TRUE) {
		  if(((plum_cmd_msg_t*)call RadioPacket.getPayload(&pending_msg, sizeof(plum_cmd_msg_t)))->cmdID == PLUM_TIME) {
			  atomic {
				  timeNow = call TimeCounter.get();

				  // hacky, but recovers the counter value when the packet was received
				  timePrev = (((plum_cmd_msg_t*)call RadioPacket.getPayload(&pending_msg, sizeof(plum_cmd_msg_t)))->sampleRate);
				  timePrev = (timePrev << 16) + ((plum_cmd_msg_t*)call RadioPacket.getPayload(&pending_msg, sizeof(plum_cmd_msg_t)))->statusRate;
			
				  ((plum_cmd_msg_t*)call RadioPacket.getPayload(&pending_msg, sizeof(plum_cmd_msg_t)))->unixTime = ((timeNow - timePrev) / 1024) + ((plum_cmd_msg_t*)call RadioPacket.getPayload(&pending_msg, sizeof(plum_cmd_msg_t)))->unixTime;
			  }
		  }
		
		  call RadioCmdMsgSend.send(((plum_cmd_msg_t*)call RadioPacket.getPayload(&pending_msg, sizeof(plum_cmd_msg_t)))->addr, &pending_msg, sizeof(plum_cmd_msg_t));
		  call SerialCmdMsgSend.send(((plum_cmd_msg_t*)call RadioPacket.getPayload(&pending_msg, sizeof(plum_cmd_msg_t)))->addr, &pending_msg, sizeof(plum_cmd_msg_t));
		
		  msgPending = FALSE;

		  if (m_scan == FALSE && currentBuffer == bufferTail) {
			  call CC2420Config.setAutoAck(FALSE, FALSE);
			  call CC2420Config.sync();
		  }
	  }
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

