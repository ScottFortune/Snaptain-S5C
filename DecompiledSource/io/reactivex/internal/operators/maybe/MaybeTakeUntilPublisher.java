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

public final class MaybeTakeUntilPublisher<T, U> extends AbstractMaybeWithUpstream<T, T> {
  final Publisher<U> other;
  
  public MaybeTakeUntilPublisher(MaybeSource<T> paramMaybeSource, Publisher<U> paramPublisher) {
    super(paramMaybeSource);
    this.other = paramPublisher;
  }
  
  protected void subscribeActual(MaybeObserver<? super T> paramMaybeObserver) {
    TakeUntilMainMaybeObserver<T, Object> takeUntilMainMaybeObserver = new TakeUntilMainMaybeObserver<T, Object>(paramMaybeObserver);
    paramMaybeObserver.onSubscribe(takeUntilMainMaybeObserver);
    this.other.subscribe((Subscriber)takeUntilMainMaybeObserver.other);
    this.source.subscribe(takeUntilMainMaybeObserver);
  }
  
  static final class TakeUntilMainMaybeObserver<T, U> extends AtomicReference<Disposable> implements MaybeObserver<T>, Disposable {
    private static final long serialVersionUID = -2187421758664251153L;
    
    final MaybeObserver<? super T> downstream;
    
    final TakeUntilOtherMaybeObserver<U> other;
    
    TakeUntilMainMaybeObserver(MaybeObserver<? super T> param1MaybeObserver) {
      this.downstream = param1MaybeObserver;
      this.other = new TakeUntilOtherMaybeObserver<U>(this);
    }
    
    public void dispose() {
      DisposableHelper.dispose(this);
      SubscriptionHelper.cancel(this.other);
    }
    
    public boolean isDisposed() {
      return DisposableHelper.isDisposed(get());
    }
    
    public void onComplete() {
      SubscriptionHelper.cancel(this.other);
      if (getAndSet((Disposable)DisposableHelper.DISPOSED) != DisposableHelper.DISPOSED)
        this.downstream.onComplete(); 
    }
    
    public void onError(Throwable param1Throwable) {
      SubscriptionHelper.cancel(this.other);
      if (getAndSet((Disposable)DisposableHelper.DISPOSED) != DisposableHelper.DISPOSED) {
        this.downstream.onError(param1Throwable);
      } else {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      DisposableHelper.setOnce(this, param1Disposable);
    }
    
    public void onSuccess(T param1T) {
      SubscriptionHelper.cancel(this.other);
      if (getAndSet((Disposable)DisposableHelper.DISPOSED) != DisposableHelper.DISPOSED)
        this.downstream.onSuccess(param1T); 
    }
    
    void otherComplete() {
      if (DisposableHelper.dispose(this))
        this.downstream.onComplete(); 
    }
    
    void otherError(Throwable param1Throwable) {
      if (DisposableHelper.dispose(this)) {
        this.downstream.onError(param1Throwable);
      } else {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
    
    static final class TakeUntilOtherMaybeObserver<U> extends AtomicReference<Subscription> implements FlowableSubscriber<U> {
      private static final long serialVersionUID = -1266041316834525931L;
      
      final MaybeTakeUntilPublisher.TakeUntilMainMaybeObserver<?, U> parent;
      
      TakeUntilOtherMaybeObserver(MaybeTakeUntilPublisher.TakeUntilMainMaybeObserver<?, U> param2TakeUntilMainMaybeObserver) {
        this.parent = param2TakeUntilMainMaybeObserver;
      }
      
      public void onComplete() {
        this.parent.otherComplete();
      }
      
      public void onError(Throwable param2Throwable) {
        this.parent.otherError(param2Throwable);
      }
      
      public void onNext(Object param2Object) {
        SubscriptionHelper.cancel(this);
        this.parent.otherComplete();
      }
      
      public void onSubscribe(Subscription param2Subscription) {
        SubscriptionHelper.setOnce(this, param2Subscription, Long.MAX_VALUE);
      }
    }
  }
  
  static final class TakeUntilOtherMaybeObserver<U> extends AtomicReference<Subscription> implements FlowableSubscriber<U> {
    private static final long serialVersionUID = -1266041316834525931L;
    
    final MaybeTakeUntilPublisher.TakeUntilMainMaybeObserver<?, U> parent;
    
    TakeUntilOtherMaybeObserver(MaybeTakeUntilPublisher.TakeUntilMainMaybeObserver<?, U> param1TakeUntilMainMaybeObserver) {
      this.parent = param1TakeUntilMainMaybeObserver;
    }
    
    public void onComplete() {
      this.parent.otherComplete();
    }
    
    public void onError(Throwable param1Throwable) {
      this.parent.otherError(param1Throwable);
    }
    
    public void onNext(Object param1Object) {
      SubscriptionHelper.cancel(this);
      this.parent.otherComplete();
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      SubscriptionHelper.setOnce(this, param1Subscription, Long.MAX_VALUE);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/maybe/MaybeTakeUntilPublisher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */