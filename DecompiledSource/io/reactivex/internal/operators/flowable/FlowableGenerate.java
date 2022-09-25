package io.reactivex.internal.operators.flowable;

import io.reactivex.Emitter;
import io.reactivex.Flowable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.subscriptions.EmptySubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicLong;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableGenerate<T, S> extends Flowable<T> {
  final Consumer<? super S> disposeState;
  
  final BiFunction<S, Emitter<T>, S> generator;
  
  final Callable<S> stateSupplier;
  
  public FlowableGenerate(Callable<S> paramCallable, BiFunction<S, Emitter<T>, S> paramBiFunction, Consumer<? super S> paramConsumer) {
    this.stateSupplier = paramCallable;
    this.generator = paramBiFunction;
    this.disposeState = paramConsumer;
  }
  
  public void subscribeActual(Subscriber<? super T> paramSubscriber) {
    try {
      return;
    } finally {
      Exception exception = null;
      Exceptions.throwIfFatal(exception);
      EmptySubscription.error(exception, paramSubscriber);
    } 
  }
  
  static final class GeneratorSubscription<T, S> extends AtomicLong implements Emitter<T>, Subscription {
    private static final long serialVersionUID = 7565982551505011832L;
    
    volatile boolean cancelled;
    
    final Consumer<? super S> disposeState;
    
    final Subscriber<? super T> downstream;
    
    final BiFunction<S, ? super Emitter<T>, S> generator;
    
    boolean hasNext;
    
    S state;
    
    boolean terminate;
    
    GeneratorSubscription(Subscriber<? super T> param1Subscriber, BiFunction<S, ? super Emitter<T>, S> param1BiFunction, Consumer<? super S> param1Consumer, S param1S) {
      this.downstream = param1Subscriber;
      this.generator = param1BiFunction;
      this.disposeState = param1Consumer;
      this.state = param1S;
    }
    
    private void dispose(S param1S) {
      try {
        this.disposeState.accept(param1S);
      } finally {
        param1S = null;
        Exceptions.throwIfFatal((Throwable)param1S);
      } 
    }
    
    public void cancel() {
      if (!this.cancelled) {
        this.cancelled = true;
        if (BackpressureHelper.add(this, 1L) == 0L) {
          S s = this.state;
          this.state = null;
          dispose(s);
        } 
      } 
    }
    
    public void onComplete() {
      if (!this.terminate) {
        this.terminate = true;
        this.downstream.onComplete();
      } 
    }
    
    public void onError(Throwable param1Throwable) {
      if (this.terminate) {
        RxJavaPlugins.onError(param1Throwable);
      } else {
        Throwable throwable = param1Throwable;
        if (param1Throwable == null)
          throwable = new NullPointerException("onError called with null. Null values are generally not allowed in 2.x operators and sources."); 
        this.terminate = true;
        this.downstream.onError(throwable);
      } 
    }
    
    public void onNext(T param1T) {
      if (!this.terminate)
        if (this.hasNext) {
          onError(new IllegalStateException("onNext already called in this generate turn"));
        } else if (param1T == null) {
          onError(new NullPointerException("onNext called with null. Null values are generally not allowed in 2.x operators and sources."));
        } else {
          this.hasNext = true;
          this.downstream.onNext(param1T);
        }  
    }
    
    public void request(long param1Long) {
      long l;
      if (!SubscriptionHelper.validate(param1Long))
        return; 
      if (BackpressureHelper.add(this, param1Long) != 0L)
        return; 
      S s = this.state;
      BiFunction<S, ? super Emitter<T>, S> biFunction = this.generator;
      do {
        l = 0L;
        while (true) {
          S s1 = s;
          if (l != param1Long) {
            if (this.cancelled) {
              this.state = null;
              dispose(s1);
              return;
            } 
            this.hasNext = false;
            try {
            
            } finally {
              s = null;
              Exceptions.throwIfFatal((Throwable)s);
              this.cancelled = true;
              this.state = null;
              onError((Throwable)s);
              dispose(s1);
            } 
            continue;
          } 
          long l1 = get();
          s = s1;
          param1Long = l1;
          if (l == l1) {
            this.state = s1;
            l = addAndGet(-l);
            s = s1;
            param1Long = l;
            break;
          } 
        } 
      } while (l != 0L);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableGenerate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */