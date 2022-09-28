import keyboard
from TCPStream import *
from UDPStream import *

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
Delta = 0x30
Actions = {
    'Takeoff' : (0x1, 0x80, 0x80, 0x80, 0x80),
    'Idle' : (0x0, 0x80, 0x80, 0x80, 0x80),
    'Forward' : (0x0, 0x80, 0x80 + Delta, 0x80, 0x80),
    'Backward' : (0x0, 0x80, 0x80 - Delta, 0x80, 0x80),
    'Left' : (0x0, 0x80 + Delta, 0x80, 0x80, 0x80),
    'Right' : (0x0, 0x80 - Delta, 0x80, 0x80, 0x80),
    'Up' : (0x0, 0x80, 0x80, 0x80 + Delta, 0x80),
    'Down' : (0x0, 0x80, 0x80, 0x80 - Delta, 0x80),
}

while True:
    if keyboard.is_pressed('t'):
        UDP.SendCommand(*Actions['Takeoff'])    

    elif keyboard.is_pressed('l'):
        UDP.SendCommand(*Actions['Idle'])
        UDP.Disconnect()
        exit(0)
    
    elif keyboard.is_pressed('w'):
        UDP.SendCommand(*Actions['Forward'])

    elif keyboard.is_pressed('s'):
        UDP.SendCommand(*Actions['Backward'])

    elif keyboard.is_pressed('a'):
        UDP.SendCommand(*Actions['Left'])

    elif keyboard.is_pressed('d'):
        UDP.SendCommand(*Actions['Right'])

    elif keyboard.is_pressed('space'):
        UDP.SendCommand(*Actions['Up'])

    elif keyboard.is_pressed('c'):
        UDP.SendCommand(*Actions['Down'])

