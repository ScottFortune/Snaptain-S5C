from xml.dom.expatbuilder import theDOMImplementation
from TCPStream import *
from UDPStream import *
import pygame
from pygame.locals import *
import threading

TakeoffEnabled = False
Delta = {
    'Flag': 0x0,
    'X': 0x0,
    'Y': 0x0,
    'Z': 0x0,
    'Rotation': 0x0
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


def VideoStreamHandler():
    with open('strm.h264', 'wb') as strm:
        while True:
            strm.write(TCP.Receive(0x400))


def ControlSystemHandler():
    Ranges = {
        'XY' : 0x30,
        'Z' : 0x10
    }

    pygame.init()
    pygame.joystick.init()
    joystick = pygame.joystick.Joystick(0)
    joystick.init()

    TakeoffEnabled = False

    while True:
        pygame.event.get()
        Delta['Flag'] = 0x1 if joystick.get_button(9) else 0x0

        if Delta['Flag'] is 0x1:
            TakeoffEnabled = True

        if joystick.get_button(6):
            Delta['Z'] = -0x80
            Delta['Flag'] = 0x0
            TakeoffEnabled = False
            EndTime = time.time() + 5
            while time.time() < EndTime:
                UDP.SendCommand(Delta['Flag'], 0x80 + Delta['X'], 0x80 +
                                Delta['Y'], 0x80 + Delta['Z'], 0x80 + Delta['Rotation'])

        if TakeoffEnabled:
            Delta['X'] = int(joystick.get_axis(0) * Ranges['XY'])
            Delta['Y'] = -int(joystick.get_axis(1) * Ranges['XY'])
            Delta['Z'] = 0
            if joystick.get_axis(5) > 0.0:
                Delta['Z'] += int(joystick.get_axis(5) * Ranges['Z'])
            if joystick.get_axis(2) > 0.0:
                Delta['Z'] -= int(joystick.get_axis(2) * Ranges['Z'])
            UDP.SendCommand(Delta['Flag'], 0x80 + Delta['X'], 0x80 +
                            Delta['Y'], 0x80 + Delta['Z'], 0x80 + Delta['Rotation'])


VideoThread = threading.Thread(target=VideoStreamHandler, args=())
ControlThread = threading.Thread(target=ControlSystemHandler, args=())

VideoThread.start()
ControlThread.start()
