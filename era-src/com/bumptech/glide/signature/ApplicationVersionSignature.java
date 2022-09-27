package com.bumptech.glide.signature;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import com.bumptech.glide.load.Key;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class ApplicationVersionSignature {
  private static final ConcurrentMap<String, Key> PACKAGE_NAME_TO_KEY = new ConcurrentHashMap<String, Key>();
  
  private static final String TAG = "AppVersionSignature";
  
  private static PackageInfo getPackageInfo(Context paramContext) {
    try {
      return paramContext.getPackageManager().getPackageInfo(paramContext.getPackageName(), 0);
    } catch (android.content.pm.PackageManager.NameNotFoundException nameNotFoundException) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Cannot resolve info for");
      stringBuilder.append(paramContext.getPackageName());
      Log.e("AppVersionSignature", stringBuilder.toString(), (Throwable)nameNotFoundException);
      return null;
    } 
  }
  
  private static String getVersionCode(PackageInfo paramPackageInfo) {
    String str;
    if (paramPackageInfo != null) {
      str = String.valueOf(paramPackageInfo.versionCode);
    } else {
      str = UUID.randomUUID().toString();
    } 
    return str;
  }
  
  public static Key obtain(Context paramContext) {
    String str = paramContext.getPackageName();
    Key key1 = PACKAGE_NAME_TO_KEY.get(str);
    Key key2 = key1;
    if (key1 == null) {
      key2 = obtainVersionSignature(paramContext);
      Key key = PACKAGE_NAME_TO_KEY.putIfAbsent(str, key2);
      if (key != null)
        key2 = key; 
    } 
    return key2;
  }
  
  private static Key obtainVersionSignature(Context paramContext) {
    return new ObjectKey(getVersionCode(getPackageInfo(paramContext)));
  }
  
  static void reset() {
    PACKAGE_NAME_TO_KEY.clear();
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/signature/ApplicationVersionSignature.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */