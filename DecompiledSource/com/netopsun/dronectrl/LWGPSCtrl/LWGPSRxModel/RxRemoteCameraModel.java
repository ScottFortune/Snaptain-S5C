package com.netopsun.dronectrl.LWGPSCtrl.LWGPSRxModel;

import com.netopsun.dronectrl.LGBCtrl.LGBBytesTool;
import com.netopsun.dronectrl.LGBCtrl.RxModel;

public class RxRemoteCameraModel extends RxModel {
  protected int remoteCameraStateRaw;
  
  public RemoteCameraType remoteCameraType;
  
  public int modelRawLength() {
    return 1;
  }
  
  public void setRemoteCameraStateRaw(int paramInt) {
    this.remoteCameraStateRaw = paramInt;
    if (paramInt != 1) {
      if (paramInt != 2) {
        if (paramInt == 4)
          this.remoteCameraType = RemoteCameraType.RemoteStopRecord; 
      } else {
        this.remoteCameraType = RemoteCameraType.RemoteRecordVideo;
      } 
    } else {
      this.remoteCameraType = RemoteCameraType.RemoteTakePhoto;
    } 
  }
  
  protected void unpackRawData(byte[] paramArrayOfbyte) {
    setRemoteCameraStateRaw(LGBBytesTool.covertToUInt8(paramArrayOfbyte));
  }
  
  public enum RemoteCameraType {
    RemoteRecordVideo, RemoteStopRecord, RemoteTakePhoto;
    
    static {
      $VALUES = new RemoteCameraType[] { RemoteTakePhoto, RemoteRecordVideo, RemoteStopRecord };
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/dronectrl/LWGPSCtrl/LWGPSRxModel/RxRemoteCameraModel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */