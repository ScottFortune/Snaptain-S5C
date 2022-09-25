package com.bumptech.glide.load.engine;

import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.util.Preconditions;
import java.security.MessageDigest;
import java.util.Map;

class EngineKey implements Key {
  private int hashCode;
  
  private final int height;
  
  private final Object model;
  
  private final Options options;
  
  private final Class<?> resourceClass;
  
  private final Key signature;
  
  private final Class<?> transcodeClass;
  
  private final Map<Class<?>, Transformation<?>> transformations;
  
  private final int width;
  
  EngineKey(Object paramObject, Key paramKey, int paramInt1, int paramInt2, Map<Class<?>, Transformation<?>> paramMap, Class<?> paramClass1, Class<?> paramClass2, Options paramOptions) {
    this.model = Preconditions.checkNotNull(paramObject);
    this.signature = (Key)Preconditions.checkNotNull(paramKey, "Signature must not be null");
    this.width = paramInt1;
    this.height = paramInt2;
    this.transformations = (Map<Class<?>, Transformation<?>>)Preconditions.checkNotNull(paramMap);
    this.resourceClass = (Class)Preconditions.checkNotNull(paramClass1, "Resource class must not be null");
    this.transcodeClass = (Class)Preconditions.checkNotNull(paramClass2, "Transcode class must not be null");
    this.options = (Options)Preconditions.checkNotNull(paramOptions);
  }
  
  public boolean equals(Object paramObject) {
    boolean bool = paramObject instanceof EngineKey;
    boolean bool1 = false;
    boolean bool2 = bool1;
    if (bool) {
      paramObject = paramObject;
      bool2 = bool1;
      if (this.model.equals(((EngineKey)paramObject).model)) {
        bool2 = bool1;
        if (this.signature.equals(((EngineKey)paramObject).signature)) {
          bool2 = bool1;
          if (this.height == ((EngineKey)paramObject).height) {
            bool2 = bool1;
            if (this.width == ((EngineKey)paramObject).width) {
              bool2 = bool1;
              if (this.transformations.equals(((EngineKey)paramObject).transformations)) {
                bool2 = bool1;
                if (this.resourceClass.equals(((EngineKey)paramObject).resourceClass)) {
                  bool2 = bool1;
                  if (this.transcodeClass.equals(((EngineKey)paramObject).transcodeClass)) {
                    bool2 = bool1;
                    if (this.options.equals(((EngineKey)paramObject).options))
                      bool2 = true; 
                  } 
                } 
              } 
            } 
          } 
        } 
      } 
    } 
    return bool2;
  }
  
  public int hashCode() {
    if (this.hashCode == 0) {
      this.hashCode = this.model.hashCode();
      this.hashCode = this.hashCode * 31 + this.signature.hashCode();
      this.hashCode = this.hashCode * 31 + this.width;
      this.hashCode = this.hashCode * 31 + this.height;
      this.hashCode = this.hashCode * 31 + this.transformations.hashCode();
      this.hashCode = this.hashCode * 31 + this.resourceClass.hashCode();
      this.hashCode = this.hashCode * 31 + this.transcodeClass.hashCode();
      this.hashCode = this.hashCode * 31 + this.options.hashCode();
    } 
    return this.hashCode;
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("EngineKey{model=");
    stringBuilder.append(this.model);
    stringBuilder.append(", width=");
    stringBuilder.append(this.width);
    stringBuilder.append(", height=");
    stringBuilder.append(this.height);
    stringBuilder.append(", resourceClass=");
    stringBuilder.append(this.resourceClass);
    stringBuilder.append(", transcodeClass=");
    stringBuilder.append(this.transcodeClass);
    stringBuilder.append(", signature=");
    stringBuilder.append(this.signature);
    stringBuilder.append(", hashCode=");
    stringBuilder.append(this.hashCode);
    stringBuilder.append(", transformations=");
    stringBuilder.append(this.transformations);
    stringBuilder.append(", options=");
    stringBuilder.append(this.options);
    stringBuilder.append('}');
    return stringBuilder.toString();
  }
  
  public void updateDiskCacheKey(MessageDigest paramMessageDigest) {
    throw new UnsupportedOperationException();
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/engine/EngineKey.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */