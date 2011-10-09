#include "Plum.h"
#include "printf.h"

#define SAMPLE_BUFFER_SIZE 2
#define SAMPLE_RADIO_BUFFER_SIZE 100

#define ONE_MINUTE 61440 // 60 * 1024
#define ADC_SAMPLES 3

module PlumSensingP {

	uses {
		interface Boot;

		interface SplitControl as RadioAMControl;

		interface Receive as RadioCmdMsgReceive;
		interface AMSend as RadioSampleSend;
		interface AMSend as RadioStatusSend;
		interface PacketAcknowledgements;
		
		interface LocalTime<TMilli>;
		interface Counter<TMilli, uint32_t> as TimeCounter;
	
		interface Leds;
    
		interface Timer<TMilli> as SampleTimer;
		interface Timer<TMilli> as StatusTimer;
		interface Timer<TMilli> as ListenTimer;		
   
#ifndef DISABLE_FLASH
		interface LogBlock;
#endif

#define N_SENSORS 1	
		interface Read<uint16_t> as ReadIntVol;

		interface PIRSensor as ReadPIR;
	}

} implementation {

	plum_sample_t sampleBuffer[SAMPLE_BUFFER_SIZE];
	plum_sample_t *plum_sample_msg;
	plum_sample_t radioSampleBuffer[SAMPLE_RADIO_BUFFER_SIZE];
	plum_sample_t *plum_sample;
	nx_plum_sample_t *nx_plum_sample_msg;
	nx_plum_status_t nx_plum_status;
	bool m_erased = FALSE;
	message_t sample_msg, status_msg;

	uint8_t bufferTail = 0;
	uint8_t currentBuffer = 0;

	uint8_t radioBufferTail = 0;
	uint8_t radioCurrentBuffer = 0;
	bool readMoreData;
	bool radioBusy = FALSE, dataTransfer = FALSE;
	
	blocksqnnbr_t firstRead, lastRead;
	
	uint32_t currentUnixTime, lastRcvdUnixTime = 0, timePrev;
	
	struct sockaddr_in6 route_dest;

	uint16_t sample_period = SAMPLE_PERIOD;
	uint16_t sample_time = 0;
	uint16_t status_period = STATUS_PERIOD;
	uint16_t status_time = 0;	

	bool m_sensing, sampleTimerStarted = FALSE, m_recoverConfig = FALSE;
	uint16_t seqno;
	uint16_t sampleDoneCnt = 0;
	uint8_t intvolSampleCounter = 0;  
	void initSample();
	void startSample();
	void sampleDone();
	void startSampleTimer();
	void startStatusTimer();
	task void sendSamples();
	task void sendSample();
	task void sendStatus();
	task void eraseFlash();
	task void eraseConfig();
	void recoverConfig();
	void initConfig();
	void updateTime();
	void copyToNx(nx_plum_sample_t* dest, plum_sample_t* src);

	event void Boot.booted() {
		atomic timePrev = call TimeCounter.get();
#ifndef DISABLE_FLASH
		call LogBlock.init();
#endif
		call RadioAMControl.stop();
		m_sensing = FALSE;

		seqno = 0;

		sampleTimerStarted = TRUE;
		sample_time = sample_period;
		
		nx_plum_sample_msg = (nx_plum_sample_t*)call RadioSampleSend.getPayload(&sample_msg, sizeof(nx_plum_sample_t));
		plum_sample_msg = &(sampleBuffer[currentBuffer]);
		plum_sample = &(radioSampleBuffer[radioCurrentBuffer]);

		startSample();
		startSampleTimer();
		
		status_time = status_period;
		startStatusTimer();

#ifndef DISABLE_FLASH
		recoverConfig();
#endif		
	}
  
	event void RadioAMControl.startDone(error_t e) {
		if (e == SUCCESS) {
			call Leds.led2On();
			post sendStatus();
		}
		else {
			call RadioAMControl.start();
		}
	}
  
	event void RadioAMControl.stopDone(error_t e) {
		call Leds.led2Off();
	}

	void recoverConfig() {
		error_t retval;
		m_recoverConfig = TRUE;
		retval = call LogBlock.readConfig(&nx_plum_status, sizeof(nx_plum_status_t));
		if (retval != SUCCESS) {
			printf("Recovering config... : %d\n", retval);
			printfflush();
			initConfig();
		}
	}

	void startSampleTimer() {
		if (sample_time > 60) {
			call SampleTimer.startOneShot(ONE_MINUTE);
			sample_time = sample_time - 60;
		}
		else {
			call SampleTimer.startOneShot(sample_time * 1024);
			sample_time = 0;
		}	
	}

	void startStatusTimer() {
		if (status_time > 60) {
			call StatusTimer.startOneShot(ONE_MINUTE);
			status_time = status_time - 60;
		}
		else {
			call StatusTimer.startOneShot(status_time * 1024);
			status_time = 0;
		}
	}
	
	event void SampleTimer.fired() {
		if (sample_time > 0) {
			startSampleTimer();
		}
		else {
			startSample();
			sample_time = sample_period;
			startSampleTimer();
		}
	}

	void initSample() {
		plum_sample_msg->intvol = 0;
	}
  
	void startSample() {
		initSample();
	
		if (m_sensing) return;
		m_sensing = TRUE;

		call Leds.led0On();
				
		sampleDoneCnt = 0;

		if (call ReadPIR.read() == 1) {
			printf("Taking a reading...\n");
			plum_sample_msg->seqno = seqno++;
			plum_sample_msg->sender = TOS_NODE_ID;
//			plum_sample_msg->sampleRate = call ReadPIR.read();
			plum_sample_msg->sampleRate = sample_period;
			plum_sample_msg->statusRate = status_period;
			
			if (call ReadIntVol.read() != SUCCESS)
				sampleDoneCnt++;	

			if (sampleDoneCnt == N_SENSORS) sampleDone();
		}
		else {
			// Do nothing - we don't need to store a sample
			m_sensing = FALSE;
			sampleDoneCnt = 0;
			updateTime();
			printf("Not taking a reading...\n");
		}
		printfflush();
	}

	void sampleDone() {
		plum_sample_msg->lTime = call LocalTime.get();
		updateTime();
		atomic plum_sample_msg->unixTime = currentUnixTime;
		
		m_sensing = FALSE;

		nx_plum_status.last_seqno = plum_sample_msg->seqno;
		nx_plum_status.last_unixTime = plum_sample_msg->unixTime;
		nx_plum_status.sampleRate = plum_sample_msg->sampleRate;
		nx_plum_status.statusRate = plum_sample_msg->statusRate;
		nx_plum_status.intvol = plum_sample_msg->intvol;
		
#ifndef DISABLE_FLASH
//		call LogBlock.append(plum_sample_msg, sizeof(plum_sample_t), &plum_sample_msg->blockID);
#endif

		if((currentBuffer + 1) % SAMPLE_BUFFER_SIZE == bufferTail) {
			// Should only write to flash here?
			// Signals that the buffer is full - should write buffer to flash
//			call Status.sendto(&route_dest, &(sampleBuffer[bufferTail]), sizeof(nx_struct udp_report));
#ifndef DISABLE_FLASH
			while (bufferTail != currentBuffer) {		
				call LogBlock.append(&(sampleBuffer[bufferTail]), sizeof(plum_sample_t), &((&(sampleBuffer[bufferTail]))->blockID));				
//				printf("flash W: current = %d, tail = %d, seqno = %d, blockID = %d\n", currentBuffer, bufferTail, (&(sampleBuffer[bufferTail]))->seqno, (sampleBuffer[bufferTail]).blockID);				
//				printfflush();

				// Most values in the status struct are updated with each sample
//				if (newConfig == TRUE) {
//					nx_plum_status.first_blockID = (sampleBuffer[bufferTail]).blockID;
//					nx_plum_status.last_blockID = (sampleBuffer[bufferTail]).blockID;
//
//					call LogBlock.writeConfig(&nx_plum_status, sizeof(nx_plum_status_t));
//					
//					newConfig = FALSE;
//				}
//				else if ((sampleBuffer[bufferTail]).blockID != nx_plum_status.last_blockID) {
				if ((sampleBuffer[bufferTail]).blockID != nx_plum_status.last_blockID) {
					nx_plum_status.last_blockID = (sampleBuffer[bufferTail]).blockID;
//					printf("SenseDone, full buffer: first_blockID, last_blockID = %d,%d, buffer->blockID = %d\n", nx_plum_status.first_blockID, nx_plum_status.last_blockID, (sampleBuffer[bufferTail]).blockID);
//					printfflush();					

					call LogBlock.writeConfig(&nx_plum_status, sizeof(nx_plum_status_t));
				}
					
#endif
				bufferTail = (bufferTail + 1) % SAMPLE_BUFFER_SIZE;
			}
		}

//		printf("sensed: current = %d, tail = %d, seqno = %d\n", currentBuffer, bufferTail, plum_sample_msg->seqno);
//		printfflush();
		
		currentBuffer = (currentBuffer + 1) % SAMPLE_BUFFER_SIZE;
		plum_sample_msg = &(sampleBuffer[currentBuffer]);

		call Leds.led0Off();
	}

	void updateTime() {
		atomic {
			// Overflows handled by TimeCounter event
			uint32_t timeNow = call TimeCounter.get();
			currentUnixTime = ((timeNow - timePrev) / 1024) + currentUnixTime;
			// Correction for integer divisor losing fractions of seconds
			timePrev = timeNow - ((timeNow - timePrev) % 1024);
		}
	}

	event void ListenTimer.fired() {
		if (radioBusy == FALSE && dataTransfer == FALSE) {
			call RadioAMControl.stop();
		}
		else {
			call ListenTimer.startOneShot(LISTEN_PERIOD);
		}
	}	

	event void StatusTimer.fired() {
		if (status_time > 0) {
			startStatusTimer();
		}
		else {
//			call Leds.led1On();
			call RadioAMControl.start();
			status_time = status_period;
			startStatusTimer();
		}
	}

	task void sendStatus() {
		error_t err;

//		printf("Sending Status: first_blockID, last_blockID = %d,%d\n", nx_plum_status.first_blockID, nx_plum_status.last_blockID);
//		printfflush();

		if (radioBusy == FALSE) {
			memcpy(call RadioStatusSend.getPayload(&status_msg, sizeof(nx_plum_status_t)), &nx_plum_status, sizeof(nx_plum_status_t));
			radioBusy = TRUE;
			call PacketAcknowledgements.requestAck(&status_msg);
//			call Leds.led1On();
			err = call RadioStatusSend.send(BASE_STATION_ADDR, &status_msg, sizeof(nx_plum_status_t));
		}
		else {
			post sendStatus();
		}
	}
	
	task void sendSamples() {
		printf("sendSamples(): firstRead, lastRead = %d,%d, len = %d\n", firstRead, lastRead, sizeof(plum_sample_t));
		printfflush();
		call LogBlock.read(firstRead, lastRead, sizeof(plum_sample_t));		
	}

	task void sendSample() {		
		error_t err = 255;
		if (radioBufferTail != radioCurrentBuffer && radioBusy == FALSE) {
			dataTransfer = TRUE;
//			call Leds.led2Toggle();
		    nx_plum_sample_msg = (nx_plum_sample_t*)call RadioSampleSend.getPayload(&sample_msg, sizeof(nx_plum_sample_t));
			copyToNx(nx_plum_sample_msg, &(radioSampleBuffer[radioBufferTail]));

			radioBusy = TRUE;
			err = call RadioSampleSend.send(BASE_STATION_ADDR, &sample_msg, sizeof(nx_plum_sample_t));

//			if (readMoreData == TRUE) {
				printf("%d, %d, %d, %d, %d - sendSample() : seqno, radioBufferTail, radioCurrentBuffer, err, readMore\n", radioSampleBuffer[radioBufferTail].seqno, radioBufferTail, radioCurrentBuffer, err, readMoreData);
				printfflush();
//			}
			if(err == SUCCESS) {
//				printf("err = %d, current = %d, tail = %d, seqno = %d\n", err, currentBuffer, bufferTail, ((plum_sample_t*)call RadioSampleSend.getPayload(&sample_msg, sizeof(plum_sample_t)))->seqno);
//				printfflush();
			
//				radioBufferTail = (radioBufferTail + 1) % SAMPLE_RADIO_BUFFER_SIZE;
			}
			else {				
				post sendSample();
			}
		}
		else if (radioBusy == TRUE) {
			post sendSample();
		}
		else {
			if (readMoreData == TRUE) {
				call LogBlock.readNextBlock();
			}
			else {
				dataTransfer = FALSE;
			}
		}
//		printf("%d, %d, %d, %d, %d - sendSample() : seqno, radioBufferTail, radioCurrentBuffer, err, readMore\n", radioSampleBuffer[radioBufferTail].seqno, radioBufferTail, radioCurrentBuffer, err, readMoreData);
//		printfflush();		
	}

	task void eraseFlash() {
#ifndef DISABLE_FLASH
		call LogBlock.erase();
#endif
	}

	void initConfig() {
		printf("initConfig()\n");
		printfflush();
		nx_plum_status.sender = TOS_NODE_ID;
		nx_plum_status.last_seqno = 0;
		nx_plum_status.last_unixTime = 0;
		nx_plum_status.sampleRate = sample_period;
		nx_plum_status.statusRate = status_period;
		nx_plum_status.intvol = 0;
		// JKT: ideally, this would be set elsewhere, and not explicitly
		nx_plum_status.first_blockID = 2;
		nx_plum_status.last_blockID = 2;
	}
	
	task void eraseConfig() {
		initConfig();
#ifndef DISABLE_FLASH
		call LogBlock.writeConfig(&nx_plum_status, sizeof(nx_plum_status_t));
#endif
	}
	
	event void ReadIntVol.readDone(error_t result, uint16_t data) {
		plum_sample_msg->intvol += data;
		if (++intvolSampleCounter < ADC_SAMPLES) {
			call ReadIntVol.read();
		}
		else {
			intvolSampleCounter = 0;
			plum_sample_msg->intvol /= ADC_SAMPLES;
			if (++sampleDoneCnt == N_SENSORS) sampleDone();
		}	
	}
	
	event void RadioSampleSend.sendDone(message_t* msg, error_t error) {
//		if (error == SUCCESS) {
			radioBufferTail = (radioBufferTail + 1) % SAMPLE_RADIO_BUFFER_SIZE;
//		}
//		else {
//			post sendSample();
//		}
			radioBusy = FALSE;
			post sendSample();

//		printf("samp_sD: error = %d, current = %d, tail = %d\n", error, currentBuffer, bufferTail);
//		printfflush();				
	}

	event void RadioStatusSend.sendDone(message_t* msg, error_t error) {
//		printf("stat_sD: error = %d\n", error);
//		printfflush();
//		call Leds.led1Off();
		radioBusy = FALSE;
		
		if (call PacketAcknowledgements.wasAcked(msg)) {
//			call Leds.led1On();
//			call Leds.led1Off();
			call ListenTimer.startOneShot(LISTEN_PERIOD);			
		}
		else {
//			call Leds.led1Off();
			call RadioAMControl.stop();
//			call ListenTimer.startOneShot(LISTEN_PERIOD);			
		}
	}

	event message_t* RadioCmdMsgReceive.receive(message_t* msg, void* payload, uint8_t len) {
		if (((plum_cmd_msg_t *) payload)->cmdID == PLUM_CONFIG) {
			if (status_period != ((plum_cmd_msg_t *) payload)->statusRate && ((plum_cmd_msg_t *) payload)->statusRate < MAX_STATUS_PERIOD && ((plum_cmd_msg_t *) payload)->statusRate > 0) {
				status_period = ((plum_cmd_msg_t *) payload)->statusRate;
				call StatusTimer.stop();
				status_time = status_period;
				startStatusTimer();
			}
			
			if (sample_period != ((plum_cmd_msg_t *) payload)->sampleRate && ((plum_cmd_msg_t *) payload)->sampleRate < MAX_SAMPLE_PERIOD && ((plum_cmd_msg_t *) payload)->sampleRate > 0) {
				sample_period = ((plum_cmd_msg_t *) payload)->sampleRate;
				call SampleTimer.stop();
				sample_time = sample_period;
				startSampleTimer();
			}			
		}
		else if (((plum_cmd_msg_t *) payload)->cmdID == PLUM_READ) {
//			call Leds.led2Toggle();
			firstRead = ((plum_cmd_msg_t *) payload)->blockStart;
			lastRead = ((plum_cmd_msg_t *) payload)->blockEnd;
			post sendSamples();
		}
		else if (((plum_cmd_msg_t *) payload)->cmdID == PLUM_ERASE) {
//			call Leds.led2Toggle();
			post eraseFlash();
		}
		else if (((plum_cmd_msg_t *) payload)->cmdID == PLUM_ERASE_CONFIG) {
//			call Leds.led2Toggle();
			post eraseConfig();
		}
		else if (((plum_cmd_msg_t *) payload)->cmdID == PLUM_TIME) {
			atomic {
				currentUnixTime = ((plum_cmd_msg_t *) payload)->unixTime;
				timePrev = call TimeCounter.get();
			}
			call Leds.led1Toggle();
//			printf("Received time command : %ld, %ld, %ld, %d, %d\n", ((plum_cmd_msg_t *) payload)->unixTime, currentUnixTime, timePrev, ((plum_cmd_msg_t *) payload)->blockStart, ((plum_cmd_msg_t *) payload)->cmdID);
//			printfflush();
		}

		return msg;
	}
	
	//
	// Commands
	//
/* 	event void BlinkCommand.dispatch(nx_struct cmd_payload *data, int len) { */
/* 		nx_struct cmd_payload *msg = (nx_struct cmd_payload *)data;		 */

/* 		if (msg->forward == 2) { */
/* 			// Forward value of 2 indicates response is requested */
/* 			call BlinkCommand.write(data, len); */
/* 		} */

/* 		call Leds.flash(7); */
/* 		call Leds.glow(7,0); */
/* 	} */

/* 	event void SenseCommand.dispatch(nx_struct cmd_payload *data, int len) { */
/* 		startSense(); */
/* 	} */
  
/* 	event void ConfigCommand.dispatch(nx_struct cmd_payload *data, int len) { */
/* 		nx_struct cmd_payload *msg = (nx_struct cmd_payload *)data;		 */
/* 		nx_struct config_cmd *cmd = (nx_struct config_cmd *) data->data; */

/* 		if (len != sizeof(nx_struct cmd_payload) + sizeof(nx_struct config_cmd)) return; */

/* 		if (cmd->senseRate > MAX_SENSE_PERIOD) return; */

/* 		if (cmd->senseRate != sense_period && cmd->senseRate != 0) { */
/* 			sense_period = cmd->senseRate; */
/* 			call SampleTimer.stop(); */
/* 			sense_time = sense_period; */
/* 			startSampleTimer(); */
/* 		} */
/* 		else { */
/* 			cmd->senseRate = sense_period; */
/* 		} */

/* 		if (cmd->rptRate > MAX_REPORT_PERIOD) return; */

/* 		if (cmd->rptRate != report_period && cmd->rptRate != 0) { */
/* 			report_period = cmd->rptRate; */
/* 			call ReportTimer.stop(); */
/* 			report_time = report_period; */
/* 			startReportTimer(); */
/* 		} */
/* 		else { */
/* 			cmd->rptRate = report_period; */
/* 		} */

/* 		if (msg->forward == 2) { */
/* 			// Forward value of 2 indicates response is requested */
/* 			call ConfigCommand.write(msg,  sizeof(nx_struct cmd_payload) + */
/*                          sizeof(nx_struct config_cmd));			 */
/* 		} */
/* 	} */

/* 	event void TimeCommand.dispatch(nx_struct cmd_payload *data, int len) { */
/* 		nx_struct cmd_payload *msg = (nx_struct cmd_payload *)data;		 */
/* 		nx_struct time_cmd *cmd = (nx_struct time_cmd *) data->data; */

/* 		if (len != sizeof(nx_struct cmd_payload) + sizeof(nx_struct time_cmd)) return; */
		
/* 		if (cmd->unixTime != 0) { */
/* 			// Reset state of the watchdog timer  */
/* 			call WatchdogTimer.stop(); */
/* 			watchdogMinutes = 0; */
/* 			call WatchdogTimer.startPeriodic(ONE_MINUTE); */

/* 			if (cmd->unixTime != lastRcvdUnixTime) { */
/* 				atomic { */
/* 					currentUnixTime = cmd->unixTime; */
/* 					timePrev = call TimeCounter.get(); */
/* 				} */
/* 			} */
/* 			lastRcvdUnixTime = cmd->unixTime; */
/* 		} */
/* 		else { */
/* 			updateTime(); */
/* 		} */
		
/* 		if (msg->forward == 2) { */
/* 			// Forward value of 2 indicates response is requested */
/* 			atomic cmd->unixTime = currentUnixTime; */
/* 			call TimeCommand.write(msg,  sizeof(nx_struct cmd_payload) + */
/*                          sizeof(nx_struct time_cmd));			 */
/* 		} */
/* 	} */
		
/* #ifndef DISABLE_FLASH	 */
/* 	event void LogEraseCommand.dispatch(nx_struct cmd_payload *data, int len) { */
/* 		nx_struct cmd_payload *msg = (nx_struct cmd_payload *)data;		 */

/* 		if (msg->forward == 2) { */
/* 			// Forward value of 2 indicates response is requested */
/* 			call LogEraseCommand.write(data, len); */
/* 		} */

/* 		call LogBlock.erase(); */
/* 		call Leds.glow(7,1); */
/* 		call Leds.glow(1,0);	 */
/* 	} */
/* #endif */

#ifndef DISABLE_FLASH

	event void LogBlock.eraseDone(error_t error) {
		m_erased = TRUE;
		if (call LogBlock.readConfig(&nx_plum_status, sizeof(nx_plum_status_t)) != SUCCESS) {
			initConfig();
		}	  
	}

	event void LogBlock.readDone(void *data, uint16_t numSamples, blocksqnnbr_t blockSqnNbr, bool moreData, error_t error) {
		uint16_t sampleCount = 0;
		readMoreData = moreData;
		if (error == SUCCESS) {
//			printf("%d, %d, %d, %d, %d, %d, %d - LogBlock.readDone() : radioCurrentBuffer, radioBufferTail, blockID, readMore, seqno, seqno, numSamples\n", radioCurrentBuffer, radioBufferTail, blockSqnNbr, moreData, ((plum_sample_t*) data)->seqno, radioSampleBuffer[radioCurrentBuffer].seqno, numSamples);
//			printfflush();
			for (sampleCount = 0 ; sampleCount < numSamples ; sampleCount++) {
				if ((radioCurrentBuffer + 1) % SAMPLE_RADIO_BUFFER_SIZE == radioBufferTail) {
					// Buffer is full - drop samples
					printf("LogBlock.readDone() : Packet Drop! radioCurrentBuffer = %d, radioBufferTail = %d, blockID = %d, readMore = %d\n", radioCurrentBuffer, radioBufferTail, blockSqnNbr, moreData);
					printfflush();
				}
				else {
					memcpy((void *) plum_sample, data + (sampleCount * sizeof(plum_sample_t)), sizeof(plum_sample_t));
					radioCurrentBuffer = (radioCurrentBuffer + 1) % SAMPLE_RADIO_BUFFER_SIZE;
					plum_sample = &(radioSampleBuffer[radioCurrentBuffer]);
				}
			}
			
			post sendSample();
		}
	}

	
//	event void LogBlock.getDone(Block *blockPtr, blocksqnnbr_t blockSqnNbr, error_t error) {}
	event void LogBlock.syncDone(error_t error) {}
#endif
	
	async event void TimeCounter.overflow() {
		atomic {
			currentUnixTime = ((0xffffffff - timePrev) / 1024) + currentUnixTime;
			timePrev = 0;
		}
	}

	void copyToNx(nx_plum_sample_t* dest, plum_sample_t* src) {
		dest->seqno = src->seqno;
		dest->lTime = src->lTime;
		dest->unixTime = src->unixTime;
		dest->sender = src->sender;
		dest->sampleRate = src->sampleRate;
		dest->statusRate = src->statusRate;
		dest->intvol = src->intvol;
		dest->blockID = src->blockID;
	}	

	event void LogBlock.readConfigDone(void *config, uint8_t len, error_t error) {
		if (((nx_plum_status_t *) config)->last_blockID < VOLUME_SIZE/BLOCK_SIZE && ((nx_plum_status_t *) config)->last_blockID >= 0) {
			memcpy(&nx_plum_status, (nx_plum_status_t *) config, len);
			sample_period = nx_plum_status.sampleRate;
			status_period = nx_plum_status.statusRate;
		}
		else {
			initConfig();
		}		

		if (m_recoverConfig == TRUE) {
			m_recoverConfig = FALSE;
		}

		if (m_erased == TRUE) {
			m_erased = FALSE;
		}
	}

	event void LogBlock.writeConfigDone(error_t error) {
	}

  }
