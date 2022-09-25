package io.reactivex.observers;

import io.reactivex.CompletableObserver;
import io.reactivex.MaybeObserver;
import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.fuseable.QueueDisposable;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class TestObserver<T> extends BaseTestConsumer<T, TestObserver<T>> implements Observer<T>, Disposable, MaybeObserver<T>, SingleObserver<T>, CompletableObserver {
  private final Observer<? super T> downstream;
  
  private QueueDisposable<T> qd;
  
  private final AtomicReference<Disposable> upstream = new AtomicReference<Disposable>();
  
  public TestObserver() {
    this(EmptyObserver.INSTANCE);
  }
  
  public TestObserver(Observer<? super T> paramObserver) {
    this.downstream = paramObserver;
  }
  
  public static <T> TestObserver<T> create() {
    return new TestObserver<T>();
  }
  
  public static <T> TestObserver<T> create(Observer<? super T> paramObserver) {
    return new TestObserver<T>(paramObserver);
  }
  
  static String fusionModeToString(int paramInt) {
    if (paramInt != 0) {
      if (paramInt != 1) {
        if (paramInt != 2) {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("Unknown(");
          stringBuilder.append(paramInt);
          stringBuilder.append(")");
          return stringBuilder.toString();
        } 
        return "ASYNC";
      } 
      return "SYNC";
    } 
    return "NONE";
  }
  
  final TestObserver<T> assertFuseable() {
    if (this.qd != null)
      return this; 
    throw new AssertionError("Upstream is not fuseable.");
  }
  
  final TestObserver<T> assertFusionMode(int paramInt) {
    int i = this.establishedFusionMode;
    if (i != paramInt) {
      if (this.qd != null) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Fusion mode different. Expected: ");
        stringBuilder.append(fusionModeToString(paramInt));
        stringBuilder.append(", actual: ");
        stringBuilder.append(fusionModeToString(i));
        throw new AssertionError(stringBuilder.toString());
      } 
      throw fail("Upstream is not fuseable");
    } 
    return this;
  }
  
  final TestObserver<T> assertNotFuseable() {
    if (this.qd == null)
      return this; 
    throw new AssertionError("Upstream is fuseable.");
  }
  
  public final TestObserver<T> assertNotSubscribed() {
    if (this.upstream.get() == null) {
      if (this.errors.isEmpty())
        return this; 
      throw fail("Not subscribed but errors found");
    } 
    throw fail("Subscribed!");
  }
  
  public final TestObserver<T> assertOf(Consumer<? super TestObserver<T>> paramConsumer) {
    try {
      return this;
    } finally {
      paramConsumer = null;
    } 
  }
  
  public final TestObserver<T> assertSubscribed() {
    if (this.upstream.get() != null)
      return this; 
    throw fail("Not subscribed!");
  }
  
  public final void cancel() {
    dispose();
  }
  
  public final void dispose() {
    DisposableHelper.dispose(this.upstream);
  }
  
  public final boolean hasSubscription() {
    boolean bool;
    if (this.upstream.get() != null) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public final boolean isCancelled() {
    return isDisposed();
  }
  
  public final boolean isDisposed() {
    return DisposableHelper.isDisposed(this.upstream.get());
  }
  
  public void onComplete() {
    if (!this.checkSubscriptionOnce) {
      this.checkSubscriptionOnce = true;
      if (this.upstream.get() == null)
        this.errors.add(new IllegalStateException("onSubscribe not called in proper order")); 
    } 
    try {
      this.lastThread = Thread.currentThread();
      this.completions++;
      this.downstream.onComplete();
      return;
    } finally {
      this.done.countDown();
    } 
  }
  
  public void onError(Throwable paramThrowable) {
    if (!this.checkSubscriptionOnce) {
      this.checkSubscriptionOnce = true;
      if (this.upstream.get() == null)
        this.errors.add(new IllegalStateException("onSubscribe not called in proper order")); 
    } 
    try {
      this.lastThread = Thread.currentThread();
      if (paramThrowable == null) {
        List<Throwable> list = this.errors;
        NullPointerException nullPointerException = new NullPointerException();
        this("onError received a null Throwable");
        list.add(nullPointerException);
      } else {
        this.errors.add(paramThrowable);
      } 
      this.downstream.onError(paramThrowable);
      return;
    } finally {
      this.done.countDown();
    } 
  }
  
  public void onNext(T paramT) {
    if (!this.checkSubscriptionOnce) {
      this.checkSubscriptionOnce = true;
      if (this.upstream.get() == null)
        this.errors.add(new IllegalStateException("onSubscribe not called in proper order")); 
    } 
    this.lastThread = Thread.currentThread();
    if (this.establishedFusionMode == 2) {
      try {
        while (true) {
          paramT = (T)this.qd.poll();
          if (paramT != null) {
            this.values.add(paramT);
            continue;
          } 
          break;
        } 
      } finally {
        paramT = null;
        this.errors.add((Throwable)paramT);
      } 
      return;
    } 
    this.values.add(paramT);
    if (paramT == null)
      this.errors.add(new NullPointerException("onNext received a null value")); 
    this.downstream.onNext(paramT);
  }
  
  public void onSubscribe(Disposable paramDisposable) {
    this.lastThread = Thread.currentThread();
    if (paramDisposable == null) {
      this.errors.add(new NullPointerException("onSubscribe received a null Subscription"));
      return;
    } 
    if (!this.upstream.compareAndSet(null, paramDisposable)) {
      paramDisposable.dispose();
      if (this.upstream.get() != DisposableHelper.DISPOSED) {
        List<Throwable> list = this.errors;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("onSubscribe received multiple subscriptions: ");
        stringBuilder.append(paramDisposable);
        list.add(new IllegalStateException(stringBuilder.toString()));
      } 
      return;
    } 
    if (this.initialFusionMode != 0 && paramDisposable instanceof QueueDisposable) {
      this.qd = (QueueDisposable<T>)paramDisposable;
      int i = this.qd.requestFusion(this.initialFusionMode);
      this.establishedFusionMode = i;
      if (i == 1) {
        this.checkSubscriptionOnce = true;
        this.lastThread = Thread.currentThread();
        try {
          while (true) {
            Object object = this.qd.poll();
            if (object != null) {
              this.values.add((T)object);
              continue;
            } 
            this.completions++;
            return;
          } 
        } finally {
          paramDisposable = null;
        } 
        return;
      } 
    } 
    this.downstream.onSubscribe(paramDisposable);
  }
  
  public void onSuccess(T paramT) {
    onNext(paramT);
    onComplete();
  }
  
  final TestObserver<T> setInitialFusionMode(int paramInt) {
    this.initialFusionMode = paramInt;
    return this;
  }
  
  enum EmptyObserver implements Observer<Object> {
    INSTANCE;
    
    static {
    
    }
    
    public void onComplete() {}
    
    public void onError(Throwable param1Throwable) {}
    
    public void onNext(Object param1Object) {}
    
    public void onSubscribe(Disposable param1Disposable) {}
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/observers/TestObserver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */