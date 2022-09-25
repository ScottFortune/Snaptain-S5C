package com.dd.plist;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class BinaryPropertyListWriter {
  public static final int VERSION_00 = 0;
  
  public static final int VERSION_10 = 10;
  
  public static final int VERSION_15 = 15;
  
  public static final int VERSION_20 = 20;
  
  private long count;
  
  private Map<NSObject, Integer> idMap = new HashMap<NSObject, Integer>();
  
  private int idSizeInBytes;
  
  private OutputStream out;
  
  private int version = 0;
  
  BinaryPropertyListWriter(OutputStream paramOutputStream) throws IOException {
    this.out = new BufferedOutputStream(paramOutputStream);
  }
  
  BinaryPropertyListWriter(OutputStream paramOutputStream, int paramInt) throws IOException {
    this.version = paramInt;
    this.out = new BufferedOutputStream(paramOutputStream);
  }
  
  private static int computeIdSizeInBytes(int paramInt) {
    return (paramInt < 256) ? 1 : ((paramInt < 65536) ? 2 : 4);
  }
  
  private int computeOffsetSizeInBytes(long paramLong) {
    return (paramLong < 256L) ? 1 : ((paramLong < 65536L) ? 2 : ((paramLong < 4294967296L) ? 4 : 8));
  }
  
  private static int getMinimumRequiredVersion(NSObject paramNSObject) {
    Iterator<NSObject> iterator;
    int m;
    int i = 10;
    int j = 0;
    int k = 0;
    if (paramNSObject == null) {
      m = 10;
    } else {
      m = 0;
    } 
    if (paramNSObject instanceof NSDictionary) {
      iterator = ((NSDictionary)paramNSObject).getHashMap().values().iterator();
      j = m;
      while (true) {
        m = j;
        if (iterator.hasNext()) {
          m = getMinimumRequiredVersion(iterator.next());
          if (m > j)
            j = m; 
          continue;
        } 
        break;
      } 
    } else {
      NSObject[] arrayOfNSObject;
      if (iterator instanceof NSArray) {
        arrayOfNSObject = ((NSArray)iterator).getArray();
        int n = arrayOfNSObject.length;
        j = m;
        i = k;
        while (true) {
          m = j;
          if (i < n) {
            k = getMinimumRequiredVersion(arrayOfNSObject[i]);
            m = j;
            if (k > j)
              m = k; 
            i++;
            j = m;
            continue;
          } 
          break;
        } 
      } else if (arrayOfNSObject instanceof NSSet) {
        arrayOfNSObject = ((NSSet)arrayOfNSObject).allObjects();
        int n = arrayOfNSObject.length;
        for (m = i; j < n; m = i) {
          k = getMinimumRequiredVersion(arrayOfNSObject[j]);
          i = m;
          if (k > m)
            i = k; 
          j++;
        } 
      } 
    } 
    return m;
  }
  
  public static void write(File paramFile, NSObject paramNSObject) throws IOException {
    FileOutputStream fileOutputStream = new FileOutputStream(paramFile);
    write(fileOutputStream, paramNSObject);
    fileOutputStream.close();
  }
  
  public static void write(OutputStream paramOutputStream, NSObject paramNSObject) throws IOException {
    String str;
    StringBuilder stringBuilder;
    int i = getMinimumRequiredVersion(paramNSObject);
    if (i > 0) {
      if (i != 10) {
        if (i != 15) {
          if (i == 20) {
            str = "v2.0";
          } else {
            str = "v0.0";
          } 
        } else {
          str = "v1.5";
        } 
      } else {
        str = "v1.0";
      } 
      stringBuilder = new StringBuilder();
      stringBuilder.append("The given property list structure cannot be saved. The required version of the binary format (");
      stringBuilder.append(str);
      stringBuilder.append(") is not yet supported.");
      throw new IOException(stringBuilder.toString());
    } 
    (new BinaryPropertyListWriter((OutputStream)str, i)).write((NSObject)stringBuilder);
  }
  
  public static byte[] writeToArray(NSObject paramNSObject) throws IOException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    write(byteArrayOutputStream, paramNSObject);
    return byteArrayOutputStream.toByteArray();
  }
  
  void assignID(NSObject paramNSObject) {
    if (!this.idMap.containsKey(paramNSObject)) {
      Map<NSObject, Integer> map = this.idMap;
      map.put(paramNSObject, Integer.valueOf(map.size()));
    } 
  }
  
  int getID(NSObject paramNSObject) {
    return ((Integer)this.idMap.get(paramNSObject)).intValue();
  }
  
  void write(int paramInt) throws IOException {
    this.out.write(paramInt);
    this.count++;
  }
  
  void write(NSObject paramNSObject) throws IOException {
    write(new byte[] { 98, 112, 108, 105, 115, 116 });
    int i = this.version;
    if (i != 0) {
      if (i != 10) {
        if (i != 15) {
          if (i == 20)
            write(new byte[] { 50, 48 }); 
        } else {
          write(new byte[] { 49, 53 });
        } 
      } else {
        write(new byte[] { 49, 48 });
      } 
    } else {
      write(new byte[] { 48, 48 });
    } 
    paramNSObject.assignIDs(this);
    this.idSizeInBytes = computeIdSizeInBytes(this.idMap.size());
    long[] arrayOfLong = new long[this.idMap.size()];
    Iterator<Map.Entry> iterator = this.idMap.entrySet().iterator();
    while (true) {
      boolean bool = iterator.hasNext();
      i = 0;
      if (bool) {
        Map.Entry entry = iterator.next();
        NSObject nSObject = (NSObject)entry.getKey();
        arrayOfLong[((Integer)entry.getValue()).intValue()] = this.count;
        if (nSObject == null) {
          write(0);
          continue;
        } 
        nSObject.toBinary(this);
        continue;
      } 
      long l = this.count;
      int j = computeOffsetSizeInBytes(l);
      int k = arrayOfLong.length;
      while (i < k) {
        writeBytes(arrayOfLong[i], j);
        i++;
      } 
      if (this.version != 15) {
        write(new byte[6]);
        write(j);
        write(this.idSizeInBytes);
        writeLong(this.idMap.size());
        writeLong(((Integer)this.idMap.get(paramNSObject)).intValue());
        writeLong(l);
      } 
      this.out.flush();
      return;
    } 
  }
  
  void write(byte[] paramArrayOfbyte) throws IOException {
    this.out.write(paramArrayOfbyte);
    this.count += paramArrayOfbyte.length;
  }
  
  void writeBytes(long paramLong, int paramInt) throws IOException {
    while (--paramInt >= 0) {
      write((int)(paramLong >> paramInt * 8));
      paramInt--;
    } 
  }
  
  void writeDouble(double paramDouble) throws IOException {
    writeLong(Double.doubleToRawLongBits(paramDouble));
  }
  
  void writeID(int paramInt) throws IOException {
    writeBytes(paramInt, this.idSizeInBytes);
  }
  
  void writeIntHeader(int paramInt1, int paramInt2) throws IOException {
    if (paramInt2 < 15) {
      write((paramInt1 << 4) + paramInt2);
    } else if (paramInt2 < 256) {
      write((paramInt1 << 4) + 15);
      write(16);
      writeBytes(paramInt2, 1);
    } else if (paramInt2 < 65536) {
      write((paramInt1 << 4) + 15);
      write(17);
      writeBytes(paramInt2, 2);
    } else {
      write((paramInt1 << 4) + 15);
      write(18);
      writeBytes(paramInt2, 4);
    } 
  }
  
  void writeLong(long paramLong) throws IOException {
    writeBytes(paramLong, 8);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/dd/plist/BinaryPropertyListWriter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */