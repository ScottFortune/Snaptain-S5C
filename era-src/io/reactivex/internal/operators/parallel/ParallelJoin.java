package io.reactivex.internal.operators.parallel;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.exceptions.MissingBackpressureException;
import io.reactivex.internal.fuseable.SimplePlainQueue;
import io.reactivex.internal.queue.SpscArrayQueue;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.AtomicThrowable;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.parallel.ParallelFlowable;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class ParallelJoin<T> extends Flowable<T> {
  final boolean delayErrors;
  
  final int prefetch;
  
  final ParallelFlowable<? extends T> source;
  
  public ParallelJoin(ParallelFlowable<? extends T> paramParallelFlowable, int paramInt, boolean paramBoolean) {
    this.source = paramParallelFlowable;
    this.prefetch = paramInt;
    this.delayErrors = paramBoolean;
  }
  
  protected void subscribeActual(Subscriber<? super T> paramSubscriber) {
    JoinSubscription<T> joinSubscription;
    if (this.delayErrors) {
      JoinSubscriptionDelayError<T> joinSubscriptionDelayError = new JoinSubscriptionDelayError<T>(paramSubscriber, this.source.parallelism(), this.prefetch);
    } else {
      joinSubscription = new JoinSubscription<T>(paramSubscriber, this.source.parallelism(), this.prefetch);
    } 
    paramSubscriber.onSubscribe(joinSubscription);
    this.source.subscribe((Subscriber[])joinSubscription.subscribers);
  }
  
  static final class JoinInnerSubscriber<T> extends AtomicReference<Subscription> implements FlowableSubscriber<T> {
    private static final long serialVersionUID = 8410034718427740355L;
    
    final int limit;
    
    final ParallelJoin.JoinSubscriptionBase<T> parent;
    
    final int prefetch;
    
    long produced;
    
    volatile SimplePlainQueue<T> queue;
    
    JoinInnerSubscriber(ParallelJoin.JoinSubscriptionBase<T> param1JoinSubscriptionBase, int param1Int) {
      this.parent = param1JoinSubscriptionBase;
      this.prefetch = param1Int;
      this.limit = param1Int - (param1Int >> 2);
    }
    
    public boolean cancel() {
      return SubscriptionHelper.cancel(this);
    }
    
    SimplePlainQueue<T> getQueue() {
      SpscArrayQueue spscArrayQueue;
      SimplePlainQueue<T> simplePlainQueue1 = this.queue;
      SimplePlainQueue<T> simplePlainQueue2 = simplePlainQueue1;
      if (simplePlainQueue1 == null) {
        spscArrayQueue = new SpscArrayQueue(this.prefetch);
        this.queue = (SimplePlainQueue<T>)spscArrayQueue;
      } 
      return (SimplePlainQueue<T>)spscArrayQueue;
    }
    
    public void onComplete() {
      this.parent.onComplete();
    }
    
    public void onError(Throwable param1Throwable) {
      this.parent.onError(param1Throwable);
    }
    
    public void onNext(T param1T) {
      this.parent.onNext(this, param1T);
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      SubscriptionHelper.setOnce(this, param1Subscription, this.prefetch);
    }
    
    public void request(long param1Long) {
      param1Long = this.produced + param1Long;
      if (param1Long >= this.limit) {
        this.produced = 0L;
        get().request(param1Long);
      } else {
        this.produced = param1Long;
      } 
    }
    
    public void requestOne() {
      long l = this.produced + 1L;
      if (l == this.limit) {
        this.produced = 0L;
        get().request(l);
      } else {
        this.produced = l;
      } 
    }
  }
  
  static final class JoinSubscription<T> extends JoinSubscriptionBase<T> {
    private static final long serialVersionUID = 6312374661811000451L;
    
    JoinSubscription(Subscriber<? super T> param1Subscriber, int param1Int1, int param1Int2) {
      super(param1Subscriber, param1Int1, param1Int2);
    }
    
    void drain() {
      if (getAndIncrement() != 0)
        return; 
      drainLoop();
    }
    
    void drainLoop() {
      ParallelJoin.JoinInnerSubscriber<T>[] arrayOfJoinInnerSubscriber = this.subscribers;
      int i = arrayOfJoinInnerSubscriber.length;
      Subscriber<? super T> subscriber = this.downstream;
      int j;
      for (j = 1;; j = k) {
        long l3;
        long l1 = this.requested.get();
        long l2 = 0L;
        label75: while (true) {
          l3 = l2;
          if (l2 != l1) {
            boolean bool1;
            if (this.cancelled) {
              cleanup();
              return;
            } 
            Throwable throwable = (Throwable)this.errors.get();
            if (throwable != null) {
              cleanup();
              subscriber.onError(throwable);
              return;
            } 
            if (this.done.get() == 0) {
              bool1 = true;
            } else {
              bool1 = false;
            } 
            byte b = 0;
            boolean bool2 = true;
            while (b < arrayOfJoinInnerSubscriber.length) {
              ParallelJoin.JoinInnerSubscriber<T> joinInnerSubscriber = arrayOfJoinInnerSubscriber[b];
              SimplePlainQueue<T> simplePlainQueue = joinInnerSubscriber.queue;
              boolean bool = bool2;
              l3 = l2;
              if (simplePlainQueue != null) {
                Object object = simplePlainQueue.poll();
                bool = bool2;
                l3 = l2;
                if (object != null) {
                  subscriber.onNext(object);
                  joinInnerSubscriber.requestOne();
                  l3 = 1L + l2;
                  if (l3 == l1)
                    break label75; 
                  bool = false;
                } 
              } 
              b++;
              bool2 = bool;
              l2 = l3;
            } 
            if (bool1 && bool2) {
              subscriber.onComplete();
              return;
            } 
            if (bool2) {
              l3 = l2;
              break;
            } 
            continue;
          } 
          break;
        } 
        if (l3 == l1) {
          boolean bool;
          if (this.cancelled) {
            cleanup();
            return;
          } 
          Throwable throwable = (Throwable)this.errors.get();
          if (throwable != null) {
            cleanup();
            subscriber.onError(throwable);
            return;
          } 
          if (this.done.get() == 0) {
            bool = true;
          } else {
            bool = false;
          } 
          byte b = 0;
          while (true) {
            if (b < i) {
              SimplePlainQueue<T> simplePlainQueue = (arrayOfJoinInnerSubscriber[b]).queue;
              if (simplePlainQueue != null && !simplePlainQueue.isEmpty()) {
                b = 0;
                break;
              } 
              b++;
              continue;
            } 
            b = 1;
            break;
          } 
          if (bool && b != 0) {
            subscriber.onComplete();
            return;
          } 
        } 
        if (l3 != 0L && l1 != Long.MAX_VALUE)
          this.requested.addAndGet(-l3); 
        int m = get();
        int k = m;
        if (m == j) {
          j = addAndGet(-j);
          k = j;
          if (j == 0)
            return; 
        } 
      } 
    }
    
    public void onComplete() {
      this.done.decrementAndGet();
      drain();
    }
    
    public void onError(Throwable param1Throwable) {
      if (this.errors.compareAndSet(null, param1Throwable)) {
        cancelAll();
        drain();
      } else if (param1Throwable != this.errors.get()) {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
    
    public void onNext(ParallelJoin.JoinInnerSubscriber<T> param1JoinInnerSubscriber, T param1T) {
      MissingBackpressureException<T> missingBackpressureException;
      if (get() == 0 && compareAndSet(0, 1)) {
        if (this.requested.get() != 0L) {
          this.downstream.onNext(param1T);
          if (this.requested.get() != Long.MAX_VALUE)
            this.requested.decrementAndGet(); 
          param1JoinInnerSubscriber.request(1L);
        } else if (!param1JoinInnerSubscriber.getQueue().offer(param1T)) {
          cancelAll();
          missingBackpressureException = new MissingBackpressureException("Queue full?!");
          if (this.errors.compareAndSet(null, missingBackpressureException)) {
            this.downstream.onError((Throwable)missingBackpressureException);
          } else {
            RxJavaPlugins.onError((Throwable)missingBackpressureException);
          } 
          return;
        } 
        if (decrementAndGet() == 0)
          return; 
      } else {
        if (!missingBackpressureException.getQueue().offer(param1T)) {
          cancelAll();
          onError((Throwable)new MissingBackpressureException("Queue full?!"));
          return;
        } 
        if (getAndIncrement() != 0)
          return; 
      } 
      drainLoop();
    }
  }
  
  static abstract class JoinSubscriptionBase<T> extends AtomicInteger implements Subscription {
    private static final long serialVersionUID = 3100232009247827843L;
    
    volatile boolean cancelled;
    
    final AtomicInteger done = new AtomicInteger();
    
    final Subscriber<? super T> downstream;
    
    final AtomicThrowable errors = new AtomicThrowable();
    
    final AtomicLong requested = new AtomicLong();
    
    final ParallelJoin.JoinInnerSubscriber<T>[] subscribers;
    
    JoinSubscriptionBase(Subscriber<? super T> param1Subscriber, int param1Int1, int param1Int2) {
      this.downstream = param1Subscriber;
      ParallelJoin.JoinInnerSubscriber[] arrayOfJoinInnerSubscriber = new ParallelJoin.JoinInnerSubscriber[param1Int1];
      for (byte b = 0; b < param1Int1; b++)
        arrayOfJoinInnerSubscriber[b] = new ParallelJoin.JoinInnerSubscriber(this, param1Int2); 
      this.subscribers = (ParallelJoin.JoinInnerSubscriber<T>[])arrayOfJoinInnerSubscriber;
      this.done.lazySet(param1Int1);
    }
    
    public void cancel() {
      if (!this.cancelled) {
        this.cancelled = true;
        cancelAll();
        if (getAndIncrement() == 0)
          cleanup(); 
      } 
    }
    
    void cancelAll() {
      ParallelJoin.JoinInnerSubscriber<T>[] arrayOfJoinInnerSubscriber = this.subscribers;
      int i = arrayOfJoinInnerSubscriber.length;
      for (byte b = 0; b < i; b++)
        arrayOfJoinInnerSubscriber[b].cancel(); 
    }
    
    void cleanup() {
      ParallelJoin.JoinInnerSubscriber<T>[] arrayOfJoinInnerSubscriber = this.subscribers;
      int i = arrayOfJoinInnerSubscriber.length;
      for (byte b = 0; b < i; b++)
        (arrayOfJoinInnerSubscriber[b]).queue = null; 
    }
    
    abstract void drain();
    
    abstract void onComplete();
    
    abstract void onError(Throwable param1Throwable);
    
    abstract void onNext(ParallelJoin.JoinInnerSubscriber<T> param1JoinInnerSubscriber, T param1T);
    
    public void request(long param1Long) {
      if (SubscriptionHelper.validate(param1Long)) {
        BackpressureHelper.add(this.requested, param1Long);
        drain();
      } 
    }
  }
  
  static final class JoinSubscriptionDelayError<T> extends JoinSubscriptionBase<T> {
    private static final long serialVersionUID = -5737965195918321883L;
    
    JoinSubscriptionDelayError(Subscriber<? super T> param1Subscriber, int param1Int1, int param1Int2) {
      super(param1Subscriber, param1Int1, param1Int2);
    }
    
    void drain() {
      if (getAndIncrement() != 0)
        return; 
      drainLoop();
    }
    
    void drainLoop() {
      ParallelJoin.JoinInnerSubscriber<T>[] arrayOfJoinInnerSubscriber = this.subscribers;
      int i = arrayOfJoinInnerSubscriber.length;
      Subscriber<? super T> subscriber = this.downstream;
      int j;
      for (j = 1;; j = k) {
        long l3;
        long l1 = this.requested.get();
        long l2 = 0L;
        label74: while (true) {
          l3 = l2;
          if (l2 != l1) {
            boolean bool1;
            if (this.cancelled) {
              cleanup();
              return;
            } 
            if (this.done.get() == 0) {
              bool1 = true;
            } else {
              bool1 = false;
            } 
            byte b = 0;
            boolean bool2 = true;
            while (b < i) {
              ParallelJoin.JoinInnerSubscriber<T> joinInnerSubscriber = arrayOfJoinInnerSubscriber[b];
              SimplePlainQueue<T> simplePlainQueue = joinInnerSubscriber.queue;
              boolean bool = bool2;
              l3 = l2;
              if (simplePlainQueue != null) {
                Object object = simplePlainQueue.poll();
                bool = bool2;
                l3 = l2;
                if (object != null) {
                  subscriber.onNext(object);
                  joinInnerSubscriber.requestOne();
                  l2 = 1L + l2;
                  if (l2 == l1) {
                    l3 = l2;
                    break label74;
                  } 
                  bool = false;
                  l3 = l2;
                } 
              } 
              b++;
              bool2 = bool;
              l2 = l3;
            } 
            if (bool1 && bool2) {
              if ((Throwable)this.errors.get() != null) {
                subscriber.onError(this.errors.terminate());
              } else {
                subscriber.onComplete();
              } 
              return;
            } 
            if (bool2) {
              l3 = l2;
              break;
            } 
            continue;
          } 
          break;
        } 
        if (l3 == l1) {
          boolean bool;
          if (this.cancelled) {
            cleanup();
            return;
          } 
          if (this.done.get() == 0) {
            bool = true;
          } else {
            bool = false;
          } 
          byte b = 0;
          while (true) {
            if (b < i) {
              SimplePlainQueue<T> simplePlainQueue = (arrayOfJoinInnerSubscriber[b]).queue;
              if (simplePlainQueue != null && !simplePlainQueue.isEmpty()) {
                b = 0;
                break;
              } 
              b++;
              continue;
            } 
            b = 1;
            break;
          } 
          if (bool && b != 0) {
            if ((Throwable)this.errors.get() != null) {
              subscriber.onError(this.errors.terminate());
            } else {
              subscriber.onComplete();
            } 
            return;
          } 
        } 
        if (l3 != 0L && l1 != Long.MAX_VALUE)
          this.requested.addAndGet(-l3); 
        int m = get();
        int k = m;
        if (m == j) {
          j = addAndGet(-j);
          k = j;
          if (j == 0)
            return; 
        } 
      } 
    }
    
    void onComplete() {
      this.done.decrementAndGet();
      drain();
    }
    
    void onError(Throwable param1Throwable) {
      this.errors.addThrowable(param1Throwable);
      this.done.decrementAndGet();
      drain();
    }
    
    void onNext(ParallelJoin.JoinInnerSubscriber<T> param1JoinInnerSubscriber, T param1T) {
      if (get() == 0 && compareAndSet(0, 1)) {
        if (this.requested.get() != 0L) {
          this.downstream.onNext(param1T);
          if (this.requested.get() != Long.MAX_VALUE)
            this.requested.decrementAndGet(); 
          param1JoinInnerSubscriber.request(1L);
        } else if (!param1JoinInnerSubscriber.getQueue().offer(param1T)) {
          param1JoinInnerSubscriber.cancel();
          this.errors.addThrowable((Throwable)new MissingBackpressureException("Queue full?!"));
          this.done.decrementAndGet();
          drainLoop();
          return;
        } 
        if (decrementAndGet() == 0)
          return; 
      } else {
        if (!param1JoinInnerSubscriber.getQueue().offer(param1T) && param1JoinInnerSubscriber.cancel()) {
          this.errors.addThrowable((Throwable)new MissingBackpressureException("Queue full?!"));
          this.done.decrementAndGet();
        } 
        if (getAndIncrement() != 0)
          return; 
      } 
      drainLoop();
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/parallel/ParallelJoin.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */