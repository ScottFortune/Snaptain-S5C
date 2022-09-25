package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import io.reactivex.internal.util.ErrorMode;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

public final class FlowableConcatMapPublisher<T, R> extends Flowable<R> {
  final ErrorMode errorMode;
  
  final Function<? super T, ? extends Publisher<? extends R>> mapper;
  
  final int prefetch;
  
  final Publisher<T> source;
  
  public FlowableConcatMapPublisher(Publisher<T> paramPublisher, Function<? super T, ? extends Publisher<? extends R>> paramFunction, int paramInt, ErrorMode paramErrorMode) {
    this.source = paramPublisher;
    this.mapper = paramFunction;
    this.prefetch = paramInt;
    this.errorMode = paramErrorMode;
  }
  
  protected void subscribeActual(Subscriber<? super R> paramSubscriber) {
    if (FlowableScalarXMap.tryScalarXMapSubscribe(this.source, paramSubscriber, this.mapper))
      return; 
    this.source.subscribe(FlowableConcatMap.subscribe(paramSubscriber, this.mapper, this.prefetch, this.errorMode));
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableConcatMapPublisher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */