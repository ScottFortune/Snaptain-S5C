package io.reactivex;

import io.reactivex.disposables.Disposable;

public interface SingleObserver<T> {
  void onError(Throwable paramThrowable);
  
  void onSubscribe(Disposable paramDisposable);
  
  void onSuccess(T paramT);
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/SingleObserver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */