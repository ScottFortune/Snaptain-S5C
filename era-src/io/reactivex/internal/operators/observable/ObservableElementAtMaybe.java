package io.reactivex.internal.operators.observable;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.fuseable.FuseToObservable;
import io.reactivex.plugins.RxJavaPlugins;

public final class ObservableElementAtMaybe<T> extends Maybe<T> implements FuseToObservable<T> {
  final long index;
  
  final ObservableSource<T> source;
  
  public ObservableElementAtMaybe(ObservableSource<T> paramObservableSource, long paramLong) {
    this.source = paramObservableSource;
    this.index = paramLong;
  }
  
  public Observable<T> fuseToObservable() {
    return RxJavaPlugins.onAssembly(new ObservableElementAt<T>(this.source, this.index, null, false));
  }
  
  public void subscribeActual(MaybeObserver<? super T> paramMaybeObserver) {
    this.source.subscribe(new ElementAtObserver<T>(paramMaybeObserver, this.index));
  }
  
  static final class ElementAtObserver<T> implements Observer<T>, Disposable {
    long count;
    
    boolean done;
    
    final MaybeObserver<? super T> downstream;
    
    final long index;
    
    Disposable upstream;
    
    ElementAtObserver(MaybeObserver<? super T> param1MaybeObserver, long param1Long) {
      this.downstream = param1MaybeObserver;
      this.index = param1Long;
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
        this.downstream.onComplete();
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
      long l = this.count;
      if (l == this.index) {
        this.done = true;
        this.upstream.dispose();
        this.downstream.onSuccess(param1T);
        return;
      } 
      this.count = l + 1L;
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      if (DisposableHelper.validate(this.upstream, param1Disposable)) {
        this.upstream = param1Disposable;
        this.downstream.onSubscribe(this);
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableElementAtMaybe.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */