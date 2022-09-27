package com.sun.jna;

public class FunctionParameterContext extends ToNativeContext {
  private Object[] args;
  
  private Function function;
  
  private int index;
  
  FunctionParameterContext(Function paramFunction, Object[] paramArrayOfObject, int paramInt) {
    this.function = paramFunction;
    this.args = paramArrayOfObject;
    this.index = paramInt;
  }
  
  public Function getFunction() {
    return this.function;
  }
  
  public int getParameterIndex() {
    return this.index;
  }
  
  public Object[] getParameters() {
    return this.args;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/sun/jna/FunctionParameterContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */