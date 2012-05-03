/*
 * 
 * 
 */


configuration MaxBotixDriverC { 

	provides {
		interface MaxBotixRead;
	}
	uses {
		interface Read<uint16_t> as Adc;
	}

} // End configuration

implementation { 

	components MaxBotixDriverP;
  	components MainC;
	components new AdcDriverC();		
	components new Alarm32khz32C() as CalibrationTimeAlarm;
	components HplMsp430GeneralIOC;

  	MaxBotixDriverP.Boot -> MainC;
	MaxBotixDriverP.Adc -> AdcDriverC.Read;
	MaxBotixDriverP.CalibrationTimeAlarm -> CalibrationTimeAlarm;	
//	MaxBotixDriverP.RNG -> HplMsp430GeneralIOC.Port20;
	MaxBotixDriverP.RNG -> HplMsp430GeneralIOC.Port26;	
	MaxBotixDriverP.EN -> HplMsp430GeneralIOC.Port23;
	


	MaxBotixRead = MaxBotixDriverP.MaxBotixRead;
	Adc = MaxBotixDriverP; 

} // End implementation
