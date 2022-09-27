package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.exceptions.MissingBackpressureException;
import io.reactivex.functions.Function;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.fuseable.QueueSubscription;
import io.reactivex.internal.fuseable.SimpleQueue;
import io.reactivex.internal.queue.SpscArrayQueue;
import io.reactivex.internal.subscriptions.BasicIntQueueSubscription;
import io.reactivex.internal.subscriptions.EmptySubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.internal.util.ExceptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableFlattenIterable<T, R> extends AbstractFlowableWithUpstream<T, R> {
  final Function<? super T, ? extends Iterable<? extends R>> mapper;
  
  final int prefetch;
  
  public FlowableFlattenIterable(Flowable<T> paramFlowable, Function<? super T, ? extends Iterable<? extends R>> paramFunction, int paramInt) {
    super(paramFlowable);
    this.mapper = paramFunction;
    this.prefetch = paramInt;
  }
  
  public void subscribeActual(Subscriber<? super R> paramSubscriber) {
    if (this.source instanceof java.util.concurrent.Callable)
      try {
      
      } finally {
        Exception exception = null;
        Exceptions.throwIfFatal(exception);
        EmptySubscription.error(exception, paramSubscriber);
      }  
    this.source.subscribe(new FlattenIterableSubscriber<T, R>(paramSubscriber, this.mapper, this.prefetch));
  }
  
  static final class FlattenIterableSubscriber<T, R> extends BasicIntQueueSubscription<R> implements FlowableSubscriber<T> {
    private static final long serialVersionUID = -3096000382929934955L;
    
    volatile boolean cancelled;
    
    int consumed;
    
    Iterator<? extends R> current;
    
    volatile boolean done;
    
    final Subscriber<? super R> downstream;
    
    final AtomicReference<Throwable> error;
    
    int fusionMode;
    
    final int limit;
    
    final Function<? super T, ? extends Iterable<? extends R>> mapper;
    
    final int prefetch;
    
    SimpleQueue<T> queue;
    
    final AtomicLong requested;
    
    Subscription upstream;
    
    FlattenIterableSubscriber(Subscriber<? super R> param1Subscriber, Function<? super T, ? extends Iterable<? extends R>> param1Function, int param1Int) {
      this.downstream = param1Subscriber;
      this.mapper = param1Function;
      this.prefetch = param1Int;
      this.limit = param1Int - (param1Int >> 2);
      this.error = new AtomicReference<Throwable>();
      this.requested = new AtomicLong();
    }
    
    public void cancel() {
      if (!this.cancelled) {
        this.cancelled = true;
        this.upstream.cancel();
        if (getAndIncrement() == 0)
          this.queue.clear(); 
      } 
    }
    
    boolean checkTerminated(boolean param1Boolean1, boolean param1Boolean2, Subscriber<?> param1Subscriber, SimpleQueue<?> param1SimpleQueue) {
      if (this.cancelled) {
        this.current = null;
        param1SimpleQueue.clear();
        return true;
      } 
      if (param1Boolean1) {
        if ((Throwable)this.error.get() != null) {
          Throwable throwable = ExceptionHelper.terminate(this.error);
          this.current = null;
          param1SimpleQueue.clear();
          param1Subscriber.onError(throwable);
          return true;
        } 
        if (param1Boolean2) {
          param1Subscriber.onComplete();
          return true;
        } 
      } 
      return false;
    }
    
    public void clear() {
      this.current = null;
      this.queue.clear();
    }
    
    void consumedOne(boolean param1Boolean) {
      if (param1Boolean) {
        int i = this.consumed + 1;
        if (i == this.limit) {
          this.consumed = 0;
          this.upstream.request(i);
        } else {
          this.consumed = i;
        } 
      } 
    }
    
    void drain() {
      boolean bool;
      if (getAndIncrement() != 0)
        return; 
      Subscriber<? super R> subscriber = this.downstream;
      SimpleQueue<T> simpleQueue = this.queue;
      if (this.fusionMode != 1) {
        bool = true;
      } else {
        bool = false;
      } 
      Iterator<? extends R> iterator = this.current;
      int i = 1;
      while (true) {
        Throwable throwable;
        Iterator<? extends R> iterator1 = iterator;
        if (iterator == null) {
          boolean bool1 = this.done;
          try {
          
          } finally {
            iterator1 = null;
            Exceptions.throwIfFatal((Throwable)iterator1);
            this.upstream.cancel();
            ExceptionHelper.addThrowable(this.error, (Throwable)iterator1);
            throwable = ExceptionHelper.terminate(this.error);
            this.current = null;
            simpleQueue.clear();
            subscriber.onError(throwable);
          } 
        } 
        Object object2 = throwable;
        if (throwable != null) {
          Object object;
          long l3;
          long l1 = this.requested.get();
          long l2 = 0L;
          while (true) {
            object = throwable;
            l3 = l2;
            if (l2 != l1) {
              if (checkTerminated(this.done, false, subscriber, simpleQueue))
                return; 
              try {
              
              } finally {
                throwable = null;
                Exceptions.throwIfFatal(throwable);
                this.current = null;
                this.upstream.cancel();
                ExceptionHelper.addThrowable(this.error, throwable);
                subscriber.onError(ExceptionHelper.terminate(this.error));
              } 
              continue;
            } 
            break;
          } 
          if (l3 == l1) {
            boolean bool2;
            boolean bool1 = this.done;
            if (simpleQueue.isEmpty() && object == null) {
              bool2 = true;
            } else {
              bool2 = false;
            } 
            if (checkTerminated(bool1, bool2, subscriber, simpleQueue))
              return; 
          } 
          if (l3 != 0L && l1 != Long.MAX_VALUE)
            this.requested.addAndGet(-l3); 
          object2 = object;
          if (object == null)
            continue; 
        } 
        int j = addAndGet(-i);
        Object object1 = object2;
        i = j;
        if (j == 0)
          break; 
      } 
    }
    
    public boolean isEmpty() {
      boolean bool;
      if (this.current == null && this.queue.isEmpty()) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public void onComplete() {
      if (this.done)
        return; 
      this.done = true;
      drain();
    }
    
    public void onError(Throwable param1Throwable) {
      if (!this.done && ExceptionHelper.addThrowable(this.error, param1Throwable)) {
        this.done = true;
        drain();
      } else {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
    
    public void onNext(T param1T) {
      if (this.done)
        return; 
      if (this.fusionMode == 0 && !this.queue.offer(param1T)) {
        onError((Throwable)new MissingBackpressureException("Queue is full?!"));
        return;
      } 
      drain();
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      if (SubscriptionHelper.validate(this.upstream, param1Subscription)) {
        this.upstream = param1Subscription;
        if (param1Subscription instanceof QueueSubscription) {
          QueueSubscription queueSubscription = (QueueSubscription)param1Subscription;
          int i = queueSubscription.requestFusion(3);
          if (i == 1) {
            this.fusionMode = i;
            this.queue = (SimpleQueue<T>)queueSubscription;
            this.done = true;
            this.downstream.onSubscribe((Subscription)this);
            return;
          } 
          if (i == 2) {
            this.fusionMode = i;
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
    
    public R poll() throws Exception {
      Iterator<? extends R> iterator2;
      Iterator<? extends R> iterator1 = this.current;
      while (true) {
        iterator2 = iterator1;
        if (iterator1 == null) {
          Object object1 = this.queue.poll();
          if (object1 == null)
            return null; 
          iterator2 = ((Iterable<? extends R>)this.mapper.apply(object1)).iterator();
          if (!iterator2.hasNext()) {
            object1 = null;
            continue;
          } 
          this.current = iterator2;
        } 
        break;
      } 
      Object object = ObjectHelper.requireNonNull(iterator2.next(), "The iterator returned a null value");
      if (!iterator2.hasNext())
        this.current = null; 
      return (R)object;
    }
    
    public void request(long param1Long) {
      if (SubscriptionHelper.validate(param1Long)) {
        BackpressureHelper.add(this.requested, param1Long);
        drain();
      } 
    }
    
    public int requestFusion(int param1Int) {
      return ((param1Int & 0x1) != 0 && this.fusionMode == 1) ? 1 : 0;
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableFlattenIterable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */