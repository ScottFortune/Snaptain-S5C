package com.sun.jna;

public interface ToNativeConverter {
  Class<?> nativeType();
  
  Object toNative(Object paramObject, ToNativeContext paramToNativeContext);
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/sun/jna/ToNativeConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */