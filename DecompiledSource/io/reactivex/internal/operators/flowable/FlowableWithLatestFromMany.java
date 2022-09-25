package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.fuseable.ConditionalSubscriber;
import io.reactivex.internal.subscriptions.EmptySubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.AtomicThrowable;
import io.reactivex.internal.util.HalfSerializer;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableWithLatestFromMany<T, R> extends AbstractFlowableWithUpstream<T, R> {
  final Function<? super Object[], R> combiner;
  
  final Publisher<?>[] otherArray = null;
  
  final Iterable<? extends Publisher<?>> otherIterable;
  
  public FlowableWithLatestFromMany(Flowable<T> paramFlowable, Iterable<? extends Publisher<?>> paramIterable, Function<? super Object[], R> paramFunction) {
    super(paramFlowable);
    this.otherIterable = paramIterable;
    this.combiner = paramFunction;
  }
  
  public FlowableWithLatestFromMany(Flowable<T> paramFlowable, Publisher<?>[] paramArrayOfPublisher, Function<? super Object[], R> paramFunction) {
    super(paramFlowable);
    this.otherIterable = null;
    this.combiner = paramFunction;
  }
  
  protected void subscribeActual(Subscriber<? super R> paramSubscriber) {
    Publisher[] arrayOfPublisher;
    int i;
    Publisher<?>[] arrayOfPublisher1 = this.otherArray;
    if (arrayOfPublisher1 == null) {
      Publisher[] arrayOfPublisher2 = new Publisher[8];
      try {
        Iterator<? extends Publisher<?>> iterator = this.otherIterable.iterator();
        byte b = 0;
        while (true) {
          arrayOfPublisher = arrayOfPublisher2;
          i = b;
          if (iterator.hasNext()) {
            Publisher publisher = iterator.next();
            arrayOfPublisher = arrayOfPublisher2;
            if (b == arrayOfPublisher2.length)
              arrayOfPublisher = Arrays.<Publisher>copyOf(arrayOfPublisher2, (b >> 1) + b); 
            arrayOfPublisher[b] = publisher;
            b++;
            arrayOfPublisher2 = arrayOfPublisher;
            continue;
          } 
          break;
        } 
      } finally {
        arrayOfPublisher2 = null;
        Exceptions.throwIfFatal((Throwable)arrayOfPublisher2);
        EmptySubscription.error((Throwable)arrayOfPublisher2, paramSubscriber);
      } 
    } else {
      i = arrayOfPublisher.length;
    } 
    if (i == 0) {
      (new FlowableMap<Object, R>(this.source, new SingletonArrayFunc())).subscribeActual(paramSubscriber);
      return;
    } 
    WithLatestFromSubscriber<Object, R> withLatestFromSubscriber = new WithLatestFromSubscriber<Object, R>(paramSubscriber, this.combiner, i);
    paramSubscriber.onSubscribe(withLatestFromSubscriber);
    withLatestFromSubscriber.subscribe((Publisher<?>[])arrayOfPublisher, i);
    this.source.subscribe((FlowableSubscriber)withLatestFromSubscriber);
  }
  
  final class SingletonArrayFunc implements Function<T, R> {
    public R apply(T param1T) throws Exception {
      return (R)ObjectHelper.requireNonNull(FlowableWithLatestFromMany.this.combiner.apply(new Object[] { param1T }, ), "The combiner returned a null value");
    }
  }
  
  static final class WithLatestFromSubscriber<T, R> extends AtomicInteger implements ConditionalSubscriber<T>, Subscription {
    private static final long serialVersionUID = 1577321883966341961L;
    
    final Function<? super Object[], R> combiner;
    
    volatile boolean done;
    
    final Subscriber<? super R> downstream;
    
    final AtomicThrowable error;
    
    final AtomicLong requested;
    
    final FlowableWithLatestFromMany.WithLatestInnerSubscriber[] subscribers;
    
    final AtomicReference<Subscription> upstream;
    
    final AtomicReferenceArray<Object> values;
    
    WithLatestFromSubscriber(Subscriber<? super R> param1Subscriber, Function<? super Object[], R> param1Function, int param1Int) {
      this.downstream = param1Subscriber;
      this.combiner = param1Function;
      FlowableWithLatestFromMany.WithLatestInnerSubscriber[] arrayOfWithLatestInnerSubscriber = new FlowableWithLatestFromMany.WithLatestInnerSubscriber[param1Int];
      for (byte b = 0; b < param1Int; b++)
        arrayOfWithLatestInnerSubscriber[b] = new FlowableWithLatestFromMany.WithLatestInnerSubscriber(this, b); 
      this.subscribers = arrayOfWithLatestInnerSubscriber;
      this.values = new AtomicReferenceArray(param1Int);
      this.upstream = new AtomicReference<Subscription>();
      this.requested = new AtomicLong();
      this.error = new AtomicThrowable();
    }
    
    public void cancel() {
      SubscriptionHelper.cancel(this.upstream);
      FlowableWithLatestFromMany.WithLatestInnerSubscriber[] arrayOfWithLatestInnerSubscriber = this.subscribers;
      int i = arrayOfWithLatestInnerSubscriber.length;
      for (byte b = 0; b < i; b++)
        arrayOfWithLatestInnerSubscriber[b].dispose(); 
    }
    
    void cancelAllBut(int param1Int) {
      FlowableWithLatestFromMany.WithLatestInnerSubscriber[] arrayOfWithLatestInnerSubscriber = this.subscribers;
      for (byte b = 0; b < arrayOfWithLatestInnerSubscriber.length; b++) {
        if (b != param1Int)
          arrayOfWithLatestInnerSubscriber[b].dispose(); 
      } 
    }
    
    void innerComplete(int param1Int, boolean param1Boolean) {
      if (!param1Boolean) {
        this.done = true;
        SubscriptionHelper.cancel(this.upstream);
        cancelAllBut(param1Int);
        HalfSerializer.onComplete(this.downstream, this, this.error);
      } 
    }
    
    void innerError(int param1Int, Throwable param1Throwable) {
      this.done = true;
      SubscriptionHelper.cancel(this.upstream);
      cancelAllBut(param1Int);
      HalfSerializer.onError(this.downstream, param1Throwable, this, this.error);
    }
    
    void innerNext(int param1Int, Object param1Object) {
      this.values.set(param1Int, param1Object);
    }
    
    public void onComplete() {
      if (!this.done) {
        this.done = true;
        cancelAllBut(-1);
        HalfSerializer.onComplete(this.downstream, this, this.error);
      } 
    }
    
    public void onError(Throwable param1Throwable) {
      if (this.done) {
        RxJavaPlugins.onError(param1Throwable);
        return;
      } 
      this.done = true;
      cancelAllBut(-1);
      HalfSerializer.onError(this.downstream, param1Throwable, this, this.error);
    }
    
    public void onNext(T param1T) {
      if (!tryOnNext(param1T) && !this.done)
        ((Subscription)this.upstream.get()).request(1L); 
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      SubscriptionHelper.deferredSetOnce(this.upstream, this.requested, param1Subscription);
    }
    
    public void request(long param1Long) {
      SubscriptionHelper.deferredRequest(this.upstream, this.requested, param1Long);
    }
    
    void subscribe(Publisher<?>[] param1ArrayOfPublisher, int param1Int) {
      FlowableWithLatestFromMany.WithLatestInnerSubscriber[] arrayOfWithLatestInnerSubscriber = this.subscribers;
      AtomicReference<Subscription> atomicReference = this.upstream;
      for (byte b = 0; b < param1Int; b++) {
        if (atomicReference.get() == SubscriptionHelper.CANCELLED)
          return; 
        param1ArrayOfPublisher[b].subscribe((Subscriber)arrayOfWithLatestInnerSubscriber[b]);
      } 
    }
    
    public boolean tryOnNext(T param1T) {
      if (this.done)
        return false; 
      AtomicReferenceArray<Object> atomicReferenceArray = this.values;
      int i = atomicReferenceArray.length();
      Object[] arrayOfObject = new Object[i + 1];
      arrayOfObject[0] = param1T;
      byte b = 0;
      while (b < i) {
        param1T = (T)atomicReferenceArray.get(b);
        if (param1T == null)
          return false; 
        arrayOfObject[++b] = param1T;
      } 
      try {
        return true;
      } finally {
        param1T = null;
        Exceptions.throwIfFatal((Throwable)param1T);
        cancel();
        onError((Throwable)param1T);
      } 
    }
  }
  
  static final class WithLatestInnerSubscriber extends AtomicReference<Subscription> implements FlowableSubscriber<Object> {
    private static final long serialVersionUID = 3256684027868224024L;
    
    boolean hasValue;
    
    final int index;
    
    final FlowableWithLatestFromMany.WithLatestFromSubscriber<?, ?> parent;
    
    WithLatestInnerSubscriber(FlowableWithLatestFromMany.WithLatestFromSubscriber<?, ?> param1WithLatestFromSubscriber, int param1Int) {
      this.parent = param1WithLatestFromSubscriber;
      this.index = param1Int;
    }
    
    void dispose() {
      SubscriptionHelper.cancel(this);
    }
    
    public void onComplete() {
      this.parent.innerComplete(this.index, this.hasValue);
    }
    
    public void onError(Throwable param1Throwable) {
      this.parent.innerError(this.index, param1Throwable);
    }
    
    public void onNext(Object param1Object) {
      if (!this.hasValue)
        this.hasValue = true; 
      this.parent.innerNext(this.index, param1Object);
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      SubscriptionHelper.setOnce(this, param1Subscription, Long.MAX_VALUE);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableWithLatestFromMany.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */