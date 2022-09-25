package com.bumptech.glide.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.atomic.AtomicReference;

public final class ByteBufferUtil {
  private static final AtomicReference<byte[]> BUFFER_REF = (AtomicReference)new AtomicReference<byte>();
  
  private static final int BUFFER_SIZE = 16384;
  
  public static ByteBuffer fromFile(File paramFile) throws IOException {
    IOException iOException;
    RandomAccessFile randomAccessFile;
    MappedByteBuffer mappedByteBuffer = null;
    try {
      long l = paramFile.length();
    } finally {
      paramFile = null;
    } 
    if (iOException != null)
      try {
        iOException.close();
      } catch (IOException iOException1) {} 
    if (randomAccessFile != null)
      try {
        randomAccessFile.close();
      } catch (IOException iOException1) {} 
    throw paramFile;
  }
  
  public static ByteBuffer fromStream(InputStream paramInputStream) throws IOException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(16384);
    byte[] arrayOfByte1 = BUFFER_REF.getAndSet(null);
    byte[] arrayOfByte2 = arrayOfByte1;
    if (arrayOfByte1 == null)
      arrayOfByte2 = new byte[16384]; 
    while (true) {
      int i = paramInputStream.read(arrayOfByte2);
      if (i >= 0) {
        byteArrayOutputStream.write(arrayOfByte2, 0, i);
        continue;
      } 
      BUFFER_REF.set(arrayOfByte2);
      byte[] arrayOfByte = byteArrayOutputStream.toByteArray();
      return (ByteBuffer)ByteBuffer.allocateDirect(arrayOfByte.length).put(arrayOfByte).position(0);
    } 
  }
  
  private static SafeArray getSafeArray(ByteBuffer paramByteBuffer) {
    return (!paramByteBuffer.isReadOnly() && paramByteBuffer.hasArray()) ? new SafeArray(paramByteBuffer.array(), paramByteBuffer.arrayOffset(), paramByteBuffer.limit()) : null;
  }
  
  public static byte[] toBytes(ByteBuffer paramByteBuffer) {
    byte[] arrayOfByte;
    SafeArray safeArray = getSafeArray(paramByteBuffer);
    if (safeArray != null && safeArray.offset == 0 && safeArray.limit == safeArray.data.length) {
      arrayOfByte = paramByteBuffer.array();
    } else {
      ByteBuffer byteBuffer = arrayOfByte.asReadOnlyBuffer();
      arrayOfByte = new byte[byteBuffer.limit()];
      byteBuffer.position(0);
      byteBuffer.get(arrayOfByte);
    } 
    return arrayOfByte;
  }
  
  public static void toFile(ByteBuffer paramByteBuffer, File paramFile) throws IOException {
    paramByteBuffer.position(0);
    FileChannel fileChannel = null;
    File file = null;
    try {
      RandomAccessFile randomAccessFile = new RandomAccessFile();
      this(paramFile, "rw");
      paramFile = file;
    } finally {
      paramByteBuffer = null;
    } 
    if (fileChannel != null)
      try {
        fileChannel.close();
      } catch (IOException iOException) {} 
    if (paramFile != null)
      try {
        paramFile.close();
      } catch (IOException iOException) {} 
    throw paramByteBuffer;
  }
  
  public static InputStream toStream(ByteBuffer paramByteBuffer) {
    return new ByteBufferStream(paramByteBuffer);
  }
  
  public static void toStream(ByteBuffer paramByteBuffer, OutputStream paramOutputStream) throws IOException {
    SafeArray safeArray = getSafeArray(paramByteBuffer);
    if (safeArray != null) {
      paramOutputStream.write(safeArray.data, safeArray.offset, safeArray.offset + safeArray.limit);
    } else {
      byte[] arrayOfByte2 = BUFFER_REF.getAndSet(null);
      byte[] arrayOfByte1 = arrayOfByte2;
      if (arrayOfByte2 == null)
        arrayOfByte1 = new byte[16384]; 
      while (paramByteBuffer.remaining() > 0) {
        int i = Math.min(paramByteBuffer.remaining(), arrayOfByte1.length);
        paramByteBuffer.get(arrayOfByte1, 0, i);
        paramOutputStream.write(arrayOfByte1, 0, i);
      } 
      BUFFER_REF.set(arrayOfByte1);
    } 
  }
  
  private static class ByteBufferStream extends InputStream {
    private static final int UNSET = -1;
    
    private final ByteBuffer byteBuffer;
    
    private int markPos = -1;
    
    ByteBufferStream(ByteBuffer param1ByteBuffer) {
      this.byteBuffer = param1ByteBuffer;
    }
    
    public int available() {
      return this.byteBuffer.remaining();
    }
    
    public void mark(int param1Int) {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: aload_0
      //   4: getfield byteBuffer : Ljava/nio/ByteBuffer;
      //   7: invokevirtual position : ()I
      //   10: putfield markPos : I
      //   13: aload_0
      //   14: monitorexit
      //   15: return
      //   16: astore_2
      //   17: aload_0
      //   18: monitorexit
      //   19: aload_2
      //   20: athrow
      // Exception table:
      //   from	to	target	type
      //   2	13	16	finally
    }
    
    public boolean markSupported() {
      return true;
    }
    
    public int read() {
      return !this.byteBuffer.hasRemaining() ? -1 : (this.byteBuffer.get() & 0xFF);
    }
    
    public int read(byte[] param1ArrayOfbyte, int param1Int1, int param1Int2) throws IOException {
      if (!this.byteBuffer.hasRemaining())
        return -1; 
      param1Int2 = Math.min(param1Int2, available());
      this.byteBuffer.get(param1ArrayOfbyte, param1Int1, param1Int2);
      return param1Int2;
    }
    
    public void reset() throws IOException {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: getfield markPos : I
      //   6: iconst_m1
      //   7: if_icmpeq -> 25
      //   10: aload_0
      //   11: getfield byteBuffer : Ljava/nio/ByteBuffer;
      //   14: aload_0
      //   15: getfield markPos : I
      //   18: invokevirtual position : (I)Ljava/nio/Buffer;
      //   21: pop
      //   22: aload_0
      //   23: monitorexit
      //   24: return
      //   25: new java/io/IOException
      //   28: astore_1
      //   29: aload_1
      //   30: ldc 'Cannot reset to unset mark position'
      //   32: invokespecial <init> : (Ljava/lang/String;)V
      //   35: aload_1
      //   36: athrow
      //   37: astore_1
      //   38: aload_0
      //   39: monitorexit
      //   40: aload_1
      //   41: athrow
      // Exception table:
      //   from	to	target	type
      //   2	22	37	finally
      //   25	37	37	finally
    }
    
    public long skip(long param1Long) throws IOException {
      if (!this.byteBuffer.hasRemaining())
        return -1L; 
      param1Long = Math.min(param1Long, available());
      ByteBuffer byteBuffer = this.byteBuffer;
      byteBuffer.position((int)(byteBuffer.position() + param1Long));
      return param1Long;
    }
  }
  
  static final class SafeArray {
    final byte[] data;
    
    final int limit;
    
    final int offset;
    
    SafeArray(byte[] param1ArrayOfbyte, int param1Int1, int param1Int2) {
      this.data = param1ArrayOfbyte;
      this.offset = param1Int1;
      this.limit = param1Int2;
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/util/ByteBufferUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */