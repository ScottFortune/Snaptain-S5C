package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.NoSuchElementException;

public final class ObservableSingleSingle<T> extends Single<T> {
  final T defaultValue;
  
  final ObservableSource<? extends T> source;
  
  public ObservableSingleSingle(ObservableSource<? extends T> paramObservableSource, T paramT) {
    this.source = paramObservableSource;
    this.defaultValue = paramT;
  }
  
  public void subscribeActual(SingleObserver<? super T> paramSingleObserver) {
    this.source.subscribe(new SingleElementObserver<T>(paramSingleObserver, this.defaultValue));
  }
  
  static final class SingleElementObserver<T> implements Observer<T>, Disposable {
    final T defaultValue;
    
    boolean done;
    
    final SingleObserver<? super T> downstream;
    
    Disposable upstream;
    
    T value;
    
    SingleElementObserver(SingleObserver<? super T> param1SingleObserver, T param1T) {
      this.downstream = param1SingleObserver;
      this.defaultValue = param1T;
    }
    
    public void dispose() {
      this.upstream.dispose();
    }
    
    public boolean isDisposed() {
      return this.upstream.isDisposed();
    }
    
    public void onComplete() {
      if (this.done)
        return; 
      this.done = true;
      T t1 = this.value;
      this.value = null;
      T t2 = t1;
      if (t1 == null)
        t2 = this.defaultValue; 
      if (t2 != null) {
        this.downstream.onSuccess(t2);
      } else {
        this.downstream.onError(new NoSuchElementException());
      } 
    }
    
    public void onError(Throwable param1Throwable) {
      if (this.done) {
        RxJavaPlugins.onError(param1Throwable);
        return;
      } 
      this.done = true;
      this.downstream.onError(param1Throwable);
    }
    
    public void onNext(T param1T) {
      if (this.done)
        return; 
      if (this.value != null) {
        this.done = true;
        this.upstream.dispose();
        this.downstream.onError(new IllegalArgumentException("Sequence contains more than one element!"));
        return;
      } 
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


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableSingleSingle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */