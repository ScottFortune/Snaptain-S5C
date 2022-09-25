package io.reactivex.internal.observers;

import io.reactivex.CompletableObserver;
import io.reactivex.MaybeObserver;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.util.BlockingHelper;
import io.reactivex.internal.util.ExceptionHelper;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public final class BlockingMultiObserver<T> extends CountDownLatch implements SingleObserver<T>, CompletableObserver, MaybeObserver<T> {
  volatile boolean cancelled;
  
  Throwable error;
  
  Disposable upstream;
  
  T value;
  
  public BlockingMultiObserver() {
    super(1);
  }
  
  public boolean blockingAwait(long paramLong, TimeUnit paramTimeUnit) {
    if (getCount() != 0L)
      try {
        BlockingHelper.verifyNonBlocking();
        if (!await(paramLong, paramTimeUnit)) {
          dispose();
          return false;
        } 
      } catch (InterruptedException interruptedException) {
        dispose();
        throw ExceptionHelper.wrapOrThrow(interruptedException);
      }  
    Throwable throwable = this.error;
    if (throwable == null)
      return true; 
    throw ExceptionHelper.wrapOrThrow(throwable);
  }
  
  public T blockingGet() {
    if (getCount() != 0L)
      try {
        BlockingHelper.verifyNonBlocking();
        await();
      } catch (InterruptedException interruptedException) {
        dispose();
        throw ExceptionHelper.wrapOrThrow(interruptedException);
      }  
    Throwable throwable = this.error;
    if (throwable == null)
      return this.value; 
    throw ExceptionHelper.wrapOrThrow(throwable);
  }
  
  public T blockingGet(T paramT) {
    T t;
    if (getCount() != 0L)
      try {
        BlockingHelper.verifyNonBlocking();
        await();
      } catch (InterruptedException interruptedException) {
        dispose();
        throw ExceptionHelper.wrapOrThrow(interruptedException);
      }  
    Throwable throwable = this.error;
    if (throwable == null) {
      t = this.value;
      if (t != null)
        paramT = t; 
      return paramT;
    } 
    throw ExceptionHelper.wrapOrThrow(t);
  }
  
  public Throwable blockingGetError() {
    if (getCount() != 0L)
      try {
        BlockingHelper.verifyNonBlocking();
        await();
      } catch (InterruptedException interruptedException) {
        dispose();
        return interruptedException;
      }  
    return this.error;
  }
  
  public Throwable blockingGetError(long paramLong, TimeUnit paramTimeUnit) {
    if (getCount() != 0L)
      try {
        BlockingHelper.verifyNonBlocking();
        if (!await(paramLong, paramTimeUnit)) {
          dispose();
          TimeoutException timeoutException = new TimeoutException();
          this(ExceptionHelper.timeoutMessage(paramLong, paramTimeUnit));
          throw ExceptionHelper.wrapOrThrow(timeoutException);
        } 
      } catch (InterruptedException interruptedException) {
        dispose();
        throw ExceptionHelper.wrapOrThrow(interruptedException);
      }  
    return this.error;
  }
  
  void dispose() {
    this.cancelled = true;
    Disposable disposable = this.upstream;
    if (disposable != null)
      disposable.dispose(); 
  }
  
  public void onComplete() {
    countDown();
  }
  
  public void onError(Throwable paramThrowable) {
    this.error = paramThrowable;
    countDown();
  }
  
  public void onSubscribe(Disposable paramDisposable) {
    this.upstream = paramDisposable;
    if (this.cancelled)
      paramDisposable.dispose(); 
  }
  
  public void onSuccess(T paramT) {
    this.value = paramT;
    countDown();
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/observers/BlockingMultiObserver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */