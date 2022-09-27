package io.reactivex.internal.operators.parallel;

import io.reactivex.FlowableSubscriber;
import io.reactivex.Scheduler;
import io.reactivex.exceptions.MissingBackpressureException;
import io.reactivex.internal.fuseable.ConditionalSubscriber;
import io.reactivex.internal.queue.SpscArrayQueue;
import io.reactivex.internal.schedulers.SchedulerMultiWorkerSupport;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.parallel.ParallelFlowable;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class ParallelRunOn<T> extends ParallelFlowable<T> {
  final int prefetch;
  
  final Scheduler scheduler;
  
  final ParallelFlowable<? extends T> source;
  
  public ParallelRunOn(ParallelFlowable<? extends T> paramParallelFlowable, Scheduler paramScheduler, int paramInt) {
    this.source = paramParallelFlowable;
    this.scheduler = paramScheduler;
    this.prefetch = paramInt;
  }
  
  void createSubscriber(int paramInt, Subscriber<? super T>[] paramArrayOfSubscriber, Subscriber<T>[] paramArrayOfSubscriber1, Scheduler.Worker paramWorker) {
    Subscriber<? super T> subscriber = paramArrayOfSubscriber[paramInt];
    SpscArrayQueue<?> spscArrayQueue = new SpscArrayQueue(this.prefetch);
    if (subscriber instanceof ConditionalSubscriber) {
      paramArrayOfSubscriber1[paramInt] = (Subscriber<T>)new RunOnConditionalSubscriber((ConditionalSubscriber)subscriber, this.prefetch, spscArrayQueue, paramWorker);
    } else {
      paramArrayOfSubscriber1[paramInt] = (Subscriber<T>)new RunOnSubscriber<T>(subscriber, this.prefetch, (SpscArrayQueue)spscArrayQueue, paramWorker);
    } 
  }
  
  public int parallelism() {
    return this.source.parallelism();
  }
  
  public void subscribe(Subscriber<? super T>[] paramArrayOfSubscriber) {
    if (!validate((Subscriber[])paramArrayOfSubscriber))
      return; 
    int i = paramArrayOfSubscriber.length;
    Subscriber[] arrayOfSubscriber = new Subscriber[i];
    Scheduler scheduler = this.scheduler;
    if (scheduler instanceof SchedulerMultiWorkerSupport) {
      ((SchedulerMultiWorkerSupport)scheduler).createWorkers(i, new MultiWorkerCallback(paramArrayOfSubscriber, (Subscriber<T>[])arrayOfSubscriber));
    } else {
      for (byte b = 0; b < i; b++)
        createSubscriber(b, paramArrayOfSubscriber, (Subscriber<T>[])arrayOfSubscriber, this.scheduler.createWorker()); 
    } 
    this.source.subscribe(arrayOfSubscriber);
  }
  
  static abstract class BaseRunOnSubscriber<T> extends AtomicInteger implements FlowableSubscriber<T>, Subscription, Runnable {
    private static final long serialVersionUID = 9222303586456402150L;
    
    volatile boolean cancelled;
    
    int consumed;
    
    volatile boolean done;
    
    Throwable error;
    
    final int limit;
    
    final int prefetch;
    
    final SpscArrayQueue<T> queue;
    
    final AtomicLong requested = new AtomicLong();
    
    Subscription upstream;
    
    final Scheduler.Worker worker;
    
    BaseRunOnSubscriber(int param1Int, SpscArrayQueue<T> param1SpscArrayQueue, Scheduler.Worker param1Worker) {
      this.prefetch = param1Int;
      this.queue = param1SpscArrayQueue;
      this.limit = param1Int - (param1Int >> 2);
      this.worker = param1Worker;
    }
    
    public final void cancel() {
      if (!this.cancelled) {
        this.cancelled = true;
        this.upstream.cancel();
        this.worker.dispose();
        if (getAndIncrement() == 0)
          this.queue.clear(); 
      } 
    }
    
    public final void onComplete() {
      if (this.done)
        return; 
      this.done = true;
      schedule();
    }
    
    public final void onError(Throwable param1Throwable) {
      if (this.done) {
        RxJavaPlugins.onError(param1Throwable);
        return;
      } 
      this.error = param1Throwable;
      this.done = true;
      schedule();
    }
    
    public final void onNext(T param1T) {
      if (this.done)
        return; 
      if (!this.queue.offer(param1T)) {
        this.upstream.cancel();
        onError((Throwable)new MissingBackpressureException("Queue is full?!"));
        return;
      } 
      schedule();
    }
    
    public final void request(long param1Long) {
      if (SubscriptionHelper.validate(param1Long)) {
        BackpressureHelper.add(this.requested, param1Long);
        schedule();
      } 
    }
    
    final void schedule() {
      if (getAndIncrement() == 0)
        this.worker.schedule(this); 
    }
  }
  
  final class MultiWorkerCallback implements SchedulerMultiWorkerSupport.WorkerCallback {
    final Subscriber<T>[] parents;
    
    final Subscriber<? super T>[] subscribers;
    
    MultiWorkerCallback(Subscriber<? super T>[] param1ArrayOfSubscriber, Subscriber<T>[] param1ArrayOfSubscriber1) {
      this.subscribers = param1ArrayOfSubscriber;
      this.parents = param1ArrayOfSubscriber1;
    }
    
    public void onWorker(int param1Int, Scheduler.Worker param1Worker) {
      ParallelRunOn.this.createSubscriber(param1Int, this.subscribers, this.parents, param1Worker);
    }
  }
  
  static final class RunOnConditionalSubscriber<T> extends BaseRunOnSubscriber<T> {
    private static final long serialVersionUID = 1075119423897941642L;
    
    final ConditionalSubscriber<? super T> downstream;
    
    RunOnConditionalSubscriber(ConditionalSubscriber<? super T> param1ConditionalSubscriber, int param1Int, SpscArrayQueue<T> param1SpscArrayQueue, Scheduler.Worker param1Worker) {
      super(param1Int, param1SpscArrayQueue, param1Worker);
      this.downstream = param1ConditionalSubscriber;
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      if (SubscriptionHelper.validate(this.upstream, param1Subscription)) {
        this.upstream = param1Subscription;
        this.downstream.onSubscribe(this);
        param1Subscription.request(this.prefetch);
      } 
    }
    
    public void run() {
      int i = this.consumed;
      SpscArrayQueue<T> spscArrayQueue = this.queue;
      ConditionalSubscriber<? super T> conditionalSubscriber = this.downstream;
      int j = this.limit;
      int k = 1;
      while (true) {
        long l1 = this.requested.get();
        long l2 = 0L;
        int m = k;
        while (l2 != l1) {
          if (this.cancelled) {
            spscArrayQueue.clear();
            return;
          } 
          boolean bool = this.done;
          if (bool) {
            Throwable throwable = this.error;
            if (throwable != null) {
              spscArrayQueue.clear();
              conditionalSubscriber.onError(throwable);
              this.worker.dispose();
              return;
            } 
          } 
          Object object = spscArrayQueue.poll();
          if (object == null) {
            k = 1;
          } else {
            k = 0;
          } 
          if (bool && k) {
            conditionalSubscriber.onComplete();
            this.worker.dispose();
            return;
          } 
          if (k)
            break; 
          long l = l2;
          if (conditionalSubscriber.tryOnNext(object))
            l = l2 + 1L; 
          k = i + 1;
          if (k == j) {
            this.upstream.request(k);
            k = 0;
          } 
          i = k;
          l2 = l;
        } 
        if (l2 == l1) {
          if (this.cancelled) {
            spscArrayQueue.clear();
            return;
          } 
          if (this.done) {
            Throwable throwable = this.error;
            if (throwable != null) {
              spscArrayQueue.clear();
              conditionalSubscriber.onError(throwable);
              this.worker.dispose();
              return;
            } 
            if (spscArrayQueue.isEmpty()) {
              conditionalSubscriber.onComplete();
              this.worker.dispose();
              return;
            } 
          } 
        } 
        if (l2 != 0L && l1 != Long.MAX_VALUE)
          this.requested.addAndGet(-l2); 
        int n = get();
        k = n;
        if (n == m) {
          this.consumed = i;
          m = addAndGet(-m);
          k = m;
          if (m == 0)
            break; 
        } 
      } 
    }
  }
  
  static final class RunOnSubscriber<T> extends BaseRunOnSubscriber<T> {
    private static final long serialVersionUID = 1075119423897941642L;
    
    final Subscriber<? super T> downstream;
    
    RunOnSubscriber(Subscriber<? super T> param1Subscriber, int param1Int, SpscArrayQueue<T> param1SpscArrayQueue, Scheduler.Worker param1Worker) {
      super(param1Int, param1SpscArrayQueue, param1Worker);
      this.downstream = param1Subscriber;
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      if (SubscriptionHelper.validate(this.upstream, param1Subscription)) {
        this.upstream = param1Subscription;
        this.downstream.onSubscribe(this);
        param1Subscription.request(this.prefetch);
      } 
    }
    
    public void run() {
      int i = this.consumed;
      SpscArrayQueue<T> spscArrayQueue = this.queue;
      Subscriber<? super T> subscriber = this.downstream;
      int j = this.limit;
      int k = 1;
      while (true) {
        long l1 = this.requested.get();
        long l2 = 0L;
        int m = k;
        while (l2 != l1) {
          if (this.cancelled) {
            spscArrayQueue.clear();
            return;
          } 
          boolean bool = this.done;
          if (bool) {
            Throwable throwable = this.error;
            if (throwable != null) {
              spscArrayQueue.clear();
              subscriber.onError(throwable);
              this.worker.dispose();
              return;
            } 
          } 
          Object object = spscArrayQueue.poll();
          if (object == null) {
            k = 1;
          } else {
            k = 0;
          } 
          if (bool && k) {
            subscriber.onComplete();
            this.worker.dispose();
            return;
          } 
          if (k)
            break; 
          subscriber.onNext(object);
          l2++;
          k = i + 1;
          if (k == j) {
            this.upstream.request(k);
            k = 0;
          } 
          i = k;
        } 
        if (l2 == l1) {
          if (this.cancelled) {
            spscArrayQueue.clear();
            return;
          } 
          if (this.done) {
            Throwable throwable = this.error;
            if (throwable != null) {
              spscArrayQueue.clear();
              subscriber.onError(throwable);
              this.worker.dispose();
              return;
            } 
            if (spscArrayQueue.isEmpty()) {
              subscriber.onComplete();
              this.worker.dispose();
              return;
            } 
          } 
        } 
        if (l2 != 0L && l1 != Long.MAX_VALUE)
          this.requested.addAndGet(-l2); 
        int n = get();
        k = n;
        if (n == m) {
          this.consumed = i;
          m = addAndGet(-m);
          k = m;
          if (m == 0)
            break; 
        } 
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/parallel/ParallelRunOn.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */