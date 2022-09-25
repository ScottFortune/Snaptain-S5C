package io.reactivex.subscribers;

import io.reactivex.FlowableSubscriber;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.fuseable.QueueSubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.observers.BaseTestConsumer;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public class TestSubscriber<T> extends BaseTestConsumer<T, TestSubscriber<T>> implements FlowableSubscriber<T>, Subscription, Disposable {
  private volatile boolean cancelled;
  
  private final Subscriber<? super T> downstream;
  
  private final AtomicLong missedRequested;
  
  private QueueSubscription<T> qs;
  
  private final AtomicReference<Subscription> upstream;
  
  public TestSubscriber() {
    this((Subscriber<? super T>)EmptySubscriber.INSTANCE, Long.MAX_VALUE);
  }
  
  public TestSubscriber(long paramLong) {
    this((Subscriber<? super T>)EmptySubscriber.INSTANCE, paramLong);
  }
  
  public TestSubscriber(Subscriber<? super T> paramSubscriber) {
    this(paramSubscriber, Long.MAX_VALUE);
  }
  
  public TestSubscriber(Subscriber<? super T> paramSubscriber, long paramLong) {
    if (paramLong >= 0L) {
      this.downstream = paramSubscriber;
      this.upstream = new AtomicReference<Subscription>();
      this.missedRequested = new AtomicLong(paramLong);
      return;
    } 
    throw new IllegalArgumentException("Negative initial request not allowed");
  }
  
  public static <T> TestSubscriber<T> create() {
    return new TestSubscriber<T>();
  }
  
  public static <T> TestSubscriber<T> create(long paramLong) {
    return new TestSubscriber<T>(paramLong);
  }
  
  public static <T> TestSubscriber<T> create(Subscriber<? super T> paramSubscriber) {
    return new TestSubscriber<T>(paramSubscriber);
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
  
  final TestSubscriber<T> assertFuseable() {
    if (this.qs != null)
      return this; 
    throw new AssertionError("Upstream is not fuseable.");
  }
  
  final TestSubscriber<T> assertFusionMode(int paramInt) {
    int i = this.establishedFusionMode;
    if (i != paramInt) {
      if (this.qs != null) {
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
  
  final TestSubscriber<T> assertNotFuseable() {
    if (this.qs == null)
      return this; 
    throw new AssertionError("Upstream is fuseable.");
  }
  
  public final TestSubscriber<T> assertNotSubscribed() {
    if (this.upstream.get() == null) {
      if (this.errors.isEmpty())
        return this; 
      throw fail("Not subscribed but errors found");
    } 
    throw fail("Subscribed!");
  }
  
  public final TestSubscriber<T> assertOf(Consumer<? super TestSubscriber<T>> paramConsumer) {
    try {
      return this;
    } finally {
      paramConsumer = null;
    } 
  }
  
  public final TestSubscriber<T> assertSubscribed() {
    if (this.upstream.get() != null)
      return this; 
    throw fail("Not subscribed!");
  }
  
  public final void cancel() {
    if (!this.cancelled) {
      this.cancelled = true;
      SubscriptionHelper.cancel(this.upstream);
    } 
  }
  
  public final void dispose() {
    cancel();
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
    return this.cancelled;
  }
  
  public final boolean isDisposed() {
    return this.cancelled;
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
        this.errors.add(new NullPointerException("onSubscribe not called in proper order")); 
    } 
    try {
      this.lastThread = Thread.currentThread();
      this.errors.add(paramThrowable);
      if (paramThrowable == null) {
        List<IllegalStateException> list = this.errors;
        IllegalStateException illegalStateException = new IllegalStateException();
        this("onError received a null Throwable");
        list.add(illegalStateException);
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
          paramT = (T)this.qs.poll();
          if (paramT != null) {
            this.values.add(paramT);
            continue;
          } 
          break;
        } 
      } finally {
        paramT = null;
        this.errors.add(paramT);
      } 
      return;
    } 
    this.values.add(paramT);
    if (paramT == null)
      this.errors.add(new NullPointerException("onNext received a null value")); 
    this.downstream.onNext(paramT);
  }
  
  protected void onStart() {}
  
  public void onSubscribe(Subscription paramSubscription) {
    this.lastThread = Thread.currentThread();
    if (paramSubscription == null) {
      this.errors.add(new NullPointerException("onSubscribe received a null Subscription"));
      return;
    } 
    if (!this.upstream.compareAndSet(null, paramSubscription)) {
      paramSubscription.cancel();
      if (this.upstream.get() != SubscriptionHelper.CANCELLED) {
        List<IllegalStateException> list = this.errors;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("onSubscribe received multiple subscriptions: ");
        stringBuilder.append(paramSubscription);
        list.add(new IllegalStateException(stringBuilder.toString()));
      } 
      return;
    } 
    if (this.initialFusionMode != 0 && paramSubscription instanceof QueueSubscription) {
      this.qs = (QueueSubscription<T>)paramSubscription;
      int i = this.qs.requestFusion(this.initialFusionMode);
      this.establishedFusionMode = i;
      if (i == 1) {
        this.checkSubscriptionOnce = true;
        this.lastThread = Thread.currentThread();
        try {
          while (true) {
            Object object = this.qs.poll();
            if (object != null) {
              this.values.add(object);
              continue;
            } 
            return;
          } 
        } finally {
          paramSubscription = null;
        } 
        return;
      } 
    } 
    this.downstream.onSubscribe(paramSubscription);
    long l = this.missedRequested.getAndSet(0L);
    if (l != 0L)
      paramSubscription.request(l); 
    onStart();
  }
  
  public final void request(long paramLong) {
    SubscriptionHelper.deferredRequest(this.upstream, this.missedRequested, paramLong);
  }
  
  public final TestSubscriber<T> requestMore(long paramLong) {
    request(paramLong);
    return this;
  }
  
  final TestSubscriber<T> setInitialFusionMode(int paramInt) {
    this.initialFusionMode = paramInt;
    return this;
  }
  
  enum EmptySubscriber implements FlowableSubscriber<Object> {
    INSTANCE;
    
    static {
    
    }
    
    public void onComplete() {}
    
    public void onError(Throwable param1Throwable) {}
    
    public void onNext(Object param1Object) {}
    
    public void onSubscribe(Subscription param1Subscription) {}
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/subscribers/TestSubscriber.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */