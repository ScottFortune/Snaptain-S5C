from socket import *

class FHSerialPort:
    def Init(self, IP, Port):
        self.Socket = socket(AF_INET, SOCK_STREAM)
        self.Socket.connect((IP, Port))

    def Free(self):
        self.Socket.close()
        self.Socket = None

    def Receive(self, n):
        return self.Socket.recv(n)

    def Send(self, data):
        return self.Socket.send(data)