package io.reactivex.internal.operators.completable;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableSource;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public final class CompletableDelay extends Completable {
  final long delay;
  
  final boolean delayError;
  
  final Scheduler scheduler;
  
  final CompletableSource source;
  
  final TimeUnit unit;
  
  public CompletableDelay(CompletableSource paramCompletableSource, long paramLong, TimeUnit paramTimeUnit, Scheduler paramScheduler, boolean paramBoolean) {
    this.source = paramCompletableSource;
    this.delay = paramLong;
    this.unit = paramTimeUnit;
    this.scheduler = paramScheduler;
    this.delayError = paramBoolean;
  }
  
  protected void subscribeActual(CompletableObserver paramCompletableObserver) {
    this.source.subscribe(new Delay(paramCompletableObserver, this.delay, this.unit, this.scheduler, this.delayError));
  }
  
  static final class Delay extends AtomicReference<Disposable> implements CompletableObserver, Runnable, Disposable {
    private static final long serialVersionUID = 465972761105851022L;
    
    final long delay;
    
    final boolean delayError;
    
    final CompletableObserver downstream;
    
    Throwable error;
    
    final Scheduler scheduler;
    
    final TimeUnit unit;
    
    Delay(CompletableObserver param1CompletableObserver, long param1Long, TimeUnit param1TimeUnit, Scheduler param1Scheduler, boolean param1Boolean) {
      this.downstream = param1CompletableObserver;
      this.delay = param1Long;
      this.unit = param1TimeUnit;
      this.scheduler = param1Scheduler;
      this.delayError = param1Boolean;
    }
    
    public void dispose() {
      DisposableHelper.dispose(this);
    }
    
    public boolean isDisposed() {
      return DisposableHelper.isDisposed(get());
    }
    
    public void onComplete() {
      DisposableHelper.replace(this, this.scheduler.scheduleDirect(this, this.delay, this.unit));
    }
    
    public void onError(Throwable param1Throwable) {
      long l;
      this.error = param1Throwable;
      Scheduler scheduler = this.scheduler;
      if (this.delayError) {
        l = this.delay;
      } else {
        l = 0L;
      } 
      DisposableHelper.replace(this, scheduler.scheduleDirect(this, l, this.unit));
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      if (DisposableHelper.setOnce(this, param1Disposable))
        this.downstream.onSubscribe(this); 
    }
    
    public void run() {
      Throwable throwable = this.error;
      this.error = null;
      if (throwable != null) {
        this.downstream.onError(throwable);
      } else {
        this.downstream.onComplete();
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/completable/CompletableDelay.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */