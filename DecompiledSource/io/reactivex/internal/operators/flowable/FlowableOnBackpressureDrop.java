package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicLong;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableOnBackpressureDrop<T> extends AbstractFlowableWithUpstream<T, T> implements Consumer<T> {
  final Consumer<? super T> onDrop = this;
  
  public FlowableOnBackpressureDrop(Flowable<T> paramFlowable) {
    super(paramFlowable);
  }
  
  public FlowableOnBackpressureDrop(Flowable<T> paramFlowable, Consumer<? super T> paramConsumer) {
    super(paramFlowable);
  }
  
  public void accept(T paramT) {}
  
  protected void subscribeActual(Subscriber<? super T> paramSubscriber) {
    this.source.subscribe(new BackpressureDropSubscriber<T>(paramSubscriber, this.onDrop));
  }
  
  static final class BackpressureDropSubscriber<T> extends AtomicLong implements FlowableSubscriber<T>, Subscription {
    private static final long serialVersionUID = -6246093802440953054L;
    
    boolean done;
    
    final Subscriber<? super T> downstream;
    
    final Consumer<? super T> onDrop;
    
    Subscription upstream;
    
    BackpressureDropSubscriber(Subscriber<? super T> param1Subscriber, Consumer<? super T> param1Consumer) {
      this.downstream = param1Subscriber;
      this.onDrop = param1Consumer;
    }
    
    public void cancel() {
      this.upstream.cancel();
    }
    
    public void onComplete() {
      if (this.done)
        return; 
      this.done = true;
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
      if (this.done)
        return; 
      if (get() != 0L) {
        this.downstream.onNext(param1T);
        BackpressureHelper.produced(this, 1L);
      } else {
        try {
          this.onDrop.accept(param1T);
        } finally {
          param1T = null;
          Exceptions.throwIfFatal((Throwable)param1T);
          cancel();
        } 
      } 
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      if (SubscriptionHelper.validate(this.upstream, param1Subscription)) {
        this.upstream = param1Subscription;
        this.downstream.onSubscribe(this);
        param1Subscription.request(Long.MAX_VALUE);
      } 
    }
    
    public void request(long param1Long) {
      if (SubscriptionHelper.validate(param1Long))
        BackpressureHelper.add(this, param1Long); 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableOnBackpressureDrop.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */