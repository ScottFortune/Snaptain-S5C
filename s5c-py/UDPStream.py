from functools import reduce
import socket
import time

def byte(x):
    return (x & 0xFF).to_bytes(1, 'little')

class UDPStream:
    def __init__(self):
        self.Socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)

    def Connect(self, IP : str, Port : int):
        self.Socket.connect((IP, Port))

    def Disconnect(self):
        self.Socket.close()

    def SendCommand(self, index, dx, dy, dz, dr):
        SendBuf = b'\x66'
        SendBuf += byte(dx) + byte(dy) + byte(dz)
        SendBuf += byte(dr)
        SendBuf += byte(index)
        SendBuf += byte(reduce(lambda x, y: x ^ y, SendBuf[1:6]))
        SendBuf += b'\x99'
        self.Socket.send(SendBuf)