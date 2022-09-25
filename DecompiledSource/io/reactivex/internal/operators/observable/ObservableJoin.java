package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.queue.SpscLinkedArrayQueue;
import io.reactivex.internal.util.ExceptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public final class ObservableJoin<TLeft, TRight, TLeftEnd, TRightEnd, R> extends AbstractObservableWithUpstream<TLeft, R> {
  final Function<? super TLeft, ? extends ObservableSource<TLeftEnd>> leftEnd;
  
  final ObservableSource<? extends TRight> other;
  
  final BiFunction<? super TLeft, ? super TRight, ? extends R> resultSelector;
  
  final Function<? super TRight, ? extends ObservableSource<TRightEnd>> rightEnd;
  
  public ObservableJoin(ObservableSource<TLeft> paramObservableSource, ObservableSource<? extends TRight> paramObservableSource1, Function<? super TLeft, ? extends ObservableSource<TLeftEnd>> paramFunction, Function<? super TRight, ? extends ObservableSource<TRightEnd>> paramFunction1, BiFunction<? super TLeft, ? super TRight, ? extends R> paramBiFunction) {
    super(paramObservableSource);
    this.other = paramObservableSource1;
    this.leftEnd = paramFunction;
    this.rightEnd = paramFunction1;
    this.resultSelector = paramBiFunction;
  }
  
  protected void subscribeActual(Observer<? super R> paramObserver) {
    JoinDisposable<TLeft, TRight, TLeftEnd, TRightEnd, R> joinDisposable = new JoinDisposable<TLeft, TRight, TLeftEnd, TRightEnd, R>(paramObserver, this.leftEnd, this.rightEnd, this.resultSelector);
    paramObserver.onSubscribe(joinDisposable);
    paramObserver = new ObservableGroupJoin.LeftRightObserver(joinDisposable, true);
    joinDisposable.disposables.add((Disposable)paramObserver);
    ObservableGroupJoin.LeftRightObserver leftRightObserver = new ObservableGroupJoin.LeftRightObserver(joinDisposable, false);
    joinDisposable.disposables.add(leftRightObserver);
    this.source.subscribe(paramObserver);
    this.other.subscribe(leftRightObserver);
  }
  
  static final class JoinDisposable<TLeft, TRight, TLeftEnd, TRightEnd, R> extends AtomicInteger implements Disposable, ObservableGroupJoin.JoinSupport {
    static final Integer LEFT_CLOSE = Integer.valueOf(3);
    
    static final Integer LEFT_VALUE = Integer.valueOf(1);
    
    static final Integer RIGHT_CLOSE = Integer.valueOf(4);
    
    static final Integer RIGHT_VALUE = Integer.valueOf(2);
    
    private static final long serialVersionUID = -6071216598687999801L;
    
    final AtomicInteger active;
    
    volatile boolean cancelled;
    
    final CompositeDisposable disposables;
    
    final Observer<? super R> downstream;
    
    final AtomicReference<Throwable> error;
    
    final Function<? super TLeft, ? extends ObservableSource<TLeftEnd>> leftEnd;
    
    int leftIndex;
    
    final Map<Integer, TLeft> lefts;
    
    final SpscLinkedArrayQueue<Object> queue;
    
    final BiFunction<? super TLeft, ? super TRight, ? extends R> resultSelector;
    
    final Function<? super TRight, ? extends ObservableSource<TRightEnd>> rightEnd;
    
    int rightIndex;
    
    final Map<Integer, TRight> rights;
    
    static {
    
    }
    
    JoinDisposable(Observer<? super R> param1Observer, Function<? super TLeft, ? extends ObservableSource<TLeftEnd>> param1Function, Function<? super TRight, ? extends ObservableSource<TRightEnd>> param1Function1, BiFunction<? super TLeft, ? super TRight, ? extends R> param1BiFunction) {
      this.downstream = param1Observer;
      this.disposables = new CompositeDisposable();
      this.queue = new SpscLinkedArrayQueue(Observable.bufferSize());
      this.lefts = new LinkedHashMap<Integer, TLeft>();
      this.rights = new LinkedHashMap<Integer, TRight>();
      this.error = new AtomicReference<Throwable>();
      this.leftEnd = param1Function;
      this.rightEnd = param1Function1;
      this.resultSelector = param1BiFunction;
      this.active = new AtomicInteger(2);
    }
    
    void cancelAll() {
      this.disposables.dispose();
    }
    
    public void dispose() {
      if (!this.cancelled) {
        this.cancelled = true;
        cancelAll();
        if (getAndIncrement() == 0)
          this.queue.clear(); 
      } 
    }
    
    void drain() {
      if (getAndIncrement() != 0)
        return; 
      SpscLinkedArrayQueue<Object> spscLinkedArrayQueue = this.queue;
      Observer<? super R> observer = this.downstream;
      int i = 1;
      while (true) {
        int j;
        boolean bool;
        if (this.cancelled) {
          spscLinkedArrayQueue.clear();
          return;
        } 
        if ((Throwable)this.error.get() != null) {
          spscLinkedArrayQueue.clear();
          cancelAll();
          errorAll(observer);
          return;
        } 
        if (this.active.get() == 0) {
          j = 1;
        } else {
          j = 0;
        } 
        Integer integer = (Integer)spscLinkedArrayQueue.poll();
        if (integer == null) {
          bool = true;
        } else {
          bool = false;
        } 
        if (j && bool) {
          this.lefts.clear();
          this.rights.clear();
          this.disposables.dispose();
          observer.onComplete();
          return;
        } 
        if (bool) {
          j = addAndGet(-i);
          i = j;
          if (j == 0)
            return; 
          continue;
        } 
        Object object = spscLinkedArrayQueue.poll();
        if (integer == LEFT_VALUE) {
          j = this.leftIndex;
          this.leftIndex = j + 1;
          this.lefts.put(Integer.valueOf(j), (TLeft)object);
          try {
            ObservableSource observableSource = (ObservableSource)ObjectHelper.requireNonNull(this.leftEnd.apply(object), "The leftEnd returned a null ObservableSource");
            leftRightEndObserver = new ObservableGroupJoin.LeftRightEndObserver(this, true, j);
            this.disposables.add(leftRightEndObserver);
            observableSource.subscribe(leftRightEndObserver);
          } finally {
            object = null;
            fail((Throwable)object, observer, spscLinkedArrayQueue);
          } 
          continue;
        } 
        if (integer == RIGHT_VALUE) {
          j = this.rightIndex;
          this.rightIndex = j + 1;
          this.rights.put(Integer.valueOf(j), (TRight)object);
          try {
            ObservableSource observableSource = (ObservableSource)ObjectHelper.requireNonNull(this.rightEnd.apply(object), "The rightEnd returned a null ObservableSource");
            leftRightEndObserver = new ObservableGroupJoin.LeftRightEndObserver(this, false, j);
            this.disposables.add(leftRightEndObserver);
            observableSource.subscribe(leftRightEndObserver);
          } finally {
            object = null;
            fail((Throwable)object, observer, spscLinkedArrayQueue);
          } 
          continue;
        } 
        if (integer == LEFT_CLOSE) {
          object = object;
          this.lefts.remove(Integer.valueOf(((ObservableGroupJoin.LeftRightEndObserver)object).index));
          this.disposables.remove((Disposable)object);
          continue;
        } 
        object = object;
        this.rights.remove(Integer.valueOf(((ObservableGroupJoin.LeftRightEndObserver)object).index));
        this.disposables.remove((Disposable)object);
      } 
    }
    
    void errorAll(Observer<?> param1Observer) {
      Throwable throwable = ExceptionHelper.terminate(this.error);
      this.lefts.clear();
      this.rights.clear();
      param1Observer.onError(throwable);
    }
    
    void fail(Throwable param1Throwable, Observer<?> param1Observer, SpscLinkedArrayQueue<?> param1SpscLinkedArrayQueue) {
      Exceptions.throwIfFatal(param1Throwable);
      ExceptionHelper.addThrowable(this.error, param1Throwable);
      param1SpscLinkedArrayQueue.clear();
      cancelAll();
      errorAll(param1Observer);
    }
    
    public void innerClose(boolean param1Boolean, ObservableGroupJoin.LeftRightEndObserver param1LeftRightEndObserver) {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: getfield queue : Lio/reactivex/internal/queue/SpscLinkedArrayQueue;
      //   6: astore_3
      //   7: iload_1
      //   8: ifeq -> 19
      //   11: getstatic io/reactivex/internal/operators/observable/ObservableJoin$JoinDisposable.LEFT_CLOSE : Ljava/lang/Integer;
      //   14: astore #4
      //   16: goto -> 24
      //   19: getstatic io/reactivex/internal/operators/observable/ObservableJoin$JoinDisposable.RIGHT_CLOSE : Ljava/lang/Integer;
      //   22: astore #4
      //   24: aload_3
      //   25: aload #4
      //   27: aload_2
      //   28: invokevirtual offer : (Ljava/lang/Object;Ljava/lang/Object;)Z
      //   31: pop
      //   32: aload_0
      //   33: monitorexit
      //   34: aload_0
      //   35: invokevirtual drain : ()V
      //   38: return
      //   39: astore_2
      //   40: aload_0
      //   41: monitorexit
      //   42: aload_2
      //   43: athrow
      // Exception table:
      //   from	to	target	type
      //   2	7	39	finally
      //   11	16	39	finally
      //   19	24	39	finally
      //   24	34	39	finally
      //   40	42	39	finally
    }
    
    public void innerCloseError(Throwable param1Throwable) {
      if (ExceptionHelper.addThrowable(this.error, param1Throwable)) {
        drain();
      } else {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
    
    public void innerComplete(ObservableGroupJoin.LeftRightObserver param1LeftRightObserver) {
      this.disposables.delete(param1LeftRightObserver);
      this.active.decrementAndGet();
      drain();
    }
    
    public void innerError(Throwable param1Throwable) {
      if (ExceptionHelper.addThrowable(this.error, param1Throwable)) {
        this.active.decrementAndGet();
        drain();
      } else {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
    
    public void innerValue(boolean param1Boolean, Object param1Object) {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: getfield queue : Lio/reactivex/internal/queue/SpscLinkedArrayQueue;
      //   6: astore_3
      //   7: iload_1
      //   8: ifeq -> 19
      //   11: getstatic io/reactivex/internal/operators/observable/ObservableJoin$JoinDisposable.LEFT_VALUE : Ljava/lang/Integer;
      //   14: astore #4
      //   16: goto -> 24
      //   19: getstatic io/reactivex/internal/operators/observable/ObservableJoin$JoinDisposable.RIGHT_VALUE : Ljava/lang/Integer;
      //   22: astore #4
      //   24: aload_3
      //   25: aload #4
      //   27: aload_2
      //   28: invokevirtual offer : (Ljava/lang/Object;Ljava/lang/Object;)Z
      //   31: pop
      //   32: aload_0
      //   33: monitorexit
      //   34: aload_0
      //   35: invokevirtual drain : ()V
      //   38: return
      //   39: astore_2
      //   40: aload_0
      //   41: monitorexit
      //   42: aload_2
      //   43: athrow
      // Exception table:
      //   from	to	target	type
      //   2	7	39	finally
      //   11	16	39	finally
      //   19	24	39	finally
      //   24	34	39	finally
      //   40	42	39	finally
    }
    
    public boolean isDisposed() {
      return this.cancelled;
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableJoin.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */