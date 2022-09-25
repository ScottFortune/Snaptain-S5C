package com.bumptech.glide.load.resource.gif;

import android.content.Context;
import android.graphics.Bitmap;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;
import com.bumptech.glide.util.Preconditions;
import java.security.MessageDigest;

public class GifDrawableTransformation implements Transformation<GifDrawable> {
  private final Transformation<Bitmap> wrapped;
  
  public GifDrawableTransformation(Transformation<Bitmap> paramTransformation) {
    this.wrapped = (Transformation<Bitmap>)Preconditions.checkNotNull(paramTransformation);
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject instanceof GifDrawableTransformation) {
      paramObject = paramObject;
      return this.wrapped.equals(((GifDrawableTransformation)paramObject).wrapped);
    } 
    return false;
  }
  
  public int hashCode() {
    return this.wrapped.hashCode();
  }
  
  public Resource<GifDrawable> transform(Context paramContext, Resource<GifDrawable> paramResource, int paramInt1, int paramInt2) {
    GifDrawable gifDrawable = (GifDrawable)paramResource.get();
    BitmapPool bitmapPool = Glide.get(paramContext).getBitmapPool();
    BitmapResource bitmapResource = new BitmapResource(gifDrawable.getFirstFrame(), bitmapPool);
    Resource resource = this.wrapped.transform(paramContext, (Resource)bitmapResource, paramInt1, paramInt2);
    if (!bitmapResource.equals(resource))
      bitmapResource.recycle(); 
    Bitmap bitmap = (Bitmap)resource.get();
    gifDrawable.setFrameTransformation(this.wrapped, bitmap);
    return paramResource;
  }
  
  public void updateDiskCacheKey(MessageDigest paramMessageDigest) {
    this.wrapped.updateDiskCacheKey(paramMessageDigest);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/resource/gif/GifDrawableTransformation.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */