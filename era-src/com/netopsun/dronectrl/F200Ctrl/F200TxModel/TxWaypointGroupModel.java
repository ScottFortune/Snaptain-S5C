package com.netopsun.dronectrl.F200Ctrl.F200TxModel;

import com.netopsun.dronectrl.F200Ctrl.F200CtrlMesgId;

public class TxWaypointGroupModel extends F200TxModel {
  TxWaypointModel[] txWaypointModels;
  
  private int wayPointCnt;
  
  public TxWaypointGroupModel(TxWaypointModel[] paramArrayOfTxWaypointModel) {
    this.wayPointCnt = paramArrayOfTxWaypointModel.length;
    this.txWaypointModels = paramArrayOfTxWaypointModel;
  }
  
  public F200CtrlMesgId getF200CtrlMesgId() {
    return F200CtrlMesgId.MSP_WayPointMode;
  }
  
  public byte[] getRawData() {
    int i = this.txWaypointModels[0].modelRawLength();
    byte[] arrayOfByte = new byte[this.wayPointCnt * i + 1];
    byte b = 0;
    while (true) {
      int j = this.wayPointCnt;
      if (b < j) {
        System.arraycopy(this.txWaypointModels[b].getRawData(), 0, arrayOfByte, i * b + 1, i);
        b++;
        continue;
      } 
      arrayOfByte[0] = (byte)(byte)j;
      return arrayOfByte;
    } 
  }
  
  public int[] modelValues() {
    return new int[0];
  }
  
  public int[] rawByteLens() {
    return new int[0];
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/dronectrl/F200Ctrl/F200TxModel/TxWaypointGroupModel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */