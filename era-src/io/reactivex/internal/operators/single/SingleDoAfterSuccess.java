package io.reactivex.internal.operators.single;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.disposables.DisposableHelper;

public final class SingleDoAfterSuccess<T> extends Single<T> {
  final Consumer<? super T> onAfterSuccess;
  
  final SingleSource<T> source;
  
  public SingleDoAfterSuccess(SingleSource<T> paramSingleSource, Consumer<? super T> paramConsumer) {
    this.source = paramSingleSource;
    this.onAfterSuccess = paramConsumer;
  }
  
  protected void subscribeActual(SingleObserver<? super T> paramSingleObserver) {
    this.source.subscribe(new DoAfterObserver<T>(paramSingleObserver, this.onAfterSuccess));
  }
  
  static final class DoAfterObserver<T> implements SingleObserver<T>, Disposable {
    final SingleObserver<? super T> downstream;
    
    final Consumer<? super T> onAfterSuccess;
    
    Disposable upstream;
    
    DoAfterObserver(SingleObserver<? super T> param1SingleObserver, Consumer<? super T> param1Consumer) {
      this.downstream = param1SingleObserver;
      this.onAfterSuccess = param1Consumer;
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
      this.downstream.onSuccess(param1T);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/single/SingleDoAfterSuccess.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */