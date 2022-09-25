package com.netopsun.jrdevices;

import com.netopsun.deviceshub.base.CMDCommunicator;
import com.netopsun.deviceshub.base.Devices;
import com.netopsun.deviceshub.interfaces.Cancelable;
import com.netopsun.deviceshub.interfaces.NothingCancel;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;

public class JRCMDCommunicator extends CMDCommunicator {
  private final int cmdPort;
  
  private volatile int commandNum = 1;
  
  private Disposable datagramReceiveTask;
  
  DatagramSocket datagramSocket;
  
  private InetAddress inetAddress;
  
  volatile boolean isConnect;
  
  private final JRDevices jrDevices;
  
  Socket socket;
  
  private final int tcpCMDPort;
  
  public JRCMDCommunicator(Devices paramDevices) {
    super(paramDevices);
    this.jrDevices = (JRDevices)paramDevices;
    this.cmdPort = this.jrDevices.getCmdPort();
    this.tcpCMDPort = this.jrDevices.getTcpCMDPort();
  }
  
  public int connectInternal() {
    if (this.isConnect)
      return 0; 
    try {
      DatagramSocket datagramSocket = new DatagramSocket();
      this();
      this.datagramSocket = datagramSocket;
      this.inetAddress = InetAddress.getByName(this.jrDevices.getDevicesIP());
      DatagramPacket datagramPacket = new DatagramPacket();
      this(new byte[1], 1, this.inetAddress, this.cmdPort);
      this.datagramSocket.send(datagramPacket);
      if (this.datagramReceiveTask != null)
        this.datagramReceiveTask.dispose(); 
      ObservableOnSubscribe<Object> observableOnSubscribe = new ObservableOnSubscribe<Object>() {
          public void subscribe(ObservableEmitter<Object> param1ObservableEmitter) throws Exception {
            byte[] arrayOfByte = new byte[1024];
            DatagramPacket datagramPacket = new DatagramPacket(arrayOfByte, arrayOfByte.length);
            while (!param1ObservableEmitter.isDisposed()) {
              try {
                JRCMDCommunicator.this.datagramSocket.receive(datagramPacket);
                if (datagramPacket.getLength() > 0)
                  Arrays.copyOf(datagramPacket.getData(), datagramPacket.getLength()); 
              } catch (Exception exception) {
                param1ObservableEmitter.onComplete();
              } 
            } 
          }
        };
      super(this);
      this.datagramReceiveTask = Observable.create(observableOnSubscribe).subscribeOn(Schedulers.io()).subscribe();
      this.isConnect = true;
      return 0;
    } catch (IOException iOException) {
      iOException.printStackTrace();
      return -1;
    } 
  }
  
  public int disconnectInternal() {
    Disposable disposable = this.datagramReceiveTask;
    if (disposable != null)
      disposable.dispose(); 
    DatagramSocket datagramSocket = this.datagramSocket;
    if (datagramSocket != null)
      datagramSocket.close(); 
    this.isConnect = false;
    return 0;
  }
  
  public void fillCheckSum(byte[] paramArrayOfbyte, int paramInt1, int paramInt2, int paramInt3) {
    int i = paramInt1;
    byte b = 0;
    while (i <= paramInt2) {
      if (i == paramInt1) {
        b = paramArrayOfbyte[paramInt1];
      } else {
        b = (byte)(b ^ paramArrayOfbyte[i]);
      } 
      i++;
    } 
    paramArrayOfbyte[paramInt3] = (byte)b;
  }
  
  public boolean isConnected() {
    boolean bool;
    DatagramSocket datagramSocket = this.datagramSocket;
    if (datagramSocket != null && datagramSocket.isConnected() && this.isConnect) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public Cancelable remoteStartRecord(boolean paramBoolean, int paramInt, CMDCommunicator.OnExecuteCMDResult paramOnExecuteCMDResult) {
    return super.remoteStartRecord(paramBoolean, paramInt, paramOnExecuteCMDResult);
  }
  
  public Cancelable remoteStopRecord(boolean paramBoolean, int paramInt, CMDCommunicator.OnExecuteCMDResult paramOnExecuteCMDResult) {
    return super.remoteStopRecord(paramBoolean, paramInt, paramOnExecuteCMDResult);
  }
  
  public Cancelable remoteTakePhoto(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, CMDCommunicator.OnExecuteCMDResult paramOnExecuteCMDResult) {
    return super.remoteTakePhoto(paramBoolean, paramInt1, paramInt2, paramInt3, paramOnExecuteCMDResult);
  }
  
  public Cancelable remoteTakePhoto(boolean paramBoolean, int paramInt, CMDCommunicator.OnExecuteCMDResult paramOnExecuteCMDResult) {
    return super.remoteTakePhoto(paramBoolean, paramInt, paramOnExecuteCMDResult);
  }
  
  public Cancelable rotateVideo(boolean paramBoolean1, int paramInt, boolean paramBoolean2, CMDCommunicator.OnExecuteCMDResult paramOnExecuteCMDResult) {
    Observable.create(new ObservableOnSubscribe<Object>() {
          public void subscribe(ObservableEmitter<Object> param1ObservableEmitter) throws Exception {
            byte[] arrayOfByte = new byte[7];
            arrayOfByte[0] = (byte)-52;
            arrayOfByte[1] = (byte)90;
            arrayOfByte[2] = (byte)(byte)JRCMDCommunicator.this.commandNum;
            arrayOfByte[3] = (byte)1;
            arrayOfByte[4] = (byte)2;
            arrayOfByte[5] = (byte)1;
            arrayOfByte[6] = (byte)0;
            JRCMDCommunicator.this.fillCheckSum(arrayOfByte, 2, arrayOfByte.length - 2, arrayOfByte.length - 1);
            JRCMDCommunicator.this.sendCommand(arrayOfByte, arrayOfByte.length);
          }
        }).subscribeOn(Schedulers.io()).subscribe();
    return (Cancelable)new NothingCancel();
  }
  
  protected void sendCommand(byte[] paramArrayOfbyte, int paramInt) {
    if (this.datagramSocket == null)
      return; 
    DatagramPacket datagramPacket = new DatagramPacket(paramArrayOfbyte, paramInt, this.inetAddress, this.cmdPort);
    try {
      this.datagramSocket.send(datagramPacket);
    } catch (IOException iOException) {
      iOException.printStackTrace();
    } 
    this.commandNum++;
    if (this.commandNum > 100)
      this.commandNum = 1; 
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/jrdevices/JRCMDCommunicator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */