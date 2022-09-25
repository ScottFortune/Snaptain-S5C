package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.util.Log;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPoolAdapter;
import com.bumptech.glide.load.resource.ImageDecoderResourceDecoder;
import java.io.IOException;

public final class BitmapImageDecoderResourceDecoder extends ImageDecoderResourceDecoder<Bitmap> {
  private static final String TAG = "BitmapImageDecoder";
  
  private final BitmapPool bitmapPool = (BitmapPool)new BitmapPoolAdapter();
  
  protected Resource<Bitmap> decode(ImageDecoder.Source paramSource, int paramInt1, int paramInt2, ImageDecoder.OnHeaderDecodedListener paramOnHeaderDecodedListener) throws IOException {
    Bitmap bitmap = ImageDecoder.decodeBitmap(paramSource, paramOnHeaderDecodedListener);
    if (Log.isLoggable("BitmapImageDecoder", 2)) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Decoded [");
      stringBuilder.append(bitmap.getWidth());
      stringBuilder.append("x");
      stringBuilder.append(bitmap.getHeight());
      stringBuilder.append("] for [");
      stringBuilder.append(paramInt1);
      stringBuilder.append("x");
      stringBuilder.append(paramInt2);
      stringBuilder.append("]");
      Log.v("BitmapImageDecoder", stringBuilder.toString());
    } 
    return new BitmapResource(bitmap, this.bitmapPool);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/resource/bitmap/BitmapImageDecoderResourceDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */