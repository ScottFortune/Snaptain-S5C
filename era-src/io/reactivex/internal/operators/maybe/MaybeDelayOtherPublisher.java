package io.reactivex.internal.operators.maybe;

import io.reactivex.FlowableSubscriber;
import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class MaybeDelayOtherPublisher<T, U> extends AbstractMaybeWithUpstream<T, T> {
  final Publisher<U> other;
  
  public MaybeDelayOtherPublisher(MaybeSource<T> paramMaybeSource, Publisher<U> paramPublisher) {
    super(paramMaybeSource);
    this.other = paramPublisher;
  }
  
  protected void subscribeActual(MaybeObserver<? super T> paramMaybeObserver) {
    this.source.subscribe(new DelayMaybeObserver<T, U>(paramMaybeObserver, this.other));
  }
  
  static final class DelayMaybeObserver<T, U> implements MaybeObserver<T>, Disposable {
    final MaybeDelayOtherPublisher.OtherSubscriber<T> other;
    
    final Publisher<U> otherSource;
    
    Disposable upstream;
    
    DelayMaybeObserver(MaybeObserver<? super T> param1MaybeObserver, Publisher<U> param1Publisher) {
      this.other = new MaybeDelayOtherPublisher.OtherSubscriber<T>(param1MaybeObserver);
      this.otherSource = param1Publisher;
    }
    
    public void dispose() {
      this.upstream.dispose();
      this.upstream = (Disposable)DisposableHelper.DISPOSED;
      SubscriptionHelper.cancel(this.other);
    }
    
    public boolean isDisposed() {
      boolean bool;
      if (this.other.get() == SubscriptionHelper.CANCELLED) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public void onComplete() {
      this.upstream = (Disposable)DisposableHelper.DISPOSED;
      subscribeNext();
    }
    
    public void onError(Throwable param1Throwable) {
      this.upstream = (Disposable)DisposableHelper.DISPOSED;
      this.other.error = param1Throwable;
      subscribeNext();
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      if (DisposableHelper.validate(this.upstream, param1Disposable)) {
        this.upstream = param1Disposable;
        this.other.downstream.onSubscribe(this);
      } 
    }
    
    public void onSuccess(T param1T) {
      this.upstream = (Disposable)DisposableHelper.DISPOSED;
      this.other.value = param1T;
      subscribeNext();
    }
    
    void subscribeNext() {
      this.otherSource.subscribe((Subscriber)this.other);
    }
  }
  
  static final class OtherSubscriber<T> extends AtomicReference<Subscription> implements FlowableSubscriber<Object> {
    private static final long serialVersionUID = -1215060610805418006L;
    
    final MaybeObserver<? super T> downstream;
    
    Throwable error;
    
    T value;
    
    OtherSubscriber(MaybeObserver<? super T> param1MaybeObserver) {
      this.downstream = param1MaybeObserver;
    }
    
    public void onComplete() {
      Throwable throwable = this.error;
      if (throwable != null) {
        this.downstream.onError(throwable);
      } else {
        T t = this.value;
        if (t != null) {
          this.downstream.onSuccess(t);
        } else {
          this.downstream.onComplete();
        } 
      } 
    }
    
    public void onError(Throwable param1Throwable) {
      Throwable throwable = this.error;
      if (throwable == null) {
        this.downstream.onError(param1Throwable);
      } else {
        this.downstream.onError((Throwable)new CompositeException(new Throwable[] { throwable, param1Throwable }));
      } 
    }
    
    public void onNext(Object param1Object) {
      param1Object = get();
      if (param1Object != SubscriptionHelper.CANCELLED) {
        lazySet((Subscription)SubscriptionHelper.CANCELLED);
        param1Object.cancel();
        onComplete();
      } 
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      SubscriptionHelper.setOnce(this, param1Subscription, Long.MAX_VALUE);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/maybe/MaybeDelayOtherPublisher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */