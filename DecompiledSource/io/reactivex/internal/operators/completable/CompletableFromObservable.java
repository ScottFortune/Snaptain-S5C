package io.reactivex.internal.operators.completable;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public final class CompletableFromObservable<T> extends Completable {
  final ObservableSource<T> observable;
  
  public CompletableFromObservable(ObservableSource<T> paramObservableSource) {
    this.observable = paramObservableSource;
  }
  
  protected void subscribeActual(CompletableObserver paramCompletableObserver) {
    this.observable.subscribe(new CompletableFromObservableObserver(paramCompletableObserver));
  }
  
  static final class CompletableFromObservableObserver<T> implements Observer<T> {
    final CompletableObserver co;
    
    CompletableFromObservableObserver(CompletableObserver param1CompletableObserver) {
      this.co = param1CompletableObserver;
    }
    
    public void onComplete() {
      this.co.onComplete();
    }
    
    public void onError(Throwable param1Throwable) {
      this.co.onError(param1Throwable);
    }
    
    public void onNext(T param1T) {}
    
    public void onSubscribe(Disposable param1Disposable) {
      this.co.onSubscribe(param1Disposable);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/completable/CompletableFromObservable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */