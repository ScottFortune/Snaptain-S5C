package io.reactivex.internal.operators.single;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.Callable;
import org.reactivestreams.Publisher;

public final class SingleInternalHelper {
  private SingleInternalHelper() {
    throw new IllegalStateException("No instances!");
  }
  
  public static <T> Callable<NoSuchElementException> emptyThrower() {
    return NoSuchElementCallable.INSTANCE;
  }
  
  public static <T> Iterable<? extends Flowable<T>> iterableToFlowable(Iterable<? extends SingleSource<? extends T>> paramIterable) {
    return new ToFlowableIterable<T>(paramIterable);
  }
  
  public static <T> Function<SingleSource<? extends T>, Publisher<? extends T>> toFlowable() {
    return ToFlowable.INSTANCE;
  }
  
  public static <T> Function<SingleSource<? extends T>, Observable<? extends T>> toObservable() {
    return ToObservable.INSTANCE;
  }
  
  enum NoSuchElementCallable implements Callable<NoSuchElementException> {
    INSTANCE;
    
    static {
    
    }
    
    public NoSuchElementException call() throws Exception {
      return new NoSuchElementException();
    }
  }
  
  enum ToFlowable implements Function<SingleSource, Publisher> {
    INSTANCE;
    
    static {
    
    }
    
    public Publisher apply(SingleSource<?> param1SingleSource) {
      return (Publisher)new SingleToFlowable(param1SingleSource);
    }
  }
  
  static final class ToFlowableIterable<T> implements Iterable<Flowable<T>> {
    private final Iterable<? extends SingleSource<? extends T>> sources;
    
    ToFlowableIterable(Iterable<? extends SingleSource<? extends T>> param1Iterable) {
      this.sources = param1Iterable;
    }
    
    public Iterator<Flowable<T>> iterator() {
      return new SingleInternalHelper.ToFlowableIterator<T>(this.sources.iterator());
    }
  }
  
  static final class ToFlowableIterator<T> implements Iterator<Flowable<T>> {
    private final Iterator<? extends SingleSource<? extends T>> sit;
    
    ToFlowableIterator(Iterator<? extends SingleSource<? extends T>> param1Iterator) {
      this.sit = param1Iterator;
    }
    
    public boolean hasNext() {
      return this.sit.hasNext();
    }
    
    public Flowable<T> next() {
      return new SingleToFlowable<T>(this.sit.next());
    }
    
    public void remove() {
      throw new UnsupportedOperationException();
    }
  }
  
  enum ToObservable implements Function<SingleSource, Observable> {
    INSTANCE;
    
    static {
    
    }
    
    public Observable apply(SingleSource<?> param1SingleSource) {
      return new SingleToObservable(param1SingleSource);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/single/SingleInternalHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */