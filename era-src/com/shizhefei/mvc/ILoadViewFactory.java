package com.shizhefei.mvc;

import android.view.View;

public interface ILoadViewFactory {
  ILoadMoreView madeLoadMoreView();
  
  ILoadView madeLoadView();
  
  public static interface FootViewAdder {
    View addFootView(int param1Int);
    
    View addFootView(View param1View);
    
    View getContentView();
  }
  
  public static interface ILoadMoreView {
    void init(ILoadViewFactory.FootViewAdder param1FootViewAdder, View.OnClickListener param1OnClickListener);
    
    void showFail(Exception param1Exception);
    
    void showLoading();
    
    void showNomore();
    
    void showNormal();
  }
  
  public static interface ILoadView {
    void init(View param1View, View.OnClickListener param1OnClickListener);
    
    void restore();
    
    void showEmpty();
    
    void showFail(Exception param1Exception);
    
    void showLoading();
    
    void tipFail(Exception param1Exception);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/shizhefei/mvc/ILoadViewFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */