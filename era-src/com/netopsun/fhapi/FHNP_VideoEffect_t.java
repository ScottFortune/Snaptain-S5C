package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class FHNP_VideoEffect_t extends Structure {
  public byte btBrightness;
  
  public byte btContrast;
  
  public byte btHue;
  
  public byte[] btRes = new byte[3];
  
  public byte btSaturation;
  
  public byte btSharpness;
  
  public FHNP_VideoEffect_t() {}
  
  public FHNP_VideoEffect_t(byte paramByte1, byte paramByte2, byte paramByte3, byte paramByte4, byte paramByte5, byte[] paramArrayOfbyte) {
    this.btBrightness = (byte)paramByte1;
    this.btContrast = (byte)paramByte2;
    this.btSaturation = (byte)paramByte3;
    this.btHue = (byte)paramByte4;
    this.btSharpness = (byte)paramByte5;
    if (paramArrayOfbyte.length == this.btRes.length) {
      this.btRes = paramArrayOfbyte;
      return;
    } 
    throw new IllegalArgumentException("Wrong array size !");
  }
  
  public FHNP_VideoEffect_t(Pointer paramPointer) {
    super(paramPointer);
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { "btBrightness", "btContrast", "btSaturation", "btHue", "btSharpness", "btRes" });
  }
  
  public static class ByReference extends FHNP_VideoEffect_t implements Structure.ByReference {}
  
  public static class ByValue extends FHNP_VideoEffect_t implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/FHNP_VideoEffect_t.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */