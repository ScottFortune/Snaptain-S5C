package com.bumptech.glide.load.engine.bitmap_recycle;

public final class ByteArrayAdapter implements ArrayAdapterInterface<byte[]> {
  private static final String TAG = "ByteArrayPool";
  
  public int getArrayLength(byte[] paramArrayOfbyte) {
    return paramArrayOfbyte.length;
  }
  
  public int getElementSizeInBytes() {
    return 1;
  }
  
  public String getTag() {
    return "ByteArrayPool";
  }
  
  public byte[] newArray(int paramInt) {
    return new byte[paramInt];
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/engine/bitmap_recycle/ByteArrayAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */