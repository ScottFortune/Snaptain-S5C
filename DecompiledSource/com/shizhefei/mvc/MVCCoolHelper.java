package com.shizhefei.mvc;

import android.view.View;
import com.shizhefei.view.coolrefreshview.CoolRefreshView;
import com.shizhefei.view.coolrefreshview.OnPullListener;
import com.shizhefei.view.coolrefreshview.SimpleOnPullListener;

public class MVCCoolHelper<DATA> extends MVCHelper<DATA> {
  public MVCCoolHelper(CoolRefreshView paramCoolRefreshView) {
    super(new RefreshView(paramCoolRefreshView));
  }
  
  public MVCCoolHelper(CoolRefreshView paramCoolRefreshView, ILoadViewFactory.ILoadView paramILoadView, ILoadViewFactory.ILoadMoreView paramILoadMoreView) {
    super(new RefreshView(paramCoolRefreshView), paramILoadView, paramILoadMoreView);
  }
  
  private static class RefreshView implements IRefreshView {
    private CoolRefreshView coolRefreshView;
    
    private OnPullListener onPullListener = (OnPullListener)new SimpleOnPullListener() {
        public void onRefreshing(CoolRefreshView param2CoolRefreshView) {
          if (MVCCoolHelper.RefreshView.this.onRefreshListener != null)
            MVCCoolHelper.RefreshView.this.onRefreshListener.onRefresh(); 
        }
      };
    
    private IRefreshView.OnRefreshListener onRefreshListener;
    
    public RefreshView(CoolRefreshView param1CoolRefreshView) {
      this.coolRefreshView = param1CoolRefreshView;
      param1CoolRefreshView.addOnPullListener(this.onPullListener);
    }
    
    public View getContentView() {
      return this.coolRefreshView.getContentView();
    }
    
    public View getSwitchView() {
      return (View)this.coolRefreshView;
    }
    
    public void setOnRefreshListener(IRefreshView.OnRefreshListener param1OnRefreshListener) {
      this.onRefreshListener = param1OnRefreshListener;
    }
    
    public void showRefreshComplete() {
      this.coolRefreshView.removeOnPullListener(this.onPullListener);
      this.coolRefreshView.setRefreshing(false);
      this.coolRefreshView.addOnPullListener(this.onPullListener);
    }
    
    public void showRefreshing() {
      this.coolRefreshView.removeOnPullListener(this.onPullListener);
      this.coolRefreshView.setRefreshing(true);
      this.coolRefreshView.addOnPullListener(this.onPullListener);
    }
  }
  
  class null extends SimpleOnPullListener {
    public void onRefreshing(CoolRefreshView param1CoolRefreshView) {
      if (this.this$0.onRefreshListener != null)
        this.this$0.onRefreshListener.onRefresh(); 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/shizhefei/mvc/MVCCoolHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */