package io.reactivex.internal.operators.maybe;

import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.disposables.SequentialDisposable;
import java.util.concurrent.atomic.AtomicReference;

public final class MaybeSubscribeOn<T> extends AbstractMaybeWithUpstream<T, T> {
  final Scheduler scheduler;
  
  public MaybeSubscribeOn(MaybeSource<T> paramMaybeSource, Scheduler paramScheduler) {
    super(paramMaybeSource);
    this.scheduler = paramScheduler;
  }
  
  protected void subscribeActual(MaybeObserver<? super T> paramMaybeObserver) {
    SubscribeOnMaybeObserver<T> subscribeOnMaybeObserver = new SubscribeOnMaybeObserver<T>(paramMaybeObserver);
    paramMaybeObserver.onSubscribe(subscribeOnMaybeObserver);
    subscribeOnMaybeObserver.task.replace(this.scheduler.scheduleDirect(new SubscribeTask<T>(subscribeOnMaybeObserver, this.source)));
  }
  
  static final class SubscribeOnMaybeObserver<T> extends AtomicReference<Disposable> implements MaybeObserver<T>, Disposable {
    private static final long serialVersionUID = 8571289934935992137L;
    
    final MaybeObserver<? super T> downstream;
    
    final SequentialDisposable task;
    
    SubscribeOnMaybeObserver(MaybeObserver<? super T> param1MaybeObserver) {
      this.downstream = param1MaybeObserver;
      this.task = new SequentialDisposable();
    }
    
    public void dispose() {
      DisposableHelper.dispose(this);
      this.task.dispose();
    }
    
    public boolean isDisposed() {
      return DisposableHelper.isDisposed(get());
    }
    
    public void onComplete() {
      this.downstream.onComplete();
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
  
  static final class SubscribeTask<T> implements Runnable {
    final MaybeObserver<? super T> observer;
    
    final MaybeSource<T> source;
    
    SubscribeTask(MaybeObserver<? super T> param1MaybeObserver, MaybeSource<T> param1MaybeSource) {
      this.observer = param1MaybeObserver;
      this.source = param1MaybeSource;
    }
    
    public void run() {
      this.source.subscribe(this.observer);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/maybe/MaybeSubscribeOn.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */