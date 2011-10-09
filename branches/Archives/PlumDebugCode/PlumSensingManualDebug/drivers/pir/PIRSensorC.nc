configuration PIRSensorC
{
	provides interface PIRSensor;
}
implementation
{
	components MainC, PIRSensorP;
	PIRSensorP -> MainC.Boot;
	PIRSensor = PIRSensorP;

	components HplMsp430GeneralIOC;
	// PLUM platform connects PIR sensor at GIO3 (MSP430 Port 2.6)
	PIRSensorP.GIO->HplMsp430GeneralIOC.Port26;
}
