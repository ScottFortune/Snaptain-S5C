package com.netopsun.deviceshub.base;

import com.netopsun.deviceshub.interfaces.ConnectResultCallback;
import java.nio.ByteBuffer;

public abstract class VideoCommunicator {
  public static final int EAGAIN = -11;
  
  public static final int EOF = FFERRORTAG('E', 'O', 'F', ' ');
  
  public static final int INPUT_OUTPUT_ERROR = -5;
  
  public static final int NETWORK_IS_UNREACHABLE = -101;
  
  protected volatile ConnectResultCallback connectResultCallback;
  
  protected int currentReconnectTimes;
  
  private final Devices devices;
  
  protected long readFrameTimeOut = 2000000000L;
  
  protected int shouldReconnectTimes = 3;
  
  public VideoCommunicator(Devices paramDevices) {
    this.devices = paramDevices;
  }
  
  protected static int FFERRORTAG(char paramChar1, char paramChar2, char paramChar3, char paramChar4) {
    return -((byte)paramChar1 | (byte)paramChar2 << 8 | (byte)paramChar3 << 16 | (byte)paramChar4 << 24);
  }
  
  public int connect() {
    this.currentReconnectTimes = 0;
    this.devices.getConnectHandler().notifyConnectVideo();
    return 0;
  }
  
  public abstract int connectInternal();
  
  public int disconnect() {
    this.devices.getConnectHandler().notifyDisconnectVideo();
    return 0;
  }
  
  public abstract int disconnectInternal();
  
  public ConnectResultCallback getConnectResultCallback() {
    return this.connectResultCallback;
  }
  
  public long getPlaybackCurrentPosition() {
    return 0L;
  }
  
  public long getPlaybackDuration() {
    return 0L;
  }
  
  public abstract boolean isConnected();
  
  public int maxFPS() {
    return -1;
  }
  
  public abstract int maxFrameSize();
  
  public abstract int read(ByteBuffer paramByteBuffer, int paramInt);
  
  public abstract int seek(long paramLong, int paramInt);
  
  public void setConnectResultCallback(ConnectResultCallback paramConnectResultCallback) {
    this.connectResultCallback = paramConnectResultCallback;
  }
  
  public void setMacString(String paramString) {}
  
  public void setPlaybackDuration(String paramString) {}
  
  public void setPlaybackStartTime(String paramString) {}
  
  public void setPlaybackUrl(String paramString) {}
  
  public void setReadFrameTimeOut(int paramInt) {
    this.readFrameTimeOut = paramInt;
  }
  
  public void setShouldReconnectTimes(int paramInt) {
    this.shouldReconnectTimes = paramInt;
    if (paramInt < 0)
      this.shouldReconnectTimes = 1000000000; 
  }
  
  public void setVideoDefaultQuality(int paramInt) {}
  
  protected boolean shouldRetryConnect() {
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
  
  public void switchToPlaybackStream(String paramString) {}
  
  public boolean usingMediaCodeC() {
    return true;
  }
  
  public String videoStreamFormat() {
    return null;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/deviceshub/base/VideoCommunicator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */