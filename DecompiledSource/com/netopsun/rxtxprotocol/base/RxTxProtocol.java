package com.netopsun.rxtxprotocol.base;

import android.location.Location;
import com.netopsun.deviceshub.base.RxTxCommunicator;
import com.netopsun.rxtxprotocol.base.gps_receiver.DroneMsgCallback;
import com.netopsun.rxtxprotocol.base.simple_receiver.SimpleDroneMsgCallback;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

public abstract class RxTxProtocol {
  private static final String TAG = "RxTxProtocol";
  
  private static volatile Disposable autoSendTask;
  
  protected volatile float accelerator;
  
  protected volatile int aroundMode;
  
  protected volatile Location aroundPointLocation;
  
  protected volatile int autoCharge;
  
  protected volatile int calibration;
  
  protected volatile float cameraPositionValue;
  
  protected volatile int compassCalibration;
  
  protected volatile boolean displayTestInfoOnce;
  
  public volatile double droneFlyBackAltitude;
  
  public volatile double droneMaxDistance;
  
  public volatile double droneMaxHigh;
  
  protected volatile DroneMsgCallback droneMsgCallback;
  
  protected volatile int emergencyStop;
  
  protected volatile int flyback;
  
  protected volatile Location followMeLocation;
  
  protected volatile int followedMeMode;
  
  protected volatile float followedModeAcceleratorRockerX;
  
  protected volatile float followedModeAcceleratorRockerY;
  
  protected volatile float followedModeDirectionRockerX;
  
  protected volatile float followedModeDirectionRockerY;
  
  protected volatile int headless;
  
  protected volatile boolean isDisplayTestInfo = false;
  
  protected volatile boolean isRockerOpen = false;
  
  protected volatile int landing;
  
  protected volatile int leftWheelSpeed = 100;
  
  protected volatile int normalFlyMode;
  
  protected volatile int openDroneRecordLight;
  
  protected volatile int openDroneTakePhotoLight;
  
  protected volatile boolean openFollowedMode = false;
  
  protected volatile int openLight;
  
  protected volatile float pitch;
  
  protected volatile int rightWheelSpeed = 100;
  
  protected volatile float roll;
  
  protected final RxTxCommunicator rxTxCommunicator;
  
  protected final MyConcurrentLinkedQueue<byte[]> sendQueue = (MyConcurrentLinkedQueue)new MyConcurrentLinkedQueue<byte>();
  
  private volatile Disposable sendTask;
  
  protected volatile SimpleDroneMsgCallback simpleDroneMsgCallback;
  
  protected volatile int takeOff;
  
  protected volatile int unlocked;
  
  protected volatile Location[] wayPointLocations;
  
  protected volatile int wayPointMode;
  
  protected volatile float yaw;
  
  public RxTxProtocol(final RxTxCommunicator rxTxCommunicator) {
    this.rxTxCommunicator = rxTxCommunicator;
    this.sendQueue.setOnAddObjectCallback(new Runnable() {
          public void run() {
            synchronized (RxTxProtocol.this.sendQueue) {
              if (RxTxProtocol.this.sendTask == null || RxTxProtocol.this.sendTask.isDisposed()) {
                RxTxProtocol rxTxProtocol = RxTxProtocol.this;
                ObservableOnSubscribe<Object> observableOnSubscribe = new ObservableOnSubscribe<Object>() {
                    long lastHasSendBytesTime = 0L;
                    
                    int ret;
                    
                    public void subscribe(ObservableEmitter<Object> param2ObservableEmitter) throws Exception {
                      this.lastHasSendBytesTime = System.currentTimeMillis();
                      while (!param2ObservableEmitter.isDisposed()) {
                        byte[] arrayOfByte = RxTxProtocol.this.sendQueue.poll();
                        if (arrayOfByte != null) {
                          this.ret = rxTxCommunicator.send(arrayOfByte);
                          this.lastHasSendBytesTime = System.currentTimeMillis();
                        } 
                        try {
                          Thread.sleep(5L);
                        } catch (InterruptedException interruptedException) {
                          interruptedException.printStackTrace();
                        } 
                        if (System.currentTimeMillis() - this.lastHasSendBytesTime > 5000L) {
                          param2ObservableEmitter.onComplete();
                          break;
                        } 
                      } 
                    }
                  };
                super(this);
                RxTxProtocol.access$002(rxTxProtocol, Observable.create(observableOnSubscribe).subscribeOn(Schedulers.newThread()).subscribe());
              } 
              return;
            } 
          }
        });
  }
  
  public static int byte2Int(byte[] paramArrayOfbyte, int paramInt) {
    byte b1 = paramArrayOfbyte[paramInt];
    byte b2 = paramArrayOfbyte[paramInt + 1];
    byte b3 = paramArrayOfbyte[paramInt + 2];
    return paramArrayOfbyte[paramInt + 3] & 0xFF | (b1 & 0xFF) << 24 | (b2 & 0xFF) << 16 | (b3 & 0xFF) << 8;
  }
  
  public static void intToByte(int paramInt1, byte[] paramArrayOfbyte, int paramInt2) {
    paramArrayOfbyte[paramInt2] = (byte)(byte)(paramInt1 >> 24 & 0xFF);
    paramArrayOfbyte[paramInt2 + 1] = (byte)(byte)(paramInt1 >> 16 & 0xFF);
    paramArrayOfbyte[paramInt2 + 2] = (byte)(byte)(paramInt1 >> 8 & 0xFF);
    paramArrayOfbyte[paramInt2 + 3] = (byte)(byte)(paramInt1 & 0xFF);
  }
  
  public static void intToByteLittle(int paramInt1, byte[] paramArrayOfbyte, int paramInt2) {
    paramArrayOfbyte[paramInt2 + 3] = (byte)(byte)(paramInt1 >> 24 & 0xFF);
    paramArrayOfbyte[paramInt2 + 2] = (byte)(byte)(paramInt1 >> 16 & 0xFF);
    paramArrayOfbyte[paramInt2 + 1] = (byte)(byte)(paramInt1 >> 8 & 0xFF);
    paramArrayOfbyte[paramInt2] = (byte)(byte)(paramInt1 & 0xFF);
  }
  
  public static void shortToByteLittle(short paramShort, byte[] paramArrayOfbyte, int paramInt) {
    paramArrayOfbyte[paramInt + 1] = (byte)(byte)(paramShort >> 8 & 0xFF);
    paramArrayOfbyte[paramInt] = (byte)(byte)(paramShort & 0xFF);
  }
  
  public boolean couldAddSendBytes() {
    return true;
  }
  
  public void fillCheckSum(byte[] paramArrayOfbyte, int paramInt1, int paramInt2, int paramInt3) {
    int i = paramInt1;
    byte b = 0;
    while (i <= paramInt2) {
      if (i == paramInt1) {
        b = paramArrayOfbyte[paramInt1];
      } else {
        b = (byte)(b ^ paramArrayOfbyte[i]);
      } 
      i++;
    } 
    paramArrayOfbyte[paramInt3] = (byte)b;
  }
  
  public abstract void notifySend();
  
  public void release() {
    if (this.sendTask != null)
      this.sendTask.dispose(); 
  }
  
  public void setAccelerator(float paramFloat) {
    float f = paramFloat;
    if (paramFloat >= 100.0F)
      f = 99.99F; 
    paramFloat = f;
    if (f <= -100.0F)
      paramFloat = -99.99F; 
    this.accelerator = paramFloat;
  }
  
  public void setAroundMode(boolean paramBoolean) {
    byte b;
    if (paramBoolean) {
      b = 1;
    } else {
      b = 2;
    } 
    this.aroundMode = b;
  }
  
  public void setAroundPointLocation(Location paramLocation) {
    this.aroundPointLocation = paramLocation;
  }
  
  public void setAutoCharge(boolean paramBoolean) {
    byte b;
    if (paramBoolean) {
      b = 1;
    } else {
      b = 2;
    } 
    this.autoCharge = b;
  }
  
  public void setCalibration(boolean paramBoolean) {
    byte b;
    if (paramBoolean) {
      b = 1;
    } else {
      b = 2;
    } 
    this.calibration = b;
  }
  
  public void setCameraPositionValue(float paramFloat) {
    this.cameraPositionValue = paramFloat;
  }
  
  public void setCompassCalibration(boolean paramBoolean) {
    byte b;
    if (paramBoolean) {
      b = 1;
    } else {
      b = 2;
    } 
    this.compassCalibration = b;
  }
  
  public void setDisplayTestInfo(boolean paramBoolean1, boolean paramBoolean2) {
    this.isDisplayTestInfo = paramBoolean1;
    this.displayTestInfoOnce = paramBoolean2;
  }
  
  public void setDroneFlyBackAltitude(double paramDouble) {
    this.droneFlyBackAltitude = paramDouble;
  }
  
  public void setDroneMaxDistance(double paramDouble) {
    this.droneMaxDistance = paramDouble;
  }
  
  public void setDroneMaxHigh(double paramDouble) {
    this.droneMaxHigh = paramDouble;
  }
  
  public void setDroneMsgCallback(DroneMsgCallback paramDroneMsgCallback) {
    this.droneMsgCallback = paramDroneMsgCallback;
  }
  
  public void setEmergencyStop(boolean paramBoolean) {
    byte b;
    if (paramBoolean) {
      b = 1;
    } else {
      b = 2;
    } 
    this.emergencyStop = b;
  }
  
  public void setFlyback(boolean paramBoolean) {
    byte b;
    if (paramBoolean) {
      b = 1;
    } else {
      b = 2;
    } 
    this.flyback = b;
  }
  
  public void setFollowMeLocation(Location paramLocation) {
    this.followMeLocation = paramLocation;
  }
  
  public void setFollowedMeMode(boolean paramBoolean) {
    byte b;
    if (paramBoolean) {
      b = 1;
    } else {
      b = 2;
    } 
    this.followedMeMode = b;
  }
  
  public void setFollowedModeAcceleratorRockerX(float paramFloat) {
    float f = paramFloat;
    if (paramFloat >= 100.0F)
      f = 99.99F; 
    this.followedModeAcceleratorRockerX = f;
  }
  
  public void setFollowedModeAcceleratorRockerY(float paramFloat) {
    float f = paramFloat;
    if (paramFloat >= 100.0F)
      f = 99.99F; 
    this.followedModeAcceleratorRockerY = f;
  }
  
  public void setFollowedModeDirectionRockerX(float paramFloat) {
    float f = paramFloat;
    if (paramFloat >= 100.0F)
      f = 99.99F; 
    this.followedModeDirectionRockerX = f;
  }
  
  public void setFollowedModeDirectionRockerY(float paramFloat) {
    float f = paramFloat;
    if (paramFloat >= 100.0F)
      f = 99.99F; 
    this.followedModeDirectionRockerY = f;
  }
  
  public void setHeadless(boolean paramBoolean) {
    byte b;
    if (paramBoolean) {
      b = 1;
    } else {
      b = 2;
    } 
    this.headless = b;
  }
  
  public void setLanding(boolean paramBoolean) {
    byte b;
    if (paramBoolean) {
      b = 1;
    } else {
      b = 2;
    } 
    this.landing = b;
  }
  
  public void setLeftWheelSpeed(int paramInt) {
    this.leftWheelSpeed = paramInt;
  }
  
  public void setNormalFlyMode(boolean paramBoolean) {
    byte b;
    if (paramBoolean) {
      b = 1;
    } else {
      b = 2;
    } 
    this.normalFlyMode = b;
  }
  
  public void setOpenDroneRecordLight(boolean paramBoolean) {
    byte b;
    if (paramBoolean) {
      b = 1;
    } else {
      b = 2;
    } 
    this.openDroneRecordLight = b;
  }
  
  public void setOpenDroneTakePhotoLight(boolean paramBoolean) {
    byte b;
    if (paramBoolean) {
      b = 1;
    } else {
      b = 2;
    } 
    this.openDroneTakePhotoLight = b;
  }
  
  public void setOpenFollowedMode(boolean paramBoolean) {
    this.openFollowedMode = paramBoolean;
  }
  
  public void setOpenLight(boolean paramBoolean) {
    byte b;
    if (paramBoolean) {
      b = 1;
    } else {
      b = 2;
    } 
    this.openLight = b;
  }
  
  public void setPitch(float paramFloat) {
    float f = paramFloat;
    if (paramFloat >= 100.0F)
      f = 99.99F; 
    paramFloat = f;
    if (f <= -100.0F)
      paramFloat = -99.99F; 
    this.pitch = paramFloat;
  }
  
  public void setRightWheelSpeed(int paramInt) {
    this.rightWheelSpeed = paramInt;
  }
  
  public void setRockerOpen(boolean paramBoolean) {
    this.isRockerOpen = paramBoolean;
  }
  
  public void setRoll(float paramFloat) {
    float f = paramFloat;
    if (paramFloat >= 100.0F)
      f = 99.99F; 
    paramFloat = f;
    if (f <= -100.0F)
      paramFloat = -99.99F; 
    this.roll = paramFloat;
  }
  
  public void setSimpleDroneMsgCallback(SimpleDroneMsgCallback paramSimpleDroneMsgCallback) {
    this.simpleDroneMsgCallback = paramSimpleDroneMsgCallback;
  }
  
  public void setTakeOff(boolean paramBoolean) {
    byte b;
    if (paramBoolean) {
      b = 1;
    } else {
      b = 2;
    } 
    this.takeOff = b;
  }
  
  public void setUnlocked(boolean paramBoolean) {
    byte b;
    if (paramBoolean) {
      b = 1;
    } else {
      b = 2;
    } 
    this.unlocked = b;
  }
  
  public void setWayPointLocations(Location[] paramArrayOfLocation) {
    setWayPointLocations(paramArrayOfLocation, true);
  }
  
  public void setWayPointLocations(Location[] paramArrayOfLocation, boolean paramBoolean) {
    this.wayPointLocations = paramArrayOfLocation;
  }
  
  public void setWayPointMode(boolean paramBoolean) {
    byte b;
    if (paramBoolean) {
      b = 1;
    } else {
      b = 2;
    } 
    this.wayPointMode = b;
  }
  
  public void setYaw(float paramFloat) {
    float f = paramFloat;
    if (paramFloat >= 100.0F)
      f = 99.99F; 
    paramFloat = f;
    if (f <= -100.0F)
      paramFloat = -99.99F; 
    this.yaw = paramFloat;
  }
  
  public void startAutomaticTimingSend(int paramInt) {
    if (autoSendTask != null)
      autoSendTask.dispose(); 
    autoSendTask = Observable.interval(paramInt, TimeUnit.MILLISECONDS).subscribe(new Consumer<Long>() {
          public void accept(Long param1Long) throws Exception {
            RxTxProtocol.this.notifySend();
          }
        });
  }
  
  public void stopAutomaticTimingSend() {
    if (autoSendTask != null)
      autoSendTask.dispose(); 
  }
  
  public static class MyConcurrentLinkedQueue<E> extends ConcurrentLinkedQueue {
    private Runnable onAddObjectCallback;
    
    public boolean add(Object param1Object) {
      this.onAddObjectCallback.run();
      return super.add(param1Object);
    }
    
    public void setOnAddObjectCallback(Runnable param1Runnable) {
      this.onAddObjectCallback = param1Runnable;
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/rxtxprotocol/base/RxTxProtocol.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */