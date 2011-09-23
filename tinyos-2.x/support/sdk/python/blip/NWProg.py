
import struct, time
import ShellConstants
from Exceptions import ParseException

import NWProgConsts, NWProgReq, NWProgReply, ShortDelugeIdent

class NWProg:
    """nwprog [list | boot <imgno> [when] | reboot]"""

    def cmd_id(self):
        return ShellConstants.ShellConstants.BSHELL_NWPROG
    def cmd_name(self):
        return "nwprog"

    def parse(self, cmd):
        cmdmsg = NWProgReq.NWProgReq()
        if len(cmd) < 2:
            raise ParseException
        if cmd[1] == 'reboot':
            cmdmsg.set_cmd(NWProgConsts.NWProgConsts.NWPROG_CMD_REBOOT)
	    forward = 0
            return cmdmsg.data,forward
        elif cmd[1] == 'list':
            cmdmsg.set_cmd(NWProgConsts.NWProgConsts.NWPROG_CMD_LIST)
	    forward = 0
            return cmdmsg.data,forward
        elif cmd[1] == 'boot':
            if len(cmd) < 3:
                raise ParseException
            cmdmsg.set_cmd(NWProgConsts.NWProgConsts.NWPROG_CMD_BOOT)
            cmdmsg.set_imgno(int(cmd[2]))

            if len(cmd) == 4:
                cmdmsg.set_cmd_data_when(int(cmd[3]))
            else:
                cmdmsg.set_cmd_data_when(30)
	    forward = 0
            return cmdmsg.data,forward

    def display(self, msg):
        extra_data = msg[NWProgReq.DEFAULT_MESSAGE_SIZE:]
        repmsg = NWProgReq.NWProgReq(msg[0:NWProgReq.DEFAULT_MESSAGE_SIZE])
        if repmsg.get_cmd() == NWProgConsts.NWProgConsts.NWPROG_CMD_READDONE:
            print "%i valid image(s)" % repmsg.get_cmd_data_nimages()
        elif repmsg.get_cmd() == NWProgConsts.NWProgConsts.NWPROG_CMD_IMAGEIFO:
            t, = struct.unpack(">L", extra_data[48:52])

            print "Image %i:" % repmsg.get_imgno()
            print "  Appname:", extra_data[0:16]
            print "  Username:", extra_data[16:32]
            print "  Hostname:", extra_data[32:48]
            print "  Build time:", time.ctime(t)
            
        elif repmsg.get_cmd() == NWProgConsts.NWProgConsts.NWPROG_CMD_BOOT:
            print "REBOOT %i" % repmsg.get_cmd_data_when()
