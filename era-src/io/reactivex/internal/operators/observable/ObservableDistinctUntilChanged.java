package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.functions.BiPredicate;
import io.reactivex.functions.Function;
import io.reactivex.internal.observers.BasicFuseableObserver;

public final class ObservableDistinctUntilChanged<T, K> extends AbstractObservableWithUpstream<T, T> {
  final BiPredicate<? super K, ? super K> comparer;
  
  final Function<? super T, K> keySelector;
  
  public ObservableDistinctUntilChanged(ObservableSource<T> paramObservableSource, Function<? super T, K> paramFunction, BiPredicate<? super K, ? super K> paramBiPredicate) {
    super(paramObservableSource);
    this.keySelector = paramFunction;
    this.comparer = paramBiPredicate;
  }
  
  protected void subscribeActual(Observer<? super T> paramObserver) {
    this.source.subscribe((Observer)new DistinctUntilChangedObserver<T, K>(paramObserver, this.keySelector, this.comparer));
  }
  
  static final class DistinctUntilChangedObserver<T, K> extends BasicFuseableObserver<T, T> {
    final BiPredicate<? super K, ? super K> comparer;
    
    boolean hasValue;
    
    final Function<? super T, K> keySelector;
    
    K last;
    
    DistinctUntilChangedObserver(Observer<? super T> param1Observer, Function<? super T, K> param1Function, BiPredicate<? super K, ? super K> param1BiPredicate) {
      super(param1Observer);
      this.keySelector = param1Function;
      this.comparer = param1BiPredicate;
    }
    
    public void onNext(T param1T) {
      if (this.done)
        return; 
      if (this.sourceMode != 0) {
        this.downstream.onNext(param1T);
        return;
      } 
      try {
        Object object = this.keySelector.apply(param1T);
        return;
      } finally {
        param1T = null;
        fail((Throwable)param1T);
      } 
    }
    
    public T poll() throws Exception {
      while (true) {
        Object object1 = this.qd.poll();
        if (object1 == null)
          return null; 
        Object object2 = this.keySelector.apply(object1);
        if (!this.hasValue) {
          this.hasValue = true;
          this.last = (K)object2;
          return (T)object1;
        } 
        if (!this.comparer.test(this.last, object2)) {
          this.last = (K)object2;
          return (T)object1;
        } 
        this.last = (K)object2;
      } 
    }
    
    public int requestFusion(int param1Int) {
      return transitiveBoundaryFusion(param1Int);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableDistinctUntilChanged.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */