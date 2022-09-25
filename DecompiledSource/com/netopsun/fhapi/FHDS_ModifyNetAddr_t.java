package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class FHDS_ModifyNetAddr_t extends Structure {
  public int dwDevType;
  
  public FHDS_PubNetAddr_t stDevNetAddr;
  
  public FHDS_ModifyNetAddr_t() {}
  
  public FHDS_ModifyNetAddr_t(int paramInt, FHDS_PubNetAddr_t paramFHDS_PubNetAddr_t) {
    this.dwDevType = paramInt;
    this.stDevNetAddr = paramFHDS_PubNetAddr_t;
  }
  
  public FHDS_ModifyNetAddr_t(Pointer paramPointer) {
    super(paramPointer);
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { "dwDevType", "stDevNetAddr" });
  }
  
  public static class ByReference extends FHDS_ModifyNetAddr_t implements Structure.ByReference {}
  
  public static class ByValue extends FHDS_ModifyNetAddr_t implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/FHDS_ModifyNetAddr_t.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */