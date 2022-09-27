package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.exceptions.MissingBackpressureException;
import io.reactivex.functions.BiPredicate;
import io.reactivex.internal.fuseable.QueueSubscription;
import io.reactivex.internal.fuseable.SimpleQueue;
import io.reactivex.internal.queue.SpscArrayQueue;
import io.reactivex.internal.subscriptions.DeferredScalarSubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.AtomicThrowable;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableSequenceEqual<T> extends Flowable<Boolean> {
  final BiPredicate<? super T, ? super T> comparer;
  
  final Publisher<? extends T> first;
  
  final int prefetch;
  
  final Publisher<? extends T> second;
  
  public FlowableSequenceEqual(Publisher<? extends T> paramPublisher1, Publisher<? extends T> paramPublisher2, BiPredicate<? super T, ? super T> paramBiPredicate, int paramInt) {
    this.first = paramPublisher1;
    this.second = paramPublisher2;
    this.comparer = paramBiPredicate;
    this.prefetch = paramInt;
  }
  
  public void subscribeActual(Subscriber<? super Boolean> paramSubscriber) {
    EqualCoordinator<T> equalCoordinator = new EqualCoordinator<T>(paramSubscriber, this.prefetch, this.comparer);
    paramSubscriber.onSubscribe((Subscription)equalCoordinator);
    equalCoordinator.subscribe(this.first, this.second);
  }
  
  static final class EqualCoordinator<T> extends DeferredScalarSubscription<Boolean> implements EqualCoordinatorHelper {
    private static final long serialVersionUID = -6178010334400373240L;
    
    final BiPredicate<? super T, ? super T> comparer;
    
    final AtomicThrowable error;
    
    final FlowableSequenceEqual.EqualSubscriber<T> first;
    
    final FlowableSequenceEqual.EqualSubscriber<T> second;
    
    T v1;
    
    T v2;
    
    final AtomicInteger wip;
    
    EqualCoordinator(Subscriber<? super Boolean> param1Subscriber, int param1Int, BiPredicate<? super T, ? super T> param1BiPredicate) {
      super(param1Subscriber);
      this.comparer = param1BiPredicate;
      this.wip = new AtomicInteger();
      this.first = new FlowableSequenceEqual.EqualSubscriber<T>(this, param1Int);
      this.second = new FlowableSequenceEqual.EqualSubscriber<T>(this, param1Int);
      this.error = new AtomicThrowable();
    }
    
    public void cancel() {
      super.cancel();
      this.first.cancel();
      this.second.cancel();
      if (this.wip.getAndIncrement() == 0) {
        this.first.clear();
        this.second.clear();
      } 
    }
    
    void cancelAndClear() {
      this.first.cancel();
      this.first.clear();
      this.second.cancel();
      this.second.clear();
    }
    
    public void drain() {
      int j;
      if (this.wip.getAndIncrement() != 0)
        return; 
      int i = 1;
      do {
        SimpleQueue<T> simpleQueue1 = this.first.queue;
        SimpleQueue<T> simpleQueue2 = this.second.queue;
        if (simpleQueue1 != null && simpleQueue2 != null) {
          while (true) {
            boolean bool2;
            boolean bool4;
            if (isCancelled()) {
              this.first.clear();
              this.second.clear();
              return;
            } 
            if ((Throwable)this.error.get() != null) {
              cancelAndClear();
              this.downstream.onError(this.error.terminate());
              return;
            } 
            boolean bool1 = this.first.done;
            T t1 = this.v1;
            T t2 = t1;
            if (t1 == null)
              try {
              
              } finally {
                t2 = null;
                Exceptions.throwIfFatal((Throwable)t2);
                cancelAndClear();
                this.error.addThrowable((Throwable)t2);
                this.downstream.onError(this.error.terminate());
              }  
            if (t2 == null) {
              bool2 = true;
            } else {
              bool2 = false;
            } 
            boolean bool3 = this.second.done;
            T t3 = this.v2;
            t1 = t3;
            if (t3 == null)
              try {
              
              } finally {
                t2 = null;
                Exceptions.throwIfFatal((Throwable)t2);
                cancelAndClear();
                this.error.addThrowable((Throwable)t2);
                this.downstream.onError(this.error.terminate());
              }  
            if (t1 == null) {
              bool4 = true;
            } else {
              bool4 = false;
            } 
            if (bool1 && bool3 && bool2 && bool4) {
              complete(Boolean.valueOf(true));
              return;
            } 
            if (bool1 && bool3 && bool2 != bool4) {
              cancelAndClear();
              complete(Boolean.valueOf(false));
              return;
            } 
            if (bool2 || bool4)
              break; 
            try {
              bool1 = this.comparer.test(t2, t1);
            } finally {
              t2 = null;
              Exceptions.throwIfFatal((Throwable)t2);
              cancelAndClear();
              this.error.addThrowable((Throwable)t2);
              this.downstream.onError(this.error.terminate());
            } 
          } 
        } else {
          if (isCancelled()) {
            this.first.clear();
            this.second.clear();
            return;
          } 
          if ((Throwable)this.error.get() != null) {
            cancelAndClear();
            this.downstream.onError(this.error.terminate());
            return;
          } 
        } 
        j = this.wip.addAndGet(-i);
        i = j;
      } while (j != 0);
    }
    
    public void innerError(Throwable param1Throwable) {
      if (this.error.addThrowable(param1Throwable)) {
        drain();
      } else {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
    
    void subscribe(Publisher<? extends T> param1Publisher1, Publisher<? extends T> param1Publisher2) {
      param1Publisher1.subscribe((Subscriber)this.first);
      param1Publisher2.subscribe((Subscriber)this.second);
    }
  }
  
  static interface EqualCoordinatorHelper {
    void drain();
    
    void innerError(Throwable param1Throwable);
  }
  
  static final class EqualSubscriber<T> extends AtomicReference<Subscription> implements FlowableSubscriber<T> {
    private static final long serialVersionUID = 4804128302091633067L;
    
    volatile boolean done;
    
    final int limit;
    
    final FlowableSequenceEqual.EqualCoordinatorHelper parent;
    
    final int prefetch;
    
    long produced;
    
    volatile SimpleQueue<T> queue;
    
    int sourceMode;
    
    EqualSubscriber(FlowableSequenceEqual.EqualCoordinatorHelper param1EqualCoordinatorHelper, int param1Int) {
      this.parent = param1EqualCoordinatorHelper;
      this.limit = param1Int - (param1Int >> 2);
      this.prefetch = param1Int;
    }
    
    public void cancel() {
      SubscriptionHelper.cancel(this);
    }
    
    void clear() {
      SimpleQueue<T> simpleQueue = this.queue;
      if (simpleQueue != null)
        simpleQueue.clear(); 
    }
    
    public void onComplete() {
      this.done = true;
      this.parent.drain();
    }
    
    public void onError(Throwable param1Throwable) {
      this.parent.innerError(param1Throwable);
    }
    
    public void onNext(T param1T) {
      if (this.sourceMode == 0 && !this.queue.offer(param1T)) {
        onError((Throwable)new MissingBackpressureException());
        return;
      } 
      this.parent.drain();
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      if (SubscriptionHelper.setOnce(this, param1Subscription)) {
        if (param1Subscription instanceof QueueSubscription) {
          QueueSubscription queueSubscription = (QueueSubscription)param1Subscription;
          int i = queueSubscription.requestFusion(3);
          if (i == 1) {
            this.sourceMode = i;
            this.queue = (SimpleQueue<T>)queueSubscription;
            this.done = true;
            this.parent.drain();
            return;
          } 
          if (i == 2) {
            this.sourceMode = i;
            this.queue = (SimpleQueue<T>)queueSubscription;
            param1Subscription.request(this.prefetch);
            return;
          } 
        } 
        this.queue = (SimpleQueue<T>)new SpscArrayQueue(this.prefetch);
        param1Subscription.request(this.prefetch);
      } 
    }
    
    public void request() {
      if (this.sourceMode != 1) {
        long l = this.produced + 1L;
        if (l >= this.limit) {
          this.produced = 0L;
          get().request(l);
        } else {
          this.produced = l;
        } 
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableSequenceEqual.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */