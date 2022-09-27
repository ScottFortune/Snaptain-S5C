package com.shizhefei.task;

public enum Code {
  CANCEL, EXCEPTION, SUCCESS;
  
  static {
    EXCEPTION = new Code("EXCEPTION", 1);
    CANCEL = new Code("CANCEL", 2);
    $VALUES = new Code[] { SUCCESS, EXCEPTION, CANCEL };
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/shizhefei/task/Code.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */