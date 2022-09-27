package com.netopsun.dronectrl.F200Ctrl.F200TxModel;

import com.netopsun.dronectrl.F200Ctrl.F200CtrlMesgId;

public class TxWaypointConfigModel extends F200TxModel {
  private int state = 1;
  
  private int wayPointH;
  
  private int wayPointSpeed;
  
  private int wayPointTime;
  
  public TxWaypointConfigModel(int paramInt1, int paramInt2, int paramInt3) {
    setWayPointH(paramInt3);
    setWayPointSpeed(paramInt1);
    setWayPointTime(paramInt2);
  }
  
  public F200CtrlMesgId getF200CtrlMesgId() {
    return F200CtrlMesgId.MSP_SETUP_WayPoint;
  }
  
  public int[] modelValues() {
    return new int[] { this.state, this.wayPointSpeed, this.wayPointTime, this.wayPointH };
  }
  
  public int[] rawByteLens() {
    return new int[] { 1, 1, 1, 1 };
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


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/dronectrl/F200Ctrl/F200TxModel/TxWaypointConfigModel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */