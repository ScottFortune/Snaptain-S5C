package com.shizhefei.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtils {
  public static boolean hasNetwork(Context paramContext) {
    try {
      NetworkInfo networkInfo = ((ConnectivityManager)paramContext.getSystemService("connectivity")).getActiveNetworkInfo();
    } finally {
      paramContext = null;
    } 
    return false;
  }
  
  public static boolean isWifi(Context paramContext) {
    NetworkInfo networkInfo = ((ConnectivityManager)paramContext.getSystemService("connectivity")).getActiveNetworkInfo();
    boolean bool = false;
    if (networkInfo == null)
      return false; 
    if (networkInfo.getType() == 1)
      bool = true; 
    return bool;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/shizhefei/utils/NetworkUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */