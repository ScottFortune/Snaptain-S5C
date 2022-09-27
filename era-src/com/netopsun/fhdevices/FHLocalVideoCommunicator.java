package com.netopsun.fhdevices;

import android.util.Log;
import com.netopsun.deviceshub.base.VideoCommunicator;
import com.netopsun.fhapi.FHDEV_NetExLibrary;
import com.netopsun.fhapi.FHDEV_NetLibrary;
import com.netopsun.fhapi.FHNP_VFrameHead_t;
import com.netopsun.fhapi.LPFHNP_FrameHead_t_union;
import com.sun.jna.Pointer;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.util.AbstractMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

public class FHLocalVideoCommunicator extends VideoCommunicator {
  private static final String TAG = "FHLocalVideo";
  
  private final FHDEV_NetLibrary.fDataCallBack fDataCallBack = new FHDEV_NetLibrary.fDataCallBack() {
      volatile boolean isWaitKeyFrame = true;
      
      public void apply(Pointer param1Pointer1, int param1Int1, LPFHNP_FrameHead_t_union param1LPFHNP_FrameHead_t_union, Pointer param1Pointer2, int param1Int2, Pointer param1Pointer3) {
        if (this.isWaitKeyFrame) {
          param1LPFHNP_FrameHead_t_union.setType(FHNP_VFrameHead_t.class);
          param1LPFHNP_FrameHead_t_union.setAutoSynch(true);
          param1LPFHNP_FrameHead_t_union.autoRead();
          param1LPFHNP_FrameHead_t_union.stVFrameHead.setAutoSynch(true);
          param1LPFHNP_FrameHead_t_union.stVFrameHead.autoRead();
          if (param1LPFHNP_FrameHead_t_union.stVFrameHead.btFrameType != 0)
            return; 
          this.isWaitKeyFrame = false;
        } 
        Map.Entry<byte[], Integer> entry = FHLocalVideoCommunicator.this.writableByteQueue.poll();
        if (entry == null) {
          this.isWaitKeyFrame = true;
          return;
        } 
        if (((byte[])entry.getKey()).length < param1Int2) {
          Log.e("FHLocalVideo", "接收帧数据大于缓存数组容量");
          FHLocalVideoCommunicator.this.writableByteQueue.add(entry);
          return;
        } 
        param1Pointer2.read(0L, entry.getKey(), 0, param1Int2);
        entry.setValue(Integer.valueOf(param1Int2));
        FHLocalVideoCommunicator.this.readableByteQueue.add(entry);
      }
    };
  
  private final int frameBufferSize = 1000000;
  
  private Pointer playHandle;
  
  private long playbackDuration;
  
  private String playbackFileName;
  
  ConcurrentLinkedQueue<Map.Entry<byte[], Integer>> readableByteQueue = new ConcurrentLinkedQueue<Map.Entry<byte[], Integer>>();
  
  ConcurrentLinkedQueue<Map.Entry<byte[], Integer>> writableByteQueue = new ConcurrentLinkedQueue<Map.Entry<byte[], Integer>>();
  
  public FHLocalVideoCommunicator() {
    super(null);
    FHDEV_NetLibrary.INSTANCE.FHDEV_NET_Cleanup();
    FHDEV_NetLibrary.INSTANCE.FHDEV_NET_Init();
  }
  
  public FHLocalVideoCommunicator(String paramString) {
    super(null);
    this.playbackFileName = paramString;
    FHDEV_NetLibrary.INSTANCE.FHDEV_NET_Cleanup();
    FHDEV_NetLibrary.INSTANCE.FHDEV_NET_Init();
  }
  
  public int connect() {
    StringBuilder stringBuilder;
    this.readableByteQueue.clear();
    this.writableByteQueue.clear();
    byte[] arrayOfByte1 = new byte[1000000];
    byte[] arrayOfByte2 = new byte[1000000];
    byte[] arrayOfByte3 = new byte[1000000];
    ConcurrentLinkedQueue<Map.Entry<byte[], Integer>> concurrentLinkedQueue = this.writableByteQueue;
    Integer integer = Integer.valueOf(0);
    concurrentLinkedQueue.add((Map.Entry)new AbstractMap.SimpleEntry<byte, Integer>(arrayOfByte1, integer));
    this.writableByteQueue.add((Map.Entry)new AbstractMap.SimpleEntry<byte, Integer>(arrayOfByte2, integer));
    this.writableByteQueue.add((Map.Entry)new AbstractMap.SimpleEntry<byte, Integer>(arrayOfByte3, integer));
    this.playbackFileName = (new File(this.playbackFileName)).getAbsolutePath();
    String str = this.playbackFileName;
    if (str == null)
      return -1; 
    ByteBuffer byteBuffer = ByteBuffer.allocateDirect((str.getBytes()).length + 1);
    byteBuffer.order(ByteOrder.nativeOrder());
    byteBuffer.put(this.playbackFileName.getBytes());
    byteBuffer.flip();
    Pointer pointer = FHDEV_NetExLibrary.INSTANCE.FHDEV_NET_StartRecPlay(byteBuffer, 0, this.fDataCallBack, Pointer.NULL);
    if (pointer == null) {
      stringBuilder = new StringBuilder();
      stringBuilder.append("open fail:");
      stringBuilder.append(this.playbackFileName);
      stringBuilder.append(" error:");
      stringBuilder.append(FHDEV_NetLibrary.INSTANCE.FHDEV_NET_GetLastError());
      Log.e("FHLocalVideo", stringBuilder.toString());
      return -1;
    } 
    this.playHandle = (Pointer)stringBuilder;
    FHDEV_NetLibrary.INSTANCE.FHDEV_NET_ClosePlayBackAudio((Pointer)stringBuilder);
    return 0;
  }
  
  public int connectInternal() {
    return 0;
  }
  
  public void continuePlay() {
    if (this.playHandle != null)
      FHDEV_NetExLibrary.INSTANCE.FHDEV_NET_RecPlayCtrl(this.playHandle, 4, 0); 
  }
  
  public int disconnect() {
    FHDEV_NetExLibrary.INSTANCE.FHDEV_NET_StopRecPlay(this.playHandle);
    return 0;
  }
  
  public int disconnectInternal() {
    return 0;
  }
  
  public long getPlaybackCurrentPosition() {
    if (this.playHandle == null)
      return 0L; 
    IntBuffer intBuffer = IntBuffer.allocate(1);
    FHDEV_NetExLibrary.INSTANCE.FHDEV_NET_GetRecPlayProgress(this.playHandle, intBuffer);
    if (this.playbackDuration == 0L)
      getPlaybackDuration(); 
    double d1 = intBuffer.get();
    Double.isNaN(d1);
    double d2 = d1 / 100.0D;
    d1 = this.playbackDuration;
    Double.isNaN(d1);
    return (long)(d2 * d1);
  }
  
  public long getPlaybackDuration() {
    if (this.playHandle == null)
      return 0L; 
    LongBuffer longBuffer1 = LongBuffer.allocate(1);
    LongBuffer longBuffer2 = LongBuffer.allocate(1);
    FHDEV_NetExLibrary.INSTANCE.FHDEV_NET_GetRecPlayTimeInfo(this.playHandle, longBuffer1, longBuffer2);
    this.playbackDuration = (longBuffer2.get() - longBuffer1.get()) / 1000L;
    return this.playbackDuration;
  }
  
  public boolean isConnected() {
    return true;
  }
  
  public int maxFrameSize() {
    return 1000000;
  }
  
  public void pause() {
    if (this.playHandle != null)
      FHDEV_NetExLibrary.INSTANCE.FHDEV_NET_RecPlayCtrl(this.playHandle, 3, 0); 
  }
  
  public int read(ByteBuffer paramByteBuffer, int paramInt) {
    Map.Entry<byte[], Integer> entry = this.readableByteQueue.poll();
    if (entry != null) {
      paramInt = ((Integer)entry.getValue()).intValue();
      paramByteBuffer.clear();
      paramByteBuffer.put((byte[])entry.getKey(), 0, paramInt);
      this.writableByteQueue.add(entry);
      return paramInt;
    } 
    return -11;
  }
  
  public int seek(long paramLong, int paramInt) {
    if (this.playHandle == null)
      return 0; 
    FHDEV_NetExLibrary.INSTANCE.FHDEV_NET_JumpRecPlay(this.playHandle, (int)paramLong);
    return 0;
  }
  
  public void setPlaybackUrl(String paramString) {
    this.playbackFileName = paramString;
  }
  
  public boolean usingMediaCodeC() {
    return false;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhdevices/FHLocalVideoCommunicator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */