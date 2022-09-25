package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorSpace;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.ImageHeaderParser;
import com.bumptech.glide.load.ImageHeaderParserUtils;
import com.bumptech.glide.load.Option;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.PreferredColorSpace;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.util.LogTime;
import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.util.Util;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public final class Downsampler {
  public static final Option<Boolean> ALLOW_HARDWARE_CONFIG;
  
  public static final Option<DecodeFormat> DECODE_FORMAT = Option.memory("com.bumptech.glide.load.resource.bitmap.Downsampler.DecodeFormat", DecodeFormat.DEFAULT);
  
  @Deprecated
  public static final Option<DownsampleStrategy> DOWNSAMPLE_STRATEGY;
  
  private static final DecodeCallbacks EMPTY_CALLBACKS;
  
  public static final Option<Boolean> FIX_BITMAP_SIZE_TO_REQUESTED_DIMENSIONS;
  
  private static final String ICO_MIME_TYPE = "image/x-ico";
  
  private static final int MARK_POSITION = 10485760;
  
  private static final Set<String> NO_DOWNSAMPLE_PRE_N_MIME_TYPES;
  
  private static final Queue<BitmapFactory.Options> OPTIONS_QUEUE;
  
  public static final Option<PreferredColorSpace> PREFERRED_COLOR_SPACE = Option.memory("com.bumptech.glide.load.resource.bitmap.Downsampler.PreferredColorSpace", PreferredColorSpace.SRGB);
  
  static final String TAG = "Downsampler";
  
  private static final Set<ImageHeaderParser.ImageType> TYPES_THAT_USE_POOL_PRE_KITKAT;
  
  private static final String WBMP_MIME_TYPE = "image/vnd.wap.wbmp";
  
  private final BitmapPool bitmapPool;
  
  private final ArrayPool byteArrayPool;
  
  private final DisplayMetrics displayMetrics;
  
  private final HardwareConfigState hardwareConfigState = HardwareConfigState.getInstance();
  
  private final List<ImageHeaderParser> parsers;
  
  static {
    DOWNSAMPLE_STRATEGY = DownsampleStrategy.OPTION;
    Boolean bool = Boolean.valueOf(false);
    FIX_BITMAP_SIZE_TO_REQUESTED_DIMENSIONS = Option.memory("com.bumptech.glide.load.resource.bitmap.Downsampler.FixBitmapSize", bool);
    ALLOW_HARDWARE_CONFIG = Option.memory("com.bumptech.glide.load.resource.bitmap.Downsampler.AllowHardwareDecode", bool);
    NO_DOWNSAMPLE_PRE_N_MIME_TYPES = Collections.unmodifiableSet(new HashSet<String>(Arrays.asList(new String[] { "image/vnd.wap.wbmp", "image/x-ico" })));
    EMPTY_CALLBACKS = new DecodeCallbacks() {
        public void onDecodeComplete(BitmapPool param1BitmapPool, Bitmap param1Bitmap) {}
        
        public void onObtainBounds() {}
      };
    TYPES_THAT_USE_POOL_PRE_KITKAT = Collections.unmodifiableSet(EnumSet.of(ImageHeaderParser.ImageType.JPEG, ImageHeaderParser.ImageType.PNG_A, ImageHeaderParser.ImageType.PNG));
    OPTIONS_QUEUE = Util.createQueue(0);
  }
  
  public Downsampler(List<ImageHeaderParser> paramList, DisplayMetrics paramDisplayMetrics, BitmapPool paramBitmapPool, ArrayPool paramArrayPool) {
    this.parsers = paramList;
    this.displayMetrics = (DisplayMetrics)Preconditions.checkNotNull(paramDisplayMetrics);
    this.bitmapPool = (BitmapPool)Preconditions.checkNotNull(paramBitmapPool);
    this.byteArrayPool = (ArrayPool)Preconditions.checkNotNull(paramArrayPool);
  }
  
  private static int adjustTargetDensityForError(double paramDouble) {
    int i = getDensityMultiplier(paramDouble);
    double d = i;
    Double.isNaN(d);
    int j = round(d * paramDouble);
    d = (j / i);
    Double.isNaN(d);
    d = paramDouble / d;
    paramDouble = j;
    Double.isNaN(paramDouble);
    return round(d * paramDouble);
  }
  
  private void calculateConfig(InputStream paramInputStream, DecodeFormat paramDecodeFormat, boolean paramBoolean1, boolean paramBoolean2, BitmapFactory.Options paramOptions, int paramInt1, int paramInt2) {
    Bitmap.Config config;
    if (this.hardwareConfigState.setHardwareConfigIfAllowed(paramInt1, paramInt2, paramOptions, paramBoolean1, paramBoolean2))
      return; 
    if (paramDecodeFormat == DecodeFormat.PREFER_ARGB_8888 || Build.VERSION.SDK_INT == 16) {
      paramOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
      return;
    } 
    paramBoolean2 = false;
    try {
      paramBoolean1 = ImageHeaderParserUtils.getType(this.parsers, paramInputStream, this.byteArrayPool).hasAlpha();
    } catch (IOException iOException) {
      paramBoolean1 = paramBoolean2;
      if (Log.isLoggable("Downsampler", 3)) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Cannot determine whether the image has alpha or not from header, format ");
        stringBuilder.append(paramDecodeFormat);
        Log.d("Downsampler", stringBuilder.toString(), iOException);
        paramBoolean1 = paramBoolean2;
      } 
    } 
    if (paramBoolean1) {
      config = Bitmap.Config.ARGB_8888;
    } else {
      config = Bitmap.Config.RGB_565;
    } 
    paramOptions.inPreferredConfig = config;
    if (paramOptions.inPreferredConfig == Bitmap.Config.RGB_565)
      paramOptions.inDither = true; 
  }
  
  private static void calculateScaling(ImageHeaderParser.ImageType paramImageType, InputStream paramInputStream, DecodeCallbacks paramDecodeCallbacks, BitmapPool paramBitmapPool, DownsampleStrategy paramDownsampleStrategy, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, BitmapFactory.Options paramOptions) throws IOException {
    StringBuilder stringBuilder;
    int i;
    int j;
    if (paramInt2 <= 0 || paramInt3 <= 0) {
      if (Log.isLoggable("Downsampler", 3)) {
        stringBuilder = new StringBuilder();
        stringBuilder.append("Unable to determine dimensions for: ");
        stringBuilder.append(paramImageType);
        stringBuilder.append(" with target [");
        stringBuilder.append(paramInt4);
        stringBuilder.append("x");
        stringBuilder.append(paramInt5);
        stringBuilder.append("]");
        Log.d("Downsampler", stringBuilder.toString());
      } 
      return;
    } 
    if (isRotationRequired(paramInt1)) {
      i = paramInt2;
      j = paramInt3;
    } else {
      j = paramInt2;
      i = paramInt3;
    } 
    float f = paramDownsampleStrategy.getScaleFactor(j, i, paramInt4, paramInt5);
    if (f > 0.0F) {
      DownsampleStrategy.SampleSizeRounding sampleSizeRounding = paramDownsampleStrategy.getSampleSizeRounding(j, i, paramInt4, paramInt5);
      if (sampleSizeRounding != null) {
        float f1 = j;
        int k = round((f * f1));
        float f2 = i;
        int m = round((f * f2));
        k = j / k;
        m = i / m;
        if (sampleSizeRounding == DownsampleStrategy.SampleSizeRounding.MEMORY) {
          m = Math.max(k, m);
        } else {
          m = Math.min(k, m);
        } 
        if (Build.VERSION.SDK_INT <= 23 && NO_DOWNSAMPLE_PRE_N_MIME_TYPES.contains(paramOptions.outMimeType)) {
          m = 1;
        } else {
          k = Math.max(1, Integer.highestOneBit(m));
          m = k;
          if (sampleSizeRounding == DownsampleStrategy.SampleSizeRounding.MEMORY) {
            m = k;
            if (k < 1.0F / f)
              m = k << 1; 
          } 
        } 
        paramOptions.inSampleSize = m;
        if (paramImageType == ImageHeaderParser.ImageType.JPEG) {
          float f3 = Math.min(m, 8);
          int n = (int)Math.ceil((f1 / f3));
          k = (int)Math.ceil((f2 / f3));
          int i1 = m / 8;
          i = k;
          j = n;
          if (i1 > 0) {
            j = n / i1;
            i = k / i1;
          } 
        } else {
          double d1;
          if (paramImageType == ImageHeaderParser.ImageType.PNG || paramImageType == ImageHeaderParser.ImageType.PNG_A) {
            float f3 = m;
            j = (int)Math.floor((f1 / f3));
            d1 = Math.floor((f2 / f3));
          } else {
            if (paramImageType == ImageHeaderParser.ImageType.WEBP || paramImageType == ImageHeaderParser.ImageType.WEBP_A) {
              if (Build.VERSION.SDK_INT >= 24) {
                float f3 = m;
                j = Math.round(f1 / f3);
                i = Math.round(f2 / f3);
              } else {
                float f3 = m;
                j = (int)Math.floor((f1 / f3));
                double d2 = Math.floor((f2 / f3));
                i = (int)d2;
              } 
            } else if (j % m != 0 || i % m != 0) {
              int[] arrayOfInt = getDimensions((InputStream)stringBuilder, paramOptions, paramDecodeCallbacks, paramBitmapPool);
              j = arrayOfInt[0];
              i = arrayOfInt[1];
            } else {
              j /= m;
              i /= m;
            } 
            d1 = paramDownsampleStrategy.getScaleFactor(j, i, paramInt4, paramInt5);
          } 
          i = (int)d1;
        } 
      } else {
        throw new IllegalArgumentException("Cannot round with null rounding");
      } 
    } else {
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("Cannot scale with factor: ");
      stringBuilder1.append(f);
      stringBuilder1.append(" from: ");
      stringBuilder1.append(paramDownsampleStrategy);
      stringBuilder1.append(", source: [");
      stringBuilder1.append(paramInt2);
      stringBuilder1.append("x");
      stringBuilder1.append(paramInt3);
      stringBuilder1.append("], target: [");
      stringBuilder1.append(paramInt4);
      stringBuilder1.append("x");
      stringBuilder1.append(paramInt5);
      stringBuilder1.append("]");
      throw new IllegalArgumentException(stringBuilder1.toString());
    } 
    double d = paramDownsampleStrategy.getScaleFactor(j, i, paramInt4, paramInt5);
  }
  
  private Bitmap decodeFromWrappedStreams(InputStream paramInputStream, BitmapFactory.Options paramOptions, DownsampleStrategy paramDownsampleStrategy, DecodeFormat paramDecodeFormat, PreferredColorSpace paramPreferredColorSpace, boolean paramBoolean1, int paramInt1, int paramInt2, boolean paramBoolean2, DecodeCallbacks paramDecodeCallbacks) throws IOException {
    Bitmap bitmap1;
    int n;
    long l = LogTime.getLogTime();
    int[] arrayOfInt = getDimensions(paramInputStream, paramOptions, paramDecodeCallbacks, this.bitmapPool);
    boolean bool = false;
    int i = arrayOfInt[0];
    int j = arrayOfInt[1];
    String str = paramOptions.outMimeType;
    if (i == -1 || j == -1)
      paramBoolean1 = false; 
    int k = ImageHeaderParserUtils.getOrientation(this.parsers, paramInputStream, this.byteArrayPool);
    int m = TransformationUtils.getExifOrientationDegrees(k);
    boolean bool1 = TransformationUtils.isExifOrientationRequired(k);
    if (paramInt1 == Integer.MIN_VALUE) {
      if (isRotationRequired(m)) {
        n = j;
      } else {
        n = i;
      } 
    } else {
      n = paramInt1;
    } 
    int i1 = paramInt2;
    if (i1 == Integer.MIN_VALUE)
      if (isRotationRequired(m)) {
        i1 = i;
      } else {
        i1 = j;
      }  
    ImageHeaderParser.ImageType imageType = ImageHeaderParserUtils.getType(this.parsers, paramInputStream, this.byteArrayPool);
    calculateScaling(imageType, paramInputStream, paramDecodeCallbacks, this.bitmapPool, paramDownsampleStrategy, m, i, j, n, i1, paramOptions);
    calculateConfig(paramInputStream, paramDecodeFormat, paramBoolean1, bool1, paramOptions, n, i1);
    if (Build.VERSION.SDK_INT >= 19) {
      m = 1;
    } else {
      m = 0;
    } 
    if (paramOptions.inSampleSize == 1 || m != 0) {
      Downsampler downsampler1 = this;
      if (downsampler1.shouldUsePool(imageType)) {
        if (i < 0 || j < 0 || !paramBoolean2 || m == 0) {
          float f1;
          if (isScaling(paramOptions)) {
            f1 = paramOptions.inTargetDensity / paramOptions.inDensity;
          } else {
            f1 = 1.0F;
          } 
          int i2 = paramOptions.inSampleSize;
          float f2 = i;
          float f3 = i2;
          i1 = (int)Math.ceil((f2 / f3));
          n = (int)Math.ceil((j / f3));
          m = Math.round(i1 * f1);
          int i3 = Math.round(n * f1);
          n = m;
          i1 = i3;
          if (Log.isLoggable("Downsampler", 2)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Calculated target [");
            stringBuilder.append(m);
            stringBuilder.append("x");
            stringBuilder.append(i3);
            stringBuilder.append("] for source [");
            stringBuilder.append(i);
            stringBuilder.append("x");
            stringBuilder.append(j);
            stringBuilder.append("], sampleSize: ");
            stringBuilder.append(i2);
            stringBuilder.append(", targetDensity: ");
            stringBuilder.append(paramOptions.inTargetDensity);
            stringBuilder.append(", density: ");
            stringBuilder.append(paramOptions.inDensity);
            stringBuilder.append(", density multiplier: ");
            stringBuilder.append(f1);
            Log.v("Downsampler", stringBuilder.toString());
            i1 = i3;
            n = m;
          } 
        } 
        if (n > 0 && i1 > 0)
          setInBitmap(paramOptions, downsampler1.bitmapPool, n, i1); 
      } 
    } 
    Downsampler downsampler = this;
    if (Build.VERSION.SDK_INT >= 28) {
      ColorSpace.Named named;
      n = bool;
      if (paramPreferredColorSpace == PreferredColorSpace.DISPLAY_P3) {
        n = bool;
        if (paramOptions.outColorSpace != null) {
          n = bool;
          if (paramOptions.outColorSpace.isWideGamut())
            n = 1; 
        } 
      } 
      if (n != 0) {
        named = ColorSpace.Named.DISPLAY_P3;
      } else {
        named = ColorSpace.Named.SRGB;
      } 
      paramOptions.inPreferredColorSpace = ColorSpace.get(named);
    } else if (Build.VERSION.SDK_INT >= 26) {
      paramOptions.inPreferredColorSpace = ColorSpace.get(ColorSpace.Named.SRGB);
    } 
    Bitmap bitmap2 = decodeStream(paramInputStream, paramOptions, paramDecodeCallbacks, downsampler.bitmapPool);
    paramDecodeCallbacks.onDecodeComplete(downsampler.bitmapPool, bitmap2);
    if (Log.isLoggable("Downsampler", 2))
      logDecode(i, j, str, paramOptions, bitmap2, paramInt1, paramInt2, l); 
    paramInputStream = null;
    if (bitmap2 != null) {
      bitmap2.setDensity(downsampler.displayMetrics.densityDpi);
      Bitmap bitmap = TransformationUtils.rotateImageExif(downsampler.bitmapPool, bitmap2, k);
      bitmap1 = bitmap;
      if (!bitmap2.equals(bitmap)) {
        downsampler.bitmapPool.put(bitmap2);
        bitmap1 = bitmap;
      } 
    } 
    return bitmap1;
  }
  
  private static Bitmap decodeStream(InputStream paramInputStream, BitmapFactory.Options paramOptions, DecodeCallbacks paramDecodeCallbacks, BitmapPool paramBitmapPool) throws IOException {
    if (paramOptions.inJustDecodeBounds) {
      paramInputStream.mark(10485760);
    } else {
      paramDecodeCallbacks.onObtainBounds();
    } 
    int i = paramOptions.outWidth;
    int j = paramOptions.outHeight;
    String str = paramOptions.outMimeType;
    TransformationUtils.getBitmapDrawableLock().lock();
    try {
      Bitmap bitmap = BitmapFactory.decodeStream(paramInputStream, null, paramOptions);
      TransformationUtils.getBitmapDrawableLock().unlock();
      if (paramOptions.inJustDecodeBounds)
        paramInputStream.reset(); 
      return bitmap;
    } catch (IllegalArgumentException illegalArgumentException) {
      IOException iOException = newIoExceptionForInBitmapAssertion(illegalArgumentException, i, j, str, paramOptions);
      if (Log.isLoggable("Downsampler", 3))
        Log.d("Downsampler", "Failed to decode with inBitmap, trying again without Bitmap re-use", iOException); 
      Bitmap bitmap = paramOptions.inBitmap;
      if (bitmap != null)
        try {
          paramInputStream.reset();
          paramBitmapPool.put(paramOptions.inBitmap);
          paramOptions.inBitmap = null;
          Bitmap bitmap1 = decodeStream(paramInputStream, paramOptions, paramDecodeCallbacks, paramBitmapPool);
          TransformationUtils.getBitmapDrawableLock().unlock();
          return bitmap1;
        } catch (IOException iOException1) {
          throw iOException;
        }  
      throw iOException;
    } finally {}
    TransformationUtils.getBitmapDrawableLock().unlock();
    throw paramInputStream;
  }
  
  private static String getBitmapString(Bitmap paramBitmap) {
    String str;
    if (paramBitmap == null)
      return null; 
    if (Build.VERSION.SDK_INT >= 19) {
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append(" (");
      stringBuilder1.append(paramBitmap.getAllocationByteCount());
      stringBuilder1.append(")");
      str = stringBuilder1.toString();
    } else {
      str = "";
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("[");
    stringBuilder.append(paramBitmap.getWidth());
    stringBuilder.append("x");
    stringBuilder.append(paramBitmap.getHeight());
    stringBuilder.append("] ");
    stringBuilder.append(paramBitmap.getConfig());
    stringBuilder.append(str);
    return stringBuilder.toString();
  }
  
  private static BitmapFactory.Options getDefaultOptions() {
    // Byte code:
    //   0: ldc com/bumptech/glide/load/resource/bitmap/Downsampler
    //   2: monitorenter
    //   3: getstatic com/bumptech/glide/load/resource/bitmap/Downsampler.OPTIONS_QUEUE : Ljava/util/Queue;
    //   6: astore_0
    //   7: aload_0
    //   8: monitorenter
    //   9: getstatic com/bumptech/glide/load/resource/bitmap/Downsampler.OPTIONS_QUEUE : Ljava/util/Queue;
    //   12: invokeinterface poll : ()Ljava/lang/Object;
    //   17: checkcast android/graphics/BitmapFactory$Options
    //   20: astore_1
    //   21: aload_0
    //   22: monitorexit
    //   23: aload_1
    //   24: astore_0
    //   25: aload_1
    //   26: ifnonnull -> 41
    //   29: new android/graphics/BitmapFactory$Options
    //   32: astore_0
    //   33: aload_0
    //   34: invokespecial <init> : ()V
    //   37: aload_0
    //   38: invokestatic resetOptions : (Landroid/graphics/BitmapFactory$Options;)V
    //   41: ldc com/bumptech/glide/load/resource/bitmap/Downsampler
    //   43: monitorexit
    //   44: aload_0
    //   45: areturn
    //   46: astore_1
    //   47: aload_0
    //   48: monitorexit
    //   49: aload_1
    //   50: athrow
    //   51: astore_0
    //   52: ldc com/bumptech/glide/load/resource/bitmap/Downsampler
    //   54: monitorexit
    //   55: aload_0
    //   56: athrow
    // Exception table:
    //   from	to	target	type
    //   3	9	51	finally
    //   9	23	46	finally
    //   29	41	51	finally
    //   47	49	46	finally
    //   49	51	51	finally
  }
  
  private static int getDensityMultiplier(double paramDouble) {
    if (paramDouble > 1.0D)
      paramDouble = 1.0D / paramDouble; 
    return (int)Math.round(paramDouble * 2.147483647E9D);
  }
  
  private static int[] getDimensions(InputStream paramInputStream, BitmapFactory.Options paramOptions, DecodeCallbacks paramDecodeCallbacks, BitmapPool paramBitmapPool) throws IOException {
    paramOptions.inJustDecodeBounds = true;
    decodeStream(paramInputStream, paramOptions, paramDecodeCallbacks, paramBitmapPool);
    paramOptions.inJustDecodeBounds = false;
    return new int[] { paramOptions.outWidth, paramOptions.outHeight };
  }
  
  private static String getInBitmapString(BitmapFactory.Options paramOptions) {
    return getBitmapString(paramOptions.inBitmap);
  }
  
  private static boolean isRotationRequired(int paramInt) {
    return (paramInt == 90 || paramInt == 270);
  }
  
  private static boolean isScaling(BitmapFactory.Options paramOptions) {
    boolean bool;
    if (paramOptions.inTargetDensity > 0 && paramOptions.inDensity > 0 && paramOptions.inTargetDensity != paramOptions.inDensity) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private static void logDecode(int paramInt1, int paramInt2, String paramString, BitmapFactory.Options paramOptions, Bitmap paramBitmap, int paramInt3, int paramInt4, long paramLong) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Decoded ");
    stringBuilder.append(getBitmapString(paramBitmap));
    stringBuilder.append(" from [");
    stringBuilder.append(paramInt1);
    stringBuilder.append("x");
    stringBuilder.append(paramInt2);
    stringBuilder.append("] ");
    stringBuilder.append(paramString);
    stringBuilder.append(" with inBitmap ");
    stringBuilder.append(getInBitmapString(paramOptions));
    stringBuilder.append(" for [");
    stringBuilder.append(paramInt3);
    stringBuilder.append("x");
    stringBuilder.append(paramInt4);
    stringBuilder.append("], sample size: ");
    stringBuilder.append(paramOptions.inSampleSize);
    stringBuilder.append(", density: ");
    stringBuilder.append(paramOptions.inDensity);
    stringBuilder.append(", target density: ");
    stringBuilder.append(paramOptions.inTargetDensity);
    stringBuilder.append(", thread: ");
    stringBuilder.append(Thread.currentThread().getName());
    stringBuilder.append(", duration: ");
    stringBuilder.append(LogTime.getElapsedMillis(paramLong));
    Log.v("Downsampler", stringBuilder.toString());
  }
  
  private static IOException newIoExceptionForInBitmapAssertion(IllegalArgumentException paramIllegalArgumentException, int paramInt1, int paramInt2, String paramString, BitmapFactory.Options paramOptions) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Exception decoding bitmap, outWidth: ");
    stringBuilder.append(paramInt1);
    stringBuilder.append(", outHeight: ");
    stringBuilder.append(paramInt2);
    stringBuilder.append(", outMimeType: ");
    stringBuilder.append(paramString);
    stringBuilder.append(", inBitmap: ");
    stringBuilder.append(getInBitmapString(paramOptions));
    return new IOException(stringBuilder.toString(), paramIllegalArgumentException);
  }
  
  private static void releaseOptions(BitmapFactory.Options paramOptions) {
    resetOptions(paramOptions);
    synchronized (OPTIONS_QUEUE) {
      OPTIONS_QUEUE.offer(paramOptions);
      return;
    } 
  }
  
  private static void resetOptions(BitmapFactory.Options paramOptions) {
    paramOptions.inTempStorage = null;
    paramOptions.inDither = false;
    paramOptions.inScaled = false;
    paramOptions.inSampleSize = 1;
    paramOptions.inPreferredConfig = null;
    paramOptions.inJustDecodeBounds = false;
    paramOptions.inDensity = 0;
    paramOptions.inTargetDensity = 0;
    if (Build.VERSION.SDK_INT >= 26) {
      paramOptions.inPreferredColorSpace = null;
      paramOptions.outColorSpace = null;
      paramOptions.outConfig = null;
    } 
    paramOptions.outWidth = 0;
    paramOptions.outHeight = 0;
    paramOptions.outMimeType = null;
    paramOptions.inBitmap = null;
    paramOptions.inMutable = true;
  }
  
  private static int round(double paramDouble) {
    return (int)(paramDouble + 0.5D);
  }
  
  private static void setInBitmap(BitmapFactory.Options paramOptions, BitmapPool paramBitmapPool, int paramInt1, int paramInt2) {
    Bitmap.Config config1;
    if (Build.VERSION.SDK_INT >= 26) {
      if (paramOptions.inPreferredConfig == Bitmap.Config.HARDWARE)
        return; 
      config1 = paramOptions.outConfig;
    } else {
      config1 = null;
    } 
    Bitmap.Config config2 = config1;
    if (config1 == null)
      config2 = paramOptions.inPreferredConfig; 
    paramOptions.inBitmap = paramBitmapPool.getDirty(paramInt1, paramInt2, config2);
  }
  
  private boolean shouldUsePool(ImageHeaderParser.ImageType paramImageType) {
    return (Build.VERSION.SDK_INT >= 19) ? true : TYPES_THAT_USE_POOL_PRE_KITKAT.contains(paramImageType);
  }
  
  public Resource<Bitmap> decode(InputStream paramInputStream, int paramInt1, int paramInt2, Options paramOptions) throws IOException {
    return decode(paramInputStream, paramInt1, paramInt2, paramOptions, EMPTY_CALLBACKS);
  }
  
  public Resource<Bitmap> decode(InputStream paramInputStream, int paramInt1, int paramInt2, Options paramOptions, DecodeCallbacks paramDecodeCallbacks) throws IOException {
    boolean bool1;
    Preconditions.checkArgument(paramInputStream.markSupported(), "You must provide an InputStream that supports mark()");
    byte[] arrayOfByte = (byte[])this.byteArrayPool.get(65536, byte[].class);
    BitmapFactory.Options options = getDefaultOptions();
    options.inTempStorage = arrayOfByte;
    DecodeFormat decodeFormat = (DecodeFormat)paramOptions.get(DECODE_FORMAT);
    PreferredColorSpace preferredColorSpace = (PreferredColorSpace)paramOptions.get(PREFERRED_COLOR_SPACE);
    DownsampleStrategy downsampleStrategy = (DownsampleStrategy)paramOptions.get(DownsampleStrategy.OPTION);
    boolean bool = ((Boolean)paramOptions.get(FIX_BITMAP_SIZE_TO_REQUESTED_DIMENSIONS)).booleanValue();
    if (paramOptions.get(ALLOW_HARDWARE_CONFIG) != null && ((Boolean)paramOptions.get(ALLOW_HARDWARE_CONFIG)).booleanValue()) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    try {
      return BitmapResource.obtain(decodeFromWrappedStreams(paramInputStream, options, downsampleStrategy, decodeFormat, preferredColorSpace, bool1, paramInt1, paramInt2, bool, paramDecodeCallbacks), this.bitmapPool);
    } finally {
      releaseOptions(options);
      this.byteArrayPool.put(arrayOfByte);
    } 
  }
  
  public boolean handles(InputStream paramInputStream) {
    return true;
  }
  
  public boolean handles(ByteBuffer paramByteBuffer) {
    return true;
  }
  
  public static interface DecodeCallbacks {
    void onDecodeComplete(BitmapPool param1BitmapPool, Bitmap param1Bitmap) throws IOException;
    
    void onObtainBounds();
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/resource/bitmap/Downsampler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */