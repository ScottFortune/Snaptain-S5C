package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.exceptions.MissingBackpressureException;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.subscribers.SerializedSubscriber;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableSamplePublisher<T> extends Flowable<T> {
  final boolean emitLast;
  
  final Publisher<?> other;
  
  final Publisher<T> source;
  
  public FlowableSamplePublisher(Publisher<T> paramPublisher, Publisher<?> paramPublisher1, boolean paramBoolean) {
    this.source = paramPublisher;
    this.other = paramPublisher1;
    this.emitLast = paramBoolean;
  }
  
  protected void subscribeActual(Subscriber<? super T> paramSubscriber) {
    SerializedSubscriber serializedSubscriber = new SerializedSubscriber(paramSubscriber);
    if (this.emitLast) {
      this.source.subscribe((Subscriber)new SampleMainEmitLast((Subscriber<?>)serializedSubscriber, this.other));
    } else {
      this.source.subscribe((Subscriber)new SampleMainNoLast((Subscriber<?>)serializedSubscriber, this.other));
    } 
  }
  
  static final class SampleMainEmitLast<T> extends SamplePublisherSubscriber<T> {
    private static final long serialVersionUID = -3029755663834015785L;
    
    volatile boolean done;
    
    final AtomicInteger wip = new AtomicInteger();
    
    SampleMainEmitLast(Subscriber<? super T> param1Subscriber, Publisher<?> param1Publisher) {
      super(param1Subscriber, param1Publisher);
    }
    
    void completion() {
      this.done = true;
      if (this.wip.getAndIncrement() == 0) {
        emit();
        this.downstream.onComplete();
      } 
    }
    
    void run() {
      if (this.wip.getAndIncrement() == 0)
        do {
          boolean bool = this.done;
          emit();
          if (bool) {
            this.downstream.onComplete();
            return;
          } 
        } while (this.wip.decrementAndGet() != 0); 
    }
  }
  
  static final class SampleMainNoLast<T> extends SamplePublisherSubscriber<T> {
    private static final long serialVersionUID = -3029755663834015785L;
    
    SampleMainNoLast(Subscriber<? super T> param1Subscriber, Publisher<?> param1Publisher) {
      super(param1Subscriber, param1Publisher);
    }
    
    void completion() {
      this.downstream.onComplete();
    }
    
    void run() {
      emit();
    }
  }
  
  static abstract class SamplePublisherSubscriber<T> extends AtomicReference<T> implements FlowableSubscriber<T>, Subscription {
    private static final long serialVersionUID = -3517602651313910099L;
    
    final Subscriber<? super T> downstream;
    
    final AtomicReference<Subscription> other = new AtomicReference<Subscription>();
    
    final AtomicLong requested = new AtomicLong();
    
    final Publisher<?> sampler;
    
    Subscription upstream;
    
    SamplePublisherSubscriber(Subscriber<? super T> param1Subscriber, Publisher<?> param1Publisher) {
      this.downstream = param1Subscriber;
      this.sampler = param1Publisher;
    }
    
    public void cancel() {
      SubscriptionHelper.cancel(this.other);
      this.upstream.cancel();
    }
    
    public void complete() {
      this.upstream.cancel();
      completion();
    }
    
    abstract void completion();
    
    void emit() {
      T t = getAndSet(null);
      if (t != null)
        if (this.requested.get() != 0L) {
          this.downstream.onNext(t);
          BackpressureHelper.produced(this.requested, 1L);
        } else {
          cancel();
          this.downstream.onError((Throwable)new MissingBackpressureException("Couldn't emit value due to lack of requests!"));
        }  
    }
    
    public void error(Throwable param1Throwable) {
      this.upstream.cancel();
      this.downstream.onError(param1Throwable);
    }
    
    public void onComplete() {
      SubscriptionHelper.cancel(this.other);
      completion();
    }
    
    public void onError(Throwable param1Throwable) {
      SubscriptionHelper.cancel(this.other);
      this.downstream.onError(param1Throwable);
    }
    
    public void onNext(T param1T) {
      lazySet(param1T);
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      if (SubscriptionHelper.validate(this.upstream, param1Subscription)) {
        this.upstream = param1Subscription;
        this.downstream.onSubscribe(this);
        if (this.other.get() == null) {
          this.sampler.subscribe((Subscriber)new FlowableSamplePublisher.SamplerSubscriber(this));
          param1Subscription.request(Long.MAX_VALUE);
        } 
      } 
    }
    
    public void request(long param1Long) {
      if (SubscriptionHelper.validate(param1Long))
        BackpressureHelper.add(this.requested, param1Long); 
    }
    
    abstract void run();
    
    void setOther(Subscription param1Subscription) {
      SubscriptionHelper.setOnce(this.other, param1Subscription, Long.MAX_VALUE);
    }
  }
  
  static final class SamplerSubscriber<T> implements FlowableSubscriber<Object> {
    final FlowableSamplePublisher.SamplePublisherSubscriber<T> parent;
    
    SamplerSubscriber(FlowableSamplePublisher.SamplePublisherSubscriber<T> param1SamplePublisherSubscriber) {
      this.parent = param1SamplePublisherSubscriber;
    }
    
    public void onComplete() {
      this.parent.complete();
    }
    
    public void onError(Throwable param1Throwable) {
      this.parent.error(param1Throwable);
    }
    
    public void onNext(Object param1Object) {
      this.parent.run();
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      this.parent.setOther(param1Subscription);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableSamplePublisher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */