package com.netopsun.fhdevices;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import com.netopsun.deviceshub.base.CMDCommunicator;
import com.netopsun.fhapi.FHDEV_NetLibrary;
import com.netopsun.fhapi.FHNP_VFrameHead_t;
import com.netopsun.fhapi.LPFHNP_FrameHead_t_union;
import com.sun.jna.Pointer;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class RemoteRecordFileDownloadCallback implements FHDEV_NetLibrary.fDataCallBack {
  private final MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
  
  private final CountDownLatch countDownLatch;
  
  private final int frameRate;
  
  private byte[] header_pps;
  
  private byte[] header_sps;
  
  private MediaMuxer mMuxer;
  
  private final CMDCommunicator.OnExecuteCMDResult onExecuteCMDResult;
  
  private int videoTrackIndex = -1;
  
  public RemoteRecordFileDownloadCallback(MediaMuxer paramMediaMuxer, int paramInt, CountDownLatch paramCountDownLatch, CMDCommunicator.OnExecuteCMDResult paramOnExecuteCMDResult) {
    this.mMuxer = paramMediaMuxer;
    this.frameRate = paramInt;
    this.countDownLatch = paramCountDownLatch;
    this.onExecuteCMDResult = paramOnExecuteCMDResult;
  }
  
  private List<Integer> findFrameIndexes(byte[] paramArrayOfbyte) {
    ArrayList<Integer> arrayList = new ArrayList();
    for (byte b = 0; b < paramArrayOfbyte.length - 3; b++) {
      if (paramArrayOfbyte[b] == 0 && paramArrayOfbyte[b + 1] == 0 && paramArrayOfbyte[b + 2] == 0 && paramArrayOfbyte[b + 3] == 1)
        arrayList.add(Integer.valueOf(b)); 
    } 
    return arrayList;
  }
  
  private byte[] findPPS(Pointer paramPointer, int paramInt) {
    byte[] arrayOfByte = paramPointer.getByteArray(0L, paramInt);
    List<Integer> list = findFrameIndexes(arrayOfByte);
    for (byte b = 0; b < list.size(); b++) {
      int i = ((Integer)list.get(b)).intValue();
      int j = i + 4;
      if (j < paramInt && (arrayOfByte[j] & 0x1F) == 8)
        return (b == list.size() - 1) ? Arrays.copyOfRange(arrayOfByte, i, paramInt) : Arrays.copyOfRange(arrayOfByte, i, ((Integer)list.get(b + 1)).intValue()); 
    } 
    return null;
  }
  
  private byte[] findSPS(Pointer paramPointer, int paramInt) {
    byte[] arrayOfByte = paramPointer.getByteArray(0L, paramInt);
    List<Integer> list = findFrameIndexes(arrayOfByte);
    for (byte b = 0; b < list.size(); b++) {
      int i = ((Integer)list.get(b)).intValue();
      int j = i + 4;
      if (j < paramInt && (arrayOfByte[j] & 0x1F) == 7)
        return (b == list.size() - 1) ? Arrays.copyOfRange(arrayOfByte, i, paramInt) : Arrays.copyOfRange(arrayOfByte, i, ((Integer)list.get(b + 1)).intValue()); 
    } 
    return null;
  }
  
  public void apply(Pointer paramPointer1, int paramInt1, LPFHNP_FrameHead_t_union paramLPFHNP_FrameHead_t_union, Pointer paramPointer2, int paramInt2, Pointer paramPointer3) {
    paramLPFHNP_FrameHead_t_union.setType(FHNP_VFrameHead_t.class);
    paramLPFHNP_FrameHead_t_union.read();
    paramLPFHNP_FrameHead_t_union.stVFrameHead.read();
    if (paramInt2 == 0) {
      this.countDownLatch.countDown();
      return;
    } 
    if (paramLPFHNP_FrameHead_t_union.stVFrameHead.btFrameType != 4 && paramLPFHNP_FrameHead_t_union.stVFrameHead.btFrameType != 3) {
      if (this.videoTrackIndex == -1) {
        if (this.header_sps == null)
          this.header_sps = findSPS(paramPointer2, paramInt2); 
        if (this.header_pps == null)
          this.header_pps = findPPS(paramPointer2, paramInt2); 
        if (this.header_sps == null || this.header_pps == null)
          return; 
        MediaFormat mediaFormat = MediaFormat.createVideoFormat("video/avc", paramLPFHNP_FrameHead_t_union.stVFrameHead.wWidth, paramLPFHNP_FrameHead_t_union.stVFrameHead.wHeight);
        mediaFormat.setByteBuffer("csd-0", ByteBuffer.wrap(this.header_sps));
        mediaFormat.setByteBuffer("csd-1", ByteBuffer.wrap(this.header_pps));
        mediaFormat.setInteger("color-format", 19);
        mediaFormat.setInteger("max-input-size", paramLPFHNP_FrameHead_t_union.stVFrameHead.wWidth * paramLPFHNP_FrameHead_t_union.stVFrameHead.wHeight);
        mediaFormat.setInteger("capture-rate", this.frameRate);
        mediaFormat.setInteger("frame-rate", this.frameRate);
        this.videoTrackIndex = this.mMuxer.addTrack(mediaFormat);
        this.mMuxer.start();
      } 
      MediaCodec.BufferInfo bufferInfo = this.bufferInfo;
      bufferInfo.offset = 0;
      bufferInfo.size = paramInt2;
      if (paramLPFHNP_FrameHead_t_union.stVFrameHead.btFrameType == 0)
        this.bufferInfo.flags = 1; 
      this.bufferInfo.presentationTimeUs = paramLPFHNP_FrameHead_t_union.stVFrameHead.ullTimeStamp;
      this.mMuxer.writeSampleData(this.videoTrackIndex, paramPointer2.getByteBuffer(0L, paramInt2), this.bufferInfo);
    } 
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/fhdevices/RemoteRecordFileDownloadCallback.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */