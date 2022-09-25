package com.netopsun.dronectrl.LWGPSCtrl.LWGPSTxModel;

import com.netopsun.dronectrl.LWGPSCtrl.LWGPSCtrlMesgId;

public class TxWaypointConfigModel extends LWGPSTxModel {
  private int wayPointH;
  
  private int wayPointSpeed;
  
  private int wayPointTime;
  
  public TxWaypointConfigModel(int paramInt1, int paramInt2, int paramInt3) {
    setWayPointH(paramInt3);
    setWayPointSpeed(paramInt1);
    setWayPointTime(paramInt2);
  }
  
  public LWGPSCtrlMesgId getLWGPSCtrlMesgId() {
    return LWGPSCtrlMesgId.MSP_SETUP_WayPoint;
  }
  
  public int[] modelValues() {
    return new int[] { this.wayPointSpeed, this.wayPointTime, this.wayPointH };
  }
  
  public int[] rawByteLens() {
    return new int[] { 1, 1, 1 };
  }
  
  public void setWayPointH(int paramInt) {
    this.wayPointH = paramInt;
  }
  
  public void setWayPointSpeed(int paramInt) {
    this.wayPointSpeed = paramInt;
  }
  
  public void setWayPointTime(int paramInt) {
    this.wayPointTime = paramInt;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/dronectrl/LWGPSCtrl/LWGPSTxModel/TxWaypointConfigModel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */