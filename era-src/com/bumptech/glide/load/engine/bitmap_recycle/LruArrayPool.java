package com.bumptech.glide.load.engine.bitmap_recycle;

import android.util.Log;
import com.bumptech.glide.util.Preconditions;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public final class LruArrayPool implements ArrayPool {
  private static final int DEFAULT_SIZE = 4194304;
  
  static final int MAX_OVER_SIZE_MULTIPLE = 8;
  
  private static final int SINGLE_ARRAY_MAX_SIZE_DIVISOR = 2;
  
  private final Map<Class<?>, ArrayAdapterInterface<?>> adapters = new HashMap<Class<?>, ArrayAdapterInterface<?>>();
  
  private int currentSize;
  
  private final GroupedLinkedMap<Key, Object> groupedMap = new GroupedLinkedMap<Key, Object>();
  
  private final KeyPool keyPool = new KeyPool();
  
  private final int maxSize = 4194304;
  
  private final Map<Class<?>, NavigableMap<Integer, Integer>> sortedSizes = new HashMap<Class<?>, NavigableMap<Integer, Integer>>();
  
  public LruArrayPool() {}
  
  public LruArrayPool(int paramInt) {}
  
  private void decrementArrayOfSize(int paramInt, Class<?> paramClass) {
    NavigableMap<Integer, Integer> navigableMap = getSizesForAdapter(paramClass);
    Integer integer = navigableMap.get(Integer.valueOf(paramInt));
    if (integer != null) {
      if (integer.intValue() == 1) {
        navigableMap.remove(Integer.valueOf(paramInt));
      } else {
        navigableMap.put(Integer.valueOf(paramInt), Integer.valueOf(integer.intValue() - 1));
      } 
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Tried to decrement empty size, size: ");
    stringBuilder.append(paramInt);
    stringBuilder.append(", this: ");
    stringBuilder.append(this);
    throw new NullPointerException(stringBuilder.toString());
  }
  
  private void evict() {
    evictToSize(this.maxSize);
  }
  
  private void evictToSize(int paramInt) {
    while (this.currentSize > paramInt) {
      Object object = this.groupedMap.removeLast();
      Preconditions.checkNotNull(object);
      ArrayAdapterInterface<Object> arrayAdapterInterface = getAdapterFromObject(object);
      this.currentSize -= arrayAdapterInterface.getArrayLength(object) * arrayAdapterInterface.getElementSizeInBytes();
      decrementArrayOfSize(arrayAdapterInterface.getArrayLength(object), object.getClass());
      if (Log.isLoggable(arrayAdapterInterface.getTag(), 2)) {
        String str = arrayAdapterInterface.getTag();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("evicted: ");
        stringBuilder.append(arrayAdapterInterface.getArrayLength(object));
        Log.v(str, stringBuilder.toString());
      } 
    } 
  }
  
  private <T> ArrayAdapterInterface<T> getAdapterFromObject(T paramT) {
    return getAdapterFromType((Class)paramT.getClass());
  }
  
  private <T> ArrayAdapterInterface<T> getAdapterFromType(Class<T> paramClass) {
    StringBuilder stringBuilder;
    ArrayAdapterInterface arrayAdapterInterface1 = this.adapters.get(paramClass);
    ArrayAdapterInterface arrayAdapterInterface2 = arrayAdapterInterface1;
    if (arrayAdapterInterface1 == null) {
      if (paramClass.equals(int[].class)) {
        arrayAdapterInterface2 = new IntegerArrayAdapter();
      } else if (paramClass.equals(byte[].class)) {
        arrayAdapterInterface2 = new ByteArrayAdapter();
      } else {
        stringBuilder = new StringBuilder();
        stringBuilder.append("No array pool found for: ");
        stringBuilder.append(paramClass.getSimpleName());
        throw new IllegalArgumentException(stringBuilder.toString());
      } 
      this.adapters.put(paramClass, stringBuilder);
    } 
    return (ArrayAdapterInterface<T>)stringBuilder;
  }
  
  private <T> T getArrayForKey(Key paramKey) {
    return (T)this.groupedMap.get(paramKey);
  }
  
  private <T> T getForKey(Key paramKey, Class<T> paramClass) {
    ArrayAdapterInterface<T> arrayAdapterInterface = getAdapterFromType(paramClass);
    T t2 = (T)getArrayForKey(paramKey);
    if (t2 != null) {
      this.currentSize -= arrayAdapterInterface.getArrayLength(t2) * arrayAdapterInterface.getElementSizeInBytes();
      decrementArrayOfSize(arrayAdapterInterface.getArrayLength(t2), paramClass);
    } 
    T t1 = t2;
    if (t2 == null) {
      if (Log.isLoggable(arrayAdapterInterface.getTag(), 2)) {
        String str = arrayAdapterInterface.getTag();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Allocated ");
        stringBuilder.append(paramKey.size);
        stringBuilder.append(" bytes");
        Log.v(str, stringBuilder.toString());
      } 
      t1 = arrayAdapterInterface.newArray(paramKey.size);
    } 
    return t1;
  }
  
  private NavigableMap<Integer, Integer> getSizesForAdapter(Class<?> paramClass) {
    NavigableMap<Object, Object> navigableMap1 = (NavigableMap)this.sortedSizes.get(paramClass);
    NavigableMap<Object, Object> navigableMap2 = navigableMap1;
    if (navigableMap1 == null) {
      navigableMap2 = new TreeMap<Object, Object>();
      this.sortedSizes.put(paramClass, navigableMap2);
    } 
    return (NavigableMap)navigableMap2;
  }
  
  private boolean isNoMoreThanHalfFull() {
    int i = this.currentSize;
    return (i == 0 || this.maxSize / i >= 2);
  }
  
  private boolean isSmallEnoughForReuse(int paramInt) {
    boolean bool;
    if (paramInt <= this.maxSize / 2) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private boolean mayFillRequest(int paramInt, Integer paramInteger) {
    boolean bool;
    if (paramInteger != null && (isNoMoreThanHalfFull() || paramInteger.intValue() <= paramInt * 8)) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void clearMemory() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iconst_0
    //   4: invokespecial evictToSize : (I)V
    //   7: aload_0
    //   8: monitorexit
    //   9: return
    //   10: astore_1
    //   11: aload_0
    //   12: monitorexit
    //   13: aload_1
    //   14: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	10	finally
  }
  
  public <T> T get(int paramInt, Class<T> paramClass) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_2
    //   4: invokespecial getSizesForAdapter : (Ljava/lang/Class;)Ljava/util/NavigableMap;
    //   7: iload_1
    //   8: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   11: invokeinterface ceilingKey : (Ljava/lang/Object;)Ljava/lang/Object;
    //   16: checkcast java/lang/Integer
    //   19: astore_3
    //   20: aload_0
    //   21: iload_1
    //   22: aload_3
    //   23: invokespecial mayFillRequest : (ILjava/lang/Integer;)Z
    //   26: ifeq -> 45
    //   29: aload_0
    //   30: getfield keyPool : Lcom/bumptech/glide/load/engine/bitmap_recycle/LruArrayPool$KeyPool;
    //   33: aload_3
    //   34: invokevirtual intValue : ()I
    //   37: aload_2
    //   38: invokevirtual get : (ILjava/lang/Class;)Lcom/bumptech/glide/load/engine/bitmap_recycle/LruArrayPool$Key;
    //   41: astore_3
    //   42: goto -> 55
    //   45: aload_0
    //   46: getfield keyPool : Lcom/bumptech/glide/load/engine/bitmap_recycle/LruArrayPool$KeyPool;
    //   49: iload_1
    //   50: aload_2
    //   51: invokevirtual get : (ILjava/lang/Class;)Lcom/bumptech/glide/load/engine/bitmap_recycle/LruArrayPool$Key;
    //   54: astore_3
    //   55: aload_0
    //   56: aload_3
    //   57: aload_2
    //   58: invokespecial getForKey : (Lcom/bumptech/glide/load/engine/bitmap_recycle/LruArrayPool$Key;Ljava/lang/Class;)Ljava/lang/Object;
    //   61: astore_2
    //   62: aload_0
    //   63: monitorexit
    //   64: aload_2
    //   65: areturn
    //   66: astore_2
    //   67: aload_0
    //   68: monitorexit
    //   69: aload_2
    //   70: athrow
    // Exception table:
    //   from	to	target	type
    //   2	42	66	finally
    //   45	55	66	finally
    //   55	62	66	finally
  }
  
  int getCurrentSize() {
    Iterator<Class<?>> iterator = this.sortedSizes.keySet().iterator();
    int i = 0;
    label11: while (iterator.hasNext()) {
      Class<?> clazz = iterator.next();
      Iterator<Integer> iterator1 = ((NavigableMap)this.sortedSizes.get(clazz)).keySet().iterator();
      int j = i;
      while (true) {
        i = j;
        if (iterator1.hasNext()) {
          Integer integer = iterator1.next();
          ArrayAdapterInterface<?> arrayAdapterInterface = getAdapterFromType(clazz);
          j += integer.intValue() * ((Integer)((NavigableMap)this.sortedSizes.get(clazz)).get(integer)).intValue() * arrayAdapterInterface.getElementSizeInBytes();
          continue;
        } 
        continue label11;
      } 
    } 
    return i;
  }
  
  public <T> T getExact(int paramInt, Class<T> paramClass) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_0
    //   4: getfield keyPool : Lcom/bumptech/glide/load/engine/bitmap_recycle/LruArrayPool$KeyPool;
    //   7: iload_1
    //   8: aload_2
    //   9: invokevirtual get : (ILjava/lang/Class;)Lcom/bumptech/glide/load/engine/bitmap_recycle/LruArrayPool$Key;
    //   12: aload_2
    //   13: invokespecial getForKey : (Lcom/bumptech/glide/load/engine/bitmap_recycle/LruArrayPool$Key;Ljava/lang/Class;)Ljava/lang/Object;
    //   16: astore_2
    //   17: aload_0
    //   18: monitorexit
    //   19: aload_2
    //   20: areturn
    //   21: astore_2
    //   22: aload_0
    //   23: monitorexit
    //   24: aload_2
    //   25: athrow
    // Exception table:
    //   from	to	target	type
    //   2	17	21	finally
  }
  
  public <T> void put(T paramT) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_1
    //   3: invokevirtual getClass : ()Ljava/lang/Class;
    //   6: astore_2
    //   7: aload_0
    //   8: aload_2
    //   9: invokespecial getAdapterFromType : (Ljava/lang/Class;)Lcom/bumptech/glide/load/engine/bitmap_recycle/ArrayAdapterInterface;
    //   12: astore_3
    //   13: aload_3
    //   14: aload_1
    //   15: invokeinterface getArrayLength : (Ljava/lang/Object;)I
    //   20: istore #4
    //   22: aload_3
    //   23: invokeinterface getElementSizeInBytes : ()I
    //   28: iload #4
    //   30: imul
    //   31: istore #5
    //   33: aload_0
    //   34: iload #5
    //   36: invokespecial isSmallEnoughForReuse : (I)Z
    //   39: istore #6
    //   41: iload #6
    //   43: ifne -> 49
    //   46: aload_0
    //   47: monitorexit
    //   48: return
    //   49: aload_0
    //   50: getfield keyPool : Lcom/bumptech/glide/load/engine/bitmap_recycle/LruArrayPool$KeyPool;
    //   53: iload #4
    //   55: aload_2
    //   56: invokevirtual get : (ILjava/lang/Class;)Lcom/bumptech/glide/load/engine/bitmap_recycle/LruArrayPool$Key;
    //   59: astore_3
    //   60: aload_0
    //   61: getfield groupedMap : Lcom/bumptech/glide/load/engine/bitmap_recycle/GroupedLinkedMap;
    //   64: aload_3
    //   65: aload_1
    //   66: invokevirtual put : (Lcom/bumptech/glide/load/engine/bitmap_recycle/Poolable;Ljava/lang/Object;)V
    //   69: aload_0
    //   70: aload_2
    //   71: invokespecial getSizesForAdapter : (Ljava/lang/Class;)Ljava/util/NavigableMap;
    //   74: astore_1
    //   75: aload_1
    //   76: aload_3
    //   77: getfield size : I
    //   80: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   83: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   88: checkcast java/lang/Integer
    //   91: astore_2
    //   92: aload_3
    //   93: getfield size : I
    //   96: istore #7
    //   98: iconst_1
    //   99: istore #4
    //   101: aload_2
    //   102: ifnonnull -> 108
    //   105: goto -> 116
    //   108: iconst_1
    //   109: aload_2
    //   110: invokevirtual intValue : ()I
    //   113: iadd
    //   114: istore #4
    //   116: aload_1
    //   117: iload #7
    //   119: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   122: iload #4
    //   124: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   127: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   132: pop
    //   133: aload_0
    //   134: aload_0
    //   135: getfield currentSize : I
    //   138: iload #5
    //   140: iadd
    //   141: putfield currentSize : I
    //   144: aload_0
    //   145: invokespecial evict : ()V
    //   148: aload_0
    //   149: monitorexit
    //   150: return
    //   151: astore_1
    //   152: aload_0
    //   153: monitorexit
    //   154: aload_1
    //   155: athrow
    // Exception table:
    //   from	to	target	type
    //   2	41	151	finally
    //   49	98	151	finally
    //   108	116	151	finally
    //   116	148	151	finally
  }
  
  @Deprecated
  public <T> void put(T paramT, Class<T> paramClass) {
    put(paramT);
  }
  
  public void trimMemory(int paramInt) {
    /* monitor enter ThisExpression{ObjectType{com/bumptech/glide/load/engine/bitmap_recycle/LruArrayPool}} */
    if (paramInt >= 40) {
      try {
        clearMemory();
      } finally {
        Exception exception;
      } 
    } else if (paramInt >= 20 || paramInt == 15) {
      evictToSize(this.maxSize / 2);
    } 
    /* monitor exit ThisExpression{ObjectType{com/bumptech/glide/load/engine/bitmap_recycle/LruArrayPool}} */
  }
  
  private static final class Key implements Poolable {
    private Class<?> arrayClass;
    
    private final LruArrayPool.KeyPool pool;
    
    int size;
    
    Key(LruArrayPool.KeyPool param1KeyPool) {
      this.pool = param1KeyPool;
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
          if (this.arrayClass == ((Key)param1Object).arrayClass)
            bool2 = true; 
        } 
      } 
      return bool2;
    }
    
    public int hashCode() {
      byte b;
      int i = this.size;
      Class<?> clazz = this.arrayClass;
      if (clazz != null) {
        b = clazz.hashCode();
      } else {
        b = 0;
      } 
      return i * 31 + b;
    }
    
    void init(int param1Int, Class<?> param1Class) {
      this.size = param1Int;
      this.arrayClass = param1Class;
    }
    
    public void offer() {
      this.pool.offer(this);
    }
    
    public String toString() {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Key{size=");
      stringBuilder.append(this.size);
      stringBuilder.append("array=");
      stringBuilder.append(this.arrayClass);
      stringBuilder.append('}');
      return stringBuilder.toString();
    }
  }
  
  private static final class KeyPool extends BaseKeyPool<Key> {
    protected LruArrayPool.Key create() {
      return new LruArrayPool.Key(this);
    }
    
    LruArrayPool.Key get(int param1Int, Class<?> param1Class) {
      LruArrayPool.Key key = get();
      key.init(param1Int, param1Class);
      return key;
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/engine/bitmap_recycle/LruArrayPool.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */