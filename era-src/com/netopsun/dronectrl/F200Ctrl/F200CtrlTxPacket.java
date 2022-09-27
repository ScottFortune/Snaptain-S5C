package com.netopsun.dronectrl.F200Ctrl;

import com.netopsun.dronectrl.F200Ctrl.F200TxModel.F200TxModel;
import com.netopsun.dronectrl.LGBCtrl.LGBBytesTool;
import com.netopsun.dronectrl.LGBCtrl.Range;
import java.io.UnsupportedEncodingException;

public class F200CtrlTxPacket {
  private byte[] contentData;
  
  private F200CtrlMesgId f200CtrlMesgId = null;
  
  private byte[] headerData;
  
  private byte mesgId;
  
  F200CtrlTxPacket(F200CtrlMesgId paramF200CtrlMesgId) {
    try {
      this.headerData = "BT<".getBytes("UTF-8");
    } catch (UnsupportedEncodingException unsupportedEncodingException) {
      unsupportedEncodingException.printStackTrace();
    } 
    setF200CtrlMesgId(paramF200CtrlMesgId);
    this.contentData = new byte[] { 1 };
  }
  
  F200CtrlTxPacket(F200CtrlMesgId paramF200CtrlMesgId, byte[] paramArrayOfbyte) {
    try {
      this.headerData = "BT<".getBytes("UTF-8");
    } catch (UnsupportedEncodingException unsupportedEncodingException) {
      unsupportedEncodingException.printStackTrace();
    } 
    setF200CtrlMesgId(paramF200CtrlMesgId);
    this.contentData = paramArrayOfbyte;
  }
  
  F200CtrlTxPacket(F200TxModel paramF200TxModel) {
    try {
      this.headerData = "BT<".getBytes("UTF-8");
    } catch (UnsupportedEncodingException unsupportedEncodingException) {
      unsupportedEncodingException.printStackTrace();
    } 
    setF200CtrlMesgId(paramF200TxModel.getF200CtrlMesgId());
    this.contentData = paramF200TxModel.getRawData();
  }
  
  public byte[] getRawData() {
    if (this.contentData == null && this.mesgId != 0)
      return null; 
    byte[] arrayOfByte1 = this.headerData;
    byte[] arrayOfByte2 = new byte[arrayOfByte1.length + this.contentData.length + 3];
    System.arraycopy(arrayOfByte1, 0, arrayOfByte2, 0, arrayOfByte1.length);
    int i = this.headerData.length + 0;
    arrayOfByte1 = this.contentData;
    arrayOfByte2[i] = (byte)(byte)(arrayOfByte1.length & 0xFF);
    arrayOfByte2[++i] = (byte)this.mesgId;
    System.arraycopy(arrayOfByte1, 0, arrayOfByte2, ++i, arrayOfByte1.length);
    arrayOfByte1 = this.contentData;
    arrayOfByte2[i + arrayOfByte1.length] = (byte)(byte)(LGBBytesTool.caculateXOR(LGBBytesTool.subBytes(arrayOfByte2, new Range(3, arrayOfByte1.length + 2))) & 0xFF);
    return arrayOfByte2;
  }
  
  public void setF200CtrlMesgId(F200CtrlMesgId paramF200CtrlMesgId) {
    this.f200CtrlMesgId = paramF200CtrlMesgId;
    switch (paramF200CtrlMesgId) {
      default:
        this.mesgId = (byte)0;
        return;
      case MSP_SETUP_WayPoint:
        this.mesgId = (byte)110;
        return;
      case MSP_READ_WayPoint:
        this.mesgId = (byte)109;
        return;
      case MSP_CAMER_CTRL:
        this.mesgId = (byte)108;
        return;
      case MSP_RC_DATA:
        this.mesgId = (byte)107;
        return;
      case MSP_WayPointMode:
        this.mesgId = (byte)106;
        return;
      case MSP_Circle:
        this.mesgId = (byte)105;
        return;
      case MSP_FollowMe:
        this.mesgId = (byte)104;
        return;
      case MSP_GPS_DISPLAY_NAVIGATION:
        this.mesgId = (byte)103;
        return;
      case MSP_AHRS_DISPLAY:
        this.mesgId = (byte)102;
        return;
      case MSP_SENSOR_CALIBRATION_SETUP:
        this.mesgId = (byte)101;
        return;
      case MSP_F200_ONEKEY_FUN:
        break;
    } 
    this.mesgId = (byte)100;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/dronectrl/F200Ctrl/F200CtrlTxPacket.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */