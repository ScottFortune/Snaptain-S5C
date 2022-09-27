package com.netopsun.anykadevices;

import com.netopsun.deviceshub.base.RxTxCommunicator;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class AnykaRxTxCommunicatorByUDP extends RxTxCommunicator {
  final AnykaDevices anykaDevices;
  
  private InetAddress inetAddress;
  
  volatile boolean isConnect;
  
  volatile long lastSendSuccessTimes = 0L;
  
  private Disposable receiveTask;
  
  private Disposable sendBeatTask;
  
  DatagramSocket socket;
  
  public AnykaRxTxCommunicatorByUDP(AnykaDevices paramAnykaDevices) {
    super(paramAnykaDevices);
    this.anykaDevices = paramAnykaDevices;
  }
  
  public int connectInternal() {
    if (this.isConnect)
      return 0; 
    try {
      DatagramSocket datagramSocket = new DatagramSocket();
      this();
      this.socket = datagramSocket;
      this.inetAddress = InetAddress.getByName(this.anykaDevices.getDevicesIP());
      DatagramPacket datagramPacket = new DatagramPacket();
      this(new byte[1], 1, this.inetAddress, this.anykaDevices.getRxtxPort());
      this.socket.send(datagramPacket);
      this.lastSendSuccessTimes = System.currentTimeMillis();
      if (this.receiveTask != null)
        this.receiveTask.dispose(); 
      ObservableOnSubscribe<Object> observableOnSubscribe = new ObservableOnSubscribe<Object>() {
          public void subscribe(ObservableEmitter<Object> param1ObservableEmitter) throws Exception {
            byte[] arrayOfByte = new byte[1024];
            DatagramPacket datagramPacket = new DatagramPacket(arrayOfByte, arrayOfByte.length);
            while (!param1ObservableEmitter.isDisposed()) {
              try {
                AnykaRxTxCommunicatorByUDP.this.socket.receive(datagramPacket);
                if (datagramPacket.getLength() > 0) {
                  byte[] arrayOfByte1 = Arrays.copyOf(datagramPacket.getData(), datagramPacket.getLength());
                  if (AnykaRxTxCommunicatorByUDP.this.onReceiveCallback != null)
                    AnykaRxTxCommunicatorByUDP.this.onReceiveCallback.onReceive(arrayOfByte1); 
                } 
              } catch (Exception exception) {
                param1ObservableEmitter.onComplete();
              } 
            } 
          }
        };
      super(this);
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
    super.disconnect();
  }
  
  public int disconnectInternal() {
    Disposable disposable = this.receiveTask;
    if (disposable != null)
      disposable.dispose(); 
    disposable = this.sendBeatTask;
    if (disposable != null)
      disposable.dispose(); 
    DatagramSocket datagramSocket = this.socket;
    if (datagramSocket != null)
      datagramSocket.close(); 
    this.isConnect = false;
    return 0;
  }
  
  public int interruptSend() {
    return 0;
  }
  
  public boolean isConnected() {
    boolean bool;
    DatagramSocket datagramSocket = this.socket;
    if (datagramSocket != null && datagramSocket.isConnected() && this.isConnect) {
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
    //   3: getfield socket : Ljava/net/DatagramSocket;
    //   6: ifnonnull -> 22
    //   9: aload_0
    //   10: getfield inetAddress : Ljava/net/InetAddress;
    //   13: astore_2
    //   14: aload_2
    //   15: ifnull -> 22
    //   18: aload_0
    //   19: monitorexit
    //   20: iconst_m1
    //   21: ireturn
    //   22: new java/net/DatagramPacket
    //   25: astore_2
    //   26: aload_2
    //   27: aload_1
    //   28: aload_1
    //   29: arraylength
    //   30: aload_0
    //   31: getfield inetAddress : Ljava/net/InetAddress;
    //   34: aload_0
    //   35: getfield anykaDevices : Lcom/netopsun/anykadevices/AnykaDevices;
    //   38: invokevirtual getRxtxPort : ()I
    //   41: invokespecial <init> : ([BILjava/net/InetAddress;I)V
    //   44: aload_0
    //   45: getfield socket : Ljava/net/DatagramSocket;
    //   48: aload_2
    //   49: invokevirtual send : (Ljava/net/DatagramPacket;)V
    //   52: aload_0
    //   53: monitorexit
    //   54: iconst_0
    //   55: ireturn
    //   56: astore_1
    //   57: aload_1
    //   58: invokevirtual printStackTrace : ()V
    //   61: aload_0
    //   62: getfield lastSendSuccessTimes : J
    //   65: lconst_0
    //   66: lcmp
    //   67: ifeq -> 109
    //   70: aload_0
    //   71: getfield autoReconnect : Z
    //   74: ifeq -> 109
    //   77: invokestatic currentTimeMillis : ()J
    //   80: aload_0
    //   81: getfield lastSendSuccessTimes : J
    //   84: lsub
    //   85: ldc2_w 3000
    //   88: lcmp
    //   89: ifle -> 109
    //   92: aload_0
    //   93: getfield anykaDevices : Lcom/netopsun/anykadevices/AnykaDevices;
    //   96: invokevirtual getConnectHandler : ()Lcom/netopsun/deviceshub/base/ConnectHandler;
    //   99: invokevirtual notifyReconnectRxTx : ()V
    //   102: aload_0
    //   103: invokestatic currentTimeMillis : ()J
    //   106: putfield lastSendSuccessTimes : J
    //   109: aload_0
    //   110: monitorexit
    //   111: iconst_m1
    //   112: ireturn
    //   113: astore_1
    //   114: aload_0
    //   115: monitorexit
    //   116: aload_1
    //   117: athrow
    // Exception table:
    //   from	to	target	type
    //   2	14	113	finally
    //   22	52	56	java/io/IOException
    //   22	52	113	finally
    //   57	109	113	finally
  }
  
  public boolean startSendHeartBeatPackage(int paramInt, final byte[] beatData) {
    Disposable disposable = this.sendBeatTask;
    if (disposable != null)
      disposable.dispose(); 
    this.sendBeatTask = Observable.interval(paramInt, TimeUnit.MILLISECONDS).subscribe(new Consumer<Long>() {
          public void accept(Long param1Long) throws Exception {
            AnykaRxTxCommunicatorByUDP.this.send(beatData);
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


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/anykadevices/AnykaRxTxCommunicatorByUDP.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */