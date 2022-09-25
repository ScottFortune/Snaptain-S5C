package io.reactivex.internal.operators.completable;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableSource;

public final class CompletableFromUnsafeSource extends Completable {
  final CompletableSource source;
  
  public CompletableFromUnsafeSource(CompletableSource paramCompletableSource) {
    this.source = paramCompletableSource;
  }
  
  protected void subscribeActual(CompletableObserver paramCompletableObserver) {
    this.source.subscribe(paramCompletableObserver);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/completable/CompletableFromUnsafeSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */