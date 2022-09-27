package com.bumptech.glide.load.engine.cache;

import android.util.Log;
import com.bumptech.glide.disklrucache.DiskLruCache;
import com.bumptech.glide.load.Key;
import java.io.File;
import java.io.IOException;

public class DiskLruCacheWrapper implements DiskCache {
  private static final int APP_VERSION = 1;
  
  private static final String TAG = "DiskLruCacheWrapper";
  
  private static final int VALUE_COUNT = 1;
  
  private static DiskLruCacheWrapper wrapper;
  
  private final File directory;
  
  private DiskLruCache diskLruCache;
  
  private final long maxSize;
  
  private final SafeKeyGenerator safeKeyGenerator;
  
  private final DiskCacheWriteLocker writeLocker = new DiskCacheWriteLocker();
  
  @Deprecated
  protected DiskLruCacheWrapper(File paramFile, long paramLong) {
    this.directory = paramFile;
    this.maxSize = paramLong;
    this.safeKeyGenerator = new SafeKeyGenerator();
  }
  
  public static DiskCache create(File paramFile, long paramLong) {
    return new DiskLruCacheWrapper(paramFile, paramLong);
  }
  
  @Deprecated
  public static DiskCache get(File paramFile, long paramLong) {
    // Byte code:
    //   0: ldc com/bumptech/glide/load/engine/cache/DiskLruCacheWrapper
    //   2: monitorenter
    //   3: getstatic com/bumptech/glide/load/engine/cache/DiskLruCacheWrapper.wrapper : Lcom/bumptech/glide/load/engine/cache/DiskLruCacheWrapper;
    //   6: ifnonnull -> 23
    //   9: new com/bumptech/glide/load/engine/cache/DiskLruCacheWrapper
    //   12: astore_3
    //   13: aload_3
    //   14: aload_0
    //   15: lload_1
    //   16: invokespecial <init> : (Ljava/io/File;J)V
    //   19: aload_3
    //   20: putstatic com/bumptech/glide/load/engine/cache/DiskLruCacheWrapper.wrapper : Lcom/bumptech/glide/load/engine/cache/DiskLruCacheWrapper;
    //   23: getstatic com/bumptech/glide/load/engine/cache/DiskLruCacheWrapper.wrapper : Lcom/bumptech/glide/load/engine/cache/DiskLruCacheWrapper;
    //   26: astore_0
    //   27: ldc com/bumptech/glide/load/engine/cache/DiskLruCacheWrapper
    //   29: monitorexit
    //   30: aload_0
    //   31: areturn
    //   32: astore_0
    //   33: ldc com/bumptech/glide/load/engine/cache/DiskLruCacheWrapper
    //   35: monitorexit
    //   36: aload_0
    //   37: athrow
    // Exception table:
    //   from	to	target	type
    //   3	23	32	finally
    //   23	27	32	finally
  }
  
  private DiskLruCache getDiskCache() throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield diskLruCache : Lcom/bumptech/glide/disklrucache/DiskLruCache;
    //   6: ifnonnull -> 26
    //   9: aload_0
    //   10: aload_0
    //   11: getfield directory : Ljava/io/File;
    //   14: iconst_1
    //   15: iconst_1
    //   16: aload_0
    //   17: getfield maxSize : J
    //   20: invokestatic open : (Ljava/io/File;IIJ)Lcom/bumptech/glide/disklrucache/DiskLruCache;
    //   23: putfield diskLruCache : Lcom/bumptech/glide/disklrucache/DiskLruCache;
    //   26: aload_0
    //   27: getfield diskLruCache : Lcom/bumptech/glide/disklrucache/DiskLruCache;
    //   30: astore_1
    //   31: aload_0
    //   32: monitorexit
    //   33: aload_1
    //   34: areturn
    //   35: astore_1
    //   36: aload_0
    //   37: monitorexit
    //   38: aload_1
    //   39: athrow
    // Exception table:
    //   from	to	target	type
    //   2	26	35	finally
    //   26	31	35	finally
  }
  
  private void resetDiskCache() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aconst_null
    //   4: putfield diskLruCache : Lcom/bumptech/glide/disklrucache/DiskLruCache;
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
  
  public void clear() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokespecial getDiskCache : ()Lcom/bumptech/glide/disklrucache/DiskLruCache;
    //   6: invokevirtual delete : ()V
    //   9: aload_0
    //   10: invokespecial resetDiskCache : ()V
    //   13: goto -> 42
    //   16: astore_1
    //   17: goto -> 45
    //   20: astore_1
    //   21: ldc 'DiskLruCacheWrapper'
    //   23: iconst_5
    //   24: invokestatic isLoggable : (Ljava/lang/String;I)Z
    //   27: ifeq -> 9
    //   30: ldc 'DiskLruCacheWrapper'
    //   32: ldc 'Unable to clear disk cache or disk cache cleared externally'
    //   34: aload_1
    //   35: invokestatic w : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   38: pop
    //   39: goto -> 9
    //   42: aload_0
    //   43: monitorexit
    //   44: return
    //   45: aload_0
    //   46: invokespecial resetDiskCache : ()V
    //   49: aload_1
    //   50: athrow
    //   51: astore_1
    //   52: aload_0
    //   53: monitorexit
    //   54: goto -> 59
    //   57: aload_1
    //   58: athrow
    //   59: goto -> 57
    // Exception table:
    //   from	to	target	type
    //   2	9	20	java/io/IOException
    //   2	9	16	finally
    //   9	13	51	finally
    //   21	39	16	finally
    //   45	51	51	finally
  }
  
  public void delete(Key paramKey) {
    String str = this.safeKeyGenerator.getSafeKey(paramKey);
    try {
      getDiskCache().remove(str);
    } catch (IOException iOException) {
      if (Log.isLoggable("DiskLruCacheWrapper", 5))
        Log.w("DiskLruCacheWrapper", "Unable to delete from disk cache", iOException); 
    } 
  }
  
  public File get(Key paramKey) {
    String str = this.safeKeyGenerator.getSafeKey(paramKey);
    if (Log.isLoggable("DiskLruCacheWrapper", 2)) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Get: Obtained: ");
      stringBuilder.append(str);
      stringBuilder.append(" for for Key: ");
      stringBuilder.append(paramKey);
      Log.v("DiskLruCacheWrapper", stringBuilder.toString());
    } 
    Key key = null;
    try {
      DiskLruCache.Value value = getDiskCache().get(str);
      paramKey = key;
      if (value != null)
        File file = value.getFile(0); 
    } catch (IOException iOException) {
      paramKey = key;
      if (Log.isLoggable("DiskLruCacheWrapper", 5)) {
        Log.w("DiskLruCacheWrapper", "Unable to get from disk cache", iOException);
        paramKey = key;
      } 
    } 
    return (File)paramKey;
  }
  
  public void put(Key paramKey, DiskCache.Writer paramWriter) {
    String str = this.safeKeyGenerator.getSafeKey(paramKey);
    this.writeLocker.acquire(str);
    try {
      if (Log.isLoggable("DiskLruCacheWrapper", 2)) {
        StringBuilder stringBuilder = new StringBuilder();
        this();
        stringBuilder.append("Put: Obtained: ");
        stringBuilder.append(str);
        stringBuilder.append(" for for Key: ");
        stringBuilder.append(paramKey);
        Log.v("DiskLruCacheWrapper", stringBuilder.toString());
      } 
      try {
        DiskLruCache diskLruCache = getDiskCache();
        DiskLruCache.Value value = diskLruCache.get(str);
        if (value != null)
          return; 
        DiskLruCache.Editor editor = diskLruCache.edit(str);
        if (editor != null) {
          try {
            if (paramWriter.write(editor.getFile(0)))
              editor.commit(); 
          } finally {
            editor.abortUnlessCommitted();
          } 
        } else {
          IllegalStateException illegalStateException = new IllegalStateException();
          StringBuilder stringBuilder = new StringBuilder();
          this();
          stringBuilder.append("Had two simultaneous puts for: ");
          stringBuilder.append(str);
          this(stringBuilder.toString());
          throw illegalStateException;
        } 
      } catch (IOException iOException) {
        if (Log.isLoggable("DiskLruCacheWrapper", 5))
          Log.w("DiskLruCacheWrapper", "Unable to put to disk cache", iOException); 
      } 
      return;
    } finally {
      this.writeLocker.release(str);
    } 
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/engine/cache/DiskLruCacheWrapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */