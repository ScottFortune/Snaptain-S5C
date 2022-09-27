package com.netopsun.fhdevices;

import android.util.Log;
import com.netopsun.deviceshub.base.RxTxCommunicator;
import com.netopsun.fhapi.FHDEV_NetLibrary;
import com.netopsun.fhapi.FHNP_SerialPort_t;
import com.sun.jna.Pointer;
import java.nio.ByteBuffer;

public class FHRxTxCommunicator extends RxTxCommunicator {
  public volatile int currentBaudRate = 0;
  
  private ByteBuffer directBuffer = ByteBuffer.allocateDirect(20000);
  
  final FHDevices fhDevices;
  
  volatile boolean isConnect;
  
  long lastSendSuccessTimes = 0L;
  
  private final FHDevices.OnSerialDataAvailableCallback onSerialDataAvailableCallback = new FHDevices.OnSerialDataAvailableCallback() {
      public void onSerialDataAvailable(int param1Int1, int param1Int2) {
        if (FHRxTxCommunicator.this.onReceiveCallback != null) {
          FHRxTxCommunicator.this.directBuffer.clear();
          byte[] arrayOfByte = new byte[param1Int1];
          FHRxTxCommunicator.this.directBuffer.get(arrayOfByte);
          FHRxTxCommunicator.this.onReceiveCallback.onReceive(arrayOfByte);
        } 
      }
    };
  
  private Pointer serialHandle;
  
  public FHRxTxCommunicator(FHDevices paramFHDevices) {
    super(paramFHDevices);
    this.fhDevices = paramFHDevices;
  }
  
  public int connectInternal() {
    if (this.isConnect)
      return 0; 
    Pointer pointer1 = this.serialHandle;
    if (pointer1 != null)
      FHDevices.stopSerialEx(Pointer.nativeValue(pointer1)); 
    Pointer pointer2 = this.fhDevices.getUserID();
    FHNP_SerialPort_t fHNP_SerialPort_t = new FHNP_SerialPort_t();
    int i = this.fhDevices.getBaudRate();
    fHNP_SerialPort_t.iBaudRate = i;
    fHNP_SerialPort_t.iDataBit = 8;
    fHNP_SerialPort_t.iStopBit = 1;
    fHNP_SerialPort_t.btParity = (byte)0;
    fHNP_SerialPort_t.btFlowCtrl = (byte)0;
    fHNP_SerialPort_t.setAutoSynch(true);
    fHNP_SerialPort_t.autoWrite();
    if (FHDEV_NetLibrary.INSTANCE.FHDEV_NET_SetDevConfig(pointer2, 12, (byte)1, fHNP_SerialPort_t.getPointer(), fHNP_SerialPort_t.size()) == 0) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("set baud fail: error ");
      stringBuilder.append(FHDEV_NetLibrary.INSTANCE.FHDEV_NET_GetLastError());
      Log.e("FHRxTxCommunicator", stringBuilder.toString());
      i = FHDEV_NetLibrary.INSTANCE.FHDEV_NET_GetLastError();
      if (i == 0 || i == 2002 || i == 2003)
        this.fhDevices.setNoInitDevices(); 
      stringBuilder = new StringBuilder();
      stringBuilder.append("FHDEV_NET_StartSerialEx: error ");
      stringBuilder.append(i);
      Log.e("FHRxTxCommunicator", stringBuilder.toString());
      try {
        Thread.sleep(300L);
      } catch (InterruptedException interruptedException) {
        interruptedException.printStackTrace();
      } 
      return -1;
    } 
    if (pointer2 != null) {
      long l = FHDevices.startSerialEx(this.directBuffer, Pointer.nativeValue(pointer2), 1, 1, (byte)0, 1, this.onSerialDataAvailableCallback);
      Pointer pointer = Pointer.NULL;
      if (l != 0L)
        pointer = new Pointer(l); 
      if (pointer == null) {
        i = FHDEV_NetLibrary.INSTANCE.FHDEV_NET_GetLastError();
        if (i == 0 || i == 2002 || i == 2003)
          this.fhDevices.setNoInitDevices(); 
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("FHDEV_NET_StartSerialEx: error ");
        stringBuilder.append(i);
        Log.e("FHRxTxCommunicator", stringBuilder.toString());
        try {
          Thread.sleep(300L);
        } catch (InterruptedException interruptedException) {
          interruptedException.printStackTrace();
        } 
        return -1;
      } 
      this.serialHandle = (Pointer)interruptedException;
      this.currentBaudRate = i;
      this.isConnect = true;
      this.lastSendSuccessTimes = System.currentTimeMillis();
      return 0;
    } 
    try {
      Thread.sleep(300L);
    } catch (InterruptedException interruptedException) {
      interruptedException.printStackTrace();
    } 
    return -1;
  }
  
  public void disconnect() {
    this.lastSendSuccessTimes = 0L;
    super.disconnect();
  }
  
  public int disconnectInternal() {
    this.isConnect = false;
    this.currentBaudRate = 0;
    Pointer pointer = this.serialHandle;
    if (pointer != null && FHDevices.stopSerialEx(Pointer.nativeValue(pointer)) > 0) {
      this.serialHandle = null;
      return 0;
    } 
    return -1;
  }
  
  public int interruptSend() {
    return 0;
  }
  
  public boolean isConnected() {
    return this.isConnect;
  }
  
  public int send(byte[] paramArrayOfbyte) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield serialHandle : Lcom/sun/jna/Pointer;
    //   6: astore_2
    //   7: iconst_m1
    //   8: istore_3
    //   9: iload_3
    //   10: istore #4
    //   12: aload_2
    //   13: ifnull -> 43
    //   16: getstatic com/netopsun/fhapi/FHDEV_NetLibrary.INSTANCE : Lcom/netopsun/fhapi/FHDEV_NetLibrary;
    //   19: aload_2
    //   20: aload_1
    //   21: invokestatic wrap : ([B)Ljava/nio/ByteBuffer;
    //   24: aload_1
    //   25: arraylength
    //   26: invokeinterface FHDEV_NET_SendSerial : (Lcom/sun/jna/Pointer;Ljava/nio/ByteBuffer;I)I
    //   31: ifne -> 40
    //   34: iload_3
    //   35: istore #4
    //   37: goto -> 43
    //   40: iconst_0
    //   41: istore #4
    //   43: iload #4
    //   45: iflt -> 55
    //   48: aload_0
    //   49: invokestatic currentTimeMillis : ()J
    //   52: putfield lastSendSuccessTimes : J
    //   55: aload_0
    //   56: getfield lastSendSuccessTimes : J
    //   59: lconst_0
    //   60: lcmp
    //   61: ifeq -> 103
    //   64: aload_0
    //   65: getfield autoReconnect : Z
    //   68: ifeq -> 103
    //   71: invokestatic currentTimeMillis : ()J
    //   74: aload_0
    //   75: getfield lastSendSuccessTimes : J
    //   78: lsub
    //   79: ldc2_w 3000
    //   82: lcmp
    //   83: ifle -> 103
    //   86: aload_0
    //   87: getfield fhDevices : Lcom/netopsun/fhdevices/FHDevices;
    //   90: invokevirtual getConnectHandler : ()Lcom/netopsun/deviceshub/base/ConnectHandler;
    //   93: invokevirtual notifyReconnectRxTx : ()V
    //   96: aload_0
    //   97: invokestatic currentTimeMillis : ()J
    //   100: putfield lastSendSuccessTimes : J
    //   103: aload_0
    //   104: monitorexit
    //   105: iload #4
    //   107: ireturn
    //   108: astore_1
    //   109: aload_0
    //   110: monitorexit
    //   111: aload_1
    //   112: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	108	finally
    //   16	34	108	finally
    //   48	55	108	finally
    //   55	103	108	finally
  }
  
  public boolean shouldRetryConnect() {
    return super.shouldRetryConnect();
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhdevices/FHRxTxCommunicator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */