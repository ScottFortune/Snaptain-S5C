package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.disposables.EmptyDisposable;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;

public final class ObservableUsing<T, D> extends Observable<T> {
  final Consumer<? super D> disposer;
  
  final boolean eager;
  
  final Callable<? extends D> resourceSupplier;
  
  final Function<? super D, ? extends ObservableSource<? extends T>> sourceSupplier;
  
  public ObservableUsing(Callable<? extends D> paramCallable, Function<? super D, ? extends ObservableSource<? extends T>> paramFunction, Consumer<? super D> paramConsumer, boolean paramBoolean) {
    this.resourceSupplier = paramCallable;
    this.sourceSupplier = paramFunction;
    this.disposer = paramConsumer;
    this.eager = paramBoolean;
  }
  
  public void subscribeActual(Observer<? super T> paramObserver) {
    try {
      D d;
    } finally {
      Exception exception = null;
      Exceptions.throwIfFatal(exception);
      EmptyDisposable.error(exception, paramObserver);
    } 
  }
  
  static final class UsingObserver<T, D> extends AtomicBoolean implements Observer<T>, Disposable {
    private static final long serialVersionUID = 5904473792286235046L;
    
    final Consumer<? super D> disposer;
    
    final Observer<? super T> downstream;
    
    final boolean eager;
    
    final D resource;
    
    Disposable upstream;
    
    UsingObserver(Observer<? super T> param1Observer, D param1D, Consumer<? super D> param1Consumer, boolean param1Boolean) {
      this.downstream = param1Observer;
      this.resource = param1D;
      this.disposer = param1Consumer;
      this.eager = param1Boolean;
    }
    
    public void dispose() {
      disposeAfter();
      this.upstream.dispose();
    }
    
    void disposeAfter() {
      if (compareAndSet(false, true))
        try {
          this.disposer.accept(this.resource);
        } finally {
          Exception exception = null;
          Exceptions.throwIfFatal(exception);
        }  
    }
    
    public boolean isDisposed() {
      return get();
    }
    
    public void onComplete() {
      if (this.eager) {
        if (compareAndSet(false, true))
          try {
            this.disposer.accept(this.resource);
          } finally {
            Exception exception = null;
            Exceptions.throwIfFatal(exception);
            this.downstream.onError(exception);
          }  
        this.upstream.dispose();
        this.downstream.onComplete();
      } else {
        this.downstream.onComplete();
        this.upstream.dispose();
        disposeAfter();
      } 
    }
    
    public void onError(Throwable param1Throwable) {
      if (this.eager) {
        CompositeException compositeException;
        Throwable throwable = param1Throwable;
        if (compareAndSet(false, true))
          try {
          
          } finally {
            throwable = null;
            Exceptions.throwIfFatal(throwable);
          }  
        this.upstream.dispose();
        this.downstream.onError((Throwable)compositeException);
      } else {
        this.downstream.onError(param1Throwable);
        this.upstream.dispose();
        disposeAfter();
      } 
    }
    
    public void onNext(T param1T) {
      this.downstream.onNext(param1T);
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      if (DisposableHelper.validate(this.upstream, param1Disposable)) {
        this.upstream = param1Disposable;
        this.downstream.onSubscribe(this);
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableUsing.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */