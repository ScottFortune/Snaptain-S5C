package com.bumptech.glide.load.resource.bytes;

import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.util.Preconditions;

public class BytesResource implements Resource<byte[]> {
  private final byte[] bytes;
  
  public BytesResource(byte[] paramArrayOfbyte) {
    this.bytes = (byte[])Preconditions.checkNotNull(paramArrayOfbyte);
  }
  
  public byte[] get() {
    return this.bytes;
  }
  
  public Class<byte[]> getResourceClass() {
    return byte[].class;
  }
  
  public int getSize() {
    return this.bytes.length;
  }
  
  public void recycle() {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/resource/bytes/BytesResource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */