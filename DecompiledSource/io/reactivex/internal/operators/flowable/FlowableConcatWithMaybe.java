package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.subscribers.SinglePostCompleteSubscriber;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableConcatWithMaybe<T> extends AbstractFlowableWithUpstream<T, T> {
  final MaybeSource<? extends T> other;
  
  public FlowableConcatWithMaybe(Flowable<T> paramFlowable, MaybeSource<? extends T> paramMaybeSource) {
    super(paramFlowable);
    this.other = paramMaybeSource;
  }
  
  protected void subscribeActual(Subscriber<? super T> paramSubscriber) {
    this.source.subscribe((FlowableSubscriber)new ConcatWithSubscriber<T>(paramSubscriber, this.other));
  }
  
  static final class ConcatWithSubscriber<T> extends SinglePostCompleteSubscriber<T, T> implements MaybeObserver<T> {
    private static final long serialVersionUID = -7346385463600070225L;
    
    boolean inMaybe;
    
    MaybeSource<? extends T> other;
    
    final AtomicReference<Disposable> otherDisposable;
    
    ConcatWithSubscriber(Subscriber<? super T> param1Subscriber, MaybeSource<? extends T> param1MaybeSource) {
      super(param1Subscriber);
      this.other = param1MaybeSource;
      this.otherDisposable = new AtomicReference<Disposable>();
    }
    
    public void cancel() {
      super.cancel();
      DisposableHelper.dispose(this.otherDisposable);
    }
    
    public void onComplete() {
      if (this.inMaybe) {
        this.downstream.onComplete();
      } else {
        this.inMaybe = true;
        this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
        MaybeSource<? extends T> maybeSource = this.other;
        this.other = null;
        maybeSource.subscribe(this);
      } 
    }
    
    public void onError(Throwable param1Throwable) {
      this.downstream.onError(param1Throwable);
    }
    
    public void onNext(T param1T) {
      this.produced++;
      this.downstream.onNext(param1T);
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      DisposableHelper.setOnce(this.otherDisposable, param1Disposable);
    }
    
    public void onSuccess(T param1T) {
      complete(param1T);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableConcatWithMaybe.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */