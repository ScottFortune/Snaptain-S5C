package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import com.bumptech.glide.load.engine.Initializable;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.util.Util;

public class BitmapResource implements Resource<Bitmap>, Initializable {
  private final Bitmap bitmap;
  
  private final BitmapPool bitmapPool;
  
  public BitmapResource(Bitmap paramBitmap, BitmapPool paramBitmapPool) {
    this.bitmap = (Bitmap)Preconditions.checkNotNull(paramBitmap, "Bitmap must not be null");
    this.bitmapPool = (BitmapPool)Preconditions.checkNotNull(paramBitmapPool, "BitmapPool must not be null");
  }
  
  public static BitmapResource obtain(Bitmap paramBitmap, BitmapPool paramBitmapPool) {
    return (paramBitmap == null) ? null : new BitmapResource(paramBitmap, paramBitmapPool);
  }
  
  public Bitmap get() {
    return this.bitmap;
  }
  
  public Class<Bitmap> getResourceClass() {
    return Bitmap.class;
  }
  
  public int getSize() {
    return Util.getBitmapByteSize(this.bitmap);
  }
  
  public void initialize() {
    this.bitmap.prepareToDraw();
  }
  
  public void recycle() {
    this.bitmapPool.put(this.bitmap);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/resource/bitmap/BitmapResource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */