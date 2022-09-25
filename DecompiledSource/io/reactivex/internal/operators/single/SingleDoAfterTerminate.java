package io.reactivex.internal.operators.single;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Action;
import io.reactivex.internal.disposables.DisposableHelper;

public final class SingleDoAfterTerminate<T> extends Single<T> {
  final Action onAfterTerminate;
  
  final SingleSource<T> source;
  
  public SingleDoAfterTerminate(SingleSource<T> paramSingleSource, Action paramAction) {
    this.source = paramSingleSource;
    this.onAfterTerminate = paramAction;
  }
  
  protected void subscribeActual(SingleObserver<? super T> paramSingleObserver) {
    this.source.subscribe(new DoAfterTerminateObserver<T>(paramSingleObserver, this.onAfterTerminate));
  }
  
  static final class DoAfterTerminateObserver<T> implements SingleObserver<T>, Disposable {
    final SingleObserver<? super T> downstream;
    
    final Action onAfterTerminate;
    
    Disposable upstream;
    
    DoAfterTerminateObserver(SingleObserver<? super T> param1SingleObserver, Action param1Action) {
      this.downstream = param1SingleObserver;
      this.onAfterTerminate = param1Action;
    }
    
    private void onAfterTerminate() {
      try {
        this.onAfterTerminate.run();
      } finally {
        Exception exception = null;
        Exceptions.throwIfFatal(exception);
      } 
    }
    
    public void dispose() {
      this.upstream.dispose();
    }
    
    public boolean isDisposed() {
      return this.upstream.isDisposed();
    }
    
    public void onError(Throwable param1Throwable) {
      this.downstream.onError(param1Throwable);
      onAfterTerminate();
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      if (DisposableHelper.validate(this.upstream, param1Disposable)) {
        this.upstream = param1Disposable;
        this.downstream.onSubscribe(this);
      } 
    }
    
    public void onSuccess(T param1T) {
      this.downstream.onSuccess(param1T);
      onAfterTerminate();
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/single/SingleDoAfterTerminate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */