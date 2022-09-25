package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.internal.queue.SpscLinkedArrayQueue;
import io.reactivex.observables.GroupedObservable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public final class ObservableGroupBy<T, K, V> extends AbstractObservableWithUpstream<T, GroupedObservable<K, V>> {
  final int bufferSize;
  
  final boolean delayError;
  
  final Function<? super T, ? extends K> keySelector;
  
  final Function<? super T, ? extends V> valueSelector;
  
  public ObservableGroupBy(ObservableSource<T> paramObservableSource, Function<? super T, ? extends K> paramFunction, Function<? super T, ? extends V> paramFunction1, int paramInt, boolean paramBoolean) {
    super(paramObservableSource);
    this.keySelector = paramFunction;
    this.valueSelector = paramFunction1;
    this.bufferSize = paramInt;
    this.delayError = paramBoolean;
  }
  
  public void subscribeActual(Observer<? super GroupedObservable<K, V>> paramObserver) {
    this.source.subscribe(new GroupByObserver<T, K, V>(paramObserver, this.keySelector, this.valueSelector, this.bufferSize, this.delayError));
  }
  
  public static final class GroupByObserver<T, K, V> extends AtomicInteger implements Observer<T>, Disposable {
    static final Object NULL_KEY = new Object();
    
    private static final long serialVersionUID = -3688291656102519502L;
    
    final int bufferSize;
    
    final AtomicBoolean cancelled = new AtomicBoolean();
    
    final boolean delayError;
    
    final Observer<? super GroupedObservable<K, V>> downstream;
    
    final Map<Object, ObservableGroupBy.GroupedUnicast<K, V>> groups;
    
    final Function<? super T, ? extends K> keySelector;
    
    Disposable upstream;
    
    final Function<? super T, ? extends V> valueSelector;
    
    public GroupByObserver(Observer<? super GroupedObservable<K, V>> param1Observer, Function<? super T, ? extends K> param1Function, Function<? super T, ? extends V> param1Function1, int param1Int, boolean param1Boolean) {
      this.downstream = param1Observer;
      this.keySelector = param1Function;
      this.valueSelector = param1Function1;
      this.bufferSize = param1Int;
      this.delayError = param1Boolean;
      this.groups = new ConcurrentHashMap<Object, ObservableGroupBy.GroupedUnicast<K, V>>();
      lazySet(1);
    }
    
    public void cancel(K param1K) {
      if (param1K == null)
        param1K = (K)NULL_KEY; 
      this.groups.remove(param1K);
      if (decrementAndGet() == 0)
        this.upstream.dispose(); 
    }
    
    public void dispose() {
      if (this.cancelled.compareAndSet(false, true) && decrementAndGet() == 0)
        this.upstream.dispose(); 
    }
    
    public boolean isDisposed() {
      return this.cancelled.get();
    }
    
    public void onComplete() {
      ArrayList arrayList = new ArrayList(this.groups.values());
      this.groups.clear();
      Iterator<ObservableGroupBy.GroupedUnicast> iterator = arrayList.iterator();
      while (iterator.hasNext())
        ((ObservableGroupBy.GroupedUnicast)iterator.next()).onComplete(); 
      this.downstream.onComplete();
    }
    
    public void onError(Throwable param1Throwable) {
      ArrayList arrayList = new ArrayList(this.groups.values());
      this.groups.clear();
      Iterator<ObservableGroupBy.GroupedUnicast> iterator = arrayList.iterator();
      while (iterator.hasNext())
        ((ObservableGroupBy.GroupedUnicast)iterator.next()).onError(param1Throwable); 
      this.downstream.onError(param1Throwable);
    }
    
    public void onNext(T param1T) {
      try {
        Object object2;
        Object object1 = this.keySelector.apply(param1T);
        if (object1 != null) {
          object2 = object1;
        } else {
          object2 = NULL_KEY;
        } 
      } finally {
        param1T = null;
        Exceptions.throwIfFatal((Throwable)param1T);
        this.upstream.dispose();
        onError((Throwable)param1T);
      } 
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      if (DisposableHelper.validate(this.upstream, param1Disposable)) {
        this.upstream = param1Disposable;
        this.downstream.onSubscribe(this);
      } 
    }
  }
  
  static final class GroupedUnicast<K, T> extends GroupedObservable<K, T> {
    final ObservableGroupBy.State<T, K> state;
    
    protected GroupedUnicast(K param1K, ObservableGroupBy.State<T, K> param1State) {
      super(param1K);
      this.state = param1State;
    }
    
    public static <T, K> GroupedUnicast<K, T> createWith(K param1K, int param1Int, ObservableGroupBy.GroupByObserver<?, K, T> param1GroupByObserver, boolean param1Boolean) {
      return new GroupedUnicast<K, T>(param1K, new ObservableGroupBy.State<T, K>(param1Int, param1GroupByObserver, param1K, param1Boolean));
    }
    
    public void onComplete() {
      this.state.onComplete();
    }
    
    public void onError(Throwable param1Throwable) {
      this.state.onError(param1Throwable);
    }
    
    public void onNext(T param1T) {
      this.state.onNext(param1T);
    }
    
    protected void subscribeActual(Observer<? super T> param1Observer) {
      this.state.subscribe(param1Observer);
    }
  }
  
  static final class State<T, K> extends AtomicInteger implements Disposable, ObservableSource<T> {
    private static final long serialVersionUID = -3852313036005250360L;
    
    final AtomicReference<Observer<? super T>> actual = new AtomicReference<Observer<? super T>>();
    
    final AtomicBoolean cancelled = new AtomicBoolean();
    
    final boolean delayError;
    
    volatile boolean done;
    
    Throwable error;
    
    final K key;
    
    final AtomicBoolean once = new AtomicBoolean();
    
    final ObservableGroupBy.GroupByObserver<?, K, T> parent;
    
    final SpscLinkedArrayQueue<T> queue;
    
    State(int param1Int, ObservableGroupBy.GroupByObserver<?, K, T> param1GroupByObserver, K param1K, boolean param1Boolean) {
      this.queue = new SpscLinkedArrayQueue(param1Int);
      this.parent = param1GroupByObserver;
      this.key = param1K;
      this.delayError = param1Boolean;
    }
    
    boolean checkTerminated(boolean param1Boolean1, boolean param1Boolean2, Observer<? super T> param1Observer, boolean param1Boolean3) {
      if (this.cancelled.get()) {
        this.queue.clear();
        this.parent.cancel(this.key);
        this.actual.lazySet(null);
        return true;
      } 
      if (param1Boolean1)
        if (param1Boolean3) {
          if (param1Boolean2) {
            Throwable throwable = this.error;
            this.actual.lazySet(null);
            if (throwable != null) {
              param1Observer.onError(throwable);
            } else {
              param1Observer.onComplete();
            } 
            return true;
          } 
        } else {
          Throwable throwable = this.error;
          if (throwable != null) {
            this.queue.clear();
            this.actual.lazySet(null);
            param1Observer.onError(throwable);
            return true;
          } 
          if (param1Boolean2) {
            this.actual.lazySet(null);
            param1Observer.onComplete();
            return true;
          } 
        }  
      return false;
    }
    
    public void dispose() {
      if (this.cancelled.compareAndSet(false, true) && getAndIncrement() == 0) {
        this.actual.lazySet(null);
        this.parent.cancel(this.key);
      } 
    }
    
    void drain() {
      if (getAndIncrement() != 0)
        return; 
      SpscLinkedArrayQueue<T> spscLinkedArrayQueue = this.queue;
      boolean bool = this.delayError;
      Observer<? super T> observer = this.actual.get();
      int i = 1;
      while (true) {
        if (observer != null)
          while (true) {
            boolean bool2;
            boolean bool1 = this.done;
            Object object = spscLinkedArrayQueue.poll();
            if (object == null) {
              bool2 = true;
            } else {
              bool2 = false;
            } 
            if (checkTerminated(bool1, bool2, observer, bool))
              return; 
            if (bool2)
              break; 
            observer.onNext(object);
          }  
        int j = addAndGet(-i);
        if (j == 0)
          return; 
        i = j;
        if (observer == null) {
          observer = this.actual.get();
          i = j;
        } 
      } 
    }
    
    public boolean isDisposed() {
      return this.cancelled.get();
    }
    
    public void onComplete() {
      this.done = true;
      drain();
    }
    
    public void onError(Throwable param1Throwable) {
      this.error = param1Throwable;
      this.done = true;
      drain();
    }
    
    public void onNext(T param1T) {
      this.queue.offer(param1T);
      drain();
    }
    
    public void subscribe(Observer<? super T> param1Observer) {
      if (this.once.compareAndSet(false, true)) {
        param1Observer.onSubscribe(this);
        this.actual.lazySet(param1Observer);
        if (this.cancelled.get()) {
          this.actual.lazySet(null);
        } else {
          drain();
        } 
      } else {
        EmptyDisposable.error(new IllegalStateException("Only one Observer allowed!"), param1Observer);
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableGroupBy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */