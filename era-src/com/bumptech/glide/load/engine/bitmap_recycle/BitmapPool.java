package com.bumptech.glide.load.engine.bitmap_recycle;

import android.graphics.Bitmap;

public interface BitmapPool {
  void clearMemory();
  
  Bitmap get(int paramInt1, int paramInt2, Bitmap.Config paramConfig);
  
  Bitmap getDirty(int paramInt1, int paramInt2, Bitmap.Config paramConfig);
  
  long getMaxSize();
  
  void put(Bitmap paramBitmap);
  
  void setSizeMultiplier(float paramFloat);
  
  void trimMemory(int paramInt);
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/engine/bitmap_recycle/BitmapPool.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */