package com.netopsun.live555;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Live555RTSPClient {
  private ByteBuffer byteBuffer;
  
  private int myCalcKey = -1;
  
  private volatile long rtspClientPtr;
  
  private volatile Thread rtspThread;
  
  private boolean sendReport = true;
  
  private boolean useTCP = true;
  
  static {
    System.loadLibrary("BasicUsageEnvironment");
    System.loadLibrary("UsageEnvironment");
    System.loadLibrary("groupsock");
    System.loadLibrary("liveMedia");
    System.loadLibrary("live555-native-lib");
  }
  
  private static native void openRTSP(String paramString, ByteBuffer paramByteBuffer, JNIRTSPCallback paramJNIRTSPCallback);
  
  private static native void sendOptionsCommand(long paramLong);
  
  private static native void sendRTCPBytes(long paramLong, byte[] paramArrayOfbyte);
  
  private static native void setMyCalcKey(long paramLong, int paramInt);
  
  private static native void setSendRTCPReport(long paramLong, boolean paramBoolean);
  
  private static native void setUseTCP(long paramLong, boolean paramBoolean);
  
  private static native void shutdownRTSP(long paramLong, int paramInt);
  
  public void close() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield rtspClientPtr : J
    //   6: lconst_0
    //   7: lcmp
    //   8: ifeq -> 19
    //   11: aload_0
    //   12: getfield rtspClientPtr : J
    //   15: iconst_0
    //   16: invokestatic shutdownRTSP : (JI)V
    //   19: aload_0
    //   20: monitorexit
    //   21: return
    //   22: astore_1
    //   23: aload_0
    //   24: monitorexit
    //   25: aload_1
    //   26: athrow
    // Exception table:
    //   from	to	target	type
    //   2	19	22	finally
    //   19	21	22	finally
    //   23	25	22	finally
  }
  
  public void exitWithOutShutdownRTSP() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield rtspClientPtr : J
    //   6: lconst_0
    //   7: lcmp
    //   8: ifeq -> 19
    //   11: aload_0
    //   12: getfield rtspClientPtr : J
    //   15: iconst_2
    //   16: invokestatic shutdownRTSP : (JI)V
    //   19: aload_0
    //   20: monitorexit
    //   21: return
    //   22: astore_1
    //   23: aload_0
    //   24: monitorexit
    //   25: aload_1
    //   26: athrow
    // Exception table:
    //   from	to	target	type
    //   2	19	22	finally
    //   19	21	22	finally
    //   23	25	22	finally
  }
  
  public void open(final String url, int paramInt, final RTSPCallback callback) {
    if (this.rtspThread != null && this.rtspThread.isAlive()) {
      callback.onConnectFail("上次连接未断开，等待直到onConnectFinish");
      return;
    } 
    this.byteBuffer = ByteBuffer.allocateDirect(paramInt);
    this.byteBuffer.order(ByteOrder.nativeOrder());
    this.rtspThread = new Thread(new Runnable() {
          public void run() {
            Live555RTSPClient.openRTSP(url, Live555RTSPClient.this.byteBuffer, new Live555RTSPClient.JNIRTSPCallback() {
                  public void beforeShutDown() {
                    synchronized (Live555RTSPClient.this) {
                      Live555RTSPClient.access$102(Live555RTSPClient.this, 0L);
                      return;
                    } 
                  }
                  
                  public void onConnectFail(String param2String) {
                    callback.onConnectFail(param2String);
                  }
                  
                  public void onConnectStart(long param2Long) {
                    Live555RTSPClient.access$102(Live555RTSPClient.this, param2Long);
                    if (param2Long != 0L) {
                      Live555RTSPClient.setSendRTCPReport(param2Long, Live555RTSPClient.this.sendReport);
                      Live555RTSPClient.setUseTCP(param2Long, Live555RTSPClient.this.useTCP);
                      Live555RTSPClient.setMyCalcKey(param2Long, Live555RTSPClient.this.myCalcKey);
                    } 
                  }
                  
                  public void onConnectSuccess(String param2String) {
                    callback.onConnectSuccess(param2String);
                  }
                  
                  public void onFrameDataAvailable(int param2Int1, int param2Int2) {
                    Live555RTSPClient.this.byteBuffer.clear();
                    Live555RTSPClient.this.byteBuffer.limit(param2Int1);
                    callback.onFrameDataAvailable(Live555RTSPClient.this.byteBuffer, param2Int1, param2Int2);
                  }
                });
            (new Thread(new Runnable() {
                  public void run() {
                    Live555RTSPClient.access$902(Live555RTSPClient.this, null);
                    callback.onConnectFinish();
                  }
                })).start();
          }
        });
    this.rtspThread.start();
  }
  
  public void sendOptionsCommand() {
    if (this.rtspClientPtr != 0L)
      sendOptionsCommand(this.rtspClientPtr); 
  }
  
  public void sendRTCPBytes(byte[] paramArrayOfbyte) {
    if (this.rtspClientPtr != 0L)
      sendRTCPBytes(this.rtspClientPtr, paramArrayOfbyte); 
  }
  
  public void setMyCalcKey(int paramInt) {
    this.myCalcKey = paramInt;
    if (this.rtspClientPtr != 0L)
      setMyCalcKey(this.rtspClientPtr, paramInt); 
  }
  
  public void setSendRTCPReport(boolean paramBoolean) {
    this.sendReport = paramBoolean;
  }
  
  public void setUseTCP(boolean paramBoolean) {
    this.useTCP = paramBoolean;
  }
  
  public static interface JNIRTSPCallback {
    void beforeShutDown();
    
    void onConnectFail(String param1String);
    
    void onConnectStart(long param1Long);
    
    void onConnectSuccess(String param1String);
    
    void onFrameDataAvailable(int param1Int1, int param1Int2);
  }
  
  public static interface RTSPCallback {
    void onConnectFail(String param1String);
    
    void onConnectFinish();
    
    void onConnectSuccess(String param1String);
    
    void onFrameDataAvailable(ByteBuffer param1ByteBuffer, int param1Int1, int param1Int2);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/live555/Live555RTSPClient.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */