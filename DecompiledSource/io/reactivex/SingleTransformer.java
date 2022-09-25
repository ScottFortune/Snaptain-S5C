package io.reactivex;

public interface SingleTransformer<Upstream, Downstream> {
  SingleSource<Downstream> apply(Single<Upstream> paramSingle);
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/SingleTransformer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */