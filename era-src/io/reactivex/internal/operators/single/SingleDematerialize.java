package io.reactivex.internal.operators.single;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.Notification;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;

public final class SingleDematerialize<T, R> extends Maybe<R> {
  final Function<? super T, Notification<R>> selector;
  
  final Single<T> source;
  
  public SingleDematerialize(Single<T> paramSingle, Function<? super T, Notification<R>> paramFunction) {
    this.source = paramSingle;
    this.selector = paramFunction;
  }
  
  protected void subscribeActual(MaybeObserver<? super R> paramMaybeObserver) {
    this.source.subscribe(new DematerializeObserver<T, R>(paramMaybeObserver, this.selector));
  }
  
  static final class DematerializeObserver<T, R> implements SingleObserver<T>, Disposable {
    final MaybeObserver<? super R> downstream;
    
    final Function<? super T, Notification<R>> selector;
    
    Disposable upstream;
    
    DematerializeObserver(MaybeObserver<? super R> param1MaybeObserver, Function<? super T, Notification<R>> param1Function) {
      this.downstream = param1MaybeObserver;
      this.selector = param1Function;
    }
    
    public void dispose() {
      this.upstream.dispose();
    }
    
    public boolean isDisposed() {
      return this.upstream.isDisposed();
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


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/single/SingleDematerialize.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */