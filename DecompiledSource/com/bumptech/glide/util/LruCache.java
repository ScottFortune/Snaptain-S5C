package com.bumptech.glide.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class LruCache<T, Y> {
  private final Map<T, Y> cache = new LinkedHashMap<T, Y>(100, 0.75F, true);
  
  private long currentSize;
  
  private final long initialMaxSize;
  
  private long maxSize;
  
  public LruCache(long paramLong) {
    this.initialMaxSize = paramLong;
    this.maxSize = paramLong;
  }
  
  private void evict() {
    trimToSize(this.maxSize);
  }
  
  public void clearMemory() {
    trimToSize(0L);
  }
  
  public boolean contains(T paramT) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield cache : Ljava/util/Map;
    //   6: aload_1
    //   7: invokeinterface containsKey : (Ljava/lang/Object;)Z
    //   12: istore_2
    //   13: aload_0
    //   14: monitorexit
    //   15: iload_2
    //   16: ireturn
    //   17: astore_1
    //   18: aload_0
    //   19: monitorexit
    //   20: aload_1
    //   21: athrow
    // Exception table:
    //   from	to	target	type
    //   2	13	17	finally
  }
  
  public Y get(T paramT) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield cache : Ljava/util/Map;
    //   6: aload_1
    //   7: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   12: astore_1
    //   13: aload_0
    //   14: monitorexit
    //   15: aload_1
    //   16: areturn
    //   17: astore_1
    //   18: aload_0
    //   19: monitorexit
    //   20: aload_1
    //   21: athrow
    // Exception table:
    //   from	to	target	type
    //   2	13	17	finally
  }
  
  protected int getCount() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield cache : Ljava/util/Map;
    //   6: invokeinterface size : ()I
    //   11: istore_1
    //   12: aload_0
    //   13: monitorexit
    //   14: iload_1
    //   15: ireturn
    //   16: astore_2
    //   17: aload_0
    //   18: monitorexit
    //   19: aload_2
    //   20: athrow
    // Exception table:
    //   from	to	target	type
    //   2	12	16	finally
  }
  
  public long getCurrentSize() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield currentSize : J
    //   6: lstore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: lload_1
    //   10: lreturn
    //   11: astore_3
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_3
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  public long getMaxSize() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield maxSize : J
    //   6: lstore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: lload_1
    //   10: lreturn
    //   11: astore_3
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_3
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  protected int getSize(Y paramY) {
    return 1;
  }
  
  protected void onItemEvicted(T paramT, Y paramY) {}
  
  public Y put(T paramT, Y paramY) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_2
    //   4: invokevirtual getSize : (Ljava/lang/Object;)I
    //   7: i2l
    //   8: lstore_3
    //   9: lload_3
    //   10: aload_0
    //   11: getfield maxSize : J
    //   14: lcmp
    //   15: iflt -> 28
    //   18: aload_0
    //   19: aload_1
    //   20: aload_2
    //   21: invokevirtual onItemEvicted : (Ljava/lang/Object;Ljava/lang/Object;)V
    //   24: aload_0
    //   25: monitorexit
    //   26: aconst_null
    //   27: areturn
    //   28: aload_2
    //   29: ifnull -> 42
    //   32: aload_0
    //   33: aload_0
    //   34: getfield currentSize : J
    //   37: lload_3
    //   38: ladd
    //   39: putfield currentSize : J
    //   42: aload_0
    //   43: getfield cache : Ljava/util/Map;
    //   46: aload_1
    //   47: aload_2
    //   48: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   53: astore #5
    //   55: aload #5
    //   57: ifnull -> 92
    //   60: aload_0
    //   61: aload_0
    //   62: getfield currentSize : J
    //   65: aload_0
    //   66: aload #5
    //   68: invokevirtual getSize : (Ljava/lang/Object;)I
    //   71: i2l
    //   72: lsub
    //   73: putfield currentSize : J
    //   76: aload #5
    //   78: aload_2
    //   79: invokevirtual equals : (Ljava/lang/Object;)Z
    //   82: ifne -> 92
    //   85: aload_0
    //   86: aload_1
    //   87: aload #5
    //   89: invokevirtual onItemEvicted : (Ljava/lang/Object;Ljava/lang/Object;)V
    //   92: aload_0
    //   93: invokespecial evict : ()V
    //   96: aload_0
    //   97: monitorexit
    //   98: aload #5
    //   100: areturn
    //   101: astore_1
    //   102: aload_0
    //   103: monitorexit
    //   104: aload_1
    //   105: athrow
    // Exception table:
    //   from	to	target	type
    //   2	24	101	finally
    //   32	42	101	finally
    //   42	55	101	finally
    //   60	92	101	finally
    //   92	96	101	finally
  }
  
  public Y remove(T paramT) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield cache : Ljava/util/Map;
    //   6: aload_1
    //   7: invokeinterface remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   12: astore_1
    //   13: aload_1
    //   14: ifnull -> 32
    //   17: aload_0
    //   18: aload_0
    //   19: getfield currentSize : J
    //   22: aload_0
    //   23: aload_1
    //   24: invokevirtual getSize : (Ljava/lang/Object;)I
    //   27: i2l
    //   28: lsub
    //   29: putfield currentSize : J
    //   32: aload_0
    //   33: monitorexit
    //   34: aload_1
    //   35: areturn
    //   36: astore_1
    //   37: aload_0
    //   38: monitorexit
    //   39: aload_1
    //   40: athrow
    // Exception table:
    //   from	to	target	type
    //   2	13	36	finally
    //   17	32	36	finally
  }
  
  public void setSizeMultiplier(float paramFloat) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: fload_1
    //   3: fconst_0
    //   4: fcmpg
    //   5: iflt -> 34
    //   8: aload_0
    //   9: aload_0
    //   10: getfield initialMaxSize : J
    //   13: l2f
    //   14: fload_1
    //   15: fmul
    //   16: invokestatic round : (F)I
    //   19: i2l
    //   20: putfield maxSize : J
    //   23: aload_0
    //   24: invokespecial evict : ()V
    //   27: aload_0
    //   28: monitorexit
    //   29: return
    //   30: astore_2
    //   31: goto -> 46
    //   34: new java/lang/IllegalArgumentException
    //   37: astore_2
    //   38: aload_2
    //   39: ldc 'Multiplier must be >= 0'
    //   41: invokespecial <init> : (Ljava/lang/String;)V
    //   44: aload_2
    //   45: athrow
    //   46: aload_0
    //   47: monitorexit
    //   48: aload_2
    //   49: athrow
    // Exception table:
    //   from	to	target	type
    //   8	27	30	finally
    //   34	46	30	finally
  }
  
  protected void trimToSize(long paramLong) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield currentSize : J
    //   6: lload_1
    //   7: lcmp
    //   8: ifle -> 88
    //   11: aload_0
    //   12: getfield cache : Ljava/util/Map;
    //   15: invokeinterface entrySet : ()Ljava/util/Set;
    //   20: invokeinterface iterator : ()Ljava/util/Iterator;
    //   25: astore_3
    //   26: aload_3
    //   27: invokeinterface next : ()Ljava/lang/Object;
    //   32: checkcast java/util/Map$Entry
    //   35: astore #4
    //   37: aload #4
    //   39: invokeinterface getValue : ()Ljava/lang/Object;
    //   44: astore #5
    //   46: aload_0
    //   47: aload_0
    //   48: getfield currentSize : J
    //   51: aload_0
    //   52: aload #5
    //   54: invokevirtual getSize : (Ljava/lang/Object;)I
    //   57: i2l
    //   58: lsub
    //   59: putfield currentSize : J
    //   62: aload #4
    //   64: invokeinterface getKey : ()Ljava/lang/Object;
    //   69: astore #4
    //   71: aload_3
    //   72: invokeinterface remove : ()V
    //   77: aload_0
    //   78: aload #4
    //   80: aload #5
    //   82: invokevirtual onItemEvicted : (Ljava/lang/Object;Ljava/lang/Object;)V
    //   85: goto -> 2
    //   88: aload_0
    //   89: monitorexit
    //   90: return
    //   91: astore_3
    //   92: aload_0
    //   93: monitorexit
    //   94: goto -> 99
    //   97: aload_3
    //   98: athrow
    //   99: goto -> 97
    // Exception table:
    //   from	to	target	type
    //   2	85	91	finally
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/util/LruCache.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */