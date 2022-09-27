package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.functions.Function;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.observers.BasicFuseableObserver;

public final class ObservableMap<T, U> extends AbstractObservableWithUpstream<T, U> {
  final Function<? super T, ? extends U> function;
  
  public ObservableMap(ObservableSource<T> paramObservableSource, Function<? super T, ? extends U> paramFunction) {
    super(paramObservableSource);
    this.function = paramFunction;
  }
  
  public void subscribeActual(Observer<? super U> paramObserver) {
    this.source.subscribe((Observer)new MapObserver<T, U>(paramObserver, this.function));
  }
  
  static final class MapObserver<T, U> extends BasicFuseableObserver<T, U> {
    final Function<? super T, ? extends U> mapper;
    
    MapObserver(Observer<? super U> param1Observer, Function<? super T, ? extends U> param1Function) {
      super(param1Observer);
      this.mapper = param1Function;
    }
    
    public void onNext(T param1T) {
      if (this.done)
        return; 
      if (this.sourceMode != 0) {
        this.downstream.onNext(null);
        return;
      } 
      try {
        return;
      } finally {
        param1T = null;
        fail((Throwable)param1T);
      } 
    }
    
    public U poll() throws Exception {
      Object object = this.qd.poll();
      if (object != null) {
        object = ObjectHelper.requireNonNull(this.mapper.apply(object), "The mapper function returned a null value.");
      } else {
        object = null;
      } 
      return (U)object;
    }
    
    public int requestFusion(int param1Int) {
      return transitiveBoundaryFusion(param1Int);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */