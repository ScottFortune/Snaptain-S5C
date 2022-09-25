package com.sun.jna;

public class FunctionResultContext extends FromNativeContext {
  private Object[] args;
  
  private Function function;
  
  FunctionResultContext(Class<?> paramClass, Function paramFunction, Object[] paramArrayOfObject) {
    super(paramClass);
    this.function = paramFunction;
    this.args = paramArrayOfObject;
  }
  
  public Object[] getArguments() {
    return this.args;
  }
  
  public Function getFunction() {
    return this.function;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/sun/jna/FunctionResultContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */