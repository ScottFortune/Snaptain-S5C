package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class FHNP_EncodeWaterMarkV2_t extends Structure {
  public byte btEnable;
  
  public byte[] btRes = new byte[2];
  
  public byte btSize;
  
  public byte[] chText = new byte[256];
  
  public FHNP_EncodeWaterMarkV2_t() {}
  
  public FHNP_EncodeWaterMarkV2_t(byte paramByte1, byte paramByte2, byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) {
    this.btEnable = (byte)paramByte1;
    this.btSize = (byte)paramByte2;
    if (paramArrayOfbyte1.length == this.btRes.length) {
      this.btRes = paramArrayOfbyte1;
      if (paramArrayOfbyte2.length == this.chText.length) {
        this.chText = paramArrayOfbyte2;
        return;
      } 
      throw new IllegalArgumentException("Wrong array size !");
    } 
    throw new IllegalArgumentException("Wrong array size !");
  }
  
  public FHNP_EncodeWaterMarkV2_t(Pointer paramPointer) {
    super(paramPointer);
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { "btEnable", "btSize", "btRes", "chText" });
  }
  
  public static class ByReference extends FHNP_EncodeWaterMarkV2_t implements Structure.ByReference {}
  
  public static class ByValue extends FHNP_EncodeWaterMarkV2_t implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/FHNP_EncodeWaterMarkV2_t.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */