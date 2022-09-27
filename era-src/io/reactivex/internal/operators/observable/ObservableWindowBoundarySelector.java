package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.fuseable.SimplePlainQueue;
import io.reactivex.internal.observers.QueueDrainObserver;
import io.reactivex.internal.queue.MpscLinkedQueue;
import io.reactivex.internal.util.NotificationLite;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.SerializedObserver;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.subjects.UnicastSubject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public final class ObservableWindowBoundarySelector<T, B, V> extends AbstractObservableWithUpstream<T, Observable<T>> {
  final int bufferSize;
  
  final Function<? super B, ? extends ObservableSource<V>> close;
  
  final ObservableSource<B> open;
  
  public ObservableWindowBoundarySelector(ObservableSource<T> paramObservableSource, ObservableSource<B> paramObservableSource1, Function<? super B, ? extends ObservableSource<V>> paramFunction, int paramInt) {
    super(paramObservableSource);
    this.open = paramObservableSource1;
    this.close = paramFunction;
    this.bufferSize = paramInt;
  }
  
  public void subscribeActual(Observer<? super Observable<T>> paramObserver) {
    this.source.subscribe((Observer)new WindowBoundaryMainObserver<Object, B, V>((Observer<? super Observable<?>>)new SerializedObserver(paramObserver), this.open, this.close, this.bufferSize));
  }
  
  static final class OperatorWindowBoundaryCloseObserver<T, V> extends DisposableObserver<V> {
    boolean done;
    
    final ObservableWindowBoundarySelector.WindowBoundaryMainObserver<T, ?, V> parent;
    
    final UnicastSubject<T> w;
    
    OperatorWindowBoundaryCloseObserver(ObservableWindowBoundarySelector.WindowBoundaryMainObserver<T, ?, V> param1WindowBoundaryMainObserver, UnicastSubject<T> param1UnicastSubject) {
      this.parent = param1WindowBoundaryMainObserver;
      this.w = param1UnicastSubject;
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
      dispose();
      onComplete();
    }
  }
  
  static final class OperatorWindowBoundaryOpenObserver<T, B> extends DisposableObserver<B> {
    final ObservableWindowBoundarySelector.WindowBoundaryMainObserver<T, B, ?> parent;
    
    OperatorWindowBoundaryOpenObserver(ObservableWindowBoundarySelector.WindowBoundaryMainObserver<T, B, ?> param1WindowBoundaryMainObserver) {
      this.parent = param1WindowBoundaryMainObserver;
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
  
  static final class WindowBoundaryMainObserver<T, B, V> extends QueueDrainObserver<T, Object, Observable<T>> implements Disposable {
    final AtomicReference<Disposable> boundary = new AtomicReference<Disposable>();
    
    final int bufferSize;
    
    final Function<? super B, ? extends ObservableSource<V>> close;
    
    final ObservableSource<B> open;
    
    final CompositeDisposable resources;
    
    final AtomicBoolean stopWindows = new AtomicBoolean();
    
    Disposable upstream;
    
    final AtomicLong windows = new AtomicLong();
    
    final List<UnicastSubject<T>> ws;
    
    WindowBoundaryMainObserver(Observer<? super Observable<T>> param1Observer, ObservableSource<B> param1ObservableSource, Function<? super B, ? extends ObservableSource<V>> param1Function, int param1Int) {
      super(param1Observer, (SimplePlainQueue)new MpscLinkedQueue());
      this.open = param1ObservableSource;
      this.close = param1Function;
      this.bufferSize = param1Int;
      this.resources = new CompositeDisposable();
      this.ws = new ArrayList<UnicastSubject<T>>();
      this.windows.lazySet(1L);
    }
    
    public void accept(Observer<? super Observable<T>> param1Observer, Object param1Object) {}
    
    void close(ObservableWindowBoundarySelector.OperatorWindowBoundaryCloseObserver<T, V> param1OperatorWindowBoundaryCloseObserver) {
      this.resources.delete((Disposable)param1OperatorWindowBoundaryCloseObserver);
      this.queue.offer(new ObservableWindowBoundarySelector.WindowOperation<T, Object>(param1OperatorWindowBoundaryCloseObserver.w, null));
      if (enter())
        drainLoop(); 
    }
    
    public void dispose() {
      if (this.stopWindows.compareAndSet(false, true)) {
        DisposableHelper.dispose(this.boundary);
        if (this.windows.decrementAndGet() == 0L)
          this.upstream.dispose(); 
      } 
    }
    
    void disposeBoundary() {
      this.resources.dispose();
      DisposableHelper.dispose(this.boundary);
    }
    
    void drainLoop() {
      MpscLinkedQueue mpscLinkedQueue = (MpscLinkedQueue)this.queue;
      Observer observer = this.downstream;
      List<UnicastSubject<T>> list = this.ws;
      int i = 1;
      while (true) {
        Iterator<UnicastSubject<T>> iterator1;
        int j;
        boolean bool = this.done;
        Object<Object, Object> object = (Object<Object, Object>)mpscLinkedQueue.poll();
        if (object == null) {
          j = 1;
        } else {
          j = 0;
        } 
        if (bool && j) {
          disposeBoundary();
          Throwable throwable = this.error;
          if (throwable != null) {
            iterator1 = list.iterator();
            while (iterator1.hasNext())
              ((UnicastSubject)iterator1.next()).onError(throwable); 
          } else {
            Iterator<UnicastSubject<T>> iterator = list.iterator();
            while (iterator.hasNext())
              ((UnicastSubject)iterator.next()).onComplete(); 
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
        if (object instanceof ObservableWindowBoundarySelector.WindowOperation) {
          ObservableWindowBoundarySelector.WindowOperation windowOperation = (ObservableWindowBoundarySelector.WindowOperation)object;
          if (windowOperation.w != null) {
            if (list.remove(windowOperation.w)) {
              windowOperation.w.onComplete();
              if (this.windows.decrementAndGet() == 0L) {
                disposeBoundary();
                return;
              } 
            } 
            continue;
          } 
          if (this.stopWindows.get())
            continue; 
          object = (Object<Object, Object>)UnicastSubject.create(this.bufferSize);
          list.add(object);
          iterator1.onNext(object);
          try {
          
          } finally {
            object = null;
            Exceptions.throwIfFatal((Throwable)object);
            this.stopWindows.set(true);
          } 
          continue;
        } 
        Iterator<UnicastSubject<T>> iterator2 = list.iterator();
        while (iterator2.hasNext())
          ((UnicastSubject)iterator2.next()).onNext(NotificationLite.getValue(object)); 
      } 
    }
    
    void error(Throwable param1Throwable) {
      this.upstream.dispose();
      this.resources.dispose();
      onError(param1Throwable);
    }
    
    public boolean isDisposed() {
      return this.stopWindows.get();
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
      if (fastEnter()) {
        Iterator<UnicastSubject<T>> iterator = this.ws.iterator();
        while (iterator.hasNext())
          ((UnicastSubject)iterator.next()).onNext(param1T); 
        if (leave(-1) == 0)
          return; 
      } else {
        this.queue.offer(NotificationLite.next(param1T));
        if (!enter())
          return; 
      } 
      drainLoop();
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      if (DisposableHelper.validate(this.upstream, param1Disposable)) {
        this.upstream = param1Disposable;
        this.downstream.onSubscribe(this);
        if (this.stopWindows.get())
          return; 
        ObservableWindowBoundarySelector.OperatorWindowBoundaryOpenObserver<Object, Object> operatorWindowBoundaryOpenObserver = new ObservableWindowBoundarySelector.OperatorWindowBoundaryOpenObserver<Object, Object>(this);
        if (this.boundary.compareAndSet(null, operatorWindowBoundaryOpenObserver))
          this.open.subscribe((Observer)operatorWindowBoundaryOpenObserver); 
      } 
    }
    
    void open(B param1B) {
      this.queue.offer(new ObservableWindowBoundarySelector.WindowOperation<Object, B>(null, param1B));
      if (enter())
        drainLoop(); 
    }
  }
  
  static final class WindowOperation<T, B> {
    final B open;
    
    final UnicastSubject<T> w;
    
    WindowOperation(UnicastSubject<T> param1UnicastSubject, B param1B) {
      this.w = param1UnicastSubject;
      this.open = param1B;
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableWindowBoundarySelector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */