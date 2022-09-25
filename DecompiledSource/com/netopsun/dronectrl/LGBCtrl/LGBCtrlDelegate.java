package com.netopsun.dronectrl.LGBCtrl;

public interface LGBCtrlDelegate {
  void didConnected();
  
  void didRecvRecordStartCmd();
  
  void didRecvRecordStopCmd();
  
  void didRecvTakePhotoCmd();
  
  void disConnected();
  
  void droneStatusUpdate(DroneStatusModel paramDroneStatusModel);
  
  void recvCircleCommand();
  
  void recvFollowmeCommand();
  
  void recvWaypointCommand();
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/dronectrl/LGBCtrl/LGBCtrlDelegate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */