package io.reactivex.internal.operators.parallel;

import io.reactivex.FlowableSubscriber;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.exceptions.MissingBackpressureException;
import io.reactivex.internal.fuseable.QueueSubscription;
import io.reactivex.internal.fuseable.SimpleQueue;
import io.reactivex.internal.queue.SpscArrayQueue;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.parallel.ParallelFlowable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLongArray;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class ParallelFromPublisher<T> extends ParallelFlowable<T> {
  final int parallelism;
  
  final int prefetch;
  
  final Publisher<? extends T> source;
  
  public ParallelFromPublisher(Publisher<? extends T> paramPublisher, int paramInt1, int paramInt2) {
    this.source = paramPublisher;
    this.parallelism = paramInt1;
    this.prefetch = paramInt2;
  }
  
  public int parallelism() {
    return this.parallelism;
  }
  
  public void subscribe(Subscriber<? super T>[] paramArrayOfSubscriber) {
    if (!validate((Subscriber[])paramArrayOfSubscriber))
      return; 
    this.source.subscribe((Subscriber)new ParallelDispatcher<T>(paramArrayOfSubscriber, this.prefetch));
  }
  
  static final class ParallelDispatcher<T> extends AtomicInteger implements FlowableSubscriber<T> {
    private static final long serialVersionUID = -4470634016609963609L;
    
    volatile boolean cancelled;
    
    volatile boolean done;
    
    final long[] emissions;
    
    Throwable error;
    
    int index;
    
    final int limit;
    
    final int prefetch;
    
    int produced;
    
    SimpleQueue<T> queue;
    
    final AtomicLongArray requests;
    
    int sourceMode;
    
    final AtomicInteger subscriberCount = new AtomicInteger();
    
    final Subscriber<? super T>[] subscribers;
    
    Subscription upstream;
    
    ParallelDispatcher(Subscriber<? super T>[] param1ArrayOfSubscriber, int param1Int) {
      this.subscribers = param1ArrayOfSubscriber;
      this.prefetch = param1Int;
      this.limit = param1Int - (param1Int >> 2);
      param1Int = param1ArrayOfSubscriber.length;
      int i = param1Int + param1Int;
      this.requests = new AtomicLongArray(i + 1);
      this.requests.lazySet(i, param1Int);
      this.emissions = new long[param1Int];
    }
    
    void cancel(int param1Int) {
      if (this.requests.decrementAndGet(param1Int) == 0L) {
        this.cancelled = true;
        this.upstream.cancel();
        if (getAndIncrement() == 0)
          this.queue.clear(); 
      } 
    }
    
    void drain() {
      if (getAndIncrement() != 0)
        return; 
      if (this.sourceMode == 1) {
        drainSync();
      } else {
        drainAsync();
      } 
    }
    
    void drainAsync() {
      SimpleQueue<T> simpleQueue = this.queue;
      Subscriber<? super T>[] arrayOfSubscriber = this.subscribers;
      AtomicLongArray atomicLongArray = this.requests;
      long[] arrayOfLong = this.emissions;
      int i = arrayOfLong.length;
      int j = this.index;
      int k = this.produced;
      int m = 1;
      while (true) {
        int i2;
        int i3;
        boolean bool1 = false;
        boolean bool2 = false;
        boolean bool3 = false;
        int n = 0;
        int i1 = k;
        do {
          if (this.cancelled) {
            simpleQueue.clear();
            return;
          } 
          boolean bool4 = this.done;
          if (bool4) {
            Throwable throwable = this.error;
            if (throwable != null) {
              simpleQueue.clear();
              m = arrayOfSubscriber.length;
              for (k = bool3; k < m; k++)
                arrayOfSubscriber[k].onError(throwable); 
              return;
            } 
          } 
          boolean bool5 = simpleQueue.isEmpty();
          if (bool4 && bool5) {
            m = arrayOfSubscriber.length;
            for (k = bool1; k < m; k++)
              arrayOfSubscriber[k].onComplete(); 
            return;
          } 
          if (bool5) {
            k = j;
            i2 = i1;
            break;
          } 
          long l1 = atomicLongArray.get(j);
          long l2 = arrayOfLong[j];
          if (l1 != l2 && atomicLongArray.get(i + j) == 0L) {
            try {
              Object object = simpleQueue.poll();
              if (object == null) {
                int i4 = i1;
                k = j;
                break;
              } 
              arrayOfSubscriber[j].onNext(object);
              arrayOfLong[j] = l2 + 1L;
            } finally {
              atomicLongArray = null;
              Exceptions.throwIfFatal((Throwable)atomicLongArray);
              this.upstream.cancel();
              m = arrayOfSubscriber.length;
              for (k = bool2; k < m; k++)
                arrayOfSubscriber[k].onError((Throwable)atomicLongArray); 
            } 
          } else {
            i3 = n + 1;
            i2 = i1;
          } 
          k = ++j;
          if (j == i)
            k = 0; 
          j = k;
          n = i3;
          i1 = i2;
        } while (i3 != i);
        j = get();
        if (j == m) {
          this.index = k;
          this.produced = i2;
          i1 = addAndGet(-m);
          j = k;
          k = i2;
          m = i1;
          if (i1 == 0)
            return; 
          continue;
        } 
        m = j;
        j = k;
        k = i2;
      } 
    }
    
    void drainSync() {
      SimpleQueue<T> simpleQueue = this.queue;
      Subscriber<? super T>[] arrayOfSubscriber = this.subscribers;
      AtomicLongArray atomicLongArray = this.requests;
      long[] arrayOfLong = this.emissions;
      int i = arrayOfLong.length;
      int j = this.index;
      int k;
      for (k = 1;; k = i1) {
        int i1;
        boolean bool1 = false;
        boolean bool2 = false;
        boolean bool3 = false;
        int m = 0;
        int n = j;
        while (true) {
          if (this.cancelled) {
            simpleQueue.clear();
            return;
          } 
          if (simpleQueue.isEmpty()) {
            k = arrayOfSubscriber.length;
            for (j = bool3; j < k; j++)
              arrayOfSubscriber[j].onComplete(); 
            return;
          } 
          long l1 = atomicLongArray.get(n);
          long l2 = arrayOfLong[n];
          if (l1 != l2 && atomicLongArray.get(i + n) == 0L) {
            try {
            
            } finally {
              arrayOfLong = null;
              Exceptions.throwIfFatal((Throwable)arrayOfLong);
              this.upstream.cancel();
              k = arrayOfSubscriber.length;
              for (j = bool2; j < k; j++)
                arrayOfSubscriber[j].onError((Throwable)arrayOfLong); 
            } 
          } else {
            i1 = m + 1;
          } 
          j = ++n;
          if (n == i)
            j = 0; 
          n = j;
          m = i1;
          if (i1 == i) {
            i1 = get();
            if (i1 == k) {
              this.index = j;
              i1 = addAndGet(-k);
              k = i1;
              if (i1 == 0)
                return; 
              continue;
            } 
            break;
          } 
        } 
      } 
    }
    
    public void onComplete() {
      this.done = true;
      drain();
    }
    
    public void onError(Throwable param1Throwable) {
      this.error = param1Throwable;
      this.done = true;
      drain();
    }
    
    public void onNext(T param1T) {
      if (this.sourceMode == 0 && !this.queue.offer(param1T)) {
        this.upstream.cancel();
        onError((Throwable)new MissingBackpressureException("Queue is full?"));
        return;
      } 
      drain();
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      if (SubscriptionHelper.validate(this.upstream, param1Subscription)) {
        this.upstream = param1Subscription;
        if (param1Subscription instanceof QueueSubscription) {
          QueueSubscription queueSubscription = (QueueSubscription)param1Subscription;
          int i = queueSubscription.requestFusion(7);
          if (i == 1) {
            this.sourceMode = i;
            this.queue = (SimpleQueue<T>)queueSubscription;
            this.done = true;
            setupSubscribers();
            drain();
            return;
          } 
          if (i == 2) {
            this.sourceMode = i;
            this.queue = (SimpleQueue<T>)queueSubscription;
            setupSubscribers();
            param1Subscription.request(this.prefetch);
            return;
          } 
        } 
        this.queue = (SimpleQueue<T>)new SpscArrayQueue(this.prefetch);
        setupSubscribers();
        param1Subscription.request(this.prefetch);
      } 
    }
    
    void setupSubscribers() {
      Subscriber<? super T>[] arrayOfSubscriber = this.subscribers;
      int i = arrayOfSubscriber.length;
      for (int j = 0; j < i; j = k) {
        if (this.cancelled)
          return; 
        AtomicInteger atomicInteger = this.subscriberCount;
        int k = j + 1;
        atomicInteger.lazySet(k);
        arrayOfSubscriber[j].onSubscribe(new RailSubscription(j, i));
      } 
    }
    
    final class RailSubscription implements Subscription {
      final int j;
      
      final int m;
      
      RailSubscription(int param2Int1, int param2Int2) {
        this.j = param2Int1;
        this.m = param2Int2;
      }
      
      public void cancel() {
        AtomicLongArray atomicLongArray = ParallelFromPublisher.ParallelDispatcher.this.requests;
        int i = this.m;
        if (atomicLongArray.compareAndSet(this.j + i, 0L, 1L)) {
          ParallelFromPublisher.ParallelDispatcher parallelDispatcher = ParallelFromPublisher.ParallelDispatcher.this;
          i = this.m;
          parallelDispatcher.cancel(i + i);
        } 
      }
      
      public void request(long param2Long) {
        if (SubscriptionHelper.validate(param2Long)) {
          AtomicLongArray atomicLongArray = ParallelFromPublisher.ParallelDispatcher.this.requests;
          while (true) {
            long l1 = atomicLongArray.get(this.j);
            if (l1 == Long.MAX_VALUE)
              return; 
            long l2 = BackpressureHelper.addCap(l1, param2Long);
            if (atomicLongArray.compareAndSet(this.j, l1, l2)) {
              if (ParallelFromPublisher.ParallelDispatcher.this.subscriberCount.get() == this.m)
                ParallelFromPublisher.ParallelDispatcher.this.drain(); 
              break;
            } 
          } 
        } 
      }
    }
  }
  
  final class RailSubscription implements Subscription {
    final int j;
    
    final int m;
    
    RailSubscription(int param1Int1, int param1Int2) {
      this.j = param1Int1;
      this.m = param1Int2;
    }
    
    public void cancel() {
      AtomicLongArray atomicLongArray = this.this$0.requests;
      int i = this.m;
      if (atomicLongArray.compareAndSet(this.j + i, 0L, 1L)) {
        ParallelFromPublisher.ParallelDispatcher parallelDispatcher = this.this$0;
        i = this.m;
        parallelDispatcher.cancel(i + i);
      } 
    }
    
    public void request(long param1Long) {
      if (SubscriptionHelper.validate(param1Long)) {
        AtomicLongArray atomicLongArray = this.this$0.requests;
        while (true) {
          long l1 = atomicLongArray.get(this.j);
          if (l1 == Long.MAX_VALUE)
            return; 
          long l2 = BackpressureHelper.addCap(l1, param1Long);
          if (atomicLongArray.compareAndSet(this.j, l1, l2)) {
            if (this.this$0.subscriberCount.get() == this.m)
              this.this$0.drain(); 
            break;
          } 
        } 
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/parallel/ParallelFromPublisher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */