
/*
 * 
 */



module MaxBotixDriverP {

	uses {
		interface Boot;
		interface Read<uint16_t> as Adc;
		interface HplMsp430GeneralIO as RNG;				// corresponds to Gio0, port 2.6
		interface HplMsp430GeneralIO as EN;				// corresponds to Gio2, port 2.3
		interface Alarm<T32khz,uint32_t> as CalibrationTimeAlarm;		
		
	}	
	provides {
		interface MaxBotixRead;
	}

}  // End module

implementation {
  

	uint8_t counter = 0;

	uint16_t val_1;
	uint16_t val_4[4];
	uint16_t val_8[8];

	uint32_t calibrationTime = 32000;
	
	enum {
		READ_1 = 0,
		READ_4 = 1,
		READ_8 = 2,	
	};
	
	uint16_t state = READ_1;



	task void readDoneTask();		
	task void read4DoneTask();
	task void read8DoneTask();

  	event void Boot.booted() {
		call RNG.makeOutput(); 
		call EN.makeOutput(); 
  	}	 

	void prepareSensor() {
		call EN.set();
		call RNG.set();
		call CalibrationTimeAlarm.start(calibrationTime); 
	}

	command error_t MaxBotixRead.read() {
		state = READ_1;	
		prepareSensor();	
		return SUCCESS;
	}

	command error_t MaxBotixRead.read4() {
		state = READ_4;		
		prepareSensor();
		return SUCCESS;
	}

	command error_t MaxBotixRead.read8() {
		state = READ_8;	
		prepareSensor();	
		return SUCCESS;
	}

	async event void CalibrationTimeAlarm.fired() {
		call Adc.read();		
	}

	event void Adc.readDone(error_t err, uint16_t voltage) {
		if (state == READ_1) {
			val_1 = voltage;
			counter = 0;
			call EN.clr();
			call RNG.clr();
			post readDoneTask();
		} else if (state == READ_4) {
			if (counter < 3) {
				val_4[counter] = voltage;
				counter++;
				call Adc.read();
			} else if (counter >= 3) {
				val_4[counter] = voltage;		
				counter = 0;	
				call EN.clr();
				call RNG.clr();
				post read4DoneTask();		
			}			
		} else if (state == READ_8) {
			if (counter < 7) {
				val_8[counter] = voltage;
				counter++;
				call Adc.read();
			} else if (counter >= 7) {
				val_8[counter] = voltage;		
				counter = 0;
				call EN.clr();
				call RNG.clr();
				post read8DoneTask();			
			}		
		} 
  	}
	
	task void readDoneTask() {
		 signal MaxBotixRead.readDone(SUCCESS, val_1);
	}
	task void read4DoneTask() {
		signal MaxBotixRead.read4Done(SUCCESS, val_4);	
	}
	task void read8DoneTask() {
		signal MaxBotixRead.read8Done(SUCCESS, val_8);
	}



} // End implementation
