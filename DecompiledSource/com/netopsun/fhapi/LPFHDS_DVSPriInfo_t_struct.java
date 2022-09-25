package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class LPFHDS_DVSPriInfo_t_struct extends Structure {
  public byte[] btRes = new byte[4];
  
  public LPFHDS_DVSPriInfo_t_struct() {}
  
  public LPFHDS_DVSPriInfo_t_struct(Pointer paramPointer) {
    super(paramPointer);
  }
  
  public LPFHDS_DVSPriInfo_t_struct(byte[] paramArrayOfbyte) {
    if (paramArrayOfbyte.length == this.btRes.length) {
      this.btRes = paramArrayOfbyte;
      return;
    } 
    throw new IllegalArgumentException("Wrong array size !");
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { "btRes" });
  }
  
  public static class ByReference extends LPFHDS_DVSPriInfo_t_struct implements Structure.ByReference {}
  
  public static class ByValue extends LPFHDS_DVSPriInfo_t_struct implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/LPFHDS_DVSPriInfo_t_struct.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */