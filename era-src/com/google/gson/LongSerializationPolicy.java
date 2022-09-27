package com.google.gson;

public enum LongSerializationPolicy {
  DEFAULT {
    public JsonElement serialize(Long param1Long) {
      return new JsonPrimitive(param1Long);
    }
  },
  STRING {
    public JsonElement serialize(Long param1Long) {
      return new JsonPrimitive(String.valueOf(param1Long));
    }
  };
  
  static {
    $VALUES = new LongSerializationPolicy[] { DEFAULT, STRING };
  }
  
  public abstract JsonElement serialize(Long paramLong);
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/google/gson/LongSerializationPolicy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */