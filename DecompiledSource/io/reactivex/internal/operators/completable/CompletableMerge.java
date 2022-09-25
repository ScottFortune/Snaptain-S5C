package io.reactivex.internal.operators.completable;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableSource;
import io.reactivex.FlowableSubscriber;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.AtomicThrowable;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class CompletableMerge extends Completable {
  final boolean delayErrors;
  
  final int maxConcurrency;
  
  final Publisher<? extends CompletableSource> source;
  
  public CompletableMerge(Publisher<? extends CompletableSource> paramPublisher, int paramInt, boolean paramBoolean) {
    this.source = paramPublisher;
    this.maxConcurrency = paramInt;
    this.delayErrors = paramBoolean;
  }
  
  public void subscribeActual(CompletableObserver paramCompletableObserver) {
    CompletableMergeSubscriber completableMergeSubscriber = new CompletableMergeSubscriber(paramCompletableObserver, this.maxConcurrency, this.delayErrors);
    this.source.subscribe((Subscriber)completableMergeSubscriber);
  }
  
  static final class CompletableMergeSubscriber extends AtomicInteger implements FlowableSubscriber<CompletableSource>, Disposable {
    private static final long serialVersionUID = -2108443387387077490L;
    
    final boolean delayErrors;
    
    final CompletableObserver downstream;
    
    final AtomicThrowable error;
    
    final int maxConcurrency;
    
    final CompositeDisposable set;
    
    Subscription upstream;
    
    CompletableMergeSubscriber(CompletableObserver param1CompletableObserver, int param1Int, boolean param1Boolean) {
      this.downstream = param1CompletableObserver;
      this.maxConcurrency = param1Int;
      this.delayErrors = param1Boolean;
      this.set = new CompositeDisposable();
      this.error = new AtomicThrowable();
      lazySet(1);
    }
    
    public void dispose() {
      this.upstream.cancel();
      this.set.dispose();
    }
    
    void innerComplete(MergeInnerObserver param1MergeInnerObserver) {
      this.set.delete(param1MergeInnerObserver);
      if (decrementAndGet() == 0) {
        Throwable throwable = (Throwable)this.error.get();
        if (throwable != null) {
          this.downstream.onError(throwable);
        } else {
          this.downstream.onComplete();
        } 
      } else if (this.maxConcurrency != Integer.MAX_VALUE) {
        this.upstream.request(1L);
      } 
    }
    
    void innerError(MergeInnerObserver param1MergeInnerObserver, Throwable param1Throwable) {
      this.set.delete(param1MergeInnerObserver);
      if (!this.delayErrors) {
        this.upstream.cancel();
        this.set.dispose();
        if (this.error.addThrowable(param1Throwable)) {
          if (getAndSet(0) > 0)
            this.downstream.onError(this.error.terminate()); 
        } else {
          RxJavaPlugins.onError(param1Throwable);
        } 
      } else if (this.error.addThrowable(param1Throwable)) {
        if (decrementAndGet() == 0) {
          this.downstream.onError(this.error.terminate());
        } else if (this.maxConcurrency != Integer.MAX_VALUE) {
          this.upstream.request(1L);
        } 
      } else {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
    
    public boolean isDisposed() {
      return this.set.isDisposed();
    }
    
    public void onComplete() {
      if (decrementAndGet() == 0)
        if ((Throwable)this.error.get() != null) {
          this.downstream.onError(this.error.terminate());
        } else {
          this.downstream.onComplete();
        }  
    }
    
    public void onError(Throwable param1Throwable) {
      if (!this.delayErrors) {
        this.set.dispose();
        if (this.error.addThrowable(param1Throwable)) {
          if (getAndSet(0) > 0)
            this.downstream.onError(this.error.terminate()); 
        } else {
          RxJavaPlugins.onError(param1Throwable);
        } 
      } else if (this.error.addThrowable(param1Throwable)) {
        if (decrementAndGet() == 0)
          this.downstream.onError(this.error.terminate()); 
      } else {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
    
    public void onNext(CompletableSource param1CompletableSource) {
      getAndIncrement();
      MergeInnerObserver mergeInnerObserver = new MergeInnerObserver();
      this.set.add(mergeInnerObserver);
      param1CompletableSource.subscribe(mergeInnerObserver);
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      if (SubscriptionHelper.validate(this.upstream, param1Subscription)) {
        this.upstream = param1Subscription;
        this.downstream.onSubscribe(this);
        int i = this.maxConcurrency;
        if (i == Integer.MAX_VALUE) {
          param1Subscription.request(Long.MAX_VALUE);
        } else {
          param1Subscription.request(i);
        } 
      } 
    }
    
    final class MergeInnerObserver extends AtomicReference<Disposable> implements CompletableObserver, Disposable {
      private static final long serialVersionUID = 251330541679988317L;
      
      public void dispose() {
        DisposableHelper.dispose(this);
      }
      
      public boolean isDisposed() {
        return DisposableHelper.isDisposed(get());
      }
      
      public void onComplete() {
        CompletableMerge.CompletableMergeSubscriber.this.innerComplete(this);
      }
      
      public void onError(Throwable param2Throwable) {
        CompletableMerge.CompletableMergeSubscriber.this.innerError(this, param2Throwable);
      }
      
      public void onSubscribe(Disposable param2Disposable) {
        DisposableHelper.setOnce(this, param2Disposable);
      }
    }
  }
  
  final class MergeInnerObserver extends AtomicReference<Disposable> implements CompletableObserver, Disposable {
    private static final long serialVersionUID = 251330541679988317L;
    
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


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/completable/CompletableMerge.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */