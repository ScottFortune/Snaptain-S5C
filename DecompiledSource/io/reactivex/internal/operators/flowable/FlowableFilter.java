package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.functions.Predicate;
import io.reactivex.internal.fuseable.ConditionalSubscriber;
import io.reactivex.internal.fuseable.QueueSubscription;
import io.reactivex.internal.subscribers.BasicFuseableConditionalSubscriber;
import io.reactivex.internal.subscribers.BasicFuseableSubscriber;
import org.reactivestreams.Subscriber;

public final class FlowableFilter<T> extends AbstractFlowableWithUpstream<T, T> {
  final Predicate<? super T> predicate;
  
  public FlowableFilter(Flowable<T> paramFlowable, Predicate<? super T> paramPredicate) {
    super(paramFlowable);
    this.predicate = paramPredicate;
  }
  
  protected void subscribeActual(Subscriber<? super T> paramSubscriber) {
    if (paramSubscriber instanceof ConditionalSubscriber) {
      this.source.subscribe((FlowableSubscriber)new FilterConditionalSubscriber<T>((ConditionalSubscriber<? super T>)paramSubscriber, this.predicate));
    } else {
      this.source.subscribe((FlowableSubscriber)new FilterSubscriber<T>(paramSubscriber, this.predicate));
    } 
  }
  
  static final class FilterConditionalSubscriber<T> extends BasicFuseableConditionalSubscriber<T, T> {
    final Predicate<? super T> filter;
    
    FilterConditionalSubscriber(ConditionalSubscriber<? super T> param1ConditionalSubscriber, Predicate<? super T> param1Predicate) {
      super(param1ConditionalSubscriber);
      this.filter = param1Predicate;
    }
    
    public void onNext(T param1T) {
      if (!tryOnNext(param1T))
        this.upstream.request(1L); 
    }
    
    public T poll() throws Exception {
      QueueSubscription queueSubscription = this.qs;
      Predicate<? super T> predicate = this.filter;
      while (true) {
        Object object = queueSubscription.poll();
        if (object == null)
          return null; 
        if (predicate.test(object))
          return (T)object; 
        if (this.sourceMode == 2)
          queueSubscription.request(1L); 
      } 
    }
    
    public int requestFusion(int param1Int) {
      return transitiveBoundaryFusion(param1Int);
    }
    
    public boolean tryOnNext(T param1T) {
      if (this.done)
        return false; 
      if (this.sourceMode != 0)
        return this.downstream.tryOnNext(null); 
      boolean bool = true;
      try {
        return bool;
      } finally {
        param1T = null;
        fail((Throwable)param1T);
      } 
    }
  }
  
  static final class FilterSubscriber<T> extends BasicFuseableSubscriber<T, T> implements ConditionalSubscriber<T> {
    final Predicate<? super T> filter;
    
    FilterSubscriber(Subscriber<? super T> param1Subscriber, Predicate<? super T> param1Predicate) {
      super(param1Subscriber);
      this.filter = param1Predicate;
    }
    
    public void onNext(T param1T) {
      if (!tryOnNext(param1T))
        this.upstream.request(1L); 
    }
    
    public T poll() throws Exception {
      QueueSubscription queueSubscription = this.qs;
      Predicate<? super T> predicate = this.filter;
      while (true) {
        Object object = queueSubscription.poll();
        if (object == null)
          return null; 
        if (predicate.test(object))
          return (T)object; 
        if (this.sourceMode == 2)
          queueSubscription.request(1L); 
      } 
    }
    
    public int requestFusion(int param1Int) {
      return transitiveBoundaryFusion(param1Int);
    }
    
    public boolean tryOnNext(T param1T) {
      if (this.done)
        return false; 
      if (this.sourceMode != 0) {
        this.downstream.onNext(null);
        return true;
      } 
      try {
        return bool;
      } finally {
        param1T = null;
        fail((Throwable)param1T);
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */