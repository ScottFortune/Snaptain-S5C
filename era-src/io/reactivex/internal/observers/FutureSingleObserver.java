package io.reactivex.internal.observers;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.util.BlockingHelper;
import io.reactivex.internal.util.ExceptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

public final class FutureSingleObserver<T> extends CountDownLatch implements SingleObserver<T>, Future<T>, Disposable {
  Throwable error;
  
  final AtomicReference<Disposable> upstream = new AtomicReference<Disposable>();
  
  T value;
  
  public FutureSingleObserver() {
    super(1);
  }
  
  public boolean cancel(boolean paramBoolean) {
    while (true) {
      Disposable disposable = this.upstream.get();
      if (disposable == this || disposable == DisposableHelper.DISPOSED)
        break; 
      if (this.upstream.compareAndSet(disposable, DisposableHelper.DISPOSED)) {
        if (disposable != null)
          disposable.dispose(); 
        countDown();
        return true;
      } 
    } 
    return false;
  }
  
  public void dispose() {}
  
  public T get() throws InterruptedException, ExecutionException {
    if (getCount() != 0L) {
      BlockingHelper.verifyNonBlocking();
      await();
    } 
    if (!isCancelled()) {
      Throwable throwable = this.error;
      if (throwable == null)
        return this.value; 
      throw new ExecutionException(throwable);
    } 
    throw new CancellationException();
  }
  
  public T get(long paramLong, TimeUnit paramTimeUnit) throws InterruptedException, ExecutionException, TimeoutException {
    if (getCount() != 0L) {
      BlockingHelper.verifyNonBlocking();
      if (!await(paramLong, paramTimeUnit))
        throw new TimeoutException(ExceptionHelper.timeoutMessage(paramLong, paramTimeUnit)); 
    } 
    if (!isCancelled()) {
      Throwable throwable = this.error;
      if (throwable == null)
        return this.value; 
      throw new ExecutionException(throwable);
    } 
    throw new CancellationException();
  }
  
  public boolean isCancelled() {
    return DisposableHelper.isDisposed(this.upstream.get());
  }
  
  public boolean isDisposed() {
    return isDone();
  }
  
  public boolean isDone() {
    boolean bool;
    if (getCount() == 0L) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void onError(Throwable paramThrowable) {
    while (true) {
      Disposable disposable = this.upstream.get();
      if (disposable == DisposableHelper.DISPOSED) {
        RxJavaPlugins.onError(paramThrowable);
        return;
      } 
      this.error = paramThrowable;
      if (this.upstream.compareAndSet(disposable, this)) {
        countDown();
        return;
      } 
    } 
  }
  
  public void onSubscribe(Disposable paramDisposable) {
    DisposableHelper.setOnce(this.upstream, paramDisposable);
  }
  
  public void onSuccess(T paramT) {
    Disposable disposable = this.upstream.get();
    if (disposable == DisposableHelper.DISPOSED)
      return; 
    this.value = paramT;
    this.upstream.compareAndSet(disposable, this);
    countDown();
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/observers/FutureSingleObserver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */