package com.sun.jna.ptr;

import com.sun.jna.Native;
import com.sun.jna.Pointer;

public class PointerByReference extends ByReference {
  public PointerByReference() {
    this(null);
  }
  
  public PointerByReference(Pointer paramPointer) {
    super(Native.POINTER_SIZE);
    setValue(paramPointer);
  }
  
  public Pointer getValue() {
    return getPointer().getPointer(0L);
  }
  
  public void setValue(Pointer paramPointer) {
    getPointer().setPointer(0L, paramPointer);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/sun/jna/ptr/PointerByReference.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */