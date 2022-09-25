package io.reactivex.internal.operators.mixed;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class MaybeFlatMapPublisher<T, R> extends Flowable<R> {
  final Function<? super T, ? extends Publisher<? extends R>> mapper;
  
  final MaybeSource<T> source;
  
  public MaybeFlatMapPublisher(MaybeSource<T> paramMaybeSource, Function<? super T, ? extends Publisher<? extends R>> paramFunction) {
    this.source = paramMaybeSource;
    this.mapper = paramFunction;
  }
  
  protected void subscribeActual(Subscriber<? super R> paramSubscriber) {
    this.source.subscribe(new FlatMapPublisherSubscriber<T, R>(paramSubscriber, this.mapper));
  }
  
  static final class FlatMapPublisherSubscriber<T, R> extends AtomicReference<Subscription> implements FlowableSubscriber<R>, MaybeObserver<T>, Subscription {
    private static final long serialVersionUID = -8948264376121066672L;
    
    final Subscriber<? super R> downstream;
    
    final Function<? super T, ? extends Publisher<? extends R>> mapper;
    
    final AtomicLong requested;
    
    Disposable upstream;
    
    FlatMapPublisherSubscriber(Subscriber<? super R> param1Subscriber, Function<? super T, ? extends Publisher<? extends R>> param1Function) {
      this.downstream = param1Subscriber;
      this.mapper = param1Function;
      this.requested = new AtomicLong();
    }
    
    public void cancel() {
      this.upstream.dispose();
      SubscriptionHelper.cancel(this);
    }
    
    public void onComplete() {
      this.downstream.onComplete();
    }
    
    public void onError(Throwable param1Throwable) {
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
    
    public void onSubscribe(Subscription param1Subscription) {
      SubscriptionHelper.deferredSetOnce(this, this.requested, param1Subscription);
    }
    
    public void onSuccess(T param1T) {
      try {
        return;
      } finally {
        param1T = null;
        Exceptions.throwIfFatal((Throwable)param1T);
        this.downstream.onError((Throwable)param1T);
      } 
    }
    
    public void request(long param1Long) {
      SubscriptionHelper.deferredRequest(this, this.requested, param1Long);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/mixed/MaybeFlatMapPublisher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */