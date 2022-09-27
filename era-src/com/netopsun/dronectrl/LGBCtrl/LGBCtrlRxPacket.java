package com.netopsun.dronectrl.LGBCtrl;

import android.util.Log;

public abstract class LGBCtrlRxPacket {
  public byte[] allData;
  
  public byte[] contentData;
  
  public boolean isVaild;
  
  public int type;
  
  public int caculateCheckValue(byte[] paramArrayOfbyte) {
    return LGBBytesTool.caculateXOR(LGBBytesTool.subBytes(paramArrayOfbyte, getToCheckRange(paramArrayOfbyte)));
  }
  
  public boolean checkData(byte[] paramArrayOfbyte) {
    return (caculateCheckValue(paramArrayOfbyte) == (paramArrayOfbyte[(getCheckValueRange(paramArrayOfbyte)).index] & 0xFF));
  }
  
  public abstract Range getCheckValueRange(byte[] paramArrayOfbyte);
  
  public abstract Range getContentRange(byte[] paramArrayOfbyte);
  
  public abstract Range getLenRange(byte[] paramArrayOfbyte);
  
  public abstract Range getToCheckRange(byte[] paramArrayOfbyte);
  
  public abstract Range getTypeRange(byte[] paramArrayOfbyte);
  
  public boolean refreshModelData(byte[] paramArrayOfbyte) {
    if (!checkData(paramArrayOfbyte)) {
      Log.i("LGBCtrlModel", "checkData error!");
      return false;
    } 
    this.allData = paramArrayOfbyte;
    setType(paramArrayOfbyte[(getTypeRange(paramArrayOfbyte)).index] & 0xFF);
    this.contentData = LGBBytesTool.subBytes(paramArrayOfbyte, (getContentRange(paramArrayOfbyte)).index, (getContentRange(paramArrayOfbyte)).len);
    return true;
  }
  
  public void setType(int paramInt) {
    this.type = paramInt;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/dronectrl/LGBCtrl/LGBCtrlRxPacket.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */