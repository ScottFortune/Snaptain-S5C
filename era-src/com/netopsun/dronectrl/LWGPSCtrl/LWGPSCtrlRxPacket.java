package com.netopsun.dronectrl.LWGPSCtrl;

import com.netopsun.dronectrl.LGBCtrl.LGBCtrlRxPacket;
import com.netopsun.dronectrl.LGBCtrl.Range;

public class LWGPSCtrlRxPacket extends LGBCtrlRxPacket {
  LWGPSCtrlMesgId LWGPSCtrlMesgId;
  
  public Range getCheckValueRange(byte[] paramArrayOfbyte) {
    return new Range(paramArrayOfbyte.length - 1, 1);
  }
  
  public Range getContentRange(byte[] paramArrayOfbyte) {
    byte b = paramArrayOfbyte[(getLenRange(paramArrayOfbyte)).index];
    return new Range(6, paramArrayOfbyte[(getLenRange(paramArrayOfbyte)).index + 1] & 0xFF00 | b & 0xFF);
  }
  
  public Range getLenRange(byte[] paramArrayOfbyte) {
    return new Range(4, 2);
  }
  
  public Range getToCheckRange(byte[] paramArrayOfbyte) {
    return new Range(3, (getContentRange(paramArrayOfbyte)).len + 3);
  }
  
  public Range getTypeRange(byte[] paramArrayOfbyte) {
    return new Range(3, 1);
  }
  
  public void setType(int paramInt) {
    super.setType(paramInt);
    if (paramInt != 104) {
      if (paramInt != 105) {
        if (paramInt != 107) {
          if (paramInt != 112) {
            if (paramInt != 160) {
              if (paramInt != 109) {
                if (paramInt != 110) {
                  switch (paramInt) {
                    default:
                      lWGPSCtrlMesgId = this.LWGPSCtrlMesgId;
                      this.LWGPSCtrlMesgId = LWGPSCtrlMesgId.MSP_LWGPS_Unknown;
                      return;
                    case 102:
                      lWGPSCtrlMesgId = this.LWGPSCtrlMesgId;
                      this.LWGPSCtrlMesgId = LWGPSCtrlMesgId.MSP_SENSOR_CALIBRATION_SETUP;
                      return;
                    case 101:
                      lWGPSCtrlMesgId = this.LWGPSCtrlMesgId;
                      this.LWGPSCtrlMesgId = LWGPSCtrlMesgId.MSP_AHRS_DISPLAY;
                      return;
                    case 100:
                      break;
                  } 
                  LWGPSCtrlMesgId lWGPSCtrlMesgId = this.LWGPSCtrlMesgId;
                  this.LWGPSCtrlMesgId = LWGPSCtrlMesgId.MSP_LWGPS_ONEKEY_FUN;
                } else {
                  LWGPSCtrlMesgId lWGPSCtrlMesgId = this.LWGPSCtrlMesgId;
                  this.LWGPSCtrlMesgId = LWGPSCtrlMesgId.MSP_SETUP_WayPoint;
                } 
              } else {
                LWGPSCtrlMesgId lWGPSCtrlMesgId = this.LWGPSCtrlMesgId;
                this.LWGPSCtrlMesgId = LWGPSCtrlMesgId.MSP_READ_WayPoint;
              } 
            } else {
              LWGPSCtrlMesgId lWGPSCtrlMesgId = this.LWGPSCtrlMesgId;
              this.LWGPSCtrlMesgId = LWGPSCtrlMesgId.MSP_CAMER_CTRL;
            } 
          } else {
            LWGPSCtrlMesgId lWGPSCtrlMesgId = this.LWGPSCtrlMesgId;
            this.LWGPSCtrlMesgId = LWGPSCtrlMesgId.MSP_WayPointMode;
          } 
        } else {
          LWGPSCtrlMesgId lWGPSCtrlMesgId = this.LWGPSCtrlMesgId;
          this.LWGPSCtrlMesgId = LWGPSCtrlMesgId.MSP_RC_DATA;
        } 
      } else {
        LWGPSCtrlMesgId lWGPSCtrlMesgId = this.LWGPSCtrlMesgId;
        this.LWGPSCtrlMesgId = LWGPSCtrlMesgId.MSP_Circle;
      } 
    } else {
      LWGPSCtrlMesgId lWGPSCtrlMesgId = this.LWGPSCtrlMesgId;
      this.LWGPSCtrlMesgId = LWGPSCtrlMesgId.MSP_FollowMe;
    } 
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/dronectrl/LWGPSCtrl/LWGPSCtrlRxPacket.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */