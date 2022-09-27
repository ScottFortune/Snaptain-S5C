package com.vilyever.contextholder;

import android.app.Application;
import android.content.Context;

public class ContextHolder {
  static Context ApplicationContext;
  
  private final ContextHolder self = this;
  
  public static Context getContext() {
    Context context = ApplicationContext;
    if (context == null) {
      try {
        Application application = (Application)Class.forName("android.app.ActivityThread").getMethod("currentApplication", new Class[0]).invoke(null, (Object[])null);
        if (application != null) {
          ApplicationContext = (Context)application;
          return (Context)application;
        } 
      } catch (Exception null) {
        exception.printStackTrace();
      } 
      try {
        Application application = (Application)Class.forName("android.app.AppGlobals").getMethod("getInitialApplication", new Class[0]).invoke(null, (Object[])null);
        if (application != null) {
          ApplicationContext = (Context)application;
          return (Context)application;
        } 
      } catch (Exception exception) {
        exception.printStackTrace();
      } 
      throw new IllegalStateException("ContextHolder is not initialed, it is recommend to init with application context.");
    } 
    return (Context)exception;
  }
  
  public static void init(Context paramContext) {
    ApplicationContext = paramContext;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/vilyever/contextholder/ContextHolder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */