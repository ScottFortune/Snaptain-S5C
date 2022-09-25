package io.reactivex.internal.schedulers;

import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableContainer;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReferenceArray;

public final class ScheduledRunnable extends AtomicReferenceArray<Object> implements Runnable, Callable<Object>, Disposable {
  static final Object ASYNC_DISPOSED;
  
  static final Object DONE;
  
  static final int FUTURE_INDEX = 1;
  
  static final Object PARENT_DISPOSED = new Object();
  
  static final int PARENT_INDEX = 0;
  
  static final Object SYNC_DISPOSED = new Object();
  
  static final int THREAD_INDEX = 2;
  
  private static final long serialVersionUID = -6120223772001106981L;
  
  final Runnable actual;
  
  static {
    ASYNC_DISPOSED = new Object();
    DONE = new Object();
  }
  
  public ScheduledRunnable(Runnable paramRunnable, DisposableContainer paramDisposableContainer) {
    super(3);
    this.actual = paramRunnable;
    lazySet(0, paramDisposableContainer);
  }
  
  public Object call() {
    run();
    return null;
  }
  
  public void dispose() {
    while (true) {
      boolean bool;
      Object object2;
      Object object1 = get(1);
      if (object1 == DONE || object1 == SYNC_DISPOSED || object1 == ASYNC_DISPOSED)
        break; 
      if (get(2) != Thread.currentThread()) {
        bool = true;
      } else {
        bool = false;
      } 
      if (bool) {
        object2 = ASYNC_DISPOSED;
      } else {
        object2 = SYNC_DISPOSED;
      } 
      if (compareAndSet(1, object1, object2)) {
        if (object1 != null)
          ((Future)object1).cancel(bool); 
        break;
      } 
    } 
    while (true) {
      Object object = get(0);
      if (object != DONE) {
        Object object1 = PARENT_DISPOSED;
        if (object == object1 || object == null)
          break; 
        if (compareAndSet(0, object, object1)) {
          ((DisposableContainer)object).delete(this);
          break;
        } 
        continue;
      } 
      break;
    } 
  }
  
  public boolean isDisposed() {
    boolean bool = false;
    Object object = get(0);
    if (object == PARENT_DISPOSED || object == DONE)
      bool = true; 
    return bool;
  }
  
  public void run() {
    // Byte code:
    //   0: aload_0
    //   1: iconst_2
    //   2: invokestatic currentThread : ()Ljava/lang/Thread;
    //   5: invokevirtual lazySet : (ILjava/lang/Object;)V
    //   8: aload_0
    //   9: getfield actual : Ljava/lang/Runnable;
    //   12: invokeinterface run : ()V
    //   17: goto -> 25
    //   20: astore_1
    //   21: aload_1
    //   22: invokestatic onError : (Ljava/lang/Throwable;)V
    //   25: aload_0
    //   26: iconst_2
    //   27: aconst_null
    //   28: invokevirtual lazySet : (ILjava/lang/Object;)V
    //   31: aload_0
    //   32: iconst_0
    //   33: invokevirtual get : (I)Ljava/lang/Object;
    //   36: astore_1
    //   37: aload_1
    //   38: getstatic io/reactivex/internal/schedulers/ScheduledRunnable.PARENT_DISPOSED : Ljava/lang/Object;
    //   41: if_acmpeq -> 71
    //   44: aload_0
    //   45: iconst_0
    //   46: aload_1
    //   47: getstatic io/reactivex/internal/schedulers/ScheduledRunnable.DONE : Ljava/lang/Object;
    //   50: invokevirtual compareAndSet : (ILjava/lang/Object;Ljava/lang/Object;)Z
    //   53: ifeq -> 71
    //   56: aload_1
    //   57: ifnull -> 71
    //   60: aload_1
    //   61: checkcast io/reactivex/internal/disposables/DisposableContainer
    //   64: aload_0
    //   65: invokeinterface delete : (Lio/reactivex/disposables/Disposable;)Z
    //   70: pop
    //   71: aload_0
    //   72: iconst_1
    //   73: invokevirtual get : (I)Ljava/lang/Object;
    //   76: astore_1
    //   77: aload_1
    //   78: getstatic io/reactivex/internal/schedulers/ScheduledRunnable.SYNC_DISPOSED : Ljava/lang/Object;
    //   81: if_acmpeq -> 103
    //   84: aload_1
    //   85: getstatic io/reactivex/internal/schedulers/ScheduledRunnable.ASYNC_DISPOSED : Ljava/lang/Object;
    //   88: if_acmpeq -> 103
    //   91: aload_0
    //   92: iconst_1
    //   93: aload_1
    //   94: getstatic io/reactivex/internal/schedulers/ScheduledRunnable.DONE : Ljava/lang/Object;
    //   97: invokevirtual compareAndSet : (ILjava/lang/Object;Ljava/lang/Object;)Z
    //   100: ifeq -> 71
    //   103: return
    //   104: astore_1
    //   105: aload_0
    //   106: iconst_2
    //   107: aconst_null
    //   108: invokevirtual lazySet : (ILjava/lang/Object;)V
    //   111: aload_0
    //   112: iconst_0
    //   113: invokevirtual get : (I)Ljava/lang/Object;
    //   116: astore_2
    //   117: aload_2
    //   118: getstatic io/reactivex/internal/schedulers/ScheduledRunnable.PARENT_DISPOSED : Ljava/lang/Object;
    //   121: if_acmpeq -> 151
    //   124: aload_0
    //   125: iconst_0
    //   126: aload_2
    //   127: getstatic io/reactivex/internal/schedulers/ScheduledRunnable.DONE : Ljava/lang/Object;
    //   130: invokevirtual compareAndSet : (ILjava/lang/Object;Ljava/lang/Object;)Z
    //   133: ifeq -> 151
    //   136: aload_2
    //   137: ifnull -> 151
    //   140: aload_2
    //   141: checkcast io/reactivex/internal/disposables/DisposableContainer
    //   144: aload_0
    //   145: invokeinterface delete : (Lio/reactivex/disposables/Disposable;)Z
    //   150: pop
    //   151: aload_0
    //   152: iconst_1
    //   153: invokevirtual get : (I)Ljava/lang/Object;
    //   156: astore_2
    //   157: aload_2
    //   158: getstatic io/reactivex/internal/schedulers/ScheduledRunnable.SYNC_DISPOSED : Ljava/lang/Object;
    //   161: if_acmpeq -> 186
    //   164: aload_2
    //   165: getstatic io/reactivex/internal/schedulers/ScheduledRunnable.ASYNC_DISPOSED : Ljava/lang/Object;
    //   168: if_acmpeq -> 186
    //   171: aload_0
    //   172: iconst_1
    //   173: aload_2
    //   174: getstatic io/reactivex/internal/schedulers/ScheduledRunnable.DONE : Ljava/lang/Object;
    //   177: invokevirtual compareAndSet : (ILjava/lang/Object;Ljava/lang/Object;)Z
    //   180: ifne -> 186
    //   183: goto -> 151
    //   186: goto -> 191
    //   189: aload_1
    //   190: athrow
    //   191: goto -> 189
    // Exception table:
    //   from	to	target	type
    //   8	17	20	finally
    //   21	25	104	finally
  }
  
  public void setFuture(Future<?> paramFuture) {
    Object object;
    do {
      object = get(1);
      if (object == DONE)
        return; 
      if (object == SYNC_DISPOSED) {
        paramFuture.cancel(false);
        return;
      } 
      if (object == ASYNC_DISPOSED) {
        paramFuture.cancel(true);
        return;
      } 
    } while (!compareAndSet(1, object, paramFuture));
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/schedulers/ScheduledRunnable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */