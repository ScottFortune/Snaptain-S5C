package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableDelaySubscriptionOther<T, U> extends Flowable<T> {
  final Publisher<? extends T> main;
  
  final Publisher<U> other;
  
  public FlowableDelaySubscriptionOther(Publisher<? extends T> paramPublisher, Publisher<U> paramPublisher1) {
    this.main = paramPublisher;
    this.other = paramPublisher1;
  }
  
  public void subscribeActual(Subscriber<? super T> paramSubscriber) {
    MainSubscriber<T> mainSubscriber = new MainSubscriber<T>(paramSubscriber, this.main);
    paramSubscriber.onSubscribe(mainSubscriber);
    this.other.subscribe((Subscriber)mainSubscriber.other);
  }
  
  static final class MainSubscriber<T> extends AtomicLong implements FlowableSubscriber<T>, Subscription {
    private static final long serialVersionUID = 2259811067697317255L;
    
    final Subscriber<? super T> downstream;
    
    final Publisher<? extends T> main;
    
    final OtherSubscriber other;
    
    final AtomicReference<Subscription> upstream;
    
    MainSubscriber(Subscriber<? super T> param1Subscriber, Publisher<? extends T> param1Publisher) {
      this.downstream = param1Subscriber;
      this.main = param1Publisher;
      this.other = new OtherSubscriber();
      this.upstream = new AtomicReference<Subscription>();
    }
    
    public void cancel() {
      SubscriptionHelper.cancel(this.other);
      SubscriptionHelper.cancel(this.upstream);
    }
    
    void next() {
      this.main.subscribe((Subscriber)this);
    }
    
    public void onComplete() {
      this.downstream.onComplete();
    }
    
    public void onError(Throwable param1Throwable) {
      this.downstream.onError(param1Throwable);
    }
    
    public void onNext(T param1T) {
      this.downstream.onNext(param1T);
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      SubscriptionHelper.deferredSetOnce(this.upstream, this, param1Subscription);
    }
    
    public void request(long param1Long) {
      if (SubscriptionHelper.validate(param1Long))
        SubscriptionHelper.deferredRequest(this.upstream, this, param1Long); 
    }
    
    final class OtherSubscriber extends AtomicReference<Subscription> implements FlowableSubscriber<Object> {
      private static final long serialVersionUID = -3892798459447644106L;
      
      public void onComplete() {
        if (get() != SubscriptionHelper.CANCELLED)
          FlowableDelaySubscriptionOther.MainSubscriber.this.next(); 
      }
      
      public void onError(Throwable param2Throwable) {
        if (get() != SubscriptionHelper.CANCELLED) {
          FlowableDelaySubscriptionOther.MainSubscriber.this.downstream.onError(param2Throwable);
        } else {
          RxJavaPlugins.onError(param2Throwable);
        } 
      }
      
      public void onNext(Object param2Object) {
        param2Object = get();
        if (param2Object != SubscriptionHelper.CANCELLED) {
          lazySet((Subscription)SubscriptionHelper.CANCELLED);
          param2Object.cancel();
          FlowableDelaySubscriptionOther.MainSubscriber.this.next();
        } 
      }
      
      public void onSubscribe(Subscription param2Subscription) {
        if (SubscriptionHelper.setOnce(this, param2Subscription))
          param2Subscription.request(Long.MAX_VALUE); 
      }
    }
  }
  
  final class OtherSubscriber extends AtomicReference<Subscription> implements FlowableSubscriber<Object> {
    private static final long serialVersionUID = -3892798459447644106L;
    
    public void onComplete() {
      if (get() != SubscriptionHelper.CANCELLED)
        this.this$0.next(); 
    }
    
    public void onError(Throwable param1Throwable) {
      if (get() != SubscriptionHelper.CANCELLED) {
        this.this$0.downstream.onError(param1Throwable);
      } else {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
    
    public void onNext(Object param1Object) {
      param1Object = get();
      if (param1Object != SubscriptionHelper.CANCELLED) {
        lazySet((Subscription)SubscriptionHelper.CANCELLED);
        param1Object.cancel();
        this.this$0.next();
      } 
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      if (SubscriptionHelper.setOnce(this, param1Subscription))
        param1Subscription.request(Long.MAX_VALUE); 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableDelaySubscriptionOther.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */