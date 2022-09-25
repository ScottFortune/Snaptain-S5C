package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.queue.SpscLinkedArrayQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public final class ObservableTakeLastTimed<T> extends AbstractObservableWithUpstream<T, T> {
  final int bufferSize;
  
  final long count;
  
  final boolean delayError;
  
  final Scheduler scheduler;
  
  final long time;
  
  final TimeUnit unit;
  
  public ObservableTakeLastTimed(ObservableSource<T> paramObservableSource, long paramLong1, long paramLong2, TimeUnit paramTimeUnit, Scheduler paramScheduler, int paramInt, boolean paramBoolean) {
    super(paramObservableSource);
    this.count = paramLong1;
    this.time = paramLong2;
    this.unit = paramTimeUnit;
    this.scheduler = paramScheduler;
    this.bufferSize = paramInt;
    this.delayError = paramBoolean;
  }
  
  public void subscribeActual(Observer<? super T> paramObserver) {
    this.source.subscribe(new TakeLastTimedObserver<T>(paramObserver, this.count, this.time, this.unit, this.scheduler, this.bufferSize, this.delayError));
  }
  
  static final class TakeLastTimedObserver<T> extends AtomicBoolean implements Observer<T>, Disposable {
    private static final long serialVersionUID = -5677354903406201275L;
    
    volatile boolean cancelled;
    
    final long count;
    
    final boolean delayError;
    
    final Observer<? super T> downstream;
    
    Throwable error;
    
    final SpscLinkedArrayQueue<Object> queue;
    
    final Scheduler scheduler;
    
    final long time;
    
    final TimeUnit unit;
    
    Disposable upstream;
    
    TakeLastTimedObserver(Observer<? super T> param1Observer, long param1Long1, long param1Long2, TimeUnit param1TimeUnit, Scheduler param1Scheduler, int param1Int, boolean param1Boolean) {
      this.downstream = param1Observer;
      this.count = param1Long1;
      this.time = param1Long2;
      this.unit = param1TimeUnit;
      this.scheduler = param1Scheduler;
      this.queue = new SpscLinkedArrayQueue(param1Int);
      this.delayError = param1Boolean;
    }
    
    public void dispose() {
      if (!this.cancelled) {
        this.cancelled = true;
        this.upstream.dispose();
        if (compareAndSet(false, true))
          this.queue.clear(); 
      } 
    }
    
    void drain() {
      if (!compareAndSet(false, true))
        return; 
      Observer<? super T> observer = this.downstream;
      SpscLinkedArrayQueue<Object> spscLinkedArrayQueue = this.queue;
      boolean bool = this.delayError;
      long l1 = this.scheduler.now(this.unit);
      long l2 = this.time;
      while (true) {
        Throwable throwable;
        boolean bool1;
        if (this.cancelled) {
          spscLinkedArrayQueue.clear();
          return;
        } 
        if (!bool) {
          Throwable throwable1 = this.error;
          if (throwable1 != null) {
            spscLinkedArrayQueue.clear();
            observer.onError(throwable1);
            return;
          } 
        } 
        Object object2 = spscLinkedArrayQueue.poll();
        if (object2 == null) {
          bool1 = true;
        } else {
          bool1 = false;
        } 
        if (bool1) {
          throwable = this.error;
          if (throwable != null) {
            observer.onError(throwable);
          } else {
            observer.onComplete();
          } 
          return;
        } 
        Object object1 = throwable.poll();
        if (((Long)object2).longValue() < l1 - l2)
          continue; 
        observer.onNext(object1);
      } 
    }
    
    public boolean isDisposed() {
      return this.cancelled;
    }
    
    public void onComplete() {
      drain();
    }
    
    public void onError(Throwable param1Throwable) {
      this.error = param1Throwable;
      drain();
    }
    
    public void onNext(T param1T) {
      boolean bool;
      SpscLinkedArrayQueue<Object> spscLinkedArrayQueue = this.queue;
      long l1 = this.scheduler.now(this.unit);
      long l2 = this.time;
      long l3 = this.count;
      if (l3 == Long.MAX_VALUE) {
        bool = true;
      } else {
        bool = false;
      } 
      spscLinkedArrayQueue.offer(Long.valueOf(l1), param1T);
      while (!spscLinkedArrayQueue.isEmpty() && (((Long)spscLinkedArrayQueue.peek()).longValue() <= l1 - l2 || (!bool && (spscLinkedArrayQueue.size() >> 1) > l3))) {
        spscLinkedArrayQueue.poll();
        spscLinkedArrayQueue.poll();
      } 
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      if (DisposableHelper.validate(this.upstream, param1Disposable)) {
        this.upstream = param1Disposable;
        this.downstream.onSubscribe(this);
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableTakeLastTimed.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */