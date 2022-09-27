package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class FHNP_DeviceConfig_t extends Structure {
  public byte[] btRes = new byte[3];
  
  public byte btVideoInputMode;
  
  public byte[] chDeviceName = new byte[128];
  
  public int iDeviceIndex;
  
  public FHNP_DeviceConfig_t() {}
  
  public FHNP_DeviceConfig_t(Pointer paramPointer) {
    super(paramPointer);
  }
  
  public FHNP_DeviceConfig_t(byte[] paramArrayOfbyte1, int paramInt, byte paramByte, byte[] paramArrayOfbyte2) {
    if (paramArrayOfbyte1.length == this.chDeviceName.length) {
      this.chDeviceName = paramArrayOfbyte1;
      this.iDeviceIndex = paramInt;
      this.btVideoInputMode = (byte)paramByte;
      if (paramArrayOfbyte2.length == this.btRes.length) {
        this.btRes = paramArrayOfbyte2;
        return;
      } 
      throw new IllegalArgumentException("Wrong array size !");
    } 
    throw new IllegalArgumentException("Wrong array size !");
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { "chDeviceName", "iDeviceIndex", "btVideoInputMode", "btRes" });
  }
  
  public static class ByReference extends FHNP_DeviceConfig_t implements Structure.ByReference {}
  
  public static class ByValue extends FHNP_DeviceConfig_t implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/FHNP_DeviceConfig_t.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */