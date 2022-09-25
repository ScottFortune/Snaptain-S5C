package com.shizhefei.mvc.viewhandler;

import android.view.View;
import com.shizhefei.mvc.ILoadViewFactory;
import com.shizhefei.mvc.MVCHelper;

public interface ViewHandler {
  boolean handleSetAdapter(View paramView, Object paramObject, ILoadViewFactory.ILoadMoreView paramILoadMoreView, View.OnClickListener paramOnClickListener);
  
  void setOnScrollBottomListener(View paramView, MVCHelper.OnScrollBottomListener paramOnScrollBottomListener);
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/shizhefei/mvc/viewhandler/ViewHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */