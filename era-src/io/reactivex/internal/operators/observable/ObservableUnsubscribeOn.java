package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicBoolean;

public final class ObservableUnsubscribeOn<T> extends AbstractObservableWithUpstream<T, T> {
  final Scheduler scheduler;
  
  public ObservableUnsubscribeOn(ObservableSource<T> paramObservableSource, Scheduler paramScheduler) {
    super(paramObservableSource);
    this.scheduler = paramScheduler;
  }
  
  public void subscribeActual(Observer<? super T> paramObserver) {
    this.source.subscribe(new UnsubscribeObserver<T>(paramObserver, this.scheduler));
  }
  
  static final class UnsubscribeObserver<T> extends AtomicBoolean implements Observer<T>, Disposable {
    private static final long serialVersionUID = 1015244841293359600L;
    
    final Observer<? super T> downstream;
    
    final Scheduler scheduler;
    
    Disposable upstream;
    
    UnsubscribeObserver(Observer<? super T> param1Observer, Scheduler param1Scheduler) {
      this.downstream = param1Observer;
      this.scheduler = param1Scheduler;
    }
    
    public void dispose() {
      if (compareAndSet(false, true))
        this.scheduler.scheduleDirect(new DisposeTask()); 
    }
    
    public boolean isDisposed() {
      return get();
    }
    
    public void onComplete() {
      if (!get())
        this.downstream.onComplete(); 
    }
    
    public void onError(Throwable param1Throwable) {
      if (get()) {
        RxJavaPlugins.onError(param1Throwable);
        return;
      } 
      this.downstream.onError(param1Throwable);
    }
    
    public void onNext(T param1T) {
      if (!get())
        this.downstream.onNext(param1T); 
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      if (DisposableHelper.validate(this.upstream, param1Disposable)) {
        this.upstream = param1Disposable;
        this.downstream.onSubscribe(this);
      } 
    }
    
    final class DisposeTask implements Runnable {
      public void run() {
        ObservableUnsubscribeOn.UnsubscribeObserver.this.upstream.dispose();
      }
    }
  }
  
  final class DisposeTask implements Runnable {
    public void run() {
      this.this$0.upstream.dispose();
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableUnsubscribeOn.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */