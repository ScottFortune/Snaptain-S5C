from TCPStream import *
from UDPStream import *
import pygame
from pygame.locals import *

TakeoffEnabled = False
Delta = {
    'Flag' : 0x0,
    'X' : 0x0,
    'Y' : 0x0,
    'Z' : 0x0,
    'Rotation' : 0x0
}
Host = '172.17.10.1'
Ports = {
    'TCP': 8888,
    'UDP': 9125
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

Range = 0x30
Z_Range = 0x10
pygame.init()
pygame.joystick.init()
joystick = pygame.joystick.Joystick(0)
joystick.init()

while True:
    Delta['X'] = int(joystick.get_axis(0) * Range)
    Delta['Y'] = -int(joystick.get_axis(1) * Range)
    if joystick.get_axis(5) > 0.0 or joystick.get_axis(2) > 0.0:
        Delta['Z'] = int(joystick.get_axis(5) * Z_Range) - int(joystick.get_axis(2) * Z_Range)
    else:
        Delta['Z'] = 0
    if TakeoffEnabled:
        UDP.SendCommand(Delta['Flag'], 0x80 + Delta['X'], 0x80 + Delta['Y'], 0x80 + Delta['Z'], 0x80 + Delta['Rotation'])
    for event in pygame.event.get():
        if event.type == pygame.JOYBUTTONDOWN and joystick.get_button(9):
            Delta['Flag'] = 0x1
            TakeoffEnabled = True
 


