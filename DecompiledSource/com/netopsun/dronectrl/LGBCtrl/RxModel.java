package com.netopsun.dronectrl.LGBCtrl;

public abstract class RxModel {
  protected RxModelChangeCallBack rxModelChangeCallBack;
  
  boolean checkRawDataLen(byte[] paramArrayOfbyte) {
    return !(paramArrayOfbyte.length != modelRawLength());
  }
  
  public abstract int modelRawLength();
  
  public void refreshWithRawData(byte[] paramArrayOfbyte) {
    if (!checkRawDataLen(paramArrayOfbyte))
      return; 
    unpackRawData(paramArrayOfbyte);
    RxModelChangeCallBack rxModelChangeCallBack = this.rxModelChangeCallBack;
    if (rxModelChangeCallBack != null)
      rxModelChangeCallBack.callback(this); 
  }
  
  public void setCallBack(RxModelChangeCallBack paramRxModelChangeCallBack) {
    this.rxModelChangeCallBack = paramRxModelChangeCallBack;
  }
  
  protected abstract void unpackRawData(byte[] paramArrayOfbyte);
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/dronectrl/LGBCtrl/RxModel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */