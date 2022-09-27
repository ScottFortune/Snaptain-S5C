package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class FHNP_Defence_ClearAlarm_t extends Structure {
  public byte btBuzzer;
  
  public byte[] btGpioOut = new byte[8];
  
  public byte[] btRes = new byte[3];
  
  public FHNP_Defence_ClearAlarm_t() {}
  
  public FHNP_Defence_ClearAlarm_t(byte paramByte, byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) {
    this.btBuzzer = (byte)paramByte;
    if (paramArrayOfbyte1.length == this.btRes.length) {
      this.btRes = paramArrayOfbyte1;
      if (paramArrayOfbyte2.length == this.btGpioOut.length) {
        this.btGpioOut = paramArrayOfbyte2;
        return;
      } 
      throw new IllegalArgumentException("Wrong array size !");
    } 
    throw new IllegalArgumentException("Wrong array size !");
  }
  
  public FHNP_Defence_ClearAlarm_t(Pointer paramPointer) {
    super(paramPointer);
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { "btBuzzer", "btRes", "btGpioOut" });
  }
  
  public static class ByReference extends FHNP_Defence_ClearAlarm_t implements Structure.ByReference {}
  
  public static class ByValue extends FHNP_Defence_ClearAlarm_t implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/FHNP_Defence_ClearAlarm_t.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */