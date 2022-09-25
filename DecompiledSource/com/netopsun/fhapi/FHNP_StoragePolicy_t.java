package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class FHNP_StoragePolicy_t extends Structure {
  public byte btCleanData;
  
  public byte btRecycleFlag;
  
  public byte[] btRes = new byte[2];
  
  public int dwFullThreshold;
  
  public int dwMaxHour;
  
  public FHNP_StoragePolicy_t() {}
  
  public FHNP_StoragePolicy_t(int paramInt1, int paramInt2, byte paramByte1, byte paramByte2, byte[] paramArrayOfbyte) {
    this.dwMaxHour = paramInt1;
    this.dwFullThreshold = paramInt2;
    this.btRecycleFlag = (byte)paramByte1;
    this.btCleanData = (byte)paramByte2;
    if (paramArrayOfbyte.length == this.btRes.length) {
      this.btRes = paramArrayOfbyte;
      return;
    } 
    throw new IllegalArgumentException("Wrong array size !");
  }
  
  public FHNP_StoragePolicy_t(Pointer paramPointer) {
    super(paramPointer);
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { "dwMaxHour", "dwFullThreshold", "btRecycleFlag", "btCleanData", "btRes" });
  }
  
  public static class ByReference extends FHNP_StoragePolicy_t implements Structure.ByReference {}
  
  public static class ByValue extends FHNP_StoragePolicy_t implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/FHNP_StoragePolicy_t.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */