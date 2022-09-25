package io.reactivex.internal.operators.maybe;

import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.fuseable.HasUpstreamMaybeSource;
import java.util.concurrent.atomic.AtomicReference;

public final class MaybeSwitchIfEmptySingle<T> extends Single<T> implements HasUpstreamMaybeSource<T> {
  final SingleSource<? extends T> other;
  
  final MaybeSource<T> source;
  
  public MaybeSwitchIfEmptySingle(MaybeSource<T> paramMaybeSource, SingleSource<? extends T> paramSingleSource) {
    this.source = paramMaybeSource;
    this.other = paramSingleSource;
  }
  
  public MaybeSource<T> source() {
    return this.source;
  }
  
  protected void subscribeActual(SingleObserver<? super T> paramSingleObserver) {
    this.source.subscribe(new SwitchIfEmptyMaybeObserver<T>(paramSingleObserver, this.other));
  }
  
  static final class SwitchIfEmptyMaybeObserver<T> extends AtomicReference<Disposable> implements MaybeObserver<T>, Disposable {
    private static final long serialVersionUID = 4603919676453758899L;
    
    final SingleObserver<? super T> downstream;
    
    final SingleSource<? extends T> other;
    
    SwitchIfEmptyMaybeObserver(SingleObserver<? super T> param1SingleObserver, SingleSource<? extends T> param1SingleSource) {
      this.downstream = param1SingleObserver;
      this.other = param1SingleSource;
    }
    
    public void dispose() {
      DisposableHelper.dispose(this);
    }
    
    public boolean isDisposed() {
      return DisposableHelper.isDisposed(get());
    }
    
    public void onComplete() {
      Disposable disposable = get();
      if (disposable != DisposableHelper.DISPOSED && compareAndSet(disposable, null))
        this.other.subscribe(new OtherSingleObserver<T>(this.downstream, this)); 
    }
    
    public void onError(Throwable param1Throwable) {
      this.downstream.onError(param1Throwable);
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      if (DisposableHelper.setOnce(this, param1Disposable))
        this.downstream.onSubscribe(this); 
    }
    
    public void onSuccess(T param1T) {
      this.downstream.onSuccess(param1T);
    }
    
    static final class OtherSingleObserver<T> implements SingleObserver<T> {
      final SingleObserver<? super T> downstream;
      
      final AtomicReference<Disposable> parent;
      
      OtherSingleObserver(SingleObserver<? super T> param2SingleObserver, AtomicReference<Disposable> param2AtomicReference) {
        this.downstream = param2SingleObserver;
        this.parent = param2AtomicReference;
      }
      
      public void onError(Throwable param2Throwable) {
        this.downstream.onError(param2Throwable);
      }
      
      public void onSubscribe(Disposable param2Disposable) {
        DisposableHelper.setOnce(this.parent, param2Disposable);
      }
      
      public void onSuccess(T param2T) {
        this.downstream.onSuccess(param2T);
      }
    }
  }
  
  static final class OtherSingleObserver<T> implements SingleObserver<T> {
    final SingleObserver<? super T> downstream;
    
    final AtomicReference<Disposable> parent;
    
    OtherSingleObserver(SingleObserver<? super T> param1SingleObserver, AtomicReference<Disposable> param1AtomicReference) {
      this.downstream = param1SingleObserver;
      this.parent = param1AtomicReference;
    }
    
    public void onError(Throwable param1Throwable) {
      this.downstream.onError(param1Throwable);
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      DisposableHelper.setOnce(this.parent, param1Disposable);
    }
    
    public void onSuccess(T param1T) {
      this.downstream.onSuccess(param1T);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/maybe/MaybeSwitchIfEmptySingle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */