package com.bumptech.glide.load.model;

import androidx.core.util.Pools;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.util.Preconditions;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MultiModelLoaderFactory {
  private static final Factory DEFAULT_FACTORY = new Factory();
  
  private static final ModelLoader<Object, Object> EMPTY_MODEL_LOADER = new EmptyModelLoader();
  
  private final Set<Entry<?, ?>> alreadyUsedEntries = new HashSet<Entry<?, ?>>();
  
  private final List<Entry<?, ?>> entries = new ArrayList<Entry<?, ?>>();
  
  private final Factory factory;
  
  private final Pools.Pool<List<Throwable>> throwableListPool;
  
  public MultiModelLoaderFactory(Pools.Pool<List<Throwable>> paramPool) {
    this(paramPool, DEFAULT_FACTORY);
  }
  
  MultiModelLoaderFactory(Pools.Pool<List<Throwable>> paramPool, Factory paramFactory) {
    this.throwableListPool = paramPool;
    this.factory = paramFactory;
  }
  
  private <Model, Data> void add(Class<Model> paramClass, Class<Data> paramClass1, ModelLoaderFactory<? extends Model, ? extends Data> paramModelLoaderFactory, boolean paramBoolean) {
    boolean bool;
    Entry<Model, Data> entry = new Entry<Model, Data>(paramClass, paramClass1, paramModelLoaderFactory);
    List<Entry<?, ?>> list = this.entries;
    if (paramBoolean) {
      bool = list.size();
    } else {
      bool = false;
    } 
    list.add(bool, entry);
  }
  
  private <Model, Data> ModelLoader<Model, Data> build(Entry<?, ?> paramEntry) {
    return (ModelLoader<Model, Data>)Preconditions.checkNotNull(paramEntry.factory.build(this));
  }
  
  private static <Model, Data> ModelLoader<Model, Data> emptyModelLoader() {
    return (ModelLoader)EMPTY_MODEL_LOADER;
  }
  
  private <Model, Data> ModelLoaderFactory<Model, Data> getFactory(Entry<?, ?> paramEntry) {
    return (ModelLoaderFactory)paramEntry.factory;
  }
  
  <Model, Data> void append(Class<Model> paramClass, Class<Data> paramClass1, ModelLoaderFactory<? extends Model, ? extends Data> paramModelLoaderFactory) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: aload_2
    //   5: aload_3
    //   6: iconst_1
    //   7: invokespecial add : (Ljava/lang/Class;Ljava/lang/Class;Lcom/bumptech/glide/load/model/ModelLoaderFactory;Z)V
    //   10: aload_0
    //   11: monitorexit
    //   12: return
    //   13: astore_1
    //   14: aload_0
    //   15: monitorexit
    //   16: aload_1
    //   17: athrow
    // Exception table:
    //   from	to	target	type
    //   2	10	13	finally
  }
  
  public <Model, Data> ModelLoader<Model, Data> build(Class<Model> paramClass, Class<Data> paramClass1) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: new java/util/ArrayList
    //   5: astore_3
    //   6: aload_3
    //   7: invokespecial <init> : ()V
    //   10: aload_0
    //   11: getfield entries : Ljava/util/List;
    //   14: invokeinterface iterator : ()Ljava/util/Iterator;
    //   19: astore #4
    //   21: iconst_0
    //   22: istore #5
    //   24: aload #4
    //   26: invokeinterface hasNext : ()Z
    //   31: ifeq -> 116
    //   34: aload #4
    //   36: invokeinterface next : ()Ljava/lang/Object;
    //   41: checkcast com/bumptech/glide/load/model/MultiModelLoaderFactory$Entry
    //   44: astore #6
    //   46: aload_0
    //   47: getfield alreadyUsedEntries : Ljava/util/Set;
    //   50: aload #6
    //   52: invokeinterface contains : (Ljava/lang/Object;)Z
    //   57: ifeq -> 66
    //   60: iconst_1
    //   61: istore #5
    //   63: goto -> 24
    //   66: aload #6
    //   68: aload_1
    //   69: aload_2
    //   70: invokevirtual handles : (Ljava/lang/Class;Ljava/lang/Class;)Z
    //   73: ifeq -> 24
    //   76: aload_0
    //   77: getfield alreadyUsedEntries : Ljava/util/Set;
    //   80: aload #6
    //   82: invokeinterface add : (Ljava/lang/Object;)Z
    //   87: pop
    //   88: aload_3
    //   89: aload_0
    //   90: aload #6
    //   92: invokespecial build : (Lcom/bumptech/glide/load/model/MultiModelLoaderFactory$Entry;)Lcom/bumptech/glide/load/model/ModelLoader;
    //   95: invokeinterface add : (Ljava/lang/Object;)Z
    //   100: pop
    //   101: aload_0
    //   102: getfield alreadyUsedEntries : Ljava/util/Set;
    //   105: aload #6
    //   107: invokeinterface remove : (Ljava/lang/Object;)Z
    //   112: pop
    //   113: goto -> 24
    //   116: aload_3
    //   117: invokeinterface size : ()I
    //   122: iconst_1
    //   123: if_icmple -> 143
    //   126: aload_0
    //   127: getfield factory : Lcom/bumptech/glide/load/model/MultiModelLoaderFactory$Factory;
    //   130: aload_3
    //   131: aload_0
    //   132: getfield throwableListPool : Landroidx/core/util/Pools$Pool;
    //   135: invokevirtual build : (Ljava/util/List;Landroidx/core/util/Pools$Pool;)Lcom/bumptech/glide/load/model/MultiModelLoader;
    //   138: astore_1
    //   139: aload_0
    //   140: monitorexit
    //   141: aload_1
    //   142: areturn
    //   143: aload_3
    //   144: invokeinterface size : ()I
    //   149: iconst_1
    //   150: if_icmpne -> 168
    //   153: aload_3
    //   154: iconst_0
    //   155: invokeinterface get : (I)Ljava/lang/Object;
    //   160: checkcast com/bumptech/glide/load/model/ModelLoader
    //   163: astore_1
    //   164: aload_0
    //   165: monitorexit
    //   166: aload_1
    //   167: areturn
    //   168: iload #5
    //   170: ifeq -> 181
    //   173: invokestatic emptyModelLoader : ()Lcom/bumptech/glide/load/model/ModelLoader;
    //   176: astore_1
    //   177: aload_0
    //   178: monitorexit
    //   179: aload_1
    //   180: areturn
    //   181: new com/bumptech/glide/Registry$NoModelLoaderAvailableException
    //   184: astore_3
    //   185: aload_3
    //   186: aload_1
    //   187: aload_2
    //   188: invokespecial <init> : (Ljava/lang/Class;Ljava/lang/Class;)V
    //   191: aload_3
    //   192: athrow
    //   193: astore_1
    //   194: aload_0
    //   195: getfield alreadyUsedEntries : Ljava/util/Set;
    //   198: invokeinterface clear : ()V
    //   203: aload_1
    //   204: athrow
    //   205: astore_1
    //   206: aload_0
    //   207: monitorexit
    //   208: goto -> 213
    //   211: aload_1
    //   212: athrow
    //   213: goto -> 211
    // Exception table:
    //   from	to	target	type
    //   2	21	193	finally
    //   24	60	193	finally
    //   66	113	193	finally
    //   116	139	193	finally
    //   143	164	193	finally
    //   173	177	193	finally
    //   181	193	193	finally
    //   194	205	205	finally
  }
  
  <Model> List<ModelLoader<Model, ?>> build(Class<Model> paramClass) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: new java/util/ArrayList
    //   5: astore_2
    //   6: aload_2
    //   7: invokespecial <init> : ()V
    //   10: aload_0
    //   11: getfield entries : Ljava/util/List;
    //   14: invokeinterface iterator : ()Ljava/util/Iterator;
    //   19: astore_3
    //   20: aload_3
    //   21: invokeinterface hasNext : ()Z
    //   26: ifeq -> 106
    //   29: aload_3
    //   30: invokeinterface next : ()Ljava/lang/Object;
    //   35: checkcast com/bumptech/glide/load/model/MultiModelLoaderFactory$Entry
    //   38: astore #4
    //   40: aload_0
    //   41: getfield alreadyUsedEntries : Ljava/util/Set;
    //   44: aload #4
    //   46: invokeinterface contains : (Ljava/lang/Object;)Z
    //   51: ifeq -> 57
    //   54: goto -> 20
    //   57: aload #4
    //   59: aload_1
    //   60: invokevirtual handles : (Ljava/lang/Class;)Z
    //   63: ifeq -> 20
    //   66: aload_0
    //   67: getfield alreadyUsedEntries : Ljava/util/Set;
    //   70: aload #4
    //   72: invokeinterface add : (Ljava/lang/Object;)Z
    //   77: pop
    //   78: aload_2
    //   79: aload_0
    //   80: aload #4
    //   82: invokespecial build : (Lcom/bumptech/glide/load/model/MultiModelLoaderFactory$Entry;)Lcom/bumptech/glide/load/model/ModelLoader;
    //   85: invokeinterface add : (Ljava/lang/Object;)Z
    //   90: pop
    //   91: aload_0
    //   92: getfield alreadyUsedEntries : Ljava/util/Set;
    //   95: aload #4
    //   97: invokeinterface remove : (Ljava/lang/Object;)Z
    //   102: pop
    //   103: goto -> 20
    //   106: aload_0
    //   107: monitorexit
    //   108: aload_2
    //   109: areturn
    //   110: astore_1
    //   111: aload_0
    //   112: getfield alreadyUsedEntries : Ljava/util/Set;
    //   115: invokeinterface clear : ()V
    //   120: aload_1
    //   121: athrow
    //   122: astore_1
    //   123: aload_0
    //   124: monitorexit
    //   125: goto -> 130
    //   128: aload_1
    //   129: athrow
    //   130: goto -> 128
    // Exception table:
    //   from	to	target	type
    //   2	20	110	finally
    //   20	54	110	finally
    //   57	103	110	finally
    //   111	122	122	finally
  }
  
  List<Class<?>> getDataClasses(Class<?> paramClass) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: new java/util/ArrayList
    //   5: astore_2
    //   6: aload_2
    //   7: invokespecial <init> : ()V
    //   10: aload_0
    //   11: getfield entries : Ljava/util/List;
    //   14: invokeinterface iterator : ()Ljava/util/Iterator;
    //   19: astore_3
    //   20: aload_3
    //   21: invokeinterface hasNext : ()Z
    //   26: ifeq -> 78
    //   29: aload_3
    //   30: invokeinterface next : ()Ljava/lang/Object;
    //   35: checkcast com/bumptech/glide/load/model/MultiModelLoaderFactory$Entry
    //   38: astore #4
    //   40: aload_2
    //   41: aload #4
    //   43: getfield dataClass : Ljava/lang/Class;
    //   46: invokeinterface contains : (Ljava/lang/Object;)Z
    //   51: ifne -> 20
    //   54: aload #4
    //   56: aload_1
    //   57: invokevirtual handles : (Ljava/lang/Class;)Z
    //   60: ifeq -> 20
    //   63: aload_2
    //   64: aload #4
    //   66: getfield dataClass : Ljava/lang/Class;
    //   69: invokeinterface add : (Ljava/lang/Object;)Z
    //   74: pop
    //   75: goto -> 20
    //   78: aload_0
    //   79: monitorexit
    //   80: aload_2
    //   81: areturn
    //   82: astore_1
    //   83: aload_0
    //   84: monitorexit
    //   85: goto -> 90
    //   88: aload_1
    //   89: athrow
    //   90: goto -> 88
    // Exception table:
    //   from	to	target	type
    //   2	20	82	finally
    //   20	75	82	finally
  }
  
  <Model, Data> void prepend(Class<Model> paramClass, Class<Data> paramClass1, ModelLoaderFactory<? extends Model, ? extends Data> paramModelLoaderFactory) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: aload_2
    //   5: aload_3
    //   6: iconst_0
    //   7: invokespecial add : (Ljava/lang/Class;Ljava/lang/Class;Lcom/bumptech/glide/load/model/ModelLoaderFactory;Z)V
    //   10: aload_0
    //   11: monitorexit
    //   12: return
    //   13: astore_1
    //   14: aload_0
    //   15: monitorexit
    //   16: aload_1
    //   17: athrow
    // Exception table:
    //   from	to	target	type
    //   2	10	13	finally
  }
  
  <Model, Data> List<ModelLoaderFactory<? extends Model, ? extends Data>> remove(Class<Model> paramClass, Class<Data> paramClass1) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: new java/util/ArrayList
    //   5: astore_3
    //   6: aload_3
    //   7: invokespecial <init> : ()V
    //   10: aload_0
    //   11: getfield entries : Ljava/util/List;
    //   14: invokeinterface iterator : ()Ljava/util/Iterator;
    //   19: astore #4
    //   21: aload #4
    //   23: invokeinterface hasNext : ()Z
    //   28: ifeq -> 76
    //   31: aload #4
    //   33: invokeinterface next : ()Ljava/lang/Object;
    //   38: checkcast com/bumptech/glide/load/model/MultiModelLoaderFactory$Entry
    //   41: astore #5
    //   43: aload #5
    //   45: aload_1
    //   46: aload_2
    //   47: invokevirtual handles : (Ljava/lang/Class;Ljava/lang/Class;)Z
    //   50: ifeq -> 21
    //   53: aload #4
    //   55: invokeinterface remove : ()V
    //   60: aload_3
    //   61: aload_0
    //   62: aload #5
    //   64: invokespecial getFactory : (Lcom/bumptech/glide/load/model/MultiModelLoaderFactory$Entry;)Lcom/bumptech/glide/load/model/ModelLoaderFactory;
    //   67: invokeinterface add : (Ljava/lang/Object;)Z
    //   72: pop
    //   73: goto -> 21
    //   76: aload_0
    //   77: monitorexit
    //   78: aload_3
    //   79: areturn
    //   80: astore_1
    //   81: aload_0
    //   82: monitorexit
    //   83: goto -> 88
    //   86: aload_1
    //   87: athrow
    //   88: goto -> 86
    // Exception table:
    //   from	to	target	type
    //   2	21	80	finally
    //   21	73	80	finally
  }
  
  <Model, Data> List<ModelLoaderFactory<? extends Model, ? extends Data>> replace(Class<Model> paramClass, Class<Data> paramClass1, ModelLoaderFactory<? extends Model, ? extends Data> paramModelLoaderFactory) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: aload_2
    //   5: invokevirtual remove : (Ljava/lang/Class;Ljava/lang/Class;)Ljava/util/List;
    //   8: astore #4
    //   10: aload_0
    //   11: aload_1
    //   12: aload_2
    //   13: aload_3
    //   14: invokevirtual append : (Ljava/lang/Class;Ljava/lang/Class;Lcom/bumptech/glide/load/model/ModelLoaderFactory;)V
    //   17: aload_0
    //   18: monitorexit
    //   19: aload #4
    //   21: areturn
    //   22: astore_1
    //   23: aload_0
    //   24: monitorexit
    //   25: aload_1
    //   26: athrow
    // Exception table:
    //   from	to	target	type
    //   2	17	22	finally
  }
  
  private static class EmptyModelLoader implements ModelLoader<Object, Object> {
    public ModelLoader.LoadData<Object> buildLoadData(Object param1Object, int param1Int1, int param1Int2, Options param1Options) {
      return null;
    }
    
    public boolean handles(Object param1Object) {
      return false;
    }
  }
  
  private static class Entry<Model, Data> {
    final Class<Data> dataClass;
    
    final ModelLoaderFactory<? extends Model, ? extends Data> factory;
    
    private final Class<Model> modelClass;
    
    public Entry(Class<Model> param1Class, Class<Data> param1Class1, ModelLoaderFactory<? extends Model, ? extends Data> param1ModelLoaderFactory) {
      this.modelClass = param1Class;
      this.dataClass = param1Class1;
      this.factory = param1ModelLoaderFactory;
    }
    
    public boolean handles(Class<?> param1Class) {
      return this.modelClass.isAssignableFrom(param1Class);
    }
    
    public boolean handles(Class<?> param1Class1, Class<?> param1Class2) {
      boolean bool;
      if (handles(param1Class1) && this.dataClass.isAssignableFrom(param1Class2)) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
  }
  
  static class Factory {
    public <Model, Data> MultiModelLoader<Model, Data> build(List<ModelLoader<Model, Data>> param1List, Pools.Pool<List<Throwable>> param1Pool) {
      return new MultiModelLoader<Model, Data>(param1List, param1Pool);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/model/MultiModelLoaderFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */