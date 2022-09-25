package com.netopsun.fhdevices;

import android.text.TextUtils;
import android.util.Log;
import com.netopsun.deviceshub.base.CMDCommunicator;
import com.netopsun.deviceshub.base.ConnectHandler;
import com.netopsun.deviceshub.base.Devices;
import com.netopsun.deviceshub.base.RxTxCommunicator;
import com.netopsun.deviceshub.base.VideoCommunicator;
import com.netopsun.fhapi.FHDEV_NetLibrary;
import com.netopsun.fhapi.LPFHNP_Capacity_t_struct;
import com.sun.jna.Pointer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;

public class FHDevices extends Devices {
  private static final String TAG = "FHDevices";
  
  private volatile String aesKey = "guanxukj@fh8620.";
  
  private volatile int baudRate = 115200;
  
  private volatile String deviceFlag = "fh?";
  
  private volatile String devicesIP = "172.19.10.1";
  
  private final FHCMDCommunicator fhCmdCommunicator = new FHCMDCommunicator(this);
  
  private final FHRxTxCommunicator fhRxTxCommunicator = new FHRxTxCommunicator(this);
  
  private final FHVideoCommunicator fhVideoCommunicator = new FHVideoCommunicator(this);
  
  public volatile String firmwareFlag = "0000-00-00?";
  
  private volatile boolean isInitDevices = false;
  
  private volatile String password = "gxrdw60";
  
  private volatile String password2 = "gxrdw602";
  
  private volatile int port = 8866;
  
  private volatile Pointer userID;
  
  private volatile String userName = "guanxukeji";
  
  private volatile String userName2 = "guanxukeji2";
  
  static {
    System.loadLibrary("FHExtraJni");
  }
  
  private FHDevices(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6) {
    initConnectHandler(this.fhVideoCommunicator, this.fhCmdCommunicator, this.fhRxTxCommunicator);
    setParams(paramString1, paramString2, paramString3, paramString4, paramString5, paramString6);
  }
  
  public static Devices open(String paramString) {
    if (!paramString.contains("fh://"))
      return null; 
    HashMap hashMap = getParamsFormURL(paramString);
    return new FHDevices((String)hashMap.get("ip"), (String)hashMap.get("port"), (String)hashMap.get("userName"), (String)hashMap.get("password"), (String)hashMap.get("aesKey"), (String)hashMap.get("baudRate"));
  }
  
  private void setParams(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6) {
    if (!TextUtils.isEmpty(paramString1))
      this.devicesIP = paramString1; 
    if (!TextUtils.isEmpty(paramString6))
      this.baudRate = Integer.valueOf(paramString6).intValue(); 
    if (!TextUtils.isEmpty(paramString2))
      this.port = Integer.valueOf(paramString2).intValue(); 
    if (!TextUtils.isEmpty(paramString3))
      this.userName = paramString3; 
    if (!TextUtils.isEmpty(paramString4))
      this.password = paramString4; 
    if (!TextUtils.isEmpty(paramString5))
      this.aesKey = paramString5; 
    if ("null".equals(paramString5))
      this.aesKey = null; 
  }
  
  public static native long startPlayBack(long paramLong1, long paramLong2, ByteBuffer paramByteBuffer, OnFrameDataAvailableCallback paramOnFrameDataAvailableCallback);
  
  public static native long startRealPlay(long paramLong1, long paramLong2, ByteBuffer paramByteBuffer, OnFrameDataAvailableCallback paramOnFrameDataAvailableCallback);
  
  public static native long startSerialEx(ByteBuffer paramByteBuffer, long paramLong, int paramInt1, int paramInt2, byte paramByte, int paramInt3, OnSerialDataAvailableCallback paramOnSerialDataAvailableCallback);
  
  public static native int stopPlayBack(long paramLong);
  
  public static native int stopRealPlay(long paramLong);
  
  public static native int stopSerialEx(long paramLong);
  
  public static String time2Date(int paramInt) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(paramInt >> 16);
    int i = (0xFF00 & paramInt) >> 8;
    if (i < 10)
      stringBuilder.append("0"); 
    stringBuilder.append(i);
    paramInt &= 0xFF;
    if (paramInt < 10)
      stringBuilder.append("0"); 
    stringBuilder.append(paramInt);
    return stringBuilder.toString();
  }
  
  protected int IFNeedInitDevices() {
    if (!this.isInitDevices) {
      FHDEV_NetLibrary.INSTANCE.FHDEV_NET_Cleanup();
      int i = FHDEV_NetLibrary.INSTANCE.FHDEV_NET_Init();
      FHDEV_NetLibrary.INSTANCE.FHDEV_NET_SetHeartbeatTime(2000);
      if (i == 0) {
        i = FHDEV_NetLibrary.INSTANCE.FHDEV_NET_GetLastError();
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append(i);
        stringBuilder1.append("");
        Log.e("Init FHLibrary fail", stringBuilder1.toString());
        try {
          Thread.sleep(300L);
        } catch (InterruptedException interruptedException) {
          interruptedException.printStackTrace();
        } 
        return -1;
      } 
      byte[] arrayOfByte1 = this.devicesIP.getBytes();
      byte[] arrayOfByte2 = this.userName.getBytes();
      byte[] arrayOfByte3 = this.password.getBytes();
      byte[] arrayOfByte4 = this.userName2.getBytes();
      byte[] arrayOfByte5 = this.password2.getBytes();
      ByteBuffer byteBuffer1 = ByteBuffer.allocateDirect(arrayOfByte1.length + 1);
      byteBuffer1.put(arrayOfByte1);
      byteBuffer1.flip();
      ByteBuffer byteBuffer2 = ByteBuffer.allocateDirect(arrayOfByte2.length + 1);
      byteBuffer2.put(arrayOfByte2);
      byteBuffer2.flip();
      ByteBuffer byteBuffer3 = ByteBuffer.allocateDirect(arrayOfByte3.length + 1);
      byteBuffer3.put(arrayOfByte3);
      byteBuffer3.flip();
      ByteBuffer byteBuffer4 = ByteBuffer.allocateDirect(arrayOfByte4.length + 1);
      byteBuffer4.put(arrayOfByte4);
      byteBuffer4.flip();
      ByteBuffer byteBuffer5 = ByteBuffer.allocateDirect(arrayOfByte5.length + 1);
      byteBuffer5.put(arrayOfByte5);
      byteBuffer5.flip();
      if (this.aesKey != null) {
        byte[] arrayOfByte = this.aesKey.getBytes();
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(arrayOfByte.length + 1);
        byteBuffer.put(arrayOfByte);
        byteBuffer.flip();
        FHDEV_NetLibrary.INSTANCE.FHDEV_NET_SetCryptKey(byteBuffer);
      } 
      LPFHNP_Capacity_t_struct.ByReference byReference = new LPFHNP_Capacity_t_struct.ByReference();
      byReference.clear();
      if (this.userID != null)
        FHDEV_NetLibrary.INSTANCE.FHDEV_NET_Logout(this.userID); 
      this.userID = FHDEV_NetLibrary.INSTANCE.FHDEV_NET_Login(byteBuffer1, (short)this.port, byteBuffer2, byteBuffer3, (LPFHNP_Capacity_t_struct)byReference);
      byReference.setAutoSynch(true);
      byReference.autoRead();
      String str = time2Date(((LPFHNP_Capacity_t_struct)byReference).dwFWBuildDate);
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(str);
      stringBuilder.append(" bundle:");
      stringBuilder.append(((LPFHNP_Capacity_t_struct)byReference).dwFWVersion);
      this.firmwareFlag = stringBuilder.toString();
      if (this.userID == null) {
        i = FHDEV_NetLibrary.INSTANCE.FHDEV_NET_GetLastError();
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append(i);
        stringBuilder1.append("");
        Log.e("login fail", stringBuilder1.toString());
        try {
          Thread.sleep(300L);
        } catch (InterruptedException interruptedException) {
          interruptedException.printStackTrace();
        } 
        return -1;
      } 
      IntBuffer intBuffer = IntBuffer.allocate(1);
      FHDEV_NetLibrary.INSTANCE.FHDEV_NET_GetDeviceFlag(this.userID, intBuffer);
      this.isInitDevices = true;
    } 
    return 0;
  }
  
  public void close() {
    this.fhRxTxCommunicator.stopSendHeartBeatPackage();
    this.fhRxTxCommunicator.disconnect();
    this.fhVideoCommunicator.disconnect();
    this.fhCmdCommunicator.disconnect();
    if (!this.isInitDevices)
      return; 
    getConnectHandler().getHandler().post(new Runnable() {
          public void run() {
            if (FHDevices.this.userID != null) {
              if (FHDEV_NetLibrary.INSTANCE.FHDEV_NET_Logout(FHDevices.this.userID) > 0)
                FHDevices.access$002(FHDevices.this, (Pointer)null); 
              FHDEV_NetLibrary.INSTANCE.FHDEV_NET_Cleanup();
            } 
            FHDevices.access$102(FHDevices.this, false);
          }
        });
  }
  
  public int getBaudRate() {
    return this.baudRate;
  }
  
  public CMDCommunicator getCMDCommunicator() {
    return this.fhCmdCommunicator;
  }
  
  protected ConnectHandler getConnectHandler() {
    return super.getConnectHandler();
  }
  
  public String getDevicesFlag() {
    return this.deviceFlag;
  }
  
  public String getDevicesIP() {
    return this.devicesIP;
  }
  
  public String getDevicesName() {
    return "fhdevices";
  }
  
  public RxTxCommunicator getRxTxCommunicator() {
    return this.fhRxTxCommunicator;
  }
  
  public Pointer getUserID() {
    return this.userID;
  }
  
  public VideoCommunicator getVideoCommunicator() {
    return this.fhVideoCommunicator;
  }
  
  public boolean isRelease() {
    return getConnectHandler().isRelease();
  }
  
  public void release() {
    super.release();
  }
  
  public void setNoInitDevices() {
    this.isInitDevices = false;
  }
  
  public void updateParams(String paramString) {
    if (!paramString.contains("fh://"))
      return; 
    HashMap hashMap = getParamsFormURL(paramString);
    setParams((String)hashMap.get("ip"), (String)hashMap.get("port"), (String)hashMap.get("userName"), (String)hashMap.get("password"), (String)hashMap.get("aesKey"), (String)hashMap.get("baudRate"));
  }
  
  public static interface OnFrameDataAvailableCallback {
    void onFrameDataAvailable(int param1Int1, int param1Int2);
  }
  
  public static interface OnSerialDataAvailableCallback {
    void onSerialDataAvailable(int param1Int1, int param1Int2);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhdevices/FHDevices.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */