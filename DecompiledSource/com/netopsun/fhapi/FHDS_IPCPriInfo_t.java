package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class FHDS_IPCPriInfo_t extends Structure {
  public byte[] btRes = new byte[4];
  
  public FHDS_IPCPriInfo_t() {}
  
  public FHDS_IPCPriInfo_t(Pointer paramPointer) {
    super(paramPointer);
  }
  
  public FHDS_IPCPriInfo_t(byte[] paramArrayOfbyte) {
    if (paramArrayOfbyte.length == this.btRes.length) {
      this.btRes = paramArrayOfbyte;
      return;
    } 
    throw new IllegalArgumentException("Wrong array size !");
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { "btRes" });
  }
  
  public static class ByReference extends FHDS_IPCPriInfo_t implements Structure.ByReference {}
  
  public static class ByValue extends FHDS_IPCPriInfo_t implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/FHDS_IPCPriInfo_t.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */