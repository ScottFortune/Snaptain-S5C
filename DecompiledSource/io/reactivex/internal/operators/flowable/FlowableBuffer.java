package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.BooleanSupplier;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.internal.util.QueueDrainHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableBuffer<T, C extends Collection<? super T>> extends AbstractFlowableWithUpstream<T, C> {
  final Callable<C> bufferSupplier;
  
  final int size;
  
  final int skip;
  
  public FlowableBuffer(Flowable<T> paramFlowable, int paramInt1, int paramInt2, Callable<C> paramCallable) {
    super(paramFlowable);
    this.size = paramInt1;
    this.skip = paramInt2;
    this.bufferSupplier = paramCallable;
  }
  
  public void subscribeActual(Subscriber<? super C> paramSubscriber) {
    int i = this.size;
    int j = this.skip;
    if (i == j) {
      this.source.subscribe(new PublisherBufferExactSubscriber<Object, C>(paramSubscriber, this.size, this.bufferSupplier));
    } else if (j > i) {
      this.source.subscribe(new PublisherBufferSkipSubscriber<Object, C>(paramSubscriber, this.size, this.skip, this.bufferSupplier));
    } else {
      this.source.subscribe(new PublisherBufferOverlappingSubscriber<Object, C>(paramSubscriber, this.size, this.skip, this.bufferSupplier));
    } 
  }
  
  static final class PublisherBufferExactSubscriber<T, C extends Collection<? super T>> implements FlowableSubscriber<T>, Subscription {
    C buffer;
    
    final Callable<C> bufferSupplier;
    
    boolean done;
    
    final Subscriber<? super C> downstream;
    
    int index;
    
    final int size;
    
    Subscription upstream;
    
    PublisherBufferExactSubscriber(Subscriber<? super C> param1Subscriber, int param1Int, Callable<C> param1Callable) {
      this.downstream = param1Subscriber;
      this.size = param1Int;
      this.bufferSupplier = param1Callable;
    }
    
    public void cancel() {
      this.upstream.cancel();
    }
    
    public void onComplete() {
      if (this.done)
        return; 
      this.done = true;
      C c = this.buffer;
      if (c != null && !c.isEmpty())
        this.downstream.onNext(c); 
      this.downstream.onComplete();
    }
    
    public void onError(Throwable param1Throwable) {
      if (this.done) {
        RxJavaPlugins.onError(param1Throwable);
        return;
      } 
      this.done = true;
      this.downstream.onError(param1Throwable);
    }
    
    public void onNext(T param1T) {
      Collection<T> collection;
      if (this.done)
        return; 
      C c1 = this.buffer;
      C c2 = c1;
      if (c1 == null)
        try {
        
        } finally {
          param1T = null;
          Exceptions.throwIfFatal((Throwable)param1T);
          cancel();
          onError((Throwable)param1T);
        }  
      collection.add(param1T);
      int i = this.index + 1;
      if (i == this.size) {
        this.index = 0;
        this.buffer = null;
        this.downstream.onNext(collection);
      } else {
        this.index = i;
      } 
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      if (SubscriptionHelper.validate(this.upstream, param1Subscription)) {
        this.upstream = param1Subscription;
        this.downstream.onSubscribe(this);
      } 
    }
    
    public void request(long param1Long) {
      if (SubscriptionHelper.validate(param1Long))
        this.upstream.request(BackpressureHelper.multiplyCap(param1Long, this.size)); 
    }
  }
  
  static final class PublisherBufferOverlappingSubscriber<T, C extends Collection<? super T>> extends AtomicLong implements FlowableSubscriber<T>, Subscription, BooleanSupplier {
    private static final long serialVersionUID = -7370244972039324525L;
    
    final Callable<C> bufferSupplier;
    
    final ArrayDeque<C> buffers;
    
    volatile boolean cancelled;
    
    boolean done;
    
    final Subscriber<? super C> downstream;
    
    int index;
    
    final AtomicBoolean once;
    
    long produced;
    
    final int size;
    
    final int skip;
    
    Subscription upstream;
    
    PublisherBufferOverlappingSubscriber(Subscriber<? super C> param1Subscriber, int param1Int1, int param1Int2, Callable<C> param1Callable) {
      this.downstream = param1Subscriber;
      this.size = param1Int1;
      this.skip = param1Int2;
      this.bufferSupplier = param1Callable;
      this.once = new AtomicBoolean();
      this.buffers = new ArrayDeque<C>();
    }
    
    public void cancel() {
      this.cancelled = true;
      this.upstream.cancel();
    }
    
    public boolean getAsBoolean() {
      return this.cancelled;
    }
    
    public void onComplete() {
      if (this.done)
        return; 
      this.done = true;
      long l = this.produced;
      if (l != 0L)
        BackpressureHelper.produced(this, l); 
      QueueDrainHelper.postComplete(this.downstream, this.buffers, this, this);
    }
    
    public void onError(Throwable param1Throwable) {
      if (this.done) {
        RxJavaPlugins.onError(param1Throwable);
        return;
      } 
      this.done = true;
      this.buffers.clear();
      this.downstream.onError(param1Throwable);
    }
    
    public void onNext(T param1T) {
      if (this.done)
        return; 
      ArrayDeque<C> arrayDeque = this.buffers;
      int i = this.index;
      int j = i + 1;
      if (i == 0)
        try {
        
        } finally {
          param1T = null;
          Exceptions.throwIfFatal((Throwable)param1T);
          cancel();
          onError((Throwable)param1T);
        }  
      Collection<T> collection = (Collection)arrayDeque.peek();
      if (collection != null && collection.size() + 1 == this.size) {
        arrayDeque.poll();
        collection.add(param1T);
        this.produced++;
        this.downstream.onNext(collection);
      } 
      Iterator<C> iterator = arrayDeque.iterator();
      while (iterator.hasNext())
        ((Collection<T>)iterator.next()).add(param1T); 
      i = j;
      if (j == this.skip)
        i = 0; 
      this.index = i;
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      if (SubscriptionHelper.validate(this.upstream, param1Subscription)) {
        this.upstream = param1Subscription;
        this.downstream.onSubscribe(this);
      } 
    }
    
    public void request(long param1Long) {
      if (SubscriptionHelper.validate(param1Long)) {
        if (QueueDrainHelper.postCompleteRequest(param1Long, this.downstream, this.buffers, this, this))
          return; 
        if (!this.once.get() && this.once.compareAndSet(false, true)) {
          param1Long = BackpressureHelper.multiplyCap(this.skip, param1Long - 1L);
          param1Long = BackpressureHelper.addCap(this.size, param1Long);
          this.upstream.request(param1Long);
        } else {
          param1Long = BackpressureHelper.multiplyCap(this.skip, param1Long);
          this.upstream.request(param1Long);
        } 
      } 
    }
  }
  
  static final class PublisherBufferSkipSubscriber<T, C extends Collection<? super T>> extends AtomicInteger implements FlowableSubscriber<T>, Subscription {
    private static final long serialVersionUID = -5616169793639412593L;
    
    C buffer;
    
    final Callable<C> bufferSupplier;
    
    boolean done;
    
    final Subscriber<? super C> downstream;
    
    int index;
    
    final int size;
    
    final int skip;
    
    Subscription upstream;
    
    PublisherBufferSkipSubscriber(Subscriber<? super C> param1Subscriber, int param1Int1, int param1Int2, Callable<C> param1Callable) {
      this.downstream = param1Subscriber;
      this.size = param1Int1;
      this.skip = param1Int2;
      this.bufferSupplier = param1Callable;
    }
    
    public void cancel() {
      this.upstream.cancel();
    }
    
    public void onComplete() {
      if (this.done)
        return; 
      this.done = true;
      C c = this.buffer;
      this.buffer = null;
      if (c != null)
        this.downstream.onNext(c); 
      this.downstream.onComplete();
    }
    
    public void onError(Throwable param1Throwable) {
      if (this.done) {
        RxJavaPlugins.onError(param1Throwable);
        return;
      } 
      this.done = true;
      this.buffer = null;
      this.downstream.onError(param1Throwable);
    }
    
    public void onNext(T param1T) {
      Collection<T> collection;
      if (this.done)
        return; 
      C c = this.buffer;
      int i = this.index;
      int j = i + 1;
      if (i == 0)
        try {
        
        } finally {
          param1T = null;
          Exceptions.throwIfFatal((Throwable)param1T);
          cancel();
          onError((Throwable)param1T);
        }  
      if (collection != null) {
        collection.add(param1T);
        if (collection.size() == this.size) {
          this.buffer = null;
          this.downstream.onNext(collection);
        } 
      } 
      i = j;
      if (j == this.skip)
        i = 0; 
      this.index = i;
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      if (SubscriptionHelper.validate(this.upstream, param1Subscription)) {
        this.upstream = param1Subscription;
        this.downstream.onSubscribe(this);
      } 
    }
    
    public void request(long param1Long) {
      if (SubscriptionHelper.validate(param1Long))
        if (get() == 0 && compareAndSet(0, 1)) {
          long l = BackpressureHelper.multiplyCap(param1Long, this.size);
          param1Long = BackpressureHelper.multiplyCap((this.skip - this.size), param1Long - 1L);
          this.upstream.request(BackpressureHelper.addCap(l, param1Long));
        } else {
          this.upstream.request(BackpressureHelper.multiplyCap(this.skip, param1Long));
        }  
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableBuffer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */