package com.netopsun.deviceshub.base;

import com.netopsun.deviceshub.base.bean.RemoteVideoFiles;
import com.netopsun.deviceshub.interfaces.Cancelable;
import com.netopsun.deviceshub.interfaces.ConnectResultCallback;
import com.netopsun.deviceshub.interfaces.NothingCancel;

public abstract class CMDCommunicator {
  public static final int SDCardNotReady = 0;
  
  public static final int SDCardReady = 1;
  
  public int SDCardState = 0;
  
  protected volatile boolean autoReconnect = false;
  
  protected volatile ConnectResultCallback connectResultCallback;
  
  private int currentReconnectTimes;
  
  private final Devices devices;
  
  protected volatile OnReceiveRemotePhotoCallback onReceiveRemotePhotoCallback;
  
  protected OnRemoteCMDListener onRemoteCMDListener;
  
  protected volatile int shouldReconnectTimes = 0;
  
  public CMDCommunicator(Devices paramDevices) {
    this.devices = paramDevices;
  }
  
  public Cancelable changePassword(boolean paramBoolean, int paramInt, String paramString1, String paramString2, OnExecuteCMDResult paramOnExecuteCMDResult) {
    return (Cancelable)new NothingCancel();
  }
  
  public Cancelable configIpcamAp(boolean paramBoolean, int paramInt, String paramString1, String paramString2, OnExecuteCMDResult paramOnExecuteCMDResult) {
    return (Cancelable)new NothingCancel();
  }
  
  public void connect() {
    this.currentReconnectTimes = 0;
    this.devices.getConnectHandler().notifyConnectCMD();
  }
  
  public abstract int connectInternal();
  
  public Cancelable deleteRemoteRecordFile(boolean paramBoolean, int paramInt, String paramString, OnExecuteCMDResult paramOnExecuteCMDResult) {
    return (Cancelable)new NothingCancel();
  }
  
  public void disconnect() {
    this.devices.getConnectHandler().notifyDisconnectCMD();
  }
  
  public abstract int disconnectInternal();
  
  public Cancelable downloadRemoteRecordFile(boolean paramBoolean, int paramInt, String paramString1, String paramString2, OnExecuteCMDResult paramOnExecuteCMDResult) {
    return (Cancelable)new NothingCancel();
  }
  
  public ConnectResultCallback getConnectResultCallback() {
    return this.connectResultCallback;
  }
  
  public Cancelable getDevicesMACString(boolean paramBoolean, int paramInt, OnExecuteCMDResult paramOnExecuteCMDResult) {
    return (Cancelable)new NothingCancel();
  }
  
  public Cancelable getFirmwareVersion(boolean paramBoolean, int paramInt, OnExecuteCMDResult paramOnExecuteCMDResult) {
    return (Cancelable)new NothingCancel();
  }
  
  public Cancelable getIpcamApList(boolean paramBoolean, int paramInt, OnExecuteCMDResult paramOnExecuteCMDResult) {
    return (Cancelable)new NothingCancel();
  }
  
  public Cancelable getRemoteRecordFileList(boolean paramBoolean, int paramInt1, int paramInt2, String paramString, OnExecuteCMDResult paramOnExecuteCMDResult) {
    return (Cancelable)new NothingCancel();
  }
  
  public Cancelable getRemoteSDCardStatus(boolean paramBoolean, int paramInt, OnExecuteCMDResult paramOnExecuteCMDResult) {
    return (Cancelable)new NothingCancel();
  }
  
  public Cancelable getSDKVersion(boolean paramBoolean, int paramInt1, int paramInt2, OnExecuteCMDResult paramOnExecuteCMDResult) {
    return (Cancelable)new NothingCancel();
  }
  
  public abstract boolean isConnected();
  
  public boolean isVideoReversal() {
    return false;
  }
  
  public Cancelable remoteStartRecord(boolean paramBoolean, int paramInt, OnExecuteCMDResult paramOnExecuteCMDResult) {
    return (Cancelable)new NothingCancel();
  }
  
  public Cancelable remoteStopRecord(boolean paramBoolean, int paramInt, OnExecuteCMDResult paramOnExecuteCMDResult) {
    return (Cancelable)new NothingCancel();
  }
  
  public Cancelable remoteTakePhoto(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, OnExecuteCMDResult paramOnExecuteCMDResult) {
    return (Cancelable)new NothingCancel();
  }
  
  public Cancelable remoteTakePhoto(boolean paramBoolean, int paramInt, OnExecuteCMDResult paramOnExecuteCMDResult) {
    return (Cancelable)new NothingCancel();
  }
  
  public Cancelable rotateVideo(boolean paramBoolean1, int paramInt, boolean paramBoolean2, OnExecuteCMDResult paramOnExecuteCMDResult) {
    return (Cancelable)new NothingCancel();
  }
  
  public Cancelable sendReceiveRemotePhotoCMD() {
    return (Cancelable)new NothingCancel();
  }
  
  public void setAutoReconnect(boolean paramBoolean, int paramInt) {
    this.autoReconnect = paramBoolean;
  }
  
  public void setBatteryMsgCallback(OnExecuteCMDResult paramOnExecuteCMDResult) {}
  
  public void setConnectResultCallback(ConnectResultCallback paramConnectResultCallback) {
    this.connectResultCallback = paramConnectResultCallback;
  }
  
  public Cancelable setIpcamExitRemoteMode(boolean paramBoolean, int paramInt, OnExecuteCMDResult paramOnExecuteCMDResult) {
    return (Cancelable)new NothingCancel();
  }
  
  public Cancelable setIpcamlanguage(boolean paramBoolean, int paramInt1, int paramInt2, OnExecuteCMDResult paramOnExecuteCMDResult) {
    return (Cancelable)new NothingCancel();
  }
  
  public void setOnRemoteCMDListener(OnRemoteCMDListener paramOnRemoteCMDListener) {
    this.onRemoteCMDListener = paramOnRemoteCMDListener;
  }
  
  public void setReceiveRemotePhotoCallback(OnReceiveRemotePhotoCallback paramOnReceiveRemotePhotoCallback) {
    this.onReceiveRemotePhotoCallback = paramOnReceiveRemotePhotoCallback;
  }
  
  public void setShouldReconnectTimes(int paramInt) {
    this.shouldReconnectTimes = paramInt;
    if (paramInt < 0)
      this.shouldReconnectTimes = 1000000000; 
  }
  
  public Cancelable setVideoQuality(boolean paramBoolean, int paramInt1, int paramInt2, OnExecuteCMDResult paramOnExecuteCMDResult) {
    return (Cancelable)new NothingCancel();
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
  
  public Cancelable startPlaybackStream(boolean paramBoolean, int paramInt, String paramString, OnExecuteCMDResult paramOnExecuteCMDResult) {
    return (Cancelable)new NothingCancel();
  }
  
  public RemoteVideoFiles string2RemoteVideoFiles(String paramString) {
    return null;
  }
  
  public Cancelable switchCamera(boolean paramBoolean1, int paramInt, boolean paramBoolean2, OnExecuteCMDResult paramOnExecuteCMDResult) {
    return (Cancelable)new NothingCancel();
  }
  
  public static abstract class OnExecuteCMDResult {
    public static final int CANCEL_BY_USER = -507;
    
    public static final int RECEIVE_TIMEOUT = -505;
    
    public static final int TIMEOUT = -504;
    
    public Object cmdTypeObject;
    
    public abstract void onResult(int param1Int, String param1String);
  }
  
  public static interface OnReceiveRemotePhotoCallback {
    String generatePhotoFileName();
    
    void onReceive(int param1Int, boolean param1Boolean, String param1String);
  }
  
  public static interface OnRemoteCMDListener {
    void onRemoteStartRecord();
    
    void onRemoteStopRecord();
    
    void onRemoteTakePhoto();
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/deviceshub/base/CMDCommunicator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */