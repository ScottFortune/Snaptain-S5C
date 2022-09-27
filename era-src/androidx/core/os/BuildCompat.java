package androidx.core.os;

import android.os.Build;

public class BuildCompat {
  @Deprecated
  public static boolean isAtLeastN() {
    boolean bool;
    if (Build.VERSION.SDK_INT >= 24) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  @Deprecated
  public static boolean isAtLeastNMR1() {
    boolean bool;
    if (Build.VERSION.SDK_INT >= 25) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  @Deprecated
  public static boolean isAtLeastO() {
    boolean bool;
    if (Build.VERSION.SDK_INT >= 26) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  @Deprecated
  public static boolean isAtLeastOMR1() {
    boolean bool;
    if (Build.VERSION.SDK_INT >= 27) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  @Deprecated
  public static boolean isAtLeastP() {
    boolean bool;
    if (Build.VERSION.SDK_INT >= 28) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public static boolean isAtLeastQ() {
    int i = Build.VERSION.CODENAME.length();
    boolean bool = true;
    if (i != 1 || Build.VERSION.CODENAME.charAt(0) < 'Q' || Build.VERSION.CODENAME.charAt(0) > 'Z')
      bool = false; 
    return bool;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/androidx/core/os/BuildCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */