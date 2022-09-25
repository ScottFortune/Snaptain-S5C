package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class LPFHNP_RecPic_Notify_t_struct extends Structure {
  public byte btLockFlag;
  
  public byte btRecOrPic;
  
  public byte[] btReserve = new byte[2];
  
  public int dwType;
  
  public long ullDataSize;
  
  public long ullStartTime;
  
  public long ullStopTime;
  
  public LPFHNP_RecPic_Notify_t_struct() {}
  
  public LPFHNP_RecPic_Notify_t_struct(byte paramByte1, byte paramByte2, byte[] paramArrayOfbyte, int paramInt, long paramLong1, long paramLong2, long paramLong3) {
    this.btRecOrPic = (byte)paramByte1;
    this.btLockFlag = (byte)paramByte2;
    if (paramArrayOfbyte.length == this.btReserve.length) {
      this.btReserve = paramArrayOfbyte;
      this.dwType = paramInt;
      this.ullStartTime = paramLong1;
      this.ullStopTime = paramLong2;
      this.ullDataSize = paramLong3;
      return;
    } 
    throw new IllegalArgumentException("Wrong array size !");
  }
  
  public LPFHNP_RecPic_Notify_t_struct(Pointer paramPointer) {
    super(paramPointer);
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { "btRecOrPic", "btLockFlag", "btReserve", "dwType", "ullStartTime", "ullStopTime", "ullDataSize" });
  }
  
  public static class ByReference extends LPFHNP_RecPic_Notify_t_struct implements Structure.ByReference {}
  
  public static class ByValue extends LPFHNP_RecPic_Notify_t_struct implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/LPFHNP_RecPic_Notify_t_struct.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */