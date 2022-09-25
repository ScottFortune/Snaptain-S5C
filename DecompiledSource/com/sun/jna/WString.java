package com.sun.jna;

public final class WString implements CharSequence, Comparable {
  private String string;
  
  public WString(String paramString) {
    if (paramString != null) {
      this.string = paramString;
      return;
    } 
    throw new NullPointerException("String initializer must be non-null");
  }
  
  public char charAt(int paramInt) {
    return toString().charAt(paramInt);
  }
  
  public int compareTo(Object paramObject) {
    return toString().compareTo(paramObject.toString());
  }
  
  public boolean equals(Object paramObject) {
    boolean bool;
    if (paramObject instanceof WString && toString().equals(paramObject.toString())) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
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
    return this.string;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/sun/jna/WString.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */