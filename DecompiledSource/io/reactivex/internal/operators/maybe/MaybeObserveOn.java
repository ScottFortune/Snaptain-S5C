package io.reactivex.internal.operators.maybe;

import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import java.util.concurrent.atomic.AtomicReference;

public final class MaybeObserveOn<T> extends AbstractMaybeWithUpstream<T, T> {
  final Scheduler scheduler;
  
  public MaybeObserveOn(MaybeSource<T> paramMaybeSource, Scheduler paramScheduler) {
    super(paramMaybeSource);
    this.scheduler = paramScheduler;
  }
  
  protected void subscribeActual(MaybeObserver<? super T> paramMaybeObserver) {
    this.source.subscribe(new ObserveOnMaybeObserver<T>(paramMaybeObserver, this.scheduler));
  }
  
  static final class ObserveOnMaybeObserver<T> extends AtomicReference<Disposable> implements MaybeObserver<T>, Disposable, Runnable {
    private static final long serialVersionUID = 8571289934935992137L;
    
    final MaybeObserver<? super T> downstream;
    
    Throwable error;
    
    final Scheduler scheduler;
    
    T value;
    
    ObserveOnMaybeObserver(MaybeObserver<? super T> param1MaybeObserver, Scheduler param1Scheduler) {
      this.downstream = param1MaybeObserver;
      this.scheduler = param1Scheduler;
    }
    
    public void dispose() {
      DisposableHelper.dispose(this);
    }
    
    public boolean isDisposed() {
      return DisposableHelper.isDisposed(get());
    }
    
    public void onComplete() {
      DisposableHelper.replace(this, this.scheduler.scheduleDirect(this));
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
        this.error = null;
        this.downstream.onError(throwable);
      } else {
        T t = this.value;
        if (t != null) {
          this.value = null;
          this.downstream.onSuccess(t);
        } else {
          this.downstream.onComplete();
        } 
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/maybe/MaybeObserveOn.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */