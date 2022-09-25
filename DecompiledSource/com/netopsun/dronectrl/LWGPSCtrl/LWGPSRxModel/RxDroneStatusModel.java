package com.netopsun.dronectrl.LWGPSCtrl.LWGPSRxModel;

import com.netopsun.dronectrl.LGBCtrl.DroneStatusModel;
import com.netopsun.dronectrl.LGBCtrl.LGBBytesTool;
import com.netopsun.dronectrl.LGBCtrl.RxModel;

public class RxDroneStatusModel extends RxModel {
  private int AircraftGpsLat;
  
  private int AircraftGpsLon;
  
  private int BatteryLevel;
  
  private int FlyModeRaw;
  
  private int GPSFine;
  
  private int HDistance;
  
  private int HVelocity;
  
  private int PitchAngle;
  
  private int RollAngle;
  
  private int SatelliteNum;
  
  private int VDistance;
  
  private int VVelocity;
  
  private int YawAngle;
  
  public double getAircraftGpsLat() {
    double d = this.AircraftGpsLat;
    Double.isNaN(d);
    return d / 1.0E7D;
  }
  
  public double getAircraftGpsLon() {
    double d = this.AircraftGpsLon;
    Double.isNaN(d);
    return d / 1.0E7D;
  }
  
  public int getBatteryLevel() {
    return this.BatteryLevel;
  }
  
  public DroneStatusModel.FlyModeType getFlyMode() {
    null = DroneStatusModel.FlyModeType.UnsupportedFun;
    switch ((this.FlyModeRaw & 0xF0) >> 4) {
      default:
        return DroneStatusModel.FlyModeType.UnsupportedFun;
      case 8:
        return DroneStatusModel.FlyModeType.Stabilize;
      case 7:
        return DroneStatusModel.FlyModeType.Surround;
      case 6:
        return DroneStatusModel.FlyModeType.FollowMe;
      case 5:
        return DroneStatusModel.FlyModeType.RouteFlight;
      case 4:
        return DroneStatusModel.FlyModeType.FlyBack;
      case 3:
        return DroneStatusModel.FlyModeType.Landing;
      case 2:
        return DroneStatusModel.FlyModeType.TakeOff;
      case 1:
        break;
    } 
    return DroneStatusModel.FlyModeType.Hovering;
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
  
  public boolean getGPSFine() {
    boolean bool;
    if (this.GPSFine != 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public double getHDistance() {
    double d = this.HDistance;
    Double.isNaN(d);
    return d * 0.1D;
  }
  
  public double getHVelocity() {
    double d = this.HVelocity;
    Double.isNaN(d);
    return d * 0.1D;
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
  
  public int getSatelliteNum() {
    return this.SatelliteNum;
  }
  
  public double getVDistance() {
    double d = this.VDistance;
    Double.isNaN(d);
    return d * 0.1D;
  }
  
  public int getVVelocity() {
    return this.VVelocity;
  }
  
  public double getYawAngle() {
    double d = this.YawAngle;
    Double.isNaN(d);
    return d * 0.1D;
  }
  
  public int modelRawLength() {
    return 24;
  }
  
  protected void unpackRawData(byte[] paramArrayOfbyte) {
    this.RollAngle = LGBBytesTool.covertToInt16(paramArrayOfbyte);
    this.PitchAngle = LGBBytesTool.covertToInt16(LGBBytesTool.subBytes(paramArrayOfbyte, 2, 2));
    this.YawAngle = LGBBytesTool.covertToInt16(LGBBytesTool.subBytes(paramArrayOfbyte, 4, 2));
    this.FlyModeRaw = LGBBytesTool.covertToUInt8(LGBBytesTool.subBytes(paramArrayOfbyte, 6, 1));
    this.HDistance = LGBBytesTool.covertToUInt16(LGBBytesTool.subBytes(paramArrayOfbyte, 7, 2));
    this.HVelocity = LGBBytesTool.covertToUInt8(LGBBytesTool.subBytes(paramArrayOfbyte, 9, 1));
    this.VDistance = LGBBytesTool.covertToInt16(LGBBytesTool.subBytes(paramArrayOfbyte, 10, 2));
    this.VVelocity = LGBBytesTool.covertToInt8(LGBBytesTool.subBytes(paramArrayOfbyte, 12, 1));
    this.SatelliteNum = LGBBytesTool.covertToUInt8(LGBBytesTool.subBytes(paramArrayOfbyte, 13, 1));
    this.GPSFine = LGBBytesTool.covertToUInt8(LGBBytesTool.subBytes(paramArrayOfbyte, 14, 1));
    this.AircraftGpsLat = LGBBytesTool.covertToInt32(LGBBytesTool.subBytes(paramArrayOfbyte, 15, 4));
    this.AircraftGpsLon = LGBBytesTool.covertToInt32(LGBBytesTool.subBytes(paramArrayOfbyte, 19, 4));
    this.BatteryLevel = LGBBytesTool.covertToUInt8(LGBBytesTool.subBytes(paramArrayOfbyte, 23, 1));
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/dronectrl/LWGPSCtrl/LWGPSRxModel/RxDroneStatusModel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */