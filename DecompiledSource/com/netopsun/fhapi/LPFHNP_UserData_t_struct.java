package com.netopsun.fhapi;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class LPFHNP_UserData_t_struct extends Structure {
  public byte[] btData = new byte[2048];
  
  public int dwSize;
  
  public LPFHNP_UserData_t_struct() {}
  
  public LPFHNP_UserData_t_struct(int paramInt, byte[] paramArrayOfbyte) {
    this.dwSize = paramInt;
    if (paramArrayOfbyte.length == this.btData.length) {
      this.btData = paramArrayOfbyte;
      return;
    } 
    throw new IllegalArgumentException("Wrong array size !");
  }
  
  public LPFHNP_UserData_t_struct(Pointer paramPointer) {
    super(paramPointer);
  }
  
  protected List<String> getFieldOrder() {
    return Arrays.asList(new String[] { "dwSize", "btData" });
  }
  
  public static class ByReference extends LPFHNP_UserData_t_struct implements Structure.ByReference {}
  
  public static class ByValue extends LPFHNP_UserData_t_struct implements Structure.ByValue {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/LPFHNP_UserData_t_struct.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */