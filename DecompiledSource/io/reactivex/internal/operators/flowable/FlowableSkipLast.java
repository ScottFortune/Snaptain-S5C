package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import java.util.ArrayDeque;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableSkipLast<T> extends AbstractFlowableWithUpstream<T, T> {
  final int skip;
  
  public FlowableSkipLast(Flowable<T> paramFlowable, int paramInt) {
    super(paramFlowable);
    this.skip = paramInt;
  }
  
  protected void subscribeActual(Subscriber<? super T> paramSubscriber) {
    this.source.subscribe(new SkipLastSubscriber<T>(paramSubscriber, this.skip));
  }
  
  static final class SkipLastSubscriber<T> extends ArrayDeque<T> implements FlowableSubscriber<T>, Subscription {
    private static final long serialVersionUID = -3807491841935125653L;
    
    final Subscriber<? super T> downstream;
    
    final int skip;
    
    Subscription upstream;
    
    SkipLastSubscriber(Subscriber<? super T> param1Subscriber, int param1Int) {
      super(param1Int);
      this.downstream = param1Subscriber;
      this.skip = param1Int;
    }
    
    public void cancel() {
      this.upstream.cancel();
    }
    
    public void onComplete() {
      this.downstream.onComplete();
    }
    
    public void onError(Throwable param1Throwable) {
      this.downstream.onError(param1Throwable);
    }
    
    public void onNext(T param1T) {
      if (this.skip == size()) {
        this.downstream.onNext(poll());
      } else {
        this.upstream.request(1L);
      } 
      offer(param1T);
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      if (SubscriptionHelper.validate(this.upstream, param1Subscription)) {
        this.upstream = param1Subscription;
        this.downstream.onSubscribe(this);
      } 
    }
    
    public void request(long param1Long) {
      this.upstream.request(param1Long);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableSkipLast.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */