package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.fuseable.FuseToFlowable;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.NoSuchElementException;
import org.reactivestreams.Subscription;

public final class FlowableElementAtSingle<T> extends Single<T> implements FuseToFlowable<T> {
  final T defaultValue;
  
  final long index;
  
  final Flowable<T> source;
  
  public FlowableElementAtSingle(Flowable<T> paramFlowable, long paramLong, T paramT) {
    this.source = paramFlowable;
    this.index = paramLong;
    this.defaultValue = paramT;
  }
  
  public Flowable<T> fuseToFlowable() {
    return RxJavaPlugins.onAssembly(new FlowableElementAt<T>(this.source, this.index, this.defaultValue, true));
  }
  
  protected void subscribeActual(SingleObserver<? super T> paramSingleObserver) {
    this.source.subscribe(new ElementAtSubscriber<T>(paramSingleObserver, this.index, this.defaultValue));
  }
  
  static final class ElementAtSubscriber<T> implements FlowableSubscriber<T>, Disposable {
    long count;
    
    final T defaultValue;
    
    boolean done;
    
    final SingleObserver<? super T> downstream;
    
    final long index;
    
    Subscription upstream;
    
    ElementAtSubscriber(SingleObserver<? super T> param1SingleObserver, long param1Long, T param1T) {
      this.downstream = param1SingleObserver;
      this.index = param1Long;
      this.defaultValue = param1T;
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
      if (!this.done) {
        this.done = true;
        T t = this.defaultValue;
        if (t != null) {
          this.downstream.onSuccess(t);
        } else {
          this.downstream.onError(new NoSuchElementException());
        } 
      } 
    }
    
    public void onError(Throwable param1Throwable) {
      if (this.done) {
        RxJavaPlugins.onError(param1Throwable);
        return;
      } 
      this.done = true;
      this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
      this.downstream.onError(param1Throwable);
    }
    
    public void onNext(T param1T) {
      if (this.done)
        return; 
      long l = this.count;
      if (l == this.index) {
        this.done = true;
        this.upstream.cancel();
        this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
        this.downstream.onSuccess(param1T);
        return;
      } 
      this.count = l + 1L;
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


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableElementAtSingle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */