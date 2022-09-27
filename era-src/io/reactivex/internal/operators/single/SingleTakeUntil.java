package io.reactivex.internal.operators.single;

import io.reactivex.FlowableSubscriber;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.CancellationException;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class SingleTakeUntil<T, U> extends Single<T> {
  final Publisher<U> other;
  
  final SingleSource<T> source;
  
  public SingleTakeUntil(SingleSource<T> paramSingleSource, Publisher<U> paramPublisher) {
    this.source = paramSingleSource;
    this.other = paramPublisher;
  }
  
  protected void subscribeActual(SingleObserver<? super T> paramSingleObserver) {
    TakeUntilMainObserver<T> takeUntilMainObserver = new TakeUntilMainObserver<T>(paramSingleObserver);
    paramSingleObserver.onSubscribe(takeUntilMainObserver);
    this.other.subscribe((Subscriber)takeUntilMainObserver.other);
    this.source.subscribe(takeUntilMainObserver);
  }
  
  static final class TakeUntilMainObserver<T> extends AtomicReference<Disposable> implements SingleObserver<T>, Disposable {
    private static final long serialVersionUID = -622603812305745221L;
    
    final SingleObserver<? super T> downstream;
    
    final SingleTakeUntil.TakeUntilOtherSubscriber other;
    
    TakeUntilMainObserver(SingleObserver<? super T> param1SingleObserver) {
      this.downstream = param1SingleObserver;
      this.other = new SingleTakeUntil.TakeUntilOtherSubscriber(this);
    }
    
    public void dispose() {
      DisposableHelper.dispose(this);
      this.other.dispose();
    }
    
    public boolean isDisposed() {
      return DisposableHelper.isDisposed(get());
    }
    
    public void onError(Throwable param1Throwable) {
      this.other.dispose();
      if (get() != DisposableHelper.DISPOSED && getAndSet((Disposable)DisposableHelper.DISPOSED) != DisposableHelper.DISPOSED) {
        this.downstream.onError(param1Throwable);
        return;
      } 
      RxJavaPlugins.onError(param1Throwable);
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      DisposableHelper.setOnce(this, param1Disposable);
    }
    
    public void onSuccess(T param1T) {
      this.other.dispose();
      if (getAndSet((Disposable)DisposableHelper.DISPOSED) != DisposableHelper.DISPOSED)
        this.downstream.onSuccess(param1T); 
    }
    
    void otherError(Throwable param1Throwable) {
      if (get() != DisposableHelper.DISPOSED) {
        Disposable disposable = getAndSet((Disposable)DisposableHelper.DISPOSED);
        if (disposable != DisposableHelper.DISPOSED) {
          if (disposable != null)
            disposable.dispose(); 
          this.downstream.onError(param1Throwable);
          return;
        } 
      } 
      RxJavaPlugins.onError(param1Throwable);
    }
  }
  
  static final class TakeUntilOtherSubscriber extends AtomicReference<Subscription> implements FlowableSubscriber<Object> {
    private static final long serialVersionUID = 5170026210238877381L;
    
    final SingleTakeUntil.TakeUntilMainObserver<?> parent;
    
    TakeUntilOtherSubscriber(SingleTakeUntil.TakeUntilMainObserver<?> param1TakeUntilMainObserver) {
      this.parent = param1TakeUntilMainObserver;
    }
    
    public void dispose() {
      SubscriptionHelper.cancel(this);
    }
    
    public void onComplete() {
      if (get() != SubscriptionHelper.CANCELLED) {
        lazySet((Subscription)SubscriptionHelper.CANCELLED);
        this.parent.otherError(new CancellationException());
      } 
    }
    
    public void onError(Throwable param1Throwable) {
      this.parent.otherError(param1Throwable);
    }
    
    public void onNext(Object param1Object) {
      if (SubscriptionHelper.cancel(this))
        this.parent.otherError(new CancellationException()); 
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      SubscriptionHelper.setOnce(this, param1Subscription, Long.MAX_VALUE);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/single/SingleTakeUntil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */