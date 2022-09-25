package com.google.gson.internal.reflect;

import java.lang.reflect.AccessibleObject;

final class PreJava9ReflectionAccessor extends ReflectionAccessor {
  public void makeAccessible(AccessibleObject paramAccessibleObject) {
    paramAccessibleObject.setAccessible(true);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/google/gson/internal/reflect/PreJava9ReflectionAccessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */