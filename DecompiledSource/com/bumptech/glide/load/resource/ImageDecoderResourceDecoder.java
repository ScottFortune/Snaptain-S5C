package com.bumptech.glide.load.resource;

import android.graphics.ColorSpace;
import android.graphics.ImageDecoder;
import android.os.Build;
import android.util.Log;
import android.util.Size;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.PreferredColorSpace;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy;
import com.bumptech.glide.load.resource.bitmap.Downsampler;
import com.bumptech.glide.load.resource.bitmap.HardwareConfigState;
import java.io.IOException;

public abstract class ImageDecoderResourceDecoder<T> implements ResourceDecoder<ImageDecoder.Source, T> {
  private static final String TAG = "ImageDecoder";
  
  final HardwareConfigState hardwareConfigState = HardwareConfigState.getInstance();
  
  protected abstract Resource<T> decode(ImageDecoder.Source paramSource, int paramInt1, int paramInt2, ImageDecoder.OnHeaderDecodedListener paramOnHeaderDecodedListener) throws IOException;
  
  public final Resource<T> decode(ImageDecoder.Source paramSource, final int requestedWidth, final int requestedHeight, Options paramOptions) throws IOException {
    final boolean isHardwareConfigAllowed;
    final DecodeFormat decodeFormat = (DecodeFormat)paramOptions.get(Downsampler.DECODE_FORMAT);
    final DownsampleStrategy strategy = (DownsampleStrategy)paramOptions.get(DownsampleStrategy.OPTION);
    if (paramOptions.get(Downsampler.ALLOW_HARDWARE_CONFIG) != null && ((Boolean)paramOptions.get(Downsampler.ALLOW_HARDWARE_CONFIG)).booleanValue()) {
      bool = true;
    } else {
      bool = false;
    } 
    return decode(paramSource, requestedWidth, requestedHeight, new ImageDecoder.OnHeaderDecodedListener() {
          public void onHeaderDecoded(ImageDecoder param1ImageDecoder, ImageDecoder.ImageInfo param1ImageInfo, ImageDecoder.Source param1Source) {
            boolean bool = ImageDecoderResourceDecoder.this.hardwareConfigState.isHardwareConfigAllowed(requestedWidth, requestedHeight, isHardwareConfigAllowed, false);
            boolean bool1 = true;
            if (bool) {
              param1ImageDecoder.setAllocator(3);
            } else {
              param1ImageDecoder.setAllocator(1);
            } 
            if (decodeFormat == DecodeFormat.PREFER_RGB_565)
              param1ImageDecoder.setMemorySizePolicy(0); 
            param1ImageDecoder.setOnPartialImageListener(new ImageDecoder.OnPartialImageListener() {
                  public boolean onPartialImage(ImageDecoder.DecodeException param2DecodeException) {
                    return false;
                  }
                });
            Size size = param1ImageInfo.getSize();
            int i = requestedWidth;
            int j = i;
            if (i == Integer.MIN_VALUE)
              j = size.getWidth(); 
            int k = requestedHeight;
            i = k;
            if (k == Integer.MIN_VALUE)
              i = size.getHeight(); 
            float f = strategy.getScaleFactor(size.getWidth(), size.getHeight(), j, i);
            j = Math.round(size.getWidth() * f);
            i = Math.round(size.getHeight() * f);
            if (Log.isLoggable("ImageDecoder", 2)) {
              StringBuilder stringBuilder = new StringBuilder();
              stringBuilder.append("Resizing from [");
              stringBuilder.append(size.getWidth());
              stringBuilder.append("x");
              stringBuilder.append(size.getHeight());
              stringBuilder.append("] to [");
              stringBuilder.append(j);
              stringBuilder.append("x");
              stringBuilder.append(i);
              stringBuilder.append("] scaleFactor: ");
              stringBuilder.append(f);
              Log.v("ImageDecoder", stringBuilder.toString());
            } 
            param1ImageDecoder.setTargetSize(j, i);
            if (Build.VERSION.SDK_INT >= 28) {
              ColorSpace.Named named;
              if (preferredColorSpace == PreferredColorSpace.DISPLAY_P3 && param1ImageInfo.getColorSpace() != null && param1ImageInfo.getColorSpace().isWideGamut()) {
                j = bool1;
              } else {
                j = 0;
              } 
              if (j != 0) {
                named = ColorSpace.Named.DISPLAY_P3;
              } else {
                named = ColorSpace.Named.SRGB;
              } 
              param1ImageDecoder.setTargetColorSpace(ColorSpace.get(named));
            } else if (Build.VERSION.SDK_INT >= 26) {
              param1ImageDecoder.setTargetColorSpace(ColorSpace.get(ColorSpace.Named.SRGB));
            } 
          }
        });
  }
  
  public final boolean handles(ImageDecoder.Source paramSource, Options paramOptions) {
    return true;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/resource/ImageDecoderResourceDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */