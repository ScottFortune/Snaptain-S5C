package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.disposables.SequentialDisposable;
import io.reactivex.internal.fuseable.SimplePlainQueue;
import io.reactivex.internal.observers.QueueDrainObserver;
import io.reactivex.internal.queue.MpscLinkedQueue;
import io.reactivex.internal.util.NotificationLite;
import io.reactivex.observers.SerializedObserver;
import io.reactivex.subjects.UnicastSubject;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public final class ObservableWindowTimed<T> extends AbstractObservableWithUpstream<T, Observable<T>> {
  final int bufferSize;
  
  final long maxSize;
  
  final boolean restartTimerOnMaxSize;
  
  final Scheduler scheduler;
  
  final long timeskip;
  
  final long timespan;
  
  final TimeUnit unit;
  
  public ObservableWindowTimed(ObservableSource<T> paramObservableSource, long paramLong1, long paramLong2, TimeUnit paramTimeUnit, Scheduler paramScheduler, long paramLong3, int paramInt, boolean paramBoolean) {
    super(paramObservableSource);
    this.timespan = paramLong1;
    this.timeskip = paramLong2;
    this.unit = paramTimeUnit;
    this.scheduler = paramScheduler;
    this.maxSize = paramLong3;
    this.bufferSize = paramInt;
    this.restartTimerOnMaxSize = paramBoolean;
  }
  
  public void subscribeActual(Observer<? super Observable<T>> paramObserver) {
    SerializedObserver serializedObserver = new SerializedObserver(paramObserver);
    if (this.timespan == this.timeskip) {
      if (this.maxSize == Long.MAX_VALUE) {
        this.source.subscribe(new WindowExactUnboundedObserver((Observer<? super Observable<?>>)serializedObserver, this.timespan, this.unit, this.scheduler, this.bufferSize));
        return;
      } 
      this.source.subscribe((Observer)new WindowExactBoundedObserver((Observer<? super Observable<?>>)serializedObserver, this.timespan, this.unit, this.scheduler, this.bufferSize, this.maxSize, this.restartTimerOnMaxSize));
      return;
    } 
    this.source.subscribe((Observer)new WindowSkipObserver((Observer<? super Observable<?>>)serializedObserver, this.timespan, this.timeskip, this.unit, this.scheduler.createWorker(), this.bufferSize));
  }
  
  static final class WindowExactBoundedObserver<T> extends QueueDrainObserver<T, Object, Observable<T>> implements Disposable {
    final int bufferSize;
    
    long count;
    
    final long maxSize;
    
    long producerIndex;
    
    final boolean restartTimerOnMaxSize;
    
    final Scheduler scheduler;
    
    volatile boolean terminated;
    
    final SequentialDisposable timer = new SequentialDisposable();
    
    final long timespan;
    
    final TimeUnit unit;
    
    Disposable upstream;
    
    UnicastSubject<T> window;
    
    final Scheduler.Worker worker;
    
    WindowExactBoundedObserver(Observer<? super Observable<T>> param1Observer, long param1Long1, TimeUnit param1TimeUnit, Scheduler param1Scheduler, int param1Int, long param1Long2, boolean param1Boolean) {
      super(param1Observer, (SimplePlainQueue)new MpscLinkedQueue());
      this.timespan = param1Long1;
      this.unit = param1TimeUnit;
      this.scheduler = param1Scheduler;
      this.bufferSize = param1Int;
      this.maxSize = param1Long2;
      this.restartTimerOnMaxSize = param1Boolean;
      if (param1Boolean) {
        this.worker = param1Scheduler.createWorker();
      } else {
        this.worker = null;
      } 
    }
    
    public void dispose() {
      this.cancelled = true;
    }
    
    void disposeTimer() {
      DisposableHelper.dispose((AtomicReference)this.timer);
      Scheduler.Worker worker = this.worker;
      if (worker != null)
        worker.dispose(); 
    }
    
    void drainLoop() {
      MpscLinkedQueue mpscLinkedQueue = (MpscLinkedQueue)this.queue;
      Observer observer = this.downstream;
      UnicastSubject<T> unicastSubject = this.window;
      int i = 1;
      while (true) {
        int j;
        if (this.terminated) {
          this.upstream.dispose();
          mpscLinkedQueue.clear();
          disposeTimer();
          return;
        } 
        boolean bool1 = this.done;
        Object object = mpscLinkedQueue.poll();
        if (object == null) {
          j = 1;
        } else {
          j = 0;
        } 
        boolean bool2 = object instanceof ConsumerIndexHolder;
        if (bool1 && (j || bool2)) {
          this.window = null;
          mpscLinkedQueue.clear();
          object = this.error;
          if (object != null) {
            unicastSubject.onError((Throwable)object);
          } else {
            unicastSubject.onComplete();
          } 
          disposeTimer();
          return;
        } 
        if (j) {
          j = leave(-i);
          i = j;
          if (j == 0)
            return; 
          continue;
        } 
        if (bool2) {
          object = object;
          if (!this.restartTimerOnMaxSize || this.producerIndex == ((ConsumerIndexHolder)object).index) {
            unicastSubject.onComplete();
            this.count = 0L;
            unicastSubject = UnicastSubject.create(this.bufferSize);
            this.window = unicastSubject;
            observer.onNext(unicastSubject);
          } 
          continue;
        } 
        unicastSubject.onNext(NotificationLite.getValue(object));
        long l = this.count + 1L;
        if (l >= this.maxSize) {
          this.producerIndex++;
          this.count = 0L;
          unicastSubject.onComplete();
          object = UnicastSubject.create(this.bufferSize);
          this.window = (UnicastSubject<T>)object;
          this.downstream.onNext(object);
          Object object1 = object;
          if (this.restartTimerOnMaxSize) {
            Disposable disposable1 = (Disposable)this.timer.get();
            disposable1.dispose();
            object1 = this.worker;
            ConsumerIndexHolder consumerIndexHolder = new ConsumerIndexHolder(this.producerIndex, this);
            l = this.timespan;
            Disposable disposable2 = object1.schedulePeriodically(consumerIndexHolder, l, l, this.unit);
            object1 = object;
            if (!this.timer.compareAndSet(disposable1, disposable2)) {
              disposable2.dispose();
              object1 = object;
            } 
          } 
          continue;
        } 
        this.count = l;
      } 
    }
    
    public boolean isDisposed() {
      return this.cancelled;
    }
    
    public void onComplete() {
      this.done = true;
      if (enter())
        drainLoop(); 
      this.downstream.onComplete();
    }
    
    public void onError(Throwable param1Throwable) {
      this.error = param1Throwable;
      this.done = true;
      if (enter())
        drainLoop(); 
      this.downstream.onError(param1Throwable);
    }
    
    public void onNext(T param1T) {
      Disposable disposable;
      if (this.terminated)
        return; 
      if (fastEnter()) {
        UnicastSubject<T> unicastSubject = this.window;
        unicastSubject.onNext(param1T);
        long l = this.count + 1L;
        if (l >= this.maxSize) {
          this.producerIndex++;
          this.count = 0L;
          unicastSubject.onComplete();
          UnicastSubject<T> unicastSubject1 = UnicastSubject.create(this.bufferSize);
          this.window = unicastSubject1;
          this.downstream.onNext(unicastSubject1);
          if (this.restartTimerOnMaxSize) {
            ((Disposable)this.timer.get()).dispose();
            Scheduler.Worker worker = this.worker;
            ConsumerIndexHolder consumerIndexHolder = new ConsumerIndexHolder(this.producerIndex, this);
            l = this.timespan;
            disposable = worker.schedulePeriodically(consumerIndexHolder, l, l, this.unit);
            DisposableHelper.replace((AtomicReference)this.timer, disposable);
          } 
        } else {
          this.count = l;
        } 
        if (leave(-1) == 0)
          return; 
      } else {
        this.queue.offer(NotificationLite.next(disposable));
        if (!enter())
          return; 
      } 
      drainLoop();
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      if (DisposableHelper.validate(this.upstream, param1Disposable)) {
        Disposable disposable;
        this.upstream = param1Disposable;
        Observer observer = this.downstream;
        observer.onSubscribe(this);
        if (this.cancelled)
          return; 
        UnicastSubject<T> unicastSubject = UnicastSubject.create(this.bufferSize);
        this.window = unicastSubject;
        observer.onNext(unicastSubject);
        ConsumerIndexHolder consumerIndexHolder = new ConsumerIndexHolder(this.producerIndex, this);
        if (this.restartTimerOnMaxSize) {
          Scheduler.Worker worker = this.worker;
          long l = this.timespan;
          disposable = worker.schedulePeriodically(consumerIndexHolder, l, l, this.unit);
        } else {
          Scheduler scheduler = this.scheduler;
          long l = this.timespan;
          disposable = scheduler.schedulePeriodicallyDirect((Runnable)disposable, l, l, this.unit);
        } 
        this.timer.replace(disposable);
      } 
    }
    
    static final class ConsumerIndexHolder implements Runnable {
      final long index;
      
      final ObservableWindowTimed.WindowExactBoundedObserver<?> parent;
      
      ConsumerIndexHolder(long param2Long, ObservableWindowTimed.WindowExactBoundedObserver<?> param2WindowExactBoundedObserver) {
        this.index = param2Long;
        this.parent = param2WindowExactBoundedObserver;
      }
      
      public void run() {
        ObservableWindowTimed.WindowExactBoundedObserver<?> windowExactBoundedObserver = this.parent;
        if (!windowExactBoundedObserver.cancelled) {
          windowExactBoundedObserver.queue.offer(this);
        } else {
          windowExactBoundedObserver.terminated = true;
        } 
        if (windowExactBoundedObserver.enter())
          windowExactBoundedObserver.drainLoop(); 
      }
    }
  }
  
  static final class ConsumerIndexHolder implements Runnable {
    final long index;
    
    final ObservableWindowTimed.WindowExactBoundedObserver<?> parent;
    
    ConsumerIndexHolder(long param1Long, ObservableWindowTimed.WindowExactBoundedObserver<?> param1WindowExactBoundedObserver) {
      this.index = param1Long;
      this.parent = param1WindowExactBoundedObserver;
    }
    
    public void run() {
      ObservableWindowTimed.WindowExactBoundedObserver<?> windowExactBoundedObserver = this.parent;
      if (!windowExactBoundedObserver.cancelled) {
        windowExactBoundedObserver.queue.offer(this);
      } else {
        windowExactBoundedObserver.terminated = true;
      } 
      if (windowExactBoundedObserver.enter())
        windowExactBoundedObserver.drainLoop(); 
    }
  }
  
  static final class WindowExactUnboundedObserver<T> extends QueueDrainObserver<T, Object, Observable<T>> implements Observer<T>, Disposable, Runnable {
    static final Object NEXT = new Object();
    
    final int bufferSize;
    
    final Scheduler scheduler;
    
    volatile boolean terminated;
    
    final SequentialDisposable timer = new SequentialDisposable();
    
    final long timespan;
    
    final TimeUnit unit;
    
    Disposable upstream;
    
    UnicastSubject<T> window;
    
    WindowExactUnboundedObserver(Observer<? super Observable<T>> param1Observer, long param1Long, TimeUnit param1TimeUnit, Scheduler param1Scheduler, int param1Int) {
      super(param1Observer, (SimplePlainQueue)new MpscLinkedQueue());
      this.timespan = param1Long;
      this.unit = param1TimeUnit;
      this.scheduler = param1Scheduler;
      this.bufferSize = param1Int;
    }
    
    public void dispose() {
      this.cancelled = true;
    }
    
    void drainLoop() {
      MpscLinkedQueue mpscLinkedQueue = (MpscLinkedQueue)this.queue;
      Observer observer = this.downstream;
      UnicastSubject<T> unicastSubject = this.window;
      int i = 1;
      while (true) {
        boolean bool1 = this.terminated;
        boolean bool2 = this.done;
        Object object = mpscLinkedQueue.poll();
        if (bool2 && (object == null || object == NEXT)) {
          this.window = null;
          mpscLinkedQueue.clear();
          Throwable throwable = this.error;
          if (throwable != null) {
            unicastSubject.onError(throwable);
          } else {
            unicastSubject.onComplete();
          } 
          this.timer.dispose();
          return;
        } 
        if (object == null) {
          int j = leave(-i);
          i = j;
          if (j == 0)
            return; 
          continue;
        } 
        if (object == NEXT) {
          unicastSubject.onComplete();
          if (!bool1) {
            unicastSubject = UnicastSubject.create(this.bufferSize);
            this.window = unicastSubject;
            observer.onNext(unicastSubject);
            continue;
          } 
          this.upstream.dispose();
          continue;
        } 
        unicastSubject.onNext(NotificationLite.getValue(object));
      } 
    }
    
    public boolean isDisposed() {
      return this.cancelled;
    }
    
    public void onComplete() {
      this.done = true;
      if (enter())
        drainLoop(); 
      this.downstream.onComplete();
    }
    
    public void onError(Throwable param1Throwable) {
      this.error = param1Throwable;
      this.done = true;
      if (enter())
        drainLoop(); 
      this.downstream.onError(param1Throwable);
    }
    
    public void onNext(T param1T) {
      if (this.terminated)
        return; 
      if (fastEnter()) {
        this.window.onNext(param1T);
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
        this.window = UnicastSubject.create(this.bufferSize);
        Observer observer = this.downstream;
        observer.onSubscribe(this);
        observer.onNext(this.window);
        if (!this.cancelled) {
          Scheduler scheduler = this.scheduler;
          long l = this.timespan;
          Disposable disposable = scheduler.schedulePeriodicallyDirect(this, l, l, this.unit);
          this.timer.replace(disposable);
        } 
      } 
    }
    
    public void run() {
      if (this.cancelled)
        this.terminated = true; 
      this.queue.offer(NEXT);
      if (enter())
        drainLoop(); 
    }
  }
  
  static final class WindowSkipObserver<T> extends QueueDrainObserver<T, Object, Observable<T>> implements Disposable, Runnable {
    final int bufferSize;
    
    volatile boolean terminated;
    
    final long timeskip;
    
    final long timespan;
    
    final TimeUnit unit;
    
    Disposable upstream;
    
    final List<UnicastSubject<T>> windows;
    
    final Scheduler.Worker worker;
    
    WindowSkipObserver(Observer<? super Observable<T>> param1Observer, long param1Long1, long param1Long2, TimeUnit param1TimeUnit, Scheduler.Worker param1Worker, int param1Int) {
      super(param1Observer, (SimplePlainQueue)new MpscLinkedQueue());
      this.timespan = param1Long1;
      this.timeskip = param1Long2;
      this.unit = param1TimeUnit;
      this.worker = param1Worker;
      this.bufferSize = param1Int;
      this.windows = new LinkedList<UnicastSubject<T>>();
    }
    
    void complete(UnicastSubject<T> param1UnicastSubject) {
      this.queue.offer(new SubjectWork<T>(param1UnicastSubject, false));
      if (enter())
        drainLoop(); 
    }
    
    public void dispose() {
      this.cancelled = true;
    }
    
    void drainLoop() {
      MpscLinkedQueue mpscLinkedQueue = (MpscLinkedQueue)this.queue;
      Observer observer = this.downstream;
      List<UnicastSubject<T>> list = this.windows;
      int i = 1;
      while (true) {
        Iterator<UnicastSubject<T>> iterator1;
        int j;
        if (this.terminated) {
          this.upstream.dispose();
          mpscLinkedQueue.clear();
          list.clear();
          this.worker.dispose();
          return;
        } 
        boolean bool1 = this.done;
        Object object = mpscLinkedQueue.poll();
        if (object == null) {
          j = 1;
        } else {
          j = 0;
        } 
        boolean bool2 = object instanceof SubjectWork;
        if (bool1 && (j || bool2)) {
          mpscLinkedQueue.clear();
          Throwable throwable = this.error;
          if (throwable != null) {
            iterator1 = list.iterator();
            while (iterator1.hasNext())
              ((UnicastSubject)iterator1.next()).onError(throwable); 
          } else {
            iterator1 = list.iterator();
            while (iterator1.hasNext())
              ((UnicastSubject)iterator1.next()).onComplete(); 
          } 
          list.clear();
          this.worker.dispose();
          return;
        } 
        if (j) {
          j = leave(-i);
          i = j;
          if (j == 0)
            return; 
          continue;
        } 
        if (bool2) {
          object = object;
          if (((SubjectWork)object).open) {
            if (this.cancelled)
              continue; 
            object = UnicastSubject.create(this.bufferSize);
            list.add(object);
            iterator1.onNext(object);
            this.worker.schedule(new CompletionTask((UnicastSubject<T>)object), this.timespan, this.unit);
            continue;
          } 
          list.remove(((SubjectWork)object).w);
          ((SubjectWork)object).w.onComplete();
          if (list.isEmpty() && this.cancelled)
            this.terminated = true; 
          continue;
        } 
        Iterator<UnicastSubject<T>> iterator2 = list.iterator();
        while (iterator2.hasNext())
          ((UnicastSubject)iterator2.next()).onNext(object); 
      } 
    }
    
    public boolean isDisposed() {
      return this.cancelled;
    }
    
    public void onComplete() {
      this.done = true;
      if (enter())
        drainLoop(); 
      this.downstream.onComplete();
    }
    
    public void onError(Throwable param1Throwable) {
      this.error = param1Throwable;
      this.done = true;
      if (enter())
        drainLoop(); 
      this.downstream.onError(param1Throwable);
    }
    
    public void onNext(T param1T) {
      if (fastEnter()) {
        Iterator<UnicastSubject<T>> iterator = this.windows.iterator();
        while (iterator.hasNext())
          ((UnicastSubject)iterator.next()).onNext(param1T); 
        if (leave(-1) == 0)
          return; 
      } else {
        this.queue.offer(param1T);
        if (!enter())
          return; 
      } 
      drainLoop();
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      if (DisposableHelper.validate(this.upstream, param1Disposable)) {
        this.upstream = param1Disposable;
        this.downstream.onSubscribe(this);
        if (this.cancelled)
          return; 
        UnicastSubject<T> unicastSubject = UnicastSubject.create(this.bufferSize);
        this.windows.add(unicastSubject);
        this.downstream.onNext(unicastSubject);
        this.worker.schedule(new CompletionTask(unicastSubject), this.timespan, this.unit);
        Scheduler.Worker worker = this.worker;
        long l = this.timeskip;
        worker.schedulePeriodically(this, l, l, this.unit);
      } 
    }
    
    public void run() {
      SubjectWork subjectWork = new SubjectWork(UnicastSubject.create(this.bufferSize), true);
      if (!this.cancelled)
        this.queue.offer(subjectWork); 
      if (enter())
        drainLoop(); 
    }
    
    final class CompletionTask implements Runnable {
      private final UnicastSubject<T> w;
      
      CompletionTask(UnicastSubject<T> param2UnicastSubject) {
        this.w = param2UnicastSubject;
      }
      
      public void run() {
        ObservableWindowTimed.WindowSkipObserver.this.complete(this.w);
      }
    }
    
    static final class SubjectWork<T> {
      final boolean open;
      
      final UnicastSubject<T> w;
      
      SubjectWork(UnicastSubject<T> param2UnicastSubject, boolean param2Boolean) {
        this.w = param2UnicastSubject;
        this.open = param2Boolean;
      }
    }
  }
  
  final class CompletionTask implements Runnable {
    private final UnicastSubject<T> w;
    
    CompletionTask(UnicastSubject<T> param1UnicastSubject) {
      this.w = param1UnicastSubject;
    }
    
    public void run() {
      this.this$0.complete(this.w);
    }
  }
  
  static final class SubjectWork<T> {
    final boolean open;
    
    final UnicastSubject<T> w;
    
    SubjectWork(UnicastSubject<T> param1UnicastSubject, boolean param1Boolean) {
      this.w = param1UnicastSubject;
      this.open = param1Boolean;
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableWindowTimed.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */