package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableOperator;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.plugins.RxJavaPlugins;
import org.reactivestreams.Subscriber;

public final class FlowableLift<R, T> extends AbstractFlowableWithUpstream<T, R> {
  final FlowableOperator<? extends R, ? super T> operator;
  
  public FlowableLift(Flowable<T> paramFlowable, FlowableOperator<? extends R, ? super T> paramFlowableOperator) {
    super(paramFlowable);
    this.operator = paramFlowableOperator;
  }
  
  public void subscribeActual(Subscriber<? super R> paramSubscriber) {
    try {
      paramSubscriber = this.operator.apply(paramSubscriber);
      if (paramSubscriber != null)
        return; 
      NullPointerException nullPointerException = new NullPointerException();
      StringBuilder stringBuilder = new StringBuilder();
      this();
      stringBuilder.append("Operator ");
      stringBuilder.append(this.operator);
      stringBuilder.append(" returned a null Subscriber");
      this(stringBuilder.toString());
      throw nullPointerException;
    } catch (NullPointerException nullPointerException) {
      throw nullPointerException;
    } finally {
      paramSubscriber = null;
      Exceptions.throwIfFatal((Throwable)paramSubscriber);
      RxJavaPlugins.onError((Throwable)paramSubscriber);
      NullPointerException nullPointerException = new NullPointerException("Actually not, but can't throw other exceptions due to RS");
      nullPointerException.initCause((Throwable)paramSubscriber);
    } 
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableLift.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */