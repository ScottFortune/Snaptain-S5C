package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class FHNP_EncodeAudio_t extends Structure {
  public byte btAudioFmt;
  
  public byte btAudioTrack;
  
  public byte btBitWidth;
  
  public byte btRes;
  
  public int dwDataLen;
  
  public int dwSampleRate;
  
  public FHNP_EncodeAudio_t() {}
  
  public FHNP_EncodeAudio_t(byte paramByte1, byte paramByte2, byte paramByte3, byte paramByte4, int paramInt1, int paramInt2) {
    this.btAudioFmt = (byte)paramByte1;
    this.btBitWidth = (byte)paramByte2;
    this.btAudioTrack = (byte)paramByte3;
    this.btRes = (byte)paramByte4;
    this.dwSampleRate = paramInt1;
    this.dwDataLen = paramInt2;
  }
  
  public FHNP_EncodeAudio_t(Pointer paramPointer) {
    super(paramPointer);
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { "btAudioFmt", "btBitWidth", "btAudioTrack", "btRes", "dwSampleRate", "dwDataLen" });
  }
  
  public static class ByReference extends FHNP_EncodeAudio_t implements Structure.ByReference {}
  
  public static class ByValue extends FHNP_EncodeAudio_t implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/FHNP_EncodeAudio_t.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */