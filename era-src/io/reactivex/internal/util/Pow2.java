package io.reactivex.internal.util;

public final class Pow2 {
  private Pow2() {
    throw new IllegalStateException("No instances!");
  }
  
  public static boolean isPowerOfTwo(int paramInt) {
    boolean bool;
    if ((paramInt & paramInt - 1) == 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public static int roundToPowerOfTwo(int paramInt) {
    return 1 << 32 - Integer.numberOfLeadingZeros(paramInt - 1);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/util/Pow2.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */