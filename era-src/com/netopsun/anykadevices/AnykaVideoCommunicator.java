package com.netopsun.anykadevices;

import android.text.TextUtils;
import com.netopsun.deviceshub.base.CMDCommunicator;
import com.netopsun.deviceshub.base.VideoCommunicator;
import com.netopsun.deviceshub.interfaces.Cancelable;
import com.netopsun.deviceshub.interfaces.NothingCancel;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

public class AnykaVideoCommunicator extends VideoCommunicator {
  private static final byte[] ANYKA_PING = new byte[] { 
      108, 101, 119, 101, 105, 95, 99, 109, 100, 0, 
      1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
      0, 0, 0, 0, 0, 0 };
  
  private static final int CONNECT_SUCCESS = 0;
  
  private static final byte[] START_PLAY_BACK_CMD;
  
  private static final byte[] START_VIDEO_720P_CMD = new byte[] { 
      108, 101, 119, 101, 105, 95, 99, 109, 100, 0, 
      2, 0, 0, 0, 1, 0, 0, 0, 0, 0, 
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
      0, 0, 0, 0, 0, 0 };
  
  private static final byte[] START_VIDEO_VGA_CMD = new byte[] { 
      108, 101, 119, 101, 105, 95, 99, 109, 100, 0, 
      2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
      0, 0, 0, 0, 0, 0 };
  
  private static final String TAG = "AnykaVideoCommunicator";
  
  final AnykaDevices anykaDevices;
  
  public ConnectInternalCallback connectInternalCallback;
  
  private volatile int connectStatusFlagForIJK = -1;
  
  private int duration;
  
  private final int frameBufferSize = 900000;
  
  private volatile boolean isConnected = false;
  
  private volatile boolean isConnecting = false;
  
  boolean isRemoteRecording = false;
  
  private volatile long lastReadFrameSuccessTime;
  
  private CMDCommunicator.OnRemoteCMDListener onRemoteCMDListener;
  
  private String playBackName;
  
  private int quality = 2;
  
  ConcurrentLinkedQueue<Map.Entry<byte[], Integer>> readableByteQueue = new ConcurrentLinkedQueue<Map.Entry<byte[], Integer>>();
  
  private Disposable receiveVideoFrameTask;
  
  private Disposable sendHeartBeatTask;
  
  private int startTime;
  
  private volatile Socket videoSocket = new Socket();
  
  ConcurrentLinkedQueue<Map.Entry<byte[], Integer>> writableByteQueue = new ConcurrentLinkedQueue<Map.Entry<byte[], Integer>>();
  
  static {
    START_PLAY_BACK_CMD = new byte[] { 
        108, 101, 119, 101, 105, 95, 99, 109, 100, 0, 
        9, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 124, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0 };
  }
  
  protected AnykaVideoCommunicator(AnykaDevices paramAnykaDevices) {
    super(paramAnykaDevices);
    this.anykaDevices = paramAnykaDevices;
  }
  
  private void connectVideoSocket(int paramInt) throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield videoSocket : Ljava/net/Socket;
    //   6: ifnull -> 34
    //   9: aload_0
    //   10: getfield videoSocket : Ljava/net/Socket;
    //   13: invokevirtual isClosed : ()Z
    //   16: ifne -> 34
    //   19: aload_0
    //   20: getfield videoSocket : Ljava/net/Socket;
    //   23: invokevirtual isConnected : ()Z
    //   26: istore_2
    //   27: iload_2
    //   28: ifeq -> 34
    //   31: aload_0
    //   32: monitorexit
    //   33: return
    //   34: new java/net/Socket
    //   37: astore_3
    //   38: aload_3
    //   39: invokespecial <init> : ()V
    //   42: aload_0
    //   43: aload_3
    //   44: putfield videoSocket : Ljava/net/Socket;
    //   47: new java/net/InetSocketAddress
    //   50: astore_3
    //   51: aload_3
    //   52: aload_0
    //   53: getfield anykaDevices : Lcom/netopsun/anykadevices/AnykaDevices;
    //   56: invokevirtual getDevicesIP : ()Ljava/lang/String;
    //   59: aload_0
    //   60: getfield anykaDevices : Lcom/netopsun/anykadevices/AnykaDevices;
    //   63: invokevirtual getVideoPort : ()I
    //   66: invokespecial <init> : (Ljava/lang/String;I)V
    //   69: aload_0
    //   70: getfield videoSocket : Ljava/net/Socket;
    //   73: aload_3
    //   74: iload_1
    //   75: sipush #1000
    //   78: imul
    //   79: invokevirtual connect : (Ljava/net/SocketAddress;I)V
    //   82: aload_0
    //   83: monitorexit
    //   84: return
    //   85: astore_3
    //   86: aload_0
    //   87: monitorexit
    //   88: aload_3
    //   89: athrow
    // Exception table:
    //   from	to	target	type
    //   2	27	85	finally
    //   34	82	85	finally
  }
  
  private AnykaVideoFrameDataExtractor.OnFrameDataCallback getFrameDataCallback() {
    return new AnykaVideoFrameDataExtractor.OnFrameDataCallback() {
        public void onFrameDataAvailable(byte[] param1ArrayOfbyte, int param1Int1, int param1Int2) {
          AnykaVideoCommunicator.access$102(AnykaVideoCommunicator.this, System.currentTimeMillis());
          Map.Entry<byte[], Integer> entry = AnykaVideoCommunicator.this.writableByteQueue.poll();
          if (entry != null) {
            System.arraycopy(param1ArrayOfbyte, param1Int1, entry.getKey(), 0, param1Int2);
            entry.setValue(Integer.valueOf(param1Int2));
            AnykaVideoCommunicator.this.readableByteQueue.add(entry);
          } 
        }
        
        public void onRemoteRecord() {
          if (AnykaVideoCommunicator.this.onRemoteCMDListener != null)
            if (AnykaVideoCommunicator.this.isRemoteRecording) {
              AnykaVideoCommunicator.this.onRemoteCMDListener.onRemoteStopRecord();
            } else {
              AnykaVideoCommunicator.this.onRemoteCMDListener.onRemoteStartRecord();
            }  
          AnykaVideoCommunicator anykaVideoCommunicator = AnykaVideoCommunicator.this;
          anykaVideoCommunicator.isRemoteRecording ^= 0x1;
        }
        
        public void onRemoteTakePhoto() {
          if (AnykaVideoCommunicator.this.onRemoteCMDListener != null)
            AnykaVideoCommunicator.this.onRemoteCMDListener.onRemoteTakePhoto(); 
        }
      };
  }
  
  private static byte[] intToByteArray(int paramInt) {
    byte b1 = (byte)(paramInt >> 24 & 0xFF);
    byte b2 = (byte)(paramInt >> 16 & 0xFF);
    byte b3 = (byte)(paramInt >> 8 & 0xFF);
    return new byte[] { (byte)(paramInt & 0xFF), b3, b2, b1 };
  }
  
  private void interruptConnectVideoSocket() {
    if (!this.videoSocket.isClosed())
      try {
        this.videoSocket.close();
      } catch (IOException iOException) {
        iOException.printStackTrace();
      }  
  }
  
  public int connect() {
    this.connectStatusFlagForIJK = -11;
    super.connect();
    return 0;
  }
  
  public int connectInternal() {
    Disposable disposable = this.receiveVideoFrameTask;
    if (disposable != null && !disposable.isDisposed())
      this.receiveVideoFrameTask.dispose(); 
    disposable = this.sendHeartBeatTask;
    if (disposable != null && !disposable.isDisposed())
      this.sendHeartBeatTask.dispose(); 
    if (!this.anykaDevices.getCMDCommunicator().isConnected()) {
      this.anykaDevices.getConnectHandler().notifyConnectCMD();
      return -1;
    } 
    this.isConnecting = true;
    try {
      connectVideoSocket(5);
      final InputStream inputStream = this.videoSocket.getInputStream();
      if (TextUtils.isEmpty(this.playBackName)) {
        if (this.quality == 1) {
          this.videoSocket.getOutputStream().write(START_VIDEO_720P_CMD);
        } else if (this.quality == 2) {
          this.videoSocket.getOutputStream().write(START_VIDEO_VGA_CMD);
        } 
      } else {
        byte[] arrayOfByte1 = Arrays.copyOf(START_PLAY_BACK_CMD, START_PLAY_BACK_CMD.length + 124);
        byte[] arrayOfByte2 = this.playBackName.getBytes();
        System.arraycopy(intToByteArray(this.startTime), 0, arrayOfByte1, START_PLAY_BACK_CMD.length, 4);
        System.arraycopy(intToByteArray(this.startTime + this.duration), 0, arrayOfByte1, START_PLAY_BACK_CMD.length + 4, 4);
        System.arraycopy(arrayOfByte2, 0, arrayOfByte1, START_PLAY_BACK_CMD.length + 24, arrayOfByte2.length);
        this.videoSocket.getOutputStream().write(arrayOfByte1);
      } 
      this.videoSocket.getOutputStream().flush();
      ConnectInternalCallback connectInternalCallback = this.connectInternalCallback;
      if (connectInternalCallback != null)
        connectInternalCallback.connectSuccess(); 
      this.connectStatusFlagForIJK = 0;
      this.isConnected = true;
      this.lastReadFrameSuccessTime = System.currentTimeMillis();
      this.isConnecting = false;
      this.receiveVideoFrameTask = Observable.create(new ObservableOnSubscribe<Object>() {
            public void subscribe(ObservableEmitter<Object> param1ObservableEmitter) throws Exception {
              AnykaVideoCommunicator.this.readableByteQueue.clear();
              AnykaVideoCommunicator.this.writableByteQueue.clear();
              byte[] arrayOfByte1 = new byte[900000];
              byte[] arrayOfByte2 = new byte[900000];
              ConcurrentLinkedQueue<Map.Entry<byte[], Integer>> concurrentLinkedQueue = AnykaVideoCommunicator.this.writableByteQueue;
              Integer integer = Integer.valueOf(0);
              concurrentLinkedQueue.add((Map.Entry)new AbstractMap.SimpleEntry<byte, Integer>(arrayOfByte1, integer));
              AnykaVideoCommunicator.this.writableByteQueue.add((Map.Entry)new AbstractMap.SimpleEntry<byte, Integer>(arrayOfByte2, integer));
              byte[] arrayOfByte3 = new byte[1024];
              AnykaVideoFrameDataExtractor anykaVideoFrameDataExtractor = new AnykaVideoFrameDataExtractor();
              anykaVideoFrameDataExtractor.setOnFrameDataCallback(AnykaVideoCommunicator.this.getFrameDataCallback());
              while (!param1ObservableEmitter.isDisposed()) {
                try {
                  anykaVideoFrameDataExtractor.onVideoData(arrayOfByte3, inputStream.read(arrayOfByte3));
                } catch (Exception exception) {
                  exception.printStackTrace();
                  break;
                } 
              } 
              param1ObservableEmitter.onComplete();
            }
          }).unsubscribeOn(Schedulers.newThread()).subscribeOn(Schedulers.newThread()).subscribe();
      this.sendHeartBeatTask = Observable.interval(1L, 1L, TimeUnit.SECONDS).observeOn(Schedulers.io()).subscribe(new Consumer<Long>() {
            public void accept(Long param1Long) throws Exception {
              if (AnykaVideoCommunicator.this.videoSocket != null)
                try {
                  AnykaVideoCommunicator.this.videoSocket.getOutputStream().write(AnykaVideoCommunicator.ANYKA_PING);
                } catch (Exception exception) {
                  exception.printStackTrace();
                }  
            }
          });
      return 0;
    } catch (IOException iOException) {
      try {
        this.videoSocket.close();
      } catch (IOException iOException1) {
        iOException1.printStackTrace();
      } 
      iOException.printStackTrace();
      try {
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
  }
  
  public int disconnect() {
    interruptConnectVideoSocket();
    this.lastReadFrameSuccessTime = 0L;
    return super.disconnect();
  }
  
  public int disconnectInternal() {
    Disposable disposable = this.receiveVideoFrameTask;
    if (disposable != null && !disposable.isDisposed()) {
      this.anykaDevices.tryInterruptInit();
      this.receiveVideoFrameTask.dispose();
    } 
    disposable = this.sendHeartBeatTask;
    if (disposable != null && !disposable.isDisposed())
      this.sendHeartBeatTask.dispose(); 
    this.isConnected = false;
    return 0;
  }
  
  public long getPlaybackCurrentPosition() {
    return 0L;
  }
  
  public long getPlaybackDuration() {
    return (this.duration * 1000);
  }
  
  public boolean isConnected() {
    return this.isConnected;
  }
  
  public boolean isRemoteRecording() {
    return this.isRemoteRecording;
  }
  
  public int maxFrameSize() {
    return 900000;
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
    if (this.playBackName == null && this.lastReadFrameSuccessTime != 0L && System.currentTimeMillis() - this.lastReadFrameSuccessTime > this.readFrameTimeOut) {
      this.anykaDevices.getConnectHandler().notifyReconnectVideo();
      this.lastReadFrameSuccessTime = System.currentTimeMillis();
    } 
    return -11;
  }
  
  public int seek(long paramLong, int paramInt) {
    return 0;
  }
  
  public void setConnectInternalCallback(ConnectInternalCallback paramConnectInternalCallback) {
    this.connectInternalCallback = paramConnectInternalCallback;
  }
  
  public void setOnRemoteCMDListener(CMDCommunicator.OnRemoteCMDListener paramOnRemoteCMDListener) {
    this.onRemoteCMDListener = paramOnRemoteCMDListener;
  }
  
  public void setPlaybackDuration(String paramString) {
    try {
      this.duration = (int)(Long.valueOf(paramString).longValue() / 1000L);
    } catch (Exception exception) {
      exception.printStackTrace();
    } 
  }
  
  public void setPlaybackStartTime(String paramString) {
    try {
      this.startTime = (int)(Long.valueOf(paramString).longValue() / 1000L);
    } catch (Exception exception) {
      exception.printStackTrace();
    } 
  }
  
  public void setPlaybackUrl(String paramString) {
    this.playBackName = paramString;
  }
  
  public void setRemoteRecording(boolean paramBoolean) {
    this.isRemoteRecording = paramBoolean;
  }
  
  public void setVideoDefaultQuality(int paramInt) {
    this.quality = paramInt;
  }
  
  public Cancelable switchVideoQuality(int paramInt, CMDCommunicator.OnExecuteCMDResult paramOnExecuteCMDResult) {
    setVideoDefaultQuality(paramInt);
    this.anykaDevices.getConnectHandler().notifyReconnectVideo();
    return (Cancelable)new NothingCancel();
  }
  
  public boolean usingMediaCodeC() {
    return true;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/anykadevices/AnykaVideoCommunicator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */