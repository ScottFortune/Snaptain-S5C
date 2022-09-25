package io.reactivex.internal.operators.parallel;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.BiFunction;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.subscriptions.DeferredScalarSubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.parallel.ParallelFlowable;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class ParallelReduceFull<T> extends Flowable<T> {
  final BiFunction<T, T, T> reducer;
  
  final ParallelFlowable<? extends T> source;
  
  public ParallelReduceFull(ParallelFlowable<? extends T> paramParallelFlowable, BiFunction<T, T, T> paramBiFunction) {
    this.source = paramParallelFlowable;
    this.reducer = paramBiFunction;
  }
  
  protected void subscribeActual(Subscriber<? super T> paramSubscriber) {
    ParallelReduceFullMainSubscriber<T> parallelReduceFullMainSubscriber = new ParallelReduceFullMainSubscriber<T>(paramSubscriber, this.source.parallelism(), this.reducer);
    paramSubscriber.onSubscribe((Subscription)parallelReduceFullMainSubscriber);
    this.source.subscribe((Subscriber[])parallelReduceFullMainSubscriber.subscribers);
  }
  
  static final class ParallelReduceFullInnerSubscriber<T> extends AtomicReference<Subscription> implements FlowableSubscriber<T> {
    private static final long serialVersionUID = -7954444275102466525L;
    
    boolean done;
    
    final ParallelReduceFull.ParallelReduceFullMainSubscriber<T> parent;
    
    final BiFunction<T, T, T> reducer;
    
    T value;
    
    ParallelReduceFullInnerSubscriber(ParallelReduceFull.ParallelReduceFullMainSubscriber<T> param1ParallelReduceFullMainSubscriber, BiFunction<T, T, T> param1BiFunction) {
      this.parent = param1ParallelReduceFullMainSubscriber;
      this.reducer = param1BiFunction;
    }
    
    void cancel() {
      SubscriptionHelper.cancel(this);
    }
    
    public void onComplete() {
      if (!this.done) {
        this.done = true;
        this.parent.innerComplete(this.value);
      } 
    }
    
    public void onError(Throwable param1Throwable) {
      if (this.done) {
        RxJavaPlugins.onError(param1Throwable);
        return;
      } 
      this.done = true;
      this.parent.innerError(param1Throwable);
    }
    
    public void onNext(T param1T) {
      if (!this.done) {
        T t = this.value;
        if (t == null) {
          this.value = param1T;
        } else {
          try {
          
          } finally {
            param1T = null;
            Exceptions.throwIfFatal((Throwable)param1T);
            get().cancel();
          } 
        } 
      } 
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      SubscriptionHelper.setOnce(this, param1Subscription, Long.MAX_VALUE);
    }
  }
  
  static final class ParallelReduceFullMainSubscriber<T> extends DeferredScalarSubscription<T> {
    private static final long serialVersionUID = -5370107872170712765L;
    
    final AtomicReference<ParallelReduceFull.SlotPair<T>> current = new AtomicReference<ParallelReduceFull.SlotPair<T>>();
    
    final AtomicReference<Throwable> error = new AtomicReference<Throwable>();
    
    final BiFunction<T, T, T> reducer;
    
    final AtomicInteger remaining = new AtomicInteger();
    
    final ParallelReduceFull.ParallelReduceFullInnerSubscriber<T>[] subscribers;
    
    ParallelReduceFullMainSubscriber(Subscriber<? super T> param1Subscriber, int param1Int, BiFunction<T, T, T> param1BiFunction) {
      super(param1Subscriber);
      ParallelReduceFull.ParallelReduceFullInnerSubscriber[] arrayOfParallelReduceFullInnerSubscriber = new ParallelReduceFull.ParallelReduceFullInnerSubscriber[param1Int];
      for (byte b = 0; b < param1Int; b++)
        arrayOfParallelReduceFullInnerSubscriber[b] = new ParallelReduceFull.ParallelReduceFullInnerSubscriber<T>(this, param1BiFunction); 
      this.subscribers = (ParallelReduceFull.ParallelReduceFullInnerSubscriber<T>[])arrayOfParallelReduceFullInnerSubscriber;
      this.reducer = param1BiFunction;
      this.remaining.lazySet(param1Int);
    }
    
    ParallelReduceFull.SlotPair<T> addValue(T param1T) {
      while (true) {
        ParallelReduceFull.SlotPair<T> slotPair1 = this.current.get();
        ParallelReduceFull.SlotPair<T> slotPair2 = slotPair1;
        if (slotPair1 == null) {
          slotPair1 = new ParallelReduceFull.SlotPair();
          slotPair2 = slotPair1;
          if (!this.current.compareAndSet(null, slotPair1))
            continue; 
        } 
        int i = slotPair2.tryAcquireSlot();
        if (i < 0) {
          this.current.compareAndSet(slotPair2, null);
          continue;
        } 
        if (i == 0) {
          slotPair2.first = param1T;
        } else {
          slotPair2.second = param1T;
        } 
        if (slotPair2.releaseSlot()) {
          this.current.compareAndSet(slotPair2, null);
          return slotPair2;
        } 
        return null;
      } 
    }
    
    public void cancel() {
      ParallelReduceFull.ParallelReduceFullInnerSubscriber<T>[] arrayOfParallelReduceFullInnerSubscriber = this.subscribers;
      int i = arrayOfParallelReduceFullInnerSubscriber.length;
      for (byte b = 0; b < i; b++)
        arrayOfParallelReduceFullInnerSubscriber[b].cancel(); 
    }
    
    void innerComplete(T param1T) {
      if (param1T != null)
        while (true) {
          ParallelReduceFull.SlotPair<T> slotPair = addValue(param1T);
          if (slotPair != null) {
            try {
              Object object = ObjectHelper.requireNonNull(this.reducer.apply(slotPair.first, slotPair.second), "The reducer returned a null value");
            } finally {
              slotPair = null;
              Exceptions.throwIfFatal((Throwable)slotPair);
              innerError((Throwable)slotPair);
            } 
            continue;
          } 
          break;
        }  
      if (this.remaining.decrementAndGet() == 0) {
        ParallelReduceFull.SlotPair slotPair = this.current.get();
        this.current.lazySet(null);
        if (slotPair != null) {
          complete(slotPair.first);
        } else {
          this.downstream.onComplete();
        } 
      } 
    }
    
    void innerError(Throwable param1Throwable) {
      if (this.error.compareAndSet(null, param1Throwable)) {
        cancel();
        this.downstream.onError(param1Throwable);
      } else if (param1Throwable != this.error.get()) {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
  }
  
  static final class SlotPair<T> extends AtomicInteger {
    private static final long serialVersionUID = 473971317683868662L;
    
    T first;
    
    final AtomicInteger releaseIndex = new AtomicInteger();
    
    T second;
    
    boolean releaseSlot() {
      boolean bool;
      if (this.releaseIndex.incrementAndGet() == 2) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    int tryAcquireSlot() {
      while (true) {
        int i = get();
        if (i >= 2)
          return -1; 
        if (compareAndSet(i, i + 1))
          return i; 
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/parallel/ParallelReduceFull.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */