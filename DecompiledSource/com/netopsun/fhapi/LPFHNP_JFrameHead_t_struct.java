package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class LPFHNP_JFrameHead_t_struct extends Structure {
  public byte btFrameType;
  
  public byte[] btRes = new byte[4];
  
  public long ullTimeStamp;
  
  public LPFHNP_JFrameHead_t_struct() {}
  
  public LPFHNP_JFrameHead_t_struct(byte paramByte, long paramLong, byte[] paramArrayOfbyte) {
    this.btFrameType = (byte)paramByte;
    this.ullTimeStamp = paramLong;
    if (paramArrayOfbyte.length == this.btRes.length) {
      this.btRes = paramArrayOfbyte;
      return;
    } 
    throw new IllegalArgumentException("Wrong array size !");
  }
  
  public LPFHNP_JFrameHead_t_struct(Pointer paramPointer) {
    super(paramPointer);
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { "btFrameType", "ullTimeStamp", "btRes" });
  }
  
  public static class ByReference extends LPFHNP_JFrameHead_t_struct implements Structure.ByReference {}
  
  public static class ByValue extends LPFHNP_JFrameHead_t_struct implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/LPFHNP_JFrameHead_t_struct.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */