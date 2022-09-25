package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.util.EmptyComponent;

public final class ObservableDetach<T> extends AbstractObservableWithUpstream<T, T> {
  public ObservableDetach(ObservableSource<T> paramObservableSource) {
    super(paramObservableSource);
  }
  
  protected void subscribeActual(Observer<? super T> paramObserver) {
    this.source.subscribe(new DetachObserver<T>(paramObserver));
  }
  
  static final class DetachObserver<T> implements Observer<T>, Disposable {
    Observer<? super T> downstream;
    
    Disposable upstream;
    
    DetachObserver(Observer<? super T> param1Observer) {
      this.downstream = param1Observer;
    }
    
    public void dispose() {
      Disposable disposable = this.upstream;
      this.upstream = (Disposable)EmptyComponent.INSTANCE;
      this.downstream = EmptyComponent.asObserver();
      disposable.dispose();
    }
    
    public boolean isDisposed() {
      return this.upstream.isDisposed();
    }
    
    public void onComplete() {
      Observer<? super T> observer = this.downstream;
      this.upstream = (Disposable)EmptyComponent.INSTANCE;
      this.downstream = EmptyComponent.asObserver();
      observer.onComplete();
    }
    
    public void onError(Throwable param1Throwable) {
      Observer<? super T> observer = this.downstream;
      this.upstream = (Disposable)EmptyComponent.INSTANCE;
      this.downstream = EmptyComponent.asObserver();
      observer.onError(param1Throwable);
    }
    
    public void onNext(T param1T) {
      this.downstream.onNext(param1T);
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      if (DisposableHelper.validate(this.upstream, param1Disposable)) {
        this.upstream = param1Disposable;
        this.downstream.onSubscribe(this);
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableDetach.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */