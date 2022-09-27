package com.netopsun.dronectrl.F200Ctrl.F200TxModel;

import com.netopsun.dronectrl.LGBCtrl.TxModel;

public class TxWaypointModel extends TxModel {
  private int waypointGpsLat;
  
  private int waypointGpsLon;
  
  private int waypointH;
  
  public TxWaypointModel(double paramDouble1, double paramDouble2, int paramInt) {
    setWaypointGpsLat(paramDouble1);
    setWaypointGpsLon(paramDouble2);
    setWaypointH(paramInt);
  }
  
  public int[] modelValues() {
    return new int[] { this.waypointGpsLat, this.waypointGpsLon, this.waypointH };
  }
  
  public int[] rawByteLens() {
    return new int[] { 4, 4, 1 };
  }
  
  public void setWaypointGpsLat(double paramDouble) {
    this.waypointGpsLat = (int)(paramDouble * 1.0E7D);
  }
  
  public void setWaypointGpsLon(double paramDouble) {
    this.waypointGpsLon = (int)(paramDouble * 1.0E7D);
  }
  
  public void setWaypointH(int paramInt) {
    this.waypointH = paramInt;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/dronectrl/F200Ctrl/F200TxModel/TxWaypointModel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */