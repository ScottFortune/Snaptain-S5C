package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.plugins.RxJavaPlugins;

public final class ObservableTake<T> extends AbstractObservableWithUpstream<T, T> {
  final long limit;
  
  public ObservableTake(ObservableSource<T> paramObservableSource, long paramLong) {
    super(paramObservableSource);
    this.limit = paramLong;
  }
  
  protected void subscribeActual(Observer<? super T> paramObserver) {
    this.source.subscribe(new TakeObserver<T>(paramObserver, this.limit));
  }
  
  static final class TakeObserver<T> implements Observer<T>, Disposable {
    boolean done;
    
    final Observer<? super T> downstream;
    
    long remaining;
    
    Disposable upstream;
    
    TakeObserver(Observer<? super T> param1Observer, long param1Long) {
      this.downstream = param1Observer;
      this.remaining = param1Long;
    }
    
    public void dispose() {
      this.upstream.dispose();
    }
    
    public boolean isDisposed() {
      return this.upstream.isDisposed();
    }
    
    public void onComplete() {
      if (!this.done) {
        this.done = true;
        this.upstream.dispose();
        this.downstream.onComplete();
      } 
    }
    
    public void onError(Throwable param1Throwable) {
      if (this.done) {
        RxJavaPlugins.onError(param1Throwable);
        return;
      } 
      this.done = true;
      this.upstream.dispose();
      this.downstream.onError(param1Throwable);
    }
    
    public void onNext(T param1T) {
      if (!this.done) {
        long l = this.remaining;
        this.remaining = l - 1L;
        if (l > 0L) {
          boolean bool;
          if (this.remaining == 0L) {
            bool = true;
          } else {
            bool = false;
          } 
          this.downstream.onNext(param1T);
          if (bool)
            onComplete(); 
        } 
      } 
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      if (DisposableHelper.validate(this.upstream, param1Disposable)) {
        this.upstream = param1Disposable;
        if (this.remaining == 0L) {
          this.done = true;
          param1Disposable.dispose();
          EmptyDisposable.complete(this.downstream);
        } else {
          this.downstream.onSubscribe(this);
        } 
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableTake.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */