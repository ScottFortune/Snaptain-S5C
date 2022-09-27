package com.shizhefei.mvc;

public interface OnLoadMoreStateChangeListener<DATA> {
  void onEndLoadMore(IDataAdapter<DATA> paramIDataAdapter, DATA paramDATA);
  
  void onStartLoadMore(IDataAdapter<DATA> paramIDataAdapter);
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/shizhefei/mvc/OnLoadMoreStateChangeListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */