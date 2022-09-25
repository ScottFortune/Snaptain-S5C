package com.dd.plist;

import java.io.IOException;

public class NSNumber extends NSObject implements Comparable<Object> {
  public static final int BOOLEAN = 2;
  
  public static final int INTEGER = 0;
  
  public static final int REAL = 1;
  
  private boolean boolValue;
  
  private double doubleValue;
  
  private long longValue;
  
  private int type;
  
  public NSNumber(double paramDouble) {
    this.doubleValue = paramDouble;
    this.longValue = (long)paramDouble;
    this.type = 1;
  }
  
  public NSNumber(int paramInt) {
    long l = paramInt;
    this.longValue = l;
    this.doubleValue = l;
    this.type = 0;
  }
  
  public NSNumber(long paramLong) {
    this.longValue = paramLong;
    this.doubleValue = paramLong;
    this.type = 0;
  }
  
  public NSNumber(String paramString) {
    if (paramString != null) {
      boolean bool = false;
      try {
        long l = Long.parseLong(paramString);
        this.longValue = l;
        this.doubleValue = l;
        this.type = 0;
      } catch (Exception exception) {
        try {
          this.doubleValue = Double.parseDouble(paramString);
          this.longValue = Math.round(this.doubleValue);
          this.type = 1;
        } catch (Exception exception1) {
          try {
            if (paramString.toLowerCase().equals("true") || paramString.toLowerCase().equals("yes"))
              bool = true; 
            this.boolValue = bool;
            if (this.boolValue || paramString.toLowerCase().equals("false") || paramString.toLowerCase().equals("no")) {
              long l;
              this.type = 2;
              if (this.boolValue) {
                l = 1L;
              } else {
                l = 0L;
              } 
              this.longValue = l;
              this.doubleValue = l;
              return;
            } 
            Exception exception2 = new Exception();
            this("not a boolean");
            throw exception2;
          } catch (Exception exception2) {
            throw new IllegalArgumentException("The given string neither represents a double, an int nor a boolean value.");
          } 
        } 
      } 
      return;
    } 
    throw new IllegalArgumentException("The given string is null and cannot be parsed as number.");
  }
  
  public NSNumber(boolean paramBoolean) {
    long l;
    this.boolValue = paramBoolean;
    if (paramBoolean) {
      l = 1L;
    } else {
      l = 0L;
    } 
    this.longValue = l;
    this.doubleValue = l;
    this.type = 2;
  }
  
  public NSNumber(byte[] paramArrayOfbyte, int paramInt) {
    if (paramInt != 0) {
      if (paramInt == 1) {
        this.doubleValue = BinaryPropertyListParser.parseDouble(paramArrayOfbyte);
        this.longValue = Math.round(this.doubleValue);
      } else {
        throw new IllegalArgumentException("Type argument is not valid.");
      } 
    } else {
      long l = BinaryPropertyListParser.parseLong(paramArrayOfbyte);
      this.longValue = l;
      this.doubleValue = l;
    } 
    this.type = paramInt;
  }
  
  public boolean boolValue() {
    boolean bool;
    if (this.type == 2)
      return this.boolValue; 
    if (this.longValue != 0L) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public int compareTo(Object paramObject) {
    double d = doubleValue();
    boolean bool = paramObject instanceof NSNumber;
    boolean bool1 = false;
    byte b = 0;
    if (bool) {
      double d1 = ((NSNumber)paramObject).doubleValue();
      if (d < d1) {
        b = -1;
      } else if (d != d1) {
        b = 1;
      } 
      return b;
    } 
    if (paramObject instanceof Number) {
      double d1 = ((Number)paramObject).doubleValue();
      if (d < d1) {
        b = -1;
      } else if (d == d1) {
        b = bool1;
      } else {
        b = 1;
      } 
      return b;
    } 
    return -1;
  }
  
  public double doubleValue() {
    return this.doubleValue;
  }
  
  public boolean equals(Object paramObject) {
    boolean bool = paramObject instanceof NSNumber;
    boolean bool1 = false;
    if (!bool)
      return false; 
    paramObject = paramObject;
    bool = bool1;
    if (this.type == ((NSNumber)paramObject).type) {
      bool = bool1;
      if (this.longValue == ((NSNumber)paramObject).longValue) {
        bool = bool1;
        if (this.doubleValue == ((NSNumber)paramObject).doubleValue) {
          bool = bool1;
          if (this.boolValue == ((NSNumber)paramObject).boolValue)
            bool = true; 
        } 
      } 
    } 
    return bool;
  }
  
  public float floatValue() {
    return (float)this.doubleValue;
  }
  
  public int hashCode() {
    int i = this.type;
    long l = this.longValue;
    return ((i * 37 + (int)(l ^ l >>> 32L)) * 37 + (int)(Double.doubleToLongBits(this.doubleValue) ^ Double.doubleToLongBits(this.doubleValue) >>> 32L)) * 37 + boolValue();
  }
  
  public int intValue() {
    return (int)this.longValue;
  }
  
  public boolean isBoolean() {
    boolean bool;
    if (this.type == 2) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isInteger() {
    boolean bool;
    if (this.type == 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isReal() {
    int i = this.type;
    boolean bool = true;
    if (i != 1)
      bool = false; 
    return bool;
  }
  
  public long longValue() {
    return this.longValue;
  }
  
  protected void toASCII(StringBuilder paramStringBuilder, int paramInt) {
    indent(paramStringBuilder, paramInt);
    if (this.type == 2) {
      String str;
      if (this.boolValue) {
        str = "YES";
      } else {
        str = "NO";
      } 
      paramStringBuilder.append(str);
    } else {
      paramStringBuilder.append(toString());
    } 
  }
  
  protected void toASCIIGnuStep(StringBuilder paramStringBuilder, int paramInt) {
    indent(paramStringBuilder, paramInt);
    paramInt = this.type;
    if (paramInt != 0) {
      if (paramInt != 1) {
        if (paramInt == 2)
          if (this.boolValue) {
            paramStringBuilder.append("<*BY>");
          } else {
            paramStringBuilder.append("<*BN>");
          }  
      } else {
        paramStringBuilder.append("<*R");
        paramStringBuilder.append(toString());
        paramStringBuilder.append(">");
      } 
    } else {
      paramStringBuilder.append("<*I");
      paramStringBuilder.append(toString());
      paramStringBuilder.append(">");
    } 
  }
  
  void toBinary(BinaryPropertyListWriter paramBinaryPropertyListWriter) throws IOException {
    int i = type();
    byte b = 8;
    if (i != 0) {
      if (i != 1) {
        if (i == 2) {
          if (boolValue())
            b = 9; 
          paramBinaryPropertyListWriter.write(b);
        } 
      } else {
        paramBinaryPropertyListWriter.write(35);
        paramBinaryPropertyListWriter.writeDouble(doubleValue());
      } 
    } else if (longValue() < 0L) {
      paramBinaryPropertyListWriter.write(19);
      paramBinaryPropertyListWriter.writeBytes(longValue(), 8);
    } else if (longValue() <= 255L) {
      paramBinaryPropertyListWriter.write(16);
      paramBinaryPropertyListWriter.writeBytes(longValue(), 1);
    } else if (longValue() <= 65535L) {
      paramBinaryPropertyListWriter.write(17);
      paramBinaryPropertyListWriter.writeBytes(longValue(), 2);
    } else if (longValue() <= 4294967295L) {
      paramBinaryPropertyListWriter.write(18);
      paramBinaryPropertyListWriter.writeBytes(longValue(), 4);
    } else {
      paramBinaryPropertyListWriter.write(19);
      paramBinaryPropertyListWriter.writeBytes(longValue(), 8);
    } 
  }
  
  public String toString() {
    int i = this.type;
    return (i != 0) ? ((i != 1) ? ((i != 2) ? super.toString() : String.valueOf(boolValue())) : String.valueOf(doubleValue())) : String.valueOf(longValue());
  }
  
  void toXML(StringBuilder paramStringBuilder, int paramInt) {
    indent(paramStringBuilder, paramInt);
    paramInt = this.type;
    if (paramInt != 0) {
      if (paramInt != 1) {
        if (paramInt == 2)
          if (boolValue()) {
            paramStringBuilder.append("<true/>");
          } else {
            paramStringBuilder.append("<false/>");
          }  
      } else {
        paramStringBuilder.append("<real>");
        paramStringBuilder.append(doubleValue());
        paramStringBuilder.append("</real>");
      } 
    } else {
      paramStringBuilder.append("<integer>");
      paramStringBuilder.append(longValue());
      paramStringBuilder.append("</integer>");
    } 
  }
  
  public int type() {
    return this.type;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/dd/plist/NSNumber.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */