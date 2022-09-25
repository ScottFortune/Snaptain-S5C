package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class LPFHNP_Capacity_t_struct extends Structure {
  public byte btAlarmInNum;
  
  public byte btAlarmOutNum;
  
  public byte btAudioInNum;
  
  public byte btChannelNum;
  
  public byte btDecodeChannelNum;
  
  public byte btDevType;
  
  public byte btHDMINum;
  
  public byte btIPCanNum;
  
  public byte btNetWorkPortNum;
  
  public byte btRS232Num;
  
  public byte btRS485Num;
  
  public byte[] btRes = new byte[3];
  
  public byte[] btRes2 = new byte[4];
  
  public byte btSpotNum;
  
  public byte btStorageNum;
  
  public byte btTalkPortNum;
  
  public byte btUSBNum;
  
  public byte btVGANum;
  
  public byte btVideoInNum;
  
  public byte[] chSerialNum = new byte[32];
  
  public int dwFWBuildDate;
  
  public int dwFWVersion;
  
  public int dwHWVersion;
  
  public int dwSWBuildDate;
  
  public int dwSWVersion;
  
  public LPFHNP_Capacity_t_struct() {}
  
  public LPFHNP_Capacity_t_struct(Pointer paramPointer) {
    super(paramPointer);
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { 
          "chSerialNum", "btDevType", "btRes", "dwHWVersion", "dwFWVersion", "dwFWBuildDate", "dwSWVersion", "dwSWBuildDate", "btVideoInNum", "btAudioInNum", 
          "btAlarmInNum", "btAlarmOutNum", "btTalkPortNum", "btRS232Num", "btRS485Num", "btNetWorkPortNum", "btStorageNum", "btChannelNum", "btDecodeChannelNum", "btVGANum", 
          "btHDMINum", "btUSBNum", "btSpotNum", "btIPCanNum", "btRes2" });
  }
  
  public static class ByReference extends LPFHNP_Capacity_t_struct implements Structure.ByReference {}
  
  public static class ByValue extends LPFHNP_Capacity_t_struct implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/LPFHNP_Capacity_t_struct.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */