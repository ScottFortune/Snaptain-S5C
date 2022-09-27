package io.reactivex.internal.operators.flowable;

import io.reactivex.Emitter;
import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.flowables.ConnectableFlowable;
import io.reactivex.functions.Action;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.internal.functions.Functions;
import io.reactivex.internal.functions.ObjectHelper;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableInternalHelper {
  private FlowableInternalHelper() {
    throw new IllegalStateException("No instances!");
  }
  
  public static <T, U> Function<T, Publisher<U>> flatMapIntoIterable(Function<? super T, ? extends Iterable<? extends U>> paramFunction) {
    return new FlatMapIntoIterable<T, U>(paramFunction);
  }
  
  public static <T, U, R> Function<T, Publisher<R>> flatMapWithCombiner(Function<? super T, ? extends Publisher<? extends U>> paramFunction, BiFunction<? super T, ? super U, ? extends R> paramBiFunction) {
    return new FlatMapWithCombinerOuter<T, R, U>(paramBiFunction, paramFunction);
  }
  
  public static <T, U> Function<T, Publisher<T>> itemDelay(Function<? super T, ? extends Publisher<U>> paramFunction) {
    return new ItemDelayFunction<T, U>(paramFunction);
  }
  
  public static <T> Callable<ConnectableFlowable<T>> replayCallable(Flowable<T> paramFlowable) {
    return new ReplayCallable<T>(paramFlowable);
  }
  
  public static <T> Callable<ConnectableFlowable<T>> replayCallable(Flowable<T> paramFlowable, int paramInt) {
    return new BufferedReplayCallable<T>(paramFlowable, paramInt);
  }
  
  public static <T> Callable<ConnectableFlowable<T>> replayCallable(Flowable<T> paramFlowable, int paramInt, long paramLong, TimeUnit paramTimeUnit, Scheduler paramScheduler) {
    return new BufferedTimedReplay<T>(paramFlowable, paramInt, paramLong, paramTimeUnit, paramScheduler);
  }
  
  public static <T> Callable<ConnectableFlowable<T>> replayCallable(Flowable<T> paramFlowable, long paramLong, TimeUnit paramTimeUnit, Scheduler paramScheduler) {
    return new TimedReplay<T>(paramFlowable, paramLong, paramTimeUnit, paramScheduler);
  }
  
  public static <T, R> Function<Flowable<T>, Publisher<R>> replayFunction(Function<? super Flowable<T>, ? extends Publisher<R>> paramFunction, Scheduler paramScheduler) {
    return new ReplayFunction<T, R>(paramFunction, paramScheduler);
  }
  
  public static <T, S> BiFunction<S, Emitter<T>, S> simpleBiGenerator(BiConsumer<S, Emitter<T>> paramBiConsumer) {
    return new SimpleBiGenerator<T, S>(paramBiConsumer);
  }
  
  public static <T, S> BiFunction<S, Emitter<T>, S> simpleGenerator(Consumer<Emitter<T>> paramConsumer) {
    return new SimpleGenerator<T, S>(paramConsumer);
  }
  
  public static <T> Action subscriberOnComplete(Subscriber<T> paramSubscriber) {
    return new SubscriberOnComplete<T>(paramSubscriber);
  }
  
  public static <T> Consumer<Throwable> subscriberOnError(Subscriber<T> paramSubscriber) {
    return new SubscriberOnError<T>(paramSubscriber);
  }
  
  public static <T> Consumer<T> subscriberOnNext(Subscriber<T> paramSubscriber) {
    return new SubscriberOnNext<T>(paramSubscriber);
  }
  
  public static <T, R> Function<List<Publisher<? extends T>>, Publisher<? extends R>> zipIterable(Function<? super Object[], ? extends R> paramFunction) {
    return new ZipIterableFunction<T, R>(paramFunction);
  }
  
  static final class BufferedReplayCallable<T> implements Callable<ConnectableFlowable<T>> {
    private final int bufferSize;
    
    private final Flowable<T> parent;
    
    BufferedReplayCallable(Flowable<T> param1Flowable, int param1Int) {
      this.parent = param1Flowable;
      this.bufferSize = param1Int;
    }
    
    public ConnectableFlowable<T> call() {
      return this.parent.replay(this.bufferSize);
    }
  }
  
  static final class BufferedTimedReplay<T> implements Callable<ConnectableFlowable<T>> {
    private final int bufferSize;
    
    private final Flowable<T> parent;
    
    private final Scheduler scheduler;
    
    private final long time;
    
    private final TimeUnit unit;
    
    BufferedTimedReplay(Flowable<T> param1Flowable, int param1Int, long param1Long, TimeUnit param1TimeUnit, Scheduler param1Scheduler) {
      this.parent = param1Flowable;
      this.bufferSize = param1Int;
      this.time = param1Long;
      this.unit = param1TimeUnit;
      this.scheduler = param1Scheduler;
    }
    
    public ConnectableFlowable<T> call() {
      return this.parent.replay(this.bufferSize, this.time, this.unit, this.scheduler);
    }
  }
  
  static final class FlatMapIntoIterable<T, U> implements Function<T, Publisher<U>> {
    private final Function<? super T, ? extends Iterable<? extends U>> mapper;
    
    FlatMapIntoIterable(Function<? super T, ? extends Iterable<? extends U>> param1Function) {
      this.mapper = param1Function;
    }
    
    public Publisher<U> apply(T param1T) throws Exception {
      return (Publisher<U>)new FlowableFromIterable((Iterable)ObjectHelper.requireNonNull(this.mapper.apply(param1T), "The mapper returned a null Iterable"));
    }
  }
  
  static final class FlatMapWithCombinerInner<U, R, T> implements Function<U, R> {
    private final BiFunction<? super T, ? super U, ? extends R> combiner;
    
    private final T t;
    
    FlatMapWithCombinerInner(BiFunction<? super T, ? super U, ? extends R> param1BiFunction, T param1T) {
      this.combiner = param1BiFunction;
      this.t = param1T;
    }
    
    public R apply(U param1U) throws Exception {
      return (R)this.combiner.apply(this.t, param1U);
    }
  }
  
  static final class FlatMapWithCombinerOuter<T, R, U> implements Function<T, Publisher<R>> {
    private final BiFunction<? super T, ? super U, ? extends R> combiner;
    
    private final Function<? super T, ? extends Publisher<? extends U>> mapper;
    
    FlatMapWithCombinerOuter(BiFunction<? super T, ? super U, ? extends R> param1BiFunction, Function<? super T, ? extends Publisher<? extends U>> param1Function) {
      this.combiner = param1BiFunction;
      this.mapper = param1Function;
    }
    
    public Publisher<R> apply(T param1T) throws Exception {
      return (Publisher)new FlowableMapPublisher<Object, Object>((Publisher)ObjectHelper.requireNonNull(this.mapper.apply(param1T), "The mapper returned a null Publisher"), new FlowableInternalHelper.FlatMapWithCombinerInner<Object, Object, T>(this.combiner, param1T));
    }
  }
  
  static final class ItemDelayFunction<T, U> implements Function<T, Publisher<T>> {
    final Function<? super T, ? extends Publisher<U>> itemDelay;
    
    ItemDelayFunction(Function<? super T, ? extends Publisher<U>> param1Function) {
      this.itemDelay = param1Function;
    }
    
    public Publisher<T> apply(T param1T) throws Exception {
      return (Publisher<T>)(new FlowableTakePublisher((Publisher)ObjectHelper.requireNonNull(this.itemDelay.apply(param1T), "The itemDelay returned a null Publisher"), 1L)).map(Functions.justFunction(param1T)).defaultIfEmpty(param1T);
    }
  }
  
  static final class ReplayCallable<T> implements Callable<ConnectableFlowable<T>> {
    private final Flowable<T> parent;
    
    ReplayCallable(Flowable<T> param1Flowable) {
      this.parent = param1Flowable;
    }
    
    public ConnectableFlowable<T> call() {
      return this.parent.replay();
    }
  }
  
  static final class ReplayFunction<T, R> implements Function<Flowable<T>, Publisher<R>> {
    private final Scheduler scheduler;
    
    private final Function<? super Flowable<T>, ? extends Publisher<R>> selector;
    
    ReplayFunction(Function<? super Flowable<T>, ? extends Publisher<R>> param1Function, Scheduler param1Scheduler) {
      this.selector = param1Function;
      this.scheduler = param1Scheduler;
    }
    
    public Publisher<R> apply(Flowable<T> param1Flowable) throws Exception {
      return (Publisher<R>)Flowable.fromPublisher((Publisher)ObjectHelper.requireNonNull(this.selector.apply(param1Flowable), "The selector returned a null Publisher")).observeOn(this.scheduler);
    }
  }
  
  public enum RequestMax implements Consumer<Subscription> {
    INSTANCE;
    
    static {
    
    }
    
    public void accept(Subscription param1Subscription) throws Exception {
      param1Subscription.request(Long.MAX_VALUE);
    }
  }
  
  static final class SimpleBiGenerator<T, S> implements BiFunction<S, Emitter<T>, S> {
    final BiConsumer<S, Emitter<T>> consumer;
    
    SimpleBiGenerator(BiConsumer<S, Emitter<T>> param1BiConsumer) {
      this.consumer = param1BiConsumer;
    }
    
    public S apply(S param1S, Emitter<T> param1Emitter) throws Exception {
      this.consumer.accept(param1S, param1Emitter);
      return param1S;
    }
  }
  
  static final class SimpleGenerator<T, S> implements BiFunction<S, Emitter<T>, S> {
    final Consumer<Emitter<T>> consumer;
    
    SimpleGenerator(Consumer<Emitter<T>> param1Consumer) {
      this.consumer = param1Consumer;
    }
    
    public S apply(S param1S, Emitter<T> param1Emitter) throws Exception {
      this.consumer.accept(param1Emitter);
      return param1S;
    }
  }
  
  static final class SubscriberOnComplete<T> implements Action {
    final Subscriber<T> subscriber;
    
    SubscriberOnComplete(Subscriber<T> param1Subscriber) {
      this.subscriber = param1Subscriber;
    }
    
    public void run() throws Exception {
      this.subscriber.onComplete();
    }
  }
  
  static final class SubscriberOnError<T> implements Consumer<Throwable> {
    final Subscriber<T> subscriber;
    
    SubscriberOnError(Subscriber<T> param1Subscriber) {
      this.subscriber = param1Subscriber;
    }
    
    public void accept(Throwable param1Throwable) throws Exception {
      this.subscriber.onError(param1Throwable);
    }
  }
  
  static final class SubscriberOnNext<T> implements Consumer<T> {
    final Subscriber<T> subscriber;
    
    SubscriberOnNext(Subscriber<T> param1Subscriber) {
      this.subscriber = param1Subscriber;
    }
    
    public void accept(T param1T) throws Exception {
      this.subscriber.onNext(param1T);
    }
  }
  
  static final class TimedReplay<T> implements Callable<ConnectableFlowable<T>> {
    private final Flowable<T> parent;
    
    private final Scheduler scheduler;
    
    private final long time;
    
    private final TimeUnit unit;
    
    TimedReplay(Flowable<T> param1Flowable, long param1Long, TimeUnit param1TimeUnit, Scheduler param1Scheduler) {
      this.parent = param1Flowable;
      this.time = param1Long;
      this.unit = param1TimeUnit;
      this.scheduler = param1Scheduler;
    }
    
    public ConnectableFlowable<T> call() {
      return this.parent.replay(this.time, this.unit, this.scheduler);
    }
  }
  
  static final class ZipIterableFunction<T, R> implements Function<List<Publisher<? extends T>>, Publisher<? extends R>> {
    private final Function<? super Object[], ? extends R> zipper;
    
    ZipIterableFunction(Function<? super Object[], ? extends R> param1Function) {
      this.zipper = param1Function;
    }
    
    public Publisher<? extends R> apply(List<Publisher<? extends T>> param1List) {
      return (Publisher<? extends R>)Flowable.zipIterable(param1List, this.zipper, false, Flowable.bufferSize());
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableInternalHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */