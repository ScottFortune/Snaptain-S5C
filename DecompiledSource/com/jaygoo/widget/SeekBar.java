package com.jaygoo.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import androidx.core.content.ContextCompat;
import java.text.DecimalFormat;

public class SeekBar {
  public static final int INDICATOR_ALWAYS_HIDE = 1;
  
  public static final int INDICATOR_ALWAYS_SHOW = 3;
  
  public static final int INDICATOR_ALWAYS_SHOW_AFTER_TOUCH = 2;
  
  public static final int INDICATOR_SHOW_WHEN_TOUCH = 0;
  
  public static final int MATCH_PARENT = -2;
  
  public static final int WRAP_CONTENT = -1;
  
  ValueAnimator anim;
  
  int bottom;
  
  float currPercent;
  
  Path indicatorArrowPath = new Path();
  
  private int indicatorArrowSize;
  
  private int indicatorBackgroundColor;
  
  Bitmap indicatorBitmap;
  
  private int indicatorDrawableId;
  
  private int indicatorHeight;
  
  private int indicatorMargin;
  
  private int indicatorPaddingBottom;
  
  private int indicatorPaddingLeft;
  
  private int indicatorPaddingRight;
  
  private int indicatorPaddingTop;
  
  private float indicatorRadius;
  
  Rect indicatorRect = new Rect();
  
  private int indicatorShowMode;
  
  private int indicatorTextColor;
  
  DecimalFormat indicatorTextDecimalFormat;
  
  Rect indicatorTextRect = new Rect();
  
  private int indicatorTextSize;
  
  String indicatorTextStringFormat;
  
  private int indicatorWidth;
  
  boolean isActivate = false;
  
  boolean isLeft;
  
  private boolean isShowIndicator;
  
  boolean isVisible = true;
  
  int left;
  
  float material = 0.0F;
  
  Paint paint = new Paint(1);
  
  RangeSeekBar rangeSeekBar;
  
  int right;
  
  int scaleThumbHeight;
  
  int scaleThumbWidth;
  
  Bitmap thumbBitmap;
  
  private int thumbDrawableId;
  
  private int thumbHeight;
  
  Bitmap thumbInactivatedBitmap;
  
  private int thumbInactivatedDrawableId;
  
  float thumbScaleRatio;
  
  private int thumbWidth;
  
  int top;
  
  String userText2Draw;
  
  public SeekBar(RangeSeekBar paramRangeSeekBar, AttributeSet paramAttributeSet, boolean paramBoolean) {
    this.rangeSeekBar = paramRangeSeekBar;
    this.isLeft = paramBoolean;
    initAttrs(paramAttributeSet);
    initBitmap();
    initVariables();
  }
  
  private void initAttrs(AttributeSet paramAttributeSet) {
    TypedArray typedArray = getContext().obtainStyledAttributes(paramAttributeSet, R.styleable.RangeSeekBar);
    if (typedArray == null)
      return; 
    this.indicatorMargin = (int)typedArray.getDimension(R.styleable.RangeSeekBar_rsb_indicator_margin, 0.0F);
    this.indicatorDrawableId = typedArray.getResourceId(R.styleable.RangeSeekBar_rsb_indicator_drawable, 0);
    this.indicatorShowMode = typedArray.getInt(R.styleable.RangeSeekBar_rsb_indicator_show_mode, 1);
    this.indicatorHeight = typedArray.getLayoutDimension(R.styleable.RangeSeekBar_rsb_indicator_height, -1);
    this.indicatorWidth = typedArray.getLayoutDimension(R.styleable.RangeSeekBar_rsb_indicator_width, -1);
    this.indicatorTextSize = (int)typedArray.getDimension(R.styleable.RangeSeekBar_rsb_indicator_text_size, Utils.dp2px(getContext(), 14.0F));
    this.indicatorTextColor = typedArray.getColor(R.styleable.RangeSeekBar_rsb_indicator_text_color, -1);
    this.indicatorBackgroundColor = typedArray.getColor(R.styleable.RangeSeekBar_rsb_indicator_background_color, ContextCompat.getColor(getContext(), R.color.colorAccent));
    this.indicatorPaddingLeft = (int)typedArray.getDimension(R.styleable.RangeSeekBar_rsb_indicator_padding_left, 0.0F);
    this.indicatorPaddingRight = (int)typedArray.getDimension(R.styleable.RangeSeekBar_rsb_indicator_padding_right, 0.0F);
    this.indicatorPaddingTop = (int)typedArray.getDimension(R.styleable.RangeSeekBar_rsb_indicator_padding_top, 0.0F);
    this.indicatorPaddingBottom = (int)typedArray.getDimension(R.styleable.RangeSeekBar_rsb_indicator_padding_bottom, 0.0F);
    this.indicatorArrowSize = (int)typedArray.getDimension(R.styleable.RangeSeekBar_rsb_indicator_arrow_size, 0.0F);
    this.thumbDrawableId = typedArray.getResourceId(R.styleable.RangeSeekBar_rsb_thumb_drawable, R.drawable.rsb_default_thumb);
    this.thumbInactivatedDrawableId = typedArray.getResourceId(R.styleable.RangeSeekBar_rsb_thumb_inactivated_drawable, 0);
    this.thumbWidth = (int)typedArray.getDimension(R.styleable.RangeSeekBar_rsb_thumb_width, Utils.dp2px(getContext(), 26.0F));
    this.thumbHeight = (int)typedArray.getDimension(R.styleable.RangeSeekBar_rsb_thumb_height, Utils.dp2px(getContext(), 26.0F));
    this.thumbScaleRatio = typedArray.getFloat(R.styleable.RangeSeekBar_rsb_thumb_scale_ratio, 1.0F);
    this.indicatorRadius = typedArray.getDimension(R.styleable.RangeSeekBar_rsb_indicator_radius, 0.0F);
    typedArray.recycle();
  }
  
  private void initBitmap() {
    setIndicatorDrawableId(this.indicatorDrawableId);
    setThumbDrawableId(this.thumbDrawableId, this.thumbWidth, this.thumbHeight);
    setThumbInactivatedDrawableId(this.thumbInactivatedDrawableId, this.thumbWidth, this.thumbHeight);
  }
  
  protected boolean collide(float paramFloat1, float paramFloat2) {
    boolean bool;
    int i = (int)(this.rangeSeekBar.getProgressWidth() * this.currPercent);
    if (paramFloat1 > (this.left + i) && paramFloat1 < (this.right + i) && paramFloat2 > this.top && paramFloat2 < this.bottom) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  protected void draw(Canvas paramCanvas) {
    if (!this.isVisible)
      return; 
    int i = (int)(this.rangeSeekBar.getProgressWidth() * this.currPercent);
    paramCanvas.save();
    paramCanvas.translate(i, 0.0F);
    paramCanvas.translate(this.left, 0.0F);
    if (this.isShowIndicator)
      onDrawIndicator(paramCanvas, this.paint, formatCurrentIndicatorText(this.userText2Draw)); 
    onDrawThumb(paramCanvas);
    paramCanvas.restore();
  }
  
  protected String formatCurrentIndicatorText(String paramString) {
    SeekBarState[] arrayOfSeekBarState = this.rangeSeekBar.getRangeSeekBarState();
    String str2 = paramString;
    if (TextUtils.isEmpty(paramString))
      if (this.isLeft) {
        DecimalFormat decimalFormat = this.indicatorTextDecimalFormat;
        if (decimalFormat != null) {
          str2 = decimalFormat.format((arrayOfSeekBarState[0]).value);
        } else {
          str2 = (arrayOfSeekBarState[0]).indicatorText;
        } 
      } else {
        DecimalFormat decimalFormat = this.indicatorTextDecimalFormat;
        if (decimalFormat != null) {
          str2 = decimalFormat.format((arrayOfSeekBarState[1]).value);
        } else {
          str2 = (arrayOfSeekBarState[1]).indicatorText;
        } 
      }  
    String str1 = this.indicatorTextStringFormat;
    paramString = str2;
    if (str1 != null)
      paramString = String.format(str1, new Object[] { str2 }); 
    return paramString;
  }
  
  protected boolean getActivate() {
    return this.isActivate;
  }
  
  public Context getContext() {
    return this.rangeSeekBar.getContext();
  }
  
  public int getIndicatorArrowSize() {
    return this.indicatorArrowSize;
  }
  
  public int getIndicatorBackgroundColor() {
    return this.indicatorBackgroundColor;
  }
  
  public int getIndicatorDrawableId() {
    return this.indicatorDrawableId;
  }
  
  public int getIndicatorHeight() {
    return this.indicatorHeight;
  }
  
  public int getIndicatorMargin() {
    return this.indicatorMargin;
  }
  
  public int getIndicatorPaddingBottom() {
    return this.indicatorPaddingBottom;
  }
  
  public int getIndicatorPaddingLeft() {
    return this.indicatorPaddingLeft;
  }
  
  public int getIndicatorPaddingRight() {
    return this.indicatorPaddingRight;
  }
  
  public int getIndicatorPaddingTop() {
    return this.indicatorPaddingTop;
  }
  
  public float getIndicatorRadius() {
    return this.indicatorRadius;
  }
  
  public int getIndicatorRawHeight() {
    int i = this.indicatorHeight;
    if (i > 0) {
      if (this.indicatorBitmap != null) {
        int m = this.indicatorMargin;
        return i + m;
      } 
      i += this.indicatorArrowSize;
      int k = this.indicatorMargin;
      return i + k;
    } 
    if (this.indicatorBitmap != null) {
      i = Utils.measureText("8", this.indicatorTextSize).height() + this.indicatorPaddingTop + this.indicatorPaddingBottom;
      int k = this.indicatorMargin;
      return i + k;
    } 
    i = Utils.measureText("8", this.indicatorTextSize).height() + this.indicatorPaddingTop + this.indicatorPaddingBottom + this.indicatorMargin;
    int j = this.indicatorArrowSize;
    return i + j;
  }
  
  public int getIndicatorShowMode() {
    return this.indicatorShowMode;
  }
  
  public int getIndicatorTextColor() {
    return this.indicatorTextColor;
  }
  
  public DecimalFormat getIndicatorTextDecimalFormat() {
    return this.indicatorTextDecimalFormat;
  }
  
  public int getIndicatorTextSize() {
    return this.indicatorTextSize;
  }
  
  public int getIndicatorWidth() {
    return this.indicatorWidth;
  }
  
  public float getProgress() {
    float f1 = this.rangeSeekBar.getMaxProgress();
    float f2 = this.rangeSeekBar.getMinProgress();
    return this.rangeSeekBar.getMinProgress() + (f1 - f2) * this.currPercent;
  }
  
  public float getRawHeight() {
    return (getIndicatorHeight() + getIndicatorArrowSize() + getIndicatorMargin()) + getThumbScaleHeight();
  }
  
  public Resources getResources() {
    return (getContext() != null) ? getContext().getResources() : null;
  }
  
  public int getThumbDrawableId() {
    return this.thumbDrawableId;
  }
  
  public int getThumbHeight() {
    return this.thumbHeight;
  }
  
  public int getThumbInactivatedDrawableId() {
    return this.thumbInactivatedDrawableId;
  }
  
  public float getThumbScaleHeight() {
    return this.thumbHeight * this.thumbScaleRatio;
  }
  
  public float getThumbScaleRatio() {
    return this.thumbScaleRatio;
  }
  
  public float getThumbScaleWidth() {
    return this.thumbWidth * this.thumbScaleRatio;
  }
  
  public int getThumbWidth() {
    return this.thumbWidth;
  }
  
  protected void initVariables() {
    this.scaleThumbWidth = this.thumbWidth;
    this.scaleThumbHeight = this.thumbHeight;
    if (this.indicatorHeight == -1)
      this.indicatorHeight = Utils.measureText("8", this.indicatorTextSize).height() + this.indicatorPaddingTop + this.indicatorPaddingBottom; 
    if (this.indicatorArrowSize <= 0)
      this.indicatorArrowSize = this.thumbWidth / 4; 
  }
  
  public boolean isShowIndicator() {
    return this.isShowIndicator;
  }
  
  public boolean isVisible() {
    return this.isVisible;
  }
  
  public void materialRestore() {
    ValueAnimator valueAnimator = this.anim;
    if (valueAnimator != null)
      valueAnimator.cancel(); 
    this.anim = ValueAnimator.ofFloat(new float[] { this.material, 0.0F });
    this.anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
          public void onAnimationUpdate(ValueAnimator param1ValueAnimator) {
            SeekBar.this.material = ((Float)param1ValueAnimator.getAnimatedValue()).floatValue();
            if (SeekBar.this.rangeSeekBar != null)
              SeekBar.this.rangeSeekBar.invalidate(); 
          }
        });
    this.anim.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter() {
          public void onAnimationEnd(Animator param1Animator) {
            SeekBar seekBar = SeekBar.this;
            seekBar.material = 0.0F;
            if (seekBar.rangeSeekBar != null)
              SeekBar.this.rangeSeekBar.invalidate(); 
          }
        });
    this.anim.start();
  }
  
  protected void onDrawIndicator(Canvas paramCanvas, Paint paramPaint, String paramString) {
    if (paramString == null)
      return; 
    paramPaint.setTextSize(this.indicatorTextSize);
    paramPaint.setStyle(Paint.Style.FILL);
    paramPaint.setColor(this.indicatorBackgroundColor);
    paramPaint.getTextBounds(paramString, 0, paramString.length(), this.indicatorTextRect);
    int i = this.indicatorTextRect.width() + this.indicatorPaddingLeft + this.indicatorPaddingRight;
    int j = this.indicatorWidth;
    int k = i;
    if (j > i)
      k = j; 
    int m = this.indicatorTextRect.height() + this.indicatorPaddingTop + this.indicatorPaddingBottom;
    j = this.indicatorHeight;
    i = m;
    if (j > m)
      i = j; 
    Rect rect = this.indicatorRect;
    rect.left = (int)(this.scaleThumbWidth / 2.0F - k / 2.0F);
    rect.top = this.bottom - i - this.scaleThumbHeight - this.indicatorMargin;
    rect.right = rect.left + k;
    rect = this.indicatorRect;
    rect.bottom = rect.top + i;
    if (this.indicatorBitmap == null) {
      j = this.scaleThumbWidth / 2;
      m = this.indicatorRect.bottom;
      int n = this.indicatorArrowSize;
      this.indicatorArrowPath.reset();
      this.indicatorArrowPath.moveTo(j, m);
      Path path = this.indicatorArrowPath;
      float f1 = (j - n);
      float f2 = (m - n);
      path.lineTo(f1, f2);
      this.indicatorArrowPath.lineTo((n + j), f2);
      this.indicatorArrowPath.close();
      paramCanvas.drawPath(this.indicatorArrowPath, paramPaint);
      Rect rect1 = this.indicatorRect;
      rect1.bottom -= this.indicatorArrowSize;
      rect1 = this.indicatorRect;
      rect1.top -= this.indicatorArrowSize;
    } 
    m = Utils.dp2px(getContext(), 1.0F);
    j = this.indicatorRect.width() / 2 - (int)(this.rangeSeekBar.getProgressWidth() * this.currPercent) - this.rangeSeekBar.getProgressLeft() + m;
    m = this.indicatorRect.width() / 2 - (int)(this.rangeSeekBar.getProgressWidth() * (1.0F - this.currPercent)) - this.rangeSeekBar.getProgressPaddingRight() + m;
    if (j > 0) {
      rect = this.indicatorRect;
      rect.left += j;
      rect = this.indicatorRect;
      rect.right += j;
    } else if (m > 0) {
      rect = this.indicatorRect;
      rect.left -= m;
      rect = this.indicatorRect;
      rect.right -= m;
    } 
    Bitmap bitmap = this.indicatorBitmap;
    if (bitmap != null) {
      Utils.drawBitmap(paramCanvas, paramPaint, bitmap, this.indicatorRect);
    } else if (this.indicatorRadius > 0.0F) {
      RectF rectF = new RectF(this.indicatorRect);
      float f = this.indicatorRadius;
      paramCanvas.drawRoundRect(rectF, f, f, paramPaint);
    } else {
      paramCanvas.drawRect(this.indicatorRect, paramPaint);
    } 
    if (this.indicatorPaddingLeft > 0) {
      k = this.indicatorRect.left + this.indicatorPaddingLeft;
    } else if (this.indicatorPaddingRight > 0) {
      k = this.indicatorRect.right - this.indicatorPaddingRight - this.indicatorTextRect.width();
    } else {
      j = this.indicatorRect.left;
      k = (k - this.indicatorTextRect.width()) / 2 + j;
    } 
    if (this.indicatorPaddingTop > 0) {
      i = this.indicatorRect.top + this.indicatorTextRect.height() + this.indicatorPaddingTop;
    } else if (this.indicatorPaddingBottom > 0) {
      i = this.indicatorRect.bottom - this.indicatorTextRect.height() - this.indicatorPaddingBottom;
    } else {
      i = this.indicatorRect.bottom - (i - this.indicatorTextRect.height()) / 2 + 1;
    } 
    paramPaint.setColor(this.indicatorTextColor);
    paramCanvas.drawText(paramString, k, i, paramPaint);
  }
  
  protected void onDrawThumb(Canvas paramCanvas) {
    Bitmap bitmap = this.thumbInactivatedBitmap;
    if (bitmap != null && !this.isActivate) {
      paramCanvas.drawBitmap(bitmap, 0.0F, this.rangeSeekBar.getProgressTop() + (this.rangeSeekBar.getProgressHeight() - this.scaleThumbHeight) / 2.0F, null);
    } else {
      bitmap = this.thumbBitmap;
      if (bitmap != null)
        paramCanvas.drawBitmap(bitmap, 0.0F, this.rangeSeekBar.getProgressTop() + (this.rangeSeekBar.getProgressHeight() - this.scaleThumbHeight) / 2.0F, null); 
    } 
  }
  
  protected void onSizeChanged(int paramInt1, int paramInt2) {
    initVariables();
    initBitmap();
    float f = paramInt1;
    this.left = (int)(f - getThumbScaleWidth() / 2.0F);
    this.right = (int)(f + getThumbScaleWidth() / 2.0F);
    this.top = paramInt2 - getThumbHeight() / 2;
    this.bottom = paramInt2 + getThumbHeight() / 2;
  }
  
  public void resetThumb() {
    this.scaleThumbWidth = getThumbWidth();
    this.scaleThumbHeight = getThumbHeight();
    int i = this.rangeSeekBar.getProgressBottom();
    int j = this.scaleThumbHeight;
    this.top = i - j / 2;
    this.bottom = i + j / 2;
    setThumbDrawableId(this.thumbDrawableId, this.scaleThumbWidth, j);
  }
  
  public void scaleThumb() {
    this.scaleThumbWidth = (int)getThumbScaleWidth();
    this.scaleThumbHeight = (int)getThumbScaleHeight();
    int i = this.rangeSeekBar.getProgressBottom();
    int j = this.scaleThumbHeight;
    this.top = i - j / 2;
    this.bottom = i + j / 2;
    setThumbDrawableId(this.thumbDrawableId, this.scaleThumbWidth, j);
  }
  
  protected void setActivate(boolean paramBoolean) {
    this.isActivate = paramBoolean;
  }
  
  public void setIndicatorArrowSize(int paramInt) {
    this.indicatorArrowSize = paramInt;
  }
  
  public void setIndicatorBackgroundColor(int paramInt) {
    this.indicatorBackgroundColor = paramInt;
  }
  
  public void setIndicatorDrawableId(int paramInt) {
    if (paramInt != 0) {
      this.indicatorDrawableId = paramInt;
      this.indicatorBitmap = BitmapFactory.decodeResource(getResources(), paramInt);
    } 
  }
  
  public void setIndicatorHeight(int paramInt) {
    this.indicatorHeight = paramInt;
  }
  
  public void setIndicatorMargin(int paramInt) {
    this.indicatorMargin = paramInt;
  }
  
  public void setIndicatorPaddingBottom(int paramInt) {
    this.indicatorPaddingBottom = paramInt;
  }
  
  public void setIndicatorPaddingLeft(int paramInt) {
    this.indicatorPaddingLeft = paramInt;
  }
  
  public void setIndicatorPaddingRight(int paramInt) {
    this.indicatorPaddingRight = paramInt;
  }
  
  public void setIndicatorPaddingTop(int paramInt) {
    this.indicatorPaddingTop = paramInt;
  }
  
  public void setIndicatorRadius(float paramFloat) {
    this.indicatorRadius = paramFloat;
  }
  
  public void setIndicatorShowMode(int paramInt) {
    this.indicatorShowMode = paramInt;
  }
  
  public void setIndicatorText(String paramString) {
    this.userText2Draw = paramString;
  }
  
  public void setIndicatorTextColor(int paramInt) {
    this.indicatorTextColor = paramInt;
  }
  
  public void setIndicatorTextDecimalFormat(String paramString) {
    this.indicatorTextDecimalFormat = new DecimalFormat(paramString);
  }
  
  public void setIndicatorTextSize(int paramInt) {
    this.indicatorTextSize = paramInt;
  }
  
  public void setIndicatorTextStringFormat(String paramString) {
    this.indicatorTextStringFormat = paramString;
  }
  
  public void setIndicatorWidth(int paramInt) {
    this.indicatorWidth = paramInt;
  }
  
  protected void setShowIndicatorEnable(boolean paramBoolean) {
    int i = this.indicatorShowMode;
    if (i != 0) {
      if (i != 1) {
        if (i == 2 || i == 3)
          this.isShowIndicator = true; 
      } else {
        this.isShowIndicator = false;
      } 
    } else {
      this.isShowIndicator = paramBoolean;
    } 
  }
  
  public void setThumbDrawableId(int paramInt) {
    if (this.thumbWidth > 0 && this.thumbHeight > 0) {
      if (paramInt != 0 && getResources() != null) {
        this.thumbDrawableId = paramInt;
        if (Build.VERSION.SDK_INT >= 21) {
          this.thumbBitmap = Utils.drawableToBitmap(this.thumbWidth, this.thumbHeight, getResources().getDrawable(paramInt, null));
        } else {
          this.thumbBitmap = Utils.drawableToBitmap(this.thumbWidth, this.thumbHeight, getResources().getDrawable(paramInt));
        } 
      } 
      return;
    } 
    throw new IllegalArgumentException("please set thumbWidth and thumbHeight first!");
  }
  
  public void setThumbDrawableId(int paramInt1, int paramInt2, int paramInt3) {
    if (paramInt1 != 0 && getResources() != null && paramInt2 > 0 && paramInt3 > 0) {
      this.thumbDrawableId = paramInt1;
      if (Build.VERSION.SDK_INT >= 21) {
        this.thumbBitmap = Utils.drawableToBitmap(paramInt2, paramInt3, getResources().getDrawable(paramInt1, null));
      } else {
        this.thumbBitmap = Utils.drawableToBitmap(paramInt2, paramInt3, getResources().getDrawable(paramInt1));
      } 
    } 
  }
  
  public void setThumbHeight(int paramInt) {
    this.thumbHeight = paramInt;
  }
  
  public void setThumbInactivatedDrawableId(int paramInt1, int paramInt2, int paramInt3) {
    if (paramInt1 != 0 && getResources() != null) {
      this.thumbInactivatedDrawableId = paramInt1;
      if (Build.VERSION.SDK_INT >= 21) {
        this.thumbInactivatedBitmap = Utils.drawableToBitmap(paramInt2, paramInt3, getResources().getDrawable(paramInt1, null));
      } else {
        this.thumbInactivatedBitmap = Utils.drawableToBitmap(paramInt2, paramInt3, getResources().getDrawable(paramInt1));
      } 
    } 
  }
  
  public void setThumbWidth(int paramInt) {
    this.thumbWidth = paramInt;
  }
  
  public void setTypeface(Typeface paramTypeface) {
    this.paint.setTypeface(paramTypeface);
  }
  
  public void setVisible(boolean paramBoolean) {
    this.isVisible = paramBoolean;
  }
  
  public void showIndicator(boolean paramBoolean) {
    this.isShowIndicator = paramBoolean;
  }
  
  protected void slide(float paramFloat) {
    float f;
    if (paramFloat < 0.0F) {
      f = 0.0F;
    } else {
      f = paramFloat;
      if (paramFloat > 1.0F)
        f = 1.0F; 
    } 
    this.currPercent = f;
  }
  
  public static @interface IndicatorModeDef {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/jaygoo/widget/SeekBar.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */