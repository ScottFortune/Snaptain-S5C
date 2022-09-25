package com.sun.jna.ptr;

import com.sun.jna.NativeLong;

public class NativeLongByReference extends ByReference {
  public NativeLongByReference() {
    this(new NativeLong(0L));
  }
  
  public NativeLongByReference(NativeLong paramNativeLong) {
    super(NativeLong.SIZE);
    setValue(paramNativeLong);
  }
  
  public NativeLong getValue() {
    return getPointer().getNativeLong(0L);
  }
  
  public void setValue(NativeLong paramNativeLong) {
    getPointer().setNativeLong(0L, paramNativeLong);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/sun/jna/ptr/NativeLongByReference.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */