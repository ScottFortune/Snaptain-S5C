package com.netopsun.rxtxprotocol.dfs_gps_protocol;

import com.netopsun.rxtxprotocol.base.gps_receiver.DroneMsgCallback;
import com.netopsun.rxtxprotocol.base.gps_receiver.DroneStatusModel;

public class DFSReceiveDataAnalyzer {
  private DroneMsgCallback droneMsgCallback;
  
  private final DroneStatusModel droneStatusModel = new DroneStatusModel();
  
  private int lastRecordValue;
  
  private final DFSPacketSplitter packetSplitter = new DFSPacketSplitter(new DFSPacketSplitter.Callback() {
        public void onPackage(byte[] param1ArrayOfbyte) {
          DFSReceiveDataAnalyzer.this.isDroneMsg(param1ArrayOfbyte);
          DFSReceiveDataAnalyzer.this.isDroneTestMsg(param1ArrayOfbyte);
          DFSReceiveDataAnalyzer.this.isRemoteCMDMsg(param1ArrayOfbyte);
          DFSReceiveDataAnalyzer.this.isWayPointNumMsg(param1ArrayOfbyte);
        }
      });
  
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
    boolean bool2;
    byte b = paramArrayOfbyte[2];
    boolean bool1 = false;
    if (b != 29)
      return false; 
    DroneStatusModel droneStatusModel = this.droneStatusModel;
    droneStatusModel.pitchAngle = paramArrayOfbyte[4];
    droneStatusModel.rollAngle = paramArrayOfbyte[5];
    droneStatusModel.yawAngle = (short)(paramArrayOfbyte[18] & 0xFF | (paramArrayOfbyte[19] & 0xFF) << 8);
    droneStatusModel.speedH = ((paramArrayOfbyte[6] & 0xFF) / 10.0F);
    droneStatusModel.batteryVoltage = ((paramArrayOfbyte[7] & 0xFF) / 10.0F);
    droneStatusModel.speedV = (paramArrayOfbyte[9] / 10.0F);
    droneStatusModel.accuracy = ((paramArrayOfbyte[10] & 0xFF) / 10.0F);
    droneStatusModel.satelliteNum = (paramArrayOfbyte[11] & 0xF0) >> 4;
    if (droneStatusModel.satelliteNum > 9) {
      bool2 = true;
    } else {
      bool2 = false;
    } 
    droneStatusModel.gpsFine = bool2;
    droneStatusModel = this.droneStatusModel;
    droneStatusModel.remoteRssi = (paramArrayOfbyte[11] & 0xF);
    if ((paramArrayOfbyte[12] & 0xF) == 7)
      droneStatusModel.flyMode = DroneStatusModel.FlyModeType.FlyBack; 
    if ((paramArrayOfbyte[12] & 0xF) == 5)
      this.droneStatusModel.flyMode = DroneStatusModel.FlyModeType.HeadLess; 
    if ((paramArrayOfbyte[12] & 0xF) == 3) {
      this.droneStatusModel.flyMode = DroneStatusModel.FlyModeType.GPS;
      this.droneStatusModel.gpsMode = 1;
    } 
    if ((paramArrayOfbyte[12] & 0xF) == 2) {
      this.droneStatusModel.flyMode = DroneStatusModel.FlyModeType.Stabilize;
      this.droneStatusModel.gpsMode = 0;
    } 
    if ((paramArrayOfbyte[12] & 0xF) == 1)
      this.droneStatusModel.flyMode = DroneStatusModel.FlyModeType.Posture; 
    if ((paramArrayOfbyte[12] & 0xF) == 0)
      this.droneStatusModel.flyMode = DroneStatusModel.FlyModeType.Manual; 
    if ((paramArrayOfbyte[13] & 0x60) >> 5 == 3) {
      this.droneStatusModel.flyMode = DroneStatusModel.FlyModeType.FollowMe;
      DroneMsgCallback droneMsgCallback1 = this.droneMsgCallback;
      if (droneMsgCallback1 != null)
        droneMsgCallback1.recvFollowmeCommand(); 
    } 
    if ((paramArrayOfbyte[13] & 0x60) >> 5 == 0) {
      this.droneStatusModel.flyMode = DroneStatusModel.FlyModeType.RouteFlight;
      DroneMsgCallback droneMsgCallback1 = this.droneMsgCallback;
      if (droneMsgCallback1 != null)
        droneMsgCallback1.recvWaypointCommand(); 
    } 
    if ((paramArrayOfbyte[37] & 0x1) == 1) {
      this.droneStatusModel.flyMode = DroneStatusModel.FlyModeType.Surround;
      DroneMsgCallback droneMsgCallback1 = this.droneMsgCallback;
      if (droneMsgCallback1 != null)
        droneMsgCallback1.recvCircleCommand(); 
    } 
    if ((paramArrayOfbyte[12] & 0x10) >> 4 == 0) {
      this.droneStatusModel.flyState = DroneStatusModel.FlyStateType.Locked;
    } else {
      this.droneStatusModel.flyState = DroneStatusModel.FlyStateType.UnlockedAndTakeOff;
    } 
    droneStatusModel = this.droneStatusModel;
    droneStatusModel.reqCalibraCompass = (paramArrayOfbyte[12] & 0x40) >> 6;
    droneStatusModel.lowPowerWarning = (paramArrayOfbyte[12] & 0x80) >> 7;
    droneStatusModel.compassCalibrationX = 1;
    droneStatusModel.compassCalibrationY = 1;
    droneStatusModel.compassCalibrationProgress = 100;
    if ((paramArrayOfbyte[13] & 0x3) == 1) {
      droneStatusModel.compassCalibrationX = 0;
      droneStatusModel.compassCalibrationY = 0;
      droneStatusModel.compassCalibrationProgress = 25;
    } 
    if ((paramArrayOfbyte[13] & 0x3) == 2) {
      droneStatusModel = this.droneStatusModel;
      droneStatusModel.compassCalibrationX = 1;
      droneStatusModel.compassCalibrationY = 0;
      droneStatusModel.compassCalibrationProgress = 75;
    } 
    droneStatusModel = this.droneStatusModel;
    droneStatusModel.accelerationCalibration = (paramArrayOfbyte[13] & 0x80) >> 7;
    droneStatusModel.altitude = ((short)(paramArrayOfbyte[14] & 0xFF | (paramArrayOfbyte[15] & 0xFF) << 8) / 10.0F);
    droneStatusModel.homeDistance = (paramArrayOfbyte[20] & 0xFF | (paramArrayOfbyte[21] & 0xFF) << 8);
    int i = byte2IntLittle(paramArrayOfbyte, 28);
    this.droneStatusModel.droneLng = (i / 1.0E7F);
    i = byte2IntLittle(paramArrayOfbyte, 32);
    droneStatusModel = this.droneStatusModel;
    droneStatusModel.droneLat = (i / 1.0E7F);
    if ((paramArrayOfbyte[36] & 0x1) == 0) {
      i = 1;
    } else {
      i = 0;
    } 
    droneStatusModel.insInitOk = i;
    droneStatusModel = this.droneStatusModel;
    if (((paramArrayOfbyte[36] & 0xFF) >> 1 & 0x1) == 0) {
      i = 1;
    } else {
      i = 0;
    } 
    droneStatusModel.baroInitOk = i;
    droneStatusModel = this.droneStatusModel;
    if (((paramArrayOfbyte[36] & 0xFF) >> 2 & 0x1) == 0) {
      i = 1;
    } else {
      i = 0;
    } 
    droneStatusModel.magInitOk = i;
    droneStatusModel = this.droneStatusModel;
    if (((paramArrayOfbyte[36] & 0xFF) >> 3 & 0x1) == 0) {
      i = 1;
    } else {
      i = 0;
    } 
    droneStatusModel.flowInitOk = i;
    droneStatusModel = this.droneStatusModel;
    if (((paramArrayOfbyte[36] & 0xFF) >> 4 & 0x1) == 0) {
      i = 1;
    } else {
      i = 0;
    } 
    droneStatusModel.gpsInitOk = i;
    droneStatusModel = this.droneStatusModel;
    i = bool1;
    if (((paramArrayOfbyte[36] & 0xFF) >> 5 & 0x1) == 1)
      i = 5; 
    droneStatusModel.flyBackStatus = i;
    if (((paramArrayOfbyte[36] & 0xFF) >> 7 & 0x1) == 1)
      this.droneStatusModel.flyMode = DroneStatusModel.FlyModeType.FlyBack; 
    if ((paramArrayOfbyte[37] >> 1 & 0x1) == 1)
      this.droneStatusModel.lowPowerWarning = 2; 
    DroneMsgCallback droneMsgCallback = this.droneMsgCallback;
    if (droneMsgCallback != null)
      droneMsgCallback.droneStatusUpdate(this.droneStatusModel); 
    return true;
  }
  
  private boolean isDroneTestMsg(byte[] paramArrayOfbyte) {
    if (paramArrayOfbyte[2] != 9)
      return false; 
    short s = byte2Int16Little(paramArrayOfbyte, 4);
    this.droneStatusModel.testInfo.gyro_x = s;
    s = byte2Int16Little(paramArrayOfbyte, 6);
    this.droneStatusModel.testInfo.gyro_y = s;
    s = byte2Int16Little(paramArrayOfbyte, 8);
    this.droneStatusModel.testInfo.gyro_z = s;
    s = byte2Int16Little(paramArrayOfbyte, 10);
    this.droneStatusModel.testInfo.acc_x = s;
    s = byte2Int16Little(paramArrayOfbyte, 12);
    this.droneStatusModel.testInfo.acc_y = s;
    s = byte2Int16Little(paramArrayOfbyte, 14);
    this.droneStatusModel.testInfo.acc_z = s;
    s = byte2Int16Little(paramArrayOfbyte, 16);
    this.droneStatusModel.testInfo.mag_x = s;
    s = byte2Int16Little(paramArrayOfbyte, 18);
    this.droneStatusModel.testInfo.mag_y = s;
    s = byte2Int16Little(paramArrayOfbyte, 20);
    this.droneStatusModel.testInfo.mag_z = s;
    s = byte2Int16Little(paramArrayOfbyte, 22);
    this.droneStatusModel.testInfo.baro_alt = s / 100.0F;
    s = byte2Int16Little(paramArrayOfbyte, 24);
    this.droneStatusModel.testInfo.temperature = s / 10;
    s = byte2Int16Little(paramArrayOfbyte, 26);
    this.droneStatusModel.testInfo.pitch = s;
    s = byte2Int16Little(paramArrayOfbyte, 28);
    this.droneStatusModel.testInfo.roll = s;
    s = byte2Int16Little(paramArrayOfbyte, 30);
    this.droneStatusModel.testInfo.yaw = s;
    this.droneStatusModel.testInfo.InsInitOk = this.droneStatusModel.insInitOk;
    this.droneStatusModel.testInfo.BaroInitOk = this.droneStatusModel.baroInitOk;
    this.droneStatusModel.testInfo.MagInitOk = this.droneStatusModel.magInitOk;
    this.droneStatusModel.testInfo.GpsInitOk = this.droneStatusModel.gpsInitOk;
    this.droneStatusModel.testInfo.FlowInitOk = this.droneStatusModel.flowInitOk;
    this.droneStatusModel.testInfo.gpsNum = this.droneStatusModel.satelliteNum;
    this.droneStatusModel.testInfo.AirplaneLat = this.droneStatusModel.droneLat;
    this.droneStatusModel.testInfo.AirplaneLon = this.droneStatusModel.droneLng;
    this.droneStatusModel.testInfo.GpsQuality = (float)this.droneStatusModel.accuracy;
    this.droneStatusModel.testInfo.batVal = (float)this.droneStatusModel.batteryVoltage;
    this.droneStatusModel.testInfo.GpsFine = this.droneStatusModel.gpsFine;
    DroneMsgCallback droneMsgCallback = this.droneMsgCallback;
    if (droneMsgCallback != null)
      droneMsgCallback.droneTestInfoUpdate(this.droneStatusModel); 
    return true;
  }
  
  private boolean isRemoteCMDMsg(byte[] paramArrayOfbyte) {
    if (paramArrayOfbyte[2] != 40)
      return false; 
    if ((paramArrayOfbyte[4] & 0xFF) == 1) {
      DroneMsgCallback droneMsgCallback = this.droneMsgCallback;
      if (droneMsgCallback != null)
        droneMsgCallback.didRecvTakePhotoCmd(); 
    } 
    int i = paramArrayOfbyte[4] & 0xFF;
    if (i != this.lastRecordValue) {
      DroneMsgCallback droneMsgCallback = this.droneMsgCallback;
      if (droneMsgCallback != null)
        if (i == 2) {
          droneMsgCallback.didRecvRecordStartCmd();
        } else if (i == 0) {
          droneMsgCallback.didRecvRecordStopCmd();
        }  
    } 
    this.lastRecordValue = i;
    return true;
  }
  
  private boolean isWayPointNumMsg(byte[] paramArrayOfbyte) {
    if (paramArrayOfbyte[2] != 34)
      return false; 
    this.sendWayPointSuccessNum = paramArrayOfbyte[4] & 0xFF;
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


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/rxtxprotocol/dfs_gps_protocol/DFSReceiveDataAnalyzer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */