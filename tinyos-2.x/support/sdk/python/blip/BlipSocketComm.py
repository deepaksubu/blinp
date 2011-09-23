
from Exceptions import CommException
import socket
SHELL_PORT = 2001

class BlipSocketComm:

    def __init__(self, addr, Interface=None):
        self.remote = (addr, SHELL_PORT)
        self.sock = socket.socket(socket.AF_INET6, socket.SOCK_DGRAM)

        if Interface != None:
            self.sock.setsockopt(socket.SOL_SOCKET, 25, Interface)

    def send(self, data):
        try:
            rv = self.sock.sendto(data, self.remote)
            return rv
        except Exception, (rv, str):
            raise CommException, str

    def close(self):
        return self.sock.close()

    def recvfrom(self, len):
        return self.sock.recvfrom(len)

