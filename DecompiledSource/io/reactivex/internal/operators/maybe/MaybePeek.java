package io.reactivex.internal.operators.maybe;

import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.plugins.RxJavaPlugins;

public final class MaybePeek<T> extends AbstractMaybeWithUpstream<T, T> {
  final Action onAfterTerminate;
  
  final Action onCompleteCall;
  
  final Action onDisposeCall;
  
  final Consumer<? super Throwable> onErrorCall;
  
  final Consumer<? super Disposable> onSubscribeCall;
  
  final Consumer<? super T> onSuccessCall;
  
  public MaybePeek(MaybeSource<T> paramMaybeSource, Consumer<? super Disposable> paramConsumer, Consumer<? super T> paramConsumer1, Consumer<? super Throwable> paramConsumer2, Action paramAction1, Action paramAction2, Action paramAction3) {
    super(paramMaybeSource);
    this.onSubscribeCall = paramConsumer;
    this.onSuccessCall = paramConsumer1;
    this.onErrorCall = paramConsumer2;
    this.onCompleteCall = paramAction1;
    this.onAfterTerminate = paramAction2;
    this.onDisposeCall = paramAction3;
  }
  
  protected void subscribeActual(MaybeObserver<? super T> paramMaybeObserver) {
    this.source.subscribe(new MaybePeekObserver<T>(paramMaybeObserver, this));
  }
  
  static final class MaybePeekObserver<T> implements MaybeObserver<T>, Disposable {
    final MaybeObserver<? super T> downstream;
    
    final MaybePeek<T> parent;
    
    Disposable upstream;
    
    MaybePeekObserver(MaybeObserver<? super T> param1MaybeObserver, MaybePeek<T> param1MaybePeek) {
      this.downstream = param1MaybeObserver;
      this.parent = param1MaybePeek;
    }
    
    public void dispose() {
      try {
        this.parent.onDisposeCall.run();
      } finally {
        Exception exception = null;
        Exceptions.throwIfFatal(exception);
      } 
    }
    
    public boolean isDisposed() {
      return this.upstream.isDisposed();
    }
    
    void onAfterTerminate() {
      try {
        this.parent.onAfterTerminate.run();
      } finally {
        Exception exception = null;
        Exceptions.throwIfFatal(exception);
      } 
    }
    
    public void onComplete() {
      if (this.upstream == DisposableHelper.DISPOSED)
        return; 
      try {
        this.parent.onCompleteCall.run();
        return;
      } finally {
        Exception exception = null;
        Exceptions.throwIfFatal(exception);
        onErrorInner(exception);
      } 
    }
    
    public void onError(Throwable param1Throwable) {
      if (this.upstream == DisposableHelper.DISPOSED) {
        RxJavaPlugins.onError(param1Throwable);
        return;
      } 
      onErrorInner(param1Throwable);
    }
    
    void onErrorInner(Throwable param1Throwable) {
      CompositeException compositeException;
      try {
        this.parent.onErrorCall.accept(param1Throwable);
      } finally {
        Exception exception = null;
        Exceptions.throwIfFatal(exception);
      } 
      onAfterTerminate();
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      if (DisposableHelper.validate(this.upstream, param1Disposable))
        try {
        
        } finally {
          Exception exception = null;
          Exceptions.throwIfFatal(exception);
          param1Disposable.dispose();
          this.upstream = (Disposable)DisposableHelper.DISPOSED;
        }  
    }
    
    public void onSuccess(T param1T) {
      if (this.upstream == DisposableHelper.DISPOSED)
        return; 
      try {
        this.parent.onSuccessCall.accept(param1T);
        return;
      } finally {
        param1T = null;
        Exceptions.throwIfFatal((Throwable)param1T);
        onErrorInner((Throwable)param1T);
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/maybe/MaybePeek.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */