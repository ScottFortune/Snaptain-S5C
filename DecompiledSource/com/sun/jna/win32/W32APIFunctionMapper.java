package com.sun.jna.win32;

import com.sun.jna.FunctionMapper;
import com.sun.jna.NativeLibrary;
import java.lang.reflect.Method;

public class W32APIFunctionMapper implements FunctionMapper {
  public static final FunctionMapper ASCII;
  
  public static final FunctionMapper UNICODE = new W32APIFunctionMapper(true);
  
  private final String suffix;
  
  static {
    ASCII = new W32APIFunctionMapper(false);
  }
  
  protected W32APIFunctionMapper(boolean paramBoolean) {
    String str;
    if (paramBoolean) {
      str = "W";
    } else {
      str = "A";
    } 
    this.suffix = str;
  }
  
  public String getFunctionName(NativeLibrary paramNativeLibrary, Method paramMethod) {
    String str2 = paramMethod.getName();
    String str1 = str2;
    if (!str2.endsWith("W")) {
      str1 = str2;
      if (!str2.endsWith("A"))
        try {
          StringBuilder stringBuilder = new StringBuilder();
          this();
          stringBuilder.append(str2);
          stringBuilder.append(this.suffix);
          String str = paramNativeLibrary.getFunction(stringBuilder.toString(), 63).getName();
        } catch (UnsatisfiedLinkError unsatisfiedLinkError) {
          str1 = str2;
        }  
    } 
    return str1;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/sun/jna/win32/W32APIFunctionMapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */