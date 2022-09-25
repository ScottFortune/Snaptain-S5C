package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.internal.subscriptions.DeferredScalarSubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.NoSuchElementException;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableSingle<T> extends AbstractFlowableWithUpstream<T, T> {
  final T defaultValue;
  
  final boolean failOnEmpty;
  
  public FlowableSingle(Flowable<T> paramFlowable, T paramT, boolean paramBoolean) {
    super(paramFlowable);
    this.defaultValue = paramT;
    this.failOnEmpty = paramBoolean;
  }
  
  protected void subscribeActual(Subscriber<? super T> paramSubscriber) {
    this.source.subscribe(new SingleElementSubscriber<T>(paramSubscriber, this.defaultValue, this.failOnEmpty));
  }
  
  static final class SingleElementSubscriber<T> extends DeferredScalarSubscription<T> implements FlowableSubscriber<T> {
    private static final long serialVersionUID = -5526049321428043809L;
    
    final T defaultValue;
    
    boolean done;
    
    final boolean failOnEmpty;
    
    Subscription upstream;
    
    SingleElementSubscriber(Subscriber<? super T> param1Subscriber, T param1T, boolean param1Boolean) {
      super(param1Subscriber);
      this.defaultValue = param1T;
      this.failOnEmpty = param1Boolean;
    }
    
    public void cancel() {
      super.cancel();
      this.upstream.cancel();
    }
    
    public void onComplete() {
      if (this.done)
        return; 
      this.done = true;
      Object object1 = this.value;
      this.value = null;
      Object object2 = object1;
      if (object1 == null)
        object2 = this.defaultValue; 
      if (object2 == null) {
        if (this.failOnEmpty) {
          this.downstream.onError(new NoSuchElementException());
        } else {
          this.downstream.onComplete();
        } 
      } else {
        complete(object2);
      } 
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
      if (this.done)
        return; 
      if (this.value != null) {
        this.done = true;
        this.upstream.cancel();
        this.downstream.onError(new IllegalArgumentException("Sequence contains more than one element!"));
        return;
      } 
      this.value = param1T;
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      if (SubscriptionHelper.validate(this.upstream, param1Subscription)) {
        this.upstream = param1Subscription;
        this.downstream.onSubscribe((Subscription)this);
        param1Subscription.request(Long.MAX_VALUE);
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableSingle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */