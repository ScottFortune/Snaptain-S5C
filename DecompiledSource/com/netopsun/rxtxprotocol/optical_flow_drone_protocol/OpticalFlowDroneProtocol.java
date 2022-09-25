package com.netopsun.rxtxprotocol.optical_flow_drone_protocol;

import com.netopsun.deviceshub.base.RxTxCommunicator;
import com.netopsun.rxtxprotocol.base.RxTxProtocol;
import com.netopsun.rxtxprotocol.base.simple_receiver.SimpleDroneMsgCallback;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import java.util.concurrent.TimeUnit;

public class OpticalFlowDroneProtocol extends RxTxProtocol {
  final byte[] bytes = new byte[20];
  
  final OpticalFlowReceiveDataAnalyzer receiveDataAnalyzer = new OpticalFlowReceiveDataAnalyzer();
  
  public OpticalFlowDroneProtocol(RxTxCommunicator paramRxTxCommunicator) {
    super(paramRxTxCommunicator);
    paramRxTxCommunicator.setOnReceiveCallback(new RxTxCommunicator.OnReceiveCallback() {
          public void onReceive(byte[] param1ArrayOfbyte) {
            OpticalFlowDroneProtocol.this.receiveDataAnalyzer.parseData(param1ArrayOfbyte, param1ArrayOfbyte.length);
          }
        });
  }
  
  public void notifySend() {
    byte[] arrayOfByte = this.bytes;
    arrayOfByte[0] = (byte)102;
    arrayOfByte[1] = (byte)20;
    arrayOfByte[2] = (byte)(byte)(int)(this.roll / 100.0F * 128.0F + 128.0F);
    this.bytes[3] = (byte)(byte)(int)(this.pitch / 100.0F * 128.0F + 128.0F);
    this.bytes[4] = (byte)(byte)(int)(this.accelerator / 100.0F * 128.0F + 128.0F);
    this.bytes[5] = (byte)(byte)(int)(this.yaw / 100.0F * 128.0F + 128.0F);
    if (this.takeOff == 1) {
      b1 = (byte)1;
    } else {
      b1 = 0;
    } 
    byte b2 = b1;
    if (this.landing == 1)
      b2 = (byte)(b1 | 0x1); 
    byte b1 = b2;
    if (this.emergencyStop == 1)
      b1 = (byte)(b2 | 0x2); 
    b2 = b1;
    if (this.calibration == 1)
      b2 = (byte)(b1 | 0x4); 
    b1 = b2;
    if (this.openLight == 1)
      b1 = (byte)(b2 | 0x10); 
    this.bytes[6] = (byte)b1;
    if (this.headless == 1) {
      b2 = (byte)1;
    } else {
      b2 = 0;
    } 
    arrayOfByte = this.bytes;
    arrayOfByte[7] = (byte)b2;
    arrayOfByte[8] = (byte)0;
    arrayOfByte[9] = (byte)0;
    if (this.openFollowedMode) {
      arrayOfByte = this.bytes;
      arrayOfByte[8] = (byte)-1;
      arrayOfByte[9] = (byte)-1;
    } 
    this.bytes[10] = (byte)(byte)(int)(this.followedModeDirectionRockerY / 100.0F * 128.0F + 128.0F);
    this.bytes[11] = (byte)(byte)(int)(this.followedModeAcceleratorRockerX / 100.0F * 128.0F + 128.0F);
    this.bytes[12] = (byte)(byte)(int)(this.followedModeAcceleratorRockerY / 100.0F * 128.0F + 128.0F);
    this.bytes[13] = (byte)(byte)(int)(this.followedModeDirectionRockerX / 100.0F * 128.0F + 128.0F);
    fillCheckSum(this.bytes, 2, 17, 18);
    arrayOfByte = this.bytes;
    arrayOfByte[19] = (byte)-103;
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
            OpticalFlowDroneProtocol.access$402(OpticalFlowDroneProtocol.this, 0);
          }
        });
  }
  
  public void setEmergencyStop(boolean paramBoolean) {
    super.setEmergencyStop(paramBoolean);
    Observable.timer(1L, TimeUnit.SECONDS).subscribe(new Consumer<Long>() {
          public void accept(Long param1Long) throws Exception {
            OpticalFlowDroneProtocol.access$302(OpticalFlowDroneProtocol.this, 0);
          }
        });
  }
  
  public void setLanding(boolean paramBoolean) {
    super.setLanding(paramBoolean);
    Observable.timer(1L, TimeUnit.SECONDS).subscribe(new Consumer<Long>() {
          public void accept(Long param1Long) throws Exception {
            OpticalFlowDroneProtocol.access$202(OpticalFlowDroneProtocol.this, 0);
          }
        });
  }
  
  public void setOpenLight(boolean paramBoolean) {
    super.setOpenLight(paramBoolean);
    Observable.timer(1L, TimeUnit.SECONDS).subscribe(new Consumer<Long>() {
          public void accept(Long param1Long) throws Exception {
            OpticalFlowDroneProtocol.access$002(OpticalFlowDroneProtocol.this, 0);
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
            OpticalFlowDroneProtocol.access$102(OpticalFlowDroneProtocol.this, 0);
          }
        });
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/rxtxprotocol/optical_flow_drone_protocol/OpticalFlowDroneProtocol.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */