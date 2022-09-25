package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.disposables.ResettableConnectable;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public final class ObservableRefCount<T> extends Observable<T> {
  RefConnection connection;
  
  final int n;
  
  final Scheduler scheduler;
  
  final ConnectableObservable<T> source;
  
  final long timeout;
  
  final TimeUnit unit;
  
  public ObservableRefCount(ConnectableObservable<T> paramConnectableObservable) {
    this(paramConnectableObservable, 1, 0L, TimeUnit.NANOSECONDS, null);
  }
  
  public ObservableRefCount(ConnectableObservable<T> paramConnectableObservable, int paramInt, long paramLong, TimeUnit paramTimeUnit, Scheduler paramScheduler) {
    this.source = paramConnectableObservable;
    this.n = paramInt;
    this.timeout = paramLong;
    this.unit = paramTimeUnit;
    this.scheduler = paramScheduler;
  }
  
  void cancel(RefConnection paramRefConnection) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield connection : Lio/reactivex/internal/operators/observable/ObservableRefCount$RefConnection;
    //   6: ifnull -> 109
    //   9: aload_0
    //   10: getfield connection : Lio/reactivex/internal/operators/observable/ObservableRefCount$RefConnection;
    //   13: aload_1
    //   14: if_acmpeq -> 20
    //   17: goto -> 109
    //   20: aload_1
    //   21: getfield subscriberCount : J
    //   24: lconst_1
    //   25: lsub
    //   26: lstore_2
    //   27: aload_1
    //   28: lload_2
    //   29: putfield subscriberCount : J
    //   32: lload_2
    //   33: lconst_0
    //   34: lcmp
    //   35: ifne -> 106
    //   38: aload_1
    //   39: getfield connected : Z
    //   42: ifne -> 48
    //   45: goto -> 106
    //   48: aload_0
    //   49: getfield timeout : J
    //   52: lconst_0
    //   53: lcmp
    //   54: ifne -> 65
    //   57: aload_0
    //   58: aload_1
    //   59: invokevirtual timeout : (Lio/reactivex/internal/operators/observable/ObservableRefCount$RefConnection;)V
    //   62: aload_0
    //   63: monitorexit
    //   64: return
    //   65: new io/reactivex/internal/disposables/SequentialDisposable
    //   68: astore #4
    //   70: aload #4
    //   72: invokespecial <init> : ()V
    //   75: aload_1
    //   76: aload #4
    //   78: putfield timer : Lio/reactivex/disposables/Disposable;
    //   81: aload_0
    //   82: monitorexit
    //   83: aload #4
    //   85: aload_0
    //   86: getfield scheduler : Lio/reactivex/Scheduler;
    //   89: aload_1
    //   90: aload_0
    //   91: getfield timeout : J
    //   94: aload_0
    //   95: getfield unit : Ljava/util/concurrent/TimeUnit;
    //   98: invokevirtual scheduleDirect : (Ljava/lang/Runnable;JLjava/util/concurrent/TimeUnit;)Lio/reactivex/disposables/Disposable;
    //   101: invokevirtual replace : (Lio/reactivex/disposables/Disposable;)Z
    //   104: pop
    //   105: return
    //   106: aload_0
    //   107: monitorexit
    //   108: return
    //   109: aload_0
    //   110: monitorexit
    //   111: return
    //   112: astore_1
    //   113: aload_0
    //   114: monitorexit
    //   115: aload_1
    //   116: athrow
    // Exception table:
    //   from	to	target	type
    //   2	17	112	finally
    //   20	32	112	finally
    //   38	45	112	finally
    //   48	64	112	finally
    //   65	83	112	finally
    //   106	108	112	finally
    //   109	111	112	finally
    //   113	115	112	finally
  }
  
  void clearTimer(RefConnection paramRefConnection) {
    if (paramRefConnection.timer != null) {
      paramRefConnection.timer.dispose();
      paramRefConnection.timer = null;
    } 
  }
  
  void reset(RefConnection paramRefConnection) {
    ConnectableObservable<T> connectableObservable = this.source;
    if (connectableObservable instanceof Disposable) {
      ((Disposable)connectableObservable).dispose();
    } else if (connectableObservable instanceof ResettableConnectable) {
      ((ResettableConnectable)connectableObservable).resetIf(paramRefConnection.get());
    } 
  }
  
  protected void subscribeActual(Observer<? super T> paramObserver) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield connection : Lio/reactivex/internal/operators/observable/ObservableRefCount$RefConnection;
    //   6: astore_2
    //   7: aload_2
    //   8: astore_3
    //   9: aload_2
    //   10: ifnonnull -> 27
    //   13: new io/reactivex/internal/operators/observable/ObservableRefCount$RefConnection
    //   16: astore_3
    //   17: aload_3
    //   18: aload_0
    //   19: invokespecial <init> : (Lio/reactivex/internal/operators/observable/ObservableRefCount;)V
    //   22: aload_0
    //   23: aload_3
    //   24: putfield connection : Lio/reactivex/internal/operators/observable/ObservableRefCount$RefConnection;
    //   27: aload_3
    //   28: getfield subscriberCount : J
    //   31: lstore #4
    //   33: lload #4
    //   35: lconst_0
    //   36: lcmp
    //   37: ifne -> 56
    //   40: aload_3
    //   41: getfield timer : Lio/reactivex/disposables/Disposable;
    //   44: ifnull -> 56
    //   47: aload_3
    //   48: getfield timer : Lio/reactivex/disposables/Disposable;
    //   51: invokeinterface dispose : ()V
    //   56: lload #4
    //   58: lconst_1
    //   59: ladd
    //   60: lstore #4
    //   62: aload_3
    //   63: lload #4
    //   65: putfield subscriberCount : J
    //   68: aload_3
    //   69: getfield connected : Z
    //   72: istore #6
    //   74: iconst_1
    //   75: istore #7
    //   77: iload #6
    //   79: ifne -> 101
    //   82: lload #4
    //   84: aload_0
    //   85: getfield n : I
    //   88: i2l
    //   89: lcmp
    //   90: ifne -> 101
    //   93: aload_3
    //   94: iconst_1
    //   95: putfield connected : Z
    //   98: goto -> 104
    //   101: iconst_0
    //   102: istore #7
    //   104: aload_0
    //   105: monitorexit
    //   106: aload_0
    //   107: getfield source : Lio/reactivex/observables/ConnectableObservable;
    //   110: new io/reactivex/internal/operators/observable/ObservableRefCount$RefCountObserver
    //   113: dup
    //   114: aload_1
    //   115: aload_0
    //   116: aload_3
    //   117: invokespecial <init> : (Lio/reactivex/Observer;Lio/reactivex/internal/operators/observable/ObservableRefCount;Lio/reactivex/internal/operators/observable/ObservableRefCount$RefConnection;)V
    //   120: invokevirtual subscribe : (Lio/reactivex/Observer;)V
    //   123: iload #7
    //   125: ifeq -> 136
    //   128: aload_0
    //   129: getfield source : Lio/reactivex/observables/ConnectableObservable;
    //   132: aload_3
    //   133: invokevirtual connect : (Lio/reactivex/functions/Consumer;)V
    //   136: return
    //   137: astore_1
    //   138: aload_0
    //   139: monitorexit
    //   140: aload_1
    //   141: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	137	finally
    //   13	27	137	finally
    //   27	33	137	finally
    //   40	56	137	finally
    //   62	74	137	finally
    //   82	98	137	finally
    //   104	106	137	finally
    //   138	140	137	finally
  }
  
  void terminated(RefConnection paramRefConnection) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield source : Lio/reactivex/observables/ConnectableObservable;
    //   6: instanceof io/reactivex/internal/operators/observable/ObservablePublishClassic
    //   9: ifeq -> 63
    //   12: aload_0
    //   13: getfield connection : Lio/reactivex/internal/operators/observable/ObservableRefCount$RefConnection;
    //   16: ifnull -> 37
    //   19: aload_0
    //   20: getfield connection : Lio/reactivex/internal/operators/observable/ObservableRefCount$RefConnection;
    //   23: aload_1
    //   24: if_acmpne -> 37
    //   27: aload_0
    //   28: aconst_null
    //   29: putfield connection : Lio/reactivex/internal/operators/observable/ObservableRefCount$RefConnection;
    //   32: aload_0
    //   33: aload_1
    //   34: invokevirtual clearTimer : (Lio/reactivex/internal/operators/observable/ObservableRefCount$RefConnection;)V
    //   37: aload_1
    //   38: getfield subscriberCount : J
    //   41: lconst_1
    //   42: lsub
    //   43: lstore_2
    //   44: aload_1
    //   45: lload_2
    //   46: putfield subscriberCount : J
    //   49: lload_2
    //   50: lconst_0
    //   51: lcmp
    //   52: ifne -> 111
    //   55: aload_0
    //   56: aload_1
    //   57: invokevirtual reset : (Lio/reactivex/internal/operators/observable/ObservableRefCount$RefConnection;)V
    //   60: goto -> 111
    //   63: aload_0
    //   64: getfield connection : Lio/reactivex/internal/operators/observable/ObservableRefCount$RefConnection;
    //   67: ifnull -> 111
    //   70: aload_0
    //   71: getfield connection : Lio/reactivex/internal/operators/observable/ObservableRefCount$RefConnection;
    //   74: aload_1
    //   75: if_acmpne -> 111
    //   78: aload_0
    //   79: aload_1
    //   80: invokevirtual clearTimer : (Lio/reactivex/internal/operators/observable/ObservableRefCount$RefConnection;)V
    //   83: aload_1
    //   84: getfield subscriberCount : J
    //   87: lconst_1
    //   88: lsub
    //   89: lstore_2
    //   90: aload_1
    //   91: lload_2
    //   92: putfield subscriberCount : J
    //   95: lload_2
    //   96: lconst_0
    //   97: lcmp
    //   98: ifne -> 111
    //   101: aload_0
    //   102: aconst_null
    //   103: putfield connection : Lio/reactivex/internal/operators/observable/ObservableRefCount$RefConnection;
    //   106: aload_0
    //   107: aload_1
    //   108: invokevirtual reset : (Lio/reactivex/internal/operators/observable/ObservableRefCount$RefConnection;)V
    //   111: aload_0
    //   112: monitorexit
    //   113: return
    //   114: astore_1
    //   115: aload_0
    //   116: monitorexit
    //   117: aload_1
    //   118: athrow
    // Exception table:
    //   from	to	target	type
    //   2	37	114	finally
    //   37	49	114	finally
    //   55	60	114	finally
    //   63	95	114	finally
    //   101	111	114	finally
    //   111	113	114	finally
    //   115	117	114	finally
  }
  
  void timeout(RefConnection paramRefConnection) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_1
    //   3: getfield subscriberCount : J
    //   6: lconst_0
    //   7: lcmp
    //   8: ifne -> 97
    //   11: aload_1
    //   12: aload_0
    //   13: getfield connection : Lio/reactivex/internal/operators/observable/ObservableRefCount$RefConnection;
    //   16: if_acmpne -> 97
    //   19: aload_0
    //   20: aconst_null
    //   21: putfield connection : Lio/reactivex/internal/operators/observable/ObservableRefCount$RefConnection;
    //   24: aload_1
    //   25: invokevirtual get : ()Ljava/lang/Object;
    //   28: checkcast io/reactivex/disposables/Disposable
    //   31: astore_2
    //   32: aload_1
    //   33: invokestatic dispose : (Ljava/util/concurrent/atomic/AtomicReference;)Z
    //   36: pop
    //   37: aload_0
    //   38: getfield source : Lio/reactivex/observables/ConnectableObservable;
    //   41: instanceof io/reactivex/disposables/Disposable
    //   44: ifeq -> 62
    //   47: aload_0
    //   48: getfield source : Lio/reactivex/observables/ConnectableObservable;
    //   51: checkcast io/reactivex/disposables/Disposable
    //   54: invokeinterface dispose : ()V
    //   59: goto -> 97
    //   62: aload_0
    //   63: getfield source : Lio/reactivex/observables/ConnectableObservable;
    //   66: instanceof io/reactivex/internal/disposables/ResettableConnectable
    //   69: ifeq -> 97
    //   72: aload_2
    //   73: ifnonnull -> 84
    //   76: aload_1
    //   77: iconst_1
    //   78: putfield disconnectedEarly : Z
    //   81: goto -> 97
    //   84: aload_0
    //   85: getfield source : Lio/reactivex/observables/ConnectableObservable;
    //   88: checkcast io/reactivex/internal/disposables/ResettableConnectable
    //   91: aload_2
    //   92: invokeinterface resetIf : (Lio/reactivex/disposables/Disposable;)V
    //   97: aload_0
    //   98: monitorexit
    //   99: return
    //   100: astore_1
    //   101: aload_0
    //   102: monitorexit
    //   103: aload_1
    //   104: athrow
    // Exception table:
    //   from	to	target	type
    //   2	59	100	finally
    //   62	72	100	finally
    //   76	81	100	finally
    //   84	97	100	finally
    //   97	99	100	finally
    //   101	103	100	finally
  }
  
  static final class RefConnection extends AtomicReference<Disposable> implements Runnable, Consumer<Disposable> {
    private static final long serialVersionUID = -4552101107598366241L;
    
    boolean connected;
    
    boolean disconnectedEarly;
    
    final ObservableRefCount<?> parent;
    
    long subscriberCount;
    
    Disposable timer;
    
    RefConnection(ObservableRefCount<?> param1ObservableRefCount) {
      this.parent = param1ObservableRefCount;
    }
    
    public void accept(Disposable param1Disposable) throws Exception {
      DisposableHelper.replace(this, param1Disposable);
      synchronized (this.parent) {
        if (this.disconnectedEarly)
          ((ResettableConnectable)this.parent.source).resetIf(param1Disposable); 
        return;
      } 
    }
    
    public void run() {
      this.parent.timeout(this);
    }
  }
  
  static final class RefCountObserver<T> extends AtomicBoolean implements Observer<T>, Disposable {
    private static final long serialVersionUID = -7419642935409022375L;
    
    final ObservableRefCount.RefConnection connection;
    
    final Observer<? super T> downstream;
    
    final ObservableRefCount<T> parent;
    
    Disposable upstream;
    
    RefCountObserver(Observer<? super T> param1Observer, ObservableRefCount<T> param1ObservableRefCount, ObservableRefCount.RefConnection param1RefConnection) {
      this.downstream = param1Observer;
      this.parent = param1ObservableRefCount;
      this.connection = param1RefConnection;
    }
    
    public void dispose() {
      this.upstream.dispose();
      if (compareAndSet(false, true))
        this.parent.cancel(this.connection); 
    }
    
    public boolean isDisposed() {
      return this.upstream.isDisposed();
    }
    
    public void onComplete() {
      if (compareAndSet(false, true)) {
        this.parent.terminated(this.connection);
        this.downstream.onComplete();
      } 
    }
    
    public void onError(Throwable param1Throwable) {
      if (compareAndSet(false, true)) {
        this.parent.terminated(this.connection);
        this.downstream.onError(param1Throwable);
      } else {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
    
    public void onNext(T param1T) {
      this.downstream.onNext(param1T);
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      if (DisposableHelper.validate(this.upstream, param1Disposable)) {
        this.upstream = param1Disposable;
        this.downstream.onSubscribe(this);
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableRefCount.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */