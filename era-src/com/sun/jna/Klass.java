package com.sun.jna;

import java.lang.reflect.InvocationTargetException;

abstract class Klass {
  public static <T> T newInstance(Class<T> paramClass) {
    try {
      return (T)paramClass.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
    } catch (IllegalAccessException illegalAccessException) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Can't create an instance of ");
      stringBuilder.append(paramClass);
      stringBuilder.append(", requires a public no-arg constructor: ");
      stringBuilder.append(illegalAccessException);
      throw new IllegalArgumentException(stringBuilder.toString(), illegalAccessException);
    } catch (IllegalArgumentException illegalArgumentException) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Can't create an instance of ");
      stringBuilder.append(paramClass);
      stringBuilder.append(", requires a public no-arg constructor: ");
      stringBuilder.append(illegalArgumentException);
      throw new IllegalArgumentException(stringBuilder.toString(), illegalArgumentException);
    } catch (InstantiationException instantiationException) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Can't create an instance of ");
      stringBuilder.append(paramClass);
      stringBuilder.append(", requires a public no-arg constructor: ");
      stringBuilder.append(instantiationException);
      throw new IllegalArgumentException(stringBuilder.toString(), instantiationException);
    } catch (NoSuchMethodException noSuchMethodException) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Can't create an instance of ");
      stringBuilder.append(paramClass);
      stringBuilder.append(", requires a public no-arg constructor: ");
      stringBuilder.append(noSuchMethodException);
      throw new IllegalArgumentException(stringBuilder.toString(), noSuchMethodException);
    } catch (SecurityException securityException) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Can't create an instance of ");
      stringBuilder.append(paramClass);
      stringBuilder.append(", requires a public no-arg constructor: ");
      stringBuilder.append(securityException);
      throw new IllegalArgumentException(stringBuilder.toString(), securityException);
    } catch (InvocationTargetException invocationTargetException) {
      if (invocationTargetException.getCause() instanceof RuntimeException)
        throw (RuntimeException)invocationTargetException.getCause(); 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Can't create an instance of ");
      stringBuilder.append(paramClass);
      stringBuilder.append(", requires a public no-arg constructor: ");
      stringBuilder.append(invocationTargetException);
      throw new IllegalArgumentException(stringBuilder.toString(), invocationTargetException);
    } 
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/sun/jna/Klass.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */