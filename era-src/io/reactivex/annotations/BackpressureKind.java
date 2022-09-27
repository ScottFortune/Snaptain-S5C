package io.reactivex.annotations;

public enum BackpressureKind {
  ERROR, FULL, NONE, PASS_THROUGH, SPECIAL, UNBOUNDED_IN;
  
  static {
    FULL = new BackpressureKind("FULL", 1);
    SPECIAL = new BackpressureKind("SPECIAL", 2);
    UNBOUNDED_IN = new BackpressureKind("UNBOUNDED_IN", 3);
    ERROR = new BackpressureKind("ERROR", 4);
    NONE = new BackpressureKind("NONE", 5);
    $VALUES = new BackpressureKind[] { PASS_THROUGH, FULL, SPECIAL, UNBOUNDED_IN, ERROR, NONE };
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/annotations/BackpressureKind.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */