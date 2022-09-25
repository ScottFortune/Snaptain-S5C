package com.bumptech.glide.util;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Looper;
import com.bumptech.glide.load.model.Model;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Queue;

public final class Util {
  private static final int HASH_ACCUMULATOR = 17;
  
  private static final int HASH_MULTIPLIER = 31;
  
  private static final char[] HEX_CHAR_ARRAY = "0123456789abcdef".toCharArray();
  
  private static final char[] SHA_256_CHARS = new char[64];
  
  public static void assertBackgroundThread() {
    if (isOnBackgroundThread())
      return; 
    throw new IllegalArgumentException("You must call this method on a background thread");
  }
  
  public static void assertMainThread() {
    if (isOnMainThread())
      return; 
    throw new IllegalArgumentException("You must call this method on the main thread");
  }
  
  public static boolean bothModelsNullEquivalentOrEquals(Object paramObject1, Object paramObject2) {
    if (paramObject1 == null) {
      boolean bool;
      if (paramObject2 == null) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    } 
    return (paramObject1 instanceof Model) ? ((Model)paramObject1).isEquivalentTo(paramObject2) : paramObject1.equals(paramObject2);
  }
  
  public static boolean bothNullOrEqual(Object paramObject1, Object paramObject2) {
    boolean bool;
    if (paramObject1 == null) {
      if (paramObject2 == null) {
        bool = true;
      } else {
        bool = false;
      } 
    } else {
      bool = paramObject1.equals(paramObject2);
    } 
    return bool;
  }
  
  private static String bytesToHex(byte[] paramArrayOfbyte, char[] paramArrayOfchar) {
    for (byte b = 0; b < paramArrayOfbyte.length; b++) {
      int i = paramArrayOfbyte[b] & 0xFF;
      int j = b * 2;
      char[] arrayOfChar = HEX_CHAR_ARRAY;
      paramArrayOfchar[j] = (char)arrayOfChar[i >>> 4];
      paramArrayOfchar[j + 1] = (char)arrayOfChar[i & 0xF];
    } 
    return new String(paramArrayOfchar);
  }
  
  public static <T> Queue<T> createQueue(int paramInt) {
    return new ArrayDeque<T>(paramInt);
  }
  
  public static int getBitmapByteSize(int paramInt1, int paramInt2, Bitmap.Config paramConfig) {
    return paramInt1 * paramInt2 * getBytesPerPixel(paramConfig);
  }
  
  public static int getBitmapByteSize(Bitmap paramBitmap) {
    if (!paramBitmap.isRecycled()) {
      if (Build.VERSION.SDK_INT >= 19)
        try {
          return paramBitmap.getAllocationByteCount();
        } catch (NullPointerException nullPointerException) {} 
      return paramBitmap.getHeight() * paramBitmap.getRowBytes();
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Cannot obtain size for recycled Bitmap: ");
    stringBuilder.append(paramBitmap);
    stringBuilder.append("[");
    stringBuilder.append(paramBitmap.getWidth());
    stringBuilder.append("x");
    stringBuilder.append(paramBitmap.getHeight());
    stringBuilder.append("] ");
    stringBuilder.append(paramBitmap.getConfig());
    throw new IllegalStateException(stringBuilder.toString());
  }
  
  private static int getBytesPerPixel(Bitmap.Config paramConfig) {
    Bitmap.Config config = paramConfig;
    if (paramConfig == null)
      config = Bitmap.Config.ARGB_8888; 
    int i = null.$SwitchMap$android$graphics$Bitmap$Config[config.ordinal()];
    byte b = 4;
    if (i != 1) {
      if (i != 2 && i != 3) {
        if (i == 4)
          b = 8; 
      } else {
        b = 2;
      } 
    } else {
      b = 1;
    } 
    return b;
  }
  
  @Deprecated
  public static int getSize(Bitmap paramBitmap) {
    return getBitmapByteSize(paramBitmap);
  }
  
  public static <T> List<T> getSnapshot(Collection<T> paramCollection) {
    ArrayList<Collection<T>> arrayList = new ArrayList(paramCollection.size());
    for (Collection<T> paramCollection : paramCollection) {
      if (paramCollection != null)
        arrayList.add(paramCollection); 
    } 
    return (List)arrayList;
  }
  
  public static int hashCode(float paramFloat) {
    return hashCode(paramFloat, 17);
  }
  
  public static int hashCode(float paramFloat, int paramInt) {
    return hashCode(Float.floatToIntBits(paramFloat), paramInt);
  }
  
  public static int hashCode(int paramInt) {
    return hashCode(paramInt, 17);
  }
  
  public static int hashCode(int paramInt1, int paramInt2) {
    return paramInt2 * 31 + paramInt1;
  }
  
  public static int hashCode(Object paramObject, int paramInt) {
    int i;
    if (paramObject == null) {
      i = 0;
    } else {
      i = paramObject.hashCode();
    } 
    return hashCode(i, paramInt);
  }
  
  public static int hashCode(boolean paramBoolean) {
    return hashCode(paramBoolean, 17);
  }
  
  public static int hashCode(boolean paramBoolean, int paramInt) {
    return hashCode(paramBoolean, paramInt);
  }
  
  public static boolean isOnBackgroundThread() {
    return isOnMainThread() ^ true;
  }
  
  public static boolean isOnMainThread() {
    boolean bool;
    if (Looper.myLooper() == Looper.getMainLooper()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private static boolean isValidDimension(int paramInt) {
    return (paramInt > 0 || paramInt == Integer.MIN_VALUE);
  }
  
  public static boolean isValidDimensions(int paramInt1, int paramInt2) {
    boolean bool;
    if (isValidDimension(paramInt1) && isValidDimension(paramInt2)) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public static String sha256BytesToHex(byte[] paramArrayOfbyte) {
    synchronized (SHA_256_CHARS) {
      return bytesToHex(paramArrayOfbyte, SHA_256_CHARS);
    } 
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/util/Util.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */