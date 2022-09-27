package com.netopsun.rxtxprotocol.snap_blue_light_gps_protocol;

import com.netopsun.rxtxprotocol.base.gps_receiver.DroneMsgCallback;
import com.netopsun.rxtxprotocol.base.gps_receiver.DroneStatusModel;

public class SnapReceiveDataAnalyzer {
  private DroneMsgCallback droneMsgCallback;
  
  private final DroneStatusModel droneStatusModel = new DroneStatusModel();
  
  private int lastRecordValue;
  
  private int lastTakePhotoValue;
  
  private final SnapPacketSplitter packetSplitter = new SnapPacketSplitter(new SnapPacketSplitter.Callback() {
        public void onPackage(byte[] param1ArrayOfbyte) {
          SnapReceiveDataAnalyzer.this.isDroneRealTimeMsg(param1ArrayOfbyte);
          SnapReceiveDataAnalyzer.this.isDroneBaseMsg(param1ArrayOfbyte);
          SnapReceiveDataAnalyzer.this.isGPSMsg(param1ArrayOfbyte);
          SnapReceiveDataAnalyzer.this.isWayPointNumMsg(param1ArrayOfbyte);
          SnapReceiveDataAnalyzer.this.isCMDMsg(param1ArrayOfbyte);
        }
      });
  
  private volatile int sendWayPointSuccessNum = -1;
  
  public static int byte2Int(byte[] paramArrayOfbyte, int paramInt) {
    byte b1 = paramArrayOfbyte[paramInt];
    byte b2 = paramArrayOfbyte[paramInt + 1];
    byte b3 = paramArrayOfbyte[paramInt + 2];
    return paramArrayOfbyte[paramInt + 3] & 0xFF | (b1 & 0xFF) << 24 | (b2 & 0xFF) << 16 | (b3 & 0xFF) << 8;
  }
  
  public static int byte2IntLittle(byte[] paramArrayOfbyte, int paramInt) {
    byte b1 = paramArrayOfbyte[paramInt + 3];
    byte b2 = paramArrayOfbyte[paramInt + 2];
    byte b3 = paramArrayOfbyte[paramInt + 1];
    return paramArrayOfbyte[paramInt] & 0xFF | (b1 & 0xFF) << 24 | (b2 & 0xFF) << 16 | (b3 & 0xFF) << 8;
  }
  
  private boolean isCMDMsg(byte[] paramArrayOfbyte) {
    if (paramArrayOfbyte[1] != -114)
      return false; 
    if (paramArrayOfbyte[3] == 1 && paramArrayOfbyte[4] == 0) {
      DroneMsgCallback droneMsgCallback = this.droneMsgCallback;
      if (droneMsgCallback != null)
        droneMsgCallback.didRecvTakePhotoCmd(); 
    } 
    if (paramArrayOfbyte[3] == 0 && paramArrayOfbyte[4] == 1) {
      DroneMsgCallback droneMsgCallback = this.droneMsgCallback;
      if (droneMsgCallback != null)
        droneMsgCallback.didRecvRecordStartCmd(); 
    } 
    if (paramArrayOfbyte[3] == 0 && paramArrayOfbyte[4] == 0) {
      DroneMsgCallback droneMsgCallback = this.droneMsgCallback;
      if (droneMsgCallback != null)
        droneMsgCallback.didRecvRecordStopCmd(); 
    } 
    return true;
  }
  
  private boolean isDroneBaseMsg(byte[] paramArrayOfbyte) {
    if (paramArrayOfbyte[1] != -118)
      return false; 
    DroneStatusModel droneStatusModel = this.droneStatusModel;
    droneStatusModel.droneMaxHigh = ((paramArrayOfbyte[12] & 0x3) << 8 | paramArrayOfbyte[11] & 0xFF);
    byte b = paramArrayOfbyte[13];
    droneStatusModel.droneMaxDistance = ((paramArrayOfbyte[12] & 0xFF) >> 2 | (b & 0x3F) << 6);
    b = paramArrayOfbyte[14];
    droneStatusModel.droneFlyBackAltitude = ((paramArrayOfbyte[13] & 0x3) >> 6 | (b & 0x3F) << 2);
    DroneMsgCallback droneMsgCallback = this.droneMsgCallback;
    if (droneMsgCallback != null)
      droneMsgCallback.droneStatusUpdate(droneStatusModel); 
    return true;
  }
  
  private boolean isDroneRealTimeMsg(byte[] paramArrayOfbyte) {
    byte b = paramArrayOfbyte[1];
    boolean bool = false;
    if (b != -117)
      return false; 
    DroneStatusModel droneStatusModel = this.droneStatusModel;
    droneStatusModel.rollAngle = ((paramArrayOfbyte[4] & 0x80) << 1 | paramArrayOfbyte[3] & 0xFF);
    droneStatusModel.pitchAngle = ((paramArrayOfbyte[4] & 0xFF) >> 1 | (paramArrayOfbyte[5] & 0x3) << 7);
    droneStatusModel.yawAngle = ((paramArrayOfbyte[5] & 0xFF) >> 2 | (paramArrayOfbyte[6] & 0x7) << 6);
    droneStatusModel.insInitOk = (paramArrayOfbyte[6] & 0xFF) >> 3 & 0x1;
    droneStatusModel.baroInitOk = (paramArrayOfbyte[6] & 0xFF) >> 4 & 0x1;
    droneStatusModel.magInitOk = (paramArrayOfbyte[6] & 0xFF) >> 5 & 0x1;
    droneStatusModel.gpsInitOk = (paramArrayOfbyte[6] & 0xFF) >> 6 & 0x1;
    droneStatusModel.flowInitOk = (paramArrayOfbyte[6] & 0xFF) >> 7 & 0x1;
    droneStatusModel.accelerationCalibration = paramArrayOfbyte[7] & 0x1;
    droneStatusModel.compassCalibrationX = (paramArrayOfbyte[7] & 0xFF) >> 1 & 0x1;
    droneStatusModel.compassCalibrationY = (paramArrayOfbyte[7] & 0xFF) >> 2 & 0x1;
    droneStatusModel.compassCalibrationProgress = (paramArrayOfbyte[7] & 0xFF) >> 3 | (paramArrayOfbyte[8] & 0x3) << 5;
    droneStatusModel.batteryVoltage = ((paramArrayOfbyte[8] & 0xFF) >> 2 | (paramArrayOfbyte[9] & 0x1) << 6);
    double d = droneStatusModel.batteryVoltage / 10.0D;
    if (d > 8.8D) {
      this.droneStatusModel.batteryLevel = 100;
    } else if (d < 6.8D) {
      this.droneStatusModel.batteryLevel = 0;
    } else {
      this.droneStatusModel.batteryLevel = (int)(15223.6333D - 6100.5569D * d + 805.7715D * d * d - 34.9797D * d * d * d);
    } 
    if (this.droneStatusModel.batteryLevel < 0) {
      this.droneStatusModel.batteryLevel = 0;
    } else if (this.droneStatusModel.batteryLevel > 100) {
      this.droneStatusModel.batteryLevel = 100;
    } 
    droneStatusModel = this.droneStatusModel;
    droneStatusModel.lowPowerWarning = (paramArrayOfbyte[9] & 0x6) >> 1;
    droneStatusModel.temperatureWarning = (paramArrayOfbyte[9] & 0x8) >> 3;
    if ((paramArrayOfbyte[9] & 0x30) >> 4 == 1)
      droneStatusModel.flyState = DroneStatusModel.FlyStateType.locked_rotor; 
    if ((paramArrayOfbyte[9] & 0x30) >> 4 == 2)
      this.droneStatusModel.flyState = DroneStatusModel.FlyStateType.Inclination_protect; 
    if ((paramArrayOfbyte[9] & 0xC0) >> 6 == 0)
      this.droneStatusModel.flyState = DroneStatusModel.FlyStateType.Locked; 
    if ((paramArrayOfbyte[9] & 0xC0) >> 6 == 1)
      this.droneStatusModel.flyState = DroneStatusModel.FlyStateType.UnlockedNotTakeOff; 
    if ((paramArrayOfbyte[9] & 0xC0) >> 6 == 2)
      this.droneStatusModel.flyState = DroneStatusModel.FlyStateType.UnlockedAndTakeOff; 
    if ((paramArrayOfbyte[10] & 0xF) == 0)
      this.droneStatusModel.flyMode = DroneStatusModel.FlyModeType.Stabilize; 
    if ((paramArrayOfbyte[10] & 0xF) == 1)
      this.droneStatusModel.flyMode = DroneStatusModel.FlyModeType.FlyBack; 
    if ((paramArrayOfbyte[10] & 0xF) == 2) {
      this.droneStatusModel.flyMode = DroneStatusModel.FlyModeType.FollowMe;
      DroneMsgCallback droneMsgCallback1 = this.droneMsgCallback;
      if (droneMsgCallback1 != null)
        droneMsgCallback1.recvFollowmeCommand(); 
    } 
    if ((paramArrayOfbyte[10] & 0xF) == 3) {
      this.droneStatusModel.flyMode = DroneStatusModel.FlyModeType.Surround;
      DroneMsgCallback droneMsgCallback1 = this.droneMsgCallback;
      if (droneMsgCallback1 != null)
        droneMsgCallback1.recvCircleCommand(); 
    } 
    if ((paramArrayOfbyte[10] & 0xF) == 4) {
      this.droneStatusModel.flyMode = DroneStatusModel.FlyModeType.RouteFlight;
      DroneMsgCallback droneMsgCallback1 = this.droneMsgCallback;
      if (droneMsgCallback1 != null)
        droneMsgCallback1.recvWaypointCommand(); 
    } 
    droneStatusModel = this.droneStatusModel;
    droneStatusModel.flyBackStatus = (paramArrayOfbyte[10] & 0x70) >> 4;
    droneStatusModel.satelliteNum = (paramArrayOfbyte[11] & 0xE0) >> 5 | (paramArrayOfbyte[12] & 0x3) << 3;
    if ((paramArrayOfbyte[12] & 0x4) >> 2 == 1)
      bool = true; 
    droneStatusModel.gpsFine = bool;
    droneStatusModel = this.droneStatusModel;
    droneStatusModel.gpsMode = (paramArrayOfbyte[13] & 0xC) >> 2;
    int i = (paramArrayOfbyte[14] & 0x7) << 4 | (paramArrayOfbyte[13] & 0xF0) >> 4;
    droneStatusModel.remoteBatteryVal = (i / 10.0F);
    droneStatusModel.remoteBatteryLevel = (i * 25 - 900);
    if (droneStatusModel.remoteBatteryLevel > 100.0D)
      this.droneStatusModel.remoteBatteryLevel = 100.0D; 
    if (this.droneStatusModel.remoteBatteryLevel < 0.0D)
      this.droneStatusModel.remoteBatteryLevel = 0.0D; 
    DroneMsgCallback droneMsgCallback = this.droneMsgCallback;
    if (droneMsgCallback != null)
      droneMsgCallback.droneStatusUpdate(this.droneStatusModel); 
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
  
  public void setSendWayPointSuccessNum(int paramInt) {
    this.sendWayPointSuccessNum = paramInt;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/rxtxprotocol/snap_blue_light_gps_protocol/SnapReceiveDataAnalyzer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */