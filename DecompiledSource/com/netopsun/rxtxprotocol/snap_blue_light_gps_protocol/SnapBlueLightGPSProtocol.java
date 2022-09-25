package com.netopsun.rxtxprotocol.snap_blue_light_gps_protocol;

import android.location.Location;
import android.os.Bundle;
import com.netopsun.deviceshub.base.RxTxCommunicator;
import com.netopsun.rxtxprotocol.base.RxTxProtocol;
import com.netopsun.rxtxprotocol.base.gps_receiver.DroneMsgCallback;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.util.concurrent.TimeUnit;

public class SnapBlueLightGPSProtocol extends RxTxProtocol {
  final byte[] bytes = new byte[13];
  
  private volatile boolean isDroneSettingChange;
  
  private final SnapReceiveDataAnalyzer receiveDataAnalyzer = new SnapReceiveDataAnalyzer();
  
  private Disposable sendWayPointsTask;
  
  public SnapBlueLightGPSProtocol(RxTxCommunicator paramRxTxCommunicator) {
    super(paramRxTxCommunicator);
    paramRxTxCommunicator.setOnReceiveCallback(new RxTxCommunicator.OnReceiveCallback() {
          public void onReceive(byte[] param1ArrayOfbyte) {
            SnapBlueLightGPSProtocol.this.receiveDataAnalyzer.parseData(param1ArrayOfbyte, param1ArrayOfbyte.length);
          }
        });
  }
  
  private void sendDroneSettingCmd(double paramDouble1, double paramDouble2, double paramDouble3) {
    int i = (int)paramDouble1;
    int j = (int)paramDouble2;
    int k = (int)paramDouble3;
    byte[] arrayOfByte = new byte[8];
    arrayOfByte[0] = (byte)104;
    arrayOfByte[1] = (byte)5;
    arrayOfByte[2] = (byte)4;
    arrayOfByte[3] = (byte)(byte)i;
    arrayOfByte[4] = (byte)(byte)(i >> 8 & 0x3 | arrayOfByte[4]);
    arrayOfByte[4] = (byte)(byte)(arrayOfByte[4] | j << 2 & 0xFC);
    arrayOfByte[5] = (byte)(byte)(arrayOfByte[5] | j >> 6 & 0x3F);
    arrayOfByte[5] = (byte)(byte)(arrayOfByte[5] | k << 6 & 0xC0);
    arrayOfByte[6] = (byte)(byte)(arrayOfByte[6] | k >> 2 & 0x3F);
    fillCheckSum(arrayOfByte, 1, 6, 7);
    if (couldAddSendBytes())
      this.sendQueue.add(arrayOfByte); 
  }
  
  private void sendFollowmeCmd(double paramDouble1, double paramDouble2) {
    int i = (int)(paramDouble2 * 1.0E7D);
    int j = (int)(paramDouble1 * 1.0E7D);
    byte[] arrayOfByte = new byte[12];
    arrayOfByte[0] = (byte)104;
    arrayOfByte[1] = (byte)2;
    arrayOfByte[2] = (byte)8;
    intToByteLittle(i, arrayOfByte, 3);
    intToByteLittle(j, arrayOfByte, 7);
    fillCheckSum(arrayOfByte, 1, 10, 11);
    if (couldAddSendBytes())
      this.sendQueue.add(arrayOfByte); 
  }
  
  private void sendSurroundCmd(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5) {
    int i = (int)(paramDouble2 * 1.0E7D);
    int j = (int)(paramDouble1 * 1.0E7D);
    byte[] arrayOfByte = new byte[16];
    arrayOfByte[0] = (byte)104;
    arrayOfByte[1] = (byte)3;
    arrayOfByte[2] = (byte)12;
    intToByteLittle(i, arrayOfByte, 3);
    intToByteLittle(j, arrayOfByte, 7);
    arrayOfByte[11] = (byte)(byte)(arrayOfByte[11] | (byte)(int)(paramDouble3 * 10.0D));
    j = (int)(paramDouble4 * 10.0D);
    arrayOfByte[12] = (byte)(byte)(arrayOfByte[12] | (byte)(j & 0xFF));
    i = arrayOfByte[13];
    arrayOfByte[13] = (byte)(byte)((byte)(j >> 8 & 0xF) | i);
    j = (int)(paramDouble5 * 10.0D);
    arrayOfByte[13] = (byte)(byte)(arrayOfByte[13] | (byte)((j & 0xF) << 4));
    i = arrayOfByte[14];
    arrayOfByte[14] = (byte)(byte)((byte)((j & 0x1F) >> 4) | i);
    arrayOfByte[14] = (byte)(byte)(arrayOfByte[14] | 0x20);
    fillCheckSum(arrayOfByte, 1, 14, 15);
    if (couldAddSendBytes())
      this.sendQueue.add(arrayOfByte); 
  }
  
  private void sendWayPointCmd(int paramInt1, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, int paramInt2) {
    int i = (int)(paramDouble2 * 1.0E7D);
    int j = (int)(paramDouble1 * 1.0E7D);
    byte[] arrayOfByte = new byte[16];
    arrayOfByte[0] = (byte)104;
    arrayOfByte[1] = (byte)4;
    arrayOfByte[2] = (byte)12;
    intToByteLittle(i, arrayOfByte, 3);
    intToByteLittle(j, arrayOfByte, 7);
    j = arrayOfByte[11];
    arrayOfByte[11] = (byte)(byte)((byte)(paramInt1 & 0x1F) | j);
    paramInt1 = (int)(paramDouble4 * 10.0D);
    arrayOfByte[11] = (byte)(byte)(arrayOfByte[11] | (byte)((paramInt1 & 0x7) << 5));
    arrayOfByte[12] = (byte)(byte)(arrayOfByte[12] | (byte)(paramInt1 >> 3 & 0xFF));
    j = arrayOfByte[13];
    arrayOfByte[13] = (byte)(byte)((byte)(paramInt1 >> 11 & 0x1) | j);
    paramInt1 = (int)(paramDouble3 * 10.0D);
    j = arrayOfByte[13];
    arrayOfByte[13] = (byte)(byte)((byte)(paramInt1 << 1) | j);
    arrayOfByte[14] = (byte)(byte)(arrayOfByte[14] | (byte)(paramInt2 / 1000));
    fillCheckSum(arrayOfByte, 1, 14, 15);
    if (couldAddSendBytes())
      this.sendQueue.add(arrayOfByte); 
  }
  
  public void notifySend() {
    byte[] arrayOfByte = this.bytes;
    arrayOfByte[0] = (byte)104;
    arrayOfByte[1] = (byte)1;
    arrayOfByte[2] = (byte)9;
    arrayOfByte[3] = (byte)(byte)(int)(this.roll / 100.0F * 128.0F + 128.0F);
    this.bytes[4] = (byte)(byte)(int)(this.pitch / 100.0F * 128.0F + 128.0F);
    this.bytes[5] = (byte)(byte)(int)(this.accelerator / 100.0F * 128.0F + 128.0F);
    this.bytes[6] = (byte)(byte)(int)(this.yaw / 100.0F * 128.0F + 128.0F);
    arrayOfByte = this.bytes;
    arrayOfByte[7] = (byte)32;
    arrayOfByte[8] = (byte)8;
    if (this.headless == 1) {
      arrayOfByte = this.bytes;
      arrayOfByte[8] = (byte)(byte)(arrayOfByte[8] | 0x20);
    } 
    if (this.normalFlyMode == 1) {
      arrayOfByte = this.bytes;
      arrayOfByte[8] = (byte)(byte)(arrayOfByte[8] | 0x80);
    } 
    this.bytes[9] = (byte)0;
    if (this.calibration == 1) {
      arrayOfByte = this.bytes;
      arrayOfByte[9] = (byte)(byte)(arrayOfByte[9] | 0x1);
    } 
    if (this.compassCalibration == 1) {
      arrayOfByte = this.bytes;
      arrayOfByte[9] = (byte)(byte)(arrayOfByte[9] | 0x2);
    } 
    if (this.unlocked == 1) {
      arrayOfByte = this.bytes;
      arrayOfByte[9] = (byte)(byte)(arrayOfByte[9] | 0x4);
    } 
    if (this.takeOff == 1) {
      arrayOfByte = this.bytes;
      arrayOfByte[9] = (byte)(byte)(0x8 | arrayOfByte[9]);
    } 
    if (this.landing == 1) {
      arrayOfByte = this.bytes;
      arrayOfByte[9] = (byte)(byte)(arrayOfByte[9] | 0x10);
    } 
    if (this.flyback == 1) {
      arrayOfByte = this.bytes;
      arrayOfByte[9] = (byte)(byte)(0x20 | arrayOfByte[9]);
    } 
    if (this.emergencyStop == 1) {
      arrayOfByte = this.bytes;
      arrayOfByte[9] = (byte)(byte)(arrayOfByte[9] | 0x40);
    } 
    if (this.followedMeMode == 1) {
      arrayOfByte = this.bytes;
      arrayOfByte[9] = (byte)(byte)(arrayOfByte[9] | 0x80);
    } 
    this.bytes[10] = (byte)0;
    if (this.aroundMode == 1) {
      arrayOfByte = this.bytes;
      arrayOfByte[10] = (byte)(byte)(arrayOfByte[10] | 0x1);
    } 
    if (this.wayPointMode == 1) {
      arrayOfByte = this.bytes;
      arrayOfByte[10] = (byte)(byte)(0x2 | arrayOfByte[10]);
    } 
    this.bytes[11] = (byte)0;
    if (this.isRockerOpen) {
      arrayOfByte = this.bytes;
      arrayOfByte[11] = (byte)(byte)(arrayOfByte[11] | 0x1);
    } 
    fillCheckSum(this.bytes, 1, 11, 12);
    arrayOfByte = (byte[])this.bytes.clone();
    if (couldAddSendBytes()) {
      this.sendQueue.clear();
      this.sendQueue.add(arrayOfByte);
    } 
    Location location = this.followMeLocation;
    if (location != null) {
      sendFollowmeCmd(location.getLatitude(), location.getLongitude());
      this.followMeLocation = null;
    } 
    location = this.aroundPointLocation;
    if (location != null) {
      Bundle bundle = location.getExtras();
      if (bundle != null)
        bundle.getInt("circleR"); 
      sendSurroundCmd(location.getLatitude(), location.getLongitude(), location.getSpeed(), location.getAltitude(), 5);
      this.aroundPointLocation = null;
    } 
    if (this.isDroneSettingChange) {
      sendDroneSettingCmd(this.droneMaxHigh, this.droneMaxDistance, this.droneFlyBackAltitude);
      this.isDroneSettingChange = false;
    } 
  }
  
  public void setAroundMode(boolean paramBoolean) {
    super.setAroundMode(paramBoolean);
    if (!paramBoolean) {
      Disposable disposable = this.sendWayPointsTask;
      if (disposable != null)
        disposable.dispose(); 
    } 
    Observable.timer(1L, TimeUnit.SECONDS).subscribe(new Consumer<Long>() {
          public void accept(Long param1Long) throws Exception {
            SnapBlueLightGPSProtocol.access$1102(SnapBlueLightGPSProtocol.this, 0);
          }
        });
  }
  
  public void setCalibration(boolean paramBoolean) {
    super.setCalibration(paramBoolean);
    Observable.timer(1L, TimeUnit.SECONDS).subscribe(new Consumer<Long>() {
          public void accept(Long param1Long) throws Exception {
            SnapBlueLightGPSProtocol.access$202(SnapBlueLightGPSProtocol.this, 0);
          }
        });
  }
  
  public void setCompassCalibration(boolean paramBoolean) {
    super.setCompassCalibration(paramBoolean);
    Observable.timer(1L, TimeUnit.SECONDS).subscribe(new Consumer<Long>() {
          public void accept(Long param1Long) throws Exception {
            SnapBlueLightGPSProtocol.access$302(SnapBlueLightGPSProtocol.this, 0);
          }
        });
  }
  
  public void setDroneFlyBackAltitude(double paramDouble) {
    super.setDroneFlyBackAltitude(paramDouble);
    this.isDroneSettingChange = true;
  }
  
  public void setDroneMaxDistance(double paramDouble) {
    super.setDroneMaxDistance(paramDouble);
    this.isDroneSettingChange = true;
  }
  
  public void setDroneMaxHigh(double paramDouble) {
    super.setDroneMaxHigh(paramDouble);
    this.isDroneSettingChange = true;
  }
  
  public void setDroneMsgCallback(DroneMsgCallback paramDroneMsgCallback) {
    this.receiveDataAnalyzer.setDroneMsgCallback(paramDroneMsgCallback);
  }
  
  public void setEmergencyStop(boolean paramBoolean) {
    super.setEmergencyStop(paramBoolean);
    Observable.timer(1L, TimeUnit.SECONDS).subscribe(new Consumer<Long>() {
          public void accept(Long param1Long) throws Exception {
            SnapBlueLightGPSProtocol.access$602(SnapBlueLightGPSProtocol.this, 0);
          }
        });
  }
  
  public void setFlyback(boolean paramBoolean) {
    super.setFlyback(paramBoolean);
    Observable.timer(1L, TimeUnit.SECONDS).subscribe(new Consumer<Long>() {
          public void accept(Long param1Long) throws Exception {
            SnapBlueLightGPSProtocol.access$802(SnapBlueLightGPSProtocol.this, 0);
          }
        });
  }
  
  public void setFollowedMeMode(boolean paramBoolean) {
    super.setFollowedMeMode(paramBoolean);
    if (paramBoolean) {
      Disposable disposable = this.sendWayPointsTask;
      if (disposable != null)
        disposable.dispose(); 
    } 
    Observable.timer(1L, TimeUnit.SECONDS).subscribe(new Consumer<Long>() {
          public void accept(Long param1Long) throws Exception {
            SnapBlueLightGPSProtocol.access$1202(SnapBlueLightGPSProtocol.this, 0);
          }
        });
  }
  
  public void setLanding(boolean paramBoolean) {
    super.setLanding(paramBoolean);
    Observable.timer(1L, TimeUnit.SECONDS).subscribe(new Consumer<Long>() {
          public void accept(Long param1Long) throws Exception {
            SnapBlueLightGPSProtocol.access$502(SnapBlueLightGPSProtocol.this, 0);
          }
        });
  }
  
  public void setNormalFlyMode(boolean paramBoolean) {
    super.setNormalFlyMode(paramBoolean);
    if (paramBoolean) {
      Disposable disposable = this.sendWayPointsTask;
      if (disposable != null)
        disposable.dispose(); 
    } 
    Observable.timer(1L, TimeUnit.SECONDS).subscribe(new Consumer<Long>() {
          public void accept(Long param1Long) throws Exception {
            SnapBlueLightGPSProtocol.access$102(SnapBlueLightGPSProtocol.this, 0);
          }
        });
  }
  
  public void setTakeOff(boolean paramBoolean) {
    super.setTakeOff(paramBoolean);
    Observable.timer(1L, TimeUnit.SECONDS).subscribe(new Consumer<Long>() {
          public void accept(Long param1Long) throws Exception {
            SnapBlueLightGPSProtocol.access$402(SnapBlueLightGPSProtocol.this, 0);
          }
        });
  }
  
  public void setUnlocked(boolean paramBoolean) {
    super.setUnlocked(paramBoolean);
    Observable.timer(1L, TimeUnit.SECONDS).subscribe(new Consumer<Long>() {
          public void accept(Long param1Long) throws Exception {
            SnapBlueLightGPSProtocol.access$702(SnapBlueLightGPSProtocol.this, 0);
          }
        });
  }
  
  public void setWayPointLocations(final Location[] wayPointLocations, final boolean autoSetWayPointMode) {
    super.setWayPointLocations(wayPointLocations, autoSetWayPointMode);
    Disposable disposable = this.sendWayPointsTask;
    if (disposable != null)
      disposable.dispose(); 
    if (!couldAddSendBytes())
      return; 
    this.receiveDataAnalyzer.setSendWayPointSuccessNum(-1);
    this.sendWayPointsTask = Observable.create(new ObservableOnSubscribe<Object>() {
          public void subscribe(ObservableEmitter<Object> param1ObservableEmitter) throws Exception {
            null  = this;
            int i = 0;
            int j = 0;
            while (i < wayPoints.length && !param1ObservableEmitter.isDisposed()) {
              SnapBlueLightGPSProtocol.this.sendWayPointCmd(i, wayPointLocations[i].getLatitude(), wayPointLocations[i].getLongitude(), wayPointLocations[i].getSpeed(), wayPointLocations[i].getAltitude(), (int)wayPointLocations[i].getTime());
              int k = 0;
              while (true) {
                if (!param1ObservableEmitter.isDisposed()) {
                  try {
                    Thread.sleep(1L);
                  } catch (InterruptedException interruptedException) {}
                  int m = k + 1;
                  if (SnapBlueLightGPSProtocol.this.receiveDataAnalyzer.getSendWayPointSuccessNum() == i) {
                    k = i + 1;
                    j = 0;
                    break;
                  } 
                  k = m;
                  if (m >= 100) {
                    m = j + 1;
                    k = i;
                    j = m;
                    if (m >= 50) {
                      param1ObservableEmitter.onComplete();
                      k = i;
                      j = m;
                    } 
                    break;
                  } 
                  continue;
                } 
                k = i;
                break;
              } 
               = this;
              i = k;
            } 
            if (!param1ObservableEmitter.isDisposed() && autoSetWayPointMode)
              SnapBlueLightGPSProtocol.this.setWayPointMode(true); 
          }
        }).subscribeOn(Schedulers.io()).subscribe();
  }
  
  public void setWayPointMode(boolean paramBoolean) {
    super.setWayPointMode(paramBoolean);
    if (!paramBoolean) {
      Disposable disposable = this.sendWayPointsTask;
      if (disposable != null)
        disposable.dispose(); 
    } 
    Observable.timer(1L, TimeUnit.SECONDS).subscribe(new Consumer<Long>() {
          public void accept(Long param1Long) throws Exception {
            SnapBlueLightGPSProtocol.access$1002(SnapBlueLightGPSProtocol.this, 0);
          }
        });
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/rxtxprotocol/snap_blue_light_gps_protocol/SnapBlueLightGPSProtocol.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */