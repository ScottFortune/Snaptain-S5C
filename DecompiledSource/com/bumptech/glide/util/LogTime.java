package com.bumptech.glide.util;

import android.os.Build;
import android.os.SystemClock;

public final class LogTime {
  private static final double MILLIS_MULTIPLIER;
  
  static {
    int i = Build.VERSION.SDK_INT;
    double d = 1.0D;
    if (i >= 17)
      d = 1.0D / Math.pow(10.0D, 6.0D); 
    MILLIS_MULTIPLIER = d;
  }
  
  public static double getElapsedMillis(long paramLong) {
    double d1 = (getLogTime() - paramLong);
    double d2 = MILLIS_MULTIPLIER;
    Double.isNaN(d1);
    return d1 * d2;
  }
  
  public static long getLogTime() {
    return (Build.VERSION.SDK_INT >= 17) ? SystemClock.elapsedRealtimeNanos() : SystemClock.uptimeMillis();
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/util/LogTime.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */