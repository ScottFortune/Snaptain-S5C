package com.netopsun.rxtxprotocol.hy_blue_light_gps_protocol;

import com.netopsun.rxtxprotocol.base.gps_receiver.DroneMsgCallback;
import com.netopsun.rxtxprotocol.base.gps_receiver.DroneStatusModel;

public class HYReceiveDataAnalyzer {
  private DroneMsgCallback droneMsgCallback;
  
  private final DroneStatusModel droneStatusModel = new DroneStatusModel();
  
  private int lastRecordValue;
  
  private int lastTakePhotoValue;
  
  private final HYPacketSplitter packetSplitter = new HYPacketSplitter(new HYPacketSplitter.Callback() {
        public void onPackage(byte[] param1ArrayOfbyte) {
          HYReceiveDataAnalyzer.this.isDroneMsg(param1ArrayOfbyte);
          HYReceiveDataAnalyzer.this.isDroneMsg3Or3b(param1ArrayOfbyte);
          HYReceiveDataAnalyzer.this.isDroneMsg4(param1ArrayOfbyte);
          HYReceiveDataAnalyzer.this.isGPSMsg(param1ArrayOfbyte);
          HYReceiveDataAnalyzer.this.isWayPointNumMsg(param1ArrayOfbyte);
        }
      });
  
  private int readBatteryMode = 0;
  
  private volatile int sendWayPointSuccessNum = -1;
  
  public static int byte2Int(byte[] paramArrayOfbyte, int paramInt) {
    byte b1 = paramArrayOfbyte[paramInt];
    byte b2 = paramArrayOfbyte[paramInt + 1];
    byte b3 = paramArrayOfbyte[paramInt + 2];
    return paramArrayOfbyte[paramInt + 3] & 0xFF | (b1 & 0xFF) << 24 | (b2 & 0xFF) << 16 | (b3 & 0xFF) << 8;
  }
  
  public static short byte2Int16Little(byte[] paramArrayOfbyte, int paramInt) {
    byte b = paramArrayOfbyte[paramInt + 1];
    return (short)(paramArrayOfbyte[paramInt] & 0xFF | (b & 0xFF) << 8);
  }
  
  public static int byte2IntLittle(byte[] paramArrayOfbyte, int paramInt) {
    byte b1 = paramArrayOfbyte[paramInt + 3];
    byte b2 = paramArrayOfbyte[paramInt + 2];
    byte b3 = paramArrayOfbyte[paramInt + 1];
    return paramArrayOfbyte[paramInt] & 0xFF | (b1 & 0xFF) << 24 | (b2 & 0xFF) << 16 | (b3 & 0xFF) << 8;
  }
  
  private boolean isDroneMsg(byte[] paramArrayOfbyte) {
    byte b = paramArrayOfbyte[1];
    boolean bool = false;
    if (b != -117)
      return false; 
    DroneStatusModel droneStatusModel = this.droneStatusModel;
    droneStatusModel.rollAngle = ((short)((paramArrayOfbyte[4] & 0xFF) << 8 | paramArrayOfbyte[3] & 0xFF) / 100);
    droneStatusModel.pitchAngle = ((short)((paramArrayOfbyte[6] & 0xFF) << 8 | paramArrayOfbyte[5] & 0xFF) / 100);
    droneStatusModel.yawAngle = ((short)((paramArrayOfbyte[8] & 0xFF) << 8 | paramArrayOfbyte[7] & 0xFF) / 100);
    droneStatusModel.insInitOk = (paramArrayOfbyte[9] & 0xFF) >> 0 & 0x1;
    droneStatusModel.baroInitOk = (paramArrayOfbyte[9] & 0xFF) >> 1 & 0x1;
    droneStatusModel.magInitOk = (paramArrayOfbyte[9] & 0xFF) >> 2 & 0x1;
    droneStatusModel.gpsInitOk = (paramArrayOfbyte[9] & 0xFF) >> 3 & 0x1;
    droneStatusModel.flowInitOk = (paramArrayOfbyte[9] & 0xFF) >> 4 & 0x1;
    droneStatusModel.accelerationCalibration = (paramArrayOfbyte[9] & 0xFF) >> 5 & 0x1;
    droneStatusModel.compassCalibrationX = (paramArrayOfbyte[9] & 0xFF) >> 6 & 0x1;
    droneStatusModel.compassCalibrationY = (paramArrayOfbyte[9] & 0xFF) >> 7 & 0x1;
    droneStatusModel.compassCalibrationProgress = paramArrayOfbyte[10] & Byte.MAX_VALUE;
    if (this.readBatteryMode == 1)
      droneStatusModel.batteryLevel = (paramArrayOfbyte[11] & 0x3F) << 1 | (paramArrayOfbyte[10] & 0xFF) >> 7; 
    droneStatusModel = this.droneStatusModel;
    droneStatusModel.lowPowerWarning = (paramArrayOfbyte[11] & 0xC0) >> 6;
    if ((paramArrayOfbyte[12] & 0x6) >> 1 == 1)
      droneStatusModel.flyState = DroneStatusModel.FlyStateType.locked_rotor; 
    if ((paramArrayOfbyte[12] & 0x6) >> 1 == 2)
      this.droneStatusModel.flyState = DroneStatusModel.FlyStateType.Inclination_protect; 
    if ((paramArrayOfbyte[12] & 0x18) >> 3 == 0)
      this.droneStatusModel.flyState = DroneStatusModel.FlyStateType.Locked; 
    if ((paramArrayOfbyte[12] & 0x18) >> 3 == 1)
      this.droneStatusModel.flyState = DroneStatusModel.FlyStateType.UnlockedNotTakeOff; 
    if ((paramArrayOfbyte[12] & 0x18) >> 3 == 2)
      this.droneStatusModel.flyState = DroneStatusModel.FlyStateType.UnlockedAndTakeOff; 
    if ((paramArrayOfbyte[12] & 0xE0) >> 5 == 0)
      this.droneStatusModel.flyMode = DroneStatusModel.FlyModeType.Stabilize; 
    if ((paramArrayOfbyte[12] & 0xE0) >> 5 == 1)
      this.droneStatusModel.flyMode = DroneStatusModel.FlyModeType.FlyBack; 
    if ((paramArrayOfbyte[12] & 0xE0) >> 5 == 2) {
      this.droneStatusModel.flyMode = DroneStatusModel.FlyModeType.FollowMe;
      DroneMsgCallback droneMsgCallback1 = this.droneMsgCallback;
      if (droneMsgCallback1 != null)
        droneMsgCallback1.recvFollowmeCommand(); 
    } 
    if ((paramArrayOfbyte[12] & 0xE0) >> 5 == 3) {
      this.droneStatusModel.flyMode = DroneStatusModel.FlyModeType.Surround;
      DroneMsgCallback droneMsgCallback1 = this.droneMsgCallback;
      if (droneMsgCallback1 != null)
        droneMsgCallback1.recvCircleCommand(); 
    } 
    if ((paramArrayOfbyte[12] & 0xE0) >> 5 == 4) {
      this.droneStatusModel.flyMode = DroneStatusModel.FlyModeType.RouteFlight;
      DroneMsgCallback droneMsgCallback1 = this.droneMsgCallback;
      if (droneMsgCallback1 != null)
        droneMsgCallback1.recvWaypointCommand(); 
    } 
    this.droneStatusModel.flyBackStatus = (paramArrayOfbyte[13] & 0xE) >> 1;
    int i = (paramArrayOfbyte[13] & 0x10) >> 4;
    if (i == 1 && this.lastTakePhotoValue != 1) {
      DroneMsgCallback droneMsgCallback1 = this.droneMsgCallback;
      if (droneMsgCallback1 != null)
        droneMsgCallback1.didRecvTakePhotoCmd(); 
    } 
    this.lastTakePhotoValue = i;
    i = (paramArrayOfbyte[13] & 0x20) >> 5;
    if (i != this.lastRecordValue)
      if (i == 1) {
        this.droneMsgCallback.didRecvRecordStartCmd();
      } else {
        this.droneMsgCallback.didRecvRecordStopCmd();
      }  
    this.lastRecordValue = i;
    droneStatusModel = this.droneStatusModel;
    droneStatusModel.satelliteNum = (paramArrayOfbyte[14] & 0x7C) >> 2;
    if ((paramArrayOfbyte[14] & 0x80) >> 7 == 1)
      bool = true; 
    droneStatusModel.gpsFine = bool;
    droneStatusModel = this.droneStatusModel;
    droneStatusModel.gpsMode = (paramArrayOfbyte[15] & 0x80) >> 7 | (paramArrayOfbyte[16] & 0x1) << 1;
    droneStatusModel.rcFastMode = (paramArrayOfbyte[16] & 0x6) >> 1;
    DroneMsgCallback droneMsgCallback = this.droneMsgCallback;
    if (droneMsgCallback != null)
      droneMsgCallback.droneStatusUpdate(droneStatusModel); 
    return true;
  }
  
  private boolean isDroneMsg3Or3b(byte[] paramArrayOfbyte) {
    if (paramArrayOfbyte[1] != -114 && paramArrayOfbyte[1] != -113)
      return false; 
    DroneStatusModel droneStatusModel = this.droneStatusModel;
    byte b = paramArrayOfbyte[3];
    droneStatusModel.magInterference = ((0x3 & paramArrayOfbyte[4]) << 8 | b & 0xFF);
    if (this.readBatteryMode == 0) {
      droneStatusModel.batteryVoltage = (((paramArrayOfbyte[4] & 0xFF) >> 2 | (paramArrayOfbyte[5] & 0x3F) << 6) / 100.0F);
      double d = droneStatusModel.batteryVoltage;
      if (d > 12.5D) {
        this.droneStatusModel.batteryLevel = 100;
      } else if (d < 10.0D) {
        this.droneStatusModel.batteryLevel = 0;
      } else {
        this.droneStatusModel.batteryLevel = (int)(15918.9084070807D - 4239.01294984951D * d + 372.230713098469D * d * d - 10.7495530218571D * d * d * d);
      } 
      if (this.droneStatusModel.batteryLevel < 0) {
        this.droneStatusModel.batteryLevel = 0;
      } else if (this.droneStatusModel.batteryLevel > 100) {
        this.droneStatusModel.batteryLevel = 100;
      } 
    } 
    droneStatusModel = this.droneStatusModel;
    b = paramArrayOfbyte[5];
    droneStatusModel.accuracy = (((paramArrayOfbyte[5] & 0xFF) << 2 | (b & 0xFF) >> 6) / 100.0F);
    return true;
  }
  
  private boolean isDroneMsg4(byte[] paramArrayOfbyte) {
    if (paramArrayOfbyte[1] != -111)
      return false; 
    short s = byte2Int16Little(paramArrayOfbyte, 3);
    this.droneStatusModel.testInfo.gyro_x = s;
    s = byte2Int16Little(paramArrayOfbyte, 5);
    this.droneStatusModel.testInfo.gyro_y = s;
    s = byte2Int16Little(paramArrayOfbyte, 7);
    this.droneStatusModel.testInfo.gyro_z = s;
    s = byte2Int16Little(paramArrayOfbyte, 9);
    this.droneStatusModel.testInfo.acc_x = s;
    s = byte2Int16Little(paramArrayOfbyte, 11);
    this.droneStatusModel.testInfo.acc_y = s;
    s = byte2Int16Little(paramArrayOfbyte, 13);
    this.droneStatusModel.testInfo.acc_z = s;
    s = byte2Int16Little(paramArrayOfbyte, 15);
    this.droneStatusModel.testInfo.mag_x = s;
    s = byte2Int16Little(paramArrayOfbyte, 17);
    this.droneStatusModel.testInfo.mag_y = s;
    s = byte2Int16Little(paramArrayOfbyte, 19);
    this.droneStatusModel.testInfo.mag_z = s;
    this.droneStatusModel.testInfo.temperature = paramArrayOfbyte[21];
    DroneMsgCallback droneMsgCallback = this.droneMsgCallback;
    if (droneMsgCallback != null)
      droneMsgCallback.droneTestInfoUpdate(this.droneStatusModel); 
    return true;
  }
  
  private boolean isGPSMsg(byte[] paramArrayOfbyte) {
    if (paramArrayOfbyte[1] != -116)
      return false; 
    int i = byte2IntLittle(paramArrayOfbyte, 3);
    this.droneStatusModel.droneLng = (i / 1.0E7F);
    i = byte2IntLittle(paramArrayOfbyte, 7);
    DroneStatusModel droneStatusModel = this.droneStatusModel;
    droneStatusModel.droneLat = (i / 1.0E7F);
    droneStatusModel.altitude = ((paramArrayOfbyte[11] & 0xFF | (paramArrayOfbyte[12] & 0xF) << 8) / 10.0F - 100.0F);
    droneStatusModel.homeDistance = (((paramArrayOfbyte[12] & 0xF0) >> 4 | (paramArrayOfbyte[13] & 0xFF) << 4 | (paramArrayOfbyte[14] & 0x1) << 12) / 10.0F);
    droneStatusModel.speedH = (((paramArrayOfbyte[14] & 0xFF) >> 1) / 10.0F);
    droneStatusModel.speedV = (paramArrayOfbyte[15] / 10.0F);
    DroneMsgCallback droneMsgCallback = this.droneMsgCallback;
    if (droneMsgCallback != null)
      droneMsgCallback.droneStatusUpdate(droneStatusModel); 
    return true;
  }
  
  private boolean isWayPointNumMsg(byte[] paramArrayOfbyte) {
    if (paramArrayOfbyte[1] != -124)
      return false; 
    this.sendWayPointSuccessNum = paramArrayOfbyte[3] & 0xFF;
    return true;
  }
  
  public int getSendWayPointSuccessNum() {
    return this.sendWayPointSuccessNum;
  }
  
  public void parseData(byte[] paramArrayOfbyte, int paramInt) {
    for (byte b = 0; b < paramInt; b++)
      this.packetSplitter.onByte(paramArrayOfbyte[b]); 
  }
  
  public void setDroneMsgCallback(DroneMsgCallback paramDroneMsgCallback) {
    this.droneMsgCallback = paramDroneMsgCallback;
  }
  
  public void setReadBatteryMode(int paramInt) {
    this.readBatteryMode = paramInt;
  }
  
  public void setSendWayPointSuccessNum(int paramInt) {
    this.sendWayPointSuccessNum = paramInt;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/rxtxprotocol/hy_blue_light_gps_protocol/HYReceiveDataAnalyzer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */