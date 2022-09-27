package com.sun.jna.ptr;

public class ShortByReference extends ByReference {
  public ShortByReference() {
    this((short)0);
  }
  
  public ShortByReference(short paramShort) {
    super(2);
    setValue(paramShort);
  }
  
  public short getValue() {
    return getPointer().getShort(0L);
  }
  
  public void setValue(short paramShort) {
    getPointer().setShort(0L, paramShort);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/sun/jna/ptr/ShortByReference.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */