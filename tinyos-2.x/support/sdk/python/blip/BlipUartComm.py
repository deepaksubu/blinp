
from tinyos.message import MoteIF, Message
from threading import Condition
import BlipUartMsg
import socket,time

class BlipUartComm:

    def __init__(self, sf):
        self.mif = MoteIF.MoteIF()
        self.source = self.mif.addSource(sf)
        time.sleep(1)
        
        self.mif.addListener(self, BlipUartMsg.BlipUartMsg)
        self.rx_cv = Condition()

        self.data_pending = False
        self.data = []


    def sendMsg(self, addr, msg, group=0x0, amtype=None):
        if (amtype == None): amtype = msg.get_amType()
        try:
            self.mif.sendMsg(self.source, addr, amtype, group, msg)
        except:
            print "Sending message to", addr, "failed!"


    def receive(self, source, msg):
        self.rx_cv.acquire()
        self.data_pending = True
        self.data = msg.data
        self.rx_cv.notify()
        self.rx_cv.release()

        
    def send(self, d):
        msg = BlipUartMsg.BlipUartMsg(data=d, data_length=len(d))
        self.sendMsg(0xffff, msg)
        
    def close(self):
        print "finishing moteifs..."
        self.mif.removeListener(self)
        self.mif.finishAll()

    def recvfrom(self, len):
        self.rx_cv.acquire()
        while not self.data_pending:
            self.rx_cv.wait()
        my_data = self.data
        self.data_pending = False
        self.rx_cv.release()

        return (my_data, [])
