package io.reactivex.internal.observers;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.util.BlockingHelper;
import io.reactivex.internal.util.ExceptionHelper;
import java.util.concurrent.CountDownLatch;

public abstract class BlockingBaseObserver<T> extends CountDownLatch implements Observer<T>, Disposable {
  volatile boolean cancelled;
  
  Throwable error;
  
  Disposable upstream;
  
  T value;
  
  public BlockingBaseObserver() {
    super(1);
  }
  
  public final T blockingGet() {
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
  
  public final void dispose() {
    this.cancelled = true;
    Disposable disposable = this.upstream;
    if (disposable != null)
      disposable.dispose(); 
  }
  
  public final boolean isDisposed() {
    return this.cancelled;
  }
  
  public final void onComplete() {
    countDown();
  }
  
  public final void onSubscribe(Disposable paramDisposable) {
    this.upstream = paramDisposable;
    if (this.cancelled)
      paramDisposable.dispose(); 
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/observers/BlockingBaseObserver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */