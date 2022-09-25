package com.jaygoo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class RangeSeekBar extends View {
  private static final int MIN_INTERCEPT_DISTANCE = 100;
  
  public static final int SEEKBAR_MODE_RANGE = 2;
  
  public static final int SEEKBAR_MODE_SINGLE = 1;
  
  public static final int TICK_MARK_GRAVITY_CENTER = 1;
  
  public static final int TICK_MARK_GRAVITY_LEFT = 0;
  
  public static final int TICK_MARK_GRAVITY_RIGHT = 2;
  
  public static final int TRICK_MARK_MODE_NUMBER = 0;
  
  public static final int TRICK_MARK_MODE_OTHER = 1;
  
  private OnRangeChangedListener callback;
  
  SeekBar currTouchSB;
  
  private boolean enableThumbOverlap;
  
  private int gravity;
  
  private boolean isEnable = true;
  
  boolean isScaleThumb = false;
  
  SeekBar leftSB;
  
  private float maxProgress;
  
  private float minInterval;
  
  private float minProgress;
  
  Paint paint = new Paint();
  
  Bitmap progressBitmap;
  
  private int progressBottom;
  
  private int progressColor;
  
  Bitmap progressDefaultBitmap;
  
  private int progressDefaultColor;
  
  private int progressDefaultDrawableId;
  
  RectF progressDefaultDstRect = new RectF();
  
  private int progressDrawableId;
  
  RectF progressDstRect = new RectF();
  
  private int progressHeight;
  
  private int progressLeft;
  
  private int progressPaddingRight;
  
  private float progressRadius;
  
  private int progressRight;
  
  Rect progressSrcRect = new Rect();
  
  private int progressTop;
  
  private int progressWidth;
  
  float reservePercent;
  
  SeekBar rightSB;
  
  private int seekBarMode;
  
  RectF stepDivRect = new RectF();
  
  private int steps;
  
  private boolean stepsAutoBonding;
  
  List<Bitmap> stepsBitmaps = new ArrayList<Bitmap>();
  
  private int stepsColor;
  
  private int stepsDrawableId;
  
  private float stepsHeight;
  
  private float stepsRadius;
  
  private float stepsWidth;
  
  private int tickMarkGravity;
  
  private int tickMarkInRangeTextColor;
  
  private int tickMarkLayoutGravity;
  
  private int tickMarkMode;
  
  private CharSequence[] tickMarkTextArray;
  
  private int tickMarkTextColor;
  
  private int tickMarkTextMargin;
  
  Rect tickMarkTextRect = new Rect();
  
  private int tickMarkTextSize;
  
  float touchDownX;
  
  float touchDownY;
  
  public RangeSeekBar(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public RangeSeekBar(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    initAttrs(paramAttributeSet);
    initPaint();
    initSeekBar(paramAttributeSet);
    initStepsBitmap();
  }
  
  private void changeThumbActivateState(boolean paramBoolean) {
    boolean bool = false;
    if (paramBoolean) {
      SeekBar seekBar = this.currTouchSB;
      if (seekBar != null) {
        paramBoolean = bool;
        if (seekBar == this.leftSB)
          paramBoolean = true; 
        this.leftSB.setActivate(paramBoolean);
        if (this.seekBarMode == 2)
          this.rightSB.setActivate(paramBoolean ^ true); 
        return;
      } 
    } 
    this.leftSB.setActivate(false);
    if (this.seekBarMode == 2)
      this.rightSB.setActivate(false); 
  }
  
  private void initAttrs(AttributeSet paramAttributeSet) {
    try {
      TypedArray typedArray = getContext().obtainStyledAttributes(paramAttributeSet, R.styleable.RangeSeekBar);
      this.seekBarMode = typedArray.getInt(R.styleable.RangeSeekBar_rsb_mode, 2);
      this.minProgress = typedArray.getFloat(R.styleable.RangeSeekBar_rsb_min, 0.0F);
      this.maxProgress = typedArray.getFloat(R.styleable.RangeSeekBar_rsb_max, 100.0F);
      this.minInterval = typedArray.getFloat(R.styleable.RangeSeekBar_rsb_min_interval, 0.0F);
      this.gravity = typedArray.getInt(R.styleable.RangeSeekBar_rsb_gravity, 0);
      this.progressColor = typedArray.getColor(R.styleable.RangeSeekBar_rsb_progress_color, -11806366);
      this.progressRadius = (int)typedArray.getDimension(R.styleable.RangeSeekBar_rsb_progress_radius, -1.0F);
      this.progressDefaultColor = typedArray.getColor(R.styleable.RangeSeekBar_rsb_progress_default_color, -2631721);
      this.progressDrawableId = typedArray.getResourceId(R.styleable.RangeSeekBar_rsb_progress_drawable, 0);
      this.progressDefaultDrawableId = typedArray.getResourceId(R.styleable.RangeSeekBar_rsb_progress_drawable_default, 0);
      this.progressHeight = (int)typedArray.getDimension(R.styleable.RangeSeekBar_rsb_progress_height, Utils.dp2px(getContext(), 2.0F));
      this.tickMarkMode = typedArray.getInt(R.styleable.RangeSeekBar_rsb_tick_mark_mode, 0);
      this.tickMarkGravity = typedArray.getInt(R.styleable.RangeSeekBar_rsb_tick_mark_gravity, 1);
      this.tickMarkLayoutGravity = typedArray.getInt(R.styleable.RangeSeekBar_rsb_tick_mark_layout_gravity, 0);
      this.tickMarkTextArray = typedArray.getTextArray(R.styleable.RangeSeekBar_rsb_tick_mark_text_array);
      this.tickMarkTextMargin = (int)typedArray.getDimension(R.styleable.RangeSeekBar_rsb_tick_mark_text_margin, Utils.dp2px(getContext(), 7.0F));
      this.tickMarkTextSize = (int)typedArray.getDimension(R.styleable.RangeSeekBar_rsb_tick_mark_text_size, Utils.dp2px(getContext(), 12.0F));
      this.tickMarkTextColor = typedArray.getColor(R.styleable.RangeSeekBar_rsb_tick_mark_text_color, this.progressDefaultColor);
      this.tickMarkInRangeTextColor = typedArray.getColor(R.styleable.RangeSeekBar_rsb_tick_mark_text_color, this.progressColor);
      this.steps = typedArray.getInt(R.styleable.RangeSeekBar_rsb_steps, 0);
      this.stepsColor = typedArray.getColor(R.styleable.RangeSeekBar_rsb_step_color, -6447715);
      this.stepsRadius = typedArray.getDimension(R.styleable.RangeSeekBar_rsb_step_radius, 0.0F);
      this.stepsWidth = typedArray.getDimension(R.styleable.RangeSeekBar_rsb_step_width, 0.0F);
      this.stepsHeight = typedArray.getDimension(R.styleable.RangeSeekBar_rsb_step_height, 0.0F);
      this.stepsDrawableId = typedArray.getResourceId(R.styleable.RangeSeekBar_rsb_step_drawable, 0);
      this.stepsAutoBonding = typedArray.getBoolean(R.styleable.RangeSeekBar_rsb_step_auto_bonding, true);
      typedArray.recycle();
    } catch (Exception exception) {
      exception.printStackTrace();
    } 
  }
  
  private void initPaint() {
    this.paint.setStyle(Paint.Style.FILL);
    this.paint.setColor(this.progressDefaultColor);
    this.paint.setTextSize(this.tickMarkTextSize);
  }
  
  private void initProgressBitmap() {
    if (this.progressBitmap == null)
      this.progressBitmap = Utils.drawableToBitmap(getContext(), this.progressWidth, this.progressHeight, this.progressDrawableId); 
    if (this.progressDefaultBitmap == null)
      this.progressDefaultBitmap = Utils.drawableToBitmap(getContext(), this.progressWidth, this.progressHeight, this.progressDefaultDrawableId); 
  }
  
  private void initSeekBar(AttributeSet paramAttributeSet) {
    boolean bool = true;
    this.leftSB = new SeekBar(this, paramAttributeSet, true);
    this.rightSB = new SeekBar(this, paramAttributeSet, false);
    SeekBar seekBar = this.rightSB;
    if (this.seekBarMode == 1)
      bool = false; 
    seekBar.setVisible(bool);
  }
  
  private void initStepsBitmap() {
    if (verifyStepsMode() && this.stepsDrawableId != 0 && this.stepsBitmaps.isEmpty()) {
      Bitmap bitmap = Utils.drawableToBitmap(getContext(), (int)this.stepsWidth, (int)this.stepsHeight, this.stepsDrawableId);
      for (byte b = 0; b <= this.steps; b++)
        this.stepsBitmaps.add(bitmap); 
    } 
  }
  
  private void resetCurrentSeekBarThumb() {
    SeekBar seekBar = this.currTouchSB;
    if (seekBar != null && seekBar.getThumbScaleRatio() > 1.0F && this.isScaleThumb) {
      this.isScaleThumb = false;
      this.currTouchSB.resetThumb();
    } 
  }
  
  private void scaleCurrentSeekBarThumb() {
    SeekBar seekBar = this.currTouchSB;
    if (seekBar != null && seekBar.getThumbScaleRatio() > 1.0F && !this.isScaleThumb) {
      this.isScaleThumb = true;
      this.currTouchSB.scaleThumb();
    } 
  }
  
  private boolean verifyStepsMode() {
    return !(this.steps < 1 || this.stepsHeight <= 0.0F || this.stepsWidth <= 0.0F);
  }
  
  protected float calculateCurrentSeekBarPercent(float paramFloat) {
    if (this.currTouchSB == null)
      return 0.0F; 
    float f = (paramFloat - getProgressLeft()) * 1.0F / this.progressWidth;
    if (paramFloat < getProgressLeft()) {
      f = 0.0F;
    } else if (paramFloat > getProgressRight()) {
      f = 1.0F;
    } 
    paramFloat = f;
    if (this.seekBarMode == 2) {
      SeekBar seekBar1 = this.currTouchSB;
      SeekBar seekBar2 = this.leftSB;
      if (seekBar1 == seekBar2) {
        paramFloat = f;
        if (f > this.rightSB.currPercent - this.reservePercent)
          paramFloat = this.rightSB.currPercent - this.reservePercent; 
      } else {
        paramFloat = f;
        if (seekBar1 == this.rightSB) {
          paramFloat = f;
          if (f < seekBar2.currPercent + this.reservePercent) {
            paramFloat = this.leftSB.currPercent;
            paramFloat = this.reservePercent + paramFloat;
          } 
        } 
      } 
    } 
    return paramFloat;
  }
  
  protected float getEventX(MotionEvent paramMotionEvent) {
    return paramMotionEvent.getX();
  }
  
  protected float getEventY(MotionEvent paramMotionEvent) {
    return paramMotionEvent.getY();
  }
  
  public int getGravity() {
    return this.gravity;
  }
  
  public SeekBar getLeftSeekBar() {
    return this.leftSB;
  }
  
  public float getMaxProgress() {
    return this.maxProgress;
  }
  
  public float getMinInterval() {
    return this.minInterval;
  }
  
  public float getMinProgress() {
    return this.minProgress;
  }
  
  public int getProgressBottom() {
    return this.progressBottom;
  }
  
  public int getProgressColor() {
    return this.progressColor;
  }
  
  public int getProgressDefaultColor() {
    return this.progressDefaultColor;
  }
  
  public int getProgressDefaultDrawableId() {
    return this.progressDefaultDrawableId;
  }
  
  public int getProgressDrawableId() {
    return this.progressDrawableId;
  }
  
  public int getProgressHeight() {
    return this.progressHeight;
  }
  
  public int getProgressLeft() {
    return this.progressLeft;
  }
  
  public int getProgressPaddingRight() {
    return this.progressPaddingRight;
  }
  
  public float getProgressRadius() {
    return this.progressRadius;
  }
  
  public int getProgressRight() {
    return this.progressRight;
  }
  
  public int getProgressTop() {
    return this.progressTop;
  }
  
  public int getProgressWidth() {
    return this.progressWidth;
  }
  
  public SeekBarState[] getRangeSeekBarState() {
    SeekBarState seekBarState1 = new SeekBarState();
    seekBarState1.value = this.leftSB.getProgress();
    seekBarState1.indicatorText = String.valueOf(seekBarState1.value);
    if (Utils.compareFloat(seekBarState1.value, this.minProgress) == 0) {
      seekBarState1.isMin = true;
    } else if (Utils.compareFloat(seekBarState1.value, this.maxProgress) == 0) {
      seekBarState1.isMax = true;
    } 
    SeekBarState seekBarState2 = new SeekBarState();
    if (this.seekBarMode == 2) {
      seekBarState2.value = this.rightSB.getProgress();
      seekBarState2.indicatorText = String.valueOf(seekBarState2.value);
      if (Utils.compareFloat(this.rightSB.currPercent, this.minProgress) == 0) {
        seekBarState2.isMin = true;
      } else if (Utils.compareFloat(this.rightSB.currPercent, this.maxProgress) == 0) {
        seekBarState2.isMax = true;
      } 
    } 
    return new SeekBarState[] { seekBarState1, seekBarState2 };
  }
  
  protected float getRawHeight() {
    float f;
    if (this.seekBarMode == 1) {
      float f1 = this.leftSB.getRawHeight();
      f = f1;
      if (this.tickMarkLayoutGravity == 1) {
        f = f1;
        if (this.tickMarkTextArray != null) {
          f = Math.max((this.leftSB.getThumbScaleHeight() - this.progressHeight) / 2.0F, getTickMarkRawHeight());
          f = f1 - this.leftSB.getThumbScaleHeight() / 2.0F + this.progressHeight / 2.0F + f;
        } 
      } 
    } else {
      float f1 = Math.max(this.leftSB.getRawHeight(), this.rightSB.getRawHeight());
      f = f1;
      if (this.tickMarkLayoutGravity == 1) {
        f = f1;
        if (this.tickMarkTextArray != null) {
          float f2 = Math.max(this.leftSB.getThumbScaleHeight(), this.rightSB.getThumbScaleHeight());
          f = Math.max((f2 - this.progressHeight) / 2.0F, getTickMarkRawHeight());
          f = f1 - f2 / 2.0F + this.progressHeight / 2.0F + f;
        } 
      } 
    } 
    return f;
  }
  
  public SeekBar getRightSeekBar() {
    return this.rightSB;
  }
  
  public int getSeekBarMode() {
    return this.seekBarMode;
  }
  
  public int getSteps() {
    return this.steps;
  }
  
  public List<Bitmap> getStepsBitmaps() {
    return this.stepsBitmaps;
  }
  
  public int getStepsColor() {
    return this.stepsColor;
  }
  
  public int getStepsDrawableId() {
    return this.stepsDrawableId;
  }
  
  public float getStepsHeight() {
    return this.stepsHeight;
  }
  
  public float getStepsRadius() {
    return this.stepsRadius;
  }
  
  public float getStepsWidth() {
    return this.stepsWidth;
  }
  
  public int getTickMarkGravity() {
    return this.tickMarkGravity;
  }
  
  public int getTickMarkInRangeTextColor() {
    return this.tickMarkInRangeTextColor;
  }
  
  public int getTickMarkLayoutGravity() {
    return this.tickMarkLayoutGravity;
  }
  
  public int getTickMarkMode() {
    return this.tickMarkMode;
  }
  
  protected int getTickMarkRawHeight() {
    CharSequence[] arrayOfCharSequence = this.tickMarkTextArray;
    return (arrayOfCharSequence != null && arrayOfCharSequence.length > 0) ? (this.tickMarkTextMargin + Utils.measureText(String.valueOf(arrayOfCharSequence[0]), this.tickMarkTextSize).height() + 3) : 0;
  }
  
  public CharSequence[] getTickMarkTextArray() {
    return this.tickMarkTextArray;
  }
  
  public int getTickMarkTextColor() {
    return this.tickMarkTextColor;
  }
  
  public int getTickMarkTextMargin() {
    return this.tickMarkTextMargin;
  }
  
  public int getTickMarkTextSize() {
    return this.tickMarkTextSize;
  }
  
  public boolean isEnableThumbOverlap() {
    return this.enableThumbOverlap;
  }
  
  public boolean isStepsAutoBonding() {
    return this.stepsAutoBonding;
  }
  
  protected void onDraw(Canvas paramCanvas) {
    super.onDraw(paramCanvas);
    onDrawTickMark(paramCanvas, this.paint);
    onDrawProgressBar(paramCanvas, this.paint);
    onDrawSteps(paramCanvas, this.paint);
    onDrawSeekBar(paramCanvas);
  }
  
  protected void onDrawProgressBar(Canvas paramCanvas, Paint paramPaint) {
    Rect rect;
    if (Utils.verifyBitmap(this.progressDefaultBitmap)) {
      paramCanvas.drawBitmap(this.progressDefaultBitmap, null, this.progressDefaultDstRect, paramPaint);
    } else {
      paramPaint.setColor(this.progressDefaultColor);
      RectF rectF = this.progressDefaultDstRect;
      float f = this.progressRadius;
      paramCanvas.drawRoundRect(rectF, f, f, paramPaint);
    } 
    if (this.seekBarMode == 2) {
      this.progressDstRect.top = getProgressTop();
      this.progressDstRect.left = this.leftSB.left + this.leftSB.getThumbScaleWidth() / 2.0F + this.progressWidth * this.leftSB.currPercent;
      this.progressDstRect.right = this.rightSB.left + this.rightSB.getThumbScaleWidth() / 2.0F + this.progressWidth * this.rightSB.currPercent;
      this.progressDstRect.bottom = getProgressBottom();
    } else {
      this.progressDstRect.top = getProgressTop();
      this.progressDstRect.left = this.leftSB.left + this.leftSB.getThumbScaleWidth() / 2.0F;
      this.progressDstRect.right = this.leftSB.left + this.leftSB.getThumbScaleWidth() / 2.0F + this.progressWidth * this.leftSB.currPercent;
      this.progressDstRect.bottom = getProgressBottom();
    } 
    if (Utils.verifyBitmap(this.progressBitmap)) {
      rect = this.progressSrcRect;
      rect.top = 0;
      rect.bottom = this.progressBitmap.getHeight();
      int i = this.progressBitmap.getWidth();
      if (this.seekBarMode == 2) {
        rect = this.progressSrcRect;
        float f = i;
        rect.left = (int)(this.leftSB.currPercent * f);
        this.progressSrcRect.right = (int)(f * this.rightSB.currPercent);
      } else {
        rect = this.progressSrcRect;
        rect.left = 0;
        rect.right = (int)(i * this.leftSB.currPercent);
      } 
      paramCanvas.drawBitmap(this.progressBitmap, this.progressSrcRect, this.progressDstRect, null);
    } else {
      rect.setColor(this.progressColor);
      RectF rectF = this.progressDstRect;
      float f = this.progressRadius;
      paramCanvas.drawRoundRect(rectF, f, f, (Paint)rect);
    } 
  }
  
  protected void onDrawSeekBar(Canvas paramCanvas) {
    if (this.leftSB.getIndicatorShowMode() == 3)
      this.leftSB.setShowIndicatorEnable(true); 
    this.leftSB.draw(paramCanvas);
    if (this.seekBarMode == 2) {
      if (this.rightSB.getIndicatorShowMode() == 3)
        this.rightSB.setShowIndicatorEnable(true); 
      this.rightSB.draw(paramCanvas);
    } 
  }
  
  protected void onDrawSteps(Canvas paramCanvas, Paint paramPaint) {
    if (!verifyStepsMode())
      return; 
    int i = getProgressWidth() / this.steps;
    float f = (this.stepsHeight - getProgressHeight()) / 2.0F;
    for (byte b = 0; b <= this.steps; b++) {
      float f1 = (getProgressLeft() + b * i) - this.stepsWidth / 2.0F;
      this.stepDivRect.set(f1, getProgressTop() - f, this.stepsWidth + f1, getProgressBottom() + f);
      if (this.stepsBitmaps.isEmpty() || this.stepsBitmaps.size() <= b) {
        paramPaint.setColor(this.stepsColor);
        RectF rectF = this.stepDivRect;
        f1 = this.stepsRadius;
        paramCanvas.drawRoundRect(rectF, f1, f1, paramPaint);
      } else {
        paramCanvas.drawBitmap(this.stepsBitmaps.get(b), null, this.stepDivRect, paramPaint);
      } 
    } 
  }
  
  protected void onDrawTickMark(Canvas paramCanvas, Paint paramPaint) {
    // Byte code:
    //   0: aload_0
    //   1: getfield tickMarkTextArray : [Ljava/lang/CharSequence;
    //   4: astore_3
    //   5: aload_3
    //   6: ifnull -> 342
    //   9: aload_0
    //   10: getfield progressWidth : I
    //   13: aload_3
    //   14: arraylength
    //   15: iconst_1
    //   16: isub
    //   17: idiv
    //   18: istore #4
    //   20: iconst_0
    //   21: istore #5
    //   23: aload_0
    //   24: getfield tickMarkTextArray : [Ljava/lang/CharSequence;
    //   27: astore_3
    //   28: iload #5
    //   30: aload_3
    //   31: arraylength
    //   32: if_icmpge -> 342
    //   35: aload_3
    //   36: iload #5
    //   38: aaload
    //   39: invokeinterface toString : ()Ljava/lang/String;
    //   44: astore_3
    //   45: aload_3
    //   46: invokestatic isEmpty : (Ljava/lang/CharSequence;)Z
    //   49: ifeq -> 55
    //   52: goto -> 336
    //   55: aload_2
    //   56: aload_3
    //   57: iconst_0
    //   58: aload_3
    //   59: invokevirtual length : ()I
    //   62: aload_0
    //   63: getfield tickMarkTextRect : Landroid/graphics/Rect;
    //   66: invokevirtual getTextBounds : (Ljava/lang/String;IILandroid/graphics/Rect;)V
    //   69: aload_2
    //   70: aload_0
    //   71: getfield tickMarkTextColor : I
    //   74: invokevirtual setColor : (I)V
    //   77: aload_0
    //   78: getfield tickMarkMode : I
    //   81: iconst_1
    //   82: if_icmpne -> 173
    //   85: aload_0
    //   86: getfield tickMarkGravity : I
    //   89: istore #6
    //   91: iload #6
    //   93: iconst_2
    //   94: if_icmpne -> 125
    //   97: aload_0
    //   98: invokevirtual getProgressLeft : ()I
    //   101: iload #5
    //   103: iload #4
    //   105: imul
    //   106: iadd
    //   107: aload_0
    //   108: getfield tickMarkTextRect : Landroid/graphics/Rect;
    //   111: invokevirtual width : ()I
    //   114: isub
    //   115: istore #6
    //   117: iload #6
    //   119: i2f
    //   120: fstore #7
    //   122: goto -> 285
    //   125: iload #6
    //   127: iconst_1
    //   128: if_icmpne -> 158
    //   131: aload_0
    //   132: invokevirtual getProgressLeft : ()I
    //   135: iload #5
    //   137: iload #4
    //   139: imul
    //   140: iadd
    //   141: i2f
    //   142: aload_0
    //   143: getfield tickMarkTextRect : Landroid/graphics/Rect;
    //   146: invokevirtual width : ()I
    //   149: i2f
    //   150: fconst_2
    //   151: fdiv
    //   152: fsub
    //   153: fstore #7
    //   155: goto -> 285
    //   158: aload_0
    //   159: invokevirtual getProgressLeft : ()I
    //   162: iload #5
    //   164: iload #4
    //   166: imul
    //   167: iadd
    //   168: istore #6
    //   170: goto -> 117
    //   173: aload_3
    //   174: invokestatic parseFloat : (Ljava/lang/String;)F
    //   177: fstore #8
    //   179: aload_0
    //   180: invokevirtual getRangeSeekBarState : ()[Lcom/jaygoo/widget/SeekBarState;
    //   183: astore #9
    //   185: fload #8
    //   187: aload #9
    //   189: iconst_0
    //   190: aaload
    //   191: getfield value : F
    //   194: invokestatic compareFloat : (FF)I
    //   197: iconst_m1
    //   198: if_icmpeq -> 233
    //   201: fload #8
    //   203: aload #9
    //   205: iconst_1
    //   206: aaload
    //   207: getfield value : F
    //   210: invokestatic compareFloat : (FF)I
    //   213: iconst_1
    //   214: if_icmpeq -> 233
    //   217: aload_0
    //   218: getfield seekBarMode : I
    //   221: iconst_2
    //   222: if_icmpne -> 233
    //   225: aload_2
    //   226: aload_0
    //   227: getfield tickMarkInRangeTextColor : I
    //   230: invokevirtual setColor : (I)V
    //   233: aload_0
    //   234: invokevirtual getProgressLeft : ()I
    //   237: i2f
    //   238: fstore #10
    //   240: aload_0
    //   241: getfield progressWidth : I
    //   244: i2f
    //   245: fstore #7
    //   247: aload_0
    //   248: getfield minProgress : F
    //   251: fstore #11
    //   253: fload #10
    //   255: fload #7
    //   257: fload #8
    //   259: fload #11
    //   261: fsub
    //   262: fmul
    //   263: aload_0
    //   264: getfield maxProgress : F
    //   267: fload #11
    //   269: fsub
    //   270: fdiv
    //   271: fadd
    //   272: aload_0
    //   273: getfield tickMarkTextRect : Landroid/graphics/Rect;
    //   276: invokevirtual width : ()I
    //   279: i2f
    //   280: fconst_2
    //   281: fdiv
    //   282: fsub
    //   283: fstore #7
    //   285: aload_0
    //   286: getfield tickMarkLayoutGravity : I
    //   289: ifne -> 306
    //   292: aload_0
    //   293: invokevirtual getProgressTop : ()I
    //   296: aload_0
    //   297: getfield tickMarkTextMargin : I
    //   300: isub
    //   301: istore #6
    //   303: goto -> 325
    //   306: aload_0
    //   307: invokevirtual getProgressBottom : ()I
    //   310: aload_0
    //   311: getfield tickMarkTextMargin : I
    //   314: iadd
    //   315: aload_0
    //   316: getfield tickMarkTextRect : Landroid/graphics/Rect;
    //   319: invokevirtual height : ()I
    //   322: iadd
    //   323: istore #6
    //   325: aload_1
    //   326: aload_3
    //   327: fload #7
    //   329: iload #6
    //   331: i2f
    //   332: aload_2
    //   333: invokevirtual drawText : (Ljava/lang/String;FFLandroid/graphics/Paint;)V
    //   336: iinc #5, 1
    //   339: goto -> 23
    //   342: return
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    int i = View.MeasureSpec.getSize(paramInt2);
    paramInt2 = View.MeasureSpec.getMode(paramInt2);
    if (paramInt2 == 1073741824) {
      paramInt2 = View.MeasureSpec.makeMeasureSpec(i, 1073741824);
    } else if (paramInt2 == Integer.MIN_VALUE && getParent() instanceof ViewGroup && i == -1) {
      paramInt2 = View.MeasureSpec.makeMeasureSpec(((ViewGroup)getParent()).getMeasuredHeight(), -2147483648);
    } else {
      float f;
      if (this.gravity == 2) {
        float f1;
        if (this.tickMarkTextArray != null && this.tickMarkLayoutGravity == 1) {
          f = getRawHeight();
          f1 = getTickMarkRawHeight();
        } else {
          f = getRawHeight();
          f1 = Math.max(this.leftSB.getThumbScaleHeight(), this.rightSB.getThumbScaleHeight()) / 2.0F;
        } 
        f = (f - f1) * 2.0F;
      } else {
        f = getRawHeight();
      } 
      paramInt2 = View.MeasureSpec.makeMeasureSpec((int)f, 1073741824);
    } 
    super.onMeasure(paramInt1, paramInt2);
  }
  
  protected void onMeasureProgress(int paramInt1, int paramInt2) {
    int i = paramInt2 - getPaddingBottom() - getPaddingTop();
    if (paramInt2 <= 0)
      return; 
    paramInt2 = this.gravity;
    if (paramInt2 == 0) {
      float f1;
      if (this.leftSB.getIndicatorShowMode() != 1 || this.rightSB.getIndicatorShowMode() != 1) {
        f1 = Math.max(this.leftSB.getIndicatorRawHeight(), this.rightSB.getIndicatorRawHeight());
      } else {
        f1 = 0.0F;
      } 
      float f2 = Math.max(this.leftSB.getThumbScaleHeight(), this.rightSB.getThumbScaleHeight());
      paramInt2 = this.progressHeight;
      f2 -= paramInt2 / 2.0F;
      this.progressTop = (int)((f2 - paramInt2) / 2.0F + f1);
      if (this.tickMarkTextArray != null && this.tickMarkLayoutGravity == 0)
        this.progressTop = (int)Math.max(getTickMarkRawHeight(), f1 + (f2 - this.progressHeight) / 2.0F); 
      this.progressBottom = this.progressTop + this.progressHeight;
    } else if (paramInt2 == 1) {
      if (this.tickMarkTextArray != null && this.tickMarkLayoutGravity == 1) {
        this.progressBottom = i - getTickMarkRawHeight();
      } else {
        this.progressBottom = (int)(i - Math.max(this.leftSB.getThumbScaleHeight(), this.rightSB.getThumbScaleHeight()) / 2.0F + this.progressHeight / 2.0F);
      } 
      this.progressTop = this.progressBottom - this.progressHeight;
    } else {
      paramInt2 = this.progressHeight;
      this.progressTop = (i - paramInt2) / 2;
      this.progressBottom = this.progressTop + paramInt2;
    } 
    paramInt2 = (int)Math.max(this.leftSB.getThumbScaleWidth(), this.rightSB.getThumbScaleWidth()) / 2;
    this.progressLeft = getPaddingLeft() + paramInt2;
    this.progressRight = paramInt1 - paramInt2 - getPaddingRight();
    this.progressWidth = this.progressRight - this.progressLeft;
    this.progressDefaultDstRect.set(getProgressLeft(), getProgressTop(), getProgressRight(), getProgressBottom());
    this.progressPaddingRight = paramInt1 - this.progressRight;
    if (this.progressRadius <= 0.0F)
      this.progressRadius = (int)((getProgressBottom() - getProgressTop()) * 0.45F); 
    initProgressBitmap();
  }
  
  public void onRestoreInstanceState(Parcelable paramParcelable) {
    try {
      SavedState savedState = (SavedState)paramParcelable;
      super.onRestoreInstanceState(savedState.getSuperState());
      setRange(savedState.minValue, savedState.maxValue, savedState.rangeInterval);
      setProgress(savedState.currSelectedMin, savedState.currSelectedMax);
    } catch (Exception exception) {
      exception.printStackTrace();
    } 
  }
  
  public Parcelable onSaveInstanceState() {
    SavedState savedState = new SavedState(super.onSaveInstanceState());
    savedState.minValue = this.minProgress;
    savedState.maxValue = this.maxProgress;
    savedState.rangeInterval = this.minInterval;
    SeekBarState[] arrayOfSeekBarState = getRangeSeekBarState();
    savedState.currSelectedMin = (arrayOfSeekBarState[0]).value;
    savedState.currSelectedMax = (arrayOfSeekBarState[1]).value;
    return (Parcelable)savedState;
  }
  
  protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    onMeasureProgress(paramInt1, paramInt2);
    setRange(this.minProgress, this.maxProgress, this.minInterval);
    paramInt1 = (getProgressBottom() + getProgressTop()) / 2;
    this.leftSB.onSizeChanged(getProgressLeft(), paramInt1);
    if (this.seekBarMode == 2)
      this.rightSB.onSizeChanged(getProgressLeft(), paramInt1); 
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    boolean bool1 = this.isEnable;
    boolean bool2 = true;
    if (!bool1)
      return true; 
    int i = paramMotionEvent.getAction();
    float f = 1.0F;
    bool1 = false;
    if (i != 0) {
      if (i != 1) {
        if (i != 2) {
          if (i == 3) {
            if (this.seekBarMode == 2)
              this.rightSB.setShowIndicatorEnable(false); 
            SeekBar seekBar = this.currTouchSB;
            if (seekBar == this.leftSB) {
              resetCurrentSeekBarThumb();
            } else if (seekBar == this.rightSB) {
              resetCurrentSeekBarThumb();
            } 
            this.leftSB.setShowIndicatorEnable(false);
            if (this.callback != null) {
              SeekBarState[] arrayOfSeekBarState = getRangeSeekBarState();
              this.callback.onRangeChanged(this, (arrayOfSeekBarState[0]).value, (arrayOfSeekBarState[1]).value, false);
            } 
            if (getParent() != null)
              getParent().requestDisallowInterceptTouchEvent(true); 
            changeThumbActivateState(false);
          } 
        } else {
          float f1 = getEventX(paramMotionEvent);
          if (this.seekBarMode == 2 && this.leftSB.currPercent == this.rightSB.currPercent) {
            this.currTouchSB.materialRestore();
            OnRangeChangedListener onRangeChangedListener1 = this.callback;
            if (onRangeChangedListener1 != null) {
              if (this.currTouchSB == this.leftSB) {
                bool2 = true;
              } else {
                bool2 = false;
              } 
              onRangeChangedListener1.onStopTrackingTouch(this, bool2);
            } 
            if (f1 - this.touchDownX > 0.0F) {
              SeekBar seekBar1 = this.currTouchSB;
              if (seekBar1 != this.rightSB) {
                seekBar1.setShowIndicatorEnable(false);
                resetCurrentSeekBarThumb();
                this.currTouchSB = this.rightSB;
              } 
            } else {
              SeekBar seekBar1 = this.currTouchSB;
              if (seekBar1 != this.leftSB) {
                seekBar1.setShowIndicatorEnable(false);
                resetCurrentSeekBarThumb();
                this.currTouchSB = this.leftSB;
              } 
            } 
            onRangeChangedListener1 = this.callback;
            if (onRangeChangedListener1 != null) {
              if (this.currTouchSB == this.leftSB) {
                bool2 = true;
              } else {
                bool2 = false;
              } 
              onRangeChangedListener1.onStartTrackingTouch(this, bool2);
            } 
          } 
          scaleCurrentSeekBarThumb();
          SeekBar seekBar = this.currTouchSB;
          if (seekBar.material < 1.0F)
            f = this.currTouchSB.material + 0.1F; 
          seekBar.material = f;
          this.touchDownX = f1;
          this.currTouchSB.slide(calculateCurrentSeekBarPercent(this.touchDownX));
          this.currTouchSB.setShowIndicatorEnable(true);
          if (this.callback != null) {
            SeekBarState[] arrayOfSeekBarState = getRangeSeekBarState();
            this.callback.onRangeChanged(this, (arrayOfSeekBarState[0]).value, (arrayOfSeekBarState[1]).value, true);
          } 
          invalidate();
          if (getParent() != null)
            getParent().requestDisallowInterceptTouchEvent(true); 
          changeThumbActivateState(true);
        } 
      } else {
        if (verifyStepsMode() && this.stepsAutoBonding) {
          float f1 = calculateCurrentSeekBarPercent(getEventX(paramMotionEvent));
          f = 1.0F / this.steps;
          i = (new BigDecimal((f1 / f))).setScale(0, RoundingMode.HALF_UP).intValue();
          this.currTouchSB.slide(i * f);
        } 
        if (this.seekBarMode == 2)
          this.rightSB.setShowIndicatorEnable(false); 
        this.leftSB.setShowIndicatorEnable(false);
        this.currTouchSB.materialRestore();
        resetCurrentSeekBarThumb();
        if (this.callback != null) {
          SeekBarState[] arrayOfSeekBarState = getRangeSeekBarState();
          this.callback.onRangeChanged(this, (arrayOfSeekBarState[0]).value, (arrayOfSeekBarState[1]).value, false);
        } 
        if (getParent() != null)
          getParent().requestDisallowInterceptTouchEvent(true); 
        OnRangeChangedListener onRangeChangedListener1 = this.callback;
        if (onRangeChangedListener1 != null) {
          if (this.currTouchSB != this.leftSB)
            bool2 = false; 
          onRangeChangedListener1.onStopTrackingTouch(this, bool2);
        } 
        changeThumbActivateState(false);
      } 
      return super.onTouchEvent(paramMotionEvent);
    } 
    this.touchDownX = getEventX(paramMotionEvent);
    this.touchDownY = getEventY(paramMotionEvent);
    if (this.seekBarMode == 2) {
      if (this.rightSB.currPercent >= 1.0F && this.leftSB.collide(getEventX(paramMotionEvent), getEventY(paramMotionEvent))) {
        this.currTouchSB = this.leftSB;
        scaleCurrentSeekBarThumb();
      } else if (this.rightSB.collide(getEventX(paramMotionEvent), getEventY(paramMotionEvent))) {
        this.currTouchSB = this.rightSB;
        scaleCurrentSeekBarThumb();
      } else {
        f = (this.touchDownX - getProgressLeft()) * 1.0F / this.progressWidth;
        if (Math.abs(this.leftSB.currPercent - f) < Math.abs(this.rightSB.currPercent - f)) {
          this.currTouchSB = this.leftSB;
        } else {
          this.currTouchSB = this.rightSB;
        } 
        f = calculateCurrentSeekBarPercent(this.touchDownX);
        this.currTouchSB.slide(f);
      } 
    } else {
      this.currTouchSB = this.leftSB;
      scaleCurrentSeekBarThumb();
    } 
    if (getParent() != null)
      getParent().requestDisallowInterceptTouchEvent(true); 
    OnRangeChangedListener onRangeChangedListener = this.callback;
    if (onRangeChangedListener != null) {
      bool2 = bool1;
      if (this.currTouchSB == this.leftSB)
        bool2 = true; 
      onRangeChangedListener.onStartTrackingTouch(this, bool2);
    } 
    changeThumbActivateState(true);
    return true;
  }
  
  public void setEnableThumbOverlap(boolean paramBoolean) {
    this.enableThumbOverlap = paramBoolean;
  }
  
  public void setEnabled(boolean paramBoolean) {
    super.setEnabled(paramBoolean);
    this.isEnable = paramBoolean;
  }
  
  public void setGravity(int paramInt) {
    this.gravity = paramInt;
  }
  
  public void setIndicatorText(String paramString) {
    this.leftSB.setIndicatorText(paramString);
    if (this.seekBarMode == 2)
      this.rightSB.setIndicatorText(paramString); 
  }
  
  public void setIndicatorTextDecimalFormat(String paramString) {
    this.leftSB.setIndicatorTextDecimalFormat(paramString);
    if (this.seekBarMode == 2)
      this.rightSB.setIndicatorTextDecimalFormat(paramString); 
  }
  
  public void setIndicatorTextStringFormat(String paramString) {
    this.leftSB.setIndicatorTextStringFormat(paramString);
    if (this.seekBarMode == 2)
      this.rightSB.setIndicatorTextStringFormat(paramString); 
  }
  
  public void setOnRangeChangedListener(OnRangeChangedListener paramOnRangeChangedListener) {
    this.callback = paramOnRangeChangedListener;
  }
  
  public void setProgress(float paramFloat) {
    setProgress(paramFloat, this.maxProgress);
  }
  
  public void setProgress(float paramFloat1, float paramFloat2) {
    float f1 = Math.min(paramFloat1, paramFloat2);
    paramFloat2 = Math.max(f1, paramFloat2);
    float f2 = this.minInterval;
    paramFloat1 = f1;
    if (paramFloat2 - f1 < f2)
      paramFloat1 = paramFloat2 - f2; 
    f1 = this.minProgress;
    if (paramFloat1 >= f1) {
      f2 = this.maxProgress;
      if (paramFloat2 <= f2) {
        f2 -= f1;
        this.leftSB.currPercent = Math.abs(paramFloat1 - f1) / f2;
        if (this.seekBarMode == 2)
          this.rightSB.currPercent = Math.abs(paramFloat2 - this.minProgress) / f2; 
        OnRangeChangedListener onRangeChangedListener = this.callback;
        if (onRangeChangedListener != null)
          onRangeChangedListener.onRangeChanged(this, paramFloat1, paramFloat2, false); 
        invalidate();
        return;
      } 
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("setProgress() max > (preset max - offsetValue) . #max:");
      stringBuilder1.append(paramFloat2);
      stringBuilder1.append(" #preset max:");
      stringBuilder1.append(paramFloat2);
      throw new IllegalArgumentException(stringBuilder1.toString());
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("setProgress() min < (preset min - offsetValue) . #min:");
    stringBuilder.append(paramFloat1);
    stringBuilder.append(" #preset min:");
    stringBuilder.append(paramFloat2);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public void setProgressBottom(int paramInt) {
    this.progressBottom = paramInt;
  }
  
  public void setProgressColor(int paramInt) {
    this.progressColor = paramInt;
  }
  
  public void setProgressColor(int paramInt1, int paramInt2) {
    this.progressDefaultColor = paramInt1;
    this.progressColor = paramInt2;
  }
  
  public void setProgressDefaultColor(int paramInt) {
    this.progressDefaultColor = paramInt;
  }
  
  public void setProgressDefaultDrawableId(int paramInt) {
    this.progressDefaultDrawableId = paramInt;
    this.progressDefaultBitmap = null;
    initProgressBitmap();
  }
  
  public void setProgressDrawableId(int paramInt) {
    this.progressDrawableId = paramInt;
    this.progressBitmap = null;
    initProgressBitmap();
  }
  
  public void setProgressHeight(int paramInt) {
    this.progressHeight = paramInt;
  }
  
  public void setProgressLeft(int paramInt) {
    this.progressLeft = paramInt;
  }
  
  public void setProgressRadius(float paramFloat) {
    this.progressRadius = paramFloat;
  }
  
  public void setProgressRight(int paramInt) {
    this.progressRight = paramInt;
  }
  
  public void setProgressTop(int paramInt) {
    this.progressTop = paramInt;
  }
  
  public void setProgressWidth(int paramInt) {
    this.progressWidth = paramInt;
  }
  
  public void setRange(float paramFloat1, float paramFloat2) {
    setRange(paramFloat1, paramFloat2, this.minInterval);
  }
  
  public void setRange(float paramFloat1, float paramFloat2, float paramFloat3) {
    if (paramFloat2 > paramFloat1) {
      if (paramFloat3 >= 0.0F) {
        float f = paramFloat2 - paramFloat1;
        if (paramFloat3 < f) {
          this.maxProgress = paramFloat2;
          this.minProgress = paramFloat1;
          this.minInterval = paramFloat3;
          this.reservePercent = paramFloat3 / f;
          if (this.seekBarMode == 2)
            if (this.leftSB.currPercent + this.reservePercent <= 1.0F && this.leftSB.currPercent + this.reservePercent > this.rightSB.currPercent) {
              this.leftSB.currPercent += this.reservePercent;
            } else if (this.rightSB.currPercent - this.reservePercent >= 0.0F && this.rightSB.currPercent - this.reservePercent < this.leftSB.currPercent) {
              this.rightSB.currPercent -= this.reservePercent;
            }  
          invalidate();
          return;
        } 
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("setRange() interval must be less than (max - min) ! #minInterval:");
        stringBuilder2.append(paramFloat3);
        stringBuilder2.append(" #max - min:");
        stringBuilder2.append(f);
        throw new IllegalArgumentException(stringBuilder2.toString());
      } 
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("setRange() interval must be greater than zero ! #minInterval:");
      stringBuilder1.append(paramFloat3);
      throw new IllegalArgumentException(stringBuilder1.toString());
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("setRange() max must be greater than min ! #max:");
    stringBuilder.append(paramFloat2);
    stringBuilder.append(" #min:");
    stringBuilder.append(paramFloat1);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public void setSeekBarMode(int paramInt) {
    this.seekBarMode = paramInt;
    SeekBar seekBar = this.rightSB;
    boolean bool = true;
    if (paramInt == 1)
      bool = false; 
    seekBar.setVisible(bool);
  }
  
  public void setSteps(int paramInt) {
    this.steps = paramInt;
  }
  
  public void setStepsAutoBonding(boolean paramBoolean) {
    this.stepsAutoBonding = paramBoolean;
  }
  
  public void setStepsBitmaps(List<Bitmap> paramList) {
    if (paramList != null && !paramList.isEmpty() && paramList.size() > this.steps) {
      this.stepsBitmaps.clear();
      this.stepsBitmaps.addAll(paramList);
      return;
    } 
    throw new IllegalArgumentException("stepsBitmaps must > steps !");
  }
  
  public void setStepsColor(int paramInt) {
    this.stepsColor = paramInt;
  }
  
  public void setStepsDrawable(List<Integer> paramList) {
    if (paramList != null && !paramList.isEmpty() && paramList.size() > this.steps) {
      if (verifyStepsMode()) {
        ArrayList<Bitmap> arrayList = new ArrayList();
        for (byte b = 0; b < paramList.size(); b++)
          arrayList.add(Utils.drawableToBitmap(getContext(), (int)this.stepsWidth, (int)this.stepsHeight, ((Integer)paramList.get(b)).intValue())); 
        setStepsBitmaps(arrayList);
        return;
      } 
      throw new IllegalArgumentException("stepsWidth must > 0, stepsHeight must > 0,steps must > 0 First!!");
    } 
    IllegalArgumentException illegalArgumentException = new IllegalArgumentException("stepsDrawableIds must > steps !");
    throw illegalArgumentException;
  }
  
  public void setStepsDrawableId(int paramInt) {
    this.stepsBitmaps.clear();
    this.stepsDrawableId = paramInt;
    initStepsBitmap();
  }
  
  public void setStepsHeight(float paramFloat) {
    this.stepsHeight = paramFloat;
  }
  
  public void setStepsRadius(float paramFloat) {
    this.stepsRadius = paramFloat;
  }
  
  public void setStepsWidth(float paramFloat) {
    this.stepsWidth = paramFloat;
  }
  
  public void setTickMarkGravity(int paramInt) {
    this.tickMarkGravity = paramInt;
  }
  
  public void setTickMarkInRangeTextColor(int paramInt) {
    this.tickMarkInRangeTextColor = paramInt;
  }
  
  public void setTickMarkLayoutGravity(int paramInt) {
    this.tickMarkLayoutGravity = paramInt;
  }
  
  public void setTickMarkMode(int paramInt) {
    this.tickMarkMode = paramInt;
  }
  
  public void setTickMarkTextArray(CharSequence[] paramArrayOfCharSequence) {
    this.tickMarkTextArray = paramArrayOfCharSequence;
  }
  
  public void setTickMarkTextColor(int paramInt) {
    this.tickMarkTextColor = paramInt;
  }
  
  public void setTickMarkTextMargin(int paramInt) {
    this.tickMarkTextMargin = paramInt;
  }
  
  public void setTickMarkTextSize(int paramInt) {
    this.tickMarkTextSize = paramInt;
  }
  
  public void setTypeface(Typeface paramTypeface) {
    this.paint.setTypeface(paramTypeface);
  }
  
  public static class Gravity {
    public static final int BOTTOM = 1;
    
    public static final int CENTER = 2;
    
    public static final int TOP = 0;
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface GravityDef {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface SeekBarModeDef {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface TickMarkGravityDef {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface TickMarkLayoutGravityDef {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface TickMarkModeDef {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/jaygoo/widget/RangeSeekBar.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */