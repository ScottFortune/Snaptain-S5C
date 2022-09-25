package io.reactivex.internal.disposables;

import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.util.ExceptionHelper;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public final class ListCompositeDisposable implements Disposable, DisposableContainer {
  volatile boolean disposed;
  
  List<Disposable> resources;
  
  public ListCompositeDisposable() {}
  
  public ListCompositeDisposable(Iterable<? extends Disposable> paramIterable) {
    ObjectHelper.requireNonNull(paramIterable, "resources is null");
    this.resources = new LinkedList<Disposable>();
    for (Disposable disposable : paramIterable) {
      ObjectHelper.requireNonNull(disposable, "Disposable item is null");
      this.resources.add(disposable);
    } 
  }
  
  public ListCompositeDisposable(Disposable... paramVarArgs) {
    ObjectHelper.requireNonNull(paramVarArgs, "resources is null");
    this.resources = new LinkedList<Disposable>();
    int i = paramVarArgs.length;
    for (byte b = 0; b < i; b++) {
      Disposable disposable = paramVarArgs[b];
      ObjectHelper.requireNonNull(disposable, "Disposable item is null");
      this.resources.add(disposable);
    } 
  }
  
  public boolean add(Disposable paramDisposable) {
    // Byte code:
    //   0: aload_1
    //   1: ldc 'd is null'
    //   3: invokestatic requireNonNull : (Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;
    //   6: pop
    //   7: aload_0
    //   8: getfield disposed : Z
    //   11: ifne -> 69
    //   14: aload_0
    //   15: monitorenter
    //   16: aload_0
    //   17: getfield disposed : Z
    //   20: ifne -> 59
    //   23: aload_0
    //   24: getfield resources : Ljava/util/List;
    //   27: astore_2
    //   28: aload_2
    //   29: astore_3
    //   30: aload_2
    //   31: ifnonnull -> 47
    //   34: new java/util/LinkedList
    //   37: astore_3
    //   38: aload_3
    //   39: invokespecial <init> : ()V
    //   42: aload_0
    //   43: aload_3
    //   44: putfield resources : Ljava/util/List;
    //   47: aload_3
    //   48: aload_1
    //   49: invokeinterface add : (Ljava/lang/Object;)Z
    //   54: pop
    //   55: aload_0
    //   56: monitorexit
    //   57: iconst_1
    //   58: ireturn
    //   59: aload_0
    //   60: monitorexit
    //   61: goto -> 69
    //   64: astore_1
    //   65: aload_0
    //   66: monitorexit
    //   67: aload_1
    //   68: athrow
    //   69: aload_1
    //   70: invokeinterface dispose : ()V
    //   75: iconst_0
    //   76: ireturn
    // Exception table:
    //   from	to	target	type
    //   16	28	64	finally
    //   34	47	64	finally
    //   47	57	64	finally
    //   59	61	64	finally
    //   65	67	64	finally
  }
  
  public boolean addAll(Disposable... paramVarArgs) {
    // Byte code:
    //   0: aload_1
    //   1: ldc 'ds is null'
    //   3: invokestatic requireNonNull : (Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;
    //   6: pop
    //   7: aload_0
    //   8: getfield disposed : Z
    //   11: istore_2
    //   12: iconst_0
    //   13: istore_3
    //   14: iload_2
    //   15: ifne -> 111
    //   18: aload_0
    //   19: monitorenter
    //   20: aload_0
    //   21: getfield disposed : Z
    //   24: ifne -> 101
    //   27: aload_0
    //   28: getfield resources : Ljava/util/List;
    //   31: astore #4
    //   33: aload #4
    //   35: astore #5
    //   37: aload #4
    //   39: ifnonnull -> 58
    //   42: new java/util/LinkedList
    //   45: astore #5
    //   47: aload #5
    //   49: invokespecial <init> : ()V
    //   52: aload_0
    //   53: aload #5
    //   55: putfield resources : Ljava/util/List;
    //   58: aload_1
    //   59: arraylength
    //   60: istore #6
    //   62: iload_3
    //   63: iload #6
    //   65: if_icmpge -> 97
    //   68: aload_1
    //   69: iload_3
    //   70: aaload
    //   71: astore #4
    //   73: aload #4
    //   75: ldc 'd is null'
    //   77: invokestatic requireNonNull : (Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;
    //   80: pop
    //   81: aload #5
    //   83: aload #4
    //   85: invokeinterface add : (Ljava/lang/Object;)Z
    //   90: pop
    //   91: iinc #3, 1
    //   94: goto -> 62
    //   97: aload_0
    //   98: monitorexit
    //   99: iconst_1
    //   100: ireturn
    //   101: aload_0
    //   102: monitorexit
    //   103: goto -> 111
    //   106: astore_1
    //   107: aload_0
    //   108: monitorexit
    //   109: aload_1
    //   110: athrow
    //   111: aload_1
    //   112: arraylength
    //   113: istore #6
    //   115: iconst_0
    //   116: istore_3
    //   117: iload_3
    //   118: iload #6
    //   120: if_icmpge -> 137
    //   123: aload_1
    //   124: iload_3
    //   125: aaload
    //   126: invokeinterface dispose : ()V
    //   131: iinc #3, 1
    //   134: goto -> 117
    //   137: iconst_0
    //   138: ireturn
    // Exception table:
    //   from	to	target	type
    //   20	33	106	finally
    //   42	58	106	finally
    //   58	62	106	finally
    //   73	91	106	finally
    //   97	99	106	finally
    //   101	103	106	finally
    //   107	109	106	finally
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
    //   21: getfield resources : Ljava/util/List;
    //   24: astore_1
    //   25: aload_0
    //   26: aconst_null
    //   27: putfield resources : Ljava/util/List;
    //   30: aload_0
    //   31: monitorexit
    //   32: aload_0
    //   33: aload_1
    //   34: invokevirtual dispose : (Ljava/util/List;)V
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
    //   1: ldc 'Disposable item is null'
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
    //   30: getfield resources : Ljava/util/List;
    //   33: astore_2
    //   34: aload_2
    //   35: ifnull -> 55
    //   38: aload_2
    //   39: aload_1
    //   40: invokeinterface remove : (Ljava/lang/Object;)Z
    //   45: ifne -> 51
    //   48: goto -> 55
    //   51: aload_0
    //   52: monitorexit
    //   53: iconst_1
    //   54: ireturn
    //   55: aload_0
    //   56: monitorexit
    //   57: iconst_0
    //   58: ireturn
    //   59: astore_1
    //   60: aload_0
    //   61: monitorexit
    //   62: aload_1
    //   63: athrow
    // Exception table:
    //   from	to	target	type
    //   18	27	59	finally
    //   29	34	59	finally
    //   38	48	59	finally
    //   51	53	59	finally
    //   55	57	59	finally
    //   60	62	59	finally
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
    //   26: getfield resources : Ljava/util/List;
    //   29: astore_1
    //   30: aload_0
    //   31: aconst_null
    //   32: putfield resources : Ljava/util/List;
    //   35: aload_0
    //   36: monitorexit
    //   37: aload_0
    //   38: aload_1
    //   39: invokevirtual dispose : (Ljava/util/List;)V
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
  
  void dispose(List<Disposable> paramList) {
    if (paramList == null)
      return; 
    List list = null;
    Iterator<Disposable> iterator = paramList.iterator();
    paramList = list;
    while (iterator.hasNext()) {
      Disposable disposable = iterator.next();
      try {
        disposable.dispose();
      } finally {
        Exception exception = null;
        Exceptions.throwIfFatal(exception);
        List<Disposable> list1 = paramList;
        if (paramList == null)
          list1 = new ArrayList<Disposable>(); 
        list1.add(exception);
      } 
    } 
    if (paramList != null) {
      if (paramList.size() == 1)
        throw ExceptionHelper.wrapOrThrow((Throwable)paramList.get(0)); 
      throw new CompositeException(paramList);
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
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/disposables/ListCompositeDisposable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */