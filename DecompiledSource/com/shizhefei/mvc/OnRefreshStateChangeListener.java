package com.shizhefei.mvc;

public interface OnRefreshStateChangeListener<DATA> {
  void onEndRefresh(IDataAdapter<DATA> paramIDataAdapter, DATA paramDATA);
  
  void onStartRefresh(IDataAdapter<DATA> paramIDataAdapter);
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/shizhefei/mvc/OnRefreshStateChangeListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */