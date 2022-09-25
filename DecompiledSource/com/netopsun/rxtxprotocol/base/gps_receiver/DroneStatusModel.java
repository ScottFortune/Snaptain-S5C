package com.netopsun.rxtxprotocol.base.gps_receiver;

public class DroneStatusModel {
  public int accelerationCalibration;
  
  public double accuracy;
  
  public double altitude;
  
  public int baroInitOk;
  
  public int batteryLevel;
  
  public double batteryVoltage;
  
  public int compassCalibrationProgress;
  
  public int compassCalibrationX = 1;
  
  public int compassCalibrationY = 1;
  
  public double droneFlyBackAltitude;
  
  public double droneLat;
  
  public double droneLng;
  
  public double droneMaxDistance;
  
  public double droneMaxHigh;
  
  public int flowInitOk;
  
  public int flyBackStatus = 0;
  
  public FlyModeType flyMode = FlyModeType.Stabilize;
  
  public FlyStateType flyState = FlyStateType.Locked;
  
  public int flyTime;
  
  public boolean gpsFine = false;
  
  public int gpsInitOk;
  
  public int gpsMode;
  
  public double homeDistance;
  
  public int insInitOk;
  
  public int lowPowerWarning;
  
  public int magInitOk;
  
  public double magInterference;
  
  public double pitchAngle;
  
  public int rcFastMode;
  
  public double remoteBatteryLevel;
  
  public double remoteBatteryVal;
  
  public double remoteRssi;
  
  public int reqCalibraCompass;
  
  public double rollAngle;
  
  public int satelliteNum;
  
  public double speedH;
  
  public double speedV;
  
  public int temperatureWarning;
  
  public TestInfo testInfo = new TestInfo();
  
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
  
  public enum FlyModeType {
    AltitudeHold, FlyBack, FollowMe, GPS, HeadLess, HighLimit, Hovering, Landing, Manual, Posture, RouteFlight, Stabilize, Surround, TakeOff, UnsupportedFun;
    
    static {
      Landing = new FlyModeType("Landing", 4);
      RouteFlight = new FlyModeType("RouteFlight", 5);
      FollowMe = new FlyModeType("FollowMe", 6);
      Surround = new FlyModeType("Surround", 7);
      Stabilize = new FlyModeType("Stabilize", 8);
      UnsupportedFun = new FlyModeType("UnsupportedFun", 9);
      HeadLess = new FlyModeType("HeadLess", 10);
      GPS = new FlyModeType("GPS", 11);
      AltitudeHold = new FlyModeType("AltitudeHold", 12);
      Posture = new FlyModeType("Posture", 13);
      Manual = new FlyModeType("Manual", 14);
      $VALUES = new FlyModeType[] { 
          HighLimit, Hovering, FlyBack, TakeOff, Landing, RouteFlight, FollowMe, Surround, Stabilize, UnsupportedFun, 
          HeadLess, GPS, AltitudeHold, Posture, Manual };
    }
  }
  
  public enum FlyStateType {
    Locked, OneKeyFlyingBack, OneKeyLanding, OneKeyTakeOff, OutOfControlAndFlyingBack, UnlockedAndTakeOff, FlyingBackClass1, FlyingBackClass2, Inclination_protect, LandingBecauseLowPower, UnlockedNotTakeOff, UnsupportedFun, locked_rotor;
    
    static {
      OutOfControlAndFlyingBack = new FlyStateType("OutOfControlAndFlyingBack", 3);
      FlyingBackClass1 = new FlyStateType("FlyingBackClass1", 4);
      FlyingBackClass2 = new FlyStateType("FlyingBackClass2", 5);
      OneKeyFlyingBack = new FlyStateType("OneKeyFlyingBack", 6);
      LandingBecauseLowPower = new FlyStateType("LandingBecauseLowPower", 7);
      OneKeyLanding = new FlyStateType("OneKeyLanding", 8);
      OneKeyTakeOff = new FlyStateType("OneKeyTakeOff", 9);
      UnsupportedFun = new FlyStateType("UnsupportedFun", 10);
      locked_rotor = new FlyStateType("locked_rotor", 11);
      Inclination_protect = new FlyStateType("Inclination_protect", 12);
      $VALUES = new FlyStateType[] { 
          Locked, UnlockedNotTakeOff, UnlockedAndTakeOff, OutOfControlAndFlyingBack, FlyingBackClass1, FlyingBackClass2, OneKeyFlyingBack, LandingBecauseLowPower, OneKeyLanding, OneKeyTakeOff, 
          UnsupportedFun, locked_rotor, Inclination_protect };
    }
  }
  
  public class TestInfo {
    public double AirplaneLat;
    
    public double AirplaneLon;
    
    public int BaroInitOk;
    
    public int FlowInitOk;
    
    public int GpsFine;
    
    public int GpsInitOk;
    
    public float GpsQuality;
    
    public int InsInitOk;
    
    public int MagInitOk;
    
    public int acc_x;
    
    public int acc_y;
    
    public int acc_z;
    
    public float baro_alt;
    
    public float batVal;
    
    public int current1;
    
    public int current2;
    
    public int gpsNum;
    
    public int gyro_x;
    
    public int gyro_y;
    
    public int gyro_z;
    
    public int mag_x;
    
    public int mag_y;
    
    public int mag_z;
    
    public int pitch;
    
    public int roll;
    
    public int temperature;
    
    public int yaw;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/rxtxprotocol/base/gps_receiver/DroneStatusModel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */