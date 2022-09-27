package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class FHNP_BookMarkSearch_t extends Structure {
  public byte btChanNum;
  
  public byte[] btChannel = new byte[32];
  
  public byte btDataType;
  
  public byte btRes;
  
  public byte[] chKey = new byte[32];
  
  public FHNP_Time_t stStartTime;
  
  public FHNP_Time_t stStopTime;
  
  public FHNP_BookMarkSearch_t() {}
  
  public FHNP_BookMarkSearch_t(byte paramByte1, byte paramByte2, byte paramByte3, byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, FHNP_Time_t paramFHNP_Time_t1, FHNP_Time_t paramFHNP_Time_t2) {
    this.btChanNum = (byte)paramByte1;
    this.btDataType = (byte)paramByte2;
    this.btRes = (byte)paramByte3;
    if (paramArrayOfbyte1.length == this.btChannel.length) {
      this.btChannel = paramArrayOfbyte1;
      if (paramArrayOfbyte2.length == this.chKey.length) {
        this.chKey = paramArrayOfbyte2;
        this.stStartTime = paramFHNP_Time_t1;
        this.stStopTime = paramFHNP_Time_t2;
        return;
      } 
      throw new IllegalArgumentException("Wrong array size !");
    } 
    throw new IllegalArgumentException("Wrong array size !");
  }
  
  public FHNP_BookMarkSearch_t(Pointer paramPointer) {
    super(paramPointer);
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { "btChanNum", "btDataType", "btRes", "btChannel", "chKey", "stStartTime", "stStopTime" });
  }
  
  public static class ByReference extends FHNP_BookMarkSearch_t implements Structure.ByReference {}
  
  public static class ByValue extends FHNP_BookMarkSearch_t implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/FHNP_BookMarkSearch_t.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */