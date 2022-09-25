package com.netopsun.dronectrl.LWGPSCtrl.LWGPSTxModel;

import android.util.Log;
import com.netopsun.dronectrl.LWGPSCtrl.LWGPSCtrlMesgId;

public class TxGpsCircleModel extends LWGPSTxModel {
  private int CircleGpsLat;
  
  private int CircleGpsLon;
  
  private int CircleH;
  
  private int CircleR;
  
  private int CircleV = 10;
  
  public TxGpsCircleModel(int paramInt1, int paramInt2, double paramDouble1, double paramDouble2) {
    setCircleH(paramInt1);
    setCircleR(paramInt2);
    setCircleGpsLat(paramDouble1);
    setCircleGpsLon(paramDouble2);
  }
  
  public LWGPSCtrlMesgId getLWGPSCtrlMesgId() {
    return LWGPSCtrlMesgId.MSP_Circle;
  }
  
  public int[] modelValues() {
    return new int[] { this.CircleH, this.CircleR, this.CircleV, this.CircleGpsLat, this.CircleGpsLon };
  }
  
  public int[] rawByteLens() {
    return new int[] { 1, 1, 1, 4, 4 };
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
  
  public void setCircleV(int paramInt) {
    int i = paramInt;
    if (paramInt > 128) {
      Log.i("TxGpsCircleModel", "速度不大于12.8m/s");
      i = 10;
    } 
    this.CircleR = i;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/dronectrl/LWGPSCtrl/LWGPSTxModel/TxGpsCircleModel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */