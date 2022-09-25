package com.vilyever.socketclient.util;

public class StringValidation {
  public static final String RegexAllChinese = "^[\\u4e00-\\u9fa5]*$";
  
  public static final String RegexChineseName = "^[\\u4e00-\\u9fa5]{2,15}$";
  
  public static final String RegexEmail = "w+([-+.]w+)*@w+([-.]w+)*.w+([-.]w+)*";
  
  public static final String RegexIP = "^(25[0-5]|2[0-4][0-9]|1{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\\.(25[0-5]|2[0-4][0-9]|1{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|1{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|1{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])$";
  
  public static final String RegexPhoneNumber = "^(((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8})|((\\d{3,4}-)?\\d{7,8}(-\\d{1,4})?)$";
  
  public static final String RegexPort = "^6553[0-5]|655[0-2][0-9]|65[0-4][0-9]{2}|6[0-4][0-9]{3}|[1-5][0-9]{4}|[1-9][0-9]{0,3}$";
  
  final StringValidation self = this;
  
  public static boolean validateRegex(String paramString1, String paramString2) {
    return (paramString1 == null) ? false : paramString1.matches(paramString2);
  }
  
  public static boolean validateRegularCharacter(String paramString, int paramInt1, int paramInt2) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("^\\w{");
    stringBuilder.append(paramInt1);
    stringBuilder.append(",");
    stringBuilder.append(paramInt2);
    stringBuilder.append("}$");
    return validateRegex(paramString, stringBuilder.toString());
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/vilyever/socketclient/util/StringValidation.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */