package io.reactivex;

public interface SingleConverter<T, R> {
  R apply(Single<T> paramSingle);
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/SingleConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */