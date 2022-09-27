package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class LPFHDS_ModifyNetAddr_t_struct extends Structure {
  public int dwDevType;
  
  public FHDS_PubNetAddr_t stDevNetAddr;
  
  public LPFHDS_ModifyNetAddr_t_struct() {}
  
  public LPFHDS_ModifyNetAddr_t_struct(int paramInt, FHDS_PubNetAddr_t paramFHDS_PubNetAddr_t) {
    this.dwDevType = paramInt;
    this.stDevNetAddr = paramFHDS_PubNetAddr_t;
  }
  
  public LPFHDS_ModifyNetAddr_t_struct(Pointer paramPointer) {
    super(paramPointer);
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { "dwDevType", "stDevNetAddr" });
  }
  
  public static class ByReference extends LPFHDS_ModifyNetAddr_t_struct implements Structure.ByReference {}
  
  public static class ByValue extends LPFHDS_ModifyNetAddr_t_struct implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/LPFHDS_ModifyNetAddr_t_struct.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */