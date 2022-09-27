package com.bumptech.glide.load.resource.transcode;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.LazyBitmapDrawableResource;
import com.bumptech.glide.util.Preconditions;

public class BitmapDrawableTranscoder implements ResourceTranscoder<Bitmap, BitmapDrawable> {
  private final Resources resources;
  
  public BitmapDrawableTranscoder(Context paramContext) {
    this(paramContext.getResources());
  }
  
  public BitmapDrawableTranscoder(Resources paramResources) {
    this.resources = (Resources)Preconditions.checkNotNull(paramResources);
  }
  
  @Deprecated
  public BitmapDrawableTranscoder(Resources paramResources, BitmapPool paramBitmapPool) {
    this(paramResources);
  }
  
  public Resource<BitmapDrawable> transcode(Resource<Bitmap> paramResource, Options paramOptions) {
    return LazyBitmapDrawableResource.obtain(this.resources, paramResource);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/resource/transcode/BitmapDrawableTranscoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */