package com.shizhefei.view.coolrefreshview.header;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import androidx.core.view.ViewCompat;
import com.shizhefei.view.coolrefreshview.CoolRefreshView;
import com.shizhefei.view.coolrefreshview.PullHeader;

public class JellyHeader extends ViewGroup implements PullHeader {
  private PullHeader.DefaultConfig config = new PullHeader.DefaultConfig() {
      public int headerViewLayoutOffset(CoolRefreshView param1CoolRefreshView, View param1View) {
        return param1View.getMeasuredHeight();
      }
      
      public int offsetToKeepHeaderWhileLoading(CoolRefreshView param1CoolRefreshView, View param1View) {
        return JellyHeader.this.offsetToKeepHeaderWhileLoading();
      }
      
      public int offsetToRefresh(CoolRefreshView param1CoolRefreshView, View param1View) {
        return JellyHeader.this.offsetToRefresh();
      }
      
      public int totalDistance(CoolRefreshView param1CoolRefreshView, View param1View) {
        return JellyHeader.this.totalDistance();
      }
    };
  
  private int currentDistance;
  
  private int defaultMinHeight;
  
  private Animation hideLoadingAnimation;
  
  private View loadingView;
  
  private int mColor = -7829368;
  
  private final Paint mPaint;
  
  private final Path mPath;
  
  private float mPointX;
  
  private ViewOutlineProvider mViewOutlineProvider;
  
  private Animation showLoadingAnimation;
  
  private int status;
  
  public JellyHeader(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public JellyHeader(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public JellyHeader(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    setWillNotDraw(false);
    this.defaultMinHeight = Utils.dipToPix(paramContext, 208.0F);
    this.mPaint = new Paint(1);
    this.mPaint.setStyle(Paint.Style.FILL);
    this.mPath = new Path();
    this.showLoadingAnimation = (Animation)new AlphaAnimation(0.0F, 1.0F);
    this.showLoadingAnimation.setDuration(300L);
    this.showLoadingAnimation.setInterpolator((Interpolator)new AccelerateInterpolator());
    this.hideLoadingAnimation = (Animation)new AlphaAnimation(1.0F, 0.0F);
    this.hideLoadingAnimation.setDuration(300L);
    this.hideLoadingAnimation.setInterpolator((Interpolator)new DecelerateInterpolator());
    if (Build.VERSION.SDK_INT >= 21) {
      this.mViewOutlineProvider = new ViewOutlineProvider() {
          public void getOutline(View param1View, Outline param1Outline) {
            if (JellyHeader.this.mPath.isConvex())
              param1Outline.setConvexPath(JellyHeader.this.mPath); 
            if (Build.VERSION.SDK_INT >= 22)
              param1Outline.offset(0, JellyHeader.this.totalDistance() - JellyHeader.this.currentDistance); 
          }
        };
      if (Build.VERSION.SDK_INT >= 21)
        setElevation(Utils.dipToPix(paramContext, 4.0F)); 
    } 
  }
  
  private int offsetToKeepHeaderWhileLoading() {
    return offsetToRefresh() / 2;
  }
  
  private int offsetToRefresh() {
    return getMeasuredHeight() / 2;
  }
  
  private int totalDistance() {
    return getMeasuredHeight();
  }
  
  public View createHeaderView(CoolRefreshView paramCoolRefreshView) {
    return (View)this;
  }
  
  public PullHeader.Config getConfig() {
    return (PullHeader.Config)this.config;
  }
  
  protected void onDraw(Canvas paramCanvas) {
    int k;
    super.onDraw(paramCanvas);
    int i = totalDistance();
    int j = offsetToKeepHeaderWhileLoading();
    if (this.status == 4) {
      k = this.currentDistance;
    } else {
      j = Math.min(this.currentDistance / 2, j);
      k = this.currentDistance;
    } 
    if (j == 0)
      return; 
    int m = paramCanvas.save();
    paramCanvas.translate(0.0F, (i - this.currentDistance));
    i = paramCanvas.getWidth();
    this.mPointX = (i / 2);
    float f1 = this.mPointX;
    float f2 = i;
    float f3 = f2 / 2.0F;
    this.mPaint.setColor(this.mColor);
    this.mPath.rewind();
    this.mPath.moveTo(0.0F, 0.0F);
    Path path = this.mPath;
    float f4 = j;
    path.lineTo(0.0F, f4);
    this.mPath.quadTo((f1 - f3) * 0.5F + f3, k, f2, f4);
    this.mPath.lineTo(f2, 0.0F);
    this.mPath.close();
    paramCanvas.drawPath(this.mPath, this.mPaint);
    paramCanvas.restoreToCount(m);
    if (Build.VERSION.SDK_INT >= 21)
      setOutlineProvider(this.mViewOutlineProvider); 
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    paramInt2 = getPaddingLeft();
    paramInt3 = getPaddingTop() + totalDistance() - offsetToKeepHeaderWhileLoading();
    paramInt1 = this.loadingView.getMeasuredHeight();
    paramInt4 = this.loadingView.getMeasuredWidth();
    this.loadingView.layout(paramInt2, paramInt3, paramInt4 + paramInt2, paramInt1 + paramInt3);
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    int i = getSuggestedMinimumHeight();
    paramInt2 = ViewCompat.resolveSizeAndState(Math.max(this.defaultMinHeight, i), paramInt2, 0);
    setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), paramInt1), paramInt2);
    if (this.loadingView != null) {
      paramInt2 = View.MeasureSpec.makeMeasureSpec(offsetToKeepHeaderWhileLoading(), 1073741824);
      paramInt1 = getChildMeasureSpec(paramInt1, getPaddingLeft() + getPaddingRight(), -1);
      this.loadingView.measure(paramInt1, paramInt2);
    } 
  }
  
  public void onPositionChange(CoolRefreshView paramCoolRefreshView, int paramInt1, int paramInt2, int paramInt3) {
    this.currentDistance = paramInt3;
    this.status = paramInt1;
    ViewCompat.postInvalidateOnAnimation((View)this);
  }
  
  public void onPullBegin(CoolRefreshView paramCoolRefreshView) {
    this.loadingView.setVisibility(8);
  }
  
  public void onPullRefreshComplete(CoolRefreshView paramCoolRefreshView) {
    this.loadingView.clearAnimation();
    this.loadingView.startAnimation(this.hideLoadingAnimation);
  }
  
  public void onRefreshing(CoolRefreshView paramCoolRefreshView) {
    this.loadingView.setVisibility(0);
    this.loadingView.clearAnimation();
    this.loadingView.startAnimation(this.showLoadingAnimation);
  }
  
  public void onReset(CoolRefreshView paramCoolRefreshView, boolean paramBoolean) {
    this.loadingView.setVisibility(8);
  }
  
  public void setDragLayoutColor(int paramInt) {
    this.mColor = paramInt;
    ViewCompat.postInvalidateOnAnimation((View)this);
  }
  
  public void setLoadingView(int paramInt) {
    this.loadingView = LayoutInflater.from(getContext()).inflate(paramInt, this, false);
    addView(this.loadingView);
  }
  
  public void setLoadingView(View paramView) {
    this.loadingView = paramView;
    ViewGroup.LayoutParams layoutParams2 = paramView.getLayoutParams();
    ViewGroup.LayoutParams layoutParams1 = layoutParams2;
    if (layoutParams2 == null)
      layoutParams1 = new ViewGroup.LayoutParams(-1, -2); 
    addView(this.loadingView, layoutParams1);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/shizhefei/view/coolrefreshview/header/JellyHeader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */