package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.util.Util;
import java.nio.ByteBuffer;
import java.security.MessageDigest;

public final class RoundedCorners extends BitmapTransformation {
  private static final String ID = "com.bumptech.glide.load.resource.bitmap.RoundedCorners";
  
  private static final byte[] ID_BYTES = "com.bumptech.glide.load.resource.bitmap.RoundedCorners".getBytes(CHARSET);
  
  private final int roundingRadius;
  
  public RoundedCorners(int paramInt) {
    boolean bool;
    if (paramInt > 0) {
      bool = true;
    } else {
      bool = false;
    } 
    Preconditions.checkArgument(bool, "roundingRadius must be greater than 0.");
    this.roundingRadius = paramInt;
  }
  
  public boolean equals(Object paramObject) {
    boolean bool = paramObject instanceof RoundedCorners;
    boolean bool1 = false;
    boolean bool2 = bool1;
    if (bool) {
      paramObject = paramObject;
      bool2 = bool1;
      if (this.roundingRadius == ((RoundedCorners)paramObject).roundingRadius)
        bool2 = true; 
    } 
    return bool2;
  }
  
  public int hashCode() {
    return Util.hashCode("com.bumptech.glide.load.resource.bitmap.RoundedCorners".hashCode(), Util.hashCode(this.roundingRadius));
  }
  
  protected Bitmap transform(BitmapPool paramBitmapPool, Bitmap paramBitmap, int paramInt1, int paramInt2) {
    return TransformationUtils.roundedCorners(paramBitmapPool, paramBitmap, this.roundingRadius);
  }
  
  public void updateDiskCacheKey(MessageDigest paramMessageDigest) {
    paramMessageDigest.update(ID_BYTES);
    paramMessageDigest.update(ByteBuffer.allocate(4).putInt(this.roundingRadius).array());
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/resource/bitmap/RoundedCorners.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */