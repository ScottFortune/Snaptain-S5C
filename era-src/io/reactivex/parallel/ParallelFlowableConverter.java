package io.reactivex.parallel;

public interface ParallelFlowableConverter<T, R> {
  R apply(ParallelFlowable<T> paramParallelFlowable);
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/parallel/ParallelFlowableConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */