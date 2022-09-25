package com.bumptech.glide.load.model;

import androidx.core.util.Pools;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ModelLoaderRegistry {
  private final ModelLoaderCache cache = new ModelLoaderCache();
  
  private final MultiModelLoaderFactory multiModelLoaderFactory;
  
  public ModelLoaderRegistry(Pools.Pool<List<Throwable>> paramPool) {
    this(new MultiModelLoaderFactory(paramPool));
  }
  
  private ModelLoaderRegistry(MultiModelLoaderFactory paramMultiModelLoaderFactory) {
    this.multiModelLoaderFactory = paramMultiModelLoaderFactory;
  }
  
  private static <A> Class<A> getClass(A paramA) {
    return (Class)paramA.getClass();
  }
  
  private <A> List<ModelLoader<A, ?>> getModelLoadersForClass(Class<A> paramClass) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield cache : Lcom/bumptech/glide/load/model/ModelLoaderRegistry$ModelLoaderCache;
    //   6: aload_1
    //   7: invokevirtual get : (Ljava/lang/Class;)Ljava/util/List;
    //   10: astore_2
    //   11: aload_2
    //   12: astore_3
    //   13: aload_2
    //   14: ifnonnull -> 38
    //   17: aload_0
    //   18: getfield multiModelLoaderFactory : Lcom/bumptech/glide/load/model/MultiModelLoaderFactory;
    //   21: aload_1
    //   22: invokevirtual build : (Ljava/lang/Class;)Ljava/util/List;
    //   25: invokestatic unmodifiableList : (Ljava/util/List;)Ljava/util/List;
    //   28: astore_3
    //   29: aload_0
    //   30: getfield cache : Lcom/bumptech/glide/load/model/ModelLoaderRegistry$ModelLoaderCache;
    //   33: aload_1
    //   34: aload_3
    //   35: invokevirtual put : (Ljava/lang/Class;Ljava/util/List;)V
    //   38: aload_0
    //   39: monitorexit
    //   40: aload_3
    //   41: areturn
    //   42: astore_1
    //   43: aload_0
    //   44: monitorexit
    //   45: aload_1
    //   46: athrow
    // Exception table:
    //   from	to	target	type
    //   2	11	42	finally
    //   17	38	42	finally
  }
  
  private <Model, Data> void tearDown(List<ModelLoaderFactory<? extends Model, ? extends Data>> paramList) {
    Iterator<ModelLoaderFactory<? extends Model, ? extends Data>> iterator = paramList.iterator();
    while (iterator.hasNext())
      ((ModelLoaderFactory)iterator.next()).teardown(); 
  }
  
  public <Model, Data> void append(Class<Model> paramClass, Class<Data> paramClass1, ModelLoaderFactory<? extends Model, ? extends Data> paramModelLoaderFactory) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield multiModelLoaderFactory : Lcom/bumptech/glide/load/model/MultiModelLoaderFactory;
    //   6: aload_1
    //   7: aload_2
    //   8: aload_3
    //   9: invokevirtual append : (Ljava/lang/Class;Ljava/lang/Class;Lcom/bumptech/glide/load/model/ModelLoaderFactory;)V
    //   12: aload_0
    //   13: getfield cache : Lcom/bumptech/glide/load/model/ModelLoaderRegistry$ModelLoaderCache;
    //   16: invokevirtual clear : ()V
    //   19: aload_0
    //   20: monitorexit
    //   21: return
    //   22: astore_1
    //   23: aload_0
    //   24: monitorexit
    //   25: aload_1
    //   26: athrow
    // Exception table:
    //   from	to	target	type
    //   2	19	22	finally
  }
  
  public <Model, Data> ModelLoader<Model, Data> build(Class<Model> paramClass, Class<Data> paramClass1) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield multiModelLoaderFactory : Lcom/bumptech/glide/load/model/MultiModelLoaderFactory;
    //   6: aload_1
    //   7: aload_2
    //   8: invokevirtual build : (Ljava/lang/Class;Ljava/lang/Class;)Lcom/bumptech/glide/load/model/ModelLoader;
    //   11: astore_1
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_1
    //   15: areturn
    //   16: astore_1
    //   17: aload_0
    //   18: monitorexit
    //   19: aload_1
    //   20: athrow
    // Exception table:
    //   from	to	target	type
    //   2	12	16	finally
  }
  
  public List<Class<?>> getDataClasses(Class<?> paramClass) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield multiModelLoaderFactory : Lcom/bumptech/glide/load/model/MultiModelLoaderFactory;
    //   6: aload_1
    //   7: invokevirtual getDataClasses : (Ljava/lang/Class;)Ljava/util/List;
    //   10: astore_1
    //   11: aload_0
    //   12: monitorexit
    //   13: aload_1
    //   14: areturn
    //   15: astore_1
    //   16: aload_0
    //   17: monitorexit
    //   18: aload_1
    //   19: athrow
    // Exception table:
    //   from	to	target	type
    //   2	11	15	finally
  }
  
  public <A> List<ModelLoader<A, ?>> getModelLoaders(A paramA) {
    List<ModelLoader<?, ?>> list = getModelLoadersForClass(getClass(paramA));
    int i = list.size();
    List<?> list1 = Collections.emptyList();
    byte b = 0;
    boolean bool;
    for (bool = true; b < i; bool = bool1) {
      ModelLoader modelLoader = list.get(b);
      List<?> list2 = list1;
      boolean bool1 = bool;
      if (modelLoader.handles(paramA)) {
        bool1 = bool;
        if (bool) {
          list1 = new ArrayList(i - b);
          bool1 = false;
        } 
        list1.add(modelLoader);
        list2 = list1;
      } 
      b++;
      list1 = list2;
    } 
    return (List)list1;
  }
  
  public <Model, Data> void prepend(Class<Model> paramClass, Class<Data> paramClass1, ModelLoaderFactory<? extends Model, ? extends Data> paramModelLoaderFactory) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield multiModelLoaderFactory : Lcom/bumptech/glide/load/model/MultiModelLoaderFactory;
    //   6: aload_1
    //   7: aload_2
    //   8: aload_3
    //   9: invokevirtual prepend : (Ljava/lang/Class;Ljava/lang/Class;Lcom/bumptech/glide/load/model/ModelLoaderFactory;)V
    //   12: aload_0
    //   13: getfield cache : Lcom/bumptech/glide/load/model/ModelLoaderRegistry$ModelLoaderCache;
    //   16: invokevirtual clear : ()V
    //   19: aload_0
    //   20: monitorexit
    //   21: return
    //   22: astore_1
    //   23: aload_0
    //   24: monitorexit
    //   25: aload_1
    //   26: athrow
    // Exception table:
    //   from	to	target	type
    //   2	19	22	finally
  }
  
  public <Model, Data> void remove(Class<Model> paramClass, Class<Data> paramClass1) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_0
    //   4: getfield multiModelLoaderFactory : Lcom/bumptech/glide/load/model/MultiModelLoaderFactory;
    //   7: aload_1
    //   8: aload_2
    //   9: invokevirtual remove : (Ljava/lang/Class;Ljava/lang/Class;)Ljava/util/List;
    //   12: invokespecial tearDown : (Ljava/util/List;)V
    //   15: aload_0
    //   16: getfield cache : Lcom/bumptech/glide/load/model/ModelLoaderRegistry$ModelLoaderCache;
    //   19: invokevirtual clear : ()V
    //   22: aload_0
    //   23: monitorexit
    //   24: return
    //   25: astore_1
    //   26: aload_0
    //   27: monitorexit
    //   28: aload_1
    //   29: athrow
    // Exception table:
    //   from	to	target	type
    //   2	22	25	finally
  }
  
  public <Model, Data> void replace(Class<Model> paramClass, Class<Data> paramClass1, ModelLoaderFactory<? extends Model, ? extends Data> paramModelLoaderFactory) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_0
    //   4: getfield multiModelLoaderFactory : Lcom/bumptech/glide/load/model/MultiModelLoaderFactory;
    //   7: aload_1
    //   8: aload_2
    //   9: aload_3
    //   10: invokevirtual replace : (Ljava/lang/Class;Ljava/lang/Class;Lcom/bumptech/glide/load/model/ModelLoaderFactory;)Ljava/util/List;
    //   13: invokespecial tearDown : (Ljava/util/List;)V
    //   16: aload_0
    //   17: getfield cache : Lcom/bumptech/glide/load/model/ModelLoaderRegistry$ModelLoaderCache;
    //   20: invokevirtual clear : ()V
    //   23: aload_0
    //   24: monitorexit
    //   25: return
    //   26: astore_1
    //   27: aload_0
    //   28: monitorexit
    //   29: aload_1
    //   30: athrow
    // Exception table:
    //   from	to	target	type
    //   2	23	26	finally
  }
  
  private static class ModelLoaderCache {
    private final Map<Class<?>, Entry<?>> cachedModelLoaders = new HashMap<Class<?>, Entry<?>>();
    
    public void clear() {
      this.cachedModelLoaders.clear();
    }
    
    public <Model> List<ModelLoader<Model, ?>> get(Class<Model> param1Class) {
      List<ModelLoader<Model, ?>> list;
      Entry entry = this.cachedModelLoaders.get(param1Class);
      if (entry == null) {
        entry = null;
      } else {
        list = entry.loaders;
      } 
      return list;
    }
    
    public <Model> void put(Class<Model> param1Class, List<ModelLoader<Model, ?>> param1List) {
      if ((Entry)this.cachedModelLoaders.put(param1Class, new Entry(param1List)) == null)
        return; 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Already cached loaders for model: ");
      stringBuilder.append(param1Class);
      throw new IllegalStateException(stringBuilder.toString());
    }
    
    private static class Entry<Model> {
      final List<ModelLoader<Model, ?>> loaders;
      
      public Entry(List<ModelLoader<Model, ?>> param2List) {
        this.loaders = param2List;
      }
    }
  }
  
  private static class Entry<Model> {
    final List<ModelLoader<Model, ?>> loaders;
    
    public Entry(List<ModelLoader<Model, ?>> param1List) {
      this.loaders = param1List;
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/model/ModelLoaderRegistry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */