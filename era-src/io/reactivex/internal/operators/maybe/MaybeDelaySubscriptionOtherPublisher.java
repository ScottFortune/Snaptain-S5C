package io.reactivex.internal.operators.maybe;

import io.reactivex.FlowableSubscriber;
import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class MaybeDelaySubscriptionOtherPublisher<T, U> extends AbstractMaybeWithUpstream<T, T> {
  final Publisher<U> other;
  
  public MaybeDelaySubscriptionOtherPublisher(MaybeSource<T> paramMaybeSource, Publisher<U> paramPublisher) {
    super(paramMaybeSource);
    this.other = paramPublisher;
  }
  
  protected void subscribeActual(MaybeObserver<? super T> paramMaybeObserver) {
    this.other.subscribe((Subscriber)new OtherSubscriber<T>(paramMaybeObserver, this.source));
  }
  
  static final class DelayMaybeObserver<T> extends AtomicReference<Disposable> implements MaybeObserver<T> {
    private static final long serialVersionUID = 706635022205076709L;
    
    final MaybeObserver<? super T> downstream;
    
    DelayMaybeObserver(MaybeObserver<? super T> param1MaybeObserver) {
      this.downstream = param1MaybeObserver;
    }
    
    public void onComplete() {
      this.downstream.onComplete();
    }
    
    public void onError(Throwable param1Throwable) {
      this.downstream.onError(param1Throwable);
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      DisposableHelper.setOnce(this, param1Disposable);
    }
    
    public void onSuccess(T param1T) {
      this.downstream.onSuccess(param1T);
    }
  }
  
  static final class OtherSubscriber<T> implements FlowableSubscriber<Object>, Disposable {
    final MaybeDelaySubscriptionOtherPublisher.DelayMaybeObserver<T> main;
    
    MaybeSource<T> source;
    
    Subscription upstream;
    
    OtherSubscriber(MaybeObserver<? super T> param1MaybeObserver, MaybeSource<T> param1MaybeSource) {
      this.main = new MaybeDelaySubscriptionOtherPublisher.DelayMaybeObserver<T>(param1MaybeObserver);
      this.source = param1MaybeSource;
    }
    
    public void dispose() {
      this.upstream.cancel();
      this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
      DisposableHelper.dispose(this.main);
    }
    
    public boolean isDisposed() {
      return DisposableHelper.isDisposed(this.main.get());
    }
    
    public void onComplete() {
      if (this.upstream != SubscriptionHelper.CANCELLED) {
        this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
        subscribeNext();
      } 
    }
    
    public void onError(Throwable param1Throwable) {
      if (this.upstream != SubscriptionHelper.CANCELLED) {
        this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
        this.main.downstream.onError(param1Throwable);
      } else {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
    
    public void onNext(Object param1Object) {
      if (this.upstream != SubscriptionHelper.CANCELLED) {
        this.upstream.cancel();
        this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
        subscribeNext();
      } 
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      if (SubscriptionHelper.validate(this.upstream, param1Subscription)) {
        this.upstream = param1Subscription;
        this.main.downstream.onSubscribe(this);
        param1Subscription.request(Long.MAX_VALUE);
      } 
    }
    
    void subscribeNext() {
      MaybeSource<T> maybeSource = this.source;
      this.source = null;
      maybeSource.subscribe(this.main);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/maybe/MaybeDelaySubscriptionOtherPublisher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */