package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;

public final class ObservableCount<T> extends AbstractObservableWithUpstream<T, Long> {
  public ObservableCount(ObservableSource<T> paramObservableSource) {
    super(paramObservableSource);
  }
  
  public void subscribeActual(Observer<? super Long> paramObserver) {
    this.source.subscribe(new CountObserver(paramObserver));
  }
  
  static final class CountObserver implements Observer<Object>, Disposable {
    long count;
    
    final Observer<? super Long> downstream;
    
    Disposable upstream;
    
    CountObserver(Observer<? super Long> param1Observer) {
      this.downstream = param1Observer;
    }
    
    public void dispose() {
      this.upstream.dispose();
    }
    
    public boolean isDisposed() {
      return this.upstream.isDisposed();
    }
    
    public void onComplete() {
      this.downstream.onNext(Long.valueOf(this.count));
      this.downstream.onComplete();
    }
    
    public void onError(Throwable param1Throwable) {
      this.downstream.onError(param1Throwable);
    }
    
    public void onNext(Object param1Object) {
      this.count++;
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      if (DisposableHelper.validate(this.upstream, param1Disposable)) {
        this.upstream = param1Disposable;
        this.downstream.onSubscribe(this);
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableCount.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */