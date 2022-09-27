package com.netopsun.dronectrl.F200Ctrl;

import android.util.Log;
import com.netopsun.dronectrl.F200Ctrl.F200RxModel.RxCalibrationStatusModel;
import com.netopsun.dronectrl.F200Ctrl.F200RxModel.RxDroneStatusModel;
import com.netopsun.dronectrl.F200Ctrl.F200RxModel.RxGpsStatusModel;
import com.netopsun.dronectrl.F200Ctrl.F200RxModel.RxWaypointConfigModel;
import com.netopsun.dronectrl.F200Ctrl.F200TxModel.F200TxModel;
import com.netopsun.dronectrl.F200Ctrl.F200TxModel.TxCalibrationModel;
import com.netopsun.dronectrl.F200Ctrl.F200TxModel.TxFollowmeModel;
import com.netopsun.dronectrl.F200Ctrl.F200TxModel.TxGpsCircleModel;
import com.netopsun.dronectrl.F200Ctrl.F200TxModel.TxOneKeyFunModel;
import com.netopsun.dronectrl.F200Ctrl.F200TxModel.TxRockerModel;
import com.netopsun.dronectrl.F200Ctrl.F200TxModel.TxWaypointConfigModel;
import com.netopsun.dronectrl.F200Ctrl.F200TxModel.TxWaypointGroupModel;
import com.netopsun.dronectrl.F200Ctrl.F200TxModel.TxWaypointModel;
import com.netopsun.dronectrl.LGBCtrl.DroneStatusModel;
import com.netopsun.dronectrl.LGBCtrl.LGBCtrl;
import com.netopsun.dronectrl.LGBCtrl.LGBCtrlDelegate;
import com.netopsun.dronectrl.LGBCtrl.LGBResultCallBack;
import com.netopsun.dronectrl.LGBCtrl.RxModel;
import com.netopsun.dronectrl.LGBCtrl.RxModelChangeCallBack;
import com.netopsun.dronectrl.LGBCtrl.WayPoint;
import com.vilyever.socketclient.SocketClient;
import com.vilyever.socketclient.helper.SocketClientAddress;
import com.vilyever.socketclient.helper.SocketClientDelegate;
import com.vilyever.socketclient.helper.SocketPacketHelper;
import com.vilyever.socketclient.helper.SocketResponsePacket;
import java.io.UnsupportedEncodingException;

public class F200Ctrl extends LGBCtrl {
  private static F200Ctrl f200Ctrl;
  
  private int cameraPositionInt = 1500;
  
  private DroneStatusModel droneStatusModel = new DroneStatusModel();
  
  private F200CtrlRxPacket f200CtrlRxPacket = new F200CtrlRxPacket();
  
  private byte[] recvFrameHead;
  
  private RxCalibrationStatusModel rxCalibrationStatusModel = new RxCalibrationStatusModel();
  
  private RxDroneStatusModel rxDroneStatusModel = new RxDroneStatusModel();
  
  private RxGpsStatusModel rxGpsStatusModel = new RxGpsStatusModel();
  
  public RxWaypointConfigModel rxWaypointConfigModel = new RxWaypointConfigModel();
  
  private SocketClient socketClient;
  
  F200Ctrl() {
    this.rxDroneStatusModel.setCallBack(new RxModelChangeCallBack() {
          public void callback(RxModel param1RxModel) {
            if (F200Ctrl.this.ctrlDelegate != null) {
              RxDroneStatusModel rxDroneStatusModel = (RxDroneStatusModel)param1RxModel;
              F200Ctrl.this.droneStatusModel.rollAngle = rxDroneStatusModel.getRollAngle();
              F200Ctrl.this.droneStatusModel.pitchAngle = rxDroneStatusModel.getPitchAngle();
              F200Ctrl.this.droneStatusModel.yawAngle = rxDroneStatusModel.getYawAngle();
              F200Ctrl.this.droneStatusModel.flyMode = rxDroneStatusModel.getFlyMode();
              F200Ctrl.this.droneStatusModel.flyState = rxDroneStatusModel.getFlyState();
              F200Ctrl.this.ctrlDelegate.droneStatusUpdate(F200Ctrl.this.droneStatusModel);
            } 
          }
        });
    this.rxGpsStatusModel.setCallBack(new RxModelChangeCallBack() {
          public void callback(RxModel param1RxModel) {
            if (F200Ctrl.this.ctrlDelegate != null) {
              RxGpsStatusModel rxGpsStatusModel = (RxGpsStatusModel)param1RxModel;
              F200Ctrl.this.droneStatusModel.altitude = rxGpsStatusModel.getVDistance();
              F200Ctrl.this.droneStatusModel.batteryLevel = rxGpsStatusModel.getBatteryLevel();
              F200Ctrl.this.droneStatusModel.droneLat = rxGpsStatusModel.getAircraftGpsLat();
              F200Ctrl.this.droneStatusModel.droneLng = rxGpsStatusModel.getAircraftGpsLon();
              F200Ctrl.this.droneStatusModel.homeDistance = rxGpsStatusModel.getHDistance();
              F200Ctrl.this.droneStatusModel.speedH = rxGpsStatusModel.getHVelocity();
              F200Ctrl.this.droneStatusModel.speedV = rxGpsStatusModel.getVVelocity();
              F200Ctrl.this.droneStatusModel.satelliteNum = rxGpsStatusModel.getSatelliteNum();
              F200Ctrl.this.ctrlDelegate.droneStatusUpdate(F200Ctrl.this.droneStatusModel);
            } 
          }
        });
    this.rxCalibrationStatusModel.setCallBack(new RxModelChangeCallBack() {
          public void callback(RxModel param1RxModel) {
            if (F200Ctrl.this.ctrlDelegate != null) {
              RxCalibrationStatusModel rxCalibrationStatusModel = (RxCalibrationStatusModel)param1RxModel;
              F200Ctrl.this.droneStatusModel.calibrationState = rxCalibrationStatusModel.calibrationState;
              F200Ctrl.this.ctrlDelegate.droneStatusUpdate(F200Ctrl.this.droneStatusModel);
            } 
          }
        });
  }
  
  private void sendOneKeyFunData(TxOneKeyFunModel.OneKeyFunCommand paramOneKeyFunCommand) {
    TxOneKeyFunModel txOneKeyFunModel = new TxOneKeyFunModel(paramOneKeyFunCommand);
    txOneKeyFunModel.getRawData();
    byte[] arrayOfByte = (new F200CtrlTxPacket((F200TxModel)txOneKeyFunModel)).getRawData();
    this.socketClient.sendData(arrayOfByte);
  }
  
  public static F200Ctrl shareF200Ctrl() {
    // Byte code:
    //   0: ldc com/netopsun/dronectrl/F200Ctrl/F200Ctrl
    //   2: monitorenter
    //   3: getstatic com/netopsun/dronectrl/F200Ctrl/F200Ctrl.f200Ctrl : Lcom/netopsun/dronectrl/F200Ctrl/F200Ctrl;
    //   6: ifnonnull -> 21
    //   9: new com/netopsun/dronectrl/F200Ctrl/F200Ctrl
    //   12: astore_0
    //   13: aload_0
    //   14: invokespecial <init> : ()V
    //   17: aload_0
    //   18: putstatic com/netopsun/dronectrl/F200Ctrl/F200Ctrl.f200Ctrl : Lcom/netopsun/dronectrl/F200Ctrl/F200Ctrl;
    //   21: getstatic com/netopsun/dronectrl/F200Ctrl/F200Ctrl.f200Ctrl : Lcom/netopsun/dronectrl/F200Ctrl/F200Ctrl;
    //   24: astore_0
    //   25: ldc com/netopsun/dronectrl/F200Ctrl/F200Ctrl
    //   27: monitorexit
    //   28: aload_0
    //   29: areturn
    //   30: astore_0
    //   31: ldc com/netopsun/dronectrl/F200Ctrl/F200Ctrl
    //   33: monitorexit
    //   34: aload_0
    //   35: athrow
    // Exception table:
    //   from	to	target	type
    //   3	21	30	finally
    //   21	25	30	finally
  }
  
  public void connect(String paramString, int paramInt) {
    if (this.socketClient == null) {
      this.socketClient = new SocketClient(new SocketClientAddress(paramString, paramInt));
      this.socketClient.setCharsetName("UTF-8");
      this.socketClient.getSocketPacketHelper().setReadStrategy(SocketPacketHelper.ReadStrategy.AutoReadByLength);
      this.socketClient.getSocketPacketHelper().setReceiveHeaderData(getRecvFrameHead());
      this.socketClient.getSocketPacketHelper().setReceivePacketLengthDataLength(1);
      this.socketClient.getSocketPacketHelper().setReceivePacketDataLengthConvertor(new SocketPacketHelper.ReceivePacketDataLengthConvertor() {
            public int obtainReceivePacketDataLength(SocketPacketHelper param1SocketPacketHelper, byte[] param1ArrayOfbyte) {
              return param1ArrayOfbyte[0] + 2;
            }
          });
      this.socketClient.getSocketPacketHelper().setReceiveTrailerData(null);
      F200CtrlTxPacket f200CtrlTxPacket1 = new F200CtrlTxPacket(F200CtrlMesgId.MSP_GPS_DISPLAY_NAVIGATION);
      F200CtrlTxPacket f200CtrlTxPacket2 = new F200CtrlTxPacket(F200CtrlMesgId.MSP_AHRS_DISPLAY);
      byte[] arrayOfByte1 = f200CtrlTxPacket1.getRawData();
      byte[] arrayOfByte2 = f200CtrlTxPacket2.getRawData();
      if (arrayOfByte1 != null && arrayOfByte2 != null) {
        byte[] arrayOfByte = new byte[arrayOfByte1.length + arrayOfByte2.length];
        System.arraycopy(arrayOfByte1, 0, arrayOfByte, 0, arrayOfByte1.length);
        System.arraycopy(arrayOfByte2, 0, arrayOfByte, arrayOfByte1.length, arrayOfByte2.length);
        this.socketClient.getHeartBeatHelper().setDefaultSendData(arrayOfByte);
        this.socketClient.getHeartBeatHelper().setHeartBeatInterval(500L);
        this.socketClient.getHeartBeatHelper().setSendHeartBeatEnabled(true);
        this.socketClient.registerSocketClientDelegate(new SocketClientDelegate() {
              public void onConnected(SocketClient param1SocketClient) {
                if (F200Ctrl.this.ctrlDelegate != null)
                  F200Ctrl.this.ctrlDelegate.didConnected(); 
                Log.i("GX", "onConnected");
              }
              
              public void onDisconnected(SocketClient param1SocketClient) {
                if (F200Ctrl.this.ctrlDelegate != null)
                  F200Ctrl.this.ctrlDelegate.disConnected(); 
                Log.i("GX", "onDisconnected");
              }
              
              public void onResponse(SocketClient param1SocketClient, SocketResponsePacket param1SocketResponsePacket) {
                byte[] arrayOfByte2 = param1SocketResponsePacket.getHeaderData();
                byte[] arrayOfByte1 = param1SocketResponsePacket.getData();
                byte[] arrayOfByte3 = param1SocketResponsePacket.getPacketLengthData();
                if (arrayOfByte2 != null && arrayOfByte1 != null && arrayOfByte3 != null) {
                  arrayOfByte1 = new byte[arrayOfByte2.length + arrayOfByte1.length + arrayOfByte3.length];
                  System.arraycopy(param1SocketResponsePacket.getHeaderData(), 0, arrayOfByte1, 0, (param1SocketResponsePacket.getHeaderData()).length);
                  System.arraycopy(param1SocketResponsePacket.getPacketLengthData(), 0, arrayOfByte1, (param1SocketResponsePacket.getHeaderData()).length, (param1SocketResponsePacket.getPacketLengthData()).length);
                  System.arraycopy(param1SocketResponsePacket.getData(), 0, arrayOfByte1, (param1SocketResponsePacket.getHeaderData()).length + (param1SocketResponsePacket.getPacketLengthData()).length, (param1SocketResponsePacket.getData()).length);
                  F200Ctrl.this.didReadOneFrameData(arrayOfByte1);
                } 
              }
            });
      } else {
        Log.e("GX", "set heart error!!!");
        return;
      } 
    } 
    if (!this.socketClient.isConnected())
      this.socketClient.connect(); 
  }
  
  protected void didReadOneFrameData(byte[] paramArrayOfbyte) {
    if (!this.f200CtrlRxPacket.refreshModelData(paramArrayOfbyte))
      return; 
    int i = null.$SwitchMap$com$netopsun$dronectrl$F200Ctrl$F200CtrlMesgId[this.f200CtrlRxPacket.f200CtrlMesgId.ordinal()];
    if (i != 1)
      if (i != 2) {
        if (i != 3) {
          if (i == 4)
            this.rxGpsStatusModel.refreshWithRawData(this.f200CtrlRxPacket.contentData); 
        } else {
          this.rxDroneStatusModel.refreshWithRawData(this.f200CtrlRxPacket.contentData);
        } 
      } else {
        this.rxCalibrationStatusModel.refreshWithRawData(this.f200CtrlRxPacket.contentData);
      }  
  }
  
  public void disconnect() {
    this.socketClient.disconnect();
  }
  
  public byte[] getRecvFrameHead() {
    if (this.recvFrameHead == null)
      try {
        this.recvFrameHead = "BT>".getBytes("UTF-8");
      } catch (UnsupportedEncodingException unsupportedEncodingException) {
        unsupportedEncodingException.printStackTrace();
      }  
    return this.recvFrameHead;
  }
  
  public RxCalibrationStatusModel getRxCalibrationStatusModel() {
    return this.rxCalibrationStatusModel;
  }
  
  public RxDroneStatusModel getRxDroneStatusModel() {
    return this.rxDroneStatusModel;
  }
  
  public RxGpsStatusModel getRxGpsStatusModel() {
    return this.rxGpsStatusModel;
  }
  
  public RxWaypointConfigModel getRxWaypointConfigModel() {
    return this.rxWaypointConfigModel;
  }
  
  public void landing(LGBResultCallBack paramLGBResultCallBack) {
    sendOneKeyFunData(TxOneKeyFunModel.OneKeyFunCommand.Landing);
  }
  
  public void lockDrone(LGBResultCallBack paramLGBResultCallBack) {
    sendOneKeyFunData(TxOneKeyFunModel.OneKeyFunCommand.LockDrone);
  }
  
  public void sendCameraPositionCmd(short paramShort) {
    this.cameraPositionInt = (int)(paramShort * 180.0F / 500.0F + 1500.0F);
    sendRockerCmd(1500, 1500, 1500, 1500, this.cameraPositionInt, 0);
  }
  
  public void sendDroneStatusRequestCmd() {
    F200CtrlTxPacket f200CtrlTxPacket = new F200CtrlTxPacket(F200CtrlMesgId.MSP_AHRS_DISPLAY);
    this.socketClient.sendData(f200CtrlTxPacket.getRawData());
  }
  
  public void sendFollowmeCmd(double paramDouble1, double paramDouble2) {
    F200CtrlTxPacket f200CtrlTxPacket = new F200CtrlTxPacket((F200TxModel)new TxFollowmeModel(paramDouble1, paramDouble2));
    this.socketClient.sendData(f200CtrlTxPacket.getRawData());
  }
  
  public void sendGPSStatusRequestCmd() {
    F200CtrlTxPacket f200CtrlTxPacket = new F200CtrlTxPacket(F200CtrlMesgId.MSP_GPS_DISPLAY_NAVIGATION);
    this.socketClient.sendData(f200CtrlTxPacket.getRawData());
  }
  
  public void sendReadWaypointConfigCmd() {
    F200CtrlTxPacket f200CtrlTxPacket = new F200CtrlTxPacket(F200CtrlMesgId.MSP_READ_WayPoint);
    this.socketClient.sendData(f200CtrlTxPacket.getRawData());
  }
  
  protected void sendRockerCmd(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6) {
    F200CtrlTxPacket f200CtrlTxPacket = new F200CtrlTxPacket((F200TxModel)new TxRockerModel(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6));
    this.socketClient.sendData(f200CtrlTxPacket.getRawData());
  }
  
  protected void sendRockerCmd(TxRockerModel paramTxRockerModel) {
    F200CtrlTxPacket f200CtrlTxPacket = new F200CtrlTxPacket((F200TxModel)paramTxRockerModel);
    this.socketClient.sendData(f200CtrlTxPacket.getRawData());
  }
  
  public void sendRockerData(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4) {
    sendRockerCmd((int)(paramDouble1 * 500.0D + 1500.0D), (int)(paramDouble2 * 500.0D + 1500.0D), (int)(paramDouble3 * 500.0D + 1500.0D), (int)(500.0D * paramDouble4 + 1500.0D), this.cameraPositionInt, 0);
  }
  
  public void sendSetupWaypointConfigCmd(int paramInt1, int paramInt2, int paramInt3) {
    sendSetupWaypointConfigCmd(new TxWaypointConfigModel(paramInt1, paramInt2, paramInt3));
  }
  
  public void sendSetupWaypointConfigCmd(TxWaypointConfigModel paramTxWaypointConfigModel) {
    F200CtrlTxPacket f200CtrlTxPacket = new F200CtrlTxPacket((F200TxModel)paramTxWaypointConfigModel);
    this.socketClient.sendData(f200CtrlTxPacket.getRawData());
  }
  
  public void sendSurroundCmd(double paramDouble1, double paramDouble2, int paramInt1, int paramInt2, LGBResultCallBack paramLGBResultCallBack) {
    F200CtrlTxPacket f200CtrlTxPacket = new F200CtrlTxPacket((F200TxModel)new TxGpsCircleModel(paramInt1, paramInt2, paramDouble1, paramDouble2));
    this.socketClient.sendData(f200CtrlTxPacket.getRawData());
  }
  
  public void sendWayPoints(WayPoint[] paramArrayOfWayPoint) {
    TxWaypointModel[] arrayOfTxWaypointModel = new TxWaypointModel[paramArrayOfWayPoint.length];
    for (byte b = 0; b < paramArrayOfWayPoint.length; b++)
      arrayOfTxWaypointModel[b] = new TxWaypointModel((paramArrayOfWayPoint[b]).lat, (paramArrayOfWayPoint[b]).lng, (paramArrayOfWayPoint[b]).wayPointH); 
    sendWaypointGroup(arrayOfTxWaypointModel);
  }
  
  protected void sendWaypointGroup(TxWaypointModel[] paramArrayOfTxWaypointModel) {
    F200CtrlTxPacket f200CtrlTxPacket = new F200CtrlTxPacket((F200TxModel)new TxWaypointGroupModel(paramArrayOfTxWaypointModel));
    this.socketClient.sendData(f200CtrlTxPacket.getRawData());
  }
  
  public void startAccCalibration(LGBResultCallBack paramLGBResultCallBack) {
    F200CtrlTxPacket f200CtrlTxPacket = new F200CtrlTxPacket((F200TxModel)new TxCalibrationModel(TxCalibrationModel.CalibrationFunCommand.AccCalibration));
    this.socketClient.sendData(f200CtrlTxPacket.getRawData());
  }
  
  public void startCompassCalibration(LGBResultCallBack paramLGBResultCallBack) {
    F200CtrlTxPacket f200CtrlTxPacket = new F200CtrlTxPacket((F200TxModel)new TxCalibrationModel(TxCalibrationModel.CalibrationFunCommand.CompassCalibration));
    this.socketClient.sendData(f200CtrlTxPacket.getRawData());
  }
  
  public void startFlyBack(LGBResultCallBack paramLGBResultCallBack) {
    sendOneKeyFunData(TxOneKeyFunModel.OneKeyFunCommand.Flyback);
  }
  
  public void startHovering(LGBResultCallBack paramLGBResultCallBack) {
    sendOneKeyFunData(TxOneKeyFunModel.OneKeyFunCommand.Hovering);
  }
  
  public void takeOff(LGBResultCallBack paramLGBResultCallBack) {
    sendOneKeyFunData(TxOneKeyFunModel.OneKeyFunCommand.TakeOff);
  }
  
  public void unlockDrone(LGBResultCallBack paramLGBResultCallBack) {
    sendOneKeyFunData(TxOneKeyFunModel.OneKeyFunCommand.UnlockDrone);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/dronectrl/F200Ctrl/F200Ctrl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */