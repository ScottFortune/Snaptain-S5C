package com.dd.plist;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

public class BinaryPropertyListParser {
  private byte[] bytes;
  
  private int majorVersion;
  
  private int minorVersion;
  
  private int objectRefSize;
  
  private int[] offsetTable;
  
  private int calculateUtf8StringLength(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
    byte b = 0;
    int i = 0;
    while (b < paramInt2) {
      int j = paramInt1 + i;
      if (paramArrayOfbyte.length <= j)
        return paramInt2; 
      int k = i;
      if (paramArrayOfbyte[j] < 128)
        k = i + 1; 
      if (paramArrayOfbyte[j] < 194)
        return paramInt2; 
      if (paramArrayOfbyte[j] < 224) {
        if ((paramArrayOfbyte[j + 1] & 0xC0) != 128)
          return paramInt2; 
        i = k + 2;
      } else if (paramArrayOfbyte[j] < 240) {
        if ((paramArrayOfbyte[j + 1] & 0xC0) != 128 || (paramArrayOfbyte[j + 2] & 0xC0) != 128)
          return paramInt2; 
        i = k + 3;
      } else {
        i = k;
        if (paramArrayOfbyte[j] < 245) {
          if ((paramArrayOfbyte[j + 1] & 0xC0) != 128 || (paramArrayOfbyte[j + 2] & 0xC0) != 128 || (paramArrayOfbyte[j + 3] & 0xC0) != 128)
            return paramInt2; 
          i = k + 4;
        } 
      } 
      b++;
    } 
    return i;
  }
  
  public static byte[] copyOfRange(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
    int i = paramInt2 - paramInt1;
    if (i >= 0) {
      byte[] arrayOfByte = new byte[i];
      System.arraycopy(paramArrayOfbyte, paramInt1, arrayOfByte, 0, i);
      return arrayOfByte;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("startIndex (");
    stringBuilder.append(paramInt1);
    stringBuilder.append(")");
    stringBuilder.append(" > endIndex (");
    stringBuilder.append(paramInt2);
    stringBuilder.append(")");
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  private NSObject doParse(byte[] paramArrayOfbyte) throws PropertyListFormatException, UnsupportedEncodingException {
    StringBuilder stringBuilder1;
    this.bytes = paramArrayOfbyte;
    paramArrayOfbyte = this.bytes;
    int i = 0;
    String str = new String(copyOfRange(paramArrayOfbyte, 0, 8));
    if (str.startsWith("bplist")) {
      this.majorVersion = str.charAt(6) - 48;
      this.minorVersion = str.charAt(7) - 48;
      if (this.majorVersion <= 0) {
        byte[] arrayOfByte = this.bytes;
        arrayOfByte = copyOfRange(arrayOfByte, arrayOfByte.length - 32, arrayOfByte.length);
        int j = (int)parseUnsignedInt(arrayOfByte, 6, 7);
        this.objectRefSize = (int)parseUnsignedInt(arrayOfByte, 7, 8);
        int k = (int)parseUnsignedInt(arrayOfByte, 8, 16);
        int m = (int)parseUnsignedInt(arrayOfByte, 16, 24);
        int n = (int)parseUnsignedInt(arrayOfByte, 24, 32);
        this.offsetTable = new int[k];
        while (i < k) {
          arrayOfByte = this.bytes;
          int i1 = i + 1;
          arrayOfByte = copyOfRange(arrayOfByte, i * j + n, i1 * j + n);
          this.offsetTable[i] = (int)parseUnsignedInt(arrayOfByte);
          i = i1;
        } 
        return parseObject(m);
      } 
      stringBuilder1 = new StringBuilder();
      stringBuilder1.append("Unsupported binary property list format: v");
      stringBuilder1.append(this.majorVersion);
      stringBuilder1.append(".");
      stringBuilder1.append(this.minorVersion);
      stringBuilder1.append(". ");
      stringBuilder1.append("Version 1.0 and later are not yet supported.");
      throw new IllegalArgumentException(stringBuilder1.toString());
    } 
    StringBuilder stringBuilder2 = new StringBuilder();
    stringBuilder2.append("The given data is no binary property list. Wrong magic bytes: ");
    stringBuilder2.append((String)stringBuilder1);
    IllegalArgumentException illegalArgumentException = new IllegalArgumentException(stringBuilder2.toString());
    throw illegalArgumentException;
  }
  
  public static NSObject parse(File paramFile) throws IOException, PropertyListFormatException {
    return parse(new FileInputStream(paramFile));
  }
  
  public static NSObject parse(InputStream paramInputStream) throws IOException, PropertyListFormatException {
    return parse(PropertyListParser.readAll(paramInputStream));
  }
  
  public static NSObject parse(byte[] paramArrayOfbyte) throws PropertyListFormatException, UnsupportedEncodingException {
    return (new BinaryPropertyListParser()).doParse(paramArrayOfbyte);
  }
  
  public static double parseDouble(byte[] paramArrayOfbyte) {
    if (paramArrayOfbyte.length == 8)
      return Double.longBitsToDouble(parseLong(paramArrayOfbyte)); 
    if (paramArrayOfbyte.length == 4)
      return Float.intBitsToFloat((int)parseLong(paramArrayOfbyte)); 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("bad byte array length ");
    stringBuilder.append(paramArrayOfbyte.length);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public static double parseDouble(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
    int i = paramInt2 - paramInt1;
    if (i == 8)
      return Double.longBitsToDouble(parseLong(paramArrayOfbyte, paramInt1, paramInt2)); 
    if (i == 4)
      return Float.intBitsToFloat((int)parseLong(paramArrayOfbyte, paramInt1, paramInt2)); 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("endIndex (");
    stringBuilder.append(paramInt2);
    stringBuilder.append(") - startIndex (");
    stringBuilder.append(paramInt1);
    stringBuilder.append(") != 4 or 8");
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public static long parseLong(byte[] paramArrayOfbyte) {
    int i = paramArrayOfbyte.length;
    long l = 0L;
    for (byte b = 0; b < i; b++)
      l = l << 8L | (paramArrayOfbyte[b] & 0xFF); 
    return l;
  }
  
  public static long parseLong(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
    long l = 0L;
    while (paramInt1 < paramInt2) {
      l = l << 8L | (paramArrayOfbyte[paramInt1] & 0xFF);
      paramInt1++;
    } 
    return l;
  }
  
  private NSObject parseObject(int paramInt) throws PropertyListFormatException, UnsupportedEncodingException {
    PrintStream printStream;
    int[] arrayOfInt6;
    NSDictionary nSDictionary;
    int[] arrayOfInt5;
    NSArray nSArray;
    byte[] arrayOfByte6;
    int[] arrayOfInt4;
    byte[] arrayOfByte5;
    int[] arrayOfInt3;
    byte[] arrayOfByte4;
    int[] arrayOfInt2;
    byte[] arrayOfByte3;
    int[] arrayOfInt1;
    byte[] arrayOfByte2;
    StringBuilder stringBuilder1;
    byte[] arrayOfByte1;
    int j;
    StringBuilder stringBuilder2;
    NSSet nSSet;
    int i = this.offsetTable[paramInt];
    byte[] arrayOfByte7 = this.bytes;
    byte b = arrayOfByte7[i];
    int k = (b & 0xF0) >> 4;
    int m = b & 0xF;
    int n = 0;
    int i1 = 0;
    int i2 = 0;
    b = 0;
    switch (k) {
      default:
        printStream = System.err;
        stringBuilder2 = new StringBuilder();
        stringBuilder2.append("WARNING: The given binary property list contains an object of unknown type (");
        stringBuilder2.append(k);
        stringBuilder2.append(")");
        printStream.println(stringBuilder2.toString());
        return null;
      case 13:
        arrayOfInt6 = readLengthAndOffset(m, i);
        i1 = arrayOfInt6[0];
        n = arrayOfInt6[1];
        nSDictionary = new NSDictionary();
        for (paramInt = b; paramInt < i1; paramInt = i3) {
          byte[] arrayOfByte = this.bytes;
          i2 = i + n;
          m = this.objectRefSize;
          int i3 = paramInt + 1;
          m = (int)parseUnsignedInt(copyOfRange(arrayOfByte, paramInt * m + i2, m * i3 + i2));
          arrayOfByte = this.bytes;
          k = this.objectRefSize;
          paramInt = (int)parseUnsignedInt(copyOfRange(arrayOfByte, i1 * k + i2 + paramInt * k, i2 + i1 * k + k * i3));
          NSObject nSObject1 = parseObject(m);
          NSObject nSObject2 = parseObject(paramInt);
          nSDictionary.put(nSObject1.toString(), nSObject2);
        } 
        return nSDictionary;
      case 12:
        arrayOfInt5 = readLengthAndOffset(m, i);
        i1 = arrayOfInt5[0];
        i2 = arrayOfInt5[1];
        nSSet = new NSSet();
        for (paramInt = n; paramInt < i1; paramInt = i3) {
          byte[] arrayOfByte = this.bytes;
          n = i + i2;
          m = this.objectRefSize;
          int i3 = paramInt + 1;
          nSSet.addObject(parseObject((int)parseUnsignedInt(copyOfRange(arrayOfByte, paramInt * m + n, n + m * i3))));
        } 
        return nSSet;
      case 11:
        arrayOfInt5 = readLengthAndOffset(m, i);
        i2 = arrayOfInt5[0];
        n = arrayOfInt5[1];
        nSSet = new NSSet(true);
        for (paramInt = i1; paramInt < i2; paramInt = i3) {
          byte[] arrayOfByte = this.bytes;
          m = i + n;
          i1 = this.objectRefSize;
          int i3 = paramInt + 1;
          nSSet.addObject(parseObject((int)parseUnsignedInt(copyOfRange(arrayOfByte, paramInt * i1 + m, m + i1 * i3))));
        } 
        return nSSet;
      case 10:
        arrayOfInt5 = readLengthAndOffset(m, i);
        i1 = arrayOfInt5[0];
        n = arrayOfInt5[1];
        nSArray = new NSArray(i1);
        paramInt = i2;
        while (true) {
          if (paramInt < i1) {
            byte[] arrayOfByte = this.bytes;
            i2 = i + n;
            m = this.objectRefSize;
            int i3 = paramInt + 1;
            i2 = (int)parseUnsignedInt(copyOfRange(arrayOfByte, paramInt * m + i2, i2 + m * i3));
            try {
              NSObject nSObject = parseObject(i2);
              nSArray.setValue(paramInt, nSObject);
              paramInt = i3;
            } finally {}
            continue;
          } 
          return nSArray;
        } 
      case 8:
        arrayOfByte6 = this.bytes;
        j = i + 1;
        return new UID(String.valueOf(paramInt), copyOfRange(arrayOfByte6, j, m + 1 + j));
      case 7:
        arrayOfInt4 = readLengthAndOffset(m, i);
        j = arrayOfInt4[1];
        paramInt = arrayOfInt4[0];
        arrayOfByte5 = this.bytes;
        j = i + j;
        paramInt = calculateUtf8StringLength(arrayOfByte5, j, paramInt);
        return new NSString(copyOfRange(this.bytes, j, paramInt + j), "UTF-8");
      case 6:
        arrayOfInt3 = readLengthAndOffset(m, i);
        paramInt = arrayOfInt3[0];
        j = arrayOfInt3[1];
        arrayOfByte4 = this.bytes;
        j = i + j;
        return new NSString(copyOfRange(arrayOfByte4, j, paramInt * 2 + j), "UTF-16BE");
      case 5:
        arrayOfInt2 = readLengthAndOffset(m, i);
        paramInt = arrayOfInt2[0];
        j = arrayOfInt2[1];
        arrayOfByte3 = this.bytes;
        j = i + j;
        return new NSString(copyOfRange(arrayOfByte3, j, paramInt + j), "ASCII");
      case 4:
        arrayOfInt1 = readLengthAndOffset(m, i);
        paramInt = arrayOfInt1[0];
        j = arrayOfInt1[1];
        arrayOfByte2 = this.bytes;
        j = i + j;
        return new NSData(copyOfRange(arrayOfByte2, j, paramInt + j));
      case 3:
        if (m == 3)
          return new NSDate(copyOfRange(arrayOfByte2, i + 1, i + 9)); 
        stringBuilder1 = new StringBuilder();
        stringBuilder1.append("The given binary property list contains a date object of an unknown type (");
        stringBuilder1.append(m);
        stringBuilder1.append(")");
        throw new PropertyListFormatException(stringBuilder1.toString());
      case 2:
        paramInt = (int)Math.pow(2.0D, m);
        arrayOfByte1 = this.bytes;
        j = i + 1;
        return new NSNumber(copyOfRange(arrayOfByte1, j, paramInt + j), 1);
      case 1:
        paramInt = (int)Math.pow(2.0D, m);
        arrayOfByte1 = this.bytes;
        j = i + 1;
        return new NSNumber(copyOfRange(arrayOfByte1, j, paramInt + j), 0);
      case 0:
        break;
    } 
    if (m != 0)
      if (m != 8) {
        if (m != 9) {
          switch (m) {
            case 14:
            case 15:
              return null;
          } 
        } else {
          return new NSNumber(true);
        } 
      } else {
        return new NSNumber(false);
      }  
    return null;
  }
  
  public static long parseUnsignedInt(byte[] paramArrayOfbyte) {
    int i = paramArrayOfbyte.length;
    long l = 0L;
    for (byte b = 0; b < i; b++)
      l = l << 8L | (paramArrayOfbyte[b] & 0xFF); 
    return l & 0xFFFFFFFFL;
  }
  
  public static long parseUnsignedInt(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
    long l = 0L;
    while (paramInt1 < paramInt2) {
      l = l << 8L | (paramArrayOfbyte[paramInt1] & 0xFF);
      paramInt1++;
    } 
    return 0xFFFFFFFFL & l;
  }
  
  private int[] readLengthAndOffset(int paramInt1, int paramInt2) {
    if (paramInt1 == 15) {
      paramInt1 = this.bytes[paramInt2 + 1];
      int i = (paramInt1 & 0xF0) >> 4;
      if (i != 1) {
        PrintStream printStream = System.err;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("BinaryPropertyListParser: Length integer has an unexpected type");
        stringBuilder.append(i);
        stringBuilder.append(". Attempting to parse anyway...");
        printStream.println(stringBuilder.toString());
      } 
      i = (int)Math.pow(2.0D, (paramInt1 & 0xF));
      paramInt1 = i + 2;
      if (i < 3) {
        byte[] arrayOfByte = this.bytes;
        paramInt2 += 2;
        i = (int)parseUnsignedInt(copyOfRange(arrayOfByte, paramInt2, i + paramInt2));
        paramInt2 = paramInt1;
        paramInt1 = i;
      } else {
        byte[] arrayOfByte = this.bytes;
        paramInt2 += 2;
        i = (new BigInteger(copyOfRange(arrayOfByte, paramInt2, i + paramInt2))).intValue();
        paramInt2 = paramInt1;
        paramInt1 = i;
      } 
    } else {
      paramInt2 = 1;
    } 
    return new int[] { paramInt1, paramInt2 };
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/dd/plist/BinaryPropertyListParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */