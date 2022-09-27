package io.reactivex.internal.observers;

public final class BlockingFirstObserver<T> extends BlockingBaseObserver<T> {
  public void onError(Throwable paramThrowable) {
    if (this.value == null)
      this.error = paramThrowable; 
    countDown();
  }
  
  public void onNext(T paramT) {
    if (this.value == null) {
      this.value = paramT;
      this.upstream.dispose();
      countDown();
    } 
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/observers/BlockingFirstObserver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */