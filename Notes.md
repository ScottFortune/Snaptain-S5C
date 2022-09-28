# Structures

## Transmission Control Protocol Packets

> TODO: Finish this section.

These packets are used for reliable data transmission to and from the drone. 

A certain packet structure is used to enable the user datagram protocol (UDP) port, which is used to send command data to the drone.
    
## User Datagram Protocol Packets
The data sent through the user datagram protocol is 8 bytes of command data. The structure is as follows:

| Address | Purpose      | Notes                                                                                                                                                                                                                                                                                      |
|---------|--------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 0x0       | PACKET_START | 0x66                                                                                                                                                                                                                                                                                       |
| 0x1       | DX           | Change in x. <br> If `DX` < 0x80, the change in x is negative, leading to a decrease in x. <br> If `DX` = 0x80, the change in x is zero, leading to no change in x. <br> If `DX` > 0x80, the change in x is positive, leading to a increase in x.                                                                                                                                                                                                                                                                                            |
| 0x2       | DY           | Change in y. <br> If `DY` < 0x80, the change in y is negative, leading to a decrease in y. <br> If `DY` = 0x80, the change in y is zero, leading to no change in y. <br> If `DY` > 0x80, the change in y is positive, leading to a increase in y.                                                                                                                                                                                                                                                                                            |
| 0x3       | DZ           | Change in z (height). <br> If `DZ` < 0x80, the change in height is negative, leading to a decrease in height. <br> If `DZ` = 0x80, the change in height is zero, leading to no change in height. <br> If `DZ` > 0x80, the change in height is positive, leading to a increase in height.                                                                                                                                                                                                                                                                                        |
| 0x4       | DR           | Velocity of rotation about the z-axis. <br> If `DR` < 0x80, the rotation velocity is negative, leading to a counter-clockwise rotation. <br> If `DR` = 0x80, the rotation velocity is zero, meaning no rotation. <br> If `DR` > 0x80, the rotation velocity is positive, leading to a clockwise rotation. |
| 0x5       | CMD_INDEX    | 0x0 - No operation (?). <br>0x1 - Takeoff. <br> Definitely more...                                                                                                                                                                                                                                  |
| 0x6       | CHECKSUM     | The checksum is calculated using exclusive OR on the bytes from 0x1 to 0x4.  In other words, `CHECKSUM` = `DX` XOR `DY` XOR `DZ` XOR `DR`.                                                                                                                                             |
| 0x7       | PACKET_END   | 0x99                                                                                                                                                                                                                                                                                       |

This data is crafted into an 8 byte packet that is sent to the drone on port 9125, provided it is enabled.