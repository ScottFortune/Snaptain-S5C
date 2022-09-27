package com.sun.jna;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.IdentityHashMap;

public class WeakMemoryHolder {
  IdentityHashMap<Reference<Object>, Memory> backingMap = new IdentityHashMap<Reference<Object>, Memory>();
  
  ReferenceQueue<Object> referenceQueue = new ReferenceQueue();
  
  public void clean() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield referenceQueue : Ljava/lang/ref/ReferenceQueue;
    //   6: astore_1
    //   7: aload_1
    //   8: invokevirtual poll : ()Ljava/lang/ref/Reference;
    //   11: astore_1
    //   12: aload_1
    //   13: ifnull -> 33
    //   16: aload_0
    //   17: getfield backingMap : Ljava/util/IdentityHashMap;
    //   20: aload_1
    //   21: invokevirtual remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   24: pop
    //   25: aload_0
    //   26: getfield referenceQueue : Ljava/lang/ref/ReferenceQueue;
    //   29: astore_1
    //   30: goto -> 7
    //   33: aload_0
    //   34: monitorexit
    //   35: return
    //   36: astore_1
    //   37: aload_0
    //   38: monitorexit
    //   39: goto -> 44
    //   42: aload_1
    //   43: athrow
    //   44: goto -> 42
    // Exception table:
    //   from	to	target	type
    //   2	7	36	finally
    //   7	12	36	finally
    //   16	30	36	finally
  }
  
  public void put(Object paramObject, Memory paramMemory) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual clean : ()V
    //   6: new java/lang/ref/WeakReference
    //   9: astore_3
    //   10: aload_3
    //   11: aload_1
    //   12: aload_0
    //   13: getfield referenceQueue : Ljava/lang/ref/ReferenceQueue;
    //   16: invokespecial <init> : (Ljava/lang/Object;Ljava/lang/ref/ReferenceQueue;)V
    //   19: aload_0
    //   20: getfield backingMap : Ljava/util/IdentityHashMap;
    //   23: aload_3
    //   24: aload_2
    //   25: invokevirtual put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   28: pop
    //   29: aload_0
    //   30: monitorexit
    //   31: return
    //   32: astore_1
    //   33: aload_0
    //   34: monitorexit
    //   35: aload_1
    //   36: athrow
    // Exception table:
    //   from	to	target	type
    //   2	29	32	finally
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/sun/jna/WeakMemoryHolder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */