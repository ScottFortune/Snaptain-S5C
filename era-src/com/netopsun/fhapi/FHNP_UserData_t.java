package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class FHNP_UserData_t extends Structure {
  public byte[] btData = new byte[2048];
  
  public int dwSize;
  
  public FHNP_UserData_t() {}
  
  public FHNP_UserData_t(int paramInt, byte[] paramArrayOfbyte) {
    this.dwSize = paramInt;
    if (paramArrayOfbyte.length == this.btData.length) {
      this.btData = paramArrayOfbyte;
      return;
    } 
    throw new IllegalArgumentException("Wrong array size !");
  }
  
  public FHNP_UserData_t(Pointer paramPointer) {
    super(paramPointer);
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { "dwSize", "btData" });
  }
  
  public static class ByReference extends FHNP_UserData_t implements Structure.ByReference {}
  
  public static class ByValue extends FHNP_UserData_t implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/FHNP_UserData_t.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */