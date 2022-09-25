package com.sun.jna;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.WeakHashMap;

public class Memory extends Pointer {
  private static final Map<Memory, Reference<Memory>> allocatedMemory = Collections.synchronizedMap(new WeakHashMap<Memory, Reference<Memory>>());
  
  private static final WeakMemoryHolder buffers = new WeakMemoryHolder();
  
  protected long size;
  
  protected Memory() {}
  
  public Memory(long paramLong) {
    this.size = paramLong;
    if (paramLong > 0L) {
      this.peer = malloc(paramLong);
      if (this.peer != 0L) {
        allocatedMemory.put(this, new WeakReference<Memory>(this));
        return;
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Cannot allocate ");
      stringBuilder.append(paramLong);
      stringBuilder.append(" bytes");
      throw new OutOfMemoryError(stringBuilder.toString());
    } 
    throw new IllegalArgumentException("Allocation size must be greater than zero");
  }
  
  public static void disposeAll() {
    Iterator<?> iterator = (new LinkedList(allocatedMemory.keySet())).iterator();
    while (iterator.hasNext())
      ((Memory)iterator.next()).dispose(); 
  }
  
  protected static void free(long paramLong) {
    if (paramLong != 0L)
      Native.free(paramLong); 
  }
  
  protected static long malloc(long paramLong) {
    return Native.malloc(paramLong);
  }
  
  public static void purge() {
    buffers.clean();
  }
  
  public Memory align(int paramInt) {
    if (paramInt > 0) {
      for (byte b = 0; b < 32; b++) {
        if (paramInt == 1 << b) {
          long l1 = paramInt;
          long l2 = l1 - 1L ^ 0xFFFFFFFFFFFFFFFFL;
          if ((this.peer & l2) != this.peer) {
            l2 = this.peer + l1 - 1L & l2;
            l1 = this.peer + this.size - l2;
            if (l1 > 0L)
              return (Memory)share(l2 - this.peer, l1); 
            throw new IllegalArgumentException("Insufficient memory to align to the requested boundary");
          } 
          return this;
        } 
      } 
      throw new IllegalArgumentException("Byte boundary must be a power of two");
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Byte boundary must be positive: ");
    stringBuilder.append(paramInt);
    IllegalArgumentException illegalArgumentException = new IllegalArgumentException(stringBuilder.toString());
    throw illegalArgumentException;
  }
  
  protected void boundsCheck(long paramLong1, long paramLong2) {
    if (paramLong1 >= 0L) {
      paramLong1 += paramLong2;
      if (paramLong1 <= this.size)
        return; 
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("Bounds exceeds available space : size=");
      stringBuilder1.append(this.size);
      stringBuilder1.append(", offset=");
      stringBuilder1.append(paramLong1);
      throw new IndexOutOfBoundsException(stringBuilder1.toString());
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Invalid offset: ");
    stringBuilder.append(paramLong1);
    throw new IndexOutOfBoundsException(stringBuilder.toString());
  }
  
  public void clear() {
    clear(this.size);
  }
  
  protected void dispose() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield peer : J
    //   6: invokestatic free : (J)V
    //   9: getstatic com/sun/jna/Memory.allocatedMemory : Ljava/util/Map;
    //   12: aload_0
    //   13: invokeinterface remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   18: pop
    //   19: aload_0
    //   20: lconst_0
    //   21: putfield peer : J
    //   24: aload_0
    //   25: monitorexit
    //   26: return
    //   27: astore_1
    //   28: getstatic com/sun/jna/Memory.allocatedMemory : Ljava/util/Map;
    //   31: aload_0
    //   32: invokeinterface remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   37: pop
    //   38: aload_0
    //   39: lconst_0
    //   40: putfield peer : J
    //   43: aload_1
    //   44: athrow
    //   45: astore_1
    //   46: aload_0
    //   47: monitorexit
    //   48: aload_1
    //   49: athrow
    // Exception table:
    //   from	to	target	type
    //   2	9	27	finally
    //   9	24	45	finally
    //   28	45	45	finally
  }
  
  public String dump() {
    return dump(0L, (int)size());
  }
  
  protected void finalize() {
    dispose();
  }
  
  public byte getByte(long paramLong) {
    boundsCheck(paramLong, 1L);
    return super.getByte(paramLong);
  }
  
  public ByteBuffer getByteBuffer(long paramLong1, long paramLong2) {
    boundsCheck(paramLong1, paramLong2);
    ByteBuffer byteBuffer = super.getByteBuffer(paramLong1, paramLong2);
    buffers.put(byteBuffer, this);
    return byteBuffer;
  }
  
  public char getChar(long paramLong) {
    boundsCheck(paramLong, 1L);
    return super.getChar(paramLong);
  }
  
  public double getDouble(long paramLong) {
    boundsCheck(paramLong, 8L);
    return super.getDouble(paramLong);
  }
  
  public float getFloat(long paramLong) {
    boundsCheck(paramLong, 4L);
    return super.getFloat(paramLong);
  }
  
  public int getInt(long paramLong) {
    boundsCheck(paramLong, 4L);
    return super.getInt(paramLong);
  }
  
  public long getLong(long paramLong) {
    boundsCheck(paramLong, 8L);
    return super.getLong(paramLong);
  }
  
  public Pointer getPointer(long paramLong) {
    boundsCheck(paramLong, Native.POINTER_SIZE);
    return super.getPointer(paramLong);
  }
  
  public short getShort(long paramLong) {
    boundsCheck(paramLong, 2L);
    return super.getShort(paramLong);
  }
  
  public String getString(long paramLong, String paramString) {
    boundsCheck(paramLong, 0L);
    return super.getString(paramLong, paramString);
  }
  
  public String getWideString(long paramLong) {
    boundsCheck(paramLong, 0L);
    return super.getWideString(paramLong);
  }
  
  public void read(long paramLong, byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
    boundsCheck(paramLong, paramInt2 * 1L);
    super.read(paramLong, paramArrayOfbyte, paramInt1, paramInt2);
  }
  
  public void read(long paramLong, char[] paramArrayOfchar, int paramInt1, int paramInt2) {
    boundsCheck(paramLong, paramInt2 * 2L);
    super.read(paramLong, paramArrayOfchar, paramInt1, paramInt2);
  }
  
  public void read(long paramLong, double[] paramArrayOfdouble, int paramInt1, int paramInt2) {
    boundsCheck(paramLong, paramInt2 * 8L);
    super.read(paramLong, paramArrayOfdouble, paramInt1, paramInt2);
  }
  
  public void read(long paramLong, float[] paramArrayOffloat, int paramInt1, int paramInt2) {
    boundsCheck(paramLong, paramInt2 * 4L);
    super.read(paramLong, paramArrayOffloat, paramInt1, paramInt2);
  }
  
  public void read(long paramLong, int[] paramArrayOfint, int paramInt1, int paramInt2) {
    boundsCheck(paramLong, paramInt2 * 4L);
    super.read(paramLong, paramArrayOfint, paramInt1, paramInt2);
  }
  
  public void read(long paramLong, long[] paramArrayOflong, int paramInt1, int paramInt2) {
    boundsCheck(paramLong, paramInt2 * 8L);
    super.read(paramLong, paramArrayOflong, paramInt1, paramInt2);
  }
  
  public void read(long paramLong, short[] paramArrayOfshort, int paramInt1, int paramInt2) {
    boundsCheck(paramLong, paramInt2 * 2L);
    super.read(paramLong, paramArrayOfshort, paramInt1, paramInt2);
  }
  
  public void setByte(long paramLong, byte paramByte) {
    boundsCheck(paramLong, 1L);
    super.setByte(paramLong, paramByte);
  }
  
  public void setChar(long paramLong, char paramChar) {
    boundsCheck(paramLong, Native.WCHAR_SIZE);
    super.setChar(paramLong, paramChar);
  }
  
  public void setDouble(long paramLong, double paramDouble) {
    boundsCheck(paramLong, 8L);
    super.setDouble(paramLong, paramDouble);
  }
  
  public void setFloat(long paramLong, float paramFloat) {
    boundsCheck(paramLong, 4L);
    super.setFloat(paramLong, paramFloat);
  }
  
  public void setInt(long paramLong, int paramInt) {
    boundsCheck(paramLong, 4L);
    super.setInt(paramLong, paramInt);
  }
  
  public void setLong(long paramLong1, long paramLong2) {
    boundsCheck(paramLong1, 8L);
    super.setLong(paramLong1, paramLong2);
  }
  
  public void setPointer(long paramLong, Pointer paramPointer) {
    boundsCheck(paramLong, Native.POINTER_SIZE);
    super.setPointer(paramLong, paramPointer);
  }
  
  public void setShort(long paramLong, short paramShort) {
    boundsCheck(paramLong, 2L);
    super.setShort(paramLong, paramShort);
  }
  
  public void setString(long paramLong, String paramString1, String paramString2) {
    boundsCheck(paramLong, (Native.getBytes(paramString1, paramString2)).length + 1L);
    super.setString(paramLong, paramString1, paramString2);
  }
  
  public void setWideString(long paramLong, String paramString) {
    boundsCheck(paramLong, (paramString.length() + 1L) * Native.WCHAR_SIZE);
    super.setWideString(paramLong, paramString);
  }
  
  public Pointer share(long paramLong) {
    return share(paramLong, size() - paramLong);
  }
  
  public Pointer share(long paramLong1, long paramLong2) {
    boundsCheck(paramLong1, paramLong2);
    return new SharedMemory(paramLong1, paramLong2);
  }
  
  public long size() {
    return this.size;
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("allocated@0x");
    stringBuilder.append(Long.toHexString(this.peer));
    stringBuilder.append(" (");
    stringBuilder.append(this.size);
    stringBuilder.append(" bytes)");
    return stringBuilder.toString();
  }
  
  public boolean valid() {
    boolean bool;
    if (this.peer != 0L) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void write(long paramLong, byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
    boundsCheck(paramLong, paramInt2 * 1L);
    super.write(paramLong, paramArrayOfbyte, paramInt1, paramInt2);
  }
  
  public void write(long paramLong, char[] paramArrayOfchar, int paramInt1, int paramInt2) {
    boundsCheck(paramLong, paramInt2 * 2L);
    super.write(paramLong, paramArrayOfchar, paramInt1, paramInt2);
  }
  
  public void write(long paramLong, double[] paramArrayOfdouble, int paramInt1, int paramInt2) {
    boundsCheck(paramLong, paramInt2 * 8L);
    super.write(paramLong, paramArrayOfdouble, paramInt1, paramInt2);
  }
  
  public void write(long paramLong, float[] paramArrayOffloat, int paramInt1, int paramInt2) {
    boundsCheck(paramLong, paramInt2 * 4L);
    super.write(paramLong, paramArrayOffloat, paramInt1, paramInt2);
  }
  
  public void write(long paramLong, int[] paramArrayOfint, int paramInt1, int paramInt2) {
    boundsCheck(paramLong, paramInt2 * 4L);
    super.write(paramLong, paramArrayOfint, paramInt1, paramInt2);
  }
  
  public void write(long paramLong, long[] paramArrayOflong, int paramInt1, int paramInt2) {
    boundsCheck(paramLong, paramInt2 * 8L);
    super.write(paramLong, paramArrayOflong, paramInt1, paramInt2);
  }
  
  public void write(long paramLong, short[] paramArrayOfshort, int paramInt1, int paramInt2) {
    boundsCheck(paramLong, paramInt2 * 2L);
    super.write(paramLong, paramArrayOfshort, paramInt1, paramInt2);
  }
  
  private class SharedMemory extends Memory {
    public SharedMemory(long param1Long1, long param1Long2) {
      this.size = param1Long2;
      this.peer = Memory.this.peer + param1Long1;
    }
    
    protected void boundsCheck(long param1Long1, long param1Long2) {
      Memory.this.boundsCheck(this.peer - Memory.this.peer + param1Long1, param1Long2);
    }
    
    protected void dispose() {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: lconst_0
      //   4: putfield peer : J
      //   7: aload_0
      //   8: monitorexit
      //   9: return
      //   10: astore_1
      //   11: aload_0
      //   12: monitorexit
      //   13: aload_1
      //   14: athrow
      // Exception table:
      //   from	to	target	type
      //   2	7	10	finally
    }
    
    public String toString() {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(super.toString());
      stringBuilder.append(" (shared from ");
      stringBuilder.append(Memory.this.toString());
      stringBuilder.append(")");
      return stringBuilder.toString();
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/sun/jna/Memory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */