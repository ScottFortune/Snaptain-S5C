package com.netopsun.rxtxprotocol.blue_light_gps_protocol;

import com.netopsun.rxtxprotocol.base.gps_receiver.DroneMsgCallback;
import com.netopsun.rxtxprotocol.base.gps_receiver.DroneStatusModel;

public class ReceiveDataAnalyzer {
  private DroneMsgCallback droneMsgCallback;
  
  private final DroneStatusModel droneStatusModel = new DroneStatusModel();
  
  private int lastRecordValue;
  
  private int lastTakePhotoValue;
  
  private final PacketSplitter packetSplitter = new PacketSplitter(new PacketSplitter.Callback() {
        public void onPackage(byte[] param1ArrayOfbyte) {
          ReceiveDataAnalyzer.this.isDroneMsg(param1ArrayOfbyte);
          ReceiveDataAnalyzer.this.isDroneTestMsg(param1ArrayOfbyte);
          ReceiveDataAnalyzer.this.isGPSMsg(param1ArrayOfbyte);
          ReceiveDataAnalyzer.this.isWayPointNumMsg(param1ArrayOfbyte);
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
    boolean bool;
    if (paramArrayOfbyte[1] != -117)
      return false; 
    DroneStatusModel droneStatusModel = this.droneStatusModel;
    droneStatusModel.rollAngle = (((paramArrayOfbyte[4] & 0x1) << 8 | paramArrayOfbyte[3] & 0xFF) - 180);
    droneStatusModel.pitchAngle = (((paramArrayOfbyte[4] & 0xFF) >> 1 | (paramArrayOfbyte[5] & 0x3) << 7) - 180);
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
    droneStatusModel.batteryLevel = (paramArrayOfbyte[8] & 0xFF) >> 2 | (paramArrayOfbyte[9] & 0x1) << 6;
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
    this.droneStatusModel.flyBackStatus = (paramArrayOfbyte[10] & 0x70) >> 4;
    int i = (paramArrayOfbyte[10] & 0x80) >> 7;
    if (i == 1 && this.lastTakePhotoValue != 1) {
      DroneMsgCallback droneMsgCallback1 = this.droneMsgCallback;
      if (droneMsgCallback1 != null)
        droneMsgCallback1.didRecvTakePhotoCmd(); 
    } 
    this.lastTakePhotoValue = i;
    i = paramArrayOfbyte[11] & 0x1;
    if (i == 1 && i != this.lastRecordValue) {
      DroneMsgCallback droneMsgCallback1 = this.droneMsgCallback;
      if (droneMsgCallback1 != null)
        if ((paramArrayOfbyte[11] & 0x2) >> 1 == 1) {
          droneMsgCallback1.didRecvRecordStartCmd();
        } else {
          droneMsgCallback1.didRecvRecordStopCmd();
        }  
    } 
    this.lastRecordValue = i;
    droneStatusModel = this.droneStatusModel;
    droneStatusModel.satelliteNum = (paramArrayOfbyte[11] & 0xE0) >> 5 | (paramArrayOfbyte[12] & 0x3) << 3;
    if ((paramArrayOfbyte[12] & 0x4) >> 2 == 1) {
      bool = true;
    } else {
      bool = false;
    } 
    droneStatusModel.gpsFine = bool;
    droneStatusModel = this.droneStatusModel;
    droneStatusModel.accuracy = ((paramArrayOfbyte[12] & 0x1F) >> 3 | (paramArrayOfbyte[13] & 0x3) << 5);
    droneStatusModel.gpsMode = (paramArrayOfbyte[13] & 0xC) >> 2;
    DroneMsgCallback droneMsgCallback = this.droneMsgCallback;
    if (droneMsgCallback != null)
      droneMsgCallback.droneStatusUpdate(droneStatusModel); 
    return true;
  }
  
  private boolean isDroneTestMsg(byte[] paramArrayOfbyte) {
    if (paramArrayOfbyte[1] != -115)
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
    this.droneStatusModel.testInfo.batVal = ((paramArrayOfbyte[22] & 0x7) << 8 | paramArrayOfbyte[21] & 0xFF) / 100.0F;
    this.droneStatusModel.testInfo.gpsNum = (paramArrayOfbyte[22] & 0xFF) >> 3;
    this.droneStatusModel.testInfo.roll = ((paramArrayOfbyte[24] & 0x1) << 8 | paramArrayOfbyte[23] & 0xFF) - 180;
    this.droneStatusModel.testInfo.pitch = ((paramArrayOfbyte[24] & 0xFF) >> 1 | (paramArrayOfbyte[25] & 0x3) << 7) - 180;
    this.droneStatusModel.testInfo.yaw = (paramArrayOfbyte[25] & 0xFF) >> 2 | (paramArrayOfbyte[26] & 0x7) << 6;
    this.droneStatusModel.testInfo.InsInitOk = (paramArrayOfbyte[26] & 0xFF) >> 3 & 0x1;
    this.droneStatusModel.testInfo.BaroInitOk = (paramArrayOfbyte[26] & 0xFF) >> 4 & 0x1;
    this.droneStatusModel.testInfo.MagInitOk = (paramArrayOfbyte[26] & 0xFF) >> 5 & 0x1;
    this.droneStatusModel.testInfo.GpsInitOk = (paramArrayOfbyte[26] & 0xFF) >> 6 & 0x1;
    this.droneStatusModel.testInfo.FlowInitOk = (paramArrayOfbyte[26] & 0xFF) >> 7 & 0x1;
    int i = byte2IntLittle(paramArrayOfbyte, 27);
    this.droneStatusModel.testInfo.AirplaneLon = (i / 1.0E7F);
    i = byte2IntLittle(paramArrayOfbyte, 31);
    this.droneStatusModel.testInfo.AirplaneLat = (i / 1.0E7F);
    this.droneStatusModel.testInfo.temperature = paramArrayOfbyte[35];
    this.droneStatusModel.testInfo.baro_alt = (((paramArrayOfbyte[37] & 0xF) << 8 | paramArrayOfbyte[36] & 0xFF) - 1000) / 10.0F;
    this.droneStatusModel.testInfo.GpsFine = (paramArrayOfbyte[37] & 0xFF) >> 4 & 0x1;
    DroneStatusModel.TestInfo testInfo = this.droneStatusModel.testInfo;
    i = paramArrayOfbyte[37];
    testInfo.GpsQuality = ((paramArrayOfbyte[38] & 0xFF) << 3 | (i & 0xFF) >> 5);
    this.droneStatusModel.testInfo.current1 = paramArrayOfbyte[39] & 0x3F;
    testInfo = this.droneStatusModel.testInfo;
    i = paramArrayOfbyte[39];
    testInfo.current2 = (paramArrayOfbyte[40] & 0xF) << 2 | (i & 0xFF) >> 6;
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
  
  public void setSendWayPointSuccessNum(int paramInt) {
    this.sendWayPointSuccessNum = paramInt;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/rxtxprotocol/blue_light_gps_protocol/ReceiveDataAnalyzer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */