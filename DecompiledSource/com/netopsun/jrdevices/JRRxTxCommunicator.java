package com.netopsun.jrdevices;

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
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class JRRxTxCommunicator extends RxTxCommunicator {
  volatile boolean isConnect;
  
  private boolean isEncryptData = true;
  
  final JRDevices jrDevices;
  
  private Disposable receiveTask;
  
  private Disposable sendBeatTask;
  
  volatile DatagramSocket socket;
  
  TxEncryptionUtil txEncryptionUtil = new TxEncryptionUtil();
  
  public JRRxTxCommunicator(JRDevices paramJRDevices) {
    super(paramJRDevices);
    this.jrDevices = paramJRDevices;
  }
  
  public int connectInternal() {
    if (this.isConnect)
      return 0; 
    try {
      if (this.socket != null)
        this.socket.close(); 
      DatagramSocket datagramSocket = new DatagramSocket();
      this((SocketAddress)null);
      this.socket = datagramSocket;
      this.socket.setReuseAddress(true);
      datagramSocket = this.socket;
      InetSocketAddress inetSocketAddress = new InetSocketAddress();
      this(this.jrDevices.getRxtxPort());
      datagramSocket.bind(inetSocketAddress);
      if (this.receiveTask != null)
        this.receiveTask.dispose(); 
      ObservableOnSubscribe<Object> observableOnSubscribe = new ObservableOnSubscribe<Object>() {
          public void subscribe(ObservableEmitter<Object> param1ObservableEmitter) throws Exception {
            byte[] arrayOfByte = new byte[1024];
            DatagramPacket datagramPacket = new DatagramPacket(arrayOfByte, arrayOfByte.length);
            while (!param1ObservableEmitter.isDisposed()) {
              try {
                JRRxTxCommunicator.this.socket.receive(datagramPacket);
                if (datagramPacket.getLength() > 0) {
                  byte[] arrayOfByte1 = Arrays.copyOf(datagramPacket.getData(), datagramPacket.getLength());
                  if (JRRxTxCommunicator.this.onReceiveCallback != null)
                    JRRxTxCommunicator.this.onReceiveCallback.onReceive(arrayOfByte1); 
                } 
                Thread.sleep(120L);
              } catch (Exception exception) {
                param1ObservableEmitter.onComplete();
              } 
            } 
          }
        };
      super(this);
      this.receiveTask = Observable.create(observableOnSubscribe).subscribeOn(Schedulers.newThread()).subscribe();
      this.isConnect = true;
      return 0;
    } catch (IOException iOException) {
      iOException.printStackTrace();
      return -1;
    } 
  }
  
  public void disconnect() {
    Disposable disposable = this.sendBeatTask;
    if (disposable != null)
      disposable.dispose(); 
    super.disconnect();
  }
  
  public int disconnectInternal() {
    if (this.socket != null)
      this.socket.close(); 
    Disposable disposable = this.receiveTask;
    if (disposable != null)
      disposable.dispose(); 
    this.isConnect = false;
    return 0;
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
    //   3: getfield jrDevices : Lcom/netopsun/jrdevices/JRDevices;
    //   6: invokevirtual getVideoCommunicator : ()Lcom/netopsun/deviceshub/base/VideoCommunicator;
    //   9: checkcast com/netopsun/jrdevices/JRVideoCommunicator
    //   12: astore_2
    //   13: aload_2
    //   14: invokevirtual getRtspClient : ()Lcom/netopsun/live555/Live555RTSPClient;
    //   17: ifnull -> 50
    //   20: aload_1
    //   21: astore_3
    //   22: aload_0
    //   23: getfield isEncryptData : Z
    //   26: ifeq -> 38
    //   29: aload_0
    //   30: getfield txEncryptionUtil : Lcom/netopsun/jrdevices/TxEncryptionUtil;
    //   33: aload_1
    //   34: invokevirtual encrypt : ([B)[B
    //   37: astore_3
    //   38: aload_2
    //   39: invokevirtual getRtspClient : ()Lcom/netopsun/live555/Live555RTSPClient;
    //   42: aload_3
    //   43: invokevirtual sendRTCPBytes : ([B)V
    //   46: aload_0
    //   47: monitorexit
    //   48: iconst_0
    //   49: ireturn
    //   50: aload_0
    //   51: monitorexit
    //   52: iconst_m1
    //   53: ireturn
    //   54: astore_1
    //   55: aload_0
    //   56: monitorexit
    //   57: aload_1
    //   58: athrow
    // Exception table:
    //   from	to	target	type
    //   2	20	54	finally
    //   22	38	54	finally
    //   38	46	54	finally
  }
  
  public void setEncryptData(boolean paramBoolean) {
    this.isEncryptData = paramBoolean;
  }
  
  public void setMac(String paramString) {
    this.txEncryptionUtil.updateKey(paramString);
  }
  
  public boolean startSendHeartBeatPackage(int paramInt, final byte[] beatData) {
    Disposable disposable = this.sendBeatTask;
    if (disposable != null)
      disposable.dispose(); 
    this.sendBeatTask = Observable.interval(paramInt, TimeUnit.MILLISECONDS).subscribe(new Consumer<Long>() {
          public void accept(Long param1Long) throws Exception {
            JRRxTxCommunicator.this.send(beatData);
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


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/jrdevices/JRRxTxCommunicator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */