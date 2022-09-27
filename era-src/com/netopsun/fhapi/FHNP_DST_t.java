package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class FHNP_DST_t extends Structure {
  public byte btDST;
  
  public byte btDSTMode;
  
  public byte[] btRes = new byte[2];
  
  public FHNP_DSTDateMode_t stDSTDateMode;
  
  public FHNP_DSTWeekMode_t stDSTWeekMode;
  
  public FHNP_DST_t() {}
  
  public FHNP_DST_t(byte paramByte1, byte paramByte2, byte[] paramArrayOfbyte, FHNP_DSTDateMode_t paramFHNP_DSTDateMode_t, FHNP_DSTWeekMode_t paramFHNP_DSTWeekMode_t) {
    this.btDST = (byte)paramByte1;
    this.btDSTMode = (byte)paramByte2;
    if (paramArrayOfbyte.length == this.btRes.length) {
      this.btRes = paramArrayOfbyte;
      this.stDSTDateMode = paramFHNP_DSTDateMode_t;
      this.stDSTWeekMode = paramFHNP_DSTWeekMode_t;
      return;
    } 
    throw new IllegalArgumentException("Wrong array size !");
  }
  
  public FHNP_DST_t(Pointer paramPointer) {
    super(paramPointer);
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { "btDST", "btDSTMode", "btRes", "stDSTDateMode", "stDSTWeekMode" });
  }
  
  public static class ByReference extends FHNP_DST_t implements Structure.ByReference {}
  
  public static class ByValue extends FHNP_DST_t implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/FHNP_DST_t.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */