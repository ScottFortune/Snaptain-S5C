package com.jaygoo.widget;

import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;

public class VerticalSeekBar extends SeekBar {
  private int indicatorTextOrientation;
  
  VerticalRangeSeekBar verticalSeekBar;
  
  public VerticalSeekBar(RangeSeekBar paramRangeSeekBar, AttributeSet paramAttributeSet, boolean paramBoolean) {
    super(paramRangeSeekBar, paramAttributeSet, paramBoolean);
    initAttrs(paramAttributeSet);
    this.verticalSeekBar = (VerticalRangeSeekBar)paramRangeSeekBar;
  }
  
  private void initAttrs(AttributeSet paramAttributeSet) {
    try {
      TypedArray typedArray = getContext().obtainStyledAttributes(paramAttributeSet, R.styleable.VerticalRangeSeekBar);
      this.indicatorTextOrientation = typedArray.getInt(R.styleable.VerticalRangeSeekBar_rsb_indicator_text_orientation, 1);
      typedArray.recycle();
    } catch (Exception exception) {
      exception.printStackTrace();
    } 
  }
  
  protected void drawVerticalIndicator(Canvas paramCanvas, Paint paramPaint, String paramString) {
    paramPaint.setTextSize(getIndicatorTextSize());
    paramPaint.setStyle(Paint.Style.FILL);
    paramPaint.setColor(getIndicatorBackgroundColor());
    int i = paramString.length();
    Rect rect = this.indicatorTextRect;
    boolean bool = false;
    paramPaint.getTextBounds(paramString, 0, i, rect);
    int j = this.indicatorTextRect.height() + getIndicatorPaddingLeft() + getIndicatorPaddingRight();
    i = j;
    if (getIndicatorWidth() > j)
      i = getIndicatorWidth(); 
    int k = this.indicatorTextRect.width() + getIndicatorPaddingTop() + getIndicatorPaddingBottom();
    j = k;
    if (getIndicatorHeight() > k)
      j = getIndicatorHeight(); 
    this.indicatorRect.left = this.scaleThumbWidth / 2 - i / 2;
    this.indicatorRect.top = this.bottom - j - this.scaleThumbHeight - getIndicatorMargin();
    this.indicatorRect.right = this.indicatorRect.left + i;
    this.indicatorRect.bottom = this.indicatorRect.top + j;
    if (this.indicatorBitmap == null) {
      int i4 = this.scaleThumbWidth / 2;
      int i5 = this.indicatorRect.bottom;
      k = getIndicatorArrowSize();
      i = getIndicatorArrowSize();
      j = getIndicatorArrowSize();
      this.indicatorArrowPath.reset();
      this.indicatorArrowPath.moveTo(i4, i5);
      Path path = this.indicatorArrowPath;
      float f5 = (i4 - k);
      float f6 = (i5 - i);
      path.lineTo(f5, f6);
      this.indicatorArrowPath.lineTo((j + i4), f6);
      this.indicatorArrowPath.close();
      paramCanvas.drawPath(this.indicatorArrowPath, paramPaint);
      Rect rect1 = this.indicatorRect;
      rect1.bottom -= getIndicatorArrowSize();
      rect1 = this.indicatorRect;
      rect1.top -= getIndicatorArrowSize();
    } 
    j = Utils.dp2px(getContext(), 1.0F);
    i = this.indicatorRect.width() / 2 - (int)(this.rangeSeekBar.getProgressWidth() * this.currPercent) - this.rangeSeekBar.getProgressLeft() + j;
    j = this.indicatorRect.width() / 2 - (int)(this.rangeSeekBar.getProgressWidth() * (1.0F - this.currPercent)) - this.rangeSeekBar.getProgressPaddingRight() + j;
    if (i > 0) {
      rect = this.indicatorRect;
      rect.left += i;
      rect = this.indicatorRect;
      rect.right += i;
    } else if (j > 0) {
      rect = this.indicatorRect;
      rect.left -= j;
      rect = this.indicatorRect;
      rect.right -= j;
    } 
    if (this.indicatorBitmap != null) {
      Utils.drawBitmap(paramCanvas, paramPaint, this.indicatorBitmap, this.indicatorRect);
    } else if (getIndicatorRadius() > 0.0F) {
      paramCanvas.drawRoundRect(new RectF(this.indicatorRect), getIndicatorRadius(), getIndicatorRadius(), paramPaint);
    } else {
      paramCanvas.drawRect(this.indicatorRect, paramPaint);
    } 
    i = this.indicatorRect.left;
    int i1 = (this.indicatorRect.width() - this.indicatorTextRect.width()) / 2;
    j = getIndicatorPaddingLeft();
    k = getIndicatorPaddingRight();
    int n = this.indicatorRect.bottom;
    int i2 = (this.indicatorRect.height() - this.indicatorTextRect.height()) / 2;
    int m = getIndicatorPaddingTop();
    int i3 = getIndicatorPaddingBottom();
    paramPaint.setColor(getIndicatorTextColor());
    float f3 = (i + i1 + j - k);
    float f1 = this.indicatorTextRect.width() / 2.0F + f3;
    float f2 = (n - i2 + m - i3);
    float f4 = f2 - this.indicatorTextRect.height() / 2.0F;
    i = bool;
    if (this.indicatorTextOrientation == 1)
      if (this.verticalSeekBar.getOrientation() == 1) {
        i = 90;
      } else {
        i = bool;
        if (this.verticalSeekBar.getOrientation() == 2)
          i = -90; 
      }  
    if (i != 0)
      paramCanvas.rotate(i, f1, f4); 
    paramCanvas.drawText(paramString, f3, f2, paramPaint);
    if (i != 0)
      paramCanvas.rotate(-i, f1, f4); 
  }
  
  public int getIndicatorTextOrientation() {
    return this.indicatorTextOrientation;
  }
  
  protected void onDrawIndicator(Canvas paramCanvas, Paint paramPaint, String paramString) {
    if (paramString == null)
      return; 
    if (this.indicatorTextOrientation == 1) {
      drawVerticalIndicator(paramCanvas, paramPaint, paramString);
    } else {
      super.onDrawIndicator(paramCanvas, paramPaint, paramString);
    } 
  }
  
  public void setIndicatorTextOrientation(int paramInt) {
    this.indicatorTextOrientation = paramInt;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/jaygoo/widget/VerticalSeekBar.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */