package io.reactivex.internal.operators.parallel;

import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Predicate;
import io.reactivex.internal.fuseable.ConditionalSubscriber;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.parallel.ParallelFailureHandling;
import io.reactivex.parallel.ParallelFlowable;
import io.reactivex.plugins.RxJavaPlugins;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class ParallelFilterTry<T> extends ParallelFlowable<T> {
  final BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> errorHandler;
  
  final Predicate<? super T> predicate;
  
  final ParallelFlowable<T> source;
  
  public ParallelFilterTry(ParallelFlowable<T> paramParallelFlowable, Predicate<? super T> paramPredicate, BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> paramBiFunction) {
    this.source = paramParallelFlowable;
    this.predicate = paramPredicate;
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
        arrayOfSubscriber[b] = (Subscriber)new ParallelFilterConditionalSubscriber<T>((ConditionalSubscriber<? super T>)subscriber, this.predicate, this.errorHandler);
      } else {
        arrayOfSubscriber[b] = (Subscriber)new ParallelFilterSubscriber<T>(subscriber, this.predicate, this.errorHandler);
      } 
    } 
    this.source.subscribe(arrayOfSubscriber);
  }
  
  static abstract class BaseFilterSubscriber<T> implements ConditionalSubscriber<T>, Subscription {
    boolean done;
    
    final BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> errorHandler;
    
    final Predicate<? super T> predicate;
    
    Subscription upstream;
    
    BaseFilterSubscriber(Predicate<? super T> param1Predicate, BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> param1BiFunction) {
      this.predicate = param1Predicate;
      this.errorHandler = param1BiFunction;
    }
    
    public final void cancel() {
      this.upstream.cancel();
    }
    
    public final void onNext(T param1T) {
      if (!tryOnNext(param1T) && !this.done)
        this.upstream.request(1L); 
    }
    
    public final void request(long param1Long) {
      this.upstream.request(param1Long);
    }
  }
  
  static final class ParallelFilterConditionalSubscriber<T> extends BaseFilterSubscriber<T> {
    final ConditionalSubscriber<? super T> downstream;
    
    ParallelFilterConditionalSubscriber(ConditionalSubscriber<? super T> param1ConditionalSubscriber, Predicate<? super T> param1Predicate, BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> param1BiFunction) {
      super(param1Predicate, param1BiFunction);
      this.downstream = param1ConditionalSubscriber;
    }
    
    public void onComplete() {
      if (!this.done) {
        this.done = true;
        this.downstream.onComplete();
      } 
    }
    
    public void onError(Throwable param1Throwable) {
      if (this.done) {
        RxJavaPlugins.onError(param1Throwable);
        return;
      } 
      this.done = true;
      this.downstream.onError(param1Throwable);
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      if (SubscriptionHelper.validate(this.upstream, param1Subscription)) {
        this.upstream = param1Subscription;
        this.downstream.onSubscribe(this);
      } 
    }
    
    public boolean tryOnNext(T param1T) {
      if (!this.done) {
        long l = 0L;
        while (true) {
          boolean bool = true;
          try {
            return bool;
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
      return false;
    }
  }
  
  static final class ParallelFilterSubscriber<T> extends BaseFilterSubscriber<T> {
    final Subscriber<? super T> downstream;
    
    ParallelFilterSubscriber(Subscriber<? super T> param1Subscriber, Predicate<? super T> param1Predicate, BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> param1BiFunction) {
      super(param1Predicate, param1BiFunction);
      this.downstream = param1Subscriber;
    }
    
    public void onComplete() {
      if (!this.done) {
        this.done = true;
        this.downstream.onComplete();
      } 
    }
    
    public void onError(Throwable param1Throwable) {
      if (this.done) {
        RxJavaPlugins.onError(param1Throwable);
        return;
      } 
      this.done = true;
      this.downstream.onError(param1Throwable);
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      if (SubscriptionHelper.validate(this.upstream, param1Subscription)) {
        this.upstream = param1Subscription;
        this.downstream.onSubscribe(this);
      } 
    }
    
    public boolean tryOnNext(T param1T) {
      if (!this.done) {
        long l = 0L;
        while (true) {
          try {
            return false;
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
      return false;
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/parallel/ParallelFilterTry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */