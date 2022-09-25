package com.netopsun.rxtxprotocol.f200_gps_protocol;

import com.netopsun.rxtxprotocol.base.gps_receiver.DroneMsgCallback;
import com.netopsun.rxtxprotocol.base.gps_receiver.DroneStatusModel;

public class F200ReceiveDataAnalyzer {
  private DroneMsgCallback droneMsgCallback;
  
  private final DroneStatusModel droneStatusModel = new DroneStatusModel();
  
  private final F200PacketSplitter packetSplitter = new F200PacketSplitter(new F200PacketSplitter.Callback() {
        public void onPackage(byte[] param1ArrayOfbyte) {
          F200ReceiveDataAnalyzer.this.isDroneMsg(param1ArrayOfbyte);
          F200ReceiveDataAnalyzer.this.isGPSMsg(param1ArrayOfbyte);
          F200ReceiveDataAnalyzer.this.isCameraCMDMsg(param1ArrayOfbyte);
          F200ReceiveDataAnalyzer.this.isCalibrationResMsg(param1ArrayOfbyte);
        }
      });
  
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
  
  public static int byte2UInt16Little(byte[] paramArrayOfbyte, int paramInt) {
    byte b = paramArrayOfbyte[paramInt + 1];
    return paramArrayOfbyte[paramInt] & 0xFF | (b & 0xFF) << 8;
  }
  
  private boolean isCalibrationResMsg(byte[] paramArrayOfbyte) {
    // Byte code:
    //   0: aload_1
    //   1: iconst_4
    //   2: baload
    //   3: bipush #101
    //   5: if_icmpeq -> 10
    //   8: iconst_0
    //   9: ireturn
    //   10: aload_0
    //   11: getfield droneMsgCallback : Lcom/netopsun/rxtxprotocol/base/gps_receiver/DroneMsgCallback;
    //   14: ifnull -> 220
    //   17: aload_1
    //   18: iconst_5
    //   19: baload
    //   20: sipush #255
    //   23: iand
    //   24: sipush #228
    //   27: if_icmpeq -> 186
    //   30: aload_1
    //   31: iconst_5
    //   32: baload
    //   33: sipush #255
    //   36: iand
    //   37: sipush #225
    //   40: if_icmpne -> 46
    //   43: goto -> 186
    //   46: aload_1
    //   47: iconst_5
    //   48: baload
    //   49: sipush #255
    //   52: iand
    //   53: sipush #229
    //   56: if_icmpne -> 83
    //   59: aload_0
    //   60: getfield droneStatusModel : Lcom/netopsun/rxtxprotocol/base/gps_receiver/DroneStatusModel;
    //   63: astore_1
    //   64: aload_1
    //   65: bipush #50
    //   67: putfield compassCalibrationProgress : I
    //   70: aload_1
    //   71: iconst_1
    //   72: putfield compassCalibrationX : I
    //   75: aload_1
    //   76: iconst_0
    //   77: putfield compassCalibrationY : I
    //   80: goto -> 207
    //   83: aload_1
    //   84: iconst_5
    //   85: baload
    //   86: sipush #255
    //   89: iand
    //   90: sipush #226
    //   93: if_icmpne -> 120
    //   96: aload_0
    //   97: getfield droneStatusModel : Lcom/netopsun/rxtxprotocol/base/gps_receiver/DroneStatusModel;
    //   100: astore_1
    //   101: aload_1
    //   102: bipush #100
    //   104: putfield compassCalibrationProgress : I
    //   107: aload_1
    //   108: iconst_1
    //   109: putfield compassCalibrationX : I
    //   112: aload_1
    //   113: iconst_1
    //   114: putfield compassCalibrationY : I
    //   117: goto -> 207
    //   120: aload_1
    //   121: iconst_5
    //   122: baload
    //   123: sipush #255
    //   126: iand
    //   127: sipush #227
    //   130: if_icmpne -> 163
    //   133: aload_0
    //   134: getfield droneStatusModel : Lcom/netopsun/rxtxprotocol/base/gps_receiver/DroneStatusModel;
    //   137: astore_1
    //   138: aload_1
    //   139: iconst_1
    //   140: putfield compassCalibrationX : I
    //   143: aload_1
    //   144: iconst_1
    //   145: putfield compassCalibrationY : I
    //   148: aload_1
    //   149: iconst_0
    //   150: putfield compassCalibrationProgress : I
    //   153: aload_1
    //   154: getstatic com/netopsun/rxtxprotocol/base/gps_receiver/DroneStatusModel$FlyStateType.locked_rotor : Lcom/netopsun/rxtxprotocol/base/gps_receiver/DroneStatusModel$FlyStateType;
    //   157: putfield flyState : Lcom/netopsun/rxtxprotocol/base/gps_receiver/DroneStatusModel$FlyStateType;
    //   160: goto -> 207
    //   163: aload_0
    //   164: getfield droneStatusModel : Lcom/netopsun/rxtxprotocol/base/gps_receiver/DroneStatusModel;
    //   167: astore_1
    //   168: aload_1
    //   169: iconst_0
    //   170: putfield compassCalibrationProgress : I
    //   173: aload_1
    //   174: iconst_1
    //   175: putfield compassCalibrationX : I
    //   178: aload_1
    //   179: iconst_1
    //   180: putfield compassCalibrationY : I
    //   183: goto -> 207
    //   186: aload_0
    //   187: getfield droneStatusModel : Lcom/netopsun/rxtxprotocol/base/gps_receiver/DroneStatusModel;
    //   190: astore_1
    //   191: aload_1
    //   192: bipush #10
    //   194: putfield compassCalibrationProgress : I
    //   197: aload_1
    //   198: iconst_0
    //   199: putfield compassCalibrationX : I
    //   202: aload_1
    //   203: iconst_0
    //   204: putfield compassCalibrationY : I
    //   207: aload_0
    //   208: getfield droneMsgCallback : Lcom/netopsun/rxtxprotocol/base/gps_receiver/DroneMsgCallback;
    //   211: aload_0
    //   212: getfield droneStatusModel : Lcom/netopsun/rxtxprotocol/base/gps_receiver/DroneStatusModel;
    //   215: invokeinterface droneStatusUpdate : (Lcom/netopsun/rxtxprotocol/base/gps_receiver/DroneStatusModel;)V
    //   220: iconst_1
    //   221: ireturn
  }
  
  private boolean isCameraCMDMsg(byte[] paramArrayOfbyte) {
    if (paramArrayOfbyte[4] != 108)
      return false; 
    DroneMsgCallback droneMsgCallback = this.droneMsgCallback;
    if (droneMsgCallback != null) {
      int i = paramArrayOfbyte[5] & 0xFF;
      if (i != 1) {
        if (i != 2) {
          if (i == 3)
            droneMsgCallback.didRecvTakePhotoCmd(); 
        } else {
          droneMsgCallback.didRecvRecordStopCmd();
        } 
      } else {
        droneMsgCallback.didRecvRecordStartCmd();
      } 
    } 
    return true;
  }
  
  private boolean isDroneMsg(byte[] paramArrayOfbyte) {
    if (paramArrayOfbyte[4] != 102)
      return false; 
    this.droneStatusModel.rollAngle = (byte2Int16Little(paramArrayOfbyte, 5) / 10.0F);
    this.droneStatusModel.pitchAngle = (byte2Int16Little(paramArrayOfbyte, 7) / 10.0F);
    this.droneStatusModel.yawAngle = (byte2Int16Little(paramArrayOfbyte, 9) / 10.0F);
    int i = ((paramArrayOfbyte[11] & 0xF0) >> 4) - 1;
    if (i >= 0 && i < (DroneStatusModel.FlyModeType.values()).length) {
      this.droneStatusModel.flyMode = DroneStatusModel.FlyModeType.values()[i];
    } else if ((paramArrayOfbyte[11] & 0xF0) >> 4 == 15) {
      this.droneStatusModel.flyMode = DroneStatusModel.FlyModeType.Stabilize;
    } else {
      this.droneStatusModel.flyMode = DroneStatusModel.FlyModeType.UnsupportedFun;
    } 
    i = paramArrayOfbyte[11] & 0xF;
    if (i >= 0 && i < (DroneStatusModel.FlyStateType.values()).length) {
      this.droneStatusModel.flyState = DroneStatusModel.FlyStateType.values()[i];
    } else {
      this.droneStatusModel.flyState = DroneStatusModel.FlyStateType.UnsupportedFun;
    } 
    DroneMsgCallback droneMsgCallback = this.droneMsgCallback;
    if (droneMsgCallback != null)
      droneMsgCallback.droneStatusUpdate(this.droneStatusModel); 
    return true;
  }
  
  private boolean isGPSMsg(byte[] paramArrayOfbyte) {
    boolean bool;
    if (paramArrayOfbyte[4] != 103)
      return false; 
    this.droneStatusModel.homeDistance = (byte2UInt16Little(paramArrayOfbyte, 5) / 10.0F);
    this.droneStatusModel.speedH = (byte2UInt16Little(paramArrayOfbyte, 7) / 10.0F);
    this.droneStatusModel.altitude = (byte2Int16Little(paramArrayOfbyte, 9) / 10.0F);
    this.droneStatusModel.speedV = (byte2Int16Little(paramArrayOfbyte, 11) / 10.0F);
    DroneStatusModel droneStatusModel = this.droneStatusModel;
    droneStatusModel.satelliteNum = paramArrayOfbyte[13] & 0xFF;
    if ((paramArrayOfbyte[14] & 0xFF) == 1) {
      bool = true;
    } else {
      bool = false;
    } 
    droneStatusModel.gpsFine = bool;
    if (this.droneStatusModel.gpsFine) {
      this.droneStatusModel.gpsMode = 1;
    } else {
      this.droneStatusModel.gpsMode = 0;
    } 
    int i = byte2IntLittle(paramArrayOfbyte, 15);
    this.droneStatusModel.droneLat = (i / 1.0E7F);
    i = byte2IntLittle(paramArrayOfbyte, 19);
    droneStatusModel = this.droneStatusModel;
    droneStatusModel.droneLng = (i / 1.0E7F);
    droneStatusModel.batteryLevel = paramArrayOfbyte[23] & 0xFF;
    DroneMsgCallback droneMsgCallback = this.droneMsgCallback;
    if (droneMsgCallback != null)
      droneMsgCallback.droneStatusUpdate(droneStatusModel); 
    return true;
  }
  
  public void parseData(byte[] paramArrayOfbyte, int paramInt) {
    for (byte b = 0; b < paramInt; b++)
      this.packetSplitter.onByte(paramArrayOfbyte[b]); 
  }
  
  public void setDroneMsgCallback(DroneMsgCallback paramDroneMsgCallback) {
    this.droneMsgCallback = paramDroneMsgCallback;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/rxtxprotocol/f200_gps_protocol/F200ReceiveDataAnalyzer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */