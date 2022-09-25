package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.observers.SerializedObserver;
import java.util.concurrent.TimeUnit;

public final class ObservableDelay<T> extends AbstractObservableWithUpstream<T, T> {
  final long delay;
  
  final boolean delayError;
  
  final Scheduler scheduler;
  
  final TimeUnit unit;
  
  public ObservableDelay(ObservableSource<T> paramObservableSource, long paramLong, TimeUnit paramTimeUnit, Scheduler paramScheduler, boolean paramBoolean) {
    super(paramObservableSource);
    this.delay = paramLong;
    this.unit = paramTimeUnit;
    this.scheduler = paramScheduler;
    this.delayError = paramBoolean;
  }
  
  public void subscribeActual(Observer<? super T> paramObserver) {
    SerializedObserver serializedObserver;
    if (!this.delayError)
      serializedObserver = new SerializedObserver(paramObserver); 
    Scheduler.Worker worker = this.scheduler.createWorker();
    this.source.subscribe(new DelayObserver((Observer<?>)serializedObserver, this.delay, this.unit, worker, this.delayError));
  }
  
  static final class DelayObserver<T> implements Observer<T>, Disposable {
    final long delay;
    
    final boolean delayError;
    
    final Observer<? super T> downstream;
    
    final TimeUnit unit;
    
    Disposable upstream;
    
    final Scheduler.Worker w;
    
    DelayObserver(Observer<? super T> param1Observer, long param1Long, TimeUnit param1TimeUnit, Scheduler.Worker param1Worker, boolean param1Boolean) {
      this.downstream = param1Observer;
      this.delay = param1Long;
      this.unit = param1TimeUnit;
      this.w = param1Worker;
      this.delayError = param1Boolean;
    }
    
    public void dispose() {
      this.upstream.dispose();
      this.w.dispose();
    }
    
    public boolean isDisposed() {
      return this.w.isDisposed();
    }
    
    public void onComplete() {
      this.w.schedule(new OnComplete(), this.delay, this.unit);
    }
    
    public void onError(Throwable param1Throwable) {
      long l;
      Scheduler.Worker worker = this.w;
      OnError onError = new OnError(param1Throwable);
      if (this.delayError) {
        l = this.delay;
      } else {
        l = 0L;
      } 
      worker.schedule(onError, l, this.unit);
    }
    
    public void onNext(T param1T) {
      this.w.schedule(new OnNext(param1T), this.delay, this.unit);
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      if (DisposableHelper.validate(this.upstream, param1Disposable)) {
        this.upstream = param1Disposable;
        this.downstream.onSubscribe(this);
      } 
    }
    
    final class OnComplete implements Runnable {
      public void run() {
        try {
          ObservableDelay.DelayObserver.this.downstream.onComplete();
          return;
        } finally {
          ObservableDelay.DelayObserver.this.w.dispose();
        } 
      }
    }
    
    final class OnError implements Runnable {
      private final Throwable throwable;
      
      OnError(Throwable param2Throwable) {
        this.throwable = param2Throwable;
      }
      
      public void run() {
        try {
          ObservableDelay.DelayObserver.this.downstream.onError(this.throwable);
          return;
        } finally {
          ObservableDelay.DelayObserver.this.w.dispose();
        } 
      }
    }
    
    final class OnNext implements Runnable {
      private final T t;
      
      OnNext(T param2T) {
        this.t = param2T;
      }
      
      public void run() {
        ObservableDelay.DelayObserver.this.downstream.onNext(this.t);
      }
    }
  }
  
  final class OnComplete implements Runnable {
    public void run() {
      try {
        this.this$0.downstream.onComplete();
        return;
      } finally {
        this.this$0.w.dispose();
      } 
    }
  }
  
  final class OnError implements Runnable {
    private final Throwable throwable;
    
    OnError(Throwable param1Throwable) {
      this.throwable = param1Throwable;
    }
    
    public void run() {
      try {
        this.this$0.downstream.onError(this.throwable);
        return;
      } finally {
        this.this$0.w.dispose();
      } 
    }
  }
  
  final class OnNext implements Runnable {
    private final T t;
    
    OnNext(T param1T) {
      this.t = param1T;
    }
    
    public void run() {
      this.this$0.downstream.onNext(this.t);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableDelay.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */