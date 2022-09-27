package io.reactivex.internal.observers;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.util.BlockingHelper;
import io.reactivex.internal.util.ExceptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.NoSuchElementException;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

public final class FutureObserver<T> extends CountDownLatch implements Observer<T>, Future<T>, Disposable {
  Throwable error;
  
  final AtomicReference<Disposable> upstream = new AtomicReference<Disposable>();
  
  T value;
  
  public FutureObserver() {
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
  
  public void onComplete() {
    if (this.value == null) {
      onError(new NoSuchElementException("The source is empty"));
      return;
    } 
    while (true) {
      Disposable disposable = this.upstream.get();
      if (disposable == this || disposable == DisposableHelper.DISPOSED)
        break; 
      if (this.upstream.compareAndSet(disposable, this)) {
        countDown();
        break;
      } 
    } 
  }
  
  public void onError(Throwable paramThrowable) {
    if (this.error == null) {
      this.error = paramThrowable;
      while (true) {
        Disposable disposable = this.upstream.get();
        if (disposable == this || disposable == DisposableHelper.DISPOSED)
          break; 
        if (this.upstream.compareAndSet(disposable, this)) {
          countDown();
          return;
        } 
      } 
      RxJavaPlugins.onError(paramThrowable);
      return;
    } 
    RxJavaPlugins.onError(paramThrowable);
  }
  
  public void onNext(T paramT) {
    if (this.value != null) {
      ((Disposable)this.upstream.get()).dispose();
      onError(new IndexOutOfBoundsException("More than one element received"));
      return;
    } 
    this.value = paramT;
  }
  
  public void onSubscribe(Disposable paramDisposable) {
    DisposableHelper.setOnce(this.upstream, paramDisposable);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/observers/FutureObserver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */