package com.netopsun.fhapi;

import com.sun.jna.Callback;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.ShortByReference;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

public interface FHDEV_NetLibrary extends Library {
  public static final FHDEV_NetLibrary INSTANCE;
  
  public static final String JNA_LIBRARY_NAME = "FHDEV_Net";
  
  public static final NativeLibrary JNA_NATIVE_LIB = NativeLibrary.getInstance("FHDEV_Net");
  
  static {
    INSTANCE = (FHDEV_NetLibrary)Native.loadLibrary("FHDEV_Net", FHDEV_NetLibrary.class);
  }
  
  int FHDEV_NET_AddUser(Pointer paramPointer, LPFHNP_User_t_struct paramLPFHNP_User_t_struct);
  
  int FHDEV_NET_CleanIspConfig(Pointer paramPointer);
  
  int FHDEV_NET_Cleanup();
  
  int FHDEV_NET_ClosePlayBackAudio(Pointer paramPointer);
  
  int FHDEV_NET_CloseRealAudio(Pointer paramPointer);
  
  int FHDEV_NET_CloseSearchPicture(Pointer paramPointer);
  
  int FHDEV_NET_CloseSearchRecord(Pointer paramPointer);
  
  Pointer FHDEV_NET_CreateDevShot(Pointer paramPointer1, byte paramByte, fDataCallBack paramfDataCallBack, Pointer paramPointer2);
  
  Pointer FHDEV_NET_CreatePicDownload(Pointer paramPointer1, LPFHNP_Picture_t_struct paramLPFHNP_Picture_t_struct, fDataCallBack paramfDataCallBack, Pointer paramPointer2);
  
  Pointer FHDEV_NET_CreateRecDownload(Pointer paramPointer1, LPFHNP_Record_t_struct paramLPFHNP_Record_t_struct, int paramInt, fDataCallBack paramfDataCallBack, Pointer paramPointer2);
  
  @Deprecated
  int FHDEV_NET_DeletePicture(Pointer paramPointer1, int paramInt, Pointer paramPointer2);
  
  int FHDEV_NET_DeletePicture(Pointer paramPointer, int paramInt, FHNP_Picture_t[] paramArrayOfFHNP_Picture_t);
  
  @Deprecated
  int FHDEV_NET_DeleteRecord(Pointer paramPointer1, int paramInt, Pointer paramPointer2);
  
  int FHDEV_NET_DeleteRecord(Pointer paramPointer, int paramInt, FHNP_Record_t[] paramArrayOfFHNP_Record_t);
  
  @Deprecated
  int FHDEV_NET_DeleteUser(Pointer paramPointer1, Pointer paramPointer2);
  
  int FHDEV_NET_DeleteUser(Pointer paramPointer, ByteBuffer paramByteBuffer);
  
  int FHDEV_NET_DestoryDevShot(Pointer paramPointer);
  
  int FHDEV_NET_DestoryPicDownload(Pointer paramPointer);
  
  int FHDEV_NET_DestoryRecDownload(Pointer paramPointer);
  
  int FHDEV_NET_DevMakeKeyFrame(Pointer paramPointer, byte paramByte1, byte paramByte2);
  
  int FHDEV_NET_DevMakeKeyPFrame(Pointer paramPointer, byte paramByte1, byte paramByte2);
  
  @Deprecated
  int FHDEV_NET_DevShot(Pointer paramPointer1, byte paramByte1, byte paramByte2, Pointer paramPointer2, IntByReference paramIntByReference);
  
  int FHDEV_NET_DevShot(Pointer paramPointer1, byte paramByte1, byte paramByte2, Pointer paramPointer2, IntBuffer paramIntBuffer);
  
  int FHDEV_NET_EnableDevRecAudio(Pointer paramPointer, byte paramByte, int paramInt);
  
  @Deprecated
  int FHDEV_NET_ExportConfig(Pointer paramPointer1, Pointer paramPointer2, int paramInt, IntByReference paramIntByReference);
  
  int FHDEV_NET_ExportConfig(Pointer paramPointer, ByteBuffer paramByteBuffer, int paramInt, IntBuffer paramIntBuffer);
  
  @Deprecated
  int FHDEV_NET_ExportConfigEx(Pointer paramPointer1, Pointer paramPointer2);
  
  int FHDEV_NET_ExportConfigEx(Pointer paramPointer, ByteBuffer paramByteBuffer);
  
  @Deprecated
  int FHDEV_NET_ExportIspConfig(Pointer paramPointer1, Pointer paramPointer2, int paramInt, IntByReference paramIntByReference);
  
  int FHDEV_NET_ExportIspConfig(Pointer paramPointer, ByteBuffer paramByteBuffer, int paramInt, IntBuffer paramIntBuffer);
  
  @Deprecated
  int FHDEV_NET_ExportIspConfigEx(Pointer paramPointer1, Pointer paramPointer2);
  
  int FHDEV_NET_ExportIspConfigEx(Pointer paramPointer, ByteBuffer paramByteBuffer);
  
  @Deprecated
  int FHDEV_NET_G711Dec(byte paramByte, int paramInt, Pointer paramPointer1, short paramShort1, Pointer paramPointer2, short paramShort2, ShortByReference paramShortByReference);
  
  int FHDEV_NET_G711Dec(byte paramByte, int paramInt, ByteBuffer paramByteBuffer1, short paramShort1, ByteBuffer paramByteBuffer2, short paramShort2, ShortBuffer paramShortBuffer);
  
  @Deprecated
  int FHDEV_NET_G711Enc(byte paramByte, int paramInt, Pointer paramPointer1, short paramShort1, Pointer paramPointer2, short paramShort2, ShortByReference paramShortByReference);
  
  int FHDEV_NET_G711Enc(byte paramByte, int paramInt, ByteBuffer paramByteBuffer1, short paramShort1, ByteBuffer paramByteBuffer2, short paramShort2, ShortBuffer paramShortBuffer);
  
  int FHDEV_NET_GetBatteryVInfo(Pointer paramPointer, LPFHNP_BatteryV_Info_t_struct paramLPFHNP_BatteryV_Info_t_struct);
  
  int FHDEV_NET_GetDevCapacity(Pointer paramPointer, LPFHNP_Capacity_t_struct paramLPFHNP_Capacity_t_struct);
  
  @Deprecated
  int FHDEV_NET_GetDevConfig(Pointer paramPointer1, int paramInt1, byte paramByte, Pointer paramPointer2, int paramInt2, IntByReference paramIntByReference);
  
  int FHDEV_NET_GetDevConfig(Pointer paramPointer1, int paramInt1, byte paramByte, Pointer paramPointer2, int paramInt2, IntBuffer paramIntBuffer);
  
  @Deprecated
  int FHDEV_NET_GetDevConfigEx(Pointer paramPointer1, int paramInt1, byte paramByte, Pointer paramPointer2, int paramInt2, Pointer paramPointer3, int paramInt3, IntByReference paramIntByReference);
  
  int FHDEV_NET_GetDevConfigEx(Pointer paramPointer1, int paramInt1, byte paramByte, Pointer paramPointer2, int paramInt2, Pointer paramPointer3, int paramInt3, IntBuffer paramIntBuffer);
  
  @Deprecated
  int FHDEV_NET_GetDevRecAudioState(Pointer paramPointer, byte paramByte, IntByReference paramIntByReference);
  
  int FHDEV_NET_GetDevRecAudioState(Pointer paramPointer, byte paramByte, IntBuffer paramIntBuffer);
  
  @Deprecated
  int FHDEV_NET_GetDevRecordState(Pointer paramPointer, byte paramByte, IntByReference paramIntByReference);
  
  int FHDEV_NET_GetDevRecordState(Pointer paramPointer, byte paramByte, IntBuffer paramIntBuffer);
  
  @Deprecated
  int FHDEV_NET_GetDeviceFlag(Pointer paramPointer, IntByReference paramIntByReference);
  
  int FHDEV_NET_GetDeviceFlag(Pointer paramPointer, IntBuffer paramIntBuffer);
  
  int FHDEV_NET_GetEncodeVideo(Pointer paramPointer, byte paramByte1, byte paramByte2, LPFHNP_EncodeVideo_t_struct paramLPFHNP_EncodeVideo_t_struct);
  
  @Deprecated
  Pointer FHDEV_NET_GetErrorMsg(IntByReference paramIntByReference);
  
  Pointer FHDEV_NET_GetErrorMsg(IntBuffer paramIntBuffer);
  
  int FHDEV_NET_GetLastError();
  
  @Deprecated
  int FHDEV_NET_GetRealAudioState(Pointer paramPointer, IntByReference paramIntByReference);
  
  int FHDEV_NET_GetRealAudioState(Pointer paramPointer, IntBuffer paramIntBuffer);
  
  @Deprecated
  int FHDEV_NET_GetSDCardFormatState(Pointer paramPointer, IntByReference paramIntByReference1, IntByReference paramIntByReference2);
  
  int FHDEV_NET_GetSDCardFormatState(Pointer paramPointer, IntBuffer paramIntBuffer1, IntBuffer paramIntBuffer2);
  
  @Deprecated
  int FHDEV_NET_GetSDCardFormatStateEx(Pointer paramPointer, IntByReference paramIntByReference1, IntByReference paramIntByReference2);
  
  int FHDEV_NET_GetSDCardFormatStateEx(Pointer paramPointer, IntBuffer paramIntBuffer1, IntBuffer paramIntBuffer2);
  
  int FHDEV_NET_GetSDCardInfo(Pointer paramPointer, LPFHNP_SDCardInfo_t_struct paramLPFHNP_SDCardInfo_t_struct);
  
  int FHDEV_NET_GetSDKBuildVersion();
  
  int FHDEV_NET_GetSDKState(Pointer paramPointer, LPFHNP_SDKState_t_struct paramLPFHNP_SDKState_t_struct);
  
  int FHDEV_NET_GetSDKVersion();
  
  @Deprecated
  int FHDEV_NET_GetTalkUnitSize(Pointer paramPointer, IntByReference paramIntByReference);
  
  int FHDEV_NET_GetTalkUnitSize(Pointer paramPointer, IntBuffer paramIntBuffer);
  
  int FHDEV_NET_GetTempRHInfo(Pointer paramPointer, LPFHNP_TEMP_RH_t_struct paramLPFHNP_TEMP_RH_t_struct);
  
  @Deprecated
  int FHDEV_NET_GetUpgradeState(Pointer paramPointer, IntByReference paramIntByReference1, IntByReference paramIntByReference2);
  
  int FHDEV_NET_GetUpgradeState(Pointer paramPointer, IntBuffer paramIntBuffer1, IntBuffer paramIntBuffer2);
  
  @Deprecated
  int FHDEV_NET_GetUserAuth(Pointer paramPointer, IntByReference paramIntByReference);
  
  int FHDEV_NET_GetUserAuth(Pointer paramPointer, IntBuffer paramIntBuffer);
  
  @Deprecated
  int FHDEV_NET_GetUserList(Pointer paramPointer1, Pointer paramPointer2);
  
  int FHDEV_NET_GetUserList(Pointer paramPointer, FHNP_User_t[] paramArrayOfFHNP_User_t);
  
  @Deprecated
  int FHDEV_NET_GetWifiQuality(Pointer paramPointer, IntByReference paramIntByReference);
  
  int FHDEV_NET_GetWifiQuality(Pointer paramPointer, IntBuffer paramIntBuffer);
  
  @Deprecated
  int FHDEV_NET_ImportConfig(Pointer paramPointer1, Pointer paramPointer2, int paramInt);
  
  int FHDEV_NET_ImportConfig(Pointer paramPointer, ByteBuffer paramByteBuffer, int paramInt);
  
  @Deprecated
  int FHDEV_NET_ImportConfigEx(Pointer paramPointer1, Pointer paramPointer2);
  
  int FHDEV_NET_ImportConfigEx(Pointer paramPointer, ByteBuffer paramByteBuffer);
  
  @Deprecated
  int FHDEV_NET_ImportIspConfig(Pointer paramPointer1, Pointer paramPointer2, int paramInt);
  
  int FHDEV_NET_ImportIspConfig(Pointer paramPointer, ByteBuffer paramByteBuffer, int paramInt);
  
  @Deprecated
  int FHDEV_NET_ImportIspConfigEx(Pointer paramPointer1, Pointer paramPointer2);
  
  int FHDEV_NET_ImportIspConfigEx(Pointer paramPointer, ByteBuffer paramByteBuffer);
  
  int FHDEV_NET_Init();
  
  int FHDEV_NET_JumpPlayBack(Pointer paramPointer, long paramLong);
  
  int FHDEV_NET_LoadSDCard(Pointer paramPointer);
  
  @Deprecated
  int FHDEV_NET_LockPicture(Pointer paramPointer1, int paramInt, Pointer paramPointer2);
  
  int FHDEV_NET_LockPicture(Pointer paramPointer, int paramInt, FHNP_Picture_t[] paramArrayOfFHNP_Picture_t);
  
  @Deprecated
  int FHDEV_NET_LockRecord(Pointer paramPointer1, int paramInt, Pointer paramPointer2);
  
  int FHDEV_NET_LockRecord(Pointer paramPointer, int paramInt, FHNP_Record_t[] paramArrayOfFHNP_Record_t);
  
  @Deprecated
  Pointer FHDEV_NET_Login(Pointer paramPointer1, short paramShort, Pointer paramPointer2, Pointer paramPointer3, LPFHNP_Capacity_t_struct paramLPFHNP_Capacity_t_struct);
  
  Pointer FHDEV_NET_Login(ByteBuffer paramByteBuffer1, short paramShort, ByteBuffer paramByteBuffer2, ByteBuffer paramByteBuffer3, LPFHNP_Capacity_t_struct paramLPFHNP_Capacity_t_struct);
  
  @Deprecated
  Pointer FHDEV_NET_LoginEx(Pointer paramPointer1, short paramShort1, Pointer paramPointer2, Pointer paramPointer3, Pointer paramPointer4, short paramShort2, LPFHNP_Capacity_t_struct paramLPFHNP_Capacity_t_struct);
  
  Pointer FHDEV_NET_LoginEx(ByteBuffer paramByteBuffer1, short paramShort1, ByteBuffer paramByteBuffer2, ByteBuffer paramByteBuffer3, ByteBuffer paramByteBuffer4, short paramShort2, LPFHNP_Capacity_t_struct paramLPFHNP_Capacity_t_struct);
  
  int FHDEV_NET_Logout(Pointer paramPointer);
  
  int FHDEV_NET_ModifyUser(Pointer paramPointer, LPFHNP_User_t_struct paramLPFHNP_User_t_struct);
  
  int FHDEV_NET_OpenPlayBackAudio(Pointer paramPointer);
  
  int FHDEV_NET_OpenRealAudio(Pointer paramPointer);
  
  int FHDEV_NET_PlayBackControl(Pointer paramPointer, int paramInt1, int paramInt2);
  
  int FHDEV_NET_RebootDev(Pointer paramPointer);
  
  int FHDEV_NET_RebootDevEx(Pointer paramPointer);
  
  int FHDEV_NET_RegisterDevNotifyFun(fNotifyCallBack paramfNotifyCallBack, Pointer paramPointer);
  
  int FHDEV_NET_RegisterPlayBackDataFun(Pointer paramPointer1, int paramInt, fDataCallBack paramfDataCallBack, Pointer paramPointer2);
  
  int FHDEV_NET_RegisterRealDataFun(Pointer paramPointer1, int paramInt, fDataCallBack paramfDataCallBack, Pointer paramPointer2);
  
  int FHDEV_NET_ResetDev(Pointer paramPointer, int paramInt);
  
  int FHDEV_NET_SaveDevConfig(Pointer paramPointer);
  
  int FHDEV_NET_SearchNextPicture(Pointer paramPointer, LPFHNP_Picture_t_struct paramLPFHNP_Picture_t_struct);
  
  int FHDEV_NET_SearchNextRecord(Pointer paramPointer, LPFHNP_Record_t_struct paramLPFHNP_Record_t_struct);
  
  Pointer FHDEV_NET_SearchPicture(Pointer paramPointer, LPFHNP_PicSearch_t_struct paramLPFHNP_PicSearch_t_struct);
  
  Pointer FHDEV_NET_SearchRecord(Pointer paramPointer, LPFHNP_RecSearch_t_struct paramLPFHNP_RecSearch_t_struct);
  
  @Deprecated
  int FHDEV_NET_SendSerial(Pointer paramPointer1, Pointer paramPointer2, int paramInt);
  
  int FHDEV_NET_SendSerial(Pointer paramPointer, ByteBuffer paramByteBuffer, int paramInt);
  
  @Deprecated
  int FHDEV_NET_SendToSerialPort(Pointer paramPointer1, int paramInt1, int paramInt2, Pointer paramPointer2, int paramInt3);
  
  int FHDEV_NET_SendToSerialPort(Pointer paramPointer, int paramInt1, int paramInt2, ByteBuffer paramByteBuffer, int paramInt3);
  
  int FHDEV_NET_SetConnectTime(int paramInt1, int paramInt2);
  
  @Deprecated
  int FHDEV_NET_SetCryptKey(Pointer paramPointer);
  
  int FHDEV_NET_SetCryptKey(ByteBuffer paramByteBuffer);
  
  int FHDEV_NET_SetDevConfig(Pointer paramPointer1, int paramInt1, byte paramByte, Pointer paramPointer2, int paramInt2);
  
  int FHDEV_NET_SetEncodeVideo(Pointer paramPointer, byte paramByte1, byte paramByte2, LPFHNP_EncodeVideo_t_struct paramLPFHNP_EncodeVideo_t_struct);
  
  int FHDEV_NET_SetHeartbeatTime(int paramInt);
  
  int FHDEV_NET_SetReconnect(int paramInt1, int paramInt2);
  
  int FHDEV_NET_SetRecvTimeOut(int paramInt);
  
  int FHDEV_NET_ShutDownDev(Pointer paramPointer);
  
  int FHDEV_NET_StartDevRecord(Pointer paramPointer, byte paramByte);
  
  Pointer FHDEV_NET_StartPlayBack(Pointer paramPointer1, LPFHNP_Playback_t_struct paramLPFHNP_Playback_t_struct, int paramInt, fDataCallBack paramfDataCallBack, Pointer paramPointer2);
  
  Pointer FHDEV_NET_StartRealPicPlay(Pointer paramPointer1, byte paramByte, fDataCallBack paramfDataCallBack, Pointer paramPointer2);
  
  Pointer FHDEV_NET_StartRealPlay(Pointer paramPointer1, LPFHNP_Preview_t_struct paramLPFHNP_Preview_t_struct, int paramInt, fDataCallBack paramfDataCallBack, Pointer paramPointer2);
  
  Pointer FHDEV_NET_StartSDCardFormat(Pointer paramPointer, int paramInt);
  
  int FHDEV_NET_StartSDCardFormatEx(Pointer paramPointer, int paramInt);
  
  Pointer FHDEV_NET_StartSerial(Pointer paramPointer1, int paramInt1, int paramInt2, fSerialDataCallBack paramfSerialDataCallBack, Pointer paramPointer2);
  
  Pointer FHDEV_NET_StartSerialEx(Pointer paramPointer1, int paramInt1, int paramInt2, byte paramByte, int paramInt3, fSerialDataCallBack paramfSerialDataCallBack, Pointer paramPointer2);
  
  Pointer FHDEV_NET_StartTalk(Pointer paramPointer1, int paramInt, fTalkDataCallBack paramfTalkDataCallBack, Pointer paramPointer2);
  
  Pointer FHDEV_NET_StartTalkEx(Pointer paramPointer1, int paramInt1, byte paramByte, int paramInt2, LPFHNP_AFrameHead_t_struct paramLPFHNP_AFrameHead_t_struct, fTalkDataCallBack paramfTalkDataCallBack, Pointer paramPointer2);
  
  @Deprecated
  Pointer FHDEV_NET_StartUpgrade(Pointer paramPointer1, Pointer paramPointer2, int paramInt1, int paramInt2);
  
  Pointer FHDEV_NET_StartUpgrade(Pointer paramPointer, ByteBuffer paramByteBuffer, int paramInt1, int paramInt2);
  
  @Deprecated
  Pointer FHDEV_NET_StartUpgradeEx(Pointer paramPointer1, Pointer paramPointer2, int paramInt);
  
  Pointer FHDEV_NET_StartUpgradeEx(Pointer paramPointer, ByteBuffer paramByteBuffer, int paramInt);
  
  int FHDEV_NET_StopDevRecord(Pointer paramPointer, byte paramByte);
  
  int FHDEV_NET_StopPlayBack(Pointer paramPointer);
  
  int FHDEV_NET_StopRealPicPlay(Pointer paramPointer);
  
  int FHDEV_NET_StopRealPlay(Pointer paramPointer);
  
  int FHDEV_NET_StopSDCardFormat(Pointer paramPointer);
  
  int FHDEV_NET_StopSDCardFormatEx(Pointer paramPointer);
  
  int FHDEV_NET_StopSerial(Pointer paramPointer);
  
  int FHDEV_NET_StopTalk(Pointer paramPointer);
  
  int FHDEV_NET_StopUpgrade(Pointer paramPointer);
  
  int FHDEV_NET_TalkCtrl(Pointer paramPointer, int paramInt);
  
  @Deprecated
  int FHDEV_NET_TalkSendData(Pointer paramPointer1, LPFHNP_AFrameHead_t_struct paramLPFHNP_AFrameHead_t_struct, Pointer paramPointer2, int paramInt);
  
  int FHDEV_NET_TalkSendData(Pointer paramPointer, LPFHNP_AFrameHead_t_struct paramLPFHNP_AFrameHead_t_struct, ByteBuffer paramByteBuffer, int paramInt);
  
  @Deprecated
  int FHDEV_NET_TestWifiConfig(Pointer paramPointer, LPFHNP_WifiConfig_t_struct paramLPFHNP_WifiConfig_t_struct, IntByReference paramIntByReference);
  
  int FHDEV_NET_TestWifiConfig(Pointer paramPointer, LPFHNP_WifiConfig_t_struct paramLPFHNP_WifiConfig_t_struct, IntBuffer paramIntBuffer);
  
  int FHDEV_NET_TimeConvert(Pointer paramPointer, int paramInt, FHNP_Time_t paramFHNP_Time_t);
  
  int FHDEV_NET_TimeConvertEx(Pointer paramPointer, long paramLong, FHNP_Time_t paramFHNP_Time_t);
  
  int FHDEV_NET_UnloadSDCard(Pointer paramPointer);
  
  @Deprecated
  int FHDEV_NET_UnlockPicture(Pointer paramPointer1, int paramInt, Pointer paramPointer2);
  
  int FHDEV_NET_UnlockPicture(Pointer paramPointer, int paramInt, FHNP_Picture_t[] paramArrayOfFHNP_Picture_t);
  
  @Deprecated
  int FHDEV_NET_UnlockRecord(Pointer paramPointer1, int paramInt, Pointer paramPointer2);
  
  int FHDEV_NET_UnlockRecord(Pointer paramPointer, int paramInt, FHNP_Record_t[] paramArrayOfFHNP_Record_t);
  
  public static interface fDataCallBack extends Callback {
    void apply(Pointer param1Pointer1, int param1Int1, LPFHNP_FrameHead_t_union param1LPFHNP_FrameHead_t_union, Pointer param1Pointer2, int param1Int2, Pointer param1Pointer3);
  }
  
  public static interface fNotifyCallBack extends Callback {
    void apply(LPFHNP_Notify_t_struct param1LPFHNP_Notify_t_struct, Pointer param1Pointer);
  }
  
  public static interface fSerialDataCallBack extends Callback {
    void apply(Pointer param1Pointer1, Pointer param1Pointer2, int param1Int, Pointer param1Pointer3);
  }
  
  public static interface fTalkDataCallBack extends Callback {
    void apply(Pointer param1Pointer1, LPFHNP_AFrameHead_t_struct param1LPFHNP_AFrameHead_t_struct, Pointer param1Pointer2, int param1Int, Pointer param1Pointer3);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/FHDEV_NetLibrary.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */