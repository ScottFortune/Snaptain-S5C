package com.sun.jna;

class NativeString implements CharSequence, Comparable {
  static final String WIDE_STRING = "--WIDE-STRING--";
  
  private String encoding;
  
  private Pointer pointer;
  
  public NativeString(WString paramWString) {
    this(paramWString.toString(), "--WIDE-STRING--");
  }
  
  public NativeString(String paramString) {
    this(paramString, Native.getDefaultStringEncoding());
  }
  
  public NativeString(String paramString1, String paramString2) {
    if (paramString1 != null) {
      this.encoding = paramString2;
      if ("--WIDE-STRING--".equals(this.encoding)) {
        this.pointer = new StringMemory(((paramString1.length() + 1) * Native.WCHAR_SIZE));
        this.pointer.setWideString(0L, paramString1);
      } else {
        byte[] arrayOfByte = Native.getBytes(paramString1, paramString2);
        this.pointer = new StringMemory((arrayOfByte.length + 1));
        this.pointer.write(0L, arrayOfByte, 0, arrayOfByte.length);
        this.pointer.setByte(arrayOfByte.length, (byte)0);
      } 
      return;
    } 
    throw new NullPointerException("String must not be null");
  }
  
  public NativeString(String paramString, boolean paramBoolean) {
    this(paramString, str);
  }
  
  public char charAt(int paramInt) {
    return toString().charAt(paramInt);
  }
  
  public int compareTo(Object paramObject) {
    return (paramObject == null) ? 1 : toString().compareTo(paramObject.toString());
  }
  
  public boolean equals(Object paramObject) {
    boolean bool = paramObject instanceof CharSequence;
    boolean bool1 = false;
    boolean bool2 = bool1;
    if (bool) {
      bool2 = bool1;
      if (compareTo(paramObject) == 0)
        bool2 = true; 
    } 
    return bool2;
  }
  
  public Pointer getPointer() {
    return this.pointer;
  }
  
  public int hashCode() {
    return toString().hashCode();
  }
  
  public int length() {
    return toString().length();
  }
  
  public CharSequence subSequence(int paramInt1, int paramInt2) {
    return toString().subSequence(paramInt1, paramInt2);
  }
  
  public String toString() {
    String str;
    if ("--WIDE-STRING--".equals(this.encoding)) {
      str = this.pointer.getWideString(0L);
    } else {
      str = this.pointer.getString(0L, this.encoding);
    } 
    return str;
  }
  
  private class StringMemory extends Memory {
    public StringMemory(long param1Long) {
      super(param1Long);
    }
    
    public String toString() {
      return NativeString.this.toString();
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/sun/jna/NativeString.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */