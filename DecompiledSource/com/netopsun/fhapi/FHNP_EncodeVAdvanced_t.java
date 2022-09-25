package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class FHNP_EncodeVAdvanced_t extends Structure {
  public byte btEntropyMode;
  
  public byte btFullFrameFlag;
  
  public byte btQPKeyFrameOrMin;
  
  public byte btQPPFrameOrMax;
  
  public byte btQPRange;
  
  public byte btRCCalib;
  
  public byte btSceEnable;
  
  public byte btTargetDelay;
  
  public FHNP_EncodeVAdvanced_t() {}
  
  public FHNP_EncodeVAdvanced_t(byte paramByte1, byte paramByte2, byte paramByte3, byte paramByte4, byte paramByte5, byte paramByte6, byte paramByte7, byte paramByte8) {
    this.btTargetDelay = (byte)paramByte1;
    this.btSceEnable = (byte)paramByte2;
    this.btEntropyMode = (byte)paramByte3;
    this.btFullFrameFlag = (byte)paramByte4;
    this.btQPKeyFrameOrMin = (byte)paramByte5;
    this.btQPPFrameOrMax = (byte)paramByte6;
    this.btQPRange = (byte)paramByte7;
    this.btRCCalib = (byte)paramByte8;
  }
  
  public FHNP_EncodeVAdvanced_t(Pointer paramPointer) {
    super(paramPointer);
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { "btTargetDelay", "btSceEnable", "btEntropyMode", "btFullFrameFlag", "btQPKeyFrameOrMin", "btQPPFrameOrMax", "btQPRange", "btRCCalib" });
  }
  
  public static class ByReference extends FHNP_EncodeVAdvanced_t implements Structure.ByReference {}
  
  public static class ByValue extends FHNP_EncodeVAdvanced_t implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/FHNP_EncodeVAdvanced_t.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */