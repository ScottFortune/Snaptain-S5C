package io.reactivex.internal.operators.parallel;

import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.internal.fuseable.ConditionalSubscriber;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.parallel.ParallelFailureHandling;
import io.reactivex.parallel.ParallelFlowable;
import io.reactivex.plugins.RxJavaPlugins;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class ParallelMapTry<T, R> extends ParallelFlowable<R> {
  final BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> errorHandler;
  
  final Function<? super T, ? extends R> mapper;
  
  final ParallelFlowable<T> source;
  
  public ParallelMapTry(ParallelFlowable<T> paramParallelFlowable, Function<? super T, ? extends R> paramFunction, BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> paramBiFunction) {
    this.source = paramParallelFlowable;
    this.mapper = paramFunction;
    this.errorHandler = paramBiFunction;
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
        arrayOfSubscriber[b] = (Subscriber)new ParallelMapTryConditionalSubscriber<T, R>((ConditionalSubscriber<? super R>)subscriber, this.mapper, this.errorHandler);
      } else {
        arrayOfSubscriber[b] = (Subscriber)new ParallelMapTrySubscriber<T, R>(subscriber, this.mapper, this.errorHandler);
      } 
    } 
    this.source.subscribe(arrayOfSubscriber);
  }
  
  static final class ParallelMapTryConditionalSubscriber<T, R> implements ConditionalSubscriber<T>, Subscription {
    boolean done;
    
    final ConditionalSubscriber<? super R> downstream;
    
    final BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> errorHandler;
    
    final Function<? super T, ? extends R> mapper;
    
    Subscription upstream;
    
    ParallelMapTryConditionalSubscriber(ConditionalSubscriber<? super R> param1ConditionalSubscriber, Function<? super T, ? extends R> param1Function, BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> param1BiFunction) {
      this.downstream = param1ConditionalSubscriber;
      this.mapper = param1Function;
      this.errorHandler = param1BiFunction;
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
      if (!tryOnNext(param1T) && !this.done)
        this.upstream.request(1L); 
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
      long l = 0L;
      while (true) {
        try {
          return this.downstream.tryOnNext(object);
        } finally {
          Exception exception = null;
          Exceptions.throwIfFatal(exception);
          try {
            BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> biFunction = this.errorHandler;
          } finally {
            param1T = null;
            Exceptions.throwIfFatal((Throwable)param1T);
            cancel();
            onError((Throwable)new CompositeException(new Throwable[] { exception, (Throwable)param1T }));
          } 
        } 
      } 
    }
  }
  
  static final class ParallelMapTrySubscriber<T, R> implements ConditionalSubscriber<T>, Subscription {
    boolean done;
    
    final Subscriber<? super R> downstream;
    
    final BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> errorHandler;
    
    final Function<? super T, ? extends R> mapper;
    
    Subscription upstream;
    
    ParallelMapTrySubscriber(Subscriber<? super R> param1Subscriber, Function<? super T, ? extends R> param1Function, BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> param1BiFunction) {
      this.downstream = param1Subscriber;
      this.mapper = param1Function;
      this.errorHandler = param1BiFunction;
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
      if (!tryOnNext(param1T) && !this.done)
        this.upstream.request(1L); 
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
      long l = 0L;
      while (true) {
        try {
          return true;
        } finally {
          Exception exception = null;
          Exceptions.throwIfFatal(exception);
          try {
            BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> biFunction = this.errorHandler;
          } finally {
            param1T = null;
            Exceptions.throwIfFatal((Throwable)param1T);
            cancel();
            onError((Throwable)new CompositeException(new Throwable[] { exception, (Throwable)param1T }));
          } 
        } 
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/parallel/ParallelMapTry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */