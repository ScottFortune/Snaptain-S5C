package io.reactivex.internal.operators.single;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class SingleFlatMapPublisher<T, R> extends Flowable<R> {
  final Function<? super T, ? extends Publisher<? extends R>> mapper;
  
  final SingleSource<T> source;
  
  public SingleFlatMapPublisher(SingleSource<T> paramSingleSource, Function<? super T, ? extends Publisher<? extends R>> paramFunction) {
    this.source = paramSingleSource;
    this.mapper = paramFunction;
  }
  
  protected void subscribeActual(Subscriber<? super R> paramSubscriber) {
    this.source.subscribe(new SingleFlatMapPublisherObserver<T, R>(paramSubscriber, this.mapper));
  }
  
  static final class SingleFlatMapPublisherObserver<S, T> extends AtomicLong implements SingleObserver<S>, FlowableSubscriber<T>, Subscription {
    private static final long serialVersionUID = 7759721921468635667L;
    
    Disposable disposable;
    
    final Subscriber<? super T> downstream;
    
    final Function<? super S, ? extends Publisher<? extends T>> mapper;
    
    final AtomicReference<Subscription> parent;
    
    SingleFlatMapPublisherObserver(Subscriber<? super T> param1Subscriber, Function<? super S, ? extends Publisher<? extends T>> param1Function) {
      this.downstream = param1Subscriber;
      this.mapper = param1Function;
      this.parent = new AtomicReference<Subscription>();
    }
    
    public void cancel() {
      this.disposable.dispose();
      SubscriptionHelper.cancel(this.parent);
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
      this.disposable = param1Disposable;
      this.downstream.onSubscribe(this);
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      SubscriptionHelper.deferredSetOnce(this.parent, this, param1Subscription);
    }
    
    public void onSuccess(S param1S) {
      try {
        return;
      } finally {
        param1S = null;
        Exceptions.throwIfFatal((Throwable)param1S);
        this.downstream.onError((Throwable)param1S);
      } 
    }
    
    public void request(long param1Long) {
      SubscriptionHelper.deferredRequest(this.parent, this, param1Long);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/single/SingleFlatMapPublisher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */