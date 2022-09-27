package io.reactivex.internal.operators.single;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.disposables.EmptyDisposable;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicReference;

public final class SingleUsing<T, U> extends Single<T> {
  final Consumer<? super U> disposer;
  
  final boolean eager;
  
  final Callable<U> resourceSupplier;
  
  final Function<? super U, ? extends SingleSource<? extends T>> singleFunction;
  
  public SingleUsing(Callable<U> paramCallable, Function<? super U, ? extends SingleSource<? extends T>> paramFunction, Consumer<? super U> paramConsumer, boolean paramBoolean) {
    this.resourceSupplier = paramCallable;
    this.singleFunction = paramFunction;
    this.disposer = paramConsumer;
    this.eager = paramBoolean;
  }
  
  protected void subscribeActual(SingleObserver<? super T> paramSingleObserver) {
    try {
    
    } finally {
      Exception exception = null;
      Exceptions.throwIfFatal(exception);
      EmptyDisposable.error(exception, paramSingleObserver);
    } 
  }
  
  static final class UsingSingleObserver<T, U> extends AtomicReference<Object> implements SingleObserver<T>, Disposable {
    private static final long serialVersionUID = -5331524057054083935L;
    
    final Consumer<? super U> disposer;
    
    final SingleObserver<? super T> downstream;
    
    final boolean eager;
    
    Disposable upstream;
    
    UsingSingleObserver(SingleObserver<? super T> param1SingleObserver, U param1U, boolean param1Boolean, Consumer<? super U> param1Consumer) {
      super(param1U);
      this.downstream = param1SingleObserver;
      this.eager = param1Boolean;
      this.disposer = param1Consumer;
    }
    
    public void dispose() {
      this.upstream.dispose();
      this.upstream = (Disposable)DisposableHelper.DISPOSED;
      disposeAfter();
    }
    
    void disposeAfter() {
      Object object = getAndSet(this);
      if (object != this)
        try {
          this.disposer.accept(object);
        } finally {
          object = null;
          Exceptions.throwIfFatal((Throwable)object);
        }  
    }
    
    public boolean isDisposed() {
      return this.upstream.isDisposed();
    }
    
    public void onError(Throwable param1Throwable) {
      this.upstream = (Disposable)DisposableHelper.DISPOSED;
      Object object = param1Throwable;
      if (this.eager) {
        object = getAndSet(this);
        if (object != this) {
          try {
          
          } finally {
            object = null;
            Exceptions.throwIfFatal((Throwable)object);
          } 
        } else {
          return;
        } 
      } 
      this.downstream.onError((Throwable)object);
      if (!this.eager)
        disposeAfter(); 
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      if (DisposableHelper.validate(this.upstream, param1Disposable)) {
        this.upstream = param1Disposable;
        this.downstream.onSubscribe(this);
      } 
    }
    
    public void onSuccess(T param1T) {
      this.upstream = (Disposable)DisposableHelper.DISPOSED;
      if (this.eager) {
        Object object = getAndSet(this);
        if (object != this) {
          try {
            this.disposer.accept(object);
          } finally {
            param1T = null;
            Exceptions.throwIfFatal((Throwable)param1T);
            this.downstream.onError((Throwable)param1T);
          } 
        } else {
          return;
        } 
      } 
      this.downstream.onSuccess(param1T);
      if (!this.eager)
        disposeAfter(); 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/single/SingleUsing.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */