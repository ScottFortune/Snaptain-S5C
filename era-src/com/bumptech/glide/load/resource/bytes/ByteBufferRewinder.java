package com.bumptech.glide.load.resource.bytes;

import com.bumptech.glide.load.data.DataRewinder;
import java.io.IOException;
import java.nio.ByteBuffer;

public class ByteBufferRewinder implements DataRewinder<ByteBuffer> {
  private final ByteBuffer buffer;
  
  public ByteBufferRewinder(ByteBuffer paramByteBuffer) {
    this.buffer = paramByteBuffer;
  }
  
  public void cleanup() {}
  
  public ByteBuffer rewindAndGet() {
    this.buffer.position(0);
    return this.buffer;
  }
  
  public static class Factory implements DataRewinder.Factory<ByteBuffer> {
    public DataRewinder<ByteBuffer> build(ByteBuffer param1ByteBuffer) {
      return new ByteBufferRewinder(param1ByteBuffer);
    }
    
    public Class<ByteBuffer> getDataClass() {
      return ByteBuffer.class;
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/resource/bytes/ByteBufferRewinder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */