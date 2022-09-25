package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class LPFHNP_Defence_PicPlan_t_struct extends Structure {
  public byte btEnable;
  
  public byte[] btRes = new byte[3];
  
  public int dwInterval;
  
  public FHNP_Schedule_t stSchedule;
  
  public LPFHNP_Defence_PicPlan_t_struct() {}
  
  public LPFHNP_Defence_PicPlan_t_struct(byte paramByte, byte[] paramArrayOfbyte, int paramInt, FHNP_Schedule_t paramFHNP_Schedule_t) {
    this.btEnable = (byte)paramByte;
    if (paramArrayOfbyte.length == this.btRes.length) {
      this.btRes = paramArrayOfbyte;
      this.dwInterval = paramInt;
      this.stSchedule = paramFHNP_Schedule_t;
      return;
    } 
    throw new IllegalArgumentException("Wrong array size !");
  }
  
  public LPFHNP_Defence_PicPlan_t_struct(Pointer paramPointer) {
    super(paramPointer);
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { "btEnable", "btRes", "dwInterval", "stSchedule" });
  }
  
  public static class ByReference extends LPFHNP_Defence_PicPlan_t_struct implements Structure.ByReference {}
  
  public static class ByValue extends LPFHNP_Defence_PicPlan_t_struct implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/LPFHNP_Defence_PicPlan_t_struct.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */