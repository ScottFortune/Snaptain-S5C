package com.guanxukeji.drone_rocker;

public abstract class ControlListener {
  private float acceleratorRockerPercentage;
  
  private boolean isRequiredReplayTrackActionCallback;
  
  private boolean isRockerActionCallback;
  
  private float pitchControlPercentage;
  
  private int pitchControlSliderCurrentLevel;
  
  private int pitchControlSliderMaxLevel;
  
  private float replayTrackProgress;
  
  private float[] replayTrackTan;
  
  private float rollControlPercentage;
  
  private int rollControlSliderCurrentLevel;
  
  private int rollControlSliderMaxLevel;
  
  private float yawControlPercentage;
  
  private int yawControlSliderCurrentLevel;
  
  private int yawControlSliderMaxLevel;
  
  public float getAcceleratorRockerPercentage() {
    return this.acceleratorRockerPercentage;
  }
  
  public float getPitchControlPercentage() {
    return this.pitchControlPercentage;
  }
  
  public int getPitchControlSliderCurrentLevel() {
    return this.pitchControlSliderCurrentLevel;
  }
  
  public int getPitchControlSliderMaxLevel() {
    return this.pitchControlSliderMaxLevel;
  }
  
  public float getReplayTrackProgress() {
    return this.replayTrackProgress;
  }
  
  public float[] getReplayTrackTan() {
    return this.replayTrackTan;
  }
  
  public float getRollControlPercentage() {
    return this.rollControlPercentage;
  }
  
  public int getRollControlSliderCurrentLevel() {
    return this.rollControlSliderCurrentLevel;
  }
  
  public int getRollControlSliderMaxLevel() {
    return this.rollControlSliderMaxLevel;
  }
  
  public float getYawControlPercentage() {
    return this.yawControlPercentage;
  }
  
  public int getYawControlSliderCurrentLevel() {
    return this.yawControlSliderCurrentLevel;
  }
  
  public int getYawControlSliderMaxLevel() {
    return this.yawControlSliderMaxLevel;
  }
  
  protected void notifyCallBack() {
    if (this.isRockerActionCallback) {
      onRockerAction(this.yawControlPercentage, this.acceleratorRockerPercentage, this.yawControlSliderCurrentLevel, this.yawControlSliderMaxLevel, this.rollControlPercentage, this.pitchControlPercentage, this.rollControlSliderCurrentLevel, this.rollControlSliderMaxLevel, this.pitchControlSliderCurrentLevel, this.pitchControlSliderMaxLevel);
      this.isRockerActionCallback = false;
    } 
    if (this.isRequiredReplayTrackActionCallback) {
      onReplayTrackAction(this.replayTrackProgress, this.replayTrackTan, this.rollControlSliderCurrentLevel, this.rollControlSliderMaxLevel, this.pitchControlSliderCurrentLevel, this.pitchControlSliderMaxLevel);
      this.isRequiredReplayTrackActionCallback = false;
    } 
  }
  
  protected void notifyTrackActionCancel() {
    onReplayTrackActionCancel();
  }
  
  public abstract void onReplayTrackAction(float paramFloat, float[] paramArrayOffloat, int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  public abstract void onReplayTrackActionCancel();
  
  public abstract void onRockerAction(float paramFloat1, float paramFloat2, int paramInt1, int paramInt2, float paramFloat3, float paramFloat4, int paramInt3, int paramInt4, int paramInt5, int paramInt6);
  
  ControlListener setAcceleratorRockerPercentage(float paramFloat) {
    this.acceleratorRockerPercentage = paramFloat;
    this.isRockerActionCallback = true;
    return this;
  }
  
  ControlListener setPitchControlPercentage(float paramFloat) {
    this.pitchControlPercentage = paramFloat;
    this.isRockerActionCallback = true;
    return this;
  }
  
  ControlListener setPitchControlSliderCurrentLevel(int paramInt) {
    this.pitchControlSliderCurrentLevel = paramInt;
    this.isRockerActionCallback = true;
    return this;
  }
  
  public ControlListener setPitchControlSliderMaxLevel(int paramInt) {
    this.pitchControlSliderMaxLevel = paramInt;
    this.isRockerActionCallback = true;
    return this;
  }
  
  ControlListener setReplayTrackProgress(float paramFloat) {
    this.replayTrackProgress = paramFloat;
    this.isRequiredReplayTrackActionCallback = true;
    return this;
  }
  
  ControlListener setReplayTrackTan(float[] paramArrayOffloat) {
    this.replayTrackTan = paramArrayOffloat;
    this.isRequiredReplayTrackActionCallback = true;
    return this;
  }
  
  ControlListener setRollControlPercentage(float paramFloat) {
    this.rollControlPercentage = paramFloat;
    this.isRockerActionCallback = true;
    return this;
  }
  
  ControlListener setRollControlSliderCurrentLevel(int paramInt) {
    this.rollControlSliderCurrentLevel = paramInt;
    this.isRockerActionCallback = true;
    return this;
  }
  
  ControlListener setRollControlSliderMaxLevel(int paramInt) {
    this.rollControlSliderMaxLevel = paramInt;
    this.isRockerActionCallback = true;
    return this;
  }
  
  ControlListener setYawControlPercentage(float paramFloat) {
    this.yawControlPercentage = paramFloat;
    this.isRockerActionCallback = true;
    return this;
  }
  
  ControlListener setYawControlSliderCurrentLevel(int paramInt) {
    this.yawControlSliderCurrentLevel = paramInt;
    this.isRockerActionCallback = true;
    return this;
  }
  
  ControlListener setYawControlSliderMaxLevel(int paramInt) {
    this.yawControlSliderMaxLevel = paramInt;
    this.isRockerActionCallback = true;
    return this;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/guanxukeji/drone_rocker/ControlListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */