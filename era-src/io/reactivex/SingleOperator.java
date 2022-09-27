package io.reactivex;

public interface SingleOperator<Downstream, Upstream> {
  SingleObserver<? super Upstream> apply(SingleObserver<? super Downstream> paramSingleObserver) throws Exception;
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/SingleOperator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */