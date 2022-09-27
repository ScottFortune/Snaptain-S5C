package io.reactivex.internal.fuseable;

import io.reactivex.Flowable;

public interface FuseToFlowable<T> {
  Flowable<T> fuseToFlowable();
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/fuseable/FuseToFlowable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */