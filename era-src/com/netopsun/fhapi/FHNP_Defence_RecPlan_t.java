package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class FHNP_Defence_RecPlan_t extends Structure {
  public byte btEnable;
  
  public byte[] btRes = new byte[3];
  
  public FHNP_Schedule_t stSchedule;
  
  public FHNP_TrigerPTZ_t stTrigerPTZ;
  
  public FHNP_Defence_RecPlan_t() {}
  
  public FHNP_Defence_RecPlan_t(byte paramByte, byte[] paramArrayOfbyte, FHNP_Schedule_t paramFHNP_Schedule_t, FHNP_TrigerPTZ_t paramFHNP_TrigerPTZ_t) {
    this.btEnable = (byte)paramByte;
    if (paramArrayOfbyte.length == this.btRes.length) {
      this.btRes = paramArrayOfbyte;
      this.stSchedule = paramFHNP_Schedule_t;
      this.stTrigerPTZ = paramFHNP_TrigerPTZ_t;
      return;
    } 
    throw new IllegalArgumentException("Wrong array size !");
  }
  
  public FHNP_Defence_RecPlan_t(Pointer paramPointer) {
    super(paramPointer);
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { "btEnable", "btRes", "stSchedule", "stTrigerPTZ" });
  }
  
  public static class ByReference extends FHNP_Defence_RecPlan_t implements Structure.ByReference {}
  
  public static class ByValue extends FHNP_Defence_RecPlan_t implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/FHNP_Defence_RecPlan_t.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */