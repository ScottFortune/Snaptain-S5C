package io.reactivex.internal.operators.maybe;

import io.reactivex.Flowable;
import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.disposables.SequentialDisposable;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.subscriptions.EmptySubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.internal.util.NotificationLite;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class MaybeConcatIterable<T> extends Flowable<T> {
  final Iterable<? extends MaybeSource<? extends T>> sources;
  
  public MaybeConcatIterable(Iterable<? extends MaybeSource<? extends T>> paramIterable) {
    this.sources = paramIterable;
  }
  
  protected void subscribeActual(Subscriber<? super T> paramSubscriber) {
    try {
      Iterator<? extends MaybeSource<? extends T>> iterator = (Iterator)ObjectHelper.requireNonNull(this.sources.iterator(), "The sources Iterable returned a null Iterator");
      return;
    } finally {
      Exception exception = null;
      Exceptions.throwIfFatal(exception);
      EmptySubscription.error(exception, paramSubscriber);
    } 
  }
  
  static final class ConcatMaybeObserver<T> extends AtomicInteger implements MaybeObserver<T>, Subscription {
    private static final long serialVersionUID = 3520831347801429610L;
    
    final AtomicReference<Object> current;
    
    final SequentialDisposable disposables;
    
    final Subscriber<? super T> downstream;
    
    long produced;
    
    final AtomicLong requested;
    
    final Iterator<? extends MaybeSource<? extends T>> sources;
    
    ConcatMaybeObserver(Subscriber<? super T> param1Subscriber, Iterator<? extends MaybeSource<? extends T>> param1Iterator) {
      this.downstream = param1Subscriber;
      this.sources = param1Iterator;
      this.requested = new AtomicLong();
      this.disposables = new SequentialDisposable();
      this.current = new AtomicReference(NotificationLite.COMPLETE);
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
        boolean bool = true;
        if (object != notificationLite) {
          long l = this.produced;
          if (l != this.requested.get()) {
            this.produced = l + 1L;
            atomicReference.lazySet(null);
            subscriber.onNext(object);
          } else {
            bool = false;
          } 
        } else {
          atomicReference.lazySet(null);
        } 
        if (!bool || sequentialDisposable.isDisposed())
          continue; 
        try {
        
        } finally {
          atomicReference = null;
          Exceptions.throwIfFatal((Throwable)atomicReference);
          subscriber.onError((Throwable)atomicReference);
        } 
      } while (decrementAndGet() != 0);
    }
    
    public void onComplete() {
      this.current.lazySet(NotificationLite.COMPLETE);
      drain();
    }
    
    public void onError(Throwable param1Throwable) {
      this.downstream.onError(param1Throwable);
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


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/maybe/MaybeConcatIterable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */