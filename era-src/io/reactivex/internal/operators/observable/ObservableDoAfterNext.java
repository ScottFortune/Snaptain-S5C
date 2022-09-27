package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.observers.BasicFuseableObserver;

public final class ObservableDoAfterNext<T> extends AbstractObservableWithUpstream<T, T> {
  final Consumer<? super T> onAfterNext;
  
  public ObservableDoAfterNext(ObservableSource<T> paramObservableSource, Consumer<? super T> paramConsumer) {
    super(paramObservableSource);
    this.onAfterNext = paramConsumer;
  }
  
  protected void subscribeActual(Observer<? super T> paramObserver) {
    this.source.subscribe((Observer)new DoAfterObserver<T>(paramObserver, this.onAfterNext));
  }
  
  static final class DoAfterObserver<T> extends BasicFuseableObserver<T, T> {
    final Consumer<? super T> onAfterNext;
    
    DoAfterObserver(Observer<? super T> param1Observer, Consumer<? super T> param1Consumer) {
      super(param1Observer);
      this.onAfterNext = param1Consumer;
    }
    
    public void onNext(T param1T) {
      this.downstream.onNext(param1T);
      if (this.sourceMode == 0)
        try {
          this.onAfterNext.accept(param1T);
        } finally {
          param1T = null;
        }  
    }
    
    public T poll() throws Exception {
      Object object = this.qd.poll();
      if (object != null)
        this.onAfterNext.accept(object); 
      return (T)object;
    }
    
    public int requestFusion(int param1Int) {
      return transitiveBoundaryFusion(param1Int);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableDoAfterNext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */