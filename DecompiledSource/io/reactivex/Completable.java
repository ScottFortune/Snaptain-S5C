package io.reactivex;

import io.reactivex.annotations.BackpressureKind;
import io.reactivex.annotations.BackpressureSupport;
import io.reactivex.annotations.CheckReturnValue;
import io.reactivex.annotations.SchedulerSupport;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Action;
import io.reactivex.functions.BiPredicate;
import io.reactivex.functions.BooleanSupplier;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.internal.functions.Functions;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.fuseable.FuseToFlowable;
import io.reactivex.internal.fuseable.FuseToMaybe;
import io.reactivex.internal.fuseable.FuseToObservable;
import io.reactivex.internal.observers.BlockingMultiObserver;
import io.reactivex.internal.observers.CallbackCompletableObserver;
import io.reactivex.internal.observers.EmptyCompletableObserver;
import io.reactivex.internal.operators.completable.CompletableAmb;
import io.reactivex.internal.operators.completable.CompletableAndThenCompletable;
import io.reactivex.internal.operators.completable.CompletableCache;
import io.reactivex.internal.operators.completable.CompletableConcat;
import io.reactivex.internal.operators.completable.CompletableConcatArray;
import io.reactivex.internal.operators.completable.CompletableConcatIterable;
import io.reactivex.internal.operators.completable.CompletableCreate;
import io.reactivex.internal.operators.completable.CompletableDefer;
import io.reactivex.internal.operators.completable.CompletableDelay;
import io.reactivex.internal.operators.completable.CompletableDetach;
import io.reactivex.internal.operators.completable.CompletableDisposeOn;
import io.reactivex.internal.operators.completable.CompletableDoFinally;
import io.reactivex.internal.operators.completable.CompletableDoOnEvent;
import io.reactivex.internal.operators.completable.CompletableEmpty;
import io.reactivex.internal.operators.completable.CompletableError;
import io.reactivex.internal.operators.completable.CompletableErrorSupplier;
import io.reactivex.internal.operators.completable.CompletableFromAction;
import io.reactivex.internal.operators.completable.CompletableFromCallable;
import io.reactivex.internal.operators.completable.CompletableFromObservable;
import io.reactivex.internal.operators.completable.CompletableFromPublisher;
import io.reactivex.internal.operators.completable.CompletableFromRunnable;
import io.reactivex.internal.operators.completable.CompletableFromSingle;
import io.reactivex.internal.operators.completable.CompletableFromUnsafeSource;
import io.reactivex.internal.operators.completable.CompletableHide;
import io.reactivex.internal.operators.completable.CompletableLift;
import io.reactivex.internal.operators.completable.CompletableMaterialize;
import io.reactivex.internal.operators.completable.CompletableMerge;
import io.reactivex.internal.operators.completable.CompletableMergeArray;
import io.reactivex.internal.operators.completable.CompletableMergeDelayErrorArray;
import io.reactivex.internal.operators.completable.CompletableMergeDelayErrorIterable;
import io.reactivex.internal.operators.completable.CompletableMergeIterable;
import io.reactivex.internal.operators.completable.CompletableNever;
import io.reactivex.internal.operators.completable.CompletableObserveOn;
import io.reactivex.internal.operators.completable.CompletableOnErrorComplete;
import io.reactivex.internal.operators.completable.CompletablePeek;
import io.reactivex.internal.operators.completable.CompletableResumeNext;
import io.reactivex.internal.operators.completable.CompletableSubscribeOn;
import io.reactivex.internal.operators.completable.CompletableTakeUntilCompletable;
import io.reactivex.internal.operators.completable.CompletableTimeout;
import io.reactivex.internal.operators.completable.CompletableTimer;
import io.reactivex.internal.operators.completable.CompletableToFlowable;
import io.reactivex.internal.operators.completable.CompletableToObservable;
import io.reactivex.internal.operators.completable.CompletableToSingle;
import io.reactivex.internal.operators.completable.CompletableUsing;
import io.reactivex.internal.operators.maybe.MaybeDelayWithCompletable;
import io.reactivex.internal.operators.maybe.MaybeFromCompletable;
import io.reactivex.internal.operators.maybe.MaybeIgnoreElementCompletable;
import io.reactivex.internal.operators.mixed.CompletableAndThenObservable;
import io.reactivex.internal.operators.mixed.CompletableAndThenPublisher;
import io.reactivex.internal.operators.single.SingleDelayWithCompletable;
import io.reactivex.observers.TestObserver;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.reactivestreams.Publisher;

public abstract class Completable implements CompletableSource {
  @CheckReturnValue
  @SchedulerSupport("none")
  public static Completable amb(Iterable<? extends CompletableSource> paramIterable) {
    ObjectHelper.requireNonNull(paramIterable, "sources is null");
    return RxJavaPlugins.onAssembly((Completable)new CompletableAmb(null, paramIterable));
  }
  
  @CheckReturnValue
  @SchedulerSupport("none")
  public static Completable ambArray(CompletableSource... paramVarArgs) {
    ObjectHelper.requireNonNull(paramVarArgs, "sources is null");
    return (paramVarArgs.length == 0) ? complete() : ((paramVarArgs.length == 1) ? wrap(paramVarArgs[0]) : RxJavaPlugins.onAssembly((Completable)new CompletableAmb(paramVarArgs, null)));
  }
  
  @CheckReturnValue
  @SchedulerSupport("none")
  public static Completable complete() {
    return RxJavaPlugins.onAssembly(CompletableEmpty.INSTANCE);
  }
  
  @CheckReturnValue
  @SchedulerSupport("none")
  public static Completable concat(Iterable<? extends CompletableSource> paramIterable) {
    ObjectHelper.requireNonNull(paramIterable, "sources is null");
    return RxJavaPlugins.onAssembly((Completable)new CompletableConcatIterable(paramIterable));
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static Completable concat(Publisher<? extends CompletableSource> paramPublisher) {
    return concat(paramPublisher, 2);
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static Completable concat(Publisher<? extends CompletableSource> paramPublisher, int paramInt) {
    ObjectHelper.requireNonNull(paramPublisher, "sources is null");
    ObjectHelper.verifyPositive(paramInt, "prefetch");
    return RxJavaPlugins.onAssembly((Completable)new CompletableConcat(paramPublisher, paramInt));
  }
  
  @CheckReturnValue
  @SchedulerSupport("none")
  public static Completable concatArray(CompletableSource... paramVarArgs) {
    ObjectHelper.requireNonNull(paramVarArgs, "sources is null");
    return (paramVarArgs.length == 0) ? complete() : ((paramVarArgs.length == 1) ? wrap(paramVarArgs[0]) : RxJavaPlugins.onAssembly((Completable)new CompletableConcatArray(paramVarArgs)));
  }
  
  @CheckReturnValue
  @SchedulerSupport("none")
  public static Completable create(CompletableOnSubscribe paramCompletableOnSubscribe) {
    ObjectHelper.requireNonNull(paramCompletableOnSubscribe, "source is null");
    return RxJavaPlugins.onAssembly((Completable)new CompletableCreate(paramCompletableOnSubscribe));
  }
  
  @CheckReturnValue
  @SchedulerSupport("none")
  public static Completable defer(Callable<? extends CompletableSource> paramCallable) {
    ObjectHelper.requireNonNull(paramCallable, "completableSupplier");
    return RxJavaPlugins.onAssembly((Completable)new CompletableDefer(paramCallable));
  }
  
  @CheckReturnValue
  @SchedulerSupport("none")
  private Completable doOnLifecycle(Consumer<? super Disposable> paramConsumer, Consumer<? super Throwable> paramConsumer1, Action paramAction1, Action paramAction2, Action paramAction3, Action paramAction4) {
    ObjectHelper.requireNonNull(paramConsumer, "onSubscribe is null");
    ObjectHelper.requireNonNull(paramConsumer1, "onError is null");
    ObjectHelper.requireNonNull(paramAction1, "onComplete is null");
    ObjectHelper.requireNonNull(paramAction2, "onTerminate is null");
    ObjectHelper.requireNonNull(paramAction3, "onAfterTerminate is null");
    ObjectHelper.requireNonNull(paramAction4, "onDispose is null");
    return RxJavaPlugins.onAssembly((Completable)new CompletablePeek(this, paramConsumer, paramConsumer1, paramAction1, paramAction2, paramAction3, paramAction4));
  }
  
  @CheckReturnValue
  @SchedulerSupport("none")
  public static Completable error(Throwable paramThrowable) {
    ObjectHelper.requireNonNull(paramThrowable, "error is null");
    return RxJavaPlugins.onAssembly((Completable)new CompletableError(paramThrowable));
  }
  
  @CheckReturnValue
  @SchedulerSupport("none")
  public static Completable error(Callable<? extends Throwable> paramCallable) {
    ObjectHelper.requireNonNull(paramCallable, "errorSupplier is null");
    return RxJavaPlugins.onAssembly((Completable)new CompletableErrorSupplier(paramCallable));
  }
  
  @CheckReturnValue
  @SchedulerSupport("none")
  public static Completable fromAction(Action paramAction) {
    ObjectHelper.requireNonNull(paramAction, "run is null");
    return RxJavaPlugins.onAssembly((Completable)new CompletableFromAction(paramAction));
  }
  
  @CheckReturnValue
  @SchedulerSupport("none")
  public static Completable fromCallable(Callable<?> paramCallable) {
    ObjectHelper.requireNonNull(paramCallable, "callable is null");
    return RxJavaPlugins.onAssembly((Completable)new CompletableFromCallable(paramCallable));
  }
  
  @CheckReturnValue
  @SchedulerSupport("none")
  public static Completable fromFuture(Future<?> paramFuture) {
    ObjectHelper.requireNonNull(paramFuture, "future is null");
    return fromAction(Functions.futureAction(paramFuture));
  }
  
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T> Completable fromMaybe(MaybeSource<T> paramMaybeSource) {
    ObjectHelper.requireNonNull(paramMaybeSource, "maybe is null");
    return RxJavaPlugins.onAssembly((Completable)new MaybeIgnoreElementCompletable(paramMaybeSource));
  }
  
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T> Completable fromObservable(ObservableSource<T> paramObservableSource) {
    ObjectHelper.requireNonNull(paramObservableSource, "observable is null");
    return RxJavaPlugins.onAssembly((Completable)new CompletableFromObservable(paramObservableSource));
  }
  
  @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T> Completable fromPublisher(Publisher<T> paramPublisher) {
    ObjectHelper.requireNonNull(paramPublisher, "publisher is null");
    return RxJavaPlugins.onAssembly((Completable)new CompletableFromPublisher(paramPublisher));
  }
  
  @CheckReturnValue
  @SchedulerSupport("none")
  public static Completable fromRunnable(Runnable paramRunnable) {
    ObjectHelper.requireNonNull(paramRunnable, "run is null");
    return RxJavaPlugins.onAssembly((Completable)new CompletableFromRunnable(paramRunnable));
  }
  
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T> Completable fromSingle(SingleSource<T> paramSingleSource) {
    ObjectHelper.requireNonNull(paramSingleSource, "single is null");
    return RxJavaPlugins.onAssembly((Completable)new CompletableFromSingle(paramSingleSource));
  }
  
  @CheckReturnValue
  @SchedulerSupport("none")
  public static Completable merge(Iterable<? extends CompletableSource> paramIterable) {
    ObjectHelper.requireNonNull(paramIterable, "sources is null");
    return RxJavaPlugins.onAssembly((Completable)new CompletableMergeIterable(paramIterable));
  }
  
  @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static Completable merge(Publisher<? extends CompletableSource> paramPublisher) {
    return merge0(paramPublisher, 2147483647, false);
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static Completable merge(Publisher<? extends CompletableSource> paramPublisher, int paramInt) {
    return merge0(paramPublisher, paramInt, false);
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  private static Completable merge0(Publisher<? extends CompletableSource> paramPublisher, int paramInt, boolean paramBoolean) {
    ObjectHelper.requireNonNull(paramPublisher, "sources is null");
    ObjectHelper.verifyPositive(paramInt, "maxConcurrency");
    return RxJavaPlugins.onAssembly((Completable)new CompletableMerge(paramPublisher, paramInt, paramBoolean));
  }
  
  @CheckReturnValue
  @SchedulerSupport("none")
  public static Completable mergeArray(CompletableSource... paramVarArgs) {
    ObjectHelper.requireNonNull(paramVarArgs, "sources is null");
    return (paramVarArgs.length == 0) ? complete() : ((paramVarArgs.length == 1) ? wrap(paramVarArgs[0]) : RxJavaPlugins.onAssembly((Completable)new CompletableMergeArray(paramVarArgs)));
  }
  
  @CheckReturnValue
  @SchedulerSupport("none")
  public static Completable mergeArrayDelayError(CompletableSource... paramVarArgs) {
    ObjectHelper.requireNonNull(paramVarArgs, "sources is null");
    return RxJavaPlugins.onAssembly((Completable)new CompletableMergeDelayErrorArray(paramVarArgs));
  }
  
  @CheckReturnValue
  @SchedulerSupport("none")
  public static Completable mergeDelayError(Iterable<? extends CompletableSource> paramIterable) {
    ObjectHelper.requireNonNull(paramIterable, "sources is null");
    return RxJavaPlugins.onAssembly((Completable)new CompletableMergeDelayErrorIterable(paramIterable));
  }
  
  @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static Completable mergeDelayError(Publisher<? extends CompletableSource> paramPublisher) {
    return merge0(paramPublisher, 2147483647, true);
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static Completable mergeDelayError(Publisher<? extends CompletableSource> paramPublisher, int paramInt) {
    return merge0(paramPublisher, paramInt, true);
  }
  
  @CheckReturnValue
  @SchedulerSupport("none")
  public static Completable never() {
    return RxJavaPlugins.onAssembly(CompletableNever.INSTANCE);
  }
  
  @CheckReturnValue
  @SchedulerSupport("custom")
  private Completable timeout0(long paramLong, TimeUnit paramTimeUnit, Scheduler paramScheduler, CompletableSource paramCompletableSource) {
    ObjectHelper.requireNonNull(paramTimeUnit, "unit is null");
    ObjectHelper.requireNonNull(paramScheduler, "scheduler is null");
    return RxJavaPlugins.onAssembly((Completable)new CompletableTimeout(this, paramLong, paramTimeUnit, paramScheduler, paramCompletableSource));
  }
  
  @CheckReturnValue
  @SchedulerSupport("io.reactivex:computation")
  public static Completable timer(long paramLong, TimeUnit paramTimeUnit) {
    return timer(paramLong, paramTimeUnit, Schedulers.computation());
  }
  
  @CheckReturnValue
  @SchedulerSupport("custom")
  public static Completable timer(long paramLong, TimeUnit paramTimeUnit, Scheduler paramScheduler) {
    ObjectHelper.requireNonNull(paramTimeUnit, "unit is null");
    ObjectHelper.requireNonNull(paramScheduler, "scheduler is null");
    return RxJavaPlugins.onAssembly((Completable)new CompletableTimer(paramLong, paramTimeUnit, paramScheduler));
  }
  
  private static NullPointerException toNpe(Throwable paramThrowable) {
    NullPointerException nullPointerException = new NullPointerException("Actually not, but can't pass out an exception otherwise...");
    nullPointerException.initCause(paramThrowable);
    return nullPointerException;
  }
  
  @CheckReturnValue
  @SchedulerSupport("none")
  public static Completable unsafeCreate(CompletableSource paramCompletableSource) {
    ObjectHelper.requireNonNull(paramCompletableSource, "source is null");
    if (!(paramCompletableSource instanceof Completable))
      return RxJavaPlugins.onAssembly((Completable)new CompletableFromUnsafeSource(paramCompletableSource)); 
    throw new IllegalArgumentException("Use of unsafeCreate(Completable)!");
  }
  
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <R> Completable using(Callable<R> paramCallable, Function<? super R, ? extends CompletableSource> paramFunction, Consumer<? super R> paramConsumer) {
    return using(paramCallable, paramFunction, paramConsumer, true);
  }
  
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <R> Completable using(Callable<R> paramCallable, Function<? super R, ? extends CompletableSource> paramFunction, Consumer<? super R> paramConsumer, boolean paramBoolean) {
    ObjectHelper.requireNonNull(paramCallable, "resourceSupplier is null");
    ObjectHelper.requireNonNull(paramFunction, "completableFunction is null");
    ObjectHelper.requireNonNull(paramConsumer, "disposer is null");
    return RxJavaPlugins.onAssembly((Completable)new CompletableUsing(paramCallable, paramFunction, paramConsumer, paramBoolean));
  }
  
  @CheckReturnValue
  @SchedulerSupport("none")
  public static Completable wrap(CompletableSource paramCompletableSource) {
    ObjectHelper.requireNonNull(paramCompletableSource, "source is null");
    return (paramCompletableSource instanceof Completable) ? RxJavaPlugins.onAssembly((Completable)paramCompletableSource) : RxJavaPlugins.onAssembly((Completable)new CompletableFromUnsafeSource(paramCompletableSource));
  }
  
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Completable ambWith(CompletableSource paramCompletableSource) {
    ObjectHelper.requireNonNull(paramCompletableSource, "other is null");
    return ambArray(new CompletableSource[] { this, paramCompletableSource });
  }
  
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Completable andThen(CompletableSource paramCompletableSource) {
    ObjectHelper.requireNonNull(paramCompletableSource, "next is null");
    return RxJavaPlugins.onAssembly((Completable)new CompletableAndThenCompletable(this, paramCompletableSource));
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <T> Flowable<T> andThen(Publisher<T> paramPublisher) {
    ObjectHelper.requireNonNull(paramPublisher, "next is null");
    return RxJavaPlugins.onAssembly((Flowable)new CompletableAndThenPublisher(this, paramPublisher));
  }
  
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <T> Maybe<T> andThen(MaybeSource<T> paramMaybeSource) {
    ObjectHelper.requireNonNull(paramMaybeSource, "next is null");
    return RxJavaPlugins.onAssembly((Maybe)new MaybeDelayWithCompletable(paramMaybeSource, this));
  }
  
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <T> Observable<T> andThen(ObservableSource<T> paramObservableSource) {
    ObjectHelper.requireNonNull(paramObservableSource, "next is null");
    return RxJavaPlugins.onAssembly((Observable)new CompletableAndThenObservable(this, paramObservableSource));
  }
  
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <T> Single<T> andThen(SingleSource<T> paramSingleSource) {
    ObjectHelper.requireNonNull(paramSingleSource, "next is null");
    return RxJavaPlugins.onAssembly((Single)new SingleDelayWithCompletable(paramSingleSource, this));
  }
  
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <R> R as(CompletableConverter<? extends R> paramCompletableConverter) {
    return ((CompletableConverter<R>)ObjectHelper.requireNonNull(paramCompletableConverter, "converter is null")).apply(this);
  }
  
  @SchedulerSupport("none")
  public final void blockingAwait() {
    BlockingMultiObserver blockingMultiObserver = new BlockingMultiObserver();
    subscribe((CompletableObserver)blockingMultiObserver);
    blockingMultiObserver.blockingGet();
  }
  
  @CheckReturnValue
  @SchedulerSupport("none")
  public final boolean blockingAwait(long paramLong, TimeUnit paramTimeUnit) {
    ObjectHelper.requireNonNull(paramTimeUnit, "unit is null");
    BlockingMultiObserver blockingMultiObserver = new BlockingMultiObserver();
    subscribe((CompletableObserver)blockingMultiObserver);
    return blockingMultiObserver.blockingAwait(paramLong, paramTimeUnit);
  }
  
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Throwable blockingGet() {
    BlockingMultiObserver blockingMultiObserver = new BlockingMultiObserver();
    subscribe((CompletableObserver)blockingMultiObserver);
    return blockingMultiObserver.blockingGetError();
  }
  
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Throwable blockingGet(long paramLong, TimeUnit paramTimeUnit) {
    ObjectHelper.requireNonNull(paramTimeUnit, "unit is null");
    BlockingMultiObserver blockingMultiObserver = new BlockingMultiObserver();
    subscribe((CompletableObserver)blockingMultiObserver);
    return blockingMultiObserver.blockingGetError(paramLong, paramTimeUnit);
  }
  
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Completable cache() {
    return RxJavaPlugins.onAssembly((Completable)new CompletableCache(this));
  }
  
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Completable compose(CompletableTransformer paramCompletableTransformer) {
    return wrap(((CompletableTransformer)ObjectHelper.requireNonNull(paramCompletableTransformer, "transformer is null")).apply(this));
  }
  
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Completable concatWith(CompletableSource paramCompletableSource) {
    ObjectHelper.requireNonNull(paramCompletableSource, "other is null");
    return RxJavaPlugins.onAssembly((Completable)new CompletableAndThenCompletable(this, paramCompletableSource));
  }
  
  @CheckReturnValue
  @SchedulerSupport("io.reactivex:computation")
  public final Completable delay(long paramLong, TimeUnit paramTimeUnit) {
    return delay(paramLong, paramTimeUnit, Schedulers.computation(), false);
  }
  
  @CheckReturnValue
  @SchedulerSupport("custom")
  public final Completable delay(long paramLong, TimeUnit paramTimeUnit, Scheduler paramScheduler) {
    return delay(paramLong, paramTimeUnit, paramScheduler, false);
  }
  
  @CheckReturnValue
  @SchedulerSupport("custom")
  public final Completable delay(long paramLong, TimeUnit paramTimeUnit, Scheduler paramScheduler, boolean paramBoolean) {
    ObjectHelper.requireNonNull(paramTimeUnit, "unit is null");
    ObjectHelper.requireNonNull(paramScheduler, "scheduler is null");
    return RxJavaPlugins.onAssembly((Completable)new CompletableDelay(this, paramLong, paramTimeUnit, paramScheduler, paramBoolean));
  }
  
  @CheckReturnValue
  @SchedulerSupport("io.reactivex:computation")
  public final Completable delaySubscription(long paramLong, TimeUnit paramTimeUnit) {
    return delaySubscription(paramLong, paramTimeUnit, Schedulers.computation());
  }
  
  @CheckReturnValue
  @SchedulerSupport("custom")
  public final Completable delaySubscription(long paramLong, TimeUnit paramTimeUnit, Scheduler paramScheduler) {
    return timer(paramLong, paramTimeUnit, paramScheduler).andThen(this);
  }
  
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Completable doAfterTerminate(Action paramAction) {
    return doOnLifecycle(Functions.emptyConsumer(), Functions.emptyConsumer(), Functions.EMPTY_ACTION, Functions.EMPTY_ACTION, paramAction, Functions.EMPTY_ACTION);
  }
  
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Completable doFinally(Action paramAction) {
    ObjectHelper.requireNonNull(paramAction, "onFinally is null");
    return RxJavaPlugins.onAssembly((Completable)new CompletableDoFinally(this, paramAction));
  }
  
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Completable doOnComplete(Action paramAction) {
    return doOnLifecycle(Functions.emptyConsumer(), Functions.emptyConsumer(), paramAction, Functions.EMPTY_ACTION, Functions.EMPTY_ACTION, Functions.EMPTY_ACTION);
  }
  
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Completable doOnDispose(Action paramAction) {
    return doOnLifecycle(Functions.emptyConsumer(), Functions.emptyConsumer(), Functions.EMPTY_ACTION, Functions.EMPTY_ACTION, Functions.EMPTY_ACTION, paramAction);
  }
  
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Completable doOnError(Consumer<? super Throwable> paramConsumer) {
    return doOnLifecycle(Functions.emptyConsumer(), paramConsumer, Functions.EMPTY_ACTION, Functions.EMPTY_ACTION, Functions.EMPTY_ACTION, Functions.EMPTY_ACTION);
  }
  
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Completable doOnEvent(Consumer<? super Throwable> paramConsumer) {
    ObjectHelper.requireNonNull(paramConsumer, "onEvent is null");
    return RxJavaPlugins.onAssembly((Completable)new CompletableDoOnEvent(this, paramConsumer));
  }
  
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Completable doOnSubscribe(Consumer<? super Disposable> paramConsumer) {
    return doOnLifecycle(paramConsumer, Functions.emptyConsumer(), Functions.EMPTY_ACTION, Functions.EMPTY_ACTION, Functions.EMPTY_ACTION, Functions.EMPTY_ACTION);
  }
  
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Completable doOnTerminate(Action paramAction) {
    return doOnLifecycle(Functions.emptyConsumer(), Functions.emptyConsumer(), Functions.EMPTY_ACTION, paramAction, Functions.EMPTY_ACTION, Functions.EMPTY_ACTION);
  }
  
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Completable hide() {
    return RxJavaPlugins.onAssembly((Completable)new CompletableHide(this));
  }
  
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Completable lift(CompletableOperator paramCompletableOperator) {
    ObjectHelper.requireNonNull(paramCompletableOperator, "onLift is null");
    return RxJavaPlugins.onAssembly((Completable)new CompletableLift(this, paramCompletableOperator));
  }
  
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <T> Single<Notification<T>> materialize() {
    return RxJavaPlugins.onAssembly((Single)new CompletableMaterialize(this));
  }
  
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Completable mergeWith(CompletableSource paramCompletableSource) {
    ObjectHelper.requireNonNull(paramCompletableSource, "other is null");
    return mergeArray(new CompletableSource[] { this, paramCompletableSource });
  }
  
  @CheckReturnValue
  @SchedulerSupport("custom")
  public final Completable observeOn(Scheduler paramScheduler) {
    ObjectHelper.requireNonNull(paramScheduler, "scheduler is null");
    return RxJavaPlugins.onAssembly((Completable)new CompletableObserveOn(this, paramScheduler));
  }
  
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Completable onErrorComplete() {
    return onErrorComplete(Functions.alwaysTrue());
  }
  
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Completable onErrorComplete(Predicate<? super Throwable> paramPredicate) {
    ObjectHelper.requireNonNull(paramPredicate, "predicate is null");
    return RxJavaPlugins.onAssembly((Completable)new CompletableOnErrorComplete(this, paramPredicate));
  }
  
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Completable onErrorResumeNext(Function<? super Throwable, ? extends CompletableSource> paramFunction) {
    ObjectHelper.requireNonNull(paramFunction, "errorMapper is null");
    return RxJavaPlugins.onAssembly((Completable)new CompletableResumeNext(this, paramFunction));
  }
  
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Completable onTerminateDetach() {
    return RxJavaPlugins.onAssembly((Completable)new CompletableDetach(this));
  }
  
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Completable repeat() {
    return fromPublisher(toFlowable().repeat());
  }
  
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Completable repeat(long paramLong) {
    return fromPublisher(toFlowable().repeat(paramLong));
  }
  
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Completable repeatUntil(BooleanSupplier paramBooleanSupplier) {
    return fromPublisher(toFlowable().repeatUntil(paramBooleanSupplier));
  }
  
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Completable repeatWhen(Function<? super Flowable<Object>, ? extends Publisher<?>> paramFunction) {
    return fromPublisher(toFlowable().repeatWhen(paramFunction));
  }
  
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Completable retry() {
    return fromPublisher(toFlowable().retry());
  }
  
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Completable retry(long paramLong) {
    return fromPublisher(toFlowable().retry(paramLong));
  }
  
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Completable retry(long paramLong, Predicate<? super Throwable> paramPredicate) {
    return fromPublisher(toFlowable().retry(paramLong, paramPredicate));
  }
  
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Completable retry(BiPredicate<? super Integer, ? super Throwable> paramBiPredicate) {
    return fromPublisher(toFlowable().retry(paramBiPredicate));
  }
  
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Completable retry(Predicate<? super Throwable> paramPredicate) {
    return fromPublisher(toFlowable().retry(paramPredicate));
  }
  
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Completable retryWhen(Function<? super Flowable<Throwable>, ? extends Publisher<?>> paramFunction) {
    return fromPublisher(toFlowable().retryWhen(paramFunction));
  }
  
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Completable startWith(CompletableSource paramCompletableSource) {
    ObjectHelper.requireNonNull(paramCompletableSource, "other is null");
    return concatArray(new CompletableSource[] { paramCompletableSource, this });
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <T> Flowable<T> startWith(Publisher<T> paramPublisher) {
    ObjectHelper.requireNonNull(paramPublisher, "other is null");
    return toFlowable().startWith(paramPublisher);
  }
  
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <T> Observable<T> startWith(Observable<T> paramObservable) {
    ObjectHelper.requireNonNull(paramObservable, "other is null");
    return paramObservable.concatWith(toObservable());
  }
  
  @SchedulerSupport("none")
  public final Disposable subscribe() {
    EmptyCompletableObserver emptyCompletableObserver = new EmptyCompletableObserver();
    subscribe((CompletableObserver)emptyCompletableObserver);
    return (Disposable)emptyCompletableObserver;
  }
  
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Disposable subscribe(Action paramAction) {
    ObjectHelper.requireNonNull(paramAction, "onComplete is null");
    CallbackCompletableObserver callbackCompletableObserver = new CallbackCompletableObserver(paramAction);
    subscribe((CompletableObserver)callbackCompletableObserver);
    return (Disposable)callbackCompletableObserver;
  }
  
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Disposable subscribe(Action paramAction, Consumer<? super Throwable> paramConsumer) {
    ObjectHelper.requireNonNull(paramConsumer, "onError is null");
    ObjectHelper.requireNonNull(paramAction, "onComplete is null");
    CallbackCompletableObserver callbackCompletableObserver = new CallbackCompletableObserver(paramConsumer, paramAction);
    subscribe((CompletableObserver)callbackCompletableObserver);
    return (Disposable)callbackCompletableObserver;
  }
  
  @SchedulerSupport("none")
  public final void subscribe(CompletableObserver paramCompletableObserver) {
    ObjectHelper.requireNonNull(paramCompletableObserver, "observer is null");
    try {
      return;
    } catch (NullPointerException nullPointerException) {
      throw nullPointerException;
    } finally {
      paramCompletableObserver = null;
      Exceptions.throwIfFatal((Throwable)paramCompletableObserver);
      RxJavaPlugins.onError((Throwable)paramCompletableObserver);
    } 
  }
  
  protected abstract void subscribeActual(CompletableObserver paramCompletableObserver);
  
  @CheckReturnValue
  @SchedulerSupport("custom")
  public final Completable subscribeOn(Scheduler paramScheduler) {
    ObjectHelper.requireNonNull(paramScheduler, "scheduler is null");
    return RxJavaPlugins.onAssembly((Completable)new CompletableSubscribeOn(this, paramScheduler));
  }
  
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <E extends CompletableObserver> E subscribeWith(E paramE) {
    subscribe((CompletableObserver)paramE);
    return paramE;
  }
  
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Completable takeUntil(CompletableSource paramCompletableSource) {
    ObjectHelper.requireNonNull(paramCompletableSource, "other is null");
    return RxJavaPlugins.onAssembly((Completable)new CompletableTakeUntilCompletable(this, paramCompletableSource));
  }
  
  @CheckReturnValue
  @SchedulerSupport("none")
  public final TestObserver<Void> test() {
    TestObserver<Void> testObserver = new TestObserver();
    subscribe((CompletableObserver)testObserver);
    return testObserver;
  }
  
  @CheckReturnValue
  @SchedulerSupport("none")
  public final TestObserver<Void> test(boolean paramBoolean) {
    TestObserver<Void> testObserver = new TestObserver();
    if (paramBoolean)
      testObserver.cancel(); 
    subscribe((CompletableObserver)testObserver);
    return testObserver;
  }
  
  @CheckReturnValue
  @SchedulerSupport("io.reactivex:computation")
  public final Completable timeout(long paramLong, TimeUnit paramTimeUnit) {
    return timeout0(paramLong, paramTimeUnit, Schedulers.computation(), null);
  }
  
  @CheckReturnValue
  @SchedulerSupport("io.reactivex:computation")
  public final Completable timeout(long paramLong, TimeUnit paramTimeUnit, CompletableSource paramCompletableSource) {
    ObjectHelper.requireNonNull(paramCompletableSource, "other is null");
    return timeout0(paramLong, paramTimeUnit, Schedulers.computation(), paramCompletableSource);
  }
  
  @CheckReturnValue
  @SchedulerSupport("custom")
  public final Completable timeout(long paramLong, TimeUnit paramTimeUnit, Scheduler paramScheduler) {
    return timeout0(paramLong, paramTimeUnit, paramScheduler, null);
  }
  
  @CheckReturnValue
  @SchedulerSupport("custom")
  public final Completable timeout(long paramLong, TimeUnit paramTimeUnit, Scheduler paramScheduler, CompletableSource paramCompletableSource) {
    ObjectHelper.requireNonNull(paramCompletableSource, "other is null");
    return timeout0(paramLong, paramTimeUnit, paramScheduler, paramCompletableSource);
  }
  
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <U> U to(Function<? super Completable, U> paramFunction) {
    try {
      return (U)((Function)ObjectHelper.requireNonNull(paramFunction, "converter is null")).apply(this);
    } finally {
      paramFunction = null;
      Exceptions.throwIfFatal((Throwable)paramFunction);
    } 
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <T> Flowable<T> toFlowable() {
    return (this instanceof FuseToFlowable) ? ((FuseToFlowable)this).fuseToFlowable() : RxJavaPlugins.onAssembly((Flowable)new CompletableToFlowable(this));
  }
  
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <T> Maybe<T> toMaybe() {
    return (this instanceof FuseToMaybe) ? ((FuseToMaybe)this).fuseToMaybe() : RxJavaPlugins.onAssembly((Maybe)new MaybeFromCompletable(this));
  }
  
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <T> Observable<T> toObservable() {
    return (this instanceof FuseToObservable) ? ((FuseToObservable)this).fuseToObservable() : RxJavaPlugins.onAssembly((Observable)new CompletableToObservable(this));
  }
  
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <T> Single<T> toSingle(Callable<? extends T> paramCallable) {
    ObjectHelper.requireNonNull(paramCallable, "completionValueSupplier is null");
    return RxJavaPlugins.onAssembly((Single)new CompletableToSingle(this, paramCallable, null));
  }
  
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <T> Single<T> toSingleDefault(T paramT) {
    ObjectHelper.requireNonNull(paramT, "completionValue is null");
    return RxJavaPlugins.onAssembly((Single)new CompletableToSingle(this, null, paramT));
  }
  
  @CheckReturnValue
  @SchedulerSupport("custom")
  public final Completable unsubscribeOn(Scheduler paramScheduler) {
    ObjectHelper.requireNonNull(paramScheduler, "scheduler is null");
    return RxJavaPlugins.onAssembly((Completable)new CompletableDisposeOn(this, paramScheduler));
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/Completable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */