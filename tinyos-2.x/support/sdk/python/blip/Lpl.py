
from Exceptions import ParseException
import LplControl, LplControlConsts

class CmdSetLpl:
    """lpl <interval> <start (s)> <length (s)>"""
    def cmd_id(self):
        return LplControlConsts.LplControlConsts.BSHELL_LPL
    def cmd_name(self):
        return "lpl"
    def display(self, msg):
        c = LplControl.LplControl(data=msg)
        print "LPL Interval: " + str(c.get_period())
# JKT: Commented because of codespace limitations
#	if (c.get_length_sec() == 0):
#	    print "Current Lease: 0s (No lease)"
#	else:
#	    print "Current Lease: %ds" % c.get_length_sec()
    def parse(self,cmd):
        if (len(cmd) != 2 and len(cmd) != 4 and len(cmd) != 5):
            raise ParseException

        cmdmsg = LplControl.LplControl()
        if cmd[1] == '-f':
            del cmd[1]
	    forward = 1
        elif len(cmd) > 1 and cmd[1] == '-r':
            del cmd[1]
	    if len(cmd) > 1:
		forward = 2
	    else:
		forward = 3
        else:
	    forward = 0
        
	if forward != 3:
            cmdmsg.set_period(int(cmd[1]))
            cmdmsg.set_start_msec(int(cmd[2]) * 1024)
            cmdmsg.set_length_sec(int(cmd[3]))

        return cmdmsg.data,forward
