package com.netopsun.dronectrl.LWGPSCtrl.LWGPSTxModel;

import com.netopsun.dronectrl.LWGPSCtrl.LWGPSCtrlMesgId;

public class TxOneKeyFunModel extends LWGPSTxModel {
  private byte commandNum;
  
  private OneKeyFunCommand oneKeyFunCommand;
  
  public TxOneKeyFunModel(OneKeyFunCommand paramOneKeyFunCommand) {
    setOneKeyFunCommand(paramOneKeyFunCommand);
  }
  
  public LWGPSCtrlMesgId getLWGPSCtrlMesgId() {
    return LWGPSCtrlMesgId.MSP_LWGPS_ONEKEY_FUN;
  }
  
  public int[] modelValues() {
    return new int[] { this.commandNum };
  }
  
  public int[] rawByteLens() {
    return new int[] { 1 };
  }
  
  public void setOneKeyFunCommand(OneKeyFunCommand paramOneKeyFunCommand) {
    this.oneKeyFunCommand = paramOneKeyFunCommand;
    int i = null.$SwitchMap$com$netopsun$dronectrl$LWGPSCtrl$LWGPSTxModel$TxOneKeyFunModel$OneKeyFunCommand[paramOneKeyFunCommand.ordinal()];
    if (i != 1) {
      if (i == 2)
        this.commandNum = (byte)1; 
    } else {
      this.commandNum = (byte)0;
    } 
  }
  
  public enum OneKeyFunCommand {
    Flyback, Hovering, Landing, LockDrone, TakeOff, UnlockDrone;
    
    static {
      Landing = new OneKeyFunCommand("Landing", 3);
      Flyback = new OneKeyFunCommand("Flyback", 4);
      Hovering = new OneKeyFunCommand("Hovering", 5);
      $VALUES = new OneKeyFunCommand[] { UnlockDrone, LockDrone, TakeOff, Landing, Flyback, Hovering };
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/dronectrl/LWGPSCtrl/LWGPSTxModel/TxOneKeyFunModel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */