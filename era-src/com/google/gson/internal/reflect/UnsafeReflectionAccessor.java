package com.google.gson.internal.reflect;

import com.google.gson.JsonIOException;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;

final class UnsafeReflectionAccessor extends ReflectionAccessor {
  private static Class unsafeClass;
  
  private final Field overrideField = getOverrideField();
  
  private final Object theUnsafe = getUnsafeInstance();
  
  private static Field getOverrideField() {
    try {
      return AccessibleObject.class.getDeclaredField("override");
    } catch (NoSuchFieldException noSuchFieldException) {
      return null;
    } 
  }
  
  private static Object getUnsafeInstance() {
    Object object = null;
    try {
      unsafeClass = Class.forName("sun.misc.Unsafe");
      Field field = unsafeClass.getDeclaredField("theUnsafe");
      field.setAccessible(true);
      Object object1 = field.get(null);
      object = object1;
    } catch (Exception exception) {}
    return object;
  }
  
  public void makeAccessible(AccessibleObject paramAccessibleObject) {
    if (!makeAccessibleWithUnsafe(paramAccessibleObject))
      try {
        paramAccessibleObject.setAccessible(true);
      } catch (SecurityException securityException) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Gson couldn't modify fields for ");
        stringBuilder.append(paramAccessibleObject);
        stringBuilder.append("\nand sun.misc.Unsafe not found.\nEither write a custom type adapter, or make fields accessible, or include sun.misc.Unsafe.");
        throw new JsonIOException(stringBuilder.toString(), securityException);
      }  
  }
  
  boolean makeAccessibleWithUnsafe(AccessibleObject paramAccessibleObject) {
    if (this.theUnsafe != null && this.overrideField != null)
      try {
        long l = ((Long)unsafeClass.getMethod("objectFieldOffset", new Class[] { Field.class }).invoke(this.theUnsafe, new Object[] { this.overrideField })).longValue();
        unsafeClass.getMethod("putBoolean", new Class[] { Object.class, long.class, boolean.class }).invoke(this.theUnsafe, new Object[] { paramAccessibleObject, Long.valueOf(l), Boolean.valueOf(true) });
        return true;
      } catch (Exception exception) {} 
    return false;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/google/gson/internal/reflect/UnsafeReflectionAccessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */