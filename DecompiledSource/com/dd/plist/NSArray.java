package com.dd.plist;

import java.io.IOException;
import java.util.Arrays;

public class NSArray extends NSObject {
  private NSObject[] array;
  
  public NSArray(int paramInt) {
    this.array = new NSObject[paramInt];
  }
  
  public NSArray(NSObject... paramVarArgs) {
    this.array = paramVarArgs;
  }
  
  void assignIDs(BinaryPropertyListWriter paramBinaryPropertyListWriter) {
    super.assignIDs(paramBinaryPropertyListWriter);
    NSObject[] arrayOfNSObject = this.array;
    int i = arrayOfNSObject.length;
    for (byte b = 0; b < i; b++)
      arrayOfNSObject[b].assignIDs(paramBinaryPropertyListWriter); 
  }
  
  public boolean containsObject(Object paramObject) {
    NSObject nSObject = NSObject.wrap(paramObject);
    for (NSObject nSObject1 : this.array) {
      if (nSObject1 == null) {
        if (paramObject == null)
          return true; 
      } else if (nSObject1.equals(nSObject)) {
        return true;
      } 
    } 
    return false;
  }
  
  public int count() {
    return this.array.length;
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == null)
      return false; 
    if (paramObject.getClass().equals(NSArray.class))
      return Arrays.equals((Object[])((NSArray)paramObject).getArray(), (Object[])this.array); 
    paramObject = NSObject.wrap(paramObject);
    return paramObject.getClass().equals(NSArray.class) ? Arrays.equals((Object[])((NSArray)paramObject).getArray(), (Object[])this.array) : false;
  }
  
  public NSObject[] getArray() {
    return this.array;
  }
  
  public int hashCode() {
    return 623 + Arrays.deepHashCode((Object[])this.array);
  }
  
  public int indexOfIdenticalObject(Object paramObject) {
    paramObject = NSObject.wrap(paramObject);
    byte b = 0;
    while (true) {
      NSObject[] arrayOfNSObject = this.array;
      if (b < arrayOfNSObject.length) {
        if (arrayOfNSObject[b] == paramObject)
          return b; 
        b++;
        continue;
      } 
      return -1;
    } 
  }
  
  public int indexOfObject(Object paramObject) {
    NSObject nSObject = NSObject.wrap(paramObject);
    byte b = 0;
    while (true) {
      paramObject = this.array;
      if (b < paramObject.length) {
        if (paramObject[b].equals(nSObject))
          return b; 
        b++;
        continue;
      } 
      return -1;
    } 
  }
  
  public NSObject lastObject() {
    NSObject[] arrayOfNSObject = this.array;
    return arrayOfNSObject[arrayOfNSObject.length - 1];
  }
  
  public NSObject objectAtIndex(int paramInt) {
    return this.array[paramInt];
  }
  
  public NSObject[] objectsAtIndexes(int... paramVarArgs) {
    NSObject[] arrayOfNSObject = new NSObject[paramVarArgs.length];
    Arrays.sort(paramVarArgs);
    for (byte b = 0; b < paramVarArgs.length; b++)
      arrayOfNSObject[b] = this.array[paramVarArgs[b]]; 
    return arrayOfNSObject;
  }
  
  public void remove(int paramInt) {
    NSObject[] arrayOfNSObject = this.array;
    if (paramInt < arrayOfNSObject.length && paramInt >= 0) {
      NSObject[] arrayOfNSObject1 = new NSObject[arrayOfNSObject.length - 1];
      System.arraycopy(arrayOfNSObject, 0, arrayOfNSObject1, 0, paramInt);
      arrayOfNSObject = this.array;
      System.arraycopy(arrayOfNSObject, paramInt + 1, arrayOfNSObject1, paramInt, arrayOfNSObject.length - paramInt - 1);
      this.array = arrayOfNSObject1;
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("invalid index:");
    stringBuilder.append(paramInt);
    stringBuilder.append(";the array length is ");
    stringBuilder.append(this.array.length);
    throw new ArrayIndexOutOfBoundsException(stringBuilder.toString());
  }
  
  public void setValue(int paramInt, Object paramObject) {
    this.array[paramInt] = NSObject.wrap(paramObject);
  }
  
  protected void toASCII(StringBuilder paramStringBuilder, int paramInt) {
    indent(paramStringBuilder, paramInt);
    paramStringBuilder.append('(');
    int i = paramStringBuilder.lastIndexOf(NEWLINE);
    byte b = 0;
    while (true) {
      NSObject[] arrayOfNSObject = this.array;
      if (b < arrayOfNSObject.length) {
        int j;
        Class<?> clazz = arrayOfNSObject[b].getClass();
        if ((clazz.equals(NSDictionary.class) || clazz.equals(NSArray.class) || clazz.equals(NSData.class)) && i != paramStringBuilder.length()) {
          paramStringBuilder.append(NEWLINE);
          j = paramStringBuilder.length();
          this.array[b].toASCII(paramStringBuilder, paramInt + 1);
        } else {
          if (b != 0)
            paramStringBuilder.append(" "); 
          this.array[b].toASCII(paramStringBuilder, 0);
          j = i;
        } 
        if (b != this.array.length - 1)
          paramStringBuilder.append(','); 
        i = j;
        if (paramStringBuilder.length() - j > 80) {
          paramStringBuilder.append(NEWLINE);
          i = paramStringBuilder.length();
        } 
        b++;
        continue;
      } 
      paramStringBuilder.append(')');
      return;
    } 
  }
  
  protected void toASCIIGnuStep(StringBuilder paramStringBuilder, int paramInt) {
    indent(paramStringBuilder, paramInt);
    paramStringBuilder.append('(');
    int i = paramStringBuilder.lastIndexOf(NEWLINE);
    byte b = 0;
    while (true) {
      NSObject[] arrayOfNSObject = this.array;
      if (b < arrayOfNSObject.length) {
        int j;
        Class<?> clazz = arrayOfNSObject[b].getClass();
        if ((clazz.equals(NSDictionary.class) || clazz.equals(NSArray.class) || clazz.equals(NSData.class)) && i != paramStringBuilder.length()) {
          paramStringBuilder.append(NEWLINE);
          j = paramStringBuilder.length();
          this.array[b].toASCIIGnuStep(paramStringBuilder, paramInt + 1);
        } else {
          if (b != 0)
            paramStringBuilder.append(" "); 
          this.array[b].toASCIIGnuStep(paramStringBuilder, 0);
          j = i;
        } 
        if (b != this.array.length - 1)
          paramStringBuilder.append(','); 
        i = j;
        if (paramStringBuilder.length() - j > 80) {
          paramStringBuilder.append(NEWLINE);
          i = paramStringBuilder.length();
        } 
        b++;
        continue;
      } 
      paramStringBuilder.append(')');
      return;
    } 
  }
  
  public String toASCIIPropertyList() {
    StringBuilder stringBuilder = new StringBuilder();
    toASCII(stringBuilder, 0);
    stringBuilder.append(NEWLINE);
    return stringBuilder.toString();
  }
  
  void toBinary(BinaryPropertyListWriter paramBinaryPropertyListWriter) throws IOException {
    paramBinaryPropertyListWriter.writeIntHeader(10, this.array.length);
    NSObject[] arrayOfNSObject = this.array;
    int i = arrayOfNSObject.length;
    for (byte b = 0; b < i; b++)
      paramBinaryPropertyListWriter.writeID(paramBinaryPropertyListWriter.getID(arrayOfNSObject[b])); 
  }
  
  public String toGnuStepASCIIPropertyList() {
    StringBuilder stringBuilder = new StringBuilder();
    toASCIIGnuStep(stringBuilder, 0);
    stringBuilder.append(NEWLINE);
    return stringBuilder.toString();
  }
  
  void toXML(StringBuilder paramStringBuilder, int paramInt) {
    indent(paramStringBuilder, paramInt);
    paramStringBuilder.append("<array>");
    paramStringBuilder.append(NSObject.NEWLINE);
    NSObject[] arrayOfNSObject = this.array;
    int i = arrayOfNSObject.length;
    for (byte b = 0; b < i; b++) {
      arrayOfNSObject[b].toXML(paramStringBuilder, paramInt + 1);
      paramStringBuilder.append(NSObject.NEWLINE);
    } 
    indent(paramStringBuilder, paramInt);
    paramStringBuilder.append("</array>");
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/dd/plist/NSArray.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */