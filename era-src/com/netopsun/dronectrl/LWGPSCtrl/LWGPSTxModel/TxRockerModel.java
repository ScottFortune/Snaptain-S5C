package com.netopsun.dronectrl.LWGPSCtrl.LWGPSTxModel;

import com.netopsun.dronectrl.LWGPSCtrl.LWGPSCtrlMesgId;

public class TxRockerModel extends LWGPSTxModel {
  private int pitch;
  
  private int roll;
  
  private int throttle;
  
  private int yaw;
  
  public TxRockerModel(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6) {
    this.roll = paramInt1;
    this.pitch = paramInt2;
    this.yaw = paramInt3;
    this.throttle = paramInt4;
  }
  
  public LWGPSCtrlMesgId getLWGPSCtrlMesgId() {
    return LWGPSCtrlMesgId.MSP_RC_DATA;
  }
  
  public int[] modelValues() {
    return new int[] { this.pitch, this.roll, this.throttle, this.yaw };
  }
  
  public int[] rawByteLens() {
    return new int[] { 1, 1, 1, 1 };
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


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/dronectrl/LWGPSCtrl/LWGPSTxModel/TxRockerModel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */