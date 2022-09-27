package io.reactivex.internal.fuseable;

public interface QueueFuseable<T> extends SimpleQueue<T> {
  public static final int ANY = 3;
  
  public static final int ASYNC = 2;
  
  public static final int BOUNDARY = 4;
  
  public static final int NONE = 0;
  
  public static final int SYNC = 1;
  
  int requestFusion(int paramInt);
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/fuseable/QueueFuseable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */