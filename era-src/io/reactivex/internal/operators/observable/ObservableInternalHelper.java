package io.reactivex.internal.operators.observable;

import io.reactivex.Emitter;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.functions.Action;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.internal.functions.Functions;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.observables.ConnectableObservable;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public final class ObservableInternalHelper {
  private ObservableInternalHelper() {
    throw new IllegalStateException("No instances!");
  }
  
  public static <T, U> Function<T, ObservableSource<U>> flatMapIntoIterable(Function<? super T, ? extends Iterable<? extends U>> paramFunction) {
    return new FlatMapIntoIterable<T, U>(paramFunction);
  }
  
  public static <T, U, R> Function<T, ObservableSource<R>> flatMapWithCombiner(Function<? super T, ? extends ObservableSource<? extends U>> paramFunction, BiFunction<? super T, ? super U, ? extends R> paramBiFunction) {
    return new FlatMapWithCombinerOuter<T, R, U>(paramBiFunction, paramFunction);
  }
  
  public static <T, U> Function<T, ObservableSource<T>> itemDelay(Function<? super T, ? extends ObservableSource<U>> paramFunction) {
    return new ItemDelayFunction<T, U>(paramFunction);
  }
  
  public static <T> Action observerOnComplete(Observer<T> paramObserver) {
    return new ObserverOnComplete<T>(paramObserver);
  }
  
  public static <T> Consumer<Throwable> observerOnError(Observer<T> paramObserver) {
    return new ObserverOnError<T>(paramObserver);
  }
  
  public static <T> Consumer<T> observerOnNext(Observer<T> paramObserver) {
    return new ObserverOnNext<T>(paramObserver);
  }
  
  public static <T> Callable<ConnectableObservable<T>> replayCallable(Observable<T> paramObservable) {
    return new ReplayCallable<T>(paramObservable);
  }
  
  public static <T> Callable<ConnectableObservable<T>> replayCallable(Observable<T> paramObservable, int paramInt) {
    return new BufferedReplayCallable<T>(paramObservable, paramInt);
  }
  
  public static <T> Callable<ConnectableObservable<T>> replayCallable(Observable<T> paramObservable, int paramInt, long paramLong, TimeUnit paramTimeUnit, Scheduler paramScheduler) {
    return new BufferedTimedReplayCallable<T>(paramObservable, paramInt, paramLong, paramTimeUnit, paramScheduler);
  }
  
  public static <T> Callable<ConnectableObservable<T>> replayCallable(Observable<T> paramObservable, long paramLong, TimeUnit paramTimeUnit, Scheduler paramScheduler) {
    return new TimedReplayCallable<T>(paramObservable, paramLong, paramTimeUnit, paramScheduler);
  }
  
  public static <T, R> Function<Observable<T>, ObservableSource<R>> replayFunction(Function<? super Observable<T>, ? extends ObservableSource<R>> paramFunction, Scheduler paramScheduler) {
    return new ReplayFunction<T, R>(paramFunction, paramScheduler);
  }
  
  public static <T, S> BiFunction<S, Emitter<T>, S> simpleBiGenerator(BiConsumer<S, Emitter<T>> paramBiConsumer) {
    return new SimpleBiGenerator<T, S>(paramBiConsumer);
  }
  
  public static <T, S> BiFunction<S, Emitter<T>, S> simpleGenerator(Consumer<Emitter<T>> paramConsumer) {
    return new SimpleGenerator<T, S>(paramConsumer);
  }
  
  public static <T, R> Function<List<ObservableSource<? extends T>>, ObservableSource<? extends R>> zipIterable(Function<? super Object[], ? extends R> paramFunction) {
    return new ZipIterableFunction<T, R>(paramFunction);
  }
  
  static final class BufferedReplayCallable<T> implements Callable<ConnectableObservable<T>> {
    private final int bufferSize;
    
    private final Observable<T> parent;
    
    BufferedReplayCallable(Observable<T> param1Observable, int param1Int) {
      this.parent = param1Observable;
      this.bufferSize = param1Int;
    }
    
    public ConnectableObservable<T> call() {
      return this.parent.replay(this.bufferSize);
    }
  }
  
  static final class BufferedTimedReplayCallable<T> implements Callable<ConnectableObservable<T>> {
    private final int bufferSize;
    
    private final Observable<T> parent;
    
    private final Scheduler scheduler;
    
    private final long time;
    
    private final TimeUnit unit;
    
    BufferedTimedReplayCallable(Observable<T> param1Observable, int param1Int, long param1Long, TimeUnit param1TimeUnit, Scheduler param1Scheduler) {
      this.parent = param1Observable;
      this.bufferSize = param1Int;
      this.time = param1Long;
      this.unit = param1TimeUnit;
      this.scheduler = param1Scheduler;
    }
    
    public ConnectableObservable<T> call() {
      return this.parent.replay(this.bufferSize, this.time, this.unit, this.scheduler);
    }
  }
  
  static final class FlatMapIntoIterable<T, U> implements Function<T, ObservableSource<U>> {
    private final Function<? super T, ? extends Iterable<? extends U>> mapper;
    
    FlatMapIntoIterable(Function<? super T, ? extends Iterable<? extends U>> param1Function) {
      this.mapper = param1Function;
    }
    
    public ObservableSource<U> apply(T param1T) throws Exception {
      return (ObservableSource<U>)new ObservableFromIterable((Iterable)ObjectHelper.requireNonNull(this.mapper.apply(param1T), "The mapper returned a null Iterable"));
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
  
  static final class FlatMapWithCombinerOuter<T, R, U> implements Function<T, ObservableSource<R>> {
    private final BiFunction<? super T, ? super U, ? extends R> combiner;
    
    private final Function<? super T, ? extends ObservableSource<? extends U>> mapper;
    
    FlatMapWithCombinerOuter(BiFunction<? super T, ? super U, ? extends R> param1BiFunction, Function<? super T, ? extends ObservableSource<? extends U>> param1Function) {
      this.combiner = param1BiFunction;
      this.mapper = param1Function;
    }
    
    public ObservableSource<R> apply(T param1T) throws Exception {
      return (ObservableSource)new ObservableMap<Object, Object>((ObservableSource)ObjectHelper.requireNonNull(this.mapper.apply(param1T), "The mapper returned a null ObservableSource"), new ObservableInternalHelper.FlatMapWithCombinerInner<Object, Object, T>(this.combiner, param1T));
    }
  }
  
  static final class ItemDelayFunction<T, U> implements Function<T, ObservableSource<T>> {
    final Function<? super T, ? extends ObservableSource<U>> itemDelay;
    
    ItemDelayFunction(Function<? super T, ? extends ObservableSource<U>> param1Function) {
      this.itemDelay = param1Function;
    }
    
    public ObservableSource<T> apply(T param1T) throws Exception {
      return (ObservableSource<T>)(new ObservableTake((ObservableSource)ObjectHelper.requireNonNull(this.itemDelay.apply(param1T), "The itemDelay returned a null ObservableSource"), 1L)).map(Functions.justFunction(param1T)).defaultIfEmpty(param1T);
    }
  }
  
  enum MapToInt implements Function<Object, Object> {
    INSTANCE;
    
    static {
    
    }
    
    public Object apply(Object param1Object) throws Exception {
      return Integer.valueOf(0);
    }
  }
  
  static final class ObserverOnComplete<T> implements Action {
    final Observer<T> observer;
    
    ObserverOnComplete(Observer<T> param1Observer) {
      this.observer = param1Observer;
    }
    
    public void run() throws Exception {
      this.observer.onComplete();
    }
  }
  
  static final class ObserverOnError<T> implements Consumer<Throwable> {
    final Observer<T> observer;
    
    ObserverOnError(Observer<T> param1Observer) {
      this.observer = param1Observer;
    }
    
    public void accept(Throwable param1Throwable) throws Exception {
      this.observer.onError(param1Throwable);
    }
  }
  
  static final class ObserverOnNext<T> implements Consumer<T> {
    final Observer<T> observer;
    
    ObserverOnNext(Observer<T> param1Observer) {
      this.observer = param1Observer;
    }
    
    public void accept(T param1T) throws Exception {
      this.observer.onNext(param1T);
    }
  }
  
  static final class ReplayCallable<T> implements Callable<ConnectableObservable<T>> {
    private final Observable<T> parent;
    
    ReplayCallable(Observable<T> param1Observable) {
      this.parent = param1Observable;
    }
    
    public ConnectableObservable<T> call() {
      return this.parent.replay();
    }
  }
  
  static final class ReplayFunction<T, R> implements Function<Observable<T>, ObservableSource<R>> {
    private final Scheduler scheduler;
    
    private final Function<? super Observable<T>, ? extends ObservableSource<R>> selector;
    
    ReplayFunction(Function<? super Observable<T>, ? extends ObservableSource<R>> param1Function, Scheduler param1Scheduler) {
      this.selector = param1Function;
      this.scheduler = param1Scheduler;
    }
    
    public ObservableSource<R> apply(Observable<T> param1Observable) throws Exception {
      return (ObservableSource<R>)Observable.wrap((ObservableSource)ObjectHelper.requireNonNull(this.selector.apply(param1Observable), "The selector returned a null ObservableSource")).observeOn(this.scheduler);
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
  
  static final class TimedReplayCallable<T> implements Callable<ConnectableObservable<T>> {
    private final Observable<T> parent;
    
    private final Scheduler scheduler;
    
    private final long time;
    
    private final TimeUnit unit;
    
    TimedReplayCallable(Observable<T> param1Observable, long param1Long, TimeUnit param1TimeUnit, Scheduler param1Scheduler) {
      this.parent = param1Observable;
      this.time = param1Long;
      this.unit = param1TimeUnit;
      this.scheduler = param1Scheduler;
    }
    
    public ConnectableObservable<T> call() {
      return this.parent.replay(this.time, this.unit, this.scheduler);
    }
  }
  
  static final class ZipIterableFunction<T, R> implements Function<List<ObservableSource<? extends T>>, ObservableSource<? extends R>> {
    private final Function<? super Object[], ? extends R> zipper;
    
    ZipIterableFunction(Function<? super Object[], ? extends R> param1Function) {
      this.zipper = param1Function;
    }
    
    public ObservableSource<? extends R> apply(List<ObservableSource<? extends T>> param1List) {
      return (ObservableSource<? extends R>)Observable.zipIterable(param1List, this.zipper, false, Observable.bufferSize());
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableInternalHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */