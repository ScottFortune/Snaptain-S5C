package io.reactivex.internal.operators.maybe;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.disposables.EmptyDisposable;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicReference;

public final class MaybeUsing<T, D> extends Maybe<T> {
  final boolean eager;
  
  final Consumer<? super D> resourceDisposer;
  
  final Callable<? extends D> resourceSupplier;
  
  final Function<? super D, ? extends MaybeSource<? extends T>> sourceSupplier;
  
  public MaybeUsing(Callable<? extends D> paramCallable, Function<? super D, ? extends MaybeSource<? extends T>> paramFunction, Consumer<? super D> paramConsumer, boolean paramBoolean) {
    this.resourceSupplier = paramCallable;
    this.sourceSupplier = paramFunction;
    this.resourceDisposer = paramConsumer;
    this.eager = paramBoolean;
  }
  
  protected void subscribeActual(MaybeObserver<? super T> paramMaybeObserver) {
    try {
    
    } finally {
      Exception exception = null;
      Exceptions.throwIfFatal(exception);
      EmptyDisposable.error(exception, paramMaybeObserver);
    } 
  }
  
  static final class UsingObserver<T, D> extends AtomicReference<Object> implements MaybeObserver<T>, Disposable {
    private static final long serialVersionUID = -674404550052917487L;
    
    final Consumer<? super D> disposer;
    
    final MaybeObserver<? super T> downstream;
    
    final boolean eager;
    
    Disposable upstream;
    
    UsingObserver(MaybeObserver<? super T> param1MaybeObserver, D param1D, Consumer<? super D> param1Consumer, boolean param1Boolean) {
      super(param1D);
      this.downstream = param1MaybeObserver;
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
        disposeResourceAfter(); 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/maybe/MaybeUsing.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */