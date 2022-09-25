package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.subscriptions.EmptySubscription;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.UnicastProcessor;
import io.reactivex.subscribers.SerializedSubscriber;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableRetryWhen<T> extends AbstractFlowableWithUpstream<T, T> {
  final Function<? super Flowable<Throwable>, ? extends Publisher<?>> handler;
  
  public FlowableRetryWhen(Flowable<T> paramFlowable, Function<? super Flowable<Throwable>, ? extends Publisher<?>> paramFunction) {
    super(paramFlowable);
    this.handler = paramFunction;
  }
  
  public void subscribeActual(Subscriber<? super T> paramSubscriber) {
    SerializedSubscriber serializedSubscriber = new SerializedSubscriber(paramSubscriber);
    FlowableProcessor<Throwable> flowableProcessor = UnicastProcessor.create(8).toSerialized();
    try {
      Publisher publisher = (Publisher)ObjectHelper.requireNonNull(this.handler.apply(flowableProcessor), "handler returned a null Publisher");
      FlowableRepeatWhen.WhenReceiver<T, Object> whenReceiver = new FlowableRepeatWhen.WhenReceiver<T, Object>((Publisher<T>)this.source);
      RetryWhenSubscriber<T, Object> retryWhenSubscriber = new RetryWhenSubscriber((Subscriber<?>)serializedSubscriber, flowableProcessor, whenReceiver);
      whenReceiver.subscriber = (FlowableRepeatWhen.WhenSourceSubscriber<T, Object>)retryWhenSubscriber;
      return;
    } finally {
      Exception exception = null;
      Exceptions.throwIfFatal(exception);
      EmptySubscription.error(exception, paramSubscriber);
    } 
  }
  
  static final class RetryWhenSubscriber<T> extends FlowableRepeatWhen.WhenSourceSubscriber<T, Throwable> {
    private static final long serialVersionUID = -2680129890138081029L;
    
    RetryWhenSubscriber(Subscriber<? super T> param1Subscriber, FlowableProcessor<Throwable> param1FlowableProcessor, Subscription param1Subscription) {
      super(param1Subscriber, param1FlowableProcessor, param1Subscription);
    }
    
    public void onComplete() {
      this.receiver.cancel();
      this.downstream.onComplete();
    }
    
    public void onError(Throwable param1Throwable) {
      again(param1Throwable);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableRetryWhen.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */