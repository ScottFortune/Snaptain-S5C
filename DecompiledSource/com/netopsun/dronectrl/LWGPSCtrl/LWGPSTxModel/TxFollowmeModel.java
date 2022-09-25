package com.netopsun.dronectrl.LWGPSCtrl.LWGPSTxModel;

import com.netopsun.dronectrl.LWGPSCtrl.LWGPSCtrlMesgId;

public class TxFollowmeModel extends LWGPSTxModel {
  int FollowGpsLat;
  
  int FollowGpsLon;
  
  public TxFollowmeModel(double paramDouble1, double paramDouble2) {
    setFollowGpsLat(paramDouble1);
    setFollowGpsLon(paramDouble2);
  }
  
  public LWGPSCtrlMesgId getLWGPSCtrlMesgId() {
    return LWGPSCtrlMesgId.MSP_FollowMe;
  }
  
  public int[] modelValues() {
    return new int[] { this.FollowGpsLat, this.FollowGpsLon };
  }
  
  public int[] rawByteLens() {
    return new int[] { 4, 4 };
  }
  
  public void setFollowGpsLat(double paramDouble) {
    this.FollowGpsLat = (int)(paramDouble * 1.0E7D);
  }
  
  public void setFollowGpsLon(double paramDouble) {
    this.FollowGpsLon = (int)(paramDouble * 1.0E7D);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/dronectrl/LWGPSCtrl/LWGPSTxModel/TxFollowmeModel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */