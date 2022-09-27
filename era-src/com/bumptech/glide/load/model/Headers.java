package com.bumptech.glide.load.model;

import java.util.Collections;
import java.util.Map;

public interface Headers {
  public static final Headers DEFAULT;
  
  @Deprecated
  public static final Headers NONE = new Headers() {
      public Map<String, String> getHeaders() {
        return Collections.emptyMap();
      }
    };
  
  static {
    DEFAULT = (new LazyHeaders.Builder()).build();
  }
  
  Map<String, String> getHeaders();
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/model/Headers.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */