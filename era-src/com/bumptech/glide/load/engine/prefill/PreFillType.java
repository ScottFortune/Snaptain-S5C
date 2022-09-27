package com.bumptech.glide.load.engine.prefill;

import android.graphics.Bitmap;
import com.bumptech.glide.util.Preconditions;

public final class PreFillType {
  static final Bitmap.Config DEFAULT_CONFIG = Bitmap.Config.RGB_565;
  
  private final Bitmap.Config config;
  
  private final int height;
  
  private final int weight;
  
  private final int width;
  
  PreFillType(int paramInt1, int paramInt2, Bitmap.Config paramConfig, int paramInt3) {
    this.config = (Bitmap.Config)Preconditions.checkNotNull(paramConfig, "Config must not be null");
    this.width = paramInt1;
    this.height = paramInt2;
    this.weight = paramInt3;
  }
  
  public boolean equals(Object paramObject) {
    boolean bool = paramObject instanceof PreFillType;
    boolean bool1 = false;
    boolean bool2 = bool1;
    if (bool) {
      paramObject = paramObject;
      bool2 = bool1;
      if (this.height == ((PreFillType)paramObject).height) {
        bool2 = bool1;
        if (this.width == ((PreFillType)paramObject).width) {
          bool2 = bool1;
          if (this.weight == ((PreFillType)paramObject).weight) {
            bool2 = bool1;
            if (this.config == ((PreFillType)paramObject).config)
              bool2 = true; 
          } 
        } 
      } 
    } 
    return bool2;
  }
  
  Bitmap.Config getConfig() {
    return this.config;
  }
  
  int getHeight() {
    return this.height;
  }
  
  int getWeight() {
    return this.weight;
  }
  
  int getWidth() {
    return this.width;
  }
  
  public int hashCode() {
    return ((this.width * 31 + this.height) * 31 + this.config.hashCode()) * 31 + this.weight;
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("PreFillSize{width=");
    stringBuilder.append(this.width);
    stringBuilder.append(", height=");
    stringBuilder.append(this.height);
    stringBuilder.append(", config=");
    stringBuilder.append(this.config);
    stringBuilder.append(", weight=");
    stringBuilder.append(this.weight);
    stringBuilder.append('}');
    return stringBuilder.toString();
  }
  
  public static class Builder {
    private Bitmap.Config config;
    
    private final int height;
    
    private int weight = 1;
    
    private final int width;
    
    public Builder(int param1Int) {
      this(param1Int, param1Int);
    }
    
    public Builder(int param1Int1, int param1Int2) {
      if (param1Int1 > 0) {
        if (param1Int2 > 0) {
          this.width = param1Int1;
          this.height = param1Int2;
          return;
        } 
        throw new IllegalArgumentException("Height must be > 0");
      } 
      throw new IllegalArgumentException("Width must be > 0");
    }
    
    PreFillType build() {
      return new PreFillType(this.width, this.height, this.config, this.weight);
    }
    
    Bitmap.Config getConfig() {
      return this.config;
    }
    
    public Builder setConfig(Bitmap.Config param1Config) {
      this.config = param1Config;
      return this;
    }
    
    public Builder setWeight(int param1Int) {
      if (param1Int > 0) {
        this.weight = param1Int;
        return this;
      } 
      throw new IllegalArgumentException("Weight must be > 0");
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/engine/prefill/PreFillType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */