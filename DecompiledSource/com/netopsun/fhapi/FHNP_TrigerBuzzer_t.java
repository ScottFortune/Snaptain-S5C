package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class FHNP_TrigerBuzzer_t extends Structure {
  public byte btBuzzerType;
  
  public byte btEnable;
  
  public byte[] btRes = new byte[2];
  
  public FHNP_TrigerBuzzer_t() {}
  
  public FHNP_TrigerBuzzer_t(byte paramByte1, byte paramByte2, byte[] paramArrayOfbyte) {
    this.btEnable = (byte)paramByte1;
    this.btBuzzerType = (byte)paramByte2;
    if (paramArrayOfbyte.length == this.btRes.length) {
      this.btRes = paramArrayOfbyte;
      return;
    } 
    throw new IllegalArgumentException("Wrong array size !");
  }
  
  public FHNP_TrigerBuzzer_t(Pointer paramPointer) {
    super(paramPointer);
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { "btEnable", "btBuzzerType", "btRes" });
  }
  
  public static class ByReference extends FHNP_TrigerBuzzer_t implements Structure.ByReference {}
  
  public static class ByValue extends FHNP_TrigerBuzzer_t implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/FHNP_TrigerBuzzer_t.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */