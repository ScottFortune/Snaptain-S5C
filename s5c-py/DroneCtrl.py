from UDPStream import *
from TCPStream import *

Host = '172.17.10.1'
Ports = {
    'TCP' : 8888,
    'UDP' : 9125
}

TCP = TCPStream()
print('[+] Connecting to TCP...')
TCP.Connect(Host, Ports['TCP'])
print('[+] Connected to TCP.')
TCP.Link()


UDP = UDPStream()
print('[+] Connecting to UDP...')
UDP.Connect(Host, Ports['UDP'])
print('[+] Connected to UDP.')
print('[+] Sending takeoff command...')
UDP.SendCommand(0x1, 0x50, 0x50, 0x0, 0x50)