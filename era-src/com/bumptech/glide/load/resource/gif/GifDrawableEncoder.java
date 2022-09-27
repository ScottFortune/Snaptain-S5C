package com.bumptech.glide.load.resource.gif;

import android.util.Log;
import com.bumptech.glide.load.EncodeStrategy;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceEncoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.util.ByteBufferUtil;
import java.io.File;
import java.io.IOException;

public class GifDrawableEncoder implements ResourceEncoder<GifDrawable> {
  private static final String TAG = "GifEncoder";
  
  public boolean encode(Resource<GifDrawable> paramResource, File paramFile, Options paramOptions) {
    boolean bool;
    GifDrawable gifDrawable = (GifDrawable)paramResource.get();
    try {
      ByteBufferUtil.toFile(gifDrawable.getBuffer(), paramFile);
      bool = true;
    } catch (IOException iOException) {
      if (Log.isLoggable("GifEncoder", 5))
        Log.w("GifEncoder", "Failed to encode GIF drawable data", iOException); 
      bool = false;
    } 
    return bool;
  }
  
  public EncodeStrategy getEncodeStrategy(Options paramOptions) {
    return EncodeStrategy.SOURCE;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/resource/gif/GifDrawableEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */