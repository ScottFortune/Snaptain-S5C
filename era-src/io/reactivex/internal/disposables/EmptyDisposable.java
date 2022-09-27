package io.reactivex.internal.disposables;

import io.reactivex.CompletableObserver;
import io.reactivex.MaybeObserver;
import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.fuseable.QueueDisposable;

public enum EmptyDisposable implements QueueDisposable<Object> {
  INSTANCE, NEVER;
  
  static {
    $VALUES = new EmptyDisposable[] { INSTANCE, NEVER };
  }
  
  public static void complete(CompletableObserver paramCompletableObserver) {
    paramCompletableObserver.onSubscribe((Disposable)INSTANCE);
    paramCompletableObserver.onComplete();
  }
  
  public static void complete(MaybeObserver<?> paramMaybeObserver) {
    paramMaybeObserver.onSubscribe((Disposable)INSTANCE);
    paramMaybeObserver.onComplete();
  }
  
  public static void complete(Observer<?> paramObserver) {
    paramObserver.onSubscribe((Disposable)INSTANCE);
    paramObserver.onComplete();
  }
  
  public static void error(Throwable paramThrowable, CompletableObserver paramCompletableObserver) {
    paramCompletableObserver.onSubscribe((Disposable)INSTANCE);
    paramCompletableObserver.onError(paramThrowable);
  }
  
  public static void error(Throwable paramThrowable, MaybeObserver<?> paramMaybeObserver) {
    paramMaybeObserver.onSubscribe((Disposable)INSTANCE);
    paramMaybeObserver.onError(paramThrowable);
  }
  
  public static void error(Throwable paramThrowable, Observer<?> paramObserver) {
    paramObserver.onSubscribe((Disposable)INSTANCE);
    paramObserver.onError(paramThrowable);
  }
  
  public static void error(Throwable paramThrowable, SingleObserver<?> paramSingleObserver) {
    paramSingleObserver.onSubscribe((Disposable)INSTANCE);
    paramSingleObserver.onError(paramThrowable);
  }
  
  public void clear() {}
  
  public void dispose() {}
  
  public boolean isDisposed() {
    boolean bool;
    if (this == INSTANCE) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isEmpty() {
    return true;
  }
  
  public boolean offer(Object paramObject) {
    throw new UnsupportedOperationException("Should not be called!");
  }
  
  public boolean offer(Object paramObject1, Object paramObject2) {
    throw new UnsupportedOperationException("Should not be called!");
  }
  
  public Object poll() throws Exception {
    return null;
  }
  
  public int requestFusion(int paramInt) {
    return paramInt & 0x2;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/disposables/EmptyDisposable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */