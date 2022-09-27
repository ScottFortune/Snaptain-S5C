package com.sun.jna;

import java.lang.reflect.Method;

public class MethodResultContext extends FunctionResultContext {
  private final Method method;
  
  MethodResultContext(Class<?> paramClass, Function paramFunction, Object[] paramArrayOfObject, Method paramMethod) {
    super(paramClass, paramFunction, paramArrayOfObject);
    this.method = paramMethod;
  }
  
  public Method getMethod() {
    return this.method;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/sun/jna/MethodResultContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */