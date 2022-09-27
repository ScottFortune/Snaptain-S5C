package io.reactivex.internal.observers;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.util.NotificationLite;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicReference;

public final class BlockingObserver<T> extends AtomicReference<Disposable> implements Observer<T>, Disposable {
  public static final Object TERMINATED = new Object();
  
  private static final long serialVersionUID = -4875965440900746268L;
  
  final Queue<Object> queue;
  
  public BlockingObserver(Queue<Object> paramQueue) {
    this.queue = paramQueue;
  }
  
  public void dispose() {
    if (DisposableHelper.dispose(this))
      this.queue.offer(TERMINATED); 
  }
  
  public boolean isDisposed() {
    boolean bool;
    if (get() == DisposableHelper.DISPOSED) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void onComplete() {
    this.queue.offer(NotificationLite.complete());
  }
  
  public void onError(Throwable paramThrowable) {
    this.queue.offer(NotificationLite.error(paramThrowable));
  }
  
  public void onNext(T paramT) {
    this.queue.offer(NotificationLite.next(paramT));
  }
  
  public void onSubscribe(Disposable paramDisposable) {
    DisposableHelper.setOnce(this, paramDisposable);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/observers/BlockingObserver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */