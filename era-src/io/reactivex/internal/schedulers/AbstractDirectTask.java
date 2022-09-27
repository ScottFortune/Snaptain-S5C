package io.reactivex.internal.schedulers;

import io.reactivex.disposables.Disposable;
import io.reactivex.internal.functions.Functions;
import io.reactivex.schedulers.SchedulerRunnableIntrospection;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicReference;

abstract class AbstractDirectTask extends AtomicReference<Future<?>> implements Disposable, SchedulerRunnableIntrospection {
  protected static final FutureTask<Void> DISPOSED;
  
  protected static final FutureTask<Void> FINISHED = new FutureTask<Void>(Functions.EMPTY_RUNNABLE, null);
  
  private static final long serialVersionUID = 1811839108042568751L;
  
  protected final Runnable runnable;
  
  protected Thread runner;
  
  static {
    DISPOSED = new FutureTask<Void>(Functions.EMPTY_RUNNABLE, null);
  }
  
  AbstractDirectTask(Runnable paramRunnable) {
    this.runnable = paramRunnable;
  }
  
  public final void dispose() {
    Future<?> future = get();
    if (future != FINISHED) {
      FutureTask<Void> futureTask = DISPOSED;
      if (future != futureTask && compareAndSet(future, futureTask) && future != null) {
        boolean bool;
        if (this.runner != Thread.currentThread()) {
          bool = true;
        } else {
          bool = false;
        } 
        future.cancel(bool);
      } 
    } 
  }
  
  public Runnable getWrappedRunnable() {
    return this.runnable;
  }
  
  public final boolean isDisposed() {
    Future<?> future = get();
    return (future == FINISHED || future == DISPOSED);
  }
  
  public final void setFuture(Future<?> paramFuture) {
    Future<?> future;
    do {
      future = get();
      if (future == FINISHED)
        break; 
      if (future == DISPOSED) {
        boolean bool;
        if (this.runner != Thread.currentThread()) {
          bool = true;
        } else {
          bool = false;
        } 
        paramFuture.cancel(bool);
        break;
      } 
    } while (!compareAndSet(future, paramFuture));
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/schedulers/AbstractDirectTask.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */