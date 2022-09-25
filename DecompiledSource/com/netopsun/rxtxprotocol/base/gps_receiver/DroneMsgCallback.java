package com.netopsun.rxtxprotocol.base.gps_receiver;

public interface DroneMsgCallback {
  void didRecvRecordStartCmd();
  
  void didRecvRecordStopCmd();
  
  void didRecvTakePhotoCmd();
  
  void droneStatusUpdate(DroneStatusModel paramDroneStatusModel);
  
  void droneTestInfoUpdate(DroneStatusModel paramDroneStatusModel);
  
  void recvCircleCommand();
  
  void recvFollowmeCommand();
  
  void recvWaypointCommand();
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/rxtxprotocol/base/gps_receiver/DroneMsgCallback.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */