package com.sun.jna;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.Map;
import java.util.WeakHashMap;

public class NativeMappedConverter implements TypeConverter {
  private static final Map<Class<?>, Reference<NativeMappedConverter>> converters = new WeakHashMap<Class<?>, Reference<NativeMappedConverter>>();
  
  private final NativeMapped instance;
  
  private final Class<?> nativeType;
  
  private final Class<?> type;
  
  public NativeMappedConverter(Class<?> paramClass) {
    if (NativeMapped.class.isAssignableFrom(paramClass)) {
      this.type = paramClass;
      this.instance = defaultValue();
      this.nativeType = this.instance.nativeType();
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Type must derive from ");
    stringBuilder.append(NativeMapped.class);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public static NativeMappedConverter getInstance(Class<?> paramClass) {
    synchronized (converters) {
      NativeMappedConverter nativeMappedConverter;
      Reference<NativeMappedConverter> reference1 = converters.get(paramClass);
      if (reference1 != null) {
        NativeMappedConverter nativeMappedConverter1 = reference1.get();
      } else {
        reference1 = null;
      } 
      Reference<NativeMappedConverter> reference2 = reference1;
      if (reference1 == null) {
        nativeMappedConverter = new NativeMappedConverter();
        this(paramClass);
        Map<Class<?>, Reference<NativeMappedConverter>> map = converters;
        reference1 = new SoftReference<NativeMappedConverter>();
        super((T)nativeMappedConverter);
        map.put(paramClass, reference1);
      } 
      return nativeMappedConverter;
    } 
  }
  
  public NativeMapped defaultValue() {
    return this.type.isEnum() ? (NativeMapped)this.type.getEnumConstants()[0] : (NativeMapped)Klass.newInstance(this.type);
  }
  
  public Object fromNative(Object paramObject, FromNativeContext paramFromNativeContext) {
    return this.instance.fromNative(paramObject, paramFromNativeContext);
  }
  
  public Class<?> nativeType() {
    return this.nativeType;
  }
  
  public Object toNative(Object paramObject, ToNativeContext paramToNativeContext) {
    Object object = paramObject;
    if (paramObject == null) {
      if (Pointer.class.isAssignableFrom(this.nativeType))
        return null; 
      object = defaultValue();
    } 
    return ((NativeMapped)object).toNative();
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/sun/jna/NativeMappedConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */