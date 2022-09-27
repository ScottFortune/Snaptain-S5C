package com.sun.jna.ptr;

public class DoubleByReference extends ByReference {
  public DoubleByReference() {
    this(0.0D);
  }
  
  public DoubleByReference(double paramDouble) {
    super(8);
    setValue(paramDouble);
  }
  
  public double getValue() {
    return getPointer().getDouble(0L);
  }
  
  public void setValue(double paramDouble) {
    getPointer().setDouble(0L, paramDouble);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/sun/jna/ptr/DoubleByReference.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */