package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import java.security.MessageDigest;

public class CenterCrop extends BitmapTransformation {
  private static final String ID = "com.bumptech.glide.load.resource.bitmap.CenterCrop";
  
  private static final byte[] ID_BYTES = "com.bumptech.glide.load.resource.bitmap.CenterCrop".getBytes(CHARSET);
  
  public boolean equals(Object paramObject) {
    return paramObject instanceof CenterCrop;
  }
  
  public int hashCode() {
    return "com.bumptech.glide.load.resource.bitmap.CenterCrop".hashCode();
  }
  
  protected Bitmap transform(BitmapPool paramBitmapPool, Bitmap paramBitmap, int paramInt1, int paramInt2) {
    return TransformationUtils.centerCrop(paramBitmapPool, paramBitmap, paramInt1, paramInt2);
  }
  
  public void updateDiskCacheKey(MessageDigest paramMessageDigest) {
    paramMessageDigest.update(ID_BYTES);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/resource/bitmap/CenterCrop.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */