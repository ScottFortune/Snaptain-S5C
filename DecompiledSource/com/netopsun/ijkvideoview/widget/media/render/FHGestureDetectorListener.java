package com.netopsun.ijkvideoview.widget.media.render;

import android.view.GestureDetector;
import android.view.MotionEvent;

public class FHGestureDetectorListener implements GestureDetector.OnGestureListener {
  private int curIndex = 0;
  
  private FHFishEyeDrawingHelper fhFishEyeDrawingHelper;
  
  private float hDegrees = -1.0F;
  
  private float[] hEyeDegrees = new float[4];
  
  private float hOffset = -1.0F;
  
  private float vDegrees = -1.0F;
  
  public FHGestureDetectorListener(FHFishEyeDrawingHelper paramFHFishEyeDrawingHelper) {
    this.fhFishEyeDrawingHelper = paramFHFishEyeDrawingHelper;
  }
  
  public boolean onDown(MotionEvent paramMotionEvent) {
    this.fhFishEyeDrawingHelper.setvelocityX(0.0F);
    this.fhFishEyeDrawingHelper.setvelocityY(0.0F);
    this.hOffset = FHFishEyeDrawingHelper.hOffset;
    this.vDegrees = FHFishEyeDrawingHelper.vDegrees;
    this.hDegrees = FHFishEyeDrawingHelper.hDegrees;
    for (byte b = 0; b < 4; b++)
      this.hEyeDegrees[b] = FHFishEyeDrawingHelper.hEyeDegrees[b]; 
    if (paramMotionEvent.getX() <= (FHFishEyeDrawingHelper.mScreenWidth / 2) && paramMotionEvent.getY() <= (FHFishEyeDrawingHelper.mScreenHeight / 2)) {
      this.curIndex = 2;
    } else if (paramMotionEvent.getX() <= FHFishEyeDrawingHelper.mScreenWidth && paramMotionEvent.getX() > (FHFishEyeDrawingHelper.mScreenWidth / 2) && paramMotionEvent.getY() <= (FHFishEyeDrawingHelper.mScreenHeight / 2)) {
      this.curIndex = 3;
    } else if (paramMotionEvent.getX() <= (FHFishEyeDrawingHelper.mScreenWidth / 2) && paramMotionEvent.getY() <= FHFishEyeDrawingHelper.mScreenHeight && paramMotionEvent.getY() > (FHFishEyeDrawingHelper.mScreenHeight / 2)) {
      this.curIndex = 0;
    } else if (paramMotionEvent.getX() <= FHFishEyeDrawingHelper.mScreenWidth && paramMotionEvent.getX() > (FHFishEyeDrawingHelper.mScreenWidth / 2) && paramMotionEvent.getY() <= FHFishEyeDrawingHelper.mScreenHeight && paramMotionEvent.getY() > (FHFishEyeDrawingHelper.mScreenHeight / 2)) {
      this.curIndex = 1;
    } 
    FHFishEyeDrawingHelper.curIndex = this.curIndex;
    return true;
  }
  
  public boolean onFling(MotionEvent paramMotionEvent1, MotionEvent paramMotionEvent2, float paramFloat1, float paramFloat2) {
    if (Math.abs(paramFloat1) > 2000.0F)
      this.fhFishEyeDrawingHelper.setvelocityX(paramFloat1); 
    if (Math.abs(paramFloat2) > 2000.0F)
      this.fhFishEyeDrawingHelper.setvelocityY(paramFloat2); 
    return false;
  }
  
  public void onLongPress(MotionEvent paramMotionEvent) {}
  
  public boolean onScroll(MotionEvent paramMotionEvent1, MotionEvent paramMotionEvent2, float paramFloat1, float paramFloat2) {
    float[] arrayOfFloat;
    if (FHFishEyeDrawingHelper.displayMode == 0 || 6 == FHFishEyeDrawingHelper.displayMode) {
      paramFloat2 = paramMotionEvent1.getX() - paramMotionEvent2.getX();
      paramFloat1 = paramMotionEvent2.getY() - paramMotionEvent1.getY();
      if (Math.abs(paramFloat2) < 2.0F && Math.abs(paramFloat1) < 2.0F)
        return false; 
      if (FHFishEyeDrawingHelper.eyeMode == 0 || 1 == FHFishEyeDrawingHelper.eyeMode || 3 == FHFishEyeDrawingHelper.eyeMode) {
        FHFishEyeDrawingHelper.vDegrees = this.vDegrees - paramFloat1 / 5.0F;
        FHFishEyeDrawingHelper.hDegrees = this.hDegrees - paramFloat2 / 5.0F;
        return false;
      } 
      if (2 == FHFishEyeDrawingHelper.eyeMode) {
        arrayOfFloat = FHFishEyeDrawingHelper.hEyeDegrees;
        int i = this.curIndex;
        arrayOfFloat[i] = this.hEyeDegrees[i] - paramFloat2 / 10.0F;
      } 
      return false;
    } 
    paramFloat1 = (paramMotionEvent2.getX() - arrayOfFloat.getX()) / 500.0F;
    FHFishEyeDrawingHelper.hOffset = this.hOffset - paramFloat1;
    return false;
  }
  
  public void onShowPress(MotionEvent paramMotionEvent) {}
  
  public boolean onSingleTapUp(MotionEvent paramMotionEvent) {
    return false;
  }
  
  public void setFhFishEyeDrawingHelper(FHFishEyeDrawingHelper paramFHFishEyeDrawingHelper) {
    this.fhFishEyeDrawingHelper = paramFHFishEyeDrawingHelper;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/ijkvideoview/widget/media/render/FHGestureDetectorListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */