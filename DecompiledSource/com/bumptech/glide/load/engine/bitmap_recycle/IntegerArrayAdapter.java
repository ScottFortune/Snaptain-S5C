package com.bumptech.glide.load.engine.bitmap_recycle;

public final class IntegerArrayAdapter implements ArrayAdapterInterface<int[]> {
  private static final String TAG = "IntegerArrayPool";
  
  public int getArrayLength(int[] paramArrayOfint) {
    return paramArrayOfint.length;
  }
  
  public int getElementSizeInBytes() {
    return 4;
  }
  
  public String getTag() {
    return "IntegerArrayPool";
  }
  
  public int[] newArray(int paramInt) {
    return new int[paramInt];
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/engine/bitmap_recycle/IntegerArrayAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */