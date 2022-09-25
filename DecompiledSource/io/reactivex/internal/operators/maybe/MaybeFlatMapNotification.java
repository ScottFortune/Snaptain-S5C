package io.reactivex.internal.operators.maybe;

import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.functions.ObjectHelper;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicReference;

public final class MaybeFlatMapNotification<T, R> extends AbstractMaybeWithUpstream<T, R> {
  final Callable<? extends MaybeSource<? extends R>> onCompleteSupplier;
  
  final Function<? super Throwable, ? extends MaybeSource<? extends R>> onErrorMapper;
  
  final Function<? super T, ? extends MaybeSource<? extends R>> onSuccessMapper;
  
  public MaybeFlatMapNotification(MaybeSource<T> paramMaybeSource, Function<? super T, ? extends MaybeSource<? extends R>> paramFunction, Function<? super Throwable, ? extends MaybeSource<? extends R>> paramFunction1, Callable<? extends MaybeSource<? extends R>> paramCallable) {
    super(paramMaybeSource);
    this.onSuccessMapper = paramFunction;
    this.onErrorMapper = paramFunction1;
    this.onCompleteSupplier = paramCallable;
  }
  
  protected void subscribeActual(MaybeObserver<? super R> paramMaybeObserver) {
    this.source.subscribe(new FlatMapMaybeObserver<T, R>(paramMaybeObserver, this.onSuccessMapper, this.onErrorMapper, this.onCompleteSupplier));
  }
  
  static final class FlatMapMaybeObserver<T, R> extends AtomicReference<Disposable> implements MaybeObserver<T>, Disposable {
    private static final long serialVersionUID = 4375739915521278546L;
    
    final MaybeObserver<? super R> downstream;
    
    final Callable<? extends MaybeSource<? extends R>> onCompleteSupplier;
    
    final Function<? super Throwable, ? extends MaybeSource<? extends R>> onErrorMapper;
    
    final Function<? super T, ? extends MaybeSource<? extends R>> onSuccessMapper;
    
    Disposable upstream;
    
    FlatMapMaybeObserver(MaybeObserver<? super R> param1MaybeObserver, Function<? super T, ? extends MaybeSource<? extends R>> param1Function, Function<? super Throwable, ? extends MaybeSource<? extends R>> param1Function1, Callable<? extends MaybeSource<? extends R>> param1Callable) {
      this.downstream = param1MaybeObserver;
      this.onSuccessMapper = param1Function;
      this.onErrorMapper = param1Function1;
      this.onCompleteSupplier = param1Callable;
    }
    
    public void dispose() {
      DisposableHelper.dispose(this);
      this.upstream.dispose();
    }
    
    public boolean isDisposed() {
      return DisposableHelper.isDisposed(get());
    }
    
    public void onComplete() {
      try {
        MaybeSource maybeSource = (MaybeSource)ObjectHelper.requireNonNull(this.onCompleteSupplier.call(), "The onCompleteSupplier returned a null MaybeSource");
        maybeSource.subscribe(new InnerObserver());
        return;
      } catch (Exception exception) {
        Exceptions.throwIfFatal(exception);
        this.downstream.onError(exception);
        return;
      } 
    }
    
    public void onError(Throwable param1Throwable) {
      try {
        MaybeSource maybeSource = (MaybeSource)ObjectHelper.requireNonNull(this.onErrorMapper.apply(param1Throwable), "The onErrorMapper returned a null MaybeSource");
        maybeSource.subscribe(new InnerObserver());
        return;
      } catch (Exception exception) {
        Exceptions.throwIfFatal(exception);
        this.downstream.onError((Throwable)new CompositeException(new Throwable[] { param1Throwable, exception }));
        return;
      } 
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      if (DisposableHelper.validate(this.upstream, param1Disposable)) {
        this.upstream = param1Disposable;
        this.downstream.onSubscribe(this);
      } 
    }
    
    public void onSuccess(T param1T) {
      try {
        MaybeSource maybeSource = (MaybeSource)ObjectHelper.requireNonNull(this.onSuccessMapper.apply(param1T), "The onSuccessMapper returned a null MaybeSource");
        maybeSource.subscribe(new InnerObserver());
        return;
      } catch (Exception exception) {
        Exceptions.throwIfFatal(exception);
        this.downstream.onError(exception);
        return;
      } 
    }
    
    final class InnerObserver implements MaybeObserver<R> {
      public void onComplete() {
        MaybeFlatMapNotification.FlatMapMaybeObserver.this.downstream.onComplete();
      }
      
      public void onError(Throwable param2Throwable) {
        MaybeFlatMapNotification.FlatMapMaybeObserver.this.downstream.onError(param2Throwable);
      }
      
      public void onSubscribe(Disposable param2Disposable) {
        DisposableHelper.setOnce(MaybeFlatMapNotification.FlatMapMaybeObserver.this, param2Disposable);
      }
      
      public void onSuccess(R param2R) {
        MaybeFlatMapNotification.FlatMapMaybeObserver.this.downstream.onSuccess(param2R);
      }
    }
  }
  
  final class InnerObserver implements MaybeObserver<R> {
    public void onComplete() {
      this.this$0.downstream.onComplete();
    }
    
    public void onError(Throwable param1Throwable) {
      this.this$0.downstream.onError(param1Throwable);
    }
    
    public void onSubscribe(Disposable param1Disposable) {
      DisposableHelper.setOnce(this.this$0, param1Disposable);
    }
    
    public void onSuccess(R param1R) {
      this.this$0.downstream.onSuccess(param1R);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/maybe/MaybeFlatMapNotification.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */