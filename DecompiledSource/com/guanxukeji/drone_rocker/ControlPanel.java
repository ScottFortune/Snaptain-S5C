package com.guanxukeji.drone_rocker;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.SoundPool;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import java.lang.reflect.InvocationTargetException;

public class ControlPanel extends FrameLayout {
  private DroneRocker acceleratorRocker;
  
  protected PassivityCloseListener accelerometerControllerPassivityCloseListener;
  
  SensorEventListener accelerometerlListener = new SensorEventListener() {
      public void onAccuracyChanged(Sensor param1Sensor, int param1Int) {
        ControlPanel controlPanel = ControlPanel.this;
        controlPanel.theFilteredRollPercent = 0.0F;
        controlPanel.theFilteredPitchPercent = 0.0F;
      }
      
      public void onSensorChanged(SensorEvent param1SensorEvent) {
        // Byte code:
        //   0: aload_1
        //   1: getfield values : [F
        //   4: iconst_0
        //   5: faload
        //   6: fstore_2
        //   7: aload_1
        //   8: getfield values : [F
        //   11: iconst_1
        //   12: faload
        //   13: fstore_3
        //   14: aload_0
        //   15: getfield this$0 : Lcom/guanxukeji/drone_rocker/ControlPanel;
        //   18: invokestatic access$000 : (Lcom/guanxukeji/drone_rocker/ControlPanel;)Lcom/guanxukeji/drone_rocker/DroneRocker;
        //   21: ifnull -> 249
        //   24: aload_0
        //   25: getfield this$0 : Lcom/guanxukeji/drone_rocker/ControlPanel;
        //   28: invokevirtual getContext : ()Landroid/content/Context;
        //   31: ldc 'window'
        //   33: invokevirtual getSystemService : (Ljava/lang/String;)Ljava/lang/Object;
        //   36: checkcast android/view/WindowManager
        //   39: invokeinterface getDefaultDisplay : ()Landroid/view/Display;
        //   44: invokevirtual getRotation : ()I
        //   47: istore #4
        //   49: iload #4
        //   51: ifeq -> 121
        //   54: fload_2
        //   55: fstore #5
        //   57: fload_3
        //   58: fstore #6
        //   60: iload #4
        //   62: iconst_1
        //   63: if_icmpeq -> 108
        //   66: iload #4
        //   68: iconst_2
        //   69: if_icmpeq -> 98
        //   72: iload #4
        //   74: iconst_3
        //   75: if_icmpeq -> 87
        //   78: fload_2
        //   79: fstore #5
        //   81: fload_3
        //   82: fstore #6
        //   84: goto -> 128
        //   87: fload_2
        //   88: fneg
        //   89: fstore #5
        //   91: fload_3
        //   92: fneg
        //   93: fstore #6
        //   95: goto -> 108
        //   98: fload_3
        //   99: fneg
        //   100: fstore #6
        //   102: fload_2
        //   103: fstore #5
        //   105: goto -> 128
        //   108: fload #5
        //   110: fstore_3
        //   111: fload #6
        //   113: fstore #5
        //   115: fload_3
        //   116: fstore #6
        //   118: goto -> 128
        //   121: fload_2
        //   122: fneg
        //   123: fstore #5
        //   125: fload_3
        //   126: fstore #6
        //   128: aload_0
        //   129: getfield this$0 : Lcom/guanxukeji/drone_rocker/ControlPanel;
        //   132: astore_1
        //   133: aload_1
        //   134: fload #5
        //   136: ldc 12.820513
        //   138: fmul
        //   139: ldc 0.13
        //   141: fmul
        //   142: aload_1
        //   143: getfield theFilteredRollPercent : F
        //   146: ldc 0.87
        //   148: fmul
        //   149: fadd
        //   150: putfield theFilteredRollPercent : F
        //   153: aload_0
        //   154: getfield this$0 : Lcom/guanxukeji/drone_rocker/ControlPanel;
        //   157: astore_1
        //   158: aload_1
        //   159: fload #6
        //   161: ldc 12.820513
        //   163: fmul
        //   164: ldc 0.13
        //   166: fmul
        //   167: aload_1
        //   168: getfield theFilteredPitchPercent : F
        //   171: ldc 0.87
        //   173: fmul
        //   174: fadd
        //   175: putfield theFilteredPitchPercent : F
        //   178: aload_0
        //   179: getfield this$0 : Lcom/guanxukeji/drone_rocker/ControlPanel;
        //   182: getfield theFilteredRollPercent : F
        //   185: invokestatic abs : (F)F
        //   188: ldc 1.6666666
        //   190: fcmpg
        //   191: ifge -> 202
        //   194: aload_0
        //   195: getfield this$0 : Lcom/guanxukeji/drone_rocker/ControlPanel;
        //   198: fconst_0
        //   199: putfield theFilteredRollPercent : F
        //   202: aload_0
        //   203: getfield this$0 : Lcom/guanxukeji/drone_rocker/ControlPanel;
        //   206: getfield theFilteredPitchPercent : F
        //   209: invokestatic abs : (F)F
        //   212: ldc 1.6666666
        //   214: fcmpg
        //   215: ifge -> 226
        //   218: aload_0
        //   219: getfield this$0 : Lcom/guanxukeji/drone_rocker/ControlPanel;
        //   222: fconst_0
        //   223: putfield theFilteredPitchPercent : F
        //   226: aload_0
        //   227: getfield this$0 : Lcom/guanxukeji/drone_rocker/ControlPanel;
        //   230: astore_1
        //   231: aload_1
        //   232: aload_1
        //   233: getfield theFilteredRollPercent : F
        //   236: aload_0
        //   237: getfield this$0 : Lcom/guanxukeji/drone_rocker/ControlPanel;
        //   240: getfield theFilteredPitchPercent : F
        //   243: fneg
        //   244: fconst_0
        //   245: fconst_0
        //   246: invokevirtual simulateTouchRocker : (FFFF)V
        //   249: return
      }
    };
  
  ControlListener controlListener;
  
  protected HandModePresenter currentHandModePresenter;
  
  protected CustomLayoutAdapter customLayout;
  
  private boolean defaultRockerMovable;
  
  private DroneRocker directionRocker;
  
  private DroneTrajectorySketchpad droneTrajectorySketchpad;
  
  private boolean isDefaultRightHandMode;
  
  private boolean isSliderVisiable = true;
  
  private boolean isTurnOnTheAccelerometerController = false;
  
  private VerticalSlider pitchSlider;
  
  private HorizontalSlider rollSlider;
  
  protected PassivityCloseListener sketchpadPassivityCloseListener;
  
  private int slideBarSlidingEffectSound;
  
  private float slideEffectSoundVolume = 1.0F;
  
  private int slideToMiddleEffectSound;
  
  private SoundPool soundPool;
  
  float theFilteredPitchPercent = 0.0F;
  
  float theFilteredRollPercent = 0.0F;
  
  private ImageView trackScaleBtn;
  
  private HorizontalSlider yawSlider;
  
  public ControlPanel(Context paramContext) {
    super(paramContext);
    init();
  }
  
  public ControlPanel(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    init();
  }
  
  public ControlPanel(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    init();
  }
  
  public ControlPanel(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2) {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    init();
  }
  
  private int dp2px(float paramFloat) {
    return (int)(paramFloat * (getContext().getResources().getDisplayMetrics()).density + 0.5F);
  }
  
  private void init() {
    inflate(getContext(), R.layout.control_panel, (ViewGroup)this);
    this.directionRocker = (DroneRocker)findViewById(R.id.direction_rocker);
    this.acceleratorRocker = (DroneRocker)findViewById(R.id.accelerator_rocker);
    this.rollSlider = (HorizontalSlider)findViewById(R.id.accelerator_horizontal_spinner);
    this.yawSlider = (HorizontalSlider)findViewById(R.id.roll_spinner);
    this.pitchSlider = (VerticalSlider)findViewById(R.id.pitch_spinner);
    this.droneTrajectorySketchpad = (DroneTrajectorySketchpad)findViewById(R.id.drone_trajectory_sketchpad);
    this.trackScaleBtn = (ImageView)findViewById(R.id.track_scale_btn);
    this.trackScaleBtn.setOnClickListener(new View.OnClickListener() {
          int currentScaleGrade = 1;
          
          public void onClick(View param1View) {
            ImageView imageView = (ImageView)param1View;
            this.currentScaleGrade++;
            if (this.currentScaleGrade > 5)
              this.currentScaleGrade = 1; 
            imageView.getDrawable().setLevel(this.currentScaleGrade);
            ControlPanel.this.droneTrajectorySketchpad.setPlayUnitAnimationDuration(this.currentScaleGrade * 1000 + 4000);
          }
        });
    this.droneTrajectorySketchpad.setReplayTrackListener(new DroneTrajectorySketchpad.ReplayTrackListener() {
          public void onReplayCancel() {
            if (ControlPanel.this.controlListener == null)
              return; 
            ControlPanel.this.controlListener.notifyTrackActionCancel();
          }
          
          public void onReplayTrack(float param1Float, float[] param1ArrayOffloat) {
            if (ControlPanel.this.controlListener == null)
              return; 
            ControlPanel.this.controlListener.setReplayTrackTan(param1ArrayOffloat);
            ControlPanel.this.controlListener.setReplayTrackProgress(param1Float);
            ControlPanel.this.controlListener.notifyCallBack();
          }
        });
    this.yawSlider.setMidpointChangelistener(new HorizontalSlider.HorizontalSliderMidpointChangeListener() {
          public void onSliderMidpointChange(boolean param1Boolean, int param1Int1, int param1Int2) {
            if (param1Boolean)
              if (param1Int1 == 0) {
                ControlPanel.this.soundPool.play(ControlPanel.this.slideToMiddleEffectSound, ControlPanel.this.slideEffectSoundVolume, ControlPanel.this.slideEffectSoundVolume, 0, 0, 1.0F);
              } else {
                ControlPanel.this.soundPool.play(ControlPanel.this.slideBarSlidingEffectSound, ControlPanel.this.slideEffectSoundVolume, ControlPanel.this.slideEffectSoundVolume, 0, 0, 1.0F);
              }  
            if (ControlPanel.this.controlListener == null)
              return; 
            ControlPanel.this.controlListener.setYawControlSliderCurrentLevel(param1Int1);
            ControlPanel.this.controlListener.setYawControlSliderMaxLevel(param1Int2);
            ControlPanel.this.controlListener.notifyCallBack();
          }
        });
    this.rollSlider.setMidpointChangelistener(new HorizontalSlider.HorizontalSliderMidpointChangeListener() {
          public void onSliderMidpointChange(boolean param1Boolean, int param1Int1, int param1Int2) {
            if (param1Boolean)
              if (param1Int1 == 0) {
                ControlPanel.this.soundPool.play(ControlPanel.this.slideToMiddleEffectSound, ControlPanel.this.slideEffectSoundVolume, ControlPanel.this.slideEffectSoundVolume, 0, 0, 1.0F);
              } else {
                ControlPanel.this.soundPool.play(ControlPanel.this.slideBarSlidingEffectSound, ControlPanel.this.slideEffectSoundVolume, ControlPanel.this.slideEffectSoundVolume, 0, 0, 1.0F);
              }  
            if (ControlPanel.this.controlListener == null)
              return; 
            ControlPanel.this.controlListener.setRollControlSliderCurrentLevel(param1Int1);
            ControlPanel.this.controlListener.setRollControlSliderMaxLevel(param1Int2);
            ControlPanel.this.controlListener.notifyCallBack();
          }
        });
    this.pitchSlider.setMidpointChangelistener(new VerticalSlider.VerticalSliderMidpointChangeListener() {
          public void onSliderMidpointChange(boolean param1Boolean, int param1Int1, int param1Int2) {
            if (param1Boolean)
              if (param1Int1 == 0) {
                ControlPanel.this.soundPool.play(ControlPanel.this.slideToMiddleEffectSound, ControlPanel.this.slideEffectSoundVolume, ControlPanel.this.slideEffectSoundVolume, 0, 0, 1.0F);
              } else {
                ControlPanel.this.soundPool.play(ControlPanel.this.slideBarSlidingEffectSound, ControlPanel.this.slideEffectSoundVolume, ControlPanel.this.slideEffectSoundVolume, 0, 0, 1.0F);
              }  
            if (ControlPanel.this.controlListener == null)
              return; 
            ControlPanel.this.controlListener.setPitchControlSliderMaxLevel(param1Int2);
            ControlPanel.this.controlListener.setPitchControlSliderCurrentLevel(param1Int1);
            ControlPanel.this.controlListener.notifyCallBack();
          }
        });
    this.soundPool = new SoundPool(100, 3, 0);
    this.slideBarSlidingEffectSound = this.soundPool.load(getContext(), R.raw.slide_bar_sliding_effect, 1);
    this.slideToMiddleEffectSound = this.soundPool.load(getContext(), R.raw.slide_to_middle_effect, 1);
  }
  
  public void cancelSimulateTouchRocker() {
    MotionEvent motionEvent = MotionEvent.obtain(System.currentTimeMillis(), System.currentTimeMillis(), 3, (this.directionRocker.getWidth() / 2), (this.directionRocker.getHeight() / 2), 0);
    motionEvent.setSource(-10086);
    this.directionRocker.onTouchEvent(motionEvent);
    motionEvent = MotionEvent.obtain(System.currentTimeMillis(), System.currentTimeMillis(), 3, (this.acceleratorRocker.getWidth() / 2), (this.acceleratorRocker.getHeight() / 2), 0);
    motionEvent.setSource(-10086);
    this.acceleratorRocker.onTouchEvent(motionEvent);
  }
  
  public boolean dispatchTouchEvent(MotionEvent paramMotionEvent) {
    if (paramMotionEvent.getActionMasked() == 0 || paramMotionEvent.getActionMasked() == 5) {
      this.currentHandModePresenter.dispatchTouchEvent(paramMotionEvent);
      return super.dispatchTouchEvent(paramMotionEvent);
    } 
    boolean bool = super.dispatchTouchEvent(paramMotionEvent);
    this.currentHandModePresenter.dispatchTouchEvent(paramMotionEvent);
    return bool;
  }
  
  protected void finalize() throws Throwable {
    SoundPool soundPool = this.soundPool;
    if (soundPool != null)
      soundPool.release(); 
    super.finalize();
  }
  
  public VerticalSlider getPitchSlider() {
    return this.pitchSlider;
  }
  
  public HorizontalSlider getRollSlider() {
    return this.rollSlider;
  }
  
  public HorizontalSlider getYawSlider() {
    return this.yawSlider;
  }
  
  public boolean isTheViewWithinTheRangeOfTouch(View paramView, float paramFloat1, float paramFloat2) {
    boolean bool;
    if (paramFloat1 < paramView.getX() + paramView.getWidth() && paramFloat1 > paramView.getX() && paramFloat2 < paramView.getY() + paramView.getHeight() && paramFloat2 > paramView.getY()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
    if (this.currentHandModePresenter == null)
      if (this.isDefaultRightHandMode) {
        this.currentHandModePresenter = new RightHandModePresenter(this.defaultRockerMovable);
      } else {
        this.currentHandModePresenter = new LeftHandModePresenter(this.defaultRockerMovable);
      }  
  }
  
  protected void onVisibilityChanged(View paramView, int paramInt) {
    super.onVisibilityChanged(paramView, paramInt);
    if (this.directionRocker == null)
      return; 
    if (paramInt == 0 && this.isTurnOnTheAccelerometerController) {
      SensorManager sensorManager = (SensorManager)getContext().getSystemService("sensor");
      sensorManager.registerListener(this.accelerometerlListener, sensorManager.getDefaultSensor(1), 1);
    } else {
      ((SensorManager)getContext().getSystemService("sensor")).unregisterListener(this.accelerometerlListener);
      cancelSimulateTouchRocker();
    } 
    if (paramInt != 0)
      this.droneTrajectorySketchpad.reset(); 
  }
  
  public void setControlListener(ControlListener paramControlListener) {
    if (paramControlListener != null) {
      paramControlListener.setYawControlSliderMaxLevel(this.yawSlider.getMaxSliderLevel());
      paramControlListener.setRollControlSliderMaxLevel(this.rollSlider.getMaxSliderLevel());
      paramControlListener.setPitchControlSliderMaxLevel(this.pitchSlider.getMaxSliderLevel());
      paramControlListener.setYawControlSliderCurrentLevel(this.yawSlider.getCurrentSliderLevel());
      paramControlListener.setRollControlSliderCurrentLevel(this.rollSlider.getCurrentSliderLevel());
      paramControlListener.setPitchControlSliderCurrentLevel(this.pitchSlider.getCurrentSliderLevel());
    } 
    this.controlListener = paramControlListener;
  }
  
  public void setCustomLayout(CustomLayoutAdapter paramCustomLayoutAdapter) {
    if (this.customLayout == null) {
      this.customLayout = paramCustomLayoutAdapter;
      return;
    } 
    this.customLayout = paramCustomLayoutAdapter;
    try {
      this.currentHandModePresenter = this.currentHandModePresenter.getClass().getDeclaredConstructor(new Class[] { ControlPanel.class, boolean.class }).newInstance(new Object[] { this, Boolean.valueOf(this.currentHandModePresenter.isRockerMoveable) });
    } catch (InstantiationException instantiationException) {
      instantiationException.printStackTrace();
    } catch (IllegalAccessException illegalAccessException) {
      illegalAccessException.printStackTrace();
    } catch (InvocationTargetException invocationTargetException) {
      invocationTargetException.printStackTrace();
    } catch (NoSuchMethodException noSuchMethodException) {
      noSuchMethodException.printStackTrace();
    } 
  }
  
  public void setRightHandMode(boolean paramBoolean) {
    this.isDefaultRightHandMode = paramBoolean;
    if (this.currentHandModePresenter == null)
      return; 
    if (paramBoolean) {
      this.currentHandModePresenter = new RightHandModePresenter(this.defaultRockerMovable);
    } else {
      this.currentHandModePresenter = new LeftHandModePresenter(this.defaultRockerMovable);
    } 
  }
  
  public void setRockerAutoHide(boolean paramBoolean) {
    this.acceleratorRocker.setAutoHide(paramBoolean);
    this.directionRocker.setAutoHide(paramBoolean);
  }
  
  public void setRocketMovable(boolean paramBoolean) {
    this.defaultRockerMovable = paramBoolean;
    HandModePresenter handModePresenter = this.currentHandModePresenter;
    if (handModePresenter != null)
      handModePresenter.setRockerMovable(paramBoolean); 
  }
  
  public void setSlideEffectSoundVolume(float paramFloat) {
    this.slideEffectSoundVolume = paramFloat;
  }
  
  public void setSliderVisibility(int paramInt) {
    boolean bool;
    if (paramInt == 0) {
      bool = true;
    } else {
      bool = false;
    } 
    this.isSliderVisiable = bool;
    this.rollSlider.setVisibility(paramInt);
    this.pitchSlider.setVisibility(paramInt);
    this.yawSlider.setVisibility(paramInt);
  }
  
  public void showTrajectorySketchpad(boolean paramBoolean, PassivityCloseListener paramPassivityCloseListener) {
    byte b2;
    this.sketchpadPassivityCloseListener = paramPassivityCloseListener;
    byte b1 = 4;
    if (paramBoolean) {
      b2 = 0;
    } else {
      b2 = 4;
    } 
    if (!paramBoolean)
      b1 = 0; 
    this.trackScaleBtn.setVisibility(b2);
    this.droneTrajectorySketchpad.setVisibility(b2);
    this.directionRocker.setVisibility(b1);
    if (b1 != 0 || this.isSliderVisiable) {
      this.yawSlider.setVisibility(b1);
      this.pitchSlider.setVisibility(b1);
      this.rollSlider.setVisibility(b1);
    } 
    if (paramBoolean) {
      paramPassivityCloseListener = this.accelerometerControllerPassivityCloseListener;
      if (paramPassivityCloseListener != null) {
        paramPassivityCloseListener.onClose("打开了轨迹画板，关闭重力感应");
        turnOnTheAccelerometerController(false, (PassivityCloseListener)null);
      } 
    } 
  }
  
  public void simulateTouchRocker(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
    HandModePresenter handModePresenter = this.currentHandModePresenter;
    if (handModePresenter != null)
      handModePresenter.simulateTouchRocker(paramFloat1, paramFloat2, paramFloat3, paramFloat4); 
  }
  
  public void turnOnFixedHighMode(boolean paramBoolean) {
    this.acceleratorRocker.setFixedHighMode(paramBoolean);
  }
  
  public void turnOnTheAccelerometerController(boolean paramBoolean, PassivityCloseListener paramPassivityCloseListener) {
    this.accelerometerControllerPassivityCloseListener = paramPassivityCloseListener;
    this.isTurnOnTheAccelerometerController = paramBoolean;
    if (this.isTurnOnTheAccelerometerController) {
      SensorManager sensorManager = (SensorManager)getContext().getSystemService("sensor");
      sensorManager.registerListener(this.accelerometerlListener, sensorManager.getDefaultSensor(1), 1);
      PassivityCloseListener passivityCloseListener = this.sketchpadPassivityCloseListener;
      if (passivityCloseListener != null) {
        passivityCloseListener.onClose("打开了重力控制");
        showTrajectorySketchpad(false, (PassivityCloseListener)null);
      } 
    } else {
      ((SensorManager)getContext().getSystemService("sensor")).unregisterListener(this.accelerometerlListener);
      cancelSimulateTouchRocker();
    } 
  }
  
  public static interface CustomLayoutAdapter {
    void onLeftHandModeAcceleratorHalfPartLayout(ViewGroup param1ViewGroup, DroneRocker param1DroneRocker, HorizontalSlider param1HorizontalSlider, DroneTrajectorySketchpad param1DroneTrajectorySketchpad, ImageView param1ImageView);
    
    void onLeftHandModeDirectionHalfPartLayout(ViewGroup param1ViewGroup, DroneRocker param1DroneRocker, HorizontalSlider param1HorizontalSlider, VerticalSlider param1VerticalSlider);
    
    void onRightHandModeAcceleratorHalfPartLayout(ViewGroup param1ViewGroup, DroneRocker param1DroneRocker, HorizontalSlider param1HorizontalSlider, DroneTrajectorySketchpad param1DroneTrajectorySketchpad, ImageView param1ImageView);
    
    void onRightHandModeDirectionHalfPartLayout(ViewGroup param1ViewGroup, DroneRocker param1DroneRocker, HorizontalSlider param1HorizontalSlider, VerticalSlider param1VerticalSlider);
  }
  
  private abstract class HandModePresenter {
    protected boolean isRockerMoveable;
    
    HandModePresenter(boolean param1Boolean) {
      this.isRockerMoveable = param1Boolean;
    }
    
    abstract boolean dispatchTouchEvent(MotionEvent param1MotionEvent);
    
    public void setRockerMovable(boolean param1Boolean) {
      this.isRockerMoveable = param1Boolean;
    }
    
    abstract void simulateTouchRocker(float param1Float1, float param1Float2, float param1Float3, float param1Float4);
  }
  
  private class LeftHandModePresenter extends HandModePresenter {
    private int accelerateHalfPartPointerId = -1;
    
    private int directionHalfPartPointerId = -1;
    
    LeftHandModePresenter(boolean param1Boolean) {
      super(param1Boolean);
      ControlPanel.this.directionRocker.setRockerBackground(R.drawable.direction_rocker_background);
      ControlPanel.this.acceleratorRocker.setRockerBackground(R.drawable.accelerator_rocker_background);
      refreshAccelerateHalfPart();
      refreshDirectionHalfPart();
      ControlPanel.this.directionRocker.setListener(new DroneRocker.RockerChangeListener() {
            public void onMidpointChange(float param2Float1, float param2Float2) {
              if (ControlPanel.this.controlListener != null) {
                ControlPanel.this.controlListener.setRollControlPercentage(param2Float1);
                ControlPanel.this.controlListener.setPitchControlPercentage(param2Float2);
                ControlPanel.this.controlListener.notifyCallBack();
              } 
            }
          });
      ControlPanel.this.acceleratorRocker.setListener(new DroneRocker.RockerChangeListener() {
            public void onMidpointChange(float param2Float1, float param2Float2) {
              if (ControlPanel.this.controlListener != null) {
                ControlPanel.this.controlListener.setYawControlPercentage(param2Float1);
                ControlPanel.this.controlListener.setAcceleratorRockerPercentage(param2Float2);
                ControlPanel.this.controlListener.notifyCallBack();
              } 
            }
          });
    }
    
    private void refreshAccelerateHalfPart() {
      int i = (ControlPanel.this.getWidth() / 2 - ControlPanel.this.acceleratorRocker.getWidth()) / 2;
      int j = (ControlPanel.this.getHeight() - ControlPanel.this.acceleratorRocker.getHeight()) / 2;
      ControlPanel.this.acceleratorRocker.setX(i);
      ControlPanel.this.acceleratorRocker.setY(j);
      j = (ControlPanel.this.getWidth() / 2 - ControlPanel.this.rollSlider.getWidth()) / 2;
      int k = ControlPanel.this.getWidth() / 2;
      i = ControlPanel.this.dp2px(10.0F);
      ControlPanel.this.rollSlider.setX((j + k));
      ControlPanel.this.rollSlider.setY(i);
      k = (ControlPanel.this.getWidth() / 2 - ControlPanel.this.trackScaleBtn.getWidth()) / 2;
      i = ControlPanel.this.getHeight();
      j = ControlPanel.this.trackScaleBtn.getHeight();
      ControlPanel.this.trackScaleBtn.setX(k);
      ControlPanel.this.trackScaleBtn.setY((i - j));
      if (ControlPanel.this.customLayout != null) {
        ControlPanel.CustomLayoutAdapter customLayoutAdapter = ControlPanel.this.customLayout;
        ControlPanel controlPanel = ControlPanel.this;
        customLayoutAdapter.onLeftHandModeAcceleratorHalfPartLayout((ViewGroup)controlPanel, controlPanel.acceleratorRocker, ControlPanel.this.rollSlider, ControlPanel.this.droneTrajectorySketchpad, ControlPanel.this.trackScaleBtn);
      } 
    }
    
    private void refreshDirectionHalfPart() {
      int i = (ControlPanel.this.getWidth() / 2 - ControlPanel.this.directionRocker.getWidth()) / 2;
      int j = ControlPanel.this.getWidth() / 2;
      int k = (ControlPanel.this.getHeight() - ControlPanel.this.directionRocker.getHeight()) / 2;
      ControlPanel.this.directionRocker.setX((i + j));
      ControlPanel.this.directionRocker.setY(k);
      j = (ControlPanel.this.getWidth() / 2 - ControlPanel.this.yawSlider.getWidth()) / 2;
      k = ControlPanel.this.dp2px(10.0F);
      ControlPanel.this.yawSlider.setX(j);
      ControlPanel.this.yawSlider.setY(k);
      if (ControlPanel.this.customLayout != null) {
        ControlPanel.CustomLayoutAdapter customLayoutAdapter = ControlPanel.this.customLayout;
        ControlPanel controlPanel = ControlPanel.this;
        customLayoutAdapter.onLeftHandModeDirectionHalfPartLayout((ViewGroup)controlPanel, controlPanel.directionRocker, ControlPanel.this.yawSlider, ControlPanel.this.pitchSlider);
      } 
    }
    
    boolean dispatchTouchEvent(MotionEvent param1MotionEvent) {
      if (!this.isRockerMoveable)
        return false; 
      if (param1MotionEvent.getActionMasked() == 0 || param1MotionEvent.getActionMasked() == 5) {
        float f1 = param1MotionEvent.getX(param1MotionEvent.getActionIndex());
        float f2 = param1MotionEvent.getY(param1MotionEvent.getActionIndex());
        ControlPanel controlPanel = ControlPanel.this;
        if (!controlPanel.isTheViewWithinTheRangeOfTouch((View)controlPanel.rollSlider, f1, f2)) {
          controlPanel = ControlPanel.this;
          if (!controlPanel.isTheViewWithinTheRangeOfTouch((View)controlPanel.pitchSlider, f1, f2)) {
            controlPanel = ControlPanel.this;
            if (controlPanel.isTheViewWithinTheRangeOfTouch((View)controlPanel.yawSlider, f1, f2))
              return false; 
            if (f1 < (ControlPanel.this.getWidth() / 2) && this.accelerateHalfPartPointerId == -1) {
              int i = (int)(f1 - (ControlPanel.this.acceleratorRocker.getWidth() / 2));
              int j = (int)(f2 - (ControlPanel.this.acceleratorRocker.getHeight() / 2));
              ControlPanel.this.acceleratorRocker.setX(i);
              ControlPanel.this.acceleratorRocker.setY(j);
              this.accelerateHalfPartPointerId = param1MotionEvent.getPointerId(param1MotionEvent.getActionIndex());
            } else if (f1 > (ControlPanel.this.getWidth() / 2) && this.directionHalfPartPointerId == -1) {
              int j = (int)(f1 - (ControlPanel.this.directionRocker.getWidth() / 2));
              int i = (int)(f2 - (ControlPanel.this.directionRocker.getHeight() / 2));
              ControlPanel.this.directionRocker.setX(j);
              ControlPanel.this.directionRocker.setY(i);
              this.directionHalfPartPointerId = param1MotionEvent.getPointerId(param1MotionEvent.getActionIndex());
            } 
            return true;
          } 
        } 
        return false;
      } 
      if (param1MotionEvent.getActionMasked() == 1 || param1MotionEvent.getActionMasked() == 6 || param1MotionEvent.getActionMasked() == 3)
        if (this.accelerateHalfPartPointerId == param1MotionEvent.getPointerId(param1MotionEvent.getActionIndex())) {
          refreshAccelerateHalfPart();
          this.accelerateHalfPartPointerId = -1;
        } else if (this.directionHalfPartPointerId == param1MotionEvent.getPointerId(param1MotionEvent.getActionIndex())) {
          refreshDirectionHalfPart();
          this.directionHalfPartPointerId = -1;
        }  
      return true;
    }
    
    void simulateTouchRocker(float param1Float1, float param1Float2, float param1Float3, float param1Float4) {
      param1Float1 /= 100.0F;
      param1Float2 /= 100.0F;
      param1Float3 /= 100.0F;
      param1Float4 /= 100.0F;
      long l1 = System.currentTimeMillis();
      long l2 = System.currentTimeMillis();
      float f = (ControlPanel.this.directionRocker.getWidth() / 2);
      MotionEvent motionEvent = MotionEvent.obtain(l1, l2, 2, (ControlPanel.this.directionRocker.getWidth() / 2) * param1Float1 + f, (ControlPanel.this.directionRocker.getHeight() / 2) - (ControlPanel.this.directionRocker.getHeight() / 2) * param1Float2, 0);
      motionEvent.setSource(-10086);
      ControlPanel.this.directionRocker.onTouchEvent(motionEvent);
      motionEvent = MotionEvent.obtain(System.currentTimeMillis(), System.currentTimeMillis(), 2, (ControlPanel.this.acceleratorRocker.getWidth() / 2) + (ControlPanel.this.acceleratorRocker.getWidth() / 2) * param1Float3, (ControlPanel.this.acceleratorRocker.getHeight() / 2) - (ControlPanel.this.acceleratorRocker.getHeight() / 2) * param1Float4, 0);
      motionEvent.setSource(-10086);
      ControlPanel.this.acceleratorRocker.onTouchEvent(motionEvent);
    }
  }
  
  class null implements DroneRocker.RockerChangeListener {
    public void onMidpointChange(float param1Float1, float param1Float2) {
      if (ControlPanel.this.controlListener != null) {
        ControlPanel.this.controlListener.setRollControlPercentage(param1Float1);
        ControlPanel.this.controlListener.setPitchControlPercentage(param1Float2);
        ControlPanel.this.controlListener.notifyCallBack();
      } 
    }
  }
  
  class null implements DroneRocker.RockerChangeListener {
    public void onMidpointChange(float param1Float1, float param1Float2) {
      if (ControlPanel.this.controlListener != null) {
        ControlPanel.this.controlListener.setYawControlPercentage(param1Float1);
        ControlPanel.this.controlListener.setAcceleratorRockerPercentage(param1Float2);
        ControlPanel.this.controlListener.notifyCallBack();
      } 
    }
  }
  
  public static interface PassivityCloseListener {
    void onClose(String param1String);
  }
  
  private class RightHandModePresenter extends HandModePresenter {
    private int accelerateHalfPartPointerId = -1;
    
    private int directionHalfPartPointerId = -1;
    
    RightHandModePresenter(boolean param1Boolean) {
      super(param1Boolean);
      ControlPanel.this.directionRocker.setRockerBackground(R.drawable.direction_rocker_background_right);
      ControlPanel.this.acceleratorRocker.setRockerBackground(R.drawable.accelerator_rocker_background_right);
      refreshAccelerateHalfPart();
      refreshDirectionHalfPart();
      ControlPanel.this.directionRocker.setListener(new DroneRocker.RockerChangeListener() {
            public void onMidpointChange(float param2Float1, float param2Float2) {
              if (ControlPanel.this.controlListener != null) {
                ControlPanel.this.controlListener.setYawControlPercentage(param2Float1);
                ControlPanel.this.controlListener.setPitchControlPercentage(param2Float2);
                ControlPanel.this.controlListener.notifyCallBack();
              } 
            }
          });
      ControlPanel.this.acceleratorRocker.setListener(new DroneRocker.RockerChangeListener() {
            public void onMidpointChange(float param2Float1, float param2Float2) {
              if (ControlPanel.this.controlListener != null) {
                ControlPanel.this.controlListener.setRollControlPercentage(param2Float1);
                ControlPanel.this.controlListener.setAcceleratorRockerPercentage(param2Float2);
                ControlPanel.this.controlListener.notifyCallBack();
              } 
            }
          });
    }
    
    private void refreshAccelerateHalfPart() {
      int i = (ControlPanel.this.getWidth() / 2 - ControlPanel.this.acceleratorRocker.getWidth()) / 2;
      int j = ControlPanel.this.getWidth() / 2;
      int k = (ControlPanel.this.getHeight() - ControlPanel.this.acceleratorRocker.getHeight()) / 2;
      ControlPanel.this.acceleratorRocker.setX((i + j));
      ControlPanel.this.acceleratorRocker.setY(k);
      k = (ControlPanel.this.getWidth() / 2 - ControlPanel.this.rollSlider.getWidth()) / 2;
      i = ControlPanel.this.getWidth() / 2;
      j = ControlPanel.this.dp2px(10.0F);
      ControlPanel.this.rollSlider.setX((k + i));
      ControlPanel.this.rollSlider.setY(j);
      int m = (ControlPanel.this.getWidth() / 2 - ControlPanel.this.trackScaleBtn.getWidth()) / 2;
      i = ControlPanel.this.getWidth() / 2;
      k = ControlPanel.this.getHeight();
      j = ControlPanel.this.trackScaleBtn.getHeight();
      ControlPanel.this.trackScaleBtn.setX((m + i));
      ControlPanel.this.trackScaleBtn.setY((k - j));
      if (ControlPanel.this.customLayout != null) {
        ControlPanel.CustomLayoutAdapter customLayoutAdapter = ControlPanel.this.customLayout;
        ControlPanel controlPanel = ControlPanel.this;
        customLayoutAdapter.onLeftHandModeAcceleratorHalfPartLayout((ViewGroup)controlPanel, controlPanel.acceleratorRocker, ControlPanel.this.rollSlider, ControlPanel.this.droneTrajectorySketchpad, ControlPanel.this.trackScaleBtn);
      } 
    }
    
    private void refreshDirectionHalfPart() {
      int i = (ControlPanel.this.getWidth() / 2 - ControlPanel.this.directionRocker.getWidth()) / 2;
      int j = (ControlPanel.this.getHeight() - ControlPanel.this.directionRocker.getHeight()) / 2;
      ControlPanel.this.directionRocker.setX(i);
      ControlPanel.this.directionRocker.setY(j);
      i = (ControlPanel.this.getWidth() / 2 - ControlPanel.this.yawSlider.getWidth()) / 2;
      j = ControlPanel.this.dp2px(10.0F);
      ControlPanel.this.yawSlider.setX(i);
      ControlPanel.this.yawSlider.setY(j);
      if (ControlPanel.this.customLayout != null) {
        ControlPanel.CustomLayoutAdapter customLayoutAdapter = ControlPanel.this.customLayout;
        ControlPanel controlPanel = ControlPanel.this;
        customLayoutAdapter.onLeftHandModeDirectionHalfPartLayout((ViewGroup)controlPanel, controlPanel.directionRocker, ControlPanel.this.yawSlider, ControlPanel.this.pitchSlider);
      } 
    }
    
    boolean dispatchTouchEvent(MotionEvent param1MotionEvent) {
      if (!this.isRockerMoveable)
        return false; 
      if (param1MotionEvent.getActionMasked() == 0 || param1MotionEvent.getActionMasked() == 5) {
        float f1 = param1MotionEvent.getX(param1MotionEvent.getActionIndex());
        float f2 = param1MotionEvent.getY(param1MotionEvent.getActionIndex());
        ControlPanel controlPanel = ControlPanel.this;
        if (!controlPanel.isTheViewWithinTheRangeOfTouch((View)controlPanel.rollSlider, f1, f2)) {
          controlPanel = ControlPanel.this;
          if (!controlPanel.isTheViewWithinTheRangeOfTouch((View)controlPanel.pitchSlider, f1, f2)) {
            controlPanel = ControlPanel.this;
            if (controlPanel.isTheViewWithinTheRangeOfTouch((View)controlPanel.yawSlider, f1, f2))
              return false; 
            if (f1 > (ControlPanel.this.getWidth() / 2) && this.accelerateHalfPartPointerId == -1) {
              int i = (int)(f1 - (ControlPanel.this.acceleratorRocker.getWidth() / 2));
              int j = (int)(f2 - (ControlPanel.this.acceleratorRocker.getHeight() / 2));
              ControlPanel.this.acceleratorRocker.setX(i);
              ControlPanel.this.acceleratorRocker.setY(j);
              this.accelerateHalfPartPointerId = param1MotionEvent.getPointerId(param1MotionEvent.getActionIndex());
            } else if (f1 < (ControlPanel.this.getWidth() / 2) && this.directionHalfPartPointerId == -1) {
              int j = (int)(f1 - (ControlPanel.this.directionRocker.getWidth() / 2));
              int i = (int)(f2 - (ControlPanel.this.directionRocker.getHeight() / 2));
              ControlPanel.this.directionRocker.setX(j);
              ControlPanel.this.directionRocker.setY(i);
              this.directionHalfPartPointerId = param1MotionEvent.getPointerId(param1MotionEvent.getActionIndex());
            } 
            return true;
          } 
        } 
        return false;
      } 
      if (param1MotionEvent.getActionMasked() == 1 || param1MotionEvent.getActionMasked() == 6 || param1MotionEvent.getActionMasked() == 3)
        if (this.accelerateHalfPartPointerId == param1MotionEvent.getPointerId(param1MotionEvent.getActionIndex())) {
          refreshAccelerateHalfPart();
          this.accelerateHalfPartPointerId = -1;
        } else if (this.directionHalfPartPointerId == param1MotionEvent.getPointerId(param1MotionEvent.getActionIndex())) {
          refreshDirectionHalfPart();
          this.directionHalfPartPointerId = -1;
        }  
      return true;
    }
    
    void simulateTouchRocker(float param1Float1, float param1Float2, float param1Float3, float param1Float4) {
      param1Float1 /= 100.0F;
      param1Float2 /= 100.0F;
      param1Float3 /= 100.0F;
      float f = param1Float4 / 100.0F;
      long l1 = System.currentTimeMillis();
      long l2 = System.currentTimeMillis();
      param1Float4 = (ControlPanel.this.directionRocker.getWidth() / 2);
      MotionEvent motionEvent = MotionEvent.obtain(l1, l2, 2, (ControlPanel.this.directionRocker.getWidth() / 2) * param1Float3 + param1Float4, (ControlPanel.this.directionRocker.getHeight() / 2) - (ControlPanel.this.directionRocker.getHeight() / 2) * param1Float2, 0);
      motionEvent.setSource(-10086);
      ControlPanel.this.directionRocker.onTouchEvent(motionEvent);
      motionEvent = MotionEvent.obtain(System.currentTimeMillis(), System.currentTimeMillis(), 2, (ControlPanel.this.acceleratorRocker.getWidth() / 2) + (ControlPanel.this.acceleratorRocker.getWidth() / 2) * param1Float1, (ControlPanel.this.acceleratorRocker.getHeight() / 2) - (ControlPanel.this.acceleratorRocker.getHeight() / 2) * f, 0);
      motionEvent.setSource(-10086);
      ControlPanel.this.acceleratorRocker.onTouchEvent(motionEvent);
    }
  }
  
  class null implements DroneRocker.RockerChangeListener {
    public void onMidpointChange(float param1Float1, float param1Float2) {
      if (ControlPanel.this.controlListener != null) {
        ControlPanel.this.controlListener.setYawControlPercentage(param1Float1);
        ControlPanel.this.controlListener.setPitchControlPercentage(param1Float2);
        ControlPanel.this.controlListener.notifyCallBack();
      } 
    }
  }
  
  class null implements DroneRocker.RockerChangeListener {
    public void onMidpointChange(float param1Float1, float param1Float2) {
      if (ControlPanel.this.controlListener != null) {
        ControlPanel.this.controlListener.setRollControlPercentage(param1Float1);
        ControlPanel.this.controlListener.setAcceleratorRockerPercentage(param1Float2);
        ControlPanel.this.controlListener.notifyCallBack();
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/guanxukeji/drone_rocker/ControlPanel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */