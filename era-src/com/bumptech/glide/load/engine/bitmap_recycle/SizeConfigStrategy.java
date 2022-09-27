package com.bumptech.glide.load.engine.bitmap_recycle;

import android.graphics.Bitmap;
import android.os.Build;
import com.bumptech.glide.util.Util;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class SizeConfigStrategy implements LruPoolStrategy {
  private static final Bitmap.Config[] ALPHA_8_IN_CONFIGS;
  
  private static final Bitmap.Config[] ARGB_4444_IN_CONFIGS;
  
  private static final Bitmap.Config[] ARGB_8888_IN_CONFIGS;
  
  private static final int MAX_SIZE_MULTIPLE = 8;
  
  private static final Bitmap.Config[] RGBA_F16_IN_CONFIGS = ARGB_8888_IN_CONFIGS;
  
  private static final Bitmap.Config[] RGB_565_IN_CONFIGS = new Bitmap.Config[] { Bitmap.Config.RGB_565 };
  
  private final GroupedLinkedMap<Key, Bitmap> groupedMap = new GroupedLinkedMap<Key, Bitmap>();
  
  private final KeyPool keyPool = new KeyPool();
  
  private final Map<Bitmap.Config, NavigableMap<Integer, Integer>> sortedSizes = new HashMap<Bitmap.Config, NavigableMap<Integer, Integer>>();
  
  static {
    ARGB_4444_IN_CONFIGS = new Bitmap.Config[] { Bitmap.Config.ARGB_4444 };
    ALPHA_8_IN_CONFIGS = new Bitmap.Config[] { Bitmap.Config.ALPHA_8 };
  }
  
  private void decrementBitmapOfSize(Integer paramInteger, Bitmap paramBitmap) {
    NavigableMap<Integer, Integer> navigableMap = getSizesForConfig(paramBitmap.getConfig());
    Integer integer = navigableMap.get(paramInteger);
    if (integer != null) {
      if (integer.intValue() == 1) {
        navigableMap.remove(paramInteger);
      } else {
        navigableMap.put(paramInteger, Integer.valueOf(integer.intValue() - 1));
      } 
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Tried to decrement empty size, size: ");
    stringBuilder.append(paramInteger);
    stringBuilder.append(", removed: ");
    stringBuilder.append(logBitmap(paramBitmap));
    stringBuilder.append(", this: ");
    stringBuilder.append(this);
    throw new NullPointerException(stringBuilder.toString());
  }
  
  private Key findBestKey(int paramInt, Bitmap.Config paramConfig) {
    // Byte code:
    //   0: aload_0
    //   1: getfield keyPool : Lcom/bumptech/glide/load/engine/bitmap_recycle/SizeConfigStrategy$KeyPool;
    //   4: iload_1
    //   5: aload_2
    //   6: invokevirtual get : (ILandroid/graphics/Bitmap$Config;)Lcom/bumptech/glide/load/engine/bitmap_recycle/SizeConfigStrategy$Key;
    //   9: astore_3
    //   10: aload_2
    //   11: invokestatic getInConfigs : (Landroid/graphics/Bitmap$Config;)[Landroid/graphics/Bitmap$Config;
    //   14: astore #4
    //   16: aload #4
    //   18: arraylength
    //   19: istore #5
    //   21: iconst_0
    //   22: istore #6
    //   24: aload_3
    //   25: astore #7
    //   27: iload #6
    //   29: iload #5
    //   31: if_icmpge -> 147
    //   34: aload #4
    //   36: iload #6
    //   38: aaload
    //   39: astore #8
    //   41: aload_0
    //   42: aload #8
    //   44: invokespecial getSizesForConfig : (Landroid/graphics/Bitmap$Config;)Ljava/util/NavigableMap;
    //   47: iload_1
    //   48: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   51: invokeinterface ceilingKey : (Ljava/lang/Object;)Ljava/lang/Object;
    //   56: checkcast java/lang/Integer
    //   59: astore #9
    //   61: aload #9
    //   63: ifnull -> 141
    //   66: aload #9
    //   68: invokevirtual intValue : ()I
    //   71: iload_1
    //   72: bipush #8
    //   74: imul
    //   75: if_icmpgt -> 141
    //   78: aload #9
    //   80: invokevirtual intValue : ()I
    //   83: iload_1
    //   84: if_icmpne -> 114
    //   87: aload #8
    //   89: ifnonnull -> 102
    //   92: aload_3
    //   93: astore #7
    //   95: aload_2
    //   96: ifnull -> 147
    //   99: goto -> 114
    //   102: aload_3
    //   103: astore #7
    //   105: aload #8
    //   107: aload_2
    //   108: invokevirtual equals : (Ljava/lang/Object;)Z
    //   111: ifne -> 147
    //   114: aload_0
    //   115: getfield keyPool : Lcom/bumptech/glide/load/engine/bitmap_recycle/SizeConfigStrategy$KeyPool;
    //   118: aload_3
    //   119: invokevirtual offer : (Lcom/bumptech/glide/load/engine/bitmap_recycle/Poolable;)V
    //   122: aload_0
    //   123: getfield keyPool : Lcom/bumptech/glide/load/engine/bitmap_recycle/SizeConfigStrategy$KeyPool;
    //   126: aload #9
    //   128: invokevirtual intValue : ()I
    //   131: aload #8
    //   133: invokevirtual get : (ILandroid/graphics/Bitmap$Config;)Lcom/bumptech/glide/load/engine/bitmap_recycle/SizeConfigStrategy$Key;
    //   136: astore #7
    //   138: goto -> 147
    //   141: iinc #6, 1
    //   144: goto -> 24
    //   147: aload #7
    //   149: areturn
  }
  
  static String getBitmapString(int paramInt, Bitmap.Config paramConfig) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("[");
    stringBuilder.append(paramInt);
    stringBuilder.append("](");
    stringBuilder.append(paramConfig);
    stringBuilder.append(")");
    return stringBuilder.toString();
  }
  
  private static Bitmap.Config[] getInConfigs(Bitmap.Config paramConfig) {
    if (Build.VERSION.SDK_INT >= 26 && Bitmap.Config.RGBA_F16.equals(paramConfig))
      return RGBA_F16_IN_CONFIGS; 
    int i = null.$SwitchMap$android$graphics$Bitmap$Config[paramConfig.ordinal()];
    return (i != 1) ? ((i != 2) ? ((i != 3) ? ((i != 4) ? new Bitmap.Config[] { paramConfig } : ALPHA_8_IN_CONFIGS) : ARGB_4444_IN_CONFIGS) : RGB_565_IN_CONFIGS) : ARGB_8888_IN_CONFIGS;
  }
  
  private NavigableMap<Integer, Integer> getSizesForConfig(Bitmap.Config paramConfig) {
    NavigableMap<Object, Object> navigableMap1 = (NavigableMap)this.sortedSizes.get(paramConfig);
    NavigableMap<Object, Object> navigableMap2 = navigableMap1;
    if (navigableMap1 == null) {
      navigableMap2 = new TreeMap<Object, Object>();
      this.sortedSizes.put(paramConfig, navigableMap2);
    } 
    return (NavigableMap)navigableMap2;
  }
  
  public Bitmap get(int paramInt1, int paramInt2, Bitmap.Config paramConfig) {
    Key key = findBestKey(Util.getBitmapByteSize(paramInt1, paramInt2, paramConfig), paramConfig);
    Bitmap bitmap = this.groupedMap.get(key);
    if (bitmap != null) {
      decrementBitmapOfSize(Integer.valueOf(key.size), bitmap);
      bitmap.reconfigure(paramInt1, paramInt2, paramConfig);
    } 
    return bitmap;
  }
  
  public int getSize(Bitmap paramBitmap) {
    return Util.getBitmapByteSize(paramBitmap);
  }
  
  public String logBitmap(int paramInt1, int paramInt2, Bitmap.Config paramConfig) {
    return getBitmapString(Util.getBitmapByteSize(paramInt1, paramInt2, paramConfig), paramConfig);
  }
  
  public String logBitmap(Bitmap paramBitmap) {
    return getBitmapString(Util.getBitmapByteSize(paramBitmap), paramBitmap.getConfig());
  }
  
  public void put(Bitmap paramBitmap) {
    int i = Util.getBitmapByteSize(paramBitmap);
    Key key = this.keyPool.get(i, paramBitmap.getConfig());
    this.groupedMap.put(key, paramBitmap);
    NavigableMap<Integer, Integer> navigableMap = getSizesForConfig(paramBitmap.getConfig());
    Integer integer = navigableMap.get(Integer.valueOf(key.size));
    int j = key.size;
    i = 1;
    if (integer != null)
      i = 1 + integer.intValue(); 
    navigableMap.put(Integer.valueOf(j), Integer.valueOf(i));
  }
  
  public Bitmap removeLast() {
    Bitmap bitmap = this.groupedMap.removeLast();
    if (bitmap != null)
      decrementBitmapOfSize(Integer.valueOf(Util.getBitmapByteSize(bitmap)), bitmap); 
    return bitmap;
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("SizeConfigStrategy{groupedMap=");
    stringBuilder.append(this.groupedMap);
    stringBuilder.append(", sortedSizes=(");
    for (Map.Entry<Bitmap.Config, NavigableMap<Integer, Integer>> entry : this.sortedSizes.entrySet()) {
      stringBuilder.append(entry.getKey());
      stringBuilder.append('[');
      stringBuilder.append(entry.getValue());
      stringBuilder.append("], ");
    } 
    if (!this.sortedSizes.isEmpty())
      stringBuilder.replace(stringBuilder.length() - 2, stringBuilder.length(), ""); 
    stringBuilder.append(")}");
    return stringBuilder.toString();
  }
  
  static {
    Bitmap.Config[] arrayOfConfig1 = new Bitmap.Config[2];
    arrayOfConfig1[0] = Bitmap.Config.ARGB_8888;
    arrayOfConfig1[1] = null;
    Bitmap.Config[] arrayOfConfig2 = arrayOfConfig1;
    if (Build.VERSION.SDK_INT >= 26) {
      arrayOfConfig2 = Arrays.<Bitmap.Config>copyOf(arrayOfConfig1, arrayOfConfig1.length + 1);
      arrayOfConfig2[arrayOfConfig2.length - 1] = Bitmap.Config.RGBA_F16;
    } 
    ARGB_8888_IN_CONFIGS = arrayOfConfig2;
  }
  
  static final class Key implements Poolable {
    private Bitmap.Config config;
    
    private final SizeConfigStrategy.KeyPool pool;
    
    int size;
    
    public Key(SizeConfigStrategy.KeyPool param1KeyPool) {
      this.pool = param1KeyPool;
    }
    
    Key(SizeConfigStrategy.KeyPool param1KeyPool, int param1Int, Bitmap.Config param1Config) {
      this(param1KeyPool);
      init(param1Int, param1Config);
    }
    
    public boolean equals(Object param1Object) {
      boolean bool = param1Object instanceof Key;
      boolean bool1 = false;
      boolean bool2 = bool1;
      if (bool) {
        param1Object = param1Object;
        bool2 = bool1;
        if (this.size == ((Key)param1Object).size) {
          bool2 = bool1;
          if (Util.bothNullOrEqual(this.config, ((Key)param1Object).config))
            bool2 = true; 
        } 
      } 
      return bool2;
    }
    
    public int hashCode() {
      byte b;
      int i = this.size;
      Bitmap.Config config = this.config;
      if (config != null) {
        b = config.hashCode();
      } else {
        b = 0;
      } 
      return i * 31 + b;
    }
    
    public void init(int param1Int, Bitmap.Config param1Config) {
      this.size = param1Int;
      this.config = param1Config;
    }
    
    public void offer() {
      this.pool.offer(this);
    }
    
    public String toString() {
      return SizeConfigStrategy.getBitmapString(this.size, this.config);
    }
  }
  
  static class KeyPool extends BaseKeyPool<Key> {
    protected SizeConfigStrategy.Key create() {
      return new SizeConfigStrategy.Key(this);
    }
    
    public SizeConfigStrategy.Key get(int param1Int, Bitmap.Config param1Config) {
      SizeConfigStrategy.Key key = get();
      key.init(param1Int, param1Config);
      return key;
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/engine/bitmap_recycle/SizeConfigStrategy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */