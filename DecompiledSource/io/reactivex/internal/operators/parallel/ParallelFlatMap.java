package io.reactivex.internal.operators.parallel;

import io.reactivex.functions.Function;
import io.reactivex.internal.operators.flowable.FlowableFlatMap;
import io.reactivex.parallel.ParallelFlowable;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

public final class ParallelFlatMap<T, R> extends ParallelFlowable<R> {
  final boolean delayError;
  
  final Function<? super T, ? extends Publisher<? extends R>> mapper;
  
  final int maxConcurrency;
  
  final int prefetch;
  
  final ParallelFlowable<T> source;
  
  public ParallelFlatMap(ParallelFlowable<T> paramParallelFlowable, Function<? super T, ? extends Publisher<? extends R>> paramFunction, boolean paramBoolean, int paramInt1, int paramInt2) {
    this.source = paramParallelFlowable;
    this.mapper = paramFunction;
    this.delayError = paramBoolean;
    this.maxConcurrency = paramInt1;
    this.prefetch = paramInt2;
  }
  
  public int parallelism() {
    return this.source.parallelism();
  }
  
  public void subscribe(Subscriber<? super R>[] paramArrayOfSubscriber) {
    if (!validate((Subscriber[])paramArrayOfSubscriber))
      return; 
    int i = paramArrayOfSubscriber.length;
    Subscriber[] arrayOfSubscriber = new Subscriber[i];
    for (byte b = 0; b < i; b++)
      arrayOfSubscriber[b] = (Subscriber)FlowableFlatMap.subscribe(paramArrayOfSubscriber[b], this.mapper, this.delayError, this.maxConcurrency, this.prefetch); 
    this.source.subscribe(arrayOfSubscriber);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/parallel/ParallelFlatMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */