package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.util.Util;
import java.io.IOException;

public final class UnitBitmapDecoder implements ResourceDecoder<Bitmap, Bitmap> {
  public Resource<Bitmap> decode(Bitmap paramBitmap, int paramInt1, int paramInt2, Options paramOptions) {
    return new NonOwnedBitmapResource(paramBitmap);
  }
  
  public boolean handles(Bitmap paramBitmap, Options paramOptions) {
    return true;
  }
  
  private static final class NonOwnedBitmapResource implements Resource<Bitmap> {
    private final Bitmap bitmap;
    
    NonOwnedBitmapResource(Bitmap param1Bitmap) {
      this.bitmap = param1Bitmap;
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
    
    public void recycle() {}
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/resource/bitmap/UnitBitmapDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */