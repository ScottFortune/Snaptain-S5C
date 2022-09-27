package com.google.gson;

import com.google.gson.internal.;
import com.google.gson.internal.LazilyParsedNumber;
import java.math.BigDecimal;
import java.math.BigInteger;

public final class JsonPrimitive extends JsonElement {
  private final Object value;
  
  public JsonPrimitive(Boolean paramBoolean) {
    this.value = .Gson.Preconditions.checkNotNull(paramBoolean);
  }
  
  public JsonPrimitive(Character paramCharacter) {
    this.value = ((Character).Gson.Preconditions.checkNotNull(paramCharacter)).toString();
  }
  
  public JsonPrimitive(Number paramNumber) {
    this.value = .Gson.Preconditions.checkNotNull(paramNumber);
  }
  
  public JsonPrimitive(String paramString) {
    this.value = .Gson.Preconditions.checkNotNull(paramString);
  }
  
  private static boolean isIntegral(JsonPrimitive paramJsonPrimitive) {
    Object object = paramJsonPrimitive.value;
    boolean bool = object instanceof Number;
    boolean bool1 = false;
    null = bool1;
    if (bool) {
      object = object;
      if (!(object instanceof BigInteger) && !(object instanceof Long) && !(object instanceof Integer) && !(object instanceof Short)) {
        null = bool1;
        return (object instanceof Byte) ? true : null;
      } 
    } else {
      return null;
    } 
    return true;
  }
  
  public JsonPrimitive deepCopy() {
    return this;
  }
  
  public boolean equals(Object paramObject) {
    boolean bool1 = true;
    boolean bool2 = true;
    boolean bool3 = true;
    if (this == paramObject)
      return true; 
    if (paramObject == null || getClass() != paramObject.getClass())
      return false; 
    paramObject = paramObject;
    if (this.value == null) {
      if (((JsonPrimitive)paramObject).value != null)
        bool3 = false; 
      return bool3;
    } 
    if (isIntegral(this) && isIntegral((JsonPrimitive)paramObject)) {
      if (getAsNumber().longValue() == paramObject.getAsNumber().longValue()) {
        bool3 = bool1;
      } else {
        bool3 = false;
      } 
      return bool3;
    } 
    if (this.value instanceof Number && ((JsonPrimitive)paramObject).value instanceof Number) {
      double d1 = getAsNumber().doubleValue();
      double d2 = paramObject.getAsNumber().doubleValue();
      bool3 = bool2;
      if (d1 != d2)
        if (Double.isNaN(d1) && Double.isNaN(d2)) {
          bool3 = bool2;
        } else {
          bool3 = false;
        }  
      return bool3;
    } 
    return this.value.equals(((JsonPrimitive)paramObject).value);
  }
  
  public BigDecimal getAsBigDecimal() {
    Object object = this.value;
    if (object instanceof BigDecimal) {
      object = object;
    } else {
      object = new BigDecimal(object.toString());
    } 
    return (BigDecimal)object;
  }
  
  public BigInteger getAsBigInteger() {
    Object object = this.value;
    if (object instanceof BigInteger) {
      object = object;
    } else {
      object = new BigInteger(object.toString());
    } 
    return (BigInteger)object;
  }
  
  public boolean getAsBoolean() {
    return isBoolean() ? ((Boolean)this.value).booleanValue() : Boolean.parseBoolean(getAsString());
  }
  
  public byte getAsByte() {
    byte b;
    if (isNumber()) {
      byte b1 = getAsNumber().byteValue();
      b = b1;
    } else {
      byte b1 = Byte.parseByte(getAsString());
      b = b1;
    } 
    return b;
  }
  
  public char getAsCharacter() {
    return getAsString().charAt(0);
  }
  
  public double getAsDouble() {
    double d;
    if (isNumber()) {
      d = getAsNumber().doubleValue();
    } else {
      d = Double.parseDouble(getAsString());
    } 
    return d;
  }
  
  public float getAsFloat() {
    float f;
    if (isNumber()) {
      f = getAsNumber().floatValue();
    } else {
      f = Float.parseFloat(getAsString());
    } 
    return f;
  }
  
  public int getAsInt() {
    int i;
    if (isNumber()) {
      i = getAsNumber().intValue();
    } else {
      i = Integer.parseInt(getAsString());
    } 
    return i;
  }
  
  public long getAsLong() {
    long l;
    if (isNumber()) {
      l = getAsNumber().longValue();
    } else {
      l = Long.parseLong(getAsString());
    } 
    return l;
  }
  
  public Number getAsNumber() {
    Object object = this.value;
    if (object instanceof String) {
      object = new LazilyParsedNumber((String)object);
    } else {
      object = object;
    } 
    return (Number)object;
  }
  
  public short getAsShort() {
    short s;
    if (isNumber()) {
      short s1 = getAsNumber().shortValue();
      s = s1;
    } else {
      short s1 = Short.parseShort(getAsString());
      s = s1;
    } 
    return s;
  }
  
  public String getAsString() {
    return isNumber() ? getAsNumber().toString() : (isBoolean() ? ((Boolean)this.value).toString() : (String)this.value);
  }
  
  public int hashCode() {
    if (this.value == null)
      return 31; 
    if (isIntegral(this)) {
      long l = getAsNumber().longValue();
      return (int)(l >>> 32L ^ l);
    } 
    Object object = this.value;
    if (object instanceof Number) {
      long l = Double.doubleToLongBits(getAsNumber().doubleValue());
      return (int)(l >>> 32L ^ l);
    } 
    return object.hashCode();
  }
  
  public boolean isBoolean() {
    return this.value instanceof Boolean;
  }
  
  public boolean isNumber() {
    return this.value instanceof Number;
  }
  
  public boolean isString() {
    return this.value instanceof String;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/google/gson/JsonPrimitive.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */