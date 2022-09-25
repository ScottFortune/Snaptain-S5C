package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class LPFHNP_PicSearch_t_struct extends Structure {
  public byte btChanNum;
  
  public byte[] btChannel = new byte[32];
  
  public byte btLockFlag;
  
  public byte[] btRes = new byte[2];
  
  public int dwPicTypeMask;
  
  public FHNP_Time_t stStartTime;
  
  public FHNP_Time_t stStopTime;
  
  public LPFHNP_PicSearch_t_struct() {}
  
  public LPFHNP_PicSearch_t_struct(byte paramByte1, byte paramByte2, byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, int paramInt, FHNP_Time_t paramFHNP_Time_t1, FHNP_Time_t paramFHNP_Time_t2) {
    this.btLockFlag = (byte)paramByte1;
    this.btChanNum = (byte)paramByte2;
    if (paramArrayOfbyte1.length == this.btRes.length) {
      this.btRes = paramArrayOfbyte1;
      if (paramArrayOfbyte2.length == this.btChannel.length) {
        this.btChannel = paramArrayOfbyte2;
        this.dwPicTypeMask = paramInt;
        this.stStartTime = paramFHNP_Time_t1;
        this.stStopTime = paramFHNP_Time_t2;
        return;
      } 
      throw new IllegalArgumentException("Wrong array size !");
    } 
    throw new IllegalArgumentException("Wrong array size !");
  }
  
  public LPFHNP_PicSearch_t_struct(Pointer paramPointer) {
    super(paramPointer);
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { "btLockFlag", "btChanNum", "btRes", "btChannel", "dwPicTypeMask", "stStartTime", "stStopTime" });
  }
  
  public static class ByReference extends LPFHNP_PicSearch_t_struct implements Structure.ByReference {}
  
  public static class ByValue extends LPFHNP_PicSearch_t_struct implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/LPFHNP_PicSearch_t_struct.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */