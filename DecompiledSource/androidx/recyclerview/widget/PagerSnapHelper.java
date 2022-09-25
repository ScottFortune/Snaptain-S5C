package androidx.recyclerview.widget;

import android.content.Context;
import android.graphics.PointF;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Interpolator;

public class PagerSnapHelper extends SnapHelper {
  private static final int MAX_SCROLL_ON_FLING_DURATION = 100;
  
  private OrientationHelper mHorizontalHelper;
  
  private OrientationHelper mVerticalHelper;
  
  private int distanceToCenter(RecyclerView.LayoutManager paramLayoutManager, View paramView, OrientationHelper paramOrientationHelper) {
    return paramOrientationHelper.getDecoratedStart(paramView) + paramOrientationHelper.getDecoratedMeasurement(paramView) / 2 - paramOrientationHelper.getStartAfterPadding() + paramOrientationHelper.getTotalSpace() / 2;
  }
  
  private View findCenterView(RecyclerView.LayoutManager paramLayoutManager, OrientationHelper paramOrientationHelper) {
    int i = paramLayoutManager.getChildCount();
    View view = null;
    if (i == 0)
      return null; 
    int j = paramOrientationHelper.getStartAfterPadding();
    int k = paramOrientationHelper.getTotalSpace() / 2;
    int m = Integer.MAX_VALUE;
    byte b = 0;
    while (b < i) {
      View view1 = paramLayoutManager.getChildAt(b);
      int n = Math.abs(paramOrientationHelper.getDecoratedStart(view1) + paramOrientationHelper.getDecoratedMeasurement(view1) / 2 - j + k);
      int i1 = m;
      if (n < m) {
        view = view1;
        i1 = n;
      } 
      b++;
      m = i1;
    } 
    return view;
  }
  
  private OrientationHelper getHorizontalHelper(RecyclerView.LayoutManager paramLayoutManager) {
    OrientationHelper orientationHelper = this.mHorizontalHelper;
    if (orientationHelper == null || orientationHelper.mLayoutManager != paramLayoutManager)
      this.mHorizontalHelper = OrientationHelper.createHorizontalHelper(paramLayoutManager); 
    return this.mHorizontalHelper;
  }
  
  private OrientationHelper getOrientationHelper(RecyclerView.LayoutManager paramLayoutManager) {
    return paramLayoutManager.canScrollVertically() ? getVerticalHelper(paramLayoutManager) : (paramLayoutManager.canScrollHorizontally() ? getHorizontalHelper(paramLayoutManager) : null);
  }
  
  private OrientationHelper getVerticalHelper(RecyclerView.LayoutManager paramLayoutManager) {
    OrientationHelper orientationHelper = this.mVerticalHelper;
    if (orientationHelper == null || orientationHelper.mLayoutManager != paramLayoutManager)
      this.mVerticalHelper = OrientationHelper.createVerticalHelper(paramLayoutManager); 
    return this.mVerticalHelper;
  }
  
  private boolean isForwardFling(RecyclerView.LayoutManager paramLayoutManager, int paramInt1, int paramInt2) {
    boolean bool = paramLayoutManager.canScrollHorizontally();
    boolean bool1 = true;
    boolean bool2 = true;
    if (bool) {
      if (paramInt1 <= 0)
        bool2 = false; 
      return bool2;
    } 
    if (paramInt2 > 0) {
      bool2 = bool1;
    } else {
      bool2 = false;
    } 
    return bool2;
  }
  
  private boolean isReverseLayout(RecyclerView.LayoutManager paramLayoutManager) {
    int i = paramLayoutManager.getItemCount();
    if (paramLayoutManager instanceof RecyclerView.SmoothScroller.ScrollVectorProvider) {
      RecyclerView.SmoothScroller.ScrollVectorProvider scrollVectorProvider = (RecyclerView.SmoothScroller.ScrollVectorProvider)paramLayoutManager;
      boolean bool = true;
      PointF pointF = scrollVectorProvider.computeScrollVectorForPosition(i - 1);
      if (pointF != null) {
        boolean bool1 = bool;
        if (pointF.x >= 0.0F)
          if (pointF.y < 0.0F) {
            bool1 = bool;
          } else {
            bool1 = false;
          }  
        return bool1;
      } 
    } 
    return false;
  }
  
  public int[] calculateDistanceToFinalSnap(RecyclerView.LayoutManager paramLayoutManager, View paramView) {
    int[] arrayOfInt = new int[2];
    if (paramLayoutManager.canScrollHorizontally()) {
      arrayOfInt[0] = distanceToCenter(paramLayoutManager, paramView, getHorizontalHelper(paramLayoutManager));
    } else {
      arrayOfInt[0] = 0;
    } 
    if (paramLayoutManager.canScrollVertically()) {
      arrayOfInt[1] = distanceToCenter(paramLayoutManager, paramView, getVerticalHelper(paramLayoutManager));
    } else {
      arrayOfInt[1] = 0;
    } 
    return arrayOfInt;
  }
  
  protected LinearSmoothScroller createSnapScroller(RecyclerView.LayoutManager paramLayoutManager) {
    return !(paramLayoutManager instanceof RecyclerView.SmoothScroller.ScrollVectorProvider) ? null : new LinearSmoothScroller(this.mRecyclerView.getContext()) {
        protected float calculateSpeedPerPixel(DisplayMetrics param1DisplayMetrics) {
          return 100.0F / param1DisplayMetrics.densityDpi;
        }
        
        protected int calculateTimeForScrolling(int param1Int) {
          return Math.min(100, super.calculateTimeForScrolling(param1Int));
        }
        
        protected void onTargetFound(View param1View, RecyclerView.State param1State, RecyclerView.SmoothScroller.Action param1Action) {
          PagerSnapHelper pagerSnapHelper = PagerSnapHelper.this;
          int[] arrayOfInt = pagerSnapHelper.calculateDistanceToFinalSnap(pagerSnapHelper.mRecyclerView.getLayoutManager(), param1View);
          int i = arrayOfInt[0];
          int j = arrayOfInt[1];
          int k = calculateTimeForDeceleration(Math.max(Math.abs(i), Math.abs(j)));
          if (k > 0)
            param1Action.update(i, j, k, (Interpolator)this.mDecelerateInterpolator); 
        }
      };
  }
  
  public View findSnapView(RecyclerView.LayoutManager paramLayoutManager) {
    return paramLayoutManager.canScrollVertically() ? findCenterView(paramLayoutManager, getVerticalHelper(paramLayoutManager)) : (paramLayoutManager.canScrollHorizontally() ? findCenterView(paramLayoutManager, getHorizontalHelper(paramLayoutManager)) : null);
  }
  
  public int findTargetSnapPosition(RecyclerView.LayoutManager paramLayoutManager, int paramInt1, int paramInt2) {
    int i = paramLayoutManager.getItemCount();
    if (i == 0)
      return -1; 
    OrientationHelper orientationHelper = getOrientationHelper(paramLayoutManager);
    if (orientationHelper == null)
      return -1; 
    int j = paramLayoutManager.getChildCount();
    byte b = 0;
    View view1 = null;
    View view2 = null;
    int k = Integer.MIN_VALUE;
    int m;
    for (m = Integer.MAX_VALUE; b < j; m = n) {
      View view4;
      int n;
      View view3 = paramLayoutManager.getChildAt(b);
      if (view3 == null) {
        view4 = view1;
        n = m;
      } else {
        int i1 = distanceToCenter(paramLayoutManager, view3, orientationHelper);
        View view = view2;
        int i2 = k;
        if (i1 <= 0) {
          view = view2;
          i2 = k;
          if (i1 > k) {
            view = view3;
            i2 = i1;
          } 
        } 
        view2 = view;
        k = i2;
        view4 = view1;
        n = m;
        if (i1 >= 0) {
          view2 = view;
          k = i2;
          view4 = view1;
          n = m;
          if (i1 < m) {
            n = i1;
            view4 = view3;
            k = i2;
            view2 = view;
          } 
        } 
      } 
      b++;
      view1 = view4;
    } 
    boolean bool = isForwardFling(paramLayoutManager, paramInt1, paramInt2);
    if (bool && view1 != null)
      return paramLayoutManager.getPosition(view1); 
    if (!bool && view2 != null)
      return paramLayoutManager.getPosition(view2); 
    if (!bool)
      view2 = view1; 
    if (view2 == null)
      return -1; 
    paramInt2 = paramLayoutManager.getPosition(view2);
    if (isReverseLayout(paramLayoutManager) == bool) {
      paramInt1 = -1;
    } else {
      paramInt1 = 1;
    } 
    paramInt1 = paramInt2 + paramInt1;
    return (paramInt1 < 0 || paramInt1 >= i) ? -1 : paramInt1;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/androidx/recyclerview/widget/PagerSnapHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */