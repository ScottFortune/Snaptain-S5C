package io.reactivex.disposables;

import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.disposables.DisposableContainer;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.util.ExceptionHelper;
import io.reactivex.internal.util.OpenHashSet;
import java.util.ArrayList;

public final class CompositeDisposable implements Disposable, DisposableContainer {
  volatile boolean disposed;
  
  OpenHashSet<Disposable> resources;
  
  public CompositeDisposable() {}
  
  public CompositeDisposable(Iterable<? extends Disposable> paramIterable) {
    ObjectHelper.requireNonNull(paramIterable, "disposables is null");
    this.resources = new OpenHashSet();
    for (Disposable disposable : paramIterable) {
      ObjectHelper.requireNonNull(disposable, "A Disposable item in the disposables sequence is null");
      this.resources.add(disposable);
    } 
  }
  
  public CompositeDisposable(Disposable... paramVarArgs) {
    ObjectHelper.requireNonNull(paramVarArgs, "disposables is null");
    this.resources = new OpenHashSet(paramVarArgs.length + 1);
    int i = paramVarArgs.length;
    for (byte b = 0; b < i; b++) {
      Disposable disposable = paramVarArgs[b];
      ObjectHelper.requireNonNull(disposable, "A Disposable in the disposables array is null");
      this.resources.add(disposable);
    } 
  }
  
  public boolean add(Disposable paramDisposable) {
    // Byte code:
    //   0: aload_1
    //   1: ldc 'disposable is null'
    //   3: invokestatic requireNonNull : (Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;
    //   6: pop
    //   7: aload_0
    //   8: getfield disposed : Z
    //   11: ifne -> 67
    //   14: aload_0
    //   15: monitorenter
    //   16: aload_0
    //   17: getfield disposed : Z
    //   20: ifne -> 57
    //   23: aload_0
    //   24: getfield resources : Lio/reactivex/internal/util/OpenHashSet;
    //   27: astore_2
    //   28: aload_2
    //   29: astore_3
    //   30: aload_2
    //   31: ifnonnull -> 47
    //   34: new io/reactivex/internal/util/OpenHashSet
    //   37: astore_3
    //   38: aload_3
    //   39: invokespecial <init> : ()V
    //   42: aload_0
    //   43: aload_3
    //   44: putfield resources : Lio/reactivex/internal/util/OpenHashSet;
    //   47: aload_3
    //   48: aload_1
    //   49: invokevirtual add : (Ljava/lang/Object;)Z
    //   52: pop
    //   53: aload_0
    //   54: monitorexit
    //   55: iconst_1
    //   56: ireturn
    //   57: aload_0
    //   58: monitorexit
    //   59: goto -> 67
    //   62: astore_1
    //   63: aload_0
    //   64: monitorexit
    //   65: aload_1
    //   66: athrow
    //   67: aload_1
    //   68: invokeinterface dispose : ()V
    //   73: iconst_0
    //   74: ireturn
    // Exception table:
    //   from	to	target	type
    //   16	28	62	finally
    //   34	47	62	finally
    //   47	55	62	finally
    //   57	59	62	finally
    //   63	65	62	finally
  }
  
  public boolean addAll(Disposable... paramVarArgs) {
    // Byte code:
    //   0: aload_1
    //   1: ldc 'disposables is null'
    //   3: invokestatic requireNonNull : (Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;
    //   6: pop
    //   7: aload_0
    //   8: getfield disposed : Z
    //   11: istore_2
    //   12: iconst_0
    //   13: istore_3
    //   14: iload_2
    //   15: ifne -> 113
    //   18: aload_0
    //   19: monitorenter
    //   20: aload_0
    //   21: getfield disposed : Z
    //   24: ifne -> 103
    //   27: aload_0
    //   28: getfield resources : Lio/reactivex/internal/util/OpenHashSet;
    //   31: astore #4
    //   33: aload #4
    //   35: astore #5
    //   37: aload #4
    //   39: ifnonnull -> 62
    //   42: new io/reactivex/internal/util/OpenHashSet
    //   45: astore #5
    //   47: aload #5
    //   49: aload_1
    //   50: arraylength
    //   51: iconst_1
    //   52: iadd
    //   53: invokespecial <init> : (I)V
    //   56: aload_0
    //   57: aload #5
    //   59: putfield resources : Lio/reactivex/internal/util/OpenHashSet;
    //   62: aload_1
    //   63: arraylength
    //   64: istore #6
    //   66: iload_3
    //   67: iload #6
    //   69: if_icmpge -> 99
    //   72: aload_1
    //   73: iload_3
    //   74: aaload
    //   75: astore #4
    //   77: aload #4
    //   79: ldc 'A Disposable in the disposables array is null'
    //   81: invokestatic requireNonNull : (Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;
    //   84: pop
    //   85: aload #5
    //   87: aload #4
    //   89: invokevirtual add : (Ljava/lang/Object;)Z
    //   92: pop
    //   93: iinc #3, 1
    //   96: goto -> 66
    //   99: aload_0
    //   100: monitorexit
    //   101: iconst_1
    //   102: ireturn
    //   103: aload_0
    //   104: monitorexit
    //   105: goto -> 113
    //   108: astore_1
    //   109: aload_0
    //   110: monitorexit
    //   111: aload_1
    //   112: athrow
    //   113: aload_1
    //   114: arraylength
    //   115: istore #6
    //   117: iconst_0
    //   118: istore_3
    //   119: iload_3
    //   120: iload #6
    //   122: if_icmpge -> 139
    //   125: aload_1
    //   126: iload_3
    //   127: aaload
    //   128: invokeinterface dispose : ()V
    //   133: iinc #3, 1
    //   136: goto -> 119
    //   139: iconst_0
    //   140: ireturn
    // Exception table:
    //   from	to	target	type
    //   20	33	108	finally
    //   42	62	108	finally
    //   62	66	108	finally
    //   77	93	108	finally
    //   99	101	108	finally
    //   103	105	108	finally
    //   109	111	108	finally
  }
  
  public void clear() {
    // Byte code:
    //   0: aload_0
    //   1: getfield disposed : Z
    //   4: ifeq -> 8
    //   7: return
    //   8: aload_0
    //   9: monitorenter
    //   10: aload_0
    //   11: getfield disposed : Z
    //   14: ifeq -> 20
    //   17: aload_0
    //   18: monitorexit
    //   19: return
    //   20: aload_0
    //   21: getfield resources : Lio/reactivex/internal/util/OpenHashSet;
    //   24: astore_1
    //   25: aload_0
    //   26: aconst_null
    //   27: putfield resources : Lio/reactivex/internal/util/OpenHashSet;
    //   30: aload_0
    //   31: monitorexit
    //   32: aload_0
    //   33: aload_1
    //   34: invokevirtual dispose : (Lio/reactivex/internal/util/OpenHashSet;)V
    //   37: return
    //   38: astore_1
    //   39: aload_0
    //   40: monitorexit
    //   41: aload_1
    //   42: athrow
    // Exception table:
    //   from	to	target	type
    //   10	19	38	finally
    //   20	32	38	finally
    //   39	41	38	finally
  }
  
  public boolean delete(Disposable paramDisposable) {
    // Byte code:
    //   0: aload_1
    //   1: ldc 'disposables is null'
    //   3: invokestatic requireNonNull : (Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;
    //   6: pop
    //   7: aload_0
    //   8: getfield disposed : Z
    //   11: ifeq -> 16
    //   14: iconst_0
    //   15: ireturn
    //   16: aload_0
    //   17: monitorenter
    //   18: aload_0
    //   19: getfield disposed : Z
    //   22: ifeq -> 29
    //   25: aload_0
    //   26: monitorexit
    //   27: iconst_0
    //   28: ireturn
    //   29: aload_0
    //   30: getfield resources : Lio/reactivex/internal/util/OpenHashSet;
    //   33: astore_2
    //   34: aload_2
    //   35: ifnull -> 53
    //   38: aload_2
    //   39: aload_1
    //   40: invokevirtual remove : (Ljava/lang/Object;)Z
    //   43: ifne -> 49
    //   46: goto -> 53
    //   49: aload_0
    //   50: monitorexit
    //   51: iconst_1
    //   52: ireturn
    //   53: aload_0
    //   54: monitorexit
    //   55: iconst_0
    //   56: ireturn
    //   57: astore_1
    //   58: aload_0
    //   59: monitorexit
    //   60: aload_1
    //   61: athrow
    // Exception table:
    //   from	to	target	type
    //   18	27	57	finally
    //   29	34	57	finally
    //   38	46	57	finally
    //   49	51	57	finally
    //   53	55	57	finally
    //   58	60	57	finally
  }
  
  public void dispose() {
    // Byte code:
    //   0: aload_0
    //   1: getfield disposed : Z
    //   4: ifeq -> 8
    //   7: return
    //   8: aload_0
    //   9: monitorenter
    //   10: aload_0
    //   11: getfield disposed : Z
    //   14: ifeq -> 20
    //   17: aload_0
    //   18: monitorexit
    //   19: return
    //   20: aload_0
    //   21: iconst_1
    //   22: putfield disposed : Z
    //   25: aload_0
    //   26: getfield resources : Lio/reactivex/internal/util/OpenHashSet;
    //   29: astore_1
    //   30: aload_0
    //   31: aconst_null
    //   32: putfield resources : Lio/reactivex/internal/util/OpenHashSet;
    //   35: aload_0
    //   36: monitorexit
    //   37: aload_0
    //   38: aload_1
    //   39: invokevirtual dispose : (Lio/reactivex/internal/util/OpenHashSet;)V
    //   42: return
    //   43: astore_1
    //   44: aload_0
    //   45: monitorexit
    //   46: aload_1
    //   47: athrow
    // Exception table:
    //   from	to	target	type
    //   10	19	43	finally
    //   20	37	43	finally
    //   44	46	43	finally
  }
  
  void dispose(OpenHashSet<Disposable> paramOpenHashSet) {
    ArrayList<Object> arrayList;
    if (paramOpenHashSet == null)
      return; 
    Object[] arrayOfObject = paramOpenHashSet.keys();
    int i = arrayOfObject.length;
    paramOpenHashSet = null;
    byte b = 0;
    while (b < i) {
      ArrayList<Object> arrayList1;
      Object object = arrayOfObject[b];
      OpenHashSet<Disposable> openHashSet = paramOpenHashSet;
      if (object instanceof Disposable)
        try {
        
        } finally {
          object = null;
          Exceptions.throwIfFatal((Throwable)object);
          openHashSet = paramOpenHashSet;
          if (paramOpenHashSet == null)
            arrayList1 = new ArrayList(); 
        }  
      b++;
      arrayList = arrayList1;
    } 
    if (arrayList != null) {
      if (arrayList.size() == 1)
        throw ExceptionHelper.wrapOrThrow((Throwable)arrayList.get(0)); 
      throw new CompositeException(arrayList);
    } 
  }
  
  public boolean isDisposed() {
    return this.disposed;
  }
  
  public boolean remove(Disposable paramDisposable) {
    if (delete(paramDisposable)) {
      paramDisposable.dispose();
      return true;
    } 
    return false;
  }
  
  public int size() {
    // Byte code:
    //   0: aload_0
    //   1: getfield disposed : Z
    //   4: istore_1
    //   5: iconst_0
    //   6: istore_2
    //   7: iload_1
    //   8: ifeq -> 13
    //   11: iconst_0
    //   12: ireturn
    //   13: aload_0
    //   14: monitorenter
    //   15: aload_0
    //   16: getfield disposed : Z
    //   19: ifeq -> 26
    //   22: aload_0
    //   23: monitorexit
    //   24: iconst_0
    //   25: ireturn
    //   26: aload_0
    //   27: getfield resources : Lio/reactivex/internal/util/OpenHashSet;
    //   30: astore_3
    //   31: aload_3
    //   32: ifnull -> 40
    //   35: aload_3
    //   36: invokevirtual size : ()I
    //   39: istore_2
    //   40: aload_0
    //   41: monitorexit
    //   42: iload_2
    //   43: ireturn
    //   44: astore_3
    //   45: aload_0
    //   46: monitorexit
    //   47: aload_3
    //   48: athrow
    // Exception table:
    //   from	to	target	type
    //   15	24	44	finally
    //   26	31	44	finally
    //   35	40	44	finally
    //   40	42	44	finally
    //   45	47	44	finally
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/disposables/CompositeDisposable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */