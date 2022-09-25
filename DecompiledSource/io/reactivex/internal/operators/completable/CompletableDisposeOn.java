package io.reactivex.internal.operators.completable;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableSource;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.plugins.RxJavaPlugins;

public final class CompletableDisposeOn extends Completable {
  final Scheduler scheduler;
  
  final CompletableSource source;
  
  public CompletableDisposeOn(CompletableSource paramCompletableSource, Scheduler paramScheduler) {
    this.source = paramCompletableSource;
    this.scheduler = paramScheduler;
  }
  
  protected void subscribeActual(CompletableObserver paramCompletableObserver) {
    this.source.subscribe(new DisposeOnObserver(paramCompletableObserver, this.scheduler));
  }
  
  static final class DisposeOnObserver implements CompletableObserver, Disposable, Runnable {
    volatile boolean disposed;
    
    final CompletableObserver downstream;
    
    final Scheduler scheduler;
    
    Disposable upstream;
    
    DisposeOnObserver(CompletableObserver param1CompletableObserver, Scheduler param1Scheduler) {
      this.downstream = param1CompletableObserver;
      this.scheduler = param1Scheduler;
    }
    
    public void dispose() {
      this.disposed = true;
      this.scheduler.scheduleDirect(this);
    }
    
    public boolean isDisposed() {
      return this.disposed;
    }
    
    public void onComplete() {
      if (this.disposed)
        return; 
      this.downstream.onComplete();
    }
    
    public void onError(Throwable param1Throwable) {
      if (this.disposed) {
        RxJavaPlugins.onError(param1Throwable);
        return;
      } 
      this.downstream.onError(param1Throwable);
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      if (DisposableHelper.validate(this.upstream, param1Disposable)) {
        this.upstream = param1Disposable;
        this.downstream.onSubscribe(this);
      } 
    }
    
    public void run() {
      this.upstream.dispose();
      this.upstream = (Disposable)DisposableHelper.DISPOSED;
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/completable/CompletableDisposeOn.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */