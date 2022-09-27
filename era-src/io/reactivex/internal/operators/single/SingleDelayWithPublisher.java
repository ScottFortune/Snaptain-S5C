package io.reactivex.internal.operators.single;

import io.reactivex.FlowableSubscriber;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.observers.ResumeSingleObserver;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class SingleDelayWithPublisher<T, U> extends Single<T> {
  final Publisher<U> other;
  
  final SingleSource<T> source;
  
  public SingleDelayWithPublisher(SingleSource<T> paramSingleSource, Publisher<U> paramPublisher) {
    this.source = paramSingleSource;
    this.other = paramPublisher;
  }
  
  protected void subscribeActual(SingleObserver<? super T> paramSingleObserver) {
    this.other.subscribe((Subscriber)new OtherSubscriber<T, Object>(paramSingleObserver, this.source));
  }
  
  static final class OtherSubscriber<T, U> extends AtomicReference<Disposable> implements FlowableSubscriber<U>, Disposable {
    private static final long serialVersionUID = -8565274649390031272L;
    
    boolean done;
    
    final SingleObserver<? super T> downstream;
    
    final SingleSource<T> source;
    
    Subscription upstream;
    
    OtherSubscriber(SingleObserver<? super T> param1SingleObserver, SingleSource<T> param1SingleSource) {
      this.downstream = param1SingleObserver;
      this.source = param1SingleSource;
    }
    
    public void dispose() {
      this.upstream.cancel();
      DisposableHelper.dispose(this);
    }
    
    public boolean isDisposed() {
      return DisposableHelper.isDisposed(get());
    }
    
    public void onComplete() {
      if (this.done)
        return; 
      this.done = true;
      this.source.subscribe((SingleObserver)new ResumeSingleObserver(this, this.downstream));
    }
    
    public void onError(Throwable param1Throwable) {
      if (this.done) {
        RxJavaPlugins.onError(param1Throwable);
        return;
      } 
      this.done = true;
      this.downstream.onError(param1Throwable);
    }
    
    public void onNext(U param1U) {
      this.upstream.cancel();
      onComplete();
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


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/single/SingleDelayWithPublisher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */