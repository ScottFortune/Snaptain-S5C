package io.reactivex.internal.operators.maybe;

import io.reactivex.Flowable;
import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.SequentialDisposable;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.AtomicThrowable;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.internal.util.NotificationLite;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class MaybeConcatArrayDelayError<T> extends Flowable<T> {
  final MaybeSource<? extends T>[] sources;
  
  public MaybeConcatArrayDelayError(MaybeSource<? extends T>[] paramArrayOfMaybeSource) {
    this.sources = paramArrayOfMaybeSource;
  }
  
  protected void subscribeActual(Subscriber<? super T> paramSubscriber) {
    ConcatMaybeObserver<T> concatMaybeObserver = new ConcatMaybeObserver<T>(paramSubscriber, this.sources);
    paramSubscriber.onSubscribe(concatMaybeObserver);
    concatMaybeObserver.drain();
  }
  
  static final class ConcatMaybeObserver<T> extends AtomicInteger implements MaybeObserver<T>, Subscription {
    private static final long serialVersionUID = 3520831347801429610L;
    
    final AtomicReference<Object> current;
    
    final SequentialDisposable disposables;
    
    final Subscriber<? super T> downstream;
    
    final AtomicThrowable errors;
    
    int index;
    
    long produced;
    
    final AtomicLong requested;
    
    final MaybeSource<? extends T>[] sources;
    
    ConcatMaybeObserver(Subscriber<? super T> param1Subscriber, MaybeSource<? extends T>[] param1ArrayOfMaybeSource) {
      this.downstream = param1Subscriber;
      this.sources = param1ArrayOfMaybeSource;
      this.requested = new AtomicLong();
      this.disposables = new SequentialDisposable();
      this.current = new AtomicReference(NotificationLite.COMPLETE);
      this.errors = new AtomicThrowable();
    }
    
    public void cancel() {
      this.disposables.dispose();
    }
    
    void drain() {
      if (getAndIncrement() != 0)
        return; 
      AtomicReference<Object> atomicReference = this.current;
      Subscriber<? super T> subscriber = this.downstream;
      SequentialDisposable sequentialDisposable = this.disposables;
      do {
        if (sequentialDisposable.isDisposed()) {
          atomicReference.lazySet(null);
          return;
        } 
        Object object = atomicReference.get();
        if (object == null)
          continue; 
        NotificationLite notificationLite = NotificationLite.COMPLETE;
        int i = 1;
        if (object != notificationLite) {
          long l = this.produced;
          if (l != this.requested.get()) {
            this.produced = l + 1L;
            atomicReference.lazySet(null);
            subscriber.onNext(object);
          } else {
            i = 0;
          } 
        } else {
          atomicReference.lazySet(null);
        } 
        if (!i || sequentialDisposable.isDisposed())
          continue; 
        i = this.index;
        MaybeSource<? extends T>[] arrayOfMaybeSource = this.sources;
        if (i == arrayOfMaybeSource.length) {
          if ((Throwable)this.errors.get() != null) {
            subscriber.onError(this.errors.terminate());
          } else {
            subscriber.onComplete();
          } 
          return;
        } 
        this.index = i + 1;
        arrayOfMaybeSource[i].subscribe(this);
      } while (decrementAndGet() != 0);
    }
    
    public void onComplete() {
      this.current.lazySet(NotificationLite.COMPLETE);
      drain();
    }
    
    public void onError(Throwable param1Throwable) {
      this.current.lazySet(NotificationLite.COMPLETE);
      if (this.errors.addThrowable(param1Throwable)) {
        drain();
      } else {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      this.disposables.replace(param1Disposable);
    }
    
    public void onSuccess(T param1T) {
      this.current.lazySet(param1T);
      drain();
    }
    
    public void request(long param1Long) {
      if (SubscriptionHelper.validate(param1Long)) {
        BackpressureHelper.add(this.requested, param1Long);
        drain();
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/maybe/MaybeConcatArrayDelayError.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */