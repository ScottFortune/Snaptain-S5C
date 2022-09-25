package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class LPFHNP_Defence_EnventRec_t_struct extends Structure {
  public byte btEncID;
  
  public byte btRecAudio;
  
  public byte[] btRes = new byte[2];
  
  public int dwExtRecTime;
  
  public int dwPreRecTime;
  
  public int dwShotFrame;
  
  public int dwShotInterval;
  
  public LPFHNP_Defence_EnventRec_t_struct() {}
  
  public LPFHNP_Defence_EnventRec_t_struct(int paramInt1, int paramInt2, byte paramByte1, byte paramByte2, byte[] paramArrayOfbyte, int paramInt3, int paramInt4) {
    this.dwPreRecTime = paramInt1;
    this.dwExtRecTime = paramInt2;
    this.btEncID = (byte)paramByte1;
    this.btRecAudio = (byte)paramByte2;
    if (paramArrayOfbyte.length == this.btRes.length) {
      this.btRes = paramArrayOfbyte;
      this.dwShotInterval = paramInt3;
      this.dwShotFrame = paramInt4;
      return;
    } 
    throw new IllegalArgumentException("Wrong array size !");
  }
  
  public LPFHNP_Defence_EnventRec_t_struct(Pointer paramPointer) {
    super(paramPointer);
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { "dwPreRecTime", "dwExtRecTime", "btEncID", "btRecAudio", "btRes", "dwShotInterval", "dwShotFrame" });
  }
  
  public static class ByReference extends LPFHNP_Defence_EnventRec_t_struct implements Structure.ByReference {}
  
  public static class ByValue extends LPFHNP_Defence_EnventRec_t_struct implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/LPFHNP_Defence_EnventRec_t_struct.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */