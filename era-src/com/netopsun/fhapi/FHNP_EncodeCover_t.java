package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class FHNP_EncodeCover_t extends Structure {
  public byte btColorType;
  
  public byte btCoverType;
  
  public byte btEnable;
  
  public byte btRes;
  
  public FHNP_Rect_t[] stRect = new FHNP_Rect_t[4];
  
  public FHNP_EncodeCover_t() {}
  
  public FHNP_EncodeCover_t(byte paramByte1, byte paramByte2, byte paramByte3, byte paramByte4, FHNP_Rect_t[] paramArrayOfFHNP_Rect_t) {
    this.btEnable = (byte)paramByte1;
    this.btCoverType = (byte)paramByte2;
    this.btColorType = (byte)paramByte3;
    this.btRes = (byte)paramByte4;
    if (paramArrayOfFHNP_Rect_t.length == this.stRect.length) {
      this.stRect = paramArrayOfFHNP_Rect_t;
      return;
    } 
    throw new IllegalArgumentException("Wrong array size !");
  }
  
  public FHNP_EncodeCover_t(Pointer paramPointer) {
    super(paramPointer);
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { "btEnable", "btCoverType", "btColorType", "btRes", "stRect" });
  }
  
  public static class ByReference extends FHNP_EncodeCover_t implements Structure.ByReference {}
  
  public static class ByValue extends FHNP_EncodeCover_t implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/FHNP_EncodeCover_t.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */