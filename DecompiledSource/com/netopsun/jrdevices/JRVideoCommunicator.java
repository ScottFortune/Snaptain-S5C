package com.netopsun.jrdevices;

import com.netopsun.deviceshub.base.VideoCommunicator;
import com.netopsun.live555.Live555RTSPClient;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.nio.ByteBuffer;
import java.util.AbstractMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class JRVideoCommunicator extends VideoCommunicator {
  private static final int CONNECT_SUCCESS = 0;
  
  private static final String TAG = "JRVideoCommunicator";
  
  private final byte[] HEAD_BYTES = new byte[] { -1, -35, 0, 4 };
  
  private final byte[] HEART_BEAT_BYTES = new byte[1];
  
  private volatile int connectStatusFlagForIJK = -1;
  
  private CountDownLatch countDownLatch;
  
  private final int frameBufferSize = 500000;
  
  private volatile boolean isConnected = false;
  
  private volatile boolean isConnecting = false;
  
  final JRDevices jrDevices;
  
  private volatile long lastReadFrameSuccessTime;
  
  private int quality = 2;
  
  ConcurrentLinkedQueue<Map.Entry<byte[], Integer>> readableByteQueue = new ConcurrentLinkedQueue<Map.Entry<byte[], Integer>>();
  
  final Live555RTSPClient rtspClient = new Live555RTSPClient();
  
  private Disposable sendHeartBeatTask;
  
  ConcurrentLinkedQueue<Map.Entry<byte[], Integer>> writableByteQueue = new ConcurrentLinkedQueue<Map.Entry<byte[], Integer>>();
  
  protected JRVideoCommunicator(JRDevices paramJRDevices) {
    super(paramJRDevices);
    this.jrDevices = paramJRDevices;
  }
  
  private Live555RTSPClient.RTSPCallback getRTSPCallback(final CountDownLatch countDownLatch, final boolean[] connectRes) {
    return new Live555RTSPClient.RTSPCallback() {
        public void onConnectFail(String param1String) {
          countDownLatch.countDown();
          connectRes[0] = false;
        }
        
        public void onConnectFinish() {}
        
        public void onConnectSuccess(String param1String) {
          JRVideoCommunicator.this.readableByteQueue.clear();
          JRVideoCommunicator.this.writableByteQueue.clear();
          byte[] arrayOfByte2 = new byte[500000];
          byte[] arrayOfByte1 = new byte[500000];
          ConcurrentLinkedQueue<Map.Entry<byte[], Integer>> concurrentLinkedQueue = JRVideoCommunicator.this.writableByteQueue;
          Integer integer = Integer.valueOf(0);
          concurrentLinkedQueue.add((Map.Entry)new AbstractMap.SimpleEntry<byte, Integer>(arrayOfByte2, integer));
          JRVideoCommunicator.this.writableByteQueue.add((Map.Entry)new AbstractMap.SimpleEntry<byte, Integer>(arrayOfByte1, integer));
          countDownLatch.countDown();
          connectRes[0] = true;
        }
        
        public void onFrameDataAvailable(ByteBuffer param1ByteBuffer, int param1Int1, int param1Int2) {
          JRVideoCommunicator.access$002(JRVideoCommunicator.this, System.currentTimeMillis());
          Map.Entry<byte[], Integer> entry = JRVideoCommunicator.this.writableByteQueue.poll();
          if (entry != null) {
            param1ByteBuffer.clear();
            param1ByteBuffer.get((byte[])entry.getKey(), 0, param1Int1);
            entry.setValue(Integer.valueOf(param1Int1));
            byte[] arrayOfByte = (byte[])entry.getKey();
            for (param1Int2 = 0; param1Int2 < param1Int1; param1Int2++) {
              int i = param1Int2 + 5;
              if (i < param1Int1 && arrayOfByte[param1Int2] == JRVideoCommunicator.this.HEAD_BYTES[0] && arrayOfByte[param1Int2 + 1] == JRVideoCommunicator.this.HEAD_BYTES[1] && arrayOfByte[param1Int2 + 2] == JRVideoCommunicator.this.HEAD_BYTES[2] && arrayOfByte[param1Int2 + 3] == JRVideoCommunicator.this.HEAD_BYTES[3]) {
                arrayOfByte[param1Int2 + 4] = (byte)0;
                arrayOfByte[i] = (byte)40;
                break;
              } 
            } 
            JRVideoCommunicator.this.readableByteQueue.add(entry);
          } 
        }
      };
  }
  
  public int connect() {
    this.connectStatusFlagForIJK = -11;
    super.connect();
    return 0;
  }
  
  public int connectInternal() {
    this.isConnecting = true;
    this.countDownLatch = new CountDownLatch(1);
    boolean[] arrayOfBoolean = new boolean[1];
    arrayOfBoolean[0] = false;
    this.rtspClient.setUseTCP(false);
    this.rtspClient.setSendRTCPReport(false);
    Live555RTSPClient live555RTSPClient = this.rtspClient;
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("rtsp://");
    stringBuilder.append(this.jrDevices.getDevicesIP());
    stringBuilder.append(":");
    stringBuilder.append(this.jrDevices.getRtspPort());
    stringBuilder.append("/webcam");
    live555RTSPClient.open(stringBuilder.toString(), 500000, getRTSPCallback(this.countDownLatch, arrayOfBoolean));
    try {
      this.countDownLatch.await();
    } catch (InterruptedException interruptedException) {
      interruptedException.printStackTrace();
    } 
    if (!arrayOfBoolean[0]) {
      try {
        this.rtspClient.exitWithOutShutdownRTSP();
        Thread.sleep(500L);
      } catch (InterruptedException interruptedException) {
        interruptedException.printStackTrace();
      } 
      if (this.currentReconnectTimes >= this.shouldReconnectTimes) {
        this.connectStatusFlagForIJK = -1;
      } else {
        this.connectStatusFlagForIJK = -11;
      } 
      this.isConnecting = false;
      return -1;
    } 
    this.connectStatusFlagForIJK = 0;
    this.isConnected = true;
    this.lastReadFrameSuccessTime = System.currentTimeMillis();
    Disposable disposable = this.sendHeartBeatTask;
    if (disposable != null)
      disposable.dispose(); 
    this.sendHeartBeatTask = Observable.interval(10L, TimeUnit.SECONDS).observeOn(Schedulers.newThread()).subscribe(new Consumer<Long>() {
          public void accept(Long param1Long) throws Exception {
            JRVideoCommunicator.this.rtspClient.sendRTCPBytes(JRVideoCommunicator.this.HEART_BEAT_BYTES);
            if (param1Long.longValue() % 3L == 0L)
              JRVideoCommunicator.this.rtspClient.sendOptionsCommand(); 
          }
        });
    this.isConnecting = false;
    return 0;
  }
  
  public int disconnect() {
    CountDownLatch countDownLatch = this.countDownLatch;
    if (countDownLatch != null)
      countDownLatch.countDown(); 
    this.lastReadFrameSuccessTime = 0L;
    return super.disconnect();
  }
  
  public int disconnectInternal() {
    this.isConnected = false;
    Disposable disposable = this.sendHeartBeatTask;
    if (disposable != null)
      disposable.dispose(); 
    this.rtspClient.exitWithOutShutdownRTSP();
    try {
      Thread.sleep(500L);
    } catch (InterruptedException interruptedException) {
      interruptedException.printStackTrace();
    } 
    return 0;
  }
  
  public Live555RTSPClient getRtspClient() {
    return this.rtspClient;
  }
  
  public boolean isConnected() {
    return this.isConnected;
  }
  
  public int maxFrameSize() {
    return 500000;
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
      this.jrDevices.getConnectHandler().notifyReconnectVideo();
      this.lastReadFrameSuccessTime = System.currentTimeMillis();
    } 
    return -11;
  }
  
  public int seek(long paramLong, int paramInt) {
    return 0;
  }
  
  public void setVideoDefaultQuality(int paramInt) {
    this.quality = paramInt;
  }
  
  public boolean usingMediaCodeC() {
    return false;
  }
  
  public String videoStreamFormat() {
    return "mjpeg";
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/jrdevices/JRVideoCommunicator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */