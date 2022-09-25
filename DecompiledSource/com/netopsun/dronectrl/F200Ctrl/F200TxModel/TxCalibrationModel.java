package com.netopsun.dronectrl.F200Ctrl.F200TxModel;

import com.netopsun.dronectrl.F200Ctrl.F200CtrlMesgId;

public class TxCalibrationModel extends F200TxModel {
  private CalibrationFunCommand calibrationFunCommand;
  
  public byte commandNum;
  
  public TxCalibrationModel(CalibrationFunCommand paramCalibrationFunCommand) {
    setCalibrationFunCommand(paramCalibrationFunCommand);
  }
  
  public F200CtrlMesgId getF200CtrlMesgId() {
    return F200CtrlMesgId.MSP_SENSOR_CALIBRATION_SETUP;
  }
  
  public int[] modelValues() {
    return new int[] { this.commandNum };
  }
  
  public int[] rawByteLens() {
    return new int[] { 1 };
  }
  
  public void setCalibrationFunCommand(CalibrationFunCommand paramCalibrationFunCommand) {
    this.calibrationFunCommand = paramCalibrationFunCommand;
    int i = null.$SwitchMap$com$netopsun$dronectrl$F200Ctrl$F200TxModel$TxCalibrationModel$CalibrationFunCommand[paramCalibrationFunCommand.ordinal()];
    if (i != 1) {
      if (i == 2)
        this.commandNum = (byte)-31; 
    } else {
      this.commandNum = (byte)-15;
    } 
  }
  
  public enum CalibrationFunCommand {
    AccCalibration, CompassCalibration;
    
    static {
    
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/dronectrl/F200Ctrl/F200TxModel/TxCalibrationModel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */