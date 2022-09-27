package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.disposables.SequentialDisposable;
import io.reactivex.internal.util.ExceptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public final class ObservableTimeoutTimed<T> extends AbstractObservableWithUpstream<T, T> {
  final ObservableSource<? extends T> other;
  
  final Scheduler scheduler;
  
  final long timeout;
  
  final TimeUnit unit;
  
  public ObservableTimeoutTimed(Observable<T> paramObservable, long paramLong, TimeUnit paramTimeUnit, Scheduler paramScheduler, ObservableSource<? extends T> paramObservableSource) {
    super((ObservableSource<T>)paramObservable);
    this.timeout = paramLong;
    this.unit = paramTimeUnit;
    this.scheduler = paramScheduler;
    this.other = paramObservableSource;
  }
  
  protected void subscribeActual(Observer<? super T> paramObserver) {
    if (this.other == null) {
      TimeoutObserver<T> timeoutObserver = new TimeoutObserver<T>(paramObserver, this.timeout, this.unit, this.scheduler.createWorker());
      paramObserver.onSubscribe(timeoutObserver);
      timeoutObserver.startTimeout(0L);
      this.source.subscribe(timeoutObserver);
    } else {
      TimeoutFallbackObserver<T> timeoutFallbackObserver = new TimeoutFallbackObserver<T>(paramObserver, this.timeout, this.unit, this.scheduler.createWorker(), this.other);
      paramObserver.onSubscribe(timeoutFallbackObserver);
      timeoutFallbackObserver.startTimeout(0L);
      this.source.subscribe(timeoutFallbackObserver);
    } 
  }
  
  static final class FallbackObserver<T> implements Observer<T> {
    final AtomicReference<Disposable> arbiter;
    
    final Observer<? super T> downstream;
    
    FallbackObserver(Observer<? super T> param1Observer, AtomicReference<Disposable> param1AtomicReference) {
      this.downstream = param1Observer;
      this.arbiter = param1AtomicReference;
    }
    
    public void onComplete() {
      this.downstream.onComplete();
    }
    
    public void onError(Throwable param1Throwable) {
      this.downstream.onError(param1Throwable);
    }
    
    public void onNext(T param1T) {
      this.downstream.onNext(param1T);
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      DisposableHelper.replace(this.arbiter, param1Disposable);
    }
  }
  
  static final class TimeoutFallbackObserver<T> extends AtomicReference<Disposable> implements Observer<T>, Disposable, TimeoutSupport {
    private static final long serialVersionUID = 3764492702657003550L;
    
    final Observer<? super T> downstream;
    
    ObservableSource<? extends T> fallback;
    
    final AtomicLong index;
    
    final SequentialDisposable task;
    
    final long timeout;
    
    final TimeUnit unit;
    
    final AtomicReference<Disposable> upstream;
    
    final Scheduler.Worker worker;
    
    TimeoutFallbackObserver(Observer<? super T> param1Observer, long param1Long, TimeUnit param1TimeUnit, Scheduler.Worker param1Worker, ObservableSource<? extends T> param1ObservableSource) {
      this.downstream = param1Observer;
      this.timeout = param1Long;
      this.unit = param1TimeUnit;
      this.worker = param1Worker;
      this.fallback = param1ObservableSource;
      this.task = new SequentialDisposable();
      this.index = new AtomicLong();
      this.upstream = new AtomicReference<Disposable>();
    }
    
    public void dispose() {
      DisposableHelper.dispose(this.upstream);
      DisposableHelper.dispose(this);
      this.worker.dispose();
    }
    
    public boolean isDisposed() {
      return DisposableHelper.isDisposed(get());
    }
    
    public void onComplete() {
      if (this.index.getAndSet(Long.MAX_VALUE) != Long.MAX_VALUE) {
        this.task.dispose();
        this.downstream.onComplete();
        this.worker.dispose();
      } 
    }
    
    public void onError(Throwable param1Throwable) {
      if (this.index.getAndSet(Long.MAX_VALUE) != Long.MAX_VALUE) {
        this.task.dispose();
        this.downstream.onError(param1Throwable);
        this.worker.dispose();
      } else {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
    
    public void onNext(T param1T) {
      long l = this.index.get();
      if (l != Long.MAX_VALUE) {
        AtomicLong atomicLong = this.index;
        long l1 = 1L + l;
        if (atomicLong.compareAndSet(l, l1)) {
          ((Disposable)this.task.get()).dispose();
          this.downstream.onNext(param1T);
          startTimeout(l1);
        } 
      } 
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      DisposableHelper.setOnce(this.upstream, param1Disposable);
    }
    
    public void onTimeout(long param1Long) {
      if (this.index.compareAndSet(param1Long, Long.MAX_VALUE)) {
        DisposableHelper.dispose(this.upstream);
        ObservableSource<? extends T> observableSource = this.fallback;
        this.fallback = null;
        observableSource.subscribe(new ObservableTimeoutTimed.FallbackObserver<T>(this.downstream, this));
        this.worker.dispose();
      } 
    }
    
    void startTimeout(long param1Long) {
      this.task.replace(this.worker.schedule(new ObservableTimeoutTimed.TimeoutTask(param1Long, this), this.timeout, this.unit));
    }
  }
  
  static final class TimeoutObserver<T> extends AtomicLong implements Observer<T>, Disposable, TimeoutSupport {
    private static final long serialVersionUID = 3764492702657003550L;
    
    final Observer<? super T> downstream;
    
    final SequentialDisposable task;
    
    final long timeout;
    
    final TimeUnit unit;
    
    final AtomicReference<Disposable> upstream;
    
    final Scheduler.Worker worker;
    
    TimeoutObserver(Observer<? super T> param1Observer, long param1Long, TimeUnit param1TimeUnit, Scheduler.Worker param1Worker) {
      this.downstream = param1Observer;
      this.timeout = param1Long;
      this.unit = param1TimeUnit;
      this.worker = param1Worker;
      this.task = new SequentialDisposable();
      this.upstream = new AtomicReference<Disposable>();
    }
    
    public void dispose() {
      DisposableHelper.dispose(this.upstream);
      this.worker.dispose();
    }
    
    public boolean isDisposed() {
      return DisposableHelper.isDisposed(this.upstream.get());
    }
    
    public void onComplete() {
      if (getAndSet(Long.MAX_VALUE) != Long.MAX_VALUE) {
        this.task.dispose();
        this.downstream.onComplete();
        this.worker.dispose();
      } 
    }
    
    public void onError(Throwable param1Throwable) {
      if (getAndSet(Long.MAX_VALUE) != Long.MAX_VALUE) {
        this.task.dispose();
        this.downstream.onError(param1Throwable);
        this.worker.dispose();
      } else {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
    
    public void onNext(T param1T) {
      long l = get();
      if (l != Long.MAX_VALUE) {
        long l1 = 1L + l;
        if (compareAndSet(l, l1)) {
          ((Disposable)this.task.get()).dispose();
          this.downstream.onNext(param1T);
          startTimeout(l1);
        } 
      } 
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      DisposableHelper.setOnce(this.upstream, param1Disposable);
    }
    
    public void onTimeout(long param1Long) {
      if (compareAndSet(param1Long, Long.MAX_VALUE)) {
        DisposableHelper.dispose(this.upstream);
        this.downstream.onError(new TimeoutException(ExceptionHelper.timeoutMessage(this.timeout, this.unit)));
        this.worker.dispose();
      } 
    }
    
    void startTimeout(long param1Long) {
      this.task.replace(this.worker.schedule(new ObservableTimeoutTimed.TimeoutTask(param1Long, this), this.timeout, this.unit));
    }
  }
  
  static interface TimeoutSupport {
    void onTimeout(long param1Long);
  }
  
  static final class TimeoutTask implements Runnable {
    final long idx;
    
    final ObservableTimeoutTimed.TimeoutSupport parent;
    
    TimeoutTask(long param1Long, ObservableTimeoutTimed.TimeoutSupport param1TimeoutSupport) {
      this.idx = param1Long;
      this.parent = param1TimeoutSupport;
    }
    
    public void run() {
      this.parent.onTimeout(this.idx);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableTimeoutTimed.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */