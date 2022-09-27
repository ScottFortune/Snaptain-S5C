package com.bumptech.glide.signature;

import com.bumptech.glide.load.Key;
import com.bumptech.glide.util.Preconditions;
import java.security.MessageDigest;

public final class ObjectKey implements Key {
  private final Object object;
  
  public ObjectKey(Object paramObject) {
    this.object = Preconditions.checkNotNull(paramObject);
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject instanceof ObjectKey) {
      paramObject = paramObject;
      return this.object.equals(((ObjectKey)paramObject).object);
    } 
    return false;
  }
  
  public int hashCode() {
    return this.object.hashCode();
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("ObjectKey{object=");
    stringBuilder.append(this.object);
    stringBuilder.append('}');
    return stringBuilder.toString();
  }
  
  public void updateDiskCacheKey(MessageDigest paramMessageDigest) {
    paramMessageDigest.update(this.object.toString().getBytes(CHARSET));
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/signature/ObjectKey.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */