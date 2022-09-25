package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.internal.fuseable.ScalarCallable;

public final class ObservableEmpty extends Observable<Object> implements ScalarCallable<Object> {
  public static final Observable<Object> INSTANCE = new ObservableEmpty();
  
  public Object call() {
    return null;
  }
  
  protected void subscribeActual(Observer<? super Object> paramObserver) {
    EmptyDisposable.complete(paramObserver);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableEmpty.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */