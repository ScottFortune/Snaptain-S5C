package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.MissingBackpressureException;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.subscribers.SerializedSubscriber;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableDebounceTimed<T> extends AbstractFlowableWithUpstream<T, T> {
  final Scheduler scheduler;
  
  final long timeout;
  
  final TimeUnit unit;
  
  public FlowableDebounceTimed(Flowable<T> paramFlowable, long paramLong, TimeUnit paramTimeUnit, Scheduler paramScheduler) {
    super(paramFlowable);
    this.timeout = paramLong;
    this.unit = paramTimeUnit;
    this.scheduler = paramScheduler;
  }
  
  protected void subscribeActual(Subscriber<? super T> paramSubscriber) {
    this.source.subscribe(new DebounceTimedSubscriber((Subscriber<?>)new SerializedSubscriber(paramSubscriber), this.timeout, this.unit, this.scheduler.createWorker()));
  }
  
  static final class DebounceEmitter<T> extends AtomicReference<Disposable> implements Runnable, Disposable {
    private static final long serialVersionUID = 6812032969491025141L;
    
    final long idx;
    
    final AtomicBoolean once = new AtomicBoolean();
    
    final FlowableDebounceTimed.DebounceTimedSubscriber<T> parent;
    
    final T value;
    
    DebounceEmitter(T param1T, long param1Long, FlowableDebounceTimed.DebounceTimedSubscriber<T> param1DebounceTimedSubscriber) {
      this.value = param1T;
      this.idx = param1Long;
      this.parent = param1DebounceTimedSubscriber;
    }
    
    public void dispose() {
      DisposableHelper.dispose(this);
    }
    
    void emit() {
      if (this.once.compareAndSet(false, true))
        this.parent.emit(this.idx, this.value, this); 
    }
    
    public boolean isDisposed() {
      boolean bool;
      if (get() == DisposableHelper.DISPOSED) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public void run() {
      emit();
    }
    
    public void setResource(Disposable param1Disposable) {
      DisposableHelper.replace(this, param1Disposable);
    }
  }
  
  static final class DebounceTimedSubscriber<T> extends AtomicLong implements FlowableSubscriber<T>, Subscription {
    private static final long serialVersionUID = -9102637559663639004L;
    
    boolean done;
    
    final Subscriber<? super T> downstream;
    
    volatile long index;
    
    final long timeout;
    
    Disposable timer;
    
    final TimeUnit unit;
    
    Subscription upstream;
    
    final Scheduler.Worker worker;
    
    DebounceTimedSubscriber(Subscriber<? super T> param1Subscriber, long param1Long, TimeUnit param1TimeUnit, Scheduler.Worker param1Worker) {
      this.downstream = param1Subscriber;
      this.timeout = param1Long;
      this.unit = param1TimeUnit;
      this.worker = param1Worker;
    }
    
    public void cancel() {
      this.upstream.cancel();
      this.worker.dispose();
    }
    
    void emit(long param1Long, T param1T, FlowableDebounceTimed.DebounceEmitter<T> param1DebounceEmitter) {
      if (param1Long == this.index)
        if (get() != 0L) {
          this.downstream.onNext(param1T);
          BackpressureHelper.produced(this, 1L);
          param1DebounceEmitter.dispose();
        } else {
          cancel();
          this.downstream.onError((Throwable)new MissingBackpressureException("Could not deliver value due to lack of requests"));
        }  
    }
    
    public void onComplete() {
      if (this.done)
        return; 
      this.done = true;
      Disposable disposable = this.timer;
      if (disposable != null)
        disposable.dispose(); 
      disposable = disposable;
      if (disposable != null)
        disposable.emit(); 
      this.downstream.onComplete();
      this.worker.dispose();
    }
    
    public void onError(Throwable param1Throwable) {
      if (this.done) {
        RxJavaPlugins.onError(param1Throwable);
        return;
      } 
      this.done = true;
      Disposable disposable = this.timer;
      if (disposable != null)
        disposable.dispose(); 
      this.downstream.onError(param1Throwable);
      this.worker.dispose();
    }
    
    public void onNext(T param1T) {
      if (this.done)
        return; 
      long l = this.index + 1L;
      this.index = l;
      Disposable disposable = this.timer;
      if (disposable != null)
        disposable.dispose(); 
      FlowableDebounceTimed.DebounceEmitter<T> debounceEmitter = new FlowableDebounceTimed.DebounceEmitter<T>(param1T, l, this);
      this.timer = debounceEmitter;
      debounceEmitter.setResource(this.worker.schedule(debounceEmitter, this.timeout, this.unit));
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      if (SubscriptionHelper.validate(this.upstream, param1Subscription)) {
        this.upstream = param1Subscription;
        this.downstream.onSubscribe(this);
        param1Subscription.request(Long.MAX_VALUE);
      } 
    }
    
    public void request(long param1Long) {
      if (SubscriptionHelper.validate(param1Long))
        BackpressureHelper.add(this, param1Long); 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableDebounceTimed.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */