package io.reactivex.internal.operators.maybe;

import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import java.util.concurrent.atomic.AtomicReference;

public final class MaybeUnsubscribeOn<T> extends AbstractMaybeWithUpstream<T, T> {
  final Scheduler scheduler;
  
  public MaybeUnsubscribeOn(MaybeSource<T> paramMaybeSource, Scheduler paramScheduler) {
    super(paramMaybeSource);
    this.scheduler = paramScheduler;
  }
  
  protected void subscribeActual(MaybeObserver<? super T> paramMaybeObserver) {
    this.source.subscribe(new UnsubscribeOnMaybeObserver<T>(paramMaybeObserver, this.scheduler));
  }
  
  static final class UnsubscribeOnMaybeObserver<T> extends AtomicReference<Disposable> implements MaybeObserver<T>, Disposable, Runnable {
    private static final long serialVersionUID = 3256698449646456986L;
    
    final MaybeObserver<? super T> downstream;
    
    Disposable ds;
    
    final Scheduler scheduler;
    
    UnsubscribeOnMaybeObserver(MaybeObserver<? super T> param1MaybeObserver, Scheduler param1Scheduler) {
      this.downstream = param1MaybeObserver;
      this.scheduler = param1Scheduler;
    }
    
    public void dispose() {
      Disposable disposable = getAndSet((Disposable)DisposableHelper.DISPOSED);
      if (disposable != DisposableHelper.DISPOSED) {
        this.ds = disposable;
        this.scheduler.scheduleDirect(this);
      } 
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
      if (DisposableHelper.setOnce(this, param1Disposable))
        this.downstream.onSubscribe(this); 
    }
    
    public void onSuccess(T param1T) {
      this.downstream.onSuccess(param1T);
    }
    
    public void run() {
      this.ds.dispose();
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/maybe/MaybeUnsubscribeOn.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */