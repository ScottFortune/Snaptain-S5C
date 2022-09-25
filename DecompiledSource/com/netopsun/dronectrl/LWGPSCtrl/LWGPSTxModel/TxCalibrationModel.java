package com.netopsun.dronectrl.LWGPSCtrl.LWGPSTxModel;

import com.netopsun.dronectrl.LWGPSCtrl.LWGPSCtrlMesgId;

public class TxCalibrationModel extends LWGPSTxModel {
  private CalibrationFunCommand calibrationFunCommand;
  
  public byte commandNum;
  
  public TxCalibrationModel(CalibrationFunCommand paramCalibrationFunCommand) {
    setCalibrationFunCommand(paramCalibrationFunCommand);
  }
  
  public LWGPSCtrlMesgId getLWGPSCtrlMesgId() {
    return LWGPSCtrlMesgId.MSP_SENSOR_CALIBRATION_SETUP;
  }
  
  public int[] modelValues() {
    return new int[] { this.commandNum };
  }
  
  public int[] rawByteLens() {
    return new int[] { 1 };
  }
  
  public void setCalibrationFunCommand(CalibrationFunCommand paramCalibrationFunCommand) {
    this.calibrationFunCommand = paramCalibrationFunCommand;
    int i = null.$SwitchMap$com$netopsun$dronectrl$LWGPSCtrl$LWGPSTxModel$TxCalibrationModel$CalibrationFunCommand[paramCalibrationFunCommand.ordinal()];
    if (i != 1) {
      if (i == 2)
        this.commandNum = (byte)2; 
    } else {
      this.commandNum = (byte)1;
    } 
  }
  
  public enum CalibrationFunCommand {
    AccCalibration, CompassCalibration;
    
    static {
    
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/dronectrl/LWGPSCtrl/LWGPSTxModel/TxCalibrationModel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */