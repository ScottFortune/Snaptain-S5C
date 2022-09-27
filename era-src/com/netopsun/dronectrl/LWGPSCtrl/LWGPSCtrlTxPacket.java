package com.netopsun.dronectrl.LWGPSCtrl;

import com.netopsun.dronectrl.LGBCtrl.LGBBytesTool;
import com.netopsun.dronectrl.LGBCtrl.Range;
import com.netopsun.dronectrl.LWGPSCtrl.LWGPSTxModel.LWGPSTxModel;

public class LWGPSCtrlTxPacket {
  private LWGPSCtrlMesgId LWGPSCtrlMesgId = null;
  
  private byte[] contentData;
  
  private byte[] headerData = new byte[] { 70, 72, 60 };
  
  private byte mesgId;
  
  LWGPSCtrlTxPacket(LWGPSCtrlMesgId paramLWGPSCtrlMesgId) {
    setLWGPSCtrlMesgId(paramLWGPSCtrlMesgId);
    this.contentData = new byte[] { 1 };
  }
  
  LWGPSCtrlTxPacket(LWGPSCtrlMesgId paramLWGPSCtrlMesgId, byte[] paramArrayOfbyte) {
    setLWGPSCtrlMesgId(paramLWGPSCtrlMesgId);
    this.contentData = paramArrayOfbyte;
  }
  
  LWGPSCtrlTxPacket(LWGPSTxModel paramLWGPSTxModel) {
    setLWGPSCtrlMesgId(paramLWGPSTxModel.getLWGPSCtrlMesgId());
    this.contentData = paramLWGPSTxModel.getRawData();
  }
  
  public byte[] getRawData() {
    if (this.contentData == null && this.mesgId != 0)
      return null; 
    byte[] arrayOfByte1 = this.headerData;
    byte[] arrayOfByte2 = new byte[arrayOfByte1.length + this.contentData.length + 4];
    System.arraycopy(arrayOfByte1, 0, arrayOfByte2, 0, arrayOfByte1.length);
    int i = this.headerData.length + 0;
    arrayOfByte2[i] = (byte)this.mesgId;
    i++;
    arrayOfByte1 = this.contentData;
    arrayOfByte2[i] = (byte)(byte)(arrayOfByte1.length & 0xFF);
    i = i + 1 + 1;
    System.arraycopy(arrayOfByte1, 0, arrayOfByte2, i, arrayOfByte1.length);
    arrayOfByte1 = this.contentData;
    arrayOfByte2[i + arrayOfByte1.length] = (byte)(byte)(LGBBytesTool.caculateXOR(LGBBytesTool.subBytes(arrayOfByte2, new Range(3, arrayOfByte1.length + 3))) & 0xFF);
    return arrayOfByte2;
  }
  
  public void setLWGPSCtrlMesgId(LWGPSCtrlMesgId paramLWGPSCtrlMesgId) {
    this.LWGPSCtrlMesgId = paramLWGPSCtrlMesgId;
    switch (paramLWGPSCtrlMesgId) {
      default:
        this.mesgId = (byte)0;
        return;
      case MSP_Camera_Position:
        this.mesgId = (byte)107;
        return;
      case MSP_SETUP_WayPoint:
        this.mesgId = (byte)113;
        return;
      case MSP_READ_WayPoint:
        this.mesgId = (byte)112;
        return;
      case MSP_CAMER_CTRL:
        this.mesgId = (byte)-96;
        return;
      case MSP_RC_DATA:
        this.mesgId = (byte)106;
        return;
      case MSP_WayPointMode:
        this.mesgId = (byte)108;
        return;
      case MSP_Circle:
        this.mesgId = (byte)110;
        return;
      case MSP_FollowMe:
        this.mesgId = (byte)109;
        return;
      case MSP_AHRS_DISPLAY:
        this.mesgId = (byte)101;
        return;
      case MSP_SENSOR_CALIBRATION_SETUP:
        this.mesgId = (byte)102;
        return;
      case MSP_LWGPS_ONEKEY_FUN3:
        this.mesgId = (byte)105;
        return;
      case MSP_LWGPS_ONEKEY_FUN2:
        this.mesgId = (byte)104;
        return;
      case MSP_LWGPS_ONEKEY_FUN:
        break;
    } 
    this.mesgId = (byte)103;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/dronectrl/LWGPSCtrl/LWGPSCtrlTxPacket.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */