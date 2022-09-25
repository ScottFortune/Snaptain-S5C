package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;

public final class ObservableTakeLastOne<T> extends AbstractObservableWithUpstream<T, T> {
  public ObservableTakeLastOne(ObservableSource<T> paramObservableSource) {
    super(paramObservableSource);
  }
  
  public void subscribeActual(Observer<? super T> paramObserver) {
    this.source.subscribe(new TakeLastOneObserver<T>(paramObserver));
  }
  
  static final class TakeLastOneObserver<T> implements Observer<T>, Disposable {
    final Observer<? super T> downstream;
    
    Disposable upstream;
    
    T value;
    
    TakeLastOneObserver(Observer<? super T> param1Observer) {
      this.downstream = param1Observer;
    }
    
    public void dispose() {
      this.value = null;
      this.upstream.dispose();
    }
    
    void emit() {
      T t = this.value;
      if (t != null) {
        this.value = null;
        this.downstream.onNext(t);
      } 
      this.downstream.onComplete();
    }
    
    public boolean isDisposed() {
      return this.upstream.isDisposed();
    }
    
    public void onComplete() {
      emit();
    }
    
    public void onError(Throwable param1Throwable) {
      this.value = null;
      this.downstream.onError(param1Throwable);
    }
    
    public void onNext(T param1T) {
      this.value = param1T;
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      if (DisposableHelper.validate(this.upstream, param1Disposable)) {
        this.upstream = param1Disposable;
        this.downstream.onSubscribe(this);
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableTakeLastOne.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */