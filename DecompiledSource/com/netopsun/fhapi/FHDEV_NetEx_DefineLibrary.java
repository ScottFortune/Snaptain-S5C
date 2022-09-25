package com.netopsun.fhapi;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

public interface FHDEV_NetEx_DefineLibrary extends Library {
  public static final int FHNPEN_ALM_CDAlarm = 85;
  
  public static final int FHNPEN_ALM_CleanAlarm = 89;
  
  public static final int FHNPEN_ALM_Defense = 90;
  
  public static final int FHNPEN_ALM_ManualAlarm = 87;
  
  public static final int FHNPEN_ALM_NoAccess = 82;
  
  public static final int FHNPEN_CFGType_ChannelName = 35;
  
  public static final int FHNPEN_CFGType_CruiseConfig = 32;
  
  public static final int FHNPEN_CFGType_EncodeConfig = 45;
  
  public static final int FHNPEN_CFGType_EncodeWaterMark = 43;
  
  public static final int FHNPEN_CFGType_EncryptConfig = 47;
  
  public static final int FHNPEN_CFGType_GpioInName = 36;
  
  public static final int FHNPEN_CFGType_GpioOutName = 37;
  
  public static final int FHNPEN_CFGType_IRLight = 53;
  
  public static final int FHNPEN_CFGType_ISPConfig = 34;
  
  public static final int FHNPEN_CFGType_PTZConfig = 31;
  
  public static final int FHNPEN_ERR_AoDataSend = 25;
  
  public static final int FHNPEN_ERR_Formating = 18;
  
  public static final int FHNPEN_ERR_MaxPlayerPort = 24;
  
  public static final int FHNPEN_ERR_MaxUserNum = 23;
  
  public static final int FHNPEN_ERR_NoneRecordData = 21;
  
  public static final int FHNPEN_ERR_NoneStorageDevice = 22;
  
  public static final int FHNPEN_ERR_Recording = 19;
  
  public static final int FHNPEN_ERR_UserBeKicked = 9;
  
  public static final int FHNPEN_OPT_DownloadOver = 129;
  
  public static final int FHNPEN_OPT_PlaybackOver = 128;
  
  public static final int FHNPEN_ST_FRAME_VH264_AAAC = 3;
  
  public static final int FHNPEN_SYS_ChangeChanName = 12;
  
  public static final int FHNPEN_SYS_ChangeDevName = 11;
  
  public static final int FHNPEN_SYS_ChangeTime = 8;
  
  public static final int FHNPEN_SYS_NetBlock = 10;
  
  public static final int FHNPEN_TransMode_MCAST_UDP_TS = 5;
  
  public static final int FHNPEN_TransMode_RTP = 2;
  
  public static final int FHNPEN_TransMode_RTP_TS = 4;
  
  public static final int FHNPEN_TransMode_RTSP_RTP_TS = 7;
  
  public static final int FHNPEN_TransMode_RTSP_UDP_TS = 6;
  
  public static final int FHNPEN_TransMode_UDP = 1;
  
  public static final int FHNPEN_TransMode_UDP_TS = 3;
  
  public static final int FHNP_MACRO_LOGLEN_MAX = 256;
  
  public static final int FHNP_MACRO_PRESET_MAX = 256;
  
  public static final FHDEV_NetEx_DefineLibrary INSTANCE;
  
  public static final String JNA_LIBRARY_NAME = "FHDEV_NetEx_Define";
  
  public static final NativeLibrary JNA_NATIVE_LIB = NativeLibrary.getInstance("FHDEV_NetEx_Define");
  
  static {
    INSTANCE = (FHDEV_NetEx_DefineLibrary)Native.loadLibrary("FHDEV_NetEx_Define", FHDEV_NetEx_DefineLibrary.class);
  }
  
  public static interface FHNPEN_BaseDataType_e {
    public static final int FHNPEN_BDT_Picture = 1;
    
    public static final int FHNPEN_BDT_Record = 0;
  }
  
  public static interface FHNPEN_DownloadCtrl_e {
    public static final int FHNPEN_DLCTRL_Pause = 2;
    
    public static final int FHNPEN_DLCTRL_Start = 1;
    
    public static final int FHNPEN_DLCTRL_Stop = 3;
  }
  
  public static interface FHNPEN_PTZCmd_e {
    public static final int FHNPEN_PTZCMD_BEGINRECLOCUS = 23;
    
    public static final int FHNPEN_PTZCMD_CAMERAOFF = 27;
    
    public static final int FHNPEN_PTZCMD_CAMERAON = 26;
    
    public static final int FHNPEN_PTZCMD_DDIAPHRAGML = 12;
    
    public static final int FHNPEN_PTZCMD_DELCRUISEPOINT = 22;
    
    public static final int FHNPEN_PTZCMD_DELETECRUISE = 20;
    
    public static final int FHNPEN_PTZCMD_DELPREPOS = 16;
    
    public static final int FHNPEN_PTZCMD_DIAPHRAGMB = 13;
    
    public static final int FHNPEN_PTZCMD_DOPREPOS = 14;
    
    public static final int FHNPEN_PTZCMD_DOWN = 1;
    
    public static final int FHNPEN_PTZCMD_FOCUSFAR = 11;
    
    public static final int FHNPEN_PTZCMD_FOCUSNEAR = 10;
    
    public static final int FHNPEN_PTZCMD_IOOFF = 31;
    
    public static final int FHNPEN_PTZCMD_IOON = 30;
    
    public static final int FHNPEN_PTZCMD_LEFT = 2;
    
    public static final int FHNPEN_PTZCMD_LEFTDOWN = 5;
    
    public static final int FHNPEN_PTZCMD_LEFTUP = 4;
    
    public static final int FHNPEN_PTZCMD_LIGHTCLOSE = 32;
    
    public static final int FHNPEN_PTZCMD_LIGHTOPEN = 33;
    
    public static final int FHNPEN_PTZCMD_RIGHT = 3;
    
    public static final int FHNPEN_PTZCMD_RIGHTDOWN = 7;
    
    public static final int FHNPEN_PTZCMD_RIGHTUP = 6;
    
    public static final int FHNPEN_PTZCMD_RUNLOCUN = 25;
    
    public static final int FHNPEN_PTZCMD_SETCRUISEPOINT = 21;
    
    public static final int FHNPEN_PTZCMD_SETPREPOS = 15;
    
    public static final int FHNPEN_PTZCMD_STARTDOCRUISE = 18;
    
    public static final int FHNPEN_PTZCMD_STOP = 17;
    
    public static final int FHNPEN_PTZCMD_STOPCRUISE = 19;
    
    public static final int FHNPEN_PTZCMD_STOPRECLOCUS = 24;
    
    public static final int FHNPEN_PTZCMD_UP = 0;
    
    public static final int FHNPEN_PTZCMD_WIPEROFF = 29;
    
    public static final int FHNPEN_PTZCMD_WIPERON = 28;
    
    public static final int FHNPEN_PTZCMD_ZOOMLONG = 9;
    
    public static final int FHNPEN_PTZCMD_ZOOMSHORT = 8;
  }
  
  public static interface FHNPEN_RecStreamType_e {
    public static final int FHNPEN_RST_AVI = 2;
    
    public static final int FHNPEN_RST_ES = 3;
    
    public static final int FHNPEN_RST_FRAME = 0;
    
    public static final int FHNPEN_RST_TS = 1;
  }
  
  public static interface FHNP_LogType_e {
    public static final int FHNP_LogType_Alarm = 3;
    
    public static final int FHNP_LogType_All = 0;
    
    public static final int FHNP_LogType_Operate = 2;
    
    public static final int FHNP_LogType_System = 1;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/FHDEV_NetEx_DefineLibrary.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */