package io.reactivex.internal.observers;

public final class BlockingLastObserver<T> extends BlockingBaseObserver<T> {
  public void onError(Throwable paramThrowable) {
    this.value = null;
    this.error = paramThrowable;
    countDown();
  }
  
  public void onNext(T paramT) {
    this.value = paramT;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/observers/BlockingLastObserver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */