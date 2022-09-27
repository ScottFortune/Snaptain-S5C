package com.netopsun.rxtxprotocol.lw_gps_protocol;

import android.location.Location;
import android.os.Bundle;
import com.netopsun.deviceshub.base.RxTxCommunicator;
import com.netopsun.dronectrl.LGBCtrl.DroneStatusModel;
import com.netopsun.dronectrl.LGBCtrl.LGBCtrl;
import com.netopsun.dronectrl.LGBCtrl.LGBCtrlDelegate;
import com.netopsun.dronectrl.LGBCtrl.WayPoint;
import com.netopsun.dronectrl.LWGPSCtrl.LWGPSCtrl;
import com.netopsun.rxtxprotocol.base.RxTxProtocol;
import com.netopsun.rxtxprotocol.base.gps_receiver.DroneMsgCallback;
import com.netopsun.rxtxprotocol.base.gps_receiver.DroneStatusModel;
import com.vilyever.socketclient.SocketDecorator;
import java.util.ArrayList;

public class LWGPSProtocol extends RxTxProtocol {
  private static final LGBCtrl lgbCtrl = (LGBCtrl)LWGPSCtrl.shareLWGPSCtrl();
  
  private volatile boolean isConnect = false;
  
  private volatile boolean isRockerChange = false;
  
  private final DroneStatusModel model = new DroneStatusModel();
  
  public LWGPSProtocol(final RxTxCommunicator rxTxCommunicator) {
    super(rxTxCommunicator);
    lgbCtrl.setCommunicator(new SocketDecorator.Communicator() {
          public void connect(SocketDecorator.ConnectCallback param1ConnectCallback) {
            rxTxCommunicator.connect();
            param1ConnectCallback.onConnect();
          }
          
          public void send(byte[] param1ArrayOfbyte) {
            rxTxCommunicator.send(param1ArrayOfbyte);
          }
          
          public void setReceiveCallback(final SocketDecorator.ReceiveCallback receiveCallback) {
            rxTxCommunicator.setOnReceiveCallback(new RxTxCommunicator.OnReceiveCallback() {
                  public void onReceive(byte[] param2ArrayOfbyte) {
                    receiveCallback.onReceive(param2ArrayOfbyte);
                  }
                });
          }
          
          public void shutdown() {
            rxTxCommunicator.disconnect();
          }
        });
    lgbCtrl.setCtrlDelegate(new LGBCtrlDelegate() {
          public void didConnected() {
            LWGPSProtocol.access$302(LWGPSProtocol.this, true);
          }
          
          public void didRecvRecordStartCmd() {
            if (LWGPSProtocol.this.droneMsgCallback != null)
              LWGPSProtocol.this.droneMsgCallback.didRecvRecordStartCmd(); 
          }
          
          public void didRecvRecordStopCmd() {
            if (LWGPSProtocol.this.droneMsgCallback != null)
              LWGPSProtocol.this.droneMsgCallback.didRecvRecordStopCmd(); 
          }
          
          public void didRecvTakePhotoCmd() {
            if (LWGPSProtocol.this.droneMsgCallback != null)
              LWGPSProtocol.this.droneMsgCallback.didRecvTakePhotoCmd(); 
          }
          
          public void disConnected() {
            LWGPSProtocol.access$302(LWGPSProtocol.this, false);
          }
          
          public void droneStatusUpdate(DroneStatusModel param1DroneStatusModel) {
            LWGPSProtocol.this.model.rollAngle = param1DroneStatusModel.rollAngle;
            LWGPSProtocol.this.model.pitchAngle = param1DroneStatusModel.pitchAngle;
            LWGPSProtocol.this.model.yawAngle = param1DroneStatusModel.yawAngle;
            LWGPSProtocol.this.model.droneLat = param1DroneStatusModel.droneLat;
            LWGPSProtocol.this.model.droneLng = param1DroneStatusModel.droneLng;
            LWGPSProtocol.this.model.satelliteNum = param1DroneStatusModel.satelliteNum;
            LWGPSProtocol.this.model.batteryLevel = param1DroneStatusModel.batteryLevel;
            LWGPSProtocol.this.model.speedH = param1DroneStatusModel.speedH;
            LWGPSProtocol.this.model.speedV = param1DroneStatusModel.speedV;
            LWGPSProtocol.this.model.altitude = param1DroneStatusModel.altitude;
            LWGPSProtocol.this.model.homeDistance = param1DroneStatusModel.homeDistance;
            LWGPSProtocol.this.model.flyTime = param1DroneStatusModel.flyTime;
            if (param1DroneStatusModel.calibrationState != null)
              if (param1DroneStatusModel.calibrationState == DroneStatusModel.CalibrationStateType.HorizontalRotation) {
                LWGPSProtocol.this.model.compassCalibrationProgress = 10;
                LWGPSProtocol.this.model.compassCalibrationX = 0;
                LWGPSProtocol.this.model.compassCalibrationY = 0;
              } else if (param1DroneStatusModel.calibrationState == DroneStatusModel.CalibrationStateType.VerticalRotation) {
                LWGPSProtocol.this.model.compassCalibrationProgress = 50;
                LWGPSProtocol.this.model.compassCalibrationX = 1;
                LWGPSProtocol.this.model.compassCalibrationY = 0;
              } else if (param1DroneStatusModel.calibrationState == DroneStatusModel.CalibrationStateType.CompassCalibrateSuc) {
                LWGPSProtocol.this.model.compassCalibrationProgress = 100;
                LWGPSProtocol.this.model.compassCalibrationX = 1;
                LWGPSProtocol.this.model.compassCalibrationY = 1;
              } else if (param1DroneStatusModel.calibrationState == DroneStatusModel.CalibrationStateType.CompassCalibrateFail) {
                LWGPSProtocol.this.model.compassCalibrationProgress = 0;
                LWGPSProtocol.this.model.flyState = DroneStatusModel.FlyStateType.locked_rotor;
              } else {
                LWGPSProtocol.this.model.compassCalibrationProgress = 0;
                LWGPSProtocol.this.model.compassCalibrationX = 0;
                LWGPSProtocol.this.model.compassCalibrationY = 0;
              }  
            if (param1DroneStatusModel.flyMode != null)
              LWGPSProtocol.this.model.flyMode = DroneStatusModel.FlyModeType.values()[param1DroneStatusModel.flyMode.ordinal()]; 
            if (param1DroneStatusModel.flyState != null)
              LWGPSProtocol.this.model.flyState = DroneStatusModel.FlyStateType.values()[param1DroneStatusModel.flyState.ordinal()]; 
            if (LWGPSProtocol.this.droneMsgCallback != null)
              LWGPSProtocol.this.droneMsgCallback.droneStatusUpdate(LWGPSProtocol.this.model); 
          }
          
          public void recvCircleCommand() {
            if (LWGPSProtocol.this.droneMsgCallback != null)
              LWGPSProtocol.this.droneMsgCallback.recvCircleCommand(); 
          }
          
          public void recvFollowmeCommand() {
            if (LWGPSProtocol.this.droneMsgCallback != null)
              LWGPSProtocol.this.droneMsgCallback.recvFollowmeCommand(); 
          }
          
          public void recvWaypointCommand() {
            if (LWGPSProtocol.this.droneMsgCallback != null)
              LWGPSProtocol.this.droneMsgCallback.recvWaypointCommand(); 
          }
        });
  }
  
  public boolean couldAddSendBytes() {
    return this.isConnect;
  }
  
  public void notifySend() {
    if (this.isRockerChange) {
      lgbCtrl.sendRockerData((this.roll / 100.0F), (this.pitch / 100.0F), (this.yaw / 100.0F), (this.accelerator / 100.0F));
      this.isRockerChange = false;
    } 
    if (this.takeOff == 1) {
      lgbCtrl.takeOff(null);
      this.takeOff = 2;
    } 
    if (this.landing == 1) {
      lgbCtrl.landing(null);
      this.landing = 2;
    } 
    if (this.emergencyStop == 1) {
      lgbCtrl.startHovering(null);
      this.emergencyStop = 2;
    } 
    if (this.calibration == 1) {
      lgbCtrl.startAccCalibration(null);
      this.calibration = 2;
    } 
    if (this.unlocked == 1) {
      lgbCtrl.unlockDrone(null);
      this.unlocked = 2;
    } 
    if (this.compassCalibration == 1) {
      lgbCtrl.startCompassCalibration(null);
      this.compassCalibration = 2;
    } 
    if (this.flyback == 1) {
      lgbCtrl.startFlyBack(null);
      this.flyback = 2;
    } 
    if (this.normalFlyMode == 1) {
      lgbCtrl.startHovering(null);
      this.normalFlyMode = 2;
    } 
    Location location1 = this.followMeLocation;
    if (location1 != null) {
      lgbCtrl.sendFollowmeCmd(location1.getLatitude(), location1.getLongitude());
      this.followMeLocation = null;
    } 
    Location location2 = this.aroundPointLocation;
    if (location2 != null) {
      Bundle bundle = location2.getExtras();
      if (bundle != null)
        bundle.getInt("circleR"); 
      lgbCtrl.sendSurroundCmd(location2.getLatitude(), location2.getLongitude(), (int)location2.getAltitude(), 5, null);
      this.aroundPointLocation = null;
    } 
    Location[] arrayOfLocation = this.wayPointLocations;
    if (arrayOfLocation != null) {
      ArrayList<WayPoint> arrayList = new ArrayList();
      int i = arrayOfLocation.length;
      for (byte b = 0; b < i; b++) {
        Location location = arrayOfLocation[b];
        arrayList.add(new WayPoint(location.getLatitude(), location.getLongitude(), (int)location.getAltitude()));
      } 
      lgbCtrl.sendWayPoints(arrayList.<WayPoint>toArray(new WayPoint[0]));
      this.wayPointLocations = null;
    } 
  }
  
  public void setAccelerator(float paramFloat) {
    super.setAccelerator(paramFloat);
    this.isRockerChange = true;
  }
  
  public void setPitch(float paramFloat) {
    super.setPitch(paramFloat);
    this.isRockerChange = true;
  }
  
  public void setRoll(float paramFloat) {
    super.setRoll(paramFloat);
    this.isRockerChange = true;
  }
  
  public void setYaw(float paramFloat) {
    super.setYaw(paramFloat);
    this.isRockerChange = true;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/rxtxprotocol/lw_gps_protocol/LWGPSProtocol.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */