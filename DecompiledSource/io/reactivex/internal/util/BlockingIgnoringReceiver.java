package io.reactivex.internal.util;

import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import java.util.concurrent.CountDownLatch;

public final class BlockingIgnoringReceiver extends CountDownLatch implements Consumer<Throwable>, Action {
  public Throwable error;
  
  public BlockingIgnoringReceiver() {
    super(1);
  }
  
  public void accept(Throwable paramThrowable) {
    this.error = paramThrowable;
    countDown();
  }
  
  public void run() {
    countDown();
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/util/BlockingIgnoringReceiver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */