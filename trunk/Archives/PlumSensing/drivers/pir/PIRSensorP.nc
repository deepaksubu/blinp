module PIRSensorP
{
  uses interface Boot;
  uses interface HplMsp430GeneralIO as GIO;

  provides interface PIRSensor;
}
implementation
{
  event void Boot.booted()
  {
    call GIO.makeInput();
  }

  command bool PIRSensor.read() {
	  return call GIO.get();
  }
}
