package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Predicate;
import io.reactivex.internal.disposables.DisposableHelper;

public final class ObservableSkipWhile<T> extends AbstractObservableWithUpstream<T, T> {
  final Predicate<? super T> predicate;
  
  public ObservableSkipWhile(ObservableSource<T> paramObservableSource, Predicate<? super T> paramPredicate) {
    super(paramObservableSource);
    this.predicate = paramPredicate;
  }
  
  public void subscribeActual(Observer<? super T> paramObserver) {
    this.source.subscribe(new SkipWhileObserver<T>(paramObserver, this.predicate));
  }
  
  static final class SkipWhileObserver<T> implements Observer<T>, Disposable {
    final Observer<? super T> downstream;
    
    boolean notSkipping;
    
    final Predicate<? super T> predicate;
    
    Disposable upstream;
    
    SkipWhileObserver(Observer<? super T> param1Observer, Predicate<? super T> param1Predicate) {
      this.downstream = param1Observer;
      this.predicate = param1Predicate;
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
      if (this.notSkipping) {
        this.downstream.onNext(param1T);
      } else {
        try {
          return;
        } finally {
          param1T = null;
          Exceptions.throwIfFatal((Throwable)param1T);
          this.upstream.dispose();
          this.downstream.onError((Throwable)param1T);
        } 
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


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableSkipWhile.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */