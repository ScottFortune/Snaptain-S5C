from FHDevice import *
from FHSerialPort import *
from struct import pack, unpack

def Login():
    Packet = Device.GetIP().encode()[::-1] 
    Packet += Device.GetUsername(1).encode()[::-1]
    Packet += Device.GetPassword(1).encode()[::-1]
    print('[+] Sending packet', Packet)
    Port.Send(Packet)
    print(Port.Receive(4096))
    
Device = FHDevice()
Port = FHSerialPort()
Port.Init(Device.GetIP(), 8888)
Login()

