package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.MissingBackpressureException;
import io.reactivex.internal.disposables.SequentialDisposable;
import io.reactivex.internal.fuseable.SimplePlainQueue;
import io.reactivex.internal.queue.MpscLinkedQueue;
import io.reactivex.internal.subscribers.QueueDrainSubscriber;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.NotificationLite;
import io.reactivex.processors.UnicastProcessor;
import io.reactivex.subscribers.SerializedSubscriber;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableWindowTimed<T> extends AbstractFlowableWithUpstream<T, Flowable<T>> {
  final int bufferSize;
  
  final long maxSize;
  
  final boolean restartTimerOnMaxSize;
  
  final Scheduler scheduler;
  
  final long timeskip;
  
  final long timespan;
  
  final TimeUnit unit;
  
  public FlowableWindowTimed(Flowable<T> paramFlowable, long paramLong1, long paramLong2, TimeUnit paramTimeUnit, Scheduler paramScheduler, long paramLong3, int paramInt, boolean paramBoolean) {
    super(paramFlowable);
    this.timespan = paramLong1;
    this.timeskip = paramLong2;
    this.unit = paramTimeUnit;
    this.scheduler = paramScheduler;
    this.maxSize = paramLong3;
    this.bufferSize = paramInt;
    this.restartTimerOnMaxSize = paramBoolean;
  }
  
  protected void subscribeActual(Subscriber<? super Flowable<T>> paramSubscriber) {
    SerializedSubscriber serializedSubscriber = new SerializedSubscriber(paramSubscriber);
    if (this.timespan == this.timeskip) {
      if (this.maxSize == Long.MAX_VALUE) {
        this.source.subscribe(new WindowExactUnboundedSubscriber((Subscriber<? super Flowable<?>>)serializedSubscriber, this.timespan, this.unit, this.scheduler, this.bufferSize));
        return;
      } 
      this.source.subscribe((FlowableSubscriber)new WindowExactBoundedSubscriber((Subscriber<? super Flowable<?>>)serializedSubscriber, this.timespan, this.unit, this.scheduler, this.bufferSize, this.maxSize, this.restartTimerOnMaxSize));
      return;
    } 
    this.source.subscribe((FlowableSubscriber)new WindowSkipSubscriber((Subscriber<? super Flowable<?>>)serializedSubscriber, this.timespan, this.timeskip, this.unit, this.scheduler.createWorker(), this.bufferSize));
  }
  
  static final class WindowExactBoundedSubscriber<T> extends QueueDrainSubscriber<T, Object, Flowable<T>> implements Subscription {
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
    
    Subscription upstream;
    
    UnicastProcessor<T> window;
    
    final Scheduler.Worker worker;
    
    WindowExactBoundedSubscriber(Subscriber<? super Flowable<T>> param1Subscriber, long param1Long1, TimeUnit param1TimeUnit, Scheduler param1Scheduler, int param1Int, long param1Long2, boolean param1Boolean) {
      super(param1Subscriber, (SimplePlainQueue)new MpscLinkedQueue());
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
    
    public void cancel() {
      this.cancelled = true;
    }
    
    public void disposeTimer() {
      this.timer.dispose();
      Scheduler.Worker worker = this.worker;
      if (worker != null)
        worker.dispose(); 
    }
    
    void drainLoop() {
      // Byte code:
      //   0: aload_0
      //   1: getfield queue : Lio/reactivex/internal/fuseable/SimplePlainQueue;
      //   4: astore_1
      //   5: aload_0
      //   6: getfield downstream : Lorg/reactivestreams/Subscriber;
      //   9: astore_2
      //   10: aload_0
      //   11: getfield window : Lio/reactivex/processors/UnicastProcessor;
      //   14: astore_3
      //   15: iconst_1
      //   16: istore #4
      //   18: aload_0
      //   19: getfield terminated : Z
      //   22: ifeq -> 45
      //   25: aload_0
      //   26: getfield upstream : Lorg/reactivestreams/Subscription;
      //   29: invokeinterface cancel : ()V
      //   34: aload_1
      //   35: invokeinterface clear : ()V
      //   40: aload_0
      //   41: invokevirtual disposeTimer : ()V
      //   44: return
      //   45: aload_0
      //   46: getfield done : Z
      //   49: istore #5
      //   51: aload_1
      //   52: invokeinterface poll : ()Ljava/lang/Object;
      //   57: astore #6
      //   59: aload #6
      //   61: ifnonnull -> 70
      //   64: iconst_1
      //   65: istore #7
      //   67: goto -> 73
      //   70: iconst_0
      //   71: istore #7
      //   73: aload #6
      //   75: instanceof io/reactivex/internal/operators/flowable/FlowableWindowTimed$WindowExactBoundedSubscriber$ConsumerIndexHolder
      //   78: istore #8
      //   80: iload #5
      //   82: ifeq -> 135
      //   85: iload #7
      //   87: ifne -> 95
      //   90: iload #8
      //   92: ifeq -> 135
      //   95: aload_0
      //   96: aconst_null
      //   97: putfield window : Lio/reactivex/processors/UnicastProcessor;
      //   100: aload_1
      //   101: invokeinterface clear : ()V
      //   106: aload_0
      //   107: getfield error : Ljava/lang/Throwable;
      //   110: astore #6
      //   112: aload #6
      //   114: ifnull -> 126
      //   117: aload_3
      //   118: aload #6
      //   120: invokevirtual onError : (Ljava/lang/Throwable;)V
      //   123: goto -> 130
      //   126: aload_3
      //   127: invokevirtual onComplete : ()V
      //   130: aload_0
      //   131: invokevirtual disposeTimer : ()V
      //   134: return
      //   135: iload #7
      //   137: ifeq -> 159
      //   140: aload_0
      //   141: iload #4
      //   143: ineg
      //   144: invokevirtual leave : (I)I
      //   147: istore #7
      //   149: iload #7
      //   151: istore #4
      //   153: iload #7
      //   155: ifne -> 18
      //   158: return
      //   159: iload #8
      //   161: ifeq -> 312
      //   164: aload #6
      //   166: checkcast io/reactivex/internal/operators/flowable/FlowableWindowTimed$WindowExactBoundedSubscriber$ConsumerIndexHolder
      //   169: astore #9
      //   171: aload_0
      //   172: getfield restartTimerOnMaxSize : Z
      //   175: ifeq -> 203
      //   178: aload_3
      //   179: astore #6
      //   181: aload_0
      //   182: getfield producerIndex : J
      //   185: aload #9
      //   187: getfield index : J
      //   190: lcmp
      //   191: ifne -> 197
      //   194: goto -> 203
      //   197: aload #6
      //   199: astore_3
      //   200: goto -> 18
      //   203: aload_3
      //   204: invokevirtual onComplete : ()V
      //   207: aload_0
      //   208: lconst_0
      //   209: putfield count : J
      //   212: aload_0
      //   213: getfield bufferSize : I
      //   216: invokestatic create : (I)Lio/reactivex/processors/UnicastProcessor;
      //   219: astore_3
      //   220: aload_0
      //   221: aload_3
      //   222: putfield window : Lio/reactivex/processors/UnicastProcessor;
      //   225: aload_0
      //   226: invokevirtual requested : ()J
      //   229: lstore #10
      //   231: lload #10
      //   233: lconst_0
      //   234: lcmp
      //   235: ifeq -> 269
      //   238: aload_2
      //   239: aload_3
      //   240: invokeinterface onNext : (Ljava/lang/Object;)V
      //   245: aload_3
      //   246: astore #6
      //   248: lload #10
      //   250: ldc2_w 9223372036854775807
      //   253: lcmp
      //   254: ifeq -> 197
      //   257: aload_0
      //   258: lconst_1
      //   259: invokevirtual produced : (J)J
      //   262: pop2
      //   263: aload_3
      //   264: astore #6
      //   266: goto -> 197
      //   269: aload_0
      //   270: aconst_null
      //   271: putfield window : Lio/reactivex/processors/UnicastProcessor;
      //   274: aload_0
      //   275: getfield queue : Lio/reactivex/internal/fuseable/SimplePlainQueue;
      //   278: invokeinterface clear : ()V
      //   283: aload_0
      //   284: getfield upstream : Lorg/reactivestreams/Subscription;
      //   287: invokeinterface cancel : ()V
      //   292: aload_2
      //   293: new io/reactivex/exceptions/MissingBackpressureException
      //   296: dup
      //   297: ldc 'Could not deliver first window due to lack of requests.'
      //   299: invokespecial <init> : (Ljava/lang/String;)V
      //   302: invokeinterface onError : (Ljava/lang/Throwable;)V
      //   307: aload_0
      //   308: invokevirtual disposeTimer : ()V
      //   311: return
      //   312: aload_3
      //   313: aload #6
      //   315: invokestatic getValue : (Ljava/lang/Object;)Ljava/lang/Object;
      //   318: invokevirtual onNext : (Ljava/lang/Object;)V
      //   321: aload_0
      //   322: getfield count : J
      //   325: lconst_1
      //   326: ladd
      //   327: lstore #10
      //   329: lload #10
      //   331: aload_0
      //   332: getfield maxSize : J
      //   335: lcmp
      //   336: iflt -> 523
      //   339: aload_0
      //   340: aload_0
      //   341: getfield producerIndex : J
      //   344: lconst_1
      //   345: ladd
      //   346: putfield producerIndex : J
      //   349: aload_0
      //   350: lconst_0
      //   351: putfield count : J
      //   354: aload_3
      //   355: invokevirtual onComplete : ()V
      //   358: aload_0
      //   359: invokevirtual requested : ()J
      //   362: lstore #10
      //   364: lload #10
      //   366: lconst_0
      //   367: lcmp
      //   368: ifeq -> 486
      //   371: aload_0
      //   372: getfield bufferSize : I
      //   375: invokestatic create : (I)Lio/reactivex/processors/UnicastProcessor;
      //   378: astore #6
      //   380: aload_0
      //   381: aload #6
      //   383: putfield window : Lio/reactivex/processors/UnicastProcessor;
      //   386: aload_0
      //   387: getfield downstream : Lorg/reactivestreams/Subscriber;
      //   390: aload #6
      //   392: invokeinterface onNext : (Ljava/lang/Object;)V
      //   397: lload #10
      //   399: ldc2_w 9223372036854775807
      //   402: lcmp
      //   403: ifeq -> 412
      //   406: aload_0
      //   407: lconst_1
      //   408: invokevirtual produced : (J)J
      //   411: pop2
      //   412: aload_0
      //   413: getfield restartTimerOnMaxSize : Z
      //   416: ifeq -> 483
      //   419: aload_0
      //   420: getfield timer : Lio/reactivex/internal/disposables/SequentialDisposable;
      //   423: invokevirtual get : ()Ljava/lang/Object;
      //   426: checkcast io/reactivex/disposables/Disposable
      //   429: invokeinterface dispose : ()V
      //   434: aload_0
      //   435: getfield worker : Lio/reactivex/Scheduler$Worker;
      //   438: astore_3
      //   439: new io/reactivex/internal/operators/flowable/FlowableWindowTimed$WindowExactBoundedSubscriber$ConsumerIndexHolder
      //   442: dup
      //   443: aload_0
      //   444: getfield producerIndex : J
      //   447: aload_0
      //   448: invokespecial <init> : (JLio/reactivex/internal/operators/flowable/FlowableWindowTimed$WindowExactBoundedSubscriber;)V
      //   451: astore #9
      //   453: aload_0
      //   454: getfield timespan : J
      //   457: lstore #10
      //   459: aload_3
      //   460: aload #9
      //   462: lload #10
      //   464: lload #10
      //   466: aload_0
      //   467: getfield unit : Ljava/util/concurrent/TimeUnit;
      //   470: invokevirtual schedulePeriodically : (Ljava/lang/Runnable;JJLjava/util/concurrent/TimeUnit;)Lio/reactivex/disposables/Disposable;
      //   473: astore_3
      //   474: aload_0
      //   475: getfield timer : Lio/reactivex/internal/disposables/SequentialDisposable;
      //   478: aload_3
      //   479: invokevirtual replace : (Lio/reactivex/disposables/Disposable;)Z
      //   482: pop
      //   483: goto -> 197
      //   486: aload_0
      //   487: aconst_null
      //   488: putfield window : Lio/reactivex/processors/UnicastProcessor;
      //   491: aload_0
      //   492: getfield upstream : Lorg/reactivestreams/Subscription;
      //   495: invokeinterface cancel : ()V
      //   500: aload_0
      //   501: getfield downstream : Lorg/reactivestreams/Subscriber;
      //   504: new io/reactivex/exceptions/MissingBackpressureException
      //   507: dup
      //   508: ldc 'Could not deliver window due to lack of requests'
      //   510: invokespecial <init> : (Ljava/lang/String;)V
      //   513: invokeinterface onError : (Ljava/lang/Throwable;)V
      //   518: aload_0
      //   519: invokevirtual disposeTimer : ()V
      //   522: return
      //   523: aload_0
      //   524: lload #10
      //   526: putfield count : J
      //   529: aload_3
      //   530: astore #6
      //   532: goto -> 197
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
        UnicastProcessor<T> unicastProcessor = this.window;
        unicastProcessor.onNext(param1T);
        long l = this.count + 1L;
        if (l >= this.maxSize) {
          this.producerIndex++;
          this.count = 0L;
          unicastProcessor.onComplete();
          l = requested();
          if (l != 0L) {
            UnicastProcessor<T> unicastProcessor1 = UnicastProcessor.create(this.bufferSize);
            this.window = unicastProcessor1;
            this.downstream.onNext(unicastProcessor1);
            if (l != Long.MAX_VALUE)
              produced(1L); 
            if (this.restartTimerOnMaxSize) {
              ((Disposable)this.timer.get()).dispose();
              Scheduler.Worker worker = this.worker;
              ConsumerIndexHolder consumerIndexHolder = new ConsumerIndexHolder(this.producerIndex, this);
              l = this.timespan;
              disposable = worker.schedulePeriodically(consumerIndexHolder, l, l, this.unit);
              this.timer.replace(disposable);
            } 
          } else {
            this.window = null;
            this.upstream.cancel();
            this.downstream.onError((Throwable)new MissingBackpressureException("Could not deliver window due to lack of requests"));
            disposeTimer();
            return;
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
    
    public void onSubscribe(Subscription param1Subscription) {
      if (SubscriptionHelper.validate(this.upstream, param1Subscription)) {
        Scheduler scheduler;
        this.upstream = param1Subscription;
        Subscriber subscriber = this.downstream;
        subscriber.onSubscribe(this);
        if (this.cancelled)
          return; 
        UnicastProcessor<T> unicastProcessor = UnicastProcessor.create(this.bufferSize);
        this.window = unicastProcessor;
        long l = requested();
        if (l != 0L) {
          Disposable disposable;
          subscriber.onNext(unicastProcessor);
          if (l != Long.MAX_VALUE)
            produced(1L); 
          ConsumerIndexHolder consumerIndexHolder = new ConsumerIndexHolder(this.producerIndex, this);
          if (this.restartTimerOnMaxSize) {
            Scheduler.Worker worker = this.worker;
            l = this.timespan;
            disposable = worker.schedulePeriodically(consumerIndexHolder, l, l, this.unit);
          } else {
            scheduler = this.scheduler;
            l = this.timespan;
            disposable = scheduler.schedulePeriodicallyDirect((Runnable)disposable, l, l, this.unit);
          } 
          if (this.timer.replace(disposable))
            param1Subscription.request(Long.MAX_VALUE); 
        } else {
          this.cancelled = true;
          param1Subscription.cancel();
          scheduler.onError((Throwable)new MissingBackpressureException("Could not deliver initial window due to lack of requests."));
        } 
      } 
    }
    
    public void request(long param1Long) {
      requested(param1Long);
    }
    
    static final class ConsumerIndexHolder implements Runnable {
      final long index;
      
      final FlowableWindowTimed.WindowExactBoundedSubscriber<?> parent;
      
      ConsumerIndexHolder(long param2Long, FlowableWindowTimed.WindowExactBoundedSubscriber<?> param2WindowExactBoundedSubscriber) {
        this.index = param2Long;
        this.parent = param2WindowExactBoundedSubscriber;
      }
      
      public void run() {
        FlowableWindowTimed.WindowExactBoundedSubscriber<?> windowExactBoundedSubscriber = this.parent;
        if (!windowExactBoundedSubscriber.cancelled) {
          windowExactBoundedSubscriber.queue.offer(this);
        } else {
          windowExactBoundedSubscriber.terminated = true;
        } 
        if (windowExactBoundedSubscriber.enter())
          windowExactBoundedSubscriber.drainLoop(); 
      }
    }
  }
  
  static final class ConsumerIndexHolder implements Runnable {
    final long index;
    
    final FlowableWindowTimed.WindowExactBoundedSubscriber<?> parent;
    
    ConsumerIndexHolder(long param1Long, FlowableWindowTimed.WindowExactBoundedSubscriber<?> param1WindowExactBoundedSubscriber) {
      this.index = param1Long;
      this.parent = param1WindowExactBoundedSubscriber;
    }
    
    public void run() {
      FlowableWindowTimed.WindowExactBoundedSubscriber<?> windowExactBoundedSubscriber = this.parent;
      if (!windowExactBoundedSubscriber.cancelled) {
        windowExactBoundedSubscriber.queue.offer(this);
      } else {
        windowExactBoundedSubscriber.terminated = true;
      } 
      if (windowExactBoundedSubscriber.enter())
        windowExactBoundedSubscriber.drainLoop(); 
    }
  }
  
  static final class WindowExactUnboundedSubscriber<T> extends QueueDrainSubscriber<T, Object, Flowable<T>> implements FlowableSubscriber<T>, Subscription, Runnable {
    static final Object NEXT = new Object();
    
    final int bufferSize;
    
    final Scheduler scheduler;
    
    volatile boolean terminated;
    
    final SequentialDisposable timer = new SequentialDisposable();
    
    final long timespan;
    
    final TimeUnit unit;
    
    Subscription upstream;
    
    UnicastProcessor<T> window;
    
    WindowExactUnboundedSubscriber(Subscriber<? super Flowable<T>> param1Subscriber, long param1Long, TimeUnit param1TimeUnit, Scheduler param1Scheduler, int param1Int) {
      super(param1Subscriber, (SimplePlainQueue)new MpscLinkedQueue());
      this.timespan = param1Long;
      this.unit = param1TimeUnit;
      this.scheduler = param1Scheduler;
      this.bufferSize = param1Int;
    }
    
    public void cancel() {
      this.cancelled = true;
    }
    
    void drainLoop() {
      SimplePlainQueue simplePlainQueue = this.queue;
      Subscriber subscriber = this.downstream;
      UnicastProcessor<T> unicastProcessor = this.window;
      int i = 1;
      while (true) {
        Object object1;
        boolean bool1 = this.terminated;
        boolean bool2 = this.done;
        Object object2 = simplePlainQueue.poll();
        if (bool2 && (object2 == null || object2 == NEXT)) {
          this.window = null;
          simplePlainQueue.clear();
          object2 = this.error;
          if (object2 != null) {
            unicastProcessor.onError((Throwable)object2);
          } else {
            unicastProcessor.onComplete();
          } 
          this.timer.dispose();
          return;
        } 
        if (object2 == null) {
          int j = leave(-i);
          i = j;
          if (j == 0)
            return; 
          continue;
        } 
        if (object2 == NEXT) {
          unicastProcessor.onComplete();
          if (!bool1) {
            object2 = UnicastProcessor.create(this.bufferSize);
            this.window = (UnicastProcessor<T>)object2;
            long l = requested();
            if (l != 0L) {
              subscriber.onNext(object2);
              object1 = object2;
              if (l != Long.MAX_VALUE) {
                produced(1L);
                object1 = object2;
              } 
              continue;
            } 
            this.window = null;
            this.queue.clear();
            this.upstream.cancel();
            subscriber.onError((Throwable)new MissingBackpressureException("Could not deliver first window due to lack of requests."));
            this.timer.dispose();
            return;
          } 
          this.upstream.cancel();
          continue;
        } 
        object1.onNext(NotificationLite.getValue(object2));
      } 
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
    
    public void onSubscribe(Subscription param1Subscription) {
      if (SubscriptionHelper.validate(this.upstream, param1Subscription)) {
        SequentialDisposable sequentialDisposable;
        this.upstream = param1Subscription;
        this.window = UnicastProcessor.create(this.bufferSize);
        Subscriber subscriber = this.downstream;
        subscriber.onSubscribe(this);
        long l = requested();
        if (l != 0L) {
          subscriber.onNext(this.window);
          if (l != Long.MAX_VALUE)
            produced(1L); 
          if (!this.cancelled) {
            sequentialDisposable = this.timer;
            Scheduler scheduler = this.scheduler;
            l = this.timespan;
            if (sequentialDisposable.replace(scheduler.schedulePeriodicallyDirect(this, l, l, this.unit)))
              param1Subscription.request(Long.MAX_VALUE); 
          } 
        } else {
          this.cancelled = true;
          param1Subscription.cancel();
          sequentialDisposable.onError((Throwable)new MissingBackpressureException("Could not deliver first window due to lack of requests."));
        } 
      } 
    }
    
    public void request(long param1Long) {
      requested(param1Long);
    }
    
    public void run() {
      if (this.cancelled)
        this.terminated = true; 
      this.queue.offer(NEXT);
      if (enter())
        drainLoop(); 
    }
  }
  
  static final class WindowSkipSubscriber<T> extends QueueDrainSubscriber<T, Object, Flowable<T>> implements Subscription, Runnable {
    final int bufferSize;
    
    volatile boolean terminated;
    
    final long timeskip;
    
    final long timespan;
    
    final TimeUnit unit;
    
    Subscription upstream;
    
    final List<UnicastProcessor<T>> windows;
    
    final Scheduler.Worker worker;
    
    WindowSkipSubscriber(Subscriber<? super Flowable<T>> param1Subscriber, long param1Long1, long param1Long2, TimeUnit param1TimeUnit, Scheduler.Worker param1Worker, int param1Int) {
      super(param1Subscriber, (SimplePlainQueue)new MpscLinkedQueue());
      this.timespan = param1Long1;
      this.timeskip = param1Long2;
      this.unit = param1TimeUnit;
      this.worker = param1Worker;
      this.bufferSize = param1Int;
      this.windows = new LinkedList<UnicastProcessor<T>>();
    }
    
    public void cancel() {
      this.cancelled = true;
    }
    
    void complete(UnicastProcessor<T> param1UnicastProcessor) {
      this.queue.offer(new SubjectWork<T>(param1UnicastProcessor, false));
      if (enter())
        drainLoop(); 
    }
    
    void drainLoop() {
      SimplePlainQueue simplePlainQueue = this.queue;
      Subscriber subscriber = this.downstream;
      List<UnicastProcessor<T>> list = this.windows;
      int i = 1;
      while (true) {
        Iterator<UnicastProcessor<T>> iterator1;
        int j;
        if (this.terminated) {
          this.upstream.cancel();
          simplePlainQueue.clear();
          list.clear();
          this.worker.dispose();
          return;
        } 
        boolean bool1 = this.done;
        Object object = simplePlainQueue.poll();
        if (object == null) {
          j = 1;
        } else {
          j = 0;
        } 
        boolean bool2 = object instanceof SubjectWork;
        if (bool1 && (j || bool2)) {
          simplePlainQueue.clear();
          Throwable throwable = this.error;
          if (throwable != null) {
            iterator1 = list.iterator();
            while (iterator1.hasNext())
              ((UnicastProcessor)iterator1.next()).onError(throwable); 
          } else {
            Iterator<UnicastProcessor<T>> iterator = list.iterator();
            while (iterator.hasNext())
              ((UnicastProcessor)iterator.next()).onComplete(); 
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
          UnicastProcessor<T> unicastProcessor;
          SubjectWork subjectWork = (SubjectWork)object;
          if (subjectWork.open) {
            if (this.cancelled)
              continue; 
            long l = requested();
            if (l != 0L) {
              unicastProcessor = UnicastProcessor.create(this.bufferSize);
              list.add(unicastProcessor);
              iterator1.onNext(unicastProcessor);
              if (l != Long.MAX_VALUE)
                produced(1L); 
              this.worker.schedule(new Completion(unicastProcessor), this.timespan, this.unit);
              continue;
            } 
            iterator1.onError((Throwable)new MissingBackpressureException("Can't emit window due to lack of requests"));
            continue;
          } 
          list.remove(((SubjectWork)unicastProcessor).w);
          ((SubjectWork)unicastProcessor).w.onComplete();
          if (list.isEmpty() && this.cancelled)
            this.terminated = true; 
          continue;
        } 
        Iterator<UnicastProcessor<T>> iterator2 = list.iterator();
        while (iterator2.hasNext())
          ((UnicastProcessor)iterator2.next()).onNext(object); 
      } 
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
        Iterator<UnicastProcessor<T>> iterator = this.windows.iterator();
        while (iterator.hasNext())
          ((UnicastProcessor)iterator.next()).onNext(param1T); 
        if (leave(-1) == 0)
          return; 
      } else {
        this.queue.offer(param1T);
        if (!enter())
          return; 
      } 
      drainLoop();
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      if (SubscriptionHelper.validate(this.upstream, param1Subscription)) {
        this.upstream = param1Subscription;
        this.downstream.onSubscribe(this);
        if (this.cancelled)
          return; 
        long l = requested();
        if (l != 0L) {
          UnicastProcessor<T> unicastProcessor = UnicastProcessor.create(this.bufferSize);
          this.windows.add(unicastProcessor);
          this.downstream.onNext(unicastProcessor);
          if (l != Long.MAX_VALUE)
            produced(1L); 
          this.worker.schedule(new Completion(unicastProcessor), this.timespan, this.unit);
          Scheduler.Worker worker = this.worker;
          l = this.timeskip;
          worker.schedulePeriodically(this, l, l, this.unit);
          param1Subscription.request(Long.MAX_VALUE);
        } else {
          param1Subscription.cancel();
          this.downstream.onError((Throwable)new MissingBackpressureException("Could not emit the first window due to lack of requests"));
        } 
      } 
    }
    
    public void request(long param1Long) {
      requested(param1Long);
    }
    
    public void run() {
      SubjectWork subjectWork = new SubjectWork(UnicastProcessor.create(this.bufferSize), true);
      if (!this.cancelled)
        this.queue.offer(subjectWork); 
      if (enter())
        drainLoop(); 
    }
    
    final class Completion implements Runnable {
      private final UnicastProcessor<T> processor;
      
      Completion(UnicastProcessor<T> param2UnicastProcessor) {
        this.processor = param2UnicastProcessor;
      }
      
      public void run() {
        FlowableWindowTimed.WindowSkipSubscriber.this.complete(this.processor);
      }
    }
    
    static final class SubjectWork<T> {
      final boolean open;
      
      final UnicastProcessor<T> w;
      
      SubjectWork(UnicastProcessor<T> param2UnicastProcessor, boolean param2Boolean) {
        this.w = param2UnicastProcessor;
        this.open = param2Boolean;
      }
    }
  }
  
  final class Completion implements Runnable {
    private final UnicastProcessor<T> processor;
    
    Completion(UnicastProcessor<T> param1UnicastProcessor) {
      this.processor = param1UnicastProcessor;
    }
    
    public void run() {
      this.this$0.complete(this.processor);
    }
  }
  
  static final class SubjectWork<T> {
    final boolean open;
    
    final UnicastProcessor<T> w;
    
    SubjectWork(UnicastProcessor<T> param1UnicastProcessor, boolean param1Boolean) {
      this.w = param1UnicastProcessor;
      this.open = param1Boolean;
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableWindowTimed.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */