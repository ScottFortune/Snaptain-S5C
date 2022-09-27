package com.shizhefei.mvc.viewhandler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.shizhefei.mvc.ILoadViewFactory;
import com.shizhefei.mvc.MVCHelper;

public class ListViewHandler implements ViewHandler {
  public boolean handleSetAdapter(View paramView, Object paramObject, ILoadViewFactory.ILoadMoreView paramILoadMoreView, View.OnClickListener paramOnClickListener) {
    boolean bool;
    ListView listView = (ListView)paramView;
    if (paramILoadMoreView != null) {
      paramILoadMoreView.init(new ListViewFootViewAdder(listView), paramOnClickListener);
      bool = true;
    } else {
      bool = false;
    } 
    if (listView instanceof ExpandableListView) {
      ((ExpandableListView)listView).setAdapter((ExpandableListAdapter)paramObject);
    } else {
      listView.setAdapter((ListAdapter)paramObject);
    } 
    return bool;
  }
  
  public void setOnScrollBottomListener(View paramView, MVCHelper.OnScrollBottomListener paramOnScrollBottomListener) {
    ListView listView = (ListView)paramView;
    listView.setOnScrollListener(new ListViewOnScrollListener(paramOnScrollBottomListener));
    listView.setOnItemSelectedListener(new ListViewOnItemSelectedListener(paramOnScrollBottomListener));
  }
  
  private static class ListViewFootViewAdder implements ILoadViewFactory.FootViewAdder {
    private ListView listView;
    
    public ListViewFootViewAdder(ListView param1ListView) {
      this.listView = param1ListView;
    }
    
    public View addFootView(int param1Int) {
      return addFootView(LayoutInflater.from(this.listView.getContext()).inflate(param1Int, (ViewGroup)this.listView, false));
    }
    
    public View addFootView(View param1View) {
      this.listView.addFooterView(param1View);
      return param1View;
    }
    
    public View getContentView() {
      return (View)this.listView;
    }
  }
  
  private static class ListViewOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
    private MVCHelper.OnScrollBottomListener onScrollBottomListener;
    
    public ListViewOnItemSelectedListener(MVCHelper.OnScrollBottomListener param1OnScrollBottomListener) {
      this.onScrollBottomListener = param1OnScrollBottomListener;
    }
    
    public void onItemSelected(AdapterView<?> param1AdapterView, View param1View, int param1Int, long param1Long) {
      if (param1AdapterView.getLastVisiblePosition() + 1 == param1AdapterView.getCount()) {
        MVCHelper.OnScrollBottomListener onScrollBottomListener = this.onScrollBottomListener;
        if (onScrollBottomListener != null)
          onScrollBottomListener.onScorllBootom(); 
      } 
    }
    
    public void onNothingSelected(AdapterView<?> param1AdapterView) {}
  }
  
  private static class ListViewOnScrollListener implements AbsListView.OnScrollListener {
    private MVCHelper.OnScrollBottomListener onScrollBottomListener;
    
    public ListViewOnScrollListener(MVCHelper.OnScrollBottomListener param1OnScrollBottomListener) {
      this.onScrollBottomListener = param1OnScrollBottomListener;
    }
    
    public void onScroll(AbsListView param1AbsListView, int param1Int1, int param1Int2, int param1Int3) {}
    
    public void onScrollStateChanged(AbsListView param1AbsListView, int param1Int) {
      if (param1Int == 0 && param1AbsListView.getLastVisiblePosition() + 1 == param1AbsListView.getCount()) {
        MVCHelper.OnScrollBottomListener onScrollBottomListener = this.onScrollBottomListener;
        if (onScrollBottomListener != null)
          onScrollBottomListener.onScorllBootom(); 
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/shizhefei/mvc/viewhandler/ListViewHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */