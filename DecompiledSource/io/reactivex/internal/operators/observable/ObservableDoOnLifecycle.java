package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.observers.DisposableLambdaObserver;

public final class ObservableDoOnLifecycle<T> extends AbstractObservableWithUpstream<T, T> {
  private final Action onDispose;
  
  private final Consumer<? super Disposable> onSubscribe;
  
  public ObservableDoOnLifecycle(Observable<T> paramObservable, Consumer<? super Disposable> paramConsumer, Action paramAction) {
    super((ObservableSource<T>)paramObservable);
    this.onSubscribe = paramConsumer;
    this.onDispose = paramAction;
  }
  
  protected void subscribeActual(Observer<? super T> paramObserver) {
    this.source.subscribe((Observer)new DisposableLambdaObserver(paramObserver, this.onSubscribe, this.onDispose));
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableDoOnLifecycle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */