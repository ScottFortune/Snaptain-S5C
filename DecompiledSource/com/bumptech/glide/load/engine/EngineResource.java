package com.bumptech.glide.load.engine;

import com.bumptech.glide.load.Key;
import com.bumptech.glide.util.Preconditions;

class EngineResource<Z> implements Resource<Z> {
  private int acquired;
  
  private final boolean isMemoryCacheable;
  
  private final boolean isRecyclable;
  
  private boolean isRecycled;
  
  private final Key key;
  
  private final ResourceListener listener;
  
  private final Resource<Z> resource;
  
  EngineResource(Resource<Z> paramResource, boolean paramBoolean1, boolean paramBoolean2, Key paramKey, ResourceListener paramResourceListener) {
    this.resource = (Resource<Z>)Preconditions.checkNotNull(paramResource);
    this.isMemoryCacheable = paramBoolean1;
    this.isRecyclable = paramBoolean2;
    this.key = paramKey;
    this.listener = (ResourceListener)Preconditions.checkNotNull(paramResourceListener);
  }
  
  void acquire() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield isRecycled : Z
    //   6: ifne -> 22
    //   9: aload_0
    //   10: aload_0
    //   11: getfield acquired : I
    //   14: iconst_1
    //   15: iadd
    //   16: putfield acquired : I
    //   19: aload_0
    //   20: monitorexit
    //   21: return
    //   22: new java/lang/IllegalStateException
    //   25: astore_1
    //   26: aload_1
    //   27: ldc 'Cannot acquire a recycled resource'
    //   29: invokespecial <init> : (Ljava/lang/String;)V
    //   32: aload_1
    //   33: athrow
    //   34: astore_1
    //   35: aload_0
    //   36: monitorexit
    //   37: aload_1
    //   38: athrow
    // Exception table:
    //   from	to	target	type
    //   2	19	34	finally
    //   22	34	34	finally
  }
  
  public Z get() {
    return this.resource.get();
  }
  
  Resource<Z> getResource() {
    return this.resource;
  }
  
  public Class<Z> getResourceClass() {
    return this.resource.getResourceClass();
  }
  
  public int getSize() {
    return this.resource.getSize();
  }
  
  boolean isMemoryCacheable() {
    return this.isMemoryCacheable;
  }
  
  public void recycle() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield acquired : I
    //   6: ifgt -> 52
    //   9: aload_0
    //   10: getfield isRecycled : Z
    //   13: ifne -> 40
    //   16: aload_0
    //   17: iconst_1
    //   18: putfield isRecycled : Z
    //   21: aload_0
    //   22: getfield isRecyclable : Z
    //   25: ifeq -> 37
    //   28: aload_0
    //   29: getfield resource : Lcom/bumptech/glide/load/engine/Resource;
    //   32: invokeinterface recycle : ()V
    //   37: aload_0
    //   38: monitorexit
    //   39: return
    //   40: new java/lang/IllegalStateException
    //   43: astore_1
    //   44: aload_1
    //   45: ldc 'Cannot recycle a resource that has already been recycled'
    //   47: invokespecial <init> : (Ljava/lang/String;)V
    //   50: aload_1
    //   51: athrow
    //   52: new java/lang/IllegalStateException
    //   55: astore_1
    //   56: aload_1
    //   57: ldc 'Cannot recycle a resource while it is still acquired'
    //   59: invokespecial <init> : (Ljava/lang/String;)V
    //   62: aload_1
    //   63: athrow
    //   64: astore_1
    //   65: aload_0
    //   66: monitorexit
    //   67: aload_1
    //   68: athrow
    // Exception table:
    //   from	to	target	type
    //   2	37	64	finally
    //   40	52	64	finally
    //   52	64	64	finally
  }
  
  void release() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield acquired : I
    //   6: ifle -> 54
    //   9: aload_0
    //   10: getfield acquired : I
    //   13: istore_1
    //   14: iconst_1
    //   15: istore_2
    //   16: iinc #1, -1
    //   19: aload_0
    //   20: iload_1
    //   21: putfield acquired : I
    //   24: iload_1
    //   25: ifne -> 31
    //   28: goto -> 33
    //   31: iconst_0
    //   32: istore_2
    //   33: aload_0
    //   34: monitorexit
    //   35: iload_2
    //   36: ifeq -> 53
    //   39: aload_0
    //   40: getfield listener : Lcom/bumptech/glide/load/engine/EngineResource$ResourceListener;
    //   43: aload_0
    //   44: getfield key : Lcom/bumptech/glide/load/Key;
    //   47: aload_0
    //   48: invokeinterface onResourceReleased : (Lcom/bumptech/glide/load/Key;Lcom/bumptech/glide/load/engine/EngineResource;)V
    //   53: return
    //   54: new java/lang/IllegalStateException
    //   57: astore_3
    //   58: aload_3
    //   59: ldc 'Cannot release a recycled or not yet acquired resource'
    //   61: invokespecial <init> : (Ljava/lang/String;)V
    //   64: aload_3
    //   65: athrow
    //   66: astore_3
    //   67: aload_0
    //   68: monitorexit
    //   69: aload_3
    //   70: athrow
    // Exception table:
    //   from	to	target	type
    //   2	14	66	finally
    //   19	24	66	finally
    //   33	35	66	finally
    //   54	66	66	finally
    //   67	69	66	finally
  }
  
  public String toString() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: new java/lang/StringBuilder
    //   5: astore_1
    //   6: aload_1
    //   7: invokespecial <init> : ()V
    //   10: aload_1
    //   11: ldc 'EngineResource{isMemoryCacheable='
    //   13: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   16: pop
    //   17: aload_1
    //   18: aload_0
    //   19: getfield isMemoryCacheable : Z
    //   22: invokevirtual append : (Z)Ljava/lang/StringBuilder;
    //   25: pop
    //   26: aload_1
    //   27: ldc ', listener='
    //   29: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   32: pop
    //   33: aload_1
    //   34: aload_0
    //   35: getfield listener : Lcom/bumptech/glide/load/engine/EngineResource$ResourceListener;
    //   38: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   41: pop
    //   42: aload_1
    //   43: ldc ', key='
    //   45: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   48: pop
    //   49: aload_1
    //   50: aload_0
    //   51: getfield key : Lcom/bumptech/glide/load/Key;
    //   54: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   57: pop
    //   58: aload_1
    //   59: ldc ', acquired='
    //   61: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   64: pop
    //   65: aload_1
    //   66: aload_0
    //   67: getfield acquired : I
    //   70: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   73: pop
    //   74: aload_1
    //   75: ldc ', isRecycled='
    //   77: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   80: pop
    //   81: aload_1
    //   82: aload_0
    //   83: getfield isRecycled : Z
    //   86: invokevirtual append : (Z)Ljava/lang/StringBuilder;
    //   89: pop
    //   90: aload_1
    //   91: ldc ', resource='
    //   93: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   96: pop
    //   97: aload_1
    //   98: aload_0
    //   99: getfield resource : Lcom/bumptech/glide/load/engine/Resource;
    //   102: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   105: pop
    //   106: aload_1
    //   107: bipush #125
    //   109: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   112: pop
    //   113: aload_1
    //   114: invokevirtual toString : ()Ljava/lang/String;
    //   117: astore_1
    //   118: aload_0
    //   119: monitorexit
    //   120: aload_1
    //   121: areturn
    //   122: astore_1
    //   123: aload_0
    //   124: monitorexit
    //   125: aload_1
    //   126: athrow
    // Exception table:
    //   from	to	target	type
    //   2	118	122	finally
  }
  
  static interface ResourceListener {
    void onResourceReleased(Key param1Key, EngineResource<?> param1EngineResource);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/engine/EngineResource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */