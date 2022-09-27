package io.reactivex.internal.operators.single;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.SequentialDisposable;
import java.util.concurrent.TimeUnit;

public final class SingleDelay<T> extends Single<T> {
  final boolean delayError;
  
  final Scheduler scheduler;
  
  final SingleSource<? extends T> source;
  
  final long time;
  
  final TimeUnit unit;
  
  public SingleDelay(SingleSource<? extends T> paramSingleSource, long paramLong, TimeUnit paramTimeUnit, Scheduler paramScheduler, boolean paramBoolean) {
    this.source = paramSingleSource;
    this.time = paramLong;
    this.unit = paramTimeUnit;
    this.scheduler = paramScheduler;
    this.delayError = paramBoolean;
  }
  
  protected void subscribeActual(SingleObserver<? super T> paramSingleObserver) {
    SequentialDisposable sequentialDisposable = new SequentialDisposable();
    paramSingleObserver.onSubscribe((Disposable)sequentialDisposable);
    this.source.subscribe(new Delay(sequentialDisposable, paramSingleObserver));
  }
  
  final class Delay implements SingleObserver<T> {
    final SingleObserver<? super T> downstream;
    
    private final SequentialDisposable sd;
    
    Delay(SequentialDisposable param1SequentialDisposable, SingleObserver<? super T> param1SingleObserver) {
      this.sd = param1SequentialDisposable;
      this.downstream = param1SingleObserver;
    }
    
    public void onError(Throwable param1Throwable) {
      long l;
      SequentialDisposable sequentialDisposable = this.sd;
      Scheduler scheduler = SingleDelay.this.scheduler;
      OnError onError = new OnError(param1Throwable);
      if (SingleDelay.this.delayError) {
        l = SingleDelay.this.time;
      } else {
        l = 0L;
      } 
      sequentialDisposable.replace(scheduler.scheduleDirect(onError, l, SingleDelay.this.unit));
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      this.sd.replace(param1Disposable);
    }
    
    public void onSuccess(T param1T) {
      this.sd.replace(SingleDelay.this.scheduler.scheduleDirect(new OnSuccess(param1T), SingleDelay.this.time, SingleDelay.this.unit));
    }
    
    final class OnError implements Runnable {
      private final Throwable e;
      
      OnError(Throwable param2Throwable) {
        this.e = param2Throwable;
      }
      
      public void run() {
        SingleDelay.Delay.this.downstream.onError(this.e);
      }
    }
    
    final class OnSuccess implements Runnable {
      private final T value;
      
      OnSuccess(T param2T) {
        this.value = param2T;
      }
      
      public void run() {
        SingleDelay.Delay.this.downstream.onSuccess(this.value);
      }
    }
  }
  
  final class OnError implements Runnable {
    private final Throwable e;
    
    OnError(Throwable param1Throwable) {
      this.e = param1Throwable;
    }
    
    public void run() {
      this.this$1.downstream.onError(this.e);
    }
  }
  
  final class OnSuccess implements Runnable {
    private final T value;
    
    OnSuccess(T param1T) {
      this.value = param1T;
    }
    
    public void run() {
      this.this$1.downstream.onSuccess(this.value);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/single/SingleDelay.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */