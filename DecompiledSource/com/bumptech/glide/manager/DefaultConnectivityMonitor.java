package com.bumptech.glide.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import com.bumptech.glide.util.Preconditions;

final class DefaultConnectivityMonitor implements ConnectivityMonitor {
  private static final String TAG = "ConnectivityMonitor";
  
  private final BroadcastReceiver connectivityReceiver = new BroadcastReceiver() {
      public void onReceive(Context param1Context, Intent param1Intent) {
        boolean bool = DefaultConnectivityMonitor.this.isConnected;
        DefaultConnectivityMonitor defaultConnectivityMonitor = DefaultConnectivityMonitor.this;
        defaultConnectivityMonitor.isConnected = defaultConnectivityMonitor.isConnected(param1Context);
        if (bool != DefaultConnectivityMonitor.this.isConnected) {
          if (Log.isLoggable("ConnectivityMonitor", 3)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("connectivity changed, isConnected: ");
            stringBuilder.append(DefaultConnectivityMonitor.this.isConnected);
            Log.d("ConnectivityMonitor", stringBuilder.toString());
          } 
          DefaultConnectivityMonitor.this.listener.onConnectivityChanged(DefaultConnectivityMonitor.this.isConnected);
        } 
      }
    };
  
  private final Context context;
  
  boolean isConnected;
  
  private boolean isRegistered;
  
  final ConnectivityMonitor.ConnectivityListener listener;
  
  DefaultConnectivityMonitor(Context paramContext, ConnectivityMonitor.ConnectivityListener paramConnectivityListener) {
    this.context = paramContext.getApplicationContext();
    this.listener = paramConnectivityListener;
  }
  
  private void register() {
    if (this.isRegistered)
      return; 
    this.isConnected = isConnected(this.context);
    try {
      Context context = this.context;
      BroadcastReceiver broadcastReceiver = this.connectivityReceiver;
      IntentFilter intentFilter = new IntentFilter();
      this("android.net.conn.CONNECTIVITY_CHANGE");
      context.registerReceiver(broadcastReceiver, intentFilter);
      this.isRegistered = true;
    } catch (SecurityException securityException) {
      if (Log.isLoggable("ConnectivityMonitor", 5))
        Log.w("ConnectivityMonitor", "Failed to register", securityException); 
    } 
  }
  
  private void unregister() {
    if (!this.isRegistered)
      return; 
    this.context.unregisterReceiver(this.connectivityReceiver);
    this.isRegistered = false;
  }
  
  boolean isConnected(Context paramContext) {
    ConnectivityManager connectivityManager = (ConnectivityManager)Preconditions.checkNotNull(paramContext.getSystemService("connectivity"));
    boolean bool = true;
    try {
      NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
      if (networkInfo == null || !networkInfo.isConnected())
        bool = false; 
      return bool;
    } catch (RuntimeException runtimeException) {
      if (Log.isLoggable("ConnectivityMonitor", 5))
        Log.w("ConnectivityMonitor", "Failed to determine connectivity status when connectivity changed", runtimeException); 
      return true;
    } 
  }
  
  public void onDestroy() {}
  
  public void onStart() {
    register();
  }
  
  public void onStop() {
    unregister();
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/manager/DefaultConnectivityMonitor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */