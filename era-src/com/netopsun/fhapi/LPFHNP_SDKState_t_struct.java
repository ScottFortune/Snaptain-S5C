package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class LPFHNP_SDKState_t_struct extends Structure {
  public int[] dwRes = new int[10];
  
  public int dwTotalFormatNum;
  
  public int dwTotalLogSearchNum;
  
  public int dwTotalLoginNum;
  
  public int dwTotalPicDownloadNum;
  
  public int dwTotalPicSearchNum;
  
  public int dwTotalPlayBackNum;
  
  public int dwTotalRealPlayNum;
  
  public int dwTotalRecDownloadNum;
  
  public int dwTotalRecSearchNum;
  
  public int dwTotalSerialNum;
  
  public int dwTotalShotNum;
  
  public int dwTotalTalkNum;
  
  public int dwTotalUpgradeNum;
  
  public LPFHNP_SDKState_t_struct() {}
  
  public LPFHNP_SDKState_t_struct(Pointer paramPointer) {
    super(paramPointer);
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { 
          "dwTotalLoginNum", "dwTotalRealPlayNum", "dwTotalPlayBackNum", "dwTotalUpgradeNum", "dwTotalSerialNum", "dwTotalTalkNum", "dwTotalShotNum", "dwTotalRecSearchNum", "dwTotalRecDownloadNum", "dwTotalPicSearchNum", 
          "dwTotalPicDownloadNum", "dwTotalLogSearchNum", "dwTotalFormatNum", "dwRes" });
  }
  
  public static class ByReference extends LPFHNP_SDKState_t_struct implements Structure.ByReference {}
  
  public static class ByValue extends LPFHNP_SDKState_t_struct implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/LPFHNP_SDKState_t_struct.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */