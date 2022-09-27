package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.subscribers.SinglePostCompleteSubscriber;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableConcatWithSingle<T> extends AbstractFlowableWithUpstream<T, T> {
  final SingleSource<? extends T> other;
  
  public FlowableConcatWithSingle(Flowable<T> paramFlowable, SingleSource<? extends T> paramSingleSource) {
    super(paramFlowable);
    this.other = paramSingleSource;
  }
  
  protected void subscribeActual(Subscriber<? super T> paramSubscriber) {
    this.source.subscribe((FlowableSubscriber)new ConcatWithSubscriber<T>(paramSubscriber, this.other));
  }
  
  static final class ConcatWithSubscriber<T> extends SinglePostCompleteSubscriber<T, T> implements SingleObserver<T> {
    private static final long serialVersionUID = -7346385463600070225L;
    
    SingleSource<? extends T> other;
    
    final AtomicReference<Disposable> otherDisposable;
    
    ConcatWithSubscriber(Subscriber<? super T> param1Subscriber, SingleSource<? extends T> param1SingleSource) {
      super(param1Subscriber);
      this.other = param1SingleSource;
      this.otherDisposable = new AtomicReference<Disposable>();
    }
    
    public void cancel() {
      super.cancel();
      DisposableHelper.dispose(this.otherDisposable);
    }
    
    public void onComplete() {
      this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
      SingleSource<? extends T> singleSource = this.other;
      this.other = null;
      singleSource.subscribe(this);
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


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableConcatWithSingle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */