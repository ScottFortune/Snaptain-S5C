package com.sun.jna.win32;

import com.sun.jna.FunctionMapper;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.sun.jna.NativeMapped;
import com.sun.jna.NativeMappedConverter;
import java.lang.reflect.Method;

public class StdCallFunctionMapper implements FunctionMapper {
  protected int getArgumentNativeStackSize(Class<?> paramClass) {
    Class<?> clazz = paramClass;
    if (NativeMapped.class.isAssignableFrom(paramClass))
      clazz = NativeMappedConverter.getInstance(paramClass).nativeType(); 
    if (clazz.isArray())
      return Native.POINTER_SIZE; 
    try {
      return Native.getNativeSize(clazz);
    } catch (IllegalArgumentException illegalArgumentException) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Unknown native stack allocation size for ");
      stringBuilder.append(clazz);
      throw new IllegalArgumentException(stringBuilder.toString());
    } 
  }
  
  public String getFunctionName(NativeLibrary paramNativeLibrary, Method paramMethod) {
    String str1;
    String str2 = paramMethod.getName();
    Class[] arrayOfClass = paramMethod.getParameterTypes();
    int i = arrayOfClass.length;
    byte b = 0;
    int j = 0;
    while (b < i) {
      j += getArgumentNativeStackSize(arrayOfClass[b]);
      b++;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(str2);
    stringBuilder.append("@");
    stringBuilder.append(j);
    String str3 = stringBuilder.toString();
    try {
      String str = paramNativeLibrary.getFunction(str3, 63).getName();
      str1 = str;
    } catch (UnsatisfiedLinkError unsatisfiedLinkError) {
      try {
        StringBuilder stringBuilder1 = new StringBuilder();
        this();
        stringBuilder1.append("_");
        stringBuilder1.append(str3);
        str1 = str1.getFunction(stringBuilder1.toString(), 63).getName();
      } catch (UnsatisfiedLinkError unsatisfiedLinkError1) {
        str1 = str2;
      } 
    } 
    return str1;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/sun/jna/win32/StdCallFunctionMapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */