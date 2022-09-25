package com.dd.plist;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public abstract class NSObject {
  static final int ASCII_LINE_LENGTH = 80;
  
  static final String INDENT = "\t";
  
  static final String NEWLINE = System.getProperty("line.separator");
  
  public static NSArray wrap(Object[] paramArrayOfObject) {
    NSArray nSArray = new NSArray(paramArrayOfObject.length);
    for (byte b = 0; b < paramArrayOfObject.length; b++)
      nSArray.setValue(b, wrap(paramArrayOfObject[b])); 
    return nSArray;
  }
  
  public static NSData wrap(byte[] paramArrayOfbyte) {
    return new NSData(paramArrayOfbyte);
  }
  
  public static NSDictionary wrap(Map<String, Object> paramMap) {
    NSDictionary nSDictionary = new NSDictionary();
    for (String str : paramMap.keySet())
      nSDictionary.put(str, wrap(paramMap.get(str))); 
    return nSDictionary;
  }
  
  public static NSNumber wrap(double paramDouble) {
    return new NSNumber(paramDouble);
  }
  
  public static NSNumber wrap(long paramLong) {
    return new NSNumber(paramLong);
  }
  
  public static NSNumber wrap(boolean paramBoolean) {
    return new NSNumber(paramBoolean);
  }
  
  public static NSObject wrap(Object paramObject) {
    NSArray nSArray;
    NSDictionary nSDictionary;
    if (paramObject == null)
      return null; 
    if (paramObject instanceof NSObject)
      return (NSObject)paramObject; 
    Class<?> clazz = paramObject.getClass();
    if (Boolean.class.equals(clazz))
      return wrap(((Boolean)paramObject).booleanValue()); 
    if (Byte.class.equals(clazz))
      return wrap(((Byte)paramObject).byteValue()); 
    if (Short.class.equals(clazz))
      return wrap(((Short)paramObject).shortValue()); 
    if (Integer.class.equals(clazz))
      return wrap(((Integer)paramObject).intValue()); 
    if (Long.class.isAssignableFrom(clazz))
      return wrap(((Long)paramObject).longValue()); 
    if (Float.class.equals(clazz))
      return wrap(((Float)paramObject).floatValue()); 
    if (Double.class.isAssignableFrom(clazz))
      return wrap(((Double)paramObject).doubleValue()); 
    if (String.class.equals(clazz))
      return new NSString((String)paramObject); 
    if (Date.class.equals(clazz))
      return new NSDate((Date)paramObject); 
    if (clazz.isArray()) {
      boolean[] arrayOfBoolean;
      float[] arrayOfFloat;
      clazz = clazz.getComponentType();
      if (clazz.equals(byte.class))
        return wrap((byte[])paramObject); 
      boolean bool = clazz.equals(boolean.class);
      boolean bool1 = false;
      boolean bool2 = false;
      boolean bool3 = false;
      boolean bool4 = false;
      boolean bool5 = false;
      byte b = 0;
      if (bool) {
        arrayOfBoolean = (boolean[])paramObject;
        paramObject = new NSArray(arrayOfBoolean.length);
        while (b < arrayOfBoolean.length) {
          paramObject.setValue(b, wrap(arrayOfBoolean[b]));
          b++;
        } 
        return (NSObject)paramObject;
      } 
      if (float.class.equals(arrayOfBoolean)) {
        arrayOfFloat = (float[])paramObject;
        paramObject = new NSArray(arrayOfFloat.length);
        for (b = bool1; b < arrayOfFloat.length; b++)
          paramObject.setValue(b, wrap(arrayOfFloat[b])); 
        return (NSObject)paramObject;
      } 
      if (double.class.equals(arrayOfFloat)) {
        paramObject = paramObject;
        nSArray = new NSArray(paramObject.length);
        for (b = bool2; b < paramObject.length; b++)
          nSArray.setValue(b, wrap(paramObject[b])); 
        return nSArray;
      } 
      if (short.class.equals(nSArray)) {
        paramObject = paramObject;
        nSArray = new NSArray(paramObject.length);
        for (b = bool3; b < paramObject.length; b++)
          nSArray.setValue(b, wrap(paramObject[b])); 
        return nSArray;
      } 
      if (int.class.equals(nSArray)) {
        paramObject = paramObject;
        nSArray = new NSArray(paramObject.length);
        for (b = bool4; b < paramObject.length; b++)
          nSArray.setValue(b, wrap(paramObject[b])); 
        return nSArray;
      } 
      if (long.class.equals(nSArray)) {
        paramObject = paramObject;
        nSArray = new NSArray(paramObject.length);
        for (b = bool5; b < paramObject.length; b++)
          nSArray.setValue(b, wrap(paramObject[b])); 
        return nSArray;
      } 
      return wrap((Object[])paramObject);
    } 
    if (Map.class.isAssignableFrom((Class<?>)nSArray)) {
      paramObject = paramObject;
      Set set = paramObject.keySet();
      nSDictionary = new NSDictionary();
      for (Object object : set) {
        Object object1 = paramObject.get(object);
        nSDictionary.put(String.valueOf(object), wrap(object1));
      } 
      return nSDictionary;
    } 
    return (NSObject)(Collection.class.isAssignableFrom((Class<?>)nSDictionary) ? wrap(((Collection)paramObject).toArray()) : wrapSerialized(paramObject));
  }
  
  public static NSSet wrap(Set<Object> paramSet) {
    NSSet nSSet = new NSSet();
    Object[] arrayOfObject = paramSet.toArray();
    int i = arrayOfObject.length;
    for (byte b = 0; b < i; b++)
      nSSet.addObject(wrap(arrayOfObject[b])); 
    return nSSet;
  }
  
  public static NSData wrapSerialized(Object paramObject) {
    try {
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      this();
      ObjectOutputStream objectOutputStream = new ObjectOutputStream();
      this(byteArrayOutputStream);
      objectOutputStream.writeObject(paramObject);
      return new NSData(byteArrayOutputStream.toByteArray());
    } catch (IOException iOException) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("The given object of class ");
      stringBuilder.append(paramObject.getClass().toString());
      stringBuilder.append(" could not be serialized and stored in a NSData object.");
      throw new RuntimeException(stringBuilder.toString());
    } 
  }
  
  void assignIDs(BinaryPropertyListWriter paramBinaryPropertyListWriter) {
    paramBinaryPropertyListWriter.assignID(this);
  }
  
  void indent(StringBuilder paramStringBuilder, int paramInt) {
    for (byte b = 0; b < paramInt; b++)
      paramStringBuilder.append("\t"); 
  }
  
  protected abstract void toASCII(StringBuilder paramStringBuilder, int paramInt);
  
  protected abstract void toASCIIGnuStep(StringBuilder paramStringBuilder, int paramInt);
  
  abstract void toBinary(BinaryPropertyListWriter paramBinaryPropertyListWriter) throws IOException;
  
  public Object toJavaObject() {
    if (this instanceof NSArray) {
      NSObject[] arrayOfNSObject = ((NSArray)this).getArray();
      Object[] arrayOfObject = new Object[arrayOfNSObject.length];
      for (byte b = 0; b < arrayOfNSObject.length; b++)
        arrayOfObject[b] = arrayOfNSObject[b].toJavaObject(); 
      return arrayOfObject;
    } 
    if (this instanceof NSDictionary) {
      HashMap<String, NSObject> hashMap = ((NSDictionary)this).getHashMap();
      HashMap<Object, Object> hashMap1 = new HashMap<Object, Object>(hashMap.size());
      for (String str : hashMap.keySet())
        hashMap1.put(str, ((NSObject)hashMap.get(str)).toJavaObject()); 
      return hashMap1;
    } 
    if (this instanceof NSSet) {
      TreeSet<Object> treeSet;
      Set<NSObject> set = ((NSSet)this).getSet();
      if (set instanceof LinkedHashSet) {
        LinkedHashSet linkedHashSet = new LinkedHashSet(set.size());
      } else {
        treeSet = new TreeSet();
      } 
      Iterator<NSObject> iterator = set.iterator();
      while (iterator.hasNext())
        treeSet.add(((NSObject)iterator.next()).toJavaObject()); 
      return treeSet;
    } 
    if (this instanceof NSNumber) {
      NSNumber nSNumber = (NSNumber)this;
      int i = nSNumber.type();
      if (i != 0)
        return (i != 1) ? ((i != 2) ? Double.valueOf(nSNumber.doubleValue()) : Boolean.valueOf(nSNumber.boolValue())) : Double.valueOf(nSNumber.doubleValue()); 
      long l = nSNumber.longValue();
      return (l > 2147483647L || l < -2147483648L) ? Long.valueOf(l) : Integer.valueOf(nSNumber.intValue());
    } 
    return (this instanceof NSString) ? ((NSString)this).getContent() : ((this instanceof NSData) ? ((NSData)this).bytes() : ((this instanceof NSDate) ? ((NSDate)this).getDate() : ((this instanceof UID) ? ((UID)this).getBytes() : this)));
  }
  
  abstract void toXML(StringBuilder paramStringBuilder, int paramInt);
  
  public String toXMLPropertyList() {
    StringBuilder stringBuilder = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    stringBuilder.append(NEWLINE);
    stringBuilder.append("<!DOCTYPE plist PUBLIC \"-//Apple//DTD PLIST 1.0//EN\" \"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">");
    stringBuilder.append(NEWLINE);
    stringBuilder.append("<plist version=\"1.0\">");
    stringBuilder.append(NEWLINE);
    toXML(stringBuilder, 0);
    stringBuilder.append(NEWLINE);
    stringBuilder.append("</plist>");
    return stringBuilder.toString();
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/dd/plist/NSObject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */