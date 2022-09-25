package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.subscriptions.EmptySubscription;
import java.util.concurrent.Callable;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

public final class FlowableDefer<T> extends Flowable<T> {
  final Callable<? extends Publisher<? extends T>> supplier;
  
  public FlowableDefer(Callable<? extends Publisher<? extends T>> paramCallable) {
    this.supplier = paramCallable;
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
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableDefer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */