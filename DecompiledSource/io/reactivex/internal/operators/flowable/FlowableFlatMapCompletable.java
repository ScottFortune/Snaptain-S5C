package io.reactivex.internal.operators.flowable;

import io.reactivex.CompletableObserver;
import io.reactivex.CompletableSource;
import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.subscriptions.BasicIntQueueSubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.AtomicThrowable;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableFlatMapCompletable<T> extends AbstractFlowableWithUpstream<T, T> {
  final boolean delayErrors;
  
  final Function<? super T, ? extends CompletableSource> mapper;
  
  final int maxConcurrency;
  
  public FlowableFlatMapCompletable(Flowable<T> paramFlowable, Function<? super T, ? extends CompletableSource> paramFunction, boolean paramBoolean, int paramInt) {
    super(paramFlowable);
    this.mapper = paramFunction;
    this.delayErrors = paramBoolean;
    this.maxConcurrency = paramInt;
  }
  
  protected void subscribeActual(Subscriber<? super T> paramSubscriber) {
    this.source.subscribe(new FlatMapCompletableMainSubscriber<T>(paramSubscriber, this.mapper, this.delayErrors, this.maxConcurrency));
  }
  
  static final class FlatMapCompletableMainSubscriber<T> extends BasicIntQueueSubscription<T> implements FlowableSubscriber<T> {
    private static final long serialVersionUID = 8443155186132538303L;
    
    volatile boolean cancelled;
    
    final boolean delayErrors;
    
    final Subscriber<? super T> downstream;
    
    final AtomicThrowable errors;
    
    final Function<? super T, ? extends CompletableSource> mapper;
    
    final int maxConcurrency;
    
    final CompositeDisposable set;
    
    Subscription upstream;
    
    FlatMapCompletableMainSubscriber(Subscriber<? super T> param1Subscriber, Function<? super T, ? extends CompletableSource> param1Function, boolean param1Boolean, int param1Int) {
      this.downstream = param1Subscriber;
      this.mapper = param1Function;
      this.delayErrors = param1Boolean;
      this.errors = new AtomicThrowable();
      this.set = new CompositeDisposable();
      this.maxConcurrency = param1Int;
      lazySet(1);
    }
    
    public void cancel() {
      this.cancelled = true;
      this.upstream.cancel();
      this.set.dispose();
    }
    
    public void clear() {}
    
    void innerComplete(InnerConsumer param1InnerConsumer) {
      this.set.delete(param1InnerConsumer);
      onComplete();
    }
    
    void innerError(InnerConsumer param1InnerConsumer, Throwable param1Throwable) {
      this.set.delete(param1InnerConsumer);
      onError(param1Throwable);
    }
    
    public boolean isEmpty() {
      return true;
    }
    
    public void onComplete() {
      if (decrementAndGet() == 0) {
        Throwable throwable = this.errors.terminate();
        if (throwable != null) {
          this.downstream.onError(throwable);
        } else {
          this.downstream.onComplete();
        } 
      } else if (this.maxConcurrency != Integer.MAX_VALUE) {
        this.upstream.request(1L);
      } 
    }
    
    public void onError(Throwable param1Throwable) {
      if (this.errors.addThrowable(param1Throwable)) {
        if (this.delayErrors) {
          if (decrementAndGet() == 0) {
            param1Throwable = this.errors.terminate();
            this.downstream.onError(param1Throwable);
          } else if (this.maxConcurrency != Integer.MAX_VALUE) {
            this.upstream.request(1L);
          } 
        } else {
          cancel();
          if (getAndSet(0) > 0) {
            param1Throwable = this.errors.terminate();
            this.downstream.onError(param1Throwable);
          } 
        } 
      } else {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
    
    public void onNext(T param1T) {
      try {
        return;
      } finally {
        param1T = null;
        Exceptions.throwIfFatal((Throwable)param1T);
        this.upstream.cancel();
        onError((Throwable)param1T);
      } 
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      if (SubscriptionHelper.validate(this.upstream, param1Subscription)) {
        this.upstream = param1Subscription;
        this.downstream.onSubscribe((Subscription)this);
        int i = this.maxConcurrency;
        if (i == Integer.MAX_VALUE) {
          param1Subscription.request(Long.MAX_VALUE);
        } else {
          param1Subscription.request(i);
        } 
      } 
    }
    
    public T poll() throws Exception {
      return null;
    }
    
    public void request(long param1Long) {}
    
    public int requestFusion(int param1Int) {
      return param1Int & 0x2;
    }
    
    final class InnerConsumer extends AtomicReference<Disposable> implements CompletableObserver, Disposable {
      private static final long serialVersionUID = 8606673141535671828L;
      
      public void dispose() {
        DisposableHelper.dispose(this);
      }
      
      public boolean isDisposed() {
        return DisposableHelper.isDisposed(get());
      }
      
      public void onComplete() {
        FlowableFlatMapCompletable.FlatMapCompletableMainSubscriber.this.innerComplete(this);
      }
      
      public void onError(Throwable param2Throwable) {
        FlowableFlatMapCompletable.FlatMapCompletableMainSubscriber.this.innerError(this, param2Throwable);
      }
      
      public void onSubscribe(Disposable param2Disposable) {
        DisposableHelper.setOnce(this, param2Disposable);
      }
    }
  }
  
  final class InnerConsumer extends AtomicReference<Disposable> implements CompletableObserver, Disposable {
    private static final long serialVersionUID = 8606673141535671828L;
    
    public void dispose() {
      DisposableHelper.dispose(this);
    }
    
    public boolean isDisposed() {
      return DisposableHelper.isDisposed(get());
    }
    
    public void onComplete() {
      this.this$0.innerComplete(this);
    }
    
    public void onError(Throwable param1Throwable) {
      this.this$0.innerError(this, param1Throwable);
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      DisposableHelper.setOnce(this, param1Disposable);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableFlatMapCompletable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */