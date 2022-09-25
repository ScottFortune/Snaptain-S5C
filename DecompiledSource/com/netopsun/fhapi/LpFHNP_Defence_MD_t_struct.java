package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class LpFHNP_Defence_MD_t_struct extends Structure {
  public byte btEnable;
  
  public byte[] btRes = new byte[2];
  
  public byte btSensitivity;
  
  public int dwFrameDelay;
  
  public int iCustomSensitive;
  
  public FHNP_Rect_t[] stRect = new FHNP_Rect_t[32];
  
  public FHNP_Schedule_t stSchedule;
  
  public FHNP_TrigerBuzzer_t stTrigerBuzzer;
  
  public FHNP_TrigerEMail_t stTrigerEMail;
  
  public FHNP_TrigerGpioOut_t stTrigerGpioOut;
  
  public FHNP_TrigerPicture_t stTrigerPicture;
  
  public FHNP_TrigerRecord_t stTrigerRecord;
  
  public LpFHNP_Defence_MD_t_struct() {}
  
  public LpFHNP_Defence_MD_t_struct(Pointer paramPointer) {
    super(paramPointer);
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { 
          "btEnable", "btSensitivity", "btRes", "iCustomSensitive", "dwFrameDelay", "stRect", "stSchedule", "stTrigerBuzzer", "stTrigerRecord", "stTrigerPicture", 
          "stTrigerEMail", "stTrigerGpioOut" });
  }
  
  public static class ByReference extends LpFHNP_Defence_MD_t_struct implements Structure.ByReference {}
  
  public static class ByValue extends LpFHNP_Defence_MD_t_struct implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/LpFHNP_Defence_MD_t_struct.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */