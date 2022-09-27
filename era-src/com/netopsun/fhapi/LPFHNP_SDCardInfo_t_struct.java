package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class LPFHNP_SDCardInfo_t_struct extends Structure {
  public byte[] btRes = new byte[3];
  
  public byte btState;
  
  public long ullTotalSize;
  
  public long ullUsedSize;
  
  public LPFHNP_SDCardInfo_t_struct() {}
  
  public LPFHNP_SDCardInfo_t_struct(byte paramByte, byte[] paramArrayOfbyte, long paramLong1, long paramLong2) {
    this.btState = (byte)paramByte;
    if (paramArrayOfbyte.length == this.btRes.length) {
      this.btRes = paramArrayOfbyte;
      this.ullTotalSize = paramLong1;
      this.ullUsedSize = paramLong2;
      return;
    } 
    throw new IllegalArgumentException("Wrong array size !");
  }
  
  public LPFHNP_SDCardInfo_t_struct(Pointer paramPointer) {
    super(paramPointer);
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { "btState", "btRes", "ullTotalSize", "ullUsedSize" });
  }
  
  public static class ByReference extends LPFHNP_SDCardInfo_t_struct implements Structure.ByReference {}
  
  public static class ByValue extends LPFHNP_SDCardInfo_t_struct implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/LPFHNP_SDCardInfo_t_struct.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */