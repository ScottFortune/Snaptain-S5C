package io.reactivex.internal.operators.parallel;

import io.reactivex.FlowableSubscriber;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.fuseable.ConditionalSubscriber;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.parallel.ParallelFlowable;
import io.reactivex.plugins.RxJavaPlugins;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class ParallelMap<T, R> extends ParallelFlowable<R> {
  final Function<? super T, ? extends R> mapper;
  
  final ParallelFlowable<T> source;
  
  public ParallelMap(ParallelFlowable<T> paramParallelFlowable, Function<? super T, ? extends R> paramFunction) {
    this.source = paramParallelFlowable;
    this.mapper = paramFunction;
  }
  
  public int parallelism() {
    return this.source.parallelism();
  }
  
  public void subscribe(Subscriber<? super R>[] paramArrayOfSubscriber) {
    if (!validate((Subscriber[])paramArrayOfSubscriber))
      return; 
    int i = paramArrayOfSubscriber.length;
    Subscriber[] arrayOfSubscriber = new Subscriber[i];
    for (byte b = 0; b < i; b++) {
      Subscriber<? super R> subscriber = paramArrayOfSubscriber[b];
      if (subscriber instanceof ConditionalSubscriber) {
        arrayOfSubscriber[b] = (Subscriber)new ParallelMapConditionalSubscriber<T, R>((ConditionalSubscriber<? super R>)subscriber, this.mapper);
      } else {
        arrayOfSubscriber[b] = (Subscriber)new ParallelMapSubscriber<T, R>(subscriber, this.mapper);
      } 
    } 
    this.source.subscribe(arrayOfSubscriber);
  }
  
  static final class ParallelMapConditionalSubscriber<T, R> implements ConditionalSubscriber<T>, Subscription {
    boolean done;
    
    final ConditionalSubscriber<? super R> downstream;
    
    final Function<? super T, ? extends R> mapper;
    
    Subscription upstream;
    
    ParallelMapConditionalSubscriber(ConditionalSubscriber<? super R> param1ConditionalSubscriber, Function<? super T, ? extends R> param1Function) {
      this.downstream = param1ConditionalSubscriber;
      this.mapper = param1Function;
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
      try {
        return;
      } finally {
        param1T = null;
        Exceptions.throwIfFatal((Throwable)param1T);
        cancel();
        onError((Throwable)param1T);
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
    
    public boolean tryOnNext(T param1T) {
      if (this.done)
        return false; 
      try {
        return this.downstream.tryOnNext(param1T);
      } finally {
        param1T = null;
        Exceptions.throwIfFatal((Throwable)param1T);
        cancel();
        onError((Throwable)param1T);
      } 
    }
  }
  
  static final class ParallelMapSubscriber<T, R> implements FlowableSubscriber<T>, Subscription {
    boolean done;
    
    final Subscriber<? super R> downstream;
    
    final Function<? super T, ? extends R> mapper;
    
    Subscription upstream;
    
    ParallelMapSubscriber(Subscriber<? super R> param1Subscriber, Function<? super T, ? extends R> param1Function) {
      this.downstream = param1Subscriber;
      this.mapper = param1Function;
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
      try {
        return;
      } finally {
        param1T = null;
        Exceptions.throwIfFatal((Throwable)param1T);
        cancel();
        onError((Throwable)param1T);
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


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/parallel/ParallelMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */