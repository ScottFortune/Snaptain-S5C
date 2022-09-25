package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.observers.BasicIntQueueDisposable;

public final class ObservableRangeLong extends Observable<Long> {
  private final long count;
  
  private final long start;
  
  public ObservableRangeLong(long paramLong1, long paramLong2) {
    this.start = paramLong1;
    this.count = paramLong2;
  }
  
  protected void subscribeActual(Observer<? super Long> paramObserver) {
    long l = this.start;
    RangeDisposable rangeDisposable = new RangeDisposable(paramObserver, l, l + this.count);
    paramObserver.onSubscribe((Disposable)rangeDisposable);
    rangeDisposable.run();
  }
  
  static final class RangeDisposable extends BasicIntQueueDisposable<Long> {
    private static final long serialVersionUID = 396518478098735504L;
    
    final Observer<? super Long> downstream;
    
    final long end;
    
    boolean fused;
    
    long index;
    
    RangeDisposable(Observer<? super Long> param1Observer, long param1Long1, long param1Long2) {
      this.downstream = param1Observer;
      this.index = param1Long1;
      this.end = param1Long2;
    }
    
    public void clear() {
      this.index = this.end;
      lazySet(1);
    }
    
    public void dispose() {
      set(1);
    }
    
    public boolean isDisposed() {
      boolean bool;
      if (get() != 0) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public boolean isEmpty() {
      boolean bool;
      if (this.index == this.end) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public Long poll() throws Exception {
      long l = this.index;
      if (l != this.end) {
        this.index = 1L + l;
        return Long.valueOf(l);
      } 
      lazySet(1);
      return null;
    }
    
    public int requestFusion(int param1Int) {
      if ((param1Int & 0x1) != 0) {
        this.fused = true;
        return 1;
      } 
      return 0;
    }
    
    void run() {
      if (this.fused)
        return; 
      Observer<? super Long> observer = this.downstream;
      long l1 = this.end;
      long l2;
      for (l2 = this.index; l2 != l1 && get() == 0; l2++)
        observer.onNext(Long.valueOf(l2)); 
      if (get() == 0) {
        lazySet(1);
        observer.onComplete();
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/observable/ObservableRangeLong.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */