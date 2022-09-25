package com.netopsun.dronectrl.F200Ctrl.F200TxModel;

import com.netopsun.dronectrl.F200Ctrl.F200CtrlMesgId;

public class TxOneKeyFunModel extends F200TxModel {
  private byte commandNum;
  
  private OneKeyFunCommand oneKeyFunCommand;
  
  public TxOneKeyFunModel(OneKeyFunCommand paramOneKeyFunCommand) {
    setOneKeyFunCommand(paramOneKeyFunCommand);
  }
  
  public F200CtrlMesgId getF200CtrlMesgId() {
    return F200CtrlMesgId.MSP_F200_ONEKEY_FUN;
  }
  
  public int[] modelValues() {
    return new int[] { this.commandNum };
  }
  
  public int[] rawByteLens() {
    return new int[] { 1 };
  }
  
  public void setOneKeyFunCommand(OneKeyFunCommand paramOneKeyFunCommand) {
    this.oneKeyFunCommand = paramOneKeyFunCommand;
    switch (paramOneKeyFunCommand) {
      default:
        return;
      case Hovering:
        this.commandNum = (byte)6;
      case Flyback:
        this.commandNum = (byte)5;
      case Landing:
        this.commandNum = (byte)4;
      case TakeOff:
        this.commandNum = (byte)3;
      case LockDrone:
        this.commandNum = (byte)2;
      case UnlockDrone:
        break;
    } 
    this.commandNum = (byte)1;
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


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/dronectrl/F200Ctrl/F200TxModel/TxOneKeyFunModel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */