package com.sun.jna;

public class CallbackThreadInitializer {
  private boolean daemon;
  
  private boolean detach;
  
  private ThreadGroup group;
  
  private String name;
  
  public CallbackThreadInitializer() {
    this(true);
  }
  
  public CallbackThreadInitializer(boolean paramBoolean) {
    this(paramBoolean, false);
  }
  
  public CallbackThreadInitializer(boolean paramBoolean1, boolean paramBoolean2) {
    this(paramBoolean1, paramBoolean2, null);
  }
  
  public CallbackThreadInitializer(boolean paramBoolean1, boolean paramBoolean2, String paramString) {
    this(paramBoolean1, paramBoolean2, paramString, null);
  }
  
  public CallbackThreadInitializer(boolean paramBoolean1, boolean paramBoolean2, String paramString, ThreadGroup paramThreadGroup) {
    this.daemon = paramBoolean1;
    this.detach = paramBoolean2;
    this.name = paramString;
    this.group = paramThreadGroup;
  }
  
  public boolean detach(Callback paramCallback) {
    return this.detach;
  }
  
  public String getName(Callback paramCallback) {
    return this.name;
  }
  
  public ThreadGroup getThreadGroup(Callback paramCallback) {
    return this.group;
  }
  
  public boolean isDaemon(Callback paramCallback) {
    return this.daemon;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/sun/jna/CallbackThreadInitializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */