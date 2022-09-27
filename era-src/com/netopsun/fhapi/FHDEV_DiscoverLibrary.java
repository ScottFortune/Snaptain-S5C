package com.netopsun.fhapi;

import com.sun.jna.Callback;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.sun.jna.Pointer;
import java.nio.ByteBuffer;

public interface FHDEV_DiscoverLibrary extends Library {
  public static final FHDEV_DiscoverLibrary INSTANCE;
  
  public static final String JNA_LIBRARY_NAME = "FHDEV_Discover";
  
  public static final NativeLibrary JNA_NATIVE_LIB = NativeLibrary.getInstance("FHDEV_Discover");
  
  static {
    INSTANCE = (FHDEV_DiscoverLibrary)Native.loadLibrary("FHDEV_Discover", FHDEV_DiscoverLibrary.class);
  }
  
  int FHDEV_DS_Cleanup();
  
  int FHDEV_DS_CloseFindDevState(Pointer paramPointer);
  
  Pointer FHDEV_DS_FindDevState(int paramInt);
  
  int FHDEV_DS_FindNextDevState(Pointer paramPointer, LPFHDS_DevState_t_struct paramLPFHDS_DevState_t_struct);
  
  int FHDEV_DS_GetSDKBuildVersion();
  
  int FHDEV_DS_GetSDKVersion();
  
  int FHDEV_DS_Init();
  
  @Deprecated
  int FHDEV_DS_InitEx(Pointer paramPointer);
  
  int FHDEV_DS_InitEx(ByteBuffer paramByteBuffer);
  
  int FHDEV_DS_ModifyNetAddr(LPFHDS_ModifyNetAddr_t_struct paramLPFHDS_ModifyNetAddr_t_struct, int paramInt);
  
  int FHDEV_DS_RegisterDevStateFun(int paramInt, fDevStateCallBack paramfDevStateCallBack, Pointer paramPointer);
  
  int FHDEV_DS_SearchDevice(int paramInt);
  
  public static interface fDevStateCallBack extends Callback {
    void apply(LPFHDS_DevState_t_struct param1LPFHDS_DevState_t_struct, Pointer param1Pointer);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/FHDEV_DiscoverLibrary.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */