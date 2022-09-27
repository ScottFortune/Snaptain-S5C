package com.mylhyl.acp;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MiuiOs {
  public static final String UNKNOWN = "UNKNOWN";
  
  public static Intent getSettingIntent(Context paramContext) {
    if (getSystemVersionCode() >= 6) {
      Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
      intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
      intent.putExtra("extra_pkgname", paramContext.getPackageName());
      return intent;
    } 
    return null;
  }
  
  public static String getSystemProperty() {
    Exception exception2;
    Object object = null;
    String str1 = null;
    String str2 = str1;
    try {
      Process process = Runtime.getRuntime().exec("getprop ro.miui.ui.version.name");
      str2 = str1;
      BufferedReader bufferedReader = new BufferedReader();
      str2 = str1;
      InputStreamReader inputStreamReader = new InputStreamReader();
      str2 = str1;
      this(process.getInputStream());
      str2 = str1;
      this(inputStreamReader, 1024);
      try {
        return str2;
      } catch (IOException iOException1) {
      
      } finally {
        String str;
        str1 = null;
        IOException iOException1 = (IOException)exception2;
      } 
    } catch (IOException iOException) {
      exception2 = (Exception)object;
    } finally {}
    Exception exception1 = exception2;
    iOException.printStackTrace();
    if (exception2 != null)
      try {
        exception2.close();
      } catch (IOException iOException1) {
        iOException1.printStackTrace();
      }  
    return "UNKNOWN";
  }
  
  public static int getSystemVersionCode() {
    String str = getSystemProperty();
    if (!TextUtils.isEmpty(str) && !str.equals("UNKNOWN") && str.length() == 2 && str.toUpperCase().startsWith("V")) {
      Integer integer = Integer.valueOf(0);
      try {
        Integer integer1 = Integer.valueOf(str.substring(1));
        integer = integer1;
      } catch (NumberFormatException numberFormatException) {
        numberFormatException.printStackTrace();
      } 
      return integer.intValue();
    } 
    return 0;
  }
  
  public static boolean isIntentAvailable(Context paramContext, Intent paramIntent) {
    boolean bool = false;
    if (paramIntent == null)
      return false; 
    if (paramContext.getPackageManager().queryIntentActivities(paramIntent, 0).size() > 0)
      bool = true; 
    return bool;
  }
  
  public static boolean isMIUI() {
    return Build.MANUFACTURER.equals("Xiaomi");
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/mylhyl/acp/MiuiOs.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */