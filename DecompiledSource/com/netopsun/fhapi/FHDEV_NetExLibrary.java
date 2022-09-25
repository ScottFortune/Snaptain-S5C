package com.netopsun.fhapi;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;

public interface FHDEV_NetExLibrary extends Library {
  public static final FHDEV_NetExLibrary INSTANCE;
  
  public static final String JNA_LIBRARY_NAME = "FHDEV_Net";
  
  public static final NativeLibrary JNA_NATIVE_LIB = NativeLibrary.getInstance("FHDEV_Net");
  
  static {
    INSTANCE = (FHDEV_NetExLibrary)Native.loadLibrary("FHDEV_Net", FHDEV_NetExLibrary.class);
  }
  
  int FHDEV_NET_AddBookMark(Pointer paramPointer, LPFHNP_BookMark_t_struct paramLPFHNP_BookMark_t_struct);
  
  int FHDEV_NET_ClearAlarm(Pointer paramPointer);
  
  int FHDEV_NET_ClearLog(Pointer paramPointer);
  
  int FHDEV_NET_CloseRealVideo(Pointer paramPointer);
  
  int FHDEV_NET_CloseSearchBookMark(Pointer paramPointer);
  
  int FHDEV_NET_CloseSearchLog(Pointer paramPointer);
  
  @Deprecated
  int FHDEV_NET_DbgReadReg(Pointer paramPointer1, short paramShort1, Pointer paramPointer2, short paramShort2, Pointer paramPointer3, Pointer paramPointer4, int paramInt1, int paramInt2, int paramInt3, Pointer paramPointer5);
  
  int FHDEV_NET_DbgReadReg(ByteBuffer paramByteBuffer1, short paramShort1, ByteBuffer paramByteBuffer2, short paramShort2, ByteBuffer paramByteBuffer3, ByteBuffer paramByteBuffer4, int paramInt1, int paramInt2, int paramInt3, ByteBuffer paramByteBuffer5);
  
  @Deprecated
  int FHDEV_NET_DbgWriteReg(Pointer paramPointer1, short paramShort1, Pointer paramPointer2, short paramShort2, Pointer paramPointer3, Pointer paramPointer4, int paramInt1, int paramInt2, int paramInt3, Pointer paramPointer5);
  
  int FHDEV_NET_DbgWriteReg(ByteBuffer paramByteBuffer1, short paramShort1, ByteBuffer paramByteBuffer2, short paramShort2, ByteBuffer paramByteBuffer3, ByteBuffer paramByteBuffer4, int paramInt1, int paramInt2, int paramInt3, ByteBuffer paramByteBuffer5);
  
  int FHDEV_NET_DefaultConfig(Pointer paramPointer, int paramInt, byte paramByte);
  
  int FHDEV_NET_Defence(Pointer paramPointer, int paramInt);
  
  @Deprecated
  int FHDEV_NET_DeleteBookMark(Pointer paramPointer1, int paramInt, Pointer paramPointer2);
  
  int FHDEV_NET_DeleteBookMark(Pointer paramPointer, int paramInt, FHNP_BookMark_t[] paramArrayOfFHNP_BookMark_t);
  
  @Deprecated
  int FHDEV_NET_DeleteLog(Pointer paramPointer1, int paramInt, Pointer paramPointer2);
  
  int FHDEV_NET_DeleteLog(Pointer paramPointer, int paramInt, FHNP_Time_t[] paramArrayOfFHNP_Time_t);
  
  @Deprecated
  int FHDEV_NET_GetBuffer(Pointer paramPointer1, byte paramByte1, byte paramByte2, Pointer paramPointer2, int paramInt1, Pointer paramPointer3, int paramInt2, IntByReference paramIntByReference);
  
  int FHDEV_NET_GetBuffer(Pointer paramPointer, byte paramByte1, byte paramByte2, ByteBuffer paramByteBuffer1, int paramInt1, ByteBuffer paramByteBuffer2, int paramInt2, IntBuffer paramIntBuffer);
  
  @Deprecated
  int FHDEV_NET_GetConvertProgress(Pointer paramPointer, IntByReference paramIntByReference);
  
  int FHDEV_NET_GetConvertProgress(Pointer paramPointer, IntBuffer paramIntBuffer);
  
  int FHDEV_NET_GetDebugParam(LPFHNP_DebugParam_t_struct paramLPFHNP_DebugParam_t_struct);
  
  @Deprecated
  int FHDEV_NET_GetDefenceState(Pointer paramPointer, IntByReference paramIntByReference);
  
  int FHDEV_NET_GetDefenceState(Pointer paramPointer, IntBuffer paramIntBuffer);
  
  int FHDEV_NET_GetEncodeWaterMark(Pointer paramPointer, byte paramByte1, byte paramByte2, LPFHNP_EncodeWaterMark_t_struct paramLPFHNP_EncodeWaterMark_t_struct);
  
  @Deprecated
  int FHDEV_NET_GetManualAlarmState(Pointer paramPointer, IntByReference paramIntByReference);
  
  int FHDEV_NET_GetManualAlarmState(Pointer paramPointer, IntBuffer paramIntBuffer);
  
  @Deprecated
  int FHDEV_NET_GetPTZCruise(Pointer paramPointer1, int paramInt, byte paramByte, Pointer paramPointer2);
  
  int FHDEV_NET_GetPTZCruise(Pointer paramPointer, int paramInt, byte paramByte, FHNP_CruisePoint_t[] paramArrayOfFHNP_CruisePoint_t);
  
  int FHDEV_NET_GetPrivateDebugParam(LPFHNP_PriDebugParam_t_struct paramLPFHNP_PriDebugParam_t_struct);
  
  @Deprecated
  int FHDEV_NET_GetRecDownloadState(Pointer paramPointer, IntByReference paramIntByReference1, IntByReference paramIntByReference2);
  
  int FHDEV_NET_GetRecDownloadState(Pointer paramPointer, IntBuffer paramIntBuffer1, IntBuffer paramIntBuffer2);
  
  @Deprecated
  int FHDEV_NET_GetRecPlayProgress(Pointer paramPointer, IntByReference paramIntByReference);
  
  int FHDEV_NET_GetRecPlayProgress(Pointer paramPointer, IntBuffer paramIntBuffer);
  
  @Deprecated
  int FHDEV_NET_GetRecPlayTimeInfo(Pointer paramPointer, LongByReference paramLongByReference1, LongByReference paramLongByReference2);
  
  int FHDEV_NET_GetRecPlayTimeInfo(Pointer paramPointer, LongBuffer paramLongBuffer1, LongBuffer paramLongBuffer2);
  
  int FHDEV_NET_JumpRecPlay(Pointer paramPointer, int paramInt);
  
  @Deprecated
  int FHDEV_NET_KickUser(Pointer paramPointer1, Pointer paramPointer2, int paramInt);
  
  int FHDEV_NET_KickUser(Pointer paramPointer, ByteBuffer paramByteBuffer, int paramInt);
  
  int FHDEV_NET_ManualAlarm(Pointer paramPointer, int paramInt);
  
  int FHDEV_NET_MirrorCtrl(Pointer paramPointer, byte paramByte);
  
  int FHDEV_NET_ModifyBookMark(Pointer paramPointer, LPFHNP_BookMark_t_struct paramLPFHNP_BookMark_t_struct);
  
  int FHDEV_NET_OpenRealVideo(Pointer paramPointer);
  
  int FHDEV_NET_PTZControl(Pointer paramPointer, int paramInt1, int paramInt2);
  
  int FHDEV_NET_PTZControlWithSpeed(Pointer paramPointer, int paramInt1, int paramInt2, int paramInt3);
  
  int FHDEV_NET_PTZCruise(Pointer paramPointer, int paramInt1, int paramInt2, byte paramByte, LPFHNP_CruisePoint_t_struct paramLPFHNP_CruisePoint_t_struct);
  
  int FHDEV_NET_PTZPreset(Pointer paramPointer, int paramInt1, int paramInt2, int paramInt3);
  
  @Deprecated
  int FHDEV_NET_PTZProtocolContentGet(Pointer paramPointer1, Pointer paramPointer2, Pointer paramPointer3);
  
  int FHDEV_NET_PTZProtocolContentGet(Pointer paramPointer, ByteBuffer paramByteBuffer1, ByteBuffer paramByteBuffer2);
  
  @Deprecated
  int FHDEV_NET_PTZProtocolInfoGet(Pointer paramPointer1, Pointer paramPointer2, LPFHNP_PTZProtocol_t_struct paramLPFHNP_PTZProtocol_t_struct);
  
  int FHDEV_NET_PTZProtocolInfoGet(Pointer paramPointer, ByteBuffer paramByteBuffer, LPFHNP_PTZProtocol_t_struct paramLPFHNP_PTZProtocol_t_struct);
  
  @Deprecated
  int FHDEV_NET_PTZProtocolInstall(Pointer paramPointer1, Pointer paramPointer2);
  
  int FHDEV_NET_PTZProtocolInstall(Pointer paramPointer, ByteBuffer paramByteBuffer);
  
  @Deprecated
  int FHDEV_NET_PTZProtocolListGet(Pointer paramPointer1, Pointer paramPointer2);
  
  int FHDEV_NET_PTZProtocolListGet(Pointer paramPointer, ByteBuffer paramByteBuffer);
  
  @Deprecated
  int FHDEV_NET_PTZProtocolUninstall(Pointer paramPointer1, Pointer paramPointer2);
  
  int FHDEV_NET_PTZProtocolUninstall(Pointer paramPointer, ByteBuffer paramByteBuffer);
  
  int FHDEV_NET_PTZTrack(Pointer paramPointer, int paramInt1, int paramInt2, int paramInt3);
  
  @Deprecated
  int FHDEV_NET_PTZTrans(Pointer paramPointer1, int paramInt1, Pointer paramPointer2, int paramInt2);
  
  int FHDEV_NET_PTZTrans(Pointer paramPointer, int paramInt1, ByteBuffer paramByteBuffer, int paramInt2);
  
  @Deprecated
  int FHDEV_NET_ReadReg(Pointer paramPointer1, int paramInt1, int paramInt2, int paramInt3, Pointer paramPointer2);
  
  int FHDEV_NET_ReadReg(Pointer paramPointer, int paramInt1, int paramInt2, int paramInt3, ByteBuffer paramByteBuffer);
  
  int FHDEV_NET_RecDownloadControl(Pointer paramPointer, int paramInt);
  
  int FHDEV_NET_RecPlayCheckTime(Pointer paramPointer, int paramInt);
  
  int FHDEV_NET_RecPlayCtrl(Pointer paramPointer, int paramInt1, int paramInt2);
  
  @Deprecated
  int FHDEV_NET_SaveData(Pointer paramPointer1, Pointer paramPointer2, int paramInt1, int paramInt2);
  
  int FHDEV_NET_SaveData(Pointer paramPointer, ByteBuffer paramByteBuffer, int paramInt1, int paramInt2);
  
  Pointer FHDEV_NET_SearchBookMark(Pointer paramPointer, LPFHNP_BookMarkSearch_t_struct paramLPFHNP_BookMarkSearch_t_struct);
  
  Pointer FHDEV_NET_SearchLog(Pointer paramPointer, LPFHNP_LogSearch_t_struct paramLPFHNP_LogSearch_t_struct);
  
  int FHDEV_NET_SearchNextBookMark(Pointer paramPointer, LPFHNP_BookMark_t_struct paramLPFHNP_BookMark_t_struct);
  
  int FHDEV_NET_SearchNextLog(Pointer paramPointer, LPFHNP_Log_t_struct paramLPFHNP_Log_t_struct);
  
  @Deprecated
  int FHDEV_NET_SetBuffer(Pointer paramPointer1, byte paramByte1, byte paramByte2, Pointer paramPointer2, int paramInt);
  
  int FHDEV_NET_SetBuffer(Pointer paramPointer, byte paramByte1, byte paramByte2, ByteBuffer paramByteBuffer, int paramInt);
  
  int FHDEV_NET_SetDebugParam(LPFHNP_DebugParam_t_struct paramLPFHNP_DebugParam_t_struct);
  
  int FHDEV_NET_SetEncodeWaterMark(Pointer paramPointer, byte paramByte1, byte paramByte2, LPFHNP_EncodeWaterMark_t_struct paramLPFHNP_EncodeWaterMark_t_struct);
  
  @Deprecated
  int FHDEV_NET_SetLogToFile(int paramInt, Pointer paramPointer);
  
  int FHDEV_NET_SetLogToFile(int paramInt, ByteBuffer paramByteBuffer);
  
  int FHDEV_NET_SetPrivateDebugParam(LPFHNP_PriDebugParam_t_struct paramLPFHNP_PriDebugParam_t_struct);
  
  @Deprecated
  Pointer FHDEV_NET_StartConvertRecFormat(Pointer paramPointer1, Pointer paramPointer2, int paramInt);
  
  Pointer FHDEV_NET_StartConvertRecFormat(ByteBuffer paramByteBuffer1, ByteBuffer paramByteBuffer2, int paramInt);
  
  @Deprecated
  Pointer FHDEV_NET_StartRecPlay(Pointer paramPointer1, int paramInt, FHDEV_NetLibrary.fDataCallBack paramfDataCallBack, Pointer paramPointer2);
  
  Pointer FHDEV_NET_StartRecPlay(ByteBuffer paramByteBuffer, int paramInt, FHDEV_NetLibrary.fDataCallBack paramfDataCallBack, Pointer paramPointer);
  
  @Deprecated
  Pointer FHDEV_NET_StartUDPPlay(Pointer paramPointer1, short paramShort, Pointer paramPointer2, int paramInt, FHDEV_NetLibrary.fDataCallBack paramfDataCallBack, Pointer paramPointer3);
  
  Pointer FHDEV_NET_StartUDPPlay(ByteBuffer paramByteBuffer1, short paramShort, ByteBuffer paramByteBuffer2, int paramInt, FHDEV_NetLibrary.fDataCallBack paramfDataCallBack, Pointer paramPointer);
  
  int FHDEV_NET_StopConvertRecFormat(Pointer paramPointer);
  
  int FHDEV_NET_StopRecPlay(Pointer paramPointer);
  
  int FHDEV_NET_StopSaveData(Pointer paramPointer);
  
  int FHDEV_NET_StopUDPPlay(Pointer paramPointer);
  
  int FHDEV_NET_V2_GetEncodeVideo(Pointer paramPointer, byte paramByte, LPFHNP_EncHead_t_struct paramLPFHNP_EncHead_t_struct, LPFHNP_EncodeVideoV2_t_struct paramLPFHNP_EncodeVideoV2_t_struct);
  
  int FHDEV_NET_V2_GetEncodeVideoAdv(Pointer paramPointer, byte paramByte, LPFHNP_EncHead_t_struct paramLPFHNP_EncHead_t_struct, LPFHNP_EncodeVAdvancedV2_t_struct paramLPFHNP_EncodeVAdvancedV2_t_struct);
  
  @Deprecated
  int FHDEV_NET_V2_GetVideoRotate(Pointer paramPointer, byte paramByte1, byte paramByte2, IntByReference paramIntByReference);
  
  int FHDEV_NET_V2_GetVideoRotate(Pointer paramPointer, byte paramByte1, byte paramByte2, IntBuffer paramIntBuffer);
  
  int FHDEV_NET_V2_SetEncodeVideo(Pointer paramPointer, byte paramByte, LPFHNP_EncHead_t_struct paramLPFHNP_EncHead_t_struct, LPFHNP_EncodeVideoV2_t_struct paramLPFHNP_EncodeVideoV2_t_struct);
  
  int FHDEV_NET_V2_SetEncodeVideoAdv(Pointer paramPointer, byte paramByte, LPFHNP_EncHead_t_struct paramLPFHNP_EncHead_t_struct, LPFHNP_EncodeVAdvancedV2_t_struct paramLPFHNP_EncodeVAdvancedV2_t_struct);
  
  @Deprecated
  int FHDEV_NET_V2_SetVideoRotate(Pointer paramPointer, byte paramByte1, byte paramByte2, IntByReference paramIntByReference);
  
  int FHDEV_NET_V2_SetVideoRotate(Pointer paramPointer, byte paramByte1, byte paramByte2, IntBuffer paramIntBuffer);
  
  @Deprecated
  int FHDEV_NET_WriteReg(Pointer paramPointer1, int paramInt1, int paramInt2, int paramInt3, Pointer paramPointer2);
  
  int FHDEV_NET_WriteReg(Pointer paramPointer, int paramInt1, int paramInt2, int paramInt3, ByteBuffer paramByteBuffer);
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhapi/FHDEV_NetExLibrary.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */