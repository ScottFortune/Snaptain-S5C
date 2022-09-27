package com.yang.firework.utils;

import android.content.Context;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class RawResourceReader {
  public static String readTextFileFromRawResource(Context paramContext, int paramInt) {
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(paramContext.getResources().openRawResource(paramInt)));
    StringBuilder stringBuilder = new StringBuilder();
    try {
      while (true) {
        String str = bufferedReader.readLine();
        if (str != null) {
          stringBuilder.append(str);
          stringBuilder.append('\n');
          continue;
        } 
        return stringBuilder.toString();
      } 
    } catch (IOException iOException) {
      return null;
    } 
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/yang/firework/utils/RawResourceReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */