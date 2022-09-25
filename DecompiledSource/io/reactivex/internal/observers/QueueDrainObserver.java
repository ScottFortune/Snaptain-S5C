package io.reactivex.internal.observers;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.fuseable.SimplePlainQueue;
import io.reactivex.internal.util.ObservableQueueDrain;
import io.reactivex.internal.util.QueueDrainHelper;

public abstract class QueueDrainObserver<T, U, V> extends QueueDrainSubscriberPad2 implements Observer<T>, ObservableQueueDrain<U, V> {
  protected volatile boolean cancelled;
  
  protected volatile boolean done;
  
  protected final Observer<? super V> downstream;
  
  protected Throwable error;
  
  protected final SimplePlainQueue<U> queue;
  
  public QueueDrainObserver(Observer<? super V> paramObserver, SimplePlainQueue<U> paramSimplePlainQueue) {
    this.downstream = paramObserver;
    this.queue = paramSimplePlainQueue;
  }
  
  public void accept(Observer<? super V> paramObserver, U paramU) {}
  
  public final boolean cancelled() {
    return this.cancelled;
  }
  
  public final boolean done() {
    return this.done;
  }
  
  public final boolean enter() {
    boolean bool;
    if (this.wip.getAndIncrement() == 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public final Throwable error() {
    return this.error;
  }
  
  public final boolean fastEnter() {
    int i = this.wip.get();
    boolean bool = true;
    if (i != 0 || !this.wip.compareAndSet(0, 1))
      bool = false; 
    return bool;
  }
  
  protected final void fastPathEmit(U paramU, boolean paramBoolean, Disposable paramDisposable) {
    Observer<? super V> observer = this.downstream;
    SimplePlainQueue<U> simplePlainQueue = this.queue;
    if (this.wip.get() == 0 && this.wip.compareAndSet(0, 1)) {
      accept(observer, paramU);
      if (leave(-1) == 0)
        return; 
    } else {
      simplePlainQueue.offer(paramU);
      if (!enter())
        return; 
    } 
    QueueDrainHelper.drainLoop(simplePlainQueue, observer, paramBoolean, paramDisposable, this);
  }
  
  protected final void fastPathOrderedEmit(U paramU, boolean paramBoolean, Disposable paramDisposable) {
    Observer<? super V> observer = this.downstream;
    SimplePlainQueue<U> simplePlainQueue = this.queue;
    if (this.wip.get() == 0 && this.wip.compareAndSet(0, 1)) {
      if (simplePlainQueue.isEmpty()) {
        accept(observer, paramU);
        if (leave(-1) == 0)
          return; 
      } else {
        simplePlainQueue.offer(paramU);
      } 
    } else {
      simplePlainQueue.offer(paramU);
      if (!enter())
        return; 
    } 
    QueueDrainHelper.drainLoop(simplePlainQueue, observer, paramBoolean, paramDisposable, this);
  }
  
  public final int leave(int paramInt) {
    return this.wip.addAndGet(paramInt);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/observers/QueueDrainObserver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */