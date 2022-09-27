package io.reactivex.internal.operators.completable;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableSource;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.functions.ObjectHelper;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

public final class CompletableMergeDelayErrorIterable extends Completable {
  final Iterable<? extends CompletableSource> sources;
  
  public CompletableMergeDelayErrorIterable(Iterable<? extends CompletableSource> paramIterable) {
    this.sources = paramIterable;
  }
  
  public void subscribeActual(CompletableObserver paramCompletableObserver) {
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    paramCompletableObserver.onSubscribe((Disposable)compositeDisposable);
    try {
      Iterator iterator = (Iterator)ObjectHelper.requireNonNull(this.sources.iterator(), "The source iterator returned is null");
      AtomicInteger atomicInteger = new AtomicInteger(1);
      return;
    } finally {
      Exception exception = null;
      Exceptions.throwIfFatal(exception);
      paramCompletableObserver.onError(exception);
    } 
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/completable/CompletableMergeDelayErrorIterable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */