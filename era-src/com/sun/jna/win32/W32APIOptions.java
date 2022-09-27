package com.sun.jna.win32;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public interface W32APIOptions extends StdCallLibrary {
  static {
    ASCII_OPTIONS = Collections.unmodifiableMap(new HashMap<String, Object>() {
          private static final long serialVersionUID = 1L;
        });
    if (Boolean.getBoolean("w32.ascii")) {
      map = ASCII_OPTIONS;
    } else {
      map = UNICODE_OPTIONS;
    } 
    DEFAULT_OPTIONS = map;
  }
  
  static {
    Map<String, Object> map;
  }
  
  public static final Map<String, Object> ASCII_OPTIONS;
  
  public static final Map<String, Object> DEFAULT_OPTIONS;
  
  public static final Map<String, Object> UNICODE_OPTIONS = Collections.unmodifiableMap(new HashMap<String, Object>() {
        private static final long serialVersionUID = 1L;
      });
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/sun/jna/win32/W32APIOptions.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */