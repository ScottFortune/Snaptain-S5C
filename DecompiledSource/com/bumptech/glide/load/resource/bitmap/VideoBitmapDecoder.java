package com.bumptech.glide.load.resource.bitmap;

import android.content.Context;
import android.os.ParcelFileDescriptor;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;

@Deprecated
public class VideoBitmapDecoder extends VideoDecoder<ParcelFileDescriptor> {
  public VideoBitmapDecoder(Context paramContext) {
    this(Glide.get(paramContext).getBitmapPool());
  }
  
  public VideoBitmapDecoder(BitmapPool paramBitmapPool) {
    super(paramBitmapPool, new VideoDecoder.ParcelFileDescriptorInitializer());
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/resource/bitmap/VideoBitmapDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */