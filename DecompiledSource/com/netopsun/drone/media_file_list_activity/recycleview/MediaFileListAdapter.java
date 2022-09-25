package com.netopsun.drone.media_file_list_activity.recycleview;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MediaFileListAdapter extends RecyclerView.Adapter<MediaFileListAdapter.ViewHolder> {
  Context context;
  
  boolean isChooseMode = false;
  
  OnItemClickListener itemClickListener;
  
  List<MediaFileBean> mediaFileBeansList;
  
  public MediaFileListAdapter(Context paramContext, List<MediaFileBean> paramList) {
    this.context = paramContext;
    this.mediaFileBeansList = paramList;
  }
  
  public void chooseAll() {
    Iterator<MediaFileBean> iterator = this.mediaFileBeansList.iterator();
    while (iterator.hasNext())
      ((MediaFileBean)iterator.next()).setChoose(true); 
    notifyDataSetChanged();
  }
  
  public void chooseNone() {
    Iterator<MediaFileBean> iterator = this.mediaFileBeansList.iterator();
    while (iterator.hasNext())
      ((MediaFileBean)iterator.next()).setChoose(false); 
    notifyDataSetChanged();
  }
  
  public void deleteChooseFile() {
    ArrayList<MediaFileBean> arrayList = new ArrayList();
    for (MediaFileBean mediaFileBean : this.mediaFileBeansList) {
      if (mediaFileBean.isChoose())
        arrayList.add(mediaFileBean); 
    } 
    this.mediaFileBeansList.removeAll(arrayList);
    notifyDataSetChanged();
    Iterator<MediaFileBean> iterator = arrayList.iterator();
    while (iterator.hasNext())
      (new File(((MediaFileBean)iterator.next()).getFilePath())).delete(); 
  }
  
  public int getItemCount() {
    return this.mediaFileBeansList.size();
  }
  
  public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
    if (!TextUtils.isEmpty(((MediaFileBean)this.mediaFileBeansList.get(i)).getGroupTitle())) {
      if (viewHolder.groupTitle.getVisibility() != 0) {
        viewHolder.itemContent.setVisibility(8);
        viewHolder.groupTitle.setVisibility(0);
      } 
      viewHolder.groupTitle.setText(((MediaFileBean)this.mediaFileBeansList.get(i)).getGroupTitle());
      return;
    } 
    if (viewHolder.groupTitle.getVisibility() == 0) {
      viewHolder.itemContent.setVisibility(0);
      viewHolder.groupTitle.setVisibility(8);
    } 
    viewHolder.mediaFileName.setText(((MediaFileBean)this.mediaFileBeansList.get(i)).getFileName());
    viewHolder.mediaFileSize.setText(((MediaFileBean)this.mediaFileBeansList.get(i)).getFileSize());
    viewHolder.mediaFileModified.setText(((MediaFileBean)this.mediaFileBeansList.get(i)).getFileModified());
    if (((MediaFileBean)this.mediaFileBeansList.get(i)).isPhoto()) {
      ((RequestBuilder)Glide.with(this.context).load(((MediaFileBean)this.mediaFileBeansList.get(i)).getFilePath()).placeholder(2131165420)).into(viewHolder.mediaTypeImg);
    } else {
      ((RequestBuilder)Glide.with(this.context).load(((MediaFileBean)this.mediaFileBeansList.get(i)).getFilePath()).placeholder(2131165421)).into(viewHolder.mediaTypeImg);
    } 
    if (this.isChooseMode) {
      viewHolder.chooseBox.setVisibility(0);
    } else {
      viewHolder.chooseBox.setVisibility(4);
    } 
    viewHolder.chooseBox.setChecked(((MediaFileBean)this.mediaFileBeansList.get(i)).isChoose());
    viewHolder.view.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            if (MediaFileListAdapter.this.isChooseMode) {
              ((MediaFileBean)MediaFileListAdapter.this.mediaFileBeansList.get(i)).setChoose(((MediaFileBean)MediaFileListAdapter.this.mediaFileBeansList.get(i)).isChoose() ^ true);
              MediaFileListAdapter.this.notifyDataSetChanged();
            } else if (MediaFileListAdapter.this.itemClickListener != null) {
              MediaFileListAdapter.this.itemClickListener.onItemClick(viewHolder.view, MediaFileListAdapter.this.mediaFileBeansList, i);
            } 
          }
        });
  }
  
  public ViewHolder onCreateViewHolder(ViewGroup paramViewGroup, int paramInt) {
    return new ViewHolder(LayoutInflater.from(this.context).inflate(2131427391, paramViewGroup, false));
  }
  
  public void setChooseMode(boolean paramBoolean) {
    this.isChooseMode = paramBoolean;
    Iterator<MediaFileBean> iterator = this.mediaFileBeansList.iterator();
    while (iterator.hasNext())
      ((MediaFileBean)iterator.next()).setChoose(false); 
    notifyDataSetChanged();
  }
  
  public void setItemClickListener(OnItemClickListener paramOnItemClickListener) {
    this.itemClickListener = paramOnItemClickListener;
  }
  
  public static interface OnItemClickListener {
    void onItemClick(View param1View, List<MediaFileBean> param1List, int param1Int);
  }
  
  static class ViewHolder extends RecyclerView.ViewHolder {
    CheckBox chooseBox;
    
    TextView groupTitle;
    
    View itemContent;
    
    TextView mediaFileModified;
    
    TextView mediaFileName;
    
    TextView mediaFileSize;
    
    ImageView mediaTypeImg;
    
    View view;
    
    ViewHolder(View param1View) {
      super(param1View);
      this.view = param1View;
      this.mediaTypeImg = (ImageView)param1View.findViewById(2131230933);
      this.mediaFileName = (TextView)param1View.findViewById(2131230931);
      this.mediaFileSize = (TextView)param1View.findViewById(2131230932);
      this.mediaFileModified = (TextView)param1View.findViewById(2131230930);
      this.chooseBox = (CheckBox)param1View.findViewById(2131230821);
      this.groupTitle = (TextView)param1View.findViewById(2131230903);
      this.itemContent = param1View.findViewById(2131230919);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/drone/media_file_list_activity/recycleview/MediaFileListAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */