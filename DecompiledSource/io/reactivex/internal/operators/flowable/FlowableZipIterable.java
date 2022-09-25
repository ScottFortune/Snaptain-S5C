package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.BiFunction;
import io.reactivex.internal.subscriptions.EmptySubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.Iterator;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableZipIterable<T, U, V> extends AbstractFlowableWithUpstream<T, V> {
  final Iterable<U> other;
  
  final BiFunction<? super T, ? super U, ? extends V> zipper;
  
  public FlowableZipIterable(Flowable<T> paramFlowable, Iterable<U> paramIterable, BiFunction<? super T, ? super U, ? extends V> paramBiFunction) {
    super(paramFlowable);
    this.other = paramIterable;
    this.zipper = paramBiFunction;
  }
  
  public void subscribeActual(Subscriber<? super V> paramSubscriber) {
    try {
    
    } finally {
      Exception exception = null;
      Exceptions.throwIfFatal(exception);
      EmptySubscription.error(exception, paramSubscriber);
    } 
  }
  
  static final class ZipIterableSubscriber<T, U, V> implements FlowableSubscriber<T>, Subscription {
    boolean done;
    
    final Subscriber<? super V> downstream;
    
    final Iterator<U> iterator;
    
    Subscription upstream;
    
    final BiFunction<? super T, ? super U, ? extends V> zipper;
    
    ZipIterableSubscriber(Subscriber<? super V> param1Subscriber, Iterator<U> param1Iterator, BiFunction<? super T, ? super U, ? extends V> param1BiFunction) {
      this.downstream = param1Subscriber;
      this.iterator = param1Iterator;
      this.zipper = param1BiFunction;
    }
    
    public void cancel() {
      this.upstream.cancel();
    }
    
    void error(Throwable param1Throwable) {
      Exceptions.throwIfFatal(param1Throwable);
      this.done = true;
      this.upstream.cancel();
      this.downstream.onError(param1Throwable);
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
      try {
      
      } finally {
        param1T = null;
        error((Throwable)param1T);
      } 
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


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableZipIterable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */