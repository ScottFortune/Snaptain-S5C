package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.fuseable.FuseToObservable;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.NoSuchElementException;

public final class ObservableElementAtSingle<T> extends Single<T> implements FuseToObservable<T> {
  final T defaultValue;
  
  final long index;
  
  final ObservableSource<T> source;
  
  public ObservableElementAtSingle(ObservableSource<T> paramObservableSource, long paramLong, T paramT) {
    this.source = paramObservableSource;
    this.index = paramLong;
    this.defaultValue = paramT;
  }
  
  public Observable<T> fuseToObservable() {
    return RxJavaPlugins.onAssembly(new ObservableElementAt<T>(this.source, this.index, this.defaultValue, true));
  }
  
  public void subscribeActual(SingleObserver<? super T> paramSingleObserver) {
    this.source.subscribe(new ElementAtObserver<T>(paramSingleObserver, this.index, this.defaultValue));
  }
  
  static final class ElementAtObserver<T> implements Observer<T>, Disposable {
    long count;
    
    final T defaultValue;
    
    boolean done;
    
    final SingleObserver<? super T> downstream;
    
    final long index;
    
    Disposable upstream;
    
    ElementAtObserver(SingleObserver<? super T> param1SingleObserver, long param1Long, T param1T) {
      this.downstream = param1SingleObserver;
      this.index = param1Long;
      this.defaultValue = param1T;
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
        T t = this.defaultValue;
        if (t != null) {
          this.downstream.onSuccess(t);
        } else {
          this.downstream.onError(new NoSuchElementException());
        } 
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


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableElementAtSingle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */