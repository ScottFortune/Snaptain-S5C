package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;

public final class ObservableHide<T> extends AbstractObservableWithUpstream<T, T> {
  public ObservableHide(ObservableSource<T> paramObservableSource) {
    super(paramObservableSource);
  }
  
  protected void subscribeActual(Observer<? super T> paramObserver) {
    this.source.subscribe(new HideDisposable<T>(paramObserver));
  }
  
  static final class HideDisposable<T> implements Observer<T>, Disposable {
    final Observer<? super T> downstream;
    
    Disposable upstream;
    
    HideDisposable(Observer<? super T> param1Observer) {
      this.downstream = param1Observer;
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
      this.downstream.onNext(param1T);
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      if (DisposableHelper.validate(this.upstream, param1Disposable)) {
        this.upstream = param1Disposable;
        this.downstream.onSubscribe(this);
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableHide.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */