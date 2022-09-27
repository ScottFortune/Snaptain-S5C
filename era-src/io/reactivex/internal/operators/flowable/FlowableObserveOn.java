package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.Scheduler;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.exceptions.MissingBackpressureException;
import io.reactivex.internal.fuseable.ConditionalSubscriber;
import io.reactivex.internal.fuseable.QueueSubscription;
import io.reactivex.internal.fuseable.SimpleQueue;
import io.reactivex.internal.queue.SpscArrayQueue;
import io.reactivex.internal.subscriptions.BasicIntQueueSubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicLong;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableObserveOn<T> extends AbstractFlowableWithUpstream<T, T> {
  final boolean delayError;
  
  final int prefetch;
  
  final Scheduler scheduler;
  
  public FlowableObserveOn(Flowable<T> paramFlowable, Scheduler paramScheduler, boolean paramBoolean, int paramInt) {
    super(paramFlowable);
    this.scheduler = paramScheduler;
    this.delayError = paramBoolean;
    this.prefetch = paramInt;
  }
  
  public void subscribeActual(Subscriber<? super T> paramSubscriber) {
    Scheduler.Worker worker = this.scheduler.createWorker();
    if (paramSubscriber instanceof ConditionalSubscriber) {
      this.source.subscribe(new ObserveOnConditionalSubscriber((ConditionalSubscriber)paramSubscriber, worker, this.delayError, this.prefetch));
    } else {
      this.source.subscribe(new ObserveOnSubscriber<T>(paramSubscriber, worker, this.delayError, this.prefetch));
    } 
  }
  
  static abstract class BaseObserveOnSubscriber<T> extends BasicIntQueueSubscription<T> implements FlowableSubscriber<T>, Runnable {
    private static final long serialVersionUID = -8241002408341274697L;
    
    volatile boolean cancelled;
    
    final boolean delayError;
    
    volatile boolean done;
    
    Throwable error;
    
    final int limit;
    
    boolean outputFused;
    
    final int prefetch;
    
    long produced;
    
    SimpleQueue<T> queue;
    
    final AtomicLong requested;
    
    int sourceMode;
    
    Subscription upstream;
    
    final Scheduler.Worker worker;
    
    BaseObserveOnSubscriber(Scheduler.Worker param1Worker, boolean param1Boolean, int param1Int) {
      this.worker = param1Worker;
      this.delayError = param1Boolean;
      this.prefetch = param1Int;
      this.requested = new AtomicLong();
      this.limit = param1Int - (param1Int >> 2);
    }
    
    public final void cancel() {
      if (this.cancelled)
        return; 
      this.cancelled = true;
      this.upstream.cancel();
      this.worker.dispose();
      if (!this.outputFused && getAndIncrement() == 0)
        this.queue.clear(); 
    }
    
    final boolean checkTerminated(boolean param1Boolean1, boolean param1Boolean2, Subscriber<?> param1Subscriber) {
      if (this.cancelled) {
        clear();
        return true;
      } 
      if (param1Boolean1)
        if (this.delayError) {
          if (param1Boolean2) {
            this.cancelled = true;
            Throwable throwable = this.error;
            if (throwable != null) {
              param1Subscriber.onError(throwable);
            } else {
              param1Subscriber.onComplete();
            } 
            this.worker.dispose();
            return true;
          } 
        } else {
          Throwable throwable = this.error;
          if (throwable != null) {
            this.cancelled = true;
            clear();
            param1Subscriber.onError(throwable);
            this.worker.dispose();
            return true;
          } 
          if (param1Boolean2) {
            this.cancelled = true;
            param1Subscriber.onComplete();
            this.worker.dispose();
            return true;
          } 
        }  
      return false;
    }
    
    public final void clear() {
      this.queue.clear();
    }
    
    public final boolean isEmpty() {
      return this.queue.isEmpty();
    }
    
    public final void onComplete() {
      if (!this.done) {
        this.done = true;
        trySchedule();
      } 
    }
    
    public final void onError(Throwable param1Throwable) {
      if (this.done) {
        RxJavaPlugins.onError(param1Throwable);
        return;
      } 
      this.error = param1Throwable;
      this.done = true;
      trySchedule();
    }
    
    public final void onNext(T param1T) {
      if (this.done)
        return; 
      if (this.sourceMode == 2) {
        trySchedule();
        return;
      } 
      if (!this.queue.offer(param1T)) {
        this.upstream.cancel();
        this.error = (Throwable)new MissingBackpressureException("Queue is full?!");
        this.done = true;
      } 
      trySchedule();
    }
    
    public final void request(long param1Long) {
      if (SubscriptionHelper.validate(param1Long)) {
        BackpressureHelper.add(this.requested, param1Long);
        trySchedule();
      } 
    }
    
    public final int requestFusion(int param1Int) {
      if ((param1Int & 0x2) != 0) {
        this.outputFused = true;
        return 2;
      } 
      return 0;
    }
    
    public final void run() {
      if (this.outputFused) {
        runBackfused();
      } else if (this.sourceMode == 1) {
        runSync();
      } else {
        runAsync();
      } 
    }
    
    abstract void runAsync();
    
    abstract void runBackfused();
    
    abstract void runSync();
    
    final void trySchedule() {
      if (getAndIncrement() != 0)
        return; 
      this.worker.schedule(this);
    }
  }
  
  static final class ObserveOnConditionalSubscriber<T> extends BaseObserveOnSubscriber<T> {
    private static final long serialVersionUID = 644624475404284533L;
    
    long consumed;
    
    final ConditionalSubscriber<? super T> downstream;
    
    ObserveOnConditionalSubscriber(ConditionalSubscriber<? super T> param1ConditionalSubscriber, Scheduler.Worker param1Worker, boolean param1Boolean, int param1Int) {
      super(param1Worker, param1Boolean, param1Int);
      this.downstream = param1ConditionalSubscriber;
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      if (SubscriptionHelper.validate(this.upstream, param1Subscription)) {
        this.upstream = param1Subscription;
        if (param1Subscription instanceof QueueSubscription) {
          QueueSubscription queueSubscription = (QueueSubscription)param1Subscription;
          int i = queueSubscription.requestFusion(7);
          if (i == 1) {
            this.sourceMode = 1;
            this.queue = (SimpleQueue<T>)queueSubscription;
            this.done = true;
            this.downstream.onSubscribe((Subscription)this);
            return;
          } 
          if (i == 2) {
            this.sourceMode = 2;
            this.queue = (SimpleQueue<T>)queueSubscription;
            this.downstream.onSubscribe((Subscription)this);
            param1Subscription.request(this.prefetch);
            return;
          } 
        } 
        this.queue = (SimpleQueue<T>)new SpscArrayQueue(this.prefetch);
        this.downstream.onSubscribe((Subscription)this);
        param1Subscription.request(this.prefetch);
      } 
    }
    
    public T poll() throws Exception {
      Object object = this.queue.poll();
      if (object != null && this.sourceMode != 1) {
        long l = this.consumed + 1L;
        if (l == this.limit) {
          this.consumed = 0L;
          this.upstream.request(l);
        } else {
          this.consumed = l;
        } 
      } 
      return (T)object;
    }
    
    void runAsync() {
      ConditionalSubscriber<? super T> conditionalSubscriber = this.downstream;
      SimpleQueue<T> simpleQueue = this.queue;
      long l1 = this.produced;
      long l2 = this.consumed;
      int i;
      for (i = 1;; i = j) {
        long l = this.requested.get();
        while (l1 != l) {
          boolean bool = this.done;
          try {
            boolean bool1;
            Object object = simpleQueue.poll();
            if (object == null) {
              bool1 = true;
            } else {
              bool1 = false;
            } 
            if (checkTerminated(bool, bool1, (Subscriber<?>)conditionalSubscriber))
              return; 
          } finally {
            Exception exception = null;
            Exceptions.throwIfFatal(exception);
            this.cancelled = true;
            this.upstream.cancel();
            simpleQueue.clear();
            conditionalSubscriber.onError(exception);
            this.worker.dispose();
          } 
        } 
        if (l1 == l && checkTerminated(this.done, simpleQueue.isEmpty(), (Subscriber<?>)conditionalSubscriber))
          return; 
        int j = get();
        if (i == j) {
          this.produced = l1;
          this.consumed = l2;
          j = addAndGet(-i);
          i = j;
          if (j == 0)
            return; 
          continue;
        } 
      } 
    }
    
    void runBackfused() {
      int j;
      int i = 1;
      do {
        if (this.cancelled)
          return; 
        boolean bool = this.done;
        this.downstream.onNext(null);
        if (bool) {
          this.cancelled = true;
          Throwable throwable = this.error;
          if (throwable != null) {
            this.downstream.onError(throwable);
          } else {
            this.downstream.onComplete();
          } 
          this.worker.dispose();
          return;
        } 
        j = addAndGet(-i);
        i = j;
      } while (j != 0);
    }
    
    void runSync() {
      ConditionalSubscriber<? super T> conditionalSubscriber = this.downstream;
      SimpleQueue<T> simpleQueue = this.queue;
      long l = this.produced;
      int i;
      for (i = 1;; i = j) {
        long l1 = this.requested.get();
        while (l != l1) {
          try {
          
          } finally {
            Exception exception = null;
            Exceptions.throwIfFatal(exception);
            this.cancelled = true;
            this.upstream.cancel();
            conditionalSubscriber.onError(exception);
            this.worker.dispose();
          } 
        } 
        if (this.cancelled)
          return; 
        if (simpleQueue.isEmpty()) {
          this.cancelled = true;
          conditionalSubscriber.onComplete();
          this.worker.dispose();
          return;
        } 
        int j = get();
        if (i == j) {
          this.produced = l;
          j = addAndGet(-i);
          i = j;
          if (j == 0)
            return; 
          continue;
        } 
      } 
    }
  }
  
  static final class ObserveOnSubscriber<T> extends BaseObserveOnSubscriber<T> implements FlowableSubscriber<T> {
    private static final long serialVersionUID = -4547113800637756442L;
    
    final Subscriber<? super T> downstream;
    
    ObserveOnSubscriber(Subscriber<? super T> param1Subscriber, Scheduler.Worker param1Worker, boolean param1Boolean, int param1Int) {
      super(param1Worker, param1Boolean, param1Int);
      this.downstream = param1Subscriber;
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      if (SubscriptionHelper.validate(this.upstream, param1Subscription)) {
        this.upstream = param1Subscription;
        if (param1Subscription instanceof QueueSubscription) {
          QueueSubscription queueSubscription = (QueueSubscription)param1Subscription;
          int i = queueSubscription.requestFusion(7);
          if (i == 1) {
            this.sourceMode = 1;
            this.queue = (SimpleQueue<T>)queueSubscription;
            this.done = true;
            this.downstream.onSubscribe((Subscription)this);
            return;
          } 
          if (i == 2) {
            this.sourceMode = 2;
            this.queue = (SimpleQueue<T>)queueSubscription;
            this.downstream.onSubscribe((Subscription)this);
            param1Subscription.request(this.prefetch);
            return;
          } 
        } 
        this.queue = (SimpleQueue<T>)new SpscArrayQueue(this.prefetch);
        this.downstream.onSubscribe((Subscription)this);
        param1Subscription.request(this.prefetch);
      } 
    }
    
    public T poll() throws Exception {
      Object object = this.queue.poll();
      if (object != null && this.sourceMode != 1) {
        long l = this.produced + 1L;
        if (l == this.limit) {
          this.produced = 0L;
          this.upstream.request(l);
        } else {
          this.produced = l;
        } 
      } 
      return (T)object;
    }
    
    void runAsync() {
      Subscriber<? super T> subscriber = this.downstream;
      SimpleQueue<T> simpleQueue = this.queue;
      long l = this.produced;
      int i;
      for (i = 1;; i = j) {
        long l1 = this.requested.get();
        while (l != l1) {
          boolean bool = this.done;
          try {
            Object object = simpleQueue.poll();
          } finally {
            Exception exception = null;
            Exceptions.throwIfFatal(exception);
            this.cancelled = true;
            this.upstream.cancel();
            simpleQueue.clear();
            subscriber.onError(exception);
            this.worker.dispose();
          } 
        } 
        if (l == l1 && checkTerminated(this.done, simpleQueue.isEmpty(), subscriber))
          return; 
        int j = get();
        if (i == j) {
          this.produced = l;
          j = addAndGet(-i);
          i = j;
          if (j == 0)
            return; 
          continue;
        } 
      } 
    }
    
    void runBackfused() {
      int j;
      int i = 1;
      do {
        if (this.cancelled)
          return; 
        boolean bool = this.done;
        this.downstream.onNext(null);
        if (bool) {
          this.cancelled = true;
          Throwable throwable = this.error;
          if (throwable != null) {
            this.downstream.onError(throwable);
          } else {
            this.downstream.onComplete();
          } 
          this.worker.dispose();
          return;
        } 
        j = addAndGet(-i);
        i = j;
      } while (j != 0);
    }
    
    void runSync() {
      Subscriber<? super T> subscriber = this.downstream;
      SimpleQueue<T> simpleQueue = this.queue;
      long l = this.produced;
      int i;
      for (i = 1;; i = j) {
        long l1 = this.requested.get();
        while (l != l1) {
          try {
          
          } finally {
            Exception exception = null;
            Exceptions.throwIfFatal(exception);
            this.cancelled = true;
            this.upstream.cancel();
            subscriber.onError(exception);
            this.worker.dispose();
          } 
        } 
        if (this.cancelled)
          return; 
        if (simpleQueue.isEmpty()) {
          this.cancelled = true;
          subscriber.onComplete();
          this.worker.dispose();
          return;
        } 
        int j = get();
        if (i == j) {
          this.produced = l;
          j = addAndGet(-i);
          i = j;
          if (j == 0)
            return; 
          continue;
        } 
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableObserveOn.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */