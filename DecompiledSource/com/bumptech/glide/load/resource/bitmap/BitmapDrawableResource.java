package com.bumptech.glide.load.resource.bitmap;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import com.bumptech.glide.load.engine.Initializable;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.drawable.DrawableResource;
import com.bumptech.glide.util.Util;

public class BitmapDrawableResource extends DrawableResource<BitmapDrawable> implements Initializable {
  private final BitmapPool bitmapPool;
  
  public BitmapDrawableResource(BitmapDrawable paramBitmapDrawable, BitmapPool paramBitmapPool) {
    super((Drawable)paramBitmapDrawable);
    this.bitmapPool = paramBitmapPool;
  }
  
  public Class<BitmapDrawable> getResourceClass() {
    return BitmapDrawable.class;
  }
  
  public int getSize() {
    return Util.getBitmapByteSize(((BitmapDrawable)this.drawable).getBitmap());
  }
  
  public void initialize() {
    ((BitmapDrawable)this.drawable).getBitmap().prepareToDraw();
  }
  
  public void recycle() {
    this.bitmapPool.put(((BitmapDrawable)this.drawable).getBitmap());
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/resource/bitmap/BitmapDrawableResource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */