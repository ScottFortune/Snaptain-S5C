package com.sun.jna.win32;

import com.sun.jna.Callback;
import com.sun.jna.FunctionMapper;
import com.sun.jna.Library;

public interface StdCallLibrary extends Library, StdCall {
  public static final FunctionMapper FUNCTION_MAPPER = new StdCallFunctionMapper();
  
  public static final int STDCALL_CONVENTION = 63;
  
  public static interface StdCallCallback extends Callback, StdCall {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/sun/jna/win32/StdCallLibrary.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */