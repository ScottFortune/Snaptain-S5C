package io.reactivex.internal.operators.single;

import io.reactivex.CompletableObserver;
import io.reactivex.CompletableSource;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.observers.ResumeSingleObserver;
import java.util.concurrent.atomic.AtomicReference;

public final class SingleDelayWithCompletable<T> extends Single<T> {
  final CompletableSource other;
  
  final SingleSource<T> source;
  
  public SingleDelayWithCompletable(SingleSource<T> paramSingleSource, CompletableSource paramCompletableSource) {
    this.source = paramSingleSource;
    this.other = paramCompletableSource;
  }
  
  protected void subscribeActual(SingleObserver<? super T> paramSingleObserver) {
    this.other.subscribe(new OtherObserver<T>(paramSingleObserver, this.source));
  }
  
  static final class OtherObserver<T> extends AtomicReference<Disposable> implements CompletableObserver, Disposable {
    private static final long serialVersionUID = -8565274649390031272L;
    
    final SingleObserver<? super T> downstream;
    
    final SingleSource<T> source;
    
    OtherObserver(SingleObserver<? super T> param1SingleObserver, SingleSource<T> param1SingleSource) {
      this.downstream = param1SingleObserver;
      this.source = param1SingleSource;
    }
    
    public void dispose() {
      DisposableHelper.dispose(this);
    }
    
    public boolean isDisposed() {
      return DisposableHelper.isDisposed(get());
    }
    
    public void onComplete() {
      this.source.subscribe((SingleObserver)new ResumeSingleObserver(this, this.downstream));
    }
    
    public void onError(Throwable param1Throwable) {
      this.downstream.onError(param1Throwable);
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      if (DisposableHelper.setOnce(this, param1Disposable))
        this.downstream.onSubscribe(this); 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/single/SingleDelayWithCompletable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */