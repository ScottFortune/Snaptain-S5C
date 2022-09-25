package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class LPFHNP_AFrameHead_t_struct extends Structure {
  public byte btAudioFormat;
  
  public byte btBitWidth;
  
  public byte btFrameType;
  
  public byte[] btRes = new byte[4];
  
  public byte btTrace;
  
  public int dwDataLen;
  
  public int dwSampleRate;
  
  public long ullTimeStamp;
  
  public LPFHNP_AFrameHead_t_struct() {}
  
  public LPFHNP_AFrameHead_t_struct(byte paramByte1, byte paramByte2, byte paramByte3, byte paramByte4, long paramLong, int paramInt1, int paramInt2, byte[] paramArrayOfbyte) {
    this.btFrameType = (byte)paramByte1;
    this.btAudioFormat = (byte)paramByte2;
    this.btBitWidth = (byte)paramByte3;
    this.btTrace = (byte)paramByte4;
    this.ullTimeStamp = paramLong;
    this.dwSampleRate = paramInt1;
    this.dwDataLen = paramInt2;
    if (paramArrayOfbyte.length == this.btRes.length) {
      this.btRes = paramArrayOfbyte;
      return;
    } 
    throw new IllegalArgumentException("Wrong array size !");
  }
  
  public LPFHNP_AFrameHead_t_struct(Pointer paramPointer) {
    super(paramPointer);
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { "btFrameType", "btAudioFormat", "btBitWidth", "btTrace", "ullTimeStamp", "dwSampleRate", "dwDataLen", "btRes" });
  }
  
  public static class ByReference extends LPFHNP_AFrameHead_t_struct implements Structure.ByReference {}
  
  public static class ByValue extends LPFHNP_AFrameHead_t_struct implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/LPFHNP_AFrameHead_t_struct.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */