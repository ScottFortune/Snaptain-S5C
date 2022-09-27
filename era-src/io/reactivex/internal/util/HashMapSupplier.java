package io.reactivex.internal.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public enum HashMapSupplier implements Callable<Map<Object, Object>> {
  INSTANCE;
  
  static {
    $VALUES = new HashMapSupplier[] { INSTANCE };
  }
  
  public static <K, V> Callable<Map<K, V>> asCallable() {
    return INSTANCE;
  }
  
  public Map<Object, Object> call() throws Exception {
    return new HashMap<Object, Object>();
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/util/HashMapSupplier.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */