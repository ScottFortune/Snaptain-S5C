package com.netopsun.deviceshub.base;

import android.util.Log;
import java.util.HashMap;

public abstract class Devices {
  private ConnectHandler connectHandler;
  
  protected static HashMap<String, String> getParamsFormURL(String paramString) {
    HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
    int i = paramString.indexOf("://");
    if (i >= 0) {
      int j = paramString.length();
      i += 3;
      if (j > i) {
        String[] arrayOfString1;
        paramString = paramString.substring(i);
        if (!paramString.contains("&")) {
          arrayOfString1 = paramString.split("=");
          if (arrayOfString1.length >= 2)
            hashMap.put(arrayOfString1[0], arrayOfString1[1]); 
          return (HashMap)hashMap;
        } 
        String[] arrayOfString2 = arrayOfString1.split("&");
        i = arrayOfString2.length;
        for (j = 0; j < i; j++) {
          arrayOfString1 = arrayOfString2[j].split("=");
          if (arrayOfString1.length >= 2)
            hashMap.put(arrayOfString1[0], arrayOfString1[1]); 
        } 
      } 
    } 
    return (HashMap)hashMap;
  }
  
  protected int IFNeedInitDevices() {
    return 0;
  }
  
  public void close() {}
  
  protected void finalize() throws Throwable {
    if (!isRelease())
      release(); 
    super.finalize();
  }
  
  public AudioCommunicator getAudioCommunicator() {
    return null;
  }
  
  public abstract CMDCommunicator getCMDCommunicator();
  
  protected ConnectHandler getConnectHandler() {
    if (this.connectHandler.isRelease())
      Log.e("Devices", "getConnectHandler error:devices is already release"); 
    return this.connectHandler;
  }
  
  public String getDevicesFlag() {
    return "null";
  }
  
  public abstract String getDevicesName();
  
  public abstract RxTxCommunicator getRxTxCommunicator();
  
  public SpeakCommunicator getSpeakCommunicator() {
    return null;
  }
  
  public abstract VideoCommunicator getVideoCommunicator();
  
  public void initConnectHandler(VideoCommunicator paramVideoCommunicator, CMDCommunicator paramCMDCommunicator, RxTxCommunicator paramRxTxCommunicator) {
    this.connectHandler = new ConnectHandler(this, paramVideoCommunicator, paramCMDCommunicator, paramRxTxCommunicator);
  }
  
  public abstract boolean isRelease();
  
  public void release() {
    close();
    this.connectHandler.release();
  }
  
  public abstract void updateParams(String paramString);
  
  public static interface ReleaseCallback {
    void onFinishRelease();
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/deviceshub/base/Devices.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */