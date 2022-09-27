package com.bumptech.glide.load.engine;

import com.bumptech.glide.load.Key;
import java.security.MessageDigest;

final class DataCacheKey implements Key {
  private final Key signature;
  
  private final Key sourceKey;
  
  DataCacheKey(Key paramKey1, Key paramKey2) {
    this.sourceKey = paramKey1;
    this.signature = paramKey2;
  }
  
  public boolean equals(Object paramObject) {
    boolean bool = paramObject instanceof DataCacheKey;
    boolean bool1 = false;
    boolean bool2 = bool1;
    if (bool) {
      paramObject = paramObject;
      bool2 = bool1;
      if (this.sourceKey.equals(((DataCacheKey)paramObject).sourceKey)) {
        bool2 = bool1;
        if (this.signature.equals(((DataCacheKey)paramObject).signature))
          bool2 = true; 
      } 
    } 
    return bool2;
  }
  
  Key getSourceKey() {
    return this.sourceKey;
  }
  
  public int hashCode() {
    return this.sourceKey.hashCode() * 31 + this.signature.hashCode();
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("DataCacheKey{sourceKey=");
    stringBuilder.append(this.sourceKey);
    stringBuilder.append(", signature=");
    stringBuilder.append(this.signature);
    stringBuilder.append('}');
    return stringBuilder.toString();
  }
  
  public void updateDiskCacheKey(MessageDigest paramMessageDigest) {
    this.sourceKey.updateDiskCacheKey(paramMessageDigest);
    this.signature.updateDiskCacheKey(paramMessageDigest);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/engine/DataCacheKey.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */