package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.subscriptions.SubscriptionArbiter;
import io.reactivex.plugins.RxJavaPlugins;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableOnErrorNext<T> extends AbstractFlowableWithUpstream<T, T> {
  final boolean allowFatal;
  
  final Function<? super Throwable, ? extends Publisher<? extends T>> nextSupplier;
  
  public FlowableOnErrorNext(Flowable<T> paramFlowable, Function<? super Throwable, ? extends Publisher<? extends T>> paramFunction, boolean paramBoolean) {
    super(paramFlowable);
    this.nextSupplier = paramFunction;
    this.allowFatal = paramBoolean;
  }
  
  protected void subscribeActual(Subscriber<? super T> paramSubscriber) {
    OnErrorNextSubscriber<T> onErrorNextSubscriber = new OnErrorNextSubscriber<T>(paramSubscriber, this.nextSupplier, this.allowFatal);
    paramSubscriber.onSubscribe((Subscription)onErrorNextSubscriber);
    this.source.subscribe(onErrorNextSubscriber);
  }
  
  static final class OnErrorNextSubscriber<T> extends SubscriptionArbiter implements FlowableSubscriber<T> {
    private static final long serialVersionUID = 4063763155303814625L;
    
    final boolean allowFatal;
    
    boolean done;
    
    final Subscriber<? super T> downstream;
    
    final Function<? super Throwable, ? extends Publisher<? extends T>> nextSupplier;
    
    boolean once;
    
    long produced;
    
    OnErrorNextSubscriber(Subscriber<? super T> param1Subscriber, Function<? super Throwable, ? extends Publisher<? extends T>> param1Function, boolean param1Boolean) {
      super(false);
      this.downstream = param1Subscriber;
      this.nextSupplier = param1Function;
      this.allowFatal = param1Boolean;
    }
    
    public void onComplete() {
      if (this.done)
        return; 
      this.done = true;
      this.once = true;
      this.downstream.onComplete();
    }
    
    public void onError(Throwable param1Throwable) {
      if (this.once) {
        if (this.done) {
          RxJavaPlugins.onError(param1Throwable);
          return;
        } 
        this.downstream.onError(param1Throwable);
        return;
      } 
      this.once = true;
      if (this.allowFatal && !(param1Throwable instanceof Exception)) {
        this.downstream.onError(param1Throwable);
        return;
      } 
      try {
        Publisher publisher = (Publisher)ObjectHelper.requireNonNull(this.nextSupplier.apply(param1Throwable), "The nextSupplier returned a null Publisher");
        return;
      } finally {
        Exception exception = null;
        Exceptions.throwIfFatal(exception);
        this.downstream.onError((Throwable)new CompositeException(new Throwable[] { param1Throwable, exception }));
      } 
    }
    
    public void onNext(T param1T) {
      if (this.done)
        return; 
      if (!this.once)
        this.produced++; 
      this.downstream.onNext(param1T);
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      setSubscription(param1Subscription);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableOnErrorNext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */