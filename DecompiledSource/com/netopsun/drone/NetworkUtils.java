package com.netopsun.drone;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import androidx.appcompat.app.AlertDialog;

public class NetworkUtils {
  public static void checkIfNeedShowDialog(Context paramContext) {
    if (isMobile(paramContext.getApplicationContext()))
      showNetworkSettingDialog(paramContext); 
  }
  
  private static boolean isMobile(Context paramContext) {
    ConnectivityManager connectivityManager = (ConnectivityManager)paramContext.getSystemService("connectivity");
    boolean bool = false;
    if (connectivityManager == null)
      return false; 
    if (connectivityManager.getActiveNetworkInfo().getType() == 0)
      bool = true; 
    return bool;
  }
  
  private static void openSetting(Context paramContext) {
    paramContext.startActivity(new Intent("android.settings.DATA_ROAMING_SETTINGS"));
  }
  
  private static void showNetworkSettingDialog(final Context context) {
    (new AlertDialog.Builder(context)).setTitle(context.getString(2131624071)).setMessage(context.getString(2131624155)).setPositiveButton(context.getString(2131624076), new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface param1DialogInterface, int param1Int) {
            NetworkUtils.openSetting(context);
          }
        }).setNegativeButton(context.getString(2131624020), new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface param1DialogInterface, int param1Int) {}
        }).show();
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/drone/NetworkUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */