package io.reactivex.processors;

import io.reactivex.annotations.BackpressureKind;
import io.reactivex.annotations.BackpressureSupport;
import io.reactivex.annotations.CheckReturnValue;
import io.reactivex.annotations.SchedulerSupport;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.exceptions.MissingBackpressureException;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.fuseable.QueueSubscription;
import io.reactivex.internal.fuseable.SimpleQueue;
import io.reactivex.internal.queue.SpscArrayQueue;
import io.reactivex.internal.queue.SpscLinkedArrayQueue;
import io.reactivex.internal.subscriptions.EmptySubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

@BackpressureSupport(BackpressureKind.FULL)
@SchedulerSupport("none")
public final class MulticastProcessor<T> extends FlowableProcessor<T> {
  static final MulticastSubscription[] EMPTY = new MulticastSubscription[0];
  
  static final MulticastSubscription[] TERMINATED = new MulticastSubscription[0];
  
  final int bufferSize;
  
  int consumed;
  
  volatile boolean done;
  
  volatile Throwable error;
  
  int fusionMode;
  
  final int limit;
  
  final AtomicBoolean once;
  
  volatile SimpleQueue<T> queue;
  
  final boolean refcount;
  
  final AtomicReference<MulticastSubscription<T>[]> subscribers;
  
  final AtomicReference<Subscription> upstream;
  
  final AtomicInteger wip;
  
  MulticastProcessor(int paramInt, boolean paramBoolean) {
    ObjectHelper.verifyPositive(paramInt, "bufferSize");
    this.bufferSize = paramInt;
    this.limit = paramInt - (paramInt >> 2);
    this.wip = new AtomicInteger();
    this.subscribers = new AtomicReference(EMPTY);
    this.upstream = new AtomicReference<Subscription>();
    this.refcount = paramBoolean;
    this.once = new AtomicBoolean();
  }
  
  @CheckReturnValue
  public static <T> MulticastProcessor<T> create() {
    return new MulticastProcessor<T>(bufferSize(), false);
  }
  
  @CheckReturnValue
  public static <T> MulticastProcessor<T> create(int paramInt) {
    return new MulticastProcessor<T>(paramInt, false);
  }
  
  @CheckReturnValue
  public static <T> MulticastProcessor<T> create(int paramInt, boolean paramBoolean) {
    return new MulticastProcessor<T>(paramInt, paramBoolean);
  }
  
  @CheckReturnValue
  public static <T> MulticastProcessor<T> create(boolean paramBoolean) {
    return new MulticastProcessor<T>(bufferSize(), paramBoolean);
  }
  
  boolean add(MulticastSubscription<T> paramMulticastSubscription) {
    while (true) {
      MulticastSubscription[] arrayOfMulticastSubscription1 = (MulticastSubscription[])this.subscribers.get();
      if (arrayOfMulticastSubscription1 == TERMINATED)
        return false; 
      int i = arrayOfMulticastSubscription1.length;
      MulticastSubscription[] arrayOfMulticastSubscription2 = new MulticastSubscription[i + 1];
      System.arraycopy(arrayOfMulticastSubscription1, 0, arrayOfMulticastSubscription2, 0, i);
      arrayOfMulticastSubscription2[i] = paramMulticastSubscription;
      if (this.subscribers.compareAndSet(arrayOfMulticastSubscription1, arrayOfMulticastSubscription2))
        return true; 
    } 
  }
  
  void drain() {
    if (this.wip.getAndIncrement() != 0)
      return; 
    AtomicReference<MulticastSubscription<T>[]> atomicReference = this.subscribers;
    int i = this.consumed;
    int j = this.limit;
    int k = this.fusionMode;
    int m = 1;
    label92: while (true) {
      SimpleQueue<T> simpleQueue = this.queue;
      int n = i;
      if (simpleQueue != null) {
        MulticastSubscription[] arrayOfMulticastSubscription = (MulticastSubscription[])atomicReference.get();
        n = i;
        if (arrayOfMulticastSubscription.length != 0) {
          MulticastSubscription[] arrayOfMulticastSubscription1;
          int i2 = arrayOfMulticastSubscription.length;
          long l = -1L;
          n = 0;
          while (n < i2) {
            MulticastSubscription multicastSubscription = arrayOfMulticastSubscription[n];
            long l1 = multicastSubscription.get();
            long l2 = l;
            if (l1 >= 0L)
              if (l == -1L) {
                l2 = l1 - multicastSubscription.emitted;
              } else {
                l2 = Math.min(l, l1 - multicastSubscription.emitted);
              }  
            n++;
            l = l2;
          } 
          while (l > 0L) {
            MulticastSubscription[] arrayOfMulticastSubscription2 = (MulticastSubscription[])atomicReference.get();
            if (arrayOfMulticastSubscription2 == TERMINATED) {
              simpleQueue.clear();
              return;
            } 
            if (arrayOfMulticastSubscription != arrayOfMulticastSubscription2)
              continue label92; 
            boolean bool = this.done;
            try {
              Object object = simpleQueue.poll();
            } finally {
              Exception exception = null;
              Exceptions.throwIfFatal(exception);
              SubscriptionHelper.cancel(this.upstream);
              arrayOfMulticastSubscription2 = null;
              this.error = exception;
              this.done = true;
            } 
            if (arrayOfMulticastSubscription2 == null) {
              n = 1;
            } else {
              n = 0;
            } 
            if (bool && n != 0) {
              Throwable throwable = this.error;
              if (throwable != null) {
                arrayOfMulticastSubscription1 = (MulticastSubscription[])atomicReference.getAndSet(TERMINATED);
                m = arrayOfMulticastSubscription1.length;
                for (i = 0; i < m; i++)
                  arrayOfMulticastSubscription1[i].onError(throwable); 
              } else {
                arrayOfMulticastSubscription2 = arrayOfMulticastSubscription1.getAndSet(TERMINATED);
                m = arrayOfMulticastSubscription2.length;
                for (i = 0; i < m; i++)
                  arrayOfMulticastSubscription2[i].onComplete(); 
              } 
              return;
            } 
            if (n != 0)
              break; 
            i2 = arrayOfMulticastSubscription.length;
            for (n = 0; n < i2; n++)
              arrayOfMulticastSubscription[n].onNext(arrayOfMulticastSubscription2); 
            long l1 = l - 1L;
            l = l1;
            if (k != 1) {
              if (++i == j) {
                ((Subscription)this.upstream.get()).request(j);
                i = 0;
                l = l1;
                continue;
              } 
              l = l1;
            } 
          } 
          if (l == 0L) {
            MulticastSubscription[] arrayOfMulticastSubscription2 = arrayOfMulticastSubscription1.get();
            if (arrayOfMulticastSubscription2 == TERMINATED) {
              simpleQueue.clear();
              return;
            } 
            if (arrayOfMulticastSubscription != arrayOfMulticastSubscription2)
              continue; 
            if (this.done && simpleQueue.isEmpty()) {
              Throwable throwable = this.error;
              if (throwable != null) {
                arrayOfMulticastSubscription1 = arrayOfMulticastSubscription1.getAndSet(TERMINATED);
                m = arrayOfMulticastSubscription1.length;
                for (i = 0; i < m; i++)
                  arrayOfMulticastSubscription1[i].onError(throwable); 
              } else {
                MulticastSubscription[] arrayOfMulticastSubscription3 = arrayOfMulticastSubscription1.getAndSet(TERMINATED);
                m = arrayOfMulticastSubscription3.length;
                for (i = 0; i < m; i++)
                  arrayOfMulticastSubscription3[i].onComplete(); 
              } 
              return;
            } 
          } 
          n = i;
        } 
      } 
      this.consumed = n;
      int i1 = this.wip.addAndGet(-m);
      i = n;
      m = i1;
      if (i1 == 0)
        break; 
    } 
  }
  
  public Throwable getThrowable() {
    Throwable throwable;
    if (this.once.get()) {
      throwable = this.error;
    } else {
      throwable = null;
    } 
    return throwable;
  }
  
  public boolean hasComplete() {
    boolean bool;
    if (this.once.get() && this.error == null) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean hasSubscribers() {
    boolean bool;
    if (((MulticastSubscription[])this.subscribers.get()).length != 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean hasThrowable() {
    boolean bool;
    if (this.once.get() && this.error != null) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean offer(T paramT) {
    if (this.once.get())
      return false; 
    ObjectHelper.requireNonNull(paramT, "offer called with null. Null values are generally not allowed in 2.x operators and sources.");
    if (this.fusionMode == 0 && this.queue.offer(paramT)) {
      drain();
      return true;
    } 
    return false;
  }
  
  public void onComplete() {
    if (this.once.compareAndSet(false, true)) {
      this.done = true;
      drain();
    } 
  }
  
  public void onError(Throwable paramThrowable) {
    ObjectHelper.requireNonNull(paramThrowable, "onError called with null. Null values are generally not allowed in 2.x operators and sources.");
    if (this.once.compareAndSet(false, true)) {
      this.error = paramThrowable;
      this.done = true;
      drain();
    } else {
      RxJavaPlugins.onError(paramThrowable);
    } 
  }
  
  public void onNext(T paramT) {
    if (this.once.get())
      return; 
    if (this.fusionMode == 0) {
      ObjectHelper.requireNonNull(paramT, "onNext called with null. Null values are generally not allowed in 2.x operators and sources.");
      if (!this.queue.offer(paramT)) {
        SubscriptionHelper.cancel(this.upstream);
        onError((Throwable)new MissingBackpressureException());
        return;
      } 
    } 
    drain();
  }
  
  public void onSubscribe(Subscription paramSubscription) {
    if (SubscriptionHelper.setOnce(this.upstream, paramSubscription)) {
      if (paramSubscription instanceof QueueSubscription) {
        QueueSubscription queueSubscription = (QueueSubscription)paramSubscription;
        int i = queueSubscription.requestFusion(3);
        if (i == 1) {
          this.fusionMode = i;
          this.queue = (SimpleQueue<T>)queueSubscription;
          this.done = true;
          drain();
          return;
        } 
        if (i == 2) {
          this.fusionMode = i;
          this.queue = (SimpleQueue<T>)queueSubscription;
          paramSubscription.request(this.bufferSize);
          return;
        } 
      } 
      this.queue = (SimpleQueue<T>)new SpscArrayQueue(this.bufferSize);
      paramSubscription.request(this.bufferSize);
    } 
  }
  
  void remove(MulticastSubscription<T> paramMulticastSubscription) {
    while (true) {
      byte b2;
      MulticastSubscription[] arrayOfMulticastSubscription1 = (MulticastSubscription[])this.subscribers.get();
      int i = arrayOfMulticastSubscription1.length;
      if (i == 0)
        return; 
      byte b1 = -1;
      byte b = 0;
      while (true) {
        b2 = b1;
        if (b < i) {
          if (arrayOfMulticastSubscription1[b] == paramMulticastSubscription) {
            b2 = b;
            break;
          } 
          b++;
          continue;
        } 
        break;
      } 
      if (b2 < 0)
        break; 
      if (i == 1) {
        if (this.refcount) {
          if (this.subscribers.compareAndSet(arrayOfMulticastSubscription1, TERMINATED)) {
            SubscriptionHelper.cancel(this.upstream);
            this.once.set(true);
            break;
          } 
          continue;
        } 
        if (this.subscribers.compareAndSet(arrayOfMulticastSubscription1, EMPTY))
          break; 
        continue;
      } 
      MulticastSubscription[] arrayOfMulticastSubscription2 = new MulticastSubscription[i - 1];
      System.arraycopy(arrayOfMulticastSubscription1, 0, arrayOfMulticastSubscription2, 0, b2);
      System.arraycopy(arrayOfMulticastSubscription1, b2 + 1, arrayOfMulticastSubscription2, b2, i - b2 - 1);
      if (this.subscribers.compareAndSet(arrayOfMulticastSubscription1, arrayOfMulticastSubscription2))
        break; 
    } 
  }
  
  public void start() {
    if (SubscriptionHelper.setOnce(this.upstream, (Subscription)EmptySubscription.INSTANCE))
      this.queue = (SimpleQueue<T>)new SpscArrayQueue(this.bufferSize); 
  }
  
  public void startUnbounded() {
    if (SubscriptionHelper.setOnce(this.upstream, (Subscription)EmptySubscription.INSTANCE))
      this.queue = (SimpleQueue<T>)new SpscLinkedArrayQueue(this.bufferSize); 
  }
  
  protected void subscribeActual(Subscriber<? super T> paramSubscriber) {
    MulticastSubscription<T> multicastSubscription = new MulticastSubscription<T>(paramSubscriber, this);
    paramSubscriber.onSubscribe(multicastSubscription);
    if (add(multicastSubscription)) {
      if (multicastSubscription.get() == Long.MIN_VALUE) {
        remove(multicastSubscription);
      } else {
        drain();
      } 
    } else {
      if (this.once.get() || !this.refcount) {
        Throwable throwable = this.error;
        if (throwable != null) {
          paramSubscriber.onError(throwable);
          return;
        } 
      } 
      paramSubscriber.onComplete();
    } 
  }
  
  static final class MulticastSubscription<T> extends AtomicLong implements Subscription {
    private static final long serialVersionUID = -363282618957264509L;
    
    final Subscriber<? super T> downstream;
    
    long emitted;
    
    final MulticastProcessor<T> parent;
    
    MulticastSubscription(Subscriber<? super T> param1Subscriber, MulticastProcessor<T> param1MulticastProcessor) {
      this.downstream = param1Subscriber;
      this.parent = param1MulticastProcessor;
    }
    
    public void cancel() {
      if (getAndSet(Long.MIN_VALUE) != Long.MIN_VALUE)
        this.parent.remove(this); 
    }
    
    void onComplete() {
      if (get() != Long.MIN_VALUE)
        this.downstream.onComplete(); 
    }
    
    void onError(Throwable param1Throwable) {
      if (get() != Long.MIN_VALUE)
        this.downstream.onError(param1Throwable); 
    }
    
    void onNext(T param1T) {
      if (get() != Long.MIN_VALUE) {
        this.emitted++;
        this.downstream.onNext(param1T);
      } 
    }
    
    public void request(long param1Long) {
      if (SubscriptionHelper.validate(param1Long))
        while (true) {
          long l = get();
          if (l != Long.MIN_VALUE) {
            long l1 = Long.MAX_VALUE;
            if (l == Long.MAX_VALUE)
              break; 
            long l2 = l + param1Long;
            if (l2 < 0L)
              l2 = l1; 
            if (compareAndSet(l, l2)) {
              this.parent.drain();
              break;
            } 
            continue;
          } 
          break;
        }  
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/processors/MulticastProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */