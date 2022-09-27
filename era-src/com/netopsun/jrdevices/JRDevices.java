package com.netopsun.jrdevices;

import android.text.TextUtils;
import com.netopsun.deviceshub.base.CMDCommunicator;
import com.netopsun.deviceshub.base.ConnectHandler;
import com.netopsun.deviceshub.base.Devices;
import com.netopsun.deviceshub.base.RxTxCommunicator;
import com.netopsun.deviceshub.base.VideoCommunicator;
import java.util.HashMap;

public class JRDevices extends Devices {
  private static final String TAG = "jrDevices";
  
  private int cmdPort = 7777;
  
  private String devicesIP = "192.168.99.1";
  
  private volatile boolean isInitDevices = false;
  
  private final JRCMDCommunicator jrCmdCommunicator;
  
  private final JRRxTxCommunicator jrRxTxCommunicator;
  
  private final JRVideoCommunicator jrVideoCommunicator;
  
  private int receiveJPGPort = 40000;
  
  private int rtspPort = 7070;
  
  private int rxtxPort = 1024;
  
  private int tcpCMDPort = 5000;
  
  private JRDevices(String paramString1, String paramString2) {
    setParams(paramString1, paramString2);
    this.jrVideoCommunicator = new JRVideoCommunicator(this);
    this.jrCmdCommunicator = new JRCMDCommunicator(this);
    this.jrRxTxCommunicator = new JRRxTxCommunicator(this);
    initConnectHandler(this.jrVideoCommunicator, this.jrCmdCommunicator, this.jrRxTxCommunicator);
  }
  
  public static Devices open(String paramString) {
    if (!paramString.contains("jr"))
      return null; 
    HashMap hashMap = getParamsFormURL(paramString);
    return new JRDevices((String)hashMap.get("rtspip"), (String)hashMap.get("rtspport"));
  }
  
  private void setParams(String paramString1, String paramString2) {
    if (!TextUtils.isEmpty(paramString1))
      this.devicesIP = paramString1; 
    if (!TextUtils.isEmpty(paramString2))
      this.rtspPort = Integer.valueOf(paramString2).intValue(); 
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
    this.jrRxTxCommunicator.stopSendHeartBeatPackage();
    this.jrRxTxCommunicator.disconnect();
    this.jrVideoCommunicator.disconnect();
    this.jrCmdCommunicator.disconnect();
  }
  
  public CMDCommunicator getCMDCommunicator() {
    return this.jrCmdCommunicator;
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
    return "jrdevices";
  }
  
  public int getReceiveJPGPort() {
    return this.receiveJPGPort;
  }
  
  public int getRtspPort() {
    return this.rtspPort;
  }
  
  public RxTxCommunicator getRxTxCommunicator() {
    return this.jrRxTxCommunicator;
  }
  
  public int getRxtxPort() {
    return this.rxtxPort;
  }
  
  public int getTcpCMDPort() {
    return this.tcpCMDPort;
  }
  
  public VideoCommunicator getVideoCommunicator() {
    return this.jrVideoCommunicator;
  }
  
  public boolean isRelease() {
    return this.isInitDevices ^ true;
  }
  
  public void tryInterruptInit() {
    boolean bool = this.isInitDevices;
  }
  
  public void updateParams(String paramString) {
    if (!paramString.contains("jr"))
      return; 
    HashMap hashMap = getParamsFormURL(paramString);
    setParams((String)hashMap.get("rtspip"), (String)hashMap.get("rtspport"));
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/jrdevices/JRDevices.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */