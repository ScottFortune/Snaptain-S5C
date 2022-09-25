package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.fuseable.ConditionalSubscriber;
import io.reactivex.internal.subscribers.BasicFuseableConditionalSubscriber;
import io.reactivex.internal.subscribers.BasicFuseableSubscriber;
import io.reactivex.plugins.RxJavaPlugins;
import org.reactivestreams.Subscriber;

public final class FlowableDoOnEach<T> extends AbstractFlowableWithUpstream<T, T> {
  final Action onAfterTerminate;
  
  final Action onComplete;
  
  final Consumer<? super Throwable> onError;
  
  final Consumer<? super T> onNext;
  
  public FlowableDoOnEach(Flowable<T> paramFlowable, Consumer<? super T> paramConsumer, Consumer<? super Throwable> paramConsumer1, Action paramAction1, Action paramAction2) {
    super(paramFlowable);
    this.onNext = paramConsumer;
    this.onError = paramConsumer1;
    this.onComplete = paramAction1;
    this.onAfterTerminate = paramAction2;
  }
  
  protected void subscribeActual(Subscriber<? super T> paramSubscriber) {
    if (paramSubscriber instanceof ConditionalSubscriber) {
      this.source.subscribe((FlowableSubscriber)new DoOnEachConditionalSubscriber<T>((ConditionalSubscriber<? super T>)paramSubscriber, this.onNext, this.onError, this.onComplete, this.onAfterTerminate));
    } else {
      this.source.subscribe((FlowableSubscriber)new DoOnEachSubscriber<T>(paramSubscriber, this.onNext, this.onError, this.onComplete, this.onAfterTerminate));
    } 
  }
  
  static final class DoOnEachConditionalSubscriber<T> extends BasicFuseableConditionalSubscriber<T, T> {
    final Action onAfterTerminate;
    
    final Action onComplete;
    
    final Consumer<? super Throwable> onError;
    
    final Consumer<? super T> onNext;
    
    DoOnEachConditionalSubscriber(ConditionalSubscriber<? super T> param1ConditionalSubscriber, Consumer<? super T> param1Consumer, Consumer<? super Throwable> param1Consumer1, Action param1Action1, Action param1Action2) {
      super(param1ConditionalSubscriber);
      this.onNext = param1Consumer;
      this.onError = param1Consumer1;
      this.onComplete = param1Action1;
      this.onAfterTerminate = param1Action2;
    }
    
    public void onComplete() {
      if (this.done)
        return; 
      try {
        this.onComplete.run();
        this.done = true;
        return;
      } finally {
        Exception exception = null;
        fail(exception);
      } 
    }
    
    public void onError(Throwable param1Throwable) {
      if (this.done) {
        RxJavaPlugins.onError(param1Throwable);
        return;
      } 
      boolean bool = true;
    }
    
    public void onNext(T param1T) {
      if (this.done)
        return; 
      if (this.sourceMode != 0) {
        this.downstream.onNext(null);
        return;
      } 
      try {
        return;
      } finally {
        param1T = null;
        fail((Throwable)param1T);
      } 
    }
    
    public T poll() throws Exception {
      try {
        Object object;
        return (T)object;
      } finally {
        Exception exception = null;
        Exceptions.throwIfFatal(exception);
      } 
    }
    
    public int requestFusion(int param1Int) {
      return transitiveBoundaryFusion(param1Int);
    }
    
    public boolean tryOnNext(T param1T) {
      if (this.done)
        return false; 
      try {
        return this.downstream.tryOnNext(param1T);
      } finally {
        param1T = null;
        fail((Throwable)param1T);
      } 
    }
  }
  
  static final class DoOnEachSubscriber<T> extends BasicFuseableSubscriber<T, T> {
    final Action onAfterTerminate;
    
    final Action onComplete;
    
    final Consumer<? super Throwable> onError;
    
    final Consumer<? super T> onNext;
    
    DoOnEachSubscriber(Subscriber<? super T> param1Subscriber, Consumer<? super T> param1Consumer, Consumer<? super Throwable> param1Consumer1, Action param1Action1, Action param1Action2) {
      super(param1Subscriber);
      this.onNext = param1Consumer;
      this.onError = param1Consumer1;
      this.onComplete = param1Action1;
      this.onAfterTerminate = param1Action2;
    }
    
    public void onComplete() {
      if (this.done)
        return; 
      try {
        this.onComplete.run();
        this.done = true;
        return;
      } finally {
        Exception exception = null;
        fail(exception);
      } 
    }
    
    public void onError(Throwable param1Throwable) {
      if (this.done) {
        RxJavaPlugins.onError(param1Throwable);
        return;
      } 
      boolean bool = true;
    }
    
    public void onNext(T param1T) {
      if (this.done)
        return; 
      if (this.sourceMode != 0) {
        this.downstream.onNext(null);
        return;
      } 
      try {
        return;
      } finally {
        param1T = null;
        fail((Throwable)param1T);
      } 
    }
    
    public T poll() throws Exception {
      try {
        Object object;
        return (T)object;
      } finally {
        Exception exception = null;
        Exceptions.throwIfFatal(exception);
      } 
    }
    
    public int requestFusion(int param1Int) {
      return transitiveBoundaryFusion(param1Int);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableDoOnEach.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */