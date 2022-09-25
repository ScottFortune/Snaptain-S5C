package com.stx.xhb.xbanner;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import androidx.core.view.VelocityTrackerCompat;
import androidx.core.view.ViewCompat;
import androidx.viewpager.widget.ViewPager;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class XBannerViewPager extends ViewPager {
  private AutoPlayDelegate mAutoPlayDelegate;
  
  private boolean mIsAllowUserScroll = true;
  
  public XBannerViewPager(Context paramContext) {
    super(paramContext);
  }
  
  public XBannerViewPager(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
  }
  
  private float getXVelocity() {
    float f;
    try {
      Field field1 = ViewPager.class.getDeclaredField("mVelocityTracker");
      field1.setAccessible(true);
      VelocityTracker velocityTracker = (VelocityTracker)field1.get(this);
      field1 = ViewPager.class.getDeclaredField("mActivePointerId");
      field1.setAccessible(true);
      Field field2 = ViewPager.class.getDeclaredField("mMaximumVelocity");
      field2.setAccessible(true);
      velocityTracker.computeCurrentVelocity(1000, field2.getInt(this));
      f = VelocityTrackerCompat.getXVelocity(velocityTracker, field1.getInt(this));
    } catch (Exception exception) {
      f = 0.0F;
    } 
    return f;
  }
  
  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent) {
    boolean bool;
    if (this.mIsAllowUserScroll && super.onInterceptTouchEvent(paramMotionEvent)) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    if (this.mIsAllowUserScroll) {
      if (this.mAutoPlayDelegate != null && (paramMotionEvent.getAction() == 3 || paramMotionEvent.getAction() == 1)) {
        this.mAutoPlayDelegate.handleAutoPlayActionUpOrCancel(getXVelocity());
        return false;
      } 
      return super.onTouchEvent(paramMotionEvent);
    } 
    return false;
  }
  
  public void setAutoPlayDelegate(AutoPlayDelegate paramAutoPlayDelegate) {
    this.mAutoPlayDelegate = paramAutoPlayDelegate;
  }
  
  public void setBannerCurrentItemInternal(int paramInt, boolean paramBoolean) {
    try {
      Method method = ViewPager.class.getDeclaredMethod("setCurrentItemInternal", new Class[] { int.class, boolean.class, boolean.class });
      method.setAccessible(true);
      method.invoke(this, new Object[] { Integer.valueOf(paramInt), Boolean.valueOf(paramBoolean), Boolean.valueOf(true) });
      ViewCompat.postInvalidateOnAnimation((View)this);
    } catch (Exception exception) {}
  }
  
  public void setIsAllowUserScroll(boolean paramBoolean) {
    this.mIsAllowUserScroll = paramBoolean;
  }
  
  public void setPageTransformer(boolean paramBoolean, ViewPager.PageTransformer paramPageTransformer) {
    boolean bool;
    if (paramPageTransformer != null) {
      bool = true;
    } else {
      bool = false;
    } 
    try {
      boolean bool1;
      boolean bool2;
      Field field2 = ViewPager.class.getDeclaredField("mPageTransformer");
      field2.setAccessible(true);
      if ((ViewPager.PageTransformer)field2.get(this) != null) {
        bool1 = true;
      } else {
        bool1 = false;
      } 
      if (bool != bool1) {
        bool2 = true;
      } else {
        bool2 = false;
      } 
      field2.set(this, paramPageTransformer);
      Method method = ViewPager.class.getDeclaredMethod("setChildrenDrawingOrderEnabledCompat", new Class[] { boolean.class });
      method.setAccessible(true);
      method.invoke(this, new Object[] { Boolean.valueOf(bool) });
      Field field1 = ViewPager.class.getDeclaredField("mDrawingOrder");
      field1.setAccessible(true);
      if (bool) {
        boolean bool3;
        if (paramBoolean) {
          bool3 = true;
        } else {
          bool3 = true;
        } 
        field1.setInt(this, bool3);
      } else {
        field1.setInt(this, 0);
      } 
      if (bool2) {
        Method method1 = ViewPager.class.getDeclaredMethod("populate", new Class[0]);
        method1.setAccessible(true);
        method1.invoke(this, new Object[0]);
      } 
    } catch (Exception exception) {}
  }
  
  public void setScrollDuration(int paramInt) {
    try {
      Field field = ViewPager.class.getDeclaredField("mScroller");
      field.setAccessible(true);
      XBannerScroller xBannerScroller = new XBannerScroller();
      this(getContext(), paramInt);
      field.set(this, xBannerScroller);
    } catch (Exception exception) {
      exception.printStackTrace();
    } 
  }
  
  public static interface AutoPlayDelegate {
    void handleAutoPlayActionUpOrCancel(float param1Float);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/stx/xhb/xbanner/XBannerViewPager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */