#include "Plum.h"

configuration PlumSensingC {

} implementation {
  components MainC, LedsC;
  components PlumSensingP as App;

  App.Boot -> MainC;
  App.Leds -> LedsC;

  components ActiveMessageC as Radio;
  App.RadioAMControl -> Radio;

  components new AMReceiverC(AM_PLUM_CMD_MSG) as RadioCmdMsgReceiver;
  App.RadioCmdMsgReceive -> RadioCmdMsgReceiver.Receive;

  components new AMSenderC(AM_NX_PLUM_STATUS) as RadioStatusSender;
  App.RadioStatusSend -> RadioStatusSender.AMSend;
  App.PacketAcknowledgements -> RadioStatusSender;
  
  components new AMSenderC(AM_NX_PLUM_SAMPLE) as RadioSampleSender;
  App.RadioSampleSend -> RadioSampleSender.AMSend;
  
  components new SerialAMReceiverC(AM_PLUM_CMD_MSG) as SerialCmdMsgReceiver;
  App.SerialCmdMsgReceive -> SerialCmdMsgReceiver.Receive;

  components new SerialAMSenderC(AM_NX_PLUM_SAMPLE) as SerialSampleSender;
  App.SerialSampleSend -> SerialSampleSender.AMSend;

  components LocalTimeMilliC as LocalTimeC;
  App.LocalTime -> LocalTimeC;
  components CounterMilli32C;
  App.TimeCounter -> CounterMilli32C;
  
  components new TimerMilliC() as SampleTimerC;
  components new TimerMilliC() as StatusTimerC;
  components new TimerMilliC() as ListenTimerC;
  App.SampleTimer -> SampleTimerC;
  App.StatusTimer -> StatusTimerC;
  App.ListenTimer -> ListenTimerC;
  
#ifndef DISABLE_FLASH
  // Storage
  components LogBlockC;
  App.LogBlock -> LogBlockC;
#endif
  
  // Sensors
  components new Msp430InternalVoltageC();
  components PIRSensorC;
  
  App.ReadIntVol -> Msp430InternalVoltageC.Read;
  App.ReadPIR -> PIRSensorC.PIRSensor;
}
