package com.netopsun.rxtxprotocol.dfs_gps_protocol;

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

public class DFSGPSProtocol extends RxTxProtocol {
  private static final int NT_Circular = 6;
  
  private static final int NT_Land = 3;
  
  private static final int NT_Nav_DellAll = 10;
  
  private static final int NT_Nav_GO = 9;
  
  private static final int NT_Nav_STOP = 8;
  
  private static final int NT_Nav_SetHome = 11;
  
  private static final int NT_Null = 0;
  
  private static final int NT_RTH = 4;
  
  private static final int NT_Signle = 5;
  
  private static final int NT_TakeOff = 2;
  
  private static final int NT_UNKNOW = 255;
  
  private static final int NT_UNLOCKMOTO = 7;
  
  private static final int NT_WayPoint = 1;
  
  final byte[] bytes = new byte[12];
  
  private final DFSReceiveDataAnalyzer receiveDataAnalyzer = new DFSReceiveDataAnalyzer();
  
  private Disposable sendWayPointsTask;
  
  public DFSGPSProtocol(RxTxCommunicator paramRxTxCommunicator) {
    super(paramRxTxCommunicator);
    paramRxTxCommunicator.setOnReceiveCallback(new RxTxCommunicator.OnReceiveCallback() {
          public void onReceive(byte[] param1ArrayOfbyte) {
            DFSGPSProtocol.this.receiveDataAnalyzer.parseData(param1ArrayOfbyte, param1ArrayOfbyte.length);
          }
        });
  }
  
  private void sendFollowmeCmd(double paramDouble1, double paramDouble2, double paramDouble3) {
    int i = (int)(paramDouble2 * 1.0E7D);
    int j = (int)(paramDouble1 * 1.0E7D);
    byte[] arrayOfByte = new byte[26];
    arrayOfByte[0] = (byte)-91;
    arrayOfByte[1] = (byte)26;
    arrayOfByte[2] = (byte)37;
    arrayOfByte[3] = (byte)21;
    arrayOfByte[4] = (byte)16;
    arrayOfByte[5] = (byte)90;
    arrayOfByte[6] = (byte)(byte)123;
    arrayOfByte[7] = (byte)(byte)0;
    arrayOfByte[8] = (byte)(byte)148;
    arrayOfByte[9] = (byte)(byte)17;
    intToByteLittle((int)(paramDouble3 * 100.0D), arrayOfByte, 12);
    intToByteLittle(i, arrayOfByte, 16);
    intToByteLittle(j, arrayOfByte, 20);
    fillCheckSum(arrayOfByte, 2, 23, 24);
    arrayOfByte[25] = (byte)90;
    if (couldAddSendBytes())
      this.sendQueue.add(arrayOfByte); 
  }
  
  private void sendNAVCMD(double paramDouble, int paramInt) {
    byte[] arrayOfByte = new byte[30];
    arrayOfByte[0] = (byte)-91;
    arrayOfByte[1] = (byte)30;
    arrayOfByte[2] = (byte)34;
    arrayOfByte[3] = (byte)26;
    arrayOfByte[5] = (byte)1;
    arrayOfByte[8] = (byte)(byte)paramInt;
    arrayOfByte[11] = (byte)1;
    paramInt = (int)(paramDouble * 100.0D);
    arrayOfByte[14] = (byte)(byte)paramInt;
    arrayOfByte[15] = (byte)(byte)(paramInt >> 8);
    fillCheckSum(arrayOfByte, 2, 27, 28);
    arrayOfByte[29] = (byte)90;
    if (couldAddSendBytes())
      this.sendQueue.add(arrayOfByte); 
  }
  
  private void sendOtherCMD(byte paramByte, byte[] paramArrayOfbyte) {
    byte[] arrayOfByte = new byte[paramArrayOfbyte.length + 6];
    arrayOfByte[0] = (byte)-91;
    arrayOfByte[1] = (byte)(byte)(paramArrayOfbyte.length + 6);
    arrayOfByte[2] = (byte)paramByte;
    arrayOfByte[3] = (byte)(byte)(paramArrayOfbyte.length + 2);
    System.arraycopy(paramArrayOfbyte, 0, arrayOfByte, 4, paramArrayOfbyte.length);
    fillCheckSum(arrayOfByte, 2, arrayOfByte.length - 3, arrayOfByte.length - 2);
    arrayOfByte[arrayOfByte.length - 1] = (byte)90;
    if (couldAddSendBytes())
      this.sendQueue.add(arrayOfByte); 
  }
  
  private void sendSurroundCmd(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5) {
    int i = (int)(paramDouble2 * 1.0E7D);
    int j = (int)(paramDouble1 * 1.0E7D);
    byte[] arrayOfByte = new byte[30];
    arrayOfByte[0] = (byte)-91;
    arrayOfByte[1] = (byte)30;
    arrayOfByte[2] = (byte)34;
    arrayOfByte[3] = (byte)24;
    arrayOfByte[5] = (byte)1;
    arrayOfByte[8] = (byte)6;
    arrayOfByte[10] = (byte)(byte)(int)(paramDouble3 * 10.0D);
    arrayOfByte[11] = (byte)(byte)(int)paramDouble5;
    int k = (int)(paramDouble4 * 100.0D);
    arrayOfByte[14] = (byte)(byte)k;
    arrayOfByte[15] = (byte)(byte)(k >> 8);
    intToByteLittle(i, arrayOfByte, 20);
    intToByteLittle(j, arrayOfByte, 24);
    fillCheckSum(arrayOfByte, 2, 27, 28);
    arrayOfByte[29] = (byte)90;
    if (couldAddSendBytes())
      this.sendQueue.add(arrayOfByte); 
  }
  
  private void sendWayPointCmd(int paramInt1, int paramInt2, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, int paramInt3) {
    int i = (int)(paramDouble2 * 1.0E7D);
    int j = (int)(paramDouble1 * 1.0E7D);
    byte[] arrayOfByte = new byte[30];
    arrayOfByte[0] = (byte)-91;
    arrayOfByte[1] = (byte)30;
    arrayOfByte[2] = (byte)34;
    arrayOfByte[3] = (byte)24;
    paramInt2 = (byte)paramInt2;
    arrayOfByte[4] = (byte)paramInt2;
    arrayOfByte[5] = (byte)1;
    arrayOfByte[6] = (byte)(byte)paramInt1;
    arrayOfByte[8] = (byte)1;
    arrayOfByte[9] = (byte)paramInt2;
    arrayOfByte[10] = (byte)(byte)(int)(paramDouble3 * 10.0D);
    arrayOfByte[11] = (byte)1;
    arrayOfByte[12] = (byte)(byte)(paramInt3 / 1000);
    paramInt1 = (int)(paramDouble4 * 100.0D);
    arrayOfByte[14] = (byte)(byte)paramInt1;
    arrayOfByte[15] = (byte)(byte)(paramInt1 >> 8);
    intToByteLittle(i, arrayOfByte, 20);
    intToByteLittle(j, arrayOfByte, 24);
    fillCheckSum(arrayOfByte, 2, 27, 28);
    arrayOfByte[29] = (byte)90;
    if (couldAddSendBytes())
      this.sendQueue.add(arrayOfByte); 
  }
  
  public void notifySend() {
    byte[] arrayOfByte = this.bytes;
    arrayOfByte[0] = (byte)-91;
    arrayOfByte[1] = (byte)12;
    arrayOfByte[2] = (byte)47;
    arrayOfByte[3] = (byte)8;
    arrayOfByte[4] = (byte)(byte)(int)this.pitch;
    this.bytes[5] = (byte)(byte)(int)this.roll;
    this.bytes[6] = (byte)(byte)(int)this.accelerator;
    this.bytes[7] = (byte)(byte)(int)this.yaw;
    arrayOfByte = this.bytes;
    arrayOfByte[8] = (byte)0;
    arrayOfByte[9] = (byte)0;
    if (this.headless == 1) {
      arrayOfByte = this.bytes;
      arrayOfByte[8] = (byte)(byte)(arrayOfByte[8] | 0x80);
    } 
    if (this.normalFlyMode == 1) {
      arrayOfByte = this.bytes;
      arrayOfByte[8] = (byte)(byte)(arrayOfByte[8] | 0x0);
    } 
    if (this.flyback == 1) {
      arrayOfByte = this.bytes;
      arrayOfByte[8] = (byte)(byte)(arrayOfByte[8] | 0x20);
    } 
    if (this.aroundMode == 1) {
      arrayOfByte = this.bytes;
      arrayOfByte[8] = (byte)(byte)(arrayOfByte[8] | 0x10);
    } 
    if (this.emergencyStop == 1) {
      arrayOfByte = this.bytes;
      arrayOfByte[8] = (byte)(byte)(arrayOfByte[8] | 0x2);
    } 
    fillCheckSum(this.bytes, 1, 9, 10);
    this.bytes[11] = (byte)90;
    arrayOfByte = new byte[1];
    if (couldAddSendBytes())
      this.sendQueue.add(arrayOfByte); 
    if (this.takeOff == 1) {
      sendNAVCMD(1.5D, 2);
      this.takeOff = 0;
    } 
    if (this.landing == 1) {
      sendNAVCMD(0.0D, 3);
      this.landing = 0;
    } 
    if (this.flyback == 1) {
      sendNAVCMD(0.0D, 4);
      this.flyback = 0;
    } 
    if (this.unlocked == 1) {
      sendNAVCMD(0.0D, 7);
      this.unlocked = 0;
    } 
    if (this.normalFlyMode == 1) {
      sendNAVCMD(0.0D, 8);
      this.normalFlyMode = 0;
    } 
    if (this.aroundMode == 1) {
      sendNAVCMD(0.0D, 6);
      this.aroundMode = 0;
    } 
    if (this.compassCalibration == 1) {
      sendOtherCMD((byte)6, new byte[] { 2 });
      this.compassCalibration = 0;
    } 
    if (this.calibration == 1) {
      sendOtherCMD((byte)6, new byte[] { 3 });
      this.calibration = 0;
    } 
    if (this.wayPointMode == 1) {
      sendNAVCMD(0.0D, 9);
      this.wayPointMode = 0;
    } else if (this.wayPointMode == 2) {
      sendNAVCMD(0.0D, 10);
      this.wayPointMode = 0;
    } 
    Location location1 = this.followMeLocation;
    if (location1 != null) {
      sendFollowmeCmd(location1.getLatitude(), location1.getLongitude(), location1.getAltitude());
      this.followMeLocation = null;
    } 
    Location location2 = this.aroundPointLocation;
    if (location2 != null) {
      Bundle bundle = location2.getExtras();
      if (bundle != null)
        bundle.getInt("circleR"); 
      sendSurroundCmd(location2.getLatitude(), location2.getLongitude(), location2.getSpeed(), location2.getAltitude(), 5);
      this.aroundPointLocation = null;
    } 
  }
  
  public void setAroundMode(boolean paramBoolean) {
    super.setAroundMode(paramBoolean);
    if (!paramBoolean) {
      Disposable disposable = this.sendWayPointsTask;
      if (disposable != null)
        disposable.dispose(); 
    } 
    this.flyback = 0;
    this.normalFlyMode = 0;
  }
  
  public void setCalibration(boolean paramBoolean) {
    super.setCalibration(paramBoolean);
    Observable.timer(1L, TimeUnit.SECONDS).subscribe(new Consumer<Long>() {
          public void accept(Long param1Long) throws Exception {
            DFSGPSProtocol.access$102(DFSGPSProtocol.this, 0);
          }
        });
  }
  
  public void setCompassCalibration(boolean paramBoolean) {
    super.setCompassCalibration(paramBoolean);
    Observable.timer(1L, TimeUnit.SECONDS).subscribe(new Consumer<Long>() {
          public void accept(Long param1Long) throws Exception {
            DFSGPSProtocol.access$202(DFSGPSProtocol.this, 0);
          }
        });
  }
  
  public void setDroneMsgCallback(DroneMsgCallback paramDroneMsgCallback) {
    this.receiveDataAnalyzer.setDroneMsgCallback(paramDroneMsgCallback);
  }
  
  public void setEmergencyStop(boolean paramBoolean) {
    super.setEmergencyStop(paramBoolean);
    Observable.timer(1L, TimeUnit.SECONDS).subscribe(new Consumer<Long>() {
          public void accept(Long param1Long) throws Exception {
            DFSGPSProtocol.access$302(DFSGPSProtocol.this, 0);
          }
        });
  }
  
  public void setFlyback(boolean paramBoolean) {
    super.setFlyback(paramBoolean);
    this.aroundMode = 0;
    this.normalFlyMode = 0;
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
            DFSGPSProtocol.access$502(DFSGPSProtocol.this, 0);
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
    this.flyback = 0;
    this.aroundMode = 0;
    this.followMeLocation = null;
    this.wayPointLocations = null;
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
            int i = 0;
            label30: while (true) {
              for (int j = 0; i < wayPoints.length && !param1ObservableEmitter.isDisposed(); j = m) {
                int m;
                DFSGPSProtocol.this.sendWayPointCmd(wayPoints.length, i, wayPointLocations[i].getLatitude(), wayPointLocations[i].getLongitude(), wayPointLocations[i].getSpeed(), wayPointLocations[i].getAltitude(), (int)wayPointLocations[i].getTime());
                int k = 0;
                while (true) {
                  m = j;
                  if (!param1ObservableEmitter.isDisposed()) {
                    try {
                      Thread.sleep(1L);
                    } catch (InterruptedException interruptedException) {}
                    m = k + 1;
                    int n = DFSGPSProtocol.this.receiveDataAnalyzer.getSendWayPointSuccessNum();
                    k = i + 1;
                    if (n == k) {
                      i = k;
                      continue label30;
                    } 
                    k = m;
                    if (m >= 100) {
                      m = ++j;
                      if (j >= 50) {
                        param1ObservableEmitter.onComplete();
                        m = j;
                      } 
                      break;
                    } 
                    continue;
                  } 
                  break;
                } 
              } 
              break;
            } 
            if (!param1ObservableEmitter.isDisposed() && autoSetWayPointMode)
              DFSGPSProtocol.this.setWayPointMode(true); 
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
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/rxtxprotocol/dfs_gps_protocol/DFSGPSProtocol.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */