package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class FHNP_TrigerEMail_t extends Structure {
  public byte btEnable;
  
  public byte[] btRes = new byte[3];
  
  public FHNP_TrigerEMail_t() {}
  
  public FHNP_TrigerEMail_t(byte paramByte, byte[] paramArrayOfbyte) {
    this.btEnable = (byte)paramByte;
    if (paramArrayOfbyte.length == this.btRes.length) {
      this.btRes = paramArrayOfbyte;
      return;
    } 
    throw new IllegalArgumentException("Wrong array size !");
  }
  
  public FHNP_TrigerEMail_t(Pointer paramPointer) {
    super(paramPointer);
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { "btEnable", "btRes" });
  }
  
  public static class ByReference extends FHNP_TrigerEMail_t implements Structure.ByReference {}
  
  public static class ByValue extends FHNP_TrigerEMail_t implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/FHNP_TrigerEMail_t.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */