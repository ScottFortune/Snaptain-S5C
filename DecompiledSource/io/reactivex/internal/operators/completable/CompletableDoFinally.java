package io.reactivex.internal.operators.completable;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Action;
import io.reactivex.internal.disposables.DisposableHelper;
import java.util.concurrent.atomic.AtomicInteger;

public final class CompletableDoFinally extends Completable {
  final Action onFinally;
  
  final CompletableSource source;
  
  public CompletableDoFinally(CompletableSource paramCompletableSource, Action paramAction) {
    this.source = paramCompletableSource;
    this.onFinally = paramAction;
  }
  
  protected void subscribeActual(CompletableObserver paramCompletableObserver) {
    this.source.subscribe(new DoFinallyObserver(paramCompletableObserver, this.onFinally));
  }
  
  static final class DoFinallyObserver extends AtomicInteger implements CompletableObserver, Disposable {
    private static final long serialVersionUID = 4109457741734051389L;
    
    final CompletableObserver downstream;
    
    final Action onFinally;
    
    Disposable upstream;
    
    DoFinallyObserver(CompletableObserver param1CompletableObserver, Action param1Action) {
      this.downstream = param1CompletableObserver;
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


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/completable/CompletableDoFinally.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */