package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import io.reactivex.internal.util.ErrorMode;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

public final class FlowableConcatMapEagerPublisher<T, R> extends Flowable<R> {
  final ErrorMode errorMode;
  
  final Function<? super T, ? extends Publisher<? extends R>> mapper;
  
  final int maxConcurrency;
  
  final int prefetch;
  
  final Publisher<T> source;
  
  public FlowableConcatMapEagerPublisher(Publisher<T> paramPublisher, Function<? super T, ? extends Publisher<? extends R>> paramFunction, int paramInt1, int paramInt2, ErrorMode paramErrorMode) {
    this.source = paramPublisher;
    this.mapper = paramFunction;
    this.maxConcurrency = paramInt1;
    this.prefetch = paramInt2;
    this.errorMode = paramErrorMode;
  }
  
  protected void subscribeActual(Subscriber<? super R> paramSubscriber) {
    this.source.subscribe((Subscriber)new FlowableConcatMapEager.ConcatMapEagerDelayErrorSubscriber<T, R>(paramSubscriber, this.mapper, this.maxConcurrency, this.prefetch, this.errorMode));
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableConcatMapEagerPublisher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */