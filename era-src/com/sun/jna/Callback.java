package com.sun.jna;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public interface Callback {
  public static final List<String> FORBIDDEN_NAMES = Collections.unmodifiableList(Arrays.asList(new String[] { "hashCode", "equals", "toString" }));
  
  public static final String METHOD_NAME = "callback";
  
  public static interface UncaughtExceptionHandler {
    void uncaughtException(Callback param1Callback, Throwable param1Throwable);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/sun/jna/Callback.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */