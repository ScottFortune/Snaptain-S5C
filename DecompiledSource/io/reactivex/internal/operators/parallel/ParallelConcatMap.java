package io.reactivex.internal.operators.parallel;

import io.reactivex.functions.Function;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.operators.flowable.FlowableConcatMap;
import io.reactivex.internal.util.ErrorMode;
import io.reactivex.parallel.ParallelFlowable;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

public final class ParallelConcatMap<T, R> extends ParallelFlowable<R> {
  final ErrorMode errorMode;
  
  final Function<? super T, ? extends Publisher<? extends R>> mapper;
  
  final int prefetch;
  
  final ParallelFlowable<T> source;
  
  public ParallelConcatMap(ParallelFlowable<T> paramParallelFlowable, Function<? super T, ? extends Publisher<? extends R>> paramFunction, int paramInt, ErrorMode paramErrorMode) {
    this.source = paramParallelFlowable;
    this.mapper = (Function<? super T, ? extends Publisher<? extends R>>)ObjectHelper.requireNonNull(paramFunction, "mapper");
    this.prefetch = paramInt;
    this.errorMode = (ErrorMode)ObjectHelper.requireNonNull(paramErrorMode, "errorMode");
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
      arrayOfSubscriber[b] = FlowableConcatMap.subscribe(paramArrayOfSubscriber[b], this.mapper, this.prefetch, this.errorMode); 
    this.source.subscribe(arrayOfSubscriber);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/parallel/ParallelConcatMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */