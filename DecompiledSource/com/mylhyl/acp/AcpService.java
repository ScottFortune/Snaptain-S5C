package com.mylhyl.acp;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

class AcpService {
  private static final String TAG = "AcpService";
  
  int checkSelfPermission(Context paramContext, String paramString) {
    try {
      int i = (paramContext.getPackageManager().getPackageInfo(paramContext.getPackageName(), 0)).applicationInfo.targetSdkVersion;
      if (Build.VERSION.SDK_INT >= 23) {
        if (i >= 23) {
          Log.i("AcpService", "targetSdkVersion >= Build.VERSION_CODES.M");
          return ContextCompat.checkSelfPermission(paramContext, paramString);
        } 
        return PermissionChecker.checkSelfPermission(paramContext, paramString);
      } 
    } catch (android.content.pm.PackageManager.NameNotFoundException nameNotFoundException) {
      nameNotFoundException.printStackTrace();
    } 
    return ContextCompat.checkSelfPermission(paramContext, paramString);
  }
  
  void requestPermissions(Activity paramActivity, String[] paramArrayOfString, int paramInt) {
    ActivityCompat.requestPermissions(paramActivity, paramArrayOfString, paramInt);
  }
  
  boolean shouldShowRequestPermissionRationale(Activity paramActivity, String paramString) {
    boolean bool = ActivityCompat.shouldShowRequestPermissionRationale(paramActivity, paramString);
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("shouldShowRational = ");
    stringBuilder.append(bool);
    Log.i("AcpService", stringBuilder.toString());
    return bool;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/mylhyl/acp/AcpService.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */