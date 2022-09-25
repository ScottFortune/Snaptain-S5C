package io.reactivex.observers;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.util.AppendOnlyLinkedArrayList;

public final class SerializedObserver<T> implements Observer<T>, Disposable {
  static final int QUEUE_LINK_SIZE = 4;
  
  final boolean delayError;
  
  volatile boolean done;
  
  final Observer<? super T> downstream;
  
  boolean emitting;
  
  AppendOnlyLinkedArrayList<Object> queue;
  
  Disposable upstream;
  
  public SerializedObserver(Observer<? super T> paramObserver) {
    this(paramObserver, false);
  }
  
  public SerializedObserver(Observer<? super T> paramObserver, boolean paramBoolean) {
    this.downstream = paramObserver;
    this.delayError = paramBoolean;
  }
  
  public void dispose() {
    this.upstream.dispose();
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
    //   28: getfield downstream : Lio/reactivex/Observer;
    //   31: invokevirtual accept : (Lio/reactivex/Observer;)Z
    //   34: ifeq -> 0
    //   37: return
    //   38: astore_1
    //   39: aload_0
    //   40: monitorexit
    //   41: goto -> 46
    //   44: aload_1
    //   45: athrow
    //   46: goto -> 44
    // Exception table:
    //   from	to	target	type
    //   2	7	38	finally
    //   11	18	38	finally
    //   19	26	38	finally
    //   39	41	38	finally
  }
  
  public boolean isDisposed() {
    return this.upstream.isDisposed();
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
    //   21: getfield emitting : Z
    //   24: ifeq -> 62
    //   27: aload_0
    //   28: getfield queue : Lio/reactivex/internal/util/AppendOnlyLinkedArrayList;
    //   31: astore_1
    //   32: aload_1
    //   33: astore_2
    //   34: aload_1
    //   35: ifnonnull -> 52
    //   38: new io/reactivex/internal/util/AppendOnlyLinkedArrayList
    //   41: astore_2
    //   42: aload_2
    //   43: iconst_4
    //   44: invokespecial <init> : (I)V
    //   47: aload_0
    //   48: aload_2
    //   49: putfield queue : Lio/reactivex/internal/util/AppendOnlyLinkedArrayList;
    //   52: aload_2
    //   53: invokestatic complete : ()Ljava/lang/Object;
    //   56: invokevirtual add : (Ljava/lang/Object;)V
    //   59: aload_0
    //   60: monitorexit
    //   61: return
    //   62: aload_0
    //   63: iconst_1
    //   64: putfield done : Z
    //   67: aload_0
    //   68: iconst_1
    //   69: putfield emitting : Z
    //   72: aload_0
    //   73: monitorexit
    //   74: aload_0
    //   75: getfield downstream : Lio/reactivex/Observer;
    //   78: invokeinterface onComplete : ()V
    //   83: return
    //   84: astore_2
    //   85: aload_0
    //   86: monitorexit
    //   87: aload_2
    //   88: athrow
    // Exception table:
    //   from	to	target	type
    //   10	19	84	finally
    //   20	32	84	finally
    //   38	52	84	finally
    //   52	61	84	finally
    //   62	74	84	finally
    //   85	87	84	finally
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
    //   18: istore_2
    //   19: iconst_1
    //   20: istore_3
    //   21: iload_2
    //   22: ifeq -> 28
    //   25: goto -> 114
    //   28: aload_0
    //   29: getfield emitting : Z
    //   32: ifeq -> 102
    //   35: aload_0
    //   36: iconst_1
    //   37: putfield done : Z
    //   40: aload_0
    //   41: getfield queue : Lio/reactivex/internal/util/AppendOnlyLinkedArrayList;
    //   44: astore #4
    //   46: aload #4
    //   48: astore #5
    //   50: aload #4
    //   52: ifnonnull -> 72
    //   55: new io/reactivex/internal/util/AppendOnlyLinkedArrayList
    //   58: astore #5
    //   60: aload #5
    //   62: iconst_4
    //   63: invokespecial <init> : (I)V
    //   66: aload_0
    //   67: aload #5
    //   69: putfield queue : Lio/reactivex/internal/util/AppendOnlyLinkedArrayList;
    //   72: aload_1
    //   73: invokestatic error : (Ljava/lang/Throwable;)Ljava/lang/Object;
    //   76: astore_1
    //   77: aload_0
    //   78: getfield delayError : Z
    //   81: ifeq -> 93
    //   84: aload #5
    //   86: aload_1
    //   87: invokevirtual add : (Ljava/lang/Object;)V
    //   90: goto -> 99
    //   93: aload #5
    //   95: aload_1
    //   96: invokevirtual setFirst : (Ljava/lang/Object;)V
    //   99: aload_0
    //   100: monitorexit
    //   101: return
    //   102: aload_0
    //   103: iconst_1
    //   104: putfield done : Z
    //   107: aload_0
    //   108: iconst_1
    //   109: putfield emitting : Z
    //   112: iconst_0
    //   113: istore_3
    //   114: aload_0
    //   115: monitorexit
    //   116: iload_3
    //   117: ifeq -> 125
    //   120: aload_1
    //   121: invokestatic onError : (Ljava/lang/Throwable;)V
    //   124: return
    //   125: aload_0
    //   126: getfield downstream : Lio/reactivex/Observer;
    //   129: aload_1
    //   130: invokeinterface onError : (Ljava/lang/Throwable;)V
    //   135: return
    //   136: astore_1
    //   137: aload_0
    //   138: monitorexit
    //   139: aload_1
    //   140: athrow
    // Exception table:
    //   from	to	target	type
    //   14	19	136	finally
    //   28	46	136	finally
    //   55	72	136	finally
    //   72	90	136	finally
    //   93	99	136	finally
    //   99	101	136	finally
    //   102	112	136	finally
    //   114	116	136	finally
    //   137	139	136	finally
  }
  
  public void onNext(T paramT) {
    // Byte code:
    //   0: aload_0
    //   1: getfield done : Z
    //   4: ifeq -> 8
    //   7: return
    //   8: aload_1
    //   9: ifnonnull -> 35
    //   12: aload_0
    //   13: getfield upstream : Lio/reactivex/disposables/Disposable;
    //   16: invokeinterface dispose : ()V
    //   21: aload_0
    //   22: new java/lang/NullPointerException
    //   25: dup
    //   26: ldc 'onNext called with null. Null values are generally not allowed in 2.x operators and sources.'
    //   28: invokespecial <init> : (Ljava/lang/String;)V
    //   31: invokevirtual onError : (Ljava/lang/Throwable;)V
    //   34: return
    //   35: aload_0
    //   36: monitorenter
    //   37: aload_0
    //   38: getfield done : Z
    //   41: ifeq -> 47
    //   44: aload_0
    //   45: monitorexit
    //   46: return
    //   47: aload_0
    //   48: getfield emitting : Z
    //   51: ifeq -> 90
    //   54: aload_0
    //   55: getfield queue : Lio/reactivex/internal/util/AppendOnlyLinkedArrayList;
    //   58: astore_2
    //   59: aload_2
    //   60: astore_3
    //   61: aload_2
    //   62: ifnonnull -> 79
    //   65: new io/reactivex/internal/util/AppendOnlyLinkedArrayList
    //   68: astore_3
    //   69: aload_3
    //   70: iconst_4
    //   71: invokespecial <init> : (I)V
    //   74: aload_0
    //   75: aload_3
    //   76: putfield queue : Lio/reactivex/internal/util/AppendOnlyLinkedArrayList;
    //   79: aload_3
    //   80: aload_1
    //   81: invokestatic next : (Ljava/lang/Object;)Ljava/lang/Object;
    //   84: invokevirtual add : (Ljava/lang/Object;)V
    //   87: aload_0
    //   88: monitorexit
    //   89: return
    //   90: aload_0
    //   91: iconst_1
    //   92: putfield emitting : Z
    //   95: aload_0
    //   96: monitorexit
    //   97: aload_0
    //   98: getfield downstream : Lio/reactivex/Observer;
    //   101: aload_1
    //   102: invokeinterface onNext : (Ljava/lang/Object;)V
    //   107: aload_0
    //   108: invokevirtual emitLoop : ()V
    //   111: return
    //   112: astore_1
    //   113: aload_0
    //   114: monitorexit
    //   115: aload_1
    //   116: athrow
    // Exception table:
    //   from	to	target	type
    //   37	46	112	finally
    //   47	59	112	finally
    //   65	79	112	finally
    //   79	89	112	finally
    //   90	97	112	finally
    //   113	115	112	finally
  }
  
  public void onSubscribe(Disposable paramDisposable) {
    if (DisposableHelper.validate(this.upstream, paramDisposable)) {
      this.upstream = paramDisposable;
      this.downstream.onSubscribe(this);
    } 
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/observers/SerializedObserver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */