package io.reactivex.internal.operators.completable;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.disposables.EmptyDisposable;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicReference;

public final class CompletableUsing<R> extends Completable {
  final Function<? super R, ? extends CompletableSource> completableFunction;
  
  final Consumer<? super R> disposer;
  
  final boolean eager;
  
  final Callable<R> resourceSupplier;
  
  public CompletableUsing(Callable<R> paramCallable, Function<? super R, ? extends CompletableSource> paramFunction, Consumer<? super R> paramConsumer, boolean paramBoolean) {
    this.resourceSupplier = paramCallable;
    this.completableFunction = paramFunction;
    this.disposer = paramConsumer;
    this.eager = paramBoolean;
  }
  
  protected void subscribeActual(CompletableObserver paramCompletableObserver) {
    try {
    
    } finally {
      Exception exception = null;
      Exceptions.throwIfFatal(exception);
      EmptyDisposable.error(exception, paramCompletableObserver);
    } 
  }
  
  static final class UsingObserver<R> extends AtomicReference<Object> implements CompletableObserver, Disposable {
    private static final long serialVersionUID = -674404550052917487L;
    
    final Consumer<? super R> disposer;
    
    final CompletableObserver downstream;
    
    final boolean eager;
    
    Disposable upstream;
    
    UsingObserver(CompletableObserver param1CompletableObserver, R param1R, Consumer<? super R> param1Consumer, boolean param1Boolean) {
      super(param1R);
      this.downstream = param1CompletableObserver;
      this.disposer = param1Consumer;
      this.eager = param1Boolean;
    }
    
    public void dispose() {
      this.upstream.dispose();
      this.upstream = (Disposable)DisposableHelper.DISPOSED;
      disposeResourceAfter();
    }
    
    void disposeResourceAfter() {
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
    
    public void onComplete() {
      this.upstream = (Disposable)DisposableHelper.DISPOSED;
      if (this.eager) {
        Object object = getAndSet(this);
        if (object != this) {
          try {
            this.disposer.accept(object);
          } finally {
            object = null;
            Exceptions.throwIfFatal((Throwable)object);
            this.downstream.onError((Throwable)object);
          } 
        } else {
          return;
        } 
      } 
      this.downstream.onComplete();
      if (!this.eager)
        disposeResourceAfter(); 
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
        disposeResourceAfter(); 
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      if (DisposableHelper.validate(this.upstream, param1Disposable)) {
        this.upstream = param1Disposable;
        this.downstream.onSubscribe(this);
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/completable/CompletableUsing.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */