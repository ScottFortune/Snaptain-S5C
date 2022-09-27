package com.netopsun.fhdevices;

import com.netopsun.deviceshub.interfaces.Cancelable;
import io.reactivex.disposables.Disposable;

public class Disposable2Cancelable implements Cancelable {
  final Disposable disposable;
  
  public Disposable2Cancelable(Disposable paramDisposable) {
    this.disposable = paramDisposable;
  }
  
  public void cancel() {
    Disposable disposable = this.disposable;
    if (disposable != null)
      disposable.dispose(); 
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhdevices/Disposable2Cancelable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */