package com.netopsun.ijkvideoview.extra.filter_choose_popupwindows;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.request.RequestOptions;
import com.netopsun.ijkvideoview.R;
import java.util.List;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;

public class FilterListAdapter extends RecyclerView.Adapter<FilterListAdapter.ViewHolder> {
  int checkPosition = 0;
  
  Context context;
  
  List<FilterBean> filterBeanList;
  
  private int iconFilterRes = R.drawable.filter_popupwindows_icon_filter;
  
  OnItemClickListener itemClickListener;
  
  private final RequestOptions options = new RequestOptions();
  
  public FilterListAdapter(Context paramContext, List<FilterBean> paramList) {
    this.context = paramContext;
    this.filterBeanList = paramList;
  }
  
  public int getIconFilterRes() {
    return this.iconFilterRes;
  }
  
  public int getItemCount() {
    return this.filterBeanList.size();
  }
  
  public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
    viewHolder.filterName.setText(((FilterBean)this.filterBeanList.get(i)).getName());
    if (this.checkPosition == i) {
      viewHolder.filterPreviewImg.setBackgroundResource(R.drawable.background_filter_preview_img);
    } else {
      viewHolder.filterPreviewImg.setBackgroundColor(0);
    } 
    Glide.with(this.context).load(Integer.valueOf(this.iconFilterRes)).apply(this.options.transform((Transformation)new GlideCircleWithBorder(this.context, ((FilterBean)this.filterBeanList.get(i)).imageFilter))).into(viewHolder.filterPreviewImg);
    viewHolder.view.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            FilterListAdapter filterListAdapter = FilterListAdapter.this;
            filterListAdapter.checkPosition = i;
            filterListAdapter.notifyDataSetChanged();
            if (FilterListAdapter.this.itemClickListener != null)
              FilterListAdapter.this.itemClickListener.onItemClick(viewHolder.view, ((FilterBean)FilterListAdapter.this.filterBeanList.get(i)).imageFilter, i); 
          }
        });
  }
  
  public ViewHolder onCreateViewHolder(ViewGroup paramViewGroup, int paramInt) {
    return new ViewHolder(LayoutInflater.from(this.context).inflate(R.layout.adapter_filter_recyclerview, null));
  }
  
  public void setIconFilterRes(int paramInt) {
    this.iconFilterRes = paramInt;
  }
  
  public FilterListAdapter setItemClickListener(OnItemClickListener paramOnItemClickListener) {
    this.itemClickListener = paramOnItemClickListener;
    return this;
  }
  
  public static interface OnItemClickListener {
    void onItemClick(View param1View, GPUImageFilter param1GPUImageFilter, int param1Int);
  }
  
  static class ViewHolder extends RecyclerView.ViewHolder {
    TextView filterName;
    
    ImageView filterPreviewImg;
    
    View view;
    
    ViewHolder(View param1View) {
      super(param1View);
      this.view = param1View;
      this.filterPreviewImg = (ImageView)param1View.findViewById(R.id.filter_preview_img);
      this.filterName = (TextView)param1View.findViewById(R.id.filter_name);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/ijkvideoview/extra/filter_choose_popupwindows/FilterListAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */