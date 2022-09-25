package io.reactivex.internal.operators.maybe;

import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import java.util.concurrent.atomic.AtomicReference;

public final class MaybeOnErrorNext<T> extends AbstractMaybeWithUpstream<T, T> {
  final boolean allowFatal;
  
  final Function<? super Throwable, ? extends MaybeSource<? extends T>> resumeFunction;
  
  public MaybeOnErrorNext(MaybeSource<T> paramMaybeSource, Function<? super Throwable, ? extends MaybeSource<? extends T>> paramFunction, boolean paramBoolean) {
    super(paramMaybeSource);
    this.resumeFunction = paramFunction;
    this.allowFatal = paramBoolean;
  }
  
  protected void subscribeActual(MaybeObserver<? super T> paramMaybeObserver) {
    this.source.subscribe(new OnErrorNextMaybeObserver<T>(paramMaybeObserver, this.resumeFunction, this.allowFatal));
  }
  
  static final class OnErrorNextMaybeObserver<T> extends AtomicReference<Disposable> implements MaybeObserver<T>, Disposable {
    private static final long serialVersionUID = 2026620218879969836L;
    
    final boolean allowFatal;
    
    final MaybeObserver<? super T> downstream;
    
    final Function<? super Throwable, ? extends MaybeSource<? extends T>> resumeFunction;
    
    OnErrorNextMaybeObserver(MaybeObserver<? super T> param1MaybeObserver, Function<? super Throwable, ? extends MaybeSource<? extends T>> param1Function, boolean param1Boolean) {
      this.downstream = param1MaybeObserver;
      this.resumeFunction = param1Function;
      this.allowFatal = param1Boolean;
    }
    
    public void dispose() {
      DisposableHelper.dispose(this);
    }
    
    public boolean isDisposed() {
      return DisposableHelper.isDisposed(get());
    }
    
    public void onComplete() {
      this.downstream.onComplete();
    }
    
    public void onError(Throwable param1Throwable) {
      if (!this.allowFatal && !(param1Throwable instanceof Exception)) {
        this.downstream.onError(param1Throwable);
        return;
      } 
      try {
        return;
      } finally {
        Exception exception = null;
        Exceptions.throwIfFatal(exception);
        this.downstream.onError((Throwable)new CompositeException(new Throwable[] { param1Throwable, exception }));
      } 
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      if (DisposableHelper.setOnce(this, param1Disposable))
        this.downstream.onSubscribe(this); 
    }
    
    public void onSuccess(T param1T) {
      this.downstream.onSuccess(param1T);
    }
    
    static final class NextMaybeObserver<T> implements MaybeObserver<T> {
      final MaybeObserver<? super T> downstream;
      
      final AtomicReference<Disposable> upstream;
      
      NextMaybeObserver(MaybeObserver<? super T> param2MaybeObserver, AtomicReference<Disposable> param2AtomicReference) {
        this.downstream = param2MaybeObserver;
        this.upstream = param2AtomicReference;
      }
      
      public void onComplete() {
        this.downstream.onComplete();
      }
      
      public void onError(Throwable param2Throwable) {
        this.downstream.onError(param2Throwable);
      }
      
      public void onSubscribe(Disposable param2Disposable) {
        DisposableHelper.setOnce(this.upstream, param2Disposable);
      }
      
      public void onSuccess(T param2T) {
        this.downstream.onSuccess(param2T);
      }
    }
  }
  
  static final class NextMaybeObserver<T> implements MaybeObserver<T> {
    final MaybeObserver<? super T> downstream;
    
    final AtomicReference<Disposable> upstream;
    
    NextMaybeObserver(MaybeObserver<? super T> param1MaybeObserver, AtomicReference<Disposable> param1AtomicReference) {
      this.downstream = param1MaybeObserver;
      this.upstream = param1AtomicReference;
    }
    
    public void onComplete() {
      this.downstream.onComplete();
    }
    
    public void onError(Throwable param1Throwable) {
      this.downstream.onError(param1Throwable);
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      DisposableHelper.setOnce(this.upstream, param1Disposable);
    }
    
    public void onSuccess(T param1T) {
      this.downstream.onSuccess(param1T);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/maybe/MaybeOnErrorNext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */