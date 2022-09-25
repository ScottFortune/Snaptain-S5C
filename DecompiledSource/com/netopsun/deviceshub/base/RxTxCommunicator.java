package com.netopsun.deviceshub.base;

import com.netopsun.deviceshub.interfaces.ConnectResultCallback;

public abstract class RxTxCommunicator {
  protected volatile boolean autoReconnect;
  
  protected volatile ConnectResultCallback connectResultCallback;
  
  protected int currentReconnectTimes;
  
  private final Devices devices;
  
  protected OnReceiveCallback onReceiveCallback;
  
  protected int shouldReconnectTimes = 3;
  
  public RxTxCommunicator(Devices paramDevices) {
    this.devices = paramDevices;
  }
  
  public void connect() {
    this.currentReconnectTimes = 0;
    this.devices.getConnectHandler().notifyConnectRxTx();
  }
  
  public abstract int connectInternal();
  
  public void disconnect() {
    this.devices.getConnectHandler().notifyDisconnectRxTx();
  }
  
  public abstract int disconnectInternal();
  
  public ConnectResultCallback getConnectResultCallback() {
    return this.connectResultCallback;
  }
  
  public abstract int interruptSend();
  
  public abstract boolean isConnected();
  
  public abstract int send(byte[] paramArrayOfbyte);
  
  public void setAutoReconnect(boolean paramBoolean, int paramInt) {
    this.autoReconnect = paramBoolean;
  }
  
  public void setConnectResultCallback(ConnectResultCallback paramConnectResultCallback) {
    this.connectResultCallback = paramConnectResultCallback;
  }
  
  public void setOnReceiveCallback(OnReceiveCallback paramOnReceiveCallback) {
    this.onReceiveCallback = paramOnReceiveCallback;
  }
  
  public void setShouldReconnectTimes(int paramInt) {
    this.shouldReconnectTimes = paramInt;
    if (paramInt < 0)
      this.shouldReconnectTimes = 1000000000; 
  }
  
  public boolean shouldRetryConnect() {
    boolean bool;
    int i = this.currentReconnectTimes;
    this.currentReconnectTimes = i + 1;
    if (i < this.shouldReconnectTimes) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean startSendHeartBeatPackage(int paramInt, byte[] paramArrayOfbyte) {
    return false;
  }
  
  public void stopSendHeartBeatPackage() {}
  
  public static interface OnReceiveCallback {
    void onReceive(byte[] param1ArrayOfbyte);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/deviceshub/base/RxTxCommunicator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */