package com.bumptech.glide.load.engine;

import androidx.core.util.Pools;
import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.util.pool.FactoryPools;
import com.bumptech.glide.util.pool.StateVerifier;

final class LockedResource<Z> implements Resource<Z>, FactoryPools.Poolable {
  private static final Pools.Pool<LockedResource<?>> POOL = FactoryPools.threadSafe(20, new FactoryPools.Factory<LockedResource<?>>() {
        public LockedResource<?> create() {
          return new LockedResource();
        }
      });
  
  private boolean isLocked;
  
  private boolean isRecycled;
  
  private final StateVerifier stateVerifier = StateVerifier.newInstance();
  
  private Resource<Z> toWrap;
  
  private void init(Resource<Z> paramResource) {
    this.isRecycled = false;
    this.isLocked = true;
    this.toWrap = paramResource;
  }
  
  static <Z> LockedResource<Z> obtain(Resource<Z> paramResource) {
    LockedResource<Z> lockedResource = (LockedResource)Preconditions.checkNotNull(POOL.acquire());
    lockedResource.init(paramResource);
    return lockedResource;
  }
  
  private void release() {
    this.toWrap = null;
    POOL.release(this);
  }
  
  public Z get() {
    return this.toWrap.get();
  }
  
  public Class<Z> getResourceClass() {
    return this.toWrap.getResourceClass();
  }
  
  public int getSize() {
    return this.toWrap.getSize();
  }
  
  public StateVerifier getVerifier() {
    return this.stateVerifier;
  }
  
  public void recycle() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield stateVerifier : Lcom/bumptech/glide/util/pool/StateVerifier;
    //   6: invokevirtual throwIfRecycled : ()V
    //   9: aload_0
    //   10: iconst_1
    //   11: putfield isRecycled : Z
    //   14: aload_0
    //   15: getfield isLocked : Z
    //   18: ifne -> 34
    //   21: aload_0
    //   22: getfield toWrap : Lcom/bumptech/glide/load/engine/Resource;
    //   25: invokeinterface recycle : ()V
    //   30: aload_0
    //   31: invokespecial release : ()V
    //   34: aload_0
    //   35: monitorexit
    //   36: return
    //   37: astore_1
    //   38: aload_0
    //   39: monitorexit
    //   40: aload_1
    //   41: athrow
    // Exception table:
    //   from	to	target	type
    //   2	34	37	finally
  }
  
  void unlock() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield stateVerifier : Lcom/bumptech/glide/util/pool/StateVerifier;
    //   6: invokevirtual throwIfRecycled : ()V
    //   9: aload_0
    //   10: getfield isLocked : Z
    //   13: ifeq -> 35
    //   16: aload_0
    //   17: iconst_0
    //   18: putfield isLocked : Z
    //   21: aload_0
    //   22: getfield isRecycled : Z
    //   25: ifeq -> 32
    //   28: aload_0
    //   29: invokevirtual recycle : ()V
    //   32: aload_0
    //   33: monitorexit
    //   34: return
    //   35: new java/lang/IllegalStateException
    //   38: astore_1
    //   39: aload_1
    //   40: ldc 'Already unlocked'
    //   42: invokespecial <init> : (Ljava/lang/String;)V
    //   45: aload_1
    //   46: athrow
    //   47: astore_1
    //   48: aload_0
    //   49: monitorexit
    //   50: aload_1
    //   51: athrow
    // Exception table:
    //   from	to	target	type
    //   2	32	47	finally
    //   35	47	47	finally
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/engine/LockedResource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */