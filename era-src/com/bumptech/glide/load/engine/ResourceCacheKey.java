package com.bumptech.glide.load.engine;

import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.util.LruCache;
import com.bumptech.glide.util.Util;
import java.nio.ByteBuffer;
import java.security.MessageDigest;

final class ResourceCacheKey implements Key {
  private static final LruCache<Class<?>, byte[]> RESOURCE_CLASS_BYTES = new LruCache(50L);
  
  private final ArrayPool arrayPool;
  
  private final Class<?> decodedResourceClass;
  
  private final int height;
  
  private final Options options;
  
  private final Key signature;
  
  private final Key sourceKey;
  
  private final Transformation<?> transformation;
  
  private final int width;
  
  ResourceCacheKey(ArrayPool paramArrayPool, Key paramKey1, Key paramKey2, int paramInt1, int paramInt2, Transformation<?> paramTransformation, Class<?> paramClass, Options paramOptions) {
    this.arrayPool = paramArrayPool;
    this.sourceKey = paramKey1;
    this.signature = paramKey2;
    this.width = paramInt1;
    this.height = paramInt2;
    this.transformation = paramTransformation;
    this.decodedResourceClass = paramClass;
    this.options = paramOptions;
  }
  
  private byte[] getResourceClassBytes() {
    byte[] arrayOfByte1 = (byte[])RESOURCE_CLASS_BYTES.get(this.decodedResourceClass);
    byte[] arrayOfByte2 = arrayOfByte1;
    if (arrayOfByte1 == null) {
      arrayOfByte2 = this.decodedResourceClass.getName().getBytes(CHARSET);
      RESOURCE_CLASS_BYTES.put(this.decodedResourceClass, arrayOfByte2);
    } 
    return arrayOfByte2;
  }
  
  public boolean equals(Object paramObject) {
    boolean bool = paramObject instanceof ResourceCacheKey;
    boolean bool1 = false;
    boolean bool2 = bool1;
    if (bool) {
      paramObject = paramObject;
      bool2 = bool1;
      if (this.height == ((ResourceCacheKey)paramObject).height) {
        bool2 = bool1;
        if (this.width == ((ResourceCacheKey)paramObject).width) {
          bool2 = bool1;
          if (Util.bothNullOrEqual(this.transformation, ((ResourceCacheKey)paramObject).transformation)) {
            bool2 = bool1;
            if (this.decodedResourceClass.equals(((ResourceCacheKey)paramObject).decodedResourceClass)) {
              bool2 = bool1;
              if (this.sourceKey.equals(((ResourceCacheKey)paramObject).sourceKey)) {
                bool2 = bool1;
                if (this.signature.equals(((ResourceCacheKey)paramObject).signature)) {
                  bool2 = bool1;
                  if (this.options.equals(((ResourceCacheKey)paramObject).options))
                    bool2 = true; 
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
    int i = ((this.sourceKey.hashCode() * 31 + this.signature.hashCode()) * 31 + this.width) * 31 + this.height;
    Transformation<?> transformation = this.transformation;
    int j = i;
    if (transformation != null)
      j = i * 31 + transformation.hashCode(); 
    return (j * 31 + this.decodedResourceClass.hashCode()) * 31 + this.options.hashCode();
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("ResourceCacheKey{sourceKey=");
    stringBuilder.append(this.sourceKey);
    stringBuilder.append(", signature=");
    stringBuilder.append(this.signature);
    stringBuilder.append(", width=");
    stringBuilder.append(this.width);
    stringBuilder.append(", height=");
    stringBuilder.append(this.height);
    stringBuilder.append(", decodedResourceClass=");
    stringBuilder.append(this.decodedResourceClass);
    stringBuilder.append(", transformation='");
    stringBuilder.append(this.transformation);
    stringBuilder.append('\'');
    stringBuilder.append(", options=");
    stringBuilder.append(this.options);
    stringBuilder.append('}');
    return stringBuilder.toString();
  }
  
  public void updateDiskCacheKey(MessageDigest paramMessageDigest) {
    byte[] arrayOfByte = (byte[])this.arrayPool.getExact(8, byte[].class);
    ByteBuffer.wrap(arrayOfByte).putInt(this.width).putInt(this.height).array();
    this.signature.updateDiskCacheKey(paramMessageDigest);
    this.sourceKey.updateDiskCacheKey(paramMessageDigest);
    paramMessageDigest.update(arrayOfByte);
    Transformation<?> transformation = this.transformation;
    if (transformation != null)
      transformation.updateDiskCacheKey(paramMessageDigest); 
    this.options.updateDiskCacheKey(paramMessageDigest);
    paramMessageDigest.update(getResourceClassBytes());
    this.arrayPool.put(arrayOfByte);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/engine/ResourceCacheKey.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */