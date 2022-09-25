package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;

public final class ObservableSkip<T> extends AbstractObservableWithUpstream<T, T> {
  final long n;
  
  public ObservableSkip(ObservableSource<T> paramObservableSource, long paramLong) {
    super(paramObservableSource);
    this.n = paramLong;
  }
  
  public void subscribeActual(Observer<? super T> paramObserver) {
    this.source.subscribe(new SkipObserver<T>(paramObserver, this.n));
  }
  
  static final class SkipObserver<T> implements Observer<T>, Disposable {
    final Observer<? super T> downstream;
    
    long remaining;
    
    Disposable upstream;
    
    SkipObserver(Observer<? super T> param1Observer, long param1Long) {
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
      this.downstream.onComplete();
    }
    
    public void onError(Throwable param1Throwable) {
      this.downstream.onError(param1Throwable);
    }
    
    public void onNext(T param1T) {
      long l = this.remaining;
      if (l != 0L) {
        this.remaining = l - 1L;
      } else {
        this.downstream.onNext(param1T);
      } 
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      if (DisposableHelper.validate(this.upstream, param1Disposable)) {
        this.upstream = param1Disposable;
        this.downstream.onSubscribe(this);
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableSkip.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */