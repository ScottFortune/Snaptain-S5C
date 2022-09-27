package io.reactivex.android;

import android.os.Looper;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class MainThreadDisposable implements Disposable {
  private final AtomicBoolean unsubscribed = new AtomicBoolean();
  
  public static void verifyMainThread() {
    if (Looper.myLooper() == Looper.getMainLooper())
      return; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Expected to be called on the main thread but was ");
    stringBuilder.append(Thread.currentThread().getName());
    throw new IllegalStateException(stringBuilder.toString());
  }
  
  public final void dispose() {
    if (this.unsubscribed.compareAndSet(false, true))
      if (Looper.myLooper() == Looper.getMainLooper()) {
        onDispose();
      } else {
        AndroidSchedulers.mainThread().scheduleDirect(new Runnable() {
              public void run() {
                MainThreadDisposable.this.onDispose();
              }
            });
      }  
  }
  
  public final boolean isDisposed() {
    return this.unsubscribed.get();
  }
  
  protected abstract void onDispose();
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/android/MainThreadDisposable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */