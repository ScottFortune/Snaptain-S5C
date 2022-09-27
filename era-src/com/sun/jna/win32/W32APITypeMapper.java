package com.sun.jna.win32;

import com.sun.jna.DefaultTypeMapper;
import com.sun.jna.FromNativeContext;
import com.sun.jna.StringArray;
import com.sun.jna.ToNativeContext;
import com.sun.jna.ToNativeConverter;
import com.sun.jna.TypeConverter;
import com.sun.jna.TypeMapper;
import com.sun.jna.WString;

public class W32APITypeMapper extends DefaultTypeMapper {
  static {
    ASCII = (TypeMapper)new W32APITypeMapper(false);
    if (Boolean.getBoolean("w32.ascii")) {
      typeMapper = ASCII;
    } else {
      typeMapper = UNICODE;
    } 
    DEFAULT = typeMapper;
  }
  
  protected W32APITypeMapper(boolean paramBoolean) {
    if (paramBoolean) {
      TypeConverter typeConverter = new TypeConverter() {
          public Object fromNative(Object param1Object, FromNativeContext param1FromNativeContext) {
            return (param1Object == null) ? null : param1Object.toString();
          }
          
          public Class<?> nativeType() {
            return WString.class;
          }
          
          public Object toNative(Object param1Object, ToNativeContext param1ToNativeContext) {
            return (param1Object == null) ? null : ((param1Object instanceof String[]) ? new StringArray((String[])param1Object, true) : new WString(param1Object.toString()));
          }
        };
      addTypeConverter(String.class, typeConverter);
      addToNativeConverter(String[].class, (ToNativeConverter)typeConverter);
    } 
    addTypeConverter(Boolean.class, new TypeConverter() {
          public Object fromNative(Object param1Object, FromNativeContext param1FromNativeContext) {
            if (((Integer)param1Object).intValue() != 0) {
              param1Object = Boolean.TRUE;
            } else {
              param1Object = Boolean.FALSE;
            } 
            return param1Object;
          }
          
          public Class<?> nativeType() {
            return Integer.class;
          }
          
          public Object toNative(Object param1Object, ToNativeContext param1ToNativeContext) {
            return Integer.valueOf(Boolean.TRUE.equals(param1Object));
          }
        });
  }
  
  static {
    TypeMapper typeMapper;
  }
  
  public static final TypeMapper ASCII;
  
  public static final TypeMapper DEFAULT;
  
  public static final TypeMapper UNICODE = (TypeMapper)new W32APITypeMapper(true);
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/sun/jna/win32/W32APITypeMapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */