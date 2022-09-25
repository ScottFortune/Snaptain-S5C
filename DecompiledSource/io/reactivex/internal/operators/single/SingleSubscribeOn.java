package io.reactivex.internal.operators.single;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.disposables.SequentialDisposable;
import java.util.concurrent.atomic.AtomicReference;

public final class SingleSubscribeOn<T> extends Single<T> {
  final Scheduler scheduler;
  
  final SingleSource<? extends T> source;
  
  public SingleSubscribeOn(SingleSource<? extends T> paramSingleSource, Scheduler paramScheduler) {
    this.source = paramSingleSource;
    this.scheduler = paramScheduler;
  }
  
  protected void subscribeActual(SingleObserver<? super T> paramSingleObserver) {
    SubscribeOnObserver<T> subscribeOnObserver = new SubscribeOnObserver<T>(paramSingleObserver, this.source);
    paramSingleObserver.onSubscribe(subscribeOnObserver);
    Disposable disposable = this.scheduler.scheduleDirect(subscribeOnObserver);
    subscribeOnObserver.task.replace(disposable);
  }
  
  static final class SubscribeOnObserver<T> extends AtomicReference<Disposable> implements SingleObserver<T>, Disposable, Runnable {
    private static final long serialVersionUID = 7000911171163930287L;
    
    final SingleObserver<? super T> downstream;
    
    final SingleSource<? extends T> source;
    
    final SequentialDisposable task;
    
    SubscribeOnObserver(SingleObserver<? super T> param1SingleObserver, SingleSource<? extends T> param1SingleSource) {
      this.downstream = param1SingleObserver;
      this.source = param1SingleSource;
      this.task = new SequentialDisposable();
    }
    
    public void dispose() {
      DisposableHelper.dispose(this);
      this.task.dispose();
    }
    
    public boolean isDisposed() {
      return DisposableHelper.isDisposed(get());
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
    
    public void run() {
      this.source.subscribe(this);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/single/SingleSubscribeOn.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */