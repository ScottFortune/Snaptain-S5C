package io.reactivex.internal.util;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public final class ConnectConsumer implements Consumer<Disposable> {
  public Disposable disposable;
  
  public void accept(Disposable paramDisposable) throws Exception {
    this.disposable = paramDisposable;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/util/ConnectConsumer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */