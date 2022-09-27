package io.reactivex.internal.operators.maybe;

import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public final class MaybeDelay<T> extends AbstractMaybeWithUpstream<T, T> {
  final long delay;
  
  final Scheduler scheduler;
  
  final TimeUnit unit;
  
  public MaybeDelay(MaybeSource<T> paramMaybeSource, long paramLong, TimeUnit paramTimeUnit, Scheduler paramScheduler) {
    super(paramMaybeSource);
    this.delay = paramLong;
    this.unit = paramTimeUnit;
    this.scheduler = paramScheduler;
  }
  
  protected void subscribeActual(MaybeObserver<? super T> paramMaybeObserver) {
    this.source.subscribe(new DelayMaybeObserver<T>(paramMaybeObserver, this.delay, this.unit, this.scheduler));
  }
  
  static final class DelayMaybeObserver<T> extends AtomicReference<Disposable> implements MaybeObserver<T>, Disposable, Runnable {
    private static final long serialVersionUID = 5566860102500855068L;
    
    final long delay;
    
    final MaybeObserver<? super T> downstream;
    
    Throwable error;
    
    final Scheduler scheduler;
    
    final TimeUnit unit;
    
    T value;
    
    DelayMaybeObserver(MaybeObserver<? super T> param1MaybeObserver, long param1Long, TimeUnit param1TimeUnit, Scheduler param1Scheduler) {
      this.downstream = param1MaybeObserver;
      this.delay = param1Long;
      this.unit = param1TimeUnit;
      this.scheduler = param1Scheduler;
    }
    
    public void dispose() {
      DisposableHelper.dispose(this);
    }
    
    public boolean isDisposed() {
      return DisposableHelper.isDisposed(get());
    }
    
    public void onComplete() {
      schedule();
    }
    
    public void onError(Throwable param1Throwable) {
      this.error = param1Throwable;
      schedule();
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      if (DisposableHelper.setOnce(this, param1Disposable))
        this.downstream.onSubscribe(this); 
    }
    
    public void onSuccess(T param1T) {
      this.value = param1T;
      schedule();
    }
    
    public void run() {
      Throwable throwable = this.error;
      if (throwable != null) {
        this.downstream.onError(throwable);
      } else {
        T t = this.value;
        if (t != null) {
          this.downstream.onSuccess(t);
        } else {
          this.downstream.onComplete();
        } 
      } 
    }
    
    void schedule() {
      DisposableHelper.replace(this, this.scheduler.scheduleDirect(this, this.delay, this.unit));
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/maybe/MaybeDelay.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */