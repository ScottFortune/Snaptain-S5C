package io.reactivex;

public interface ObservableOperator<Downstream, Upstream> {
  Observer<? super Upstream> apply(Observer<? super Downstream> paramObserver) throws Exception;
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/ObservableOperator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */