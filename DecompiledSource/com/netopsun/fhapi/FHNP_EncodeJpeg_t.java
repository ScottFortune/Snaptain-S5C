package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class FHNP_EncodeJpeg_t extends Structure {
  public byte btEncJpegQaulity;
  
  public byte[] btRes = new byte[3];
  
  public FHNP_EncodeJpeg_t() {}
  
  public FHNP_EncodeJpeg_t(byte paramByte, byte[] paramArrayOfbyte) {
    this.btEncJpegQaulity = (byte)paramByte;
    if (paramArrayOfbyte.length == this.btRes.length) {
      this.btRes = paramArrayOfbyte;
      return;
    } 
    throw new IllegalArgumentException("Wrong array size !");
  }
  
  public FHNP_EncodeJpeg_t(Pointer paramPointer) {
    super(paramPointer);
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { "btEncJpegQaulity", "btRes" });
  }
  
  public static class ByReference extends FHNP_EncodeJpeg_t implements Structure.ByReference {}
  
  public static class ByValue extends FHNP_EncodeJpeg_t implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/FHNP_EncodeJpeg_t.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */