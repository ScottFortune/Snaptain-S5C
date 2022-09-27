package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.subjects.PublishSubject;
import java.util.concurrent.atomic.AtomicReference;

public final class ObservablePublishSelector<T, R> extends AbstractObservableWithUpstream<T, R> {
  final Function<? super Observable<T>, ? extends ObservableSource<R>> selector;
  
  public ObservablePublishSelector(ObservableSource<T> paramObservableSource, Function<? super Observable<T>, ? extends ObservableSource<R>> paramFunction) {
    super(paramObservableSource);
    this.selector = paramFunction;
  }
  
  protected void subscribeActual(Observer<? super R> paramObserver) {
    PublishSubject<?> publishSubject = PublishSubject.create();
    try {
      ObservableSource observableSource = (ObservableSource)ObjectHelper.requireNonNull(this.selector.apply(publishSubject), "The selector returned a null ObservableSource");
      return;
    } finally {
      Exception exception = null;
      Exceptions.throwIfFatal(exception);
      EmptyDisposable.error(exception, paramObserver);
    } 
  }
  
  static final class SourceObserver<T, R> implements Observer<T> {
    final PublishSubject<T> subject;
    
    final AtomicReference<Disposable> target;
    
    SourceObserver(PublishSubject<T> param1PublishSubject, AtomicReference<Disposable> param1AtomicReference) {
      this.subject = param1PublishSubject;
      this.target = param1AtomicReference;
    }
    
    public void onComplete() {
      this.subject.onComplete();
    }
    
    public void onError(Throwable param1Throwable) {
      this.subject.onError(param1Throwable);
    }
    
    public void onNext(T param1T) {
      this.subject.onNext(param1T);
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      DisposableHelper.setOnce(this.target, param1Disposable);
    }
  }
  
  static final class TargetObserver<T, R> extends AtomicReference<Disposable> implements Observer<R>, Disposable {
    private static final long serialVersionUID = 854110278590336484L;
    
    final Observer<? super R> downstream;
    
    Disposable upstream;
    
    TargetObserver(Observer<? super R> param1Observer) {
      this.downstream = param1Observer;
    }
    
    public void dispose() {
      this.upstream.dispose();
      DisposableHelper.dispose(this);
    }
    
    public boolean isDisposed() {
      return this.upstream.isDisposed();
    }
    
    public void onComplete() {
      DisposableHelper.dispose(this);
      this.downstream.onComplete();
    }
    
    public void onError(Throwable param1Throwable) {
      DisposableHelper.dispose(this);
      this.downstream.onError(param1Throwable);
    }
    
    public void onNext(R param1R) {
      this.downstream.onNext(param1R);
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      if (DisposableHelper.validate(this.upstream, param1Disposable)) {
        this.upstream = param1Disposable;
        this.downstream.onSubscribe(this);
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservablePublishSelector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */