package com.vilyever.socketclient.util;

import java.util.Arrays;

public class BytesWrapper {
  private final byte[] bytes;
  
  final BytesWrapper self = this;
  
  public BytesWrapper(byte[] paramArrayOfbyte) {
    if (paramArrayOfbyte != null) {
      this.bytes = paramArrayOfbyte;
      return;
    } 
    throw new NullPointerException();
  }
  
  public boolean equals(Object paramObject) {
    return !(paramObject instanceof BytesWrapper) ? false : equalsBytes(((BytesWrapper)paramObject).getBytes());
  }
  
  public boolean equalsBytes(byte[] paramArrayOfbyte) {
    return Arrays.equals(getBytes(), paramArrayOfbyte);
  }
  
  public byte[] getBytes() {
    return this.bytes;
  }
  
  public int hashCode() {
    return Arrays.hashCode(getBytes());
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/vilyever/socketclient/util/BytesWrapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */