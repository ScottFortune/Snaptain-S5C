package com.shizhefei.mvc;

import android.view.View;

public interface IRefreshView {
  View getContentView();
  
  View getSwitchView();
  
  void setOnRefreshListener(OnRefreshListener paramOnRefreshListener);
  
  void showRefreshComplete();
  
  void showRefreshing();
  
  public static interface OnRefreshListener {
    void onRefresh();
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/shizhefei/mvc/IRefreshView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */