package io.reactivex;

import io.reactivex.annotations.BackpressureKind;
import io.reactivex.annotations.BackpressureSupport;
import io.reactivex.annotations.CheckReturnValue;
import io.reactivex.annotations.SchedulerSupport;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.flowables.ConnectableFlowable;
import io.reactivex.flowables.GroupedFlowable;
import io.reactivex.functions.Action;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.BiPredicate;
import io.reactivex.functions.BooleanSupplier;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Function3;
import io.reactivex.functions.Function4;
import io.reactivex.functions.Function5;
import io.reactivex.functions.Function6;
import io.reactivex.functions.Function7;
import io.reactivex.functions.Function8;
import io.reactivex.functions.Function9;
import io.reactivex.functions.LongConsumer;
import io.reactivex.functions.Predicate;
import io.reactivex.internal.functions.Functions;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.fuseable.ScalarCallable;
import io.reactivex.internal.operators.flowable.BlockingFlowableIterable;
import io.reactivex.internal.operators.flowable.BlockingFlowableLatest;
import io.reactivex.internal.operators.flowable.BlockingFlowableMostRecent;
import io.reactivex.internal.operators.flowable.BlockingFlowableNext;
import io.reactivex.internal.operators.flowable.FlowableAllSingle;
import io.reactivex.internal.operators.flowable.FlowableAmb;
import io.reactivex.internal.operators.flowable.FlowableAnySingle;
import io.reactivex.internal.operators.flowable.FlowableBlockingSubscribe;
import io.reactivex.internal.operators.flowable.FlowableBuffer;
import io.reactivex.internal.operators.flowable.FlowableBufferBoundary;
import io.reactivex.internal.operators.flowable.FlowableBufferBoundarySupplier;
import io.reactivex.internal.operators.flowable.FlowableBufferExactBoundary;
import io.reactivex.internal.operators.flowable.FlowableBufferTimed;
import io.reactivex.internal.operators.flowable.FlowableCache;
import io.reactivex.internal.operators.flowable.FlowableCollectSingle;
import io.reactivex.internal.operators.flowable.FlowableCombineLatest;
import io.reactivex.internal.operators.flowable.FlowableConcatArray;
import io.reactivex.internal.operators.flowable.FlowableConcatMap;
import io.reactivex.internal.operators.flowable.FlowableConcatMapEager;
import io.reactivex.internal.operators.flowable.FlowableConcatMapEagerPublisher;
import io.reactivex.internal.operators.flowable.FlowableConcatWithCompletable;
import io.reactivex.internal.operators.flowable.FlowableConcatWithMaybe;
import io.reactivex.internal.operators.flowable.FlowableConcatWithSingle;
import io.reactivex.internal.operators.flowable.FlowableCountSingle;
import io.reactivex.internal.operators.flowable.FlowableCreate;
import io.reactivex.internal.operators.flowable.FlowableDebounce;
import io.reactivex.internal.operators.flowable.FlowableDebounceTimed;
import io.reactivex.internal.operators.flowable.FlowableDefer;
import io.reactivex.internal.operators.flowable.FlowableDelay;
import io.reactivex.internal.operators.flowable.FlowableDelaySubscriptionOther;
import io.reactivex.internal.operators.flowable.FlowableDematerialize;
import io.reactivex.internal.operators.flowable.FlowableDetach;
import io.reactivex.internal.operators.flowable.FlowableDistinct;
import io.reactivex.internal.operators.flowable.FlowableDistinctUntilChanged;
import io.reactivex.internal.operators.flowable.FlowableDoAfterNext;
import io.reactivex.internal.operators.flowable.FlowableDoFinally;
import io.reactivex.internal.operators.flowable.FlowableDoOnEach;
import io.reactivex.internal.operators.flowable.FlowableDoOnLifecycle;
import io.reactivex.internal.operators.flowable.FlowableElementAtMaybe;
import io.reactivex.internal.operators.flowable.FlowableElementAtSingle;
import io.reactivex.internal.operators.flowable.FlowableEmpty;
import io.reactivex.internal.operators.flowable.FlowableError;
import io.reactivex.internal.operators.flowable.FlowableFilter;
import io.reactivex.internal.operators.flowable.FlowableFlatMap;
import io.reactivex.internal.operators.flowable.FlowableFlatMapCompletableCompletable;
import io.reactivex.internal.operators.flowable.FlowableFlatMapMaybe;
import io.reactivex.internal.operators.flowable.FlowableFlatMapSingle;
import io.reactivex.internal.operators.flowable.FlowableFlattenIterable;
import io.reactivex.internal.operators.flowable.FlowableFromArray;
import io.reactivex.internal.operators.flowable.FlowableFromCallable;
import io.reactivex.internal.operators.flowable.FlowableFromFuture;
import io.reactivex.internal.operators.flowable.FlowableFromIterable;
import io.reactivex.internal.operators.flowable.FlowableFromPublisher;
import io.reactivex.internal.operators.flowable.FlowableGenerate;
import io.reactivex.internal.operators.flowable.FlowableGroupBy;
import io.reactivex.internal.operators.flowable.FlowableGroupJoin;
import io.reactivex.internal.operators.flowable.FlowableHide;
import io.reactivex.internal.operators.flowable.FlowableIgnoreElements;
import io.reactivex.internal.operators.flowable.FlowableIgnoreElementsCompletable;
import io.reactivex.internal.operators.flowable.FlowableInternalHelper;
import io.reactivex.internal.operators.flowable.FlowableInterval;
import io.reactivex.internal.operators.flowable.FlowableIntervalRange;
import io.reactivex.internal.operators.flowable.FlowableJoin;
import io.reactivex.internal.operators.flowable.FlowableJust;
import io.reactivex.internal.operators.flowable.FlowableLastMaybe;
import io.reactivex.internal.operators.flowable.FlowableLastSingle;
import io.reactivex.internal.operators.flowable.FlowableLift;
import io.reactivex.internal.operators.flowable.FlowableLimit;
import io.reactivex.internal.operators.flowable.FlowableMap;
import io.reactivex.internal.operators.flowable.FlowableMapNotification;
import io.reactivex.internal.operators.flowable.FlowableMaterialize;
import io.reactivex.internal.operators.flowable.FlowableMergeWithCompletable;
import io.reactivex.internal.operators.flowable.FlowableMergeWithMaybe;
import io.reactivex.internal.operators.flowable.FlowableMergeWithSingle;
import io.reactivex.internal.operators.flowable.FlowableNever;
import io.reactivex.internal.operators.flowable.FlowableObserveOn;
import io.reactivex.internal.operators.flowable.FlowableOnBackpressureBuffer;
import io.reactivex.internal.operators.flowable.FlowableOnBackpressureBufferStrategy;
import io.reactivex.internal.operators.flowable.FlowableOnBackpressureDrop;
import io.reactivex.internal.operators.flowable.FlowableOnBackpressureLatest;
import io.reactivex.internal.operators.flowable.FlowableOnErrorNext;
import io.reactivex.internal.operators.flowable.FlowableOnErrorReturn;
import io.reactivex.internal.operators.flowable.FlowablePublish;
import io.reactivex.internal.operators.flowable.FlowablePublishMulticast;
import io.reactivex.internal.operators.flowable.FlowableRange;
import io.reactivex.internal.operators.flowable.FlowableRangeLong;
import io.reactivex.internal.operators.flowable.FlowableReduceMaybe;
import io.reactivex.internal.operators.flowable.FlowableReduceSeedSingle;
import io.reactivex.internal.operators.flowable.FlowableReduceWithSingle;
import io.reactivex.internal.operators.flowable.FlowableRepeat;
import io.reactivex.internal.operators.flowable.FlowableRepeatUntil;
import io.reactivex.internal.operators.flowable.FlowableRepeatWhen;
import io.reactivex.internal.operators.flowable.FlowableReplay;
import io.reactivex.internal.operators.flowable.FlowableRetryBiPredicate;
import io.reactivex.internal.operators.flowable.FlowableRetryPredicate;
import io.reactivex.internal.operators.flowable.FlowableRetryWhen;
import io.reactivex.internal.operators.flowable.FlowableSamplePublisher;
import io.reactivex.internal.operators.flowable.FlowableSampleTimed;
import io.reactivex.internal.operators.flowable.FlowableScalarXMap;
import io.reactivex.internal.operators.flowable.FlowableScan;
import io.reactivex.internal.operators.flowable.FlowableScanSeed;
import io.reactivex.internal.operators.flowable.FlowableSequenceEqualSingle;
import io.reactivex.internal.operators.flowable.FlowableSerialized;
import io.reactivex.internal.operators.flowable.FlowableSingleMaybe;
import io.reactivex.internal.operators.flowable.FlowableSingleSingle;
import io.reactivex.internal.operators.flowable.FlowableSkip;
import io.reactivex.internal.operators.flowable.FlowableSkipLast;
import io.reactivex.internal.operators.flowable.FlowableSkipLastTimed;
import io.reactivex.internal.operators.flowable.FlowableSkipUntil;
import io.reactivex.internal.operators.flowable.FlowableSkipWhile;
import io.reactivex.internal.operators.flowable.FlowableSubscribeOn;
import io.reactivex.internal.operators.flowable.FlowableSwitchIfEmpty;
import io.reactivex.internal.operators.flowable.FlowableSwitchMap;
import io.reactivex.internal.operators.flowable.FlowableTake;
import io.reactivex.internal.operators.flowable.FlowableTakeLast;
import io.reactivex.internal.operators.flowable.FlowableTakeLastOne;
import io.reactivex.internal.operators.flowable.FlowableTakeLastTimed;
import io.reactivex.internal.operators.flowable.FlowableTakeUntil;
import io.reactivex.internal.operators.flowable.FlowableTakeUntilPredicate;
import io.reactivex.internal.operators.flowable.FlowableTakeWhile;
import io.reactivex.internal.operators.flowable.FlowableThrottleFirstTimed;
import io.reactivex.internal.operators.flowable.FlowableThrottleLatest;
import io.reactivex.internal.operators.flowable.FlowableTimeInterval;
import io.reactivex.internal.operators.flowable.FlowableTimeout;
import io.reactivex.internal.operators.flowable.FlowableTimeoutTimed;
import io.reactivex.internal.operators.flowable.FlowableTimer;
import io.reactivex.internal.operators.flowable.FlowableToListSingle;
import io.reactivex.internal.operators.flowable.FlowableUnsubscribeOn;
import io.reactivex.internal.operators.flowable.FlowableUsing;
import io.reactivex.internal.operators.flowable.FlowableWindow;
import io.reactivex.internal.operators.flowable.FlowableWindowBoundary;
import io.reactivex.internal.operators.flowable.FlowableWindowBoundarySelector;
import io.reactivex.internal.operators.flowable.FlowableWindowBoundarySupplier;
import io.reactivex.internal.operators.flowable.FlowableWindowTimed;
import io.reactivex.internal.operators.flowable.FlowableWithLatestFrom;
import io.reactivex.internal.operators.flowable.FlowableWithLatestFromMany;
import io.reactivex.internal.operators.flowable.FlowableZip;
import io.reactivex.internal.operators.flowable.FlowableZipIterable;
import io.reactivex.internal.operators.mixed.FlowableConcatMapCompletable;
import io.reactivex.internal.operators.mixed.FlowableConcatMapMaybe;
import io.reactivex.internal.operators.mixed.FlowableConcatMapSingle;
import io.reactivex.internal.operators.mixed.FlowableSwitchMapCompletable;
import io.reactivex.internal.operators.mixed.FlowableSwitchMapMaybe;
import io.reactivex.internal.operators.mixed.FlowableSwitchMapSingle;
import io.reactivex.internal.operators.observable.ObservableFromPublisher;
import io.reactivex.internal.schedulers.ImmediateThinScheduler;
import io.reactivex.internal.subscribers.BlockingFirstSubscriber;
import io.reactivex.internal.subscribers.BlockingLastSubscriber;
import io.reactivex.internal.subscribers.ForEachWhileSubscriber;
import io.reactivex.internal.subscribers.FutureSubscriber;
import io.reactivex.internal.subscribers.LambdaSubscriber;
import io.reactivex.internal.subscribers.StrictSubscriber;
import io.reactivex.internal.util.ArrayListSupplier;
import io.reactivex.internal.util.ErrorMode;
import io.reactivex.internal.util.HashMapSupplier;
import io.reactivex.parallel.ParallelFlowable;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.schedulers.Timed;
import io.reactivex.subscribers.SafeSubscriber;
import io.reactivex.subscribers.TestSubscriber;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public abstract class Flowable<T> implements Publisher<T> {
  static final int BUFFER_SIZE = Math.max(1, Integer.getInteger("rx2.buffer-size", 128).intValue());
  
  @BackpressureSupport(BackpressureKind.PASS_THROUGH)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T> Flowable<T> amb(Iterable<? extends Publisher<? extends T>> paramIterable) {
    ObjectHelper.requireNonNull(paramIterable, "sources is null");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableAmb(null, paramIterable));
  }
  
  @BackpressureSupport(BackpressureKind.PASS_THROUGH)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T> Flowable<T> ambArray(Publisher<? extends T>... paramVarArgs) {
    ObjectHelper.requireNonNull(paramVarArgs, "sources is null");
    int i = paramVarArgs.length;
    return (i == 0) ? empty() : ((i == 1) ? fromPublisher(paramVarArgs[0]) : RxJavaPlugins.onAssembly((Flowable)new FlowableAmb((Publisher[])paramVarArgs, null)));
  }
  
  public static int bufferSize() {
    return BUFFER_SIZE;
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T, R> Flowable<R> combineLatest(Function<? super Object[], ? extends R> paramFunction, Publisher<? extends T>... paramVarArgs) {
    return combineLatest(paramVarArgs, paramFunction, bufferSize());
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T, R> Flowable<R> combineLatest(Iterable<? extends Publisher<? extends T>> paramIterable, Function<? super Object[], ? extends R> paramFunction) {
    return combineLatest(paramIterable, paramFunction, bufferSize());
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T, R> Flowable<R> combineLatest(Iterable<? extends Publisher<? extends T>> paramIterable, Function<? super Object[], ? extends R> paramFunction, int paramInt) {
    ObjectHelper.requireNonNull(paramIterable, "sources is null");
    ObjectHelper.requireNonNull(paramFunction, "combiner is null");
    ObjectHelper.verifyPositive(paramInt, "bufferSize");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableCombineLatest(paramIterable, paramFunction, paramInt, false));
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T1, T2, R> Flowable<R> combineLatest(Publisher<? extends T1> paramPublisher, Publisher<? extends T2> paramPublisher1, BiFunction<? super T1, ? super T2, ? extends R> paramBiFunction) {
    ObjectHelper.requireNonNull(paramPublisher, "source1 is null");
    ObjectHelper.requireNonNull(paramPublisher1, "source2 is null");
    return combineLatest(Functions.toFunction(paramBiFunction), (Publisher<?>[])new Publisher[] { paramPublisher, paramPublisher1 });
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T1, T2, T3, R> Flowable<R> combineLatest(Publisher<? extends T1> paramPublisher, Publisher<? extends T2> paramPublisher1, Publisher<? extends T3> paramPublisher2, Function3<? super T1, ? super T2, ? super T3, ? extends R> paramFunction3) {
    ObjectHelper.requireNonNull(paramPublisher, "source1 is null");
    ObjectHelper.requireNonNull(paramPublisher1, "source2 is null");
    ObjectHelper.requireNonNull(paramPublisher2, "source3 is null");
    return combineLatest(Functions.toFunction(paramFunction3), (Publisher<?>[])new Publisher[] { paramPublisher, paramPublisher1, paramPublisher2 });
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T1, T2, T3, T4, R> Flowable<R> combineLatest(Publisher<? extends T1> paramPublisher, Publisher<? extends T2> paramPublisher1, Publisher<? extends T3> paramPublisher2, Publisher<? extends T4> paramPublisher3, Function4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> paramFunction4) {
    ObjectHelper.requireNonNull(paramPublisher, "source1 is null");
    ObjectHelper.requireNonNull(paramPublisher1, "source2 is null");
    ObjectHelper.requireNonNull(paramPublisher2, "source3 is null");
    ObjectHelper.requireNonNull(paramPublisher3, "source4 is null");
    return combineLatest(Functions.toFunction(paramFunction4), (Publisher<?>[])new Publisher[] { paramPublisher, paramPublisher1, paramPublisher2, paramPublisher3 });
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T1, T2, T3, T4, T5, R> Flowable<R> combineLatest(Publisher<? extends T1> paramPublisher, Publisher<? extends T2> paramPublisher1, Publisher<? extends T3> paramPublisher2, Publisher<? extends T4> paramPublisher3, Publisher<? extends T5> paramPublisher4, Function5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? extends R> paramFunction5) {
    ObjectHelper.requireNonNull(paramPublisher, "source1 is null");
    ObjectHelper.requireNonNull(paramPublisher1, "source2 is null");
    ObjectHelper.requireNonNull(paramPublisher2, "source3 is null");
    ObjectHelper.requireNonNull(paramPublisher3, "source4 is null");
    ObjectHelper.requireNonNull(paramPublisher4, "source5 is null");
    return combineLatest(Functions.toFunction(paramFunction5), (Publisher<?>[])new Publisher[] { paramPublisher, paramPublisher1, paramPublisher2, paramPublisher3, paramPublisher4 });
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T1, T2, T3, T4, T5, T6, R> Flowable<R> combineLatest(Publisher<? extends T1> paramPublisher, Publisher<? extends T2> paramPublisher1, Publisher<? extends T3> paramPublisher2, Publisher<? extends T4> paramPublisher3, Publisher<? extends T5> paramPublisher4, Publisher<? extends T6> paramPublisher5, Function6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends R> paramFunction6) {
    ObjectHelper.requireNonNull(paramPublisher, "source1 is null");
    ObjectHelper.requireNonNull(paramPublisher1, "source2 is null");
    ObjectHelper.requireNonNull(paramPublisher2, "source3 is null");
    ObjectHelper.requireNonNull(paramPublisher3, "source4 is null");
    ObjectHelper.requireNonNull(paramPublisher4, "source5 is null");
    ObjectHelper.requireNonNull(paramPublisher5, "source6 is null");
    return combineLatest(Functions.toFunction(paramFunction6), (Publisher<?>[])new Publisher[] { paramPublisher, paramPublisher1, paramPublisher2, paramPublisher3, paramPublisher4, paramPublisher5 });
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T1, T2, T3, T4, T5, T6, T7, R> Flowable<R> combineLatest(Publisher<? extends T1> paramPublisher, Publisher<? extends T2> paramPublisher1, Publisher<? extends T3> paramPublisher2, Publisher<? extends T4> paramPublisher3, Publisher<? extends T5> paramPublisher4, Publisher<? extends T6> paramPublisher5, Publisher<? extends T7> paramPublisher6, Function7<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? extends R> paramFunction7) {
    ObjectHelper.requireNonNull(paramPublisher, "source1 is null");
    ObjectHelper.requireNonNull(paramPublisher1, "source2 is null");
    ObjectHelper.requireNonNull(paramPublisher2, "source3 is null");
    ObjectHelper.requireNonNull(paramPublisher3, "source4 is null");
    ObjectHelper.requireNonNull(paramPublisher4, "source5 is null");
    ObjectHelper.requireNonNull(paramPublisher5, "source6 is null");
    ObjectHelper.requireNonNull(paramPublisher6, "source7 is null");
    return combineLatest(Functions.toFunction(paramFunction7), (Publisher<?>[])new Publisher[] { paramPublisher, paramPublisher1, paramPublisher2, paramPublisher3, paramPublisher4, paramPublisher5, paramPublisher6 });
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T1, T2, T3, T4, T5, T6, T7, T8, R> Flowable<R> combineLatest(Publisher<? extends T1> paramPublisher, Publisher<? extends T2> paramPublisher1, Publisher<? extends T3> paramPublisher2, Publisher<? extends T4> paramPublisher3, Publisher<? extends T5> paramPublisher4, Publisher<? extends T6> paramPublisher5, Publisher<? extends T7> paramPublisher6, Publisher<? extends T8> paramPublisher7, Function8<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? extends R> paramFunction8) {
    ObjectHelper.requireNonNull(paramPublisher, "source1 is null");
    ObjectHelper.requireNonNull(paramPublisher1, "source2 is null");
    ObjectHelper.requireNonNull(paramPublisher2, "source3 is null");
    ObjectHelper.requireNonNull(paramPublisher3, "source4 is null");
    ObjectHelper.requireNonNull(paramPublisher4, "source5 is null");
    ObjectHelper.requireNonNull(paramPublisher5, "source6 is null");
    ObjectHelper.requireNonNull(paramPublisher6, "source7 is null");
    ObjectHelper.requireNonNull(paramPublisher7, "source8 is null");
    return combineLatest(Functions.toFunction(paramFunction8), (Publisher<?>[])new Publisher[] { paramPublisher, paramPublisher1, paramPublisher2, paramPublisher3, paramPublisher4, paramPublisher5, paramPublisher6, paramPublisher7 });
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, R> Flowable<R> combineLatest(Publisher<? extends T1> paramPublisher, Publisher<? extends T2> paramPublisher1, Publisher<? extends T3> paramPublisher2, Publisher<? extends T4> paramPublisher3, Publisher<? extends T5> paramPublisher4, Publisher<? extends T6> paramPublisher5, Publisher<? extends T7> paramPublisher6, Publisher<? extends T8> paramPublisher7, Publisher<? extends T9> paramPublisher8, Function9<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? extends R> paramFunction9) {
    ObjectHelper.requireNonNull(paramPublisher, "source1 is null");
    ObjectHelper.requireNonNull(paramPublisher1, "source2 is null");
    ObjectHelper.requireNonNull(paramPublisher2, "source3 is null");
    ObjectHelper.requireNonNull(paramPublisher3, "source4 is null");
    ObjectHelper.requireNonNull(paramPublisher4, "source5 is null");
    ObjectHelper.requireNonNull(paramPublisher5, "source6 is null");
    ObjectHelper.requireNonNull(paramPublisher6, "source7 is null");
    ObjectHelper.requireNonNull(paramPublisher7, "source8 is null");
    ObjectHelper.requireNonNull(paramPublisher8, "source9 is null");
    return combineLatest(Functions.toFunction(paramFunction9), (Publisher<?>[])new Publisher[] { paramPublisher, paramPublisher1, paramPublisher2, paramPublisher3, paramPublisher4, paramPublisher5, paramPublisher6, paramPublisher7, paramPublisher8 });
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T, R> Flowable<R> combineLatest(Publisher<? extends T>[] paramArrayOfPublisher, Function<? super Object[], ? extends R> paramFunction) {
    return combineLatest(paramArrayOfPublisher, paramFunction, bufferSize());
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T, R> Flowable<R> combineLatest(Publisher<? extends T>[] paramArrayOfPublisher, Function<? super Object[], ? extends R> paramFunction, int paramInt) {
    ObjectHelper.requireNonNull(paramArrayOfPublisher, "sources is null");
    if (paramArrayOfPublisher.length == 0)
      return empty(); 
    ObjectHelper.requireNonNull(paramFunction, "combiner is null");
    ObjectHelper.verifyPositive(paramInt, "bufferSize");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableCombineLatest((Publisher[])paramArrayOfPublisher, paramFunction, paramInt, false));
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T, R> Flowable<R> combineLatestDelayError(Function<? super Object[], ? extends R> paramFunction, int paramInt, Publisher<? extends T>... paramVarArgs) {
    return combineLatestDelayError(paramVarArgs, paramFunction, paramInt);
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T, R> Flowable<R> combineLatestDelayError(Function<? super Object[], ? extends R> paramFunction, Publisher<? extends T>... paramVarArgs) {
    return combineLatestDelayError(paramVarArgs, paramFunction, bufferSize());
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T, R> Flowable<R> combineLatestDelayError(Iterable<? extends Publisher<? extends T>> paramIterable, Function<? super Object[], ? extends R> paramFunction) {
    return combineLatestDelayError(paramIterable, paramFunction, bufferSize());
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T, R> Flowable<R> combineLatestDelayError(Iterable<? extends Publisher<? extends T>> paramIterable, Function<? super Object[], ? extends R> paramFunction, int paramInt) {
    ObjectHelper.requireNonNull(paramIterable, "sources is null");
    ObjectHelper.requireNonNull(paramFunction, "combiner is null");
    ObjectHelper.verifyPositive(paramInt, "bufferSize");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableCombineLatest(paramIterable, paramFunction, paramInt, true));
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T, R> Flowable<R> combineLatestDelayError(Publisher<? extends T>[] paramArrayOfPublisher, Function<? super Object[], ? extends R> paramFunction) {
    return combineLatestDelayError(paramArrayOfPublisher, paramFunction, bufferSize());
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T, R> Flowable<R> combineLatestDelayError(Publisher<? extends T>[] paramArrayOfPublisher, Function<? super Object[], ? extends R> paramFunction, int paramInt) {
    ObjectHelper.requireNonNull(paramArrayOfPublisher, "sources is null");
    ObjectHelper.requireNonNull(paramFunction, "combiner is null");
    ObjectHelper.verifyPositive(paramInt, "bufferSize");
    return (paramArrayOfPublisher.length == 0) ? empty() : RxJavaPlugins.onAssembly((Flowable)new FlowableCombineLatest((Publisher[])paramArrayOfPublisher, paramFunction, paramInt, true));
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T> Flowable<T> concat(Iterable<? extends Publisher<? extends T>> paramIterable) {
    ObjectHelper.requireNonNull(paramIterable, "sources is null");
    return fromIterable(paramIterable).concatMapDelayError(Functions.identity(), 2, false);
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T> Flowable<T> concat(Publisher<? extends Publisher<? extends T>> paramPublisher) {
    return concat(paramPublisher, bufferSize());
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T> Flowable<T> concat(Publisher<? extends Publisher<? extends T>> paramPublisher, int paramInt) {
    return fromPublisher(paramPublisher).concatMap(Functions.identity(), paramInt);
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T> Flowable<T> concat(Publisher<? extends T> paramPublisher1, Publisher<? extends T> paramPublisher2) {
    ObjectHelper.requireNonNull(paramPublisher1, "source1 is null");
    ObjectHelper.requireNonNull(paramPublisher2, "source2 is null");
    return concatArray((Publisher<? extends T>[])new Publisher[] { paramPublisher1, paramPublisher2 });
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T> Flowable<T> concat(Publisher<? extends T> paramPublisher1, Publisher<? extends T> paramPublisher2, Publisher<? extends T> paramPublisher3) {
    ObjectHelper.requireNonNull(paramPublisher1, "source1 is null");
    ObjectHelper.requireNonNull(paramPublisher2, "source2 is null");
    ObjectHelper.requireNonNull(paramPublisher3, "source3 is null");
    return concatArray((Publisher<? extends T>[])new Publisher[] { paramPublisher1, paramPublisher2, paramPublisher3 });
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T> Flowable<T> concat(Publisher<? extends T> paramPublisher1, Publisher<? extends T> paramPublisher2, Publisher<? extends T> paramPublisher3, Publisher<? extends T> paramPublisher4) {
    ObjectHelper.requireNonNull(paramPublisher1, "source1 is null");
    ObjectHelper.requireNonNull(paramPublisher2, "source2 is null");
    ObjectHelper.requireNonNull(paramPublisher3, "source3 is null");
    ObjectHelper.requireNonNull(paramPublisher4, "source4 is null");
    return concatArray((Publisher<? extends T>[])new Publisher[] { paramPublisher1, paramPublisher2, paramPublisher3, paramPublisher4 });
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T> Flowable<T> concatArray(Publisher<? extends T>... paramVarArgs) {
    return (paramVarArgs.length == 0) ? empty() : ((paramVarArgs.length == 1) ? fromPublisher(paramVarArgs[0]) : RxJavaPlugins.onAssembly((Flowable)new FlowableConcatArray((Publisher[])paramVarArgs, false)));
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T> Flowable<T> concatArrayDelayError(Publisher<? extends T>... paramVarArgs) {
    return (paramVarArgs.length == 0) ? empty() : ((paramVarArgs.length == 1) ? fromPublisher(paramVarArgs[0]) : RxJavaPlugins.onAssembly((Flowable)new FlowableConcatArray((Publisher[])paramVarArgs, true)));
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T> Flowable<T> concatArrayEager(int paramInt1, int paramInt2, Publisher<? extends T>... paramVarArgs) {
    ObjectHelper.requireNonNull(paramVarArgs, "sources is null");
    ObjectHelper.verifyPositive(paramInt1, "maxConcurrency");
    ObjectHelper.verifyPositive(paramInt2, "prefetch");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableConcatMapEager((Flowable)new FlowableFromArray((Object[])paramVarArgs), Functions.identity(), paramInt1, paramInt2, ErrorMode.IMMEDIATE));
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T> Flowable<T> concatArrayEager(Publisher<? extends T>... paramVarArgs) {
    return concatArrayEager(bufferSize(), bufferSize(), paramVarArgs);
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T> Flowable<T> concatArrayEagerDelayError(int paramInt1, int paramInt2, Publisher<? extends T>... paramVarArgs) {
    return fromArray(paramVarArgs).concatMapEagerDelayError(Functions.identity(), paramInt1, paramInt2, true);
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T> Flowable<T> concatArrayEagerDelayError(Publisher<? extends T>... paramVarArgs) {
    return concatArrayEagerDelayError(bufferSize(), bufferSize(), paramVarArgs);
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T> Flowable<T> concatDelayError(Iterable<? extends Publisher<? extends T>> paramIterable) {
    ObjectHelper.requireNonNull(paramIterable, "sources is null");
    return fromIterable(paramIterable).concatMapDelayError(Functions.identity());
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T> Flowable<T> concatDelayError(Publisher<? extends Publisher<? extends T>> paramPublisher) {
    return concatDelayError(paramPublisher, bufferSize(), true);
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T> Flowable<T> concatDelayError(Publisher<? extends Publisher<? extends T>> paramPublisher, int paramInt, boolean paramBoolean) {
    return fromPublisher(paramPublisher).concatMapDelayError(Functions.identity(), paramInt, paramBoolean);
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T> Flowable<T> concatEager(Iterable<? extends Publisher<? extends T>> paramIterable) {
    return concatEager(paramIterable, bufferSize(), bufferSize());
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T> Flowable<T> concatEager(Iterable<? extends Publisher<? extends T>> paramIterable, int paramInt1, int paramInt2) {
    ObjectHelper.requireNonNull(paramIterable, "sources is null");
    ObjectHelper.verifyPositive(paramInt1, "maxConcurrency");
    ObjectHelper.verifyPositive(paramInt2, "prefetch");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableConcatMapEager((Flowable)new FlowableFromIterable(paramIterable), Functions.identity(), paramInt1, paramInt2, ErrorMode.IMMEDIATE));
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T> Flowable<T> concatEager(Publisher<? extends Publisher<? extends T>> paramPublisher) {
    return concatEager(paramPublisher, bufferSize(), bufferSize());
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T> Flowable<T> concatEager(Publisher<? extends Publisher<? extends T>> paramPublisher, int paramInt1, int paramInt2) {
    ObjectHelper.requireNonNull(paramPublisher, "sources is null");
    ObjectHelper.verifyPositive(paramInt1, "maxConcurrency");
    ObjectHelper.verifyPositive(paramInt2, "prefetch");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableConcatMapEagerPublisher(paramPublisher, Functions.identity(), paramInt1, paramInt2, ErrorMode.IMMEDIATE));
  }
  
  @BackpressureSupport(BackpressureKind.SPECIAL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T> Flowable<T> create(FlowableOnSubscribe<T> paramFlowableOnSubscribe, BackpressureStrategy paramBackpressureStrategy) {
    ObjectHelper.requireNonNull(paramFlowableOnSubscribe, "source is null");
    ObjectHelper.requireNonNull(paramBackpressureStrategy, "mode is null");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableCreate(paramFlowableOnSubscribe, paramBackpressureStrategy));
  }
  
  @BackpressureSupport(BackpressureKind.PASS_THROUGH)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T> Flowable<T> defer(Callable<? extends Publisher<? extends T>> paramCallable) {
    ObjectHelper.requireNonNull(paramCallable, "supplier is null");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableDefer(paramCallable));
  }
  
  @BackpressureSupport(BackpressureKind.PASS_THROUGH)
  @CheckReturnValue
  @SchedulerSupport("none")
  private Flowable<T> doOnEach(Consumer<? super T> paramConsumer, Consumer<? super Throwable> paramConsumer1, Action paramAction1, Action paramAction2) {
    ObjectHelper.requireNonNull(paramConsumer, "onNext is null");
    ObjectHelper.requireNonNull(paramConsumer1, "onError is null");
    ObjectHelper.requireNonNull(paramAction1, "onComplete is null");
    ObjectHelper.requireNonNull(paramAction2, "onAfterTerminate is null");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableDoOnEach(this, paramConsumer, paramConsumer1, paramAction1, paramAction2));
  }
  
  @BackpressureSupport(BackpressureKind.PASS_THROUGH)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T> Flowable<T> empty() {
    return RxJavaPlugins.onAssembly(FlowableEmpty.INSTANCE);
  }
  
  @BackpressureSupport(BackpressureKind.PASS_THROUGH)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T> Flowable<T> error(Throwable paramThrowable) {
    ObjectHelper.requireNonNull(paramThrowable, "throwable is null");
    return error(Functions.justCallable(paramThrowable));
  }
  
  @BackpressureSupport(BackpressureKind.PASS_THROUGH)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T> Flowable<T> error(Callable<? extends Throwable> paramCallable) {
    ObjectHelper.requireNonNull(paramCallable, "supplier is null");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableError(paramCallable));
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T> Flowable<T> fromArray(T... paramVarArgs) {
    ObjectHelper.requireNonNull(paramVarArgs, "items is null");
    return (paramVarArgs.length == 0) ? empty() : ((paramVarArgs.length == 1) ? just(paramVarArgs[0]) : RxJavaPlugins.onAssembly((Flowable)new FlowableFromArray((Object[])paramVarArgs)));
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T> Flowable<T> fromCallable(Callable<? extends T> paramCallable) {
    ObjectHelper.requireNonNull(paramCallable, "supplier is null");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableFromCallable(paramCallable));
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T> Flowable<T> fromFuture(Future<? extends T> paramFuture) {
    ObjectHelper.requireNonNull(paramFuture, "future is null");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableFromFuture(paramFuture, 0L, null));
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T> Flowable<T> fromFuture(Future<? extends T> paramFuture, long paramLong, TimeUnit paramTimeUnit) {
    ObjectHelper.requireNonNull(paramFuture, "future is null");
    ObjectHelper.requireNonNull(paramTimeUnit, "unit is null");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableFromFuture(paramFuture, paramLong, paramTimeUnit));
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("custom")
  public static <T> Flowable<T> fromFuture(Future<? extends T> paramFuture, long paramLong, TimeUnit paramTimeUnit, Scheduler paramScheduler) {
    ObjectHelper.requireNonNull(paramScheduler, "scheduler is null");
    return fromFuture(paramFuture, paramLong, paramTimeUnit).subscribeOn(paramScheduler);
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("custom")
  public static <T> Flowable<T> fromFuture(Future<? extends T> paramFuture, Scheduler paramScheduler) {
    ObjectHelper.requireNonNull(paramScheduler, "scheduler is null");
    return fromFuture(paramFuture).subscribeOn(paramScheduler);
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T> Flowable<T> fromIterable(Iterable<? extends T> paramIterable) {
    ObjectHelper.requireNonNull(paramIterable, "source is null");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableFromIterable(paramIterable));
  }
  
  @BackpressureSupport(BackpressureKind.PASS_THROUGH)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T> Flowable<T> fromPublisher(Publisher<? extends T> paramPublisher) {
    if (paramPublisher instanceof Flowable)
      return RxJavaPlugins.onAssembly((Flowable)paramPublisher); 
    ObjectHelper.requireNonNull(paramPublisher, "source is null");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableFromPublisher(paramPublisher));
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T> Flowable<T> generate(Consumer<Emitter<T>> paramConsumer) {
    ObjectHelper.requireNonNull(paramConsumer, "generator is null");
    return generate(Functions.nullSupplier(), FlowableInternalHelper.simpleGenerator(paramConsumer), Functions.emptyConsumer());
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T, S> Flowable<T> generate(Callable<S> paramCallable, BiConsumer<S, Emitter<T>> paramBiConsumer) {
    ObjectHelper.requireNonNull(paramBiConsumer, "generator is null");
    return generate(paramCallable, FlowableInternalHelper.simpleBiGenerator(paramBiConsumer), Functions.emptyConsumer());
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T, S> Flowable<T> generate(Callable<S> paramCallable, BiConsumer<S, Emitter<T>> paramBiConsumer, Consumer<? super S> paramConsumer) {
    ObjectHelper.requireNonNull(paramBiConsumer, "generator is null");
    return generate(paramCallable, FlowableInternalHelper.simpleBiGenerator(paramBiConsumer), paramConsumer);
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T, S> Flowable<T> generate(Callable<S> paramCallable, BiFunction<S, Emitter<T>, S> paramBiFunction) {
    return generate(paramCallable, paramBiFunction, Functions.emptyConsumer());
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T, S> Flowable<T> generate(Callable<S> paramCallable, BiFunction<S, Emitter<T>, S> paramBiFunction, Consumer<? super S> paramConsumer) {
    ObjectHelper.requireNonNull(paramCallable, "initialState is null");
    ObjectHelper.requireNonNull(paramBiFunction, "generator is null");
    ObjectHelper.requireNonNull(paramConsumer, "disposeState is null");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableGenerate(paramCallable, paramBiFunction, paramConsumer));
  }
  
  @BackpressureSupport(BackpressureKind.ERROR)
  @CheckReturnValue
  @SchedulerSupport("io.reactivex:computation")
  public static Flowable<Long> interval(long paramLong1, long paramLong2, TimeUnit paramTimeUnit) {
    return interval(paramLong1, paramLong2, paramTimeUnit, Schedulers.computation());
  }
  
  @BackpressureSupport(BackpressureKind.ERROR)
  @CheckReturnValue
  @SchedulerSupport("custom")
  public static Flowable<Long> interval(long paramLong1, long paramLong2, TimeUnit paramTimeUnit, Scheduler paramScheduler) {
    ObjectHelper.requireNonNull(paramTimeUnit, "unit is null");
    ObjectHelper.requireNonNull(paramScheduler, "scheduler is null");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableInterval(Math.max(0L, paramLong1), Math.max(0L, paramLong2), paramTimeUnit, paramScheduler));
  }
  
  @BackpressureSupport(BackpressureKind.ERROR)
  @CheckReturnValue
  @SchedulerSupport("io.reactivex:computation")
  public static Flowable<Long> interval(long paramLong, TimeUnit paramTimeUnit) {
    return interval(paramLong, paramLong, paramTimeUnit, Schedulers.computation());
  }
  
  @BackpressureSupport(BackpressureKind.ERROR)
  @CheckReturnValue
  @SchedulerSupport("custom")
  public static Flowable<Long> interval(long paramLong, TimeUnit paramTimeUnit, Scheduler paramScheduler) {
    return interval(paramLong, paramLong, paramTimeUnit, paramScheduler);
  }
  
  @BackpressureSupport(BackpressureKind.ERROR)
  @CheckReturnValue
  @SchedulerSupport("io.reactivex:computation")
  public static Flowable<Long> intervalRange(long paramLong1, long paramLong2, long paramLong3, long paramLong4, TimeUnit paramTimeUnit) {
    return intervalRange(paramLong1, paramLong2, paramLong3, paramLong4, paramTimeUnit, Schedulers.computation());
  }
  
  @BackpressureSupport(BackpressureKind.ERROR)
  @CheckReturnValue
  @SchedulerSupport("custom")
  public static Flowable<Long> intervalRange(long paramLong1, long paramLong2, long paramLong3, long paramLong4, TimeUnit paramTimeUnit, Scheduler paramScheduler) {
    if (paramLong2 >= 0L) {
      if (paramLong2 == 0L)
        return empty().delay(paramLong3, paramTimeUnit, paramScheduler); 
      paramLong2 = paramLong1 + paramLong2 - 1L;
      if (paramLong1 <= 0L || paramLong2 >= 0L) {
        ObjectHelper.requireNonNull(paramTimeUnit, "unit is null");
        ObjectHelper.requireNonNull(paramScheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly((Flowable)new FlowableIntervalRange(paramLong1, paramLong2, Math.max(0L, paramLong3), Math.max(0L, paramLong4), paramTimeUnit, paramScheduler));
      } 
      throw new IllegalArgumentException("Overflow! start + count is bigger than Long.MAX_VALUE");
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("count >= 0 required but it was ");
    stringBuilder.append(paramLong2);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T> Flowable<T> just(T paramT) {
    ObjectHelper.requireNonNull(paramT, "item is null");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableJust(paramT));
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T> Flowable<T> just(T paramT1, T paramT2) {
    ObjectHelper.requireNonNull(paramT1, "item1 is null");
    ObjectHelper.requireNonNull(paramT2, "item2 is null");
    return fromArray((T[])new Object[] { paramT1, paramT2 });
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T> Flowable<T> just(T paramT1, T paramT2, T paramT3) {
    ObjectHelper.requireNonNull(paramT1, "item1 is null");
    ObjectHelper.requireNonNull(paramT2, "item2 is null");
    ObjectHelper.requireNonNull(paramT3, "item3 is null");
    return fromArray((T[])new Object[] { paramT1, paramT2, paramT3 });
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T> Flowable<T> just(T paramT1, T paramT2, T paramT3, T paramT4) {
    ObjectHelper.requireNonNull(paramT1, "item1 is null");
    ObjectHelper.requireNonNull(paramT2, "item2 is null");
    ObjectHelper.requireNonNull(paramT3, "item3 is null");
    ObjectHelper.requireNonNull(paramT4, "item4 is null");
    return fromArray((T[])new Object[] { paramT1, paramT2, paramT3, paramT4 });
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T> Flowable<T> just(T paramT1, T paramT2, T paramT3, T paramT4, T paramT5) {
    ObjectHelper.requireNonNull(paramT1, "item1 is null");
    ObjectHelper.requireNonNull(paramT2, "item2 is null");
    ObjectHelper.requireNonNull(paramT3, "item3 is null");
    ObjectHelper.requireNonNull(paramT4, "item4 is null");
    ObjectHelper.requireNonNull(paramT5, "item5 is null");
    return fromArray((T[])new Object[] { paramT1, paramT2, paramT3, paramT4, paramT5 });
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T> Flowable<T> just(T paramT1, T paramT2, T paramT3, T paramT4, T paramT5, T paramT6) {
    ObjectHelper.requireNonNull(paramT1, "item1 is null");
    ObjectHelper.requireNonNull(paramT2, "item2 is null");
    ObjectHelper.requireNonNull(paramT3, "item3 is null");
    ObjectHelper.requireNonNull(paramT4, "item4 is null");
    ObjectHelper.requireNonNull(paramT5, "item5 is null");
    ObjectHelper.requireNonNull(paramT6, "item6 is null");
    return fromArray((T[])new Object[] { paramT1, paramT2, paramT3, paramT4, paramT5, paramT6 });
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T> Flowable<T> just(T paramT1, T paramT2, T paramT3, T paramT4, T paramT5, T paramT6, T paramT7) {
    ObjectHelper.requireNonNull(paramT1, "item1 is null");
    ObjectHelper.requireNonNull(paramT2, "item2 is null");
    ObjectHelper.requireNonNull(paramT3, "item3 is null");
    ObjectHelper.requireNonNull(paramT4, "item4 is null");
    ObjectHelper.requireNonNull(paramT5, "item5 is null");
    ObjectHelper.requireNonNull(paramT6, "item6 is null");
    ObjectHelper.requireNonNull(paramT7, "item7 is null");
    return fromArray((T[])new Object[] { paramT1, paramT2, paramT3, paramT4, paramT5, paramT6, paramT7 });
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T> Flowable<T> just(T paramT1, T paramT2, T paramT3, T paramT4, T paramT5, T paramT6, T paramT7, T paramT8) {
    ObjectHelper.requireNonNull(paramT1, "item1 is null");
    ObjectHelper.requireNonNull(paramT2, "item2 is null");
    ObjectHelper.requireNonNull(paramT3, "item3 is null");
    ObjectHelper.requireNonNull(paramT4, "item4 is null");
    ObjectHelper.requireNonNull(paramT5, "item5 is null");
    ObjectHelper.requireNonNull(paramT6, "item6 is null");
    ObjectHelper.requireNonNull(paramT7, "item7 is null");
    ObjectHelper.requireNonNull(paramT8, "item8 is null");
    return fromArray((T[])new Object[] { paramT1, paramT2, paramT3, paramT4, paramT5, paramT6, paramT7, paramT8 });
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T> Flowable<T> just(T paramT1, T paramT2, T paramT3, T paramT4, T paramT5, T paramT6, T paramT7, T paramT8, T paramT9) {
    ObjectHelper.requireNonNull(paramT1, "item1 is null");
    ObjectHelper.requireNonNull(paramT2, "item2 is null");
    ObjectHelper.requireNonNull(paramT3, "item3 is null");
    ObjectHelper.requireNonNull(paramT4, "item4 is null");
    ObjectHelper.requireNonNull(paramT5, "item5 is null");
    ObjectHelper.requireNonNull(paramT6, "item6 is null");
    ObjectHelper.requireNonNull(paramT7, "item7 is null");
    ObjectHelper.requireNonNull(paramT8, "item8 is null");
    ObjectHelper.requireNonNull(paramT9, "item9 is null");
    return fromArray((T[])new Object[] { paramT1, paramT2, paramT3, paramT4, paramT5, paramT6, paramT7, paramT8, paramT9 });
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T> Flowable<T> just(T paramT1, T paramT2, T paramT3, T paramT4, T paramT5, T paramT6, T paramT7, T paramT8, T paramT9, T paramT10) {
    ObjectHelper.requireNonNull(paramT1, "item1 is null");
    ObjectHelper.requireNonNull(paramT2, "item2 is null");
    ObjectHelper.requireNonNull(paramT3, "item3 is null");
    ObjectHelper.requireNonNull(paramT4, "item4 is null");
    ObjectHelper.requireNonNull(paramT5, "item5 is null");
    ObjectHelper.requireNonNull(paramT6, "item6 is null");
    ObjectHelper.requireNonNull(paramT7, "item7 is null");
    ObjectHelper.requireNonNull(paramT8, "item8 is null");
    ObjectHelper.requireNonNull(paramT9, "item9 is null");
    ObjectHelper.requireNonNull(paramT10, "item10 is null");
    return fromArray((T[])new Object[] { paramT1, paramT2, paramT3, paramT4, paramT5, paramT6, paramT7, paramT8, paramT9, paramT10 });
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T> Flowable<T> merge(Iterable<? extends Publisher<? extends T>> paramIterable) {
    return fromIterable(paramIterable).flatMap(Functions.identity());
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T> Flowable<T> merge(Iterable<? extends Publisher<? extends T>> paramIterable, int paramInt) {
    return fromIterable(paramIterable).flatMap(Functions.identity(), paramInt);
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T> Flowable<T> merge(Iterable<? extends Publisher<? extends T>> paramIterable, int paramInt1, int paramInt2) {
    return fromIterable(paramIterable).flatMap(Functions.identity(), false, paramInt1, paramInt2);
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T> Flowable<T> merge(Publisher<? extends Publisher<? extends T>> paramPublisher) {
    return merge(paramPublisher, bufferSize());
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T> Flowable<T> merge(Publisher<? extends Publisher<? extends T>> paramPublisher, int paramInt) {
    return fromPublisher(paramPublisher).flatMap(Functions.identity(), paramInt);
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T> Flowable<T> merge(Publisher<? extends T> paramPublisher1, Publisher<? extends T> paramPublisher2) {
    ObjectHelper.requireNonNull(paramPublisher1, "source1 is null");
    ObjectHelper.requireNonNull(paramPublisher2, "source2 is null");
    return fromArray(new Publisher[] { paramPublisher1, paramPublisher2 }).flatMap(Functions.identity(), false, 2);
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T> Flowable<T> merge(Publisher<? extends T> paramPublisher1, Publisher<? extends T> paramPublisher2, Publisher<? extends T> paramPublisher3) {
    ObjectHelper.requireNonNull(paramPublisher1, "source1 is null");
    ObjectHelper.requireNonNull(paramPublisher2, "source2 is null");
    ObjectHelper.requireNonNull(paramPublisher3, "source3 is null");
    return fromArray(new Publisher[] { paramPublisher1, paramPublisher2, paramPublisher3 }).flatMap(Functions.identity(), false, 3);
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T> Flowable<T> merge(Publisher<? extends T> paramPublisher1, Publisher<? extends T> paramPublisher2, Publisher<? extends T> paramPublisher3, Publisher<? extends T> paramPublisher4) {
    ObjectHelper.requireNonNull(paramPublisher1, "source1 is null");
    ObjectHelper.requireNonNull(paramPublisher2, "source2 is null");
    ObjectHelper.requireNonNull(paramPublisher3, "source3 is null");
    ObjectHelper.requireNonNull(paramPublisher4, "source4 is null");
    return fromArray(new Publisher[] { paramPublisher1, paramPublisher2, paramPublisher3, paramPublisher4 }).flatMap(Functions.identity(), false, 4);
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T> Flowable<T> mergeArray(int paramInt1, int paramInt2, Publisher<? extends T>... paramVarArgs) {
    return fromArray(paramVarArgs).flatMap(Functions.identity(), false, paramInt1, paramInt2);
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T> Flowable<T> mergeArray(Publisher<? extends T>... paramVarArgs) {
    return fromArray(paramVarArgs).flatMap(Functions.identity(), paramVarArgs.length);
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T> Flowable<T> mergeArrayDelayError(int paramInt1, int paramInt2, Publisher<? extends T>... paramVarArgs) {
    return fromArray(paramVarArgs).flatMap(Functions.identity(), true, paramInt1, paramInt2);
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T> Flowable<T> mergeArrayDelayError(Publisher<? extends T>... paramVarArgs) {
    return fromArray(paramVarArgs).flatMap(Functions.identity(), true, paramVarArgs.length);
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T> Flowable<T> mergeDelayError(Iterable<? extends Publisher<? extends T>> paramIterable) {
    return fromIterable(paramIterable).flatMap(Functions.identity(), true);
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T> Flowable<T> mergeDelayError(Iterable<? extends Publisher<? extends T>> paramIterable, int paramInt) {
    return fromIterable(paramIterable).flatMap(Functions.identity(), true, paramInt);
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T> Flowable<T> mergeDelayError(Iterable<? extends Publisher<? extends T>> paramIterable, int paramInt1, int paramInt2) {
    return fromIterable(paramIterable).flatMap(Functions.identity(), true, paramInt1, paramInt2);
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T> Flowable<T> mergeDelayError(Publisher<? extends Publisher<? extends T>> paramPublisher) {
    return mergeDelayError(paramPublisher, bufferSize());
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T> Flowable<T> mergeDelayError(Publisher<? extends Publisher<? extends T>> paramPublisher, int paramInt) {
    return fromPublisher(paramPublisher).flatMap(Functions.identity(), true, paramInt);
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T> Flowable<T> mergeDelayError(Publisher<? extends T> paramPublisher1, Publisher<? extends T> paramPublisher2) {
    ObjectHelper.requireNonNull(paramPublisher1, "source1 is null");
    ObjectHelper.requireNonNull(paramPublisher2, "source2 is null");
    return fromArray(new Publisher[] { paramPublisher1, paramPublisher2 }).flatMap(Functions.identity(), true, 2);
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T> Flowable<T> mergeDelayError(Publisher<? extends T> paramPublisher1, Publisher<? extends T> paramPublisher2, Publisher<? extends T> paramPublisher3) {
    ObjectHelper.requireNonNull(paramPublisher1, "source1 is null");
    ObjectHelper.requireNonNull(paramPublisher2, "source2 is null");
    ObjectHelper.requireNonNull(paramPublisher3, "source3 is null");
    return fromArray(new Publisher[] { paramPublisher1, paramPublisher2, paramPublisher3 }).flatMap(Functions.identity(), true, 3);
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T> Flowable<T> mergeDelayError(Publisher<? extends T> paramPublisher1, Publisher<? extends T> paramPublisher2, Publisher<? extends T> paramPublisher3, Publisher<? extends T> paramPublisher4) {
    ObjectHelper.requireNonNull(paramPublisher1, "source1 is null");
    ObjectHelper.requireNonNull(paramPublisher2, "source2 is null");
    ObjectHelper.requireNonNull(paramPublisher3, "source3 is null");
    ObjectHelper.requireNonNull(paramPublisher4, "source4 is null");
    return fromArray(new Publisher[] { paramPublisher1, paramPublisher2, paramPublisher3, paramPublisher4 }).flatMap(Functions.identity(), true, 4);
  }
  
  @BackpressureSupport(BackpressureKind.PASS_THROUGH)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T> Flowable<T> never() {
    return RxJavaPlugins.onAssembly(FlowableNever.INSTANCE);
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static Flowable<Integer> range(int paramInt1, int paramInt2) {
    if (paramInt2 >= 0) {
      if (paramInt2 == 0)
        return empty(); 
      if (paramInt2 == 1)
        return just(Integer.valueOf(paramInt1)); 
      if (paramInt1 + (paramInt2 - 1) <= 2147483647L)
        return RxJavaPlugins.onAssembly((Flowable)new FlowableRange(paramInt1, paramInt2)); 
      throw new IllegalArgumentException("Integer overflow");
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("count >= 0 required but it was ");
    stringBuilder.append(paramInt2);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static Flowable<Long> rangeLong(long paramLong1, long paramLong2) {
    if (paramLong2 >= 0L) {
      if (paramLong2 == 0L)
        return empty(); 
      if (paramLong2 == 1L)
        return just(Long.valueOf(paramLong1)); 
      if (paramLong1 <= 0L || paramLong2 - 1L + paramLong1 >= 0L)
        return RxJavaPlugins.onAssembly((Flowable)new FlowableRangeLong(paramLong1, paramLong2)); 
      throw new IllegalArgumentException("Overflow! start + count is bigger than Long.MAX_VALUE");
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("count >= 0 required but it was ");
    stringBuilder.append(paramLong2);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T> Single<Boolean> sequenceEqual(Publisher<? extends T> paramPublisher1, Publisher<? extends T> paramPublisher2) {
    return sequenceEqual(paramPublisher1, paramPublisher2, ObjectHelper.equalsPredicate(), bufferSize());
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T> Single<Boolean> sequenceEqual(Publisher<? extends T> paramPublisher1, Publisher<? extends T> paramPublisher2, int paramInt) {
    return sequenceEqual(paramPublisher1, paramPublisher2, ObjectHelper.equalsPredicate(), paramInt);
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T> Single<Boolean> sequenceEqual(Publisher<? extends T> paramPublisher1, Publisher<? extends T> paramPublisher2, BiPredicate<? super T, ? super T> paramBiPredicate) {
    return sequenceEqual(paramPublisher1, paramPublisher2, paramBiPredicate, bufferSize());
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T> Single<Boolean> sequenceEqual(Publisher<? extends T> paramPublisher1, Publisher<? extends T> paramPublisher2, BiPredicate<? super T, ? super T> paramBiPredicate, int paramInt) {
    ObjectHelper.requireNonNull(paramPublisher1, "source1 is null");
    ObjectHelper.requireNonNull(paramPublisher2, "source2 is null");
    ObjectHelper.requireNonNull(paramBiPredicate, "isEqual is null");
    ObjectHelper.verifyPositive(paramInt, "bufferSize");
    return RxJavaPlugins.onAssembly((Single)new FlowableSequenceEqualSingle(paramPublisher1, paramPublisher2, paramBiPredicate, paramInt));
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T> Flowable<T> switchOnNext(Publisher<? extends Publisher<? extends T>> paramPublisher) {
    return fromPublisher(paramPublisher).switchMap(Functions.identity());
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T> Flowable<T> switchOnNext(Publisher<? extends Publisher<? extends T>> paramPublisher, int paramInt) {
    return fromPublisher(paramPublisher).switchMap(Functions.identity(), paramInt);
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T> Flowable<T> switchOnNextDelayError(Publisher<? extends Publisher<? extends T>> paramPublisher) {
    return switchOnNextDelayError(paramPublisher, bufferSize());
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T> Flowable<T> switchOnNextDelayError(Publisher<? extends Publisher<? extends T>> paramPublisher, int paramInt) {
    return fromPublisher(paramPublisher).switchMapDelayError(Functions.identity(), paramInt);
  }
  
  private Flowable<T> timeout0(long paramLong, TimeUnit paramTimeUnit, Publisher<? extends T> paramPublisher, Scheduler paramScheduler) {
    ObjectHelper.requireNonNull(paramTimeUnit, "timeUnit is null");
    ObjectHelper.requireNonNull(paramScheduler, "scheduler is null");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableTimeoutTimed(this, paramLong, paramTimeUnit, paramScheduler, paramPublisher));
  }
  
  private <U, V> Flowable<T> timeout0(Publisher<U> paramPublisher, Function<? super T, ? extends Publisher<V>> paramFunction, Publisher<? extends T> paramPublisher1) {
    ObjectHelper.requireNonNull(paramFunction, "itemTimeoutIndicator is null");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableTimeout(this, paramPublisher, paramFunction, paramPublisher1));
  }
  
  @BackpressureSupport(BackpressureKind.ERROR)
  @CheckReturnValue
  @SchedulerSupport("io.reactivex:computation")
  public static Flowable<Long> timer(long paramLong, TimeUnit paramTimeUnit) {
    return timer(paramLong, paramTimeUnit, Schedulers.computation());
  }
  
  @BackpressureSupport(BackpressureKind.ERROR)
  @CheckReturnValue
  @SchedulerSupport("custom")
  public static Flowable<Long> timer(long paramLong, TimeUnit paramTimeUnit, Scheduler paramScheduler) {
    ObjectHelper.requireNonNull(paramTimeUnit, "unit is null");
    ObjectHelper.requireNonNull(paramScheduler, "scheduler is null");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableTimer(Math.max(0L, paramLong), paramTimeUnit, paramScheduler));
  }
  
  @BackpressureSupport(BackpressureKind.NONE)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T> Flowable<T> unsafeCreate(Publisher<T> paramPublisher) {
    ObjectHelper.requireNonNull(paramPublisher, "onSubscribe is null");
    if (!(paramPublisher instanceof Flowable))
      return RxJavaPlugins.onAssembly((Flowable)new FlowableFromPublisher(paramPublisher)); 
    throw new IllegalArgumentException("unsafeCreate(Flowable) should be upgraded");
  }
  
  @BackpressureSupport(BackpressureKind.PASS_THROUGH)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T, D> Flowable<T> using(Callable<? extends D> paramCallable, Function<? super D, ? extends Publisher<? extends T>> paramFunction, Consumer<? super D> paramConsumer) {
    return using(paramCallable, paramFunction, paramConsumer, true);
  }
  
  @BackpressureSupport(BackpressureKind.PASS_THROUGH)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T, D> Flowable<T> using(Callable<? extends D> paramCallable, Function<? super D, ? extends Publisher<? extends T>> paramFunction, Consumer<? super D> paramConsumer, boolean paramBoolean) {
    ObjectHelper.requireNonNull(paramCallable, "resourceSupplier is null");
    ObjectHelper.requireNonNull(paramFunction, "sourceSupplier is null");
    ObjectHelper.requireNonNull(paramConsumer, "resourceDisposer is null");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableUsing(paramCallable, paramFunction, paramConsumer, paramBoolean));
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T, R> Flowable<R> zip(Iterable<? extends Publisher<? extends T>> paramIterable, Function<? super Object[], ? extends R> paramFunction) {
    ObjectHelper.requireNonNull(paramFunction, "zipper is null");
    ObjectHelper.requireNonNull(paramIterable, "sources is null");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableZip(null, paramIterable, paramFunction, bufferSize(), false));
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T, R> Flowable<R> zip(Publisher<? extends Publisher<? extends T>> paramPublisher, Function<? super Object[], ? extends R> paramFunction) {
    ObjectHelper.requireNonNull(paramFunction, "zipper is null");
    return fromPublisher(paramPublisher).toList().flatMapPublisher(FlowableInternalHelper.zipIterable(paramFunction));
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T1, T2, R> Flowable<R> zip(Publisher<? extends T1> paramPublisher, Publisher<? extends T2> paramPublisher1, BiFunction<? super T1, ? super T2, ? extends R> paramBiFunction) {
    ObjectHelper.requireNonNull(paramPublisher, "source1 is null");
    ObjectHelper.requireNonNull(paramPublisher1, "source2 is null");
    return zipArray(Functions.toFunction(paramBiFunction), false, bufferSize(), (Publisher<?>[])new Publisher[] { paramPublisher, paramPublisher1 });
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T1, T2, R> Flowable<R> zip(Publisher<? extends T1> paramPublisher, Publisher<? extends T2> paramPublisher1, BiFunction<? super T1, ? super T2, ? extends R> paramBiFunction, boolean paramBoolean) {
    ObjectHelper.requireNonNull(paramPublisher, "source1 is null");
    ObjectHelper.requireNonNull(paramPublisher1, "source2 is null");
    return zipArray(Functions.toFunction(paramBiFunction), paramBoolean, bufferSize(), (Publisher<?>[])new Publisher[] { paramPublisher, paramPublisher1 });
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T1, T2, R> Flowable<R> zip(Publisher<? extends T1> paramPublisher, Publisher<? extends T2> paramPublisher1, BiFunction<? super T1, ? super T2, ? extends R> paramBiFunction, boolean paramBoolean, int paramInt) {
    ObjectHelper.requireNonNull(paramPublisher, "source1 is null");
    ObjectHelper.requireNonNull(paramPublisher1, "source2 is null");
    return zipArray(Functions.toFunction(paramBiFunction), paramBoolean, paramInt, (Publisher<?>[])new Publisher[] { paramPublisher, paramPublisher1 });
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T1, T2, T3, R> Flowable<R> zip(Publisher<? extends T1> paramPublisher, Publisher<? extends T2> paramPublisher1, Publisher<? extends T3> paramPublisher2, Function3<? super T1, ? super T2, ? super T3, ? extends R> paramFunction3) {
    ObjectHelper.requireNonNull(paramPublisher, "source1 is null");
    ObjectHelper.requireNonNull(paramPublisher1, "source2 is null");
    ObjectHelper.requireNonNull(paramPublisher2, "source3 is null");
    return zipArray(Functions.toFunction(paramFunction3), false, bufferSize(), (Publisher<?>[])new Publisher[] { paramPublisher, paramPublisher1, paramPublisher2 });
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T1, T2, T3, T4, R> Flowable<R> zip(Publisher<? extends T1> paramPublisher, Publisher<? extends T2> paramPublisher1, Publisher<? extends T3> paramPublisher2, Publisher<? extends T4> paramPublisher3, Function4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> paramFunction4) {
    ObjectHelper.requireNonNull(paramPublisher, "source1 is null");
    ObjectHelper.requireNonNull(paramPublisher1, "source2 is null");
    ObjectHelper.requireNonNull(paramPublisher2, "source3 is null");
    ObjectHelper.requireNonNull(paramPublisher3, "source4 is null");
    return zipArray(Functions.toFunction(paramFunction4), false, bufferSize(), (Publisher<?>[])new Publisher[] { paramPublisher, paramPublisher1, paramPublisher2, paramPublisher3 });
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T1, T2, T3, T4, T5, R> Flowable<R> zip(Publisher<? extends T1> paramPublisher, Publisher<? extends T2> paramPublisher1, Publisher<? extends T3> paramPublisher2, Publisher<? extends T4> paramPublisher3, Publisher<? extends T5> paramPublisher4, Function5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? extends R> paramFunction5) {
    ObjectHelper.requireNonNull(paramPublisher, "source1 is null");
    ObjectHelper.requireNonNull(paramPublisher1, "source2 is null");
    ObjectHelper.requireNonNull(paramPublisher2, "source3 is null");
    ObjectHelper.requireNonNull(paramPublisher3, "source4 is null");
    ObjectHelper.requireNonNull(paramPublisher4, "source5 is null");
    return zipArray(Functions.toFunction(paramFunction5), false, bufferSize(), (Publisher<?>[])new Publisher[] { paramPublisher, paramPublisher1, paramPublisher2, paramPublisher3, paramPublisher4 });
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T1, T2, T3, T4, T5, T6, R> Flowable<R> zip(Publisher<? extends T1> paramPublisher, Publisher<? extends T2> paramPublisher1, Publisher<? extends T3> paramPublisher2, Publisher<? extends T4> paramPublisher3, Publisher<? extends T5> paramPublisher4, Publisher<? extends T6> paramPublisher5, Function6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends R> paramFunction6) {
    ObjectHelper.requireNonNull(paramPublisher, "source1 is null");
    ObjectHelper.requireNonNull(paramPublisher1, "source2 is null");
    ObjectHelper.requireNonNull(paramPublisher2, "source3 is null");
    ObjectHelper.requireNonNull(paramPublisher3, "source4 is null");
    ObjectHelper.requireNonNull(paramPublisher4, "source5 is null");
    ObjectHelper.requireNonNull(paramPublisher5, "source6 is null");
    return zipArray(Functions.toFunction(paramFunction6), false, bufferSize(), (Publisher<?>[])new Publisher[] { paramPublisher, paramPublisher1, paramPublisher2, paramPublisher3, paramPublisher4, paramPublisher5 });
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T1, T2, T3, T4, T5, T6, T7, R> Flowable<R> zip(Publisher<? extends T1> paramPublisher, Publisher<? extends T2> paramPublisher1, Publisher<? extends T3> paramPublisher2, Publisher<? extends T4> paramPublisher3, Publisher<? extends T5> paramPublisher4, Publisher<? extends T6> paramPublisher5, Publisher<? extends T7> paramPublisher6, Function7<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? extends R> paramFunction7) {
    ObjectHelper.requireNonNull(paramPublisher, "source1 is null");
    ObjectHelper.requireNonNull(paramPublisher1, "source2 is null");
    ObjectHelper.requireNonNull(paramPublisher2, "source3 is null");
    ObjectHelper.requireNonNull(paramPublisher3, "source4 is null");
    ObjectHelper.requireNonNull(paramPublisher4, "source5 is null");
    ObjectHelper.requireNonNull(paramPublisher5, "source6 is null");
    ObjectHelper.requireNonNull(paramPublisher6, "source7 is null");
    return zipArray(Functions.toFunction(paramFunction7), false, bufferSize(), (Publisher<?>[])new Publisher[] { paramPublisher, paramPublisher1, paramPublisher2, paramPublisher3, paramPublisher4, paramPublisher5, paramPublisher6 });
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T1, T2, T3, T4, T5, T6, T7, T8, R> Flowable<R> zip(Publisher<? extends T1> paramPublisher, Publisher<? extends T2> paramPublisher1, Publisher<? extends T3> paramPublisher2, Publisher<? extends T4> paramPublisher3, Publisher<? extends T5> paramPublisher4, Publisher<? extends T6> paramPublisher5, Publisher<? extends T7> paramPublisher6, Publisher<? extends T8> paramPublisher7, Function8<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? extends R> paramFunction8) {
    ObjectHelper.requireNonNull(paramPublisher, "source1 is null");
    ObjectHelper.requireNonNull(paramPublisher1, "source2 is null");
    ObjectHelper.requireNonNull(paramPublisher2, "source3 is null");
    ObjectHelper.requireNonNull(paramPublisher3, "source4 is null");
    ObjectHelper.requireNonNull(paramPublisher4, "source5 is null");
    ObjectHelper.requireNonNull(paramPublisher5, "source6 is null");
    ObjectHelper.requireNonNull(paramPublisher6, "source7 is null");
    ObjectHelper.requireNonNull(paramPublisher7, "source8 is null");
    return zipArray(Functions.toFunction(paramFunction8), false, bufferSize(), (Publisher<?>[])new Publisher[] { paramPublisher, paramPublisher1, paramPublisher2, paramPublisher3, paramPublisher4, paramPublisher5, paramPublisher6, paramPublisher7 });
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, R> Flowable<R> zip(Publisher<? extends T1> paramPublisher, Publisher<? extends T2> paramPublisher1, Publisher<? extends T3> paramPublisher2, Publisher<? extends T4> paramPublisher3, Publisher<? extends T5> paramPublisher4, Publisher<? extends T6> paramPublisher5, Publisher<? extends T7> paramPublisher6, Publisher<? extends T8> paramPublisher7, Publisher<? extends T9> paramPublisher8, Function9<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? extends R> paramFunction9) {
    ObjectHelper.requireNonNull(paramPublisher, "source1 is null");
    ObjectHelper.requireNonNull(paramPublisher1, "source2 is null");
    ObjectHelper.requireNonNull(paramPublisher2, "source3 is null");
    ObjectHelper.requireNonNull(paramPublisher3, "source4 is null");
    ObjectHelper.requireNonNull(paramPublisher4, "source5 is null");
    ObjectHelper.requireNonNull(paramPublisher5, "source6 is null");
    ObjectHelper.requireNonNull(paramPublisher6, "source7 is null");
    ObjectHelper.requireNonNull(paramPublisher7, "source8 is null");
    ObjectHelper.requireNonNull(paramPublisher8, "source9 is null");
    return zipArray(Functions.toFunction(paramFunction9), false, bufferSize(), (Publisher<?>[])new Publisher[] { paramPublisher, paramPublisher1, paramPublisher2, paramPublisher3, paramPublisher4, paramPublisher5, paramPublisher6, paramPublisher7, paramPublisher8 });
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T, R> Flowable<R> zipArray(Function<? super Object[], ? extends R> paramFunction, boolean paramBoolean, int paramInt, Publisher<? extends T>... paramVarArgs) {
    if (paramVarArgs.length == 0)
      return empty(); 
    ObjectHelper.requireNonNull(paramFunction, "zipper is null");
    ObjectHelper.verifyPositive(paramInt, "bufferSize");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableZip((Publisher[])paramVarArgs, null, paramFunction, paramInt, paramBoolean));
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public static <T, R> Flowable<R> zipIterable(Iterable<? extends Publisher<? extends T>> paramIterable, Function<? super Object[], ? extends R> paramFunction, boolean paramBoolean, int paramInt) {
    ObjectHelper.requireNonNull(paramFunction, "zipper is null");
    ObjectHelper.requireNonNull(paramIterable, "sources is null");
    ObjectHelper.verifyPositive(paramInt, "bufferSize");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableZip(null, paramIterable, paramFunction, paramInt, paramBoolean));
  }
  
  @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Single<Boolean> all(Predicate<? super T> paramPredicate) {
    ObjectHelper.requireNonNull(paramPredicate, "predicate is null");
    return RxJavaPlugins.onAssembly((Single)new FlowableAllSingle(this, paramPredicate));
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<T> ambWith(Publisher<? extends T> paramPublisher) {
    ObjectHelper.requireNonNull(paramPublisher, "other is null");
    return ambArray((Publisher<? extends T>[])new Publisher[] { this, paramPublisher });
  }
  
  @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Single<Boolean> any(Predicate<? super T> paramPredicate) {
    ObjectHelper.requireNonNull(paramPredicate, "predicate is null");
    return RxJavaPlugins.onAssembly((Single)new FlowableAnySingle(this, paramPredicate));
  }
  
  @BackpressureSupport(BackpressureKind.SPECIAL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <R> R as(FlowableConverter<T, ? extends R> paramFlowableConverter) {
    return (R)((FlowableConverter)ObjectHelper.requireNonNull(paramFlowableConverter, "converter is null")).apply(this);
  }
  
  @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final T blockingFirst() {
    BlockingFirstSubscriber blockingFirstSubscriber = new BlockingFirstSubscriber();
    subscribe((FlowableSubscriber<? super T>)blockingFirstSubscriber);
    Object object = blockingFirstSubscriber.blockingGet();
    if (object != null)
      return (T)object; 
    throw new NoSuchElementException();
  }
  
  @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final T blockingFirst(T paramT) {
    BlockingFirstSubscriber blockingFirstSubscriber = new BlockingFirstSubscriber();
    subscribe((FlowableSubscriber<? super T>)blockingFirstSubscriber);
    Object object = blockingFirstSubscriber.blockingGet();
    if (object != null)
      paramT = (T)object; 
    return paramT;
  }
  
  @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
  @SchedulerSupport("none")
  public final void blockingForEach(Consumer<? super T> paramConsumer) {
    Iterator<T> iterator = blockingIterable().iterator();
    while (iterator.hasNext()) {
      try {
        paramConsumer.accept(iterator.next());
      } finally {
        paramConsumer = null;
        Exceptions.throwIfFatal((Throwable)paramConsumer);
        ((Disposable)iterator).dispose();
      } 
    } 
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Iterable<T> blockingIterable() {
    return blockingIterable(bufferSize());
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Iterable<T> blockingIterable(int paramInt) {
    ObjectHelper.verifyPositive(paramInt, "bufferSize");
    return (Iterable<T>)new BlockingFlowableIterable(this, paramInt);
  }
  
  @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final T blockingLast() {
    BlockingLastSubscriber blockingLastSubscriber = new BlockingLastSubscriber();
    subscribe((FlowableSubscriber<? super T>)blockingLastSubscriber);
    Object object = blockingLastSubscriber.blockingGet();
    if (object != null)
      return (T)object; 
    throw new NoSuchElementException();
  }
  
  @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final T blockingLast(T paramT) {
    BlockingLastSubscriber blockingLastSubscriber = new BlockingLastSubscriber();
    subscribe((FlowableSubscriber<? super T>)blockingLastSubscriber);
    Object object = blockingLastSubscriber.blockingGet();
    if (object != null)
      paramT = (T)object; 
    return paramT;
  }
  
  @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Iterable<T> blockingLatest() {
    return (Iterable<T>)new BlockingFlowableLatest(this);
  }
  
  @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Iterable<T> blockingMostRecent(T paramT) {
    return (Iterable<T>)new BlockingFlowableMostRecent(this, paramT);
  }
  
  @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Iterable<T> blockingNext() {
    return (Iterable<T>)new BlockingFlowableNext(this);
  }
  
  @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final T blockingSingle() {
    return singleOrError().blockingGet();
  }
  
  @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final T blockingSingle(T paramT) {
    return single(paramT).blockingGet();
  }
  
  @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
  @SchedulerSupport("none")
  public final void blockingSubscribe() {
    FlowableBlockingSubscribe.subscribe(this);
  }
  
  @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
  @SchedulerSupport("none")
  public final void blockingSubscribe(Consumer<? super T> paramConsumer) {
    FlowableBlockingSubscribe.subscribe(this, paramConsumer, Functions.ON_ERROR_MISSING, Functions.EMPTY_ACTION);
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @SchedulerSupport("none")
  public final void blockingSubscribe(Consumer<? super T> paramConsumer, int paramInt) {
    FlowableBlockingSubscribe.subscribe(this, paramConsumer, Functions.ON_ERROR_MISSING, Functions.EMPTY_ACTION, paramInt);
  }
  
  @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
  @SchedulerSupport("none")
  public final void blockingSubscribe(Consumer<? super T> paramConsumer, Consumer<? super Throwable> paramConsumer1) {
    FlowableBlockingSubscribe.subscribe(this, paramConsumer, paramConsumer1, Functions.EMPTY_ACTION);
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @SchedulerSupport("none")
  public final void blockingSubscribe(Consumer<? super T> paramConsumer, Consumer<? super Throwable> paramConsumer1, int paramInt) {
    FlowableBlockingSubscribe.subscribe(this, paramConsumer, paramConsumer1, Functions.EMPTY_ACTION, paramInt);
  }
  
  @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
  @SchedulerSupport("none")
  public final void blockingSubscribe(Consumer<? super T> paramConsumer, Consumer<? super Throwable> paramConsumer1, Action paramAction) {
    FlowableBlockingSubscribe.subscribe(this, paramConsumer, paramConsumer1, paramAction);
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @SchedulerSupport("none")
  public final void blockingSubscribe(Consumer<? super T> paramConsumer, Consumer<? super Throwable> paramConsumer1, Action paramAction, int paramInt) {
    FlowableBlockingSubscribe.subscribe(this, paramConsumer, paramConsumer1, paramAction, paramInt);
  }
  
  @BackpressureSupport(BackpressureKind.SPECIAL)
  @SchedulerSupport("none")
  public final void blockingSubscribe(Subscriber<? super T> paramSubscriber) {
    FlowableBlockingSubscribe.subscribe(this, paramSubscriber);
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<List<T>> buffer(int paramInt) {
    return buffer(paramInt, paramInt);
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<List<T>> buffer(int paramInt1, int paramInt2) {
    return buffer(paramInt1, paramInt2, ArrayListSupplier.asCallable());
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <U extends Collection<? super T>> Flowable<U> buffer(int paramInt1, int paramInt2, Callable<U> paramCallable) {
    ObjectHelper.verifyPositive(paramInt1, "count");
    ObjectHelper.verifyPositive(paramInt2, "skip");
    ObjectHelper.requireNonNull(paramCallable, "bufferSupplier is null");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableBuffer(this, paramInt1, paramInt2, paramCallable));
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <U extends Collection<? super T>> Flowable<U> buffer(int paramInt, Callable<U> paramCallable) {
    return buffer(paramInt, paramInt, paramCallable);
  }
  
  @BackpressureSupport(BackpressureKind.ERROR)
  @CheckReturnValue
  @SchedulerSupport("io.reactivex:computation")
  public final Flowable<List<T>> buffer(long paramLong1, long paramLong2, TimeUnit paramTimeUnit) {
    return buffer(paramLong1, paramLong2, paramTimeUnit, Schedulers.computation(), ArrayListSupplier.asCallable());
  }
  
  @BackpressureSupport(BackpressureKind.ERROR)
  @CheckReturnValue
  @SchedulerSupport("custom")
  public final Flowable<List<T>> buffer(long paramLong1, long paramLong2, TimeUnit paramTimeUnit, Scheduler paramScheduler) {
    return buffer(paramLong1, paramLong2, paramTimeUnit, paramScheduler, ArrayListSupplier.asCallable());
  }
  
  @BackpressureSupport(BackpressureKind.ERROR)
  @CheckReturnValue
  @SchedulerSupport("custom")
  public final <U extends Collection<? super T>> Flowable<U> buffer(long paramLong1, long paramLong2, TimeUnit paramTimeUnit, Scheduler paramScheduler, Callable<U> paramCallable) {
    ObjectHelper.requireNonNull(paramTimeUnit, "unit is null");
    ObjectHelper.requireNonNull(paramScheduler, "scheduler is null");
    ObjectHelper.requireNonNull(paramCallable, "bufferSupplier is null");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableBufferTimed(this, paramLong1, paramLong2, paramTimeUnit, paramScheduler, paramCallable, 2147483647, false));
  }
  
  @BackpressureSupport(BackpressureKind.ERROR)
  @CheckReturnValue
  @SchedulerSupport("io.reactivex:computation")
  public final Flowable<List<T>> buffer(long paramLong, TimeUnit paramTimeUnit) {
    return buffer(paramLong, paramTimeUnit, Schedulers.computation(), 2147483647);
  }
  
  @BackpressureSupport(BackpressureKind.ERROR)
  @CheckReturnValue
  @SchedulerSupport("io.reactivex:computation")
  public final Flowable<List<T>> buffer(long paramLong, TimeUnit paramTimeUnit, int paramInt) {
    return buffer(paramLong, paramTimeUnit, Schedulers.computation(), paramInt);
  }
  
  @BackpressureSupport(BackpressureKind.ERROR)
  @CheckReturnValue
  @SchedulerSupport("custom")
  public final Flowable<List<T>> buffer(long paramLong, TimeUnit paramTimeUnit, Scheduler paramScheduler) {
    return buffer(paramLong, paramTimeUnit, paramScheduler, 2147483647, ArrayListSupplier.asCallable(), false);
  }
  
  @BackpressureSupport(BackpressureKind.ERROR)
  @CheckReturnValue
  @SchedulerSupport("custom")
  public final Flowable<List<T>> buffer(long paramLong, TimeUnit paramTimeUnit, Scheduler paramScheduler, int paramInt) {
    return buffer(paramLong, paramTimeUnit, paramScheduler, paramInt, ArrayListSupplier.asCallable(), false);
  }
  
  @BackpressureSupport(BackpressureKind.ERROR)
  @CheckReturnValue
  @SchedulerSupport("custom")
  public final <U extends Collection<? super T>> Flowable<U> buffer(long paramLong, TimeUnit paramTimeUnit, Scheduler paramScheduler, int paramInt, Callable<U> paramCallable, boolean paramBoolean) {
    ObjectHelper.requireNonNull(paramTimeUnit, "unit is null");
    ObjectHelper.requireNonNull(paramScheduler, "scheduler is null");
    ObjectHelper.requireNonNull(paramCallable, "bufferSupplier is null");
    ObjectHelper.verifyPositive(paramInt, "count");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableBufferTimed(this, paramLong, paramLong, paramTimeUnit, paramScheduler, paramCallable, paramInt, paramBoolean));
  }
  
  @BackpressureSupport(BackpressureKind.ERROR)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <TOpening, TClosing> Flowable<List<T>> buffer(Flowable<? extends TOpening> paramFlowable, Function<? super TOpening, ? extends Publisher<? extends TClosing>> paramFunction) {
    return buffer(paramFlowable, paramFunction, ArrayListSupplier.asCallable());
  }
  
  @BackpressureSupport(BackpressureKind.ERROR)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <TOpening, TClosing, U extends Collection<? super T>> Flowable<U> buffer(Flowable<? extends TOpening> paramFlowable, Function<? super TOpening, ? extends Publisher<? extends TClosing>> paramFunction, Callable<U> paramCallable) {
    ObjectHelper.requireNonNull(paramFlowable, "openingIndicator is null");
    ObjectHelper.requireNonNull(paramFunction, "closingIndicator is null");
    ObjectHelper.requireNonNull(paramCallable, "bufferSupplier is null");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableBufferBoundary(this, paramFlowable, paramFunction, paramCallable));
  }
  
  @BackpressureSupport(BackpressureKind.ERROR)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <B> Flowable<List<T>> buffer(Callable<? extends Publisher<B>> paramCallable) {
    return buffer(paramCallable, ArrayListSupplier.asCallable());
  }
  
  @BackpressureSupport(BackpressureKind.ERROR)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <B, U extends Collection<? super T>> Flowable<U> buffer(Callable<? extends Publisher<B>> paramCallable, Callable<U> paramCallable1) {
    ObjectHelper.requireNonNull(paramCallable, "boundaryIndicatorSupplier is null");
    ObjectHelper.requireNonNull(paramCallable1, "bufferSupplier is null");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableBufferBoundarySupplier(this, paramCallable, paramCallable1));
  }
  
  @BackpressureSupport(BackpressureKind.ERROR)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <B> Flowable<List<T>> buffer(Publisher<B> paramPublisher) {
    return buffer(paramPublisher, ArrayListSupplier.asCallable());
  }
  
  @BackpressureSupport(BackpressureKind.ERROR)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <B> Flowable<List<T>> buffer(Publisher<B> paramPublisher, int paramInt) {
    ObjectHelper.verifyPositive(paramInt, "initialCapacity");
    return buffer(paramPublisher, Functions.createArrayList(paramInt));
  }
  
  @BackpressureSupport(BackpressureKind.ERROR)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <B, U extends Collection<? super T>> Flowable<U> buffer(Publisher<B> paramPublisher, Callable<U> paramCallable) {
    ObjectHelper.requireNonNull(paramPublisher, "boundaryIndicator is null");
    ObjectHelper.requireNonNull(paramCallable, "bufferSupplier is null");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableBufferExactBoundary(this, paramPublisher, paramCallable));
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<T> cache() {
    return cacheWithInitialCapacity(16);
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<T> cacheWithInitialCapacity(int paramInt) {
    ObjectHelper.verifyPositive(paramInt, "initialCapacity");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableCache(this, paramInt));
  }
  
  @BackpressureSupport(BackpressureKind.PASS_THROUGH)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <U> Flowable<U> cast(Class<U> paramClass) {
    ObjectHelper.requireNonNull(paramClass, "clazz is null");
    return map(Functions.castFunction(paramClass));
  }
  
  @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <U> Single<U> collect(Callable<? extends U> paramCallable, BiConsumer<? super U, ? super T> paramBiConsumer) {
    ObjectHelper.requireNonNull(paramCallable, "initialItemSupplier is null");
    ObjectHelper.requireNonNull(paramBiConsumer, "collector is null");
    return RxJavaPlugins.onAssembly((Single)new FlowableCollectSingle(this, paramCallable, paramBiConsumer));
  }
  
  @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <U> Single<U> collectInto(U paramU, BiConsumer<? super U, ? super T> paramBiConsumer) {
    ObjectHelper.requireNonNull(paramU, "initialItem is null");
    return collect(Functions.justCallable(paramU), paramBiConsumer);
  }
  
  @BackpressureSupport(BackpressureKind.PASS_THROUGH)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <R> Flowable<R> compose(FlowableTransformer<? super T, ? extends R> paramFlowableTransformer) {
    return fromPublisher(((FlowableTransformer)ObjectHelper.requireNonNull(paramFlowableTransformer, "composer is null")).apply(this));
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <R> Flowable<R> concatMap(Function<? super T, ? extends Publisher<? extends R>> paramFunction) {
    return concatMap(paramFunction, 2);
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <R> Flowable<R> concatMap(Function<? super T, ? extends Publisher<? extends R>> paramFunction, int paramInt) {
    ObjectHelper.requireNonNull(paramFunction, "mapper is null");
    ObjectHelper.verifyPositive(paramInt, "prefetch");
    if (this instanceof ScalarCallable) {
      Object object = ((ScalarCallable)this).call();
      return (object == null) ? empty() : FlowableScalarXMap.scalarXMap(object, paramFunction);
    } 
    return RxJavaPlugins.onAssembly((Flowable)new FlowableConcatMap(this, paramFunction, paramInt, ErrorMode.IMMEDIATE));
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Completable concatMapCompletable(Function<? super T, ? extends CompletableSource> paramFunction) {
    return concatMapCompletable(paramFunction, 2);
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Completable concatMapCompletable(Function<? super T, ? extends CompletableSource> paramFunction, int paramInt) {
    ObjectHelper.requireNonNull(paramFunction, "mapper is null");
    ObjectHelper.verifyPositive(paramInt, "prefetch");
    return RxJavaPlugins.onAssembly((Completable)new FlowableConcatMapCompletable(this, paramFunction, ErrorMode.IMMEDIATE, paramInt));
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Completable concatMapCompletableDelayError(Function<? super T, ? extends CompletableSource> paramFunction) {
    return concatMapCompletableDelayError(paramFunction, true, 2);
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Completable concatMapCompletableDelayError(Function<? super T, ? extends CompletableSource> paramFunction, boolean paramBoolean) {
    return concatMapCompletableDelayError(paramFunction, paramBoolean, 2);
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Completable concatMapCompletableDelayError(Function<? super T, ? extends CompletableSource> paramFunction, boolean paramBoolean, int paramInt) {
    ErrorMode errorMode;
    ObjectHelper.requireNonNull(paramFunction, "mapper is null");
    ObjectHelper.verifyPositive(paramInt, "prefetch");
    if (paramBoolean) {
      errorMode = ErrorMode.END;
    } else {
      errorMode = ErrorMode.BOUNDARY;
    } 
    return RxJavaPlugins.onAssembly((Completable)new FlowableConcatMapCompletable(this, paramFunction, errorMode, paramInt));
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <R> Flowable<R> concatMapDelayError(Function<? super T, ? extends Publisher<? extends R>> paramFunction) {
    return concatMapDelayError(paramFunction, 2, true);
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <R> Flowable<R> concatMapDelayError(Function<? super T, ? extends Publisher<? extends R>> paramFunction, int paramInt, boolean paramBoolean) {
    Object object;
    ObjectHelper.requireNonNull(paramFunction, "mapper is null");
    ObjectHelper.verifyPositive(paramInt, "prefetch");
    if (this instanceof ScalarCallable) {
      object = ((ScalarCallable)this).call();
      return (object == null) ? empty() : FlowableScalarXMap.scalarXMap(object, paramFunction);
    } 
    if (paramBoolean) {
      object = ErrorMode.END;
    } else {
      object = ErrorMode.BOUNDARY;
    } 
    return RxJavaPlugins.onAssembly((Flowable)new FlowableConcatMap(this, paramFunction, paramInt, (ErrorMode)object));
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <R> Flowable<R> concatMapEager(Function<? super T, ? extends Publisher<? extends R>> paramFunction) {
    return concatMapEager(paramFunction, bufferSize(), bufferSize());
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <R> Flowable<R> concatMapEager(Function<? super T, ? extends Publisher<? extends R>> paramFunction, int paramInt1, int paramInt2) {
    ObjectHelper.requireNonNull(paramFunction, "mapper is null");
    ObjectHelper.verifyPositive(paramInt1, "maxConcurrency");
    ObjectHelper.verifyPositive(paramInt2, "prefetch");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableConcatMapEager(this, paramFunction, paramInt1, paramInt2, ErrorMode.IMMEDIATE));
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <R> Flowable<R> concatMapEagerDelayError(Function<? super T, ? extends Publisher<? extends R>> paramFunction, int paramInt1, int paramInt2, boolean paramBoolean) {
    ErrorMode errorMode;
    ObjectHelper.requireNonNull(paramFunction, "mapper is null");
    ObjectHelper.verifyPositive(paramInt1, "maxConcurrency");
    ObjectHelper.verifyPositive(paramInt2, "prefetch");
    if (paramBoolean) {
      errorMode = ErrorMode.END;
    } else {
      errorMode = ErrorMode.BOUNDARY;
    } 
    return RxJavaPlugins.onAssembly((Flowable)new FlowableConcatMapEager(this, paramFunction, paramInt1, paramInt2, errorMode));
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <R> Flowable<R> concatMapEagerDelayError(Function<? super T, ? extends Publisher<? extends R>> paramFunction, boolean paramBoolean) {
    return concatMapEagerDelayError(paramFunction, bufferSize(), bufferSize(), paramBoolean);
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <U> Flowable<U> concatMapIterable(Function<? super T, ? extends Iterable<? extends U>> paramFunction) {
    return concatMapIterable(paramFunction, 2);
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <U> Flowable<U> concatMapIterable(Function<? super T, ? extends Iterable<? extends U>> paramFunction, int paramInt) {
    ObjectHelper.requireNonNull(paramFunction, "mapper is null");
    ObjectHelper.verifyPositive(paramInt, "prefetch");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableFlattenIterable(this, paramFunction, paramInt));
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <R> Flowable<R> concatMapMaybe(Function<? super T, ? extends MaybeSource<? extends R>> paramFunction) {
    return concatMapMaybe(paramFunction, 2);
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <R> Flowable<R> concatMapMaybe(Function<? super T, ? extends MaybeSource<? extends R>> paramFunction, int paramInt) {
    ObjectHelper.requireNonNull(paramFunction, "mapper is null");
    ObjectHelper.verifyPositive(paramInt, "prefetch");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableConcatMapMaybe(this, paramFunction, ErrorMode.IMMEDIATE, paramInt));
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <R> Flowable<R> concatMapMaybeDelayError(Function<? super T, ? extends MaybeSource<? extends R>> paramFunction) {
    return concatMapMaybeDelayError(paramFunction, true, 2);
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <R> Flowable<R> concatMapMaybeDelayError(Function<? super T, ? extends MaybeSource<? extends R>> paramFunction, boolean paramBoolean) {
    return concatMapMaybeDelayError(paramFunction, paramBoolean, 2);
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <R> Flowable<R> concatMapMaybeDelayError(Function<? super T, ? extends MaybeSource<? extends R>> paramFunction, boolean paramBoolean, int paramInt) {
    ErrorMode errorMode;
    ObjectHelper.requireNonNull(paramFunction, "mapper is null");
    ObjectHelper.verifyPositive(paramInt, "prefetch");
    if (paramBoolean) {
      errorMode = ErrorMode.END;
    } else {
      errorMode = ErrorMode.BOUNDARY;
    } 
    return RxJavaPlugins.onAssembly((Flowable)new FlowableConcatMapMaybe(this, paramFunction, errorMode, paramInt));
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <R> Flowable<R> concatMapSingle(Function<? super T, ? extends SingleSource<? extends R>> paramFunction) {
    return concatMapSingle(paramFunction, 2);
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <R> Flowable<R> concatMapSingle(Function<? super T, ? extends SingleSource<? extends R>> paramFunction, int paramInt) {
    ObjectHelper.requireNonNull(paramFunction, "mapper is null");
    ObjectHelper.verifyPositive(paramInt, "prefetch");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableConcatMapSingle(this, paramFunction, ErrorMode.IMMEDIATE, paramInt));
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <R> Flowable<R> concatMapSingleDelayError(Function<? super T, ? extends SingleSource<? extends R>> paramFunction) {
    return concatMapSingleDelayError(paramFunction, true, 2);
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <R> Flowable<R> concatMapSingleDelayError(Function<? super T, ? extends SingleSource<? extends R>> paramFunction, boolean paramBoolean) {
    return concatMapSingleDelayError(paramFunction, paramBoolean, 2);
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <R> Flowable<R> concatMapSingleDelayError(Function<? super T, ? extends SingleSource<? extends R>> paramFunction, boolean paramBoolean, int paramInt) {
    ErrorMode errorMode;
    ObjectHelper.requireNonNull(paramFunction, "mapper is null");
    ObjectHelper.verifyPositive(paramInt, "prefetch");
    if (paramBoolean) {
      errorMode = ErrorMode.END;
    } else {
      errorMode = ErrorMode.BOUNDARY;
    } 
    return RxJavaPlugins.onAssembly((Flowable)new FlowableConcatMapSingle(this, paramFunction, errorMode, paramInt));
  }
  
  @BackpressureSupport(BackpressureKind.PASS_THROUGH)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<T> concatWith(CompletableSource paramCompletableSource) {
    ObjectHelper.requireNonNull(paramCompletableSource, "other is null");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableConcatWithCompletable(this, paramCompletableSource));
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<T> concatWith(MaybeSource<? extends T> paramMaybeSource) {
    ObjectHelper.requireNonNull(paramMaybeSource, "other is null");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableConcatWithMaybe(this, paramMaybeSource));
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<T> concatWith(SingleSource<? extends T> paramSingleSource) {
    ObjectHelper.requireNonNull(paramSingleSource, "other is null");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableConcatWithSingle(this, paramSingleSource));
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<T> concatWith(Publisher<? extends T> paramPublisher) {
    ObjectHelper.requireNonNull(paramPublisher, "other is null");
    return concat(this, paramPublisher);
  }
  
  @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Single<Boolean> contains(Object paramObject) {
    ObjectHelper.requireNonNull(paramObject, "item is null");
    return any(Functions.equalsWith(paramObject));
  }
  
  @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Single<Long> count() {
    return RxJavaPlugins.onAssembly((Single)new FlowableCountSingle(this));
  }
  
  @BackpressureSupport(BackpressureKind.ERROR)
  @CheckReturnValue
  @SchedulerSupport("io.reactivex:computation")
  public final Flowable<T> debounce(long paramLong, TimeUnit paramTimeUnit) {
    return debounce(paramLong, paramTimeUnit, Schedulers.computation());
  }
  
  @BackpressureSupport(BackpressureKind.ERROR)
  @CheckReturnValue
  @SchedulerSupport("custom")
  public final Flowable<T> debounce(long paramLong, TimeUnit paramTimeUnit, Scheduler paramScheduler) {
    ObjectHelper.requireNonNull(paramTimeUnit, "unit is null");
    ObjectHelper.requireNonNull(paramScheduler, "scheduler is null");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableDebounceTimed(this, paramLong, paramTimeUnit, paramScheduler));
  }
  
  @BackpressureSupport(BackpressureKind.ERROR)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <U> Flowable<T> debounce(Function<? super T, ? extends Publisher<U>> paramFunction) {
    ObjectHelper.requireNonNull(paramFunction, "debounceIndicator is null");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableDebounce(this, paramFunction));
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<T> defaultIfEmpty(T paramT) {
    ObjectHelper.requireNonNull(paramT, "defaultItem is null");
    return switchIfEmpty(just(paramT));
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("io.reactivex:computation")
  public final Flowable<T> delay(long paramLong, TimeUnit paramTimeUnit) {
    return delay(paramLong, paramTimeUnit, Schedulers.computation(), false);
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("custom")
  public final Flowable<T> delay(long paramLong, TimeUnit paramTimeUnit, Scheduler paramScheduler) {
    return delay(paramLong, paramTimeUnit, paramScheduler, false);
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("custom")
  public final Flowable<T> delay(long paramLong, TimeUnit paramTimeUnit, Scheduler paramScheduler, boolean paramBoolean) {
    ObjectHelper.requireNonNull(paramTimeUnit, "unit is null");
    ObjectHelper.requireNonNull(paramScheduler, "scheduler is null");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableDelay(this, Math.max(0L, paramLong), paramTimeUnit, paramScheduler, paramBoolean));
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("io.reactivex:computation")
  public final Flowable<T> delay(long paramLong, TimeUnit paramTimeUnit, boolean paramBoolean) {
    return delay(paramLong, paramTimeUnit, Schedulers.computation(), paramBoolean);
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <U> Flowable<T> delay(Function<? super T, ? extends Publisher<U>> paramFunction) {
    ObjectHelper.requireNonNull(paramFunction, "itemDelayIndicator is null");
    return flatMap(FlowableInternalHelper.itemDelay(paramFunction));
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <U, V> Flowable<T> delay(Publisher<U> paramPublisher, Function<? super T, ? extends Publisher<V>> paramFunction) {
    return delaySubscription(paramPublisher).delay(paramFunction);
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("io.reactivex:computation")
  public final Flowable<T> delaySubscription(long paramLong, TimeUnit paramTimeUnit) {
    return delaySubscription(paramLong, paramTimeUnit, Schedulers.computation());
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("custom")
  public final Flowable<T> delaySubscription(long paramLong, TimeUnit paramTimeUnit, Scheduler paramScheduler) {
    return delaySubscription(timer(paramLong, paramTimeUnit, paramScheduler));
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <U> Flowable<T> delaySubscription(Publisher<U> paramPublisher) {
    ObjectHelper.requireNonNull(paramPublisher, "subscriptionIndicator is null");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableDelaySubscriptionOther(this, paramPublisher));
  }
  
  @BackpressureSupport(BackpressureKind.PASS_THROUGH)
  @CheckReturnValue
  @SchedulerSupport("none")
  @Deprecated
  public final <T2> Flowable<T2> dematerialize() {
    return RxJavaPlugins.onAssembly((Flowable)new FlowableDematerialize(this, Functions.identity()));
  }
  
  @BackpressureSupport(BackpressureKind.PASS_THROUGH)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <R> Flowable<R> dematerialize(Function<? super T, Notification<R>> paramFunction) {
    ObjectHelper.requireNonNull(paramFunction, "selector is null");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableDematerialize(this, paramFunction));
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<T> distinct() {
    return distinct(Functions.identity(), Functions.createHashSet());
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <K> Flowable<T> distinct(Function<? super T, K> paramFunction) {
    return distinct(paramFunction, Functions.createHashSet());
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <K> Flowable<T> distinct(Function<? super T, K> paramFunction, Callable<? extends Collection<? super K>> paramCallable) {
    ObjectHelper.requireNonNull(paramFunction, "keySelector is null");
    ObjectHelper.requireNonNull(paramCallable, "collectionSupplier is null");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableDistinct(this, paramFunction, paramCallable));
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<T> distinctUntilChanged() {
    return distinctUntilChanged(Functions.identity());
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<T> distinctUntilChanged(BiPredicate<? super T, ? super T> paramBiPredicate) {
    ObjectHelper.requireNonNull(paramBiPredicate, "comparer is null");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableDistinctUntilChanged(this, Functions.identity(), paramBiPredicate));
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <K> Flowable<T> distinctUntilChanged(Function<? super T, K> paramFunction) {
    ObjectHelper.requireNonNull(paramFunction, "keySelector is null");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableDistinctUntilChanged(this, paramFunction, ObjectHelper.equalsPredicate()));
  }
  
  @BackpressureSupport(BackpressureKind.PASS_THROUGH)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<T> doAfterNext(Consumer<? super T> paramConsumer) {
    ObjectHelper.requireNonNull(paramConsumer, "onAfterNext is null");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableDoAfterNext(this, paramConsumer));
  }
  
  @BackpressureSupport(BackpressureKind.PASS_THROUGH)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<T> doAfterTerminate(Action paramAction) {
    return doOnEach(Functions.emptyConsumer(), Functions.emptyConsumer(), Functions.EMPTY_ACTION, paramAction);
  }
  
  @BackpressureSupport(BackpressureKind.PASS_THROUGH)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<T> doFinally(Action paramAction) {
    ObjectHelper.requireNonNull(paramAction, "onFinally is null");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableDoFinally(this, paramAction));
  }
  
  @BackpressureSupport(BackpressureKind.PASS_THROUGH)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<T> doOnCancel(Action paramAction) {
    return doOnLifecycle(Functions.emptyConsumer(), Functions.EMPTY_LONG_CONSUMER, paramAction);
  }
  
  @BackpressureSupport(BackpressureKind.PASS_THROUGH)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<T> doOnComplete(Action paramAction) {
    return doOnEach(Functions.emptyConsumer(), Functions.emptyConsumer(), paramAction, Functions.EMPTY_ACTION);
  }
  
  @BackpressureSupport(BackpressureKind.PASS_THROUGH)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<T> doOnEach(Consumer<? super Notification<T>> paramConsumer) {
    ObjectHelper.requireNonNull(paramConsumer, "onNotification is null");
    return doOnEach(Functions.notificationOnNext(paramConsumer), Functions.notificationOnError(paramConsumer), Functions.notificationOnComplete(paramConsumer), Functions.EMPTY_ACTION);
  }
  
  @BackpressureSupport(BackpressureKind.PASS_THROUGH)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<T> doOnEach(Subscriber<? super T> paramSubscriber) {
    ObjectHelper.requireNonNull(paramSubscriber, "subscriber is null");
    return doOnEach(FlowableInternalHelper.subscriberOnNext(paramSubscriber), FlowableInternalHelper.subscriberOnError(paramSubscriber), FlowableInternalHelper.subscriberOnComplete(paramSubscriber), Functions.EMPTY_ACTION);
  }
  
  @BackpressureSupport(BackpressureKind.PASS_THROUGH)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<T> doOnError(Consumer<? super Throwable> paramConsumer) {
    return doOnEach(Functions.emptyConsumer(), paramConsumer, Functions.EMPTY_ACTION, Functions.EMPTY_ACTION);
  }
  
  @BackpressureSupport(BackpressureKind.PASS_THROUGH)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<T> doOnLifecycle(Consumer<? super Subscription> paramConsumer, LongConsumer paramLongConsumer, Action paramAction) {
    ObjectHelper.requireNonNull(paramConsumer, "onSubscribe is null");
    ObjectHelper.requireNonNull(paramLongConsumer, "onRequest is null");
    ObjectHelper.requireNonNull(paramAction, "onCancel is null");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableDoOnLifecycle(this, paramConsumer, paramLongConsumer, paramAction));
  }
  
  @BackpressureSupport(BackpressureKind.PASS_THROUGH)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<T> doOnNext(Consumer<? super T> paramConsumer) {
    return doOnEach(paramConsumer, Functions.emptyConsumer(), Functions.EMPTY_ACTION, Functions.EMPTY_ACTION);
  }
  
  @BackpressureSupport(BackpressureKind.PASS_THROUGH)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<T> doOnRequest(LongConsumer paramLongConsumer) {
    return doOnLifecycle(Functions.emptyConsumer(), paramLongConsumer, Functions.EMPTY_ACTION);
  }
  
  @BackpressureSupport(BackpressureKind.PASS_THROUGH)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<T> doOnSubscribe(Consumer<? super Subscription> paramConsumer) {
    return doOnLifecycle(paramConsumer, Functions.EMPTY_LONG_CONSUMER, Functions.EMPTY_ACTION);
  }
  
  @BackpressureSupport(BackpressureKind.PASS_THROUGH)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<T> doOnTerminate(Action paramAction) {
    return doOnEach(Functions.emptyConsumer(), Functions.actionConsumer(paramAction), paramAction, Functions.EMPTY_ACTION);
  }
  
  @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Maybe<T> elementAt(long paramLong) {
    if (paramLong >= 0L)
      return RxJavaPlugins.onAssembly((Maybe)new FlowableElementAtMaybe(this, paramLong)); 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("index >= 0 required but it was ");
    stringBuilder.append(paramLong);
    throw new IndexOutOfBoundsException(stringBuilder.toString());
  }
  
  @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Single<T> elementAt(long paramLong, T paramT) {
    if (paramLong >= 0L) {
      ObjectHelper.requireNonNull(paramT, "defaultItem is null");
      return RxJavaPlugins.onAssembly((Single)new FlowableElementAtSingle(this, paramLong, paramT));
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("index >= 0 required but it was ");
    stringBuilder.append(paramLong);
    throw new IndexOutOfBoundsException(stringBuilder.toString());
  }
  
  @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Single<T> elementAtOrError(long paramLong) {
    if (paramLong >= 0L)
      return RxJavaPlugins.onAssembly((Single)new FlowableElementAtSingle(this, paramLong, null)); 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("index >= 0 required but it was ");
    stringBuilder.append(paramLong);
    throw new IndexOutOfBoundsException(stringBuilder.toString());
  }
  
  @BackpressureSupport(BackpressureKind.PASS_THROUGH)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<T> filter(Predicate<? super T> paramPredicate) {
    ObjectHelper.requireNonNull(paramPredicate, "predicate is null");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableFilter(this, paramPredicate));
  }
  
  @BackpressureSupport(BackpressureKind.SPECIAL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Single<T> first(T paramT) {
    return elementAt(0L, paramT);
  }
  
  @BackpressureSupport(BackpressureKind.SPECIAL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Maybe<T> firstElement() {
    return elementAt(0L);
  }
  
  @BackpressureSupport(BackpressureKind.SPECIAL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Single<T> firstOrError() {
    return elementAtOrError(0L);
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <R> Flowable<R> flatMap(Function<? super T, ? extends Publisher<? extends R>> paramFunction) {
    return flatMap(paramFunction, false, bufferSize(), bufferSize());
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <R> Flowable<R> flatMap(Function<? super T, ? extends Publisher<? extends R>> paramFunction, int paramInt) {
    return flatMap(paramFunction, false, paramInt, bufferSize());
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <U, R> Flowable<R> flatMap(Function<? super T, ? extends Publisher<? extends U>> paramFunction, BiFunction<? super T, ? super U, ? extends R> paramBiFunction) {
    return flatMap(paramFunction, paramBiFunction, false, bufferSize(), bufferSize());
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <U, R> Flowable<R> flatMap(Function<? super T, ? extends Publisher<? extends U>> paramFunction, BiFunction<? super T, ? super U, ? extends R> paramBiFunction, int paramInt) {
    return flatMap(paramFunction, paramBiFunction, false, paramInt, bufferSize());
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <U, R> Flowable<R> flatMap(Function<? super T, ? extends Publisher<? extends U>> paramFunction, BiFunction<? super T, ? super U, ? extends R> paramBiFunction, boolean paramBoolean) {
    return flatMap(paramFunction, paramBiFunction, paramBoolean, bufferSize(), bufferSize());
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <U, R> Flowable<R> flatMap(Function<? super T, ? extends Publisher<? extends U>> paramFunction, BiFunction<? super T, ? super U, ? extends R> paramBiFunction, boolean paramBoolean, int paramInt) {
    return flatMap(paramFunction, paramBiFunction, paramBoolean, paramInt, bufferSize());
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <U, R> Flowable<R> flatMap(Function<? super T, ? extends Publisher<? extends U>> paramFunction, BiFunction<? super T, ? super U, ? extends R> paramBiFunction, boolean paramBoolean, int paramInt1, int paramInt2) {
    ObjectHelper.requireNonNull(paramFunction, "mapper is null");
    ObjectHelper.requireNonNull(paramBiFunction, "combiner is null");
    ObjectHelper.verifyPositive(paramInt1, "maxConcurrency");
    ObjectHelper.verifyPositive(paramInt2, "bufferSize");
    return flatMap(FlowableInternalHelper.flatMapWithCombiner(paramFunction, paramBiFunction), paramBoolean, paramInt1, paramInt2);
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <R> Flowable<R> flatMap(Function<? super T, ? extends Publisher<? extends R>> paramFunction, Function<? super Throwable, ? extends Publisher<? extends R>> paramFunction1, Callable<? extends Publisher<? extends R>> paramCallable) {
    ObjectHelper.requireNonNull(paramFunction, "onNextMapper is null");
    ObjectHelper.requireNonNull(paramFunction1, "onErrorMapper is null");
    ObjectHelper.requireNonNull(paramCallable, "onCompleteSupplier is null");
    return merge((Publisher<? extends Publisher<? extends R>>)new FlowableMapNotification(this, paramFunction, paramFunction1, paramCallable));
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <R> Flowable<R> flatMap(Function<? super T, ? extends Publisher<? extends R>> paramFunction, Function<Throwable, ? extends Publisher<? extends R>> paramFunction1, Callable<? extends Publisher<? extends R>> paramCallable, int paramInt) {
    ObjectHelper.requireNonNull(paramFunction, "onNextMapper is null");
    ObjectHelper.requireNonNull(paramFunction1, "onErrorMapper is null");
    ObjectHelper.requireNonNull(paramCallable, "onCompleteSupplier is null");
    return merge((Publisher<? extends Publisher<? extends R>>)new FlowableMapNotification(this, paramFunction, paramFunction1, paramCallable), paramInt);
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <R> Flowable<R> flatMap(Function<? super T, ? extends Publisher<? extends R>> paramFunction, boolean paramBoolean) {
    return flatMap(paramFunction, paramBoolean, bufferSize(), bufferSize());
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <R> Flowable<R> flatMap(Function<? super T, ? extends Publisher<? extends R>> paramFunction, boolean paramBoolean, int paramInt) {
    return flatMap(paramFunction, paramBoolean, paramInt, bufferSize());
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <R> Flowable<R> flatMap(Function<? super T, ? extends Publisher<? extends R>> paramFunction, boolean paramBoolean, int paramInt1, int paramInt2) {
    ObjectHelper.requireNonNull(paramFunction, "mapper is null");
    ObjectHelper.verifyPositive(paramInt1, "maxConcurrency");
    ObjectHelper.verifyPositive(paramInt2, "bufferSize");
    if (this instanceof ScalarCallable) {
      Object object = ((ScalarCallable)this).call();
      return (object == null) ? empty() : FlowableScalarXMap.scalarXMap(object, paramFunction);
    } 
    return RxJavaPlugins.onAssembly((Flowable)new FlowableFlatMap(this, paramFunction, paramBoolean, paramInt1, paramInt2));
  }
  
  @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Completable flatMapCompletable(Function<? super T, ? extends CompletableSource> paramFunction) {
    return flatMapCompletable(paramFunction, false, 2147483647);
  }
  
  @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Completable flatMapCompletable(Function<? super T, ? extends CompletableSource> paramFunction, boolean paramBoolean, int paramInt) {
    ObjectHelper.requireNonNull(paramFunction, "mapper is null");
    ObjectHelper.verifyPositive(paramInt, "maxConcurrency");
    return RxJavaPlugins.onAssembly((Completable)new FlowableFlatMapCompletableCompletable(this, paramFunction, paramBoolean, paramInt));
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <U> Flowable<U> flatMapIterable(Function<? super T, ? extends Iterable<? extends U>> paramFunction) {
    return flatMapIterable(paramFunction, bufferSize());
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <U> Flowable<U> flatMapIterable(Function<? super T, ? extends Iterable<? extends U>> paramFunction, int paramInt) {
    ObjectHelper.requireNonNull(paramFunction, "mapper is null");
    ObjectHelper.verifyPositive(paramInt, "bufferSize");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableFlattenIterable(this, paramFunction, paramInt));
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <U, V> Flowable<V> flatMapIterable(Function<? super T, ? extends Iterable<? extends U>> paramFunction, BiFunction<? super T, ? super U, ? extends V> paramBiFunction) {
    ObjectHelper.requireNonNull(paramFunction, "mapper is null");
    ObjectHelper.requireNonNull(paramBiFunction, "resultSelector is null");
    return flatMap(FlowableInternalHelper.flatMapIntoIterable(paramFunction), paramBiFunction, false, bufferSize(), bufferSize());
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <U, V> Flowable<V> flatMapIterable(Function<? super T, ? extends Iterable<? extends U>> paramFunction, BiFunction<? super T, ? super U, ? extends V> paramBiFunction, int paramInt) {
    ObjectHelper.requireNonNull(paramFunction, "mapper is null");
    ObjectHelper.requireNonNull(paramBiFunction, "resultSelector is null");
    return flatMap(FlowableInternalHelper.flatMapIntoIterable(paramFunction), paramBiFunction, false, bufferSize(), paramInt);
  }
  
  @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <R> Flowable<R> flatMapMaybe(Function<? super T, ? extends MaybeSource<? extends R>> paramFunction) {
    return flatMapMaybe(paramFunction, false, 2147483647);
  }
  
  @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <R> Flowable<R> flatMapMaybe(Function<? super T, ? extends MaybeSource<? extends R>> paramFunction, boolean paramBoolean, int paramInt) {
    ObjectHelper.requireNonNull(paramFunction, "mapper is null");
    ObjectHelper.verifyPositive(paramInt, "maxConcurrency");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableFlatMapMaybe(this, paramFunction, paramBoolean, paramInt));
  }
  
  @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <R> Flowable<R> flatMapSingle(Function<? super T, ? extends SingleSource<? extends R>> paramFunction) {
    return flatMapSingle(paramFunction, false, 2147483647);
  }
  
  @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <R> Flowable<R> flatMapSingle(Function<? super T, ? extends SingleSource<? extends R>> paramFunction, boolean paramBoolean, int paramInt) {
    ObjectHelper.requireNonNull(paramFunction, "mapper is null");
    ObjectHelper.verifyPositive(paramInt, "maxConcurrency");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableFlatMapSingle(this, paramFunction, paramBoolean, paramInt));
  }
  
  @BackpressureSupport(BackpressureKind.NONE)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Disposable forEach(Consumer<? super T> paramConsumer) {
    return subscribe(paramConsumer);
  }
  
  @BackpressureSupport(BackpressureKind.NONE)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Disposable forEachWhile(Predicate<? super T> paramPredicate) {
    return forEachWhile(paramPredicate, Functions.ON_ERROR_MISSING, Functions.EMPTY_ACTION);
  }
  
  @BackpressureSupport(BackpressureKind.NONE)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Disposable forEachWhile(Predicate<? super T> paramPredicate, Consumer<? super Throwable> paramConsumer) {
    return forEachWhile(paramPredicate, paramConsumer, Functions.EMPTY_ACTION);
  }
  
  @BackpressureSupport(BackpressureKind.NONE)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Disposable forEachWhile(Predicate<? super T> paramPredicate, Consumer<? super Throwable> paramConsumer, Action paramAction) {
    ObjectHelper.requireNonNull(paramPredicate, "onNext is null");
    ObjectHelper.requireNonNull(paramConsumer, "onError is null");
    ObjectHelper.requireNonNull(paramAction, "onComplete is null");
    ForEachWhileSubscriber forEachWhileSubscriber = new ForEachWhileSubscriber(paramPredicate, paramConsumer, paramAction);
    subscribe((FlowableSubscriber<? super T>)forEachWhileSubscriber);
    return (Disposable)forEachWhileSubscriber;
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <K> Flowable<GroupedFlowable<K, T>> groupBy(Function<? super T, ? extends K> paramFunction) {
    return groupBy(paramFunction, Functions.identity(), false, bufferSize());
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <K, V> Flowable<GroupedFlowable<K, V>> groupBy(Function<? super T, ? extends K> paramFunction, Function<? super T, ? extends V> paramFunction1) {
    return groupBy(paramFunction, paramFunction1, false, bufferSize());
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <K, V> Flowable<GroupedFlowable<K, V>> groupBy(Function<? super T, ? extends K> paramFunction, Function<? super T, ? extends V> paramFunction1, boolean paramBoolean) {
    return groupBy(paramFunction, paramFunction1, paramBoolean, bufferSize());
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <K, V> Flowable<GroupedFlowable<K, V>> groupBy(Function<? super T, ? extends K> paramFunction, Function<? super T, ? extends V> paramFunction1, boolean paramBoolean, int paramInt) {
    ObjectHelper.requireNonNull(paramFunction, "keySelector is null");
    ObjectHelper.requireNonNull(paramFunction1, "valueSelector is null");
    ObjectHelper.verifyPositive(paramInt, "bufferSize");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableGroupBy(this, paramFunction, paramFunction1, paramInt, paramBoolean, null));
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <K, V> Flowable<GroupedFlowable<K, V>> groupBy(Function<? super T, ? extends K> paramFunction, Function<? super T, ? extends V> paramFunction1, boolean paramBoolean, int paramInt, Function<? super Consumer<Object>, ? extends Map<K, Object>> paramFunction2) {
    ObjectHelper.requireNonNull(paramFunction, "keySelector is null");
    ObjectHelper.requireNonNull(paramFunction1, "valueSelector is null");
    ObjectHelper.verifyPositive(paramInt, "bufferSize");
    ObjectHelper.requireNonNull(paramFunction2, "evictingMapFactory is null");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableGroupBy(this, paramFunction, paramFunction1, paramInt, paramBoolean, paramFunction2));
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <K> Flowable<GroupedFlowable<K, T>> groupBy(Function<? super T, ? extends K> paramFunction, boolean paramBoolean) {
    return groupBy(paramFunction, Functions.identity(), paramBoolean, bufferSize());
  }
  
  @BackpressureSupport(BackpressureKind.ERROR)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <TRight, TLeftEnd, TRightEnd, R> Flowable<R> groupJoin(Publisher<? extends TRight> paramPublisher, Function<? super T, ? extends Publisher<TLeftEnd>> paramFunction, Function<? super TRight, ? extends Publisher<TRightEnd>> paramFunction1, BiFunction<? super T, ? super Flowable<TRight>, ? extends R> paramBiFunction) {
    ObjectHelper.requireNonNull(paramPublisher, "other is null");
    ObjectHelper.requireNonNull(paramFunction, "leftEnd is null");
    ObjectHelper.requireNonNull(paramFunction1, "rightEnd is null");
    ObjectHelper.requireNonNull(paramBiFunction, "resultSelector is null");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableGroupJoin(this, paramPublisher, paramFunction, paramFunction1, paramBiFunction));
  }
  
  @BackpressureSupport(BackpressureKind.PASS_THROUGH)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<T> hide() {
    return RxJavaPlugins.onAssembly((Flowable)new FlowableHide(this));
  }
  
  @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Completable ignoreElements() {
    return RxJavaPlugins.onAssembly((Completable)new FlowableIgnoreElementsCompletable(this));
  }
  
  @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Single<Boolean> isEmpty() {
    return all(Functions.alwaysFalse());
  }
  
  @BackpressureSupport(BackpressureKind.ERROR)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <TRight, TLeftEnd, TRightEnd, R> Flowable<R> join(Publisher<? extends TRight> paramPublisher, Function<? super T, ? extends Publisher<TLeftEnd>> paramFunction, Function<? super TRight, ? extends Publisher<TRightEnd>> paramFunction1, BiFunction<? super T, ? super TRight, ? extends R> paramBiFunction) {
    ObjectHelper.requireNonNull(paramPublisher, "other is null");
    ObjectHelper.requireNonNull(paramFunction, "leftEnd is null");
    ObjectHelper.requireNonNull(paramFunction1, "rightEnd is null");
    ObjectHelper.requireNonNull(paramBiFunction, "resultSelector is null");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableJoin(this, paramPublisher, paramFunction, paramFunction1, paramBiFunction));
  }
  
  @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Single<T> last(T paramT) {
    ObjectHelper.requireNonNull(paramT, "defaultItem");
    return RxJavaPlugins.onAssembly((Single)new FlowableLastSingle(this, paramT));
  }
  
  @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Maybe<T> lastElement() {
    return RxJavaPlugins.onAssembly((Maybe)new FlowableLastMaybe(this));
  }
  
  @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Single<T> lastOrError() {
    return RxJavaPlugins.onAssembly((Single)new FlowableLastSingle(this, null));
  }
  
  @BackpressureSupport(BackpressureKind.SPECIAL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <R> Flowable<R> lift(FlowableOperator<? extends R, ? super T> paramFlowableOperator) {
    ObjectHelper.requireNonNull(paramFlowableOperator, "lifter is null");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableLift(this, paramFlowableOperator));
  }
  
  @BackpressureSupport(BackpressureKind.SPECIAL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<T> limit(long paramLong) {
    if (paramLong >= 0L)
      return RxJavaPlugins.onAssembly((Flowable)new FlowableLimit(this, paramLong)); 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("count >= 0 required but it was ");
    stringBuilder.append(paramLong);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  @BackpressureSupport(BackpressureKind.PASS_THROUGH)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <R> Flowable<R> map(Function<? super T, ? extends R> paramFunction) {
    ObjectHelper.requireNonNull(paramFunction, "mapper is null");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableMap(this, paramFunction));
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<Notification<T>> materialize() {
    return RxJavaPlugins.onAssembly((Flowable)new FlowableMaterialize(this));
  }
  
  @BackpressureSupport(BackpressureKind.PASS_THROUGH)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<T> mergeWith(CompletableSource paramCompletableSource) {
    ObjectHelper.requireNonNull(paramCompletableSource, "other is null");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableMergeWithCompletable(this, paramCompletableSource));
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<T> mergeWith(MaybeSource<? extends T> paramMaybeSource) {
    ObjectHelper.requireNonNull(paramMaybeSource, "other is null");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableMergeWithMaybe(this, paramMaybeSource));
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<T> mergeWith(SingleSource<? extends T> paramSingleSource) {
    ObjectHelper.requireNonNull(paramSingleSource, "other is null");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableMergeWithSingle(this, paramSingleSource));
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<T> mergeWith(Publisher<? extends T> paramPublisher) {
    ObjectHelper.requireNonNull(paramPublisher, "other is null");
    return merge(this, paramPublisher);
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("custom")
  public final Flowable<T> observeOn(Scheduler paramScheduler) {
    return observeOn(paramScheduler, false, bufferSize());
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("custom")
  public final Flowable<T> observeOn(Scheduler paramScheduler, boolean paramBoolean) {
    return observeOn(paramScheduler, paramBoolean, bufferSize());
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("custom")
  public final Flowable<T> observeOn(Scheduler paramScheduler, boolean paramBoolean, int paramInt) {
    ObjectHelper.requireNonNull(paramScheduler, "scheduler is null");
    ObjectHelper.verifyPositive(paramInt, "bufferSize");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableObserveOn(this, paramScheduler, paramBoolean, paramInt));
  }
  
  @BackpressureSupport(BackpressureKind.PASS_THROUGH)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <U> Flowable<U> ofType(Class<U> paramClass) {
    ObjectHelper.requireNonNull(paramClass, "clazz is null");
    return filter(Functions.isInstanceOf(paramClass)).cast(paramClass);
  }
  
  @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<T> onBackpressureBuffer() {
    return onBackpressureBuffer(bufferSize(), false, true);
  }
  
  @BackpressureSupport(BackpressureKind.ERROR)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<T> onBackpressureBuffer(int paramInt) {
    return onBackpressureBuffer(paramInt, false, false);
  }
  
  @BackpressureSupport(BackpressureKind.ERROR)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<T> onBackpressureBuffer(int paramInt, Action paramAction) {
    return onBackpressureBuffer(paramInt, false, false, paramAction);
  }
  
  @BackpressureSupport(BackpressureKind.ERROR)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<T> onBackpressureBuffer(int paramInt, boolean paramBoolean) {
    return onBackpressureBuffer(paramInt, paramBoolean, false);
  }
  
  @BackpressureSupport(BackpressureKind.SPECIAL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<T> onBackpressureBuffer(int paramInt, boolean paramBoolean1, boolean paramBoolean2) {
    ObjectHelper.verifyPositive(paramInt, "capacity");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableOnBackpressureBuffer(this, paramInt, paramBoolean2, paramBoolean1, Functions.EMPTY_ACTION));
  }
  
  @BackpressureSupport(BackpressureKind.SPECIAL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<T> onBackpressureBuffer(int paramInt, boolean paramBoolean1, boolean paramBoolean2, Action paramAction) {
    ObjectHelper.requireNonNull(paramAction, "onOverflow is null");
    ObjectHelper.verifyPositive(paramInt, "capacity");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableOnBackpressureBuffer(this, paramInt, paramBoolean2, paramBoolean1, paramAction));
  }
  
  @BackpressureSupport(BackpressureKind.SPECIAL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<T> onBackpressureBuffer(long paramLong, Action paramAction, BackpressureOverflowStrategy paramBackpressureOverflowStrategy) {
    ObjectHelper.requireNonNull(paramBackpressureOverflowStrategy, "overflowStrategy is null");
    ObjectHelper.verifyPositive(paramLong, "capacity");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableOnBackpressureBufferStrategy(this, paramLong, paramAction, paramBackpressureOverflowStrategy));
  }
  
  @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<T> onBackpressureBuffer(boolean paramBoolean) {
    return onBackpressureBuffer(bufferSize(), paramBoolean, true);
  }
  
  @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<T> onBackpressureDrop() {
    return RxJavaPlugins.onAssembly((Flowable)new FlowableOnBackpressureDrop(this));
  }
  
  @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<T> onBackpressureDrop(Consumer<? super T> paramConsumer) {
    ObjectHelper.requireNonNull(paramConsumer, "onDrop is null");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableOnBackpressureDrop(this, paramConsumer));
  }
  
  @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<T> onBackpressureLatest() {
    return RxJavaPlugins.onAssembly((Flowable)new FlowableOnBackpressureLatest(this));
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<T> onErrorResumeNext(Function<? super Throwable, ? extends Publisher<? extends T>> paramFunction) {
    ObjectHelper.requireNonNull(paramFunction, "resumeFunction is null");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableOnErrorNext(this, paramFunction, false));
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<T> onErrorResumeNext(Publisher<? extends T> paramPublisher) {
    ObjectHelper.requireNonNull(paramPublisher, "next is null");
    return onErrorResumeNext(Functions.justFunction(paramPublisher));
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<T> onErrorReturn(Function<? super Throwable, ? extends T> paramFunction) {
    ObjectHelper.requireNonNull(paramFunction, "valueSupplier is null");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableOnErrorReturn(this, paramFunction));
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<T> onErrorReturnItem(T paramT) {
    ObjectHelper.requireNonNull(paramT, "item is null");
    return onErrorReturn(Functions.justFunction(paramT));
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<T> onExceptionResumeNext(Publisher<? extends T> paramPublisher) {
    ObjectHelper.requireNonNull(paramPublisher, "next is null");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableOnErrorNext(this, Functions.justFunction(paramPublisher), true));
  }
  
  @BackpressureSupport(BackpressureKind.PASS_THROUGH)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<T> onTerminateDetach() {
    return RxJavaPlugins.onAssembly((Flowable)new FlowableDetach(this));
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final ParallelFlowable<T> parallel() {
    return ParallelFlowable.from(this);
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final ParallelFlowable<T> parallel(int paramInt) {
    ObjectHelper.verifyPositive(paramInt, "parallelism");
    return ParallelFlowable.from(this, paramInt);
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final ParallelFlowable<T> parallel(int paramInt1, int paramInt2) {
    ObjectHelper.verifyPositive(paramInt1, "parallelism");
    ObjectHelper.verifyPositive(paramInt2, "prefetch");
    return ParallelFlowable.from(this, paramInt1, paramInt2);
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <R> Flowable<R> publish(Function<? super Flowable<T>, ? extends Publisher<R>> paramFunction) {
    return publish(paramFunction, bufferSize());
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <R> Flowable<R> publish(Function<? super Flowable<T>, ? extends Publisher<? extends R>> paramFunction, int paramInt) {
    ObjectHelper.requireNonNull(paramFunction, "selector is null");
    ObjectHelper.verifyPositive(paramInt, "prefetch");
    return RxJavaPlugins.onAssembly((Flowable)new FlowablePublishMulticast(this, paramFunction, paramInt, false));
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final ConnectableFlowable<T> publish() {
    return publish(bufferSize());
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final ConnectableFlowable<T> publish(int paramInt) {
    ObjectHelper.verifyPositive(paramInt, "bufferSize");
    return FlowablePublish.create(this, paramInt);
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<T> rebatchRequests(int paramInt) {
    return observeOn(ImmediateThinScheduler.INSTANCE, true, paramInt);
  }
  
  @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Maybe<T> reduce(BiFunction<T, T, T> paramBiFunction) {
    ObjectHelper.requireNonNull(paramBiFunction, "reducer is null");
    return RxJavaPlugins.onAssembly((Maybe)new FlowableReduceMaybe(this, paramBiFunction));
  }
  
  @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <R> Single<R> reduce(R paramR, BiFunction<R, ? super T, R> paramBiFunction) {
    ObjectHelper.requireNonNull(paramR, "seed is null");
    ObjectHelper.requireNonNull(paramBiFunction, "reducer is null");
    return RxJavaPlugins.onAssembly((Single)new FlowableReduceSeedSingle(this, paramR, paramBiFunction));
  }
  
  @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <R> Single<R> reduceWith(Callable<R> paramCallable, BiFunction<R, ? super T, R> paramBiFunction) {
    ObjectHelper.requireNonNull(paramCallable, "seedSupplier is null");
    ObjectHelper.requireNonNull(paramBiFunction, "reducer is null");
    return RxJavaPlugins.onAssembly((Single)new FlowableReduceWithSingle(this, paramCallable, paramBiFunction));
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<T> repeat() {
    return repeat(Long.MAX_VALUE);
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<T> repeat(long paramLong) {
    if (paramLong >= 0L)
      return (paramLong == 0L) ? empty() : RxJavaPlugins.onAssembly((Flowable)new FlowableRepeat(this, paramLong)); 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("times >= 0 required but it was ");
    stringBuilder.append(paramLong);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<T> repeatUntil(BooleanSupplier paramBooleanSupplier) {
    ObjectHelper.requireNonNull(paramBooleanSupplier, "stop is null");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableRepeatUntil(this, paramBooleanSupplier));
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<T> repeatWhen(Function<? super Flowable<Object>, ? extends Publisher<?>> paramFunction) {
    ObjectHelper.requireNonNull(paramFunction, "handler is null");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableRepeatWhen(this, paramFunction));
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <R> Flowable<R> replay(Function<? super Flowable<T>, ? extends Publisher<R>> paramFunction) {
    ObjectHelper.requireNonNull(paramFunction, "selector is null");
    return FlowableReplay.multicastSelector(FlowableInternalHelper.replayCallable(this), paramFunction);
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <R> Flowable<R> replay(Function<? super Flowable<T>, ? extends Publisher<R>> paramFunction, int paramInt) {
    ObjectHelper.requireNonNull(paramFunction, "selector is null");
    ObjectHelper.verifyPositive(paramInt, "bufferSize");
    return FlowableReplay.multicastSelector(FlowableInternalHelper.replayCallable(this, paramInt), paramFunction);
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("io.reactivex:computation")
  public final <R> Flowable<R> replay(Function<? super Flowable<T>, ? extends Publisher<R>> paramFunction, int paramInt, long paramLong, TimeUnit paramTimeUnit) {
    return replay(paramFunction, paramInt, paramLong, paramTimeUnit, Schedulers.computation());
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("custom")
  public final <R> Flowable<R> replay(Function<? super Flowable<T>, ? extends Publisher<R>> paramFunction, int paramInt, long paramLong, TimeUnit paramTimeUnit, Scheduler paramScheduler) {
    ObjectHelper.requireNonNull(paramFunction, "selector is null");
    ObjectHelper.requireNonNull(paramTimeUnit, "unit is null");
    ObjectHelper.verifyPositive(paramInt, "bufferSize");
    ObjectHelper.requireNonNull(paramScheduler, "scheduler is null");
    return FlowableReplay.multicastSelector(FlowableInternalHelper.replayCallable(this, paramInt, paramLong, paramTimeUnit, paramScheduler), paramFunction);
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("custom")
  public final <R> Flowable<R> replay(Function<? super Flowable<T>, ? extends Publisher<R>> paramFunction, int paramInt, Scheduler paramScheduler) {
    ObjectHelper.requireNonNull(paramFunction, "selector is null");
    ObjectHelper.requireNonNull(paramScheduler, "scheduler is null");
    ObjectHelper.verifyPositive(paramInt, "bufferSize");
    return FlowableReplay.multicastSelector(FlowableInternalHelper.replayCallable(this, paramInt), FlowableInternalHelper.replayFunction(paramFunction, paramScheduler));
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("io.reactivex:computation")
  public final <R> Flowable<R> replay(Function<? super Flowable<T>, ? extends Publisher<R>> paramFunction, long paramLong, TimeUnit paramTimeUnit) {
    return replay(paramFunction, paramLong, paramTimeUnit, Schedulers.computation());
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("custom")
  public final <R> Flowable<R> replay(Function<? super Flowable<T>, ? extends Publisher<R>> paramFunction, long paramLong, TimeUnit paramTimeUnit, Scheduler paramScheduler) {
    ObjectHelper.requireNonNull(paramFunction, "selector is null");
    ObjectHelper.requireNonNull(paramTimeUnit, "unit is null");
    ObjectHelper.requireNonNull(paramScheduler, "scheduler is null");
    return FlowableReplay.multicastSelector(FlowableInternalHelper.replayCallable(this, paramLong, paramTimeUnit, paramScheduler), paramFunction);
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("custom")
  public final <R> Flowable<R> replay(Function<? super Flowable<T>, ? extends Publisher<R>> paramFunction, Scheduler paramScheduler) {
    ObjectHelper.requireNonNull(paramFunction, "selector is null");
    ObjectHelper.requireNonNull(paramScheduler, "scheduler is null");
    return FlowableReplay.multicastSelector(FlowableInternalHelper.replayCallable(this), FlowableInternalHelper.replayFunction(paramFunction, paramScheduler));
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final ConnectableFlowable<T> replay() {
    return FlowableReplay.createFrom(this);
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final ConnectableFlowable<T> replay(int paramInt) {
    ObjectHelper.verifyPositive(paramInt, "bufferSize");
    return FlowableReplay.create(this, paramInt);
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("io.reactivex:computation")
  public final ConnectableFlowable<T> replay(int paramInt, long paramLong, TimeUnit paramTimeUnit) {
    return replay(paramInt, paramLong, paramTimeUnit, Schedulers.computation());
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("custom")
  public final ConnectableFlowable<T> replay(int paramInt, long paramLong, TimeUnit paramTimeUnit, Scheduler paramScheduler) {
    ObjectHelper.verifyPositive(paramInt, "bufferSize");
    ObjectHelper.requireNonNull(paramTimeUnit, "unit is null");
    ObjectHelper.requireNonNull(paramScheduler, "scheduler is null");
    ObjectHelper.verifyPositive(paramInt, "bufferSize");
    return FlowableReplay.create(this, paramLong, paramTimeUnit, paramScheduler, paramInt);
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("custom")
  public final ConnectableFlowable<T> replay(int paramInt, Scheduler paramScheduler) {
    ObjectHelper.requireNonNull(paramScheduler, "scheduler is null");
    return FlowableReplay.observeOn(replay(paramInt), paramScheduler);
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("io.reactivex:computation")
  public final ConnectableFlowable<T> replay(long paramLong, TimeUnit paramTimeUnit) {
    return replay(paramLong, paramTimeUnit, Schedulers.computation());
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("custom")
  public final ConnectableFlowable<T> replay(long paramLong, TimeUnit paramTimeUnit, Scheduler paramScheduler) {
    ObjectHelper.requireNonNull(paramTimeUnit, "unit is null");
    ObjectHelper.requireNonNull(paramScheduler, "scheduler is null");
    return FlowableReplay.create(this, paramLong, paramTimeUnit, paramScheduler);
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("custom")
  public final ConnectableFlowable<T> replay(Scheduler paramScheduler) {
    ObjectHelper.requireNonNull(paramScheduler, "scheduler is null");
    return FlowableReplay.observeOn(replay(), paramScheduler);
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<T> retry() {
    return retry(Long.MAX_VALUE, Functions.alwaysTrue());
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<T> retry(long paramLong) {
    return retry(paramLong, Functions.alwaysTrue());
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<T> retry(long paramLong, Predicate<? super Throwable> paramPredicate) {
    if (paramLong >= 0L) {
      ObjectHelper.requireNonNull(paramPredicate, "predicate is null");
      return RxJavaPlugins.onAssembly((Flowable)new FlowableRetryPredicate(this, paramLong, paramPredicate));
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("times >= 0 required but it was ");
    stringBuilder.append(paramLong);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<T> retry(BiPredicate<? super Integer, ? super Throwable> paramBiPredicate) {
    ObjectHelper.requireNonNull(paramBiPredicate, "predicate is null");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableRetryBiPredicate(this, paramBiPredicate));
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<T> retry(Predicate<? super Throwable> paramPredicate) {
    return retry(Long.MAX_VALUE, paramPredicate);
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<T> retryUntil(BooleanSupplier paramBooleanSupplier) {
    ObjectHelper.requireNonNull(paramBooleanSupplier, "stop is null");
    return retry(Long.MAX_VALUE, Functions.predicateReverseFor(paramBooleanSupplier));
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<T> retryWhen(Function<? super Flowable<Throwable>, ? extends Publisher<?>> paramFunction) {
    ObjectHelper.requireNonNull(paramFunction, "handler is null");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableRetryWhen(this, paramFunction));
  }
  
  @BackpressureSupport(BackpressureKind.PASS_THROUGH)
  @SchedulerSupport("none")
  public final void safeSubscribe(Subscriber<? super T> paramSubscriber) {
    ObjectHelper.requireNonNull(paramSubscriber, "s is null");
    if (paramSubscriber instanceof SafeSubscriber) {
      subscribe((FlowableSubscriber<? super T>)paramSubscriber);
    } else {
      subscribe((FlowableSubscriber<? super T>)new SafeSubscriber(paramSubscriber));
    } 
  }
  
  @BackpressureSupport(BackpressureKind.ERROR)
  @CheckReturnValue
  @SchedulerSupport("io.reactivex:computation")
  public final Flowable<T> sample(long paramLong, TimeUnit paramTimeUnit) {
    return sample(paramLong, paramTimeUnit, Schedulers.computation());
  }
  
  @BackpressureSupport(BackpressureKind.ERROR)
  @CheckReturnValue
  @SchedulerSupport("custom")
  public final Flowable<T> sample(long paramLong, TimeUnit paramTimeUnit, Scheduler paramScheduler) {
    ObjectHelper.requireNonNull(paramTimeUnit, "unit is null");
    ObjectHelper.requireNonNull(paramScheduler, "scheduler is null");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableSampleTimed(this, paramLong, paramTimeUnit, paramScheduler, false));
  }
  
  @BackpressureSupport(BackpressureKind.ERROR)
  @CheckReturnValue
  @SchedulerSupport("custom")
  public final Flowable<T> sample(long paramLong, TimeUnit paramTimeUnit, Scheduler paramScheduler, boolean paramBoolean) {
    ObjectHelper.requireNonNull(paramTimeUnit, "unit is null");
    ObjectHelper.requireNonNull(paramScheduler, "scheduler is null");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableSampleTimed(this, paramLong, paramTimeUnit, paramScheduler, paramBoolean));
  }
  
  @BackpressureSupport(BackpressureKind.ERROR)
  @CheckReturnValue
  @SchedulerSupport("io.reactivex:computation")
  public final Flowable<T> sample(long paramLong, TimeUnit paramTimeUnit, boolean paramBoolean) {
    return sample(paramLong, paramTimeUnit, Schedulers.computation(), paramBoolean);
  }
  
  @BackpressureSupport(BackpressureKind.ERROR)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <U> Flowable<T> sample(Publisher<U> paramPublisher) {
    ObjectHelper.requireNonNull(paramPublisher, "sampler is null");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableSamplePublisher(this, paramPublisher, false));
  }
  
  @BackpressureSupport(BackpressureKind.ERROR)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <U> Flowable<T> sample(Publisher<U> paramPublisher, boolean paramBoolean) {
    ObjectHelper.requireNonNull(paramPublisher, "sampler is null");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableSamplePublisher(this, paramPublisher, paramBoolean));
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<T> scan(BiFunction<T, T, T> paramBiFunction) {
    ObjectHelper.requireNonNull(paramBiFunction, "accumulator is null");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableScan(this, paramBiFunction));
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <R> Flowable<R> scan(R paramR, BiFunction<R, ? super T, R> paramBiFunction) {
    ObjectHelper.requireNonNull(paramR, "initialValue is null");
    return scanWith(Functions.justCallable(paramR), paramBiFunction);
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <R> Flowable<R> scanWith(Callable<R> paramCallable, BiFunction<R, ? super T, R> paramBiFunction) {
    ObjectHelper.requireNonNull(paramCallable, "seedSupplier is null");
    ObjectHelper.requireNonNull(paramBiFunction, "accumulator is null");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableScanSeed(this, paramCallable, paramBiFunction));
  }
  
  @BackpressureSupport(BackpressureKind.PASS_THROUGH)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<T> serialize() {
    return RxJavaPlugins.onAssembly((Flowable)new FlowableSerialized(this));
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<T> share() {
    return publish().refCount();
  }
  
  @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Single<T> single(T paramT) {
    ObjectHelper.requireNonNull(paramT, "defaultItem is null");
    return RxJavaPlugins.onAssembly((Single)new FlowableSingleSingle(this, paramT));
  }
  
  @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Maybe<T> singleElement() {
    return RxJavaPlugins.onAssembly((Maybe)new FlowableSingleMaybe(this));
  }
  
  @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Single<T> singleOrError() {
    return RxJavaPlugins.onAssembly((Single)new FlowableSingleSingle(this, null));
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<T> skip(long paramLong) {
    return (paramLong <= 0L) ? RxJavaPlugins.onAssembly(this) : RxJavaPlugins.onAssembly((Flowable)new FlowableSkip(this, paramLong));
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<T> skip(long paramLong, TimeUnit paramTimeUnit) {
    return skipUntil(timer(paramLong, paramTimeUnit));
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("custom")
  public final Flowable<T> skip(long paramLong, TimeUnit paramTimeUnit, Scheduler paramScheduler) {
    return skipUntil(timer(paramLong, paramTimeUnit, paramScheduler));
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<T> skipLast(int paramInt) {
    if (paramInt >= 0)
      return (paramInt == 0) ? RxJavaPlugins.onAssembly(this) : RxJavaPlugins.onAssembly((Flowable)new FlowableSkipLast(this, paramInt)); 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("count >= 0 required but it was ");
    stringBuilder.append(paramInt);
    throw new IndexOutOfBoundsException(stringBuilder.toString());
  }
  
  @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<T> skipLast(long paramLong, TimeUnit paramTimeUnit) {
    return skipLast(paramLong, paramTimeUnit, Schedulers.computation(), false, bufferSize());
  }
  
  @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
  @CheckReturnValue
  @SchedulerSupport("custom")
  public final Flowable<T> skipLast(long paramLong, TimeUnit paramTimeUnit, Scheduler paramScheduler) {
    return skipLast(paramLong, paramTimeUnit, paramScheduler, false, bufferSize());
  }
  
  @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
  @CheckReturnValue
  @SchedulerSupport("custom")
  public final Flowable<T> skipLast(long paramLong, TimeUnit paramTimeUnit, Scheduler paramScheduler, boolean paramBoolean) {
    return skipLast(paramLong, paramTimeUnit, paramScheduler, paramBoolean, bufferSize());
  }
  
  @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
  @CheckReturnValue
  @SchedulerSupport("custom")
  public final Flowable<T> skipLast(long paramLong, TimeUnit paramTimeUnit, Scheduler paramScheduler, boolean paramBoolean, int paramInt) {
    ObjectHelper.requireNonNull(paramTimeUnit, "unit is null");
    ObjectHelper.requireNonNull(paramScheduler, "scheduler is null");
    ObjectHelper.verifyPositive(paramInt, "bufferSize");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableSkipLastTimed(this, paramLong, paramTimeUnit, paramScheduler, paramInt << 1, paramBoolean));
  }
  
  @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<T> skipLast(long paramLong, TimeUnit paramTimeUnit, boolean paramBoolean) {
    return skipLast(paramLong, paramTimeUnit, Schedulers.computation(), paramBoolean, bufferSize());
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <U> Flowable<T> skipUntil(Publisher<U> paramPublisher) {
    ObjectHelper.requireNonNull(paramPublisher, "other is null");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableSkipUntil(this, paramPublisher));
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<T> skipWhile(Predicate<? super T> paramPredicate) {
    ObjectHelper.requireNonNull(paramPredicate, "predicate is null");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableSkipWhile(this, paramPredicate));
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<T> sorted() {
    return toList().toFlowable().map(Functions.listSorter(Functions.naturalComparator())).flatMapIterable(Functions.identity());
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<T> sorted(Comparator<? super T> paramComparator) {
    ObjectHelper.requireNonNull(paramComparator, "sortFunction");
    return toList().toFlowable().map(Functions.listSorter(paramComparator)).flatMapIterable(Functions.identity());
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<T> startWith(Iterable<? extends T> paramIterable) {
    return concatArray((Publisher<? extends T>[])new Publisher[] { fromIterable(paramIterable), this });
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<T> startWith(T paramT) {
    ObjectHelper.requireNonNull(paramT, "value is null");
    return concatArray((Publisher<? extends T>[])new Publisher[] { just(paramT), this });
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<T> startWith(Publisher<? extends T> paramPublisher) {
    ObjectHelper.requireNonNull(paramPublisher, "other is null");
    return concatArray((Publisher<? extends T>[])new Publisher[] { paramPublisher, this });
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<T> startWithArray(T... paramVarArgs) {
    Flowable<T> flowable = fromArray(paramVarArgs);
    return (flowable == empty()) ? RxJavaPlugins.onAssembly(this) : concatArray((Publisher<? extends T>[])new Publisher[] { flowable, this });
  }
  
  @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
  @SchedulerSupport("none")
  public final Disposable subscribe() {
    return subscribe(Functions.emptyConsumer(), Functions.ON_ERROR_MISSING, Functions.EMPTY_ACTION, (Consumer<? super Subscription>)FlowableInternalHelper.RequestMax.INSTANCE);
  }
  
  @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Disposable subscribe(Consumer<? super T> paramConsumer) {
    return subscribe(paramConsumer, Functions.ON_ERROR_MISSING, Functions.EMPTY_ACTION, (Consumer<? super Subscription>)FlowableInternalHelper.RequestMax.INSTANCE);
  }
  
  @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Disposable subscribe(Consumer<? super T> paramConsumer, Consumer<? super Throwable> paramConsumer1) {
    return subscribe(paramConsumer, paramConsumer1, Functions.EMPTY_ACTION, (Consumer<? super Subscription>)FlowableInternalHelper.RequestMax.INSTANCE);
  }
  
  @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Disposable subscribe(Consumer<? super T> paramConsumer, Consumer<? super Throwable> paramConsumer1, Action paramAction) {
    return subscribe(paramConsumer, paramConsumer1, paramAction, (Consumer<? super Subscription>)FlowableInternalHelper.RequestMax.INSTANCE);
  }
  
  @BackpressureSupport(BackpressureKind.SPECIAL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Disposable subscribe(Consumer<? super T> paramConsumer, Consumer<? super Throwable> paramConsumer1, Action paramAction, Consumer<? super Subscription> paramConsumer2) {
    ObjectHelper.requireNonNull(paramConsumer, "onNext is null");
    ObjectHelper.requireNonNull(paramConsumer1, "onError is null");
    ObjectHelper.requireNonNull(paramAction, "onComplete is null");
    ObjectHelper.requireNonNull(paramConsumer2, "onSubscribe is null");
    LambdaSubscriber lambdaSubscriber = new LambdaSubscriber(paramConsumer, paramConsumer1, paramAction, paramConsumer2);
    subscribe((FlowableSubscriber<? super T>)lambdaSubscriber);
    return (Disposable)lambdaSubscriber;
  }
  
  @BackpressureSupport(BackpressureKind.SPECIAL)
  @SchedulerSupport("none")
  public final void subscribe(FlowableSubscriber<? super T> paramFlowableSubscriber) {
    ObjectHelper.requireNonNull(paramFlowableSubscriber, "s is null");
    try {
      return;
    } catch (NullPointerException nullPointerException) {
      throw nullPointerException;
    } finally {
      paramFlowableSubscriber = null;
      Exceptions.throwIfFatal((Throwable)paramFlowableSubscriber);
      RxJavaPlugins.onError((Throwable)paramFlowableSubscriber);
      NullPointerException nullPointerException = new NullPointerException("Actually not, but can't throw other exceptions due to RS");
      nullPointerException.initCause((Throwable)paramFlowableSubscriber);
    } 
  }
  
  @BackpressureSupport(BackpressureKind.SPECIAL)
  @SchedulerSupport("none")
  public final void subscribe(Subscriber<? super T> paramSubscriber) {
    if (paramSubscriber instanceof FlowableSubscriber) {
      subscribe((FlowableSubscriber<? super T>)paramSubscriber);
    } else {
      ObjectHelper.requireNonNull(paramSubscriber, "s is null");
      subscribe((FlowableSubscriber<? super T>)new StrictSubscriber(paramSubscriber));
    } 
  }
  
  protected abstract void subscribeActual(Subscriber<? super T> paramSubscriber);
  
  @BackpressureSupport(BackpressureKind.PASS_THROUGH)
  @CheckReturnValue
  @SchedulerSupport("custom")
  public final Flowable<T> subscribeOn(Scheduler paramScheduler) {
    ObjectHelper.requireNonNull(paramScheduler, "scheduler is null");
    return subscribeOn(paramScheduler, this instanceof FlowableCreate ^ true);
  }
  
  @BackpressureSupport(BackpressureKind.PASS_THROUGH)
  @CheckReturnValue
  @SchedulerSupport("custom")
  public final Flowable<T> subscribeOn(Scheduler paramScheduler, boolean paramBoolean) {
    ObjectHelper.requireNonNull(paramScheduler, "scheduler is null");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableSubscribeOn(this, paramScheduler, paramBoolean));
  }
  
  @BackpressureSupport(BackpressureKind.SPECIAL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <E extends Subscriber<? super T>> E subscribeWith(E paramE) {
    subscribe((Subscriber<? super T>)paramE);
    return paramE;
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<T> switchIfEmpty(Publisher<? extends T> paramPublisher) {
    ObjectHelper.requireNonNull(paramPublisher, "other is null");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableSwitchIfEmpty(this, paramPublisher));
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <R> Flowable<R> switchMap(Function<? super T, ? extends Publisher<? extends R>> paramFunction) {
    return switchMap(paramFunction, bufferSize());
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <R> Flowable<R> switchMap(Function<? super T, ? extends Publisher<? extends R>> paramFunction, int paramInt) {
    return switchMap0(paramFunction, paramInt, false);
  }
  
  <R> Flowable<R> switchMap0(Function<? super T, ? extends Publisher<? extends R>> paramFunction, int paramInt, boolean paramBoolean) {
    ObjectHelper.requireNonNull(paramFunction, "mapper is null");
    ObjectHelper.verifyPositive(paramInt, "bufferSize");
    if (this instanceof ScalarCallable) {
      Object object = ((ScalarCallable)this).call();
      return (object == null) ? empty() : FlowableScalarXMap.scalarXMap(object, paramFunction);
    } 
    return RxJavaPlugins.onAssembly((Flowable)new FlowableSwitchMap(this, paramFunction, paramInt, paramBoolean));
  }
  
  @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Completable switchMapCompletable(Function<? super T, ? extends CompletableSource> paramFunction) {
    ObjectHelper.requireNonNull(paramFunction, "mapper is null");
    return RxJavaPlugins.onAssembly((Completable)new FlowableSwitchMapCompletable(this, paramFunction, false));
  }
  
  @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Completable switchMapCompletableDelayError(Function<? super T, ? extends CompletableSource> paramFunction) {
    ObjectHelper.requireNonNull(paramFunction, "mapper is null");
    return RxJavaPlugins.onAssembly((Completable)new FlowableSwitchMapCompletable(this, paramFunction, true));
  }
  
  @BackpressureSupport(BackpressureKind.SPECIAL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <R> Flowable<R> switchMapDelayError(Function<? super T, ? extends Publisher<? extends R>> paramFunction) {
    return switchMapDelayError(paramFunction, bufferSize());
  }
  
  @BackpressureSupport(BackpressureKind.SPECIAL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <R> Flowable<R> switchMapDelayError(Function<? super T, ? extends Publisher<? extends R>> paramFunction, int paramInt) {
    return switchMap0(paramFunction, paramInt, true);
  }
  
  @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <R> Flowable<R> switchMapMaybe(Function<? super T, ? extends MaybeSource<? extends R>> paramFunction) {
    ObjectHelper.requireNonNull(paramFunction, "mapper is null");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableSwitchMapMaybe(this, paramFunction, false));
  }
  
  @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <R> Flowable<R> switchMapMaybeDelayError(Function<? super T, ? extends MaybeSource<? extends R>> paramFunction) {
    ObjectHelper.requireNonNull(paramFunction, "mapper is null");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableSwitchMapMaybe(this, paramFunction, true));
  }
  
  @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <R> Flowable<R> switchMapSingle(Function<? super T, ? extends SingleSource<? extends R>> paramFunction) {
    ObjectHelper.requireNonNull(paramFunction, "mapper is null");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableSwitchMapSingle(this, paramFunction, false));
  }
  
  @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <R> Flowable<R> switchMapSingleDelayError(Function<? super T, ? extends SingleSource<? extends R>> paramFunction) {
    ObjectHelper.requireNonNull(paramFunction, "mapper is null");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableSwitchMapSingle(this, paramFunction, true));
  }
  
  @BackpressureSupport(BackpressureKind.SPECIAL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<T> take(long paramLong) {
    if (paramLong >= 0L)
      return RxJavaPlugins.onAssembly((Flowable)new FlowableTake(this, paramLong)); 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("count >= 0 required but it was ");
    stringBuilder.append(paramLong);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  @BackpressureSupport(BackpressureKind.PASS_THROUGH)
  @CheckReturnValue
  @SchedulerSupport("io.reactivex:computation")
  public final Flowable<T> take(long paramLong, TimeUnit paramTimeUnit) {
    return takeUntil(timer(paramLong, paramTimeUnit));
  }
  
  @BackpressureSupport(BackpressureKind.PASS_THROUGH)
  @CheckReturnValue
  @SchedulerSupport("custom")
  public final Flowable<T> take(long paramLong, TimeUnit paramTimeUnit, Scheduler paramScheduler) {
    return takeUntil(timer(paramLong, paramTimeUnit, paramScheduler));
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<T> takeLast(int paramInt) {
    if (paramInt >= 0)
      return (paramInt == 0) ? RxJavaPlugins.onAssembly((Flowable)new FlowableIgnoreElements(this)) : ((paramInt == 1) ? RxJavaPlugins.onAssembly((Flowable)new FlowableTakeLastOne(this)) : RxJavaPlugins.onAssembly((Flowable)new FlowableTakeLast(this, paramInt))); 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("count >= 0 required but it was ");
    stringBuilder.append(paramInt);
    throw new IndexOutOfBoundsException(stringBuilder.toString());
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<T> takeLast(long paramLong1, long paramLong2, TimeUnit paramTimeUnit) {
    return takeLast(paramLong1, paramLong2, paramTimeUnit, Schedulers.computation(), false, bufferSize());
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("custom")
  public final Flowable<T> takeLast(long paramLong1, long paramLong2, TimeUnit paramTimeUnit, Scheduler paramScheduler) {
    return takeLast(paramLong1, paramLong2, paramTimeUnit, paramScheduler, false, bufferSize());
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("custom")
  public final Flowable<T> takeLast(long paramLong1, long paramLong2, TimeUnit paramTimeUnit, Scheduler paramScheduler, boolean paramBoolean, int paramInt) {
    ObjectHelper.requireNonNull(paramTimeUnit, "unit is null");
    ObjectHelper.requireNonNull(paramScheduler, "scheduler is null");
    ObjectHelper.verifyPositive(paramInt, "bufferSize");
    if (paramLong1 >= 0L)
      return RxJavaPlugins.onAssembly((Flowable)new FlowableTakeLastTimed(this, paramLong1, paramLong2, paramTimeUnit, paramScheduler, paramInt, paramBoolean)); 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("count >= 0 required but it was ");
    stringBuilder.append(paramLong1);
    throw new IndexOutOfBoundsException(stringBuilder.toString());
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("io.reactivex:computation")
  public final Flowable<T> takeLast(long paramLong, TimeUnit paramTimeUnit) {
    return takeLast(paramLong, paramTimeUnit, Schedulers.computation(), false, bufferSize());
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("custom")
  public final Flowable<T> takeLast(long paramLong, TimeUnit paramTimeUnit, Scheduler paramScheduler) {
    return takeLast(paramLong, paramTimeUnit, paramScheduler, false, bufferSize());
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("custom")
  public final Flowable<T> takeLast(long paramLong, TimeUnit paramTimeUnit, Scheduler paramScheduler, boolean paramBoolean) {
    return takeLast(paramLong, paramTimeUnit, paramScheduler, paramBoolean, bufferSize());
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("custom")
  public final Flowable<T> takeLast(long paramLong, TimeUnit paramTimeUnit, Scheduler paramScheduler, boolean paramBoolean, int paramInt) {
    return takeLast(Long.MAX_VALUE, paramLong, paramTimeUnit, paramScheduler, paramBoolean, paramInt);
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("io.reactivex:computation")
  public final Flowable<T> takeLast(long paramLong, TimeUnit paramTimeUnit, boolean paramBoolean) {
    return takeLast(paramLong, paramTimeUnit, Schedulers.computation(), paramBoolean, bufferSize());
  }
  
  @BackpressureSupport(BackpressureKind.PASS_THROUGH)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<T> takeUntil(Predicate<? super T> paramPredicate) {
    ObjectHelper.requireNonNull(paramPredicate, "stopPredicate is null");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableTakeUntilPredicate(this, paramPredicate));
  }
  
  @BackpressureSupport(BackpressureKind.PASS_THROUGH)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <U> Flowable<T> takeUntil(Publisher<U> paramPublisher) {
    ObjectHelper.requireNonNull(paramPublisher, "other is null");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableTakeUntil(this, paramPublisher));
  }
  
  @BackpressureSupport(BackpressureKind.PASS_THROUGH)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<T> takeWhile(Predicate<? super T> paramPredicate) {
    ObjectHelper.requireNonNull(paramPredicate, "predicate is null");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableTakeWhile(this, paramPredicate));
  }
  
  @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final TestSubscriber<T> test() {
    TestSubscriber<T> testSubscriber = new TestSubscriber();
    subscribe((FlowableSubscriber<? super T>)testSubscriber);
    return testSubscriber;
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final TestSubscriber<T> test(long paramLong) {
    TestSubscriber<T> testSubscriber = new TestSubscriber(paramLong);
    subscribe((FlowableSubscriber<? super T>)testSubscriber);
    return testSubscriber;
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final TestSubscriber<T> test(long paramLong, boolean paramBoolean) {
    TestSubscriber<T> testSubscriber = new TestSubscriber(paramLong);
    if (paramBoolean)
      testSubscriber.cancel(); 
    subscribe((FlowableSubscriber<? super T>)testSubscriber);
    return testSubscriber;
  }
  
  @BackpressureSupport(BackpressureKind.ERROR)
  @CheckReturnValue
  @SchedulerSupport("io.reactivex:computation")
  public final Flowable<T> throttleFirst(long paramLong, TimeUnit paramTimeUnit) {
    return throttleFirst(paramLong, paramTimeUnit, Schedulers.computation());
  }
  
  @BackpressureSupport(BackpressureKind.ERROR)
  @CheckReturnValue
  @SchedulerSupport("custom")
  public final Flowable<T> throttleFirst(long paramLong, TimeUnit paramTimeUnit, Scheduler paramScheduler) {
    ObjectHelper.requireNonNull(paramTimeUnit, "unit is null");
    ObjectHelper.requireNonNull(paramScheduler, "scheduler is null");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableThrottleFirstTimed(this, paramLong, paramTimeUnit, paramScheduler));
  }
  
  @BackpressureSupport(BackpressureKind.ERROR)
  @CheckReturnValue
  @SchedulerSupport("io.reactivex:computation")
  public final Flowable<T> throttleLast(long paramLong, TimeUnit paramTimeUnit) {
    return sample(paramLong, paramTimeUnit);
  }
  
  @BackpressureSupport(BackpressureKind.ERROR)
  @CheckReturnValue
  @SchedulerSupport("custom")
  public final Flowable<T> throttleLast(long paramLong, TimeUnit paramTimeUnit, Scheduler paramScheduler) {
    return sample(paramLong, paramTimeUnit, paramScheduler);
  }
  
  @BackpressureSupport(BackpressureKind.ERROR)
  @CheckReturnValue
  @SchedulerSupport("io.reactivex:computation")
  public final Flowable<T> throttleLatest(long paramLong, TimeUnit paramTimeUnit) {
    return throttleLatest(paramLong, paramTimeUnit, Schedulers.computation(), false);
  }
  
  @BackpressureSupport(BackpressureKind.ERROR)
  @CheckReturnValue
  @SchedulerSupport("custom")
  public final Flowable<T> throttleLatest(long paramLong, TimeUnit paramTimeUnit, Scheduler paramScheduler) {
    return throttleLatest(paramLong, paramTimeUnit, paramScheduler, false);
  }
  
  @BackpressureSupport(BackpressureKind.ERROR)
  @CheckReturnValue
  @SchedulerSupport("custom")
  public final Flowable<T> throttleLatest(long paramLong, TimeUnit paramTimeUnit, Scheduler paramScheduler, boolean paramBoolean) {
    ObjectHelper.requireNonNull(paramTimeUnit, "unit is null");
    ObjectHelper.requireNonNull(paramScheduler, "scheduler is null");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableThrottleLatest(this, paramLong, paramTimeUnit, paramScheduler, paramBoolean));
  }
  
  @BackpressureSupport(BackpressureKind.ERROR)
  @CheckReturnValue
  @SchedulerSupport("io.reactivex:computation")
  public final Flowable<T> throttleLatest(long paramLong, TimeUnit paramTimeUnit, boolean paramBoolean) {
    return throttleLatest(paramLong, paramTimeUnit, Schedulers.computation(), paramBoolean);
  }
  
  @BackpressureSupport(BackpressureKind.ERROR)
  @CheckReturnValue
  @SchedulerSupport("io.reactivex:computation")
  public final Flowable<T> throttleWithTimeout(long paramLong, TimeUnit paramTimeUnit) {
    return debounce(paramLong, paramTimeUnit);
  }
  
  @BackpressureSupport(BackpressureKind.ERROR)
  @CheckReturnValue
  @SchedulerSupport("custom")
  public final Flowable<T> throttleWithTimeout(long paramLong, TimeUnit paramTimeUnit, Scheduler paramScheduler) {
    return debounce(paramLong, paramTimeUnit, paramScheduler);
  }
  
  @BackpressureSupport(BackpressureKind.PASS_THROUGH)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<Timed<T>> timeInterval() {
    return timeInterval(TimeUnit.MILLISECONDS, Schedulers.computation());
  }
  
  @BackpressureSupport(BackpressureKind.PASS_THROUGH)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<Timed<T>> timeInterval(Scheduler paramScheduler) {
    return timeInterval(TimeUnit.MILLISECONDS, paramScheduler);
  }
  
  @BackpressureSupport(BackpressureKind.PASS_THROUGH)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<Timed<T>> timeInterval(TimeUnit paramTimeUnit) {
    return timeInterval(paramTimeUnit, Schedulers.computation());
  }
  
  @BackpressureSupport(BackpressureKind.PASS_THROUGH)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<Timed<T>> timeInterval(TimeUnit paramTimeUnit, Scheduler paramScheduler) {
    ObjectHelper.requireNonNull(paramTimeUnit, "unit is null");
    ObjectHelper.requireNonNull(paramScheduler, "scheduler is null");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableTimeInterval(this, paramTimeUnit, paramScheduler));
  }
  
  @BackpressureSupport(BackpressureKind.PASS_THROUGH)
  @CheckReturnValue
  @SchedulerSupport("io.reactivex:computation")
  public final Flowable<T> timeout(long paramLong, TimeUnit paramTimeUnit) {
    return timeout0(paramLong, paramTimeUnit, null, Schedulers.computation());
  }
  
  @BackpressureSupport(BackpressureKind.PASS_THROUGH)
  @CheckReturnValue
  @SchedulerSupport("custom")
  public final Flowable<T> timeout(long paramLong, TimeUnit paramTimeUnit, Scheduler paramScheduler) {
    return timeout0(paramLong, paramTimeUnit, null, paramScheduler);
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("custom")
  public final Flowable<T> timeout(long paramLong, TimeUnit paramTimeUnit, Scheduler paramScheduler, Publisher<? extends T> paramPublisher) {
    ObjectHelper.requireNonNull(paramPublisher, "other is null");
    return timeout0(paramLong, paramTimeUnit, paramPublisher, paramScheduler);
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("io.reactivex:computation")
  public final Flowable<T> timeout(long paramLong, TimeUnit paramTimeUnit, Publisher<? extends T> paramPublisher) {
    ObjectHelper.requireNonNull(paramPublisher, "other is null");
    return timeout0(paramLong, paramTimeUnit, paramPublisher, Schedulers.computation());
  }
  
  @BackpressureSupport(BackpressureKind.PASS_THROUGH)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <V> Flowable<T> timeout(Function<? super T, ? extends Publisher<V>> paramFunction) {
    return timeout0(null, paramFunction, null);
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <V> Flowable<T> timeout(Function<? super T, ? extends Publisher<V>> paramFunction, Flowable<? extends T> paramFlowable) {
    ObjectHelper.requireNonNull(paramFlowable, "other is null");
    return timeout0(null, paramFunction, paramFlowable);
  }
  
  @BackpressureSupport(BackpressureKind.PASS_THROUGH)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <U, V> Flowable<T> timeout(Publisher<U> paramPublisher, Function<? super T, ? extends Publisher<V>> paramFunction) {
    ObjectHelper.requireNonNull(paramPublisher, "firstTimeoutIndicator is null");
    return timeout0(paramPublisher, paramFunction, null);
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <U, V> Flowable<T> timeout(Publisher<U> paramPublisher, Function<? super T, ? extends Publisher<V>> paramFunction, Publisher<? extends T> paramPublisher1) {
    ObjectHelper.requireNonNull(paramPublisher, "firstTimeoutSelector is null");
    ObjectHelper.requireNonNull(paramPublisher1, "other is null");
    return timeout0(paramPublisher, paramFunction, paramPublisher1);
  }
  
  @BackpressureSupport(BackpressureKind.PASS_THROUGH)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<Timed<T>> timestamp() {
    return timestamp(TimeUnit.MILLISECONDS, Schedulers.computation());
  }
  
  @BackpressureSupport(BackpressureKind.PASS_THROUGH)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<Timed<T>> timestamp(Scheduler paramScheduler) {
    return timestamp(TimeUnit.MILLISECONDS, paramScheduler);
  }
  
  @BackpressureSupport(BackpressureKind.PASS_THROUGH)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<Timed<T>> timestamp(TimeUnit paramTimeUnit) {
    return timestamp(paramTimeUnit, Schedulers.computation());
  }
  
  @BackpressureSupport(BackpressureKind.PASS_THROUGH)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<Timed<T>> timestamp(TimeUnit paramTimeUnit, Scheduler paramScheduler) {
    ObjectHelper.requireNonNull(paramTimeUnit, "unit is null");
    ObjectHelper.requireNonNull(paramScheduler, "scheduler is null");
    return map(Functions.timestampWith(paramTimeUnit, paramScheduler));
  }
  
  @BackpressureSupport(BackpressureKind.SPECIAL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <R> R to(Function<? super Flowable<T>, R> paramFunction) {
    try {
      return (R)((Function)ObjectHelper.requireNonNull(paramFunction, "converter is null")).apply(this);
    } finally {
      paramFunction = null;
      Exceptions.throwIfFatal((Throwable)paramFunction);
    } 
  }
  
  @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Future<T> toFuture() {
    return (Future<T>)subscribeWith(new FutureSubscriber());
  }
  
  @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Single<List<T>> toList() {
    return RxJavaPlugins.onAssembly((Single)new FlowableToListSingle(this));
  }
  
  @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Single<List<T>> toList(int paramInt) {
    ObjectHelper.verifyPositive(paramInt, "capacityHint");
    return RxJavaPlugins.onAssembly((Single)new FlowableToListSingle(this, Functions.createArrayList(paramInt)));
  }
  
  @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <U extends Collection<? super T>> Single<U> toList(Callable<U> paramCallable) {
    ObjectHelper.requireNonNull(paramCallable, "collectionSupplier is null");
    return RxJavaPlugins.onAssembly((Single)new FlowableToListSingle(this, paramCallable));
  }
  
  @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <K> Single<Map<K, T>> toMap(Function<? super T, ? extends K> paramFunction) {
    ObjectHelper.requireNonNull(paramFunction, "keySelector is null");
    return collect(HashMapSupplier.asCallable(), Functions.toMapKeySelector(paramFunction));
  }
  
  @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <K, V> Single<Map<K, V>> toMap(Function<? super T, ? extends K> paramFunction, Function<? super T, ? extends V> paramFunction1) {
    ObjectHelper.requireNonNull(paramFunction, "keySelector is null");
    ObjectHelper.requireNonNull(paramFunction1, "valueSelector is null");
    return collect(HashMapSupplier.asCallable(), Functions.toMapKeyValueSelector(paramFunction, paramFunction1));
  }
  
  @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <K, V> Single<Map<K, V>> toMap(Function<? super T, ? extends K> paramFunction, Function<? super T, ? extends V> paramFunction1, Callable<? extends Map<K, V>> paramCallable) {
    ObjectHelper.requireNonNull(paramFunction, "keySelector is null");
    ObjectHelper.requireNonNull(paramFunction1, "valueSelector is null");
    return collect(paramCallable, Functions.toMapKeyValueSelector(paramFunction, paramFunction1));
  }
  
  @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <K> Single<Map<K, Collection<T>>> toMultimap(Function<? super T, ? extends K> paramFunction) {
    return toMultimap(paramFunction, Functions.identity(), HashMapSupplier.asCallable(), ArrayListSupplier.asFunction());
  }
  
  @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <K, V> Single<Map<K, Collection<V>>> toMultimap(Function<? super T, ? extends K> paramFunction, Function<? super T, ? extends V> paramFunction1) {
    return toMultimap(paramFunction, paramFunction1, HashMapSupplier.asCallable(), ArrayListSupplier.asFunction());
  }
  
  @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <K, V> Single<Map<K, Collection<V>>> toMultimap(Function<? super T, ? extends K> paramFunction, Function<? super T, ? extends V> paramFunction1, Callable<Map<K, Collection<V>>> paramCallable) {
    return toMultimap(paramFunction, paramFunction1, paramCallable, ArrayListSupplier.asFunction());
  }
  
  @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <K, V> Single<Map<K, Collection<V>>> toMultimap(Function<? super T, ? extends K> paramFunction, Function<? super T, ? extends V> paramFunction1, Callable<? extends Map<K, Collection<V>>> paramCallable, Function<? super K, ? extends Collection<? super V>> paramFunction2) {
    ObjectHelper.requireNonNull(paramFunction, "keySelector is null");
    ObjectHelper.requireNonNull(paramFunction1, "valueSelector is null");
    ObjectHelper.requireNonNull(paramCallable, "mapSupplier is null");
    ObjectHelper.requireNonNull(paramFunction2, "collectionFactory is null");
    return collect(paramCallable, Functions.toMultimapKeyValueSelector(paramFunction, paramFunction1, paramFunction2));
  }
  
  @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Observable<T> toObservable() {
    return RxJavaPlugins.onAssembly((Observable)new ObservableFromPublisher(this));
  }
  
  @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Single<List<T>> toSortedList() {
    return toSortedList(Functions.naturalComparator());
  }
  
  @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Single<List<T>> toSortedList(int paramInt) {
    return toSortedList(Functions.naturalComparator(), paramInt);
  }
  
  @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Single<List<T>> toSortedList(Comparator<? super T> paramComparator) {
    ObjectHelper.requireNonNull(paramComparator, "comparator is null");
    return toList().map(Functions.listSorter(paramComparator));
  }
  
  @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Single<List<T>> toSortedList(Comparator<? super T> paramComparator, int paramInt) {
    ObjectHelper.requireNonNull(paramComparator, "comparator is null");
    return toList(paramInt).map(Functions.listSorter(paramComparator));
  }
  
  @BackpressureSupport(BackpressureKind.PASS_THROUGH)
  @CheckReturnValue
  @SchedulerSupport("custom")
  public final Flowable<T> unsubscribeOn(Scheduler paramScheduler) {
    ObjectHelper.requireNonNull(paramScheduler, "scheduler is null");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableUnsubscribeOn(this, paramScheduler));
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<Flowable<T>> window(long paramLong) {
    return window(paramLong, paramLong, bufferSize());
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<Flowable<T>> window(long paramLong1, long paramLong2) {
    return window(paramLong1, paramLong2, bufferSize());
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final Flowable<Flowable<T>> window(long paramLong1, long paramLong2, int paramInt) {
    ObjectHelper.verifyPositive(paramLong2, "skip");
    ObjectHelper.verifyPositive(paramLong1, "count");
    ObjectHelper.verifyPositive(paramInt, "bufferSize");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableWindow(this, paramLong1, paramLong2, paramInt));
  }
  
  @BackpressureSupport(BackpressureKind.ERROR)
  @CheckReturnValue
  @SchedulerSupport("io.reactivex:computation")
  public final Flowable<Flowable<T>> window(long paramLong1, long paramLong2, TimeUnit paramTimeUnit) {
    return window(paramLong1, paramLong2, paramTimeUnit, Schedulers.computation(), bufferSize());
  }
  
  @BackpressureSupport(BackpressureKind.ERROR)
  @CheckReturnValue
  @SchedulerSupport("custom")
  public final Flowable<Flowable<T>> window(long paramLong1, long paramLong2, TimeUnit paramTimeUnit, Scheduler paramScheduler) {
    return window(paramLong1, paramLong2, paramTimeUnit, paramScheduler, bufferSize());
  }
  
  @BackpressureSupport(BackpressureKind.ERROR)
  @CheckReturnValue
  @SchedulerSupport("custom")
  public final Flowable<Flowable<T>> window(long paramLong1, long paramLong2, TimeUnit paramTimeUnit, Scheduler paramScheduler, int paramInt) {
    ObjectHelper.verifyPositive(paramInt, "bufferSize");
    ObjectHelper.verifyPositive(paramLong1, "timespan");
    ObjectHelper.verifyPositive(paramLong2, "timeskip");
    ObjectHelper.requireNonNull(paramScheduler, "scheduler is null");
    ObjectHelper.requireNonNull(paramTimeUnit, "unit is null");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableWindowTimed(this, paramLong1, paramLong2, paramTimeUnit, paramScheduler, Long.MAX_VALUE, paramInt, false));
  }
  
  @BackpressureSupport(BackpressureKind.ERROR)
  @CheckReturnValue
  @SchedulerSupport("io.reactivex:computation")
  public final Flowable<Flowable<T>> window(long paramLong, TimeUnit paramTimeUnit) {
    return window(paramLong, paramTimeUnit, Schedulers.computation(), Long.MAX_VALUE, false);
  }
  
  @BackpressureSupport(BackpressureKind.ERROR)
  @CheckReturnValue
  @SchedulerSupport("io.reactivex:computation")
  public final Flowable<Flowable<T>> window(long paramLong1, TimeUnit paramTimeUnit, long paramLong2) {
    return window(paramLong1, paramTimeUnit, Schedulers.computation(), paramLong2, false);
  }
  
  @BackpressureSupport(BackpressureKind.ERROR)
  @CheckReturnValue
  @SchedulerSupport("io.reactivex:computation")
  public final Flowable<Flowable<T>> window(long paramLong1, TimeUnit paramTimeUnit, long paramLong2, boolean paramBoolean) {
    return window(paramLong1, paramTimeUnit, Schedulers.computation(), paramLong2, paramBoolean);
  }
  
  @BackpressureSupport(BackpressureKind.ERROR)
  @CheckReturnValue
  @SchedulerSupport("custom")
  public final Flowable<Flowable<T>> window(long paramLong, TimeUnit paramTimeUnit, Scheduler paramScheduler) {
    return window(paramLong, paramTimeUnit, paramScheduler, Long.MAX_VALUE, false);
  }
  
  @BackpressureSupport(BackpressureKind.ERROR)
  @CheckReturnValue
  @SchedulerSupport("custom")
  public final Flowable<Flowable<T>> window(long paramLong1, TimeUnit paramTimeUnit, Scheduler paramScheduler, long paramLong2) {
    return window(paramLong1, paramTimeUnit, paramScheduler, paramLong2, false);
  }
  
  @BackpressureSupport(BackpressureKind.ERROR)
  @CheckReturnValue
  @SchedulerSupport("custom")
  public final Flowable<Flowable<T>> window(long paramLong1, TimeUnit paramTimeUnit, Scheduler paramScheduler, long paramLong2, boolean paramBoolean) {
    return window(paramLong1, paramTimeUnit, paramScheduler, paramLong2, paramBoolean, bufferSize());
  }
  
  @BackpressureSupport(BackpressureKind.ERROR)
  @CheckReturnValue
  @SchedulerSupport("custom")
  public final Flowable<Flowable<T>> window(long paramLong1, TimeUnit paramTimeUnit, Scheduler paramScheduler, long paramLong2, boolean paramBoolean, int paramInt) {
    ObjectHelper.verifyPositive(paramInt, "bufferSize");
    ObjectHelper.requireNonNull(paramScheduler, "scheduler is null");
    ObjectHelper.requireNonNull(paramTimeUnit, "unit is null");
    ObjectHelper.verifyPositive(paramLong2, "count");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableWindowTimed(this, paramLong1, paramLong1, paramTimeUnit, paramScheduler, paramLong2, paramInt, paramBoolean));
  }
  
  @BackpressureSupport(BackpressureKind.ERROR)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <B> Flowable<Flowable<T>> window(Callable<? extends Publisher<B>> paramCallable) {
    return window(paramCallable, bufferSize());
  }
  
  @BackpressureSupport(BackpressureKind.ERROR)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <B> Flowable<Flowable<T>> window(Callable<? extends Publisher<B>> paramCallable, int paramInt) {
    ObjectHelper.requireNonNull(paramCallable, "boundaryIndicatorSupplier is null");
    ObjectHelper.verifyPositive(paramInt, "bufferSize");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableWindowBoundarySupplier(this, paramCallable, paramInt));
  }
  
  @BackpressureSupport(BackpressureKind.ERROR)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <B> Flowable<Flowable<T>> window(Publisher<B> paramPublisher) {
    return window(paramPublisher, bufferSize());
  }
  
  @BackpressureSupport(BackpressureKind.ERROR)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <B> Flowable<Flowable<T>> window(Publisher<B> paramPublisher, int paramInt) {
    ObjectHelper.requireNonNull(paramPublisher, "boundaryIndicator is null");
    ObjectHelper.verifyPositive(paramInt, "bufferSize");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableWindowBoundary(this, paramPublisher, paramInt));
  }
  
  @BackpressureSupport(BackpressureKind.ERROR)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <U, V> Flowable<Flowable<T>> window(Publisher<U> paramPublisher, Function<? super U, ? extends Publisher<V>> paramFunction) {
    return window(paramPublisher, paramFunction, bufferSize());
  }
  
  @BackpressureSupport(BackpressureKind.ERROR)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <U, V> Flowable<Flowable<T>> window(Publisher<U> paramPublisher, Function<? super U, ? extends Publisher<V>> paramFunction, int paramInt) {
    ObjectHelper.requireNonNull(paramPublisher, "openingIndicator is null");
    ObjectHelper.requireNonNull(paramFunction, "closingIndicator is null");
    ObjectHelper.verifyPositive(paramInt, "bufferSize");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableWindowBoundarySelector(this, paramPublisher, paramFunction, paramInt));
  }
  
  @BackpressureSupport(BackpressureKind.PASS_THROUGH)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <R> Flowable<R> withLatestFrom(Iterable<? extends Publisher<?>> paramIterable, Function<? super Object[], R> paramFunction) {
    ObjectHelper.requireNonNull(paramIterable, "others is null");
    ObjectHelper.requireNonNull(paramFunction, "combiner is null");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableWithLatestFromMany(this, paramIterable, paramFunction));
  }
  
  @BackpressureSupport(BackpressureKind.PASS_THROUGH)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <U, R> Flowable<R> withLatestFrom(Publisher<? extends U> paramPublisher, BiFunction<? super T, ? super U, ? extends R> paramBiFunction) {
    ObjectHelper.requireNonNull(paramPublisher, "other is null");
    ObjectHelper.requireNonNull(paramBiFunction, "combiner is null");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableWithLatestFrom(this, paramBiFunction, paramPublisher));
  }
  
  @BackpressureSupport(BackpressureKind.PASS_THROUGH)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <T1, T2, R> Flowable<R> withLatestFrom(Publisher<T1> paramPublisher, Publisher<T2> paramPublisher1, Function3<? super T, ? super T1, ? super T2, R> paramFunction3) {
    ObjectHelper.requireNonNull(paramPublisher, "source1 is null");
    ObjectHelper.requireNonNull(paramPublisher1, "source2 is null");
    Function<? super Object[], R> function = Functions.toFunction(paramFunction3);
    return withLatestFrom((Publisher<?>[])new Publisher[] { paramPublisher, paramPublisher1 }, function);
  }
  
  @BackpressureSupport(BackpressureKind.PASS_THROUGH)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <T1, T2, T3, R> Flowable<R> withLatestFrom(Publisher<T1> paramPublisher, Publisher<T2> paramPublisher1, Publisher<T3> paramPublisher2, Function4<? super T, ? super T1, ? super T2, ? super T3, R> paramFunction4) {
    ObjectHelper.requireNonNull(paramPublisher, "source1 is null");
    ObjectHelper.requireNonNull(paramPublisher1, "source2 is null");
    ObjectHelper.requireNonNull(paramPublisher2, "source3 is null");
    Function<? super Object[], R> function = Functions.toFunction(paramFunction4);
    return withLatestFrom((Publisher<?>[])new Publisher[] { paramPublisher, paramPublisher1, paramPublisher2 }, function);
  }
  
  @BackpressureSupport(BackpressureKind.PASS_THROUGH)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <T1, T2, T3, T4, R> Flowable<R> withLatestFrom(Publisher<T1> paramPublisher, Publisher<T2> paramPublisher1, Publisher<T3> paramPublisher2, Publisher<T4> paramPublisher3, Function5<? super T, ? super T1, ? super T2, ? super T3, ? super T4, R> paramFunction5) {
    ObjectHelper.requireNonNull(paramPublisher, "source1 is null");
    ObjectHelper.requireNonNull(paramPublisher1, "source2 is null");
    ObjectHelper.requireNonNull(paramPublisher2, "source3 is null");
    ObjectHelper.requireNonNull(paramPublisher3, "source4 is null");
    Function<? super Object[], R> function = Functions.toFunction(paramFunction5);
    return withLatestFrom((Publisher<?>[])new Publisher[] { paramPublisher, paramPublisher1, paramPublisher2, paramPublisher3 }, function);
  }
  
  @BackpressureSupport(BackpressureKind.PASS_THROUGH)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <R> Flowable<R> withLatestFrom(Publisher<?>[] paramArrayOfPublisher, Function<? super Object[], R> paramFunction) {
    ObjectHelper.requireNonNull(paramArrayOfPublisher, "others is null");
    ObjectHelper.requireNonNull(paramFunction, "combiner is null");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableWithLatestFromMany(this, (Publisher[])paramArrayOfPublisher, paramFunction));
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <U, R> Flowable<R> zipWith(Iterable<U> paramIterable, BiFunction<? super T, ? super U, ? extends R> paramBiFunction) {
    ObjectHelper.requireNonNull(paramIterable, "other is null");
    ObjectHelper.requireNonNull(paramBiFunction, "zipper is null");
    return RxJavaPlugins.onAssembly((Flowable)new FlowableZipIterable(this, paramIterable, paramBiFunction));
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <U, R> Flowable<R> zipWith(Publisher<? extends U> paramPublisher, BiFunction<? super T, ? super U, ? extends R> paramBiFunction) {
    ObjectHelper.requireNonNull(paramPublisher, "other is null");
    return zip(this, paramPublisher, paramBiFunction);
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <U, R> Flowable<R> zipWith(Publisher<? extends U> paramPublisher, BiFunction<? super T, ? super U, ? extends R> paramBiFunction, boolean paramBoolean) {
    return zip(this, paramPublisher, paramBiFunction, paramBoolean);
  }
  
  @BackpressureSupport(BackpressureKind.FULL)
  @CheckReturnValue
  @SchedulerSupport("none")
  public final <U, R> Flowable<R> zipWith(Publisher<? extends U> paramPublisher, BiFunction<? super T, ? super U, ? extends R> paramBiFunction, boolean paramBoolean, int paramInt) {
    return zip(this, paramPublisher, paramBiFunction, paramBoolean, paramInt);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/Flowable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */