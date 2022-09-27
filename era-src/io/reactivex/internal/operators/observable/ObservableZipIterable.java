package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.BiFunction;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.Iterator;

public final class ObservableZipIterable<T, U, V> extends Observable<V> {
  final Iterable<U> other;
  
  final Observable<? extends T> source;
  
  final BiFunction<? super T, ? super U, ? extends V> zipper;
  
  public ObservableZipIterable(Observable<? extends T> paramObservable, Iterable<U> paramIterable, BiFunction<? super T, ? super U, ? extends V> paramBiFunction) {
    this.source = paramObservable;
    this.other = paramIterable;
    this.zipper = paramBiFunction;
  }
  
  public void subscribeActual(Observer<? super V> paramObserver) {
    try {
    
    } finally {
      Exception exception = null;
      Exceptions.throwIfFatal(exception);
      EmptyDisposable.error(exception, paramObserver);
    } 
  }
  
  static final class ZipIterableObserver<T, U, V> implements Observer<T>, Disposable {
    boolean done;
    
    final Observer<? super V> downstream;
    
    final Iterator<U> iterator;
    
    Disposable upstream;
    
    final BiFunction<? super T, ? super U, ? extends V> zipper;
    
    ZipIterableObserver(Observer<? super V> param1Observer, Iterator<U> param1Iterator, BiFunction<? super T, ? super U, ? extends V> param1BiFunction) {
      this.downstream = param1Observer;
      this.iterator = param1Iterator;
      this.zipper = param1BiFunction;
    }
    
    public void dispose() {
      this.upstream.dispose();
    }
    
    void error(Throwable param1Throwable) {
      this.done = true;
      this.upstream.dispose();
      this.downstream.onError(param1Throwable);
    }
    
    public boolean isDisposed() {
      return this.upstream.isDisposed();
    }
    
    public void onComplete() {
      if (this.done)
        return; 
      this.done = true;
      this.downstream.onComplete();
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
      
      } finally {
        param1T = null;
        Exceptions.throwIfFatal((Throwable)param1T);
        error((Throwable)param1T);
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


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableZipIterable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */