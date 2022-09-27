package com.netopsun.rxtxprotocol.optical_flow_drone_protocol;

import com.netopsun.rxtxprotocol.base.simple_receiver.SimpleDroneMsgCallback;

public class OpticalFlowReceiveDataAnalyzer {
  private boolean isStartRecord;
  
  private boolean lastRecordValue;
  
  private boolean lastTakePhotoValue;
  
  byte[] receiveData = new byte[10];
  
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
    boolean bool = false;
    if (arrayOfByte[0] == 102) {
      byte b = getCheckSum(arrayOfByte, 1, 8);
      arrayOfByte = this.receiveData;
      if (b == arrayOfByte[9] && this.simpleDroneMsgCallback != null) {
        boolean bool1;
        float f = (arrayOfByte[1] & 0xFF) / 10.0F;
        double d = f;
        Double.isNaN(d);
        int j = (int)(d * 160.7142D - 517.8571D);
        int i = j;
        if (j > 100)
          i = 100; 
        j = i;
        if (i < 0)
          j = 0; 
        this.simpleDroneMsgCallback.onBatteryLevel(j, f);
        if ((this.receiveData[2] & 0x1) == 1) {
          bool1 = true;
        } else {
          bool1 = false;
        } 
        if ((0x2 & this.receiveData[2]) >> 1 == 1)
          bool = true; 
        if (bool1 && this.lastTakePhotoValue != bool1)
          this.simpleDroneMsgCallback.didRecvTakePhotoCmd(); 
        this.lastTakePhotoValue = bool1;
        if (bool && this.lastRecordValue != bool) {
          this.isStartRecord ^= 0x1;
          if (this.isStartRecord) {
            this.simpleDroneMsgCallback.didRecvRecordStartCmd();
          } else {
            this.simpleDroneMsgCallback.didRecvRecordStopCmd();
          } 
        } 
        this.lastRecordValue = bool;
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


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/rxtxprotocol/optical_flow_drone_protocol/OpticalFlowReceiveDataAnalyzer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */