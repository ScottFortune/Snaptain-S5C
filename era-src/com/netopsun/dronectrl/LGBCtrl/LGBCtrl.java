package com.netopsun.dronectrl.LGBCtrl;

import com.vilyever.socketclient.SocketDecorator;

public abstract class LGBCtrl {
  protected SocketDecorator.Communicator communicator;
  
  protected LGBCtrlDelegate ctrlDelegate;
  
  DroneStatusModel droneStatusModel;
  
  public abstract void connect(String paramString, int paramInt);
  
  public abstract void disconnect();
  
  public abstract void landing(LGBResultCallBack paramLGBResultCallBack);
  
  public abstract void lockDrone(LGBResultCallBack paramLGBResultCallBack);
  
  public void sendCameraPositionCmd(short paramShort) {}
  
  public void sendDroneStatusRequestCmd() {}
  
  public abstract void sendFollowmeCmd(double paramDouble1, double paramDouble2);
  
  public void sendGPSStatusRequestCmd() {}
  
  public void sendReadWaypointConfigCmd() {}
  
  public abstract void sendRockerData(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4);
  
  public void sendSetupWaypointConfigCmd(int paramInt1, int paramInt2, int paramInt3) {}
  
  public abstract void sendSurroundCmd(double paramDouble1, double paramDouble2, int paramInt1, int paramInt2, LGBResultCallBack paramLGBResultCallBack);
  
  public abstract void sendWayPoints(WayPoint[] paramArrayOfWayPoint);
  
  public void setCommunicator(SocketDecorator.Communicator paramCommunicator) {
    this.communicator = paramCommunicator;
    SocketDecorator.Action.setCommunicator(paramCommunicator);
  }
  
  public void setCtrlDelegate(LGBCtrlDelegate paramLGBCtrlDelegate) {
    this.ctrlDelegate = paramLGBCtrlDelegate;
  }
  
  public abstract void startAccCalibration(LGBResultCallBack paramLGBResultCallBack);
  
  public abstract void startCompassCalibration(LGBResultCallBack paramLGBResultCallBack);
  
  public abstract void startFlyBack(LGBResultCallBack paramLGBResultCallBack);
  
  public abstract void startHovering(LGBResultCallBack paramLGBResultCallBack);
  
  public abstract void takeOff(LGBResultCallBack paramLGBResultCallBack);
  
  public abstract void unlockDrone(LGBResultCallBack paramLGBResultCallBack);
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/dronectrl/LGBCtrl/LGBCtrl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */