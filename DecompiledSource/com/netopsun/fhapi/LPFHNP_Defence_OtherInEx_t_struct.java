package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class LPFHNP_Defence_OtherInEx_t_struct extends Structure {
  public byte btEnable;
  
  public byte[] btRes = new byte[3];
  
  public FHNP_Schedule_t stSchedule;
  
  public FHNP_TrigerBuzzer_t stTrigerBuzzer;
  
  public FHNP_TrigerEMail_t stTrigerEMail;
  
  public FHNP_TrigerGpioOut_t stTrigerGpioOut;
  
  public FHNP_TrigerPTZ_t stTrigerPTZ;
  
  public FHNP_TrigerPicture_t stTrigerPicture;
  
  public FHNP_TrigerRecord_t stTrigerRecord;
  
  public FHNP_TrigerScreenFlash_t stTrigerScreen;
  
  public LPFHNP_Defence_OtherInEx_t_struct() {}
  
  public LPFHNP_Defence_OtherInEx_t_struct(Pointer paramPointer) {
    super(paramPointer);
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { "btEnable", "btRes", "stSchedule", "stTrigerBuzzer", "stTrigerPTZ", "stTrigerRecord", "stTrigerPicture", "stTrigerEMail", "stTrigerScreen", "stTrigerGpioOut" });
  }
  
  public static class ByReference extends LPFHNP_Defence_OtherInEx_t_struct implements Structure.ByReference {}
  
  public static class ByValue extends LPFHNP_Defence_OtherInEx_t_struct implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/LPFHNP_Defence_OtherInEx_t_struct.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */