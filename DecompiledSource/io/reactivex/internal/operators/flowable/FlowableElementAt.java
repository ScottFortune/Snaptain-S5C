package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.internal.subscriptions.DeferredScalarSubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.NoSuchElementException;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableElementAt<T> extends AbstractFlowableWithUpstream<T, T> {
  final T defaultValue;
  
  final boolean errorOnFewer;
  
  final long index;
  
  public FlowableElementAt(Flowable<T> paramFlowable, long paramLong, T paramT, boolean paramBoolean) {
    super(paramFlowable);
    this.index = paramLong;
    this.defaultValue = paramT;
    this.errorOnFewer = paramBoolean;
  }
  
  protected void subscribeActual(Subscriber<? super T> paramSubscriber) {
    this.source.subscribe(new ElementAtSubscriber<T>(paramSubscriber, this.index, this.defaultValue, this.errorOnFewer));
  }
  
  static final class ElementAtSubscriber<T> extends DeferredScalarSubscription<T> implements FlowableSubscriber<T> {
    private static final long serialVersionUID = 4066607327284737757L;
    
    long count;
    
    final T defaultValue;
    
    boolean done;
    
    final boolean errorOnFewer;
    
    final long index;
    
    Subscription upstream;
    
    ElementAtSubscriber(Subscriber<? super T> param1Subscriber, long param1Long, T param1T, boolean param1Boolean) {
      super(param1Subscriber);
      this.index = param1Long;
      this.defaultValue = param1T;
      this.errorOnFewer = param1Boolean;
    }
    
    public void cancel() {
      super.cancel();
      this.upstream.cancel();
    }
    
    public void onComplete() {
      if (!this.done) {
        this.done = true;
        T t = this.defaultValue;
        if (t == null) {
          if (this.errorOnFewer) {
            this.downstream.onError(new NoSuchElementException());
          } else {
            this.downstream.onComplete();
          } 
        } else {
          complete(t);
        } 
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
      long l = this.count;
      if (l == this.index) {
        this.done = true;
        this.upstream.cancel();
        complete(param1T);
        return;
      } 
      this.count = l + 1L;
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


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableElementAt.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */