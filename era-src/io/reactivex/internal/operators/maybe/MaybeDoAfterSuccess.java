package io.reactivex.internal.operators.maybe;

import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.disposables.DisposableHelper;

public final class MaybeDoAfterSuccess<T> extends AbstractMaybeWithUpstream<T, T> {
  final Consumer<? super T> onAfterSuccess;
  
  public MaybeDoAfterSuccess(MaybeSource<T> paramMaybeSource, Consumer<? super T> paramConsumer) {
    super(paramMaybeSource);
    this.onAfterSuccess = paramConsumer;
  }
  
  protected void subscribeActual(MaybeObserver<? super T> paramMaybeObserver) {
    this.source.subscribe(new DoAfterObserver<T>(paramMaybeObserver, this.onAfterSuccess));
  }
  
  static final class DoAfterObserver<T> implements MaybeObserver<T>, Disposable {
    final MaybeObserver<? super T> downstream;
    
    final Consumer<? super T> onAfterSuccess;
    
    Disposable upstream;
    
    DoAfterObserver(MaybeObserver<? super T> param1MaybeObserver, Consumer<? super T> param1Consumer) {
      this.downstream = param1MaybeObserver;
      this.onAfterSuccess = param1Consumer;
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
      this.downstream.onError(param1Throwable);
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


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/maybe/MaybeDoAfterSuccess.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */