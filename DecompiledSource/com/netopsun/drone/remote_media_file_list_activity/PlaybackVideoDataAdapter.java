package com.netopsun.drone.remote_media_file_list_activity;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.netopsun.deviceshub.base.bean.RemoteVideoFiles;
import com.shizhefei.mvc.IDataAdapter;
import java.util.ArrayList;
import java.util.List;

public class PlaybackVideoDataAdapter extends BaseAdapter implements IDataAdapter<List<RemoteVideoFiles.RemoteVideoFilesBean>> {
  private final Context context;
  
  final List<RemoteVideoFiles.RemoteVideoFilesBean> fileNameList = new ArrayList<RemoteVideoFiles.RemoteVideoFilesBean>();
  
  private OnItemButtonClickListener onDeleteClickListener;
  
  public PlaybackVideoDataAdapter(Context paramContext) {
    this.context = paramContext;
  }
  
  public int getCount() {
    return this.fileNameList.size();
  }
  
  public List<RemoteVideoFiles.RemoteVideoFilesBean> getData() {
    return this.fileNameList;
  }
  
  public Object getItem(int paramInt) {
    return null;
  }
  
  public long getItemId(int paramInt) {
    return paramInt;
  }
  
  public View getView(final int position, View paramView, ViewGroup paramViewGroup) {
    View view = paramView;
    if (paramView == null) {
      view = View.inflate(this.context, 2131427380, null);
      view.setTag(new ViewHolder(view));
    } 
    final ViewHolder viewHolder = (ViewHolder)view.getTag();
    viewHolder.mRemoteFileName.setText(((RemoteVideoFiles.RemoteVideoFilesBean)this.fileNameList.get(position)).getVideo_name());
    viewHolder.mDeleteBtn.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            if (PlaybackVideoDataAdapter.this.onDeleteClickListener != null)
              PlaybackVideoDataAdapter.this.onDeleteClickListener.onDeleteButtonClick(viewHolder.mDeleteBtn, position); 
          }
        });
    viewHolder.mDownloadBtn.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            if (PlaybackVideoDataAdapter.this.onDeleteClickListener != null)
              PlaybackVideoDataAdapter.this.onDeleteClickListener.onDownloadButtonClick(viewHolder.mDeleteBtn, position); 
          }
        });
    return view;
  }
  
  public void notifyDataChanged(List<RemoteVideoFiles.RemoteVideoFilesBean> paramList, boolean paramBoolean) {
    if (paramBoolean)
      this.fileNameList.clear(); 
    this.fileNameList.addAll(paramList);
    notifyDataSetChanged();
  }
  
  public void setOnDeleteClickListener(OnItemButtonClickListener paramOnItemButtonClickListener) {
    this.onDeleteClickListener = paramOnItemButtonClickListener;
  }
  
  static interface OnItemButtonClickListener {
    void onDeleteButtonClick(View param1View, int param1Int);
    
    void onDownloadButtonClick(View param1View, int param1Int);
  }
  
  static class ViewHolder {
    View mDeleteBtn;
    
    View mDownloadBtn;
    
    TextView mRemoteFileName;
    
    View view;
    
    ViewHolder(View param1View) {
      this.view = param1View;
      this.mRemoteFileName = (TextView)param1View.findViewById(2131230981);
      this.mDeleteBtn = param1View.findViewById(2131230879);
      this.mDownloadBtn = param1View.findViewById(2131230885);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/drone/remote_media_file_list_activity/PlaybackVideoDataAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */