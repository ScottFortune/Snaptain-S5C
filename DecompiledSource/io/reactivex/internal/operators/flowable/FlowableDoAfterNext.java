package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.fuseable.ConditionalSubscriber;
import io.reactivex.internal.subscribers.BasicFuseableConditionalSubscriber;
import io.reactivex.internal.subscribers.BasicFuseableSubscriber;
import org.reactivestreams.Subscriber;

public final class FlowableDoAfterNext<T> extends AbstractFlowableWithUpstream<T, T> {
  final Consumer<? super T> onAfterNext;
  
  public FlowableDoAfterNext(Flowable<T> paramFlowable, Consumer<? super T> paramConsumer) {
    super(paramFlowable);
    this.onAfterNext = paramConsumer;
  }
  
  protected void subscribeActual(Subscriber<? super T> paramSubscriber) {
    if (paramSubscriber instanceof ConditionalSubscriber) {
      this.source.subscribe((FlowableSubscriber)new DoAfterConditionalSubscriber<T>((ConditionalSubscriber<? super T>)paramSubscriber, this.onAfterNext));
    } else {
      this.source.subscribe((FlowableSubscriber)new DoAfterSubscriber<T>(paramSubscriber, this.onAfterNext));
    } 
  }
  
  static final class DoAfterConditionalSubscriber<T> extends BasicFuseableConditionalSubscriber<T, T> {
    final Consumer<? super T> onAfterNext;
    
    DoAfterConditionalSubscriber(ConditionalSubscriber<? super T> param1ConditionalSubscriber, Consumer<? super T> param1Consumer) {
      super(param1ConditionalSubscriber);
      this.onAfterNext = param1Consumer;
    }
    
    public void onNext(T param1T) {
      this.downstream.onNext(param1T);
      if (this.sourceMode == 0)
        try {
          this.onAfterNext.accept(param1T);
        } finally {
          param1T = null;
        }  
    }
    
    public T poll() throws Exception {
      Object object = this.qs.poll();
      if (object != null)
        this.onAfterNext.accept(object); 
      return (T)object;
    }
    
    public int requestFusion(int param1Int) {
      return transitiveBoundaryFusion(param1Int);
    }
    
    public boolean tryOnNext(T param1T) {
      boolean bool = this.downstream.tryOnNext(param1T);
      try {
        this.onAfterNext.accept(param1T);
      } finally {
        param1T = null;
      } 
    }
  }
  
  static final class DoAfterSubscriber<T> extends BasicFuseableSubscriber<T, T> {
    final Consumer<? super T> onAfterNext;
    
    DoAfterSubscriber(Subscriber<? super T> param1Subscriber, Consumer<? super T> param1Consumer) {
      super(param1Subscriber);
      this.onAfterNext = param1Consumer;
    }
    
    public void onNext(T param1T) {
      if (this.done)
        return; 
      this.downstream.onNext(param1T);
      if (this.sourceMode == 0)
        try {
          this.onAfterNext.accept(param1T);
        } finally {
          param1T = null;
        }  
    }
    
    public T poll() throws Exception {
      Object object = this.qs.poll();
      if (object != null)
        this.onAfterNext.accept(object); 
      return (T)object;
    }
    
    public int requestFusion(int param1Int) {
      return transitiveBoundaryFusion(param1Int);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableDoAfterNext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */