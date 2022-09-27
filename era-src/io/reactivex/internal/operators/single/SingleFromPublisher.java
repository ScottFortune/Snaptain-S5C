package io.reactivex.internal.operators.single;

import io.reactivex.FlowableSubscriber;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.NoSuchElementException;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class SingleFromPublisher<T> extends Single<T> {
  final Publisher<? extends T> publisher;
  
  public SingleFromPublisher(Publisher<? extends T> paramPublisher) {
    this.publisher = paramPublisher;
  }
  
  protected void subscribeActual(SingleObserver<? super T> paramSingleObserver) {
    this.publisher.subscribe((Subscriber)new ToSingleObserver<T>(paramSingleObserver));
  }
  
  static final class ToSingleObserver<T> implements FlowableSubscriber<T>, Disposable {
    volatile boolean disposed;
    
    boolean done;
    
    final SingleObserver<? super T> downstream;
    
    Subscription upstream;
    
    T value;
    
    ToSingleObserver(SingleObserver<? super T> param1SingleObserver) {
      this.downstream = param1SingleObserver;
    }
    
    public void dispose() {
      this.disposed = true;
      this.upstream.cancel();
    }
    
    public boolean isDisposed() {
      return this.disposed;
    }
    
    public void onComplete() {
      if (this.done)
        return; 
      this.done = true;
      T t = this.value;
      this.value = null;
      if (t == null) {
        this.downstream.onError(new NoSuchElementException("The source Publisher is empty"));
      } else {
        this.downstream.onSuccess(t);
      } 
    }
    
    public void onError(Throwable param1Throwable) {
      if (this.done) {
        RxJavaPlugins.onError(param1Throwable);
        return;
      } 
      this.done = true;
      this.value = null;
      this.downstream.onError(param1Throwable);
    }
    
    public void onNext(T param1T) {
      if (this.done)
        return; 
      if (this.value != null) {
        this.upstream.cancel();
        this.done = true;
        this.value = null;
        this.downstream.onError(new IndexOutOfBoundsException("Too many elements in the Publisher"));
      } else {
        this.value = param1T;
      } 
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      if (SubscriptionHelper.validate(this.upstream, param1Subscription)) {
        this.upstream = param1Subscription;
        this.downstream.onSubscribe(this);
        param1Subscription.request(Long.MAX_VALUE);
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/single/SingleFromPublisher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */