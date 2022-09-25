package com.netopsun.dronectrl.F200Ctrl.F200TxModel;

import android.util.Log;
import com.netopsun.dronectrl.F200Ctrl.F200CtrlMesgId;

public class TxGpsCircleModel extends F200TxModel {
  private int CircleGpsLat;
  
  private int CircleGpsLon;
  
  private int CircleH;
  
  private int CircleR;
  
  private int State = 1;
  
  public TxGpsCircleModel(int paramInt1, int paramInt2, double paramDouble1, double paramDouble2) {
    setCircleH(paramInt1);
    setCircleR(paramInt2);
    setCircleGpsLat(paramDouble1);
    setCircleGpsLon(paramDouble2);
  }
  
  public F200CtrlMesgId getF200CtrlMesgId() {
    return F200CtrlMesgId.MSP_Circle;
  }
  
  public int[] modelValues() {
    return new int[] { this.State, this.CircleH, this.CircleR, this.CircleGpsLat, this.CircleGpsLon };
  }
  
  public int[] rawByteLens() {
    return new int[] { 1, 2, 1, 4, 4 };
  }
  
  public void setCircleGpsLat(double paramDouble) {
    this.CircleGpsLat = (int)(paramDouble * 1.0E7D);
  }
  
  public void setCircleGpsLon(double paramDouble) {
    this.CircleGpsLon = (int)(paramDouble * 1.0E7D);
  }
  
  public void setCircleH(int paramInt) {
    int i = paramInt;
    if (paramInt < 3) {
      Log.i("TxGpsCircleModel", "环绕高度最小3m");
      i = 3;
    } 
    this.CircleH = i;
  }
  
  public void setCircleR(int paramInt) {
    int i = paramInt;
    if (paramInt < 3) {
      Log.i("TxGpsCircleModel", "环绕半径最小3m");
      i = 3;
    } 
    this.CircleR = i;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/dronectrl/F200Ctrl/F200TxModel/TxGpsCircleModel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */