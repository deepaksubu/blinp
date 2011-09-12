/**
 * @author Kevin Klues <klueska@cs.stanford.edu>
 * @date July 24, 2007
 */

#include "Plum.h"
configuration PlumSensingBaseAppC{}
implementation {
  components MainC, PlumSensingBaseC as App;
  components new QueueC(message_t, MSG_QUEUE_SIZE) as Queue;
  components LedsC;
  MainC.Boot <- App;
  App.MsgQueue -> Queue;
  App.Leds -> LedsC;

  components SerialActiveMessageC as Serial;
  App.SerialAMControl -> Serial;
  App.SerialAMPacket -> Serial;
  App.SerialPacket -> Serial;

  components ActiveMessageC as Radio;
  App.RadioAMControl -> Radio;
  App.RadioAMPacket -> Radio;
  App.RadioPacket -> Radio;

  components CC2420ControlC;
  App.CC2420Config -> CC2420ControlC;

  components new SerialAMReceiverC(AM_PLUM_CMD_MSG) as SerialCmdMsgReceiver;
  components new AMSenderC(AM_PLUM_CMD_MSG) as RadioCmdMsgSender;
  components new SerialAMSenderC(AM_PLUM_CMD_MSG) as SerialCmdMsgSender;
  App.SerialCmdMsgReceive -> SerialCmdMsgReceiver;
  App.RadioCmdMsgSend -> RadioCmdMsgSender;
  App.SerialCmdMsgSend -> SerialCmdMsgSender;

  components new AMReceiverC(AM_NX_PLUM_SAMPLE) as RadioSampleReceiver;
  components new SerialAMSenderC(AM_NX_PLUM_SAMPLE) as SerialSampleSender;
  App.RadioSampleReceive -> RadioSampleReceiver;
  App.SerialSampleSend -> SerialSampleSender;

  components new AMReceiverC(AM_NX_PLUM_STATUS) as RadioStatusReceiver;
  components new SerialAMSenderC(AM_NX_PLUM_STATUS) as SerialStatusSender;
  App.RadioStatusReceive -> RadioStatusReceiver;
  App.SerialStatusSend -> SerialStatusSender;

  components CounterMilli32C;
  App.TimeCounter -> CounterMilli32C;
  
//  components CC2420ActiveMessageC as LPLProvider;
//  App.LPL -> LPLProvider;
}

