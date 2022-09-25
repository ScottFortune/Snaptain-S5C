package com.fh.lib;

import android.graphics.Rect;

public class FHSDK {
  public static final int PLAY_TYPE_LOCATE_PLAYBACK = 3;
  
  public static final int PLAY_TYPE_MP4FILE = 4;
  
  public static final int PLAY_TYPE_PREVIEW = 0;
  
  public static final int PLAY_TYPE_REMOTE_PLAYBACK = 2;
  
  public static final int PLAY_TYPE_UDP = 1;
  
  static {
    System.loadLibrary("EyeViewJni");
  }
  
  public static native boolean adjustCircle(byte[] paramArrayOfbyte, int paramInt1, int paramInt2, int paramInt3, Define.Circle paramCircle);
  
  public static native boolean apiCleanup();
  
  public static native boolean apiInit();
  
  public static native boolean bind(long paramLong1, long paramLong2);
  
  public static native boolean clear();
  
  public static native boolean closeAudio(int paramInt);
  
  public static native boolean closeSearchPicture(long paramLong);
  
  public static native boolean closeSearchRecord(long paramLong);
  
  public static native boolean continuePBPlay();
  
  public static native long createBuffer(int paramInt);
  
  public static native long createPicDownload(long paramLong, Define.Picture paramPicture);
  
  public static native long createWindow(int paramInt);
  
  public static native boolean destoryPicDownload(long paramLong);
  
  public static native boolean destroyBuffer(long paramLong);
  
  public static native boolean destroyWindow(long paramLong);
  
  public static native boolean draw(long paramLong);
  
  public static native boolean expandLookAt(long paramLong, float paramFloat);
  
  public static native boolean eyeLookAt(long paramLong, float paramFloat1, float paramFloat2, float paramFloat3);
  
  public static native boolean eyeLookAtEx(long paramLong, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4);
  
  public static native boolean frameParse(long paramLong, byte[] paramArrayOfbyte, int paramInt);
  
  public static native int getConvertProgress(long paramLong);
  
  public static native long getCurrentPts();
  
  public static native int getDevStatus();
  
  public static native int getDeviceFlag(long paramLong);
  
  public static native boolean getDeviceTime(long paramLong, Define.DeviceTime paramDeviceTime);
  
  public static native int getDisplayMode(long paramLong);
  
  public static native int getDisplayType(long paramLong);
  
  public static native boolean getEncodeVideoConfig(long paramLong, int paramInt, Define.VideoEncode paramVideoEncode);
  
  public static native int getFieldOfView(long paramLong);
  
  public static native boolean getIPConfig(long paramLong, Define.IpConfig paramIpConfig);
  
  public static native int getImagingType(long paramLong);
  
  public static native boolean getInterruptFlag();
  
  public static native int getMDAlarm();
  
  public static native float getMaxHDegress(long paramLong);
  
  public static native float getMaxVDegress(long paramLong);
  
  public static native float getMaxZDepth(long paramLong);
  
  public static native float getMinHDegress(long paramLong);
  
  public static native float getMinVDegress(long paramLong);
  
  public static native boolean getRealAudioState();
  
  public static native int getRecPlayProgress();
  
  public static native boolean getRecPlayTimeInfo(Define.PBRecTime paramPBRecTime);
  
  public static native boolean getRemoteRecordState(long paramLong);
  
  public static native boolean getSDCardFormatState(Define.SDCardFormat paramSDCardFormat);
  
  public static native boolean getSDCardInfo(long paramLong, Define.SDCardInfo paramSDCardInfo);
  
  public static native boolean getSerialPortConfig(long paramLong, Define.SerialPortCfg paramSerialPortCfg);
  
  public static native int getTalkUnitSize(long paramLong);
  
  public static native float getVerticalCutRatio(long paramLong);
  
  public static native boolean getVideoBCSS(long paramLong, Define.BCSS paramBCSS);
  
  public static native float getViewAngle(long paramLong);
  
  public static native boolean getWifiConfig(long paramLong, Define.WifiConfig paramWifiConfig);
  
  public static native boolean init(int paramInt1, int paramInt2);
  
  public static native boolean isBind(long paramLong);
  
  public static native boolean jumpPlayBack(long paramLong);
  
  public static native boolean loadSDCard(long paramLong);
  
  public static native boolean locateContinuePBPlay();
  
  public static native boolean locateJumpPlayBack(int paramInt);
  
  public static native boolean locatePausePBPlay();
  
  public static native boolean locatePlayFrame();
  
  public static native long login(String paramString1, int paramInt, String paramString2, String paramString3);
  
  public static native boolean logout(long paramLong);
  
  public static native boolean mirrorCtrl(long paramLong, int paramInt);
  
  public static native int mp4GetCurSec();
  
  public static native int mp4GetFileDuration();
  
  public static native int mp4GetPlayStatus();
  
  public static native boolean mp4SeekTo(int paramInt);
  
  public static native boolean mp4SetPlayStatus(int paramInt);
  
  public static native boolean openAudio(int paramInt);
  
  public static native boolean pausePBPlay();
  
  public static native boolean playFrame();
  
  public static native int readTextureID(long paramLong, int paramInt);
  
  public static native boolean registerDevNotifyFun();
  
  public static native boolean registerDevStateFun();
  
  public static native void registerNotifyCallBack(Define.CbDataInterface paramCbDataInterface);
  
  public static native void registerStreamDataCallBack(Define.StreamDataCallBackInterface paramStreamDataCallBackInterface);
  
  public static native void registerUpdateCallBack(Define.YUVDataCallBackInterface paramYUVDataCallBackInterface);
  
  public static native boolean resetDev(long paramLong);
  
  public static native boolean resetEyeView(long paramLong);
  
  public static native boolean resetStandardCircle(long paramLong);
  
  public static native boolean restartDev(long paramLong);
  
  public static native boolean saveBMP(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3, int paramInt1, int paramInt2);
  
  public static native boolean saveDevConfig(long paramLong);
  
  public static native boolean searchCleanup();
  
  public static native long searchDev();
  
  public static native boolean searchDevClose(long paramLong);
  
  public static native boolean searchInit();
  
  public static native boolean searchNextDev(long paramLong, Define.DevSearch paramDevSearch);
  
  public static native boolean searchNextPicture(long paramLong, Define.Picture paramPicture);
  
  public static native boolean searchNextRecord(long paramLong, Define.Record paramRecord);
  
  public static native long searchPicture(long paramLong, Define.PicSearch paramPicSearch);
  
  public static native long searchRecord(long paramLong, Define.RecSearch paramRecSearch);
  
  public static native boolean send2Sdl(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3, int paramInt1, int paramInt2);
  
  public static native boolean sendSerial(long paramLong, byte[] paramArrayOfbyte, int paramInt);
  
  public static native boolean sendTalkData(byte[] paramArrayOfbyte, int paramInt1, int paramInt2, int paramInt3);
  
  public static native boolean sendToSerialPort(long paramLong, int paramInt1, int paramInt2, byte[] paramArrayOfbyte, int paramInt3);
  
  public static native boolean setCryptKey(String paramString);
  
  public static native boolean setDebugMode(long paramLong, byte[] paramArrayOfbyte, int paramInt1, int paramInt2);
  
  public static native boolean setDevName(long paramLong, String paramString);
  
  public static native boolean setDeviceTime(long paramLong, Define.DeviceTime paramDeviceTime);
  
  public static native boolean setDisplayType(long paramLong, int paramInt);
  
  public static native boolean setEncodeVideoConfig(long paramLong, int paramInt, Define.VideoEncode paramVideoEncode);
  
  public static native boolean setFieldOfView(long paramLong, int paramInt);
  
  public static native boolean setIPConfig(long paramLong, Define.IpConfig paramIpConfig);
  
  public static native boolean setImagingType(long paramLong, int paramInt);
  
  public static native void setInt(long paramLong, int paramInt1, int paramInt2);
  
  public static native boolean setLocatePBSpeed(int paramInt);
  
  public static native boolean setPBSpeed(int paramInt);
  
  public static native boolean setSerialPortConfig(long paramLong, Define.SerialPortCfg paramSerialPortCfg);
  
  public static native void setShotOn();
  
  public static native void setShotPath(String paramString);
  
  public static native boolean setShowRect(Rect paramRect, boolean paramBoolean);
  
  public static native boolean setStandardCircle(long paramLong, float paramFloat1, float paramFloat2, float paramFloat3);
  
  public static native boolean setVerticalCutRatio(long paramLong, float paramFloat);
  
  public static native boolean setVideoBCSS(long paramLong, Define.BCSS paramBCSS);
  
  public static native boolean setWifiConfig(long paramLong, Define.WifiConfig paramWifiConfig);
  
  public static native boolean shot(long paramLong, String paramString, boolean paramBoolean);
  
  public static native byte[] snapshot(int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean);
  
  public static native long startConvertRecFormat(String paramString1, String paramString2);
  
  public static native boolean startLocalRecord(int paramInt, String paramString);
  
  public static native boolean startPlay();
  
  public static native boolean startRemoteRecord(long paramLong);
  
  public static native boolean startSDCardFormat(long paramLong, int paramInt);
  
  public static native long startSerial(long paramLong, int paramInt1, int paramInt2, Define.SerialDataCallBackInterface paramSerialDataCallBackInterface);
  
  public static native long startSerialEx(long paramLong, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean, Define.SerialDataCallBackInterface paramSerialDataCallBackInterface);
  
  public static native boolean startTalk(long paramLong);
  
  public static native boolean stopConvertRecFormat(long paramLong);
  
  public static native boolean stopLocalRecord();
  
  public static native boolean stopPlay();
  
  public static native boolean stopRemoteRecord(long paramLong);
  
  public static native boolean stopSDCardFormat();
  
  public static native boolean stopSerial(long paramLong);
  
  public static native boolean stopTalk();
  
  public static native boolean testWifiConfig(long paramLong, Define.WifiConfig paramWifiConfig);
  
  public static native String timeConvert(long paramLong1, long paramLong2);
  
  public static native boolean unInit();
  
  public static native boolean unLoadSDCard(long paramLong);
  
  public static native boolean unbind(long paramLong);
  
  public static native boolean update(long paramLong, byte[] paramArrayOfbyte, int paramInt1, int paramInt2);
  
  public static native boolean viewport(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  public static native boolean yuv420sp2yuv(byte[] paramArrayOfbyte1, int paramInt1, int paramInt2, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3, byte[] paramArrayOfbyte4);
  
  public static native boolean yuv420sp2yuv420p(byte[] paramArrayOfbyte1, int paramInt1, int paramInt2, byte[] paramArrayOfbyte2);
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/fh/lib/FHSDK.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */