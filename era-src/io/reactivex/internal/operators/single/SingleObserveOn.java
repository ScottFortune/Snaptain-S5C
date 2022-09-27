package io.reactivex.internal.operators.single;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import java.util.concurrent.atomic.AtomicReference;

public final class SingleObserveOn<T> extends Single<T> {
  final Scheduler scheduler;
  
  final SingleSource<T> source;
  
  public SingleObserveOn(SingleSource<T> paramSingleSource, Scheduler paramScheduler) {
    this.source = paramSingleSource;
    this.scheduler = paramScheduler;
  }
  
  protected void subscribeActual(SingleObserver<? super T> paramSingleObserver) {
    this.source.subscribe(new ObserveOnSingleObserver<T>(paramSingleObserver, this.scheduler));
  }
  
  static final class ObserveOnSingleObserver<T> extends AtomicReference<Disposable> implements SingleObserver<T>, Disposable, Runnable {
    private static final long serialVersionUID = 3528003840217436037L;
    
    final SingleObserver<? super T> downstream;
    
    Throwable error;
    
    final Scheduler scheduler;
    
    T value;
    
    ObserveOnSingleObserver(SingleObserver<? super T> param1SingleObserver, Scheduler param1Scheduler) {
      this.downstream = param1SingleObserver;
      this.scheduler = param1Scheduler;
    }
    
    public void dispose() {
      DisposableHelper.dispose(this);
    }
    
    public boolean isDisposed() {
      return DisposableHelper.isDisposed(get());
    }
    
    public void onError(Throwable param1Throwable) {
      this.error = param1Throwable;
      DisposableHelper.replace(this, this.scheduler.scheduleDirect(this));
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      if (DisposableHelper.setOnce(this, param1Disposable))
        this.downstream.onSubscribe(this); 
    }
    
    public void onSuccess(T param1T) {
      this.value = param1T;
      DisposableHelper.replace(this, this.scheduler.scheduleDirect(this));
    }
    
    public void run() {
      Throwable throwable = this.error;
      if (throwable != null) {
        this.downstream.onError(throwable);
      } else {
        this.downstream.onSuccess(this.value);
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/single/SingleObserveOn.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */