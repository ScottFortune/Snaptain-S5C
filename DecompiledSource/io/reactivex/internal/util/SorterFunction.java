package io.reactivex.internal.util;

import io.reactivex.functions.Function;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public final class SorterFunction<T> implements Function<List<T>, List<T>> {
  final Comparator<? super T> comparator;
  
  public SorterFunction(Comparator<? super T> paramComparator) {
    this.comparator = paramComparator;
  }
  
  public List<T> apply(List<T> paramList) throws Exception {
    Collections.sort(paramList, this.comparator);
    return paramList;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/util/SorterFunction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */