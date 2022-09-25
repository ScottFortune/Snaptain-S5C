package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class FHNP_Defence_DefenceCfg_t extends Structure {
  public byte btCD;
  
  public byte[] btException = new byte[8];
  
  public byte[] btGpioIn = new byte[8];
  
  public byte btMD;
  
  public byte btRes;
  
  public byte btVideoLost;
  
  public FHNP_Defence_DefenceCfg_t() {}
  
  public FHNP_Defence_DefenceCfg_t(Pointer paramPointer) {
    super(paramPointer);
  }
  
  public FHNP_Defence_DefenceCfg_t(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte paramByte1, byte paramByte2, byte paramByte3, byte paramByte4) {
    if (paramArrayOfbyte1.length == this.btGpioIn.length) {
      this.btGpioIn = paramArrayOfbyte1;
      if (paramArrayOfbyte2.length == this.btException.length) {
        this.btException = paramArrayOfbyte2;
        this.btMD = (byte)paramByte1;
        this.btCD = (byte)paramByte2;
        this.btVideoLost = (byte)paramByte3;
        this.btRes = (byte)paramByte4;
        return;
      } 
      throw new IllegalArgumentException("Wrong array size !");
    } 
    throw new IllegalArgumentException("Wrong array size !");
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { "btGpioIn", "btException", "btMD", "btCD", "btVideoLost", "btRes" });
  }
  
  public static class ByReference extends FHNP_Defence_DefenceCfg_t implements Structure.ByReference {}
  
  public static class ByValue extends FHNP_Defence_DefenceCfg_t implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/FHNP_Defence_DefenceCfg_t.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */