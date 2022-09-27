package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.functions.Predicate;
import io.reactivex.internal.observers.BasicFuseableObserver;

public final class ObservableFilter<T> extends AbstractObservableWithUpstream<T, T> {
  final Predicate<? super T> predicate;
  
  public ObservableFilter(ObservableSource<T> paramObservableSource, Predicate<? super T> paramPredicate) {
    super(paramObservableSource);
    this.predicate = paramPredicate;
  }
  
  public void subscribeActual(Observer<? super T> paramObserver) {
    this.source.subscribe((Observer)new FilterObserver<T>(paramObserver, this.predicate));
  }
  
  static final class FilterObserver<T> extends BasicFuseableObserver<T, T> {
    final Predicate<? super T> filter;
    
    FilterObserver(Observer<? super T> param1Observer, Predicate<? super T> param1Predicate) {
      super(param1Observer);
      this.filter = param1Predicate;
    }
    
    public void onNext(T param1T) {
      if (this.sourceMode == 0) {
        try {
        
        } finally {
          param1T = null;
          fail((Throwable)param1T);
        } 
      } else {
        this.downstream.onNext(null);
      } 
    }
    
    public T poll() throws Exception {
      Object object;
      do {
        object = this.qd.poll();
      } while (object != null && !this.filter.test(object));
      return (T)object;
    }
    
    public int requestFusion(int param1Int) {
      return transitiveBoundaryFusion(param1Int);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */