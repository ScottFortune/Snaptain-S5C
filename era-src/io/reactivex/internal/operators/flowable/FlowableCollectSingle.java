package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.BiConsumer;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.internal.fuseable.FuseToFlowable;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.Callable;
import org.reactivestreams.Subscription;

public final class FlowableCollectSingle<T, U> extends Single<U> implements FuseToFlowable<U> {
  final BiConsumer<? super U, ? super T> collector;
  
  final Callable<? extends U> initialSupplier;
  
  final Flowable<T> source;
  
  public FlowableCollectSingle(Flowable<T> paramFlowable, Callable<? extends U> paramCallable, BiConsumer<? super U, ? super T> paramBiConsumer) {
    this.source = paramFlowable;
    this.initialSupplier = paramCallable;
    this.collector = paramBiConsumer;
  }
  
  public Flowable<U> fuseToFlowable() {
    return RxJavaPlugins.onAssembly(new FlowableCollect<T, U>(this.source, this.initialSupplier, this.collector));
  }
  
  protected void subscribeActual(SingleObserver<? super U> paramSingleObserver) {
    try {
      return;
    } finally {
      Exception exception = null;
      EmptyDisposable.error(exception, paramSingleObserver);
    } 
  }
  
  static final class CollectSubscriber<T, U> implements FlowableSubscriber<T>, Disposable {
    final BiConsumer<? super U, ? super T> collector;
    
    boolean done;
    
    final SingleObserver<? super U> downstream;
    
    final U u;
    
    Subscription upstream;
    
    CollectSubscriber(SingleObserver<? super U> param1SingleObserver, U param1U, BiConsumer<? super U, ? super T> param1BiConsumer) {
      this.downstream = param1SingleObserver;
      this.collector = param1BiConsumer;
      this.u = param1U;
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
      if (this.done)
        return; 
      this.done = true;
      this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
      this.downstream.onSuccess(this.u);
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
      try {
        this.collector.accept(this.u, param1T);
      } finally {
        param1T = null;
        Exceptions.throwIfFatal((Throwable)param1T);
        this.upstream.cancel();
      } 
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


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableCollectSingle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */