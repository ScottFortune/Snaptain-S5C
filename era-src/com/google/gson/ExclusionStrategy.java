package com.google.gson;

public interface ExclusionStrategy {
  boolean shouldSkipClass(Class<?> paramClass);
  
  boolean shouldSkipField(FieldAttributes paramFieldAttributes);
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/google/gson/ExclusionStrategy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */