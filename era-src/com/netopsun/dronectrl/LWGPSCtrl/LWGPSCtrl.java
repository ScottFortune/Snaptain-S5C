package com.netopsun.dronectrl.LWGPSCtrl;

import android.util.Log;
import com.netopsun.dronectrl.DebugSocketClient;
import com.netopsun.dronectrl.F200Ctrl.F200RxModel.RxCalibrationStatusModel;
import com.netopsun.dronectrl.LGBCtrl.DroneStatusModel;
import com.netopsun.dronectrl.LGBCtrl.LGBCtrl;
import com.netopsun.dronectrl.LGBCtrl.LGBCtrlDelegate;
import com.netopsun.dronectrl.LGBCtrl.LGBResultCallBack;
import com.netopsun.dronectrl.LGBCtrl.RxModel;
import com.netopsun.dronectrl.LGBCtrl.RxModelChangeCallBack;
import com.netopsun.dronectrl.LGBCtrl.WayPoint;
import com.netopsun.dronectrl.LWGPSCtrl.LWGPSRxModel.RxCalibrationStatusModel;
import com.netopsun.dronectrl.LWGPSCtrl.LWGPSRxModel.RxDroneStatusModel;
import com.netopsun.dronectrl.LWGPSCtrl.LWGPSRxModel.RxRemoteCameraModel;
import com.netopsun.dronectrl.LWGPSCtrl.LWGPSRxModel.RxWaypointConfigModel;
import com.netopsun.dronectrl.LWGPSCtrl.LWGPSTxModel.LWGPSTxModel;
import com.netopsun.dronectrl.LWGPSCtrl.LWGPSTxModel.TxCalibrationModel;
import com.netopsun.dronectrl.LWGPSCtrl.LWGPSTxModel.TxCameraPositioneModel;
import com.netopsun.dronectrl.LWGPSCtrl.LWGPSTxModel.TxFollowmeModel;
import com.netopsun.dronectrl.LWGPSCtrl.LWGPSTxModel.TxGpsCircleModel;
import com.netopsun.dronectrl.LWGPSCtrl.LWGPSTxModel.TxOneKeyFun2Model;
import com.netopsun.dronectrl.LWGPSCtrl.LWGPSTxModel.TxOneKeyFun3Model;
import com.netopsun.dronectrl.LWGPSCtrl.LWGPSTxModel.TxOneKeyFunModel;
import com.netopsun.dronectrl.LWGPSCtrl.LWGPSTxModel.TxRockerModel;
import com.netopsun.dronectrl.LWGPSCtrl.LWGPSTxModel.TxWaypointConfigModel;
import com.netopsun.dronectrl.LWGPSCtrl.LWGPSTxModel.TxWaypointGroupModel;
import com.netopsun.dronectrl.LWGPSCtrl.LWGPSTxModel.TxWaypointModel;
import com.vilyever.socketclient.SocketClient;
import com.vilyever.socketclient.helper.SocketClientAddress;
import com.vilyever.socketclient.helper.SocketClientDelegate;
import com.vilyever.socketclient.helper.SocketPacketHelper;
import com.vilyever.socketclient.helper.SocketResponsePacket;

public class LWGPSCtrl extends LGBCtrl {
  private static LWGPSCtrl LWGPSCtrl;
  
  private LWGPSCtrlRxPacket LWGPSCtrlRxPacket = new LWGPSCtrlRxPacket();
  
  private DroneStatusModel droneStatusModel = new DroneStatusModel();
  
  private byte[] recvFrameHead;
  
  private RxCalibrationStatusModel rxCalibrationStatusModel = new RxCalibrationStatusModel();
  
  private RxDroneStatusModel rxDroneStatusModel = new RxDroneStatusModel();
  
  private RxRemoteCameraModel rxRemoteCameraModel = new RxRemoteCameraModel();
  
  public RxWaypointConfigModel rxWaypointConfigModel = new RxWaypointConfigModel();
  
  private SocketClient socketClient;
  
  LWGPSCtrl() {
    this.rxDroneStatusModel.setCallBack(new RxModelChangeCallBack() {
          public void callback(RxModel param1RxModel) {
            if (LWGPSCtrl.this.ctrlDelegate != null) {
              RxDroneStatusModel rxDroneStatusModel = (RxDroneStatusModel)param1RxModel;
              LWGPSCtrl.this.droneStatusModel.rollAngle = rxDroneStatusModel.getRollAngle();
              LWGPSCtrl.this.droneStatusModel.pitchAngle = rxDroneStatusModel.getPitchAngle();
              LWGPSCtrl.this.droneStatusModel.yawAngle = rxDroneStatusModel.getYawAngle();
              LWGPSCtrl.this.droneStatusModel.flyMode = rxDroneStatusModel.getFlyMode();
              LWGPSCtrl.this.droneStatusModel.flyState = rxDroneStatusModel.getFlyState();
              LWGPSCtrl.this.droneStatusModel.altitude = rxDroneStatusModel.getVDistance();
              LWGPSCtrl.this.droneStatusModel.batteryLevel = rxDroneStatusModel.getBatteryLevel();
              LWGPSCtrl.this.droneStatusModel.droneLat = rxDroneStatusModel.getAircraftGpsLat();
              LWGPSCtrl.this.droneStatusModel.droneLng = rxDroneStatusModel.getAircraftGpsLon();
              LWGPSCtrl.this.droneStatusModel.homeDistance = rxDroneStatusModel.getHDistance();
              LWGPSCtrl.this.droneStatusModel.speedH = rxDroneStatusModel.getHVelocity();
              LWGPSCtrl.this.droneStatusModel.speedV = rxDroneStatusModel.getVVelocity();
              LWGPSCtrl.this.droneStatusModel.satelliteNum = rxDroneStatusModel.getSatelliteNum();
              LWGPSCtrl.this.ctrlDelegate.droneStatusUpdate(LWGPSCtrl.this.droneStatusModel);
            } 
          }
        });
    this.rxRemoteCameraModel.setCallBack(new RxModelChangeCallBack() {
          public void callback(RxModel param1RxModel) {
            if (LWGPSCtrl.this.ctrlDelegate != null) {
              RxRemoteCameraModel rxRemoteCameraModel = (RxRemoteCameraModel)param1RxModel;
              int i = LWGPSCtrl.null.$SwitchMap$com$netopsun$dronectrl$LWGPSCtrl$LWGPSRxModel$RxRemoteCameraModel$RemoteCameraType[rxRemoteCameraModel.remoteCameraType.ordinal()];
              if (i != 1) {
                if (i != 2) {
                  if (i == 3)
                    LWGPSCtrl.this.ctrlDelegate.didRecvRecordStopCmd(); 
                } else {
                  LWGPSCtrl.this.ctrlDelegate.didRecvRecordStartCmd();
                } 
              } else {
                LWGPSCtrl.this.ctrlDelegate.didRecvTakePhotoCmd();
              } 
            } 
          }
        });
    this.rxCalibrationStatusModel.setCallBack(new RxModelChangeCallBack() {
          public void callback(RxModel param1RxModel) {
            if (LWGPSCtrl.this.ctrlDelegate != null) {
              RxCalibrationStatusModel rxCalibrationStatusModel = (RxCalibrationStatusModel)param1RxModel;
              LWGPSCtrl.this.droneStatusModel.calibrationState = rxCalibrationStatusModel.calibrationState;
              LWGPSCtrl.this.ctrlDelegate.droneStatusUpdate(LWGPSCtrl.this.droneStatusModel);
            } 
          }
        });
  }
  
  private void sendOneKeyFunData(TxOneKeyFunModel.OneKeyFunCommand paramOneKeyFunCommand) {
    TxOneKeyFunModel txOneKeyFunModel;
    TxOneKeyFun2Model txOneKeyFun2Model;
    if (paramOneKeyFunCommand == TxOneKeyFunModel.OneKeyFunCommand.UnlockDrone || paramOneKeyFunCommand == TxOneKeyFunModel.OneKeyFunCommand.LockDrone) {
      txOneKeyFunModel = new TxOneKeyFunModel(paramOneKeyFunCommand);
      txOneKeyFunModel.getRawData();
    } else {
      TxOneKeyFun3Model txOneKeyFun3Model;
      if (txOneKeyFunModel == TxOneKeyFunModel.OneKeyFunCommand.Hovering) {
        txOneKeyFun3Model = new TxOneKeyFun3Model((TxOneKeyFunModel.OneKeyFunCommand)txOneKeyFunModel);
        txOneKeyFun3Model.getRawData();
      } else {
        txOneKeyFun2Model = new TxOneKeyFun2Model((TxOneKeyFunModel.OneKeyFunCommand)txOneKeyFun3Model);
        txOneKeyFun2Model.getRawData();
      } 
    } 
    byte[] arrayOfByte = (new LWGPSCtrlTxPacket((LWGPSTxModel)txOneKeyFun2Model)).getRawData();
    this.socketClient.sendData(arrayOfByte);
  }
  
  public static LWGPSCtrl shareLWGPSCtrl() {
    // Byte code:
    //   0: ldc com/netopsun/dronectrl/LWGPSCtrl/LWGPSCtrl
    //   2: monitorenter
    //   3: getstatic com/netopsun/dronectrl/LWGPSCtrl/LWGPSCtrl.LWGPSCtrl : Lcom/netopsun/dronectrl/LWGPSCtrl/LWGPSCtrl;
    //   6: ifnonnull -> 21
    //   9: new com/netopsun/dronectrl/LWGPSCtrl/LWGPSCtrl
    //   12: astore_0
    //   13: aload_0
    //   14: invokespecial <init> : ()V
    //   17: aload_0
    //   18: putstatic com/netopsun/dronectrl/LWGPSCtrl/LWGPSCtrl.LWGPSCtrl : Lcom/netopsun/dronectrl/LWGPSCtrl/LWGPSCtrl;
    //   21: getstatic com/netopsun/dronectrl/LWGPSCtrl/LWGPSCtrl.LWGPSCtrl : Lcom/netopsun/dronectrl/LWGPSCtrl/LWGPSCtrl;
    //   24: astore_0
    //   25: ldc com/netopsun/dronectrl/LWGPSCtrl/LWGPSCtrl
    //   27: monitorexit
    //   28: aload_0
    //   29: areturn
    //   30: astore_0
    //   31: ldc com/netopsun/dronectrl/LWGPSCtrl/LWGPSCtrl
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
      this.socketClient = (SocketClient)new DebugSocketClient(new SocketClientAddress(paramString, paramInt));
      this.socketClient.setCharsetName("UTF-8");
      this.socketClient.getSocketPacketHelper().setReadStrategy(SocketPacketHelper.ReadStrategy.AutoReadByLength);
      this.socketClient.getSocketPacketHelper().setReceiveHeaderData(getRecvFrameHead());
      this.socketClient.getSocketPacketHelper().setReceivePacketLengthDataLength(3);
      this.socketClient.getSocketPacketHelper().setReceivePacketDataLengthConvertor(new SocketPacketHelper.ReceivePacketDataLengthConvertor() {
            public int obtainReceivePacketDataLength(SocketPacketHelper param1SocketPacketHelper, byte[] param1ArrayOfbyte) {
              byte b = param1ArrayOfbyte[1];
              return (param1ArrayOfbyte[2] & 0xFF00 | b & 0xFF) + 1;
            }
          });
      this.socketClient.getSocketPacketHelper().setReceiveTrailerData(null);
      byte[] arrayOfByte = (new LWGPSCtrlTxPacket(LWGPSCtrlMesgId.MSP_AHRS_DISPLAY)).getRawData();
      if (arrayOfByte != null) {
        byte[] arrayOfByte1 = new byte[arrayOfByte.length];
        System.arraycopy(arrayOfByte, 0, arrayOfByte1, 0, arrayOfByte.length);
        this.socketClient.getHeartBeatHelper().setDefaultSendData(arrayOfByte1);
        this.socketClient.getHeartBeatHelper().setHeartBeatInterval(500L);
        this.socketClient.getHeartBeatHelper().setSendHeartBeatEnabled(true);
        this.socketClient.registerSocketClientDelegate(new SocketClientDelegate() {
              public void onConnected(SocketClient param1SocketClient) {
                if (LWGPSCtrl.this.ctrlDelegate != null)
                  LWGPSCtrl.this.ctrlDelegate.didConnected(); 
                Log.i("GX", "onConnected");
              }
              
              public void onDisconnected(SocketClient param1SocketClient) {
                if (LWGPSCtrl.this.ctrlDelegate != null)
                  LWGPSCtrl.this.ctrlDelegate.disConnected(); 
                Log.i("GX", "onDisconnected");
              }
              
              public void onResponse(SocketClient param1SocketClient, SocketResponsePacket param1SocketResponsePacket) {
                byte[] arrayOfByte1 = new byte[3];
                arrayOfByte1[0] = 70;
                arrayOfByte1[1] = 72;
                arrayOfByte1[2] = 62;
                byte[] arrayOfByte2 = param1SocketResponsePacket.getData();
                byte[] arrayOfByte3 = param1SocketResponsePacket.getPacketLengthData();
                if (param1SocketResponsePacket.getHeaderData() != null && arrayOfByte2 != null && arrayOfByte3 != null) {
                  arrayOfByte3 = new byte[arrayOfByte1.length + arrayOfByte2.length + arrayOfByte3.length];
                  System.arraycopy(arrayOfByte1, 0, arrayOfByte3, 0, arrayOfByte1.length);
                  System.arraycopy(param1SocketResponsePacket.getPacketLengthData(), 0, arrayOfByte3, arrayOfByte1.length, (param1SocketResponsePacket.getPacketLengthData()).length);
                  System.arraycopy(param1SocketResponsePacket.getData(), 0, arrayOfByte3, arrayOfByte1.length + (param1SocketResponsePacket.getPacketLengthData()).length, (param1SocketResponsePacket.getData()).length);
                  StringBuilder stringBuilder = new StringBuilder();
                  stringBuilder.append("");
                  stringBuilder.append(DebugSocketClient.ArrarytoHexString(arrayOfByte3));
                  Log.d("RECV Packet:", stringBuilder.toString());
                  LWGPSCtrl.this.didReadOneFrameData(arrayOfByte3);
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
    if (!this.LWGPSCtrlRxPacket.refreshModelData(paramArrayOfbyte))
      return; 
    int i = null.$SwitchMap$com$netopsun$dronectrl$LWGPSCtrl$LWGPSCtrlMesgId[this.LWGPSCtrlRxPacket.LWGPSCtrlMesgId.ordinal()];
    if (i != 1)
      if (i != 2) {
        if (i != 3) {
          if (i != 4) {
            if (i != 5)
              return; 
          } else {
            this.rxWaypointConfigModel.refreshWithRawData(this.LWGPSCtrlRxPacket.contentData);
          } 
          this.rxRemoteCameraModel.refreshWithRawData(this.LWGPSCtrlRxPacket.contentData);
        } else {
          this.rxDroneStatusModel.refreshWithRawData(this.LWGPSCtrlRxPacket.contentData);
        } 
      } else {
        this.rxCalibrationStatusModel.refreshWithRawData(this.LWGPSCtrlRxPacket.contentData);
      }  
  }
  
  public void disconnect() {
    this.socketClient.disconnect();
  }
  
  public byte[] getRecvFrameHead() {
    return new byte[] { 70, 72, 62 };
  }
  
  public RxCalibrationStatusModel getRxCalibrationStatusModel() {
    return this.rxCalibrationStatusModel;
  }
  
  public RxDroneStatusModel getRxDroneStatusModel() {
    return this.rxDroneStatusModel;
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
    LWGPSCtrlTxPacket lWGPSCtrlTxPacket = new LWGPSCtrlTxPacket((LWGPSTxModel)new TxCameraPositioneModel(paramShort));
    this.socketClient.sendData(lWGPSCtrlTxPacket.getRawData());
  }
  
  public void sendDroneStatusRequestCmd() {
    LWGPSCtrlTxPacket lWGPSCtrlTxPacket = new LWGPSCtrlTxPacket(LWGPSCtrlMesgId.MSP_AHRS_DISPLAY);
    this.socketClient.sendData(lWGPSCtrlTxPacket.getRawData());
  }
  
  public void sendFollowmeCmd(double paramDouble1, double paramDouble2) {
    LWGPSCtrlTxPacket lWGPSCtrlTxPacket = new LWGPSCtrlTxPacket((LWGPSTxModel)new TxFollowmeModel(paramDouble1, paramDouble2));
    this.socketClient.sendData(lWGPSCtrlTxPacket.getRawData());
  }
  
  public void sendGPSStatusRequestCmd() {
    LWGPSCtrlTxPacket lWGPSCtrlTxPacket = new LWGPSCtrlTxPacket(LWGPSCtrlMesgId.MSP_AHRS_DISPLAY);
    this.socketClient.sendData(lWGPSCtrlTxPacket.getRawData());
  }
  
  public void sendReadWaypointConfigCmd() {
    LWGPSCtrlTxPacket lWGPSCtrlTxPacket = new LWGPSCtrlTxPacket(LWGPSCtrlMesgId.MSP_READ_WayPoint);
    this.socketClient.sendData(lWGPSCtrlTxPacket.getRawData());
  }
  
  protected void sendRockerCmd(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6) {
    LWGPSCtrlTxPacket lWGPSCtrlTxPacket = new LWGPSCtrlTxPacket((LWGPSTxModel)new TxRockerModel(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6));
    this.socketClient.sendData(lWGPSCtrlTxPacket.getRawData());
  }
  
  protected void sendRockerCmd(TxRockerModel paramTxRockerModel) {
    LWGPSCtrlTxPacket lWGPSCtrlTxPacket = new LWGPSCtrlTxPacket((LWGPSTxModel)paramTxRockerModel);
    this.socketClient.sendData(lWGPSCtrlTxPacket.getRawData());
  }
  
  public void sendRockerData(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4) {
    sendRockerCmd((int)(paramDouble1 * 127.0D + 128.0D), (int)(paramDouble2 * 127.0D + 128.0D), (int)(paramDouble3 * 127.0D + 128.0D), (int)(127.0D * paramDouble4 + 128.0D), 0, 0);
  }
  
  public void sendSetupWaypointConfigCmd(int paramInt1, int paramInt2, int paramInt3) {
    sendSetupWaypointConfigCmd(new TxWaypointConfigModel(paramInt1, paramInt2, paramInt3));
  }
  
  public void sendSetupWaypointConfigCmd(TxWaypointConfigModel paramTxWaypointConfigModel) {
    LWGPSCtrlTxPacket lWGPSCtrlTxPacket = new LWGPSCtrlTxPacket((LWGPSTxModel)paramTxWaypointConfigModel);
    this.socketClient.sendData(lWGPSCtrlTxPacket.getRawData());
  }
  
  public void sendSurroundCmd(double paramDouble1, double paramDouble2, int paramInt1, int paramInt2, LGBResultCallBack paramLGBResultCallBack) {
    LWGPSCtrlTxPacket lWGPSCtrlTxPacket = new LWGPSCtrlTxPacket((LWGPSTxModel)new TxGpsCircleModel(paramInt1, paramInt2, paramDouble1, paramDouble2));
    this.socketClient.sendData(lWGPSCtrlTxPacket.getRawData());
  }
  
  public void sendWayPoints(WayPoint[] paramArrayOfWayPoint) {
    TxWaypointModel[] arrayOfTxWaypointModel = new TxWaypointModel[paramArrayOfWayPoint.length];
    for (byte b = 0; b < arrayOfTxWaypointModel.length; b++)
      arrayOfTxWaypointModel[b] = new TxWaypointModel((paramArrayOfWayPoint[b]).lat, (paramArrayOfWayPoint[b]).lng, (paramArrayOfWayPoint[b]).wayPointH); 
    sendWaypointGroup(arrayOfTxWaypointModel);
  }
  
  protected void sendWaypointGroup(TxWaypointModel[] paramArrayOfTxWaypointModel) {
    LWGPSCtrlTxPacket lWGPSCtrlTxPacket = new LWGPSCtrlTxPacket((LWGPSTxModel)new TxWaypointGroupModel(paramArrayOfTxWaypointModel));
    this.socketClient.sendData(lWGPSCtrlTxPacket.getRawData());
  }
  
  public void startAccCalibration(LGBResultCallBack paramLGBResultCallBack) {
    LWGPSCtrlTxPacket lWGPSCtrlTxPacket = new LWGPSCtrlTxPacket((LWGPSTxModel)new TxCalibrationModel(TxCalibrationModel.CalibrationFunCommand.AccCalibration));
    this.socketClient.sendData(lWGPSCtrlTxPacket.getRawData());
  }
  
  public void startCompassCalibration(LGBResultCallBack paramLGBResultCallBack) {
    LWGPSCtrlTxPacket lWGPSCtrlTxPacket = new LWGPSCtrlTxPacket((LWGPSTxModel)new TxCalibrationModel(TxCalibrationModel.CalibrationFunCommand.CompassCalibration));
    this.socketClient.sendData(lWGPSCtrlTxPacket.getRawData());
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


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/dronectrl/LWGPSCtrl/LWGPSCtrl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */