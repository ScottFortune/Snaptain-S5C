package com.netopsun.anykadevices;

import com.netopsun.deviceshub.base.RxTxCommunicator;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class AnykaRxTxCommunicator extends RxTxCommunicator {
  final AnykaDevices anykaDevices;
  
  public ConnectInternalCallback connectInternalCallback;
  
  volatile boolean isConnect;
  
  volatile long lastSendSuccessTimes = 0L;
  
  private OutputStream outputStream;
  
  private Disposable receiveTask;
  
  private Disposable sendBeatTask;
  
  Socket socket = new Socket();
  
  public AnykaRxTxCommunicator(AnykaDevices paramAnykaDevices) {
    super(paramAnykaDevices);
    this.anykaDevices = paramAnykaDevices;
  }
  
  public int connectInternal() {
    if (this.isConnect)
      return 0; 
    try {
      InetSocketAddress inetSocketAddress = new InetSocketAddress();
      this(this.anykaDevices.getDevicesIP(), this.anykaDevices.getRxtxPort());
      Socket socket = new Socket();
      this();
      this.socket = socket;
      this.socket.connect(inetSocketAddress);
      if (this.connectInternalCallback != null)
        this.connectInternalCallback.connectSuccess(); 
      this.outputStream = this.socket.getOutputStream();
      InputStream inputStream = this.socket.getInputStream();
      this.lastSendSuccessTimes = System.currentTimeMillis();
      if (this.receiveTask != null)
        this.receiveTask.dispose(); 
      long l = System.currentTimeMillis();
      ObservableOnSubscribe<Object> observableOnSubscribe = new ObservableOnSubscribe<Object>() {
          public void subscribe(ObservableEmitter<Object> param1ObservableEmitter) throws Exception {
            byte[] arrayOfByte = new byte[1024];
            while (!param1ObservableEmitter.isDisposed()) {
              try {
                int i = inputStream.read(arrayOfByte);
                if (i > 0) {
                  byte[] arrayOfByte1 = Arrays.copyOf(arrayOfByte, i);
                  if (AnykaRxTxCommunicator.this.onReceiveCallback != null)
                    AnykaRxTxCommunicator.this.onReceiveCallback.onReceive(arrayOfByte1); 
                } 
              } catch (Exception exception) {
                if (System.currentTimeMillis() - startReceiveTime > 2000L) {
                  AnykaRxTxCommunicator.this.anykaDevices.getConnectHandler().notifyReconnectRxTx();
                  param1ObservableEmitter.onComplete();
                } 
              } 
            } 
          }
        };
      super(this, inputStream, l);
      this.receiveTask = Observable.create(observableOnSubscribe).subscribeOn(Schedulers.io()).subscribe();
      this.isConnect = true;
      return 0;
    } catch (IOException iOException) {
      iOException.printStackTrace();
      return -1;
    } 
  }
  
  public void disconnect() {
    this.lastSendSuccessTimes = 0L;
    Disposable disposable = this.sendBeatTask;
    if (disposable != null)
      disposable.dispose(); 
    super.disconnect();
  }
  
  public int disconnectInternal() {
    Disposable disposable = this.receiveTask;
    if (disposable != null)
      disposable.dispose(); 
    Socket socket = this.socket;
    if (socket != null)
      try {
        socket.close();
      } catch (IOException iOException) {
        iOException.printStackTrace();
      }  
    this.isConnect = false;
    return 0;
  }
  
  public int interruptSend() {
    return 0;
  }
  
  public boolean isConnected() {
    boolean bool;
    Socket socket = this.socket;
    if (socket != null && socket.isConnected() && this.isConnect) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public int send(byte[] paramArrayOfbyte) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield outputStream : Ljava/io/OutputStream;
    //   6: astore_2
    //   7: aload_2
    //   8: ifnonnull -> 15
    //   11: aload_0
    //   12: monitorexit
    //   13: iconst_m1
    //   14: ireturn
    //   15: aload_0
    //   16: getfield outputStream : Ljava/io/OutputStream;
    //   19: aload_1
    //   20: invokevirtual write : ([B)V
    //   23: aload_0
    //   24: getfield outputStream : Ljava/io/OutputStream;
    //   27: invokevirtual flush : ()V
    //   30: aload_0
    //   31: invokestatic currentTimeMillis : ()J
    //   34: putfield lastSendSuccessTimes : J
    //   37: aload_0
    //   38: monitorexit
    //   39: iconst_0
    //   40: ireturn
    //   41: astore_1
    //   42: aload_1
    //   43: invokevirtual printStackTrace : ()V
    //   46: aload_0
    //   47: getfield socket : Ljava/net/Socket;
    //   50: invokevirtual isClosed : ()Z
    //   53: istore_3
    //   54: iload_3
    //   55: ifne -> 73
    //   58: aload_0
    //   59: getfield socket : Ljava/net/Socket;
    //   62: invokevirtual close : ()V
    //   65: goto -> 73
    //   68: astore_1
    //   69: aload_1
    //   70: invokevirtual printStackTrace : ()V
    //   73: aload_0
    //   74: getfield lastSendSuccessTimes : J
    //   77: lconst_0
    //   78: lcmp
    //   79: ifeq -> 121
    //   82: aload_0
    //   83: getfield autoReconnect : Z
    //   86: ifeq -> 121
    //   89: invokestatic currentTimeMillis : ()J
    //   92: aload_0
    //   93: getfield lastSendSuccessTimes : J
    //   96: lsub
    //   97: ldc2_w 3000
    //   100: lcmp
    //   101: ifle -> 121
    //   104: aload_0
    //   105: getfield anykaDevices : Lcom/netopsun/anykadevices/AnykaDevices;
    //   108: invokevirtual getConnectHandler : ()Lcom/netopsun/deviceshub/base/ConnectHandler;
    //   111: invokevirtual notifyReconnectRxTx : ()V
    //   114: aload_0
    //   115: invokestatic currentTimeMillis : ()J
    //   118: putfield lastSendSuccessTimes : J
    //   121: aload_0
    //   122: monitorexit
    //   123: iconst_m1
    //   124: ireturn
    //   125: astore_1
    //   126: aload_0
    //   127: monitorexit
    //   128: aload_1
    //   129: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	125	finally
    //   15	37	41	java/io/IOException
    //   15	37	125	finally
    //   42	54	125	finally
    //   58	65	68	java/io/IOException
    //   58	65	125	finally
    //   69	73	125	finally
    //   73	121	125	finally
  }
  
  public void setConnectInternalCallback(ConnectInternalCallback paramConnectInternalCallback) {
    this.connectInternalCallback = paramConnectInternalCallback;
  }
  
  public boolean startSendHeartBeatPackage(int paramInt, final byte[] beatData) {
    Disposable disposable = this.sendBeatTask;
    if (disposable != null)
      disposable.dispose(); 
    this.sendBeatTask = Observable.interval(paramInt, TimeUnit.MILLISECONDS).subscribe(new Consumer<Long>() {
          public void accept(Long param1Long) throws Exception {
            AnykaRxTxCommunicator.this.send(beatData);
          }
        });
    return true;
  }
  
  public void stopSendHeartBeatPackage() {
    Disposable disposable = this.sendBeatTask;
    if (disposable != null)
      disposable.dispose(); 
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/anykadevices/AnykaRxTxCommunicator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */