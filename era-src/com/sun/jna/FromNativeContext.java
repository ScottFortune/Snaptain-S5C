package com.sun.jna;

public class FromNativeContext {
  private Class<?> type;
  
  FromNativeContext(Class<?> paramClass) {
    this.type = paramClass;
  }
  
  public Class<?> getTargetType() {
    return this.type;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/sun/jna/FromNativeContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */