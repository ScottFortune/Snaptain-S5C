package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.fuseable.QueueDisposable;
import io.reactivex.internal.fuseable.SimpleQueue;
import io.reactivex.internal.observers.BasicIntQueueDisposable;
import io.reactivex.internal.queue.SpscLinkedArrayQueue;
import io.reactivex.plugins.RxJavaPlugins;

public final class ObservableObserveOn<T> extends AbstractObservableWithUpstream<T, T> {
  final int bufferSize;
  
  final boolean delayError;
  
  final Scheduler scheduler;
  
  public ObservableObserveOn(ObservableSource<T> paramObservableSource, Scheduler paramScheduler, boolean paramBoolean, int paramInt) {
    super(paramObservableSource);
    this.scheduler = paramScheduler;
    this.delayError = paramBoolean;
    this.bufferSize = paramInt;
  }
  
  protected void subscribeActual(Observer<? super T> paramObserver) {
    Scheduler scheduler = this.scheduler;
    if (scheduler instanceof io.reactivex.internal.schedulers.TrampolineScheduler) {
      this.source.subscribe(paramObserver);
    } else {
      Scheduler.Worker worker = scheduler.createWorker();
      this.source.subscribe(new ObserveOnObserver<T>(paramObserver, worker, this.delayError, this.bufferSize));
    } 
  }
  
  static final class ObserveOnObserver<T> extends BasicIntQueueDisposable<T> implements Observer<T>, Runnable {
    private static final long serialVersionUID = 6576896619930983584L;
    
    final int bufferSize;
    
    final boolean delayError;
    
    volatile boolean disposed;
    
    volatile boolean done;
    
    final Observer<? super T> downstream;
    
    Throwable error;
    
    boolean outputFused;
    
    SimpleQueue<T> queue;
    
    int sourceMode;
    
    Disposable upstream;
    
    final Scheduler.Worker worker;
    
    ObserveOnObserver(Observer<? super T> param1Observer, Scheduler.Worker param1Worker, boolean param1Boolean, int param1Int) {
      this.downstream = param1Observer;
      this.worker = param1Worker;
      this.delayError = param1Boolean;
      this.bufferSize = param1Int;
    }
    
    boolean checkTerminated(boolean param1Boolean1, boolean param1Boolean2, Observer<? super T> param1Observer) {
      if (this.disposed) {
        this.queue.clear();
        return true;
      } 
      if (param1Boolean1) {
        Throwable throwable = this.error;
        if (this.delayError) {
          if (param1Boolean2) {
            this.disposed = true;
            if (throwable != null) {
              param1Observer.onError(throwable);
            } else {
              param1Observer.onComplete();
            } 
            this.worker.dispose();
            return true;
          } 
        } else {
          if (throwable != null) {
            this.disposed = true;
            this.queue.clear();
            param1Observer.onError(throwable);
            this.worker.dispose();
            return true;
          } 
          if (param1Boolean2) {
            this.disposed = true;
            param1Observer.onComplete();
            this.worker.dispose();
            return true;
          } 
        } 
      } 
      return false;
    }
    
    public void clear() {
      this.queue.clear();
    }
    
    public void dispose() {
      if (!this.disposed) {
        this.disposed = true;
        this.upstream.dispose();
        this.worker.dispose();
        if (!this.outputFused && getAndIncrement() == 0)
          this.queue.clear(); 
      } 
    }
    
    void drainFused() {
      int j;
      int i = 1;
      do {
        if (this.disposed)
          return; 
        boolean bool = this.done;
        Throwable throwable = this.error;
        if (!this.delayError && bool && throwable != null) {
          this.disposed = true;
          this.downstream.onError(throwable);
          this.worker.dispose();
          return;
        } 
        this.downstream.onNext(null);
        if (bool) {
          this.disposed = true;
          throwable = this.error;
          if (throwable != null) {
            this.downstream.onError(throwable);
          } else {
            this.downstream.onComplete();
          } 
          this.worker.dispose();
          return;
        } 
        j = addAndGet(-i);
        i = j;
      } while (j != 0);
    }
    
    void drainNormal() {
      SimpleQueue<T> simpleQueue = this.queue;
      Observer<? super T> observer = this.downstream;
      int i = 1;
      label23: while (true) {
        if (checkTerminated(this.done, simpleQueue.isEmpty(), observer))
          return; 
        while (true) {
          boolean bool = this.done;
          try {
            boolean bool1;
            Object object = simpleQueue.poll();
            if (object == null) {
              bool1 = true;
            } else {
              bool1 = false;
            } 
            if (checkTerminated(bool, bool1, observer))
              return; 
            if (bool1) {
              int j = addAndGet(-i);
              i = j;
              if (j == 0)
                return; 
              continue label23;
            } 
            observer.onNext(object);
          } finally {
            Exception exception = null;
            Exceptions.throwIfFatal(exception);
            this.disposed = true;
            this.upstream.dispose();
            simpleQueue.clear();
            observer.onError(exception);
            this.worker.dispose();
          } 
        } 
        break;
      } 
    }
    
    public boolean isDisposed() {
      return this.disposed;
    }
    
    public boolean isEmpty() {
      return this.queue.isEmpty();
    }
    
    public void onComplete() {
      if (this.done)
        return; 
      this.done = true;
      schedule();
    }
    
    public void onError(Throwable param1Throwable) {
      if (this.done) {
        RxJavaPlugins.onError(param1Throwable);
        return;
      } 
      this.error = param1Throwable;
      this.done = true;
      schedule();
    }
    
    public void onNext(T param1T) {
      if (this.done)
        return; 
      if (this.sourceMode != 2)
        this.queue.offer(param1T); 
      schedule();
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      if (DisposableHelper.validate(this.upstream, param1Disposable)) {
        this.upstream = param1Disposable;
        if (param1Disposable instanceof QueueDisposable) {
          QueueDisposable queueDisposable = (QueueDisposable)param1Disposable;
          int i = queueDisposable.requestFusion(7);
          if (i == 1) {
            this.sourceMode = i;
            this.queue = (SimpleQueue<T>)queueDisposable;
            this.done = true;
            this.downstream.onSubscribe((Disposable)this);
            schedule();
            return;
          } 
          if (i == 2) {
            this.sourceMode = i;
            this.queue = (SimpleQueue<T>)queueDisposable;
            this.downstream.onSubscribe((Disposable)this);
            return;
          } 
        } 
        this.queue = (SimpleQueue<T>)new SpscLinkedArrayQueue(this.bufferSize);
        this.downstream.onSubscribe((Disposable)this);
      } 
    }
    
    public T poll() throws Exception {
      return (T)this.queue.poll();
    }
    
    public int requestFusion(int param1Int) {
      if ((param1Int & 0x2) != 0) {
        this.outputFused = true;
        return 2;
      } 
      return 0;
    }
    
    public void run() {
      if (this.outputFused) {
        drainFused();
      } else {
        drainNormal();
      } 
    }
    
    void schedule() {
      if (getAndIncrement() == 0)
        this.worker.schedule(this); 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableObserveOn.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */