package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class LPFHNP_Defence_OtherIn_t_struct extends Structure {
  public byte btEnable;
  
  public byte[] btRes = new byte[3];
  
  public FHNP_Schedule_t stSchedule;
  
  public FHNP_TrigerBuzzer_t stTrigerBuzzer;
  
  public FHNP_TrigerEMail_t stTrigerEMail;
  
  public FHNP_TrigerGpioOut_t stTrigerGpioOut;
  
  public FHNP_TrigerPTZ_t stTrigerPTZ;
  
  public FHNP_TrigerPicture_t stTrigerPicture;
  
  public FHNP_TrigerRecord_t stTrigerRecord;
  
  public LPFHNP_Defence_OtherIn_t_struct() {}
  
  public LPFHNP_Defence_OtherIn_t_struct(byte paramByte, byte[] paramArrayOfbyte, FHNP_Schedule_t paramFHNP_Schedule_t, FHNP_TrigerBuzzer_t paramFHNP_TrigerBuzzer_t, FHNP_TrigerPTZ_t paramFHNP_TrigerPTZ_t, FHNP_TrigerRecord_t paramFHNP_TrigerRecord_t, FHNP_TrigerPicture_t paramFHNP_TrigerPicture_t, FHNP_TrigerEMail_t paramFHNP_TrigerEMail_t, FHNP_TrigerGpioOut_t paramFHNP_TrigerGpioOut_t) {
    this.btEnable = (byte)paramByte;
    if (paramArrayOfbyte.length == this.btRes.length) {
      this.btRes = paramArrayOfbyte;
      this.stSchedule = paramFHNP_Schedule_t;
      this.stTrigerBuzzer = paramFHNP_TrigerBuzzer_t;
      this.stTrigerPTZ = paramFHNP_TrigerPTZ_t;
      this.stTrigerRecord = paramFHNP_TrigerRecord_t;
      this.stTrigerPicture = paramFHNP_TrigerPicture_t;
      this.stTrigerEMail = paramFHNP_TrigerEMail_t;
      this.stTrigerGpioOut = paramFHNP_TrigerGpioOut_t;
      return;
    } 
    throw new IllegalArgumentException("Wrong array size !");
  }
  
  public LPFHNP_Defence_OtherIn_t_struct(Pointer paramPointer) {
    super(paramPointer);
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { "btEnable", "btRes", "stSchedule", "stTrigerBuzzer", "stTrigerPTZ", "stTrigerRecord", "stTrigerPicture", "stTrigerEMail", "stTrigerGpioOut" });
  }
  
  public static class ByReference extends LPFHNP_Defence_OtherIn_t_struct implements Structure.ByReference {}
  
  public static class ByValue extends LPFHNP_Defence_OtherIn_t_struct implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/LPFHNP_Defence_OtherIn_t_struct.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */