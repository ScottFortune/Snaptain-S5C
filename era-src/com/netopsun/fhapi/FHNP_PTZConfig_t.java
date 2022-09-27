package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class FHNP_PTZConfig_t extends Structure {
  public byte btHoppo;
  
  public byte btOperateRep;
  
  public byte btPresetRep;
  
  public byte[] btRes = new byte[2];
  
  public byte btVoppo;
  
  public byte[] chProtocolName = new byte[32];
  
  public int dwBaudRate;
  
  public int dwStepSpeed;
  
  public short wDecoderAddress;
  
  public FHNP_PTZConfig_t() {}
  
  public FHNP_PTZConfig_t(Pointer paramPointer) {
    super(paramPointer);
  }
  
  public FHNP_PTZConfig_t(byte[] paramArrayOfbyte1, int paramInt1, int paramInt2, byte paramByte1, byte paramByte2, short paramShort, byte paramByte3, byte paramByte4, byte[] paramArrayOfbyte2) {
    if (paramArrayOfbyte1.length == this.chProtocolName.length) {
      this.chProtocolName = paramArrayOfbyte1;
      this.dwBaudRate = paramInt1;
      this.dwStepSpeed = paramInt2;
      this.btHoppo = (byte)paramByte1;
      this.btVoppo = (byte)paramByte2;
      this.wDecoderAddress = (short)paramShort;
      this.btOperateRep = (byte)paramByte3;
      this.btPresetRep = (byte)paramByte4;
      if (paramArrayOfbyte2.length == this.btRes.length) {
        this.btRes = paramArrayOfbyte2;
        return;
      } 
      throw new IllegalArgumentException("Wrong array size !");
    } 
    throw new IllegalArgumentException("Wrong array size !");
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { "chProtocolName", "dwBaudRate", "dwStepSpeed", "btHoppo", "btVoppo", "wDecoderAddress", "btOperateRep", "btPresetRep", "btRes" });
  }
  
  public static class ByReference extends FHNP_PTZConfig_t implements Structure.ByReference {}
  
  public static class ByValue extends FHNP_PTZConfig_t implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/FHNP_PTZConfig_t.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */