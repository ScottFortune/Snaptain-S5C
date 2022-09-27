package com.bumptech.glide.manager;

import android.content.Context;
import android.util.Log;
import androidx.core.content.ContextCompat;

public class DefaultConnectivityMonitorFactory implements ConnectivityMonitorFactory {
  private static final String NETWORK_PERMISSION = "android.permission.ACCESS_NETWORK_STATE";
  
  private static final String TAG = "ConnectivityMonitor";
  
  public ConnectivityMonitor build(Context paramContext, ConnectivityMonitor.ConnectivityListener paramConnectivityListener) {
    NullConnectivityMonitor nullConnectivityMonitor;
    boolean bool;
    if (ContextCompat.checkSelfPermission(paramContext, "android.permission.ACCESS_NETWORK_STATE") == 0) {
      bool = true;
    } else {
      bool = false;
    } 
    if (Log.isLoggable("ConnectivityMonitor", 3)) {
      String str;
      if (bool) {
        str = "ACCESS_NETWORK_STATE permission granted, registering connectivity monitor";
      } else {
        str = "ACCESS_NETWORK_STATE permission missing, cannot register connectivity monitor";
      } 
      Log.d("ConnectivityMonitor", str);
    } 
    if (bool) {
      DefaultConnectivityMonitor defaultConnectivityMonitor = new DefaultConnectivityMonitor(paramContext, paramConnectivityListener);
    } else {
      nullConnectivityMonitor = new NullConnectivityMonitor();
    } 
    return nullConnectivityMonitor;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/manager/DefaultConnectivityMonitorFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */