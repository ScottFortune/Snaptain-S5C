package io.reactivex;

public interface MaybeTransformer<Upstream, Downstream> {
  MaybeSource<Downstream> apply(Maybe<Upstream> paramMaybe);
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/MaybeTransformer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */