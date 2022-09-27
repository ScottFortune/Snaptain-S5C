package com.netopsun.dronectrl.F200Ctrl.F200TxModel;

import com.netopsun.dronectrl.F200Ctrl.F200CtrlMesgId;

public class TxRockerModel extends F200TxModel {
  private int cameraS1;
  
  private int cameraS2;
  
  private int pitch;
  
  private int roll;
  
  private int throttle;
  
  private int yaw;
  
  public TxRockerModel(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6) {
    this.roll = paramInt1;
    this.pitch = paramInt2;
    this.yaw = paramInt3;
    this.throttle = paramInt4;
    this.cameraS1 = paramInt5;
    this.cameraS2 = paramInt6;
  }
  
  public F200CtrlMesgId getF200CtrlMesgId() {
    return F200CtrlMesgId.MSP_RC_DATA;
  }
  
  public int[] modelValues() {
    return new int[] { this.roll, this.pitch, this.yaw, this.throttle, this.cameraS1, this.cameraS2 };
  }
  
  public int[] rawByteLens() {
    return new int[] { 2, 2, 2, 2, 2, 2 };
  }
  
  public void setCameraS1(int paramInt) {
    this.cameraS1 = paramInt;
  }
  
  public void setCameraS2(int paramInt) {
    this.cameraS2 = paramInt;
  }
  
  public void setPitch(int paramInt) {
    this.pitch = paramInt;
  }
  
  public void setRoll(int paramInt) {
    this.roll = paramInt;
  }
  
  public void setThrottle(int paramInt) {
    this.throttle = paramInt;
  }
  
  public void setYaw(int paramInt) {
    this.yaw = paramInt;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/dronectrl/F200Ctrl/F200TxModel/TxRockerModel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */