package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.internal.subscriptions.EmptySubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableUsing<T, D> extends Flowable<T> {
  final Consumer<? super D> disposer;
  
  final boolean eager;
  
  final Callable<? extends D> resourceSupplier;
  
  final Function<? super D, ? extends Publisher<? extends T>> sourceSupplier;
  
  public FlowableUsing(Callable<? extends D> paramCallable, Function<? super D, ? extends Publisher<? extends T>> paramFunction, Consumer<? super D> paramConsumer, boolean paramBoolean) {
    this.resourceSupplier = paramCallable;
    this.sourceSupplier = paramFunction;
    this.disposer = paramConsumer;
    this.eager = paramBoolean;
  }
  
  public void subscribeActual(Subscriber<? super T> paramSubscriber) {
    try {
      D d;
    } finally {
      Exception exception = null;
      Exceptions.throwIfFatal(exception);
      EmptySubscription.error(exception, paramSubscriber);
    } 
  }
  
  static final class UsingSubscriber<T, D> extends AtomicBoolean implements FlowableSubscriber<T>, Subscription {
    private static final long serialVersionUID = 5904473792286235046L;
    
    final Consumer<? super D> disposer;
    
    final Subscriber<? super T> downstream;
    
    final boolean eager;
    
    final D resource;
    
    Subscription upstream;
    
    UsingSubscriber(Subscriber<? super T> param1Subscriber, D param1D, Consumer<? super D> param1Consumer, boolean param1Boolean) {
      this.downstream = param1Subscriber;
      this.resource = param1D;
      this.disposer = param1Consumer;
      this.eager = param1Boolean;
    }
    
    public void cancel() {
      disposeAfter();
      this.upstream.cancel();
    }
    
    void disposeAfter() {
      if (compareAndSet(false, true))
        try {
          this.disposer.accept(this.resource);
        } finally {
          Exception exception = null;
          Exceptions.throwIfFatal(exception);
        }  
    }
    
    public void onComplete() {
      if (this.eager) {
        if (compareAndSet(false, true))
          try {
            this.disposer.accept(this.resource);
          } finally {
            Exception exception = null;
            Exceptions.throwIfFatal(exception);
            this.downstream.onError(exception);
          }  
        this.upstream.cancel();
        this.downstream.onComplete();
      } else {
        this.downstream.onComplete();
        this.upstream.cancel();
        disposeAfter();
      } 
    }
    
    public void onError(Throwable param1Throwable) {
      if (this.eager) {
        Exception exception1 = null;
        Exception exception2 = exception1;
        if (compareAndSet(false, true))
          try {
            this.disposer.accept(this.resource);
          } finally {
            exception2 = null;
          }  
        this.upstream.cancel();
        if (exception2 != null) {
          this.downstream.onError((Throwable)new CompositeException(new Throwable[] { param1Throwable, exception2 }));
        } else {
          this.downstream.onError(param1Throwable);
        } 
      } else {
        this.downstream.onError(param1Throwable);
        this.upstream.cancel();
        disposeAfter();
      } 
    }
    
    public void onNext(T param1T) {
      this.downstream.onNext(param1T);
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


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableUsing.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */