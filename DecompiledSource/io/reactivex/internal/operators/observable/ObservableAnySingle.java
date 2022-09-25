package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Predicate;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.fuseable.FuseToObservable;
import io.reactivex.plugins.RxJavaPlugins;

public final class ObservableAnySingle<T> extends Single<Boolean> implements FuseToObservable<Boolean> {
  final Predicate<? super T> predicate;
  
  final ObservableSource<T> source;
  
  public ObservableAnySingle(ObservableSource<T> paramObservableSource, Predicate<? super T> paramPredicate) {
    this.source = paramObservableSource;
    this.predicate = paramPredicate;
  }
  
  public Observable<Boolean> fuseToObservable() {
    return RxJavaPlugins.onAssembly(new ObservableAny<T>(this.source, this.predicate));
  }
  
  protected void subscribeActual(SingleObserver<? super Boolean> paramSingleObserver) {
    this.source.subscribe(new AnyObserver<T>(paramSingleObserver, this.predicate));
  }
  
  static final class AnyObserver<T> implements Observer<T>, Disposable {
    boolean done;
    
    final SingleObserver<? super Boolean> downstream;
    
    final Predicate<? super T> predicate;
    
    Disposable upstream;
    
    AnyObserver(SingleObserver<? super Boolean> param1SingleObserver, Predicate<? super T> param1Predicate) {
      this.downstream = param1SingleObserver;
      this.predicate = param1Predicate;
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
        this.downstream.onSuccess(Boolean.valueOf(false));
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
      try {
        return;
      } finally {
        param1T = null;
        Exceptions.throwIfFatal((Throwable)param1T);
        this.upstream.dispose();
        onError((Throwable)param1T);
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


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableAnySingle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */