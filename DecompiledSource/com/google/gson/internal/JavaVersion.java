package com.google.gson.internal;

public final class JavaVersion {
  private static final int majorJavaVersion = determineMajorJavaVersion();
  
  private static int determineMajorJavaVersion() {
    return getMajorJavaVersion(System.getProperty("java.version"));
  }
  
  private static int extractBeginningInt(String paramString) {
    try {
      StringBuilder stringBuilder = new StringBuilder();
      this();
      null = 0;
      while (null < paramString.length()) {
        char c = paramString.charAt(null);
        if (Character.isDigit(c)) {
          stringBuilder.append(c);
          null++;
        } 
      } 
      return Integer.parseInt(stringBuilder.toString());
    } catch (NumberFormatException numberFormatException) {
      return -1;
    } 
  }
  
  public static int getMajorJavaVersion() {
    return majorJavaVersion;
  }
  
  static int getMajorJavaVersion(String paramString) {
    int i = parseDotted(paramString);
    int j = i;
    if (i == -1)
      j = extractBeginningInt(paramString); 
    return (j == -1) ? 6 : j;
  }
  
  public static boolean isJava9OrLater() {
    boolean bool;
    if (majorJavaVersion >= 9) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private static int parseDotted(String paramString) {
    try {
      String[] arrayOfString = paramString.split("[._]");
      int i = Integer.parseInt(arrayOfString[0]);
      return (i == 1 && arrayOfString.length > 1) ? Integer.parseInt(arrayOfString[1]) : i;
    } catch (NumberFormatException numberFormatException) {
      return -1;
    } 
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/google/gson/internal/JavaVersion.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */