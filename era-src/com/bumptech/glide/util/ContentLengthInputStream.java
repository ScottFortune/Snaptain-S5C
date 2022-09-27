package com.bumptech.glide.util;

import android.text.TextUtils;
import android.util.Log;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public final class ContentLengthInputStream extends FilterInputStream {
  private static final String TAG = "ContentLengthStream";
  
  private static final int UNKNOWN = -1;
  
  private final long contentLength;
  
  private int readSoFar;
  
  private ContentLengthInputStream(InputStream paramInputStream, long paramLong) {
    super(paramInputStream);
    this.contentLength = paramLong;
  }
  
  private int checkReadSoFarOrThrow(int paramInt) throws IOException {
    if (paramInt >= 0) {
      this.readSoFar += paramInt;
    } else if (this.contentLength - this.readSoFar > 0L) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Failed to read all expected data, expected: ");
      stringBuilder.append(this.contentLength);
      stringBuilder.append(", but read: ");
      stringBuilder.append(this.readSoFar);
      throw new IOException(stringBuilder.toString());
    } 
    return paramInt;
  }
  
  public static InputStream obtain(InputStream paramInputStream, long paramLong) {
    return new ContentLengthInputStream(paramInputStream, paramLong);
  }
  
  public static InputStream obtain(InputStream paramInputStream, String paramString) {
    return obtain(paramInputStream, parseContentLength(paramString));
  }
  
  private static int parseContentLength(String paramString) {
    if (!TextUtils.isEmpty(paramString)) {
      byte b1;
      try {
        b1 = Integer.parseInt(paramString);
      } catch (NumberFormatException numberFormatException) {
        if (Log.isLoggable("ContentLengthStream", 3)) {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("failed to parse content length header: ");
          stringBuilder.append(paramString);
          Log.d("ContentLengthStream", stringBuilder.toString(), numberFormatException);
        } 
        b1 = -1;
      } 
      return b1;
    } 
    byte b = -1;
  }
  
  public int available() throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield contentLength : J
    //   6: aload_0
    //   7: getfield readSoFar : I
    //   10: i2l
    //   11: lsub
    //   12: aload_0
    //   13: getfield in : Ljava/io/InputStream;
    //   16: invokevirtual available : ()I
    //   19: i2l
    //   20: invokestatic max : (JJ)J
    //   23: lstore_1
    //   24: lload_1
    //   25: l2i
    //   26: istore_3
    //   27: aload_0
    //   28: monitorexit
    //   29: iload_3
    //   30: ireturn
    //   31: astore #4
    //   33: aload_0
    //   34: monitorexit
    //   35: aload #4
    //   37: athrow
    // Exception table:
    //   from	to	target	type
    //   2	24	31	finally
  }
  
  public int read() throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokespecial read : ()I
    //   6: istore_1
    //   7: iload_1
    //   8: iflt -> 16
    //   11: iconst_1
    //   12: istore_2
    //   13: goto -> 18
    //   16: iconst_m1
    //   17: istore_2
    //   18: aload_0
    //   19: iload_2
    //   20: invokespecial checkReadSoFarOrThrow : (I)I
    //   23: pop
    //   24: aload_0
    //   25: monitorexit
    //   26: iload_1
    //   27: ireturn
    //   28: astore_3
    //   29: aload_0
    //   30: monitorexit
    //   31: aload_3
    //   32: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	28	finally
    //   18	24	28	finally
  }
  
  public int read(byte[] paramArrayOfbyte) throws IOException {
    return read(paramArrayOfbyte, 0, paramArrayOfbyte.length);
  }
  
  public int read(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_0
    //   4: aload_1
    //   5: iload_2
    //   6: iload_3
    //   7: invokespecial read : ([BII)I
    //   10: invokespecial checkReadSoFarOrThrow : (I)I
    //   13: istore_2
    //   14: aload_0
    //   15: monitorexit
    //   16: iload_2
    //   17: ireturn
    //   18: astore_1
    //   19: aload_0
    //   20: monitorexit
    //   21: aload_1
    //   22: athrow
    // Exception table:
    //   from	to	target	type
    //   2	14	18	finally
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/util/ContentLengthInputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */