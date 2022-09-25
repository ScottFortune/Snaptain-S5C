package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public final class ObservableThrottleLatest<T> extends AbstractObservableWithUpstream<T, T> {
  final boolean emitLast;
  
  final Scheduler scheduler;
  
  final long timeout;
  
  final TimeUnit unit;
  
  public ObservableThrottleLatest(Observable<T> paramObservable, long paramLong, TimeUnit paramTimeUnit, Scheduler paramScheduler, boolean paramBoolean) {
    super((ObservableSource<T>)paramObservable);
    this.timeout = paramLong;
    this.unit = paramTimeUnit;
    this.scheduler = paramScheduler;
    this.emitLast = paramBoolean;
  }
  
  protected void subscribeActual(Observer<? super T> paramObserver) {
    this.source.subscribe(new ThrottleLatestObserver<T>(paramObserver, this.timeout, this.unit, this.scheduler.createWorker(), this.emitLast));
  }
  
  static final class ThrottleLatestObserver<T> extends AtomicInteger implements Observer<T>, Disposable, Runnable {
    private static final long serialVersionUID = -8296689127439125014L;
    
    volatile boolean cancelled;
    
    volatile boolean done;
    
    final Observer<? super T> downstream;
    
    final boolean emitLast;
    
    Throwable error;
    
    final AtomicReference<T> latest;
    
    final long timeout;
    
    volatile boolean timerFired;
    
    boolean timerRunning;
    
    final TimeUnit unit;
    
    Disposable upstream;
    
    final Scheduler.Worker worker;
    
    ThrottleLatestObserver(Observer<? super T> param1Observer, long param1Long, TimeUnit param1TimeUnit, Scheduler.Worker param1Worker, boolean param1Boolean) {
      this.downstream = param1Observer;
      this.timeout = param1Long;
      this.unit = param1TimeUnit;
      this.worker = param1Worker;
      this.emitLast = param1Boolean;
      this.latest = new AtomicReference<T>();
    }
    
    public void dispose() {
      this.cancelled = true;
      this.upstream.dispose();
      this.worker.dispose();
      if (getAndIncrement() == 0)
        this.latest.lazySet(null); 
    }
    
    void drain() {
      if (getAndIncrement() != 0)
        return; 
      AtomicReference<T> atomicReference = this.latest;
      Observer<? super T> observer = this.downstream;
      int i = 1;
      while (true) {
        if (this.cancelled) {
          atomicReference.lazySet(null);
          return;
        } 
        boolean bool = this.done;
        if (bool && this.error != null) {
          atomicReference.lazySet(null);
          observer.onError(this.error);
          this.worker.dispose();
          return;
        } 
        if (atomicReference.get() == null) {
          j = 1;
        } else {
          j = 0;
        } 
        if (bool) {
          atomicReference = (AtomicReference<T>)atomicReference.getAndSet(null);
          if (!j && this.emitLast)
            observer.onNext(atomicReference); 
          observer.onComplete();
          this.worker.dispose();
          return;
        } 
        if (j) {
          if (this.timerFired) {
            this.timerRunning = false;
            this.timerFired = false;
          } 
        } else if (!this.timerRunning || this.timerFired) {
          observer.onNext(atomicReference.getAndSet(null));
          this.timerFired = false;
          this.timerRunning = true;
          this.worker.schedule(this, this.timeout, this.unit);
          continue;
        } 
        int j = addAndGet(-i);
        i = j;
        if (j == 0)
          return; 
      } 
    }
    
    public boolean isDisposed() {
      return this.cancelled;
    }
    
    public void onComplete() {
      this.done = true;
      drain();
    }
    
    public void onError(Throwable param1Throwable) {
      this.error = param1Throwable;
      this.done = true;
      drain();
    }
    
    public void onNext(T param1T) {
      this.latest.set(param1T);
      drain();
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      if (DisposableHelper.validate(this.upstream, param1Disposable)) {
        this.upstream = param1Disposable;
        this.downstream.onSubscribe(this);
      } 
    }
    
    public void run() {
      this.timerFired = true;
      drain();
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableThrottleLatest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */