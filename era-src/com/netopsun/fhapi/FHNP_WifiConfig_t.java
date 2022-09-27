package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class FHNP_WifiConfig_t extends Structure {
  public byte btPSKLen;
  
  public byte[] btRes = new byte[3];
  
  public int dwChannel;
  
  public int iMode;
  
  public int iType;
  
  public byte[] sDummy = new byte[3];
  
  public byte[] sPSK = new byte[128];
  
  public byte[] sSSID = new byte[33];
  
  public FHNP_WifiConfig_t() {}
  
  public FHNP_WifiConfig_t(Pointer paramPointer) {
    super(paramPointer);
  }
  
  public FHNP_WifiConfig_t(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfbyte3, byte paramByte, byte[] paramArrayOfbyte4) {
    if (paramArrayOfbyte1.length == this.sSSID.length) {
      this.sSSID = paramArrayOfbyte1;
      if (paramArrayOfbyte2.length == this.sDummy.length) {
        this.sDummy = paramArrayOfbyte2;
        this.dwChannel = paramInt1;
        this.iMode = paramInt2;
        this.iType = paramInt3;
        if (paramArrayOfbyte3.length == this.sPSK.length) {
          this.sPSK = paramArrayOfbyte3;
          this.btPSKLen = (byte)paramByte;
          if (paramArrayOfbyte4.length == this.btRes.length) {
            this.btRes = paramArrayOfbyte4;
            return;
          } 
          throw new IllegalArgumentException("Wrong array size !");
        } 
        throw new IllegalArgumentException("Wrong array size !");
      } 
      throw new IllegalArgumentException("Wrong array size !");
    } 
    throw new IllegalArgumentException("Wrong array size !");
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { "sSSID", "sDummy", "dwChannel", "iMode", "iType", "sPSK", "btPSKLen", "btRes" });
  }
  
  public static class ByReference extends FHNP_WifiConfig_t implements Structure.ByReference {}
  
  public static class ByValue extends FHNP_WifiConfig_t implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/FHNP_WifiConfig_t.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */