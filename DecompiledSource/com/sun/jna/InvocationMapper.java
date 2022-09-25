package com.sun.jna;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public interface InvocationMapper {
  InvocationHandler getInvocationHandler(NativeLibrary paramNativeLibrary, Method paramMethod);
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/sun/jna/InvocationMapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */