package androidx.core.content.res;

import android.content.res.Resources;
import android.os.Build;

public final class ConfigurationHelper {
  public static int getDensityDpi(Resources paramResources) {
    return (Build.VERSION.SDK_INT >= 17) ? (paramResources.getConfiguration()).densityDpi : (paramResources.getDisplayMetrics()).densityDpi;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/androidx/core/content/res/ConfigurationHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */