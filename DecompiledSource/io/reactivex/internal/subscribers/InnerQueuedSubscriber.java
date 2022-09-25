package io.reactivex.internal.subscribers;

import io.reactivex.FlowableSubscriber;
import io.reactivex.internal.fuseable.QueueSubscription;
import io.reactivex.internal.fuseable.SimpleQueue;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.QueueDrainHelper;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscription;

public final class InnerQueuedSubscriber<T> extends AtomicReference<Subscription> implements FlowableSubscriber<T>, Subscription {
  private static final long serialVersionUID = 22876611072430776L;
  
  volatile boolean done;
  
  int fusionMode;
  
  final int limit;
  
  final InnerQueuedSubscriberSupport<T> parent;
  
  final int prefetch;
  
  long produced;
  
  volatile SimpleQueue<T> queue;
  
  public InnerQueuedSubscriber(InnerQueuedSubscriberSupport<T> paramInnerQueuedSubscriberSupport, int paramInt) {
    this.parent = paramInnerQueuedSubscriberSupport;
    this.prefetch = paramInt;
    this.limit = paramInt - (paramInt >> 2);
  }
  
  public void cancel() {
    SubscriptionHelper.cancel(this);
  }
  
  public boolean isDone() {
    return this.done;
  }
  
  public void onComplete() {
    this.parent.innerComplete(this);
  }
  
  public void onError(Throwable paramThrowable) {
    this.parent.innerError(this, paramThrowable);
  }
  
  public void onNext(T paramT) {
    if (this.fusionMode == 0) {
      this.parent.innerNext(this, paramT);
    } else {
      this.parent.drain();
    } 
  }
  
  public void onSubscribe(Subscription paramSubscription) {
    if (SubscriptionHelper.setOnce(this, paramSubscription)) {
      if (paramSubscription instanceof QueueSubscription) {
        QueueSubscription queueSubscription = (QueueSubscription)paramSubscription;
        int i = queueSubscription.requestFusion(3);
        if (i == 1) {
          this.fusionMode = i;
          this.queue = (SimpleQueue<T>)queueSubscription;
          this.done = true;
          this.parent.innerComplete(this);
          return;
        } 
        if (i == 2) {
          this.fusionMode = i;
          this.queue = (SimpleQueue<T>)queueSubscription;
          QueueDrainHelper.request(paramSubscription, this.prefetch);
          return;
        } 
      } 
      this.queue = QueueDrainHelper.createQueue(this.prefetch);
      QueueDrainHelper.request(paramSubscription, this.prefetch);
    } 
  }
  
  public SimpleQueue<T> queue() {
    return this.queue;
  }
  
  public void request(long paramLong) {
    if (this.fusionMode != 1) {
      paramLong = this.produced + paramLong;
      if (paramLong >= this.limit) {
        this.produced = 0L;
        get().request(paramLong);
      } else {
        this.produced = paramLong;
      } 
    } 
  }
  
  public void requestOne() {
    if (this.fusionMode != 1) {
      long l = this.produced + 1L;
      if (l == this.limit) {
        this.produced = 0L;
        get().request(l);
      } else {
        this.produced = l;
      } 
    } 
  }
  
  public void setDone() {
    this.done = true;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/subscribers/InnerQueuedSubscriber.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */