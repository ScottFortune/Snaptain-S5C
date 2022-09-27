package com.bumptech.glide.load.resource.bitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.util.Preconditions;
import java.security.MessageDigest;

@Deprecated
public class BitmapDrawableTransformation implements Transformation<BitmapDrawable> {
  private final Transformation<Drawable> wrapped;
  
  public BitmapDrawableTransformation(Transformation<Bitmap> paramTransformation) {
    this.wrapped = (Transformation<Drawable>)Preconditions.checkNotNull(new DrawableTransformation(paramTransformation, false));
  }
  
  private static Resource<BitmapDrawable> convertToBitmapDrawableResource(Resource<Drawable> paramResource) {
    if (paramResource.get() instanceof BitmapDrawable)
      return (Resource)paramResource; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Wrapped transformation unexpectedly returned a non BitmapDrawable resource: ");
    stringBuilder.append(paramResource.get());
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  private static Resource<Drawable> convertToDrawableResource(Resource<BitmapDrawable> paramResource) {
    return (Resource)paramResource;
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject instanceof BitmapDrawableTransformation) {
      paramObject = paramObject;
      return this.wrapped.equals(((BitmapDrawableTransformation)paramObject).wrapped);
    } 
    return false;
  }
  
  public int hashCode() {
    return this.wrapped.hashCode();
  }
  
  public Resource<BitmapDrawable> transform(Context paramContext, Resource<BitmapDrawable> paramResource, int paramInt1, int paramInt2) {
    paramResource = (Resource)convertToDrawableResource(paramResource);
    return convertToBitmapDrawableResource(this.wrapped.transform(paramContext, paramResource, paramInt1, paramInt2));
  }
  
  public void updateDiskCacheKey(MessageDigest paramMessageDigest) {
    this.wrapped.updateDiskCacheKey(paramMessageDigest);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/resource/bitmap/BitmapDrawableTransformation.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */