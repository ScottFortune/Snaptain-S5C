package io.reactivex.internal.util;

import io.reactivex.functions.BiFunction;
import java.util.List;

public enum ListAddBiConsumer implements BiFunction<List, Object, List> {
  INSTANCE;
  
  static {
    $VALUES = new ListAddBiConsumer[] { INSTANCE };
  }
  
  public static <T> BiFunction<List<T>, T, List<T>> instance() {
    return INSTANCE;
  }
  
  public List apply(List<Object> paramList, Object paramObject) throws Exception {
    paramList.add(paramObject);
    return paramList;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/util/ListAddBiConsumer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */