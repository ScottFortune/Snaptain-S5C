package com.bumptech.glide.load;

import com.bumptech.glide.util.Preconditions;
import java.security.MessageDigest;

public final class Option<T> {
  private static final CacheKeyUpdater<Object> EMPTY_UPDATER = new CacheKeyUpdater() {
      public void update(byte[] param1ArrayOfbyte, Object param1Object, MessageDigest param1MessageDigest) {}
    };
  
  private final CacheKeyUpdater<T> cacheKeyUpdater;
  
  private final T defaultValue;
  
  private final String key;
  
  private volatile byte[] keyBytes;
  
  private Option(String paramString, T paramT, CacheKeyUpdater<T> paramCacheKeyUpdater) {
    this.key = Preconditions.checkNotEmpty(paramString);
    this.defaultValue = paramT;
    this.cacheKeyUpdater = (CacheKeyUpdater<T>)Preconditions.checkNotNull(paramCacheKeyUpdater);
  }
  
  public static <T> Option<T> disk(String paramString, CacheKeyUpdater<T> paramCacheKeyUpdater) {
    return new Option<T>(paramString, null, paramCacheKeyUpdater);
  }
  
  public static <T> Option<T> disk(String paramString, T paramT, CacheKeyUpdater<T> paramCacheKeyUpdater) {
    return new Option<T>(paramString, paramT, paramCacheKeyUpdater);
  }
  
  private static <T> CacheKeyUpdater<T> emptyUpdater() {
    return (CacheKeyUpdater)EMPTY_UPDATER;
  }
  
  private byte[] getKeyBytes() {
    if (this.keyBytes == null)
      this.keyBytes = this.key.getBytes(Key.CHARSET); 
    return this.keyBytes;
  }
  
  public static <T> Option<T> memory(String paramString) {
    return new Option<T>(paramString, null, emptyUpdater());
  }
  
  public static <T> Option<T> memory(String paramString, T paramT) {
    return new Option<T>(paramString, paramT, emptyUpdater());
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject instanceof Option) {
      paramObject = paramObject;
      return this.key.equals(((Option)paramObject).key);
    } 
    return false;
  }
  
  public T getDefaultValue() {
    return this.defaultValue;
  }
  
  public int hashCode() {
    return this.key.hashCode();
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Option{key='");
    stringBuilder.append(this.key);
    stringBuilder.append('\'');
    stringBuilder.append('}');
    return stringBuilder.toString();
  }
  
  public void update(T paramT, MessageDigest paramMessageDigest) {
    this.cacheKeyUpdater.update(getKeyBytes(), paramT, paramMessageDigest);
  }
  
  public static interface CacheKeyUpdater<T> {
    void update(byte[] param1ArrayOfbyte, T param1T, MessageDigest param1MessageDigest);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/Option.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */