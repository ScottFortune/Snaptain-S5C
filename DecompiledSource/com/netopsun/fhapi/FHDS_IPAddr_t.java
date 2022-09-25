package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class FHDS_IPAddr_t extends Structure {
  public byte[] sIPV4 = new byte[16];
  
  public byte[] sIPV6 = new byte[128];
  
  public FHDS_IPAddr_t() {}
  
  public FHDS_IPAddr_t(Pointer paramPointer) {
    super(paramPointer);
  }
  
  public FHDS_IPAddr_t(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) {
    if (paramArrayOfbyte1.length == this.sIPV4.length) {
      this.sIPV4 = paramArrayOfbyte1;
      if (paramArrayOfbyte2.length == this.sIPV6.length) {
        this.sIPV6 = paramArrayOfbyte2;
        return;
      } 
      throw new IllegalArgumentException("Wrong array size !");
    } 
    throw new IllegalArgumentException("Wrong array size !");
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { "sIPV4", "sIPV6" });
  }
  
  public static class ByReference extends FHDS_IPAddr_t implements Structure.ByReference {}
  
  public static class ByValue extends FHDS_IPAddr_t implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/FHDS_IPAddr_t.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */