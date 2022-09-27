package io.reactivex.internal.operators.single;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;

public final class SingleHide<T> extends Single<T> {
  final SingleSource<? extends T> source;
  
  public SingleHide(SingleSource<? extends T> paramSingleSource) {
    this.source = paramSingleSource;
  }
  
  protected void subscribeActual(SingleObserver<? super T> paramSingleObserver) {
    this.source.subscribe(new HideSingleObserver<T>(paramSingleObserver));
  }
  
  static final class HideSingleObserver<T> implements SingleObserver<T>, Disposable {
    final SingleObserver<? super T> downstream;
    
    Disposable upstream;
    
    HideSingleObserver(SingleObserver<? super T> param1SingleObserver) {
      this.downstream = param1SingleObserver;
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


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/single/SingleHide.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */