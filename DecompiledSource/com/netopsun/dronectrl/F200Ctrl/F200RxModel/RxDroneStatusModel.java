package com.netopsun.dronectrl.F200Ctrl.F200RxModel;

import com.netopsun.dronectrl.LGBCtrl.DroneStatusModel;
import com.netopsun.dronectrl.LGBCtrl.LGBBytesTool;
import com.netopsun.dronectrl.LGBCtrl.RxModel;

public class RxDroneStatusModel extends RxModel {
  private int FlyModeRaw;
  
  private int PitchAngle;
  
  private int RollAngle;
  
  private int YawAngle;
  
  public DroneStatusModel.FlyModeType getFlyMode() {
    DroneStatusModel.FlyModeType flyModeType = DroneStatusModel.FlyModeType.UnsupportedFun;
    int i = (this.FlyModeRaw & 0xF0) >> 4;
    if (i != 15) {
      switch (i) {
        default:
          return DroneStatusModel.FlyModeType.UnsupportedFun;
        case 8:
          return DroneStatusModel.FlyModeType.Surround;
        case 7:
          return DroneStatusModel.FlyModeType.FollowMe;
        case 6:
          return DroneStatusModel.FlyModeType.RouteFlight;
        case 5:
          return DroneStatusModel.FlyModeType.Landing;
        case 4:
          return DroneStatusModel.FlyModeType.TakeOff;
        case 3:
          return DroneStatusModel.FlyModeType.FlyBack;
        case 2:
          return DroneStatusModel.FlyModeType.Hovering;
        case 1:
          break;
      } 
      flyModeType = DroneStatusModel.FlyModeType.HighLimit;
    } else {
      flyModeType = DroneStatusModel.FlyModeType.Stabilize;
    } 
    return flyModeType;
  }
  
  public DroneStatusModel.FlyStateType getFlyState() {
    null = DroneStatusModel.FlyStateType.UnsupportedFun;
    switch (this.FlyModeRaw & 0xF) {
      default:
        return DroneStatusModel.FlyStateType.UnsupportedFun;
      case 9:
        return DroneStatusModel.FlyStateType.OneKeyTakeOff;
      case 8:
        return DroneStatusModel.FlyStateType.OneKeyLanding;
      case 7:
        return DroneStatusModel.FlyStateType.LandingBecauseLowPower;
      case 6:
        return DroneStatusModel.FlyStateType.OneKeyFlyingBack;
      case 5:
        return DroneStatusModel.FlyStateType.FlyingBackClass2;
      case 4:
        return DroneStatusModel.FlyStateType.FlyingBackClass1;
      case 3:
        return DroneStatusModel.FlyStateType.OutOfControlAndFlyingBack;
      case 2:
        return DroneStatusModel.FlyStateType.UnlockedAndTakeOff;
      case 1:
        return DroneStatusModel.FlyStateType.UnlockedNotTakeOff;
      case 0:
        break;
    } 
    return DroneStatusModel.FlyStateType.Locked;
  }
  
  public double getPitchAngle() {
    double d = this.PitchAngle;
    Double.isNaN(d);
    return d * 0.1D;
  }
  
  public double getRollAngle() {
    double d = this.RollAngle;
    Double.isNaN(d);
    return d * 0.1D;
  }
  
  public double getYawAngle() {
    double d = this.YawAngle;
    Double.isNaN(d);
    return d * 0.1D;
  }
  
  public int modelRawLength() {
    return 7;
  }
  
  protected void unpackRawData(byte[] paramArrayOfbyte) {
    this.RollAngle = LGBBytesTool.covertToInt16(paramArrayOfbyte);
    this.PitchAngle = LGBBytesTool.covertToInt16(LGBBytesTool.subBytes(paramArrayOfbyte, 2, 2));
    this.YawAngle = LGBBytesTool.covertToInt16(LGBBytesTool.subBytes(paramArrayOfbyte, 4, 2));
    this.FlyModeRaw = LGBBytesTool.covertToUInt8(LGBBytesTool.subBytes(paramArrayOfbyte, 6, 1));
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/dronectrl/F200Ctrl/F200RxModel/RxDroneStatusModel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */