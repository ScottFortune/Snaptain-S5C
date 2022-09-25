package com.netopsun.dronectrl.LWGPSCtrl.LWGPSTxModel;

import com.netopsun.dronectrl.LWGPSCtrl.LWGPSCtrlMesgId;

public class TxOneKeyFun3Model extends LWGPSTxModel {
  private byte commandNum;
  
  private TxOneKeyFunModel.OneKeyFunCommand oneKeyFunCommand;
  
  public TxOneKeyFun3Model(TxOneKeyFunModel.OneKeyFunCommand paramOneKeyFunCommand) {
    setOneKeyFunCommand(paramOneKeyFunCommand);
  }
  
  public LWGPSCtrlMesgId getLWGPSCtrlMesgId() {
    return LWGPSCtrlMesgId.MSP_LWGPS_ONEKEY_FUN3;
  }
  
  public int[] modelValues() {
    return new int[] { this.commandNum };
  }
  
  public int[] rawByteLens() {
    return new int[] { 1 };
  }
  
  public void setOneKeyFunCommand(TxOneKeyFunModel.OneKeyFunCommand paramOneKeyFunCommand) {
    this.oneKeyFunCommand = paramOneKeyFunCommand;
    if (null.$SwitchMap$com$netopsun$dronectrl$LWGPSCtrl$LWGPSTxModel$TxOneKeyFunModel$OneKeyFunCommand[paramOneKeyFunCommand.ordinal()] == 1)
      this.commandNum = (byte)1; 
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/dronectrl/LWGPSCtrl/LWGPSTxModel/TxOneKeyFun3Model.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */