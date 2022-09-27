package com.shizhefei.view.coolrefreshview.header;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import com.shizhefei.view.coolrefreshview.CoolRefreshView;
import com.shizhefei.view.coolrefreshview.PullHeader;

public class MaterialHeader extends View implements PullHeader {
  private PullHeader.DefaultConfig config = new PullHeader.DefaultConfig();
  
  private final MaterialProgressDrawable mDrawable = new MaterialProgressDrawable(getContext(), this);
  
  private float mScale = 1.0F;
  
  public MaterialHeader(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public MaterialHeader(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public MaterialHeader(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    this.mDrawable.setBackgroundColor(-1);
    this.mDrawable.setCallback((Drawable.Callback)this);
    this.mDrawable.showArrow(true);
    this.mDrawable.setAlpha(255);
  }
  
  public View createHeaderView(CoolRefreshView paramCoolRefreshView) {
    return this;
  }
  
  public PullHeader.Config getConfig() {
    return (PullHeader.Config)this.config;
  }
  
  public void invalidateDrawable(Drawable paramDrawable) {
    if (paramDrawable == this.mDrawable) {
      invalidate();
    } else {
      super.invalidateDrawable(paramDrawable);
    } 
  }
  
  protected void onDraw(Canvas paramCanvas) {
    int i = paramCanvas.save();
    Rect rect = this.mDrawable.getBounds();
    int j = getPaddingLeft();
    int k = (getMeasuredWidth() - this.mDrawable.getIntrinsicWidth()) / 2;
    int m = getMeasuredHeight() / 2;
    int n = this.mDrawable.getIntrinsicHeight() / 2;
    paramCanvas.translate((j + k), (getPaddingTop() + m - n));
    float f = this.mScale;
    paramCanvas.scale(f, f, rect.exactCenterX(), rect.exactCenterY());
    this.mDrawable.draw(paramCanvas);
    paramCanvas.restoreToCount(i);
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    paramInt1 = this.mDrawable.getIntrinsicHeight();
    this.mDrawable.setBounds(0, 0, paramInt1, paramInt1);
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    int i = getPaddingTop() + getPaddingBottom();
    paramInt2 = i;
    if (i == 0)
      paramInt2 = Utils.dipToPix(getContext(), 16.0F); 
    super.onMeasure(paramInt1, View.MeasureSpec.makeMeasureSpec(this.mDrawable.getIntrinsicHeight() + paramInt2, 1073741824));
  }
  
  public void onPositionChange(CoolRefreshView paramCoolRefreshView, int paramInt1, int paramInt2, int paramInt3) {
    paramInt2 = getConfig().totalDistance(paramCoolRefreshView, this);
    float f = Math.min(1.0F, paramInt3 * 1.0F / paramInt2);
    if (paramInt1 == 2) {
      this.mDrawable.showArrow(true);
      this.mDrawable.setStartEndTrim(0.0F, Math.min(0.8F, f * 0.8F));
      this.mDrawable.setArrowScale(Math.min(1.0F, f));
      this.mDrawable.setProgressRotation((0.4F * f - 0.25F + f * 2.0F) * 0.5F);
      invalidate();
    } 
  }
  
  public void onPullBegin(CoolRefreshView paramCoolRefreshView) {
    this.mDrawable.setAlpha(255);
  }
  
  public void onPullRefreshComplete(CoolRefreshView paramCoolRefreshView) {
    this.mDrawable.stop();
  }
  
  public void onRefreshing(CoolRefreshView paramCoolRefreshView) {
    this.mDrawable.start();
  }
  
  public void onReset(CoolRefreshView paramCoolRefreshView, boolean paramBoolean) {
    this.mScale = 1.0F;
    this.mDrawable.stop();
  }
  
  public void setColorSchemeColors(int[] paramArrayOfint) {
    this.mDrawable.setColorSchemeColors(paramArrayOfint);
    invalidate();
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/shizhefei/view/coolrefreshview/header/MaterialHeader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */