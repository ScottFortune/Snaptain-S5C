package com.shizhefei.utils;

import android.util.Log;

public class TaskLogUtil {
  private static final String TAG = "TaskLogUtil";
  
  private static boolean debug = false;
  
  private static final String matchText = "{}";
  
  public static void d(String paramString, Object... paramVarArgs) {
    if (debug)
      Log.d("TaskLogUtil", replace(paramString, paramVarArgs).toString()); 
  }
  
  public static void d(Throwable paramThrowable, String paramString, Object... paramVarArgs) {
    if (debug) {
      StringBuilder stringBuilder = replace(paramString, paramVarArgs);
      if (paramThrowable == null) {
        Log.d("TaskLogUtil", stringBuilder.toString());
      } else {
        Log.d("TaskLogUtil", stringBuilder.toString(), paramThrowable);
      } 
    } 
  }
  
  public static void e(String paramString, Object... paramVarArgs) {
    if (debug)
      Log.e("TaskLogUtil", replace(paramString, paramVarArgs).toString()); 
  }
  
  public static void e(Throwable paramThrowable, String paramString, Object... paramVarArgs) {
    if (debug) {
      StringBuilder stringBuilder = replace(paramString, paramVarArgs);
      if (paramThrowable == null) {
        Log.e("TaskLogUtil", stringBuilder.toString());
      } else {
        Log.e("TaskLogUtil", stringBuilder.toString(), paramThrowable);
      } 
    } 
  }
  
  private static StringBuilder replace(String paramString, Object... paramVarArgs) {
    StringBuilder stringBuilder = new StringBuilder(paramString);
    int i = paramVarArgs.length;
    byte b = 0;
    int j = 0;
    int k;
    for (k = 0; b < i; k = n) {
      Object object = paramVarArgs[b];
      int m = paramString.indexOf("{}", j);
      j = m;
      int n = k;
      if (m >= 0) {
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append("【 ");
        stringBuilder1.append(object);
        stringBuilder1.append(" 】");
        String str = stringBuilder1.toString();
        j = m + k;
        stringBuilder.replace(j, j + 2, str);
        j = m + 2;
        n = k + str.length() - 2;
      } 
      b++;
    } 
    return stringBuilder;
  }
  
  public static void setDebug(boolean paramBoolean) {
    debug = paramBoolean;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/shizhefei/utils/TaskLogUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */