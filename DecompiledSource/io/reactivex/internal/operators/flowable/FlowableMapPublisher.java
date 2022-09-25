package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

public final class FlowableMapPublisher<T, U> extends Flowable<U> {
  final Function<? super T, ? extends U> mapper;
  
  final Publisher<T> source;
  
  public FlowableMapPublisher(Publisher<T> paramPublisher, Function<? super T, ? extends U> paramFunction) {
    this.source = paramPublisher;
    this.mapper = paramFunction;
  }
  
  protected void subscribeActual(Subscriber<? super U> paramSubscriber) {
    this.source.subscribe((Subscriber)new FlowableMap.MapSubscriber<T, U>(paramSubscriber, this.mapper));
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableMapPublisher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */