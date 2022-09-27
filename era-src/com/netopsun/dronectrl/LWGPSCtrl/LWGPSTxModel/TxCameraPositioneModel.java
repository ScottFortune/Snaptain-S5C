package com.netopsun.dronectrl.LWGPSCtrl.LWGPSTxModel;

import com.netopsun.dronectrl.LWGPSCtrl.LWGPSCtrlMesgId;

public class TxCameraPositioneModel extends LWGPSTxModel {
  private short angle;
  
  public TxCameraPositioneModel(short paramShort) {
    this.angle = (short)paramShort;
  }
  
  public LWGPSCtrlMesgId getLWGPSCtrlMesgId() {
    return LWGPSCtrlMesgId.MSP_Camera_Position;
  }
  
  public int[] modelValues() {
    return new int[] { this.angle, 0 };
  }
  
  public int[] rawByteLens() {
    return new int[] { 2, 2 };
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/dronectrl/LWGPSCtrl/LWGPSTxModel/TxCameraPositioneModel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */