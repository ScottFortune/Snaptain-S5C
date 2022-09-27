package io.reactivex.internal.fuseable;

import io.reactivex.MaybeSource;

public interface HasUpstreamMaybeSource<T> {
  MaybeSource<T> source();
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/fuseable/HasUpstreamMaybeSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */