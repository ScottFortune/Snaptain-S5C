package io.reactivex.internal.operators.single;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.observers.DeferredScalarDisposable;

public final class SingleToObservable<T> extends Observable<T> {
  final SingleSource<? extends T> source;
  
  public SingleToObservable(SingleSource<? extends T> paramSingleSource) {
    this.source = paramSingleSource;
  }
  
  public static <T> SingleObserver<T> create(Observer<? super T> paramObserver) {
    return new SingleToObservableObserver<T>(paramObserver);
  }
  
  public void subscribeActual(Observer<? super T> paramObserver) {
    this.source.subscribe(create(paramObserver));
  }
  
  static final class SingleToObservableObserver<T> extends DeferredScalarDisposable<T> implements SingleObserver<T> {
    private static final long serialVersionUID = 3786543492451018833L;
    
    Disposable upstream;
    
    SingleToObservableObserver(Observer<? super T> param1Observer) {
      super(param1Observer);
    }
    
    public void dispose() {
      super.dispose();
      this.upstream.dispose();
    }
    
    public void onError(Throwable param1Throwable) {
      error(param1Throwable);
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      if (DisposableHelper.validate(this.upstream, param1Disposable)) {
        this.upstream = param1Disposable;
        this.downstream.onSubscribe((Disposable)this);
      } 
    }
    
    public void onSuccess(T param1T) {
      complete(param1T);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/single/SingleToObservable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */