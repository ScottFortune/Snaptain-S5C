package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class FHNP_TrigerPicture_t extends Structure {
  public byte[] btEnable = new byte[32];
  
  public FHNP_TrigerPicture_t() {}
  
  public FHNP_TrigerPicture_t(Pointer paramPointer) {
    super(paramPointer);
  }
  
  public FHNP_TrigerPicture_t(byte[] paramArrayOfbyte) {
    if (paramArrayOfbyte.length == this.btEnable.length) {
      this.btEnable = paramArrayOfbyte;
      return;
    } 
    throw new IllegalArgumentException("Wrong array size !");
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { "btEnable" });
  }
  
  public static class ByReference extends FHNP_TrigerPicture_t implements Structure.ByReference {}
  
  public static class ByValue extends FHNP_TrigerPicture_t implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/FHNP_TrigerPicture_t.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */