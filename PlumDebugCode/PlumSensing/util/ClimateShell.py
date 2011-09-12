
import struct
import ClimateConstants
from ParseException import ParseException
import ConfigCmd,TimeCmd

class CmdBlink:
    """blink"""
    def cmd_id(self):
        return ClimateConstants.ShellConstants.BSHELL_BLINK
    def cmd_name(self):
        return "blink"
    def display(self, msg):
	if len(msg) > 0:
	    print "Blink Request ACK'ed"
    def parse(self, cmd):
        if len(cmd) > 1 and cmd[1] == '-f':
            del cmd[1]
	    forward = 1
	elif len(cmd) > 1 and cmd[1] == '-r':
	    del cmd[1]
	    forward = 2
        else:
	    forward = 0

        return "",forward


class CmdSense:
    """sense"""
    def cmd_id(self):
        return ClimateConstants.ShellConstants.BSHELL_SENSE
    def cmd_name(self):
        return "sense"
    def parse(self, cmd):
        return ""

class CmdChsrt:
    """chsrt <rate (s)>"""
    def cmd_id(self):
        return ClimateConstants.ShellConstants.BSHELL_CHSRT
    def cmd_name(self):
        return "chsrt"
    def parse(self, cmd):
        if len(cmd) != 2:
            raise ParseException
        try:
            rate = int(cmd[1])
        except:
            raise ParseException

        data = struct.pack(">H", rate)
        return data

class CmdChrrt:
    """chrrt <rate (s)>"""
    def cmd_id(self):
	return ClimateConstants.ShellConstants.BSHELL_CHRRT
    def cmd_name(self):
        return "chrrt"
    def parse(self, cmd):
        if len(cmd) != 2:
            raise ParseException
        try:
            rate = int(cmd[1])
        except:
            raise ParseException

        data = struct.pack(">H", rate)
        return data

class CmdLogErase:
    """erase"""
    def cmd_id(self):
        return ClimateConstants.ShellConstants.BSHELL_LERASE
    def cmd_name(self):
        return "erase"
    def parse(self, cmd):
        return ""

class CmdConfig:
    """config <sample rate (s)> <report rate (s)>"""
    def cmd_id(self):
        return ClimateConstants.ShellConstants.BSHELL_CONFIG
    def cmd_name(self):
        return "config"
    def display(self, msg):
        c = ConfigCmd.ConfigCmd(data=msg)
        print "Sense Rate: %u" % c.get_senseRate()
        print "Report Rate: %u" % c.get_rptRate()
    def parse(self, cmd):
	cmdmsg = ConfigCmd.ConfigCmd()
	if len(cmd) > 0:
	    if cmd[1] == '-f':
            	del cmd[1]
		forward = 1
            elif cmd[1] == '-r':
	    	del cmd[1]
		forward = 2
            else:
		forward = 0
	else:
	    return ""

        if len(cmd) != 1 and len(cmd) != 3:
            raise ParseException

	if len(cmd) == 1 and forward == 1:
	    raise ParseException

        try:
                if (len(cmd) == 1):
        		cmdmsg.set_senseRate(0)
			cmdmsg.set_rptRate(0)
                else:
		        cmdmsg.set_senseRate(int(cmd[1]))
			cmdmsg.set_rptRate(int(cmd[2]))
        except:
	        raise ParseException

        return cmdmsg.data, forward

class CmdTime:
    """time <new Unix time (s)>"""
    def cmd_id(self):
        return ClimateConstants.ShellConstants.BSHELL_TIME
    def cmd_name(self):
        return "time"
    def display(self, msg):
        c = TimeCmd.TimeCmd(data=msg)
        print "Unix Time: %lu" % c.get_unixTime()
    def parse(self, cmd):
        cmdmsg = TimeCmd.TimeCmd()
        if len(cmd) > 0:
            if cmd[1] == '-f':
                del cmd[1]
		forward = 1
            elif cmd[1] == '-r':
	        del cmd[1]
		forward = 2
            else:
		forward = 0
	else:
	    return ""

        if len(cmd) != 1 and len(cmd) != 2:
            raise ParseException

        if len(cmd) == 1 and forward == 1:
            raise ParseException

        try:
            	if (len(cmd) == 1):
                        cmdmsg.set_unixTime(0)
                else:
                        cmdmsg.set_unixTime(int(cmd[1]))
	except:
                raise ParseException

        return cmdmsg.data, forward
