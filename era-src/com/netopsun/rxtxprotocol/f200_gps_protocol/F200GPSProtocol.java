package com.netopsun.rxtxprotocol.f200_gps_protocol;

import android.location.Location;
import android.os.Bundle;
import com.netopsun.deviceshub.base.RxTxCommunicator;
import com.netopsun.rxtxprotocol.base.RxTxProtocol;
import com.netopsun.rxtxprotocol.base.gps_receiver.DroneMsgCallback;

public class F200GPSProtocol extends RxTxProtocol {
  final byte[] AHRSDisplayBytes = new byte[] { 66, 84, 60, 1, 102, 1, 102 };
  
  final byte[] GPSDisplayBytes = new byte[] { 66, 84, 60, 1, 103, 1, 103 };
  
  private volatile boolean isAHRSDisplay = true;
  
  private volatile long lastRockerDataChangeTime;
  
  private volatile long lastSendDisplayCMDTime;
  
  private final F200ReceiveDataAnalyzer receiveDataAnalyzer = new F200ReceiveDataAnalyzer();
  
  final byte[] rockerBytes = new byte[18];
  
  public F200GPSProtocol(RxTxCommunicator paramRxTxCommunicator) {
    super(paramRxTxCommunicator);
    paramRxTxCommunicator.setOnReceiveCallback(new RxTxCommunicator.OnReceiveCallback() {
          public void onReceive(byte[] param1ArrayOfbyte) {
            F200GPSProtocol.this.receiveDataAnalyzer.parseData(param1ArrayOfbyte, param1ArrayOfbyte.length);
          }
        });
  }
  
  private void sendCalibrationCMD(int paramInt) {
    byte[] arrayOfByte = new byte[7];
    arrayOfByte[0] = (byte)66;
    arrayOfByte[1] = (byte)84;
    arrayOfByte[2] = (byte)60;
    arrayOfByte[3] = (byte)1;
    arrayOfByte[4] = (byte)101;
    arrayOfByte[5] = (byte)(byte)paramInt;
    arrayOfByte[6] = (byte)0;
    fillCheckSum(arrayOfByte, 3, arrayOfByte.length - 2, arrayOfByte.length - 1);
    if (couldAddSendBytes())
      this.sendQueue.add(arrayOfByte); 
  }
  
  private void sendFollowmeCmd(double paramDouble1, double paramDouble2) {
    int i = (int)(paramDouble2 * 1.0E7D);
    int j = (int)(paramDouble1 * 1.0E7D);
    byte[] arrayOfByte = new byte[15];
    arrayOfByte[0] = (byte)66;
    arrayOfByte[1] = (byte)84;
    arrayOfByte[2] = (byte)60;
    arrayOfByte[3] = (byte)9;
    arrayOfByte[4] = (byte)104;
    arrayOfByte[5] = (byte)1;
    intToByteLittle(j, arrayOfByte, 6);
    intToByteLittle(i, arrayOfByte, 10);
    fillCheckSum(arrayOfByte, 3, 13, 14);
    if (couldAddSendBytes())
      this.sendQueue.add(arrayOfByte); 
  }
  
  private void sendOneKeyFun(int paramInt) {
    byte[] arrayOfByte = new byte[7];
    arrayOfByte[0] = (byte)66;
    arrayOfByte[1] = (byte)84;
    arrayOfByte[2] = (byte)60;
    arrayOfByte[3] = (byte)1;
    arrayOfByte[4] = (byte)100;
    arrayOfByte[5] = (byte)(byte)paramInt;
    arrayOfByte[6] = (byte)0;
    fillCheckSum(arrayOfByte, 3, arrayOfByte.length - 2, arrayOfByte.length - 1);
    if (couldAddSendBytes())
      this.sendQueue.add(arrayOfByte); 
  }
  
  private void sendSurroundCmd(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4) {
    int i = (int)(paramDouble2 * 1.0E7D);
    int j = (int)(paramDouble1 * 1.0E7D);
    byte[] arrayOfByte = new byte[18];
    arrayOfByte[0] = (byte)66;
    arrayOfByte[1] = (byte)84;
    arrayOfByte[2] = (byte)60;
    arrayOfByte[3] = (byte)12;
    arrayOfByte[4] = (byte)105;
    arrayOfByte[5] = (byte)1;
    shortToByteLittle((short)(int)paramDouble3, arrayOfByte, 6);
    arrayOfByte[8] = (byte)(byte)(int)paramDouble4;
    intToByteLittle(j, arrayOfByte, 9);
    intToByteLittle(i, arrayOfByte, 13);
    fillCheckSum(arrayOfByte, 3, 16, 17);
    if (couldAddSendBytes())
      this.sendQueue.add(arrayOfByte); 
  }
  
  private void sendWayPointCmd(Location[] paramArrayOfLocation) {
    if (paramArrayOfLocation != null && paramArrayOfLocation.length != 0) {
      int i = paramArrayOfLocation.length;
      int j = i * 9;
      byte[] arrayOfByte = new byte[j + 7];
      byte b = 0;
      arrayOfByte[0] = (byte)66;
      arrayOfByte[1] = (byte)84;
      arrayOfByte[2] = (byte)60;
      arrayOfByte[3] = (byte)(byte)(j + 1);
      arrayOfByte[4] = (byte)106;
      arrayOfByte[5] = (byte)(byte)i;
      while (b < paramArrayOfLocation.length) {
        int k = (int)(paramArrayOfLocation[b].getLongitude() * 1.0E7D);
        j = (int)(paramArrayOfLocation[b].getLatitude() * 1.0E7D);
        i = b * 9;
        intToByteLittle(j, arrayOfByte, i + 6);
        intToByteLittle(k, arrayOfByte, i + 10);
        arrayOfByte[i + 14] = (byte)(byte)(int)paramArrayOfLocation[b].getAltitude();
        b++;
      } 
      fillCheckSum(arrayOfByte, 3, arrayOfByte.length - 2, arrayOfByte.length - 1);
      if (couldAddSendBytes())
        this.sendQueue.add(arrayOfByte); 
    } 
  }
  
  public void notifySend() {
    if (System.currentTimeMillis() - this.lastSendDisplayCMDTime > 250L) {
      if (couldAddSendBytes()) {
        if (this.isAHRSDisplay) {
          this.sendQueue.add(this.AHRSDisplayBytes.clone());
        } else {
          this.sendQueue.add(this.GPSDisplayBytes.clone());
        } 
        this.isAHRSDisplay ^= 0x1;
      } 
      this.lastSendDisplayCMDTime = System.currentTimeMillis();
      return;
    } 
    if (this.takeOff == 1) {
      sendOneKeyFun(3);
      this.takeOff = 2;
      return;
    } 
    if (this.landing == 1) {
      sendOneKeyFun(4);
      this.landing = 2;
      return;
    } 
    if (this.emergencyStop == 1) {
      sendOneKeyFun(6);
      this.emergencyStop = 2;
      return;
    } 
    if (this.calibration == 1) {
      sendCalibrationCMD(241);
      this.calibration = 2;
      return;
    } 
    if (this.unlocked == 1) {
      sendOneKeyFun(1);
      this.unlocked = 0;
      return;
    } 
    if (this.unlocked == 2) {
      sendOneKeyFun(2);
      this.unlocked = 0;
      return;
    } 
    if (this.compassCalibration == 1) {
      sendCalibrationCMD(225);
      this.compassCalibration = 2;
      return;
    } 
    if (this.flyback == 1) {
      sendOneKeyFun(5);
      this.flyback = 2;
      return;
    } 
    if (this.normalFlyMode == 1) {
      sendOneKeyFun(6);
      this.normalFlyMode = 2;
      return;
    } 
    Location location1 = this.followMeLocation;
    if (location1 != null) {
      sendFollowmeCmd(location1.getLatitude(), location1.getLongitude());
      this.followMeLocation = null;
      return;
    } 
    Location location2 = this.aroundPointLocation;
    if (location2 != null) {
      Bundle bundle = location2.getExtras();
      if (bundle != null)
        bundle.getInt("circleR"); 
      sendSurroundCmd(location2.getLatitude(), location2.getLongitude(), location2.getAltitude(), 5);
      this.aroundPointLocation = null;
      return;
    } 
    Location[] arrayOfLocation = this.wayPointLocations;
    if (arrayOfLocation != null) {
      sendWayPointCmd((Location[])arrayOfLocation.clone());
      this.wayPointLocations = null;
      return;
    } 
    if (System.currentTimeMillis() - this.lastRockerDataChangeTime < 1500L) {
      byte[] arrayOfByte = this.rockerBytes;
      arrayOfByte[0] = (byte)66;
      arrayOfByte[1] = (byte)84;
      arrayOfByte[2] = (byte)60;
      arrayOfByte[3] = (byte)12;
      arrayOfByte[4] = (byte)107;
      shortToByteLittle((short)(int)(this.roll / 100.0F * 500.0F + 1500.0F), this.rockerBytes, 5);
      shortToByteLittle((short)(int)(this.pitch / 100.0F * 500.0F + 1500.0F), this.rockerBytes, 7);
      shortToByteLittle((short)(int)(this.yaw / 100.0F * 500.0F + 1500.0F), this.rockerBytes, 9);
      shortToByteLittle((short)(int)(this.accelerator / 100.0F * 500.0F + 1500.0F), this.rockerBytes, 11);
      shortToByteLittle((short)(int)(this.cameraPositionValue / 100.0F * 500.0F + 1500.0F), this.rockerBytes, 13);
      shortToByteLittle((short)1500, this.rockerBytes, 15);
      fillCheckSum(this.rockerBytes, 3, 16, 17);
      arrayOfByte = (byte[])this.rockerBytes.clone();
      if (couldAddSendBytes())
        this.sendQueue.add(arrayOfByte); 
    } 
  }
  
  public void setAccelerator(float paramFloat) {
    super.setAccelerator(paramFloat);
    this.lastRockerDataChangeTime = System.currentTimeMillis();
  }
  
  public void setCameraPositionValue(float paramFloat) {
    super.setCameraPositionValue(paramFloat);
    this.lastRockerDataChangeTime = System.currentTimeMillis();
  }
  
  public void setDroneMsgCallback(DroneMsgCallback paramDroneMsgCallback) {
    this.receiveDataAnalyzer.setDroneMsgCallback(paramDroneMsgCallback);
  }
  
  public void setPitch(float paramFloat) {
    super.setPitch(paramFloat);
    this.lastRockerDataChangeTime = System.currentTimeMillis();
  }
  
  public void setRoll(float paramFloat) {
    super.setRoll(paramFloat);
    this.lastRockerDataChangeTime = System.currentTimeMillis();
  }
  
  public void setYaw(float paramFloat) {
    super.setYaw(paramFloat);
    this.lastRockerDataChangeTime = System.currentTimeMillis();
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/rxtxprotocol/f200_gps_protocol/F200GPSProtocol.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */