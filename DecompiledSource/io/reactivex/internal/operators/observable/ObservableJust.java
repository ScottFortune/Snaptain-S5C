package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.fuseable.ScalarCallable;

public final class ObservableJust<T> extends Observable<T> implements ScalarCallable<T> {
  private final T value;
  
  public ObservableJust(T paramT) {
    this.value = paramT;
  }
  
  public T call() {
    return this.value;
  }
  
  protected void subscribeActual(Observer<? super T> paramObserver) {
    ObservableScalarXMap.ScalarDisposable<T> scalarDisposable = new ObservableScalarXMap.ScalarDisposable<T>(paramObserver, this.value);
    paramObserver.onSubscribe((Disposable)scalarDisposable);
    scalarDisposable.run();
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableJust.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */