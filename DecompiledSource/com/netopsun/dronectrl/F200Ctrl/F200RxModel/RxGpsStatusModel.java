package com.netopsun.dronectrl.F200Ctrl.F200RxModel;

import com.netopsun.dronectrl.LGBCtrl.LGBBytesTool;
import com.netopsun.dronectrl.LGBCtrl.RxModel;

public class RxGpsStatusModel extends RxModel {
  private int AircraftGpsLat;
  
  private int AircraftGpsLon;
  
  private int BatteryLevel;
  
  private int GPSFine;
  
  private int HDistance;
  
  private int HVelocity;
  
  private int SatelliteNum;
  
  private int VDistance;
  
  private int VVelocity;
  
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
  
  public int modelRawLength() {
    return 19;
  }
  
  protected void unpackRawData(byte[] paramArrayOfbyte) {
    this.HDistance = LGBBytesTool.covertToUInt16(paramArrayOfbyte);
    this.HVelocity = LGBBytesTool.covertToUInt16(LGBBytesTool.subBytes(paramArrayOfbyte, 2, 2));
    this.VDistance = LGBBytesTool.covertToInt16(LGBBytesTool.subBytes(paramArrayOfbyte, 4, 2));
    this.VVelocity = LGBBytesTool.covertToInt16(LGBBytesTool.subBytes(paramArrayOfbyte, 6, 2));
    this.SatelliteNum = LGBBytesTool.covertToUInt8(LGBBytesTool.subBytes(paramArrayOfbyte, 8, 1));
    this.GPSFine = LGBBytesTool.covertToUInt8(LGBBytesTool.subBytes(paramArrayOfbyte, 9, 1));
    this.AircraftGpsLat = LGBBytesTool.covertToInt32(LGBBytesTool.subBytes(paramArrayOfbyte, 10, 4));
    this.AircraftGpsLon = LGBBytesTool.covertToInt32(LGBBytesTool.subBytes(paramArrayOfbyte, 14, 4));
    this.BatteryLevel = LGBBytesTool.covertToUInt8(LGBBytesTool.subBytes(paramArrayOfbyte, 18, 1));
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/dronectrl/F200Ctrl/F200RxModel/RxGpsStatusModel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */