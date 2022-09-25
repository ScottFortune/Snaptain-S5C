package com.netopsun.dronectrl.F200Ctrl.F200TxModel;

import com.netopsun.dronectrl.F200Ctrl.F200CtrlMesgId;

public class TxFollowmeModel extends F200TxModel {
  int FollowGpsLat;
  
  int FollowGpsLon;
  
  byte State = (byte)1;
  
  public TxFollowmeModel(double paramDouble1, double paramDouble2) {
    setFollowGpsLat(paramDouble1);
    setFollowGpsLon(paramDouble2);
  }
  
  public F200CtrlMesgId getF200CtrlMesgId() {
    return F200CtrlMesgId.MSP_FollowMe;
  }
  
  public int[] modelValues() {
    return new int[] { this.State, this.FollowGpsLat, this.FollowGpsLon };
  }
  
  public int[] rawByteLens() {
    return new int[] { 1, 4, 4 };
  }
  
  public void setFollowGpsLat(double paramDouble) {
    this.FollowGpsLat = (int)(paramDouble * 1.0E7D);
  }
  
  public void setFollowGpsLon(double paramDouble) {
    this.FollowGpsLon = (int)(paramDouble * 1.0E7D);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/dronectrl/F200Ctrl/F200TxModel/TxFollowmeModel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */