package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class LPFHNP_VFrameHead_t_struct extends Structure {
  public byte btFrameType;
  
  public byte btRes;
  
  public byte[] btRes2 = new byte[4];
  
  public byte btRestartFlag;
  
  public byte btVideoFormat;
  
  public long ullTimeStamp;
  
  public short wHeight;
  
  public short wWidth;
  
  public LPFHNP_VFrameHead_t_struct() {}
  
  public LPFHNP_VFrameHead_t_struct(byte paramByte1, byte paramByte2, byte paramByte3, byte paramByte4, short paramShort1, short paramShort2, long paramLong, byte[] paramArrayOfbyte) {
    this.btFrameType = (byte)paramByte1;
    this.btVideoFormat = (byte)paramByte2;
    this.btRestartFlag = (byte)paramByte3;
    this.btRes = (byte)paramByte4;
    this.wWidth = (short)paramShort1;
    this.wHeight = (short)paramShort2;
    this.ullTimeStamp = paramLong;
    if (paramArrayOfbyte.length == this.btRes2.length) {
      this.btRes2 = paramArrayOfbyte;
      return;
    } 
    throw new IllegalArgumentException("Wrong array size !");
  }
  
  public LPFHNP_VFrameHead_t_struct(Pointer paramPointer) {
    super(paramPointer);
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { "btFrameType", "btVideoFormat", "btRestartFlag", "btRes", "wWidth", "wHeight", "ullTimeStamp", "btRes2" });
  }
  
  public static class ByReference extends LPFHNP_VFrameHead_t_struct implements Structure.ByReference {}
  
  public static class ByValue extends LPFHNP_VFrameHead_t_struct implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/LPFHNP_VFrameHead_t_struct.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */