package com.shizhefei.mvc;

import android.view.View;

public class MVCNormalHelper<DATA> extends MVCHelper<DATA> {
  public MVCNormalHelper(View paramView) {
    super(new RefreshView(paramView));
  }
  
  public MVCNormalHelper(View paramView, ILoadViewFactory.ILoadView paramILoadView) {
    super(new RefreshView(paramView), paramILoadView);
  }
  
  public MVCNormalHelper(View paramView, ILoadViewFactory.ILoadView paramILoadView, ILoadViewFactory.ILoadMoreView paramILoadMoreView) {
    super(new RefreshView(paramView), paramILoadView, paramILoadMoreView);
  }
  
  private static class RefreshView implements IRefreshView {
    private View contentView;
    
    public RefreshView(View param1View) {
      this.contentView = param1View;
    }
    
    public View getContentView() {
      return this.contentView;
    }
    
    public View getSwitchView() {
      return this.contentView;
    }
    
    public void setOnRefreshListener(IRefreshView.OnRefreshListener param1OnRefreshListener) {}
    
    public void showRefreshComplete() {}
    
    public void showRefreshing() {}
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/shizhefei/mvc/MVCNormalHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */