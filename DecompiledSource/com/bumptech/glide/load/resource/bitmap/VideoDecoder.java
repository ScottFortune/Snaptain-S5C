package com.bumptech.glide.load.resource.bitmap;

import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import com.bumptech.glide.load.Option;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;

public class VideoDecoder<T> implements ResourceDecoder<T, Bitmap> {
  private static final MediaMetadataRetrieverFactory DEFAULT_FACTORY;
  
  public static final long DEFAULT_FRAME = -1L;
  
  static final int DEFAULT_FRAME_OPTION = 2;
  
  public static final Option<Integer> FRAME_OPTION;
  
  private static final String TAG = "VideoDecoder";
  
  public static final Option<Long> TARGET_FRAME = Option.disk("com.bumptech.glide.load.resource.bitmap.VideoBitmapDecode.TargetFrame", Long.valueOf(-1L), new Option.CacheKeyUpdater<Long>() {
        private final ByteBuffer buffer = ByteBuffer.allocate(8);
        
        public void update(byte[] param1ArrayOfbyte, Long param1Long, MessageDigest param1MessageDigest) {
          param1MessageDigest.update(param1ArrayOfbyte);
          synchronized (this.buffer) {
            this.buffer.position(0);
            param1MessageDigest.update(this.buffer.putLong(param1Long.longValue()).array());
            return;
          } 
        }
      });
  
  private final BitmapPool bitmapPool;
  
  private final MediaMetadataRetrieverFactory factory;
  
  private final MediaMetadataRetrieverInitializer<T> initializer;
  
  static {
    FRAME_OPTION = Option.disk("com.bumptech.glide.load.resource.bitmap.VideoBitmapDecode.FrameOption", Integer.valueOf(2), new Option.CacheKeyUpdater<Integer>() {
          private final ByteBuffer buffer = ByteBuffer.allocate(4);
          
          public void update(byte[] param1ArrayOfbyte, Integer param1Integer, MessageDigest param1MessageDigest) {
            if (param1Integer == null)
              return; 
            param1MessageDigest.update(param1ArrayOfbyte);
            synchronized (this.buffer) {
              this.buffer.position(0);
              param1MessageDigest.update(this.buffer.putInt(param1Integer.intValue()).array());
              return;
            } 
          }
        });
    DEFAULT_FACTORY = new MediaMetadataRetrieverFactory();
  }
  
  VideoDecoder(BitmapPool paramBitmapPool, MediaMetadataRetrieverInitializer<T> paramMediaMetadataRetrieverInitializer) {
    this(paramBitmapPool, paramMediaMetadataRetrieverInitializer, DEFAULT_FACTORY);
  }
  
  VideoDecoder(BitmapPool paramBitmapPool, MediaMetadataRetrieverInitializer<T> paramMediaMetadataRetrieverInitializer, MediaMetadataRetrieverFactory paramMediaMetadataRetrieverFactory) {
    this.bitmapPool = paramBitmapPool;
    this.initializer = paramMediaMetadataRetrieverInitializer;
    this.factory = paramMediaMetadataRetrieverFactory;
  }
  
  public static ResourceDecoder<AssetFileDescriptor, Bitmap> asset(BitmapPool paramBitmapPool) {
    return new VideoDecoder<AssetFileDescriptor>(paramBitmapPool, new AssetFileDescriptorInitializer());
  }
  
  private static Bitmap decodeFrame(MediaMetadataRetriever paramMediaMetadataRetriever, long paramLong, int paramInt1, int paramInt2, int paramInt3, DownsampleStrategy paramDownsampleStrategy) {
    Bitmap bitmap;
    if (Build.VERSION.SDK_INT >= 27 && paramInt2 != Integer.MIN_VALUE && paramInt3 != Integer.MIN_VALUE && paramDownsampleStrategy != DownsampleStrategy.NONE) {
      Bitmap bitmap1 = decodeScaledFrame(paramMediaMetadataRetriever, paramLong, paramInt1, paramInt2, paramInt3, paramDownsampleStrategy);
    } else {
      paramDownsampleStrategy = null;
    } 
    DownsampleStrategy downsampleStrategy = paramDownsampleStrategy;
    if (paramDownsampleStrategy == null)
      bitmap = decodeOriginalFrame(paramMediaMetadataRetriever, paramLong, paramInt1); 
    return bitmap;
  }
  
  private static Bitmap decodeOriginalFrame(MediaMetadataRetriever paramMediaMetadataRetriever, long paramLong, int paramInt) {
    return paramMediaMetadataRetriever.getFrameAtTime(paramLong, paramInt);
  }
  
  private static Bitmap decodeScaledFrame(MediaMetadataRetriever paramMediaMetadataRetriever, long paramLong, int paramInt1, int paramInt2, int paramInt3, DownsampleStrategy paramDownsampleStrategy) {
    try {
      int i = Integer.parseInt(paramMediaMetadataRetriever.extractMetadata(18));
      int j = Integer.parseInt(paramMediaMetadataRetriever.extractMetadata(19));
      int k = Integer.parseInt(paramMediaMetadataRetriever.extractMetadata(24));
      if (k != 90) {
        int i1 = i;
        int i2 = j;
        return paramMediaMetadataRetriever.getScaledFrameAtTime(paramLong, paramInt1, Math.round(i1 * f1), Math.round(f1 * i2));
      } 
      int n = i;
      return paramMediaMetadataRetriever.getScaledFrameAtTime(paramLong, paramInt1, Math.round(m * f), Math.round(f * n));
    } finally {
      paramMediaMetadataRetriever = null;
      if (Log.isLoggable("VideoDecoder", 3))
        Log.d("VideoDecoder", "Exception trying to decode frame on oreo+", (Throwable)paramMediaMetadataRetriever); 
    } 
  }
  
  public static ResourceDecoder<ParcelFileDescriptor, Bitmap> parcel(BitmapPool paramBitmapPool) {
    return new VideoDecoder<ParcelFileDescriptor>(paramBitmapPool, new ParcelFileDescriptorInitializer());
  }
  
  public Resource<Bitmap> decode(T paramT, int paramInt1, int paramInt2, Options paramOptions) throws IOException {
    long l = ((Long)paramOptions.get(TARGET_FRAME)).longValue();
    if (l >= 0L || l == -1L) {
      Integer integer1 = (Integer)paramOptions.get(FRAME_OPTION);
      Integer integer2 = integer1;
      if (integer1 == null)
        integer2 = Integer.valueOf(2); 
      DownsampleStrategy downsampleStrategy2 = (DownsampleStrategy)paramOptions.get(DownsampleStrategy.OPTION);
      DownsampleStrategy downsampleStrategy1 = downsampleStrategy2;
      if (downsampleStrategy2 == null)
        downsampleStrategy1 = DownsampleStrategy.DEFAULT; 
      MediaMetadataRetriever mediaMetadataRetriever = this.factory.build();
      try {
        this.initializer.initialize(mediaMetadataRetriever, paramT);
        Bitmap bitmap = decodeFrame(mediaMetadataRetriever, l, integer2.intValue(), paramInt1, paramInt2, downsampleStrategy1);
        mediaMetadataRetriever.release();
        return BitmapResource.obtain(bitmap, this.bitmapPool);
      } catch (RuntimeException runtimeException) {
        IOException iOException = new IOException();
        this(runtimeException);
        throw iOException;
      } finally {}
      mediaMetadataRetriever.release();
      throw paramT;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Requested frame must be non-negative, or DEFAULT_FRAME, given: ");
    stringBuilder.append(l);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public boolean handles(T paramT, Options paramOptions) {
    return true;
  }
  
  private static final class AssetFileDescriptorInitializer implements MediaMetadataRetrieverInitializer<AssetFileDescriptor> {
    private AssetFileDescriptorInitializer() {}
    
    public void initialize(MediaMetadataRetriever param1MediaMetadataRetriever, AssetFileDescriptor param1AssetFileDescriptor) {
      param1MediaMetadataRetriever.setDataSource(param1AssetFileDescriptor.getFileDescriptor(), param1AssetFileDescriptor.getStartOffset(), param1AssetFileDescriptor.getLength());
    }
  }
  
  static class MediaMetadataRetrieverFactory {
    public MediaMetadataRetriever build() {
      return new MediaMetadataRetriever();
    }
  }
  
  static interface MediaMetadataRetrieverInitializer<T> {
    void initialize(MediaMetadataRetriever param1MediaMetadataRetriever, T param1T);
  }
  
  static final class ParcelFileDescriptorInitializer implements MediaMetadataRetrieverInitializer<ParcelFileDescriptor> {
    public void initialize(MediaMetadataRetriever param1MediaMetadataRetriever, ParcelFileDescriptor param1ParcelFileDescriptor) {
      param1MediaMetadataRetriever.setDataSource(param1ParcelFileDescriptor.getFileDescriptor());
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/resource/bitmap/VideoDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */