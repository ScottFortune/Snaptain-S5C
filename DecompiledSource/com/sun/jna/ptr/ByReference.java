package com.sun.jna.ptr;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.PointerType;

public abstract class ByReference extends PointerType {
  protected ByReference(int paramInt) {
    setPointer((Pointer)new Memory(paramInt));
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/sun/jna/ptr/ByReference.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */