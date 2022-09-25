package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.fuseable.FuseToFlowable;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import org.reactivestreams.Subscription;

public final class FlowableCountSingle<T> extends Single<Long> implements FuseToFlowable<Long> {
  final Flowable<T> source;
  
  public FlowableCountSingle(Flowable<T> paramFlowable) {
    this.source = paramFlowable;
  }
  
  public Flowable<Long> fuseToFlowable() {
    return RxJavaPlugins.onAssembly(new FlowableCount<T>(this.source));
  }
  
  protected void subscribeActual(SingleObserver<? super Long> paramSingleObserver) {
    this.source.subscribe(new CountSubscriber(paramSingleObserver));
  }
  
  static final class CountSubscriber implements FlowableSubscriber<Object>, Disposable {
    long count;
    
    final SingleObserver<? super Long> downstream;
    
    Subscription upstream;
    
    CountSubscriber(SingleObserver<? super Long> param1SingleObserver) {
      this.downstream = param1SingleObserver;
    }
    
    public void dispose() {
      this.upstream.cancel();
      this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
    }
    
    public boolean isDisposed() {
      boolean bool;
      if (this.upstream == SubscriptionHelper.CANCELLED) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public void onComplete() {
      this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
      this.downstream.onSuccess(Long.valueOf(this.count));
    }
    
    public void onError(Throwable param1Throwable) {
      this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
      this.downstream.onError(param1Throwable);
    }
    
    public void onNext(Object param1Object) {
      this.count++;
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      if (SubscriptionHelper.validate(this.upstream, param1Subscription)) {
        this.upstream = param1Subscription;
        this.downstream.onSubscribe(this);
        param1Subscription.request(Long.MAX_VALUE);
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableCountSingle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */