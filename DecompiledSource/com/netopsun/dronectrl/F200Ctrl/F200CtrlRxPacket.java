package com.netopsun.dronectrl.F200Ctrl;

import com.netopsun.dronectrl.LGBCtrl.LGBCtrlRxPacket;
import com.netopsun.dronectrl.LGBCtrl.Range;

public class F200CtrlRxPacket extends LGBCtrlRxPacket {
  F200CtrlMesgId f200CtrlMesgId;
  
  public Range getCheckValueRange(byte[] paramArrayOfbyte) {
    return new Range(paramArrayOfbyte.length - 1, 1);
  }
  
  public Range getContentRange(byte[] paramArrayOfbyte) {
    return new Range(5, paramArrayOfbyte[(getLenRange(paramArrayOfbyte)).index]);
  }
  
  public Range getLenRange(byte[] paramArrayOfbyte) {
    return new Range(3, 1);
  }
  
  public Range getToCheckRange(byte[] paramArrayOfbyte) {
    return new Range(3, (getContentRange(paramArrayOfbyte)).len + 2);
  }
  
  public Range getTypeRange(byte[] paramArrayOfbyte) {
    return new Range(4, 1);
  }
  
  public void setType(int paramInt) {
    F200CtrlMesgId f200CtrlMesgId;
    super.setType(paramInt);
    switch (paramInt) {
      default:
        f200CtrlMesgId = this.f200CtrlMesgId;
        this.f200CtrlMesgId = F200CtrlMesgId.MSP_F200_Unknown;
        return;
      case 110:
        this.f200CtrlMesgId = F200CtrlMesgId.MSP_SETUP_WayPoint;
        return;
      case 109:
        this.f200CtrlMesgId = F200CtrlMesgId.MSP_READ_WayPoint;
        return;
      case 108:
        this.f200CtrlMesgId = F200CtrlMesgId.MSP_CAMER_CTRL;
        return;
      case 107:
        this.f200CtrlMesgId = F200CtrlMesgId.MSP_RC_DATA;
        return;
      case 106:
        this.f200CtrlMesgId = F200CtrlMesgId.MSP_WayPointMode;
        return;
      case 105:
        this.f200CtrlMesgId = F200CtrlMesgId.MSP_Circle;
        return;
      case 104:
        this.f200CtrlMesgId = F200CtrlMesgId.MSP_FollowMe;
        return;
      case 103:
        this.f200CtrlMesgId = F200CtrlMesgId.MSP_GPS_DISPLAY_NAVIGATION;
        return;
      case 102:
        this.f200CtrlMesgId = F200CtrlMesgId.MSP_AHRS_DISPLAY;
        return;
      case 101:
        this.f200CtrlMesgId = F200CtrlMesgId.MSP_SENSOR_CALIBRATION_SETUP;
        return;
      case 100:
        break;
    } 
    this.f200CtrlMesgId = F200CtrlMesgId.MSP_F200_ONEKEY_FUN;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/dronectrl/F200Ctrl/F200CtrlRxPacket.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */