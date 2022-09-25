package com.netopsun.fhdevices;

import android.media.MediaMuxer;
import android.util.Log;
import com.google.gson.Gson;
import com.netopsun.deviceshub.base.CMDCommunicator;
import com.netopsun.deviceshub.base.bean.RemoteVideoFiles;
import com.netopsun.deviceshub.interfaces.Cancelable;
import com.netopsun.deviceshub.interfaces.NothingCancel;
import com.netopsun.fhapi.FHDEV_NetExLibrary;
import com.netopsun.fhapi.FHDEV_NetLibrary;
import com.netopsun.fhapi.FHNP_Record_t;
import com.netopsun.fhapi.FHNP_Time_t;
import com.netopsun.fhapi.LPFHNP_EncodeVideo_t_struct;
import com.netopsun.fhapi.LPFHNP_Notify_t_struct;
import com.netopsun.fhapi.LPFHNP_RecSearch_t_struct;
import com.netopsun.fhapi.LPFHNP_Record_t_struct;
import com.netopsun.fhapi.LPFHNP_SDCardInfo_t_struct;
import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class FHCMDCommunicator extends CMDCommunicator {
  private static final String TAG = "FHCMDCommunicator";
  
  private static Disposable connectTask;
  
  final FHDevices fhDevices;
  
  volatile boolean isConnect = false;
  
  private final FHDEV_NetLibrary.fNotifyCallBack remoteCMDNotifyCallBack = new FHDEV_NetLibrary.fNotifyCallBack() {
      private boolean isRemoteRecording = false;
      
      public void apply(LPFHNP_Notify_t_struct param1LPFHNP_Notify_t_struct, Pointer param1Pointer) {
        if (FHCMDCommunicator.this.onRemoteCMDListener == null)
          return; 
        param1LPFHNP_Notify_t_struct.read();
        if (param1LPFHNP_Notify_t_struct.btNotify == 83) {
          byte b = param1LPFHNP_Notify_t_struct.btFlag;
          if (b == 10) {
            FHCMDCommunicator.this.onRemoteCMDListener.onRemoteTakePhoto();
          } else if (b == 11) {
            FHCMDCommunicator.this.onRemoteCMDListener.onRemoteStartRecord();
          } else if (b == 12) {
            FHCMDCommunicator.this.onRemoteCMDListener.onRemoteStopRecord();
          } 
          if (b == 1) {
            FHCMDCommunicator.this.onRemoteCMDListener.onRemoteTakePhoto();
          } else if (b == 2) {
            if (this.isRemoteRecording) {
              FHCMDCommunicator.this.onRemoteCMDListener.onRemoteStartRecord();
            } else {
              FHCMDCommunicator.this.onRemoteCMDListener.onRemoteStopRecord();
            } 
            this.isRemoteRecording ^= 0x1;
          } 
        } 
      }
    };
  
  protected FHCMDCommunicator(FHDevices paramFHDevices) {
    super(paramFHDevices);
    this.fhDevices = paramFHDevices;
  }
  
  private void getDeviceParam() {}
  
  protected static LPFHNP_RecSearch_t_struct getSearchReq(String paramString) {
    LPFHNP_RecSearch_t_struct lPFHNP_RecSearch_t_struct = new LPFHNP_RecSearch_t_struct();
    lPFHNP_RecSearch_t_struct.btLockFlag = (byte)0;
    lPFHNP_RecSearch_t_struct.btChanNum = (byte)(byte)lPFHNP_RecSearch_t_struct.btChannel.length;
    for (byte b = 0; b < lPFHNP_RecSearch_t_struct.btChannel.length; b++)
      lPFHNP_RecSearch_t_struct.btChannel[b] = (byte)1; 
    lPFHNP_RecSearch_t_struct.dwRecTypeMask = 0;
    FHNP_Time_t fHNP_Time_t2 = new FHNP_Time_t();
    lPFHNP_RecSearch_t_struct.stStartTime = fHNP_Time_t2;
    fHNP_Time_t2.year = (short)1970;
    fHNP_Time_t2.month = (byte)1;
    fHNP_Time_t2.day = (byte)1;
    if (paramString != null && paramString.length() == 17)
      try {
        fHNP_Time_t2.year = Short.valueOf(paramString.substring(0, 4)).shortValue();
        fHNP_Time_t2.month = Byte.valueOf(paramString.substring(4, 6)).byteValue();
        fHNP_Time_t2.day = Byte.valueOf(paramString.substring(6, 8)).byteValue();
        fHNP_Time_t2.hour = Byte.valueOf(paramString.substring(8, 10)).byteValue();
        fHNP_Time_t2.minute = Byte.valueOf(paramString.substring(10, 12)).byteValue();
        fHNP_Time_t2.second = Byte.valueOf(paramString.substring(12, 14)).byteValue();
        fHNP_Time_t2.msecond = Integer.valueOf(paramString.substring(14, 17)).intValue();
      } catch (Exception exception) {
        exception.printStackTrace();
      }  
    fHNP_Time_t2.setAutoSynch(true);
    fHNP_Time_t2.write();
    FHNP_Time_t fHNP_Time_t1 = new FHNP_Time_t();
    lPFHNP_RecSearch_t_struct.stStopTime = fHNP_Time_t1;
    fHNP_Time_t1.year = (short)2038;
    fHNP_Time_t1.month = (byte)1;
    fHNP_Time_t1.day = (byte)1;
    fHNP_Time_t1.setAutoSynch(true);
    fHNP_Time_t1.write();
    lPFHNP_RecSearch_t_struct.setAutoSynch(true);
    lPFHNP_RecSearch_t_struct.write();
    return lPFHNP_RecSearch_t_struct;
  }
  
  private Disposable runCMDAsync(final CMDRunnable runnable) {
    return Observable.create(new ObservableOnSubscribe<Object>() {
          public void subscribe(ObservableEmitter<Object> param1ObservableEmitter) throws Exception {
            Pointer pointer = FHCMDCommunicator.this.fhDevices.getUserID();
            runnable.run(pointer, param1ObservableEmitter);
            param1ObservableEmitter.onComplete();
          }
        }).subscribeOn(Schedulers.io()).subscribe();
  }
  
  public int connectInternal() {
    getDeviceParam();
    syncDevicesTime(false, 5, (CMDCommunicator.OnExecuteCMDResult)null);
    getRemoteSDCardStatus(false, 5, (CMDCommunicator.OnExecuteCMDResult)null);
    FHDEV_NetLibrary.INSTANCE.FHDEV_NET_RegisterDevNotifyFun(this.remoteCMDNotifyCallBack, Pointer.NULL);
    this.isConnect = true;
    return 0;
  }
  
  public Cancelable deleteRemoteRecordFile(boolean paramBoolean, int paramInt, final String fileName, final CMDCommunicator.OnExecuteCMDResult onExecuteCMDResult) {
    runCMDAsync(new CMDRunnable() {
          public void run(Pointer param1Pointer, ObservableEmitter<Object> param1ObservableEmitter) {
            CMDCommunicator.OnExecuteCMDResult onExecuteCMDResult1;
            if (param1Pointer == null) {
              onExecuteCMDResult1 = onExecuteCMDResult;
              if (onExecuteCMDResult1 != null)
                onExecuteCMDResult1.onResult(-1, "not login"); 
              return;
            } 
            LPFHNP_RecSearch_t_struct lPFHNP_RecSearch_t_struct = FHCMDCommunicator.getSearchReq(fileName);
            Pointer pointer = FHDEV_NetLibrary.INSTANCE.FHDEV_NET_SearchRecord((Pointer)onExecuteCMDResult1, lPFHNP_RecSearch_t_struct);
            if (pointer == null) {
              onExecuteCMDResult1 = onExecuteCMDResult;
              if (onExecuteCMDResult1 != null) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("not found:");
                stringBuilder.append(FHDEV_NetLibrary.INSTANCE.FHDEV_NET_GetLastError());
                onExecuteCMDResult1.onResult(-1, stringBuilder.toString());
              } 
              param1ObservableEmitter.onComplete();
              return;
            } 
            LPFHNP_Record_t_struct lPFHNP_Record_t_struct = new LPFHNP_Record_t_struct();
            if (FHDEV_NetLibrary.INSTANCE.FHDEV_NET_SearchNextRecord(pointer, lPFHNP_Record_t_struct) == 0) {
              onExecuteCMDResult1 = onExecuteCMDResult;
              if (onExecuteCMDResult1 != null)
                onExecuteCMDResult1.onResult(-1, "no match file!"); 
              param1ObservableEmitter.onComplete();
              return;
            } 
            lPFHNP_Record_t_struct.read();
            FHDEV_NetLibrary.INSTANCE.FHDEV_NET_CloseSearchRecord(pointer);
            FHNP_Record_t fHNP_Record_t = new FHNP_Record_t(lPFHNP_Record_t_struct.getPointer());
            fHNP_Record_t.read();
            FHDEV_NetLibrary fHDEV_NetLibrary = FHDEV_NetLibrary.INSTANCE;
            boolean bool = true;
            if (fHDEV_NetLibrary.FHDEV_NET_DeleteRecord((Pointer)onExecuteCMDResult1, 1, new FHNP_Record_t[] { fHNP_Record_t }) == 0)
              bool = false; 
            CMDCommunicator.OnExecuteCMDResult onExecuteCMDResult2 = onExecuteCMDResult;
            if (onExecuteCMDResult2 != null)
              if (bool) {
                onExecuteCMDResult2.onResult(0, "suceess");
              } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("");
                stringBuilder.append(FHDEV_NetLibrary.INSTANCE.FHDEV_NET_GetLastError());
                onExecuteCMDResult2.onResult(-2, stringBuilder.toString());
              }  
          }
        });
    return super.deleteRemoteRecordFile(paramBoolean, paramInt, fileName, onExecuteCMDResult);
  }
  
  public int disconnectInternal() {
    this.isConnect = false;
    return 0;
  }
  
  public Cancelable downloadRemoteRecordFile(boolean paramBoolean, int paramInt, final String fileName, final String path, final CMDCommunicator.OnExecuteCMDResult onExecuteCMDResult) {
    final CountDownLatch countDownLatch = new CountDownLatch(1);
    return new Disposable2Cancelable(Observable.create(new ObservableOnSubscribe<Object>() {
            public void subscribe(ObservableEmitter<Object> param1ObservableEmitter) throws Exception {
              CMDCommunicator.OnExecuteCMDResult onExecuteCMDResult1;
              CMDCommunicator.OnExecuteCMDResult onExecuteCMDResult2;
              Pointer pointer1 = FHCMDCommunicator.this.fhDevices.getUserID();
              if (pointer1 == null) {
                onExecuteCMDResult1 = onExecuteCMDResult;
                if (onExecuteCMDResult1 != null)
                  onExecuteCMDResult1.onResult(-1, "not login"); 
                return;
              } 
              LPFHNP_RecSearch_t_struct lPFHNP_RecSearch_t_struct = FHCMDCommunicator.getSearchReq(fileName);
              Pointer pointer2 = FHDEV_NetLibrary.INSTANCE.FHDEV_NET_SearchRecord(pointer1, lPFHNP_RecSearch_t_struct);
              if (pointer2 == null) {
                onExecuteCMDResult2 = onExecuteCMDResult;
                if (onExecuteCMDResult2 != null)
                  onExecuteCMDResult2.onResult(FHDEV_NetLibrary.INSTANCE.FHDEV_NET_GetLastError(), ""); 
                onExecuteCMDResult1.onComplete();
                return;
              } 
              LPFHNP_Record_t_struct lPFHNP_Record_t_struct = new LPFHNP_Record_t_struct();
              if (FHDEV_NetLibrary.INSTANCE.FHDEV_NET_SearchNextRecord((Pointer)onExecuteCMDResult2, lPFHNP_Record_t_struct) == 0) {
                onExecuteCMDResult2 = onExecuteCMDResult;
                if (onExecuteCMDResult2 != null)
                  onExecuteCMDResult2.onResult(-1, "no match file!"); 
                onExecuteCMDResult1.onComplete();
                return;
              } 
              lPFHNP_Record_t_struct.read();
              FHDEV_NetLibrary.INSTANCE.FHDEV_NET_CloseSearchRecord((Pointer)onExecuteCMDResult2);
              int i = 20;
              LPFHNP_EncodeVideo_t_struct lPFHNP_EncodeVideo_t_struct = new LPFHNP_EncodeVideo_t_struct();
              FHDEV_NetLibrary fHDEV_NetLibrary = FHDEV_NetLibrary.INSTANCE;
              boolean bool = true;
              if (fHDEV_NetLibrary.FHDEV_NET_GetEncodeVideo(pointer1, (byte)0, (byte)1, lPFHNP_EncodeVideo_t_struct) != 0) {
                lPFHNP_EncodeVideo_t_struct.read();
                i = lPFHNP_EncodeVideo_t_struct.iFrameRate;
              } 
              String str2 = path;
              File file = new File(str2.substring(0, str2.lastIndexOf(File.separator)));
              if (!file.exists())
                file.mkdirs(); 
              String str3 = path;
              String str1 = str3;
              if (!str3.toLowerCase().endsWith(".mp4")) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(str3);
                stringBuilder.append(".mp4");
                str1 = stringBuilder.toString();
              } 
              try {
                MediaMuxer mediaMuxer = new MediaMuxer();
                this(str1, 0);
                RemoteRecordFileDownloadCallback remoteRecordFileDownloadCallback = new RemoteRecordFileDownloadCallback(mediaMuxer, i, countDownLatch, onExecuteCMDResult);
                Pointer pointer = FHDEV_NetLibrary.INSTANCE.FHDEV_NET_CreateRecDownload(pointer1, lPFHNP_Record_t_struct, 0, remoteRecordFileDownloadCallback, Pointer.NULL);
                if (pointer == null) {
                  mediaMuxer.stop();
                  mediaMuxer.release();
                  File file1 = new File(str1);
                  if (file1.exists())
                    file1.delete(); 
                  onExecuteCMDResult1 = onExecuteCMDResult;
                  if (onExecuteCMDResult1 != null)
                    onExecuteCMDResult1.onResult(-1, "FHDEV_NET_CreateRecDownload failed!"); 
                  return;
                } 
                try {
                  countDownLatch.await();
                } catch (InterruptedException interruptedException) {
                  interruptedException.printStackTrace();
                } 
                FHDEV_NetLibrary.INSTANCE.FHDEV_NET_DestoryRecDownload(pointer);
                try {
                  mediaMuxer.stop();
                  i = 0;
                } catch (Exception exception) {
                  exception.printStackTrace();
                  i = bool;
                } 
                mediaMuxer.release();
                if (onExecuteCMDResult != null)
                  if (onExecuteCMDResult1.isDisposed() || i != 0) {
                    File file1 = new File(str1);
                    if (file1.exists())
                      file1.delete(); 
                    if (i != 0) {
                      onExecuteCMDResult.onResult(-1, "muxer fail");
                    } else {
                      onExecuteCMDResult.onResult(-1, "cancel by user");
                    } 
                  } else {
                    onExecuteCMDResult.onResult(0, "");
                  }  
                onExecuteCMDResult1.onComplete();
                return;
              } catch (Exception exception) {
                exception.printStackTrace();
                CMDCommunicator.OnExecuteCMDResult onExecuteCMDResult = onExecuteCMDResult;
                if (onExecuteCMDResult != null)
                  onExecuteCMDResult.onResult(-1, "MediaMuxer creation failed!"); 
                onExecuteCMDResult1.onComplete();
                return;
              } 
            }
          }).subscribeOn(Schedulers.newThread()).doOnDispose(new Action() {
            public void run() throws Exception {
              countDownLatch.countDown();
            }
          }).subscribe());
  }
  
  public Cancelable getRemoteRecordFileList(boolean paramBoolean, int paramInt1, final int fileCount, final String fileNameBefore, final CMDCommunicator.OnExecuteCMDResult onExecuteCMDResult) {
    return new Disposable2Cancelable(runCMDAsync(new CMDRunnable() {
            public void run(Pointer param1Pointer, ObservableEmitter<Object> param1ObservableEmitter) {
              CMDCommunicator.OnExecuteCMDResult onExecuteCMDResult;
              if (param1Pointer == null) {
                onExecuteCMDResult = onExecuteCMDResult;
                if (onExecuteCMDResult != null)
                  onExecuteCMDResult.onResult(-1, "not login"); 
                return;
              } 
              LPFHNP_RecSearch_t_struct lPFHNP_RecSearch_t_struct = FHCMDCommunicator.getSearchReq(fileNameBefore);
              Pointer pointer = FHDEV_NetLibrary.INSTANCE.FHDEV_NET_SearchRecord((Pointer)onExecuteCMDResult, lPFHNP_RecSearch_t_struct);
              if (pointer == null) {
                onExecuteCMDResult = onExecuteCMDResult;
                if (onExecuteCMDResult != null)
                  onExecuteCMDResult.onResult(FHDEV_NetLibrary.INSTANCE.FHDEV_NET_GetLastError(), ""); 
                param1ObservableEmitter.onComplete();
                return;
              } 
              RemoteVideoFiles remoteVideoFiles = new RemoteVideoFiles();
              List<RemoteVideoFiles.RemoteVideoFilesBean> list = remoteVideoFiles.getRemote_video_files();
              for (byte b = 0; b < fileCount && !param1ObservableEmitter.isDisposed(); b++) {
                LPFHNP_Record_t_struct lPFHNP_Record_t_struct = new LPFHNP_Record_t_struct();
                if (FHDEV_NetLibrary.INSTANCE.FHDEV_NET_SearchNextRecord(pointer, lPFHNP_Record_t_struct) == 0)
                  break; 
                lPFHNP_Record_t_struct.setAutoSynch(true);
                lPFHNP_Record_t_struct.autoRead();
                FHNP_Time_t fHNP_Time_t = new FHNP_Time_t();
                FHDEV_NetLibrary.INSTANCE.FHDEV_NET_TimeConvertEx((Pointer)onExecuteCMDResult, lPFHNP_Record_t_struct.ullStartTime, fHNP_Time_t);
                fHNP_Time_t.setAutoSynch(true);
                fHNP_Time_t.autoRead();
                RemoteVideoFiles.RemoteVideoFilesBean remoteVideoFilesBean = new RemoteVideoFiles.RemoteVideoFilesBean();
                String str = String.format("%04d%02d%02d%02d%02d%02d%03d", new Object[] { Short.valueOf(fHNP_Time_t.year), Byte.valueOf(fHNP_Time_t.month), Byte.valueOf(fHNP_Time_t.day), Byte.valueOf(fHNP_Time_t.hour), Byte.valueOf(fHNP_Time_t.minute), Byte.valueOf(fHNP_Time_t.second), Integer.valueOf(fHNP_Time_t.msecond) });
                if (str.equals(fileNameBefore)) {
                  b--;
                } else {
                  remoteVideoFilesBean.setVideo_name(str);
                  StringBuilder stringBuilder = new StringBuilder();
                  stringBuilder.append(lPFHNP_Record_t_struct.ullStartTime / 1000L);
                  stringBuilder.append("");
                  remoteVideoFilesBean.setRecord_start_time(stringBuilder.toString());
                  stringBuilder = new StringBuilder();
                  long l = lPFHNP_Record_t_struct.ullStopTime;
                  stringBuilder.append((l - lPFHNP_Record_t_struct.ullStartTime) / 1000L);
                  stringBuilder.append("");
                  remoteVideoFilesBean.setDuration(stringBuilder.toString());
                  list.add(remoteVideoFilesBean);
                } 
              } 
              FHDEV_NetLibrary.INSTANCE.FHDEV_NET_CloseSearchRecord(pointer);
              if (onExecuteCMDResult != null)
                if (param1ObservableEmitter.isDisposed()) {
                  onExecuteCMDResult.onResult(-1, "cancel by user");
                } else {
                  onExecuteCMDResult.onResult(0, (new Gson()).toJson(remoteVideoFiles));
                }  
              param1ObservableEmitter.onComplete();
            }
          }));
  }
  
  public Cancelable getRemoteSDCardStatus(boolean paramBoolean, int paramInt, final CMDCommunicator.OnExecuteCMDResult onExecuteCMDResult) {
    runCMDAsync(new CMDRunnable() {
          public void run(Pointer param1Pointer, ObservableEmitter<Object> param1ObservableEmitter) {
            CMDCommunicator.OnExecuteCMDResult onExecuteCMDResult;
            if (param1Pointer == null) {
              onExecuteCMDResult = onExecuteCMDResult;
              if (onExecuteCMDResult != null)
                onExecuteCMDResult.onResult(-1, "not login"); 
              return;
            } 
            LPFHNP_SDCardInfo_t_struct lPFHNP_SDCardInfo_t_struct = new LPFHNP_SDCardInfo_t_struct();
            if (FHDEV_NetLibrary.INSTANCE.FHDEV_NET_GetSDCardInfo((Pointer)onExecuteCMDResult, lPFHNP_SDCardInfo_t_struct) != 0) {
              lPFHNP_SDCardInfo_t_struct.read();
              if ((lPFHNP_SDCardInfo_t_struct.btState & 0x2) == 2) {
                FHCMDCommunicator.this.SDCardState = 1;
                onExecuteCMDResult = onExecuteCMDResult;
                if (onExecuteCMDResult != null)
                  onExecuteCMDResult.onResult(0, "1"); 
              } else {
                FHCMDCommunicator.this.SDCardState = 0;
                onExecuteCMDResult = onExecuteCMDResult;
                if (onExecuteCMDResult != null)
                  onExecuteCMDResult.onResult(0, "0"); 
              } 
            } else {
              onExecuteCMDResult = onExecuteCMDResult;
              if (onExecuteCMDResult != null)
                onExecuteCMDResult.onResult(-1, "fail"); 
            } 
          }
        });
    return (Cancelable)new NothingCancel();
  }
  
  public boolean isConnected() {
    return this.isConnect;
  }
  
  public Cancelable remoteStartRecord(boolean paramBoolean, int paramInt, final CMDCommunicator.OnExecuteCMDResult onExecuteCMDResult) {
    runCMDAsync(new CMDRunnable() {
          public void run(Pointer param1Pointer, ObservableEmitter<Object> param1ObservableEmitter) {
            CMDCommunicator.OnExecuteCMDResult onExecuteCMDResult;
            if (param1Pointer == null) {
              onExecuteCMDResult = onExecuteCMDResult;
              if (onExecuteCMDResult != null)
                onExecuteCMDResult.onResult(-1, "not login"); 
              return;
            } 
            FHDEV_NetLibrary.INSTANCE.FHDEV_NET_StartDevRecord((Pointer)onExecuteCMDResult, (byte)1);
          }
        });
    return (Cancelable)new NothingCancel();
  }
  
  public Cancelable remoteStopRecord(boolean paramBoolean, int paramInt, final CMDCommunicator.OnExecuteCMDResult onExecuteCMDResult) {
    runCMDAsync(new CMDRunnable() {
          public void run(Pointer param1Pointer, ObservableEmitter<Object> param1ObservableEmitter) {
            CMDCommunicator.OnExecuteCMDResult onExecuteCMDResult;
            if (param1Pointer == null) {
              onExecuteCMDResult = onExecuteCMDResult;
              if (onExecuteCMDResult != null)
                onExecuteCMDResult.onResult(-1, "not login"); 
              return;
            } 
            FHDEV_NetLibrary.INSTANCE.FHDEV_NET_StopDevRecord((Pointer)onExecuteCMDResult, (byte)1);
          }
        });
    return (Cancelable)new NothingCancel();
  }
  
  public Cancelable remoteTakePhoto(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, CMDCommunicator.OnExecuteCMDResult paramOnExecuteCMDResult) {
    Log.e("FHCMDCommunicator", "remoteTakePhoto: 未实现连拍功能");
    return (Cancelable)new NothingCancel();
  }
  
  public Cancelable remoteTakePhoto(boolean paramBoolean, int paramInt, final CMDCommunicator.OnExecuteCMDResult onExecuteCMDResult) {
    runCMDAsync(new CMDRunnable() {
          public void run(Pointer param1Pointer, ObservableEmitter<Object> param1ObservableEmitter) {
            CMDCommunicator.OnExecuteCMDResult onExecuteCMDResult;
            if (param1Pointer == null) {
              onExecuteCMDResult = onExecuteCMDResult;
              if (onExecuteCMDResult != null)
                onExecuteCMDResult.onResult(-1, "not login"); 
              return;
            } 
            FHDEV_NetLibrary.INSTANCE.FHDEV_NET_DevShot((Pointer)onExecuteCMDResult, (byte)1, (byte)1, null, IntBuffer.allocate(1));
          }
        });
    return (Cancelable)new NothingCancel();
  }
  
  public Cancelable rotateVideo(boolean paramBoolean1, int paramInt, boolean paramBoolean2, final CMDCommunicator.OnExecuteCMDResult onExecuteCMDResult) {
    runCMDAsync(new CMDRunnable() {
          public void run(Pointer param1Pointer, ObservableEmitter<Object> param1ObservableEmitter) {
            CMDCommunicator.OnExecuteCMDResult onExecuteCMDResult;
            if (param1Pointer == null) {
              onExecuteCMDResult = onExecuteCMDResult;
              if (onExecuteCMDResult != null)
                onExecuteCMDResult.onResult(-1, "not login"); 
              return;
            } 
            Memory memory = new Memory(1L);
            memory.setByte(0L, (byte)3);
            int i = FHDEV_NetLibrary.INSTANCE.FHDEV_NET_SetDevConfig((Pointer)onExecuteCMDResult, 55, (byte)1, (Pointer)memory, 1);
            FHDEV_NetLibrary.INSTANCE.FHDEV_NET_SaveDevConfig((Pointer)onExecuteCMDResult);
            if (i == 20)
              FHDEV_NetExLibrary.INSTANCE.FHDEV_NET_MirrorCtrl((Pointer)onExecuteCMDResult, (byte)3); 
          }
        });
    return (Cancelable)new NothingCancel();
  }
  
  public Cancelable sendReceiveRemotePhotoCMD() {
    return sendReceiveRemotePhotoCMD(true);
  }
  
  public Cancelable sendReceiveRemotePhotoCMD(final boolean isSaveInDevices) {
    final Pointer userID = this.fhDevices.getUserID();
    if (pointer != null)
      Observable.create(new ObservableOnSubscribe<Object>() {
            public void subscribe(ObservableEmitter<Object> param1ObservableEmitter) throws Exception {
              String str;
              Memory memory = new Memory(2097152L);
              IntBuffer intBuffer = IntBuffer.allocate(1);
              intBuffer.put((int)memory.size());
              intBuffer.flip();
              if (FHCMDCommunicator.this.onReceiveRemotePhotoCallback != null) {
                str = FHCMDCommunicator.this.onReceiveRemotePhotoCallback.generatePhotoFileName();
                FHCMDCommunicator.this.onReceiveRemotePhotoCallback.onReceive(50, false, str);
              } else {
                str = "";
              } 
              int i = FHDEV_NetLibrary.INSTANCE.FHDEV_NET_DevShot(userID, (byte)1, (byte)isSaveInDevices, (Pointer)memory, intBuffer);
              if (FHCMDCommunicator.this.onReceiveRemotePhotoCallback == null)
                return; 
              if (i == 0) {
                FHCMDCommunicator.this.onReceiveRemotePhotoCallback.onReceive(0, true, str);
                return;
              } 
              FHCMDCommunicator.this.onReceiveRemotePhotoCallback.onReceive(70, false, str);
              File file = new File(str);
              try {
                file.createNewFile();
                FileOutputStream fileOutputStream = new FileOutputStream();
                this(file);
                fileOutputStream.write(memory.getByteArray(0L, intBuffer.get()));
                fileOutputStream.flush();
                fileOutputStream.close();
              } catch (IOException iOException) {
                iOException.printStackTrace();
              } 
              FHCMDCommunicator.this.onReceiveRemotePhotoCallback.onReceive(100, true, str);
            }
          }).subscribeOn(Schedulers.io()).subscribe(); 
    return (Cancelable)new NothingCancel();
  }
  
  public Cancelable setVideoQuality(boolean paramBoolean, int paramInt1, int paramInt2, CMDCommunicator.OnExecuteCMDResult paramOnExecuteCMDResult) {
    this.fhDevices.getVideoCommunicator().setVideoDefaultQuality(paramInt2);
    if (((FHVideoCommunicator)this.fhDevices.getVideoCommunicator()).isConnectingVideo()) {
      if (paramOnExecuteCMDResult != null)
        paramOnExecuteCMDResult.onResult(-1, "上次操作未完成"); 
      return (Cancelable)new NothingCancel();
    } 
    ((FHVideoCommunicator)this.fhDevices.getVideoCommunicator()).reconnectVideo();
    return (Cancelable)new NothingCancel();
  }
  
  public RemoteVideoFiles string2RemoteVideoFiles(String paramString) {
    try {
      Gson gson = new Gson();
      this();
      return (RemoteVideoFiles)gson.fromJson(paramString, RemoteVideoFiles.class);
    } catch (Exception exception) {
      exception.printStackTrace();
      return null;
    } 
  }
  
  public Cancelable syncDevicesTime(boolean paramBoolean, int paramInt, CMDCommunicator.OnExecuteCMDResult paramOnExecuteCMDResult) {
    Pointer pointer = this.fhDevices.getUserID();
    if (pointer != null) {
      FHNP_Time_t fHNP_Time_t = new FHNP_Time_t();
      Calendar calendar = Calendar.getInstance();
      fHNP_Time_t.day = (byte)(byte)calendar.get(5);
      fHNP_Time_t.month = (byte)(byte)(calendar.get(2) + 1);
      fHNP_Time_t.year = (short)(short)calendar.get(1);
      fHNP_Time_t.hour = (byte)(byte)calendar.get(10);
      fHNP_Time_t.minute = (byte)(byte)calendar.get(12);
      fHNP_Time_t.second = (byte)(byte)calendar.get(13);
      fHNP_Time_t.msecond = calendar.get(14);
      fHNP_Time_t.setAutoSynch(true);
      fHNP_Time_t.autoWrite();
      if (FHDEV_NetLibrary.INSTANCE.FHDEV_NET_SetDevConfig(pointer, 9, (byte)0, fHNP_Time_t.getPointer(), fHNP_Time_t.size()) == 0) {
        Log.e("FHCMDCommunicator", "syncDevicesTime: Fail");
        return (Cancelable)new NothingCancel();
      } 
      FHDEV_NetLibrary.INSTANCE.FHDEV_NET_SaveDevConfig(pointer);
    } 
    return (Cancelable)new NothingCancel();
  }
  
  static interface CMDRunnable {
    void run(Pointer param1Pointer, ObservableEmitter<Object> param1ObservableEmitter);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhdevices/FHCMDCommunicator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */