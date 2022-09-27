package com.bumptech.glide.load.engine.prefill;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.engine.cache.MemoryCache;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;
import com.bumptech.glide.util.Util;
import java.security.MessageDigest;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

final class BitmapPreFillRunner implements Runnable {
  static final int BACKOFF_RATIO = 4;
  
  private static final Clock DEFAULT_CLOCK = new Clock();
  
  static final long INITIAL_BACKOFF_MS = 40L;
  
  static final long MAX_BACKOFF_MS = TimeUnit.SECONDS.toMillis(1L);
  
  static final long MAX_DURATION_MS = 32L;
  
  static final String TAG = "PreFillRunner";
  
  private final BitmapPool bitmapPool;
  
  private final Clock clock;
  
  private long currentDelay = 40L;
  
  private final Handler handler;
  
  private boolean isCancelled;
  
  private final MemoryCache memoryCache;
  
  private final Set<PreFillType> seenTypes = new HashSet<PreFillType>();
  
  private final PreFillQueue toPrefill;
  
  public BitmapPreFillRunner(BitmapPool paramBitmapPool, MemoryCache paramMemoryCache, PreFillQueue paramPreFillQueue) {
    this(paramBitmapPool, paramMemoryCache, paramPreFillQueue, DEFAULT_CLOCK, new Handler(Looper.getMainLooper()));
  }
  
  BitmapPreFillRunner(BitmapPool paramBitmapPool, MemoryCache paramMemoryCache, PreFillQueue paramPreFillQueue, Clock paramClock, Handler paramHandler) {
    this.bitmapPool = paramBitmapPool;
    this.memoryCache = paramMemoryCache;
    this.toPrefill = paramPreFillQueue;
    this.clock = paramClock;
    this.handler = paramHandler;
  }
  
  private long getFreeMemoryCacheBytes() {
    return this.memoryCache.getMaxSize() - this.memoryCache.getCurrentSize();
  }
  
  private long getNextDelay() {
    long l = this.currentDelay;
    this.currentDelay = Math.min(4L * l, MAX_BACKOFF_MS);
    return l;
  }
  
  private boolean isGcDetected(long paramLong) {
    boolean bool;
    if (this.clock.now() - paramLong >= 32L) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  boolean allocate() {
    boolean bool;
    long l = this.clock.now();
    while (!this.toPrefill.isEmpty() && !isGcDetected(l)) {
      Bitmap bitmap;
      PreFillType preFillType = this.toPrefill.remove();
      if (!this.seenTypes.contains(preFillType)) {
        this.seenTypes.add(preFillType);
        bitmap = this.bitmapPool.getDirty(preFillType.getWidth(), preFillType.getHeight(), preFillType.getConfig());
      } else {
        bitmap = Bitmap.createBitmap(preFillType.getWidth(), preFillType.getHeight(), preFillType.getConfig());
      } 
      int i = Util.getBitmapByteSize(bitmap);
      if (getFreeMemoryCacheBytes() >= i) {
        UniqueKey uniqueKey = new UniqueKey();
        this.memoryCache.put(uniqueKey, (Resource)BitmapResource.obtain(bitmap, this.bitmapPool));
      } else {
        this.bitmapPool.put(bitmap);
      } 
      if (Log.isLoggable("PreFillRunner", 3)) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("allocated [");
        stringBuilder.append(preFillType.getWidth());
        stringBuilder.append("x");
        stringBuilder.append(preFillType.getHeight());
        stringBuilder.append("] ");
        stringBuilder.append(preFillType.getConfig());
        stringBuilder.append(" size: ");
        stringBuilder.append(i);
        Log.d("PreFillRunner", stringBuilder.toString());
      } 
    } 
    if (!this.isCancelled && !this.toPrefill.isEmpty()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void cancel() {
    this.isCancelled = true;
  }
  
  public void run() {
    if (allocate())
      this.handler.postDelayed(this, getNextDelay()); 
  }
  
  static class Clock {
    long now() {
      return SystemClock.currentThreadTimeMillis();
    }
  }
  
  private static final class UniqueKey implements Key {
    public void updateDiskCacheKey(MessageDigest param1MessageDigest) {
      throw new UnsupportedOperationException();
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/engine/prefill/BitmapPreFillRunner.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */