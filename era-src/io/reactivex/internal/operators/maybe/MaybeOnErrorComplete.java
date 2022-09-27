package io.reactivex.internal.operators.maybe;

import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Predicate;
import io.reactivex.internal.disposables.DisposableHelper;

public final class MaybeOnErrorComplete<T> extends AbstractMaybeWithUpstream<T, T> {
  final Predicate<? super Throwable> predicate;
  
  public MaybeOnErrorComplete(MaybeSource<T> paramMaybeSource, Predicate<? super Throwable> paramPredicate) {
    super(paramMaybeSource);
    this.predicate = paramPredicate;
  }
  
  protected void subscribeActual(MaybeObserver<? super T> paramMaybeObserver) {
    this.source.subscribe(new OnErrorCompleteMaybeObserver<T>(paramMaybeObserver, this.predicate));
  }
  
  static final class OnErrorCompleteMaybeObserver<T> implements MaybeObserver<T>, Disposable {
    final MaybeObserver<? super T> downstream;
    
    final Predicate<? super Throwable> predicate;
    
    Disposable upstream;
    
    OnErrorCompleteMaybeObserver(MaybeObserver<? super T> param1MaybeObserver, Predicate<? super Throwable> param1Predicate) {
      this.downstream = param1MaybeObserver;
      this.predicate = param1Predicate;
    }
    
    public void dispose() {
      this.upstream.dispose();
    }
    
    public boolean isDisposed() {
      return this.upstream.isDisposed();
    }
    
    public void onComplete() {
      this.downstream.onComplete();
    }
    
    public void onError(Throwable param1Throwable) {
      try {
        return;
      } finally {
        Exception exception = null;
        Exceptions.throwIfFatal(exception);
        this.downstream.onError((Throwable)new CompositeException(new Throwable[] { param1Throwable, exception }));
      } 
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      if (DisposableHelper.validate(this.upstream, param1Disposable)) {
        this.upstream = param1Disposable;
        this.downstream.onSubscribe(this);
      } 
    }
    
    public void onSuccess(T param1T) {
      this.downstream.onSuccess(param1T);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/maybe/MaybeOnErrorComplete.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */