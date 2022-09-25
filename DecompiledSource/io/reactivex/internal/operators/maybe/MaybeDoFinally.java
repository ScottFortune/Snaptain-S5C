package io.reactivex.internal.operators.maybe;

import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Action;
import io.reactivex.internal.disposables.DisposableHelper;
import java.util.concurrent.atomic.AtomicInteger;

public final class MaybeDoFinally<T> extends AbstractMaybeWithUpstream<T, T> {
  final Action onFinally;
  
  public MaybeDoFinally(MaybeSource<T> paramMaybeSource, Action paramAction) {
    super(paramMaybeSource);
    this.onFinally = paramAction;
  }
  
  protected void subscribeActual(MaybeObserver<? super T> paramMaybeObserver) {
    this.source.subscribe(new DoFinallyObserver<T>(paramMaybeObserver, this.onFinally));
  }
  
  static final class DoFinallyObserver<T> extends AtomicInteger implements MaybeObserver<T>, Disposable {
    private static final long serialVersionUID = 4109457741734051389L;
    
    final MaybeObserver<? super T> downstream;
    
    final Action onFinally;
    
    Disposable upstream;
    
    DoFinallyObserver(MaybeObserver<? super T> param1MaybeObserver, Action param1Action) {
      this.downstream = param1MaybeObserver;
      this.onFinally = param1Action;
    }
    
    public void dispose() {
      this.upstream.dispose();
      runFinally();
    }
    
    public boolean isDisposed() {
      return this.upstream.isDisposed();
    }
    
    public void onComplete() {
      this.downstream.onComplete();
      runFinally();
    }
    
    public void onError(Throwable param1Throwable) {
      this.downstream.onError(param1Throwable);
      runFinally();
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      if (DisposableHelper.validate(this.upstream, param1Disposable)) {
        this.upstream = param1Disposable;
        this.downstream.onSubscribe(this);
      } 
    }
    
    public void onSuccess(T param1T) {
      this.downstream.onSuccess(param1T);
      runFinally();
    }
    
    void runFinally() {
      if (compareAndSet(0, 1))
        try {
          this.onFinally.run();
        } finally {
          Exception exception = null;
          Exceptions.throwIfFatal(exception);
        }  
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/maybe/MaybeDoFinally.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */