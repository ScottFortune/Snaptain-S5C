package io.reactivex.internal.operators.maybe;

import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.BiConsumer;
import io.reactivex.internal.disposables.DisposableHelper;

public final class MaybeDoOnEvent<T> extends AbstractMaybeWithUpstream<T, T> {
  final BiConsumer<? super T, ? super Throwable> onEvent;
  
  public MaybeDoOnEvent(MaybeSource<T> paramMaybeSource, BiConsumer<? super T, ? super Throwable> paramBiConsumer) {
    super(paramMaybeSource);
    this.onEvent = paramBiConsumer;
  }
  
  protected void subscribeActual(MaybeObserver<? super T> paramMaybeObserver) {
    this.source.subscribe(new DoOnEventMaybeObserver<T>(paramMaybeObserver, this.onEvent));
  }
  
  static final class DoOnEventMaybeObserver<T> implements MaybeObserver<T>, Disposable {
    final MaybeObserver<? super T> downstream;
    
    final BiConsumer<? super T, ? super Throwable> onEvent;
    
    Disposable upstream;
    
    DoOnEventMaybeObserver(MaybeObserver<? super T> param1MaybeObserver, BiConsumer<? super T, ? super Throwable> param1BiConsumer) {
      this.downstream = param1MaybeObserver;
      this.onEvent = param1BiConsumer;
    }
    
    public void dispose() {
      this.upstream.dispose();
      this.upstream = (Disposable)DisposableHelper.DISPOSED;
    }
    
    public boolean isDisposed() {
      return this.upstream.isDisposed();
    }
    
    public void onComplete() {
      this.upstream = (Disposable)DisposableHelper.DISPOSED;
      try {
        return;
      } finally {
        Exception exception = null;
        Exceptions.throwIfFatal(exception);
        this.downstream.onError(exception);
      } 
    }
    
    public void onError(Throwable param1Throwable) {
      CompositeException compositeException;
      this.upstream = (Disposable)DisposableHelper.DISPOSED;
      try {
        this.onEvent.accept(null, param1Throwable);
      } finally {
        Exception exception = null;
        Exceptions.throwIfFatal(exception);
      } 
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      if (DisposableHelper.validate(this.upstream, param1Disposable)) {
        this.upstream = param1Disposable;
        this.downstream.onSubscribe(this);
      } 
    }
    
    public void onSuccess(T param1T) {
      this.upstream = (Disposable)DisposableHelper.DISPOSED;
      try {
        return;
      } finally {
        param1T = null;
        Exceptions.throwIfFatal((Throwable)param1T);
        this.downstream.onError((Throwable)param1T);
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/maybe/MaybeDoOnEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */