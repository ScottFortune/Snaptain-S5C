package androidx.core.app;

import android.app.ActivityManager;
import android.os.Build;

public final class ActivityManagerCompat {
  public static boolean isLowRamDevice(ActivityManager paramActivityManager) {
    return (Build.VERSION.SDK_INT >= 19) ? paramActivityManager.isLowRamDevice() : false;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/androidx/core/app/ActivityManagerCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */