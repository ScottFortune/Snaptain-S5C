package io.reactivex.internal.operators.single;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;

public final class SingleDetach<T> extends Single<T> {
  final SingleSource<T> source;
  
  public SingleDetach(SingleSource<T> paramSingleSource) {
    this.source = paramSingleSource;
  }
  
  protected void subscribeActual(SingleObserver<? super T> paramSingleObserver) {
    this.source.subscribe(new DetachSingleObserver<T>(paramSingleObserver));
  }
  
  static final class DetachSingleObserver<T> implements SingleObserver<T>, Disposable {
    SingleObserver<? super T> downstream;
    
    Disposable upstream;
    
    DetachSingleObserver(SingleObserver<? super T> param1SingleObserver) {
      this.downstream = param1SingleObserver;
    }
    
    public void dispose() {
      this.downstream = null;
      this.upstream.dispose();
      this.upstream = (Disposable)DisposableHelper.DISPOSED;
    }
    
    public boolean isDisposed() {
      return this.upstream.isDisposed();
    }
    
    public void onError(Throwable param1Throwable) {
      this.upstream = (Disposable)DisposableHelper.DISPOSED;
      SingleObserver<? super T> singleObserver = this.downstream;
      if (singleObserver != null) {
        this.downstream = null;
        singleObserver.onError(param1Throwable);
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
      SingleObserver<? super T> singleObserver = this.downstream;
      if (singleObserver != null) {
        this.downstream = null;
        singleObserver.onSuccess(param1T);
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/single/SingleDetach.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */