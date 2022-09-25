package com.bumptech.glide.load.engine.bitmap_recycle;

import android.graphics.Bitmap;

interface LruPoolStrategy {
  Bitmap get(int paramInt1, int paramInt2, Bitmap.Config paramConfig);
  
  int getSize(Bitmap paramBitmap);
  
  String logBitmap(int paramInt1, int paramInt2, Bitmap.Config paramConfig);
  
  String logBitmap(Bitmap paramBitmap);
  
  void put(Bitmap paramBitmap);
  
  Bitmap removeLast();
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/engine/bitmap_recycle/LruPoolStrategy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */