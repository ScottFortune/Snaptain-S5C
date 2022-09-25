package com.bumptech.glide.load.resource.bitmap;

import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class RecyclableBufferedInputStream extends FilterInputStream {
  private volatile byte[] buf;
  
  private final ArrayPool byteArrayPool;
  
  private int count;
  
  private int marklimit;
  
  private int markpos = -1;
  
  private int pos;
  
  public RecyclableBufferedInputStream(InputStream paramInputStream, ArrayPool paramArrayPool) {
    this(paramInputStream, paramArrayPool, 65536);
  }
  
  RecyclableBufferedInputStream(InputStream paramInputStream, ArrayPool paramArrayPool, int paramInt) {
    super(paramInputStream);
    this.byteArrayPool = paramArrayPool;
    this.buf = (byte[])paramArrayPool.get(paramInt, byte[].class);
  }
  
  private int fillbuf(InputStream paramInputStream, byte[] paramArrayOfbyte) throws IOException {
    int i = this.markpos;
    if (i != -1) {
      int j = this.pos;
      int k = this.marklimit;
      if (j - i < k) {
        byte[] arrayOfByte;
        if (i == 0 && k > paramArrayOfbyte.length && this.count == paramArrayOfbyte.length) {
          j = paramArrayOfbyte.length * 2;
          i = j;
          if (j > k)
            i = k; 
          arrayOfByte = (byte[])this.byteArrayPool.get(i, byte[].class);
          System.arraycopy(paramArrayOfbyte, 0, arrayOfByte, 0, paramArrayOfbyte.length);
          this.buf = arrayOfByte;
          this.byteArrayPool.put(paramArrayOfbyte);
        } else {
          i = this.markpos;
          arrayOfByte = paramArrayOfbyte;
          if (i > 0) {
            System.arraycopy(paramArrayOfbyte, i, paramArrayOfbyte, 0, paramArrayOfbyte.length - i);
            arrayOfByte = paramArrayOfbyte;
          } 
        } 
        this.pos -= this.markpos;
        this.markpos = 0;
        this.count = 0;
        i = this.pos;
        k = paramInputStream.read(arrayOfByte, i, arrayOfByte.length - i);
        i = this.pos;
        if (k > 0)
          i += k; 
        this.count = i;
        return k;
      } 
    } 
    i = paramInputStream.read(paramArrayOfbyte);
    if (i > 0) {
      this.markpos = -1;
      this.pos = 0;
      this.count = i;
    } 
    return i;
  }
  
  private static IOException streamClosed() throws IOException {
    throw new IOException("BufferedInputStream is closed");
  }
  
  public int available() throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield in : Ljava/io/InputStream;
    //   6: astore_1
    //   7: aload_0
    //   8: getfield buf : [B
    //   11: ifnull -> 43
    //   14: aload_1
    //   15: ifnull -> 43
    //   18: aload_0
    //   19: getfield count : I
    //   22: istore_2
    //   23: aload_0
    //   24: getfield pos : I
    //   27: istore_3
    //   28: aload_1
    //   29: invokevirtual available : ()I
    //   32: istore #4
    //   34: aload_0
    //   35: monitorexit
    //   36: iload_2
    //   37: iload_3
    //   38: isub
    //   39: iload #4
    //   41: iadd
    //   42: ireturn
    //   43: invokestatic streamClosed : ()Ljava/io/IOException;
    //   46: athrow
    //   47: astore_1
    //   48: aload_0
    //   49: monitorexit
    //   50: aload_1
    //   51: athrow
    // Exception table:
    //   from	to	target	type
    //   2	14	47	finally
    //   18	34	47	finally
    //   43	47	47	finally
  }
  
  public void close() throws IOException {
    if (this.buf != null) {
      this.byteArrayPool.put(this.buf);
      this.buf = null;
    } 
    InputStream inputStream = this.in;
    this.in = null;
    if (inputStream != null)
      inputStream.close(); 
  }
  
  public void fixMarkLimit() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_0
    //   4: getfield buf : [B
    //   7: arraylength
    //   8: putfield marklimit : I
    //   11: aload_0
    //   12: monitorexit
    //   13: return
    //   14: astore_1
    //   15: aload_0
    //   16: monitorexit
    //   17: aload_1
    //   18: athrow
    // Exception table:
    //   from	to	target	type
    //   2	11	14	finally
  }
  
  public void mark(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_0
    //   4: getfield marklimit : I
    //   7: iload_1
    //   8: invokestatic max : (II)I
    //   11: putfield marklimit : I
    //   14: aload_0
    //   15: aload_0
    //   16: getfield pos : I
    //   19: putfield markpos : I
    //   22: aload_0
    //   23: monitorexit
    //   24: return
    //   25: astore_2
    //   26: aload_0
    //   27: monitorexit
    //   28: aload_2
    //   29: athrow
    // Exception table:
    //   from	to	target	type
    //   2	22	25	finally
  }
  
  public boolean markSupported() {
    return true;
  }
  
  public int read() throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield buf : [B
    //   6: astore_1
    //   7: aload_0
    //   8: getfield in : Ljava/io/InputStream;
    //   11: astore_2
    //   12: aload_1
    //   13: ifnull -> 113
    //   16: aload_2
    //   17: ifnull -> 113
    //   20: aload_0
    //   21: getfield pos : I
    //   24: aload_0
    //   25: getfield count : I
    //   28: if_icmplt -> 47
    //   31: aload_0
    //   32: aload_2
    //   33: aload_1
    //   34: invokespecial fillbuf : (Ljava/io/InputStream;[B)I
    //   37: istore_3
    //   38: iload_3
    //   39: iconst_m1
    //   40: if_icmpne -> 47
    //   43: aload_0
    //   44: monitorexit
    //   45: iconst_m1
    //   46: ireturn
    //   47: aload_1
    //   48: astore_2
    //   49: aload_1
    //   50: aload_0
    //   51: getfield buf : [B
    //   54: if_acmpeq -> 73
    //   57: aload_0
    //   58: getfield buf : [B
    //   61: astore_2
    //   62: aload_2
    //   63: ifnull -> 69
    //   66: goto -> 73
    //   69: invokestatic streamClosed : ()Ljava/io/IOException;
    //   72: athrow
    //   73: aload_0
    //   74: getfield count : I
    //   77: aload_0
    //   78: getfield pos : I
    //   81: isub
    //   82: ifle -> 109
    //   85: aload_0
    //   86: getfield pos : I
    //   89: istore_3
    //   90: aload_0
    //   91: iload_3
    //   92: iconst_1
    //   93: iadd
    //   94: putfield pos : I
    //   97: aload_2
    //   98: iload_3
    //   99: baload
    //   100: istore_3
    //   101: aload_0
    //   102: monitorexit
    //   103: iload_3
    //   104: sipush #255
    //   107: iand
    //   108: ireturn
    //   109: aload_0
    //   110: monitorexit
    //   111: iconst_m1
    //   112: ireturn
    //   113: invokestatic streamClosed : ()Ljava/io/IOException;
    //   116: athrow
    //   117: astore_2
    //   118: aload_0
    //   119: monitorexit
    //   120: aload_2
    //   121: athrow
    // Exception table:
    //   from	to	target	type
    //   2	12	117	finally
    //   20	38	117	finally
    //   49	62	117	finally
    //   69	73	117	finally
    //   73	97	117	finally
    //   113	117	117	finally
  }
  
  public int read(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield buf : [B
    //   6: astore #4
    //   8: aload #4
    //   10: ifnull -> 384
    //   13: iload_3
    //   14: ifne -> 21
    //   17: aload_0
    //   18: monitorexit
    //   19: iconst_0
    //   20: ireturn
    //   21: aload_0
    //   22: getfield in : Ljava/io/InputStream;
    //   25: astore #5
    //   27: aload #5
    //   29: ifnull -> 380
    //   32: aload_0
    //   33: getfield pos : I
    //   36: aload_0
    //   37: getfield count : I
    //   40: if_icmpge -> 141
    //   43: aload_0
    //   44: getfield count : I
    //   47: aload_0
    //   48: getfield pos : I
    //   51: isub
    //   52: iload_3
    //   53: if_icmplt -> 62
    //   56: iload_3
    //   57: istore #6
    //   59: goto -> 73
    //   62: aload_0
    //   63: getfield count : I
    //   66: aload_0
    //   67: getfield pos : I
    //   70: isub
    //   71: istore #6
    //   73: aload #4
    //   75: aload_0
    //   76: getfield pos : I
    //   79: aload_1
    //   80: iload_2
    //   81: iload #6
    //   83: invokestatic arraycopy : (Ljava/lang/Object;ILjava/lang/Object;II)V
    //   86: aload_0
    //   87: aload_0
    //   88: getfield pos : I
    //   91: iload #6
    //   93: iadd
    //   94: putfield pos : I
    //   97: iload #6
    //   99: iload_3
    //   100: if_icmpeq -> 136
    //   103: aload #5
    //   105: invokevirtual available : ()I
    //   108: istore #7
    //   110: iload #7
    //   112: ifne -> 118
    //   115: goto -> 136
    //   118: iload_2
    //   119: iload #6
    //   121: iadd
    //   122: istore #7
    //   124: iload_3
    //   125: iload #6
    //   127: isub
    //   128: istore_2
    //   129: iload #7
    //   131: istore #6
    //   133: goto -> 150
    //   136: aload_0
    //   137: monitorexit
    //   138: iload #6
    //   140: ireturn
    //   141: iload_3
    //   142: istore #7
    //   144: iload_2
    //   145: istore #6
    //   147: iload #7
    //   149: istore_2
    //   150: aload_0
    //   151: getfield markpos : I
    //   154: istore #7
    //   156: iconst_m1
    //   157: istore #8
    //   159: iload #7
    //   161: iconst_m1
    //   162: if_icmpne -> 215
    //   165: iload_2
    //   166: aload #4
    //   168: arraylength
    //   169: if_icmplt -> 215
    //   172: aload #5
    //   174: aload_1
    //   175: iload #6
    //   177: iload_2
    //   178: invokevirtual read : ([BII)I
    //   181: istore #9
    //   183: aload #4
    //   185: astore #10
    //   187: iload #9
    //   189: istore #7
    //   191: iload #9
    //   193: iconst_m1
    //   194: if_icmpne -> 335
    //   197: iload_2
    //   198: iload_3
    //   199: if_icmpne -> 205
    //   202: goto -> 210
    //   205: iload_3
    //   206: iload_2
    //   207: isub
    //   208: istore #8
    //   210: aload_0
    //   211: monitorexit
    //   212: iload #8
    //   214: ireturn
    //   215: aload_0
    //   216: aload #5
    //   218: aload #4
    //   220: invokespecial fillbuf : (Ljava/io/InputStream;[B)I
    //   223: istore #7
    //   225: iload #7
    //   227: iconst_m1
    //   228: if_icmpne -> 249
    //   231: iload_2
    //   232: iload_3
    //   233: if_icmpne -> 239
    //   236: goto -> 244
    //   239: iload_3
    //   240: iload_2
    //   241: isub
    //   242: istore #8
    //   244: aload_0
    //   245: monitorexit
    //   246: iload #8
    //   248: ireturn
    //   249: aload #4
    //   251: astore #10
    //   253: aload #4
    //   255: aload_0
    //   256: getfield buf : [B
    //   259: if_acmpeq -> 280
    //   262: aload_0
    //   263: getfield buf : [B
    //   266: astore #10
    //   268: aload #10
    //   270: ifnull -> 276
    //   273: goto -> 280
    //   276: invokestatic streamClosed : ()Ljava/io/IOException;
    //   279: athrow
    //   280: aload_0
    //   281: getfield count : I
    //   284: aload_0
    //   285: getfield pos : I
    //   288: isub
    //   289: iload_2
    //   290: if_icmplt -> 299
    //   293: iload_2
    //   294: istore #7
    //   296: goto -> 310
    //   299: aload_0
    //   300: getfield count : I
    //   303: aload_0
    //   304: getfield pos : I
    //   307: isub
    //   308: istore #7
    //   310: aload #10
    //   312: aload_0
    //   313: getfield pos : I
    //   316: aload_1
    //   317: iload #6
    //   319: iload #7
    //   321: invokestatic arraycopy : (Ljava/lang/Object;ILjava/lang/Object;II)V
    //   324: aload_0
    //   325: aload_0
    //   326: getfield pos : I
    //   329: iload #7
    //   331: iadd
    //   332: putfield pos : I
    //   335: iload_2
    //   336: iload #7
    //   338: isub
    //   339: istore_2
    //   340: iload_2
    //   341: ifne -> 348
    //   344: aload_0
    //   345: monitorexit
    //   346: iload_3
    //   347: ireturn
    //   348: aload #5
    //   350: invokevirtual available : ()I
    //   353: istore #8
    //   355: iload #8
    //   357: ifne -> 366
    //   360: aload_0
    //   361: monitorexit
    //   362: iload_3
    //   363: iload_2
    //   364: isub
    //   365: ireturn
    //   366: iload #6
    //   368: iload #7
    //   370: iadd
    //   371: istore #6
    //   373: aload #10
    //   375: astore #4
    //   377: goto -> 150
    //   380: invokestatic streamClosed : ()Ljava/io/IOException;
    //   383: athrow
    //   384: invokestatic streamClosed : ()Ljava/io/IOException;
    //   387: athrow
    //   388: astore_1
    //   389: aload_0
    //   390: monitorexit
    //   391: goto -> 396
    //   394: aload_1
    //   395: athrow
    //   396: goto -> 394
    // Exception table:
    //   from	to	target	type
    //   2	8	388	finally
    //   21	27	388	finally
    //   32	56	388	finally
    //   62	73	388	finally
    //   73	97	388	finally
    //   103	110	388	finally
    //   150	156	388	finally
    //   165	183	388	finally
    //   215	225	388	finally
    //   253	268	388	finally
    //   276	280	388	finally
    //   280	293	388	finally
    //   299	310	388	finally
    //   310	335	388	finally
    //   348	355	388	finally
    //   380	384	388	finally
    //   384	388	388	finally
  }
  
  public void release() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield buf : [B
    //   6: ifnull -> 27
    //   9: aload_0
    //   10: getfield byteArrayPool : Lcom/bumptech/glide/load/engine/bitmap_recycle/ArrayPool;
    //   13: aload_0
    //   14: getfield buf : [B
    //   17: invokeinterface put : (Ljava/lang/Object;)V
    //   22: aload_0
    //   23: aconst_null
    //   24: putfield buf : [B
    //   27: aload_0
    //   28: monitorexit
    //   29: return
    //   30: astore_1
    //   31: aload_0
    //   32: monitorexit
    //   33: aload_1
    //   34: athrow
    // Exception table:
    //   from	to	target	type
    //   2	27	30	finally
  }
  
  public void reset() throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield buf : [B
    //   6: ifnull -> 82
    //   9: iconst_m1
    //   10: aload_0
    //   11: getfield markpos : I
    //   14: if_icmpeq -> 28
    //   17: aload_0
    //   18: aload_0
    //   19: getfield markpos : I
    //   22: putfield pos : I
    //   25: aload_0
    //   26: monitorexit
    //   27: return
    //   28: new com/bumptech/glide/load/resource/bitmap/RecyclableBufferedInputStream$InvalidMarkException
    //   31: astore_1
    //   32: new java/lang/StringBuilder
    //   35: astore_2
    //   36: aload_2
    //   37: invokespecial <init> : ()V
    //   40: aload_2
    //   41: ldc 'Mark has been invalidated, pos: '
    //   43: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   46: pop
    //   47: aload_2
    //   48: aload_0
    //   49: getfield pos : I
    //   52: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   55: pop
    //   56: aload_2
    //   57: ldc ' markLimit: '
    //   59: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   62: pop
    //   63: aload_2
    //   64: aload_0
    //   65: getfield marklimit : I
    //   68: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   71: pop
    //   72: aload_1
    //   73: aload_2
    //   74: invokevirtual toString : ()Ljava/lang/String;
    //   77: invokespecial <init> : (Ljava/lang/String;)V
    //   80: aload_1
    //   81: athrow
    //   82: new java/io/IOException
    //   85: astore_2
    //   86: aload_2
    //   87: ldc 'Stream is closed'
    //   89: invokespecial <init> : (Ljava/lang/String;)V
    //   92: aload_2
    //   93: athrow
    //   94: astore_2
    //   95: aload_0
    //   96: monitorexit
    //   97: aload_2
    //   98: athrow
    // Exception table:
    //   from	to	target	type
    //   2	25	94	finally
    //   28	82	94	finally
    //   82	94	94	finally
  }
  
  public long skip(long paramLong) throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: lload_1
    //   3: lconst_1
    //   4: lcmp
    //   5: ifge -> 12
    //   8: aload_0
    //   9: monitorexit
    //   10: lconst_0
    //   11: lreturn
    //   12: aload_0
    //   13: getfield buf : [B
    //   16: astore_3
    //   17: aload_3
    //   18: ifnull -> 211
    //   21: aload_0
    //   22: getfield in : Ljava/io/InputStream;
    //   25: astore #4
    //   27: aload #4
    //   29: ifnull -> 207
    //   32: aload_0
    //   33: getfield count : I
    //   36: aload_0
    //   37: getfield pos : I
    //   40: isub
    //   41: i2l
    //   42: lload_1
    //   43: lcmp
    //   44: iflt -> 63
    //   47: aload_0
    //   48: aload_0
    //   49: getfield pos : I
    //   52: i2l
    //   53: lload_1
    //   54: ladd
    //   55: l2i
    //   56: putfield pos : I
    //   59: aload_0
    //   60: monitorexit
    //   61: lload_1
    //   62: lreturn
    //   63: aload_0
    //   64: getfield count : I
    //   67: i2l
    //   68: aload_0
    //   69: getfield pos : I
    //   72: i2l
    //   73: lsub
    //   74: lstore #5
    //   76: aload_0
    //   77: aload_0
    //   78: getfield count : I
    //   81: putfield pos : I
    //   84: aload_0
    //   85: getfield markpos : I
    //   88: iconst_m1
    //   89: if_icmpeq -> 190
    //   92: lload_1
    //   93: aload_0
    //   94: getfield marklimit : I
    //   97: i2l
    //   98: lcmp
    //   99: ifgt -> 190
    //   102: aload_0
    //   103: aload #4
    //   105: aload_3
    //   106: invokespecial fillbuf : (Ljava/io/InputStream;[B)I
    //   109: istore #7
    //   111: iload #7
    //   113: iconst_m1
    //   114: if_icmpne -> 122
    //   117: aload_0
    //   118: monitorexit
    //   119: lload #5
    //   121: lreturn
    //   122: aload_0
    //   123: getfield count : I
    //   126: aload_0
    //   127: getfield pos : I
    //   130: isub
    //   131: i2l
    //   132: lload_1
    //   133: lload #5
    //   135: lsub
    //   136: lcmp
    //   137: iflt -> 159
    //   140: aload_0
    //   141: aload_0
    //   142: getfield pos : I
    //   145: i2l
    //   146: lload_1
    //   147: ladd
    //   148: lload #5
    //   150: lsub
    //   151: l2i
    //   152: putfield pos : I
    //   155: aload_0
    //   156: monitorexit
    //   157: lload_1
    //   158: lreturn
    //   159: aload_0
    //   160: getfield count : I
    //   163: i2l
    //   164: lstore #8
    //   166: aload_0
    //   167: getfield pos : I
    //   170: i2l
    //   171: lstore_1
    //   172: aload_0
    //   173: aload_0
    //   174: getfield count : I
    //   177: putfield pos : I
    //   180: aload_0
    //   181: monitorexit
    //   182: lload #5
    //   184: lload #8
    //   186: ladd
    //   187: lload_1
    //   188: lsub
    //   189: lreturn
    //   190: aload #4
    //   192: lload_1
    //   193: lload #5
    //   195: lsub
    //   196: invokevirtual skip : (J)J
    //   199: lstore_1
    //   200: aload_0
    //   201: monitorexit
    //   202: lload #5
    //   204: lload_1
    //   205: ladd
    //   206: lreturn
    //   207: invokestatic streamClosed : ()Ljava/io/IOException;
    //   210: athrow
    //   211: invokestatic streamClosed : ()Ljava/io/IOException;
    //   214: athrow
    //   215: astore_3
    //   216: aload_0
    //   217: monitorexit
    //   218: aload_3
    //   219: athrow
    // Exception table:
    //   from	to	target	type
    //   12	17	215	finally
    //   21	27	215	finally
    //   32	59	215	finally
    //   63	111	215	finally
    //   122	155	215	finally
    //   159	180	215	finally
    //   190	200	215	finally
    //   207	211	215	finally
    //   211	215	215	finally
  }
  
  static class InvalidMarkException extends IOException {
    private static final long serialVersionUID = -4338378848813561757L;
    
    InvalidMarkException(String param1String) {
      super(param1String);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/resource/bitmap/RecyclableBufferedInputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */