package io.reactivex.internal.operators.single;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.util.ExceptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

public final class SingleTimeout<T> extends Single<T> {
  final SingleSource<? extends T> other;
  
  final Scheduler scheduler;
  
  final SingleSource<T> source;
  
  final long timeout;
  
  final TimeUnit unit;
  
  public SingleTimeout(SingleSource<T> paramSingleSource, long paramLong, TimeUnit paramTimeUnit, Scheduler paramScheduler, SingleSource<? extends T> paramSingleSource1) {
    this.source = paramSingleSource;
    this.timeout = paramLong;
    this.unit = paramTimeUnit;
    this.scheduler = paramScheduler;
    this.other = paramSingleSource1;
  }
  
  protected void subscribeActual(SingleObserver<? super T> paramSingleObserver) {
    TimeoutMainObserver<T> timeoutMainObserver = new TimeoutMainObserver<T>(paramSingleObserver, this.other, this.timeout, this.unit);
    paramSingleObserver.onSubscribe(timeoutMainObserver);
    DisposableHelper.replace(timeoutMainObserver.task, this.scheduler.scheduleDirect(timeoutMainObserver, this.timeout, this.unit));
    this.source.subscribe(timeoutMainObserver);
  }
  
  static final class TimeoutMainObserver<T> extends AtomicReference<Disposable> implements SingleObserver<T>, Runnable, Disposable {
    private static final long serialVersionUID = 37497744973048446L;
    
    final SingleObserver<? super T> downstream;
    
    final TimeoutFallbackObserver<T> fallback;
    
    SingleSource<? extends T> other;
    
    final AtomicReference<Disposable> task;
    
    final long timeout;
    
    final TimeUnit unit;
    
    TimeoutMainObserver(SingleObserver<? super T> param1SingleObserver, SingleSource<? extends T> param1SingleSource, long param1Long, TimeUnit param1TimeUnit) {
      this.downstream = param1SingleObserver;
      this.other = param1SingleSource;
      this.timeout = param1Long;
      this.unit = param1TimeUnit;
      this.task = new AtomicReference<Disposable>();
      if (param1SingleSource != null) {
        this.fallback = new TimeoutFallbackObserver<T>(param1SingleObserver);
      } else {
        this.fallback = null;
      } 
    }
    
    public void dispose() {
      DisposableHelper.dispose(this);
      DisposableHelper.dispose(this.task);
      TimeoutFallbackObserver<T> timeoutFallbackObserver = this.fallback;
      if (timeoutFallbackObserver != null)
        DisposableHelper.dispose(timeoutFallbackObserver); 
    }
    
    public boolean isDisposed() {
      return DisposableHelper.isDisposed(get());
    }
    
    public void onError(Throwable param1Throwable) {
      Disposable disposable = get();
      if (disposable != DisposableHelper.DISPOSED && compareAndSet(disposable, (Disposable)DisposableHelper.DISPOSED)) {
        DisposableHelper.dispose(this.task);
        this.downstream.onError(param1Throwable);
      } else {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      DisposableHelper.setOnce(this, param1Disposable);
    }
    
    public void onSuccess(T param1T) {
      Disposable disposable = get();
      if (disposable != DisposableHelper.DISPOSED && compareAndSet(disposable, (Disposable)DisposableHelper.DISPOSED)) {
        DisposableHelper.dispose(this.task);
        this.downstream.onSuccess(param1T);
      } 
    }
    
    public void run() {
      Disposable disposable = get();
      if (disposable != DisposableHelper.DISPOSED && compareAndSet(disposable, (Disposable)DisposableHelper.DISPOSED)) {
        if (disposable != null)
          disposable.dispose(); 
        SingleSource<? extends T> singleSource = this.other;
        if (singleSource == null) {
          this.downstream.onError(new TimeoutException(ExceptionHelper.timeoutMessage(this.timeout, this.unit)));
        } else {
          this.other = null;
          singleSource.subscribe(this.fallback);
        } 
      } 
    }
    
    static final class TimeoutFallbackObserver<T> extends AtomicReference<Disposable> implements SingleObserver<T> {
      private static final long serialVersionUID = 2071387740092105509L;
      
      final SingleObserver<? super T> downstream;
      
      TimeoutFallbackObserver(SingleObserver<? super T> param2SingleObserver) {
        this.downstream = param2SingleObserver;
      }
      
      public void onError(Throwable param2Throwable) {
        this.downstream.onError(param2Throwable);
      }
      
      public void onSubscribe(Disposable param2Disposable) {
        DisposableHelper.setOnce(this, param2Disposable);
      }
      
      public void onSuccess(T param2T) {
        this.downstream.onSuccess(param2T);
      }
    }
  }
  
  static final class TimeoutFallbackObserver<T> extends AtomicReference<Disposable> implements SingleObserver<T> {
    private static final long serialVersionUID = 2071387740092105509L;
    
    final SingleObserver<? super T> downstream;
    
    TimeoutFallbackObserver(SingleObserver<? super T> param1SingleObserver) {
      this.downstream = param1SingleObserver;
    }
    
    public void onError(Throwable param1Throwable) {
      this.downstream.onError(param1Throwable);
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      DisposableHelper.setOnce(this, param1Disposable);
    }
    
    public void onSuccess(T param1T) {
      this.downstream.onSuccess(param1T);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/single/SingleTimeout.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */