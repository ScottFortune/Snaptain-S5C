package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.internal.functions.Functions;
import java.util.Collection;
import java.util.concurrent.Callable;

public final class ObservableToList<T, U extends Collection<? super T>> extends AbstractObservableWithUpstream<T, U> {
  final Callable<U> collectionSupplier;
  
  public ObservableToList(ObservableSource<T> paramObservableSource, int paramInt) {
    super(paramObservableSource);
    this.collectionSupplier = Functions.createArrayList(paramInt);
  }
  
  public ObservableToList(ObservableSource<T> paramObservableSource, Callable<U> paramCallable) {
    super(paramObservableSource);
    this.collectionSupplier = paramCallable;
  }
  
  public void subscribeActual(Observer<? super U> paramObserver) {
    try {
      return;
    } finally {
      Exception exception = null;
      Exceptions.throwIfFatal(exception);
      EmptyDisposable.error(exception, paramObserver);
    } 
  }
  
  static final class ToListObserver<T, U extends Collection<? super T>> implements Observer<T>, Disposable {
    U collection;
    
    final Observer<? super U> downstream;
    
    Disposable upstream;
    
    ToListObserver(Observer<? super U> param1Observer, U param1U) {
      this.downstream = param1Observer;
      this.collection = param1U;
    }
    
    public void dispose() {
      this.upstream.dispose();
    }
    
    public boolean isDisposed() {
      return this.upstream.isDisposed();
    }
    
    public void onComplete() {
      U u = this.collection;
      this.collection = null;
      this.downstream.onNext(u);
      this.downstream.onComplete();
    }
    
    public void onError(Throwable param1Throwable) {
      this.collection = null;
      this.downstream.onError(param1Throwable);
    }
    
    public void onNext(T param1T) {
      this.collection.add(param1T);
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      if (DisposableHelper.validate(this.upstream, param1Disposable)) {
        this.upstream = param1Disposable;
        this.downstream.onSubscribe(this);
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableToList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */