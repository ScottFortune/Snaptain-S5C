package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class LPFHNP_Playback_t_struct extends Structure {
  public byte btChannel;
  
  public byte btEncID;
  
  public byte btRes;
  
  public byte btTransMode;
  
  public int dwRecTypeMask;
  
  public FHNP_Time_t stStartTime;
  
  public FHNP_Time_t stStopTime;
  
  public LPFHNP_Playback_t_struct() {}
  
  public LPFHNP_Playback_t_struct(byte paramByte1, byte paramByte2, byte paramByte3, byte paramByte4, int paramInt, FHNP_Time_t paramFHNP_Time_t1, FHNP_Time_t paramFHNP_Time_t2) {
    this.btChannel = (byte)paramByte1;
    this.btEncID = (byte)paramByte2;
    this.btTransMode = (byte)paramByte3;
    this.btRes = (byte)paramByte4;
    this.dwRecTypeMask = paramInt;
    this.stStartTime = paramFHNP_Time_t1;
    this.stStopTime = paramFHNP_Time_t2;
  }
  
  public LPFHNP_Playback_t_struct(Pointer paramPointer) {
    super(paramPointer);
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { "btChannel", "btEncID", "btTransMode", "btRes", "dwRecTypeMask", "stStartTime", "stStopTime" });
  }
  
  public static class ByReference extends LPFHNP_Playback_t_struct implements Structure.ByReference {}
  
  public static class ByValue extends LPFHNP_Playback_t_struct implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/LPFHNP_Playback_t_struct.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */