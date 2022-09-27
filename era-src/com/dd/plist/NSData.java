package com.dd.plist;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class NSData extends NSObject {
  private byte[] bytes;
  
  public NSData(File paramFile) throws IOException {
    this.bytes = new byte[(int)paramFile.length()];
    RandomAccessFile randomAccessFile = new RandomAccessFile(paramFile, "r");
    randomAccessFile.read(this.bytes);
    randomAccessFile.close();
  }
  
  public NSData(String paramString) throws IOException {
    this.bytes = Base64.decode(paramString.replaceAll("\\s+", ""));
  }
  
  public NSData(byte[] paramArrayOfbyte) {
    this.bytes = paramArrayOfbyte;
  }
  
  public byte[] bytes() {
    return this.bytes;
  }
  
  public boolean equals(Object paramObject) {
    boolean bool;
    if (paramObject.getClass().equals(getClass()) && Arrays.equals(((NSData)paramObject).bytes, this.bytes)) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public String getBase64EncodedData() {
    return Base64.encodeBytes(this.bytes);
  }
  
  public void getBytes(ByteBuffer paramByteBuffer, int paramInt) {
    byte[] arrayOfByte = this.bytes;
    paramByteBuffer.put(arrayOfByte, 0, Math.min(arrayOfByte.length, paramInt));
  }
  
  public void getBytes(ByteBuffer paramByteBuffer, int paramInt1, int paramInt2) {
    byte[] arrayOfByte = this.bytes;
    paramByteBuffer.put(arrayOfByte, paramInt1, Math.min(arrayOfByte.length, paramInt2));
  }
  
  public int hashCode() {
    return 335 + Arrays.hashCode(this.bytes);
  }
  
  public int length() {
    return this.bytes.length;
  }
  
  protected void toASCII(StringBuilder paramStringBuilder, int paramInt) {
    indent(paramStringBuilder, paramInt);
    paramStringBuilder.append('<');
    int i = paramStringBuilder.lastIndexOf(NEWLINE);
    paramInt = 0;
    while (true) {
      byte[] arrayOfByte = this.bytes;
      if (paramInt < arrayOfByte.length) {
        int j = arrayOfByte[paramInt] & 0xFF;
        if (j < 16)
          paramStringBuilder.append("0"); 
        paramStringBuilder.append(Integer.toHexString(j));
        if (paramStringBuilder.length() - i > 80) {
          paramStringBuilder.append(NEWLINE);
          j = paramStringBuilder.length();
        } else {
          j = i;
          if ((paramInt + 1) % 2 == 0) {
            j = i;
            if (paramInt != this.bytes.length - 1) {
              paramStringBuilder.append(" ");
              j = i;
            } 
          } 
        } 
        paramInt++;
        i = j;
        continue;
      } 
      paramStringBuilder.append('>');
      return;
    } 
  }
  
  protected void toASCIIGnuStep(StringBuilder paramStringBuilder, int paramInt) {
    toASCII(paramStringBuilder, paramInt);
  }
  
  void toBinary(BinaryPropertyListWriter paramBinaryPropertyListWriter) throws IOException {
    paramBinaryPropertyListWriter.writeIntHeader(4, this.bytes.length);
    paramBinaryPropertyListWriter.write(this.bytes);
  }
  
  void toXML(StringBuilder paramStringBuilder, int paramInt) {
    indent(paramStringBuilder, paramInt);
    paramStringBuilder.append("<data>");
    paramStringBuilder.append(NSObject.NEWLINE);
    for (String str : getBase64EncodedData().split("\n")) {
      indent(paramStringBuilder, paramInt + 1);
      paramStringBuilder.append(str);
      paramStringBuilder.append(NSObject.NEWLINE);
    } 
    indent(paramStringBuilder, paramInt);
    paramStringBuilder.append("</data>");
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/dd/plist/NSData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */