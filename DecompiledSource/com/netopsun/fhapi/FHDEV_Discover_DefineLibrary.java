package com.netopsun.fhapi;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

public interface FHDEV_Discover_DefineLibrary extends Library {
  public static final int FHFALSE = 0;
  
  public static final int FHTRUE = 1;
  
  public static final FHDEV_Discover_DefineLibrary INSTANCE;
  
  public static final String JNA_LIBRARY_NAME = "FHDEV_Discover_Define";
  
  public static final NativeLibrary JNA_NATIVE_LIB = NativeLibrary.getInstance("FHDEV_Discover_Define");
  
  public static final int NULL = 0;
  
  static {
    INSTANCE = (FHDEV_Discover_DefineLibrary)Native.loadLibrary("FHDEV_Discover_Define", FHDEV_Discover_DefineLibrary.class);
  }
  
  public static interface FHNPEN_DevType_e {
    public static final int FHNPEN_DType_8610 = 1;
    
    public static final int FHNPEN_DType_8620 = 8;
    
    public static final int FHNPEN_DType_8630 = 10;
    
    public static final int FHNPEN_DType_8810 = 7;
    
    public static final int FHNPEN_DType_8830 = 9;
    
    public static final int FHNPEN_DType_AllDev = 255;
    
    public static final int FHNPEN_DType_Client = 0;
    
    public static final int FHNPEN_DType_DVR = 3;
    
    public static final int FHNPEN_DType_DVS = 6;
    
    public static final int FHNPEN_DType_Decoder = 2;
    
    public static final int FHNPEN_DType_Genl = 11;
    
    public static final int FHNPEN_DType_NVR = 4;
    
    public static final int FHNPEN_DType_NVS = 5;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/FHDEV_Discover_DefineLibrary.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */