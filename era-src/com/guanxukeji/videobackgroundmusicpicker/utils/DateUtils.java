package com.guanxukeji.videobackgroundmusicpicker.utils;

public class DateUtils {
  public static String secToTime(int paramInt) {
    StringBuilder stringBuilder1 = new StringBuilder();
    Integer integer1 = Integer.valueOf(paramInt / 60 % 60);
    Integer integer2 = Integer.valueOf(paramInt % 60);
    if (integer1.intValue() < 10)
      stringBuilder1.append("0"); 
    StringBuilder stringBuilder2 = new StringBuilder();
    stringBuilder2.append(integer1);
    stringBuilder2.append(":");
    stringBuilder1.append(stringBuilder2.toString());
    if (integer2.intValue() < 10)
      stringBuilder1.append("0"); 
    stringBuilder1.append(integer2);
    return stringBuilder1.toString();
  }
  
  public static int timeToSec(String paramString) {
    String[] arrayOfString = paramString.split(":");
    Integer integer = Integer.valueOf(arrayOfString[0]);
    return Integer.valueOf(Integer.valueOf(arrayOfString[1]).intValue() + integer.intValue() * 60).intValue();
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/guanxukeji/videobackgroundmusicpicker/utils/DateUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */