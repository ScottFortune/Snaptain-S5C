package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.util.ByteBufferUtil;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class ByteBufferBitmapDecoder implements ResourceDecoder<ByteBuffer, Bitmap> {
  private final Downsampler downsampler;
  
  public ByteBufferBitmapDecoder(Downsampler paramDownsampler) {
    this.downsampler = paramDownsampler;
  }
  
  public Resource<Bitmap> decode(ByteBuffer paramByteBuffer, int paramInt1, int paramInt2, Options paramOptions) throws IOException {
    InputStream inputStream = ByteBufferUtil.toStream(paramByteBuffer);
    return this.downsampler.decode(inputStream, paramInt1, paramInt2, paramOptions);
  }
  
  public boolean handles(ByteBuffer paramByteBuffer, Options paramOptions) {
    return this.downsampler.handles(paramByteBuffer);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/resource/bitmap/ByteBufferBitmapDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */