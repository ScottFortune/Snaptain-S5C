package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.subscriptions.EmptySubscription;
import io.reactivex.internal.subscriptions.SubscriptionArbiter;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.UnicastProcessor;
import io.reactivex.subscribers.SerializedSubscriber;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableRepeatWhen<T> extends AbstractFlowableWithUpstream<T, T> {
  final Function<? super Flowable<Object>, ? extends Publisher<?>> handler;
  
  public FlowableRepeatWhen(Flowable<T> paramFlowable, Function<? super Flowable<Object>, ? extends Publisher<?>> paramFunction) {
    super(paramFlowable);
    this.handler = paramFunction;
  }
  
  public void subscribeActual(Subscriber<? super T> paramSubscriber) {
    SerializedSubscriber serializedSubscriber = new SerializedSubscriber(paramSubscriber);
    FlowableProcessor flowableProcessor = UnicastProcessor.create(8).toSerialized();
    try {
      Publisher publisher = (Publisher)ObjectHelper.requireNonNull(this.handler.apply(flowableProcessor), "handler returned a null Publisher");
      WhenReceiver<T, Object> whenReceiver = new WhenReceiver<T, Object>((Publisher<T>)this.source);
      RepeatWhenSubscriber<T, Object> repeatWhenSubscriber = new RepeatWhenSubscriber((Subscriber<?>)serializedSubscriber, flowableProcessor, whenReceiver);
      whenReceiver.subscriber = repeatWhenSubscriber;
      return;
    } finally {
      Exception exception = null;
      Exceptions.throwIfFatal(exception);
      EmptySubscription.error(exception, paramSubscriber);
    } 
  }
  
  static final class RepeatWhenSubscriber<T> extends WhenSourceSubscriber<T, Object> {
    private static final long serialVersionUID = -2680129890138081029L;
    
    RepeatWhenSubscriber(Subscriber<? super T> param1Subscriber, FlowableProcessor<Object> param1FlowableProcessor, Subscription param1Subscription) {
      super(param1Subscriber, param1FlowableProcessor, param1Subscription);
    }
    
    public void onComplete() {
      again(Integer.valueOf(0));
    }
    
    public void onError(Throwable param1Throwable) {
      this.receiver.cancel();
      this.downstream.onError(param1Throwable);
    }
  }
  
  static final class WhenReceiver<T, U> extends AtomicInteger implements FlowableSubscriber<Object>, Subscription {
    private static final long serialVersionUID = 2827772011130406689L;
    
    final AtomicLong requested;
    
    final Publisher<T> source;
    
    FlowableRepeatWhen.WhenSourceSubscriber<T, U> subscriber;
    
    final AtomicReference<Subscription> upstream;
    
    WhenReceiver(Publisher<T> param1Publisher) {
      this.source = param1Publisher;
      this.upstream = new AtomicReference<Subscription>();
      this.requested = new AtomicLong();
    }
    
    public void cancel() {
      SubscriptionHelper.cancel(this.upstream);
    }
    
    public void onComplete() {
      this.subscriber.cancel();
      this.subscriber.downstream.onComplete();
    }
    
    public void onError(Throwable param1Throwable) {
      this.subscriber.cancel();
      this.subscriber.downstream.onError(param1Throwable);
    }
    
    public void onNext(Object param1Object) {
      if (getAndIncrement() == 0)
        do {
          if (this.upstream.get() == SubscriptionHelper.CANCELLED)
            return; 
          this.source.subscribe((Subscriber)this.subscriber);
        } while (decrementAndGet() != 0); 
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      SubscriptionHelper.deferredSetOnce(this.upstream, this.requested, param1Subscription);
    }
    
    public void request(long param1Long) {
      SubscriptionHelper.deferredRequest(this.upstream, this.requested, param1Long);
    }
  }
  
  static abstract class WhenSourceSubscriber<T, U> extends SubscriptionArbiter implements FlowableSubscriber<T> {
    private static final long serialVersionUID = -5604623027276966720L;
    
    protected final Subscriber<? super T> downstream;
    
    protected final FlowableProcessor<U> processor;
    
    private long produced;
    
    protected final Subscription receiver;
    
    WhenSourceSubscriber(Subscriber<? super T> param1Subscriber, FlowableProcessor<U> param1FlowableProcessor, Subscription param1Subscription) {
      super(false);
      this.downstream = param1Subscriber;
      this.processor = param1FlowableProcessor;
      this.receiver = param1Subscription;
    }
    
    protected final void again(U param1U) {
      setSubscription((Subscription)EmptySubscription.INSTANCE);
      long l = this.produced;
      if (l != 0L) {
        this.produced = 0L;
        produced(l);
      } 
      this.receiver.request(1L);
      this.processor.onNext(param1U);
    }
    
    public final void cancel() {
      super.cancel();
      this.receiver.cancel();
    }
    
    public final void onNext(T param1T) {
      this.produced++;
      this.downstream.onNext(param1T);
    }
    
    public final void onSubscribe(Subscription param1Subscription) {
      setSubscription(param1Subscription);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableRepeatWhen.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */