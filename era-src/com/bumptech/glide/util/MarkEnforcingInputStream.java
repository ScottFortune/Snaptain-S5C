package com.bumptech.glide.util;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MarkEnforcingInputStream extends FilterInputStream {
  private static final int END_OF_STREAM = -1;
  
  private static final int UNSET = -2147483648;
  
  private int availableBytes = Integer.MIN_VALUE;
  
  public MarkEnforcingInputStream(InputStream paramInputStream) {
    super(paramInputStream);
  }
  
  private long getBytesToRead(long paramLong) {
    int i = this.availableBytes;
    if (i == 0)
      return -1L; 
    long l = paramLong;
    if (i != Integer.MIN_VALUE) {
      l = paramLong;
      if (paramLong > i)
        l = i; 
    } 
    return l;
  }
  
  private void updateAvailableBytesAfterRead(long paramLong) {
    int i = this.availableBytes;
    if (i != Integer.MIN_VALUE && paramLong != -1L)
      this.availableBytes = (int)(i - paramLong); 
  }
  
  public int available() throws IOException {
    int i = this.availableBytes;
    if (i == Integer.MIN_VALUE) {
      i = super.available();
    } else {
      i = Math.min(i, super.available());
    } 
    return i;
  }
  
  public void mark(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iload_1
    //   4: invokespecial mark : (I)V
    //   7: aload_0
    //   8: iload_1
    //   9: putfield availableBytes : I
    //   12: aload_0
    //   13: monitorexit
    //   14: return
    //   15: astore_2
    //   16: aload_0
    //   17: monitorexit
    //   18: aload_2
    //   19: athrow
    // Exception table:
    //   from	to	target	type
    //   2	12	15	finally
  }
  
  public int read() throws IOException {
    if (getBytesToRead(1L) == -1L)
      return -1; 
    int i = super.read();
    updateAvailableBytesAfterRead(1L);
    return i;
  }
  
  public int read(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws IOException {
    paramInt2 = (int)getBytesToRead(paramInt2);
    if (paramInt2 == -1)
      return -1; 
    paramInt1 = super.read(paramArrayOfbyte, paramInt1, paramInt2);
    updateAvailableBytesAfterRead(paramInt1);
    return paramInt1;
  }
  
  public void reset() throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokespecial reset : ()V
    //   6: aload_0
    //   7: ldc -2147483648
    //   9: putfield availableBytes : I
    //   12: aload_0
    //   13: monitorexit
    //   14: return
    //   15: astore_1
    //   16: aload_0
    //   17: monitorexit
    //   18: aload_1
    //   19: athrow
    // Exception table:
    //   from	to	target	type
    //   2	12	15	finally
  }
  
  public long skip(long paramLong) throws IOException {
    paramLong = getBytesToRead(paramLong);
    if (paramLong == -1L)
      return 0L; 
    paramLong = super.skip(paramLong);
    updateAvailableBytesAfterRead(paramLong);
    return paramLong;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/util/MarkEnforcingInputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */