package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class FHNP_Defence_ClearAlarmEx_t extends Structure {
  public byte btBuzzer;
  
  public byte[] btGpioOut = new byte[8];
  
  public byte btPTZ;
  
  public byte btRes;
  
  public byte btScreenFlash;
  
  public FHNP_Defence_ClearAlarmEx_t() {}
  
  public FHNP_Defence_ClearAlarmEx_t(byte paramByte1, byte paramByte2, byte paramByte3, byte paramByte4, byte[] paramArrayOfbyte) {
    this.btBuzzer = (byte)paramByte1;
    this.btPTZ = (byte)paramByte2;
    this.btScreenFlash = (byte)paramByte3;
    this.btRes = (byte)paramByte4;
    if (paramArrayOfbyte.length == this.btGpioOut.length) {
      this.btGpioOut = paramArrayOfbyte;
      return;
    } 
    throw new IllegalArgumentException("Wrong array size !");
  }
  
  public FHNP_Defence_ClearAlarmEx_t(Pointer paramPointer) {
    super(paramPointer);
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { "btBuzzer", "btPTZ", "btScreenFlash", "btRes", "btGpioOut" });
  }
  
  public static class ByReference extends FHNP_Defence_ClearAlarmEx_t implements Structure.ByReference {}
  
  public static class ByValue extends FHNP_Defence_ClearAlarmEx_t implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/FHNP_Defence_ClearAlarmEx_t.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */