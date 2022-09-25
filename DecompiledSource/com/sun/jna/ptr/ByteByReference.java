package com.sun.jna.ptr;

public class ByteByReference extends ByReference {
  public ByteByReference() {
    this((byte)0);
  }
  
  public ByteByReference(byte paramByte) {
    super(1);
    setValue(paramByte);
  }
  
  public byte getValue() {
    return getPointer().getByte(0L);
  }
  
  public void setValue(byte paramByte) {
    getPointer().setByte(0L, paramByte);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/sun/jna/ptr/ByteByReference.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */