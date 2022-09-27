package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class FHNP_DSTV2_t extends Structure {
  public byte btDST;
  
  public byte btDSTBias;
  
  public byte[] btRes = new byte[2];
  
  public FHNP_DSTWeekModeTime_t stStartTime;
  
  public FHNP_DSTWeekModeTime_t stStopTime;
  
  public FHNP_DSTV2_t() {}
  
  public FHNP_DSTV2_t(byte paramByte1, byte paramByte2, byte[] paramArrayOfbyte, FHNP_DSTWeekModeTime_t paramFHNP_DSTWeekModeTime_t1, FHNP_DSTWeekModeTime_t paramFHNP_DSTWeekModeTime_t2) {
    this.btDST = (byte)paramByte1;
    this.btDSTBias = (byte)paramByte2;
    if (paramArrayOfbyte.length == this.btRes.length) {
      this.btRes = paramArrayOfbyte;
      this.stStartTime = paramFHNP_DSTWeekModeTime_t1;
      this.stStopTime = paramFHNP_DSTWeekModeTime_t2;
      return;
    } 
    throw new IllegalArgumentException("Wrong array size !");
  }
  
  public FHNP_DSTV2_t(Pointer paramPointer) {
    super(paramPointer);
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { "btDST", "btDSTBias", "btRes", "stStartTime", "stStopTime" });
  }
  
  public static class ByReference extends FHNP_DSTV2_t implements Structure.ByReference {}
  
  public static class ByValue extends FHNP_DSTV2_t implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/FHNP_DSTV2_t.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */