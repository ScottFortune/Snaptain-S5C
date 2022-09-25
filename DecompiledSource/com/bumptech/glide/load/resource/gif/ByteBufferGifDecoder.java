package com.bumptech.glide.load.resource.gif;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import com.bumptech.glide.Glide;
import com.bumptech.glide.gifdecoder.GifDecoder;
import com.bumptech.glide.gifdecoder.GifHeader;
import com.bumptech.glide.gifdecoder.GifHeaderParser;
import com.bumptech.glide.gifdecoder.StandardGifDecoder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.ImageHeaderParser;
import com.bumptech.glide.load.ImageHeaderParserUtils;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.UnitTransformation;
import com.bumptech.glide.util.LogTime;
import com.bumptech.glide.util.Util;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Queue;

public class ByteBufferGifDecoder implements ResourceDecoder<ByteBuffer, GifDrawable> {
  private static final GifDecoderFactory GIF_DECODER_FACTORY = new GifDecoderFactory();
  
  private static final GifHeaderParserPool PARSER_POOL = new GifHeaderParserPool();
  
  private static final String TAG = "BufferGifDecoder";
  
  private final Context context;
  
  private final GifDecoderFactory gifDecoderFactory;
  
  private final GifHeaderParserPool parserPool;
  
  private final List<ImageHeaderParser> parsers;
  
  private final GifBitmapProvider provider;
  
  public ByteBufferGifDecoder(Context paramContext) {
    this(paramContext, Glide.get(paramContext).getRegistry().getImageHeaderParsers(), Glide.get(paramContext).getBitmapPool(), Glide.get(paramContext).getArrayPool());
  }
  
  public ByteBufferGifDecoder(Context paramContext, List<ImageHeaderParser> paramList, BitmapPool paramBitmapPool, ArrayPool paramArrayPool) {
    this(paramContext, paramList, paramBitmapPool, paramArrayPool, PARSER_POOL, GIF_DECODER_FACTORY);
  }
  
  ByteBufferGifDecoder(Context paramContext, List<ImageHeaderParser> paramList, BitmapPool paramBitmapPool, ArrayPool paramArrayPool, GifHeaderParserPool paramGifHeaderParserPool, GifDecoderFactory paramGifDecoderFactory) {
    this.context = paramContext.getApplicationContext();
    this.parsers = paramList;
    this.gifDecoderFactory = paramGifDecoderFactory;
    this.provider = new GifBitmapProvider(paramBitmapPool, paramArrayPool);
    this.parserPool = paramGifHeaderParserPool;
  }
  
  private GifDrawableResource decode(ByteBuffer paramByteBuffer, int paramInt1, int paramInt2, GifHeaderParser paramGifHeaderParser, Options paramOptions) {
    long l = LogTime.getLogTime();
    try {
      StringBuilder stringBuilder2;
      StringBuilder stringBuilder1;
      Bitmap.Config config;
      GifHeader gifHeader = paramGifHeaderParser.parseHeader();
      if (gifHeader.getNumFrames() <= 0 || gifHeader.getStatus() != 0)
        return null; 
      if (paramOptions.get(GifOptions.DECODE_FORMAT) == DecodeFormat.PREFER_RGB_565) {
        config = Bitmap.Config.RGB_565;
      } else {
        config = Bitmap.Config.ARGB_8888;
      } 
      int i = getSampleSize(gifHeader, paramInt1, paramInt2);
      GifDecoder gifDecoder = this.gifDecoderFactory.build(this.provider, gifHeader, (ByteBuffer)stringBuilder2, i);
      gifDecoder.setDefaultBitmapConfig(config);
      gifDecoder.advance();
      Bitmap bitmap = gifDecoder.getNextFrame();
      if (bitmap == null)
        return null; 
      UnitTransformation unitTransformation = UnitTransformation.get();
      GifDrawable gifDrawable = new GifDrawable();
      this(this.context, (GifDecoder)stringBuilder1, (Transformation<Bitmap>)unitTransformation, paramInt1, paramInt2, bitmap);
      return new GifDrawableResource(gifDrawable);
    } finally {
      if (Log.isLoggable("BufferGifDecoder", 2)) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Decoded GIF from stream in ");
        stringBuilder.append(LogTime.getElapsedMillis(l));
        Log.v("BufferGifDecoder", stringBuilder.toString());
      } 
    } 
  }
  
  private static int getSampleSize(GifHeader paramGifHeader, int paramInt1, int paramInt2) {
    int i = Math.min(paramGifHeader.getHeight() / paramInt2, paramGifHeader.getWidth() / paramInt1);
    if (i == 0) {
      i = 0;
    } else {
      i = Integer.highestOneBit(i);
    } 
    i = Math.max(1, i);
    if (Log.isLoggable("BufferGifDecoder", 2) && i > 1) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Downsampling GIF, sampleSize: ");
      stringBuilder.append(i);
      stringBuilder.append(", target dimens: [");
      stringBuilder.append(paramInt1);
      stringBuilder.append("x");
      stringBuilder.append(paramInt2);
      stringBuilder.append("], actual dimens: [");
      stringBuilder.append(paramGifHeader.getWidth());
      stringBuilder.append("x");
      stringBuilder.append(paramGifHeader.getHeight());
      stringBuilder.append("]");
      Log.v("BufferGifDecoder", stringBuilder.toString());
    } 
    return i;
  }
  
  public GifDrawableResource decode(ByteBuffer paramByteBuffer, int paramInt1, int paramInt2, Options paramOptions) {
    GifHeaderParser gifHeaderParser = this.parserPool.obtain(paramByteBuffer);
    try {
      return decode(paramByteBuffer, paramInt1, paramInt2, gifHeaderParser, paramOptions);
    } finally {
      this.parserPool.release(gifHeaderParser);
    } 
  }
  
  public boolean handles(ByteBuffer paramByteBuffer, Options paramOptions) throws IOException {
    boolean bool;
    if (!((Boolean)paramOptions.get(GifOptions.DISABLE_ANIMATION)).booleanValue() && ImageHeaderParserUtils.getType(this.parsers, paramByteBuffer) == ImageHeaderParser.ImageType.GIF) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  static class GifDecoderFactory {
    GifDecoder build(GifDecoder.BitmapProvider param1BitmapProvider, GifHeader param1GifHeader, ByteBuffer param1ByteBuffer, int param1Int) {
      return (GifDecoder)new StandardGifDecoder(param1BitmapProvider, param1GifHeader, param1ByteBuffer, param1Int);
    }
  }
  
  static class GifHeaderParserPool {
    private final Queue<GifHeaderParser> pool = Util.createQueue(0);
    
    GifHeaderParser obtain(ByteBuffer param1ByteBuffer) {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: getfield pool : Ljava/util/Queue;
      //   6: invokeinterface poll : ()Ljava/lang/Object;
      //   11: checkcast com/bumptech/glide/gifdecoder/GifHeaderParser
      //   14: astore_2
      //   15: aload_2
      //   16: astore_3
      //   17: aload_2
      //   18: ifnonnull -> 29
      //   21: new com/bumptech/glide/gifdecoder/GifHeaderParser
      //   24: astore_3
      //   25: aload_3
      //   26: invokespecial <init> : ()V
      //   29: aload_3
      //   30: aload_1
      //   31: invokevirtual setData : (Ljava/nio/ByteBuffer;)Lcom/bumptech/glide/gifdecoder/GifHeaderParser;
      //   34: astore_1
      //   35: aload_0
      //   36: monitorexit
      //   37: aload_1
      //   38: areturn
      //   39: astore_1
      //   40: aload_0
      //   41: monitorexit
      //   42: aload_1
      //   43: athrow
      // Exception table:
      //   from	to	target	type
      //   2	15	39	finally
      //   21	29	39	finally
      //   29	35	39	finally
    }
    
    void release(GifHeaderParser param1GifHeaderParser) {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_1
      //   3: invokevirtual clear : ()V
      //   6: aload_0
      //   7: getfield pool : Ljava/util/Queue;
      //   10: aload_1
      //   11: invokeinterface offer : (Ljava/lang/Object;)Z
      //   16: pop
      //   17: aload_0
      //   18: monitorexit
      //   19: return
      //   20: astore_1
      //   21: aload_0
      //   22: monitorexit
      //   23: aload_1
      //   24: athrow
      // Exception table:
      //   from	to	target	type
      //   2	17	20	finally
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/resource/gif/ByteBufferGifDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */