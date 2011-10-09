#include "Plum.h"
// #include "printf.h"

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
		interface Receive as SerialCmdMsgReceive;
		interface AMSend as SerialSampleSend;
		
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
	bool radioBusy = FALSE, serialBusy = FALSE, dataTransfer = FALSE, serialSend = FALSE, writeConfig = FALSE;
	
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
	}

	event void LogBlock.initDone(error_t e) {
#ifndef DISABLE_FLASH
		recoverConfig();
#endif		
	}
	
	event void RadioAMControl.startDone(error_t e) {
		if (e == SUCCESS) {
			post sendStatus();
		}
		else {
			call RadioAMControl.start();
		}
	}
  
	event void RadioAMControl.stopDone(error_t e) {
	}

	void recoverConfig() {
#ifdef KEEP_CONFIG
		error_t retval;
		m_recoverConfig = TRUE;
		retval = call LogBlock.readConfig(&nx_plum_status, sizeof(nx_plum_status_t));
		if (retval != SUCCESS) {
			initConfig();
		}
#endif
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

				
		sampleDoneCnt = 0;

		if (call ReadPIR.read() == 1) {
			plum_sample_msg->seqno = seqno++;
			plum_sample_msg->sender = TOS_NODE_ID;
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
		}
	}

	void sampleDone() {
		error_t err;
		plum_sample_msg->lTime = call LocalTime.get();
		updateTime();
		atomic plum_sample_msg->unixTime = currentUnixTime;
		
		m_sensing = FALSE;

		nx_plum_status.last_seqno = plum_sample_msg->seqno;
		nx_plum_status.last_unixTime = plum_sample_msg->unixTime;
		nx_plum_status.sampleRate = plum_sample_msg->sampleRate;
		nx_plum_status.statusRate = plum_sample_msg->statusRate;
		nx_plum_status.intvol = plum_sample_msg->intvol;
		
		if((currentBuffer + 1) % SAMPLE_BUFFER_SIZE == bufferTail) {
			// Should only write to flash here?
			// Signals that the buffer is full - should write buffer to flash
#ifndef DISABLE_FLASH
			if (writeConfig == TRUE) {
				writeConfig = FALSE;
				
				err = call LogBlock.writeConfig(&nx_plum_status, sizeof(nx_plum_status_t));				
			}
			
			while (bufferTail != currentBuffer) {		
				call LogBlock.append(&(sampleBuffer[bufferTail]), sizeof(plum_sample_t), &((&(sampleBuffer[bufferTail]))->blockID));				

				// Most values in the status struct are updated with each sample
				if ((sampleBuffer[bufferTail]).blockID != nx_plum_status.last_blockID) {
					writeConfig = TRUE;
					nx_plum_status.last_blockID = (sampleBuffer[bufferTail]).blockID;
				}
					
#endif
				bufferTail = (bufferTail + 1) % SAMPLE_BUFFER_SIZE;
			}
		}
		
		currentBuffer = (currentBuffer + 1) % SAMPLE_BUFFER_SIZE;
		plum_sample_msg = &(sampleBuffer[currentBuffer]);
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
			call RadioAMControl.start();
			status_time = status_period;
			startStatusTimer();
		}
	}

	task void sendStatus() {
		error_t err;

		if (radioBusy == FALSE) {
			memcpy(call RadioStatusSend.getPayload(&status_msg, sizeof(nx_plum_status_t)), &nx_plum_status, sizeof(nx_plum_status_t));
			radioBusy = TRUE;
			call PacketAcknowledgements.requestAck(&status_msg);
			err = call RadioStatusSend.send(BASE_STATION_ADDR, &status_msg, sizeof(nx_plum_status_t));
		}
		else {
			post sendStatus();
		}
	}
	
	task void sendSamples() {
		call LogBlock.read(firstRead, lastRead, sizeof(plum_sample_t));		
	}

	task void sendSample() {		
		error_t err = 255;
		
		if (radioBufferTail != radioCurrentBuffer && radioBusy == FALSE) {
			dataTransfer = TRUE;
		    nx_plum_sample_msg = (nx_plum_sample_t*)call RadioSampleSend.getPayload(&sample_msg, sizeof(nx_plum_sample_t));
			copyToNx(nx_plum_sample_msg, &(radioSampleBuffer[radioBufferTail]));

			if (serialSend == TRUE) {
				serialBusy = TRUE;
				err = call SerialSampleSend.send(BASE_STATION_ADDR, &sample_msg, sizeof(nx_plum_sample_t));
			} else {				
				radioBusy = TRUE;
				err = call RadioSampleSend.send(BASE_STATION_ADDR, &sample_msg, sizeof(nx_plum_sample_t));
			}

			if(err == SUCCESS) {			
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
				printf("sendSample() : readNextBlock() : %d, %d, %d\n", radioSampleBuffer[radioBufferTail].seqno, radioSampleBuffer[radioBufferTail].blockID, err);
				printfflush();
				call LogBlock.readNextBlock();
			}
			else {
				dataTransfer = FALSE;
				if (serialSend == TRUE) {
					serialSend = FALSE;
				}
			}
		}
	}

	task void eraseFlash() {
#ifndef DISABLE_FLASH
		call LogBlock.erase();
#endif
	}

	void initConfig() {
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

	event void SerialSampleSend.sendDone(message_t* msg, error_t error) {
		radioBufferTail = (radioBufferTail + 1) % SAMPLE_RADIO_BUFFER_SIZE;
		serialBusy = FALSE;
		post sendSample();
	}
	
	event void RadioSampleSend.sendDone(message_t* msg, error_t error) {
		radioBufferTail = (radioBufferTail + 1) % SAMPLE_RADIO_BUFFER_SIZE;
		radioBusy = FALSE;
		post sendSample();
	}

	event void RadioStatusSend.sendDone(message_t* msg, error_t error) {
		radioBusy = FALSE;
		
		if (call PacketAcknowledgements.wasAcked(msg)) {
			call ListenTimer.startOneShot(LISTEN_PERIOD);			
		}
		else {
			call RadioAMControl.stop();
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
			firstRead = ((plum_cmd_msg_t *) payload)->blockStart;
			lastRead = ((plum_cmd_msg_t *) payload)->blockEnd;
			post sendSamples();
		}
		else if (((plum_cmd_msg_t *) payload)->cmdID == PLUM_ERASE) {
			post eraseFlash();
		}
		else if (((plum_cmd_msg_t *) payload)->cmdID == PLUM_ERASE_CONFIG) {
			post eraseConfig();
		}
		else if (((plum_cmd_msg_t *) payload)->cmdID == PLUM_TIME) {
			atomic {
				currentUnixTime = ((plum_cmd_msg_t *) payload)->unixTime;
				timePrev = call TimeCounter.get();
			}
		}

		return msg;
	}

	event message_t* SerialCmdMsgReceive.receive(message_t* msg, void* payload, uint8_t len) {
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
			firstRead = ((plum_cmd_msg_t *) payload)->blockStart;
			lastRead = ((plum_cmd_msg_t *) payload)->blockEnd;
			serialSend = TRUE;
			post sendSamples();
		}
		else if (((plum_cmd_msg_t *) payload)->cmdID == PLUM_ERASE) {
			post eraseFlash();
		}
		else if (((plum_cmd_msg_t *) payload)->cmdID == PLUM_ERASE_CONFIG) {
			post eraseConfig();
		}
		else if (((plum_cmd_msg_t *) payload)->cmdID == PLUM_TIME) {
			atomic {
				currentUnixTime = ((plum_cmd_msg_t *) payload)->unixTime;
				timePrev = call TimeCounter.get();
			}
		}

		return msg;
	}
	
	//
	// Events
	//

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
			for (sampleCount = 0 ; sampleCount < numSamples ; sampleCount++) {
				if ((radioCurrentBuffer + 1) % SAMPLE_RADIO_BUFFER_SIZE == radioBufferTail) {
					// Buffer is full - drop samples
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
