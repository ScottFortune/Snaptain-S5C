package com.sun.jna;

import java.lang.reflect.Method;

public class CallbackParameterContext extends FromNativeContext {
  private Object[] args;
  
  private int index;
  
  private Method method;
  
  CallbackParameterContext(Class<?> paramClass, Method paramMethod, Object[] paramArrayOfObject, int paramInt) {
    super(paramClass);
    this.method = paramMethod;
    this.args = paramArrayOfObject;
    this.index = paramInt;
  }
  
  public Object[] getArguments() {
    return this.args;
  }
  
  public int getIndex() {
    return this.index;
  }
  
  public Method getMethod() {
    return this.method;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/sun/jna/CallbackParameterContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */