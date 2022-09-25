package com.bumptech.glide.load.data;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public final class ExifOrientationStream extends FilterInputStream {
  private static final byte[] EXIF_SEGMENT = new byte[] { 
      -1, -31, 0, 28, 69, 120, 105, 102, 0, 0, 
      77, 77, 0, 0, 0, 0, 0, 8, 0, 1, 
      1, 18, 0, 2, 0, 0, 0, 1, 0 };
  
  private static final int ORIENTATION_POSITION;
  
  private static final int SEGMENT_LENGTH = EXIF_SEGMENT.length;
  
  private static final int SEGMENT_START_POSITION = 2;
  
  private final byte orientation;
  
  private int position;
  
  static {
    ORIENTATION_POSITION = SEGMENT_LENGTH + 2;
  }
  
  public ExifOrientationStream(InputStream paramInputStream, int paramInt) {
    super(paramInputStream);
    if (paramInt >= -1 && paramInt <= 8) {
      this.orientation = (byte)(byte)paramInt;
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Cannot add invalid orientation: ");
    stringBuilder.append(paramInt);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public void mark(int paramInt) {
    throw new UnsupportedOperationException();
  }
  
  public boolean markSupported() {
    return false;
  }
  
  public int read() throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: getfield position : I
    //   4: istore_1
    //   5: iload_1
    //   6: iconst_2
    //   7: if_icmplt -> 50
    //   10: getstatic com/bumptech/glide/load/data/ExifOrientationStream.ORIENTATION_POSITION : I
    //   13: istore_2
    //   14: iload_1
    //   15: iload_2
    //   16: if_icmple -> 22
    //   19: goto -> 50
    //   22: iload_1
    //   23: iload_2
    //   24: if_icmpne -> 35
    //   27: aload_0
    //   28: getfield orientation : B
    //   31: istore_2
    //   32: goto -> 55
    //   35: getstatic com/bumptech/glide/load/data/ExifOrientationStream.EXIF_SEGMENT : [B
    //   38: iload_1
    //   39: iconst_2
    //   40: isub
    //   41: baload
    //   42: sipush #255
    //   45: iand
    //   46: istore_2
    //   47: goto -> 55
    //   50: aload_0
    //   51: invokespecial read : ()I
    //   54: istore_2
    //   55: iload_2
    //   56: iconst_m1
    //   57: if_icmpeq -> 70
    //   60: aload_0
    //   61: aload_0
    //   62: getfield position : I
    //   65: iconst_1
    //   66: iadd
    //   67: putfield position : I
    //   70: iload_2
    //   71: ireturn
  }
  
  public int read(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws IOException {
    int i = this.position;
    int j = ORIENTATION_POSITION;
    if (i > j) {
      paramInt1 = super.read(paramArrayOfbyte, paramInt1, paramInt2);
    } else if (i == j) {
      paramArrayOfbyte[paramInt1] = (byte)this.orientation;
      paramInt1 = 1;
    } else if (i < 2) {
      paramInt1 = super.read(paramArrayOfbyte, paramInt1, 2 - i);
    } else {
      paramInt2 = Math.min(j - i, paramInt2);
      System.arraycopy(EXIF_SEGMENT, this.position - 2, paramArrayOfbyte, paramInt1, paramInt2);
      paramInt1 = paramInt2;
    } 
    if (paramInt1 > 0)
      this.position += paramInt1; 
    return paramInt1;
  }
  
  public void reset() throws IOException {
    throw new UnsupportedOperationException();
  }
  
  public long skip(long paramLong) throws IOException {
    paramLong = super.skip(paramLong);
    if (paramLong > 0L)
      this.position = (int)(this.position + paramLong); 
    return paramLong;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/data/ExifOrientationStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */