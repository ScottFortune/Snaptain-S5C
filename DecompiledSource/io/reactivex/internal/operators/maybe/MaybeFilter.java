package io.reactivex.internal.operators.maybe;

import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Predicate;
import io.reactivex.internal.disposables.DisposableHelper;

public final class MaybeFilter<T> extends AbstractMaybeWithUpstream<T, T> {
  final Predicate<? super T> predicate;
  
  public MaybeFilter(MaybeSource<T> paramMaybeSource, Predicate<? super T> paramPredicate) {
    super(paramMaybeSource);
    this.predicate = paramPredicate;
  }
  
  protected void subscribeActual(MaybeObserver<? super T> paramMaybeObserver) {
    this.source.subscribe(new FilterMaybeObserver<T>(paramMaybeObserver, this.predicate));
  }
  
  static final class FilterMaybeObserver<T> implements MaybeObserver<T>, Disposable {
    final MaybeObserver<? super T> downstream;
    
    final Predicate<? super T> predicate;
    
    Disposable upstream;
    
    FilterMaybeObserver(MaybeObserver<? super T> param1MaybeObserver, Predicate<? super T> param1Predicate) {
      this.downstream = param1MaybeObserver;
      this.predicate = param1Predicate;
    }
    
    public void dispose() {
      Disposable disposable = this.upstream;
      this.upstream = (Disposable)DisposableHelper.DISPOSED;
      disposable.dispose();
    }
    
    public boolean isDisposed() {
      return this.upstream.isDisposed();
    }
    
    public void onComplete() {
      this.downstream.onComplete();
    }
    
    public void onError(Throwable param1Throwable) {
      this.downstream.onError(param1Throwable);
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      if (DisposableHelper.validate(this.upstream, param1Disposable)) {
        this.upstream = param1Disposable;
        this.downstream.onSubscribe(this);
      } 
    }
    
    public void onSuccess(T param1T) {
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


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/maybe/MaybeFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */