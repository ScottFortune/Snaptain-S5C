package com.shizhefei.view.coolrefreshview;

public interface OnPullListener {
  void onPositionChange(CoolRefreshView paramCoolRefreshView, int paramInt1, int paramInt2, int paramInt3);
  
  void onPullBegin(CoolRefreshView paramCoolRefreshView);
  
  void onPullRefreshComplete(CoolRefreshView paramCoolRefreshView);
  
  void onRefreshing(CoolRefreshView paramCoolRefreshView);
  
  void onReset(CoolRefreshView paramCoolRefreshView, boolean paramBoolean);
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/shizhefei/view/coolrefreshview/OnPullListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */