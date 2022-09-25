package io.reactivex.internal.operators.single;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public final class SingleTimer extends Single<Long> {
  final long delay;
  
  final Scheduler scheduler;
  
  final TimeUnit unit;
  
  public SingleTimer(long paramLong, TimeUnit paramTimeUnit, Scheduler paramScheduler) {
    this.delay = paramLong;
    this.unit = paramTimeUnit;
    this.scheduler = paramScheduler;
  }
  
  protected void subscribeActual(SingleObserver<? super Long> paramSingleObserver) {
    TimerDisposable timerDisposable = new TimerDisposable(paramSingleObserver);
    paramSingleObserver.onSubscribe(timerDisposable);
    timerDisposable.setFuture(this.scheduler.scheduleDirect(timerDisposable, this.delay, this.unit));
  }
  
  static final class TimerDisposable extends AtomicReference<Disposable> implements Disposable, Runnable {
    private static final long serialVersionUID = 8465401857522493082L;
    
    final SingleObserver<? super Long> downstream;
    
    TimerDisposable(SingleObserver<? super Long> param1SingleObserver) {
      this.downstream = param1SingleObserver;
    }
    
    public void dispose() {
      DisposableHelper.dispose(this);
    }
    
    public boolean isDisposed() {
      return DisposableHelper.isDisposed(get());
    }
    
    public void run() {
      this.downstream.onSuccess(Long.valueOf(0L));
    }
    
    void setFuture(Disposable param1Disposable) {
      DisposableHelper.replace(this, param1Disposable);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/single/SingleTimer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */