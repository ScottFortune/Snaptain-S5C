package com.bumptech.glide.load.engine.cache;

import androidx.core.util.Pools;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.util.LruCache;
import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.util.Util;
import com.bumptech.glide.util.pool.FactoryPools;
import com.bumptech.glide.util.pool.StateVerifier;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SafeKeyGenerator {
  private final Pools.Pool<PoolableDigestContainer> digestPool = FactoryPools.threadSafe(10, new FactoryPools.Factory<PoolableDigestContainer>() {
        public SafeKeyGenerator.PoolableDigestContainer create() {
          try {
            return new SafeKeyGenerator.PoolableDigestContainer(MessageDigest.getInstance("SHA-256"));
          } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            throw new RuntimeException(noSuchAlgorithmException);
          } 
        }
      });
  
  private final LruCache<Key, String> loadIdToSafeHash = new LruCache(1000L);
  
  private String calculateHexStringDigest(Key paramKey) {
    PoolableDigestContainer poolableDigestContainer = (PoolableDigestContainer)Preconditions.checkNotNull(this.digestPool.acquire());
    try {
      paramKey.updateDiskCacheKey(poolableDigestContainer.messageDigest);
      return Util.sha256BytesToHex(poolableDigestContainer.messageDigest.digest());
    } finally {
      this.digestPool.release(poolableDigestContainer);
    } 
  }
  
  public String getSafeKey(Key paramKey) {
    LruCache<Key, String> lruCache;
    String str;
    synchronized (this.loadIdToSafeHash) {
      String str1 = (String)this.loadIdToSafeHash.get(paramKey);
      str = str1;
      if (str1 == null)
        str = calculateHexStringDigest(paramKey); 
      synchronized (this.loadIdToSafeHash) {
        this.loadIdToSafeHash.put(paramKey, str);
        return str;
      } 
    } 
  }
  
  private static final class PoolableDigestContainer implements FactoryPools.Poolable {
    final MessageDigest messageDigest;
    
    private final StateVerifier stateVerifier = StateVerifier.newInstance();
    
    PoolableDigestContainer(MessageDigest param1MessageDigest) {
      this.messageDigest = param1MessageDigest;
    }
    
    public StateVerifier getVerifier() {
      return this.stateVerifier;
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/engine/cache/SafeKeyGenerator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */