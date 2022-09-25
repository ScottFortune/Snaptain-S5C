package io.reactivex.observers;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.plugins.RxJavaPlugins;

public final class SafeObserver<T> implements Observer<T>, Disposable {
  boolean done;
  
  final Observer<? super T> downstream;
  
  Disposable upstream;
  
  public SafeObserver(Observer<? super T> paramObserver) {
    this.downstream = paramObserver;
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
    this.done = true;
    if (this.upstream == null) {
      onCompleteNoSubscription();
      return;
    } 
  }
  
  void onCompleteNoSubscription() {
    NullPointerException nullPointerException = new NullPointerException("Subscription not set!");
    try {
      return;
    } finally {
      Exception exception = null;
      Exceptions.throwIfFatal(exception);
      RxJavaPlugins.onError((Throwable)new CompositeException(new Throwable[] { nullPointerException, exception }));
    } 
  }
  
  public void onError(Throwable paramThrowable) {
    if (this.done) {
      RxJavaPlugins.onError(paramThrowable);
      return;
    } 
    this.done = true;
    if (this.upstream == null) {
      NullPointerException nullPointerException = new NullPointerException("Subscription not set!");
      try {
        return;
      } finally {
        Exception exception = null;
        Exceptions.throwIfFatal(exception);
        RxJavaPlugins.onError((Throwable)new CompositeException(new Throwable[] { paramThrowable, nullPointerException, exception }));
      } 
    } 
    Throwable throwable = paramThrowable;
    if (paramThrowable == null);
  }
  
  public void onNext(T paramT) {
    if (this.done)
      return; 
    if (this.upstream == null) {
      onNextNoSubscription();
      return;
    } 
    if (paramT == null);
  }
  
  void onNextNoSubscription() {
    this.done = true;
    NullPointerException nullPointerException = new NullPointerException("Subscription not set!");
    try {
      return;
    } finally {
      Exception exception = null;
      Exceptions.throwIfFatal(exception);
      RxJavaPlugins.onError((Throwable)new CompositeException(new Throwable[] { nullPointerException, exception }));
    } 
  }
  
  public void onSubscribe(Disposable paramDisposable) {
    if (DisposableHelper.validate(this.upstream, paramDisposable)) {
      this.upstream = paramDisposable;
      try {
        this.downstream.onSubscribe(this);
      } finally {
        Exception exception = null;
        Exceptions.throwIfFatal(exception);
        this.done = true;
      } 
    } 
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/observers/SafeObserver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */