package com.bumptech.glide.load.resource.bitmap;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.Initializable;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.util.Preconditions;

public final class LazyBitmapDrawableResource implements Resource<BitmapDrawable>, Initializable {
  private final Resource<Bitmap> bitmapResource;
  
  private final Resources resources;
  
  private LazyBitmapDrawableResource(Resources paramResources, Resource<Bitmap> paramResource) {
    this.resources = (Resources)Preconditions.checkNotNull(paramResources);
    this.bitmapResource = (Resource<Bitmap>)Preconditions.checkNotNull(paramResource);
  }
  
  public static Resource<BitmapDrawable> obtain(Resources paramResources, Resource<Bitmap> paramResource) {
    return (paramResource == null) ? null : new LazyBitmapDrawableResource(paramResources, paramResource);
  }
  
  @Deprecated
  public static LazyBitmapDrawableResource obtain(Context paramContext, Bitmap paramBitmap) {
    return (LazyBitmapDrawableResource)obtain(paramContext.getResources(), BitmapResource.obtain(paramBitmap, Glide.get(paramContext).getBitmapPool()));
  }
  
  @Deprecated
  public static LazyBitmapDrawableResource obtain(Resources paramResources, BitmapPool paramBitmapPool, Bitmap paramBitmap) {
    return (LazyBitmapDrawableResource)obtain(paramResources, BitmapResource.obtain(paramBitmap, paramBitmapPool));
  }
  
  public BitmapDrawable get() {
    return new BitmapDrawable(this.resources, (Bitmap)this.bitmapResource.get());
  }
  
  public Class<BitmapDrawable> getResourceClass() {
    return BitmapDrawable.class;
  }
  
  public int getSize() {
    return this.bitmapResource.getSize();
  }
  
  public void initialize() {
    Resource<Bitmap> resource = this.bitmapResource;
    if (resource instanceof Initializable)
      ((Initializable)resource).initialize(); 
  }
  
  public void recycle() {
    this.bitmapResource.recycle();
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/resource/bitmap/LazyBitmapDrawableResource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */