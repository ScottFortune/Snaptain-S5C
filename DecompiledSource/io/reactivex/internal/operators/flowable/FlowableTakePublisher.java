package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

public final class FlowableTakePublisher<T> extends Flowable<T> {
  final long limit;
  
  final Publisher<T> source;
  
  public FlowableTakePublisher(Publisher<T> paramPublisher, long paramLong) {
    this.source = paramPublisher;
    this.limit = paramLong;
  }
  
  protected void subscribeActual(Subscriber<? super T> paramSubscriber) {
    this.source.subscribe((Subscriber)new FlowableTake.TakeSubscriber<T>(paramSubscriber, this.limit));
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableTakePublisher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */