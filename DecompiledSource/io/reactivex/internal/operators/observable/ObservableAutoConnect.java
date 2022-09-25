package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observables.ConnectableObservable;
import java.util.concurrent.atomic.AtomicInteger;

public final class ObservableAutoConnect<T> extends Observable<T> {
  final AtomicInteger clients;
  
  final Consumer<? super Disposable> connection;
  
  final int numberOfObservers;
  
  final ConnectableObservable<? extends T> source;
  
  public ObservableAutoConnect(ConnectableObservable<? extends T> paramConnectableObservable, int paramInt, Consumer<? super Disposable> paramConsumer) {
    this.source = paramConnectableObservable;
    this.numberOfObservers = paramInt;
    this.connection = paramConsumer;
    this.clients = new AtomicInteger();
  }
  
  public void subscribeActual(Observer<? super T> paramObserver) {
    this.source.subscribe(paramObserver);
    if (this.clients.incrementAndGet() == this.numberOfObservers)
      this.source.connect(this.connection); 
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableAutoConnect.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */