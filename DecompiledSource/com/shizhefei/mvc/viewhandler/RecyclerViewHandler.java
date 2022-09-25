package com.shizhefei.mvc.viewhandler;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.shizhefei.mvc.ILoadViewFactory;
import com.shizhefei.mvc.MVCHelper;
import com.shizhefei.recyclerview.HFAdapter;
import com.shizhefei.recyclerview.HFRecyclerAdapter;

public class RecyclerViewHandler implements ViewHandler {
  public boolean handleSetAdapter(View paramView, Object paramObject, ILoadViewFactory.ILoadMoreView paramILoadMoreView, View.OnClickListener paramOnClickListener) {
    HFRecyclerAdapter hFRecyclerAdapter;
    RecyclerView recyclerView = (RecyclerView)paramView;
    RecyclerView.Adapter adapter2 = (RecyclerView.Adapter)paramObject;
    boolean bool = false;
    RecyclerView.Adapter adapter1 = adapter2;
    if (paramILoadMoreView != null) {
      if (paramObject instanceof HFAdapter) {
        HFAdapter hFAdapter = (HFAdapter)paramObject;
      } else {
        hFRecyclerAdapter = new HFRecyclerAdapter(adapter2, false);
      } 
      paramILoadMoreView.init(new RecyclerViewFootViewAdder(recyclerView, (HFAdapter)hFRecyclerAdapter), paramOnClickListener);
      bool = true;
    } 
    recyclerView.setAdapter((RecyclerView.Adapter)hFRecyclerAdapter);
    return bool;
  }
  
  public void setOnScrollBottomListener(View paramView, MVCHelper.OnScrollBottomListener paramOnScrollBottomListener) {
    RecyclerView recyclerView = (RecyclerView)paramView;
    RecyclerViewOnScrollListener recyclerViewOnScrollListener = new RecyclerViewOnScrollListener(paramOnScrollBottomListener);
    recyclerView.addOnScrollListener(recyclerViewOnScrollListener);
    recyclerView.addOnItemTouchListener(recyclerViewOnScrollListener);
  }
  
  private static class RecyclerViewFootViewAdder implements ILoadViewFactory.FootViewAdder {
    private HFAdapter hfAdapter;
    
    private RecyclerView recyclerView;
    
    public RecyclerViewFootViewAdder(RecyclerView param1RecyclerView, HFAdapter param1HFAdapter) {
      this.recyclerView = param1RecyclerView;
      this.hfAdapter = param1HFAdapter;
    }
    
    public View addFootView(int param1Int) {
      return addFootView(LayoutInflater.from(this.recyclerView.getContext()).inflate(param1Int, (ViewGroup)this.recyclerView, false));
    }
    
    public View addFootView(View param1View) {
      this.hfAdapter.addFooter(param1View);
      return param1View;
    }
    
    public View getContentView() {
      return (View)this.recyclerView;
    }
  }
  
  private static class RecyclerViewOnScrollListener extends RecyclerView.OnScrollListener implements RecyclerView.OnItemTouchListener {
    private float endY = -1.0F;
    
    private MVCHelper.OnScrollBottomListener onScrollBottomListener;
    
    private float startY = -1.0F;
    
    public RecyclerViewOnScrollListener(MVCHelper.OnScrollBottomListener param1OnScrollBottomListener) {
      this.onScrollBottomListener = param1OnScrollBottomListener;
    }
    
    private boolean isCanScollVertically(RecyclerView param1RecyclerView) {
      int i = Build.VERSION.SDK_INT;
      boolean bool = true;
      if (i < 14) {
        boolean bool1 = bool;
        if (!ViewCompat.canScrollVertically((View)param1RecyclerView, 1))
          if (param1RecyclerView.getScrollY() < param1RecyclerView.getHeight()) {
            bool1 = bool;
          } else {
            bool1 = false;
          }  
        return bool1;
      } 
      return ViewCompat.canScrollVertically((View)param1RecyclerView, 1);
    }
    
    private boolean isScollBottom(RecyclerView param1RecyclerView) {
      return isCanScollVertically(param1RecyclerView) ^ true;
    }
    
    public boolean onInterceptTouchEvent(RecyclerView param1RecyclerView, MotionEvent param1MotionEvent) {
      int i = param1MotionEvent.getAction();
      if (i != 0) {
        if (i == 1)
          this.endY = param1MotionEvent.getY(); 
      } else {
        this.endY = -1.0F;
        this.startY = param1MotionEvent.getY();
      } 
      return false;
    }
    
    public void onRequestDisallowInterceptTouchEvent(boolean param1Boolean) {}
    
    public void onScrollStateChanged(RecyclerView param1RecyclerView, int param1Int) {
      if (param1Int == 0 && this.onScrollBottomListener != null) {
        float f = this.endY;
        if (f >= 0.0F && f < this.startY && isScollBottom(param1RecyclerView))
          this.onScrollBottomListener.onScorllBootom(); 
      } 
    }
    
    public void onScrolled(RecyclerView param1RecyclerView, int param1Int1, int param1Int2) {}
    
    public void onTouchEvent(RecyclerView param1RecyclerView, MotionEvent param1MotionEvent) {}
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/shizhefei/mvc/viewhandler/RecyclerViewHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */