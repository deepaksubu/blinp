
CONSTANTS=BSHELL_ERROR   BSHELL_ENUMERATE  BSHELL_ECHO  \
	 BSHELL_PING6   BSHELL_PING6_REPLY   BSHELL_PING6_DONE  \
	 BSHELL_UPTIME   BSHELL_IDENT BSHELL_ERROR_NOTFOUND BSHELL_NWPROG
NWPROG_CONST=NWPROG_CMD_ERASE \
  NWPROG_CMD_WRITE \
  NWPROG_CMD_READ  \
  NWPROG_CMD_LIST  \
  NWPROG_CMD_BOOT  \
  NWPROG_CMD_REBOOT\
  NWPROG_CMD_READDONE \
  NWPROG_CMD_IMAGEIFO 

BINARYSHELL=$(TOSROOT)/tos/lib/net/blip/shell/BinaryShell.h
DELUGE=$(TOSROOT)/tos/lib/net/blip/nwprog/Deluge.h

LPLCONTROL=$(TOSROOT)/apps/UDPEcho/LplControl.h

all: constants wrapper ping6 ident uartmsg

constants:
	ncg python -python-classname=ShellConstants $(BINARYSHELL) $(CONSTANTS) > ShellConstants.py

wrapper:
	mig python -python-classname=CmdPayload  $(BINARYSHELL) cmd_payload > CmdPayload.py

echo:
	mig python -python-classname=EchoCmd  $(BINARYSHELL) bshell_echo > EchoCmd.py

ping6:
	mig python -python-classname=Ping6Cmd  $(BINARYSHELL) bshell_ping6 > Ping6Cmd.py
	mig python -python-classname=Ping6Reply  $(BINARYSHELL) bshell_ping6_reply > Ping6Reply.py

ident:
	mig python -python-classname=IdentCmd  $(BINARYSHELL) bshell_ident > IdentCmd.py

nwprog:
	mig python -python-classname=NWProgReq  $(DELUGE) prog_req > NWProgReq.py
	mig python -python-classname=NWProgReply  $(DELUGE) prog_reply > NWProgReply.py
	mig python -python-classname=ShortDelugeIdent  $(DELUGE) ShortDelugeIdent > ShortDelugeIdent.py
	ncg python -python-classname=NWProgConsts $(DELUGE) $(NWPROG_CONST) > NWProgConsts.py

uartmsg:
	mig python -python-classname=BlipUartMsg  $(TOSROOT)/apps/UARTShell/UARTShell.h uartshell_msg > BlipUartMsg.py

lpl:
	mig python -python-classname=LplControl $(LPLCONTROL) lpl_cmd > LplControl.py
	ncg python -python-classname=LplControlConsts $(LPLCONTROL) BSHELL_LPL > LplControlConsts.py
