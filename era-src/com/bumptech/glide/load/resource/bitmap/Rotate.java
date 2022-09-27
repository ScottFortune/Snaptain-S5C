package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.util.Util;
import java.nio.ByteBuffer;
import java.security.MessageDigest;

public class Rotate extends BitmapTransformation {
  private static final String ID = "com.bumptech.glide.load.resource.bitmap.Rotate";
  
  private static final byte[] ID_BYTES = "com.bumptech.glide.load.resource.bitmap.Rotate".getBytes(CHARSET);
  
  private final int degreesToRotate;
  
  public Rotate(int paramInt) {
    this.degreesToRotate = paramInt;
  }
  
  public boolean equals(Object paramObject) {
    boolean bool = paramObject instanceof Rotate;
    boolean bool1 = false;
    boolean bool2 = bool1;
    if (bool) {
      paramObject = paramObject;
      bool2 = bool1;
      if (this.degreesToRotate == ((Rotate)paramObject).degreesToRotate)
        bool2 = true; 
    } 
    return bool2;
  }
  
  public int hashCode() {
    return Util.hashCode("com.bumptech.glide.load.resource.bitmap.Rotate".hashCode(), Util.hashCode(this.degreesToRotate));
  }
  
  protected Bitmap transform(BitmapPool paramBitmapPool, Bitmap paramBitmap, int paramInt1, int paramInt2) {
    return TransformationUtils.rotateImage(paramBitmap, this.degreesToRotate);
  }
  
  public void updateDiskCacheKey(MessageDigest paramMessageDigest) {
    paramMessageDigest.update(ID_BYTES);
    paramMessageDigest.update(ByteBuffer.allocate(4).putInt(this.degreesToRotate).array());
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/resource/bitmap/Rotate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */