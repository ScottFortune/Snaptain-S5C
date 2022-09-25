package io.reactivex.subscribers;

import io.reactivex.FlowableSubscriber;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class SafeSubscriber<T> implements FlowableSubscriber<T>, Subscription {
  boolean done;
  
  final Subscriber<? super T> downstream;
  
  Subscription upstream;
  
  public SafeSubscriber(Subscriber<? super T> paramSubscriber) {
    this.downstream = paramSubscriber;
  }
  
  public void cancel() {
    try {
      this.upstream.cancel();
    } finally {
      Exception exception = null;
      Exceptions.throwIfFatal(exception);
    } 
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
    NullPointerException nullPointerException;
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
  
  public void onSubscribe(Subscription paramSubscription) {
    if (SubscriptionHelper.validate(this.upstream, paramSubscription)) {
      this.upstream = paramSubscription;
      try {
        this.downstream.onSubscribe(this);
      } finally {
        Exception exception = null;
        Exceptions.throwIfFatal(exception);
        this.done = true;
      } 
    } 
  }
  
  public void request(long paramLong) {
    try {
      this.upstream.request(paramLong);
    } finally {
      Exception exception = null;
      Exceptions.throwIfFatal(exception);
    } 
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/subscribers/SafeSubscriber.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */