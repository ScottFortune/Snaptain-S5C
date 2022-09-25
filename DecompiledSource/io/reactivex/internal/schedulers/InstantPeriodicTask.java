package io.reactivex.internal.schedulers;

import io.reactivex.disposables.Disposable;
import io.reactivex.internal.functions.Functions;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicReference;

final class InstantPeriodicTask implements Callable<Void>, Disposable {
  static final FutureTask<Void> CANCELLED = new FutureTask<Void>(Functions.EMPTY_RUNNABLE, null);
  
  final ExecutorService executor;
  
  final AtomicReference<Future<?>> first;
  
  final AtomicReference<Future<?>> rest;
  
  Thread runner;
  
  final Runnable task;
  
  InstantPeriodicTask(Runnable paramRunnable, ExecutorService paramExecutorService) {
    this.task = paramRunnable;
    this.first = new AtomicReference<Future<?>>();
    this.rest = new AtomicReference<Future<?>>();
    this.executor = paramExecutorService;
  }
  
  public Void call() throws Exception {
    this.runner = Thread.currentThread();
  }
  
  public void dispose() {
    Future future = this.first.getAndSet(CANCELLED);
    boolean bool = true;
    if (future != null && future != CANCELLED) {
      boolean bool1;
      if (this.runner != Thread.currentThread()) {
        bool1 = true;
      } else {
        bool1 = false;
      } 
      future.cancel(bool1);
    } 
    future = this.rest.getAndSet(CANCELLED);
    if (future != null && future != CANCELLED) {
      boolean bool1;
      if (this.runner != Thread.currentThread()) {
        bool1 = bool;
      } else {
        bool1 = false;
      } 
      future.cancel(bool1);
    } 
  }
  
  public boolean isDisposed() {
    boolean bool;
    if (this.first.get() == CANCELLED) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  void setFirst(Future<?> paramFuture) {
    Future<?> future;
    do {
      future = this.first.get();
      if (future == CANCELLED) {
        boolean bool;
        if (this.runner != Thread.currentThread()) {
          bool = true;
        } else {
          bool = false;
        } 
        paramFuture.cancel(bool);
        return;
      } 
    } while (!this.first.compareAndSet(future, paramFuture));
  }
  
  void setRest(Future<?> paramFuture) {
    Future<?> future;
    do {
      future = this.rest.get();
      if (future == CANCELLED) {
        boolean bool;
        if (this.runner != Thread.currentThread()) {
          bool = true;
        } else {
          bool = false;
        } 
        paramFuture.cancel(bool);
        return;
      } 
    } while (!this.rest.compareAndSet(future, paramFuture));
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/schedulers/InstantPeriodicTask.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */