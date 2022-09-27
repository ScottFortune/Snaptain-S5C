package com.bumptech.glide.load.engine.bitmap_recycle;

import android.graphics.Bitmap;
import com.bumptech.glide.util.Util;
import java.util.NavigableMap;

final class SizeStrategy implements LruPoolStrategy {
  private static final int MAX_SIZE_MULTIPLE = 8;
  
  private final GroupedLinkedMap<Key, Bitmap> groupedMap = new GroupedLinkedMap<Key, Bitmap>();
  
  private final KeyPool keyPool = new KeyPool();
  
  private final NavigableMap<Integer, Integer> sortedSizes = new PrettyPrintTreeMap<Integer, Integer>();
  
  private void decrementBitmapOfSize(Integer paramInteger) {
    Integer integer = this.sortedSizes.get(paramInteger);
    if (integer.intValue() == 1) {
      this.sortedSizes.remove(paramInteger);
    } else {
      this.sortedSizes.put(paramInteger, Integer.valueOf(integer.intValue() - 1));
    } 
  }
  
  static String getBitmapString(int paramInt) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("[");
    stringBuilder.append(paramInt);
    stringBuilder.append("]");
    return stringBuilder.toString();
  }
  
  private static String getBitmapString(Bitmap paramBitmap) {
    return getBitmapString(Util.getBitmapByteSize(paramBitmap));
  }
  
  public Bitmap get(int paramInt1, int paramInt2, Bitmap.Config paramConfig) {
    int i = Util.getBitmapByteSize(paramInt1, paramInt2, paramConfig);
    Key key1 = this.keyPool.get(i);
    Integer integer = this.sortedSizes.ceilingKey(Integer.valueOf(i));
    Key key2 = key1;
    if (integer != null) {
      key2 = key1;
      if (integer.intValue() != i) {
        key2 = key1;
        if (integer.intValue() <= i * 8) {
          this.keyPool.offer(key1);
          key2 = this.keyPool.get(integer.intValue());
        } 
      } 
    } 
    Bitmap bitmap = this.groupedMap.get(key2);
    if (bitmap != null) {
      bitmap.reconfigure(paramInt1, paramInt2, paramConfig);
      decrementBitmapOfSize(integer);
    } 
    return bitmap;
  }
  
  public int getSize(Bitmap paramBitmap) {
    return Util.getBitmapByteSize(paramBitmap);
  }
  
  public String logBitmap(int paramInt1, int paramInt2, Bitmap.Config paramConfig) {
    return getBitmapString(Util.getBitmapByteSize(paramInt1, paramInt2, paramConfig));
  }
  
  public String logBitmap(Bitmap paramBitmap) {
    return getBitmapString(paramBitmap);
  }
  
  public void put(Bitmap paramBitmap) {
    int i = Util.getBitmapByteSize(paramBitmap);
    Key key = this.keyPool.get(i);
    this.groupedMap.put(key, paramBitmap);
    Integer integer = this.sortedSizes.get(Integer.valueOf(key.size));
    NavigableMap<Integer, Integer> navigableMap = this.sortedSizes;
    int j = key.size;
    i = 1;
    if (integer != null)
      i = 1 + integer.intValue(); 
    navigableMap.put(Integer.valueOf(j), Integer.valueOf(i));
  }
  
  public Bitmap removeLast() {
    Bitmap bitmap = this.groupedMap.removeLast();
    if (bitmap != null)
      decrementBitmapOfSize(Integer.valueOf(Util.getBitmapByteSize(bitmap))); 
    return bitmap;
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("SizeStrategy:\n  ");
    stringBuilder.append(this.groupedMap);
    stringBuilder.append("\n  SortedSizes");
    stringBuilder.append(this.sortedSizes);
    return stringBuilder.toString();
  }
  
  static final class Key implements Poolable {
    private final SizeStrategy.KeyPool pool;
    
    int size;
    
    Key(SizeStrategy.KeyPool param1KeyPool) {
      this.pool = param1KeyPool;
    }
    
    public boolean equals(Object param1Object) {
      boolean bool = param1Object instanceof Key;
      boolean bool1 = false;
      boolean bool2 = bool1;
      if (bool) {
        param1Object = param1Object;
        bool2 = bool1;
        if (this.size == ((Key)param1Object).size)
          bool2 = true; 
      } 
      return bool2;
    }
    
    public int hashCode() {
      return this.size;
    }
    
    public void init(int param1Int) {
      this.size = param1Int;
    }
    
    public void offer() {
      this.pool.offer(this);
    }
    
    public String toString() {
      return SizeStrategy.getBitmapString(this.size);
    }
  }
  
  static class KeyPool extends BaseKeyPool<Key> {
    protected SizeStrategy.Key create() {
      return new SizeStrategy.Key(this);
    }
    
    public SizeStrategy.Key get(int param1Int) {
      SizeStrategy.Key key = (SizeStrategy.Key)get();
      key.init(param1Int);
      return key;
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/engine/bitmap_recycle/SizeStrategy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */