package com.netopsun.dronectrl.F200Ctrl.F200RxModel;

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
    switch (paramInt) {
      default:
        switch (paramInt) {
          default:
            return;
          case 243:
            this.calibrationState = DroneStatusModel.CalibrationStateType.AccCalibrateFail;
          case 242:
            this.calibrationState = DroneStatusModel.CalibrationStateType.AccCalibrateSuc;
          case 241:
            this.calibrationState = DroneStatusModel.CalibrationStateType.AccCalibrateStart;
          case 240:
            break;
        } 
        this.calibrationState = DroneStatusModel.CalibrationStateType.UnlockedButNotAllowAccCalibrate;
      case 229:
        this.calibrationState = DroneStatusModel.CalibrationStateType.VerticalRotation;
      case 228:
        this.calibrationState = DroneStatusModel.CalibrationStateType.HorizontalRotation;
      case 227:
        this.calibrationState = DroneStatusModel.CalibrationStateType.CompassCalibrateFail;
      case 226:
        this.calibrationState = DroneStatusModel.CalibrationStateType.CompassCalibrateSuc;
      case 225:
        this.calibrationState = DroneStatusModel.CalibrationStateType.CompassCalibrateStart;
      case 224:
        break;
    } 
    this.calibrationState = DroneStatusModel.CalibrationStateType.UnlockedButNotAllowCompassCalibrate;
  }
  
  protected void unpackRawData(byte[] paramArrayOfbyte) {
    setCalibrationStateRaw(LGBBytesTool.covertToUInt8(paramArrayOfbyte));
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/dronectrl/F200Ctrl/F200RxModel/RxCalibrationStatusModel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */