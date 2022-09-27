package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.fuseable.HasUpstreamPublisher;
import org.reactivestreams.Publisher;

abstract class AbstractFlowableWithUpstream<T, R> extends Flowable<R> implements HasUpstreamPublisher<T> {
  protected final Flowable<T> source;
  
  AbstractFlowableWithUpstream(Flowable<T> paramFlowable) {
    this.source = (Flowable<T>)ObjectHelper.requireNonNull(paramFlowable, "source is null");
  }
  
  public final Publisher<T> source() {
    return (Publisher<T>)this.source;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/AbstractFlowableWithUpstream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */