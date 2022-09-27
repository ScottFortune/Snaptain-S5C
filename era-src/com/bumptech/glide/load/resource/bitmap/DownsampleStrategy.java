package com.bumptech.glide.load.resource.bitmap;

import android.os.Build;
import com.bumptech.glide.load.Option;

public abstract class DownsampleStrategy {
  static {
    CENTER_INSIDE = new CenterInside();
    CENTER_OUTSIDE = new CenterOutside();
    NONE = new None();
    DEFAULT = CENTER_OUTSIDE;
    OPTION = Option.memory("com.bumptech.glide.load.resource.bitmap.Downsampler.DownsampleStrategy", DEFAULT);
    if (Build.VERSION.SDK_INT >= 19) {
      bool = true;
    } else {
      bool = false;
    } 
    IS_BITMAP_FACTORY_SCALING_SUPPORTED = bool;
  }
  
  public abstract SampleSizeRounding getSampleSizeRounding(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  public abstract float getScaleFactor(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  static {
    boolean bool;
  }
  
  public static final DownsampleStrategy AT_LEAST = new AtLeast();
  
  public static final DownsampleStrategy AT_MOST = new AtMost();
  
  public static final DownsampleStrategy CENTER_INSIDE;
  
  public static final DownsampleStrategy CENTER_OUTSIDE;
  
  public static final DownsampleStrategy DEFAULT;
  
  public static final DownsampleStrategy FIT_CENTER = new FitCenter();
  
  static final boolean IS_BITMAP_FACTORY_SCALING_SUPPORTED;
  
  public static final DownsampleStrategy NONE;
  
  public static final Option<DownsampleStrategy> OPTION;
  
  private static class AtLeast extends DownsampleStrategy {
    public DownsampleStrategy.SampleSizeRounding getSampleSizeRounding(int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
      return DownsampleStrategy.SampleSizeRounding.QUALITY;
    }
    
    public float getScaleFactor(int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
      param1Int1 = Math.min(param1Int2 / param1Int4, param1Int1 / param1Int3);
      float f = 1.0F;
      if (param1Int1 != 0)
        f = 1.0F / Integer.highestOneBit(param1Int1); 
      return f;
    }
  }
  
  private static class AtMost extends DownsampleStrategy {
    public DownsampleStrategy.SampleSizeRounding getSampleSizeRounding(int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
      return DownsampleStrategy.SampleSizeRounding.MEMORY;
    }
    
    public float getScaleFactor(int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
      param1Int3 = (int)Math.ceil(Math.max(param1Int2 / param1Int4, param1Int1 / param1Int3));
      param1Int2 = Integer.highestOneBit(param1Int3);
      param1Int1 = 1;
      param1Int2 = Math.max(1, param1Int2);
      if (param1Int2 >= param1Int3)
        param1Int1 = 0; 
      return 1.0F / (param1Int2 << param1Int1);
    }
  }
  
  private static class CenterInside extends DownsampleStrategy {
    public DownsampleStrategy.SampleSizeRounding getSampleSizeRounding(int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
      DownsampleStrategy.SampleSizeRounding sampleSizeRounding;
      if (getScaleFactor(param1Int1, param1Int2, param1Int3, param1Int4) == 1.0F) {
        sampleSizeRounding = DownsampleStrategy.SampleSizeRounding.QUALITY;
      } else {
        sampleSizeRounding = FIT_CENTER.getSampleSizeRounding(param1Int1, param1Int2, param1Int3, param1Int4);
      } 
      return sampleSizeRounding;
    }
    
    public float getScaleFactor(int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
      return Math.min(1.0F, FIT_CENTER.getScaleFactor(param1Int1, param1Int2, param1Int3, param1Int4));
    }
  }
  
  private static class CenterOutside extends DownsampleStrategy {
    public DownsampleStrategy.SampleSizeRounding getSampleSizeRounding(int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
      return DownsampleStrategy.SampleSizeRounding.QUALITY;
    }
    
    public float getScaleFactor(int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
      return Math.max(param1Int3 / param1Int1, param1Int4 / param1Int2);
    }
  }
  
  private static class FitCenter extends DownsampleStrategy {
    public DownsampleStrategy.SampleSizeRounding getSampleSizeRounding(int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
      return IS_BITMAP_FACTORY_SCALING_SUPPORTED ? DownsampleStrategy.SampleSizeRounding.QUALITY : DownsampleStrategy.SampleSizeRounding.MEMORY;
    }
    
    public float getScaleFactor(int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
      if (IS_BITMAP_FACTORY_SCALING_SUPPORTED)
        return Math.min(param1Int3 / param1Int1, param1Int4 / param1Int2); 
      param1Int1 = Math.max(param1Int2 / param1Int4, param1Int1 / param1Int3);
      float f = 1.0F;
      if (param1Int1 != 0)
        f = 1.0F / Integer.highestOneBit(param1Int1); 
      return f;
    }
  }
  
  private static class None extends DownsampleStrategy {
    public DownsampleStrategy.SampleSizeRounding getSampleSizeRounding(int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
      return DownsampleStrategy.SampleSizeRounding.QUALITY;
    }
    
    public float getScaleFactor(int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
      return 1.0F;
    }
  }
  
  public enum SampleSizeRounding {
    MEMORY, QUALITY;
    
    static {
    
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/resource/bitmap/DownsampleStrategy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */