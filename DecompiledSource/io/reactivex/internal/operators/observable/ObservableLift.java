package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableOperator;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.plugins.RxJavaPlugins;

public final class ObservableLift<R, T> extends AbstractObservableWithUpstream<T, R> {
  final ObservableOperator<? extends R, ? super T> operator;
  
  public ObservableLift(ObservableSource<T> paramObservableSource, ObservableOperator<? extends R, ? super T> paramObservableOperator) {
    super(paramObservableSource);
    this.operator = paramObservableOperator;
  }
  
  public void subscribeActual(Observer<? super R> paramObserver) {
    try {
      Observer observer2 = this.operator.apply(paramObserver);
      StringBuilder stringBuilder = new StringBuilder();
      this();
      return;
    } catch (NullPointerException nullPointerException) {
      throw nullPointerException;
    } finally {
      Exception exception = null;
      Exceptions.throwIfFatal(exception);
      RxJavaPlugins.onError(exception);
      NullPointerException nullPointerException = new NullPointerException("Actually not, but can't throw other exceptions due to RS");
      nullPointerException.initCause(exception);
    } 
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableLift.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */