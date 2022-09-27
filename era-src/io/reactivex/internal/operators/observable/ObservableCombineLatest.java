package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.internal.queue.SpscLinkedArrayQueue;
import io.reactivex.internal.util.AtomicThrowable;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public final class ObservableCombineLatest<T, R> extends Observable<R> {
  final int bufferSize;
  
  final Function<? super Object[], ? extends R> combiner;
  
  final boolean delayError;
  
  final ObservableSource<? extends T>[] sources;
  
  final Iterable<? extends ObservableSource<? extends T>> sourcesIterable;
  
  public ObservableCombineLatest(ObservableSource<? extends T>[] paramArrayOfObservableSource, Iterable<? extends ObservableSource<? extends T>> paramIterable, Function<? super Object[], ? extends R> paramFunction, int paramInt, boolean paramBoolean) {
    this.sources = paramArrayOfObservableSource;
    this.sourcesIterable = paramIterable;
    this.combiner = paramFunction;
    this.bufferSize = paramInt;
    this.delayError = paramBoolean;
  }
  
  public void subscribeActual(Observer<? super R> paramObserver) {
    ObservableSource[] arrayOfObservableSource;
    int i;
    ObservableSource<? extends T>[] arrayOfObservableSource1 = this.sources;
    if (arrayOfObservableSource1 == null) {
      Observable[] arrayOfObservable = new Observable[8];
      Iterator<? extends ObservableSource<? extends T>> iterator = this.sourcesIterable.iterator();
      byte b = 0;
      while (true) {
        Observable[] arrayOfObservable1 = arrayOfObservable;
        i = b;
        if (iterator.hasNext()) {
          ObservableSource observableSource = iterator.next();
          arrayOfObservable1 = arrayOfObservable;
          if (b == arrayOfObservable.length) {
            arrayOfObservableSource = new ObservableSource[(b >> 2) + b];
            System.arraycopy(arrayOfObservable, 0, arrayOfObservableSource, 0, b);
          } 
          arrayOfObservableSource[b] = observableSource;
          b++;
          ObservableSource[] arrayOfObservableSource2 = arrayOfObservableSource;
          continue;
        } 
        break;
      } 
    } else {
      i = arrayOfObservableSource.length;
    } 
    if (i == 0) {
      EmptyDisposable.complete(paramObserver);
      return;
    } 
    (new LatestCoordinator<Object, Object>(paramObserver, this.combiner, i, this.bufferSize, this.delayError)).subscribe((ObservableSource<?>[])arrayOfObservableSource);
  }
  
  static final class CombinerObserver<T, R> extends AtomicReference<Disposable> implements Observer<T> {
    private static final long serialVersionUID = -4823716997131257941L;
    
    final int index;
    
    final ObservableCombineLatest.LatestCoordinator<T, R> parent;
    
    CombinerObserver(ObservableCombineLatest.LatestCoordinator<T, R> param1LatestCoordinator, int param1Int) {
      this.parent = param1LatestCoordinator;
      this.index = param1Int;
    }
    
    public void dispose() {
      DisposableHelper.dispose(this);
    }
    
    public void onComplete() {
      this.parent.innerComplete(this.index);
    }
    
    public void onError(Throwable param1Throwable) {
      this.parent.innerError(this.index, param1Throwable);
    }
    
    public void onNext(T param1T) {
      this.parent.innerNext(this.index, param1T);
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      DisposableHelper.setOnce(this, param1Disposable);
    }
  }
  
  static final class LatestCoordinator<T, R> extends AtomicInteger implements Disposable {
    private static final long serialVersionUID = 8567835998786448817L;
    
    int active;
    
    volatile boolean cancelled;
    
    final Function<? super Object[], ? extends R> combiner;
    
    int complete;
    
    final boolean delayError;
    
    volatile boolean done;
    
    final Observer<? super R> downstream;
    
    final AtomicThrowable errors = new AtomicThrowable();
    
    Object[] latest;
    
    final ObservableCombineLatest.CombinerObserver<T, R>[] observers;
    
    final SpscLinkedArrayQueue<Object[]> queue;
    
    LatestCoordinator(Observer<? super R> param1Observer, Function<? super Object[], ? extends R> param1Function, int param1Int1, int param1Int2, boolean param1Boolean) {
      this.downstream = param1Observer;
      this.combiner = param1Function;
      this.delayError = param1Boolean;
      this.latest = new Object[param1Int1];
      ObservableCombineLatest.CombinerObserver[] arrayOfCombinerObserver = new ObservableCombineLatest.CombinerObserver[param1Int1];
      for (byte b = 0; b < param1Int1; b++)
        arrayOfCombinerObserver[b] = new ObservableCombineLatest.CombinerObserver<Object, Object>(this, b); 
      this.observers = (ObservableCombineLatest.CombinerObserver<T, R>[])arrayOfCombinerObserver;
      this.queue = new SpscLinkedArrayQueue(param1Int2);
    }
    
    void cancelSources() {
      ObservableCombineLatest.CombinerObserver<T, R>[] arrayOfCombinerObserver = this.observers;
      int i = arrayOfCombinerObserver.length;
      for (byte b = 0; b < i; b++)
        arrayOfCombinerObserver[b].dispose(); 
    }
    
    void clear(SpscLinkedArrayQueue<?> param1SpscLinkedArrayQueue) {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: aconst_null
      //   4: putfield latest : [Ljava/lang/Object;
      //   7: aload_0
      //   8: monitorexit
      //   9: aload_1
      //   10: invokevirtual clear : ()V
      //   13: return
      //   14: astore_1
      //   15: aload_0
      //   16: monitorexit
      //   17: aload_1
      //   18: athrow
      // Exception table:
      //   from	to	target	type
      //   2	9	14	finally
      //   15	17	14	finally
    }
    
    public void dispose() {
      if (!this.cancelled) {
        this.cancelled = true;
        cancelSources();
        if (getAndIncrement() == 0)
          clear(this.queue); 
      } 
    }
    
    void drain() {
      if (getAndIncrement() != 0)
        return; 
      SpscLinkedArrayQueue<Object[]> spscLinkedArrayQueue = this.queue;
      Observer<? super R> observer = this.downstream;
      boolean bool = this.delayError;
      int i = 1;
      while (true) {
        Throwable throwable;
        int j;
        if (this.cancelled) {
          clear(spscLinkedArrayQueue);
          return;
        } 
        if (!bool && this.errors.get() != null) {
          cancelSources();
          clear(spscLinkedArrayQueue);
          observer.onError(this.errors.terminate());
          return;
        } 
        boolean bool1 = this.done;
        Object[] arrayOfObject = (Object[])spscLinkedArrayQueue.poll();
        if (arrayOfObject == null) {
          j = 1;
        } else {
          j = 0;
        } 
        if (bool1 && j) {
          clear(spscLinkedArrayQueue);
          throwable = this.errors.terminate();
          if (throwable == null) {
            observer.onComplete();
          } else {
            observer.onError(throwable);
          } 
          return;
        } 
        if (j) {
          j = addAndGet(-i);
          i = j;
          if (j == 0)
            return; 
          continue;
        } 
        try {
        
        } finally {
          arrayOfObject = null;
          Exceptions.throwIfFatal((Throwable)arrayOfObject);
          this.errors.addThrowable((Throwable)arrayOfObject);
          cancelSources();
          clear((SpscLinkedArrayQueue<?>)throwable);
          observer.onError(this.errors.terminate());
        } 
      } 
    }
    
    void innerComplete(int param1Int) {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: getfield latest : [Ljava/lang/Object;
      //   6: astore_2
      //   7: aload_2
      //   8: ifnonnull -> 14
      //   11: aload_0
      //   12: monitorexit
      //   13: return
      //   14: aload_2
      //   15: iload_1
      //   16: aaload
      //   17: ifnonnull -> 25
      //   20: iconst_1
      //   21: istore_1
      //   22: goto -> 27
      //   25: iconst_0
      //   26: istore_1
      //   27: iload_1
      //   28: ifne -> 49
      //   31: aload_0
      //   32: getfield complete : I
      //   35: iconst_1
      //   36: iadd
      //   37: istore_3
      //   38: aload_0
      //   39: iload_3
      //   40: putfield complete : I
      //   43: iload_3
      //   44: aload_2
      //   45: arraylength
      //   46: if_icmpne -> 54
      //   49: aload_0
      //   50: iconst_1
      //   51: putfield done : Z
      //   54: aload_0
      //   55: monitorexit
      //   56: iload_1
      //   57: ifeq -> 64
      //   60: aload_0
      //   61: invokevirtual cancelSources : ()V
      //   64: aload_0
      //   65: invokevirtual drain : ()V
      //   68: return
      //   69: astore_2
      //   70: aload_0
      //   71: monitorexit
      //   72: aload_2
      //   73: athrow
      // Exception table:
      //   from	to	target	type
      //   2	7	69	finally
      //   11	13	69	finally
      //   31	49	69	finally
      //   49	54	69	finally
      //   54	56	69	finally
      //   70	72	69	finally
    }
    
    void innerError(int param1Int, Throwable param1Throwable) {
      // Byte code:
      //   0: aload_0
      //   1: getfield errors : Lio/reactivex/internal/util/AtomicThrowable;
      //   4: aload_2
      //   5: invokevirtual addThrowable : (Ljava/lang/Throwable;)Z
      //   8: ifeq -> 99
      //   11: aload_0
      //   12: getfield delayError : Z
      //   15: ifeq -> 82
      //   18: aload_0
      //   19: monitorenter
      //   20: aload_0
      //   21: getfield latest : [Ljava/lang/Object;
      //   24: astore_2
      //   25: aload_2
      //   26: ifnonnull -> 32
      //   29: aload_0
      //   30: monitorexit
      //   31: return
      //   32: aload_2
      //   33: iload_1
      //   34: aaload
      //   35: ifnonnull -> 43
      //   38: iconst_1
      //   39: istore_1
      //   40: goto -> 45
      //   43: iconst_0
      //   44: istore_1
      //   45: iload_1
      //   46: ifne -> 67
      //   49: aload_0
      //   50: getfield complete : I
      //   53: iconst_1
      //   54: iadd
      //   55: istore_3
      //   56: aload_0
      //   57: iload_3
      //   58: putfield complete : I
      //   61: iload_3
      //   62: aload_2
      //   63: arraylength
      //   64: if_icmpne -> 72
      //   67: aload_0
      //   68: iconst_1
      //   69: putfield done : Z
      //   72: aload_0
      //   73: monitorexit
      //   74: goto -> 84
      //   77: astore_2
      //   78: aload_0
      //   79: monitorexit
      //   80: aload_2
      //   81: athrow
      //   82: iconst_1
      //   83: istore_1
      //   84: iload_1
      //   85: ifeq -> 92
      //   88: aload_0
      //   89: invokevirtual cancelSources : ()V
      //   92: aload_0
      //   93: invokevirtual drain : ()V
      //   96: goto -> 103
      //   99: aload_2
      //   100: invokestatic onError : (Ljava/lang/Throwable;)V
      //   103: return
      // Exception table:
      //   from	to	target	type
      //   20	25	77	finally
      //   29	31	77	finally
      //   49	67	77	finally
      //   67	72	77	finally
      //   72	74	77	finally
      //   78	80	77	finally
    }
    
    void innerNext(int param1Int, T param1T) {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: getfield latest : [Ljava/lang/Object;
      //   6: astore_3
      //   7: aload_3
      //   8: ifnonnull -> 14
      //   11: aload_0
      //   12: monitorexit
      //   13: return
      //   14: aload_3
      //   15: iload_1
      //   16: aaload
      //   17: astore #4
      //   19: aload_0
      //   20: getfield active : I
      //   23: istore #5
      //   25: iload #5
      //   27: istore #6
      //   29: aload #4
      //   31: ifnonnull -> 46
      //   34: iload #5
      //   36: iconst_1
      //   37: iadd
      //   38: istore #6
      //   40: aload_0
      //   41: iload #6
      //   43: putfield active : I
      //   46: aload_3
      //   47: iload_1
      //   48: aload_2
      //   49: aastore
      //   50: iload #6
      //   52: aload_3
      //   53: arraylength
      //   54: if_icmpne -> 74
      //   57: aload_0
      //   58: getfield queue : Lio/reactivex/internal/queue/SpscLinkedArrayQueue;
      //   61: aload_3
      //   62: invokevirtual clone : ()Ljava/lang/Object;
      //   65: invokevirtual offer : (Ljava/lang/Object;)Z
      //   68: pop
      //   69: iconst_1
      //   70: istore_1
      //   71: goto -> 76
      //   74: iconst_0
      //   75: istore_1
      //   76: aload_0
      //   77: monitorexit
      //   78: iload_1
      //   79: ifeq -> 86
      //   82: aload_0
      //   83: invokevirtual drain : ()V
      //   86: return
      //   87: astore_2
      //   88: aload_0
      //   89: monitorexit
      //   90: aload_2
      //   91: athrow
      // Exception table:
      //   from	to	target	type
      //   2	7	87	finally
      //   11	13	87	finally
      //   19	25	87	finally
      //   40	46	87	finally
      //   50	69	87	finally
      //   76	78	87	finally
      //   88	90	87	finally
    }
    
    public boolean isDisposed() {
      return this.cancelled;
    }
    
    public void subscribe(ObservableSource<? extends T>[] param1ArrayOfObservableSource) {
      ObservableCombineLatest.CombinerObserver<T, R>[] arrayOfCombinerObserver = this.observers;
      int i = arrayOfCombinerObserver.length;
      this.downstream.onSubscribe(this);
      for (byte b = 0; b < i && !this.done && !this.cancelled; b++)
        param1ArrayOfObservableSource[b].subscribe(arrayOfCombinerObserver[b]); 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableCombineLatest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */