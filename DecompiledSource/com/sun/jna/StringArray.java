package com.sun.jna;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StringArray extends Memory implements Function.PostCallRead {
  private String encoding;
  
  private List<NativeString> natives = new ArrayList<NativeString>();
  
  private Object[] original;
  
  public StringArray(WString[] paramArrayOfWString) {
    this((Object[])paramArrayOfWString, "--WIDE-STRING--");
  }
  
  private StringArray(Object[] paramArrayOfObject, String paramString) {
    super(((paramArrayOfObject.length + 1) * Native.POINTER_SIZE));
    this.original = paramArrayOfObject;
    this.encoding = paramString;
    byte b = 0;
    while (true) {
      int i = paramArrayOfObject.length;
      NativeString nativeString = null;
      if (b < i) {
        Pointer pointer;
        if (paramArrayOfObject[b] != null) {
          nativeString = new NativeString(paramArrayOfObject[b].toString(), paramString);
          this.natives.add(nativeString);
          pointer = nativeString.getPointer();
        } 
        setPointer((Native.POINTER_SIZE * b), pointer);
        b++;
        continue;
      } 
      setPointer((Native.POINTER_SIZE * paramArrayOfObject.length), null);
      return;
    } 
  }
  
  public StringArray(String[] paramArrayOfString) {
    this(paramArrayOfString, false);
  }
  
  public StringArray(String[] paramArrayOfString, String paramString) {
    this((Object[])paramArrayOfString, paramString);
  }
  
  public StringArray(String[] paramArrayOfString, boolean paramBoolean) {
    this(arrayOfObject, str);
  }
  
  public void read() {
    boolean bool1 = this.original instanceof WString[];
    boolean bool2 = "--WIDE-STRING--".equals(this.encoding);
    for (byte b = 0; b < this.original.length; b++) {
      WString wString;
      Pointer pointer = getPointer((Native.POINTER_SIZE * b));
      String str = null;
      if (pointer != null) {
        if (bool2) {
          str = pointer.getWideString(0L);
        } else {
          str = pointer.getString(0L, this.encoding);
        } 
        String str1 = str;
        str = str1;
        if (bool1)
          wString = new WString(str1); 
      } 
      this.original[b] = wString;
    } 
  }
  
  public String toString() {
    String str;
    if ("--WIDE-STRING--".equals(this.encoding)) {
      str = "const wchar_t*[]";
    } else {
      str = "const char*[]";
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(str);
    stringBuilder.append(Arrays.asList(this.original));
    return stringBuilder.toString();
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/sun/jna/StringArray.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */