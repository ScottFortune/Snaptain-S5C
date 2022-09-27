package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.NoSuchElementException;

public final class ObservableElementAt<T> extends AbstractObservableWithUpstream<T, T> {
  final T defaultValue;
  
  final boolean errorOnFewer;
  
  final long index;
  
  public ObservableElementAt(ObservableSource<T> paramObservableSource, long paramLong, T paramT, boolean paramBoolean) {
    super(paramObservableSource);
    this.index = paramLong;
    this.defaultValue = paramT;
    this.errorOnFewer = paramBoolean;
  }
  
  public void subscribeActual(Observer<? super T> paramObserver) {
    this.source.subscribe(new ElementAtObserver<T>(paramObserver, this.index, this.defaultValue, this.errorOnFewer));
  }
  
  static final class ElementAtObserver<T> implements Observer<T>, Disposable {
    long count;
    
    final T defaultValue;
    
    boolean done;
    
    final Observer<? super T> downstream;
    
    final boolean errorOnFewer;
    
    final long index;
    
    Disposable upstream;
    
    ElementAtObserver(Observer<? super T> param1Observer, long param1Long, T param1T, boolean param1Boolean) {
      this.downstream = param1Observer;
      this.index = param1Long;
      this.defaultValue = param1T;
      this.errorOnFewer = param1Boolean;
    }
    
    public void dispose() {
      this.upstream.dispose();
    }
    
    public boolean isDisposed() {
      return this.upstream.isDisposed();
    }
    
    public void onComplete() {
      if (!this.done) {
        this.done = true;
        T t = this.defaultValue;
        if (t == null && this.errorOnFewer) {
          this.downstream.onError(new NoSuchElementException());
        } else {
          if (t != null)
            this.downstream.onNext(t); 
          this.downstream.onComplete();
        } 
      } 
    }
    
    public void onError(Throwable param1Throwable) {
      if (this.done) {
        RxJavaPlugins.onError(param1Throwable);
        return;
      } 
      this.done = true;
      this.downstream.onError(param1Throwable);
    }
    
    public void onNext(T param1T) {
      if (this.done)
        return; 
      long l = this.count;
      if (l == this.index) {
        this.done = true;
        this.upstream.dispose();
        this.downstream.onNext(param1T);
        this.downstream.onComplete();
        return;
      } 
      this.count = l + 1L;
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      if (DisposableHelper.validate(this.upstream, param1Disposable)) {
        this.upstream = param1Disposable;
        this.downstream.onSubscribe(this);
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableElementAt.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */