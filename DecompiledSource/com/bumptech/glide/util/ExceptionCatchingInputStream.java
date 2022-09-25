package com.bumptech.glide.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Queue;

public class ExceptionCatchingInputStream extends InputStream {
  private static final Queue<ExceptionCatchingInputStream> QUEUE = Util.createQueue(0);
  
  private IOException exception;
  
  private InputStream wrapped;
  
  static void clearQueue() {
    while (!QUEUE.isEmpty())
      QUEUE.remove(); 
  }
  
  public static ExceptionCatchingInputStream obtain(InputStream paramInputStream) {
    Queue<ExceptionCatchingInputStream> queue;
    ExceptionCatchingInputStream exceptionCatchingInputStream;
    synchronized (QUEUE) {
      ExceptionCatchingInputStream exceptionCatchingInputStream1 = QUEUE.poll();
      exceptionCatchingInputStream = exceptionCatchingInputStream1;
      if (exceptionCatchingInputStream1 == null)
        exceptionCatchingInputStream = new ExceptionCatchingInputStream(); 
      exceptionCatchingInputStream.setInputStream(paramInputStream);
      return exceptionCatchingInputStream;
    } 
  }
  
  public int available() throws IOException {
    return this.wrapped.available();
  }
  
  public void close() throws IOException {
    this.wrapped.close();
  }
  
  public IOException getException() {
    return this.exception;
  }
  
  public void mark(int paramInt) {
    this.wrapped.mark(paramInt);
  }
  
  public boolean markSupported() {
    return this.wrapped.markSupported();
  }
  
  public int read() {
    byte b;
    try {
      b = this.wrapped.read();
    } catch (IOException iOException) {
      this.exception = iOException;
      b = -1;
    } 
    return b;
  }
  
  public int read(byte[] paramArrayOfbyte) {
    byte b;
    try {
      b = this.wrapped.read(paramArrayOfbyte);
    } catch (IOException iOException) {
      this.exception = iOException;
      b = -1;
    } 
    return b;
  }
  
  public int read(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
    try {
      paramInt1 = this.wrapped.read(paramArrayOfbyte, paramInt1, paramInt2);
    } catch (IOException iOException) {
      this.exception = iOException;
      paramInt1 = -1;
    } 
    return paramInt1;
  }
  
  public void release() {
    this.exception = null;
    this.wrapped = null;
    synchronized (QUEUE) {
      QUEUE.offer(this);
      return;
    } 
  }
  
  public void reset() throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield wrapped : Ljava/io/InputStream;
    //   6: invokevirtual reset : ()V
    //   9: aload_0
    //   10: monitorexit
    //   11: return
    //   12: astore_1
    //   13: aload_0
    //   14: monitorexit
    //   15: aload_1
    //   16: athrow
    // Exception table:
    //   from	to	target	type
    //   2	9	12	finally
  }
  
  void setInputStream(InputStream paramInputStream) {
    this.wrapped = paramInputStream;
  }
  
  public long skip(long paramLong) {
    try {
      paramLong = this.wrapped.skip(paramLong);
    } catch (IOException iOException) {
      this.exception = iOException;
      paramLong = 0L;
    } 
    return paramLong;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/util/ExceptionCatchingInputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */