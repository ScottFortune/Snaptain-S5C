package com.bumptech.glide.load.data;

import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.load.resource.bitmap.RecyclableBufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public final class InputStreamRewinder implements DataRewinder<InputStream> {
  private static final int MARK_LIMIT = 5242880;
  
  private final RecyclableBufferedInputStream bufferedStream;
  
  InputStreamRewinder(InputStream paramInputStream, ArrayPool paramArrayPool) {
    this.bufferedStream = new RecyclableBufferedInputStream(paramInputStream, paramArrayPool);
    this.bufferedStream.mark(5242880);
  }
  
  public void cleanup() {
    this.bufferedStream.release();
  }
  
  public InputStream rewindAndGet() throws IOException {
    this.bufferedStream.reset();
    return (InputStream)this.bufferedStream;
  }
  
  public static final class Factory implements DataRewinder.Factory<InputStream> {
    private final ArrayPool byteArrayPool;
    
    public Factory(ArrayPool param1ArrayPool) {
      this.byteArrayPool = param1ArrayPool;
    }
    
    public DataRewinder<InputStream> build(InputStream param1InputStream) {
      return new InputStreamRewinder(param1InputStream, this.byteArrayPool);
    }
    
    public Class<InputStream> getDataClass() {
      return InputStream.class;
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/data/InputStreamRewinder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */