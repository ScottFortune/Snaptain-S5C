package io.reactivex.subjects;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.util.AppendOnlyLinkedArrayList;
import io.reactivex.internal.util.NotificationLite;

final class SerializedSubject<T> extends Subject<T> implements AppendOnlyLinkedArrayList.NonThrowingPredicate<Object> {
  final Subject<T> actual;
  
  volatile boolean done;
  
  boolean emitting;
  
  AppendOnlyLinkedArrayList<Object> queue;
  
  SerializedSubject(Subject<T> paramSubject) {
    this.actual = paramSubject;
  }
  
  void emitLoop() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield queue : Lio/reactivex/internal/util/AppendOnlyLinkedArrayList;
    //   6: astore_1
    //   7: aload_1
    //   8: ifnonnull -> 19
    //   11: aload_0
    //   12: iconst_0
    //   13: putfield emitting : Z
    //   16: aload_0
    //   17: monitorexit
    //   18: return
    //   19: aload_0
    //   20: aconst_null
    //   21: putfield queue : Lio/reactivex/internal/util/AppendOnlyLinkedArrayList;
    //   24: aload_0
    //   25: monitorexit
    //   26: aload_1
    //   27: aload_0
    //   28: invokevirtual forEachWhile : (Lio/reactivex/internal/util/AppendOnlyLinkedArrayList$NonThrowingPredicate;)V
    //   31: goto -> 0
    //   34: astore_1
    //   35: aload_0
    //   36: monitorexit
    //   37: goto -> 42
    //   40: aload_1
    //   41: athrow
    //   42: goto -> 40
    // Exception table:
    //   from	to	target	type
    //   2	7	34	finally
    //   11	18	34	finally
    //   19	26	34	finally
    //   35	37	34	finally
  }
  
  public Throwable getThrowable() {
    return this.actual.getThrowable();
  }
  
  public boolean hasComplete() {
    return this.actual.hasComplete();
  }
  
  public boolean hasObservers() {
    return this.actual.hasObservers();
  }
  
  public boolean hasThrowable() {
    return this.actual.hasThrowable();
  }
  
  public void onComplete() {
    // Byte code:
    //   0: aload_0
    //   1: getfield done : Z
    //   4: ifeq -> 8
    //   7: return
    //   8: aload_0
    //   9: monitorenter
    //   10: aload_0
    //   11: getfield done : Z
    //   14: ifeq -> 20
    //   17: aload_0
    //   18: monitorexit
    //   19: return
    //   20: aload_0
    //   21: iconst_1
    //   22: putfield done : Z
    //   25: aload_0
    //   26: getfield emitting : Z
    //   29: ifeq -> 67
    //   32: aload_0
    //   33: getfield queue : Lio/reactivex/internal/util/AppendOnlyLinkedArrayList;
    //   36: astore_1
    //   37: aload_1
    //   38: astore_2
    //   39: aload_1
    //   40: ifnonnull -> 57
    //   43: new io/reactivex/internal/util/AppendOnlyLinkedArrayList
    //   46: astore_2
    //   47: aload_2
    //   48: iconst_4
    //   49: invokespecial <init> : (I)V
    //   52: aload_0
    //   53: aload_2
    //   54: putfield queue : Lio/reactivex/internal/util/AppendOnlyLinkedArrayList;
    //   57: aload_2
    //   58: invokestatic complete : ()Ljava/lang/Object;
    //   61: invokevirtual add : (Ljava/lang/Object;)V
    //   64: aload_0
    //   65: monitorexit
    //   66: return
    //   67: aload_0
    //   68: iconst_1
    //   69: putfield emitting : Z
    //   72: aload_0
    //   73: monitorexit
    //   74: aload_0
    //   75: getfield actual : Lio/reactivex/subjects/Subject;
    //   78: invokevirtual onComplete : ()V
    //   81: return
    //   82: astore_2
    //   83: aload_0
    //   84: monitorexit
    //   85: aload_2
    //   86: athrow
    // Exception table:
    //   from	to	target	type
    //   10	19	82	finally
    //   20	37	82	finally
    //   43	57	82	finally
    //   57	66	82	finally
    //   67	74	82	finally
    //   83	85	82	finally
  }
  
  public void onError(Throwable paramThrowable) {
    // Byte code:
    //   0: aload_0
    //   1: getfield done : Z
    //   4: ifeq -> 12
    //   7: aload_1
    //   8: invokestatic onError : (Ljava/lang/Throwable;)V
    //   11: return
    //   12: aload_0
    //   13: monitorenter
    //   14: aload_0
    //   15: getfield done : Z
    //   18: ifeq -> 26
    //   21: iconst_1
    //   22: istore_2
    //   23: goto -> 86
    //   26: aload_0
    //   27: iconst_1
    //   28: putfield done : Z
    //   31: aload_0
    //   32: getfield emitting : Z
    //   35: ifeq -> 79
    //   38: aload_0
    //   39: getfield queue : Lio/reactivex/internal/util/AppendOnlyLinkedArrayList;
    //   42: astore_3
    //   43: aload_3
    //   44: astore #4
    //   46: aload_3
    //   47: ifnonnull -> 67
    //   50: new io/reactivex/internal/util/AppendOnlyLinkedArrayList
    //   53: astore #4
    //   55: aload #4
    //   57: iconst_4
    //   58: invokespecial <init> : (I)V
    //   61: aload_0
    //   62: aload #4
    //   64: putfield queue : Lio/reactivex/internal/util/AppendOnlyLinkedArrayList;
    //   67: aload #4
    //   69: aload_1
    //   70: invokestatic error : (Ljava/lang/Throwable;)Ljava/lang/Object;
    //   73: invokevirtual setFirst : (Ljava/lang/Object;)V
    //   76: aload_0
    //   77: monitorexit
    //   78: return
    //   79: iconst_0
    //   80: istore_2
    //   81: aload_0
    //   82: iconst_1
    //   83: putfield emitting : Z
    //   86: aload_0
    //   87: monitorexit
    //   88: iload_2
    //   89: ifeq -> 97
    //   92: aload_1
    //   93: invokestatic onError : (Ljava/lang/Throwable;)V
    //   96: return
    //   97: aload_0
    //   98: getfield actual : Lio/reactivex/subjects/Subject;
    //   101: aload_1
    //   102: invokevirtual onError : (Ljava/lang/Throwable;)V
    //   105: return
    //   106: astore_1
    //   107: aload_0
    //   108: monitorexit
    //   109: aload_1
    //   110: athrow
    // Exception table:
    //   from	to	target	type
    //   14	21	106	finally
    //   26	43	106	finally
    //   50	67	106	finally
    //   67	78	106	finally
    //   81	86	106	finally
    //   86	88	106	finally
    //   107	109	106	finally
  }
  
  public void onNext(T paramT) {
    // Byte code:
    //   0: aload_0
    //   1: getfield done : Z
    //   4: ifeq -> 8
    //   7: return
    //   8: aload_0
    //   9: monitorenter
    //   10: aload_0
    //   11: getfield done : Z
    //   14: ifeq -> 20
    //   17: aload_0
    //   18: monitorexit
    //   19: return
    //   20: aload_0
    //   21: getfield emitting : Z
    //   24: ifeq -> 63
    //   27: aload_0
    //   28: getfield queue : Lio/reactivex/internal/util/AppendOnlyLinkedArrayList;
    //   31: astore_2
    //   32: aload_2
    //   33: astore_3
    //   34: aload_2
    //   35: ifnonnull -> 52
    //   38: new io/reactivex/internal/util/AppendOnlyLinkedArrayList
    //   41: astore_3
    //   42: aload_3
    //   43: iconst_4
    //   44: invokespecial <init> : (I)V
    //   47: aload_0
    //   48: aload_3
    //   49: putfield queue : Lio/reactivex/internal/util/AppendOnlyLinkedArrayList;
    //   52: aload_3
    //   53: aload_1
    //   54: invokestatic next : (Ljava/lang/Object;)Ljava/lang/Object;
    //   57: invokevirtual add : (Ljava/lang/Object;)V
    //   60: aload_0
    //   61: monitorexit
    //   62: return
    //   63: aload_0
    //   64: iconst_1
    //   65: putfield emitting : Z
    //   68: aload_0
    //   69: monitorexit
    //   70: aload_0
    //   71: getfield actual : Lio/reactivex/subjects/Subject;
    //   74: aload_1
    //   75: invokevirtual onNext : (Ljava/lang/Object;)V
    //   78: aload_0
    //   79: invokevirtual emitLoop : ()V
    //   82: return
    //   83: astore_1
    //   84: aload_0
    //   85: monitorexit
    //   86: aload_1
    //   87: athrow
    // Exception table:
    //   from	to	target	type
    //   10	19	83	finally
    //   20	32	83	finally
    //   38	52	83	finally
    //   52	62	83	finally
    //   63	70	83	finally
    //   84	86	83	finally
  }
  
  public void onSubscribe(Disposable paramDisposable) {
    // Byte code:
    //   0: aload_0
    //   1: getfield done : Z
    //   4: istore_2
    //   5: iconst_1
    //   6: istore_3
    //   7: iconst_1
    //   8: istore #4
    //   10: iload_2
    //   11: ifne -> 97
    //   14: aload_0
    //   15: monitorenter
    //   16: aload_0
    //   17: getfield done : Z
    //   20: ifeq -> 29
    //   23: iload #4
    //   25: istore_3
    //   26: goto -> 87
    //   29: aload_0
    //   30: getfield emitting : Z
    //   33: ifeq -> 80
    //   36: aload_0
    //   37: getfield queue : Lio/reactivex/internal/util/AppendOnlyLinkedArrayList;
    //   40: astore #5
    //   42: aload #5
    //   44: astore #6
    //   46: aload #5
    //   48: ifnonnull -> 68
    //   51: new io/reactivex/internal/util/AppendOnlyLinkedArrayList
    //   54: astore #6
    //   56: aload #6
    //   58: iconst_4
    //   59: invokespecial <init> : (I)V
    //   62: aload_0
    //   63: aload #6
    //   65: putfield queue : Lio/reactivex/internal/util/AppendOnlyLinkedArrayList;
    //   68: aload #6
    //   70: aload_1
    //   71: invokestatic disposable : (Lio/reactivex/disposables/Disposable;)Ljava/lang/Object;
    //   74: invokevirtual add : (Ljava/lang/Object;)V
    //   77: aload_0
    //   78: monitorexit
    //   79: return
    //   80: aload_0
    //   81: iconst_1
    //   82: putfield emitting : Z
    //   85: iconst_0
    //   86: istore_3
    //   87: aload_0
    //   88: monitorexit
    //   89: goto -> 97
    //   92: astore_1
    //   93: aload_0
    //   94: monitorexit
    //   95: aload_1
    //   96: athrow
    //   97: iload_3
    //   98: ifeq -> 110
    //   101: aload_1
    //   102: invokeinterface dispose : ()V
    //   107: goto -> 122
    //   110: aload_0
    //   111: getfield actual : Lio/reactivex/subjects/Subject;
    //   114: aload_1
    //   115: invokevirtual onSubscribe : (Lio/reactivex/disposables/Disposable;)V
    //   118: aload_0
    //   119: invokevirtual emitLoop : ()V
    //   122: return
    // Exception table:
    //   from	to	target	type
    //   16	23	92	finally
    //   29	42	92	finally
    //   51	68	92	finally
    //   68	79	92	finally
    //   80	85	92	finally
    //   87	89	92	finally
    //   93	95	92	finally
  }
  
  protected void subscribeActual(Observer<? super T> paramObserver) {
    this.actual.subscribe(paramObserver);
  }
  
  public boolean test(Object paramObject) {
    return NotificationLite.acceptFull(paramObject, this.actual);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/subjects/SerializedSubject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */