package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.functions.BiPredicate;
import io.reactivex.functions.Function;
import io.reactivex.internal.fuseable.ConditionalSubscriber;
import io.reactivex.internal.subscribers.BasicFuseableConditionalSubscriber;
import io.reactivex.internal.subscribers.BasicFuseableSubscriber;
import org.reactivestreams.Subscriber;

public final class FlowableDistinctUntilChanged<T, K> extends AbstractFlowableWithUpstream<T, T> {
  final BiPredicate<? super K, ? super K> comparer;
  
  final Function<? super T, K> keySelector;
  
  public FlowableDistinctUntilChanged(Flowable<T> paramFlowable, Function<? super T, K> paramFunction, BiPredicate<? super K, ? super K> paramBiPredicate) {
    super(paramFlowable);
    this.keySelector = paramFunction;
    this.comparer = paramBiPredicate;
  }
  
  protected void subscribeActual(Subscriber<? super T> paramSubscriber) {
    ConditionalSubscriber<? super T> conditionalSubscriber;
    if (paramSubscriber instanceof ConditionalSubscriber) {
      conditionalSubscriber = (ConditionalSubscriber)paramSubscriber;
      this.source.subscribe((FlowableSubscriber)new DistinctUntilChangedConditionalSubscriber<T, K>(conditionalSubscriber, this.keySelector, this.comparer));
    } else {
      this.source.subscribe((FlowableSubscriber)new DistinctUntilChangedSubscriber<T, K>((Subscriber<? super T>)conditionalSubscriber, this.keySelector, this.comparer));
    } 
  }
  
  static final class DistinctUntilChangedConditionalSubscriber<T, K> extends BasicFuseableConditionalSubscriber<T, T> {
    final BiPredicate<? super K, ? super K> comparer;
    
    boolean hasValue;
    
    final Function<? super T, K> keySelector;
    
    K last;
    
    DistinctUntilChangedConditionalSubscriber(ConditionalSubscriber<? super T> param1ConditionalSubscriber, Function<? super T, K> param1Function, BiPredicate<? super K, ? super K> param1BiPredicate) {
      super(param1ConditionalSubscriber);
      this.keySelector = param1Function;
      this.comparer = param1BiPredicate;
    }
    
    public void onNext(T param1T) {
      if (!tryOnNext(param1T))
        this.upstream.request(1L); 
    }
    
    public T poll() throws Exception {
      while (true) {
        Object object1 = this.qs.poll();
        if (object1 == null)
          return null; 
        Object object2 = this.keySelector.apply(object1);
        if (!this.hasValue) {
          this.hasValue = true;
          this.last = (K)object2;
          return (T)object1;
        } 
        if (!this.comparer.test(this.last, object2)) {
          this.last = (K)object2;
          return (T)object1;
        } 
        this.last = (K)object2;
        if (this.sourceMode != 1)
          this.upstream.request(1L); 
      } 
    }
    
    public int requestFusion(int param1Int) {
      return transitiveBoundaryFusion(param1Int);
    }
    
    public boolean tryOnNext(T param1T) {
      if (this.done)
        return false; 
      if (this.sourceMode != 0)
        return this.downstream.tryOnNext(param1T); 
      try {
        Object object = this.keySelector.apply(param1T);
        return true;
      } finally {
        param1T = null;
        fail((Throwable)param1T);
      } 
    }
  }
  
  static final class DistinctUntilChangedSubscriber<T, K> extends BasicFuseableSubscriber<T, T> implements ConditionalSubscriber<T> {
    final BiPredicate<? super K, ? super K> comparer;
    
    boolean hasValue;
    
    final Function<? super T, K> keySelector;
    
    K last;
    
    DistinctUntilChangedSubscriber(Subscriber<? super T> param1Subscriber, Function<? super T, K> param1Function, BiPredicate<? super K, ? super K> param1BiPredicate) {
      super(param1Subscriber);
      this.keySelector = param1Function;
      this.comparer = param1BiPredicate;
    }
    
    public void onNext(T param1T) {
      if (!tryOnNext(param1T))
        this.upstream.request(1L); 
    }
    
    public T poll() throws Exception {
      while (true) {
        Object object1 = this.qs.poll();
        if (object1 == null)
          return null; 
        Object object2 = this.keySelector.apply(object1);
        if (!this.hasValue) {
          this.hasValue = true;
          this.last = (K)object2;
          return (T)object1;
        } 
        if (!this.comparer.test(this.last, object2)) {
          this.last = (K)object2;
          return (T)object1;
        } 
        this.last = (K)object2;
        if (this.sourceMode != 1)
          this.upstream.request(1L); 
      } 
    }
    
    public int requestFusion(int param1Int) {
      return transitiveBoundaryFusion(param1Int);
    }
    
    public boolean tryOnNext(T param1T) {
      if (this.done)
        return false; 
      if (this.sourceMode != 0) {
        this.downstream.onNext(param1T);
        return true;
      } 
      try {
        Object object = this.keySelector.apply(param1T);
        return true;
      } finally {
        param1T = null;
        fail((Throwable)param1T);
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableDistinctUntilChanged.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */