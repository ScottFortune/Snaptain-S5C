package com.sun.jna;

public class NativeLong extends IntegerType {
  public static final int SIZE = Native.LONG_SIZE;
  
  private static final long serialVersionUID = 1L;
  
  public NativeLong() {
    this(0L);
  }
  
  public NativeLong(long paramLong) {
    this(paramLong, false);
  }
  
  public NativeLong(long paramLong, boolean paramBoolean) {
    super(SIZE, paramLong, paramBoolean);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/sun/jna/NativeLong.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */