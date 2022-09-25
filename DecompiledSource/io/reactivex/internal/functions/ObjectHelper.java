package io.reactivex.internal.functions;

import io.reactivex.functions.BiPredicate;

public final class ObjectHelper {
  static final BiPredicate<Object, Object> EQUALS = new BiObjectPredicate();
  
  private ObjectHelper() {
    throw new IllegalStateException("No instances!");
  }
  
  public static int compare(int paramInt1, int paramInt2) {
    if (paramInt1 < paramInt2) {
      paramInt1 = -1;
    } else if (paramInt1 > paramInt2) {
      paramInt1 = 1;
    } else {
      paramInt1 = 0;
    } 
    return paramInt1;
  }
  
  public static int compare(long paramLong1, long paramLong2) {
    boolean bool;
    if (paramLong1 < paramLong2) {
      bool = true;
    } else if (paramLong1 > paramLong2) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public static boolean equals(Object paramObject1, Object paramObject2) {
    return (paramObject1 == paramObject2 || (paramObject1 != null && paramObject1.equals(paramObject2)));
  }
  
  public static <T> BiPredicate<T, T> equalsPredicate() {
    return (BiPredicate)EQUALS;
  }
  
  public static int hashCode(Object paramObject) {
    boolean bool;
    if (paramObject != null) {
      bool = paramObject.hashCode();
    } else {
      bool = false;
    } 
    return bool;
  }
  
  @Deprecated
  public static long requireNonNull(long paramLong, String paramString) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Null check on a primitive: ");
    stringBuilder.append(paramString);
    throw new InternalError(stringBuilder.toString());
  }
  
  public static <T> T requireNonNull(T paramT, String paramString) {
    if (paramT != null)
      return paramT; 
    throw new NullPointerException(paramString);
  }
  
  public static int verifyPositive(int paramInt, String paramString) {
    if (paramInt > 0)
      return paramInt; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(paramString);
    stringBuilder.append(" > 0 required but it was ");
    stringBuilder.append(paramInt);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public static long verifyPositive(long paramLong, String paramString) {
    if (paramLong > 0L)
      return paramLong; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(paramString);
    stringBuilder.append(" > 0 required but it was ");
    stringBuilder.append(paramLong);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  static final class BiObjectPredicate implements BiPredicate<Object, Object> {
    public boolean test(Object param1Object1, Object param1Object2) {
      return ObjectHelper.equals(param1Object1, param1Object2);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/functions/ObjectHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */