package com.sun.jna;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DefaultTypeMapper implements TypeMapper {
  private List<Entry> fromNativeConverters = new ArrayList<Entry>();
  
  private List<Entry> toNativeConverters = new ArrayList<Entry>();
  
  private Class<?> getAltClass(Class<?> paramClass) {
    return (Class<?>)((paramClass == Boolean.class) ? boolean.class : ((paramClass == boolean.class) ? Boolean.class : ((paramClass == Byte.class) ? byte.class : ((paramClass == byte.class) ? Byte.class : ((paramClass == Character.class) ? char.class : ((paramClass == char.class) ? Character.class : ((paramClass == Short.class) ? short.class : ((paramClass == short.class) ? Short.class : ((paramClass == Integer.class) ? int.class : ((paramClass == int.class) ? Integer.class : ((paramClass == Long.class) ? long.class : ((paramClass == long.class) ? Long.class : ((paramClass == Float.class) ? float.class : ((paramClass == float.class) ? Float.class : ((paramClass == Double.class) ? double.class : ((paramClass == double.class) ? Double.class : null))))))))))))))));
  }
  
  private Object lookupConverter(Class<?> paramClass, Collection<? extends Entry> paramCollection) {
    for (Entry entry : paramCollection) {
      if (entry.type.isAssignableFrom(paramClass))
        return entry.converter; 
    } 
    return null;
  }
  
  public void addFromNativeConverter(Class<?> paramClass, FromNativeConverter paramFromNativeConverter) {
    this.fromNativeConverters.add(new Entry(paramClass, paramFromNativeConverter));
    paramClass = getAltClass(paramClass);
    if (paramClass != null)
      this.fromNativeConverters.add(new Entry(paramClass, paramFromNativeConverter)); 
  }
  
  public void addToNativeConverter(Class<?> paramClass, ToNativeConverter paramToNativeConverter) {
    this.toNativeConverters.add(new Entry(paramClass, paramToNativeConverter));
    paramClass = getAltClass(paramClass);
    if (paramClass != null)
      this.toNativeConverters.add(new Entry(paramClass, paramToNativeConverter)); 
  }
  
  public void addTypeConverter(Class<?> paramClass, TypeConverter paramTypeConverter) {
    addFromNativeConverter(paramClass, paramTypeConverter);
    addToNativeConverter(paramClass, paramTypeConverter);
  }
  
  public FromNativeConverter getFromNativeConverter(Class<?> paramClass) {
    return (FromNativeConverter)lookupConverter(paramClass, this.fromNativeConverters);
  }
  
  public ToNativeConverter getToNativeConverter(Class<?> paramClass) {
    return (ToNativeConverter)lookupConverter(paramClass, this.toNativeConverters);
  }
  
  private static class Entry {
    public Object converter;
    
    public Class<?> type;
    
    public Entry(Class<?> param1Class, Object param1Object) {
      this.type = param1Class;
      this.converter = param1Object;
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/sun/jna/DefaultTypeMapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */