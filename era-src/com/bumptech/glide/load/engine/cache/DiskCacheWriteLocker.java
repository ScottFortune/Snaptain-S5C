package com.bumptech.glide.load.engine.cache;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

final class DiskCacheWriteLocker {
  private final Map<String, WriteLock> locks = new HashMap<String, WriteLock>();
  
  private final WriteLockPool writeLockPool = new WriteLockPool();
  
  void acquire(String paramString) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield locks : Ljava/util/Map;
    //   6: aload_1
    //   7: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   12: checkcast com/bumptech/glide/load/engine/cache/DiskCacheWriteLocker$WriteLock
    //   15: astore_2
    //   16: aload_2
    //   17: astore_3
    //   18: aload_2
    //   19: ifnonnull -> 42
    //   22: aload_0
    //   23: getfield writeLockPool : Lcom/bumptech/glide/load/engine/cache/DiskCacheWriteLocker$WriteLockPool;
    //   26: invokevirtual obtain : ()Lcom/bumptech/glide/load/engine/cache/DiskCacheWriteLocker$WriteLock;
    //   29: astore_3
    //   30: aload_0
    //   31: getfield locks : Ljava/util/Map;
    //   34: aload_1
    //   35: aload_3
    //   36: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   41: pop
    //   42: aload_3
    //   43: aload_3
    //   44: getfield interestedThreads : I
    //   47: iconst_1
    //   48: iadd
    //   49: putfield interestedThreads : I
    //   52: aload_0
    //   53: monitorexit
    //   54: aload_3
    //   55: getfield lock : Ljava/util/concurrent/locks/Lock;
    //   58: invokeinterface lock : ()V
    //   63: return
    //   64: astore_1
    //   65: aload_0
    //   66: monitorexit
    //   67: aload_1
    //   68: athrow
    // Exception table:
    //   from	to	target	type
    //   2	16	64	finally
    //   22	42	64	finally
    //   42	54	64	finally
    //   65	67	64	finally
  }
  
  void release(String paramString) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield locks : Ljava/util/Map;
    //   6: aload_1
    //   7: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   12: invokestatic checkNotNull : (Ljava/lang/Object;)Ljava/lang/Object;
    //   15: checkcast com/bumptech/glide/load/engine/cache/DiskCacheWriteLocker$WriteLock
    //   18: astore_2
    //   19: aload_2
    //   20: getfield interestedThreads : I
    //   23: iconst_1
    //   24: if_icmplt -> 162
    //   27: aload_2
    //   28: aload_2
    //   29: getfield interestedThreads : I
    //   32: iconst_1
    //   33: isub
    //   34: putfield interestedThreads : I
    //   37: aload_2
    //   38: getfield interestedThreads : I
    //   41: ifne -> 150
    //   44: aload_0
    //   45: getfield locks : Ljava/util/Map;
    //   48: aload_1
    //   49: invokeinterface remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   54: checkcast com/bumptech/glide/load/engine/cache/DiskCacheWriteLocker$WriteLock
    //   57: astore_3
    //   58: aload_3
    //   59: aload_2
    //   60: invokevirtual equals : (Ljava/lang/Object;)Z
    //   63: ifeq -> 77
    //   66: aload_0
    //   67: getfield writeLockPool : Lcom/bumptech/glide/load/engine/cache/DiskCacheWriteLocker$WriteLockPool;
    //   70: aload_3
    //   71: invokevirtual offer : (Lcom/bumptech/glide/load/engine/cache/DiskCacheWriteLocker$WriteLock;)V
    //   74: goto -> 150
    //   77: new java/lang/IllegalStateException
    //   80: astore #4
    //   82: new java/lang/StringBuilder
    //   85: astore #5
    //   87: aload #5
    //   89: invokespecial <init> : ()V
    //   92: aload #5
    //   94: ldc 'Removed the wrong lock, expected to remove: '
    //   96: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   99: pop
    //   100: aload #5
    //   102: aload_2
    //   103: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   106: pop
    //   107: aload #5
    //   109: ldc ', but actually removed: '
    //   111: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   114: pop
    //   115: aload #5
    //   117: aload_3
    //   118: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   121: pop
    //   122: aload #5
    //   124: ldc ', safeKey: '
    //   126: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   129: pop
    //   130: aload #5
    //   132: aload_1
    //   133: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   136: pop
    //   137: aload #4
    //   139: aload #5
    //   141: invokevirtual toString : ()Ljava/lang/String;
    //   144: invokespecial <init> : (Ljava/lang/String;)V
    //   147: aload #4
    //   149: athrow
    //   150: aload_0
    //   151: monitorexit
    //   152: aload_2
    //   153: getfield lock : Ljava/util/concurrent/locks/Lock;
    //   156: invokeinterface unlock : ()V
    //   161: return
    //   162: new java/lang/IllegalStateException
    //   165: astore #4
    //   167: new java/lang/StringBuilder
    //   170: astore #5
    //   172: aload #5
    //   174: invokespecial <init> : ()V
    //   177: aload #5
    //   179: ldc 'Cannot release a lock that is not held, safeKey: '
    //   181: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   184: pop
    //   185: aload #5
    //   187: aload_1
    //   188: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   191: pop
    //   192: aload #5
    //   194: ldc ', interestedThreads: '
    //   196: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   199: pop
    //   200: aload #5
    //   202: aload_2
    //   203: getfield interestedThreads : I
    //   206: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   209: pop
    //   210: aload #4
    //   212: aload #5
    //   214: invokevirtual toString : ()Ljava/lang/String;
    //   217: invokespecial <init> : (Ljava/lang/String;)V
    //   220: aload #4
    //   222: athrow
    //   223: astore_1
    //   224: aload_0
    //   225: monitorexit
    //   226: aload_1
    //   227: athrow
    // Exception table:
    //   from	to	target	type
    //   2	74	223	finally
    //   77	150	223	finally
    //   150	152	223	finally
    //   162	223	223	finally
    //   224	226	223	finally
  }
  
  private static class WriteLock {
    int interestedThreads;
    
    final Lock lock = new ReentrantLock();
  }
  
  private static class WriteLockPool {
    private static final int MAX_POOL_SIZE = 10;
    
    private final Queue<DiskCacheWriteLocker.WriteLock> pool = new ArrayDeque<DiskCacheWriteLocker.WriteLock>();
    
    DiskCacheWriteLocker.WriteLock obtain() {
      Queue<DiskCacheWriteLocker.WriteLock> queue;
      DiskCacheWriteLocker.WriteLock writeLock;
      synchronized (this.pool) {
        DiskCacheWriteLocker.WriteLock writeLock1 = this.pool.poll();
        writeLock = writeLock1;
        if (writeLock1 == null)
          writeLock = new DiskCacheWriteLocker.WriteLock(); 
        return writeLock;
      } 
    }
    
    void offer(DiskCacheWriteLocker.WriteLock param1WriteLock) {
      synchronized (this.pool) {
        if (this.pool.size() < 10)
          this.pool.offer(param1WriteLock); 
        return;
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/engine/cache/DiskCacheWriteLocker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */