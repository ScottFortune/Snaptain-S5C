package com.netopsun.rxtxprotocol.base.simple_receiver;

public interface SimpleDroneMsgCallback {
  void didRecvRecordStartCmd();
  
  void didRecvRecordStopCmd();
  
  void didRecvTakePhotoCmd();
  
  void onBatteryLevel(int paramInt, float paramFloat);
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/rxtxprotocol/base/simple_receiver/SimpleDroneMsgCallback.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */