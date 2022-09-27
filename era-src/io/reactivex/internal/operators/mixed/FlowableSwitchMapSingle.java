package io.reactivex.internal.operators.mixed;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
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

public final class FlowableSwitchMapSingle<T, R> extends Flowable<R> {
  final boolean delayErrors;
  
  final Function<? super T, ? extends SingleSource<? extends R>> mapper;
  
  final Flowable<T> source;
  
  public FlowableSwitchMapSingle(Flowable<T> paramFlowable, Function<? super T, ? extends SingleSource<? extends R>> paramFunction, boolean paramBoolean) {
    this.source = paramFlowable;
    this.mapper = paramFunction;
    this.delayErrors = paramBoolean;
  }
  
  protected void subscribeActual(Subscriber<? super R> paramSubscriber) {
    this.source.subscribe(new SwitchMapSingleSubscriber<T, R>(paramSubscriber, this.mapper, this.delayErrors));
  }
  
  static final class SwitchMapSingleSubscriber<T, R> extends AtomicInteger implements FlowableSubscriber<T>, Subscription {
    static final SwitchMapSingleObserver<Object> INNER_DISPOSED = new SwitchMapSingleObserver(null);
    
    private static final long serialVersionUID = -5402190102429853762L;
    
    volatile boolean cancelled;
    
    final boolean delayErrors;
    
    volatile boolean done;
    
    final Subscriber<? super R> downstream;
    
    long emitted;
    
    final AtomicThrowable errors;
    
    final AtomicReference<SwitchMapSingleObserver<R>> inner;
    
    final Function<? super T, ? extends SingleSource<? extends R>> mapper;
    
    final AtomicLong requested;
    
    Subscription upstream;
    
    SwitchMapSingleSubscriber(Subscriber<? super R> param1Subscriber, Function<? super T, ? extends SingleSource<? extends R>> param1Function, boolean param1Boolean) {
      this.downstream = param1Subscriber;
      this.mapper = param1Function;
      this.delayErrors = param1Boolean;
      this.errors = new AtomicThrowable();
      this.requested = new AtomicLong();
      this.inner = new AtomicReference<SwitchMapSingleObserver<R>>();
    }
    
    public void cancel() {
      this.cancelled = true;
      this.upstream.cancel();
      disposeInner();
    }
    
    void disposeInner() {
      SwitchMapSingleObserver<Object> switchMapSingleObserver = (SwitchMapSingleObserver)this.inner.getAndSet(INNER_DISPOSED);
      if (switchMapSingleObserver != null && switchMapSingleObserver != INNER_DISPOSED)
        switchMapSingleObserver.dispose(); 
    }
    
    void drain() {
      if (getAndIncrement() != 0)
        return; 
      Subscriber<? super R> subscriber = this.downstream;
      AtomicThrowable atomicThrowable = this.errors;
      AtomicReference<SwitchMapSingleObserver<R>> atomicReference = this.inner;
      AtomicLong atomicLong = this.requested;
      long l = this.emitted;
      int i = 1;
      while (true) {
        int j;
        if (this.cancelled)
          return; 
        if (atomicThrowable.get() != null && !this.delayErrors) {
          subscriber.onError(atomicThrowable.terminate());
          return;
        } 
        boolean bool = this.done;
        SwitchMapSingleObserver<R> switchMapSingleObserver = atomicReference.get();
        if (switchMapSingleObserver == null) {
          j = 1;
        } else {
          j = 0;
        } 
        if (bool && j) {
          Throwable throwable = atomicThrowable.terminate();
          if (throwable != null) {
            subscriber.onError(throwable);
          } else {
            subscriber.onComplete();
          } 
          return;
        } 
        if (j || switchMapSingleObserver.item == null || l == atomicLong.get()) {
          this.emitted = l;
          j = addAndGet(-i);
          i = j;
          if (j == 0)
            break; 
          continue;
        } 
        atomicReference.compareAndSet(switchMapSingleObserver, null);
        subscriber.onNext(switchMapSingleObserver.item);
        l++;
      } 
    }
    
    void innerError(SwitchMapSingleObserver<R> param1SwitchMapSingleObserver, Throwable param1Throwable) {
      if (this.inner.compareAndSet(param1SwitchMapSingleObserver, null) && this.errors.addThrowable(param1Throwable)) {
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
      SwitchMapSingleObserver<Object> switchMapSingleObserver = (SwitchMapSingleObserver)this.inner.get();
      if (switchMapSingleObserver != null)
        switchMapSingleObserver.dispose(); 
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
    
    static final class SwitchMapSingleObserver<R> extends AtomicReference<Disposable> implements SingleObserver<R> {
      private static final long serialVersionUID = 8042919737683345351L;
      
      volatile R item;
      
      final FlowableSwitchMapSingle.SwitchMapSingleSubscriber<?, R> parent;
      
      SwitchMapSingleObserver(FlowableSwitchMapSingle.SwitchMapSingleSubscriber<?, R> param2SwitchMapSingleSubscriber) {
        this.parent = param2SwitchMapSingleSubscriber;
      }
      
      void dispose() {
        DisposableHelper.dispose(this);
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
  
  static final class SwitchMapSingleObserver<R> extends AtomicReference<Disposable> implements SingleObserver<R> {
    private static final long serialVersionUID = 8042919737683345351L;
    
    volatile R item;
    
    final FlowableSwitchMapSingle.SwitchMapSingleSubscriber<?, R> parent;
    
    SwitchMapSingleObserver(FlowableSwitchMapSingle.SwitchMapSingleSubscriber<?, R> param1SwitchMapSingleSubscriber) {
      this.parent = param1SwitchMapSingleSubscriber;
    }
    
    void dispose() {
      DisposableHelper.dispose(this);
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


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/mixed/FlowableSwitchMapSingle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */