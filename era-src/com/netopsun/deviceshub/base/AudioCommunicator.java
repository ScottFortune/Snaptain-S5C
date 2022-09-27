package com.netopsun.deviceshub.base;

import com.netopsun.deviceshub.interfaces.ConnectResultCallback;

public abstract class AudioCommunicator {
  protected volatile ConnectResultCallback connectResultCallback;
  
  protected OnAudioFrameCallback onAudioFrameCallback;
  
  public abstract void connect();
  
  public abstract void disconnect();
  
  public ConnectResultCallback getConnectResultCallback() {
    return this.connectResultCallback;
  }
  
  public abstract boolean isConnected();
  
  public void setConnectResultCallback(ConnectResultCallback paramConnectResultCallback) {
    this.connectResultCallback = paramConnectResultCallback;
  }
  
  public void setOnFrameCallback(OnAudioFrameCallback paramOnAudioFrameCallback) {
    this.onAudioFrameCallback = paramOnAudioFrameCallback;
  }
  
  public static interface OnAudioFrameCallback {
    void onAudioFrame(byte[] param1ArrayOfbyte, int param1Int);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/deviceshub/base/AudioCommunicator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */