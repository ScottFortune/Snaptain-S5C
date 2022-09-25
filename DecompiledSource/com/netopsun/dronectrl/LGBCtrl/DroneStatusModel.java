package com.netopsun.dronectrl.LGBCtrl;

public class DroneStatusModel {
  public double altitude;
  
  public int batteryLevel;
  
  public CalibrationStateType calibrationState;
  
  public double droneLat;
  
  public double droneLng;
  
  public FlyModeType flyMode;
  
  public FlyStateType flyState;
  
  public int flyTime;
  
  public double homeDistance;
  
  public double pitchAngle;
  
  public double rollAngle;
  
  public int satelliteNum;
  
  public double speedH;
  
  public double speedV;
  
  public double yawAngle;
  
  public String droneStatusString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("rollAngle:");
    stringBuilder.append(this.rollAngle);
    stringBuilder.append("\npitchAngle:");
    stringBuilder.append(this.pitchAngle);
    stringBuilder.append("\nyawAngle:");
    stringBuilder.append(this.yawAngle);
    stringBuilder.append("\ndroneLat:");
    stringBuilder.append(this.droneLat);
    stringBuilder.append("\ndroneLng:");
    stringBuilder.append(this.droneLng);
    stringBuilder.append("\nsatelliteNum:");
    stringBuilder.append(this.satelliteNum);
    stringBuilder.append("\nbatteryLevel:");
    stringBuilder.append(this.batteryLevel);
    stringBuilder.append("\nspeedH:");
    stringBuilder.append(this.speedH);
    stringBuilder.append("\nspeedV:");
    stringBuilder.append(this.speedV);
    stringBuilder.append("\naltitude:");
    stringBuilder.append(this.altitude);
    stringBuilder.append("\nhomeDistance:");
    stringBuilder.append(this.homeDistance);
    stringBuilder.append("\nflyTime:");
    stringBuilder.append(this.flyTime);
    stringBuilder.append("\nflyMode:");
    stringBuilder.append(this.flyMode);
    stringBuilder.append("\nflyState:");
    stringBuilder.append(this.flyState);
    stringBuilder.append("\n");
    return stringBuilder.toString();
  }
  
  public enum CalibrationStateType {
    AccCalibrateFail, AccCalibrateStart, AccCalibrateSuc, CompassCalibrateFail, CompassCalibrateStart, CompassCalibrateSuc, HorizontalRotation, UnlockedButNotAllowAccCalibrate, UnlockedButNotAllowCompassCalibrate, VerticalRotation;
    
    static {
      AccCalibrateFail = new CalibrationStateType("AccCalibrateFail", 3);
      UnlockedButNotAllowCompassCalibrate = new CalibrationStateType("UnlockedButNotAllowCompassCalibrate", 4);
      CompassCalibrateStart = new CalibrationStateType("CompassCalibrateStart", 5);
      CompassCalibrateSuc = new CalibrationStateType("CompassCalibrateSuc", 6);
      CompassCalibrateFail = new CalibrationStateType("CompassCalibrateFail", 7);
      HorizontalRotation = new CalibrationStateType("HorizontalRotation", 8);
      VerticalRotation = new CalibrationStateType("VerticalRotation", 9);
      $VALUES = new CalibrationStateType[] { UnlockedButNotAllowAccCalibrate, AccCalibrateStart, AccCalibrateSuc, AccCalibrateFail, UnlockedButNotAllowCompassCalibrate, CompassCalibrateStart, CompassCalibrateSuc, CompassCalibrateFail, HorizontalRotation, VerticalRotation };
    }
  }
  
  public enum FlyModeType {
    FlyBack, FollowMe, HighLimit, Hovering, Landing, RouteFlight, Stabilize, Surround, TakeOff, UnsupportedFun;
    
    static {
      Landing = new FlyModeType("Landing", 4);
      RouteFlight = new FlyModeType("RouteFlight", 5);
      FollowMe = new FlyModeType("FollowMe", 6);
      Surround = new FlyModeType("Surround", 7);
      Stabilize = new FlyModeType("Stabilize", 8);
      UnsupportedFun = new FlyModeType("UnsupportedFun", 9);
      $VALUES = new FlyModeType[] { HighLimit, Hovering, FlyBack, TakeOff, Landing, RouteFlight, FollowMe, Surround, Stabilize, UnsupportedFun };
    }
  }
  
  public enum FlyStateType {
    Locked, OneKeyFlyingBack, OneKeyLanding, OneKeyTakeOff, OutOfControlAndFlyingBack, UnlockedAndTakeOff, FlyingBackClass1, FlyingBackClass2, LandingBecauseLowPower, UnlockedNotTakeOff, UnsupportedFun;
    
    static {
      OutOfControlAndFlyingBack = new FlyStateType("OutOfControlAndFlyingBack", 3);
      FlyingBackClass1 = new FlyStateType("FlyingBackClass1", 4);
      FlyingBackClass2 = new FlyStateType("FlyingBackClass2", 5);
      OneKeyFlyingBack = new FlyStateType("OneKeyFlyingBack", 6);
      LandingBecauseLowPower = new FlyStateType("LandingBecauseLowPower", 7);
      OneKeyLanding = new FlyStateType("OneKeyLanding", 8);
      OneKeyTakeOff = new FlyStateType("OneKeyTakeOff", 9);
      UnsupportedFun = new FlyStateType("UnsupportedFun", 10);
      $VALUES = new FlyStateType[] { 
          Locked, UnlockedNotTakeOff, UnlockedAndTakeOff, OutOfControlAndFlyingBack, FlyingBackClass1, FlyingBackClass2, OneKeyFlyingBack, LandingBecauseLowPower, OneKeyLanding, OneKeyTakeOff, 
          UnsupportedFun };
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/dronectrl/LGBCtrl/DroneStatusModel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */