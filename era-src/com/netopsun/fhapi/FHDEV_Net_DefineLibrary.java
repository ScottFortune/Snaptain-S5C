package com.netopsun.fhapi;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

public interface FHDEV_Net_DefineLibrary extends Library {
  public static final int FHNPEN_ALM_GpioAlarm = 83;
  
  public static final int FHNPEN_ALM_IPConflict = 88;
  
  public static final int FHNPEN_ALM_MDAlarm = 84;
  
  public static final int FHNPEN_ALM_SDError = 80;
  
  public static final int FHNPEN_ALM_SDFull = 81;
  
  public static final int FHNPEN_ALM_VideoLost = 86;
  
  public static final int FHNPEN_ALM_WritePicErr = 92;
  
  public static final int FHNPEN_ALM_WriteRecErr = 91;
  
  public static final int FHNPEN_CFGType_AudioDuplexConfig = 46;
  
  public static final int FHNPEN_CFGType_AudioEffect = 39;
  
  public static final int FHNPEN_CFGType_AutoRebootConfig = 14;
  
  public static final int FHNPEN_CFGType_AuxDevInfo = 59;
  
  public static final int FHNPEN_CFGType_BGMAttr = 63;
  
  public static final int FHNPEN_CFGType_Brightness = 49;
  
  public static final int FHNPEN_CFGType_Contrast = 50;
  
  public static final int FHNPEN_CFGType_Custom = 60;
  
  public static final int FHNPEN_CFGType_CustomConfig = 54;
  
  public static final int FHNPEN_CFGType_DSTConfig = 7;
  
  public static final int FHNPEN_CFGType_DefenceCD = 19;
  
  public static final int FHNPEN_CFGType_DefenceClearAlarm = 26;
  
  public static final int FHNPEN_CFGType_DefenceConfig = 27;
  
  public static final int FHNPEN_CFGType_DefenceEventRec = 23;
  
  public static final int FHNPEN_CFGType_DefenceGpioIn = 21;
  
  public static final int FHNPEN_CFGType_DefenceGpioOut = 24;
  
  public static final int FHNPEN_CFGType_DefenceMD = 18;
  
  public static final int FHNPEN_CFGType_DefenceOtherIn = 22;
  
  public static final int FHNPEN_CFGType_DefenceOtherOut = 25;
  
  public static final int FHNPEN_CFGType_DefenceVideoLost = 20;
  
  public static final int FHNPEN_CFGType_EMailConfig = 13;
  
  public static final int FHNPEN_CFGType_EncodeAudio = 4;
  
  public static final int FHNPEN_CFGType_EncodeCover = 2;
  
  public static final int FHNPEN_CFGType_EncodeJpeg = 3;
  
  public static final int FHNPEN_CFGType_EncodeMode = 61;
  
  public static final int FHNPEN_CFGType_EncodeOSD = 1;
  
  public static final int FHNPEN_CFGType_EncodeOSDEx = 38;
  
  public static final int FHNPEN_CFGType_EncodeOSDMore = 65;
  
  public static final int FHNPEN_CFGType_EncodeVAdvanced = 44;
  
  public static final int FHNPEN_CFGType_EncodeVideo = 42;
  
  public static final int FHNPEN_CFGType_Hue = 58;
  
  public static final int FHNPEN_CFGType_IOAdvanced = 57;
  
  public static final int FHNPEN_CFGType_IOStatus = 56;
  
  public static final int FHNPEN_CFGType_KeyPFrameInterval = 64;
  
  public static final int FHNPEN_CFGType_MDConfig = 40;
  
  public static final int FHNPEN_CFGType_MirrorCtrl = 55;
  
  public static final int FHNPEN_CFGType_NTPConfig = 8;
  
  public static final int FHNPEN_CFGType_NetConfig = 11;
  
  public static final int FHNPEN_CFGType_OSDZoom = 41;
  
  public static final int FHNPEN_CFGType_PicturePlan = 17;
  
  public static final int FHNPEN_CFGType_PreRecTime = 48;
  
  public static final int FHNPEN_CFGType_RecordPlan = 16;
  
  public static final int FHNPEN_CFGType_RemoteInIOTC = 29;
  
  public static final int FHNPEN_CFGType_Saturation = 51;
  
  public static final int FHNPEN_CFGType_SensorOutWHFr = 67;
  
  public static final int FHNPEN_CFGType_SerialPortConfig = 12;
  
  public static final int FHNPEN_CFGType_ServerPort = 30;
  
  public static final int FHNPEN_CFGType_Sharpness = 52;
  
  public static final int FHNPEN_CFGType_SmartEncAttr = 62;
  
  public static final int FHNPEN_CFGType_SndNetConfig = 66;
  
  public static final int FHNPEN_CFGType_StoragePolicy = 15;
  
  public static final int FHNPEN_CFGType_SystemConfig = 6;
  
  public static final int FHNPEN_CFGType_SystemTime = 9;
  
  public static final int FHNPEN_CFGType_TimeZone = 10;
  
  public static final int FHNPEN_CFGType_V2_Base = 1000;
  
  public static final int FHNPEN_CFGType_V2_DDNSTest = 1018;
  
  public static final int FHNPEN_CFGType_V2_DSTConfig = 1010;
  
  public static final int FHNPEN_CFGType_V2_DefenceVideoPlan = 1040;
  
  public static final int FHNPEN_CFGType_V2_EmailTest = 1022;
  
  public static final int FHNPEN_CFGType_V2_EncodeCover = 1003;
  
  public static final int FHNPEN_CFGType_V2_EncodeJpeg = 1009;
  
  public static final int FHNPEN_CFGType_V2_EncodeOSDRotate = 1002;
  
  public static final int FHNPEN_CFGType_V2_EncodeVAdvanced = 1005;
  
  public static final int FHNPEN_CFGType_V2_EncodeVRefMacro = 1007;
  
  public static final int FHNPEN_CFGType_V2_EncodeVRotate = 1006;
  
  public static final int FHNPEN_CFGType_V2_EncodeVideo = 1004;
  
  public static final int FHNPEN_CFGType_V2_EncodeWaterMark = 1008;
  
  public static final int FHNPEN_CFGType_V2_NetConfig = 1017;
  
  public static final int FHNPEN_CFGType_V2_PPPOETest = 1019;
  
  public static final int FHNPEN_CFGType_V2_PicStreamConfig = 1027;
  
  public static final int FHNPEN_CFGType_V2_SceneConfig = 1049;
  
  public static final int FHNPEN_CFGType_V2_SysFuction = 1025;
  
  public static final int FHNPEN_CFGType_V2_UserData = 1026;
  
  public static final int FHNPEN_CFGType_VideoEffect = 5;
  
  public static final int FHNPEN_CFGType_WifiConfig = 33;
  
  public static final int FHNPEN_ERR_Authority = 5;
  
  public static final int FHNPEN_ERR_GetConfig = 2;
  
  public static final int FHNPEN_ERR_GetData = 3;
  
  public static final int FHNPEN_ERR_NotSupport = 20;
  
  public static final int FHNPEN_ERR_OutOfMemory = 11;
  
  public static final int FHNPEN_ERR_Parameter = 4;
  
  public static final int FHNPEN_ERR_Password = 7;
  
  public static final int FHNPEN_ERR_ReLogin = 8;
  
  public static final int FHNPEN_ERR_RecvOverTime = 12;
  
  public static final int FHNPEN_ERR_SDK_IllegalDevice = 2016;
  
  public static final int FHNPEN_ERR_SDK_Index = 2000;
  
  public static final int FHNPEN_ERR_SDK_MaxHandle = 2019;
  
  public static final int FHNPEN_ERR_SDK_NetBind = 2008;
  
  public static final int FHNPEN_ERR_SDK_NetConf = 2007;
  
  public static final int FHNPEN_ERR_SDK_NetConn = 2010;
  
  public static final int FHNPEN_ERR_SDK_NetConnTimeOut = 2017;
  
  public static final int FHNPEN_ERR_SDK_NetCreate = 2006;
  
  public static final int FHNPEN_ERR_SDK_NetException = 2009;
  
  public static final int FHNPEN_ERR_SDK_NetRecv = 2012;
  
  public static final int FHNPEN_ERR_SDK_NetSend = 2011;
  
  public static final int FHNPEN_ERR_SDK_NoHandle = 2003;
  
  public static final int FHNPEN_ERR_SDK_NoInit = 2001;
  
  public static final int FHNPEN_ERR_SDK_NoLogin = 2002;
  
  public static final int FHNPEN_ERR_SDK_NotSupport = 2015;
  
  public static final int FHNPEN_ERR_SDK_OpenFile = 2013;
  
  public static final int FHNPEN_ERR_SDK_OutOfMemory = 2014;
  
  public static final int FHNPEN_ERR_SDK_ParamIn = 2004;
  
  public static final int FHNPEN_ERR_SDK_RecvPacketSize = 2005;
  
  public static final int FHNPEN_ERR_SDK_Shoting = 2018;
  
  public static final int FHNPEN_ERR_SDK_Unknow = 2040;
  
  public static final int FHNPEN_ERR_SetConfig = 1;
  
  public static final int FHNPEN_ERR_UpgradeErr = 16;
  
  public static final int FHNPEN_ERR_Upgrade_DataError = 13;
  
  public static final int FHNPEN_ERR_Upgrade_OldVersion = 15;
  
  public static final int FHNPEN_ERR_Upgrade_SameVersion = 14;
  
  public static final int FHNPEN_ERR_Upgrade_WriteFlashFail = 17;
  
  public static final int FHNPEN_ERR_Upgrading = 10;
  
  public static final int FHNPEN_ERR_UserInexistence = 6;
  
  public static final int FHNPEN_OK = 0;
  
  public static final int FHNPEN_OPT_PicFinish = 132;
  
  public static final int FHNPEN_OPT_RecFinish = 131;
  
  public static final int FHNPEN_OPT_RecStart = 130;
  
  public static final int FHNPEN_SYS_BatteryV = 15;
  
  public static final int FHNPEN_SYS_ChangeIP = 3;
  
  public static final int FHNPEN_SYS_FlipMirror = 17;
  
  public static final int FHNPEN_SYS_IRLight = 16;
  
  public static final int FHNPEN_SYS_ImportConfig = 14;
  
  public static final int FHNPEN_SYS_Kick = 1;
  
  public static final int FHNPEN_SYS_OffLine = 2;
  
  public static final int FHNPEN_SYS_ReConnect = 13;
  
  public static final int FHNPEN_SYS_ReLogin = 9;
  
  public static final int FHNPEN_SYS_Reboot = 5;
  
  public static final int FHNPEN_SYS_Reset = 6;
  
  public static final int FHNPEN_SYS_ShotBtnPush = 18;
  
  public static final int FHNPEN_SYS_ShutDown = 4;
  
  public static final int FHNPEN_SYS_Upgrade = 7;
  
  public static final int FHNPEN_TransMode_TCP = 0;
  
  public static final int FHNP_MACRO_CHANNEL_MAX = 32;
  
  public static final int FHNP_MACRO_COVER_MAX = 4;
  
  public static final int FHNP_MACRO_DAILY_SCHED_ITEM_MAX = 3;
  
  public static final int FHNP_MACRO_DEVICENAMELEN_MAX = 128;
  
  public static final int FHNP_MACRO_DOMAINLEN_MAX = 128;
  
  public static final int FHNP_MACRO_EMAILRECV_MAX = 5;
  
  public static final int FHNP_MACRO_EMAIL_NAMELEN_MAX = 48;
  
  public static final int FHNP_MACRO_GOP_TH_NUM_MAX = 5;
  
  public static final int FHNP_MACRO_GPIOIN_MAX = 8;
  
  public static final int FHNP_MACRO_GPIOOUT_MAX = 8;
  
  public static final int FHNP_MACRO_IPLEN_MAX = 32;
  
  public static final int FHNP_MACRO_MD_MAX = 32;
  
  public static final int FHNP_MACRO_NAMELEN_MAX = 32;
  
  public static final int FHNP_MACRO_SCHEDULE_MAX = 8;
  
  public static final int FHNP_MACRO_UIDLEN_MAX = 24;
  
  public static final FHDEV_Net_DefineLibrary INSTANCE;
  
  public static final String JNA_LIBRARY_NAME = "FHDEV_Net_Define";
  
  public static final NativeLibrary JNA_NATIVE_LIB = NativeLibrary.getInstance("FHDEV_Net_Define");
  
  static {
    INSTANCE = (FHDEV_Net_DefineLibrary)Native.loadLibrary("FHDEV_Net_Define", FHDEV_Net_DefineLibrary.class);
  }
  
  public static interface FHNPEN_AutoRebootMode_e {
    public static final int FHNPEN_ARMode_Disable = 0;
    
    public static final int FHNPEN_ARMode_EveryDay = 1;
    
    public static final int FHNPEN_ARMode_EveryMonth = 3;
    
    public static final int FHNPEN_ARMode_EveryWeek = 2;
  }
  
  public static interface FHNPEN_ColorType_e {
    public static final int FHNPEN_COLTYPE_BLACK = 7;
    
    public static final int FHNPEN_COLTYPE_BLUE = 6;
    
    public static final int FHNPEN_COLTYPE_CYAN = 2;
    
    public static final int FHNPEN_COLTYPE_GREEN = 3;
    
    public static final int FHNPEN_COLTYPE_MAGENTA = 4;
    
    public static final int FHNPEN_COLTYPE_RED = 5;
    
    public static final int FHNPEN_COLTYPE_WHITE = 0;
    
    public static final int FHNPEN_COLTYPE_YELLOW = 1;
  }
  
  public static interface FHNPEN_CoverType_e {
    public static final int FHNPEN_CDTYPE_COLOR = 0;
    
    public static final int FHNPEN_CDTYPE_MOSAIC = 1;
  }
  
  public static interface FHNPEN_DSTMode_e {
    public static final int FHNPEN_DSTMode_Date = 0;
    
    public static final int FHNPEN_DSTMode_Week = 1;
  }
  
  public static interface FHNPEN_DevFlag_e {
    public static final int FHNPEN_DEVFLAG_FH8610 = 1;
    
    public static final int FHNPEN_DEVFLAG_FH8620 = 2;
    
    public static final int FHNPEN_DEVFLAG_FH8630 = 5;
    
    public static final int FHNPEN_DEVFLAG_FH8810 = 3;
    
    public static final int FHNPEN_DEVFLAG_FH8830 = 4;
    
    public static final int FHNPEN_DEVFLAG_FHGENL = 6;
    
    public static final int FHNPEN_DEVFLAG_UNKNOWN = 255;
  }
  
  public static interface FHNPEN_DurationState_e {
    public static final int FHNPEN_EState_Error = 4;
    
    public static final int FHNPEN_EState_Fail = 3;
    
    public static final int FHNPEN_EState_Finish = 1;
    
    public static final int FHNPEN_EState_Ing = 2;
  }
  
  public static interface FHNPEN_EncAudioBitWidth_e {
    public static final int FHNPEN_ABITWIDTH_16 = 1;
    
    public static final int FHNPEN_ABITWIDTH_32 = 2;
    
    public static final int FHNPEN_ABITWIDTH_8 = 0;
  }
  
  public static interface FHNPEN_EncAudioFormat_e {
    public static final int FHNPEN_AF_AAC = 6;
    
    public static final int FHNPEN_AF_AMR = 4;
    
    public static final int FHNPEN_AF_AMRDTX = 5;
    
    public static final int FHNPEN_AF_G711_ALAW = 0;
    
    public static final int FHNPEN_AF_G711_ULAW = 1;
    
    public static final int FHNPEN_AF_G726 = 3;
    
    public static final int FHNPEN_AF_PCM = 2;
  }
  
  public static interface FHNPEN_EncAudioSampleRate_e {
    public static final int FHNPEN_ASRATE_11025 = 11025;
    
    public static final int FHNPEN_ASRATE_16000 = 16000;
    
    public static final int FHNPEN_ASRATE_22050 = 22050;
    
    public static final int FHNPEN_ASRATE_24000 = 24000;
    
    public static final int FHNPEN_ASRATE_32000 = 32000;
    
    public static final int FHNPEN_ASRATE_44100 = 44100;
    
    public static final int FHNPEN_ASRATE_48000 = 48000;
    
    public static final int FHNPEN_ASRATE_8000 = 8000;
  }
  
  public static interface FHNPEN_EncAudioTrack_e {
    public static final int FHNPEN_ATRACK_MONO = 0;
    
    public static final int FHNPEN_ATRACK_STEREO = 1;
  }
  
  public static interface FHNPEN_EncBRCtrl_e {
    public static final int FHNPEN_BRCTRL_CBR = 1;
    
    public static final int FHNPEN_BRCTRL_FIXQP = 0;
    
    public static final int FHNPEN_BRCTRL_VBR = 2;
  }
  
  public static interface FHNPEN_EncDeinterlace_e {
    public static final int FHNPEN_DEINT_5TAP_MEDIUM = 2;
    
    public static final int FHNPEN_DEINT_5TAP_STRONG = 3;
    
    public static final int FHNPEN_DEINT_5TAP_SUPER_STRONG = 4;
    
    public static final int FHNPEN_DEINT_5TAP_WEAK = 1;
    
    public static final int FHNPEN_DEINT_ADAPT_AVG = 5;
    
    public static final int FHNPEN_DEINT_ADAPT_MEDIAN = 7;
    
    public static final int FHNPEN_DEINT_MA3 = 8;
    
    public static final int FHNPEN_DEINT_MEDIUM = 6;
    
    public static final int FHNPEN_DEINT_NONE = 0;
  }
  
  public static interface FHNPEN_EncDenoise_e {
    public static final int FHNPEN_DENOISE_NORMAL = 3;
    
    public static final int FHNPEN_DENOISE_OFF = 0;
    
    public static final int FHNPEN_DENOISE_STRONG = 4;
    
    public static final int FHNPEN_DENOISE_SUPER_STRONG = 5;
    
    public static final int FHNPEN_DENOISE_SUPER_WEAK = 1;
    
    public static final int FHNPEN_DENOISE_WEAK = 2;
  }
  
  public static interface FHNPEN_EncJpegQuality_e {
    public static final int FHNPEN_EJQ_BEST = 1;
    
    public static final int FHNPEN_EJQ_CUSTOM = 0;
    
    public static final int FHNPEN_EJQ_NORMAL = 2;
    
    public static final int FHNPEN_EJQ_WORST = 3;
  }
  
  public static interface FHNPEN_EncQuality_e {
    public static final int FHNPEN_EQ_BAD = 2;
    
    public static final int FHNPEN_EQ_BEST = 6;
    
    public static final int FHNPEN_EQ_BETTER = 5;
    
    public static final int FHNPEN_EQ_CUSTOM = 0;
    
    public static final int FHNPEN_EQ_GOOD = 4;
    
    public static final int FHNPEN_EQ_MAX = 7;
    
    public static final int FHNPEN_EQ_NORMAL = 3;
    
    public static final int FHNPEN_EQ_WORST = 1;
  }
  
  public static interface FHNPEN_EncResolution_e {
    public static final int FHEN_ER_1360x768 = 34;
    
    public static final int FHEN_ER_1440x904 = 33;
    
    public static final int FHEN_ER_1600x1200 = 26;
    
    public static final int FHEN_ER_1600x904 = 25;
    
    public static final int FHEN_ER_1792x1344 = 35;
    
    public static final int FHEN_ER_1920x1200 = 32;
    
    public static final int FHEN_ER_2048x1152 = 36;
    
    public static final int FHEN_ER_2304x1728 = 37;
    
    public static final int FHEN_ER_2560x1440 = 27;
    
    public static final int FHEN_ER_2560x1600 = 30;
    
    public static final int FHEN_ER_2560x1920 = 29;
    
    public static final int FHEN_ER_2560x1944 = 28;
    
    public static final int FHEN_ER_2592x1944 = 38;
    
    public static final int FHEN_ER_2816x2112 = 40;
    
    public static final int FHEN_ER_3072x1728 = 39;
    
    public static final int FHEN_ER_3072x2304 = 41;
    
    public static final int FHEN_ER_3264x2448 = 42;
    
    public static final int FHEN_ER_3456x2592 = 44;
    
    public static final int FHEN_ER_3600x2704 = 45;
    
    public static final int FHEN_ER_3840x2160 = 43;
    
    public static final int FHEN_ER_3840x2800 = 47;
    
    public static final int FHEN_ER_4000x3000 = 48;
    
    public static final int FHEN_ER_4096x2160 = 31;
    
    public static final int FHEN_ER_4096x2304 = 46;
    
    public static final int FHEN_ER_4096x3072 = 50;
    
    public static final int FHEN_ER_4608x2592 = 49;
    
    public static final int FHEN_ER_4800x3200 = 51;
    
    public static final int FHEN_ER_5120x2880 = 52;
    
    public static final int FHEN_ER_5120x3840 = 53;
    
    public static final int FHEN_ER_6400x4800 = 54;
    
    public static final int FHNPEN_ER_1024x1024 = 18;
    
    public static final int FHNPEN_ER_1072x1072 = 128;
    
    public static final int FHNPEN_ER_1080P = 7;
    
    public static final int FHNPEN_ER_1520x1520 = 17;
    
    public static final int FHNPEN_ER_1536x1536 = 16;
    
    public static final int FHNPEN_ER_2048x1520 = 14;
    
    public static final int FHNPEN_ER_2048x1536 = 13;
    
    public static final int FHNPEN_ER_2048x2048 = 15;
    
    public static final int FHNPEN_ER_384x384 = 24;
    
    public static final int FHNPEN_ER_4CIF = 3;
    
    public static final int FHNPEN_ER_4CIF_N = 21;
    
    public static final int FHNPEN_ER_512x512 = 19;
    
    public static final int FHNPEN_ER_640x360 = 11;
    
    public static final int FHNPEN_ER_640x480 = 9;
    
    public static final int FHNPEN_ER_720P = 6;
    
    public static final int FHNPEN_ER_768x768 = 23;
    
    public static final int FHNPEN_ER_960H = 5;
    
    public static final int FHNPEN_ER_960P = 8;
    
    public static final int FHNPEN_ER_960x960 = 12;
    
    public static final int FHNPEN_ER_CIF = 1;
    
    public static final int FHNPEN_ER_CIF_N = 20;
    
    public static final int FHNPEN_ER_D1 = 4;
    
    public static final int FHNPEN_ER_D1_N = 22;
    
    public static final int FHNPEN_ER_HALFD1 = 2;
    
    public static final int FHNPEN_ER_MAX = 129;
    
    public static final int FHNPEN_ER_QCIF = 0;
    
    public static final int FHNPEN_ER_QVGA = 10;
  }
  
  public static interface FHNPEN_EncVideoFormat_e {
    public static final int FHNPEN_VF_H264 = 0;
  }
  
  public static interface FHNPEN_ExceptionType_e {
    public static final int FHNPEN_ExcType_DiskError = 3;
    
    public static final int FHNPEN_ExcType_DiskFull = 2;
    
    public static final int FHNPEN_ExcType_IPConflict = 0;
    
    public static final int FHNPEN_ExcType_ManualAlarm = 4;
    
    public static final int FHNPEN_ExcType_Max = 5;
    
    public static final int FHNPEN_ExcType_NetDisconnected = 1;
  }
  
  public static interface FHNPEN_FlowControl_e {
    public static final int FHNPEN_FCTRL_HARD = 2;
    
    public static final int FHNPEN_FCTRL_NONE = 0;
    
    public static final int FHNPEN_FCTRL_XONXOFF = 1;
  }
  
  public static interface FHNPEN_FormatType_e {
    public static final int FHNPEN_FmtType_Fast = 0;
    
    public static final int FHNPEN_FmtType_Low = 2;
    
    public static final int FHNPEN_FmtType_Slow = 1;
  }
  
  public static interface FHNPEN_FrameType_e {
    public static final int FHNPEN_FType_AFrame = 3;
    
    public static final int FHNPEN_FType_BFrame = 2;
    
    public static final int FHNPEN_FType_IFrame = 0;
    
    public static final int FHNPEN_FType_JFrame = 4;
    
    public static final int FHNPEN_FType_PFrame = 1;
  }
  
  public static interface FHNPEN_Parity_e {
    public static final int FHNPEN_PARITY_EVEN = 2;
    
    public static final int FHNPEN_PARITY_NONE = 0;
    
    public static final int FHNPEN_PARITY_ODD = 1;
  }
  
  public static interface FHNPEN_PictureType_e {
    public static final int FHNPEN_PicType_All = 0;
    
    public static final int FHNPEN_PicType_Manual = 1;
    
    public static final int FHNPEN_PicType_Time = 2;
  }
  
  public static interface FHNPEN_PlayCtrl_e {
    public static final int FHNPEN_PBCtrl_Continue = 4;
    
    public static final int FHNPEN_PBCtrl_Fast = 6;
    
    public static final int FHNPEN_PBCtrl_FrameNext = 9;
    
    public static final int FHNPEN_PBCtrl_FramePrev = 8;
    
    public static final int FHNPEN_PBCtrl_NOIFrameNext = 10;
    
    public static final int FHNPEN_PBCtrl_Pause = 3;
    
    public static final int FHNPEN_PBCtrl_Play = 1;
    
    public static final int FHNPEN_PBCtrl_SetPlaySpeed = 7;
    
    public static final int FHNPEN_PBCtrl_Slow = 5;
    
    public static final int FHNPEN_PBCtrl_Stop = 2;
  }
  
  public static interface FHNPEN_PlaySpeed_e {
    public static final int FHNPEN_PBSpeed_16 = 4;
    
    public static final int FHNPEN_PBSpeed_1_16 = 10;
    
    public static final int FHNPEN_PBSpeed_1_2 = 7;
    
    public static final int FHNPEN_PBSpeed_1_32 = 11;
    
    public static final int FHNPEN_PBSpeed_1_4 = 8;
    
    public static final int FHNPEN_PBSpeed_1_64 = 12;
    
    public static final int FHNPEN_PBSpeed_1_8 = 9;
    
    public static final int FHNPEN_PBSpeed_2 = 1;
    
    public static final int FHNPEN_PBSpeed_32 = 5;
    
    public static final int FHNPEN_PBSpeed_4 = 2;
    
    public static final int FHNPEN_PBSpeed_64 = 6;
    
    public static final int FHNPEN_PBSpeed_8 = 3;
    
    public static final int FHNPEN_PBSpeed_Normal = 0;
  }
  
  public static interface FHNPEN_RecordType_e {
    public static final int FHNPEN_RecType_All = 0;
    
    public static final int FHNPEN_RecType_Manual = 1;
    
    public static final int FHNPEN_RecType_Time = 2;
  }
  
  public static interface FHNPEN_SDCardState_e {
    public static final int FHNPEN_SDCardState_FORMATING = 8;
    
    public static final int FHNPEN_SDCardState_FOUND = 1;
    
    public static final int FHNPEN_SDCardState_LOADED = 2;
    
    public static final int FHNPEN_SDCardState_NORMAL = 4;
  }
  
  public static interface FHNPEN_Sensitivity_e {
    public static final int FHNPEN_Sensitivity_Custom = 0;
    
    public static final int FHNPEN_Sensitivity_High = 1;
    
    public static final int FHNPEN_Sensitivity_Low = 3;
    
    public static final int FHNPEN_Sensitivity_Medium = 2;
  }
  
  public static interface FHNPEN_StreamType_e {
    public static final int FHNPEN_ST_FRAME = 0;
    
    public static final int FHNPEN_ST_TS = 1;
  }
  
  public static interface FHNPEN_TimeFormat_e {
    public static final int FHNPEN_TF_DDMMYYYY = 5;
    
    public static final int FHNPEN_TF_D_M_Y = 2;
    
    public static final int FHNPEN_TF_MMDDYYYY = 4;
    
    public static final int FHNPEN_TF_M_D_Y = 1;
    
    public static final int FHNPEN_TF_YYYYMMDD = 3;
    
    public static final int FHNPEN_TF_Y_M_D = 0;
  }
  
  public static interface FHNPEN_TimeStandard_e {
    public static final int FHNPEN_TS_12 = 1;
    
    public static final int FHNPEN_TS_24 = 0;
  }
  
  public static interface FHNPEN_TimeZone_e {
    public static final int FHNPEN_TIMEZONE_GMT = 14;
    
    public static final int FHNPEN_TIMEZONE_GMT0100 = 15;
    
    public static final int FHNPEN_TIMEZONE_GMT0200 = 16;
    
    public static final int FHNPEN_TIMEZONE_GMT0300 = 17;
    
    public static final int FHNPEN_TIMEZONE_GMT0330 = 18;
    
    public static final int FHNPEN_TIMEZONE_GMT0400 = 19;
    
    public static final int FHNPEN_TIMEZONE_GMT0430 = 20;
    
    public static final int FHNPEN_TIMEZONE_GMT0500 = 21;
    
    public static final int FHNPEN_TIMEZONE_GMT0530 = 22;
    
    public static final int FHNPEN_TIMEZONE_GMT0545 = 23;
    
    public static final int FHNPEN_TIMEZONE_GMT0600 = 24;
    
    public static final int FHNPEN_TIMEZONE_GMT0630 = 25;
    
    public static final int FHNPEN_TIMEZONE_GMT0700 = 26;
    
    public static final int FHNPEN_TIMEZONE_GMT0800 = 27;
    
    public static final int FHNPEN_TIMEZONE_GMT0900 = 28;
    
    public static final int FHNPEN_TIMEZONE_GMT0930 = 29;
    
    public static final int FHNPEN_TIMEZONE_GMT1000 = 30;
    
    public static final int FHNPEN_TIMEZONE_GMT1100 = 31;
    
    public static final int FHNPEN_TIMEZONE_GMT1200 = 32;
    
    public static final int FHNPEN_TIMEZONE_GMT1300 = 33;
    
    public static final int FHNPEN_TIMEZONE_GMT_0100 = 13;
    
    public static final int FHNPEN_TIMEZONE_GMT_0200 = 12;
    
    public static final int FHNPEN_TIMEZONE_GMT_0300 = 11;
    
    public static final int FHNPEN_TIMEZONE_GMT_0330 = 10;
    
    public static final int FHNPEN_TIMEZONE_GMT_0400 = 9;
    
    public static final int FHNPEN_TIMEZONE_GMT_0430 = 8;
    
    public static final int FHNPEN_TIMEZONE_GMT_0500 = 7;
    
    public static final int FHNPEN_TIMEZONE_GMT_0600 = 6;
    
    public static final int FHNPEN_TIMEZONE_GMT_0700 = 5;
    
    public static final int FHNPEN_TIMEZONE_GMT_0800 = 4;
    
    public static final int FHNPEN_TIMEZONE_GMT_0900 = 3;
    
    public static final int FHNPEN_TIMEZONE_GMT_1000 = 2;
    
    public static final int FHNPEN_TIMEZONE_GMT_1100 = 1;
    
    public static final int FHNPEN_TIMEZONE_GMT_1200 = 0;
  }
  
  public static interface FHNPEN_TrigerBuzzerType_e {
    public static final int FHNPEN_BuzzerType_Dida = 1;
    
    public static final int FHNPEN_BuzzerType_Long = 0;
  }
  
  public static interface FHNPEN_TrigerPTZType_e {
    public static final int FHNPEN_PTZType_Cruise = 2;
    
    public static final int FHNPEN_PTZType_Preset = 0;
    
    public static final int FHNPEN_PTZType_Track = 1;
  }
  
  public static interface FHNPEN_UpgradeState_e {
    public static final int FHNPEN_UState_Fail = 4;
    
    public static final int FHNPEN_UState_Finish = 1;
    
    public static final int FHNPEN_UState_Sending = 2;
    
    public static final int FHNPEN_UState_Unknown = 5;
    
    public static final int FHNPEN_UState_Waiting = 3;
  }
  
  public static interface FHNPEN_UserGroup_e {
    public static final int FHNPEN_UG_Admin = 1;
    
    public static final int FHNPEN_UG_User = 2;
  }
  
  public static interface FHNPEN_VideoInputMode_e {
    public static final int FHNPEN_VideoInputMode_NTSC = 1;
    
    public static final int FHNPEN_VideoInputMode_PAL = 0;
  }
  
  public static interface FHNPEN_WifiMode_e {
    public static final int FHNPEN_WLAN_MODE_ADHOC = 1;
    
    public static final int FHNPEN_WLAN_MODE_INFRASTRUCTURE = 0;
    
    public static final int FHNPEN_WLAN_MODE_P2P = 3;
    
    public static final int FHNPEN_WLAN_MODE_P2P_UAP = 4;
    
    public static final int FHNPEN_WLAN_MODE_UAP = 2;
  }
  
  public static interface FHNPEN_WifiSecurityType_e {
    public static final int FHNPEN_WLAN_SECURITY_NONE = 0;
    
    public static final int FHNPEN_WLAN_SECURITY_WEP_OPEN = 1;
    
    public static final int FHNPEN_WLAN_SECURITY_WEP_SHARED = 2;
    
    public static final int FHNPEN_WLAN_SECURITY_WPA = 3;
    
    public static final int FHNPEN_WLAN_SECURITY_WPA2 = 4;
  }
  
  public static interface FHNPEN_WriteError_e {
    public static final int FHNPEN_WET_NOINIT = 4;
    
    public static final int FHNPEN_WET_NOSD = 3;
    
    public static final int FHNPEN_WET_NOSPACE = 2;
    
    public static final int FHNPEN_WET_NOTRY = -1;
    
    public static final int FHNPEN_WET_OK = 0;
    
    public static final int FHNPEN_WET_OTHER = 5;
    
    public static final int FHNPEN_WET_PTS = 1;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/FHDEV_Net_DefineLibrary.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */