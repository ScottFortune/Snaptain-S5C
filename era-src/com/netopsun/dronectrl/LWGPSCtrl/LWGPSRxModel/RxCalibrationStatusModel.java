package com.netopsun.dronectrl.LWGPSCtrl.LWGPSRxModel;

import com.netopsun.dronectrl.LGBCtrl.DroneStatusModel;
import com.netopsun.dronectrl.LGBCtrl.LGBBytesTool;
import com.netopsun.dronectrl.LGBCtrl.RxModel;

public class RxCalibrationStatusModel extends RxModel {
  public DroneStatusModel.CalibrationStateType calibrationState;
  
  protected int calibrationStateRaw;
  
  public int modelRawLength() {
    return 1;
  }
  
  public void setCalibrationStateRaw(int paramInt) {
    this.calibrationStateRaw = paramInt;
    if (paramInt != 36) {
      switch (paramInt) {
        default:
          switch (paramInt) {
            default:
              return;
            case 34:
              this.calibrationState = DroneStatusModel.CalibrationStateType.CompassCalibrateSuc;
            case 33:
              this.calibrationState = DroneStatusModel.CalibrationStateType.CompassCalibrateStart;
            case 32:
              break;
          } 
          this.calibrationState = DroneStatusModel.CalibrationStateType.UnlockedButNotAllowCompassCalibrate;
        case 18:
          this.calibrationState = DroneStatusModel.CalibrationStateType.AccCalibrateFail;
        case 17:
          this.calibrationState = DroneStatusModel.CalibrationStateType.AccCalibrateSuc;
        case 16:
          break;
      } 
      this.calibrationState = DroneStatusModel.CalibrationStateType.AccCalibrateStart;
    } 
    this.calibrationState = DroneStatusModel.CalibrationStateType.CompassCalibrateFail;
  }
  
  protected void unpackRawData(byte[] paramArrayOfbyte) {
    setCalibrationStateRaw(LGBBytesTool.covertToUInt8(paramArrayOfbyte));
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/dronectrl/LWGPSCtrl/LWGPSRxModel/RxCalibrationStatusModel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */