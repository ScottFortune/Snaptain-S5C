package com.netopsun.dronectrl.LWGPSCtrl.LWGPSTxModel;

import com.netopsun.dronectrl.LWGPSCtrl.LWGPSCtrlMesgId;

public class TxWaypointGroupModel extends LWGPSTxModel {
  TxWaypointModel[] txWaypointModels;
  
  private int wayPointCnt;
  
  public TxWaypointGroupModel(TxWaypointModel[] paramArrayOfTxWaypointModel) {
    this.wayPointCnt = paramArrayOfTxWaypointModel.length;
    this.txWaypointModels = paramArrayOfTxWaypointModel;
  }
  
  public LWGPSCtrlMesgId getLWGPSCtrlMesgId() {
    return LWGPSCtrlMesgId.MSP_WayPointMode;
  }
  
  public byte[] getRawData() {
    int i = this.txWaypointModels[0].modelRawLength();
    int j = this.wayPointCnt;
    byte[] arrayOfByte = new byte[j * i + 1];
    arrayOfByte[0] = (byte)(byte)(j & 0xFF);
    for (j = 0; j < this.wayPointCnt; j++)
      System.arraycopy(this.txWaypointModels[j].getRawData(), 0, arrayOfByte, i * j + 1, i); 
    return arrayOfByte;
  }
  
  public int[] modelValues() {
    return new int[0];
  }
  
  public int[] rawByteLens() {
    return new int[0];
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/dronectrl/LWGPSCtrl/LWGPSTxModel/TxWaypointGroupModel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */