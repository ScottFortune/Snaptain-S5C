package io.reactivex.internal.operators.mixed;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.AtomicThrowable;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableSwitchMapMaybe<T, R> extends Flowable<R> {
  final boolean delayErrors;
  
  final Function<? super T, ? extends MaybeSource<? extends R>> mapper;
  
  final Flowable<T> source;
  
  public FlowableSwitchMapMaybe(Flowable<T> paramFlowable, Function<? super T, ? extends MaybeSource<? extends R>> paramFunction, boolean paramBoolean) {
    this.source = paramFlowable;
    this.mapper = paramFunction;
    this.delayErrors = paramBoolean;
  }
  
  protected void subscribeActual(Subscriber<? super R> paramSubscriber) {
    this.source.subscribe(new SwitchMapMaybeSubscriber<T, R>(paramSubscriber, this.mapper, this.delayErrors));
  }
  
  static final class SwitchMapMaybeSubscriber<T, R> extends AtomicInteger implements FlowableSubscriber<T>, Subscription {
    static final SwitchMapMaybeObserver<Object> INNER_DISPOSED = new SwitchMapMaybeObserver(null);
    
    private static final long serialVersionUID = -5402190102429853762L;
    
    volatile boolean cancelled;
    
    final boolean delayErrors;
    
    volatile boolean done;
    
    final Subscriber<? super R> downstream;
    
    long emitted;
    
    final AtomicThrowable errors;
    
    final AtomicReference<SwitchMapMaybeObserver<R>> inner;
    
    final Function<? super T, ? extends MaybeSource<? extends R>> mapper;
    
    final AtomicLong requested;
    
    Subscription upstream;
    
    SwitchMapMaybeSubscriber(Subscriber<? super R> param1Subscriber, Function<? super T, ? extends MaybeSource<? extends R>> param1Function, boolean param1Boolean) {
      this.downstream = param1Subscriber;
      this.mapper = param1Function;
      this.delayErrors = param1Boolean;
      this.errors = new AtomicThrowable();
      this.requested = new AtomicLong();
      this.inner = new AtomicReference<SwitchMapMaybeObserver<R>>();
    }
    
    public void cancel() {
      this.cancelled = true;
      this.upstream.cancel();
      disposeInner();
    }
    
    void disposeInner() {
      SwitchMapMaybeObserver<Object> switchMapMaybeObserver = (SwitchMapMaybeObserver)this.inner.getAndSet(INNER_DISPOSED);
      if (switchMapMaybeObserver != null && switchMapMaybeObserver != INNER_DISPOSED)
        switchMapMaybeObserver.dispose(); 
    }
    
    void drain() {
      if (getAndIncrement() != 0)
        return; 
      Subscriber<? super R> subscriber = this.downstream;
      AtomicThrowable atomicThrowable = this.errors;
      AtomicReference<SwitchMapMaybeObserver<R>> atomicReference = this.inner;
      AtomicLong atomicLong = this.requested;
      long l = this.emitted;
      int i = 1;
      while (true) {
        Throwable throwable;
        int j;
        if (this.cancelled)
          return; 
        if (atomicThrowable.get() != null && !this.delayErrors) {
          subscriber.onError(atomicThrowable.terminate());
          return;
        } 
        boolean bool = this.done;
        SwitchMapMaybeObserver<R> switchMapMaybeObserver = atomicReference.get();
        if (switchMapMaybeObserver == null) {
          j = 1;
        } else {
          j = 0;
        } 
        if (bool && j) {
          throwable = atomicThrowable.terminate();
          if (throwable != null) {
            subscriber.onError(throwable);
          } else {
            subscriber.onComplete();
          } 
          return;
        } 
        if (j || switchMapMaybeObserver.item == null || l == throwable.get()) {
          this.emitted = l;
          j = addAndGet(-i);
          i = j;
          if (j == 0)
            break; 
          continue;
        } 
        atomicReference.compareAndSet(switchMapMaybeObserver, null);
        subscriber.onNext(switchMapMaybeObserver.item);
        l++;
      } 
    }
    
    void innerComplete(SwitchMapMaybeObserver<R> param1SwitchMapMaybeObserver) {
      if (this.inner.compareAndSet(param1SwitchMapMaybeObserver, null))
        drain(); 
    }
    
    void innerError(SwitchMapMaybeObserver<R> param1SwitchMapMaybeObserver, Throwable param1Throwable) {
      if (this.inner.compareAndSet(param1SwitchMapMaybeObserver, null) && this.errors.addThrowable(param1Throwable)) {
        if (!this.delayErrors) {
          this.upstream.cancel();
          disposeInner();
        } 
        drain();
        return;
      } 
      RxJavaPlugins.onError(param1Throwable);
    }
    
    public void onComplete() {
      this.done = true;
      drain();
    }
    
    public void onError(Throwable param1Throwable) {
      if (this.errors.addThrowable(param1Throwable)) {
        if (!this.delayErrors)
          disposeInner(); 
        this.done = true;
        drain();
      } else {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
    
    public void onNext(T param1T) {
      SwitchMapMaybeObserver switchMapMaybeObserver = this.inner.get();
      if (switchMapMaybeObserver != null)
        switchMapMaybeObserver.dispose(); 
      try {
        return;
      } finally {
        param1T = null;
        Exceptions.throwIfFatal((Throwable)param1T);
        this.upstream.cancel();
        this.inner.getAndSet(INNER_DISPOSED);
        onError((Throwable)param1T);
      } 
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      if (SubscriptionHelper.validate(this.upstream, param1Subscription)) {
        this.upstream = param1Subscription;
        this.downstream.onSubscribe(this);
        param1Subscription.request(Long.MAX_VALUE);
      } 
    }
    
    public void request(long param1Long) {
      BackpressureHelper.add(this.requested, param1Long);
      drain();
    }
    
    static final class SwitchMapMaybeObserver<R> extends AtomicReference<Disposable> implements MaybeObserver<R> {
      private static final long serialVersionUID = 8042919737683345351L;
      
      volatile R item;
      
      final FlowableSwitchMapMaybe.SwitchMapMaybeSubscriber<?, R> parent;
      
      SwitchMapMaybeObserver(FlowableSwitchMapMaybe.SwitchMapMaybeSubscriber<?, R> param2SwitchMapMaybeSubscriber) {
        this.parent = param2SwitchMapMaybeSubscriber;
      }
      
      void dispose() {
        DisposableHelper.dispose(this);
      }
      
      public void onComplete() {
        this.parent.innerComplete(this);
      }
      
      public void onError(Throwable param2Throwable) {
        this.parent.innerError(this, param2Throwable);
      }
      
      public void onSubscribe(Disposable param2Disposable) {
        DisposableHelper.setOnce(this, param2Disposable);
      }
      
      public void onSuccess(R param2R) {
        this.item = param2R;
        this.parent.drain();
      }
    }
  }
  
  static final class SwitchMapMaybeObserver<R> extends AtomicReference<Disposable> implements MaybeObserver<R> {
    private static final long serialVersionUID = 8042919737683345351L;
    
    volatile R item;
    
    final FlowableSwitchMapMaybe.SwitchMapMaybeSubscriber<?, R> parent;
    
    SwitchMapMaybeObserver(FlowableSwitchMapMaybe.SwitchMapMaybeSubscriber<?, R> param1SwitchMapMaybeSubscriber) {
      this.parent = param1SwitchMapMaybeSubscriber;
    }
    
    void dispose() {
      DisposableHelper.dispose(this);
    }
    
    public void onComplete() {
      this.parent.innerComplete(this);
    }
    
    public void onError(Throwable param1Throwable) {
      this.parent.innerError(this, param1Throwable);
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      DisposableHelper.setOnce(this, param1Disposable);
    }
    
    public void onSuccess(R param1R) {
      this.item = param1R;
      this.parent.drain();
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/mixed/FlowableSwitchMapMaybe.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */