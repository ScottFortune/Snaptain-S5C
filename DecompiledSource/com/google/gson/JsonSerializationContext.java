package com.google.gson;

import java.lang.reflect.Type;

public interface JsonSerializationContext {
  JsonElement serialize(Object paramObject);
  
  JsonElement serialize(Object paramObject, Type paramType);
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/google/gson/JsonSerializationContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */