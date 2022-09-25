package androidx.core.os;

import android.os.Build;
import android.os.Trace;

public final class TraceCompat {
  public static void beginSection(String paramString) {
    if (Build.VERSION.SDK_INT >= 18)
      Trace.beginSection(paramString); 
  }
  
  public static void endSection() {
    if (Build.VERSION.SDK_INT >= 18)
      Trace.endSection(); 
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/androidx/core/os/TraceCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */