package io.reactivex.internal.util;

import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicLong;

public final class BackpressureHelper {
  private BackpressureHelper() {
    throw new IllegalStateException("No instances!");
  }
  
  public static long add(AtomicLong paramAtomicLong, long paramLong) {
    while (true) {
      long l = paramAtomicLong.get();
      if (l == Long.MAX_VALUE)
        return Long.MAX_VALUE; 
      if (paramAtomicLong.compareAndSet(l, addCap(l, paramLong)))
        return l; 
    } 
  }
  
  public static long addCancel(AtomicLong paramAtomicLong, long paramLong) {
    while (true) {
      long l = paramAtomicLong.get();
      if (l == Long.MIN_VALUE)
        return Long.MIN_VALUE; 
      if (l == Long.MAX_VALUE)
        return Long.MAX_VALUE; 
      if (paramAtomicLong.compareAndSet(l, addCap(l, paramLong)))
        return l; 
    } 
  }
  
  public static long addCap(long paramLong1, long paramLong2) {
    paramLong2 = paramLong1 + paramLong2;
    paramLong1 = paramLong2;
    if (paramLong2 < 0L)
      paramLong1 = Long.MAX_VALUE; 
    return paramLong1;
  }
  
  public static long multiplyCap(long paramLong1, long paramLong2) {
    long l = paramLong1 * paramLong2;
    return ((paramLong1 | paramLong2) >>> 31L != 0L && l / paramLong1 != paramLong2) ? Long.MAX_VALUE : l;
  }
  
  public static long produced(AtomicLong paramAtomicLong, long paramLong) {
    while (true) {
      long l1 = paramAtomicLong.get();
      if (l1 == Long.MAX_VALUE)
        return Long.MAX_VALUE; 
      long l2 = l1 - paramLong;
      long l3 = l2;
      if (l2 < 0L) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("More produced than requested: ");
        stringBuilder.append(l2);
        RxJavaPlugins.onError(new IllegalStateException(stringBuilder.toString()));
        l3 = 0L;
      } 
      if (paramAtomicLong.compareAndSet(l1, l3))
        return l3; 
    } 
  }
  
  public static long producedCancel(AtomicLong paramAtomicLong, long paramLong) {
    while (true) {
      long l1 = paramAtomicLong.get();
      if (l1 == Long.MIN_VALUE)
        return Long.MIN_VALUE; 
      if (l1 == Long.MAX_VALUE)
        return Long.MAX_VALUE; 
      long l2 = l1 - paramLong;
      long l3 = l2;
      if (l2 < 0L) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("More produced than requested: ");
        stringBuilder.append(l2);
        RxJavaPlugins.onError(new IllegalStateException(stringBuilder.toString()));
        l3 = 0L;
      } 
      if (paramAtomicLong.compareAndSet(l1, l3))
        return l3; 
    } 
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/util/BackpressureHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */