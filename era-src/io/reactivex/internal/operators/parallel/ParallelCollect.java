package io.reactivex.internal.operators.parallel;

import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.BiConsumer;
import io.reactivex.internal.subscribers.DeferredScalarSubscriber;
import io.reactivex.internal.subscriptions.EmptySubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.parallel.ParallelFlowable;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.Callable;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class ParallelCollect<T, C> extends ParallelFlowable<C> {
  final BiConsumer<? super C, ? super T> collector;
  
  final Callable<? extends C> initialCollection;
  
  final ParallelFlowable<? extends T> source;
  
  public ParallelCollect(ParallelFlowable<? extends T> paramParallelFlowable, Callable<? extends C> paramCallable, BiConsumer<? super C, ? super T> paramBiConsumer) {
    this.source = paramParallelFlowable;
    this.initialCollection = paramCallable;
    this.collector = paramBiConsumer;
  }
  
  public int parallelism() {
    return this.source.parallelism();
  }
  
  void reportError(Subscriber<?>[] paramArrayOfSubscriber, Throwable paramThrowable) {
    int i = paramArrayOfSubscriber.length;
    for (byte b = 0; b < i; b++)
      EmptySubscription.error(paramThrowable, paramArrayOfSubscriber[b]); 
  }
  
  public void subscribe(Subscriber<? super C>[] paramArrayOfSubscriber) {
    if (!validate((Subscriber[])paramArrayOfSubscriber))
      return; 
    int i = paramArrayOfSubscriber.length;
    Subscriber[] arrayOfSubscriber = new Subscriber[i];
    byte b = 0;
    while (b < i) {
      try {
      
      } finally {
        Exception exception = null;
        Exceptions.throwIfFatal(exception);
        reportError((Subscriber<?>[])paramArrayOfSubscriber, exception);
      } 
    } 
    this.source.subscribe(arrayOfSubscriber);
  }
  
  static final class ParallelCollectSubscriber<T, C> extends DeferredScalarSubscriber<T, C> {
    private static final long serialVersionUID = -4767392946044436228L;
    
    C collection;
    
    final BiConsumer<? super C, ? super T> collector;
    
    boolean done;
    
    ParallelCollectSubscriber(Subscriber<? super C> param1Subscriber, C param1C, BiConsumer<? super C, ? super T> param1BiConsumer) {
      super(param1Subscriber);
      this.collection = param1C;
      this.collector = param1BiConsumer;
    }
    
    public void cancel() {
      super.cancel();
      this.upstream.cancel();
    }
    
    public void onComplete() {
      if (this.done)
        return; 
      this.done = true;
      C c = this.collection;
      this.collection = null;
      complete(c);
    }
    
    public void onError(Throwable param1Throwable) {
      if (this.done) {
        RxJavaPlugins.onError(param1Throwable);
        return;
      } 
      this.done = true;
      this.collection = null;
      this.downstream.onError(param1Throwable);
    }
    
    public void onNext(T param1T) {
      if (this.done)
        return; 
      try {
        this.collector.accept(this.collection, param1T);
      } finally {
        param1T = null;
        Exceptions.throwIfFatal((Throwable)param1T);
        cancel();
      } 
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


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/parallel/ParallelCollect.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */