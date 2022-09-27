package com.dd.plist;

import java.io.IOException;

public class UID extends NSObject {
  private byte[] bytes;
  
  private String name;
  
  public UID(String paramString, byte[] paramArrayOfbyte) {
    this.name = paramString;
    this.bytes = paramArrayOfbyte;
  }
  
  public byte[] getBytes() {
    return this.bytes;
  }
  
  public String getName() {
    return this.name;
  }
  
  protected void toASCII(StringBuilder paramStringBuilder, int paramInt) {
    indent(paramStringBuilder, paramInt);
    paramStringBuilder.append("\"");
    paramInt = 0;
    while (true) {
      byte[] arrayOfByte = this.bytes;
      if (paramInt < arrayOfByte.length) {
        byte b = arrayOfByte[paramInt];
        if (b < 16)
          paramStringBuilder.append("0"); 
        paramStringBuilder.append(Integer.toHexString(b));
        paramInt++;
        continue;
      } 
      paramStringBuilder.append("\"");
      return;
    } 
  }
  
  protected void toASCIIGnuStep(StringBuilder paramStringBuilder, int paramInt) {
    toASCII(paramStringBuilder, paramInt);
  }
  
  void toBinary(BinaryPropertyListWriter paramBinaryPropertyListWriter) throws IOException {
    paramBinaryPropertyListWriter.write(this.bytes.length + 128 - 1);
    paramBinaryPropertyListWriter.write(this.bytes);
  }
  
  void toXML(StringBuilder paramStringBuilder, int paramInt) {
    indent(paramStringBuilder, paramInt);
    paramStringBuilder.append("<string>");
    paramInt = 0;
    while (true) {
      byte[] arrayOfByte = this.bytes;
      if (paramInt < arrayOfByte.length) {
        byte b = arrayOfByte[paramInt];
        if (b < 16)
          paramStringBuilder.append("0"); 
        paramStringBuilder.append(Integer.toHexString(b));
        paramInt++;
        continue;
      } 
      paramStringBuilder.append("</string>");
      return;
    } 
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/dd/plist/UID.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */