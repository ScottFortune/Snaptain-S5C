package com.netopsun.ijkvideoview.extra.filter_choose_popupwindows;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.netopsun.ijkvideoview.R;
import com.netopsun.ijkvideoview.extra.filter_choose_popupwindows.filter.MyGPUImageSobelEdgeDetectionFilter;
import java.util.ArrayList;
import java.util.List;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageColorInvertFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageGammaFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageGrayscaleFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSepiaToneFilter;

public class FilterChoosePopupWindows extends PopupWindow {
  private final FilterListAdapter filterListAdapter;
  
  public FilterChoosePopupWindows(Context paramContext, List<FilterBean> paramList) {
    View view = LayoutInflater.from(paramContext).inflate(R.layout.popup_windows_filter_choose, null);
    this.filterListAdapter = new FilterListAdapter(paramContext, paramList);
    RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.filter_listview);
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(paramContext);
    linearLayoutManager.setOrientation(0);
    recyclerView.setLayoutManager((RecyclerView.LayoutManager)linearLayoutManager);
    recyclerView.setAdapter(this.filterListAdapter);
    setContentView(view);
    setWidth(-2);
    setHeight(-2);
  }
  
  public static FilterChoosePopupWindows getDefaultFilterPopupWindows(Context paramContext) {
    ArrayList<FilterBean> arrayList = new ArrayList();
    FilterBean filterBean = new FilterBean();
    filterBean.setImageFilter(new GPUImageFilter());
    filterBean.setName(paramContext.getString(R.string.original_filter));
    arrayList.add(filterBean);
    filterBean = new FilterBean();
    filterBean.setImageFilter(new GPUImageBeautyFilter());
    filterBean.setName(paramContext.getString(R.string.beauty_filter));
    arrayList.add(filterBean);
    filterBean = new FilterBean();
    filterBean.setImageFilter((GPUImageFilter)new GPUImageGammaFilter());
    filterBean.setName(paramContext.getString(R.string.gamma_filter));
    arrayList.add(filterBean);
    filterBean = new FilterBean();
    filterBean.setImageFilter((GPUImageFilter)new GPUImageColorInvertFilter());
    filterBean.setName(paramContext.getString(R.string.color_invert_filter));
    arrayList.add(filterBean);
    filterBean = new FilterBean();
    filterBean.setImageFilter((GPUImageFilter)new GPUImageSepiaToneFilter());
    filterBean.setName(paramContext.getString(R.string.sepia_tone_filter));
    arrayList.add(filterBean);
    filterBean = new FilterBean();
    filterBean.setImageFilter((GPUImageFilter)new GPUImageGrayscaleFilter());
    filterBean.setName(paramContext.getString(R.string.gray_scale_filter));
    arrayList.add(filterBean);
    filterBean = new FilterBean();
    filterBean.setImageFilter((GPUImageFilter)new MyGPUImageSobelEdgeDetectionFilter());
    filterBean.setName(paramContext.getString(R.string.sobel_edge_detection_filter));
    arrayList.add(filterBean);
    return new FilterChoosePopupWindows(paramContext, arrayList);
  }
  
  public void setIconFilterRes(int paramInt) {
    this.filterListAdapter.setIconFilterRes(paramInt);
  }
  
  public void setOnItemClickListener(FilterListAdapter.OnItemClickListener paramOnItemClickListener) {
    this.filterListAdapter.setItemClickListener(paramOnItemClickListener);
  }
  
  public void show(View paramView) {
    showAtLocation(paramView, 17, 0, 0);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/ijkvideoview/extra/filter_choose_popupwindows/FilterChoosePopupWindows.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */