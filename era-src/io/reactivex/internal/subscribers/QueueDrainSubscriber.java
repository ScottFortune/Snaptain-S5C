package io.reactivex.internal.subscribers;

import io.reactivex.FlowableSubscriber;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.MissingBackpressureException;
import io.reactivex.internal.fuseable.SimplePlainQueue;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.internal.util.QueueDrain;
import io.reactivex.internal.util.QueueDrainHelper;
import org.reactivestreams.Subscriber;

public abstract class QueueDrainSubscriber<T, U, V> extends QueueDrainSubscriberPad4 implements FlowableSubscriber<T>, QueueDrain<U, V> {
  protected volatile boolean cancelled;
  
  protected volatile boolean done;
  
  protected final Subscriber<? super V> downstream;
  
  protected Throwable error;
  
  protected final SimplePlainQueue<U> queue;
  
  public QueueDrainSubscriber(Subscriber<? super V> paramSubscriber, SimplePlainQueue<U> paramSimplePlainQueue) {
    this.downstream = paramSubscriber;
    this.queue = paramSimplePlainQueue;
  }
  
  public boolean accept(Subscriber<? super V> paramSubscriber, U paramU) {
    return false;
  }
  
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
  
  protected final void fastPathEmitMax(U paramU, boolean paramBoolean, Disposable paramDisposable) {
    Subscriber<? super V> subscriber = this.downstream;
    SimplePlainQueue<U> simplePlainQueue = this.queue;
    if (fastEnter()) {
      long l = this.requested.get();
      if (l != 0L) {
        if (accept(subscriber, paramU) && l != Long.MAX_VALUE)
          produced(1L); 
        if (leave(-1) == 0)
          return; 
      } else {
        paramDisposable.dispose();
        subscriber.onError((Throwable)new MissingBackpressureException("Could not emit buffer due to lack of requests"));
        return;
      } 
    } else {
      simplePlainQueue.offer(paramU);
      if (!enter())
        return; 
    } 
    QueueDrainHelper.drainMaxLoop(simplePlainQueue, subscriber, paramBoolean, paramDisposable, this);
  }
  
  protected final void fastPathOrderedEmitMax(U paramU, boolean paramBoolean, Disposable paramDisposable) {
    Subscriber<? super V> subscriber = this.downstream;
    SimplePlainQueue<U> simplePlainQueue = this.queue;
    if (fastEnter()) {
      long l = this.requested.get();
      if (l != 0L) {
        if (simplePlainQueue.isEmpty()) {
          if (accept(subscriber, paramU) && l != Long.MAX_VALUE)
            produced(1L); 
          if (leave(-1) == 0)
            return; 
        } else {
          simplePlainQueue.offer(paramU);
        } 
      } else {
        this.cancelled = true;
        paramDisposable.dispose();
        subscriber.onError((Throwable)new MissingBackpressureException("Could not emit buffer due to lack of requests"));
        return;
      } 
    } else {
      simplePlainQueue.offer(paramU);
      if (!enter())
        return; 
    } 
    QueueDrainHelper.drainMaxLoop(simplePlainQueue, subscriber, paramBoolean, paramDisposable, this);
  }
  
  public final int leave(int paramInt) {
    return this.wip.addAndGet(paramInt);
  }
  
  public final long produced(long paramLong) {
    return this.requested.addAndGet(-paramLong);
  }
  
  public final long requested() {
    return this.requested.get();
  }
  
  public final void requested(long paramLong) {
    if (SubscriptionHelper.validate(paramLong))
      BackpressureHelper.add(this.requested, paramLong); 
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/subscribers/QueueDrainSubscriber.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */