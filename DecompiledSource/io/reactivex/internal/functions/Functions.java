package io.reactivex.internal.functions;

import io.reactivex.Notification;
import io.reactivex.Scheduler;
import io.reactivex.exceptions.OnErrorNotImplementedException;
import io.reactivex.functions.Action;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.BiFunction;
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
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Timed;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.reactivestreams.Subscription;

public final class Functions {
  static final Predicate<Object> ALWAYS_FALSE;
  
  static final Predicate<Object> ALWAYS_TRUE;
  
  public static final Action EMPTY_ACTION;
  
  static final Consumer<Object> EMPTY_CONSUMER;
  
  public static final LongConsumer EMPTY_LONG_CONSUMER;
  
  public static final Runnable EMPTY_RUNNABLE;
  
  public static final Consumer<Throwable> ERROR_CONSUMER;
  
  static final Function<Object, Object> IDENTITY = new Identity();
  
  static final Comparator<Object> NATURAL_COMPARATOR;
  
  static final Callable<Object> NULL_SUPPLIER;
  
  public static final Consumer<Throwable> ON_ERROR_MISSING;
  
  public static final Consumer<Subscription> REQUEST_MAX;
  
  static {
    EMPTY_RUNNABLE = new EmptyRunnable();
    EMPTY_ACTION = new EmptyAction();
    EMPTY_CONSUMER = new EmptyConsumer();
    ERROR_CONSUMER = new ErrorConsumer();
    ON_ERROR_MISSING = new OnErrorMissingConsumer();
    EMPTY_LONG_CONSUMER = new EmptyLongConsumer();
    ALWAYS_TRUE = new TruePredicate();
    ALWAYS_FALSE = new FalsePredicate();
    NULL_SUPPLIER = new NullCallable();
    NATURAL_COMPARATOR = new NaturalObjectComparator();
    REQUEST_MAX = new MaxRequestSubscription();
  }
  
  private Functions() {
    throw new IllegalStateException("No instances!");
  }
  
  public static <T> Consumer<T> actionConsumer(Action paramAction) {
    return new ActionConsumer<T>(paramAction);
  }
  
  public static <T> Predicate<T> alwaysFalse() {
    return (Predicate)ALWAYS_FALSE;
  }
  
  public static <T> Predicate<T> alwaysTrue() {
    return (Predicate)ALWAYS_TRUE;
  }
  
  public static <T> Consumer<T> boundedConsumer(int paramInt) {
    return new BoundedConsumer(paramInt);
  }
  
  public static <T, U> Function<T, U> castFunction(Class<U> paramClass) {
    return new CastToClass<T, U>(paramClass);
  }
  
  public static <T> Callable<List<T>> createArrayList(int paramInt) {
    return new ArrayListCapacityCallable<T>(paramInt);
  }
  
  public static <T> Callable<Set<T>> createHashSet() {
    return HashSetCallable.INSTANCE;
  }
  
  public static <T> Consumer<T> emptyConsumer() {
    return (Consumer)EMPTY_CONSUMER;
  }
  
  public static <T> Predicate<T> equalsWith(T paramT) {
    return new EqualsPredicate<T>(paramT);
  }
  
  public static Action futureAction(Future<?> paramFuture) {
    return new FutureAction(paramFuture);
  }
  
  public static <T> Function<T, T> identity() {
    return (Function)IDENTITY;
  }
  
  public static <T, U> Predicate<T> isInstanceOf(Class<U> paramClass) {
    return new ClassFilter<T, U>(paramClass);
  }
  
  public static <T> Callable<T> justCallable(T paramT) {
    return new JustValue<Object, T>(paramT);
  }
  
  public static <T, U> Function<T, U> justFunction(U paramU) {
    return new JustValue<T, U>(paramU);
  }
  
  public static <T> Function<List<T>, List<T>> listSorter(Comparator<? super T> paramComparator) {
    return new ListSorter<T>(paramComparator);
  }
  
  public static <T> Comparator<T> naturalComparator() {
    return NaturalComparator.INSTANCE;
  }
  
  public static <T> Comparator<T> naturalOrder() {
    return (Comparator)NATURAL_COMPARATOR;
  }
  
  public static <T> Action notificationOnComplete(Consumer<? super Notification<T>> paramConsumer) {
    return new NotificationOnComplete<T>(paramConsumer);
  }
  
  public static <T> Consumer<Throwable> notificationOnError(Consumer<? super Notification<T>> paramConsumer) {
    return new NotificationOnError<T>(paramConsumer);
  }
  
  public static <T> Consumer<T> notificationOnNext(Consumer<? super Notification<T>> paramConsumer) {
    return new NotificationOnNext<T>(paramConsumer);
  }
  
  public static <T> Callable<T> nullSupplier() {
    return (Callable)NULL_SUPPLIER;
  }
  
  public static <T> Predicate<T> predicateReverseFor(BooleanSupplier paramBooleanSupplier) {
    return new BooleanSupplierPredicateReverse<T>(paramBooleanSupplier);
  }
  
  public static <T> Function<T, Timed<T>> timestampWith(TimeUnit paramTimeUnit, Scheduler paramScheduler) {
    return new TimestampFunction<T>(paramTimeUnit, paramScheduler);
  }
  
  public static <T1, T2, R> Function<Object[], R> toFunction(BiFunction<? super T1, ? super T2, ? extends R> paramBiFunction) {
    ObjectHelper.requireNonNull(paramBiFunction, "f is null");
    return new Array2Func<T1, T2, R>(paramBiFunction);
  }
  
  public static <T1, T2, T3, R> Function<Object[], R> toFunction(Function3<T1, T2, T3, R> paramFunction3) {
    ObjectHelper.requireNonNull(paramFunction3, "f is null");
    return new Array3Func<T1, T2, T3, R>(paramFunction3);
  }
  
  public static <T1, T2, T3, T4, R> Function<Object[], R> toFunction(Function4<T1, T2, T3, T4, R> paramFunction4) {
    ObjectHelper.requireNonNull(paramFunction4, "f is null");
    return new Array4Func<T1, T2, T3, T4, R>(paramFunction4);
  }
  
  public static <T1, T2, T3, T4, T5, R> Function<Object[], R> toFunction(Function5<T1, T2, T3, T4, T5, R> paramFunction5) {
    ObjectHelper.requireNonNull(paramFunction5, "f is null");
    return new Array5Func<T1, T2, T3, T4, T5, R>(paramFunction5);
  }
  
  public static <T1, T2, T3, T4, T5, T6, R> Function<Object[], R> toFunction(Function6<T1, T2, T3, T4, T5, T6, R> paramFunction6) {
    ObjectHelper.requireNonNull(paramFunction6, "f is null");
    return new Array6Func<T1, T2, T3, T4, T5, T6, R>(paramFunction6);
  }
  
  public static <T1, T2, T3, T4, T5, T6, T7, R> Function<Object[], R> toFunction(Function7<T1, T2, T3, T4, T5, T6, T7, R> paramFunction7) {
    ObjectHelper.requireNonNull(paramFunction7, "f is null");
    return new Array7Func<T1, T2, T3, T4, T5, T6, T7, R>(paramFunction7);
  }
  
  public static <T1, T2, T3, T4, T5, T6, T7, T8, R> Function<Object[], R> toFunction(Function8<T1, T2, T3, T4, T5, T6, T7, T8, R> paramFunction8) {
    ObjectHelper.requireNonNull(paramFunction8, "f is null");
    return new Array8Func<T1, T2, T3, T4, T5, T6, T7, T8, R>(paramFunction8);
  }
  
  public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, R> Function<Object[], R> toFunction(Function9<T1, T2, T3, T4, T5, T6, T7, T8, T9, R> paramFunction9) {
    ObjectHelper.requireNonNull(paramFunction9, "f is null");
    return new Array9Func<T1, T2, T3, T4, T5, T6, T7, T8, T9, R>(paramFunction9);
  }
  
  public static <T, K> BiConsumer<Map<K, T>, T> toMapKeySelector(Function<? super T, ? extends K> paramFunction) {
    return new ToMapKeySelector<K, T>(paramFunction);
  }
  
  public static <T, K, V> BiConsumer<Map<K, V>, T> toMapKeyValueSelector(Function<? super T, ? extends K> paramFunction, Function<? super T, ? extends V> paramFunction1) {
    return new ToMapKeyValueSelector<K, V, T>(paramFunction1, paramFunction);
  }
  
  public static <T, K, V> BiConsumer<Map<K, Collection<V>>, T> toMultimapKeyValueSelector(Function<? super T, ? extends K> paramFunction, Function<? super T, ? extends V> paramFunction1, Function<? super K, ? extends Collection<? super V>> paramFunction2) {
    return new ToMultimapKeyValueSelector<K, V, T>(paramFunction2, paramFunction1, paramFunction);
  }
  
  static final class ActionConsumer<T> implements Consumer<T> {
    final Action action;
    
    ActionConsumer(Action param1Action) {
      this.action = param1Action;
    }
    
    public void accept(T param1T) throws Exception {
      this.action.run();
    }
  }
  
  static final class Array2Func<T1, T2, R> implements Function<Object[], R> {
    final BiFunction<? super T1, ? super T2, ? extends R> f;
    
    Array2Func(BiFunction<? super T1, ? super T2, ? extends R> param1BiFunction) {
      this.f = param1BiFunction;
    }
    
    public R apply(Object[] param1ArrayOfObject) throws Exception {
      if (param1ArrayOfObject.length == 2)
        return (R)this.f.apply(param1ArrayOfObject[0], param1ArrayOfObject[1]); 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Array of size 2 expected but got ");
      stringBuilder.append(param1ArrayOfObject.length);
      throw new IllegalArgumentException(stringBuilder.toString());
    }
  }
  
  static final class Array3Func<T1, T2, T3, R> implements Function<Object[], R> {
    final Function3<T1, T2, T3, R> f;
    
    Array3Func(Function3<T1, T2, T3, R> param1Function3) {
      this.f = param1Function3;
    }
    
    public R apply(Object[] param1ArrayOfObject) throws Exception {
      if (param1ArrayOfObject.length == 3)
        return (R)this.f.apply(param1ArrayOfObject[0], param1ArrayOfObject[1], param1ArrayOfObject[2]); 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Array of size 3 expected but got ");
      stringBuilder.append(param1ArrayOfObject.length);
      throw new IllegalArgumentException(stringBuilder.toString());
    }
  }
  
  static final class Array4Func<T1, T2, T3, T4, R> implements Function<Object[], R> {
    final Function4<T1, T2, T3, T4, R> f;
    
    Array4Func(Function4<T1, T2, T3, T4, R> param1Function4) {
      this.f = param1Function4;
    }
    
    public R apply(Object[] param1ArrayOfObject) throws Exception {
      if (param1ArrayOfObject.length == 4)
        return (R)this.f.apply(param1ArrayOfObject[0], param1ArrayOfObject[1], param1ArrayOfObject[2], param1ArrayOfObject[3]); 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Array of size 4 expected but got ");
      stringBuilder.append(param1ArrayOfObject.length);
      throw new IllegalArgumentException(stringBuilder.toString());
    }
  }
  
  static final class Array5Func<T1, T2, T3, T4, T5, R> implements Function<Object[], R> {
    private final Function5<T1, T2, T3, T4, T5, R> f;
    
    Array5Func(Function5<T1, T2, T3, T4, T5, R> param1Function5) {
      this.f = param1Function5;
    }
    
    public R apply(Object[] param1ArrayOfObject) throws Exception {
      if (param1ArrayOfObject.length == 5)
        return (R)this.f.apply(param1ArrayOfObject[0], param1ArrayOfObject[1], param1ArrayOfObject[2], param1ArrayOfObject[3], param1ArrayOfObject[4]); 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Array of size 5 expected but got ");
      stringBuilder.append(param1ArrayOfObject.length);
      throw new IllegalArgumentException(stringBuilder.toString());
    }
  }
  
  static final class Array6Func<T1, T2, T3, T4, T5, T6, R> implements Function<Object[], R> {
    final Function6<T1, T2, T3, T4, T5, T6, R> f;
    
    Array6Func(Function6<T1, T2, T3, T4, T5, T6, R> param1Function6) {
      this.f = param1Function6;
    }
    
    public R apply(Object[] param1ArrayOfObject) throws Exception {
      if (param1ArrayOfObject.length == 6)
        return (R)this.f.apply(param1ArrayOfObject[0], param1ArrayOfObject[1], param1ArrayOfObject[2], param1ArrayOfObject[3], param1ArrayOfObject[4], param1ArrayOfObject[5]); 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Array of size 6 expected but got ");
      stringBuilder.append(param1ArrayOfObject.length);
      throw new IllegalArgumentException(stringBuilder.toString());
    }
  }
  
  static final class Array7Func<T1, T2, T3, T4, T5, T6, T7, R> implements Function<Object[], R> {
    final Function7<T1, T2, T3, T4, T5, T6, T7, R> f;
    
    Array7Func(Function7<T1, T2, T3, T4, T5, T6, T7, R> param1Function7) {
      this.f = param1Function7;
    }
    
    public R apply(Object[] param1ArrayOfObject) throws Exception {
      if (param1ArrayOfObject.length == 7)
        return (R)this.f.apply(param1ArrayOfObject[0], param1ArrayOfObject[1], param1ArrayOfObject[2], param1ArrayOfObject[3], param1ArrayOfObject[4], param1ArrayOfObject[5], param1ArrayOfObject[6]); 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Array of size 7 expected but got ");
      stringBuilder.append(param1ArrayOfObject.length);
      throw new IllegalArgumentException(stringBuilder.toString());
    }
  }
  
  static final class Array8Func<T1, T2, T3, T4, T5, T6, T7, T8, R> implements Function<Object[], R> {
    final Function8<T1, T2, T3, T4, T5, T6, T7, T8, R> f;
    
    Array8Func(Function8<T1, T2, T3, T4, T5, T6, T7, T8, R> param1Function8) {
      this.f = param1Function8;
    }
    
    public R apply(Object[] param1ArrayOfObject) throws Exception {
      if (param1ArrayOfObject.length == 8)
        return (R)this.f.apply(param1ArrayOfObject[0], param1ArrayOfObject[1], param1ArrayOfObject[2], param1ArrayOfObject[3], param1ArrayOfObject[4], param1ArrayOfObject[5], param1ArrayOfObject[6], param1ArrayOfObject[7]); 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Array of size 8 expected but got ");
      stringBuilder.append(param1ArrayOfObject.length);
      throw new IllegalArgumentException(stringBuilder.toString());
    }
  }
  
  static final class Array9Func<T1, T2, T3, T4, T5, T6, T7, T8, T9, R> implements Function<Object[], R> {
    final Function9<T1, T2, T3, T4, T5, T6, T7, T8, T9, R> f;
    
    Array9Func(Function9<T1, T2, T3, T4, T5, T6, T7, T8, T9, R> param1Function9) {
      this.f = param1Function9;
    }
    
    public R apply(Object[] param1ArrayOfObject) throws Exception {
      if (param1ArrayOfObject.length == 9)
        return (R)this.f.apply(param1ArrayOfObject[0], param1ArrayOfObject[1], param1ArrayOfObject[2], param1ArrayOfObject[3], param1ArrayOfObject[4], param1ArrayOfObject[5], param1ArrayOfObject[6], param1ArrayOfObject[7], param1ArrayOfObject[8]); 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Array of size 9 expected but got ");
      stringBuilder.append(param1ArrayOfObject.length);
      throw new IllegalArgumentException(stringBuilder.toString());
    }
  }
  
  static final class ArrayListCapacityCallable<T> implements Callable<List<T>> {
    final int capacity;
    
    ArrayListCapacityCallable(int param1Int) {
      this.capacity = param1Int;
    }
    
    public List<T> call() throws Exception {
      return new ArrayList<T>(this.capacity);
    }
  }
  
  static final class BooleanSupplierPredicateReverse<T> implements Predicate<T> {
    final BooleanSupplier supplier;
    
    BooleanSupplierPredicateReverse(BooleanSupplier param1BooleanSupplier) {
      this.supplier = param1BooleanSupplier;
    }
    
    public boolean test(T param1T) throws Exception {
      return this.supplier.getAsBoolean() ^ true;
    }
  }
  
  public static class BoundedConsumer implements Consumer<Subscription> {
    final int bufferSize;
    
    BoundedConsumer(int param1Int) {
      this.bufferSize = param1Int;
    }
    
    public void accept(Subscription param1Subscription) throws Exception {
      param1Subscription.request(this.bufferSize);
    }
  }
  
  static final class CastToClass<T, U> implements Function<T, U> {
    final Class<U> clazz;
    
    CastToClass(Class<U> param1Class) {
      this.clazz = param1Class;
    }
    
    public U apply(T param1T) throws Exception {
      return this.clazz.cast(param1T);
    }
  }
  
  static final class ClassFilter<T, U> implements Predicate<T> {
    final Class<U> clazz;
    
    ClassFilter(Class<U> param1Class) {
      this.clazz = param1Class;
    }
    
    public boolean test(T param1T) throws Exception {
      return this.clazz.isInstance(param1T);
    }
  }
  
  static final class EmptyAction implements Action {
    public void run() {}
    
    public String toString() {
      return "EmptyAction";
    }
  }
  
  static final class EmptyConsumer implements Consumer<Object> {
    public void accept(Object param1Object) {}
    
    public String toString() {
      return "EmptyConsumer";
    }
  }
  
  static final class EmptyLongConsumer implements LongConsumer {
    public void accept(long param1Long) {}
  }
  
  static final class EmptyRunnable implements Runnable {
    public void run() {}
    
    public String toString() {
      return "EmptyRunnable";
    }
  }
  
  static final class EqualsPredicate<T> implements Predicate<T> {
    final T value;
    
    EqualsPredicate(T param1T) {
      this.value = param1T;
    }
    
    public boolean test(T param1T) throws Exception {
      return ObjectHelper.equals(param1T, this.value);
    }
  }
  
  static final class ErrorConsumer implements Consumer<Throwable> {
    public void accept(Throwable param1Throwable) {
      RxJavaPlugins.onError(param1Throwable);
    }
  }
  
  static final class FalsePredicate implements Predicate<Object> {
    public boolean test(Object param1Object) {
      return false;
    }
  }
  
  static final class FutureAction implements Action {
    final Future<?> future;
    
    FutureAction(Future<?> param1Future) {
      this.future = param1Future;
    }
    
    public void run() throws Exception {
      this.future.get();
    }
  }
  
  enum HashSetCallable implements Callable<Set<Object>> {
    INSTANCE;
    
    static {
    
    }
    
    public Set<Object> call() throws Exception {
      return new HashSet();
    }
  }
  
  static final class Identity implements Function<Object, Object> {
    public Object apply(Object param1Object) {
      return param1Object;
    }
    
    public String toString() {
      return "IdentityFunction";
    }
  }
  
  static final class JustValue<T, U> implements Callable<U>, Function<T, U> {
    final U value;
    
    JustValue(U param1U) {
      this.value = param1U;
    }
    
    public U apply(T param1T) throws Exception {
      return this.value;
    }
    
    public U call() throws Exception {
      return this.value;
    }
  }
  
  static final class ListSorter<T> implements Function<List<T>, List<T>> {
    final Comparator<? super T> comparator;
    
    ListSorter(Comparator<? super T> param1Comparator) {
      this.comparator = param1Comparator;
    }
    
    public List<T> apply(List<T> param1List) {
      Collections.sort(param1List, this.comparator);
      return param1List;
    }
  }
  
  static final class MaxRequestSubscription implements Consumer<Subscription> {
    public void accept(Subscription param1Subscription) throws Exception {
      param1Subscription.request(Long.MAX_VALUE);
    }
  }
  
  enum NaturalComparator implements Comparator<Object> {
    INSTANCE;
    
    static {
    
    }
    
    public int compare(Object param1Object1, Object param1Object2) {
      return ((Comparable<Object>)param1Object1).compareTo(param1Object2);
    }
  }
  
  static final class NaturalObjectComparator implements Comparator<Object> {
    public int compare(Object param1Object1, Object param1Object2) {
      return ((Comparable<Object>)param1Object1).compareTo(param1Object2);
    }
  }
  
  static final class NotificationOnComplete<T> implements Action {
    final Consumer<? super Notification<T>> onNotification;
    
    NotificationOnComplete(Consumer<? super Notification<T>> param1Consumer) {
      this.onNotification = param1Consumer;
    }
    
    public void run() throws Exception {
      this.onNotification.accept(Notification.createOnComplete());
    }
  }
  
  static final class NotificationOnError<T> implements Consumer<Throwable> {
    final Consumer<? super Notification<T>> onNotification;
    
    NotificationOnError(Consumer<? super Notification<T>> param1Consumer) {
      this.onNotification = param1Consumer;
    }
    
    public void accept(Throwable param1Throwable) throws Exception {
      this.onNotification.accept(Notification.createOnError(param1Throwable));
    }
  }
  
  static final class NotificationOnNext<T> implements Consumer<T> {
    final Consumer<? super Notification<T>> onNotification;
    
    NotificationOnNext(Consumer<? super Notification<T>> param1Consumer) {
      this.onNotification = param1Consumer;
    }
    
    public void accept(T param1T) throws Exception {
      this.onNotification.accept(Notification.createOnNext(param1T));
    }
  }
  
  static final class NullCallable implements Callable<Object> {
    public Object call() {
      return null;
    }
  }
  
  static final class OnErrorMissingConsumer implements Consumer<Throwable> {
    public void accept(Throwable param1Throwable) {
      RxJavaPlugins.onError((Throwable)new OnErrorNotImplementedException(param1Throwable));
    }
  }
  
  static final class TimestampFunction<T> implements Function<T, Timed<T>> {
    final Scheduler scheduler;
    
    final TimeUnit unit;
    
    TimestampFunction(TimeUnit param1TimeUnit, Scheduler param1Scheduler) {
      this.unit = param1TimeUnit;
      this.scheduler = param1Scheduler;
    }
    
    public Timed<T> apply(T param1T) throws Exception {
      return new Timed(param1T, this.scheduler.now(this.unit), this.unit);
    }
  }
  
  static final class ToMapKeySelector<K, T> implements BiConsumer<Map<K, T>, T> {
    private final Function<? super T, ? extends K> keySelector;
    
    ToMapKeySelector(Function<? super T, ? extends K> param1Function) {
      this.keySelector = param1Function;
    }
    
    public void accept(Map<K, T> param1Map, T param1T) throws Exception {
      param1Map.put((K)this.keySelector.apply(param1T), param1T);
    }
  }
  
  static final class ToMapKeyValueSelector<K, V, T> implements BiConsumer<Map<K, V>, T> {
    private final Function<? super T, ? extends K> keySelector;
    
    private final Function<? super T, ? extends V> valueSelector;
    
    ToMapKeyValueSelector(Function<? super T, ? extends V> param1Function, Function<? super T, ? extends K> param1Function1) {
      this.valueSelector = param1Function;
      this.keySelector = param1Function1;
    }
    
    public void accept(Map<K, V> param1Map, T param1T) throws Exception {
      param1Map.put((K)this.keySelector.apply(param1T), (V)this.valueSelector.apply(param1T));
    }
  }
  
  static final class ToMultimapKeyValueSelector<K, V, T> implements BiConsumer<Map<K, Collection<V>>, T> {
    private final Function<? super K, ? extends Collection<? super V>> collectionFactory;
    
    private final Function<? super T, ? extends K> keySelector;
    
    private final Function<? super T, ? extends V> valueSelector;
    
    ToMultimapKeyValueSelector(Function<? super K, ? extends Collection<? super V>> param1Function, Function<? super T, ? extends V> param1Function1, Function<? super T, ? extends K> param1Function2) {
      this.collectionFactory = param1Function;
      this.valueSelector = param1Function1;
      this.keySelector = param1Function2;
    }
    
    public void accept(Map<K, Collection<V>> param1Map, T param1T) throws Exception {
      Object object = this.keySelector.apply(param1T);
      Collection<V> collection1 = param1Map.get(object);
      Collection<V> collection2 = collection1;
      if (collection1 == null) {
        collection2 = (Collection)this.collectionFactory.apply(object);
        param1Map.put((K)object, collection2);
      } 
      collection2.add((V)this.valueSelector.apply(param1T));
    }
  }
  
  static final class TruePredicate implements Predicate<Object> {
    public boolean test(Object param1Object) {
      return true;
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/functions/Functions.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */