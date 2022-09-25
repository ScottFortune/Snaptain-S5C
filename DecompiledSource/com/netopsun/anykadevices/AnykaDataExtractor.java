package com.netopsun.anykadevices;

import com.netopsun.deviceshub.base.bean.RemoteVideoFiles;
import java.util.Arrays;
import java.util.List;

public class AnykaDataExtractor {
  private final int CMD_TYPE_LENGTH = 12;
  
  private OnDataCallback onDataCallback;
  
  private int remainingLength;
  
  private int state;
  
  private final byte[] tempBytes = new byte[500000];
  
  private int tempBytesCurrentPosition = -1;
  
  private static int byte2IntLittle(byte[] paramArrayOfbyte, int paramInt) {
    byte b1 = paramArrayOfbyte[paramInt + 3];
    byte b2 = paramArrayOfbyte[paramInt + 2];
    byte b3 = paramArrayOfbyte[paramInt + 1];
    return paramArrayOfbyte[paramInt] & 0xFF | (b1 & 0xFF) << 24 | (b2 & 0xFF) << 16 | (b3 & 0xFF) << 8;
  }
  
  public static String bytesToHexString(byte[] paramArrayOfbyte) {
    StringBuilder stringBuilder = new StringBuilder();
    if (paramArrayOfbyte == null || paramArrayOfbyte.length <= 0)
      return null; 
    for (byte b = 0; b < paramArrayOfbyte.length; b++) {
      String str = Integer.toHexString(paramArrayOfbyte[b] & 0xFF);
      if (str.length() < 2)
        stringBuilder.append(0); 
      stringBuilder.append(str);
    } 
    return stringBuilder.toString();
  }
  
  private boolean isFrameStart(byte paramByte) {
    int i = this.state;
    if ((i == 0 || i == 1) && paramByte == 108) {
      this.state = 1;
    } else if (this.state == 1 && paramByte == 101) {
      this.state = 2;
    } else if (this.state == 2 && paramByte == 119) {
      this.state = 3;
    } else if (this.state == 3 && paramByte == 101) {
      this.state = 4;
    } else if (this.state == 4 && paramByte == 105) {
      this.state = 5;
    } else if (this.state == 5 && paramByte == 95) {
      this.state = 6;
    } else if (this.state == 6 && paramByte == 99) {
      this.state = 7;
    } else if (this.state == 7 && paramByte == 109) {
      this.state = 8;
    } else if (this.state == 8 && paramByte == 100) {
      this.state = 9;
    } else if (this.state == 9 && paramByte == 0) {
      this.state = 10;
    } else {
      if (this.state == 10) {
        this.state = 0;
        return true;
      } 
      this.state = 0;
    } 
    return false;
  }
  
  private void onRemoteVideoListData(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
    byte[] arrayOfByte = Arrays.copyOfRange(paramArrayOfbyte, paramInt1, paramInt1 + paramInt2);
    paramInt2 /= 116;
    RemoteVideoFiles remoteVideoFiles = new RemoteVideoFiles();
    List<RemoteVideoFiles.RemoteVideoFilesBean> list = remoteVideoFiles.getRemote_video_files();
    for (paramInt1 = 0; paramInt1 < paramInt2; paramInt1++) {
      RemoteVideoFiles.RemoteVideoFilesBean remoteVideoFilesBean = new RemoteVideoFiles.RemoteVideoFilesBean();
      int i = paramInt1 * 116;
      String str = (new String(arrayOfByte, i + 16, 38)).replace("\000", "");
      int j = i + 4;
      int k = byte2IntLittle(Arrays.copyOfRange(arrayOfByte, i, j), 0);
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(k * 1000L);
      stringBuilder.append("");
      remoteVideoFilesBean.setRecord_start_time(stringBuilder.toString());
      j = byte2IntLittle(Arrays.copyOfRange(arrayOfByte, j, i + 8), 0);
      stringBuilder = new StringBuilder();
      stringBuilder.append(j * 1000L);
      stringBuilder.append("");
      remoteVideoFilesBean.setDuration(stringBuilder.toString());
      remoteVideoFilesBean.setVideo_name(str);
      list.add(remoteVideoFilesBean);
    } 
    OnDataCallback onDataCallback = this.onDataCallback;
    if (onDataCallback != null)
      onDataCallback.onRemoteVideoListData(remoteVideoFiles); 
  }
  
  public OnDataCallback getOnDataCallback() {
    return this.onDataCallback;
  }
  
  public void onData(byte[] paramArrayOfbyte, int paramInt) {
    for (byte b = 0; b < paramInt; b++) {
      if (isFrameStart(paramArrayOfbyte[b]))
        this.tempBytesCurrentPosition = 0; 
      int i = this.tempBytesCurrentPosition;
      if (i >= 0) {
        byte[] arrayOfByte = this.tempBytes;
        arrayOfByte[i] = paramArrayOfbyte[b];
        if (i == 35)
          this.remainingLength = byte2IntLittle(arrayOfByte, 12); 
        i = this.tempBytesCurrentPosition;
        int j = this.remainingLength;
        if (i == j + 12 + 23) {
          arrayOfByte = this.tempBytes;
          if (arrayOfByte[0] == 1) {
            OnDataCallback onDataCallback = this.onDataCallback;
            if (onDataCallback != null && j > 0) {
              boolean bool;
              if (arrayOfByte[36] == 1) {
                bool = true;
              } else {
                bool = false;
              } 
              onDataCallback.onRemoteSDReady(bool);
            } 
          } 
          arrayOfByte = this.tempBytes;
          if (arrayOfByte[0] == 8)
            onRemoteVideoListData(arrayOfByte, 36, this.remainingLength); 
          arrayOfByte = this.tempBytes;
          if (arrayOfByte[0] == 6 && arrayOfByte[1] == 1 && this.onDataCallback != null) {
            j = byte2IntLittle(arrayOfByte, 36);
            i = this.remainingLength;
            if (i < 196) {
              this.onDataCallback.onRemoteVideoDownload(j, this.tempBytes, 0, 0);
            } else {
              this.onDataCallback.onRemoteVideoDownload(j, this.tempBytes, 232, i - 196);
            } 
          } 
          this.tempBytesCurrentPosition = 0;
        } 
        this.tempBytesCurrentPosition++;
      } 
    } 
  }
  
  public void setOnDataCallback(OnDataCallback paramOnDataCallback) {
    this.onDataCallback = paramOnDataCallback;
  }
  
  public static class OnDataCallback {
    void onRemoteSDReady(boolean param1Boolean) {}
    
    void onRemoteVideoDownload(int param1Int1, byte[] param1ArrayOfbyte, int param1Int2, int param1Int3) {}
    
    void onRemoteVideoListData(RemoteVideoFiles param1RemoteVideoFiles) {}
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/anykadevices/AnykaDataExtractor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */