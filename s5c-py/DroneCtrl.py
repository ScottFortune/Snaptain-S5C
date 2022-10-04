from asyncio import Handle
from platform import java_ver
from Controllers.Joystick import Joystick
from Controllers.Keyboard import Keyboard
from TCPStream import *
from UDPStream import *
import pygame
from pygame.locals import *
import threading
import ffmpeg
import cv2
import numpy as np

Host = '172.17.10.1'
Ports = {
    'TCP': 8888,
    'UDP': 9125
}

pygame.init()
pygame.joystick.init()

Handler = None
while Handler == None:
    selection = input('Enter J for Joystick, K for Keyboard: ')
    if len(selection) > 1 or len(selection) == 0:
        continue

    match selection.lower():
        case 'j':
            Handler = Joystick()
        case 'k':
            Handler = Keyboard()
        

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
    with open('strm.h264', 'ab') as strm:
        while True:
            strm.write(TCP.Receive(0x400))

VideoThread = threading.Thread(target=VideoStreamHandler, args=())
VideoThread.start()
Handler.start()

while True:
    #print(Handler.GetDelta('Flag'))
    UDP.SendCommand(Handler.GetDelta('Flag'), 0x80 + Handler.GetDelta('X'), 0x80 + Handler.GetDelta('Y'), 0x80 + Handler.GetDelta('Z'), 0x80 + Handler.GetDelta('Rotation'))
