package com.netopsun.fhdevices;

import android.text.TextUtils;
import android.util.Log;
import com.netopsun.deviceshub.base.VideoCommunicator;
import com.netopsun.fhapi.FHDEV_NetLibrary;
import com.netopsun.fhapi.FHNP_Time_t;
import com.netopsun.fhapi.LPFHNP_Playback_t_struct;
import com.netopsun.fhapi.LPFHNP_Preview_t_struct;
import com.netopsun.fhapi.LPFHNP_RecSearch_t_struct;
import com.netopsun.fhapi.LPFHNP_Record_t_struct;
import com.sun.jna.Pointer;
import java.nio.ByteBuffer;
import java.util.AbstractMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

public class FHVideoCommunicator extends VideoCommunicator {
  private static final int CONNECT_SUCCESS = 0;
  
  private static final String TAG = "FHVideoCommunicator";
  
  private volatile int connectStatusFlagForIJK = -1;
  
  private final ByteBuffer directBuffer = ByteBuffer.allocateDirect(1000000);
  
  final FHDevices fhDevices;
  
  private final int frameBufferSize = 1000000;
  
  private volatile boolean isConnected = false;
  
  private volatile boolean isConnecting = false;
  
  private volatile long lastReadFrameSuccessTime;
  
  private final FHDevices.OnFrameDataAvailableCallback onFrameDataAvailableCallback = new FHDevices.OnFrameDataAvailableCallback() {
      volatile boolean isWaitKeyFrame = true;
      
      public void onFrameDataAvailable(int param1Int1, int param1Int2) {
        FHVideoCommunicator.access$002(FHVideoCommunicator.this, System.currentTimeMillis());
        if (this.isWaitKeyFrame) {
          if (param1Int2 != 0)
            return; 
          this.isWaitKeyFrame = false;
        } 
        Map.Entry<byte[], Integer> entry = FHVideoCommunicator.this.writableByteQueue.poll();
        if (entry == null) {
          this.isWaitKeyFrame = true;
          return;
        } 
        if (((byte[])entry.getKey()).length < param1Int1) {
          Log.e("FHVideoCommunicator", "接收帧数据大于缓存数组容量");
          FHVideoCommunicator.this.writableByteQueue.add(entry);
          return;
        } 
        FHVideoCommunicator.this.directBuffer.clear();
        FHVideoCommunicator.this.directBuffer.get(entry.getKey(), 0, param1Int1);
        entry.setValue(Integer.valueOf(param1Int1));
        FHVideoCommunicator.this.readableByteQueue.add(entry);
      }
    };
  
  private Pointer playHandle;
  
  private long playbackDuration;
  
  private String playbackFileName;
  
  private int quality = 2;
  
  ConcurrentLinkedQueue<Map.Entry<byte[], Integer>> readableByteQueue = new ConcurrentLinkedQueue<Map.Entry<byte[], Integer>>();
  
  ConcurrentLinkedQueue<Map.Entry<byte[], Integer>> writableByteQueue = new ConcurrentLinkedQueue<Map.Entry<byte[], Integer>>();
  
  protected FHVideoCommunicator(FHDevices paramFHDevices) {
    super(paramFHDevices);
    this.fhDevices = paramFHDevices;
  }
  
  private LPFHNP_Record_t_struct fileName2RStruct(Pointer paramPointer, String paramString) {
    LPFHNP_RecSearch_t_struct lPFHNP_RecSearch_t_struct = FHCMDCommunicator.getSearchReq(paramString);
    paramPointer = FHDEV_NetLibrary.INSTANCE.FHDEV_NET_SearchRecord(paramPointer, lPFHNP_RecSearch_t_struct);
    if (paramPointer == null)
      return null; 
    LPFHNP_Record_t_struct lPFHNP_Record_t_struct = new LPFHNP_Record_t_struct();
    if (FHDEV_NetLibrary.INSTANCE.FHDEV_NET_SearchNextRecord(paramPointer, lPFHNP_Record_t_struct) == 0)
      return null; 
    lPFHNP_Record_t_struct.read();
    FHDEV_NetLibrary.INSTANCE.FHDEV_NET_CloseSearchRecord(paramPointer);
    return lPFHNP_Record_t_struct;
  }
  
  public int connect() {
    this.connectStatusFlagForIJK = -11;
    this.currentReconnectTimes = 0;
    super.connect();
    return 0;
  }
  
  public int connectInternal() {
    this.isConnecting = true;
    Pointer pointer = this.fhDevices.getUserID();
    Integer integer = Integer.valueOf(0);
    if (pointer != null) {
      Pointer pointer1;
      this.readableByteQueue.clear();
      this.writableByteQueue.clear();
      byte[] arrayOfByte1 = new byte[1000000];
      byte[] arrayOfByte2 = new byte[1000000];
      byte[] arrayOfByte3 = new byte[1000000];
      this.writableByteQueue.add((Map.Entry)new AbstractMap.SimpleEntry<byte, Integer>(arrayOfByte1, integer));
      this.writableByteQueue.add((Map.Entry)new AbstractMap.SimpleEntry<byte, Integer>(arrayOfByte2, integer));
      this.writableByteQueue.add((Map.Entry)new AbstractMap.SimpleEntry<byte, Integer>(arrayOfByte3, integer));
      FHDEV_NetLibrary.INSTANCE.FHDEV_NET_DevMakeKeyFrame(pointer, (byte)0, (byte)0);
      arrayOfByte1 = null;
      integer = null;
      arrayOfByte2 = null;
      if (!TextUtils.isEmpty(this.playbackFileName)) {
        LPFHNP_Record_t_struct lPFHNP_Record_t_struct = fileName2RStruct(pointer, this.playbackFileName);
        Integer integer1 = integer;
        if (lPFHNP_Record_t_struct != null) {
          Pointer pointer2;
          LPFHNP_Playback_t_struct lPFHNP_Playback_t_struct = new LPFHNP_Playback_t_struct();
          lPFHNP_Playback_t_struct.clear();
          lPFHNP_Playback_t_struct.btChannel = (byte)lPFHNP_Record_t_struct.btChannel;
          lPFHNP_Playback_t_struct.btEncID = (byte)0;
          lPFHNP_Playback_t_struct.btTransMode = (byte)0;
          if (lPFHNP_Record_t_struct.btRecType == 0) {
            lPFHNP_Playback_t_struct.dwRecTypeMask = 0;
          } else {
            lPFHNP_Playback_t_struct.dwRecTypeMask |= 1 << lPFHNP_Record_t_struct.btRecType - 1;
          } 
          lPFHNP_Playback_t_struct.stStartTime = new FHNP_Time_t();
          lPFHNP_Playback_t_struct.stStopTime = new FHNP_Time_t();
          FHDEV_NetLibrary.INSTANCE.FHDEV_NET_TimeConvert(pointer, (int)(lPFHNP_Record_t_struct.ullStartTime / 1000000L), lPFHNP_Playback_t_struct.stStartTime);
          FHDEV_NetLibrary.INSTANCE.FHDEV_NET_TimeConvert(pointer, (int)(lPFHNP_Record_t_struct.ullStopTime / 1000000L), lPFHNP_Playback_t_struct.stStopTime);
          lPFHNP_Playback_t_struct.setAutoSynch(true);
          lPFHNP_Playback_t_struct.autoWrite();
          long l = FHDevices.startPlayBack(Pointer.nativeValue(pointer), Pointer.nativeValue(lPFHNP_Playback_t_struct.getPointer()), this.directBuffer, this.onFrameDataAvailableCallback);
          if (l != 0L)
            pointer2 = new Pointer(l); 
          pointer1 = pointer2;
          if (pointer2 != null) {
            FHDEV_NetLibrary.INSTANCE.FHDEV_NET_ClosePlayBackAudio(pointer2);
            pointer1 = pointer2;
          } 
        } 
      } else {
        LPFHNP_Preview_t_struct.ByReference byReference = new LPFHNP_Preview_t_struct.ByReference();
        byReference.clear();
        ((LPFHNP_Preview_t_struct)byReference).btTransMode = (byte)0;
        ((LPFHNP_Preview_t_struct)byReference).btBlocked = (byte)1;
        if (this.quality == 1) {
          ((LPFHNP_Preview_t_struct)byReference).btEncID = (byte)1;
        } else {
          ((LPFHNP_Preview_t_struct)byReference).btEncID = (byte)2;
        } 
        byReference.setAutoSynch(true);
        byReference.autoWrite();
        long l = FHDevices.startRealPlay(Pointer.nativeValue(pointer), Pointer.nativeValue(byReference.getPointer()), this.directBuffer, this.onFrameDataAvailableCallback);
        Pointer pointer2 = pointer1;
        if (l != 0L)
          pointer2 = new Pointer(l); 
        pointer1 = pointer2;
        if (pointer2 != null) {
          FHDEV_NetLibrary.INSTANCE.FHDEV_NET_CloseRealAudio(pointer2);
          pointer1 = pointer2;
        } 
      } 
      if (pointer1 == null) {
        int i = FHDEV_NetLibrary.INSTANCE.FHDEV_NET_GetLastError();
        if (i == 0 || i == 2002 || i == 2003)
          this.fhDevices.setNoInitDevices(); 
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("StartPlay fail: error ");
        stringBuilder.append(i);
        Log.e("FHVideoCommunicator", stringBuilder.toString());
        if (this.currentReconnectTimes >= this.shouldReconnectTimes) {
          this.connectStatusFlagForIJK = -1;
        } else {
          this.connectStatusFlagForIJK = -11;
        } 
        this.isConnecting = false;
        try {
          Thread.sleep(500L);
        } catch (InterruptedException interruptedException) {
          interruptedException.printStackTrace();
        } 
        return -1;
      } 
      this.playHandle = (Pointer)interruptedException;
      this.connectStatusFlagForIJK = 0;
      this.lastReadFrameSuccessTime = System.currentTimeMillis();
      this.isConnecting = false;
      this.isConnected = true;
      return 0;
    } 
    if (this.currentReconnectTimes >= this.shouldReconnectTimes) {
      this.connectStatusFlagForIJK = -1;
    } else {
      this.connectStatusFlagForIJK = -11;
    } 
    this.isConnecting = false;
    try {
      Thread.sleep(500L);
    } catch (InterruptedException interruptedException) {
      interruptedException.printStackTrace();
    } 
    return -1;
  }
  
  public int disconnect() {
    this.lastReadFrameSuccessTime = 0L;
    return super.disconnect();
  }
  
  public int disconnectInternal() {
    // Byte code:
    //   0: iconst_0
    //   1: istore_1
    //   2: aload_0
    //   3: iconst_0
    //   4: putfield isConnected : Z
    //   7: aload_0
    //   8: getfield playHandle : Lcom/sun/jna/Pointer;
    //   11: ifnull -> 60
    //   14: aload_0
    //   15: getfield playbackFileName : Ljava/lang/String;
    //   18: invokestatic isEmpty : (Ljava/lang/CharSequence;)Z
    //   21: istore_2
    //   22: iconst_1
    //   23: istore_3
    //   24: iload_2
    //   25: ifeq -> 44
    //   28: aload_0
    //   29: getfield playHandle : Lcom/sun/jna/Pointer;
    //   32: invokestatic nativeValue : (Lcom/sun/jna/Pointer;)J
    //   35: invokestatic stopRealPlay : (J)I
    //   38: ifle -> 60
    //   41: goto -> 62
    //   44: aload_0
    //   45: getfield playHandle : Lcom/sun/jna/Pointer;
    //   48: invokestatic nativeValue : (Lcom/sun/jna/Pointer;)J
    //   51: invokestatic stopPlayBack : (J)I
    //   54: ifle -> 60
    //   57: goto -> 62
    //   60: iconst_0
    //   61: istore_3
    //   62: iload_3
    //   63: ifeq -> 71
    //   66: aload_0
    //   67: aconst_null
    //   68: putfield playHandle : Lcom/sun/jna/Pointer;
    //   71: iload_3
    //   72: ifeq -> 80
    //   75: iload_1
    //   76: istore_3
    //   77: goto -> 82
    //   80: iconst_m1
    //   81: istore_3
    //   82: iload_3
    //   83: ireturn
  }
  
  public long getPlaybackCurrentPosition() {
    return 0L;
  }
  
  public long getPlaybackDuration() {
    return this.playbackDuration;
  }
  
  public boolean isConnected() {
    return this.isConnected;
  }
  
  protected boolean isConnectingVideo() {
    return this.isConnecting;
  }
  
  public int maxFrameSize() {
    return 1000000;
  }
  
  public int read(ByteBuffer paramByteBuffer, int paramInt) {
    if (this.isConnecting)
      return -11; 
    if (this.connectStatusFlagForIJK != 0)
      return this.connectStatusFlagForIJK; 
    Map.Entry<byte[], Integer> entry = this.readableByteQueue.poll();
    if (entry != null) {
      paramInt = ((Integer)entry.getValue()).intValue();
      paramByteBuffer.clear();
      paramByteBuffer.put((byte[])entry.getKey(), 0, paramInt);
      this.writableByteQueue.add(entry);
      return paramInt;
    } 
    if (this.lastReadFrameSuccessTime != 0L && System.currentTimeMillis() - this.lastReadFrameSuccessTime > this.readFrameTimeOut) {
      reconnectVideo();
      this.lastReadFrameSuccessTime = System.currentTimeMillis();
    } 
    return -11;
  }
  
  protected void reconnectVideo() {
    this.fhDevices.getConnectHandler().notifyReconnectVideo();
  }
  
  public int seek(long paramLong, int paramInt) {
    if (this.playHandle != null && !TextUtils.isEmpty(this.playbackFileName))
      FHDEV_NetLibrary.INSTANCE.FHDEV_NET_JumpPlayBack(this.playHandle, (int)paramLong); 
    return 0;
  }
  
  public void setPlaybackDuration(String paramString) {
    try {
      this.playbackDuration = Long.valueOf(paramString).longValue();
    } catch (Exception exception) {
      exception.printStackTrace();
    } 
  }
  
  public void setPlaybackUrl(String paramString) {
    this.playbackFileName = paramString;
  }
  
  public void setVideoDefaultQuality(int paramInt) {
    this.quality = paramInt;
  }
  
  public boolean usingMediaCodeC() {
    return true;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhdevices/FHVideoCommunicator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */