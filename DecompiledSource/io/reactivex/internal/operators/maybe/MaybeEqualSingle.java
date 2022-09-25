package io.reactivex.internal.operators.maybe;

import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.BiPredicate;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public final class MaybeEqualSingle<T> extends Single<Boolean> {
  final BiPredicate<? super T, ? super T> isEqual;
  
  final MaybeSource<? extends T> source1;
  
  final MaybeSource<? extends T> source2;
  
  public MaybeEqualSingle(MaybeSource<? extends T> paramMaybeSource1, MaybeSource<? extends T> paramMaybeSource2, BiPredicate<? super T, ? super T> paramBiPredicate) {
    this.source1 = paramMaybeSource1;
    this.source2 = paramMaybeSource2;
    this.isEqual = paramBiPredicate;
  }
  
  protected void subscribeActual(SingleObserver<? super Boolean> paramSingleObserver) {
    EqualCoordinator<T> equalCoordinator = new EqualCoordinator<T>(paramSingleObserver, this.isEqual);
    paramSingleObserver.onSubscribe(equalCoordinator);
    equalCoordinator.subscribe(this.source1, this.source2);
  }
  
  static final class EqualCoordinator<T> extends AtomicInteger implements Disposable {
    final SingleObserver<? super Boolean> downstream;
    
    final BiPredicate<? super T, ? super T> isEqual;
    
    final MaybeEqualSingle.EqualObserver<T> observer1;
    
    final MaybeEqualSingle.EqualObserver<T> observer2;
    
    EqualCoordinator(SingleObserver<? super Boolean> param1SingleObserver, BiPredicate<? super T, ? super T> param1BiPredicate) {
      super(2);
      this.downstream = param1SingleObserver;
      this.isEqual = param1BiPredicate;
      this.observer1 = new MaybeEqualSingle.EqualObserver<T>(this);
      this.observer2 = new MaybeEqualSingle.EqualObserver<T>(this);
    }
    
    public void dispose() {
      this.observer1.dispose();
      this.observer2.dispose();
    }
    
    void done() {
      if (decrementAndGet() == 0) {
        Object object1 = this.observer1.value;
        Object object2 = this.observer2.value;
        if (object1 != null && object2 != null) {
          try {
          
          } finally {
            object1 = null;
            Exceptions.throwIfFatal((Throwable)object1);
            this.downstream.onError((Throwable)object1);
          } 
        } else {
          boolean bool;
          SingleObserver<? super Boolean> singleObserver = this.downstream;
          if (object1 == null && object2 == null) {
            bool = true;
          } else {
            bool = false;
          } 
          singleObserver.onSuccess(Boolean.valueOf(bool));
        } 
      } 
    }
    
    void error(MaybeEqualSingle.EqualObserver<T> param1EqualObserver, Throwable param1Throwable) {
      if (getAndSet(0) > 0) {
        MaybeEqualSingle.EqualObserver<T> equalObserver = this.observer1;
        if (param1EqualObserver == equalObserver) {
          this.observer2.dispose();
        } else {
          equalObserver.dispose();
        } 
        this.downstream.onError(param1Throwable);
      } else {
        RxJavaPlugins.onError(param1Throwable);
      } 
    }
    
    public boolean isDisposed() {
      return DisposableHelper.isDisposed(this.observer1.get());
    }
    
    void subscribe(MaybeSource<? extends T> param1MaybeSource1, MaybeSource<? extends T> param1MaybeSource2) {
      param1MaybeSource1.subscribe(this.observer1);
      param1MaybeSource2.subscribe(this.observer2);
    }
  }
  
  static final class EqualObserver<T> extends AtomicReference<Disposable> implements MaybeObserver<T> {
    private static final long serialVersionUID = -3031974433025990931L;
    
    final MaybeEqualSingle.EqualCoordinator<T> parent;
    
    Object value;
    
    EqualObserver(MaybeEqualSingle.EqualCoordinator<T> param1EqualCoordinator) {
      this.parent = param1EqualCoordinator;
    }
    
    public void dispose() {
      DisposableHelper.dispose(this);
    }
    
    public void onComplete() {
      this.parent.done();
    }
    
    public void onError(Throwable param1Throwable) {
      this.parent.error(this, param1Throwable);
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      DisposableHelper.setOnce(this, param1Disposable);
    }
    
    public void onSuccess(T param1T) {
      this.value = param1T;
      this.parent.done();
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/maybe/MaybeEqualSingle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */