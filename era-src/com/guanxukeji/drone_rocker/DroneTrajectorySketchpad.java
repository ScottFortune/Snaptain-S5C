package com.guanxukeji.drone_rocker;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class DroneTrajectorySketchpad extends FrameLayout {
  private ValueAnimator animator;
  
  private ImageView droneIcon;
  
  private Path path = new Path();
  
  private Paint pathPaint;
  
  private int playUnitAnimationDuration = 4000;
  
  private float preX;
  
  private float preY;
  
  private ReplayTrackListener replayTrackListener;
  
  private int screenHeight;
  
  private int screenWidth;
  
  public DroneTrajectorySketchpad(Context paramContext) {
    super(paramContext);
    initView();
  }
  
  public DroneTrajectorySketchpad(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    initView();
  }
  
  public DroneTrajectorySketchpad(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    initView();
  }
  
  public DroneTrajectorySketchpad(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2) {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    initView();
  }
  
  private static int dip2px(Context paramContext, float paramFloat) {
    return (int)(paramFloat * (paramContext.getResources().getDisplayMetrics()).density + 0.5F);
  }
  
  private void initView() {
    WindowManager windowManager = (WindowManager)getContext().getSystemService("window");
    DisplayMetrics displayMetrics = new DisplayMetrics();
    windowManager.getDefaultDisplay().getMetrics(displayMetrics);
    this.screenWidth = displayMetrics.widthPixels;
    this.screenHeight = displayMetrics.heightPixels;
    this.droneIcon = new ImageView(getContext());
    this.droneIcon.setImageResource(R.drawable.drone_icon);
    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(dip2px(getContext(), 28.0F), dip2px(getContext(), 28.0F));
    addView((View)this.droneIcon, (ViewGroup.LayoutParams)layoutParams);
    this.droneIcon.setVisibility(4);
    this.pathPaint = new Paint();
    this.pathPaint.setColor(-65536);
    this.pathPaint.setStyle(Paint.Style.STROKE);
  }
  
  private void playAnimate() {
    final PathMeasure pathMeasure = new PathMeasure();
    pathMeasure.setPath(this.path, false);
    long l = (long)(pathMeasure.getLength() / this.screenHeight * this.playUnitAnimationDuration);
    this.animator = ValueAnimator.ofFloat(new float[] { 0.0F, 1.0F });
    this.animator.setDuration(l);
    this.animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
          public void onAnimationUpdate(ValueAnimator param1ValueAnimator) {
            if (DroneTrajectorySketchpad.this.getVisibility() != 0 || DroneTrajectorySketchpad.this.path.isEmpty()) {
              param1ValueAnimator.cancel();
              DroneTrajectorySketchpad.this.reset();
              return;
            } 
            float[] arrayOfFloat1 = new float[2];
            float[] arrayOfFloat2 = new float[2];
            PathMeasure pathMeasure = pathMeasure;
            pathMeasure.getPosTan(pathMeasure.getLength() * ((Float)param1ValueAnimator.getAnimatedValue()).floatValue(), arrayOfFloat1, arrayOfFloat2);
            DroneTrajectorySketchpad.this.droneIcon.setX(arrayOfFloat1[0] - (DroneTrajectorySketchpad.this.droneIcon.getWidth() / 2));
            DroneTrajectorySketchpad.this.droneIcon.setY(arrayOfFloat1[1] - (DroneTrajectorySketchpad.this.droneIcon.getHeight() / 2));
            if (DroneTrajectorySketchpad.this.replayTrackListener != null)
              DroneTrajectorySketchpad.this.replayTrackListener.onReplayTrack(((Float)param1ValueAnimator.getAnimatedValue()).floatValue(), arrayOfFloat2); 
          }
        });
    this.animator.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter() {
          public void onAnimationEnd(Animator param1Animator) {
            super.onAnimationEnd(param1Animator);
            DroneTrajectorySketchpad.this.reset();
          }
          
          public void onAnimationStart(Animator param1Animator) {
            super.onAnimationStart(param1Animator);
            DroneTrajectorySketchpad.this.droneIcon.setVisibility(0);
          }
        });
    this.animator.setInterpolator((TimeInterpolator)new LinearInterpolator());
    this.animator.start();
  }
  
  protected void dispatchDraw(Canvas paramCanvas) {
    paramCanvas.drawPath(this.path, this.pathPaint);
    super.dispatchDraw(paramCanvas);
  }
  
  public ImageView getDroneIcon() {
    return this.droneIcon;
  }
  
  public int getPlayUnitAnimationDuration() {
    return this.playUnitAnimationDuration;
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    int i = paramMotionEvent.getAction();
    if (i != 0) {
      if (i != 1) {
        if (i == 2) {
          float f1 = (this.preX + paramMotionEvent.getX()) / 2.0F;
          float f2 = (this.preY + paramMotionEvent.getY()) / 2.0F;
          this.path.quadTo(this.preX, this.preY, f1, f2);
          this.preX = paramMotionEvent.getX();
          this.preY = paramMotionEvent.getY();
          invalidate();
        } 
      } else {
        playAnimate();
      } 
      return super.onTouchEvent(paramMotionEvent);
    } 
    reset();
    this.path.moveTo(paramMotionEvent.getX(), paramMotionEvent.getY());
    this.preX = paramMotionEvent.getX();
    this.preY = paramMotionEvent.getY();
    return true;
  }
  
  public void reset() {
    this.path.reset();
    this.droneIcon.setVisibility(4);
    ValueAnimator valueAnimator = this.animator;
    if (valueAnimator != null)
      valueAnimator.cancel(); 
    invalidate();
    ReplayTrackListener replayTrackListener = this.replayTrackListener;
    if (replayTrackListener != null)
      replayTrackListener.onReplayCancel(); 
  }
  
  public void setPlayUnitAnimationDuration(int paramInt) {
    this.playUnitAnimationDuration = paramInt;
  }
  
  public void setReplayTrackListener(ReplayTrackListener paramReplayTrackListener) {
    this.replayTrackListener = paramReplayTrackListener;
  }
  
  protected static interface ReplayTrackListener {
    void onReplayCancel();
    
    void onReplayTrack(float param1Float, float[] param1ArrayOffloat);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/guanxukeji/drone_rocker/DroneTrajectorySketchpad.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */