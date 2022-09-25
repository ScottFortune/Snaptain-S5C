package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.fuseable.HasUpstreamObservableSource;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public final class ObservablePublish<T> extends ConnectableObservable<T> implements HasUpstreamObservableSource<T>, ObservablePublishClassic<T> {
  final AtomicReference<PublishObserver<T>> current;
  
  final ObservableSource<T> onSubscribe;
  
  final ObservableSource<T> source;
  
  private ObservablePublish(ObservableSource<T> paramObservableSource1, ObservableSource<T> paramObservableSource2, AtomicReference<PublishObserver<T>> paramAtomicReference) {
    this.onSubscribe = paramObservableSource1;
    this.source = paramObservableSource2;
    this.current = paramAtomicReference;
  }
  
  public static <T> ConnectableObservable<T> create(ObservableSource<T> paramObservableSource) {
    AtomicReference<PublishObserver<T>> atomicReference = new AtomicReference();
    return RxJavaPlugins.onAssembly(new ObservablePublish<T>(new PublishSource<T>(atomicReference), paramObservableSource, atomicReference));
  }
  
  public void connect(Consumer<? super Disposable> paramConsumer) {
    // Byte code:
    //   0: aload_0
    //   1: getfield current : Ljava/util/concurrent/atomic/AtomicReference;
    //   4: invokevirtual get : ()Ljava/lang/Object;
    //   7: checkcast io/reactivex/internal/operators/observable/ObservablePublish$PublishObserver
    //   10: astore_2
    //   11: aload_2
    //   12: ifnull -> 24
    //   15: aload_2
    //   16: astore_3
    //   17: aload_2
    //   18: invokevirtual isDisposed : ()Z
    //   21: ifeq -> 51
    //   24: new io/reactivex/internal/operators/observable/ObservablePublish$PublishObserver
    //   27: dup
    //   28: aload_0
    //   29: getfield current : Ljava/util/concurrent/atomic/AtomicReference;
    //   32: invokespecial <init> : (Ljava/util/concurrent/atomic/AtomicReference;)V
    //   35: astore_3
    //   36: aload_0
    //   37: getfield current : Ljava/util/concurrent/atomic/AtomicReference;
    //   40: aload_2
    //   41: aload_3
    //   42: invokevirtual compareAndSet : (Ljava/lang/Object;Ljava/lang/Object;)Z
    //   45: ifne -> 51
    //   48: goto -> 0
    //   51: aload_3
    //   52: getfield shouldConnect : Ljava/util/concurrent/atomic/AtomicBoolean;
    //   55: invokevirtual get : ()Z
    //   58: istore #4
    //   60: iconst_1
    //   61: istore #5
    //   63: iload #4
    //   65: ifne -> 83
    //   68: aload_3
    //   69: getfield shouldConnect : Ljava/util/concurrent/atomic/AtomicBoolean;
    //   72: iconst_0
    //   73: iconst_1
    //   74: invokevirtual compareAndSet : (ZZ)Z
    //   77: ifeq -> 83
    //   80: goto -> 86
    //   83: iconst_0
    //   84: istore #5
    //   86: aload_1
    //   87: aload_3
    //   88: invokeinterface accept : (Ljava/lang/Object;)V
    //   93: iload #5
    //   95: ifeq -> 108
    //   98: aload_0
    //   99: getfield source : Lio/reactivex/ObservableSource;
    //   102: aload_3
    //   103: invokeinterface subscribe : (Lio/reactivex/Observer;)V
    //   108: return
    //   109: astore_1
    //   110: aload_1
    //   111: invokestatic throwIfFatal : (Ljava/lang/Throwable;)V
    //   114: aload_1
    //   115: invokestatic wrapOrThrow : (Ljava/lang/Throwable;)Ljava/lang/RuntimeException;
    //   118: astore_1
    //   119: goto -> 124
    //   122: aload_1
    //   123: athrow
    //   124: goto -> 122
    // Exception table:
    //   from	to	target	type
    //   86	93	109	finally
  }
  
  public ObservableSource<T> publishSource() {
    return this.source;
  }
  
  public ObservableSource<T> source() {
    return this.source;
  }
  
  protected void subscribeActual(Observer<? super T> paramObserver) {
    this.onSubscribe.subscribe(paramObserver);
  }
  
  static final class InnerDisposable<T> extends AtomicReference<Object> implements Disposable {
    private static final long serialVersionUID = -1100270633763673112L;
    
    final Observer<? super T> child;
    
    InnerDisposable(Observer<? super T> param1Observer) {
      this.child = param1Observer;
    }
    
    public void dispose() {
      Object object = getAndSet(this);
      if (object != null && object != this)
        ((ObservablePublish.PublishObserver)object).remove(this); 
    }
    
    public boolean isDisposed() {
      boolean bool;
      if (get() == this) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    void setParent(ObservablePublish.PublishObserver<T> param1PublishObserver) {
      if (!compareAndSet(null, param1PublishObserver))
        param1PublishObserver.remove(this); 
    }
  }
  
  static final class PublishObserver<T> implements Observer<T>, Disposable {
    static final ObservablePublish.InnerDisposable[] EMPTY = new ObservablePublish.InnerDisposable[0];
    
    static final ObservablePublish.InnerDisposable[] TERMINATED = new ObservablePublish.InnerDisposable[0];
    
    final AtomicReference<PublishObserver<T>> current;
    
    final AtomicReference<ObservablePublish.InnerDisposable<T>[]> observers = new AtomicReference(EMPTY);
    
    final AtomicBoolean shouldConnect;
    
    final AtomicReference<Disposable> upstream = new AtomicReference<Disposable>();
    
    PublishObserver(AtomicReference<PublishObserver<T>> param1AtomicReference) {
      this.current = param1AtomicReference;
      this.shouldConnect = new AtomicBoolean();
    }
    
    boolean add(ObservablePublish.InnerDisposable<T> param1InnerDisposable) {
      while (true) {
        ObservablePublish.InnerDisposable[] arrayOfInnerDisposable1 = (ObservablePublish.InnerDisposable[])this.observers.get();
        if (arrayOfInnerDisposable1 == TERMINATED)
          return false; 
        int i = arrayOfInnerDisposable1.length;
        ObservablePublish.InnerDisposable[] arrayOfInnerDisposable2 = new ObservablePublish.InnerDisposable[i + 1];
        System.arraycopy(arrayOfInnerDisposable1, 0, arrayOfInnerDisposable2, 0, i);
        arrayOfInnerDisposable2[i] = param1InnerDisposable;
        if (this.observers.compareAndSet(arrayOfInnerDisposable1, arrayOfInnerDisposable2))
          return true; 
      } 
    }
    
    public void dispose() {
      if ((ObservablePublish.InnerDisposable[])this.observers.getAndSet(TERMINATED) != TERMINATED) {
        this.current.compareAndSet(this, null);
        DisposableHelper.dispose(this.upstream);
      } 
    }
    
    public boolean isDisposed() {
      boolean bool;
      if (this.observers.get() == TERMINATED) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public void onComplete() {
      this.current.compareAndSet(this, null);
      ObservablePublish.InnerDisposable[] arrayOfInnerDisposable = (ObservablePublish.InnerDisposable[])this.observers.getAndSet(TERMINATED);
      int i = arrayOfInnerDisposable.length;
      for (byte b = 0; b < i; b++)
        (arrayOfInnerDisposable[b]).child.onComplete(); 
    }
    
    public void onError(Throwable param1Throwable) {
      this.current.compareAndSet(this, null);
      ObservablePublish.InnerDisposable[] arrayOfInnerDisposable = (ObservablePublish.InnerDisposable[])this.observers.getAndSet(TERMINATED);
      if (arrayOfInnerDisposable.length != 0) {
        int i = arrayOfInnerDisposable.length;
        for (byte b = 0; b < i; b++)
          (arrayOfInnerDisposable[b]).child.onError(param1Throwable); 
      } else {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
    
    public void onNext(T param1T) {
      ObservablePublish.InnerDisposable[] arrayOfInnerDisposable = (ObservablePublish.InnerDisposable[])this.observers.get();
      int i = arrayOfInnerDisposable.length;
      for (byte b = 0; b < i; b++)
        (arrayOfInnerDisposable[b]).child.onNext(param1T); 
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      DisposableHelper.setOnce(this.upstream, param1Disposable);
    }
    
    void remove(ObservablePublish.InnerDisposable<T> param1InnerDisposable) {
      ObservablePublish.InnerDisposable[] arrayOfInnerDisposable1;
      ObservablePublish.InnerDisposable[] arrayOfInnerDisposable2;
      do {
        byte b2;
        arrayOfInnerDisposable1 = (ObservablePublish.InnerDisposable[])this.observers.get();
        int i = arrayOfInnerDisposable1.length;
        if (i == 0)
          return; 
        byte b1 = -1;
        byte b = 0;
        while (true) {
          b2 = b1;
          if (b < i) {
            if (arrayOfInnerDisposable1[b].equals(param1InnerDisposable)) {
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
          arrayOfInnerDisposable2 = EMPTY;
        } else {
          arrayOfInnerDisposable2 = new ObservablePublish.InnerDisposable[i - 1];
          System.arraycopy(arrayOfInnerDisposable1, 0, arrayOfInnerDisposable2, 0, b2);
          System.arraycopy(arrayOfInnerDisposable1, b2 + 1, arrayOfInnerDisposable2, b2, i - b2 - 1);
        } 
      } while (!this.observers.compareAndSet(arrayOfInnerDisposable1, arrayOfInnerDisposable2));
    }
  }
  
  static final class PublishSource<T> implements ObservableSource<T> {
    private final AtomicReference<ObservablePublish.PublishObserver<T>> curr;
    
    PublishSource(AtomicReference<ObservablePublish.PublishObserver<T>> param1AtomicReference) {
      this.curr = param1AtomicReference;
    }
    
    public void subscribe(Observer<? super T> param1Observer) {
      ObservablePublish.InnerDisposable<T> innerDisposable = new ObservablePublish.InnerDisposable<T>(param1Observer);
      param1Observer.onSubscribe(innerDisposable);
      while (true) {
        while (true) {
          ObservablePublish.PublishObserver publishObserver = this.curr.get();
          break;
        } 
        if (param1Observer.add(innerDisposable)) {
          innerDisposable.setParent((ObservablePublish.PublishObserver)param1Observer);
          return;
        } 
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservablePublish.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */