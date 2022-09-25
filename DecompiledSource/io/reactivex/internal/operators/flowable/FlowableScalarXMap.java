package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.subscriptions.EmptySubscription;
import io.reactivex.plugins.RxJavaPlugins;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

public final class FlowableScalarXMap {
  private FlowableScalarXMap() {
    throw new IllegalStateException("No instances!");
  }
  
  public static <T, U> Flowable<U> scalarXMap(T paramT, Function<? super T, ? extends Publisher<? extends U>> paramFunction) {
    return RxJavaPlugins.onAssembly(new ScalarXMapFlowable<T, U>(paramT, paramFunction));
  }
  
  public static <T, R> boolean tryScalarXMapSubscribe(Publisher<T> paramPublisher, Subscriber<? super R> paramSubscriber, Function<? super T, ? extends Publisher<? extends R>> paramFunction) {
    if (paramPublisher instanceof java.util.concurrent.Callable)
      try {
      
      } finally {
        paramPublisher = null;
        Exceptions.throwIfFatal((Throwable)paramPublisher);
        EmptySubscription.error((Throwable)paramPublisher, paramSubscriber);
      }  
    return false;
  }
  
  static final class ScalarXMapFlowable<T, R> extends Flowable<R> {
    final Function<? super T, ? extends Publisher<? extends R>> mapper;
    
    final T value;
    
    ScalarXMapFlowable(T param1T, Function<? super T, ? extends Publisher<? extends R>> param1Function) {
      this.value = param1T;
      this.mapper = param1Function;
    }
    
    public void subscribeActual(Subscriber<? super R> param1Subscriber) {
      try {
        return;
      } finally {
        Exception exception = null;
        EmptySubscription.error(exception, param1Subscriber);
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableScalarXMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */