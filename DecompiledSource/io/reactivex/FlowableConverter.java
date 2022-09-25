package io.reactivex;

public interface FlowableConverter<T, R> {
  R apply(Flowable<T> paramFlowable);
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/FlowableConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */