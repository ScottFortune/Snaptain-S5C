package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class FHNP_EncodeVAdvancedV2_t extends Structure {
  public byte btEntropyMode;
  
  public byte btFullFrameFlag;
  
  public byte btQPInit;
  
  public byte btQPKeyFrameOrMin;
  
  public byte btQPPFrameOrMax;
  
  public byte btRCCalib;
  
  public byte[] btReserve = new byte[8];
  
  public byte btSceEnable;
  
  public byte btTargetDelay;
  
  public int dwLevel;
  
  public FHNP_EncodeVAdvancedV2_t() {}
  
  public FHNP_EncodeVAdvancedV2_t(Pointer paramPointer) {
    super(paramPointer);
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { "dwLevel", "btTargetDelay", "btSceEnable", "btEntropyMode", "btFullFrameFlag", "btQPKeyFrameOrMin", "btQPPFrameOrMax", "btQPInit", "btRCCalib", "btReserve" });
  }
  
  public static class ByReference extends FHNP_EncodeVAdvancedV2_t implements Structure.ByReference {}
  
  public static class ByValue extends FHNP_EncodeVAdvancedV2_t implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/FHNP_EncodeVAdvancedV2_t.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */