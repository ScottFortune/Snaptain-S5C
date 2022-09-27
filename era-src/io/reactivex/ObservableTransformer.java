package io.reactivex;

public interface ObservableTransformer<Upstream, Downstream> {
  ObservableSource<Downstream> apply(Observable<Upstream> paramObservable);
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/ObservableTransformer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */