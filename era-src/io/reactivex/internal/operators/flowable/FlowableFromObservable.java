package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableFromObservable<T> extends Flowable<T> {
  private final Observable<T> upstream;
  
  public FlowableFromObservable(Observable<T> paramObservable) {
    this.upstream = paramObservable;
  }
  
  protected void subscribeActual(Subscriber<? super T> paramSubscriber) {
    this.upstream.subscribe(new SubscriberObserver<T>(paramSubscriber));
  }
  
  static final class SubscriberObserver<T> implements Observer<T>, Subscription {
    final Subscriber<? super T> downstream;
    
    Disposable upstream;
    
    SubscriberObserver(Subscriber<? super T> param1Subscriber) {
      this.downstream = param1Subscriber;
    }
    
    public void cancel() {
      this.upstream.dispose();
    }
    
    public void onComplete() {
      this.downstream.onComplete();
    }
    
    public void onError(Throwable param1Throwable) {
      this.downstream.onError(param1Throwable);
    }
    
    public void onNext(T param1T) {
      this.downstream.onNext(param1T);
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      this.upstream = param1Disposable;
      this.downstream.onSubscribe(this);
    }
    
    public void request(long param1Long) {}
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableFromObservable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */