from TCPStream import *
from UDPStream import *
from pydualsense import pydualsense, TriggerModes

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

def options_pressed(state):
    global TakeoffEnabled
    if not TakeoffEnabled:
        TakeoffEnabled = True
        DualSense.light.setColorI(0, 255, 0) 
        DualSense.triggerL.setForce(1, 255)
        DualSense.triggerR.setForce(1, 255)
    Delta['Flag'] = state
    print(Delta['Flag'])

def dxdy(stateX, stateY):
    if stateX > 0x14 or stateX < 0x14:
        Delta['X'] = stateX
    if stateY > 0x14 or stateY < 0x14:
        Delta['Y'] = -stateY

def pdz(state):
    Delta['Z'] = state // 2 

def ndz(state):
    Delta['Z'] = -state // 2 

DualSense = pydualsense()  # open controller
DualSense.init()  # initialize controller

DualSense.option_pressed += options_pressed
DualSense.left_joystick_changed += dxdy
DualSense.l2_changed += pdz
DualSense.r2_changed += ndz
DualSense.light.setColorI(255, 0, 0) 
DualSense.triggerL.setMode(TriggerModes.Rigid)
DualSense.triggerL.setForce(1, 0)
DualSense.triggerR.setMode(TriggerModes.Rigid)
DualSense.triggerR.setForce(1, 0)

TCP = TCPStream()
print('[+] Connecting to TCP...')
TCP.Connect(Host, Ports['TCP'])
print('[+] Connected to TCP.')
TCP.Link()


UDP = UDPStream()
print('[+] Connecting to UDP...')
UDP.Connect(Host, Ports['UDP'])
print('[+] Connected to UDP.')


while not DualSense.state.R1:
    if TakeoffEnabled:
        UDP.SendCommand(Delta['Flag'], 0x80 + Delta['X'], 0x80 + Delta['Y'], 0x80 + Delta['Z'], 0x80 + Delta['Rotation'])

DualSense.close()
exit()

