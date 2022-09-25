package io.reactivex.internal.operators.maybe;

import io.reactivex.Flowable;
import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.fuseable.HasUpstreamMaybeSource;
import io.reactivex.internal.subscriptions.DeferredScalarSubscription;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class MaybeToFlowable<T> extends Flowable<T> implements HasUpstreamMaybeSource<T> {
  final MaybeSource<T> source;
  
  public MaybeToFlowable(MaybeSource<T> paramMaybeSource) {
    this.source = paramMaybeSource;
  }
  
  public MaybeSource<T> source() {
    return this.source;
  }
  
  protected void subscribeActual(Subscriber<? super T> paramSubscriber) {
    this.source.subscribe(new MaybeToFlowableSubscriber<T>(paramSubscriber));
  }
  
  static final class MaybeToFlowableSubscriber<T> extends DeferredScalarSubscription<T> implements MaybeObserver<T> {
    private static final long serialVersionUID = 7603343402964826922L;
    
    Disposable upstream;
    
    MaybeToFlowableSubscriber(Subscriber<? super T> param1Subscriber) {
      super(param1Subscriber);
    }
    
    public void cancel() {
      super.cancel();
      this.upstream.dispose();
    }
    
    public void onComplete() {
      this.downstream.onComplete();
    }
    
    public void onError(Throwable param1Throwable) {
      this.downstream.onError(param1Throwable);
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      if (DisposableHelper.validate(this.upstream, param1Disposable)) {
        this.upstream = param1Disposable;
        this.downstream.onSubscribe((Subscription)this);
      } 
    }
    
    public void onSuccess(T param1T) {
      complete(param1T);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/maybe/MaybeToFlowable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */