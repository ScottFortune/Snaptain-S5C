package com.netopsun.dronectrl.LWGPSCtrl.LWGPSRxModel;

import com.netopsun.dronectrl.LGBCtrl.LGBBytesTool;
import com.netopsun.dronectrl.LGBCtrl.RxModel;

public class RxWaypointConfigModel extends RxModel {
  public int waypointMaxSpeed;
  
  public int waypointMinAltitude;
  
  public int waypointTime;
  
  public int getWaypointMaxSpeed() {
    return this.waypointMaxSpeed;
  }
  
  public int getWaypointMinAltitude() {
    return this.waypointMinAltitude;
  }
  
  public int getWaypointTime() {
    return this.waypointTime;
  }
  
  public int modelRawLength() {
    return 3;
  }
  
  protected void unpackRawData(byte[] paramArrayOfbyte) {
    this.waypointMaxSpeed = LGBBytesTool.covertToUInt8(paramArrayOfbyte);
    this.waypointTime = LGBBytesTool.covertToUInt8(LGBBytesTool.subBytes(paramArrayOfbyte, 1, 1));
    this.waypointMinAltitude = LGBBytesTool.covertToUInt8(LGBBytesTool.subBytes(paramArrayOfbyte, 2, 1));
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/dronectrl/LWGPSCtrl/LWGPSRxModel/RxWaypointConfigModel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */