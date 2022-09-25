package com.google.gson;

import java.lang.reflect.Field;
import java.util.Locale;

public enum FieldNamingPolicy implements FieldNamingStrategy {
  IDENTITY {
    public String translateName(Field param1Field) {
      return param1Field.getName();
    }
  },
  LOWER_CASE_WITH_DASHES,
  LOWER_CASE_WITH_DOTS,
  LOWER_CASE_WITH_UNDERSCORES,
  UPPER_CAMEL_CASE {
    public String translateName(Field param1Field) {
      return null.upperCaseFirstLetter(param1Field.getName());
    }
  },
  UPPER_CAMEL_CASE_WITH_SPACES {
    public String translateName(Field param1Field) {
      return null.upperCaseFirstLetter(null.separateCamelCase(param1Field.getName(), " "));
    }
  };
  
  static {
    LOWER_CASE_WITH_UNDERSCORES = new null("LOWER_CASE_WITH_UNDERSCORES", 3);
    LOWER_CASE_WITH_DASHES = new null("LOWER_CASE_WITH_DASHES", 4);
    LOWER_CASE_WITH_DOTS = new null("LOWER_CASE_WITH_DOTS", 5);
    $VALUES = new FieldNamingPolicy[] { IDENTITY, UPPER_CAMEL_CASE, UPPER_CAMEL_CASE_WITH_SPACES, LOWER_CASE_WITH_UNDERSCORES, LOWER_CASE_WITH_DASHES, LOWER_CASE_WITH_DOTS };
  }
  
  static String separateCamelCase(String paramString1, String paramString2) {
    StringBuilder stringBuilder = new StringBuilder();
    int i = paramString1.length();
    for (byte b = 0; b < i; b++) {
      char c = paramString1.charAt(b);
      if (Character.isUpperCase(c) && stringBuilder.length() != 0)
        stringBuilder.append(paramString2); 
      stringBuilder.append(c);
    } 
    return stringBuilder.toString();
  }
  
  static String upperCaseFirstLetter(String paramString) {
    int i = paramString.length();
    byte b;
    for (b = 0; !Character.isLetter(paramString.charAt(b)) && b < i - 1; b++);
    char c = paramString.charAt(b);
    if (Character.isUpperCase(c))
      return paramString; 
    c = Character.toUpperCase(c);
    if (b == 0) {
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append(c);
      stringBuilder1.append(paramString.substring(1));
      return stringBuilder1.toString();
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(paramString.substring(0, b));
    stringBuilder.append(c);
    stringBuilder.append(paramString.substring(b + 1));
    return stringBuilder.toString();
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/google/gson/FieldNamingPolicy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */