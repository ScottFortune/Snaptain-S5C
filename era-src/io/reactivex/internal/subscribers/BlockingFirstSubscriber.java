package io.reactivex.internal.subscribers;

import io.reactivex.plugins.RxJavaPlugins;

public final class BlockingFirstSubscriber<T> extends BlockingBaseSubscriber<T> {
  public void onError(Throwable paramThrowable) {
    if (this.value == null) {
      this.error = paramThrowable;
    } else {
      RxJavaPlugins.onError(paramThrowable);
    } 
    countDown();
  }
  
  public void onNext(T paramT) {
    if (this.value == null) {
      this.value = paramT;
      this.upstream.cancel();
      countDown();
    } 
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/subscribers/BlockingFirstSubscriber.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */