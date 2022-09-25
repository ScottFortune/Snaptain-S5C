package io.reactivex.processors;

import io.reactivex.annotations.CheckReturnValue;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.queue.SpscLinkedArrayQueue;
import io.reactivex.internal.subscriptions.BasicIntQueueSubscription;
import io.reactivex.internal.subscriptions.EmptySubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class UnicastProcessor<T> extends FlowableProcessor<T> {
  volatile boolean cancelled;
  
  final boolean delayError;
  
  volatile boolean done;
  
  final AtomicReference<Subscriber<? super T>> downstream;
  
  boolean enableOperatorFusion;
  
  Throwable error;
  
  final AtomicReference<Runnable> onTerminate;
  
  final AtomicBoolean once;
  
  final SpscLinkedArrayQueue<T> queue;
  
  final AtomicLong requested;
  
  final BasicIntQueueSubscription<T> wip;
  
  UnicastProcessor(int paramInt) {
    this(paramInt, null, true);
  }
  
  UnicastProcessor(int paramInt, Runnable paramRunnable) {
    this(paramInt, paramRunnable, true);
  }
  
  UnicastProcessor(int paramInt, Runnable paramRunnable, boolean paramBoolean) {
    this.queue = new SpscLinkedArrayQueue(ObjectHelper.verifyPositive(paramInt, "capacityHint"));
    this.onTerminate = new AtomicReference<Runnable>(paramRunnable);
    this.delayError = paramBoolean;
    this.downstream = new AtomicReference<Subscriber<? super T>>();
    this.once = new AtomicBoolean();
    this.wip = new UnicastQueueSubscription();
    this.requested = new AtomicLong();
  }
  
  @CheckReturnValue
  public static <T> UnicastProcessor<T> create() {
    return new UnicastProcessor<T>(bufferSize());
  }
  
  @CheckReturnValue
  public static <T> UnicastProcessor<T> create(int paramInt) {
    return new UnicastProcessor<T>(paramInt);
  }
  
  @CheckReturnValue
  public static <T> UnicastProcessor<T> create(int paramInt, Runnable paramRunnable) {
    ObjectHelper.requireNonNull(paramRunnable, "onTerminate");
    return new UnicastProcessor<T>(paramInt, paramRunnable);
  }
  
  @CheckReturnValue
  public static <T> UnicastProcessor<T> create(int paramInt, Runnable paramRunnable, boolean paramBoolean) {
    ObjectHelper.requireNonNull(paramRunnable, "onTerminate");
    return new UnicastProcessor<T>(paramInt, paramRunnable, paramBoolean);
  }
  
  @CheckReturnValue
  public static <T> UnicastProcessor<T> create(boolean paramBoolean) {
    return new UnicastProcessor<T>(bufferSize(), null, paramBoolean);
  }
  
  boolean checkTerminated(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, Subscriber<? super T> paramSubscriber, SpscLinkedArrayQueue<T> paramSpscLinkedArrayQueue) {
    if (this.cancelled) {
      paramSpscLinkedArrayQueue.clear();
      this.downstream.lazySet(null);
      return true;
    } 
    if (paramBoolean2) {
      if (paramBoolean1 && this.error != null) {
        paramSpscLinkedArrayQueue.clear();
        this.downstream.lazySet(null);
        paramSubscriber.onError(this.error);
        return true;
      } 
      if (paramBoolean3) {
        Throwable throwable = this.error;
        this.downstream.lazySet(null);
        if (throwable != null) {
          paramSubscriber.onError(throwable);
        } else {
          paramSubscriber.onComplete();
        } 
        return true;
      } 
    } 
    return false;
  }
  
  void doTerminate() {
    Runnable runnable = this.onTerminate.getAndSet(null);
    if (runnable != null)
      runnable.run(); 
  }
  
  void drain() {
    if (this.wip.getAndIncrement() != 0)
      return; 
    int i = 1;
    for (Subscriber<? super T> subscriber = this.downstream.get();; subscriber = this.downstream.get()) {
      if (subscriber != null) {
        if (this.enableOperatorFusion) {
          drainFused(subscriber);
        } else {
          drainRegular(subscriber);
        } 
        return;
      } 
      i = this.wip.addAndGet(-i);
      if (i == 0)
        return; 
    } 
  }
  
  void drainFused(Subscriber<? super T> paramSubscriber) {
    int j;
    SpscLinkedArrayQueue<T> spscLinkedArrayQueue = this.queue;
    boolean bool = this.delayError;
    int i = 1;
    do {
      if (this.cancelled) {
        this.downstream.lazySet(null);
        return;
      } 
      boolean bool1 = this.done;
      if ((bool ^ true) != 0 && bool1 && this.error != null) {
        spscLinkedArrayQueue.clear();
        this.downstream.lazySet(null);
        paramSubscriber.onError(this.error);
        return;
      } 
      paramSubscriber.onNext(null);
      if (bool1) {
        this.downstream.lazySet(null);
        Throwable throwable = this.error;
        if (throwable != null) {
          paramSubscriber.onError(throwable);
        } else {
          paramSubscriber.onComplete();
        } 
        return;
      } 
      j = this.wip.addAndGet(-i);
      i = j;
    } while (j != 0);
  }
  
  void drainRegular(Subscriber<? super T> paramSubscriber) {
    int k;
    SpscLinkedArrayQueue<T> spscLinkedArrayQueue = this.queue;
    int i = this.delayError ^ true;
    int j = 1;
    do {
      long l1 = this.requested.get();
      long l2;
      for (l2 = 0L; l1 != l2; l2 = 1L + l2) {
        boolean bool1;
        boolean bool = this.done;
        Object object = spscLinkedArrayQueue.poll();
        if (object == null) {
          bool1 = true;
        } else {
          bool1 = false;
        } 
        if (checkTerminated(i, bool, bool1, paramSubscriber, spscLinkedArrayQueue))
          return; 
        if (bool1)
          break; 
        paramSubscriber.onNext(object);
      } 
      if (l1 == l2 && checkTerminated(i, this.done, spscLinkedArrayQueue.isEmpty(), paramSubscriber, spscLinkedArrayQueue))
        return; 
      if (l2 != 0L && l1 != Long.MAX_VALUE)
        this.requested.addAndGet(-l2); 
      k = this.wip.addAndGet(-j);
      j = k;
    } while (k != 0);
  }
  
  public Throwable getThrowable() {
    return this.done ? this.error : null;
  }
  
  public boolean hasComplete() {
    boolean bool;
    if (this.done && this.error == null) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean hasSubscribers() {
    boolean bool;
    if (this.downstream.get() != null) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean hasThrowable() {
    boolean bool;
    if (this.done && this.error != null) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void onComplete() {
    if (!this.done && !this.cancelled) {
      this.done = true;
      doTerminate();
      drain();
    } 
  }
  
  public void onError(Throwable paramThrowable) {
    ObjectHelper.requireNonNull(paramThrowable, "onError called with null. Null values are generally not allowed in 2.x operators and sources.");
    if (this.done || this.cancelled) {
      RxJavaPlugins.onError(paramThrowable);
      return;
    } 
    this.error = paramThrowable;
    this.done = true;
    doTerminate();
    drain();
  }
  
  public void onNext(T paramT) {
    ObjectHelper.requireNonNull(paramT, "onNext called with null. Null values are generally not allowed in 2.x operators and sources.");
    if (!this.done && !this.cancelled) {
      this.queue.offer(paramT);
      drain();
    } 
  }
  
  public void onSubscribe(Subscription paramSubscription) {
    if (this.done || this.cancelled) {
      paramSubscription.cancel();
      return;
    } 
    paramSubscription.request(Long.MAX_VALUE);
  }
  
  protected void subscribeActual(Subscriber<? super T> paramSubscriber) {
    if (!this.once.get() && this.once.compareAndSet(false, true)) {
      paramSubscriber.onSubscribe((Subscription)this.wip);
      this.downstream.set(paramSubscriber);
      if (this.cancelled) {
        this.downstream.lazySet(null);
      } else {
        drain();
      } 
    } else {
      EmptySubscription.error(new IllegalStateException("This processor allows only a single Subscriber"), paramSubscriber);
    } 
  }
  
  final class UnicastQueueSubscription extends BasicIntQueueSubscription<T> {
    private static final long serialVersionUID = -4896760517184205454L;
    
    public void cancel() {
      if (UnicastProcessor.this.cancelled)
        return; 
      UnicastProcessor unicastProcessor = UnicastProcessor.this;
      unicastProcessor.cancelled = true;
      unicastProcessor.doTerminate();
      UnicastProcessor.this.downstream.lazySet(null);
      if (UnicastProcessor.this.wip.getAndIncrement() == 0) {
        UnicastProcessor.this.downstream.lazySet(null);
        if (!UnicastProcessor.this.enableOperatorFusion)
          UnicastProcessor.this.queue.clear(); 
      } 
    }
    
    public void clear() {
      UnicastProcessor.this.queue.clear();
    }
    
    public boolean isEmpty() {
      return UnicastProcessor.this.queue.isEmpty();
    }
    
    public T poll() {
      return (T)UnicastProcessor.this.queue.poll();
    }
    
    public void request(long param1Long) {
      if (SubscriptionHelper.validate(param1Long)) {
        BackpressureHelper.add(UnicastProcessor.this.requested, param1Long);
        UnicastProcessor.this.drain();
      } 
    }
    
    public int requestFusion(int param1Int) {
      if ((param1Int & 0x2) != 0) {
        UnicastProcessor.this.enableOperatorFusion = true;
        return 2;
      } 
      return 0;
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/processors/UnicastProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */