package io.reactivex.internal.operators.parallel;

import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.fuseable.ConditionalSubscriber;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.parallel.ParallelFailureHandling;
import io.reactivex.parallel.ParallelFlowable;
import io.reactivex.plugins.RxJavaPlugins;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class ParallelDoOnNextTry<T> extends ParallelFlowable<T> {
  final BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> errorHandler;
  
  final Consumer<? super T> onNext;
  
  final ParallelFlowable<T> source;
  
  public ParallelDoOnNextTry(ParallelFlowable<T> paramParallelFlowable, Consumer<? super T> paramConsumer, BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> paramBiFunction) {
    this.source = paramParallelFlowable;
    this.onNext = paramConsumer;
    this.errorHandler = paramBiFunction;
  }
  
  public int parallelism() {
    return this.source.parallelism();
  }
  
  public void subscribe(Subscriber<? super T>[] paramArrayOfSubscriber) {
    if (!validate((Subscriber[])paramArrayOfSubscriber))
      return; 
    int i = paramArrayOfSubscriber.length;
    Subscriber[] arrayOfSubscriber = new Subscriber[i];
    for (byte b = 0; b < i; b++) {
      Subscriber<? super T> subscriber = paramArrayOfSubscriber[b];
      if (subscriber instanceof ConditionalSubscriber) {
        arrayOfSubscriber[b] = (Subscriber)new ParallelDoOnNextConditionalSubscriber<T>((ConditionalSubscriber<? super T>)subscriber, this.onNext, this.errorHandler);
      } else {
        arrayOfSubscriber[b] = (Subscriber)new ParallelDoOnNextSubscriber<T>(subscriber, this.onNext, this.errorHandler);
      } 
    } 
    this.source.subscribe(arrayOfSubscriber);
  }
  
  static final class ParallelDoOnNextConditionalSubscriber<T> implements ConditionalSubscriber<T>, Subscription {
    boolean done;
    
    final ConditionalSubscriber<? super T> downstream;
    
    final BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> errorHandler;
    
    final Consumer<? super T> onNext;
    
    Subscription upstream;
    
    ParallelDoOnNextConditionalSubscriber(ConditionalSubscriber<? super T> param1ConditionalSubscriber, Consumer<? super T> param1Consumer, BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> param1BiFunction) {
      this.downstream = param1ConditionalSubscriber;
      this.onNext = param1Consumer;
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
          return this.downstream.tryOnNext(param1T);
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
  
  static final class ParallelDoOnNextSubscriber<T> implements ConditionalSubscriber<T>, Subscription {
    boolean done;
    
    final Subscriber<? super T> downstream;
    
    final BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> errorHandler;
    
    final Consumer<? super T> onNext;
    
    Subscription upstream;
    
    ParallelDoOnNextSubscriber(Subscriber<? super T> param1Subscriber, Consumer<? super T> param1Consumer, BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> param1BiFunction) {
      this.downstream = param1Subscriber;
      this.onNext = param1Consumer;
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
      if (!tryOnNext(param1T))
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


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/parallel/ParallelDoOnNextTry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */