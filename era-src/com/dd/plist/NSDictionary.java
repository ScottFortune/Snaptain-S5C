package com.dd.plist;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class NSDictionary extends NSObject implements Map<String, NSObject> {
  private HashMap<String, NSObject> dict = new LinkedHashMap<String, NSObject>();
  
  public String[] allKeys() {
    return (String[])this.dict.keySet().toArray((Object[])new String[count()]);
  }
  
  void assignIDs(BinaryPropertyListWriter paramBinaryPropertyListWriter) {
    super.assignIDs(paramBinaryPropertyListWriter);
    for (Map.Entry<String, NSObject> entry : this.dict.entrySet()) {
      (new NSString((String)entry.getKey())).assignIDs(paramBinaryPropertyListWriter);
      ((NSObject)entry.getValue()).assignIDs(paramBinaryPropertyListWriter);
    } 
  }
  
  public void clear() {
    this.dict.clear();
  }
  
  public boolean containsKey(Object paramObject) {
    return this.dict.containsKey(paramObject);
  }
  
  public boolean containsKey(String paramString) {
    return this.dict.containsKey(paramString);
  }
  
  public boolean containsValue(double paramDouble) {
    for (NSObject nSObject : this.dict.values()) {
      if (nSObject.getClass().equals(NSNumber.class)) {
        nSObject = nSObject;
        if (nSObject.isReal() && nSObject.doubleValue() == paramDouble)
          return true; 
      } 
    } 
    return false;
  }
  
  public boolean containsValue(long paramLong) {
    for (NSObject nSObject : this.dict.values()) {
      if (nSObject.getClass().equals(NSNumber.class)) {
        nSObject = nSObject;
        if (nSObject.isInteger() && nSObject.intValue() == paramLong)
          return true; 
      } 
    } 
    return false;
  }
  
  public boolean containsValue(NSObject paramNSObject) {
    boolean bool;
    if (paramNSObject != null && this.dict.containsValue(paramNSObject)) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean containsValue(Object paramObject) {
    if (paramObject == null)
      return false; 
    paramObject = NSObject.wrap(paramObject);
    return this.dict.containsValue(paramObject);
  }
  
  public boolean containsValue(String paramString) {
    for (NSObject nSObject : this.dict.values()) {
      if (nSObject.getClass().equals(NSString.class) && ((NSString)nSObject).getContent().equals(paramString))
        return true; 
    } 
    return false;
  }
  
  public boolean containsValue(Date paramDate) {
    for (NSObject nSObject : this.dict.values()) {
      if (nSObject.getClass().equals(NSDate.class) && ((NSDate)nSObject).getDate().equals(paramDate))
        return true; 
    } 
    return false;
  }
  
  public boolean containsValue(boolean paramBoolean) {
    for (NSObject nSObject : this.dict.values()) {
      if (nSObject.getClass().equals(NSNumber.class)) {
        nSObject = nSObject;
        if (nSObject.isBoolean() && nSObject.boolValue() == paramBoolean)
          return true; 
      } 
    } 
    return false;
  }
  
  public boolean containsValue(byte[] paramArrayOfbyte) {
    for (NSObject nSObject : this.dict.values()) {
      if (nSObject.getClass().equals(NSData.class) && Arrays.equals(((NSData)nSObject).bytes(), paramArrayOfbyte))
        return true; 
    } 
    return false;
  }
  
  public int count() {
    return this.dict.size();
  }
  
  public Set<Map.Entry<String, NSObject>> entrySet() {
    return this.dict.entrySet();
  }
  
  public boolean equals(Object paramObject) {
    boolean bool;
    if (paramObject.getClass().equals(getClass()) && ((NSDictionary)paramObject).dict.equals(this.dict)) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public NSObject get(Object paramObject) {
    return this.dict.get(paramObject);
  }
  
  public HashMap<String, NSObject> getHashMap() {
    return this.dict;
  }
  
  public int hashCode() {
    byte b;
    HashMap<String, NSObject> hashMap = this.dict;
    if (hashMap != null) {
      b = hashMap.hashCode();
    } else {
      b = 0;
    } 
    return 581 + b;
  }
  
  public boolean isEmpty() {
    return this.dict.isEmpty();
  }
  
  public Set<String> keySet() {
    return this.dict.keySet();
  }
  
  public NSObject objectForKey(String paramString) {
    return this.dict.get(paramString);
  }
  
  public NSObject put(String paramString, NSObject paramNSObject) {
    return (paramString == null) ? null : ((paramNSObject == null) ? this.dict.get(paramString) : this.dict.put(paramString, paramNSObject));
  }
  
  public NSObject put(String paramString, Object paramObject) {
    return put(paramString, NSObject.wrap(paramObject));
  }
  
  public void putAll(Map<? extends String, ? extends NSObject> paramMap) {
    for (Map.Entry<? extends String, ? extends NSObject> entry : paramMap.entrySet())
      put((String)entry.getKey(), (NSObject)entry.getValue()); 
  }
  
  public NSObject remove(Object paramObject) {
    return this.dict.remove(paramObject);
  }
  
  public NSObject remove(String paramString) {
    return this.dict.remove(paramString);
  }
  
  public int size() {
    return this.dict.size();
  }
  
  protected void toASCII(StringBuilder paramStringBuilder, int paramInt) {
    indent(paramStringBuilder, paramInt);
    paramStringBuilder.append('{');
    paramStringBuilder.append(NEWLINE);
    for (String str : allKeys()) {
      NSObject nSObject = objectForKey(str);
      indent(paramStringBuilder, paramInt + 1);
      paramStringBuilder.append("\"");
      paramStringBuilder.append(NSString.escapeStringForASCII(str));
      paramStringBuilder.append("\" =");
      Class<?> clazz = nSObject.getClass();
      if (clazz.equals(NSDictionary.class) || clazz.equals(NSArray.class) || clazz.equals(NSData.class)) {
        paramStringBuilder.append(NEWLINE);
        nSObject.toASCII(paramStringBuilder, paramInt + 2);
      } else {
        paramStringBuilder.append(" ");
        nSObject.toASCII(paramStringBuilder, 0);
      } 
      paramStringBuilder.append(';');
      paramStringBuilder.append(NEWLINE);
    } 
    indent(paramStringBuilder, paramInt);
    paramStringBuilder.append('}');
  }
  
  protected void toASCIIGnuStep(StringBuilder paramStringBuilder, int paramInt) {
    indent(paramStringBuilder, paramInt);
    paramStringBuilder.append('{');
    paramStringBuilder.append(NEWLINE);
    for (String str : (String[])this.dict.keySet().toArray((Object[])new String[this.dict.size()])) {
      NSObject nSObject = objectForKey(str);
      indent(paramStringBuilder, paramInt + 1);
      paramStringBuilder.append("\"");
      paramStringBuilder.append(NSString.escapeStringForASCII(str));
      paramStringBuilder.append("\" =");
      Class<?> clazz = nSObject.getClass();
      if (clazz.equals(NSDictionary.class) || clazz.equals(NSArray.class) || clazz.equals(NSData.class)) {
        paramStringBuilder.append(NEWLINE);
        nSObject.toASCIIGnuStep(paramStringBuilder, paramInt + 2);
      } else {
        paramStringBuilder.append(" ");
        nSObject.toASCIIGnuStep(paramStringBuilder, 0);
      } 
      paramStringBuilder.append(';');
      paramStringBuilder.append(NEWLINE);
    } 
    indent(paramStringBuilder, paramInt);
    paramStringBuilder.append('}');
  }
  
  public String toASCIIPropertyList() {
    StringBuilder stringBuilder = new StringBuilder();
    toASCII(stringBuilder, 0);
    stringBuilder.append(NEWLINE);
    return stringBuilder.toString();
  }
  
  void toBinary(BinaryPropertyListWriter paramBinaryPropertyListWriter) throws IOException {
    paramBinaryPropertyListWriter.writeIntHeader(13, this.dict.size());
    Set<Map.Entry<String, NSObject>> set = this.dict.entrySet();
    Iterator<Map.Entry<String, NSObject>> iterator = set.iterator();
    while (iterator.hasNext())
      paramBinaryPropertyListWriter.writeID(paramBinaryPropertyListWriter.getID(new NSString((String)((Map.Entry)iterator.next()).getKey()))); 
    iterator = set.iterator();
    while (iterator.hasNext())
      paramBinaryPropertyListWriter.writeID(paramBinaryPropertyListWriter.getID((NSObject)((Map.Entry)iterator.next()).getValue())); 
  }
  
  public String toGnuStepASCIIPropertyList() {
    StringBuilder stringBuilder = new StringBuilder();
    toASCIIGnuStep(stringBuilder, 0);
    stringBuilder.append(NEWLINE);
    return stringBuilder.toString();
  }
  
  void toXML(StringBuilder paramStringBuilder, int paramInt) {
    indent(paramStringBuilder, paramInt);
    paramStringBuilder.append("<dict>");
    paramStringBuilder.append(NSObject.NEWLINE);
    for (String str : this.dict.keySet()) {
      NSObject nSObject = objectForKey(str);
      int i = paramInt + 1;
      indent(paramStringBuilder, i);
      paramStringBuilder.append("<key>");
      if (str.contains("&") || str.contains("<") || str.contains(">")) {
        paramStringBuilder.append("<![CDATA[");
        paramStringBuilder.append(str.replaceAll("]]>", "]]]]><![CDATA[>"));
        paramStringBuilder.append("]]>");
      } else {
        paramStringBuilder.append(str);
      } 
      paramStringBuilder.append("</key>");
      paramStringBuilder.append(NSObject.NEWLINE);
      nSObject.toXML(paramStringBuilder, i);
      paramStringBuilder.append(NSObject.NEWLINE);
    } 
    indent(paramStringBuilder, paramInt);
    paramStringBuilder.append("</dict>");
  }
  
  public Collection<NSObject> values() {
    return this.dict.values();
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/dd/plist/NSDictionary.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */