package io.reactivex.internal.operators.flowable;

import io.reactivex.CompletableObserver;
import io.reactivex.CompletableSource;
import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.AtomicThrowable;
import io.reactivex.internal.util.HalfSerializer;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableMergeWithCompletable<T> extends AbstractFlowableWithUpstream<T, T> {
  final CompletableSource other;
  
  public FlowableMergeWithCompletable(Flowable<T> paramFlowable, CompletableSource paramCompletableSource) {
    super(paramFlowable);
    this.other = paramCompletableSource;
  }
  
  protected void subscribeActual(Subscriber<? super T> paramSubscriber) {
    MergeWithSubscriber<T> mergeWithSubscriber = new MergeWithSubscriber<T>(paramSubscriber);
    paramSubscriber.onSubscribe(mergeWithSubscriber);
    this.source.subscribe(mergeWithSubscriber);
    this.other.subscribe(mergeWithSubscriber.otherObserver);
  }
  
  static final class MergeWithSubscriber<T> extends AtomicInteger implements FlowableSubscriber<T>, Subscription {
    private static final long serialVersionUID = -4592979584110982903L;
    
    final Subscriber<? super T> downstream;
    
    final AtomicThrowable error;
    
    volatile boolean mainDone;
    
    final AtomicReference<Subscription> mainSubscription;
    
    volatile boolean otherDone;
    
    final OtherObserver otherObserver;
    
    final AtomicLong requested;
    
    MergeWithSubscriber(Subscriber<? super T> param1Subscriber) {
      this.downstream = param1Subscriber;
      this.mainSubscription = new AtomicReference<Subscription>();
      this.otherObserver = new OtherObserver(this);
      this.error = new AtomicThrowable();
      this.requested = new AtomicLong();
    }
    
    public void cancel() {
      SubscriptionHelper.cancel(this.mainSubscription);
      DisposableHelper.dispose(this.otherObserver);
    }
    
    public void onComplete() {
      this.mainDone = true;
      if (this.otherDone)
        HalfSerializer.onComplete(this.downstream, this, this.error); 
    }
    
    public void onError(Throwable param1Throwable) {
      DisposableHelper.dispose(this.otherObserver);
      HalfSerializer.onError(this.downstream, param1Throwable, this, this.error);
    }
    
    public void onNext(T param1T) {
      HalfSerializer.onNext(this.downstream, param1T, this, this.error);
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      SubscriptionHelper.deferredSetOnce(this.mainSubscription, this.requested, param1Subscription);
    }
    
    void otherComplete() {
      this.otherDone = true;
      if (this.mainDone)
        HalfSerializer.onComplete(this.downstream, this, this.error); 
    }
    
    void otherError(Throwable param1Throwable) {
      SubscriptionHelper.cancel(this.mainSubscription);
      HalfSerializer.onError(this.downstream, param1Throwable, this, this.error);
    }
    
    public void request(long param1Long) {
      SubscriptionHelper.deferredRequest(this.mainSubscription, this.requested, param1Long);
    }
    
    static final class OtherObserver extends AtomicReference<Disposable> implements CompletableObserver {
      private static final long serialVersionUID = -2935427570954647017L;
      
      final FlowableMergeWithCompletable.MergeWithSubscriber<?> parent;
      
      OtherObserver(FlowableMergeWithCompletable.MergeWithSubscriber<?> param2MergeWithSubscriber) {
        this.parent = param2MergeWithSubscriber;
      }
      
      public void onComplete() {
        this.parent.otherComplete();
      }
      
      public void onError(Throwable param2Throwable) {
        this.parent.otherError(param2Throwable);
      }
      
      public void onSubscribe(Disposable param2Disposable) {
        DisposableHelper.setOnce(this, param2Disposable);
      }
    }
  }
  
  static final class OtherObserver extends AtomicReference<Disposable> implements CompletableObserver {
    private static final long serialVersionUID = -2935427570954647017L;
    
    final FlowableMergeWithCompletable.MergeWithSubscriber<?> parent;
    
    OtherObserver(FlowableMergeWithCompletable.MergeWithSubscriber<?> param1MergeWithSubscriber) {
      this.parent = param1MergeWithSubscriber;
    }
    
    public void onComplete() {
      this.parent.otherComplete();
    }
    
    public void onError(Throwable param1Throwable) {
      this.parent.otherError(param1Throwable);
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      DisposableHelper.setOnce(this, param1Disposable);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableMergeWithCompletable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */