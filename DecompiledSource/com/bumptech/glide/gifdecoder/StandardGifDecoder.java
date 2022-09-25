package com.bumptech.glide.gifdecoder;

import android.graphics.Bitmap;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class StandardGifDecoder implements GifDecoder {
  private static final int BYTES_PER_INTEGER = 4;
  
  private static final int COLOR_TRANSPARENT_BLACK = 0;
  
  private static final int INITIAL_FRAME_POINTER = -1;
  
  private static final int MASK_INT_LOWEST_BYTE = 255;
  
  private static final int MAX_STACK_SIZE = 4096;
  
  private static final int NULL_CODE = -1;
  
  private static final String TAG = StandardGifDecoder.class.getSimpleName();
  
  private int[] act;
  
  private Bitmap.Config bitmapConfig = Bitmap.Config.ARGB_8888;
  
  private final GifDecoder.BitmapProvider bitmapProvider;
  
  private byte[] block;
  
  private int downsampledHeight;
  
  private int downsampledWidth;
  
  private int framePointer;
  
  private GifHeader header;
  
  private Boolean isFirstFrameTransparent;
  
  private byte[] mainPixels;
  
  private int[] mainScratch;
  
  private GifHeaderParser parser;
  
  private final int[] pct = new int[256];
  
  private byte[] pixelStack;
  
  private short[] prefix;
  
  private Bitmap previousImage;
  
  private ByteBuffer rawData;
  
  private int sampleSize;
  
  private boolean savePrevious;
  
  private int status;
  
  private byte[] suffix;
  
  public StandardGifDecoder(GifDecoder.BitmapProvider paramBitmapProvider) {
    this.bitmapProvider = paramBitmapProvider;
    this.header = new GifHeader();
  }
  
  public StandardGifDecoder(GifDecoder.BitmapProvider paramBitmapProvider, GifHeader paramGifHeader, ByteBuffer paramByteBuffer) {
    this(paramBitmapProvider, paramGifHeader, paramByteBuffer, 1);
  }
  
  public StandardGifDecoder(GifDecoder.BitmapProvider paramBitmapProvider, GifHeader paramGifHeader, ByteBuffer paramByteBuffer, int paramInt) {
    this(paramBitmapProvider);
    setData(paramGifHeader, paramByteBuffer, paramInt);
  }
  
  private int averageColorsNear(int paramInt1, int paramInt2, int paramInt3) {
    int i1;
    int i = paramInt1;
    int j = 0;
    int k = 0;
    int m = 0;
    int n = 0;
    int i2 = 0;
    while (i < this.sampleSize + paramInt1) {
      byte[] arrayOfByte = this.mainPixels;
      if (i < arrayOfByte.length && i < paramInt2) {
        int i5 = arrayOfByte[i];
        int i6 = this.act[i5 & 0xFF];
        int i7 = j;
        int i8 = k;
        int i9 = m;
        int i10 = n;
        i5 = i2;
        if (i6 != 0) {
          i7 = j + (i6 >> 24 & 0xFF);
          i8 = k + (i6 >> 16 & 0xFF);
          i9 = m + (i6 >> 8 & 0xFF);
          i10 = n + (i6 & 0xFF);
          i5 = i2 + 1;
        } 
        i++;
        j = i7;
        k = i8;
        m = i9;
        n = i10;
        i2 = i5;
      } 
    } 
    int i4 = paramInt1 + paramInt3;
    paramInt1 = i4;
    i = k;
    int i3 = j;
    while (paramInt1 < this.sampleSize + i4) {
      byte[] arrayOfByte = this.mainPixels;
      if (paramInt1 < arrayOfByte.length && paramInt1 < paramInt2) {
        int i5;
        paramInt3 = arrayOfByte[paramInt1];
        int i7 = this.act[paramInt3 & 0xFF];
        int i8 = i3;
        int i9 = i;
        j = m;
        k = n;
        int i6 = i2;
        if (i7 != 0) {
          i8 = i3 + (i7 >> 24 & 0xFF);
          i9 = i + (i7 >> 16 & 0xFF);
          j = m + (i7 >> 8 & 0xFF);
          k = n + (i7 & 0xFF);
          i5 = i2 + 1;
        } 
        paramInt1++;
        i3 = i8;
        i = i9;
        m = j;
        n = k;
        i1 = i5;
      } 
    } 
    return (i1 == 0) ? 0 : (i3 / i1 << 24 | i / i1 << 16 | m / i1 << 8 | n / i1);
  }
  
  private void copyCopyIntoScratchRobust(GifFrame paramGifFrame) {
    boolean bool;
    int[] arrayOfInt1 = this.mainScratch;
    int i = paramGifFrame.ih / this.sampleSize;
    int j = paramGifFrame.iy / this.sampleSize;
    int k = paramGifFrame.iw / this.sampleSize;
    int m = paramGifFrame.ix / this.sampleSize;
    int n = this.framePointer;
    Boolean bool1 = Boolean.valueOf(true);
    if (n == 0) {
      bool = true;
    } else {
      bool = false;
    } 
    int i1 = this.sampleSize;
    int i2 = this.downsampledWidth;
    int i3 = this.downsampledHeight;
    byte[] arrayOfByte = this.mainPixels;
    int[] arrayOfInt2 = this.act;
    Boolean bool2 = this.isFirstFrameTransparent;
    byte b = 0;
    n = 0;
    int i4 = 1;
    int i5;
    for (i5 = 8;; i5 = i6) {
      int i6;
      int i7;
      if (b < i) {
        int i8;
        Boolean bool3;
        if (paramGifFrame.interlace) {
          if (n >= i)
            if (++i4 != 2) {
              if (i4 != 3) {
                if (i4 == 4) {
                  n = 1;
                  i5 = 2;
                } 
              } else {
                n = 2;
                i5 = 4;
              } 
            } else {
              n = 4;
            }  
          i6 = n + i5;
          i8 = n;
          n = i6;
          i7 = i4;
          i6 = i5;
        } else {
          i8 = b;
          i6 = i5;
          i7 = i4;
        } 
        i5 = i8 + j;
        if (i1 == 1) {
          i4 = 1;
        } else {
          i4 = 0;
        } 
        if (i5 < i3) {
          i5 *= i2;
          i8 = i5 + m;
          int i9 = i8 + k;
          int i10 = i5 + i2;
          i5 = i9;
          if (i10 < i9)
            i5 = i10; 
          i9 = b * i1 * paramGifFrame.iw;
          if (i4 != 0) {
            while (true) {
              i4 = k;
              bool3 = bool2;
              if (i8 < i5) {
                i4 = arrayOfInt2[arrayOfByte[i9] & 0xFF];
                if (i4 != 0) {
                  arrayOfInt1[i8] = i4;
                  bool3 = bool2;
                } else {
                  bool3 = bool2;
                  if (bool) {
                    bool3 = bool2;
                    if (bool2 == null)
                      bool3 = bool1; 
                  } 
                } 
                i9 += i1;
                i8++;
                bool2 = bool3;
                continue;
              } 
              break;
            } 
          } else {
            i10 = k;
            int i11 = i8;
            bool3 = bool2;
            i4 = i9;
            k = i5;
            while (true) {
              int i12 = i4;
              i4 = k;
              bool2 = bool3;
              k = i10;
              if (i11 < i4) {
                k = averageColorsNear(i12, (i5 - i8) * i1 + i9, paramGifFrame.iw);
                if (k != 0) {
                  arrayOfInt1[i11] = k;
                  bool2 = bool3;
                } else {
                  bool2 = bool3;
                  if (bool) {
                    bool2 = bool3;
                    if (bool3 == null)
                      bool2 = bool1; 
                  } 
                } 
                i12 += i1;
                i11++;
                k = i4;
                i4 = i12;
                bool3 = bool2;
                continue;
              } 
              break;
            } 
            b++;
            i4 = i7;
            i5 = i6;
          } 
        } else {
          bool3 = bool2;
          i4 = k;
        } 
        k = i4;
        bool2 = bool3;
      } else {
        break;
      } 
      b++;
      i4 = i7;
    } 
    if (this.isFirstFrameTransparent == null) {
      boolean bool3;
      if (bool2 == null) {
        bool3 = false;
      } else {
        bool3 = bool2.booleanValue();
      } 
      this.isFirstFrameTransparent = Boolean.valueOf(bool3);
    } 
  }
  
  private void copyIntoScratchFast(GifFrame paramGifFrame) {
    boolean bool1;
    boolean bool2;
    GifFrame gifFrame = paramGifFrame;
    int[] arrayOfInt2 = this.mainScratch;
    int i = gifFrame.ih;
    int j = gifFrame.iy;
    int k = gifFrame.iw;
    int m = gifFrame.ix;
    if (this.framePointer == 0) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    int n = this.downsampledWidth;
    byte[] arrayOfByte = this.mainPixels;
    int[] arrayOfInt1 = this.act;
    byte b = 0;
    int i1 = -1;
    while (b < i) {
      int i2 = (b + j) * n;
      int i3 = i2 + m;
      int i4 = i3 + k;
      int i5 = i2 + n;
      i2 = i4;
      if (i5 < i4)
        i2 = i5; 
      i4 = paramGifFrame.iw * b;
      while (i3 < i2) {
        byte b1 = arrayOfByte[i4];
        int i6 = b1 & 0xFF;
        i5 = i1;
        if (i6 != i1) {
          i5 = arrayOfInt1[i6];
          if (i5 != 0) {
            arrayOfInt2[i3] = i5;
            i5 = i1;
          } else {
            i5 = b1;
          } 
        } 
        i4++;
        i3++;
        i1 = i5;
      } 
      b++;
    } 
    Boolean bool = this.isFirstFrameTransparent;
    if ((bool != null && bool.booleanValue()) || (this.isFirstFrameTransparent == null && bool1 && i1 != -1)) {
      bool2 = true;
    } else {
      bool2 = false;
    } 
    this.isFirstFrameTransparent = Boolean.valueOf(bool2);
  }
  
  private void decodeBitmapData(GifFrame paramGifFrame) {
    int i;
    StandardGifDecoder standardGifDecoder2 = this;
    if (paramGifFrame != null)
      standardGifDecoder2.rawData.position(paramGifFrame.bufferFrameStart); 
    if (paramGifFrame == null) {
      i = standardGifDecoder2.header.width * standardGifDecoder2.header.height;
    } else {
      int i14 = paramGifFrame.iw;
      i = paramGifFrame.ih * i14;
    } 
    byte[] arrayOfByte1 = standardGifDecoder2.mainPixels;
    if (arrayOfByte1 == null || arrayOfByte1.length < i)
      standardGifDecoder2.mainPixels = standardGifDecoder2.bitmapProvider.obtainByteArray(i); 
    byte[] arrayOfByte2 = standardGifDecoder2.mainPixels;
    if (standardGifDecoder2.prefix == null)
      standardGifDecoder2.prefix = new short[4096]; 
    short[] arrayOfShort = standardGifDecoder2.prefix;
    if (standardGifDecoder2.suffix == null)
      standardGifDecoder2.suffix = new byte[4096]; 
    byte[] arrayOfByte3 = standardGifDecoder2.suffix;
    if (standardGifDecoder2.pixelStack == null)
      standardGifDecoder2.pixelStack = new byte[4097]; 
    byte[] arrayOfByte4 = standardGifDecoder2.pixelStack;
    int j = readByte();
    int k = 1 << j;
    int m = k + 2;
    int n = j + 1;
    int i1 = (1 << n) - 1;
    int i2 = 0;
    for (j = 0; j < k; j++) {
      arrayOfShort[j] = (short)0;
      arrayOfByte3[j] = (byte)(byte)j;
    } 
    byte[] arrayOfByte5 = standardGifDecoder2.block;
    int i3 = n;
    int i4 = m;
    int i5 = i1;
    int i6 = 0;
    int i7 = 0;
    int i8 = 0;
    int i9 = 0;
    int i10 = 0;
    int i11 = -1;
    int i12 = 0;
    int i13 = 0;
    j = i2;
    StandardGifDecoder standardGifDecoder1 = standardGifDecoder2;
    label77: while (j < i) {
      i2 = i6;
      if (!i6) {
        i2 = readBlock();
        if (i2 <= 0) {
          standardGifDecoder1.status = 3;
          break;
        } 
        i9 = 0;
      } 
      i8 += (arrayOfByte5[i9] & 0xFF) << i7;
      int i14 = i9 + 1;
      int i15 = i2 - 1;
      i9 = i7 + 8;
      i7 = i11;
      i6 = i4;
      i11 = i3;
      i4 = i12;
      i12 = i6;
      i3 = i10;
      i10 = j;
      j = i7;
      i6 = n;
      i7 = i9;
      while (i7 >= i11) {
        i9 = i8 & i5;
        i8 >>= i11;
        i7 -= i11;
        if (i9 == k) {
          i11 = i6;
          i12 = m;
          i5 = i1;
          j = -1;
          continue;
        } 
        if (i9 == k + 1) {
          i2 = i11;
          i9 = i10;
          i10 = i3;
          i3 = i12;
          i12 = i4;
          i11 = j;
          n = i6;
          j = i9;
          i6 = i15;
          i9 = i14;
          i4 = i3;
          i3 = i2;
          continue label77;
        } 
        if (j == -1) {
          arrayOfByte2[i3] = (byte)arrayOfByte3[i9];
          i3++;
          i10++;
          standardGifDecoder1 = this;
          j = i9;
          i4 = j;
          continue;
        } 
        n = i12;
        if (i9 >= n) {
          arrayOfByte4[i13] = (byte)(byte)i4;
          i12 = i13 + 1;
          i4 = j;
        } else {
          i4 = i9;
          i12 = i13;
        } 
        while (i4 >= k) {
          arrayOfByte4[i12] = (byte)arrayOfByte3[i4];
          i12++;
          i4 = arrayOfShort[i4];
        } 
        int i16 = arrayOfByte3[i4] & 0xFF;
        byte b = (byte)i16;
        arrayOfByte2[i3] = (byte)b;
        while (true) {
          i3++;
          i13 = i10 + 1;
          if (i12 > 0) {
            arrayOfByte2[i3] = (byte)arrayOfByte4[--i12];
            i10 = i13;
            continue;
          } 
          i10 = n;
          i2 = i11;
          i4 = i5;
          if (n < 4096) {
            arrayOfShort[n] = (short)(short)j;
            arrayOfByte3[n] = (byte)b;
            j = n + 1;
            i10 = j;
            i2 = i11;
            i4 = i5;
            if ((j & i5) == 0) {
              i10 = j;
              i2 = i11;
              i4 = i5;
              if (j < 4096) {
                i2 = i11 + 1;
                i4 = i5 + j;
                i10 = j;
              } 
            } 
          } 
          break;
        } 
        j = i9;
        i9 = i16;
        n = i10;
        standardGifDecoder1 = this;
        i11 = i2;
        i10 = i13;
        i13 = i12;
        i5 = i4;
        i12 = n;
        i4 = i9;
      } 
      i2 = i11;
      i9 = i10;
      i10 = i3;
      i3 = i12;
      i11 = j;
      standardGifDecoder1 = this;
      n = i6;
      j = i9;
      i6 = i15;
      i9 = i14;
      i12 = i4;
      i4 = i3;
      i3 = i2;
    } 
    Arrays.fill(arrayOfByte2, i10, i, (byte)0);
  }
  
  private GifHeaderParser getHeaderParser() {
    if (this.parser == null)
      this.parser = new GifHeaderParser(); 
    return this.parser;
  }
  
  private Bitmap getNextBitmap() {
    Boolean bool = this.isFirstFrameTransparent;
    if (bool == null || bool.booleanValue()) {
      Bitmap.Config config1 = Bitmap.Config.ARGB_8888;
      Bitmap bitmap1 = this.bitmapProvider.obtain(this.downsampledWidth, this.downsampledHeight, config1);
      bitmap1.setHasAlpha(true);
      return bitmap1;
    } 
    Bitmap.Config config = this.bitmapConfig;
    Bitmap bitmap = this.bitmapProvider.obtain(this.downsampledWidth, this.downsampledHeight, config);
    bitmap.setHasAlpha(true);
    return bitmap;
  }
  
  private int readBlock() {
    int i = readByte();
    if (i <= 0)
      return i; 
    ByteBuffer byteBuffer = this.rawData;
    byteBuffer.get(this.block, 0, Math.min(i, byteBuffer.remaining()));
    return i;
  }
  
  private int readByte() {
    return this.rawData.get() & 0xFF;
  }
  
  private Bitmap setPixels(GifFrame paramGifFrame1, GifFrame paramGifFrame2) {
    int[] arrayOfInt = this.mainScratch;
    int i = 0;
    if (paramGifFrame2 == null) {
      Bitmap bitmap1 = this.previousImage;
      if (bitmap1 != null)
        this.bitmapProvider.release(bitmap1); 
      this.previousImage = null;
      Arrays.fill(arrayOfInt, 0);
    } 
    if (paramGifFrame2 != null && paramGifFrame2.dispose == 3 && this.previousImage == null)
      Arrays.fill(arrayOfInt, 0); 
    if (paramGifFrame2 != null && paramGifFrame2.dispose > 0)
      if (paramGifFrame2.dispose == 2) {
        int j = i;
        if (!paramGifFrame1.transparency) {
          j = this.header.bgColor;
          if (paramGifFrame1.lct != null && this.header.bgIndex == paramGifFrame1.transIndex)
            j = i; 
        } 
        int k = paramGifFrame2.ih / this.sampleSize;
        i = paramGifFrame2.iy / this.sampleSize;
        int m = paramGifFrame2.iw / this.sampleSize;
        int n = paramGifFrame2.ix / this.sampleSize;
        int i1 = this.downsampledWidth;
        int i2 = i * i1 + n;
        for (i = i2; i < k * i1 + i2; i += this.downsampledWidth) {
          for (n = i; n < i + m; n++)
            arrayOfInt[n] = j; 
        } 
      } else if (paramGifFrame2.dispose == 3) {
        Bitmap bitmap1 = this.previousImage;
        if (bitmap1 != null) {
          i = this.downsampledWidth;
          bitmap1.getPixels(arrayOfInt, 0, i, 0, 0, i, this.downsampledHeight);
        } 
      }  
    decodeBitmapData(paramGifFrame1);
    if (paramGifFrame1.interlace || this.sampleSize != 1) {
      copyCopyIntoScratchRobust(paramGifFrame1);
    } else {
      copyIntoScratchFast(paramGifFrame1);
    } 
    if (this.savePrevious && (paramGifFrame1.dispose == 0 || paramGifFrame1.dispose == 1)) {
      if (this.previousImage == null)
        this.previousImage = getNextBitmap(); 
      Bitmap bitmap1 = this.previousImage;
      i = this.downsampledWidth;
      bitmap1.setPixels(arrayOfInt, 0, i, 0, 0, i, this.downsampledHeight);
    } 
    Bitmap bitmap = getNextBitmap();
    i = this.downsampledWidth;
    bitmap.setPixels(arrayOfInt, 0, i, 0, 0, i, this.downsampledHeight);
    return bitmap;
  }
  
  public void advance() {
    this.framePointer = (this.framePointer + 1) % this.header.frameCount;
  }
  
  public void clear() {
    this.header = null;
    byte[] arrayOfByte2 = this.mainPixels;
    if (arrayOfByte2 != null)
      this.bitmapProvider.release(arrayOfByte2); 
    int[] arrayOfInt = this.mainScratch;
    if (arrayOfInt != null)
      this.bitmapProvider.release(arrayOfInt); 
    Bitmap bitmap = this.previousImage;
    if (bitmap != null)
      this.bitmapProvider.release(bitmap); 
    this.previousImage = null;
    this.rawData = null;
    this.isFirstFrameTransparent = null;
    byte[] arrayOfByte1 = this.block;
    if (arrayOfByte1 != null)
      this.bitmapProvider.release(arrayOfByte1); 
  }
  
  public int getByteSize() {
    return this.rawData.limit() + this.mainPixels.length + this.mainScratch.length * 4;
  }
  
  public int getCurrentFrameIndex() {
    return this.framePointer;
  }
  
  public ByteBuffer getData() {
    return this.rawData;
  }
  
  public int getDelay(int paramInt) {
    if (paramInt >= 0 && paramInt < this.header.frameCount) {
      paramInt = ((GifFrame)this.header.frames.get(paramInt)).delay;
    } else {
      paramInt = -1;
    } 
    return paramInt;
  }
  
  public int getFrameCount() {
    return this.header.frameCount;
  }
  
  public int getHeight() {
    return this.header.height;
  }
  
  @Deprecated
  public int getLoopCount() {
    return (this.header.loopCount == -1) ? 1 : this.header.loopCount;
  }
  
  public int getNetscapeLoopCount() {
    return this.header.loopCount;
  }
  
  public int getNextDelay() {
    if (this.header.frameCount > 0) {
      int i = this.framePointer;
      if (i >= 0)
        return getDelay(i); 
    } 
    return 0;
  }
  
  public Bitmap getNextFrame() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield header : Lcom/bumptech/glide/gifdecoder/GifHeader;
    //   6: getfield frameCount : I
    //   9: ifle -> 19
    //   12: aload_0
    //   13: getfield framePointer : I
    //   16: ifge -> 92
    //   19: getstatic com/bumptech/glide/gifdecoder/StandardGifDecoder.TAG : Ljava/lang/String;
    //   22: iconst_3
    //   23: invokestatic isLoggable : (Ljava/lang/String;I)Z
    //   26: ifeq -> 87
    //   29: getstatic com/bumptech/glide/gifdecoder/StandardGifDecoder.TAG : Ljava/lang/String;
    //   32: astore_1
    //   33: new java/lang/StringBuilder
    //   36: astore_2
    //   37: aload_2
    //   38: invokespecial <init> : ()V
    //   41: aload_2
    //   42: ldc_w 'Unable to decode frame, frameCount='
    //   45: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   48: pop
    //   49: aload_2
    //   50: aload_0
    //   51: getfield header : Lcom/bumptech/glide/gifdecoder/GifHeader;
    //   54: getfield frameCount : I
    //   57: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   60: pop
    //   61: aload_2
    //   62: ldc_w ', framePointer='
    //   65: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   68: pop
    //   69: aload_2
    //   70: aload_0
    //   71: getfield framePointer : I
    //   74: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   77: pop
    //   78: aload_1
    //   79: aload_2
    //   80: invokevirtual toString : ()Ljava/lang/String;
    //   83: invokestatic d : (Ljava/lang/String;Ljava/lang/String;)I
    //   86: pop
    //   87: aload_0
    //   88: iconst_1
    //   89: putfield status : I
    //   92: aload_0
    //   93: getfield status : I
    //   96: iconst_1
    //   97: if_icmpeq -> 364
    //   100: aload_0
    //   101: getfield status : I
    //   104: iconst_2
    //   105: if_icmpne -> 111
    //   108: goto -> 364
    //   111: aload_0
    //   112: iconst_0
    //   113: putfield status : I
    //   116: aload_0
    //   117: getfield block : [B
    //   120: ifnonnull -> 139
    //   123: aload_0
    //   124: aload_0
    //   125: getfield bitmapProvider : Lcom/bumptech/glide/gifdecoder/GifDecoder$BitmapProvider;
    //   128: sipush #255
    //   131: invokeinterface obtainByteArray : (I)[B
    //   136: putfield block : [B
    //   139: aload_0
    //   140: getfield header : Lcom/bumptech/glide/gifdecoder/GifHeader;
    //   143: getfield frames : Ljava/util/List;
    //   146: aload_0
    //   147: getfield framePointer : I
    //   150: invokeinterface get : (I)Ljava/lang/Object;
    //   155: checkcast com/bumptech/glide/gifdecoder/GifFrame
    //   158: astore_3
    //   159: aload_0
    //   160: getfield framePointer : I
    //   163: iconst_1
    //   164: isub
    //   165: istore #4
    //   167: iload #4
    //   169: iflt -> 193
    //   172: aload_0
    //   173: getfield header : Lcom/bumptech/glide/gifdecoder/GifHeader;
    //   176: getfield frames : Ljava/util/List;
    //   179: iload #4
    //   181: invokeinterface get : (I)Ljava/lang/Object;
    //   186: checkcast com/bumptech/glide/gifdecoder/GifFrame
    //   189: astore_2
    //   190: goto -> 195
    //   193: aconst_null
    //   194: astore_2
    //   195: aload_3
    //   196: getfield lct : [I
    //   199: ifnull -> 210
    //   202: aload_3
    //   203: getfield lct : [I
    //   206: astore_1
    //   207: goto -> 218
    //   210: aload_0
    //   211: getfield header : Lcom/bumptech/glide/gifdecoder/GifHeader;
    //   214: getfield gct : [I
    //   217: astore_1
    //   218: aload_0
    //   219: aload_1
    //   220: putfield act : [I
    //   223: aload_0
    //   224: getfield act : [I
    //   227: ifnonnull -> 287
    //   230: getstatic com/bumptech/glide/gifdecoder/StandardGifDecoder.TAG : Ljava/lang/String;
    //   233: iconst_3
    //   234: invokestatic isLoggable : (Ljava/lang/String;I)Z
    //   237: ifeq -> 278
    //   240: getstatic com/bumptech/glide/gifdecoder/StandardGifDecoder.TAG : Ljava/lang/String;
    //   243: astore_2
    //   244: new java/lang/StringBuilder
    //   247: astore_1
    //   248: aload_1
    //   249: invokespecial <init> : ()V
    //   252: aload_1
    //   253: ldc_w 'No valid color table found for frame #'
    //   256: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   259: pop
    //   260: aload_1
    //   261: aload_0
    //   262: getfield framePointer : I
    //   265: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   268: pop
    //   269: aload_2
    //   270: aload_1
    //   271: invokevirtual toString : ()Ljava/lang/String;
    //   274: invokestatic d : (Ljava/lang/String;Ljava/lang/String;)I
    //   277: pop
    //   278: aload_0
    //   279: iconst_1
    //   280: putfield status : I
    //   283: aload_0
    //   284: monitorexit
    //   285: aconst_null
    //   286: areturn
    //   287: aload_3
    //   288: getfield transparency : Z
    //   291: ifeq -> 353
    //   294: aload_0
    //   295: getfield act : [I
    //   298: iconst_0
    //   299: aload_0
    //   300: getfield pct : [I
    //   303: iconst_0
    //   304: aload_0
    //   305: getfield act : [I
    //   308: arraylength
    //   309: invokestatic arraycopy : (Ljava/lang/Object;ILjava/lang/Object;II)V
    //   312: aload_0
    //   313: aload_0
    //   314: getfield pct : [I
    //   317: putfield act : [I
    //   320: aload_0
    //   321: getfield act : [I
    //   324: aload_3
    //   325: getfield transIndex : I
    //   328: iconst_0
    //   329: iastore
    //   330: aload_3
    //   331: getfield dispose : I
    //   334: iconst_2
    //   335: if_icmpne -> 353
    //   338: aload_0
    //   339: getfield framePointer : I
    //   342: ifne -> 353
    //   345: aload_0
    //   346: iconst_1
    //   347: invokestatic valueOf : (Z)Ljava/lang/Boolean;
    //   350: putfield isFirstFrameTransparent : Ljava/lang/Boolean;
    //   353: aload_0
    //   354: aload_3
    //   355: aload_2
    //   356: invokespecial setPixels : (Lcom/bumptech/glide/gifdecoder/GifFrame;Lcom/bumptech/glide/gifdecoder/GifFrame;)Landroid/graphics/Bitmap;
    //   359: astore_2
    //   360: aload_0
    //   361: monitorexit
    //   362: aload_2
    //   363: areturn
    //   364: getstatic com/bumptech/glide/gifdecoder/StandardGifDecoder.TAG : Ljava/lang/String;
    //   367: iconst_3
    //   368: invokestatic isLoggable : (Ljava/lang/String;I)Z
    //   371: ifeq -> 412
    //   374: getstatic com/bumptech/glide/gifdecoder/StandardGifDecoder.TAG : Ljava/lang/String;
    //   377: astore_2
    //   378: new java/lang/StringBuilder
    //   381: astore_1
    //   382: aload_1
    //   383: invokespecial <init> : ()V
    //   386: aload_1
    //   387: ldc_w 'Unable to decode frame, status='
    //   390: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   393: pop
    //   394: aload_1
    //   395: aload_0
    //   396: getfield status : I
    //   399: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   402: pop
    //   403: aload_2
    //   404: aload_1
    //   405: invokevirtual toString : ()Ljava/lang/String;
    //   408: invokestatic d : (Ljava/lang/String;Ljava/lang/String;)I
    //   411: pop
    //   412: aload_0
    //   413: monitorexit
    //   414: aconst_null
    //   415: areturn
    //   416: astore_2
    //   417: aload_0
    //   418: monitorexit
    //   419: aload_2
    //   420: athrow
    // Exception table:
    //   from	to	target	type
    //   2	19	416	finally
    //   19	87	416	finally
    //   87	92	416	finally
    //   92	108	416	finally
    //   111	139	416	finally
    //   139	167	416	finally
    //   172	190	416	finally
    //   195	207	416	finally
    //   210	218	416	finally
    //   218	278	416	finally
    //   278	283	416	finally
    //   287	353	416	finally
    //   353	360	416	finally
    //   364	412	416	finally
  }
  
  public int getStatus() {
    return this.status;
  }
  
  public int getTotalIterationCount() {
    return (this.header.loopCount == -1) ? 1 : ((this.header.loopCount == 0) ? 0 : (this.header.loopCount + 1));
  }
  
  public int getWidth() {
    return this.header.width;
  }
  
  public int read(InputStream paramInputStream, int paramInt) {
    // Byte code:
    //   0: aload_1
    //   1: ifnull -> 95
    //   4: iload_2
    //   5: ifle -> 17
    //   8: wide iinc #2 4096
    //   14: goto -> 21
    //   17: sipush #16384
    //   20: istore_2
    //   21: new java/io/ByteArrayOutputStream
    //   24: astore_3
    //   25: aload_3
    //   26: iload_2
    //   27: invokespecial <init> : (I)V
    //   30: sipush #16384
    //   33: newarray byte
    //   35: astore #4
    //   37: aload_1
    //   38: aload #4
    //   40: iconst_0
    //   41: aload #4
    //   43: arraylength
    //   44: invokevirtual read : ([BII)I
    //   47: istore_2
    //   48: iload_2
    //   49: iconst_m1
    //   50: if_icmpeq -> 64
    //   53: aload_3
    //   54: aload #4
    //   56: iconst_0
    //   57: iload_2
    //   58: invokevirtual write : ([BII)V
    //   61: goto -> 37
    //   64: aload_3
    //   65: invokevirtual flush : ()V
    //   68: aload_0
    //   69: aload_3
    //   70: invokevirtual toByteArray : ()[B
    //   73: invokevirtual read : ([B)I
    //   76: pop
    //   77: goto -> 100
    //   80: astore_3
    //   81: getstatic com/bumptech/glide/gifdecoder/StandardGifDecoder.TAG : Ljava/lang/String;
    //   84: ldc_w 'Error reading data from stream'
    //   87: aload_3
    //   88: invokestatic w : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   91: pop
    //   92: goto -> 100
    //   95: aload_0
    //   96: iconst_2
    //   97: putfield status : I
    //   100: aload_1
    //   101: ifnull -> 123
    //   104: aload_1
    //   105: invokevirtual close : ()V
    //   108: goto -> 123
    //   111: astore_1
    //   112: getstatic com/bumptech/glide/gifdecoder/StandardGifDecoder.TAG : Ljava/lang/String;
    //   115: ldc_w 'Error closing stream'
    //   118: aload_1
    //   119: invokestatic w : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   122: pop
    //   123: aload_0
    //   124: getfield status : I
    //   127: ireturn
    // Exception table:
    //   from	to	target	type
    //   21	37	80	java/io/IOException
    //   37	48	80	java/io/IOException
    //   53	61	80	java/io/IOException
    //   64	77	80	java/io/IOException
    //   104	108	111	java/io/IOException
  }
  
  public int read(byte[] paramArrayOfbyte) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_0
    //   4: invokespecial getHeaderParser : ()Lcom/bumptech/glide/gifdecoder/GifHeaderParser;
    //   7: aload_1
    //   8: invokevirtual setData : ([B)Lcom/bumptech/glide/gifdecoder/GifHeaderParser;
    //   11: invokevirtual parseHeader : ()Lcom/bumptech/glide/gifdecoder/GifHeader;
    //   14: putfield header : Lcom/bumptech/glide/gifdecoder/GifHeader;
    //   17: aload_1
    //   18: ifnull -> 30
    //   21: aload_0
    //   22: aload_0
    //   23: getfield header : Lcom/bumptech/glide/gifdecoder/GifHeader;
    //   26: aload_1
    //   27: invokevirtual setData : (Lcom/bumptech/glide/gifdecoder/GifHeader;[B)V
    //   30: aload_0
    //   31: getfield status : I
    //   34: istore_2
    //   35: aload_0
    //   36: monitorexit
    //   37: iload_2
    //   38: ireturn
    //   39: astore_1
    //   40: aload_0
    //   41: monitorexit
    //   42: aload_1
    //   43: athrow
    // Exception table:
    //   from	to	target	type
    //   2	17	39	finally
    //   21	30	39	finally
    //   30	35	39	finally
  }
  
  public void resetFrameIndex() {
    this.framePointer = -1;
  }
  
  public void setData(GifHeader paramGifHeader, ByteBuffer paramByteBuffer) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: aload_2
    //   5: iconst_1
    //   6: invokevirtual setData : (Lcom/bumptech/glide/gifdecoder/GifHeader;Ljava/nio/ByteBuffer;I)V
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
  
  public void setData(GifHeader paramGifHeader, ByteBuffer paramByteBuffer, int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: iload_3
    //   3: ifle -> 175
    //   6: iload_3
    //   7: invokestatic highestOneBit : (I)I
    //   10: istore_3
    //   11: aload_0
    //   12: iconst_0
    //   13: putfield status : I
    //   16: aload_0
    //   17: aload_1
    //   18: putfield header : Lcom/bumptech/glide/gifdecoder/GifHeader;
    //   21: aload_0
    //   22: iconst_m1
    //   23: putfield framePointer : I
    //   26: aload_0
    //   27: aload_2
    //   28: invokevirtual asReadOnlyBuffer : ()Ljava/nio/ByteBuffer;
    //   31: putfield rawData : Ljava/nio/ByteBuffer;
    //   34: aload_0
    //   35: getfield rawData : Ljava/nio/ByteBuffer;
    //   38: iconst_0
    //   39: invokevirtual position : (I)Ljava/nio/Buffer;
    //   42: pop
    //   43: aload_0
    //   44: getfield rawData : Ljava/nio/ByteBuffer;
    //   47: getstatic java/nio/ByteOrder.LITTLE_ENDIAN : Ljava/nio/ByteOrder;
    //   50: invokevirtual order : (Ljava/nio/ByteOrder;)Ljava/nio/ByteBuffer;
    //   53: pop
    //   54: aload_0
    //   55: iconst_0
    //   56: putfield savePrevious : Z
    //   59: aload_1
    //   60: getfield frames : Ljava/util/List;
    //   63: invokeinterface iterator : ()Ljava/util/Iterator;
    //   68: astore_2
    //   69: aload_2
    //   70: invokeinterface hasNext : ()Z
    //   75: ifeq -> 99
    //   78: aload_2
    //   79: invokeinterface next : ()Ljava/lang/Object;
    //   84: checkcast com/bumptech/glide/gifdecoder/GifFrame
    //   87: getfield dispose : I
    //   90: iconst_3
    //   91: if_icmpne -> 69
    //   94: aload_0
    //   95: iconst_1
    //   96: putfield savePrevious : Z
    //   99: aload_0
    //   100: iload_3
    //   101: putfield sampleSize : I
    //   104: aload_0
    //   105: aload_1
    //   106: getfield width : I
    //   109: iload_3
    //   110: idiv
    //   111: putfield downsampledWidth : I
    //   114: aload_0
    //   115: aload_1
    //   116: getfield height : I
    //   119: iload_3
    //   120: idiv
    //   121: putfield downsampledHeight : I
    //   124: aload_0
    //   125: aload_0
    //   126: getfield bitmapProvider : Lcom/bumptech/glide/gifdecoder/GifDecoder$BitmapProvider;
    //   129: aload_1
    //   130: getfield width : I
    //   133: aload_1
    //   134: getfield height : I
    //   137: imul
    //   138: invokeinterface obtainByteArray : (I)[B
    //   143: putfield mainPixels : [B
    //   146: aload_0
    //   147: aload_0
    //   148: getfield bitmapProvider : Lcom/bumptech/glide/gifdecoder/GifDecoder$BitmapProvider;
    //   151: aload_0
    //   152: getfield downsampledWidth : I
    //   155: aload_0
    //   156: getfield downsampledHeight : I
    //   159: imul
    //   160: invokeinterface obtainIntArray : (I)[I
    //   165: putfield mainScratch : [I
    //   168: aload_0
    //   169: monitorexit
    //   170: return
    //   171: astore_1
    //   172: goto -> 211
    //   175: new java/lang/IllegalArgumentException
    //   178: astore_1
    //   179: new java/lang/StringBuilder
    //   182: astore_2
    //   183: aload_2
    //   184: invokespecial <init> : ()V
    //   187: aload_2
    //   188: ldc_w 'Sample size must be >=0, not: '
    //   191: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   194: pop
    //   195: aload_2
    //   196: iload_3
    //   197: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   200: pop
    //   201: aload_1
    //   202: aload_2
    //   203: invokevirtual toString : ()Ljava/lang/String;
    //   206: invokespecial <init> : (Ljava/lang/String;)V
    //   209: aload_1
    //   210: athrow
    //   211: aload_0
    //   212: monitorexit
    //   213: goto -> 218
    //   216: aload_1
    //   217: athrow
    //   218: goto -> 216
    // Exception table:
    //   from	to	target	type
    //   6	69	171	finally
    //   69	99	171	finally
    //   99	168	171	finally
    //   175	211	171	finally
  }
  
  public void setData(GifHeader paramGifHeader, byte[] paramArrayOfbyte) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: aload_2
    //   5: invokestatic wrap : ([B)Ljava/nio/ByteBuffer;
    //   8: invokevirtual setData : (Lcom/bumptech/glide/gifdecoder/GifHeader;Ljava/nio/ByteBuffer;)V
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
  
  public void setDefaultBitmapConfig(Bitmap.Config paramConfig) {
    if (paramConfig == Bitmap.Config.ARGB_8888 || paramConfig == Bitmap.Config.RGB_565) {
      this.bitmapConfig = paramConfig;
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Unsupported format: ");
    stringBuilder.append(paramConfig);
    stringBuilder.append(", must be one of ");
    stringBuilder.append(Bitmap.Config.ARGB_8888);
    stringBuilder.append(" or ");
    stringBuilder.append(Bitmap.Config.RGB_565);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/gifdecoder/StandardGifDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */