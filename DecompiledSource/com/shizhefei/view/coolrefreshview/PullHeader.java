package com.shizhefei.view.coolrefreshview;

import android.os.Build;
import android.view.View;
import android.widget.AbsListView;
import androidx.core.view.ViewCompat;

public interface PullHeader extends OnPullListener {
  View createHeaderView(CoolRefreshView paramCoolRefreshView);
  
  Config getConfig();
  
  public static abstract class Config {
    public abstract boolean contentCanScrollUp(CoolRefreshView param1CoolRefreshView, View param1View);
    
    public abstract int dispatchTouchMove(CoolRefreshView param1CoolRefreshView, int param1Int1, int param1Int2, int param1Int3);
    
    public abstract int headerViewLayoutOffset(CoolRefreshView param1CoolRefreshView, View param1View);
    
    public abstract int offsetToKeepHeaderWhileLoading(CoolRefreshView param1CoolRefreshView, View param1View);
    
    public abstract int offsetToRefresh(CoolRefreshView param1CoolRefreshView, View param1View);
    
    public abstract int totalDistance(CoolRefreshView param1CoolRefreshView, View param1View);
  }
  
  public static class DefaultConfig extends Config {
    public boolean contentCanScrollUp(CoolRefreshView param1CoolRefreshView, View param1View) {
      if (Build.VERSION.SDK_INT < 14) {
        boolean bool = param1View instanceof AbsListView;
        boolean bool1 = true;
        boolean bool2 = true;
        if (bool) {
          AbsListView absListView = (AbsListView)param1View;
          if (absListView.getChildCount() > 0) {
            bool = bool2;
            if (absListView.getFirstVisiblePosition() <= 0) {
              if (absListView.getChildAt(0).getTop() < absListView.getPaddingTop())
                return bool2; 
            } else {
              return bool;
            } 
          } 
          return false;
        } 
        bool = bool1;
        if (!ViewCompat.canScrollVertically(param1View, -1))
          if (param1View.getScrollY() > 0) {
            bool = bool1;
          } else {
            bool = false;
          }  
        return bool;
      } 
      return ViewCompat.canScrollVertically(param1View, -1);
    }
    
    public int dispatchTouchMove(CoolRefreshView param1CoolRefreshView, int param1Int1, int param1Int2, int param1Int3) {
      float f1 = (param1Int1 / 2);
      float f2 = (int)(-f1 * Math.abs(param1Int2) / param1Int3);
      return (int)(param1Int1 + (f2 - f1) * 0.9F);
    }
    
    public int headerViewLayoutOffset(CoolRefreshView param1CoolRefreshView, View param1View) {
      return param1View.getMeasuredHeight();
    }
    
    public int offsetToKeepHeaderWhileLoading(CoolRefreshView param1CoolRefreshView, View param1View) {
      return param1View.getMeasuredHeight();
    }
    
    public int offsetToRefresh(CoolRefreshView param1CoolRefreshView, View param1View) {
      return (int)(param1View.getMeasuredHeight() * 1.2F);
    }
    
    public int totalDistance(CoolRefreshView param1CoolRefreshView, View param1View) {
      return param1View.getMeasuredHeight() * 3;
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/shizhefei/view/coolrefreshview/PullHeader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */