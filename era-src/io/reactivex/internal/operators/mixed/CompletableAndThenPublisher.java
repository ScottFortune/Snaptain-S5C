package io.reactivex.internal.operators.mixed;

import io.reactivex.CompletableObserver;
import io.reactivex.CompletableSource;
import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class CompletableAndThenPublisher<R> extends Flowable<R> {
  final Publisher<? extends R> other;
  
  final CompletableSource source;
  
  public CompletableAndThenPublisher(CompletableSource paramCompletableSource, Publisher<? extends R> paramPublisher) {
    this.source = paramCompletableSource;
    this.other = paramPublisher;
  }
  
  protected void subscribeActual(Subscriber<? super R> paramSubscriber) {
    this.source.subscribe(new AndThenPublisherSubscriber<R>(paramSubscriber, this.other));
  }
  
  static final class AndThenPublisherSubscriber<R> extends AtomicReference<Subscription> implements FlowableSubscriber<R>, CompletableObserver, Subscription {
    private static final long serialVersionUID = -8948264376121066672L;
    
    final Subscriber<? super R> downstream;
    
    Publisher<? extends R> other;
    
    final AtomicLong requested;
    
    Disposable upstream;
    
    AndThenPublisherSubscriber(Subscriber<? super R> param1Subscriber, Publisher<? extends R> param1Publisher) {
      this.downstream = param1Subscriber;
      this.other = param1Publisher;
      this.requested = new AtomicLong();
    }
    
    public void cancel() {
      this.upstream.dispose();
      SubscriptionHelper.cancel(this);
    }
    
    public void onComplete() {
      Publisher<? extends R> publisher = this.other;
      if (publisher == null) {
        this.downstream.onComplete();
      } else {
        this.other = null;
        publisher.subscribe((Subscriber)this);
      } 
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
    
    public void request(long param1Long) {
      SubscriptionHelper.deferredRequest(this, this.requested, param1Long);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/mixed/CompletableAndThenPublisher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */