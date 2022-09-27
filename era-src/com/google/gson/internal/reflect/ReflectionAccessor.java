package com.google.gson.internal.reflect;

import com.google.gson.internal.JavaVersion;
import java.lang.reflect.AccessibleObject;

public abstract class ReflectionAccessor {
  private static final ReflectionAccessor instance;
  
  static {
    UnsafeReflectionAccessor unsafeReflectionAccessor;
    if (JavaVersion.getMajorJavaVersion() < 9) {
      PreJava9ReflectionAccessor preJava9ReflectionAccessor = new PreJava9ReflectionAccessor();
    } else {
      unsafeReflectionAccessor = new UnsafeReflectionAccessor();
    } 
    instance = unsafeReflectionAccessor;
  }
  
  public static ReflectionAccessor getInstance() {
    return instance;
  }
  
  public abstract void makeAccessible(AccessibleObject paramAccessibleObject);
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/google/gson/internal/reflect/ReflectionAccessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */