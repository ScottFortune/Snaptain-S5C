package androidx.core.location;

import android.location.LocationManager;
import android.os.Build;

public final class LocationManagerCompat {
  public static boolean isLocationEnabled(LocationManager paramLocationManager) {
    return (Build.VERSION.SDK_INT >= 28) ? paramLocationManager.isLocationEnabled() : ((paramLocationManager.isProviderEnabled("network") || paramLocationManager.isProviderEnabled("gps")));
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/androidx/core/location/LocationManagerCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */