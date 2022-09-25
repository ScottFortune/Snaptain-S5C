package com.netopsun.anykadevices;

import android.text.TextUtils;
import com.netopsun.deviceshub.base.CMDCommunicator;
import com.netopsun.deviceshub.base.ConnectHandler;
import com.netopsun.deviceshub.base.Devices;
import com.netopsun.deviceshub.base.RxTxCommunicator;
import com.netopsun.deviceshub.base.VideoCommunicator;
import java.util.HashMap;

public class AnykaDevices extends Devices {
  private static final String TAG = "AnykaDevices";
  
  private final AnykaCMDCommunicator anykaCMDCommunicator;
  
  private final RxTxCommunicator anykaRxTxCommunicator;
  
  private final AnykaVideoCommunicator anykaVideoCommunicator;
  
  private int cmdPort = 8060;
  
  private String devicesIP = "192.168.0.1";
  
  private volatile boolean isInitDevices = false;
  
  private int rxtxPort = 50000;
  
  private int videoPort = 7060;
  
  private AnykaDevices(String paramString1, String paramString2, String paramString3) {
    setParams(paramString1, paramString2);
    this.anykaVideoCommunicator = new AnykaVideoCommunicator(this);
    this.anykaCMDCommunicator = new AnykaCMDCommunicator(this);
    if ("tcp".equals(paramString3)) {
      this.anykaRxTxCommunicator = new AnykaRxTxCommunicator(this);
    } else {
      this.anykaRxTxCommunicator = new AnykaRxTxCommunicatorByUDP(this);
    } 
    initConnectHandler(this.anykaVideoCommunicator, this.anykaCMDCommunicator, this.anykaRxTxCommunicator);
  }
  
  public static Devices open(String paramString) {
    if (!paramString.contains("anyka"))
      return null; 
    HashMap hashMap = getParamsFormURL(paramString);
    return new AnykaDevices((String)hashMap.get("ip"), (String)hashMap.get("videoport"), (String)hashMap.get("rxtxmode"));
  }
  
  private void setParams(String paramString1, String paramString2) {
    if (!TextUtils.isEmpty(paramString1))
      this.devicesIP = paramString1; 
    if (!TextUtils.isEmpty(paramString2))
      this.videoPort = Integer.valueOf(paramString2).intValue(); 
  }
  
  protected int IfNeedInitDevices() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield isInitDevices : Z
    //   6: istore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: iconst_0
    //   10: ireturn
    //   11: astore_2
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_2
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  public void close() {
    this.anykaVideoCommunicator.disconnect();
    this.anykaCMDCommunicator.disconnect();
    this.anykaRxTxCommunicator.stopSendHeartBeatPackage();
    this.anykaRxTxCommunicator.disconnect();
  }
  
  public CMDCommunicator getCMDCommunicator() {
    return this.anykaCMDCommunicator;
  }
  
  public int getCmdPort() {
    return this.cmdPort;
  }
  
  protected ConnectHandler getConnectHandler() {
    return super.getConnectHandler();
  }
  
  public String getDevicesIP() {
    return this.devicesIP;
  }
  
  public String getDevicesName() {
    return "anykadevices";
  }
  
  public RxTxCommunicator getRxTxCommunicator() {
    return this.anykaRxTxCommunicator;
  }
  
  public int getRxtxPort() {
    return this.rxtxPort;
  }
  
  public VideoCommunicator getVideoCommunicator() {
    return this.anykaVideoCommunicator;
  }
  
  public int getVideoPort() {
    return this.videoPort;
  }
  
  public boolean isRelease() {
    return this.isInitDevices ^ true;
  }
  
  public void tryInterruptInit() {
    boolean bool = this.isInitDevices;
  }
  
  public void updateParams(String paramString) {
    if (!paramString.contains("anyka"))
      return; 
    HashMap hashMap = getParamsFormURL(paramString);
    setParams((String)hashMap.get("ip"), (String)hashMap.get("videoport"));
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/anykadevices/AnykaDevices.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */