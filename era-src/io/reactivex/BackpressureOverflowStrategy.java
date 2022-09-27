package io.reactivex;

public enum BackpressureOverflowStrategy {
  DROP_LATEST, DROP_OLDEST, ERROR;
  
  static {
    DROP_OLDEST = new BackpressureOverflowStrategy("DROP_OLDEST", 1);
    DROP_LATEST = new BackpressureOverflowStrategy("DROP_LATEST", 2);
    $VALUES = new BackpressureOverflowStrategy[] { ERROR, DROP_OLDEST, DROP_LATEST };
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/BackpressureOverflowStrategy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */