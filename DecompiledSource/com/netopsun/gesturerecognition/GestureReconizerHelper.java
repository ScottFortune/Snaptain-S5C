package com.netopsun.gesturerecognition;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.SoundPool;
import android.view.ViewGroup;

public class GestureReconizerHelper {
  private ReconizeResultCallback callback;
  
  final Context context;
  
  private final int[] countdownImages = new int[] { R.drawable.number1, R.drawable.number2, R.drawable.number3 };
  
  private int countdownSoundID;
  
  private int detectionSoundID;
  
  private long lastDetectPalmTime = 0L;
  
  private long lastReconizeTime = 0L;
  
  private SoundPool mSoundPoll = new SoundPool(100, 3, 0);
  
  final PopupCountdownView popupCountdownView;
  
  private int reconizeInterval = 500;
  
  public GestureReconizerHelper(Context paramContext, ReconizeResultCallback paramReconizeResultCallback) {
    this.callback = paramReconizeResultCallback;
    this.context = paramContext;
    this.detectionSoundID = this.mSoundPoll.load(paramContext, R.raw.detection, 0);
    this.countdownSoundID = this.mSoundPoll.load(paramContext, R.raw.countdown, 0);
    this.popupCountdownView = new PopupCountdownView(paramContext, ((ViewGroup)((Activity)paramContext).findViewById(16908290)).getChildAt(0), this.countdownImages);
    GestureReconizer.init(paramContext);
  }
  
  protected void finalize() throws Throwable {
    super.finalize();
    GestureReconizer.release();
  }
  
  public ReconizeResultCallback getCallback() {
    return this.callback;
  }
  
  public int getReconizeInterval() {
    return this.reconizeInterval;
  }
  
  public void process(Context paramContext, Bitmap paramBitmap, float paramFloat) {
    if (System.currentTimeMillis() - this.lastReconizeTime > this.reconizeInterval) {
      GestureReconizer.detect(paramBitmap, new GestureReconizer.GestureReconizeCallBack() {
            public void onResult(int param1Int) {
              if (param1Int == 2 || param1Int == 1 || param1Int == 3)
                if (param1Int == 2) {
                  GestureReconizerHelper.this.mSoundPoll.play(GestureReconizerHelper.this.detectionSoundID, 1.0F, 1.0F, 0, 0, 1.0F);
                  GestureReconizerHelper.access$202(GestureReconizerHelper.this, System.currentTimeMillis() + 5000L);
                  (new Thread(new Runnable() {
                        public void run() {
                          for (byte b = 0; b < 3; b++) {
                            GestureReconizerHelper.this.popupCountdownView.show(2 - b);
                            GestureReconizerHelper.this.mSoundPoll.play(GestureReconizerHelper.this.countdownSoundID, 1.0F, 1.0F, 0, 0, 1.0F);
                            try {
                              Thread.sleep(1000L);
                            } catch (InterruptedException interruptedException) {
                              interruptedException.printStackTrace();
                            } 
                          } 
                          if (GestureReconizerHelper.this.callback != null)
                            GestureReconizerHelper.this.callback.photo(); 
                        }
                      })).start();
                } else if (param1Int == 1) {
                  GestureReconizerHelper.access$202(GestureReconizerHelper.this, System.currentTimeMillis() + 5000L);
                  GestureReconizerHelper.this.mSoundPoll.play(GestureReconizerHelper.this.detectionSoundID, 1.0F, 1.0F, 0, 0, 1.0F);
                  (new Thread(new Runnable() {
                        public void run() {
                          for (byte b = 0; b < 3; b++) {
                            GestureReconizerHelper.this.popupCountdownView.show(2 - b);
                            GestureReconizerHelper.this.mSoundPoll.play(GestureReconizerHelper.this.countdownSoundID, 1.0F, 1.0F, 0, 0, 1.0F);
                            try {
                              Thread.sleep(1000L);
                            } catch (InterruptedException interruptedException) {
                              interruptedException.printStackTrace();
                            } 
                          } 
                          if (GestureReconizerHelper.this.callback != null)
                            GestureReconizerHelper.this.callback.video(); 
                        }
                      })).start();
                }  
            }
          }paramFloat);
      this.lastReconizeTime = System.currentTimeMillis();
    } 
  }
  
  public void setCallback(ReconizeResultCallback paramReconizeResultCallback) {
    this.callback = paramReconizeResultCallback;
  }
  
  public void setReconizeInterval(int paramInt) {
    this.reconizeInterval = paramInt;
  }
  
  public static interface ReconizeResultCallback {
    void photo();
    
    void video();
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/gesturerecognition/GestureReconizerHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */