package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.fuseable.SimplePlainQueue;
import io.reactivex.internal.queue.SpscArrayQueue;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.AtomicThrowable;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableMergeWithSingle<T> extends AbstractFlowableWithUpstream<T, T> {
  final SingleSource<? extends T> other;
  
  public FlowableMergeWithSingle(Flowable<T> paramFlowable, SingleSource<? extends T> paramSingleSource) {
    super(paramFlowable);
    this.other = paramSingleSource;
  }
  
  protected void subscribeActual(Subscriber<? super T> paramSubscriber) {
    MergeWithObserver<T> mergeWithObserver = new MergeWithObserver<T>(paramSubscriber);
    paramSubscriber.onSubscribe(mergeWithObserver);
    this.source.subscribe(mergeWithObserver);
    this.other.subscribe(mergeWithObserver.otherObserver);
  }
  
  static final class MergeWithObserver<T> extends AtomicInteger implements FlowableSubscriber<T>, Subscription {
    static final int OTHER_STATE_CONSUMED_OR_EMPTY = 2;
    
    static final int OTHER_STATE_HAS_VALUE = 1;
    
    private static final long serialVersionUID = -4592979584110982903L;
    
    volatile boolean cancelled;
    
    int consumed;
    
    final Subscriber<? super T> downstream;
    
    long emitted;
    
    final AtomicThrowable error;
    
    final int limit;
    
    volatile boolean mainDone;
    
    final AtomicReference<Subscription> mainSubscription;
    
    final OtherObserver<T> otherObserver;
    
    volatile int otherState;
    
    final int prefetch;
    
    volatile SimplePlainQueue<T> queue;
    
    final AtomicLong requested;
    
    T singleItem;
    
    MergeWithObserver(Subscriber<? super T> param1Subscriber) {
      this.downstream = param1Subscriber;
      this.mainSubscription = new AtomicReference<Subscription>();
      this.otherObserver = new OtherObserver<T>(this);
      this.error = new AtomicThrowable();
      this.requested = new AtomicLong();
      this.prefetch = Flowable.bufferSize();
      int i = this.prefetch;
      this.limit = i - (i >> 2);
    }
    
    public void cancel() {
      this.cancelled = true;
      SubscriptionHelper.cancel(this.mainSubscription);
      DisposableHelper.dispose(this.otherObserver);
      if (getAndIncrement() == 0) {
        this.queue = null;
        this.singleItem = null;
      } 
    }
    
    void drain() {
      if (getAndIncrement() == 0)
        drainLoop(); 
    }
    
    void drainLoop() {
      Subscriber<? super T> subscriber = this.downstream;
      long l = this.emitted;
      int i = this.consumed;
      int j = this.limit;
      int k = 1;
      do {
        long l1 = this.requested.get();
        while (l != l1) {
          if (this.cancelled) {
            this.singleItem = null;
            this.queue = null;
            return;
          } 
          if (this.error.get() != null) {
            this.singleItem = null;
            this.queue = null;
            subscriber.onError(this.error.terminate());
            return;
          } 
          int m = this.otherState;
          if (m == 1) {
            T t = this.singleItem;
            this.singleItem = null;
            this.otherState = 2;
            subscriber.onNext(t);
            l++;
            continue;
          } 
          boolean bool = this.mainDone;
          SimplePlainQueue<T> simplePlainQueue = this.queue;
          if (simplePlainQueue != null) {
            Object object = simplePlainQueue.poll();
          } else {
            simplePlainQueue = null;
          } 
          if (simplePlainQueue == null) {
            n = 1;
          } else {
            n = 0;
          } 
          if (bool && n && m == 2) {
            this.queue = null;
            subscriber.onComplete();
            return;
          } 
          if (n)
            break; 
          subscriber.onNext(simplePlainQueue);
          l++;
          int n = i + 1;
          i = n;
          if (n == j) {
            ((Subscription)this.mainSubscription.get()).request(j);
            i = 0;
          } 
        } 
        if (l == l1) {
          boolean bool1;
          if (this.cancelled) {
            this.singleItem = null;
            this.queue = null;
            return;
          } 
          if (this.error.get() != null) {
            this.singleItem = null;
            this.queue = null;
            subscriber.onError(this.error.terminate());
            return;
          } 
          boolean bool = this.mainDone;
          SimplePlainQueue<T> simplePlainQueue = this.queue;
          if (simplePlainQueue == null || simplePlainQueue.isEmpty()) {
            bool1 = true;
          } else {
            bool1 = false;
          } 
          if (bool && bool1 && this.otherState == 2) {
            this.queue = null;
            subscriber.onComplete();
            return;
          } 
        } 
        this.emitted = l;
        this.consumed = i;
        k = addAndGet(-k);
      } while (k != 0);
    }
    
    SimplePlainQueue<T> getOrCreateQueue() {
      SpscArrayQueue spscArrayQueue;
      SimplePlainQueue<T> simplePlainQueue1 = this.queue;
      SimplePlainQueue<T> simplePlainQueue2 = simplePlainQueue1;
      if (simplePlainQueue1 == null) {
        spscArrayQueue = new SpscArrayQueue(Flowable.bufferSize());
        this.queue = (SimplePlainQueue<T>)spscArrayQueue;
      } 
      return (SimplePlainQueue<T>)spscArrayQueue;
    }
    
    public void onComplete() {
      this.mainDone = true;
      drain();
    }
    
    public void onError(Throwable param1Throwable) {
      if (this.error.addThrowable(param1Throwable)) {
        DisposableHelper.dispose(this.otherObserver);
        drain();
      } else {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
    
    public void onNext(T param1T) {
      if (compareAndSet(0, 1)) {
        long l = this.emitted;
        if (this.requested.get() != l) {
          SimplePlainQueue<T> simplePlainQueue = this.queue;
          if (simplePlainQueue == null || simplePlainQueue.isEmpty()) {
            this.emitted = l + 1L;
            this.downstream.onNext(param1T);
            int i = this.consumed + 1;
            if (i == this.limit) {
              this.consumed = 0;
              ((Subscription)this.mainSubscription.get()).request(i);
            } else {
              this.consumed = i;
            } 
          } else {
            simplePlainQueue.offer(param1T);
          } 
        } else {
          getOrCreateQueue().offer(param1T);
        } 
        if (decrementAndGet() == 0)
          return; 
      } else {
        getOrCreateQueue().offer(param1T);
        if (getAndIncrement() != 0)
          return; 
      } 
      drainLoop();
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      SubscriptionHelper.setOnce(this.mainSubscription, param1Subscription, this.prefetch);
    }
    
    void otherError(Throwable param1Throwable) {
      if (this.error.addThrowable(param1Throwable)) {
        SubscriptionHelper.cancel(this.mainSubscription);
        drain();
      } else {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
    
    void otherSuccess(T param1T) {
      if (compareAndSet(0, 1)) {
        long l = this.emitted;
        if (this.requested.get() != l) {
          this.emitted = l + 1L;
          this.downstream.onNext(param1T);
          this.otherState = 2;
        } else {
          this.singleItem = param1T;
          this.otherState = 1;
          if (decrementAndGet() == 0)
            return; 
        } 
      } else {
        this.singleItem = param1T;
        this.otherState = 1;
        if (getAndIncrement() != 0)
          return; 
      } 
      drainLoop();
    }
    
    public void request(long param1Long) {
      BackpressureHelper.add(this.requested, param1Long);
      drain();
    }
    
    static final class OtherObserver<T> extends AtomicReference<Disposable> implements SingleObserver<T> {
      private static final long serialVersionUID = -2935427570954647017L;
      
      final FlowableMergeWithSingle.MergeWithObserver<T> parent;
      
      OtherObserver(FlowableMergeWithSingle.MergeWithObserver<T> param2MergeWithObserver) {
        this.parent = param2MergeWithObserver;
      }
      
      public void onError(Throwable param2Throwable) {
        this.parent.otherError(param2Throwable);
      }
      
      public void onSubscribe(Disposable param2Disposable) {
        DisposableHelper.setOnce(this, param2Disposable);
      }
      
      public void onSuccess(T param2T) {
        this.parent.otherSuccess(param2T);
      }
    }
  }
  
  static final class OtherObserver<T> extends AtomicReference<Disposable> implements SingleObserver<T> {
    private static final long serialVersionUID = -2935427570954647017L;
    
    final FlowableMergeWithSingle.MergeWithObserver<T> parent;
    
    OtherObserver(FlowableMergeWithSingle.MergeWithObserver<T> param1MergeWithObserver) {
      this.parent = param1MergeWithObserver;
    }
    
    public void onError(Throwable param1Throwable) {
      this.parent.otherError(param1Throwable);
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      DisposableHelper.setOnce(this, param1Disposable);
    }
    
    public void onSuccess(T param1T) {
      this.parent.otherSuccess(param1T);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableMergeWithSingle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */