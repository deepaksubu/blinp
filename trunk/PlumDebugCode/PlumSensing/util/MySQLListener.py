
import socket
import UdpReport
import re
import sys
import MySQLdb
import time
import subprocess

port = 7000

if __name__ == '__main__':
    conn = MySQLdb.connect (host = "localhost",
                            user = "hydro",
			    passwd = "410soda",
                            db = "hydrowatch")
    cursor = conn.cursor()
    s = socket.socket(socket.AF_INET6, socket.SOCK_DGRAM)
    s.bind(('', port))
    if len(sys.argv) < 2:
        print "\t%s <tablename>" % sys.argv[0]
        sys.exit(1)

    try:
        drop = "DROP TABLE " + str(sys.argv[1])
#        cursor.execute(drop)
    except:
        print "Drop failed... continuing"

    methods = []
    create_table = "CREATE TABLE " + str(sys.argv[1]) + " ("
    create_table += "ts TIMESTAMP, unixtimeRecv BIGINT(20), origin INT(4), "
    insert = "INSERT IGNORE INTO " + sys.argv[1] + " (unixtimeRecv, origin, "


    re = re.compile('^get_(.*)')
    for method in dir(UdpReport.UdpReport):
        result = re.search(method)
        if result != None:
            create_table += str(result.group(1)) + " INT(4), "
            insert += str(result.group(1)) + ", "
            methods.append(str(result.group(1)))

#    create_table = create_table[0:len(create_table) - 2]
    create_table += "PRIMARY KEY (lTime,unixTime,origin,seqno)"
    insert = insert[0:len(insert) - 2]
    create_table += ");"
    insert += ") VALUES ("
    print insert
    print create_table

    try:
    	cursor.execute(create_table)
    except:
    	print "Create failed. Does table exist already? Continuing...\n"

    while True:
        data, addr = s.recvfrom(1024)
        if (len(data) > 0):

            filetime  = time.strftime("%Y-%m-%d")

            print
            print str(len(data)) + ":", 
            for i in data:
                print "0x%x" % ord(i),
 
            print
            rpt = UdpReport.UdpReport(data=data, data_length=len(data))
            addr = addr[0]
            AA = addr.split(":")
            print addr
            print rpt


            thisInsert = insert
            thisInsert += time.strftime("%s") + ", "
            thisInsert += "0x" + AA[-1] + ", "

            

            for m in methods:
                try:
                    getter = getattr(rpt, 'get_' + m, None)
                    val = getter()
                except:
                    val = 0
                if (isinstance(val, list)):
                    val = val[0]
                thisInsert += str(val) + ", "
            thisInsert = thisInsert[0:len(thisInsert) - 2]
            thisInsert += ");"

            insert2local = "echo \"" + thisInsert + "\" >> ./HydroTmp/" + filetime

	    print thisInsert

            cursor.execute(thisInsert)
            subprocess.os.system(insert2local)

    conn.close()

