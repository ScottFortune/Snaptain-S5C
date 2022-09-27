package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.fuseable.QueueDisposable;
import io.reactivex.internal.fuseable.SimpleQueue;
import io.reactivex.internal.observers.InnerQueuedObserver;
import io.reactivex.internal.observers.InnerQueuedObserverSupport;
import io.reactivex.internal.queue.SpscLinkedArrayQueue;
import io.reactivex.internal.util.AtomicThrowable;
import io.reactivex.internal.util.ErrorMode;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.ArrayDeque;
import java.util.concurrent.atomic.AtomicInteger;

public final class ObservableConcatMapEager<T, R> extends AbstractObservableWithUpstream<T, R> {
  final ErrorMode errorMode;
  
  final Function<? super T, ? extends ObservableSource<? extends R>> mapper;
  
  final int maxConcurrency;
  
  final int prefetch;
  
  public ObservableConcatMapEager(ObservableSource<T> paramObservableSource, Function<? super T, ? extends ObservableSource<? extends R>> paramFunction, ErrorMode paramErrorMode, int paramInt1, int paramInt2) {
    super(paramObservableSource);
    this.mapper = paramFunction;
    this.errorMode = paramErrorMode;
    this.maxConcurrency = paramInt1;
    this.prefetch = paramInt2;
  }
  
  protected void subscribeActual(Observer<? super R> paramObserver) {
    this.source.subscribe(new ConcatMapEagerMainObserver<T, R>(paramObserver, this.mapper, this.maxConcurrency, this.prefetch, this.errorMode));
  }
  
  static final class ConcatMapEagerMainObserver<T, R> extends AtomicInteger implements Observer<T>, Disposable, InnerQueuedObserverSupport<R> {
    private static final long serialVersionUID = 8080567949447303262L;
    
    int activeCount;
    
    volatile boolean cancelled;
    
    InnerQueuedObserver<R> current;
    
    volatile boolean done;
    
    final Observer<? super R> downstream;
    
    final AtomicThrowable error;
    
    final ErrorMode errorMode;
    
    final Function<? super T, ? extends ObservableSource<? extends R>> mapper;
    
    final int maxConcurrency;
    
    final ArrayDeque<InnerQueuedObserver<R>> observers;
    
    final int prefetch;
    
    SimpleQueue<T> queue;
    
    int sourceMode;
    
    Disposable upstream;
    
    ConcatMapEagerMainObserver(Observer<? super R> param1Observer, Function<? super T, ? extends ObservableSource<? extends R>> param1Function, int param1Int1, int param1Int2, ErrorMode param1ErrorMode) {
      this.downstream = param1Observer;
      this.mapper = param1Function;
      this.maxConcurrency = param1Int1;
      this.prefetch = param1Int2;
      this.errorMode = param1ErrorMode;
      this.error = new AtomicThrowable();
      this.observers = new ArrayDeque<InnerQueuedObserver<R>>();
    }
    
    public void dispose() {
      if (this.cancelled)
        return; 
      this.cancelled = true;
      this.upstream.dispose();
      drainAndDispose();
    }
    
    void disposeAll() {
      InnerQueuedObserver<R> innerQueuedObserver = this.current;
      if (innerQueuedObserver != null)
        innerQueuedObserver.dispose(); 
      while (true) {
        innerQueuedObserver = this.observers.poll();
        if (innerQueuedObserver == null)
          return; 
        innerQueuedObserver.dispose();
      } 
    }
    
    public void drain() {
      int j;
      if (getAndIncrement() != 0)
        return; 
      SimpleQueue<T> simpleQueue = this.queue;
      ArrayDeque<InnerQueuedObserver<R>> arrayDeque = this.observers;
      Observer<? super R> observer = this.downstream;
      ErrorMode errorMode = this.errorMode;
      int i = 1;
      label82: do {
        j = this.activeCount;
        while (j != this.maxConcurrency) {
          if (this.cancelled) {
            simpleQueue.clear();
            disposeAll();
            return;
          } 
          if (errorMode == ErrorMode.IMMEDIATE && (Throwable)this.error.get() != null) {
            simpleQueue.clear();
            disposeAll();
            observer.onError(this.error.terminate());
            return;
          } 
          try {
            Object object = simpleQueue.poll();
            if (object == null)
              break; 
            object = ObjectHelper.requireNonNull(this.mapper.apply(object), "The mapper returned a null ObservableSource");
            InnerQueuedObserver<R> innerQueuedObserver = new InnerQueuedObserver(this, this.prefetch);
            arrayDeque.offer(innerQueuedObserver);
            object.subscribe((Observer)innerQueuedObserver);
            j++;
          } finally {
            Exception exception = null;
            Exceptions.throwIfFatal(exception);
            this.upstream.dispose();
            simpleQueue.clear();
            disposeAll();
            this.error.addThrowable(exception);
            observer.onError(this.error.terminate());
          } 
        } 
        this.activeCount = j;
        if (this.cancelled) {
          simpleQueue.clear();
          disposeAll();
          return;
        } 
        if (errorMode == ErrorMode.IMMEDIATE && (Throwable)this.error.get() != null) {
          simpleQueue.clear();
          disposeAll();
          observer.onError(this.error.terminate());
          return;
        } 
        InnerQueuedObserver<R> innerQueuedObserver2 = this.current;
        InnerQueuedObserver<R> innerQueuedObserver1 = innerQueuedObserver2;
        if (innerQueuedObserver2 == null) {
          if (errorMode == ErrorMode.BOUNDARY && (Throwable)this.error.get() != null) {
            simpleQueue.clear();
            disposeAll();
            observer.onError(this.error.terminate());
            return;
          } 
          boolean bool = this.done;
          innerQueuedObserver1 = arrayDeque.poll();
          if (innerQueuedObserver1 == null) {
            j = 1;
          } else {
            j = 0;
          } 
          if (bool && j != 0) {
            if ((Throwable)this.error.get() != null) {
              simpleQueue.clear();
              disposeAll();
              observer.onError(this.error.terminate());
            } else {
              observer.onComplete();
            } 
            return;
          } 
          if (j == 0)
            this.current = innerQueuedObserver1; 
        } 
        if (innerQueuedObserver1 != null) {
          SimpleQueue simpleQueue1 = innerQueuedObserver1.queue();
          while (true) {
            if (this.cancelled) {
              simpleQueue.clear();
              disposeAll();
              return;
            } 
            boolean bool = innerQueuedObserver1.isDone();
            if (errorMode == ErrorMode.IMMEDIATE && (Throwable)this.error.get() != null) {
              simpleQueue.clear();
              disposeAll();
              observer.onError(this.error.terminate());
              return;
            } 
            try {
              Object object = simpleQueue1.poll();
              if (object == null) {
                j = 1;
              } else {
                j = 0;
              } 
              if (bool && j != 0) {
                this.current = null;
                this.activeCount--;
                continue label82;
              } 
              if (j != 0)
                break; 
              observer.onNext(object);
            } finally {
              innerQueuedObserver1 = null;
              Exceptions.throwIfFatal((Throwable)innerQueuedObserver1);
              this.error.addThrowable((Throwable)innerQueuedObserver1);
              this.current = null;
              this.activeCount--;
            } 
          } 
        } 
        j = addAndGet(-i);
        i = j;
      } while (j != 0);
    }
    
    void drainAndDispose() {
      if (getAndIncrement() == 0)
        do {
          this.queue.clear();
          disposeAll();
        } while (decrementAndGet() != 0); 
    }
    
    public void innerComplete(InnerQueuedObserver<R> param1InnerQueuedObserver) {
      param1InnerQueuedObserver.setDone();
      drain();
    }
    
    public void innerError(InnerQueuedObserver<R> param1InnerQueuedObserver, Throwable param1Throwable) {
      if (this.error.addThrowable(param1Throwable)) {
        if (this.errorMode == ErrorMode.IMMEDIATE)
          this.upstream.dispose(); 
        param1InnerQueuedObserver.setDone();
        drain();
      } else {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
    
    public void innerNext(InnerQueuedObserver<R> param1InnerQueuedObserver, R param1R) {
      param1InnerQueuedObserver.queue().offer(param1R);
      drain();
    }
    
    public boolean isDisposed() {
      return this.cancelled;
    }
    
    public void onComplete() {
      this.done = true;
      drain();
    }
    
    public void onError(Throwable param1Throwable) {
      if (this.error.addThrowable(param1Throwable)) {
        this.done = true;
        drain();
      } else {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
    
    public void onNext(T param1T) {
      if (this.sourceMode == 0)
        this.queue.offer(param1T); 
      drain();
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      if (DisposableHelper.validate(this.upstream, param1Disposable)) {
        this.upstream = param1Disposable;
        if (param1Disposable instanceof QueueDisposable) {
          QueueDisposable queueDisposable = (QueueDisposable)param1Disposable;
          int i = queueDisposable.requestFusion(3);
          if (i == 1) {
            this.sourceMode = i;
            this.queue = (SimpleQueue<T>)queueDisposable;
            this.done = true;
            this.downstream.onSubscribe(this);
            drain();
            return;
          } 
          if (i == 2) {
            this.sourceMode = i;
            this.queue = (SimpleQueue<T>)queueDisposable;
            this.downstream.onSubscribe(this);
            return;
          } 
        } 
        this.queue = (SimpleQueue<T>)new SpscLinkedArrayQueue(this.prefetch);
        this.downstream.onSubscribe(this);
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableConcatMapEager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */