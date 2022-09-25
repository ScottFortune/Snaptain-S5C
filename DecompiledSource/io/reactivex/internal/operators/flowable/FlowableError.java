package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.internal.functions.ObjectHelper;
import java.util.concurrent.Callable;
import org.reactivestreams.Subscriber;

public final class FlowableError<T> extends Flowable<T> {
  final Callable<? extends Throwable> errorSupplier;
  
  public FlowableError(Callable<? extends Throwable> paramCallable) {
    this.errorSupplier = paramCallable;
  }
  
  public void subscribeActual(Subscriber<? super T> paramSubscriber) {
    Throwable throwable;
    try {
      throwable = (Throwable)ObjectHelper.requireNonNull(this.errorSupplier.call(), "Callable returned null throwable. Null values are generally not allowed in 2.x operators and sources.");
    } finally {
      throwable = null;
    } 
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableError.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */