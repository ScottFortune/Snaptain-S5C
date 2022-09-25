package com.bumptech.glide.provider;

import com.bumptech.glide.load.ResourceDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResourceDecoderRegistry {
  private final List<String> bucketPriorityList = new ArrayList<String>();
  
  private final Map<String, List<Entry<?, ?>>> decoders = new HashMap<String, List<Entry<?, ?>>>();
  
  private List<Entry<?, ?>> getOrAddEntryList(String paramString) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield bucketPriorityList : Ljava/util/List;
    //   6: aload_1
    //   7: invokeinterface contains : (Ljava/lang/Object;)Z
    //   12: ifne -> 26
    //   15: aload_0
    //   16: getfield bucketPriorityList : Ljava/util/List;
    //   19: aload_1
    //   20: invokeinterface add : (Ljava/lang/Object;)Z
    //   25: pop
    //   26: aload_0
    //   27: getfield decoders : Ljava/util/Map;
    //   30: aload_1
    //   31: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   36: checkcast java/util/List
    //   39: astore_2
    //   40: aload_2
    //   41: astore_3
    //   42: aload_2
    //   43: ifnonnull -> 66
    //   46: new java/util/ArrayList
    //   49: astore_3
    //   50: aload_3
    //   51: invokespecial <init> : ()V
    //   54: aload_0
    //   55: getfield decoders : Ljava/util/Map;
    //   58: aload_1
    //   59: aload_3
    //   60: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   65: pop
    //   66: aload_0
    //   67: monitorexit
    //   68: aload_3
    //   69: areturn
    //   70: astore_1
    //   71: aload_0
    //   72: monitorexit
    //   73: aload_1
    //   74: athrow
    // Exception table:
    //   from	to	target	type
    //   2	26	70	finally
    //   26	40	70	finally
    //   46	66	70	finally
  }
  
  public <T, R> void append(String paramString, ResourceDecoder<T, R> paramResourceDecoder, Class<T> paramClass, Class<R> paramClass1) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: invokespecial getOrAddEntryList : (Ljava/lang/String;)Ljava/util/List;
    //   7: astore #5
    //   9: new com/bumptech/glide/provider/ResourceDecoderRegistry$Entry
    //   12: astore_1
    //   13: aload_1
    //   14: aload_3
    //   15: aload #4
    //   17: aload_2
    //   18: invokespecial <init> : (Ljava/lang/Class;Ljava/lang/Class;Lcom/bumptech/glide/load/ResourceDecoder;)V
    //   21: aload #5
    //   23: aload_1
    //   24: invokeinterface add : (Ljava/lang/Object;)Z
    //   29: pop
    //   30: aload_0
    //   31: monitorexit
    //   32: return
    //   33: astore_1
    //   34: aload_0
    //   35: monitorexit
    //   36: aload_1
    //   37: athrow
    // Exception table:
    //   from	to	target	type
    //   2	30	33	finally
  }
  
  public <T, R> List<ResourceDecoder<T, R>> getDecoders(Class<T> paramClass, Class<R> paramClass1) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: new java/util/ArrayList
    //   5: astore_3
    //   6: aload_3
    //   7: invokespecial <init> : ()V
    //   10: aload_0
    //   11: getfield bucketPriorityList : Ljava/util/List;
    //   14: invokeinterface iterator : ()Ljava/util/Iterator;
    //   19: astore #4
    //   21: aload #4
    //   23: invokeinterface hasNext : ()Z
    //   28: ifeq -> 123
    //   31: aload #4
    //   33: invokeinterface next : ()Ljava/lang/Object;
    //   38: checkcast java/lang/String
    //   41: astore #5
    //   43: aload_0
    //   44: getfield decoders : Ljava/util/Map;
    //   47: aload #5
    //   49: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   54: checkcast java/util/List
    //   57: astore #5
    //   59: aload #5
    //   61: ifnonnull -> 67
    //   64: goto -> 21
    //   67: aload #5
    //   69: invokeinterface iterator : ()Ljava/util/Iterator;
    //   74: astore #6
    //   76: aload #6
    //   78: invokeinterface hasNext : ()Z
    //   83: ifeq -> 21
    //   86: aload #6
    //   88: invokeinterface next : ()Ljava/lang/Object;
    //   93: checkcast com/bumptech/glide/provider/ResourceDecoderRegistry$Entry
    //   96: astore #5
    //   98: aload #5
    //   100: aload_1
    //   101: aload_2
    //   102: invokevirtual handles : (Ljava/lang/Class;Ljava/lang/Class;)Z
    //   105: ifeq -> 76
    //   108: aload_3
    //   109: aload #5
    //   111: getfield decoder : Lcom/bumptech/glide/load/ResourceDecoder;
    //   114: invokeinterface add : (Ljava/lang/Object;)Z
    //   119: pop
    //   120: goto -> 76
    //   123: aload_0
    //   124: monitorexit
    //   125: aload_3
    //   126: areturn
    //   127: astore_1
    //   128: aload_0
    //   129: monitorexit
    //   130: goto -> 135
    //   133: aload_1
    //   134: athrow
    //   135: goto -> 133
    // Exception table:
    //   from	to	target	type
    //   2	21	127	finally
    //   21	59	127	finally
    //   67	76	127	finally
    //   76	120	127	finally
  }
  
  public <T, R> List<Class<R>> getResourceClasses(Class<T> paramClass, Class<R> paramClass1) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: new java/util/ArrayList
    //   5: astore_3
    //   6: aload_3
    //   7: invokespecial <init> : ()V
    //   10: aload_0
    //   11: getfield bucketPriorityList : Ljava/util/List;
    //   14: invokeinterface iterator : ()Ljava/util/Iterator;
    //   19: astore #4
    //   21: aload #4
    //   23: invokeinterface hasNext : ()Z
    //   28: ifeq -> 137
    //   31: aload #4
    //   33: invokeinterface next : ()Ljava/lang/Object;
    //   38: checkcast java/lang/String
    //   41: astore #5
    //   43: aload_0
    //   44: getfield decoders : Ljava/util/Map;
    //   47: aload #5
    //   49: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   54: checkcast java/util/List
    //   57: astore #5
    //   59: aload #5
    //   61: ifnonnull -> 67
    //   64: goto -> 21
    //   67: aload #5
    //   69: invokeinterface iterator : ()Ljava/util/Iterator;
    //   74: astore #6
    //   76: aload #6
    //   78: invokeinterface hasNext : ()Z
    //   83: ifeq -> 21
    //   86: aload #6
    //   88: invokeinterface next : ()Ljava/lang/Object;
    //   93: checkcast com/bumptech/glide/provider/ResourceDecoderRegistry$Entry
    //   96: astore #5
    //   98: aload #5
    //   100: aload_1
    //   101: aload_2
    //   102: invokevirtual handles : (Ljava/lang/Class;Ljava/lang/Class;)Z
    //   105: ifeq -> 76
    //   108: aload_3
    //   109: aload #5
    //   111: getfield resourceClass : Ljava/lang/Class;
    //   114: invokeinterface contains : (Ljava/lang/Object;)Z
    //   119: ifne -> 76
    //   122: aload_3
    //   123: aload #5
    //   125: getfield resourceClass : Ljava/lang/Class;
    //   128: invokeinterface add : (Ljava/lang/Object;)Z
    //   133: pop
    //   134: goto -> 76
    //   137: aload_0
    //   138: monitorexit
    //   139: aload_3
    //   140: areturn
    //   141: astore_1
    //   142: aload_0
    //   143: monitorexit
    //   144: goto -> 149
    //   147: aload_1
    //   148: athrow
    //   149: goto -> 147
    // Exception table:
    //   from	to	target	type
    //   2	21	141	finally
    //   21	59	141	finally
    //   67	76	141	finally
    //   76	134	141	finally
  }
  
  public <T, R> void prepend(String paramString, ResourceDecoder<T, R> paramResourceDecoder, Class<T> paramClass, Class<R> paramClass1) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: invokespecial getOrAddEntryList : (Ljava/lang/String;)Ljava/util/List;
    //   7: astore_1
    //   8: new com/bumptech/glide/provider/ResourceDecoderRegistry$Entry
    //   11: astore #5
    //   13: aload #5
    //   15: aload_3
    //   16: aload #4
    //   18: aload_2
    //   19: invokespecial <init> : (Ljava/lang/Class;Ljava/lang/Class;Lcom/bumptech/glide/load/ResourceDecoder;)V
    //   22: aload_1
    //   23: iconst_0
    //   24: aload #5
    //   26: invokeinterface add : (ILjava/lang/Object;)V
    //   31: aload_0
    //   32: monitorexit
    //   33: return
    //   34: astore_1
    //   35: aload_0
    //   36: monitorexit
    //   37: aload_1
    //   38: athrow
    // Exception table:
    //   from	to	target	type
    //   2	31	34	finally
  }
  
  public void setBucketPriorityList(List<String> paramList) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: new java/util/ArrayList
    //   5: astore_2
    //   6: aload_2
    //   7: aload_0
    //   8: getfield bucketPriorityList : Ljava/util/List;
    //   11: invokespecial <init> : (Ljava/util/Collection;)V
    //   14: aload_0
    //   15: getfield bucketPriorityList : Ljava/util/List;
    //   18: invokeinterface clear : ()V
    //   23: aload_0
    //   24: getfield bucketPriorityList : Ljava/util/List;
    //   27: aload_1
    //   28: invokeinterface addAll : (Ljava/util/Collection;)Z
    //   33: pop
    //   34: aload_2
    //   35: invokeinterface iterator : ()Ljava/util/Iterator;
    //   40: astore_2
    //   41: aload_2
    //   42: invokeinterface hasNext : ()Z
    //   47: ifeq -> 84
    //   50: aload_2
    //   51: invokeinterface next : ()Ljava/lang/Object;
    //   56: checkcast java/lang/String
    //   59: astore_3
    //   60: aload_1
    //   61: aload_3
    //   62: invokeinterface contains : (Ljava/lang/Object;)Z
    //   67: ifne -> 41
    //   70: aload_0
    //   71: getfield bucketPriorityList : Ljava/util/List;
    //   74: aload_3
    //   75: invokeinterface add : (Ljava/lang/Object;)Z
    //   80: pop
    //   81: goto -> 41
    //   84: aload_0
    //   85: monitorexit
    //   86: return
    //   87: astore_1
    //   88: aload_0
    //   89: monitorexit
    //   90: goto -> 95
    //   93: aload_1
    //   94: athrow
    //   95: goto -> 93
    // Exception table:
    //   from	to	target	type
    //   2	41	87	finally
    //   41	81	87	finally
  }
  
  private static class Entry<T, R> {
    private final Class<T> dataClass;
    
    final ResourceDecoder<T, R> decoder;
    
    final Class<R> resourceClass;
    
    public Entry(Class<T> param1Class, Class<R> param1Class1, ResourceDecoder<T, R> param1ResourceDecoder) {
      this.dataClass = param1Class;
      this.resourceClass = param1Class1;
      this.decoder = param1ResourceDecoder;
    }
    
    public boolean handles(Class<?> param1Class1, Class<?> param1Class2) {
      boolean bool;
      if (this.dataClass.isAssignableFrom(param1Class1) && param1Class2.isAssignableFrom(this.resourceClass)) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/provider/ResourceDecoderRegistry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */