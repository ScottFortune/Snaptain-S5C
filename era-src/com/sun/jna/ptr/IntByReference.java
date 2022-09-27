package com.sun.jna.ptr;

public class IntByReference extends ByReference {
  public IntByReference() {
    this(0);
  }
  
  public IntByReference(int paramInt) {
    super(4);
    setValue(paramInt);
  }
  
  public int getValue() {
    return getPointer().getInt(0L);
  }
  
  public void setValue(int paramInt) {
    getPointer().setInt(0L, paramInt);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/sun/jna/ptr/IntByReference.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */