package io.reactivex.internal.util;

import io.reactivex.CompletableObserver;
import io.reactivex.FlowableSubscriber;
import io.reactivex.MaybeObserver;
import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.plugins.RxJavaPlugins;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public enum EmptyComponent implements FlowableSubscriber<Object>, Observer<Object>, MaybeObserver<Object>, SingleObserver<Object>, CompletableObserver, Subscription, Disposable {
  INSTANCE;
  
  static {
    $VALUES = new EmptyComponent[] { INSTANCE };
  }
  
  public static <T> Observer<T> asObserver() {
    return INSTANCE;
  }
  
  public static <T> Subscriber<T> asSubscriber() {
    return (Subscriber<T>)INSTANCE;
  }
  
  public void cancel() {}
  
  public void dispose() {}
  
  public boolean isDisposed() {
    return true;
  }
  
  public void onComplete() {}
  
  public void onError(Throwable paramThrowable) {
    RxJavaPlugins.onError(paramThrowable);
  }
  
  public void onNext(Object paramObject) {}
  
  public void onSubscribe(Disposable paramDisposable) {
    paramDisposable.dispose();
  }
  
  public void onSubscribe(Subscription paramSubscription) {
    paramSubscription.cancel();
  }
  
  public void onSuccess(Object paramObject) {}
  
  public void request(long paramLong) {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/util/EmptyComponent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */