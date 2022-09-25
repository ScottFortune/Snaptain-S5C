package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.util.ExceptionCatchingInputStream;
import com.bumptech.glide.util.MarkEnforcingInputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamBitmapDecoder implements ResourceDecoder<InputStream, Bitmap> {
  private final ArrayPool byteArrayPool;
  
  private final Downsampler downsampler;
  
  public StreamBitmapDecoder(Downsampler paramDownsampler, ArrayPool paramArrayPool) {
    this.downsampler = paramDownsampler;
    this.byteArrayPool = paramArrayPool;
  }
  
  public Resource<Bitmap> decode(InputStream paramInputStream, int paramInt1, int paramInt2, Options paramOptions) throws IOException {
    boolean bool;
    if (paramInputStream instanceof RecyclableBufferedInputStream) {
      paramInputStream = paramInputStream;
      bool = false;
    } else {
      paramInputStream = new RecyclableBufferedInputStream(paramInputStream, this.byteArrayPool);
      bool = true;
    } 
    ExceptionCatchingInputStream exceptionCatchingInputStream = ExceptionCatchingInputStream.obtain(paramInputStream);
    MarkEnforcingInputStream markEnforcingInputStream = new MarkEnforcingInputStream((InputStream)exceptionCatchingInputStream);
    UntrustedCallbacks untrustedCallbacks = new UntrustedCallbacks((RecyclableBufferedInputStream)paramInputStream, exceptionCatchingInputStream);
    try {
      return this.downsampler.decode((InputStream)markEnforcingInputStream, paramInt1, paramInt2, paramOptions, untrustedCallbacks);
    } finally {
      exceptionCatchingInputStream.release();
      if (bool)
        paramInputStream.release(); 
    } 
  }
  
  public boolean handles(InputStream paramInputStream, Options paramOptions) {
    return this.downsampler.handles(paramInputStream);
  }
  
  static class UntrustedCallbacks implements Downsampler.DecodeCallbacks {
    private final RecyclableBufferedInputStream bufferedStream;
    
    private final ExceptionCatchingInputStream exceptionStream;
    
    UntrustedCallbacks(RecyclableBufferedInputStream param1RecyclableBufferedInputStream, ExceptionCatchingInputStream param1ExceptionCatchingInputStream) {
      this.bufferedStream = param1RecyclableBufferedInputStream;
      this.exceptionStream = param1ExceptionCatchingInputStream;
    }
    
    public void onDecodeComplete(BitmapPool param1BitmapPool, Bitmap param1Bitmap) throws IOException {
      IOException iOException = this.exceptionStream.getException();
      if (iOException != null) {
        if (param1Bitmap != null)
          param1BitmapPool.put(param1Bitmap); 
        throw iOException;
      } 
    }
    
    public void onObtainBounds() {
      this.bufferedStream.fixMarkLimit();
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/resource/bitmap/StreamBitmapDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */