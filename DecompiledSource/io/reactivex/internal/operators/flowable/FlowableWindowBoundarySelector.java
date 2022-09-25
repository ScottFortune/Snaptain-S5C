package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.MissingBackpressureException;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.fuseable.SimplePlainQueue;
import io.reactivex.internal.queue.MpscLinkedQueue;
import io.reactivex.internal.subscribers.QueueDrainSubscriber;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.NotificationLite;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.processors.UnicastProcessor;
import io.reactivex.subscribers.DisposableSubscriber;
import io.reactivex.subscribers.SerializedSubscriber;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableWindowBoundarySelector<T, B, V> extends AbstractFlowableWithUpstream<T, Flowable<T>> {
  final int bufferSize;
  
  final Function<? super B, ? extends Publisher<V>> close;
  
  final Publisher<B> open;
  
  public FlowableWindowBoundarySelector(Flowable<T> paramFlowable, Publisher<B> paramPublisher, Function<? super B, ? extends Publisher<V>> paramFunction, int paramInt) {
    super(paramFlowable);
    this.open = paramPublisher;
    this.close = paramFunction;
    this.bufferSize = paramInt;
  }
  
  protected void subscribeActual(Subscriber<? super Flowable<T>> paramSubscriber) {
    this.source.subscribe((FlowableSubscriber)new WindowBoundaryMainSubscriber<Object, B, V>((Subscriber<? super Flowable<?>>)new SerializedSubscriber(paramSubscriber), this.open, this.close, this.bufferSize));
  }
  
  static final class OperatorWindowBoundaryCloseSubscriber<T, V> extends DisposableSubscriber<V> {
    boolean done;
    
    final FlowableWindowBoundarySelector.WindowBoundaryMainSubscriber<T, ?, V> parent;
    
    final UnicastProcessor<T> w;
    
    OperatorWindowBoundaryCloseSubscriber(FlowableWindowBoundarySelector.WindowBoundaryMainSubscriber<T, ?, V> param1WindowBoundaryMainSubscriber, UnicastProcessor<T> param1UnicastProcessor) {
      this.parent = param1WindowBoundaryMainSubscriber;
      this.w = param1UnicastProcessor;
    }
    
    public void onComplete() {
      if (this.done)
        return; 
      this.done = true;
      this.parent.close(this);
    }
    
    public void onError(Throwable param1Throwable) {
      if (this.done) {
        RxJavaPlugins.onError(param1Throwable);
        return;
      } 
      this.done = true;
      this.parent.error(param1Throwable);
    }
    
    public void onNext(V param1V) {
      cancel();
      onComplete();
    }
  }
  
  static final class OperatorWindowBoundaryOpenSubscriber<T, B> extends DisposableSubscriber<B> {
    final FlowableWindowBoundarySelector.WindowBoundaryMainSubscriber<T, B, ?> parent;
    
    OperatorWindowBoundaryOpenSubscriber(FlowableWindowBoundarySelector.WindowBoundaryMainSubscriber<T, B, ?> param1WindowBoundaryMainSubscriber) {
      this.parent = param1WindowBoundaryMainSubscriber;
    }
    
    public void onComplete() {
      this.parent.onComplete();
    }
    
    public void onError(Throwable param1Throwable) {
      this.parent.error(param1Throwable);
    }
    
    public void onNext(B param1B) {
      this.parent.open(param1B);
    }
  }
  
  static final class WindowBoundaryMainSubscriber<T, B, V> extends QueueDrainSubscriber<T, Object, Flowable<T>> implements Subscription {
    final AtomicReference<Disposable> boundary = new AtomicReference<Disposable>();
    
    final int bufferSize;
    
    final Function<? super B, ? extends Publisher<V>> close;
    
    final Publisher<B> open;
    
    final CompositeDisposable resources;
    
    final AtomicBoolean stopWindows = new AtomicBoolean();
    
    Subscription upstream;
    
    final AtomicLong windows = new AtomicLong();
    
    final List<UnicastProcessor<T>> ws;
    
    WindowBoundaryMainSubscriber(Subscriber<? super Flowable<T>> param1Subscriber, Publisher<B> param1Publisher, Function<? super B, ? extends Publisher<V>> param1Function, int param1Int) {
      super(param1Subscriber, (SimplePlainQueue)new MpscLinkedQueue());
      this.open = param1Publisher;
      this.close = param1Function;
      this.bufferSize = param1Int;
      this.resources = new CompositeDisposable();
      this.ws = new ArrayList<UnicastProcessor<T>>();
      this.windows.lazySet(1L);
    }
    
    public boolean accept(Subscriber<? super Flowable<T>> param1Subscriber, Object param1Object) {
      return false;
    }
    
    public void cancel() {
      if (this.stopWindows.compareAndSet(false, true)) {
        DisposableHelper.dispose(this.boundary);
        if (this.windows.decrementAndGet() == 0L)
          this.upstream.cancel(); 
      } 
    }
    
    void close(FlowableWindowBoundarySelector.OperatorWindowBoundaryCloseSubscriber<T, V> param1OperatorWindowBoundaryCloseSubscriber) {
      this.resources.delete((Disposable)param1OperatorWindowBoundaryCloseSubscriber);
      this.queue.offer(new FlowableWindowBoundarySelector.WindowOperation<T, Object>(param1OperatorWindowBoundaryCloseSubscriber.w, null));
      if (enter())
        drainLoop(); 
    }
    
    void dispose() {
      this.resources.dispose();
      DisposableHelper.dispose(this.boundary);
    }
    
    void drainLoop() {
      SimplePlainQueue simplePlainQueue = this.queue;
      Subscriber subscriber = this.downstream;
      List<UnicastProcessor<T>> list = this.ws;
      int i = 1;
      while (true) {
        Throwable throwable;
        int j;
        boolean bool = this.done;
        Object object = simplePlainQueue.poll();
        if (object == null) {
          j = 1;
        } else {
          j = 0;
        } 
        if (bool && j) {
          dispose();
          throwable = this.error;
          if (throwable != null) {
            Iterator<UnicastProcessor<T>> iterator1 = list.iterator();
            while (iterator1.hasNext())
              ((UnicastProcessor)iterator1.next()).onError(throwable); 
          } else {
            Iterator<UnicastProcessor<T>> iterator1 = list.iterator();
            while (iterator1.hasNext())
              ((UnicastProcessor)iterator1.next()).onComplete(); 
          } 
          list.clear();
          return;
        } 
        if (j) {
          j = leave(-i);
          i = j;
          if (j == 0)
            return; 
          continue;
        } 
        if (object instanceof FlowableWindowBoundarySelector.WindowOperation) {
          object = object;
          if (((FlowableWindowBoundarySelector.WindowOperation)object).w != null) {
            if (list.remove(((FlowableWindowBoundarySelector.WindowOperation)object).w)) {
              ((FlowableWindowBoundarySelector.WindowOperation)object).w.onComplete();
              if (this.windows.decrementAndGet() == 0L) {
                dispose();
                return;
              } 
            } 
            continue;
          } 
          if (this.stopWindows.get())
            continue; 
          UnicastProcessor<T> unicastProcessor = UnicastProcessor.create(this.bufferSize);
          long l = requested();
          if (l != 0L) {
            list.add(unicastProcessor);
            throwable.onNext(unicastProcessor);
            if (l != Long.MAX_VALUE)
              produced(1L); 
            try {
              object = ObjectHelper.requireNonNull(this.close.apply(((FlowableWindowBoundarySelector.WindowOperation)object).open), "The publisher supplied is null");
            } finally {
              object = null;
              cancel();
            } 
            continue;
          } 
          cancel();
          throwable.onError((Throwable)new MissingBackpressureException("Could not deliver new window due to lack of requests"));
          continue;
        } 
        Iterator<UnicastProcessor<T>> iterator = list.iterator();
        while (iterator.hasNext())
          ((UnicastProcessor)iterator.next()).onNext(NotificationLite.getValue(object)); 
      } 
    }
    
    void error(Throwable param1Throwable) {
      this.upstream.cancel();
      this.resources.dispose();
      DisposableHelper.dispose(this.boundary);
      this.downstream.onError(param1Throwable);
    }
    
    public void onComplete() {
      if (this.done)
        return; 
      this.done = true;
      if (enter())
        drainLoop(); 
      if (this.windows.decrementAndGet() == 0L)
        this.resources.dispose(); 
      this.downstream.onComplete();
    }
    
    public void onError(Throwable param1Throwable) {
      if (this.done) {
        RxJavaPlugins.onError(param1Throwable);
        return;
      } 
      this.error = param1Throwable;
      this.done = true;
      if (enter())
        drainLoop(); 
      if (this.windows.decrementAndGet() == 0L)
        this.resources.dispose(); 
      this.downstream.onError(param1Throwable);
    }
    
    public void onNext(T param1T) {
      if (this.done)
        return; 
      if (fastEnter()) {
        Iterator<UnicastProcessor<T>> iterator = this.ws.iterator();
        while (iterator.hasNext())
          ((UnicastProcessor)iterator.next()).onNext(param1T); 
        if (leave(-1) == 0)
          return; 
      } else {
        this.queue.offer(NotificationLite.next(param1T));
        if (!enter())
          return; 
      } 
      drainLoop();
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      if (SubscriptionHelper.validate(this.upstream, param1Subscription)) {
        this.upstream = param1Subscription;
        this.downstream.onSubscribe(this);
        if (this.stopWindows.get())
          return; 
        FlowableWindowBoundarySelector.OperatorWindowBoundaryOpenSubscriber<Object, Object> operatorWindowBoundaryOpenSubscriber = new FlowableWindowBoundarySelector.OperatorWindowBoundaryOpenSubscriber<Object, Object>(this);
        if (this.boundary.compareAndSet(null, operatorWindowBoundaryOpenSubscriber)) {
          param1Subscription.request(Long.MAX_VALUE);
          this.open.subscribe((Subscriber)operatorWindowBoundaryOpenSubscriber);
        } 
      } 
    }
    
    void open(B param1B) {
      this.queue.offer(new FlowableWindowBoundarySelector.WindowOperation<Object, B>(null, param1B));
      if (enter())
        drainLoop(); 
    }
    
    public void request(long param1Long) {
      requested(param1Long);
    }
  }
  
  static final class WindowOperation<T, B> {
    final B open;
    
    final UnicastProcessor<T> w;
    
    WindowOperation(UnicastProcessor<T> param1UnicastProcessor, B param1B) {
      this.w = param1UnicastProcessor;
      this.open = param1B;
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableWindowBoundarySelector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */