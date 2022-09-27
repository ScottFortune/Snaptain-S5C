package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.queue.SpscLinkedArrayQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public final class ObservableSkipLastTimed<T> extends AbstractObservableWithUpstream<T, T> {
  final int bufferSize;
  
  final boolean delayError;
  
  final Scheduler scheduler;
  
  final long time;
  
  final TimeUnit unit;
  
  public ObservableSkipLastTimed(ObservableSource<T> paramObservableSource, long paramLong, TimeUnit paramTimeUnit, Scheduler paramScheduler, int paramInt, boolean paramBoolean) {
    super(paramObservableSource);
    this.time = paramLong;
    this.unit = paramTimeUnit;
    this.scheduler = paramScheduler;
    this.bufferSize = paramInt;
    this.delayError = paramBoolean;
  }
  
  public void subscribeActual(Observer<? super T> paramObserver) {
    this.source.subscribe(new SkipLastTimedObserver<T>(paramObserver, this.time, this.unit, this.scheduler, this.bufferSize, this.delayError));
  }
  
  static final class SkipLastTimedObserver<T> extends AtomicInteger implements Observer<T>, Disposable {
    private static final long serialVersionUID = -5677354903406201275L;
    
    volatile boolean cancelled;
    
    final boolean delayError;
    
    volatile boolean done;
    
    final Observer<? super T> downstream;
    
    Throwable error;
    
    final SpscLinkedArrayQueue<Object> queue;
    
    final Scheduler scheduler;
    
    final long time;
    
    final TimeUnit unit;
    
    Disposable upstream;
    
    SkipLastTimedObserver(Observer<? super T> param1Observer, long param1Long, TimeUnit param1TimeUnit, Scheduler param1Scheduler, int param1Int, boolean param1Boolean) {
      this.downstream = param1Observer;
      this.time = param1Long;
      this.unit = param1TimeUnit;
      this.scheduler = param1Scheduler;
      this.queue = new SpscLinkedArrayQueue(param1Int);
      this.delayError = param1Boolean;
    }
    
    public void dispose() {
      if (!this.cancelled) {
        this.cancelled = true;
        this.upstream.dispose();
        if (getAndIncrement() == 0)
          this.queue.clear(); 
      } 
    }
    
    void drain() {
      if (getAndIncrement() != 0)
        return; 
      Observer<? super T> observer = this.downstream;
      SpscLinkedArrayQueue<Object> spscLinkedArrayQueue = this.queue;
      boolean bool = this.delayError;
      TimeUnit timeUnit = this.unit;
      Scheduler scheduler = this.scheduler;
      long l = this.time;
      int i = 1;
      while (true) {
        int j;
        if (this.cancelled) {
          this.queue.clear();
          return;
        } 
        boolean bool1 = this.done;
        Long long_ = (Long)spscLinkedArrayQueue.peek();
        if (long_ == null) {
          j = 1;
        } else {
          j = 0;
        } 
        long l1 = scheduler.now(timeUnit);
        int k = j;
        if (!j) {
          k = j;
          if (long_.longValue() > l1 - l)
            k = 1; 
        } 
        if (bool1)
          if (bool) {
            if (k) {
              Throwable throwable = this.error;
              if (throwable != null) {
                observer.onError(throwable);
              } else {
                observer.onComplete();
              } 
              return;
            } 
          } else {
            Throwable throwable = this.error;
            if (throwable != null) {
              this.queue.clear();
              observer.onError(throwable);
              return;
            } 
            if (k) {
              observer.onComplete();
              return;
            } 
          }  
        if (k) {
          j = addAndGet(-i);
          i = j;
          if (j == 0)
            return; 
          continue;
        } 
        spscLinkedArrayQueue.poll();
        observer.onNext(spscLinkedArrayQueue.poll());
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
      this.queue.offer(Long.valueOf(this.scheduler.now(this.unit)), param1T);
      drain();
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      if (DisposableHelper.validate(this.upstream, param1Disposable)) {
        this.upstream = param1Disposable;
        this.downstream.onSubscribe(this);
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableSkipLastTimed.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */