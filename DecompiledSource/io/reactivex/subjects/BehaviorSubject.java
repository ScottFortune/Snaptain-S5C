package io.reactivex.subjects;

import io.reactivex.Observer;
import io.reactivex.annotations.CheckReturnValue;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.util.AppendOnlyLinkedArrayList;
import io.reactivex.internal.util.ExceptionHelper;
import io.reactivex.internal.util.NotificationLite;
import io.reactivex.plugins.RxJavaPlugins;
import java.lang.reflect.Array;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public final class BehaviorSubject<T> extends Subject<T> {
  static final BehaviorDisposable[] EMPTY;
  
  private static final Object[] EMPTY_ARRAY = new Object[0];
  
  static final BehaviorDisposable[] TERMINATED;
  
  long index;
  
  final ReadWriteLock lock = new ReentrantReadWriteLock();
  
  final Lock readLock = this.lock.readLock();
  
  final AtomicReference<BehaviorDisposable<T>[]> subscribers = new AtomicReference(EMPTY);
  
  final AtomicReference<Throwable> terminalEvent = new AtomicReference<Throwable>();
  
  final AtomicReference<Object> value = new AtomicReference();
  
  final Lock writeLock = this.lock.writeLock();
  
  static {
    EMPTY = new BehaviorDisposable[0];
    TERMINATED = new BehaviorDisposable[0];
  }
  
  BehaviorSubject() {}
  
  BehaviorSubject(T paramT) {
    this();
    this.value.lazySet(ObjectHelper.requireNonNull(paramT, "defaultValue is null"));
  }
  
  @CheckReturnValue
  public static <T> BehaviorSubject<T> create() {
    return new BehaviorSubject<T>();
  }
  
  @CheckReturnValue
  public static <T> BehaviorSubject<T> createDefault(T paramT) {
    return new BehaviorSubject<T>(paramT);
  }
  
  boolean add(BehaviorDisposable<T> paramBehaviorDisposable) {
    while (true) {
      BehaviorDisposable[] arrayOfBehaviorDisposable1 = (BehaviorDisposable[])this.subscribers.get();
      if (arrayOfBehaviorDisposable1 == TERMINATED)
        return false; 
      int i = arrayOfBehaviorDisposable1.length;
      BehaviorDisposable[] arrayOfBehaviorDisposable2 = new BehaviorDisposable[i + 1];
      System.arraycopy(arrayOfBehaviorDisposable1, 0, arrayOfBehaviorDisposable2, 0, i);
      arrayOfBehaviorDisposable2[i] = paramBehaviorDisposable;
      if (this.subscribers.compareAndSet(arrayOfBehaviorDisposable1, arrayOfBehaviorDisposable2))
        return true; 
    } 
  }
  
  public Throwable getThrowable() {
    Object object = this.value.get();
    return NotificationLite.isError(object) ? NotificationLite.getError(object) : null;
  }
  
  public T getValue() {
    Object object = this.value.get();
    return (T)((NotificationLite.isComplete(object) || NotificationLite.isError(object)) ? null : NotificationLite.getValue(object));
  }
  
  @Deprecated
  public Object[] getValues() {
    T[] arrayOfT1 = getValues((T[])EMPTY_ARRAY);
    T[] arrayOfT2 = arrayOfT1;
    if (arrayOfT1 == EMPTY_ARRAY)
      arrayOfT2 = (T[])new Object[0]; 
    return (Object[])arrayOfT2;
  }
  
  @Deprecated
  public T[] getValues(T[] paramArrayOfT) {
    Object object1 = this.value.get();
    if (object1 == null || NotificationLite.isComplete(object1) || NotificationLite.isError(object1)) {
      if (paramArrayOfT.length != 0)
        paramArrayOfT[0] = null; 
      return paramArrayOfT;
    } 
    Object object2 = NotificationLite.getValue(object1);
    if (paramArrayOfT.length != 0) {
      paramArrayOfT[0] = (T)object2;
      object1 = paramArrayOfT;
      if (paramArrayOfT.length != 1) {
        paramArrayOfT[1] = null;
        object1 = paramArrayOfT;
      } 
    } else {
      object1 = Array.newInstance(paramArrayOfT.getClass().getComponentType(), 1);
      object1[0] = object2;
    } 
    return (T[])object1;
  }
  
  public boolean hasComplete() {
    return NotificationLite.isComplete(this.value.get());
  }
  
  public boolean hasObservers() {
    boolean bool;
    if (((BehaviorDisposable[])this.subscribers.get()).length != 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean hasThrowable() {
    return NotificationLite.isError(this.value.get());
  }
  
  public boolean hasValue() {
    boolean bool;
    Object object = this.value.get();
    if (object != null && !NotificationLite.isComplete(object) && !NotificationLite.isError(object)) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void onComplete() {
    if (!this.terminalEvent.compareAndSet(null, ExceptionHelper.TERMINATED))
      return; 
    Object object = NotificationLite.complete();
    BehaviorDisposable[] arrayOfBehaviorDisposable = (BehaviorDisposable[])terminate(object);
    int i = arrayOfBehaviorDisposable.length;
    for (byte b = 0; b < i; b++)
      arrayOfBehaviorDisposable[b].emitNext(object, this.index); 
  }
  
  public void onError(Throwable paramThrowable) {
    ObjectHelper.requireNonNull(paramThrowable, "onError called with null. Null values are generally not allowed in 2.x operators and sources.");
    if (!this.terminalEvent.compareAndSet(null, paramThrowable)) {
      RxJavaPlugins.onError(paramThrowable);
      return;
    } 
    Object object = NotificationLite.error(paramThrowable);
    BehaviorDisposable[] arrayOfBehaviorDisposable = (BehaviorDisposable[])terminate(object);
    int i = arrayOfBehaviorDisposable.length;
    for (byte b = 0; b < i; b++)
      arrayOfBehaviorDisposable[b].emitNext(object, this.index); 
  }
  
  public void onNext(T paramT) {
    ObjectHelper.requireNonNull(paramT, "onNext called with null. Null values are generally not allowed in 2.x operators and sources.");
    if (this.terminalEvent.get() != null)
      return; 
    paramT = (T)NotificationLite.next(paramT);
    setCurrent(paramT);
    BehaviorDisposable[] arrayOfBehaviorDisposable = (BehaviorDisposable[])this.subscribers.get();
    int i = arrayOfBehaviorDisposable.length;
    for (byte b = 0; b < i; b++)
      arrayOfBehaviorDisposable[b].emitNext(paramT, this.index); 
  }
  
  public void onSubscribe(Disposable paramDisposable) {
    if (this.terminalEvent.get() != null)
      paramDisposable.dispose(); 
  }
  
  void remove(BehaviorDisposable<T> paramBehaviorDisposable) {
    BehaviorDisposable[] arrayOfBehaviorDisposable1;
    BehaviorDisposable[] arrayOfBehaviorDisposable2;
    do {
      byte b2;
      arrayOfBehaviorDisposable1 = (BehaviorDisposable[])this.subscribers.get();
      int i = arrayOfBehaviorDisposable1.length;
      if (i == 0)
        return; 
      byte b1 = -1;
      byte b = 0;
      while (true) {
        b2 = b1;
        if (b < i) {
          if (arrayOfBehaviorDisposable1[b] == paramBehaviorDisposable) {
            b2 = b;
            break;
          } 
          b++;
          continue;
        } 
        break;
      } 
      if (b2 < 0)
        return; 
      if (i == 1) {
        arrayOfBehaviorDisposable2 = EMPTY;
      } else {
        arrayOfBehaviorDisposable2 = new BehaviorDisposable[i - 1];
        System.arraycopy(arrayOfBehaviorDisposable1, 0, arrayOfBehaviorDisposable2, 0, b2);
        System.arraycopy(arrayOfBehaviorDisposable1, b2 + 1, arrayOfBehaviorDisposable2, b2, i - b2 - 1);
      } 
    } while (!this.subscribers.compareAndSet(arrayOfBehaviorDisposable1, arrayOfBehaviorDisposable2));
  }
  
  void setCurrent(Object paramObject) {
    this.writeLock.lock();
    this.index++;
    this.value.lazySet(paramObject);
    this.writeLock.unlock();
  }
  
  protected void subscribeActual(Observer<? super T> paramObserver) {
    BehaviorDisposable<T> behaviorDisposable = new BehaviorDisposable<T>(paramObserver, this);
    paramObserver.onSubscribe(behaviorDisposable);
    if (add(behaviorDisposable)) {
      if (behaviorDisposable.cancelled) {
        remove(behaviorDisposable);
      } else {
        behaviorDisposable.emitFirst();
      } 
    } else {
      Throwable throwable = this.terminalEvent.get();
      if (throwable == ExceptionHelper.TERMINATED) {
        paramObserver.onComplete();
      } else {
        paramObserver.onError(throwable);
      } 
    } 
  }
  
  int subscriberCount() {
    return ((BehaviorDisposable[])this.subscribers.get()).length;
  }
  
  BehaviorDisposable<T>[] terminate(Object paramObject) {
    BehaviorDisposable[] arrayOfBehaviorDisposable = (BehaviorDisposable[])this.subscribers.getAndSet(TERMINATED);
    if (arrayOfBehaviorDisposable != TERMINATED)
      setCurrent(paramObject); 
    return (BehaviorDisposable<T>[])arrayOfBehaviorDisposable;
  }
  
  static final class BehaviorDisposable<T> implements Disposable, AppendOnlyLinkedArrayList.NonThrowingPredicate<Object> {
    volatile boolean cancelled;
    
    final Observer<? super T> downstream;
    
    boolean emitting;
    
    boolean fastPath;
    
    long index;
    
    boolean next;
    
    AppendOnlyLinkedArrayList<Object> queue;
    
    final BehaviorSubject<T> state;
    
    BehaviorDisposable(Observer<? super T> param1Observer, BehaviorSubject<T> param1BehaviorSubject) {
      this.downstream = param1Observer;
      this.state = param1BehaviorSubject;
    }
    
    public void dispose() {
      if (!this.cancelled) {
        this.cancelled = true;
        this.state.remove(this);
      } 
    }
    
    void emitFirst() {
      // Byte code:
      //   0: aload_0
      //   1: getfield cancelled : Z
      //   4: ifeq -> 8
      //   7: return
      //   8: aload_0
      //   9: monitorenter
      //   10: aload_0
      //   11: getfield cancelled : Z
      //   14: ifeq -> 20
      //   17: aload_0
      //   18: monitorexit
      //   19: return
      //   20: aload_0
      //   21: getfield next : Z
      //   24: ifeq -> 30
      //   27: aload_0
      //   28: monitorexit
      //   29: return
      //   30: aload_0
      //   31: getfield state : Lio/reactivex/subjects/BehaviorSubject;
      //   34: astore_1
      //   35: aload_1
      //   36: getfield readLock : Ljava/util/concurrent/locks/Lock;
      //   39: astore_2
      //   40: aload_2
      //   41: invokeinterface lock : ()V
      //   46: aload_0
      //   47: aload_1
      //   48: getfield index : J
      //   51: putfield index : J
      //   54: aload_1
      //   55: getfield value : Ljava/util/concurrent/atomic/AtomicReference;
      //   58: invokevirtual get : ()Ljava/lang/Object;
      //   61: astore_1
      //   62: aload_2
      //   63: invokeinterface unlock : ()V
      //   68: aload_1
      //   69: ifnull -> 77
      //   72: iconst_1
      //   73: istore_3
      //   74: goto -> 79
      //   77: iconst_0
      //   78: istore_3
      //   79: aload_0
      //   80: iload_3
      //   81: putfield emitting : Z
      //   84: aload_0
      //   85: iconst_1
      //   86: putfield next : Z
      //   89: aload_0
      //   90: monitorexit
      //   91: aload_1
      //   92: ifnull -> 108
      //   95: aload_0
      //   96: aload_1
      //   97: invokevirtual test : (Ljava/lang/Object;)Z
      //   100: ifeq -> 104
      //   103: return
      //   104: aload_0
      //   105: invokevirtual emitLoop : ()V
      //   108: return
      //   109: astore_2
      //   110: aload_0
      //   111: monitorexit
      //   112: aload_2
      //   113: athrow
      // Exception table:
      //   from	to	target	type
      //   10	19	109	finally
      //   20	29	109	finally
      //   30	68	109	finally
      //   79	91	109	finally
      //   110	112	109	finally
    }
    
    void emitLoop() {
      // Byte code:
      //   0: aload_0
      //   1: getfield cancelled : Z
      //   4: ifeq -> 8
      //   7: return
      //   8: aload_0
      //   9: monitorenter
      //   10: aload_0
      //   11: getfield queue : Lio/reactivex/internal/util/AppendOnlyLinkedArrayList;
      //   14: astore_1
      //   15: aload_1
      //   16: ifnonnull -> 27
      //   19: aload_0
      //   20: iconst_0
      //   21: putfield emitting : Z
      //   24: aload_0
      //   25: monitorexit
      //   26: return
      //   27: aload_0
      //   28: aconst_null
      //   29: putfield queue : Lio/reactivex/internal/util/AppendOnlyLinkedArrayList;
      //   32: aload_0
      //   33: monitorexit
      //   34: aload_1
      //   35: aload_0
      //   36: invokevirtual forEachWhile : (Lio/reactivex/internal/util/AppendOnlyLinkedArrayList$NonThrowingPredicate;)V
      //   39: goto -> 0
      //   42: astore_1
      //   43: aload_0
      //   44: monitorexit
      //   45: goto -> 50
      //   48: aload_1
      //   49: athrow
      //   50: goto -> 48
      // Exception table:
      //   from	to	target	type
      //   10	15	42	finally
      //   19	26	42	finally
      //   27	34	42	finally
      //   43	45	42	finally
    }
    
    void emitNext(Object param1Object, long param1Long) {
      // Byte code:
      //   0: aload_0
      //   1: getfield cancelled : Z
      //   4: ifeq -> 8
      //   7: return
      //   8: aload_0
      //   9: getfield fastPath : Z
      //   12: ifne -> 107
      //   15: aload_0
      //   16: monitorenter
      //   17: aload_0
      //   18: getfield cancelled : Z
      //   21: ifeq -> 27
      //   24: aload_0
      //   25: monitorexit
      //   26: return
      //   27: aload_0
      //   28: getfield index : J
      //   31: lload_2
      //   32: lcmp
      //   33: ifne -> 39
      //   36: aload_0
      //   37: monitorexit
      //   38: return
      //   39: aload_0
      //   40: getfield emitting : Z
      //   43: ifeq -> 87
      //   46: aload_0
      //   47: getfield queue : Lio/reactivex/internal/util/AppendOnlyLinkedArrayList;
      //   50: astore #4
      //   52: aload #4
      //   54: astore #5
      //   56: aload #4
      //   58: ifnonnull -> 78
      //   61: new io/reactivex/internal/util/AppendOnlyLinkedArrayList
      //   64: astore #5
      //   66: aload #5
      //   68: iconst_4
      //   69: invokespecial <init> : (I)V
      //   72: aload_0
      //   73: aload #5
      //   75: putfield queue : Lio/reactivex/internal/util/AppendOnlyLinkedArrayList;
      //   78: aload #5
      //   80: aload_1
      //   81: invokevirtual add : (Ljava/lang/Object;)V
      //   84: aload_0
      //   85: monitorexit
      //   86: return
      //   87: aload_0
      //   88: iconst_1
      //   89: putfield next : Z
      //   92: aload_0
      //   93: monitorexit
      //   94: aload_0
      //   95: iconst_1
      //   96: putfield fastPath : Z
      //   99: goto -> 107
      //   102: astore_1
      //   103: aload_0
      //   104: monitorexit
      //   105: aload_1
      //   106: athrow
      //   107: aload_0
      //   108: aload_1
      //   109: invokevirtual test : (Ljava/lang/Object;)Z
      //   112: pop
      //   113: return
      // Exception table:
      //   from	to	target	type
      //   17	26	102	finally
      //   27	38	102	finally
      //   39	52	102	finally
      //   61	78	102	finally
      //   78	86	102	finally
      //   87	94	102	finally
      //   103	105	102	finally
    }
    
    public boolean isDisposed() {
      return this.cancelled;
    }
    
    public boolean test(Object param1Object) {
      return (this.cancelled || NotificationLite.accept(param1Object, this.downstream));
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/subjects/BehaviorSubject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */