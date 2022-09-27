package com.sun.jna.ptr;

public class FloatByReference extends ByReference {
  public FloatByReference() {
    this(0.0F);
  }
  
  public FloatByReference(float paramFloat) {
    super(4);
    setValue(paramFloat);
  }
  
  public float getValue() {
    return getPointer().getFloat(0L);
  }
  
  public void setValue(float paramFloat) {
    getPointer().setFloat(0L, paramFloat);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/sun/jna/ptr/FloatByReference.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */