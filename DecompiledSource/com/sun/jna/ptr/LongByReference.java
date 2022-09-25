package com.sun.jna.ptr;

public class LongByReference extends ByReference {
  public LongByReference() {
    this(0L);
  }
  
  public LongByReference(long paramLong) {
    super(8);
    setValue(paramLong);
  }
  
  public long getValue() {
    return getPointer().getLong(0L);
  }
  
  public void setValue(long paramLong) {
    getPointer().setLong(0L, paramLong);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/sun/jna/ptr/LongByReference.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */