package com.netopsun.rxtxprotocol.simple_drone_protocol;

import com.netopsun.deviceshub.base.RxTxCommunicator;
import com.netopsun.rxtxprotocol.base.RxTxProtocol;
import com.netopsun.rxtxprotocol.base.simple_receiver.SimpleDroneMsgCallback;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import java.util.concurrent.TimeUnit;

public class SimpleDroneProtocol extends RxTxProtocol {
  public static final String HD_UFO_MODEL_FLAG = "HD_UFO";
  
  final byte[] bytes = new byte[8];
  
  private String modelFlag = "";
  
  final SimpleReceiveDataAnalyzer receiveDataAnalyzer = new SimpleReceiveDataAnalyzer();
  
  public SimpleDroneProtocol(RxTxCommunicator paramRxTxCommunicator) {
    super(paramRxTxCommunicator);
    paramRxTxCommunicator.setOnReceiveCallback(new RxTxCommunicator.OnReceiveCallback() {
          public void onReceive(byte[] param1ArrayOfbyte) {
            SimpleDroneProtocol.this.receiveDataAnalyzer.parseData(param1ArrayOfbyte, param1ArrayOfbyte.length);
          }
        });
  }
  
  private void fill_HD_UFO_flag() {
    if (this.takeOff == 1) {
      b1 = (byte)1;
    } else {
      b1 = 0;
    } 
    byte b2 = b1;
    if (this.landing == 1)
      b2 = (byte)(b1 | 0x2); 
    byte b1 = b2;
    if (this.emergencyStop == 1)
      b1 = (byte)(b2 | 0x4); 
    b2 = b1;
    if (this.headless == 1)
      b2 = (byte)(b1 | 0x10); 
    b1 = b2;
    if (this.openDroneTakePhotoLight == 1)
      b1 = (byte)(b2 | 0x20); 
    b2 = b1;
    if (this.openDroneRecordLight == 1)
      b2 = (byte)(b1 | 0x40); 
    b1 = b2;
    if (this.calibration == 1)
      b1 = (byte)(b2 | 0x80); 
    this.bytes[5] = (byte)b1;
  }
  
  public void notifySend() {
    byte[] arrayOfByte = this.bytes;
    arrayOfByte[0] = (byte)102;
    arrayOfByte[1] = (byte)(byte)(int)(this.roll / 100.0F * 128.0F + 128.0F);
    this.bytes[2] = (byte)(byte)(int)(this.pitch / 100.0F * 128.0F + 128.0F);
    this.bytes[3] = (byte)(byte)(int)(this.accelerator / 100.0F * 128.0F + 128.0F);
    this.bytes[4] = (byte)(byte)(int)(this.yaw / 100.0F * 128.0F + 128.0F);
    if (this.modelFlag.equals("HD_UFO"))
      fill_HD_UFO_flag(); 
    fillCheckSum(this.bytes, 1, 5, 6);
    arrayOfByte = this.bytes;
    arrayOfByte[7] = (byte)-103;
    arrayOfByte = (byte[])arrayOfByte.clone();
    if (couldAddSendBytes()) {
      this.sendQueue.clear();
      this.sendQueue.add(arrayOfByte);
    } 
  }
  
  public void setCalibration(boolean paramBoolean) {
    super.setCalibration(paramBoolean);
    Observable.timer(1L, TimeUnit.SECONDS).subscribe(new Consumer<Long>() {
          public void accept(Long param1Long) throws Exception {
            SimpleDroneProtocol.access$402(SimpleDroneProtocol.this, 0);
          }
        });
  }
  
  public void setEmergencyStop(boolean paramBoolean) {
    super.setEmergencyStop(paramBoolean);
    Observable.timer(1L, TimeUnit.SECONDS).subscribe(new Consumer<Long>() {
          public void accept(Long param1Long) throws Exception {
            SimpleDroneProtocol.access$302(SimpleDroneProtocol.this, 0);
          }
        });
  }
  
  public void setLanding(boolean paramBoolean) {
    super.setLanding(paramBoolean);
    Observable.timer(1L, TimeUnit.SECONDS).subscribe(new Consumer<Long>() {
          public void accept(Long param1Long) throws Exception {
            SimpleDroneProtocol.access$202(SimpleDroneProtocol.this, 0);
          }
        });
  }
  
  public void setModelFlag(String paramString) {
    this.modelFlag = paramString;
  }
  
  public void setOpenDroneTakePhotoLight(boolean paramBoolean) {
    super.setOpenDroneTakePhotoLight(paramBoolean);
    Observable.timer(1L, TimeUnit.SECONDS).subscribe(new Consumer<Long>() {
          public void accept(Long param1Long) throws Exception {
            SimpleDroneProtocol.access$002(SimpleDroneProtocol.this, 0);
          }
        });
  }
  
  public void setSimpleDroneMsgCallback(SimpleDroneMsgCallback paramSimpleDroneMsgCallback) {
    this.receiveDataAnalyzer.setSimpleDroneMsgCallback(paramSimpleDroneMsgCallback);
  }
  
  public void setTakeOff(boolean paramBoolean) {
    super.setTakeOff(paramBoolean);
    Observable.timer(1L, TimeUnit.SECONDS).subscribe(new Consumer<Long>() {
          public void accept(Long param1Long) throws Exception {
            SimpleDroneProtocol.access$102(SimpleDroneProtocol.this, 0);
          }
        });
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/rxtxprotocol/simple_drone_protocol/SimpleDroneProtocol.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */