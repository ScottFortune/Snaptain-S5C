package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class FHNP_BookMark_t extends Structure {
  public byte btChannel;
  
  public byte btDataType;
  
  public byte btRes;
  
  public byte[] chBookMark = new byte[32];
  
  public long ullBookMark;
  
  public FHNP_BookMark_t() {}
  
  public FHNP_BookMark_t(byte paramByte1, byte paramByte2, byte paramByte3, long paramLong, byte[] paramArrayOfbyte) {
    this.btChannel = (byte)paramByte1;
    this.btDataType = (byte)paramByte2;
    this.btRes = (byte)paramByte3;
    this.ullBookMark = paramLong;
    if (paramArrayOfbyte.length == this.chBookMark.length) {
      this.chBookMark = paramArrayOfbyte;
      return;
    } 
    throw new IllegalArgumentException("Wrong array size !");
  }
  
  public FHNP_BookMark_t(Pointer paramPointer) {
    super(paramPointer);
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { "btChannel", "btDataType", "btRes", "ullBookMark", "chBookMark" });
  }
  
  public static class ByReference extends FHNP_BookMark_t implements Structure.ByReference {}
  
  public static class ByValue extends FHNP_BookMark_t implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/FHNP_BookMark_t.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */