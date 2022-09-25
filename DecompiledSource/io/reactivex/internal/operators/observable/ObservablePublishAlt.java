package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.disposables.ResettableConnectable;
import io.reactivex.internal.fuseable.HasUpstreamObservableSource;
import io.reactivex.observables.ConnectableObservable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public final class ObservablePublishAlt<T> extends ConnectableObservable<T> implements HasUpstreamObservableSource<T>, ResettableConnectable {
  final AtomicReference<PublishConnection<T>> current;
  
  final ObservableSource<T> source;
  
  public ObservablePublishAlt(ObservableSource<T> paramObservableSource) {
    this.source = paramObservableSource;
    this.current = new AtomicReference<PublishConnection<T>>();
  }
  
  public void connect(Consumer<? super Disposable> paramConsumer) {
    // Byte code:
    //   0: aload_0
    //   1: getfield current : Ljava/util/concurrent/atomic/AtomicReference;
    //   4: invokevirtual get : ()Ljava/lang/Object;
    //   7: checkcast io/reactivex/internal/operators/observable/ObservablePublishAlt$PublishConnection
    //   10: astore_2
    //   11: aload_2
    //   12: ifnull -> 24
    //   15: aload_2
    //   16: astore_3
    //   17: aload_2
    //   18: invokevirtual isDisposed : ()Z
    //   21: ifeq -> 51
    //   24: new io/reactivex/internal/operators/observable/ObservablePublishAlt$PublishConnection
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
    //   52: getfield connect : Ljava/util/concurrent/atomic/AtomicBoolean;
    //   55: invokevirtual get : ()Z
    //   58: istore #4
    //   60: iconst_1
    //   61: istore #5
    //   63: iload #4
    //   65: ifne -> 83
    //   68: aload_3
    //   69: getfield connect : Ljava/util/concurrent/atomic/AtomicBoolean;
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
  
  public void resetIf(Disposable paramDisposable) {
    this.current.compareAndSet((PublishConnection<T>)paramDisposable, null);
  }
  
  public ObservableSource<T> source() {
    return this.source;
  }
  
  protected void subscribeActual(Observer<? super T> paramObserver) {
    PublishConnection<T> publishConnection;
    while (true) {
      PublishConnection<T> publishConnection1 = this.current.get();
      publishConnection = publishConnection1;
      if (publishConnection1 == null) {
        publishConnection = new PublishConnection<T>(this.current);
        if (!this.current.compareAndSet(publishConnection1, publishConnection))
          continue; 
      } 
      break;
    } 
    InnerDisposable<T> innerDisposable = new InnerDisposable<T>(paramObserver, publishConnection);
    paramObserver.onSubscribe(innerDisposable);
    if (publishConnection.add(innerDisposable)) {
      if (innerDisposable.isDisposed())
        publishConnection.remove(innerDisposable); 
      return;
    } 
    Throwable throwable = publishConnection.error;
    if (throwable != null) {
      paramObserver.onError(throwable);
    } else {
      paramObserver.onComplete();
    } 
  }
  
  static final class InnerDisposable<T> extends AtomicReference<PublishConnection<T>> implements Disposable {
    private static final long serialVersionUID = 7463222674719692880L;
    
    final Observer<? super T> downstream;
    
    InnerDisposable(Observer<? super T> param1Observer, ObservablePublishAlt.PublishConnection<T> param1PublishConnection) {
      this.downstream = param1Observer;
      lazySet(param1PublishConnection);
    }
    
    public void dispose() {
      ObservablePublishAlt.PublishConnection<T> publishConnection = getAndSet(null);
      if (publishConnection != null)
        publishConnection.remove(this); 
    }
    
    public boolean isDisposed() {
      boolean bool;
      if (get() == null) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
  }
  
  static final class PublishConnection<T> extends AtomicReference<InnerDisposable<T>[]> implements Observer<T>, Disposable {
    static final ObservablePublishAlt.InnerDisposable[] EMPTY = new ObservablePublishAlt.InnerDisposable[0];
    
    static final ObservablePublishAlt.InnerDisposable[] TERMINATED = new ObservablePublishAlt.InnerDisposable[0];
    
    private static final long serialVersionUID = -3251430252873581268L;
    
    final AtomicBoolean connect = new AtomicBoolean();
    
    final AtomicReference<PublishConnection<T>> current;
    
    Throwable error;
    
    final AtomicReference<Disposable> upstream;
    
    PublishConnection(AtomicReference<PublishConnection<T>> param1AtomicReference) {
      this.current = param1AtomicReference;
      this.upstream = new AtomicReference<Disposable>();
      lazySet((ObservablePublishAlt.InnerDisposable<T>[])EMPTY);
    }
    
    public boolean add(ObservablePublishAlt.InnerDisposable<T> param1InnerDisposable) {
      while (true) {
        ObservablePublishAlt.InnerDisposable[] arrayOfInnerDisposable1 = (ObservablePublishAlt.InnerDisposable[])get();
        if (arrayOfInnerDisposable1 == TERMINATED)
          return false; 
        int i = arrayOfInnerDisposable1.length;
        ObservablePublishAlt.InnerDisposable[] arrayOfInnerDisposable2 = new ObservablePublishAlt.InnerDisposable[i + 1];
        System.arraycopy(arrayOfInnerDisposable1, 0, arrayOfInnerDisposable2, 0, i);
        arrayOfInnerDisposable2[i] = param1InnerDisposable;
        if (compareAndSet((ObservablePublishAlt.InnerDisposable<T>[])arrayOfInnerDisposable1, (ObservablePublishAlt.InnerDisposable<T>[])arrayOfInnerDisposable2))
          return true; 
      } 
    }
    
    public void dispose() {
      getAndSet((ObservablePublishAlt.InnerDisposable<T>[])TERMINATED);
      this.current.compareAndSet(this, null);
      DisposableHelper.dispose(this.upstream);
    }
    
    public boolean isDisposed() {
      boolean bool;
      if (get() == TERMINATED) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public void onComplete() {
      this.upstream.lazySet(DisposableHelper.DISPOSED);
      ObservablePublishAlt.InnerDisposable[] arrayOfInnerDisposable = (ObservablePublishAlt.InnerDisposable[])getAndSet((ObservablePublishAlt.InnerDisposable<T>[])TERMINATED);
      int i = arrayOfInnerDisposable.length;
      for (byte b = 0; b < i; b++)
        (arrayOfInnerDisposable[b]).downstream.onComplete(); 
    }
    
    public void onError(Throwable param1Throwable) {
      this.error = param1Throwable;
      this.upstream.lazySet(DisposableHelper.DISPOSED);
      ObservablePublishAlt.InnerDisposable[] arrayOfInnerDisposable = (ObservablePublishAlt.InnerDisposable[])getAndSet((ObservablePublishAlt.InnerDisposable<T>[])TERMINATED);
      int i = arrayOfInnerDisposable.length;
      for (byte b = 0; b < i; b++)
        (arrayOfInnerDisposable[b]).downstream.onError(param1Throwable); 
    }
    
    public void onNext(T param1T) {
      ObservablePublishAlt.InnerDisposable[] arrayOfInnerDisposable = (ObservablePublishAlt.InnerDisposable[])get();
      int i = arrayOfInnerDisposable.length;
      for (byte b = 0; b < i; b++)
        (arrayOfInnerDisposable[b]).downstream.onNext(param1T); 
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      DisposableHelper.setOnce(this.upstream, param1Disposable);
    }
    
    public void remove(ObservablePublishAlt.InnerDisposable<T> param1InnerDisposable) {
      ObservablePublishAlt.InnerDisposable[] arrayOfInnerDisposable1;
      ObservablePublishAlt.InnerDisposable[] arrayOfInnerDisposable2;
      do {
        byte b2;
        arrayOfInnerDisposable1 = (ObservablePublishAlt.InnerDisposable[])get();
        int i = arrayOfInnerDisposable1.length;
        if (i == 0)
          return; 
        byte b1 = -1;
        byte b = 0;
        while (true) {
          b2 = b1;
          if (b < i) {
            if (arrayOfInnerDisposable1[b] == param1InnerDisposable) {
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
        arrayOfInnerDisposable2 = EMPTY;
        if (i == 1)
          continue; 
        arrayOfInnerDisposable2 = new ObservablePublishAlt.InnerDisposable[i - 1];
        System.arraycopy(arrayOfInnerDisposable1, 0, arrayOfInnerDisposable2, 0, b2);
        System.arraycopy(arrayOfInnerDisposable1, b2 + 1, arrayOfInnerDisposable2, b2, i - b2 - 1);
      } while (!compareAndSet((ObservablePublishAlt.InnerDisposable<T>[])arrayOfInnerDisposable1, (ObservablePublishAlt.InnerDisposable<T>[])arrayOfInnerDisposable2));
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservablePublishAlt.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */