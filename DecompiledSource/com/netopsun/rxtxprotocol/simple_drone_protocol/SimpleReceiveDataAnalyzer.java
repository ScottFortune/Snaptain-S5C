package com.netopsun.rxtxprotocol.simple_drone_protocol;

import com.netopsun.rxtxprotocol.base.simple_receiver.SimpleDroneMsgCallback;

public class SimpleReceiveDataAnalyzer {
  private long lastStartRecordCMDTime;
  
  private long lastStopRecordCMDTime;
  
  private long lastTakePhotoCMDTime;
  
  byte[] receiveData = new byte[7];
  
  private SimpleDroneMsgCallback simpleDroneMsgCallback;
  
  private static byte getCheckSum(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
    int i = paramInt1;
    byte b1 = 0;
    byte b2;
    for (b2 = b1; i <= paramInt2; b2 = b1) {
      if (i == paramInt1) {
        b1 = paramArrayOfbyte[paramInt1];
      } else {
        b1 = (byte)(b2 ^ paramArrayOfbyte[i]);
      } 
      i++;
    } 
    return b2;
  }
  
  private void readData() {
    byte[] arrayOfByte = this.receiveData;
    if (arrayOfByte[arrayOfByte.length - 6] == -52 && arrayOfByte[arrayOfByte.length - 1] == 51) {
      byte b = getCheckSum(arrayOfByte, arrayOfByte.length - 5, arrayOfByte.length - 3);
      arrayOfByte = this.receiveData;
      if (b == arrayOfByte[arrayOfByte.length - 2]) {
        SimpleDroneMsgCallback simpleDroneMsgCallback = this.simpleDroneMsgCallback;
        if (simpleDroneMsgCallback != null) {
          if (arrayOfByte[arrayOfByte.length - 5] == 3) {
            int i = arrayOfByte[arrayOfByte.length - 4] & 0xFF;
            simpleDroneMsgCallback.onBatteryLevel(i, i);
          } 
          arrayOfByte = this.receiveData;
          if (arrayOfByte[arrayOfByte.length - 5] == 1 && arrayOfByte[arrayOfByte.length - 4] == 1 && System.currentTimeMillis() - this.lastTakePhotoCMDTime > 1500L) {
            this.simpleDroneMsgCallback.didRecvTakePhotoCmd();
            this.lastTakePhotoCMDTime = System.currentTimeMillis();
          } 
          arrayOfByte = this.receiveData;
          if (arrayOfByte[arrayOfByte.length - 5] == 2 && arrayOfByte[arrayOfByte.length - 4] == 1 && System.currentTimeMillis() - this.lastStartRecordCMDTime > 1500L) {
            this.simpleDroneMsgCallback.didRecvRecordStartCmd();
            this.lastStartRecordCMDTime = System.currentTimeMillis();
          } 
          arrayOfByte = this.receiveData;
          if (arrayOfByte[arrayOfByte.length - 5] == 2 && arrayOfByte[arrayOfByte.length - 4] == 0 && System.currentTimeMillis() - this.lastStopRecordCMDTime > 1500L) {
            this.simpleDroneMsgCallback.didRecvRecordStopCmd();
            this.lastStopRecordCMDTime = System.currentTimeMillis();
          } 
        } 
      } 
    } 
    arrayOfByte = this.receiveData;
    if (arrayOfByte[0] == -91 && arrayOfByte[1] == 7 && arrayOfByte[2] == 40 && arrayOfByte[6] == 90) {
      byte b = getCheckSum(arrayOfByte, 2, 4);
      arrayOfByte = this.receiveData;
      if (b == arrayOfByte[5]) {
        if (arrayOfByte[3] == 3 && arrayOfByte[4] == 1 && System.currentTimeMillis() - this.lastTakePhotoCMDTime > 1500L) {
          this.simpleDroneMsgCallback.didRecvTakePhotoCmd();
          this.lastTakePhotoCMDTime = System.currentTimeMillis();
        } 
        arrayOfByte = this.receiveData;
        if (arrayOfByte[3] == 3 && arrayOfByte[4] == 2 && System.currentTimeMillis() - this.lastStartRecordCMDTime > 1500L) {
          this.simpleDroneMsgCallback.didRecvRecordStartCmd();
          this.lastStartRecordCMDTime = System.currentTimeMillis();
        } 
        arrayOfByte = this.receiveData;
        if (arrayOfByte[3] == 3 && arrayOfByte[4] == 0 && System.currentTimeMillis() - this.lastStopRecordCMDTime > 1500L) {
          this.simpleDroneMsgCallback.didRecvRecordStopCmd();
          this.lastStopRecordCMDTime = System.currentTimeMillis();
        } 
      } 
    } 
  }
  
  public void parseData(byte[] paramArrayOfbyte, int paramInt) {
    for (byte b = 0; b < paramInt; b++) {
      byte[] arrayOfByte = this.receiveData;
      System.arraycopy(arrayOfByte, 1, arrayOfByte, 0, arrayOfByte.length - 1);
      arrayOfByte = this.receiveData;
      arrayOfByte[arrayOfByte.length - 1] = (byte)paramArrayOfbyte[b];
      readData();
    } 
  }
  
  public void setSimpleDroneMsgCallback(SimpleDroneMsgCallback paramSimpleDroneMsgCallback) {
    this.simpleDroneMsgCallback = paramSimpleDroneMsgCallback;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/rxtxprotocol/simple_drone_protocol/SimpleReceiveDataAnalyzer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */