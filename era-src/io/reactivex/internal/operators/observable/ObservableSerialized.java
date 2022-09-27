package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.observers.SerializedObserver;

public final class ObservableSerialized<T> extends AbstractObservableWithUpstream<T, T> {
  public ObservableSerialized(Observable<T> paramObservable) {
    super((ObservableSource<T>)paramObservable);
  }
  
  protected void subscribeActual(Observer<? super T> paramObserver) {
    this.source.subscribe((Observer)new SerializedObserver(paramObserver));
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableSerialized.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */