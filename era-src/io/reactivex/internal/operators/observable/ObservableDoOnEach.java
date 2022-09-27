package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.plugins.RxJavaPlugins;

public final class ObservableDoOnEach<T> extends AbstractObservableWithUpstream<T, T> {
  final Action onAfterTerminate;
  
  final Action onComplete;
  
  final Consumer<? super Throwable> onError;
  
  final Consumer<? super T> onNext;
  
  public ObservableDoOnEach(ObservableSource<T> paramObservableSource, Consumer<? super T> paramConsumer, Consumer<? super Throwable> paramConsumer1, Action paramAction1, Action paramAction2) {
    super(paramObservableSource);
    this.onNext = paramConsumer;
    this.onError = paramConsumer1;
    this.onComplete = paramAction1;
    this.onAfterTerminate = paramAction2;
  }
  
  public void subscribeActual(Observer<? super T> paramObserver) {
    this.source.subscribe(new DoOnEachObserver<T>(paramObserver, this.onNext, this.onError, this.onComplete, this.onAfterTerminate));
  }
  
  static final class DoOnEachObserver<T> implements Observer<T>, Disposable {
    boolean done;
    
    final Observer<? super T> downstream;
    
    final Action onAfterTerminate;
    
    final Action onComplete;
    
    final Consumer<? super Throwable> onError;
    
    final Consumer<? super T> onNext;
    
    Disposable upstream;
    
    DoOnEachObserver(Observer<? super T> param1Observer, Consumer<? super T> param1Consumer, Consumer<? super Throwable> param1Consumer1, Action param1Action1, Action param1Action2) {
      this.downstream = param1Observer;
      this.onNext = param1Consumer;
      this.onError = param1Consumer1;
      this.onComplete = param1Action1;
      this.onAfterTerminate = param1Action2;
    }
    
    public void dispose() {
      this.upstream.dispose();
    }
    
    public boolean isDisposed() {
      return this.upstream.isDisposed();
    }
    
    public void onComplete() {
      if (this.done)
        return; 
      try {
        this.onComplete.run();
        return;
      } finally {
        Exception exception = null;
        Exceptions.throwIfFatal(exception);
        onError(exception);
      } 
    }
    
    public void onError(Throwable param1Throwable) {
      CompositeException compositeException;
      if (this.done) {
        RxJavaPlugins.onError(param1Throwable);
        return;
      } 
      this.done = true;
    }
    
    public void onNext(T param1T) {
      if (this.done)
        return; 
      try {
        return;
      } finally {
        param1T = null;
        Exceptions.throwIfFatal((Throwable)param1T);
        this.upstream.dispose();
        onError((Throwable)param1T);
      } 
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      if (DisposableHelper.validate(this.upstream, param1Disposable)) {
        this.upstream = param1Disposable;
        this.downstream.onSubscribe(this);
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableDoOnEach.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */