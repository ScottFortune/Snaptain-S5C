package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.exceptions.MissingBackpressureException;
import io.reactivex.functions.Action;
import io.reactivex.internal.fuseable.SimplePlainQueue;
import io.reactivex.internal.queue.SpscArrayQueue;
import io.reactivex.internal.queue.SpscLinkedArrayQueue;
import io.reactivex.internal.subscriptions.BasicIntQueueSubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import java.util.concurrent.atomic.AtomicLong;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableOnBackpressureBuffer<T> extends AbstractFlowableWithUpstream<T, T> {
  final int bufferSize;
  
  final boolean delayError;
  
  final Action onOverflow;
  
  final boolean unbounded;
  
  public FlowableOnBackpressureBuffer(Flowable<T> paramFlowable, int paramInt, boolean paramBoolean1, boolean paramBoolean2, Action paramAction) {
    super(paramFlowable);
    this.bufferSize = paramInt;
    this.unbounded = paramBoolean1;
    this.delayError = paramBoolean2;
    this.onOverflow = paramAction;
  }
  
  protected void subscribeActual(Subscriber<? super T> paramSubscriber) {
    this.source.subscribe(new BackpressureBufferSubscriber<T>(paramSubscriber, this.bufferSize, this.unbounded, this.delayError, this.onOverflow));
  }
  
  static final class BackpressureBufferSubscriber<T> extends BasicIntQueueSubscription<T> implements FlowableSubscriber<T> {
    private static final long serialVersionUID = -2514538129242366402L;
    
    volatile boolean cancelled;
    
    final boolean delayError;
    
    volatile boolean done;
    
    final Subscriber<? super T> downstream;
    
    Throwable error;
    
    final Action onOverflow;
    
    boolean outputFused;
    
    final SimplePlainQueue<T> queue;
    
    final AtomicLong requested;
    
    Subscription upstream;
    
    BackpressureBufferSubscriber(Subscriber<? super T> param1Subscriber, int param1Int, boolean param1Boolean1, boolean param1Boolean2, Action param1Action) {
      SpscArrayQueue spscArrayQueue;
      this.requested = new AtomicLong();
      this.downstream = param1Subscriber;
      this.onOverflow = param1Action;
      this.delayError = param1Boolean2;
      if (param1Boolean1) {
        SpscLinkedArrayQueue spscLinkedArrayQueue = new SpscLinkedArrayQueue(param1Int);
      } else {
        spscArrayQueue = new SpscArrayQueue(param1Int);
      } 
      this.queue = (SimplePlainQueue<T>)spscArrayQueue;
    }
    
    public void cancel() {
      if (!this.cancelled) {
        this.cancelled = true;
        this.upstream.cancel();
        if (!this.outputFused && getAndIncrement() == 0)
          this.queue.clear(); 
      } 
    }
    
    boolean checkTerminated(boolean param1Boolean1, boolean param1Boolean2, Subscriber<? super T> param1Subscriber) {
      if (this.cancelled) {
        this.queue.clear();
        return true;
      } 
      if (param1Boolean1)
        if (this.delayError) {
          if (param1Boolean2) {
            Throwable throwable = this.error;
            if (throwable != null) {
              param1Subscriber.onError(throwable);
            } else {
              param1Subscriber.onComplete();
            } 
            return true;
          } 
        } else {
          Throwable throwable = this.error;
          if (throwable != null) {
            this.queue.clear();
            param1Subscriber.onError(throwable);
            return true;
          } 
          if (param1Boolean2) {
            param1Subscriber.onComplete();
            return true;
          } 
        }  
      return false;
    }
    
    public void clear() {
      this.queue.clear();
    }
    
    void drain() {
      if (getAndIncrement() == 0) {
        int j;
        SimplePlainQueue<T> simplePlainQueue = this.queue;
        Subscriber<? super T> subscriber = this.downstream;
        int i = 1;
        do {
          if (checkTerminated(this.done, simplePlainQueue.isEmpty(), subscriber))
            return; 
          long l1 = this.requested.get();
          long l2;
          for (l2 = 0L; l2 != l1; l2++) {
            boolean bool1;
            boolean bool = this.done;
            Object object = simplePlainQueue.poll();
            if (object == null) {
              bool1 = true;
            } else {
              bool1 = false;
            } 
            if (checkTerminated(bool, bool1, subscriber))
              return; 
            if (bool1)
              break; 
            subscriber.onNext(object);
          } 
          if (l2 == l1 && checkTerminated(this.done, simplePlainQueue.isEmpty(), subscriber))
            return; 
          if (l2 != 0L && l1 != Long.MAX_VALUE)
            this.requested.addAndGet(-l2); 
          j = addAndGet(-i);
          i = j;
        } while (j != 0);
      } 
    }
    
    public boolean isEmpty() {
      return this.queue.isEmpty();
    }
    
    public void onComplete() {
      this.done = true;
      if (this.outputFused) {
        this.downstream.onComplete();
      } else {
        drain();
      } 
    }
    
    public void onError(Throwable param1Throwable) {
      this.error = param1Throwable;
      this.done = true;
      if (this.outputFused) {
        this.downstream.onError(param1Throwable);
      } else {
        drain();
      } 
    }
    
    public void onNext(T param1T) {
      if (!this.queue.offer(param1T)) {
        this.upstream.cancel();
        MissingBackpressureException missingBackpressureException = new MissingBackpressureException("Buffer is full");
        try {
          this.onOverflow.run();
        } finally {
          Exception exception = null;
          Exceptions.throwIfFatal(exception);
        } 
      } 
      if (this.outputFused) {
        this.downstream.onNext(null);
      } else {
        drain();
      } 
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      if (SubscriptionHelper.validate(this.upstream, param1Subscription)) {
        this.upstream = param1Subscription;
        this.downstream.onSubscribe((Subscription)this);
        param1Subscription.request(Long.MAX_VALUE);
      } 
    }
    
    public T poll() throws Exception {
      return (T)this.queue.poll();
    }
    
    public void request(long param1Long) {
      if (!this.outputFused && SubscriptionHelper.validate(param1Long)) {
        BackpressureHelper.add(this.requested, param1Long);
        drain();
      } 
    }
    
    public int requestFusion(int param1Int) {
      if ((param1Int & 0x2) != 0) {
        this.outputFused = true;
        return 2;
      } 
      return 0;
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableOnBackpressureBuffer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */