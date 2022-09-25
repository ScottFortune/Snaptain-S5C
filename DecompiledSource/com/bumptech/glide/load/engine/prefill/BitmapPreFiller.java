package com.bumptech.glide.load.engine.prefill;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.engine.cache.MemoryCache;
import com.bumptech.glide.util.Util;
import java.util.HashMap;
import java.util.Map;

public final class BitmapPreFiller {
  private final BitmapPool bitmapPool;
  
  private BitmapPreFillRunner current;
  
  private final DecodeFormat defaultFormat;
  
  private final Handler handler = new Handler(Looper.getMainLooper());
  
  private final MemoryCache memoryCache;
  
  public BitmapPreFiller(MemoryCache paramMemoryCache, BitmapPool paramBitmapPool, DecodeFormat paramDecodeFormat) {
    this.memoryCache = paramMemoryCache;
    this.bitmapPool = paramBitmapPool;
    this.defaultFormat = paramDecodeFormat;
  }
  
  private static int getSizeInBytes(PreFillType paramPreFillType) {
    return Util.getBitmapByteSize(paramPreFillType.getWidth(), paramPreFillType.getHeight(), paramPreFillType.getConfig());
  }
  
  PreFillQueue generateAllocationOrder(PreFillType... paramVarArgs) {
    long l1 = this.memoryCache.getMaxSize();
    long l2 = this.memoryCache.getCurrentSize();
    long l3 = this.bitmapPool.getMaxSize();
    int i = paramVarArgs.length;
    boolean bool = false;
    int j = 0;
    int k = 0;
    while (j < i) {
      k += paramVarArgs[j].getWeight();
      j++;
    } 
    float f = (float)(l1 - l2 + l3) / k;
    HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
    j = paramVarArgs.length;
    for (k = bool; k < j; k++) {
      PreFillType preFillType = paramVarArgs[k];
      hashMap.put(preFillType, Integer.valueOf(Math.round(preFillType.getWeight() * f) / getSizeInBytes(preFillType)));
    } 
    return new PreFillQueue((Map)hashMap);
  }
  
  public void preFill(PreFillType.Builder... paramVarArgs) {
    BitmapPreFillRunner bitmapPreFillRunner = this.current;
    if (bitmapPreFillRunner != null)
      bitmapPreFillRunner.cancel(); 
    PreFillType[] arrayOfPreFillType = new PreFillType[paramVarArgs.length];
    for (byte b = 0; b < paramVarArgs.length; b++) {
      PreFillType.Builder builder = paramVarArgs[b];
      if (builder.getConfig() == null) {
        Bitmap.Config config;
        if (this.defaultFormat == DecodeFormat.PREFER_ARGB_8888) {
          config = Bitmap.Config.ARGB_8888;
        } else {
          config = Bitmap.Config.RGB_565;
        } 
        builder.setConfig(config);
      } 
      arrayOfPreFillType[b] = builder.build();
    } 
    PreFillQueue preFillQueue = generateAllocationOrder(arrayOfPreFillType);
    this.current = new BitmapPreFillRunner(this.bitmapPool, this.memoryCache, preFillQueue);
    this.handler.post(this.current);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/engine/prefill/BitmapPreFiller.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */