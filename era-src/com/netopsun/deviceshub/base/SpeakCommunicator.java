package com.netopsun.deviceshub.base;

import com.netopsun.deviceshub.interfaces.ConnectResultCallback;

public abstract class SpeakCommunicator {
  protected volatile ConnectResultCallback connectResultCallback;
  
  public abstract void connect();
  
  public abstract void disconnect();
  
  public ConnectResultCallback getConnectResultCallback() {
    return this.connectResultCallback;
  }
  
  public abstract boolean isConnected();
  
  public abstract void sendAsync(int paramInt1, byte[] paramArrayOfbyte, int paramInt2);
  
  public void setConnectResultCallback(ConnectResultCallback paramConnectResultCallback) {
    this.connectResultCallback = paramConnectResultCallback;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/deviceshub/base/SpeakCommunicator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */