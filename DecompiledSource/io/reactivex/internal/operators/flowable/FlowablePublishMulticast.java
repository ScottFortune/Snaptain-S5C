package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.exceptions.MissingBackpressureException;
import io.reactivex.functions.Function;
import io.reactivex.internal.fuseable.QueueSubscription;
import io.reactivex.internal.fuseable.SimpleQueue;
import io.reactivex.internal.subscriptions.EmptySubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.internal.util.QueueDrainHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowablePublishMulticast<T, R> extends AbstractFlowableWithUpstream<T, R> {
  final boolean delayError;
  
  final int prefetch;
  
  final Function<? super Flowable<T>, ? extends Publisher<? extends R>> selector;
  
  public FlowablePublishMulticast(Flowable<T> paramFlowable, Function<? super Flowable<T>, ? extends Publisher<? extends R>> paramFunction, int paramInt, boolean paramBoolean) {
    super(paramFlowable);
    this.selector = paramFunction;
    this.prefetch = paramInt;
    this.delayError = paramBoolean;
  }
  
  protected void subscribeActual(Subscriber<? super R> paramSubscriber) {
    MulticastProcessor<?> multicastProcessor = new MulticastProcessor(this.prefetch, this.delayError);
    try {
      return;
    } finally {
      Exception exception = null;
      Exceptions.throwIfFatal(exception);
      EmptySubscription.error(exception, paramSubscriber);
    } 
  }
  
  static final class MulticastProcessor<T> extends Flowable<T> implements FlowableSubscriber<T>, Disposable {
    static final FlowablePublishMulticast.MulticastSubscription[] EMPTY = new FlowablePublishMulticast.MulticastSubscription[0];
    
    static final FlowablePublishMulticast.MulticastSubscription[] TERMINATED = new FlowablePublishMulticast.MulticastSubscription[0];
    
    int consumed;
    
    final boolean delayError;
    
    volatile boolean done;
    
    Throwable error;
    
    final int limit;
    
    final int prefetch;
    
    volatile SimpleQueue<T> queue;
    
    int sourceMode;
    
    final AtomicReference<FlowablePublishMulticast.MulticastSubscription<T>[]> subscribers;
    
    final AtomicReference<Subscription> upstream;
    
    final AtomicInteger wip;
    
    MulticastProcessor(int param1Int, boolean param1Boolean) {
      this.prefetch = param1Int;
      this.limit = param1Int - (param1Int >> 2);
      this.delayError = param1Boolean;
      this.wip = new AtomicInteger();
      this.upstream = new AtomicReference<Subscription>();
      this.subscribers = new AtomicReference(EMPTY);
    }
    
    boolean add(FlowablePublishMulticast.MulticastSubscription<T> param1MulticastSubscription) {
      while (true) {
        FlowablePublishMulticast.MulticastSubscription[] arrayOfMulticastSubscription1 = (FlowablePublishMulticast.MulticastSubscription[])this.subscribers.get();
        if (arrayOfMulticastSubscription1 == TERMINATED)
          return false; 
        int i = arrayOfMulticastSubscription1.length;
        FlowablePublishMulticast.MulticastSubscription[] arrayOfMulticastSubscription2 = new FlowablePublishMulticast.MulticastSubscription[i + 1];
        System.arraycopy(arrayOfMulticastSubscription1, 0, arrayOfMulticastSubscription2, 0, i);
        arrayOfMulticastSubscription2[i] = param1MulticastSubscription;
        if (this.subscribers.compareAndSet(arrayOfMulticastSubscription1, arrayOfMulticastSubscription2))
          return true; 
      } 
    }
    
    void completeAll() {
      for (FlowablePublishMulticast.MulticastSubscription multicastSubscription : (FlowablePublishMulticast.MulticastSubscription[])this.subscribers.getAndSet(TERMINATED)) {
        if (multicastSubscription.get() != Long.MIN_VALUE)
          multicastSubscription.downstream.onComplete(); 
      } 
    }
    
    public void dispose() {
      SubscriptionHelper.cancel(this.upstream);
      if (this.wip.getAndIncrement() == 0) {
        SimpleQueue<T> simpleQueue = this.queue;
        if (simpleQueue != null)
          simpleQueue.clear(); 
      } 
    }
    
    void drain() {
      boolean bool;
      if (this.wip.getAndIncrement() != 0)
        return; 
      SimpleQueue<T> simpleQueue = this.queue;
      int i = this.consumed;
      int j = this.limit;
      if (this.sourceMode != 1) {
        bool = true;
      } else {
        bool = false;
      } 
      AtomicReference<FlowablePublishMulticast.MulticastSubscription<T>[]> atomicReference = this.subscribers;
      FlowablePublishMulticast.MulticastSubscription[] arrayOfMulticastSubscription = (FlowablePublishMulticast.MulticastSubscription[])atomicReference.get();
      int k = 1;
      label115: while (true) {
        SimpleQueue<T> simpleQueue2;
        Throwable throwable4;
        int m = arrayOfMulticastSubscription.length;
        if (simpleQueue != null && m != 0) {
          int n = arrayOfMulticastSubscription.length;
          long l = Long.MAX_VALUE;
          int i1 = 0;
          while (i1 < n) {
            int i3;
            long l2;
            FlowablePublishMulticast.MulticastSubscription multicastSubscription = arrayOfMulticastSubscription[i1];
            long l1 = multicastSubscription.get() - multicastSubscription.emitted;
            if (l1 != Long.MIN_VALUE) {
              i3 = m;
              l2 = l;
              if (l > l1) {
                l2 = l1;
                i3 = m;
              } 
            } else {
              i3 = m - 1;
              l2 = l;
            } 
            i1++;
            m = i3;
            l = l2;
          } 
          int i2 = i;
          if (m == 0) {
            l = 0L;
            i2 = i;
          } 
          while (l != 0L) {
            if (isDisposed()) {
              simpleQueue.clear();
              return;
            } 
            boolean bool1 = this.done;
            if (bool1 && !this.delayError) {
              Throwable throwable = this.error;
              if (throwable != null) {
                errorAll(throwable);
                return;
              } 
            } 
            try {
              Object object = simpleQueue.poll();
              if (object == null) {
                i = 1;
              } else {
                i = 0;
              } 
              if (bool1 && i != 0)
                return; 
              if (i != 0)
                break; 
              i1 = arrayOfMulticastSubscription.length;
              i = 0;
              m = 0;
              while (i < i1) {
                FlowablePublishMulticast.MulticastSubscription multicastSubscription = arrayOfMulticastSubscription[i];
                long l1 = multicastSubscription.get();
                if (l1 != Long.MIN_VALUE) {
                  if (l1 != Long.MAX_VALUE)
                    multicastSubscription.emitted++; 
                  multicastSubscription.downstream.onNext(object);
                } else {
                  m = 1;
                } 
                i++;
              } 
              l--;
              i = i2;
            } finally {
              atomicReference = null;
              Exceptions.throwIfFatal((Throwable)atomicReference);
              SubscriptionHelper.cancel(this.upstream);
              errorAll((Throwable)atomicReference);
            } 
          } 
          i = i2;
          AtomicReference<FlowablePublishMulticast.MulticastSubscription<T>[]> atomicReference1 = atomicReference;
          if (l == 0L) {
            if (isDisposed()) {
              throwable1.clear();
              return;
            } 
            boolean bool1 = this.done;
            if (bool1 && !this.delayError) {
              throwable4 = this.error;
              if (throwable4 != null) {
                errorAll(throwable4);
                return;
              } 
            } 
            i = i2;
            atomicReference1 = atomicReference;
            if (bool1) {
              i = i2;
              atomicReference1 = atomicReference;
              if (throwable1.isEmpty()) {
                throwable3 = this.error;
                if (throwable3 != null) {
                  errorAll(throwable3);
                } else {
                  completeAll();
                } 
                return;
              } 
            } 
          } 
        } else {
          throwable4 = throwable3;
        } 
        this.consumed = i;
        k = this.wip.addAndGet(-k);
        if (k == 0)
          return; 
        Throwable throwable3 = throwable1;
        if (throwable1 == null)
          simpleQueue2 = this.queue; 
        FlowablePublishMulticast.MulticastSubscription[] arrayOfMulticastSubscription2 = throwable4.get();
        Throwable throwable1 = throwable4;
        FlowablePublishMulticast.MulticastSubscription[] arrayOfMulticastSubscription1 = arrayOfMulticastSubscription2;
        SimpleQueue<T> simpleQueue3 = simpleQueue2;
        Throwable throwable2 = throwable1;
        SimpleQueue<T> simpleQueue1 = simpleQueue3;
      } 
    }
    
    void errorAll(Throwable param1Throwable) {
      for (FlowablePublishMulticast.MulticastSubscription multicastSubscription : (FlowablePublishMulticast.MulticastSubscription[])this.subscribers.getAndSet(TERMINATED)) {
        if (multicastSubscription.get() != Long.MIN_VALUE)
          multicastSubscription.downstream.onError(param1Throwable); 
      } 
    }
    
    public boolean isDisposed() {
      boolean bool;
      if (this.upstream.get() == SubscriptionHelper.CANCELLED) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public void onComplete() {
      if (!this.done) {
        this.done = true;
        drain();
      } 
    }
    
    public void onError(Throwable param1Throwable) {
      if (this.done) {
        RxJavaPlugins.onError(param1Throwable);
        return;
      } 
      this.error = param1Throwable;
      this.done = true;
      drain();
    }
    
    public void onNext(T param1T) {
      if (this.done)
        return; 
      if (this.sourceMode == 0 && !this.queue.offer(param1T)) {
        ((Subscription)this.upstream.get()).cancel();
        onError((Throwable)new MissingBackpressureException());
        return;
      } 
      drain();
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      if (SubscriptionHelper.setOnce(this.upstream, param1Subscription)) {
        if (param1Subscription instanceof QueueSubscription) {
          QueueSubscription queueSubscription = (QueueSubscription)param1Subscription;
          int i = queueSubscription.requestFusion(3);
          if (i == 1) {
            this.sourceMode = i;
            this.queue = (SimpleQueue<T>)queueSubscription;
            this.done = true;
            drain();
            return;
          } 
          if (i == 2) {
            this.sourceMode = i;
            this.queue = (SimpleQueue<T>)queueSubscription;
            QueueDrainHelper.request(param1Subscription, this.prefetch);
            return;
          } 
        } 
        this.queue = QueueDrainHelper.createQueue(this.prefetch);
        QueueDrainHelper.request(param1Subscription, this.prefetch);
      } 
    }
    
    void remove(FlowablePublishMulticast.MulticastSubscription<T> param1MulticastSubscription) {
      FlowablePublishMulticast.MulticastSubscription[] arrayOfMulticastSubscription1;
      FlowablePublishMulticast.MulticastSubscription[] arrayOfMulticastSubscription2;
      do {
        byte b2;
        arrayOfMulticastSubscription1 = (FlowablePublishMulticast.MulticastSubscription[])this.subscribers.get();
        int i = arrayOfMulticastSubscription1.length;
        if (i == 0)
          return; 
        byte b1 = -1;
        byte b = 0;
        while (true) {
          b2 = b1;
          if (b < i) {
            if (arrayOfMulticastSubscription1[b] == param1MulticastSubscription) {
              b2 = b;
              break;
            } 
            b++;
            continue;
          } 
          break;
        } 
        if (b2 < 0)
          return; 
        if (i == 1) {
          arrayOfMulticastSubscription2 = EMPTY;
        } else {
          arrayOfMulticastSubscription2 = new FlowablePublishMulticast.MulticastSubscription[i - 1];
          System.arraycopy(arrayOfMulticastSubscription1, 0, arrayOfMulticastSubscription2, 0, b2);
          System.arraycopy(arrayOfMulticastSubscription1, b2 + 1, arrayOfMulticastSubscription2, b2, i - b2 - 1);
        } 
      } while (!this.subscribers.compareAndSet(arrayOfMulticastSubscription1, arrayOfMulticastSubscription2));
    }
    
    protected void subscribeActual(Subscriber<? super T> param1Subscriber) {
      FlowablePublishMulticast.MulticastSubscription<T> multicastSubscription = new FlowablePublishMulticast.MulticastSubscription<T>(param1Subscriber, this);
      param1Subscriber.onSubscribe(multicastSubscription);
      if (add(multicastSubscription)) {
        if (multicastSubscription.isCancelled()) {
          remove(multicastSubscription);
          return;
        } 
        drain();
      } else {
        Throwable throwable = this.error;
        if (throwable != null) {
          param1Subscriber.onError(throwable);
        } else {
          param1Subscriber.onComplete();
        } 
      } 
    }
  }
  
  static final class MulticastSubscription<T> extends AtomicLong implements Subscription {
    private static final long serialVersionUID = 8664815189257569791L;
    
    final Subscriber<? super T> downstream;
    
    long emitted;
    
    final FlowablePublishMulticast.MulticastProcessor<T> parent;
    
    MulticastSubscription(Subscriber<? super T> param1Subscriber, FlowablePublishMulticast.MulticastProcessor<T> param1MulticastProcessor) {
      this.downstream = param1Subscriber;
      this.parent = param1MulticastProcessor;
    }
    
    public void cancel() {
      if (getAndSet(Long.MIN_VALUE) != Long.MIN_VALUE) {
        this.parent.remove(this);
        this.parent.drain();
      } 
    }
    
    public boolean isCancelled() {
      boolean bool;
      if (get() == Long.MIN_VALUE) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public void request(long param1Long) {
      if (SubscriptionHelper.validate(param1Long)) {
        BackpressureHelper.addCancel(this, param1Long);
        this.parent.drain();
      } 
    }
  }
  
  static final class OutputCanceller<R> implements FlowableSubscriber<R>, Subscription {
    final Subscriber<? super R> downstream;
    
    final FlowablePublishMulticast.MulticastProcessor<?> processor;
    
    Subscription upstream;
    
    OutputCanceller(Subscriber<? super R> param1Subscriber, FlowablePublishMulticast.MulticastProcessor<?> param1MulticastProcessor) {
      this.downstream = param1Subscriber;
      this.processor = param1MulticastProcessor;
    }
    
    public void cancel() {
      this.upstream.cancel();
      this.processor.dispose();
    }
    
    public void onComplete() {
      this.downstream.onComplete();
      this.processor.dispose();
    }
    
    public void onError(Throwable param1Throwable) {
      this.downstream.onError(param1Throwable);
      this.processor.dispose();
    }
    
    public void onNext(R param1R) {
      this.downstream.onNext(param1R);
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      if (SubscriptionHelper.validate(this.upstream, param1Subscription)) {
        this.upstream = param1Subscription;
        this.downstream.onSubscribe(this);
      } 
    }
    
    public void request(long param1Long) {
      this.upstream.request(param1Long);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowablePublishMulticast.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */