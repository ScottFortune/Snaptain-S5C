package com.sun.jna;

public class LastErrorException extends RuntimeException {
  private static final long serialVersionUID = 1L;
  
  private int errorCode;
  
  public LastErrorException(int paramInt) {
    this(paramInt, formatMessage(paramInt));
  }
  
  protected LastErrorException(int paramInt, String paramString) {
    super(paramString);
    this.errorCode = paramInt;
  }
  
  public LastErrorException(String paramString) {
    super(parseMessage(paramString.trim()));
    String str = paramString;
    try {
      if (paramString.startsWith("["))
        str = paramString.substring(1, paramString.indexOf("]")); 
      this.errorCode = Integer.parseInt(str);
    } catch (NumberFormatException numberFormatException) {
      this.errorCode = -1;
    } 
  }
  
  private static String formatMessage(int paramInt) {
    StringBuilder stringBuilder;
    String str;
    if (Platform.isWindows()) {
      stringBuilder = new StringBuilder();
      str = "GetLastError() returned ";
    } else {
      stringBuilder = new StringBuilder();
      str = "errno was ";
    } 
    stringBuilder.append(str);
    stringBuilder.append(paramInt);
    return stringBuilder.toString();
  }
  
  private static String parseMessage(String paramString) {
    try {
      String str = formatMessage(Integer.parseInt(paramString));
      paramString = str;
    } catch (NumberFormatException numberFormatException) {}
    return paramString;
  }
  
  public int getErrorCode() {
    return this.errorCode;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/sun/jna/LastErrorException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */